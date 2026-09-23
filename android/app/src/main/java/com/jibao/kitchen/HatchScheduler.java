package com.jibao.kitchen;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashSet;
import java.util.Set;

/** One native reminder for the current batch. No WebView timer or background service. */
public final class HatchScheduler {
    public static final String ACTION_HATCH_READY = "com.jibao.kitchen.HATCH_READY";
    static final String ACTION_TEST_READY = "com.jibao.kitchen.TEST_REMINDER_READY";
    public static final String CHANNEL_ID = "hatch_ready";
    static final String EXTRA_TOKEN = "batchToken";
    private static final String PREFS = "hatch_reminders_v1";
    private static final String TAG = "HatchScheduler";
    private static final int ALARM_REQUEST = 801;
    private static final int OPEN_REQUEST = 802;
    private static final int NOTIFICATION_ID = 803;
    private static final int TEST_NOTIFICATION_ID = 804;
    private static final int TEST_ALARM_REQUEST = 805;
    private static final int DELAYED_TEST_NOTIFICATION_ID = 806;

    private HatchScheduler() {}

    public static synchronized void syncFromSave(Context context, JSONObject state) {
        sync(context.getApplicationContext(), state, false);
    }

    public static synchronized void restore(Context context) {
        Context app = context.getApplicationContext();
        sync(app, SaveRepository.readState(app), true);
        try { restoreTest(app); }
        catch (RuntimeException failure) { clearTest(app);Log.w(TAG, "Could not restore delayed test", failure); }
    }

    /** Cancel the actual reminder, preserving delivery history across toggles and updates. */
    public static synchronized void cancel(Context context) {
        Context app = context.getApplicationContext();
        clearAlarm(app);
        preferences(app).edit().remove("activeToken").remove("error").commit();
        NotificationManager manager = app.getSystemService(NotificationManager.class);
        if (manager != null) manager.cancel(NOTIFICATION_ID);
    }

    public static synchronized JSONObject status(Context context) {
        Context app = context.getApplicationContext();
        ensureChannel(app);
        JSONObject state = SaveRepository.readState(app);
        Batch batch = Batch.from(state);
        SharedPreferences prefs = preferences(app);
        boolean enabled = state != null && state.optBoolean("alarm", false);
        boolean permission = notificationPermission(app);
        boolean channel = channelEnabled(app);
        boolean allowed = notificationsEnabled(app);
        boolean exact = exactAllowed(app);
        boolean delivered = batch != null && wasDelivered(prefs, batch.token);
        boolean scheduled = enabled && batch != null && allowed && !delivered
                && batch.token.equals(prefs.getString("scheduledToken", ""))
                && prefs.getLong("scheduledAt", 0) == batch.readyAt;
        String mode = scheduled ? prefs.getString("scheduleMode", "none") : "none";
        String message;
        if (!enabled) message = "孵化提醒已关闭。";
        else if (!permission) message = "请允许通知权限，鸡宝孵化完成后才能提醒你。";
        else if (!allowed) message = "系统已关闭游戏通知，请在通知设置中开启孵化提醒。";
        else if (batch == null) message = "孵化提醒已开启，开始孵化后会自动安排。";
        else if (delivered) message = "这一批已经提醒过了，快回厨房收取吧。";
        else if (!prefs.getString("error", "").isEmpty()) message = "提醒暂未安排成功，重新打开游戏后会再试。";
        else if (!scheduled) message = "这批孵化提醒尚未安排，请重新打开游戏后查看。";
        else if (!exact || "inexact".equals(mode)) message = "已安排孵化提醒；受系统省电策略影响，提醒可能延迟。";
        else message = "已安排孵化完成提醒。";
        if (scheduled) message += "预计 " + timeText(batch.readyAt) + " 提醒（按本批实际孵化时间）。";
        JSONObject result = new JSONObject();
        put(result, "supported", true);
        put(result, "enabled", enabled);
        put(result, "permissionGranted", permission);
        put(result, "notificationsEnabled", allowed);
        put(result, "channelEnabled", channel);
        put(result, "exactAllowed", exact);
        put(result, "scheduled", scheduled);
        put(result, "scheduledAt", scheduled ? batch.readyAt : 0);
        put(result, "readyAt", batch == null ? 0 : batch.readyAt);
        put(result, "scheduleMode", mode);
        put(result, "delivered", delivered);
        put(result, "lastDeliveredAt", prefs.getLong("lastDeliveredAt", 0));
        long testAt = prefs.getLong("testAt", 0), testPostedAt = prefs.getLong("testPostedAt", 0);
        put(result, "testScheduledAt", testAt);
        put(result, "testPostedAt", testPostedAt);
        put(result, "testMessage", testAt > 0 ? "后台测试已安排：" + timeText(testAt) + "，可以返回桌面等待。"
                : testPostedAt > 0 ? "上次后台测试于 " + timeText(testPostedAt) + " 已发送到系统通知栏。" : "");
        put(result, "message", message);
        return result;
    }

    /** Immediate permission/sound check. Does not change the real alarm or batch token. */
    public static synchronized void testNotification(Context context) {
        Context app = context.getApplicationContext();
        ensureChannel(app);
        if (!notificationsEnabled(app)) throw new IllegalStateException("请先在系统设置中允许鸡宝厨房发送通知。");
        NotificationManager manager = app.getSystemService(NotificationManager.class);
        if (manager == null) throw new IllegalStateException("系统通知服务暂不可用。");
        manager.notify(TEST_NOTIFICATION_ID, notification(app, "鸡宝厨房 · 提醒测试", "通知已经准备好！鸡宝孵化完成后，会在这里提醒你。"));
    }

    static synchronized void onAlarm(Context context, Intent intent) {
        if (intent != null && ACTION_TEST_READY.equals(intent.getAction())) {
            Context app = context.getApplicationContext();
            if (intent.getStringExtra(EXTRA_TOKEN) != null && intent.getStringExtra(EXTRA_TOKEN).equals(preferences(app).getString("testToken", ""))) {
                try { restoreTest(app); }
                catch (RuntimeException failure) { clearTest(app);Log.w(TAG, "Could not restore delayed test", failure); }
            }
            return;
        }
        if (intent == null || !ACTION_HATCH_READY.equals(intent.getAction())) return;
        Context app = context.getApplicationContext();
        JSONObject state = SaveRepository.readState(app);
        Batch batch = Batch.from(state);
        // A queued delivery from an old batch must never notify for the new batch.
        if (batch == null || !batch.token.equals(intent.getStringExtra(EXTRA_TOKEN))) {
            sync(app, state, true);
            return;
        }
        sync(app, state, true);
    }

    private static void sync(Context app, JSONObject state, boolean force) {
        ensureChannel(app);
        Batch batch = Batch.from(state);
        if (state == null || !state.optBoolean("alarm", false) || batch == null) {
            cancel(app);
            return;
        }
        SharedPreferences prefs = preferences(app);
        if (!batch.token.equals(prefs.getString("activeToken", ""))) {
            cancel(app);
            prefs.edit().putString("activeToken", batch.token).commit();
        }
        if (!notificationsEnabled(app)) {
            clearAlarm(app);
            return;
        }
        if (wasDelivered(prefs, batch.token)) {
            clearAlarm(app);
            return;
        }
        if (batch.readyAt <= System.currentTimeMillis()) {
            clearAlarm(app);
            deliver(app, batch);
            return;
        }
        boolean exact = exactAllowed(app);
        String mode = exact ? "exact" : "inexact";
        if (!force && batch.token.equals(prefs.getString("scheduledToken", ""))
                && batch.readyAt == prefs.getLong("scheduledAt", 0)
                && mode.equals(prefs.getString("scheduleMode", "none"))
                && alarmIntent(app, null, PendingIntent.FLAG_NO_CREATE) != null) return;
        AlarmManager manager = app.getSystemService(AlarmManager.class);
        if (manager == null) {
            clearAlarm(app);
            prefs.edit().putString("error", "alarm_service_unavailable").commit();
            return;
        }
        PendingIntent pending = alarmIntent(app, batch.token, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            exact = arm(manager, exact, batch.readyAt, pending);
            prefs.edit().putString("scheduledToken", batch.token)
                    .putLong("scheduledAt", batch.readyAt)
                    .putString("scheduleMode", exact ? "exact" : "inexact")
                    .remove("error").commit();
        } catch (RuntimeException failure) {
            clearAlarm(app);
            prefs.edit().putString("error", "schedule_failed").commit();
            Log.w(TAG, "Could not schedule hatch reminder", failure);
        }
    }

    private static void deliver(Context app, Batch batch) {
        if (!notificationsEnabled(app)) return;
        SharedPreferences prefs = preferences(app);
        if (wasDelivered(prefs, batch.token)) return;
        NotificationManager manager = app.getSystemService(NotificationManager.class);
        if (manager == null) return;
        Set<String> previous = deliveredTokens(prefs);
        Set<String> next = new LinkedHashSet<>(previous);
        next.add(batch.token);
        // This set is independent of save files, so restoring a recent backup does not repeat a reminder.
        while (next.size() > 256) next.remove(next.iterator().next());
        // Reserve before notifying so a receiver replay or process restart cannot emit it twice.
        if (!prefs.edit().putString("deliveredTokens", String.join("\n", next)).remove("error").commit()) return;
        try {
            String name = batch.egg == 1 ? "鸭宝" : "鸡宝";
            manager.notify(NOTIFICATION_ID, notification(app, name + "孵化完成啦！", "这一批" + name + "已经孵化完成，回厨房收取你的新伙伴吧。"));
            prefs.edit().putLong("lastDeliveredAt", System.currentTimeMillis()).commit();
        } catch (RuntimeException failure) {
            prefs.edit().putString("deliveredTokens", String.join("\n", previous)).putString("error", "notification_failed").commit();
            Log.w(TAG, "Could not post hatch reminder", failure);
        }
    }

    private static Notification notification(Context app, String title, String text) {
        Intent open = new Intent(app, MainActivity.class)
                .setAction("com.jibao.kitchen.OPEN_KITCHEN")
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("goKitchen", true);
        PendingIntent content = PendingIntent.getActivity(app, OPEN_REQUEST, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        return new Notification.Builder(app, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title).setContentText(text)
                .setStyle(new Notification.BigTextStyle().bigText(text))
                .setContentIntent(content).setAutoCancel(true)
                .setCategory(Notification.CATEGORY_REMINDER)
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setOnlyAlertOnce(true).build();
    }

    private static String timeText(long value) {
        return new java.text.SimpleDateFormat("MM月dd日 HH:mm:ss", java.util.Locale.CHINA).format(new java.util.Date(value));
    }

    // Both real reminders and the delayed test use this exact platform path.
    private static boolean arm(AlarmManager manager, boolean exact, long at, PendingIntent pending) {
        if (exact) {
            try { manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pending); return true; }
            catch (SecurityException denied) { /* Permission can change after checking. */ }
        }
        manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pending);
        return false;
    }

    /** A separate one-minute background probe; never reads or writes the game save. */
    public static synchronized void testDelayedNotification(Context context) {
        Context app = context.getApplicationContext();ensureChannel(app);
        if (!notificationsEnabled(app)) throw new IllegalStateException("请先在系统设置中允许鸡宝厨房发送通知。");
        clearTest(app);
        long at = System.currentTimeMillis() + 60000;
        String token = java.util.UUID.randomUUID().toString();
        if (!preferences(app).edit().putLong("testAt", at).putString("testToken", token).remove("testPostedAt").commit()) throw new IllegalStateException("暂时无法保存测试提醒，请稍后再试。");
        try { restoreTest(app); }
        catch (RuntimeException failure) { clearTest(app);throw new IllegalStateException("后台测试未能安排，请重新打开游戏再试。", failure); }
    }

    private static PendingIntent testIntent(Context app, String token, int flags) {
        Intent intent = new Intent(app, HatchAlarmReceiver.class).setAction(ACTION_TEST_READY);
        if (token != null) intent.putExtra(EXTRA_TOKEN, token);
        return PendingIntent.getBroadcast(app, TEST_ALARM_REQUEST, intent, flags | PendingIntent.FLAG_IMMUTABLE);
    }

    private static void clearTest(Context app) {
        PendingIntent pending = testIntent(app, null, PendingIntent.FLAG_NO_CREATE);
        AlarmManager manager = app.getSystemService(AlarmManager.class);
        if (pending != null) { if (manager != null) manager.cancel(pending); pending.cancel(); }
        preferences(app).edit().remove("testAt").remove("testToken").commit();
    }

    private static void restoreTest(Context app) {
        SharedPreferences prefs = preferences(app);
        long at = prefs.getLong("testAt", 0);if (at <= 0) return;
        if (!notificationsEnabled(app)) { clearTest(app);return; }
        AlarmManager manager = app.getSystemService(AlarmManager.class);
        if (manager == null) throw new IllegalStateException("系统提醒服务暂不可用。");
        if (at > System.currentTimeMillis()) { arm(manager, exactAllowed(app), at, testIntent(app, prefs.getString("testToken", ""), PendingIntent.FLAG_UPDATE_CURRENT));return; }
        NotificationManager notifications = app.getSystemService(NotificationManager.class);
        if (!prefs.edit().remove("testAt").remove("testToken").commit()) return;
        try {
            notifications.cancel(DELAYED_TEST_NOTIFICATION_ID);
            notifications.notify(DELAYED_TEST_NOTIFICATION_ID, notification(app, "鸡宝厨房 · 后台提醒测试", "等待结束，系统已送达这条测试通知。你的孵化进度和正式提醒保持不变。"));
            prefs.edit().putLong("testPostedAt", System.currentTimeMillis()).commit();
        } catch (RuntimeException failure) { Log.w(TAG, "Could not post delayed notification test", failure); }
        clearTest(app);
    }

    private static PendingIntent alarmIntent(Context app, String token, int flags) {
        Intent intent = new Intent(app, HatchAlarmReceiver.class).setAction(ACTION_HATCH_READY);
        if (token != null) intent.putExtra(EXTRA_TOKEN, token);
        return PendingIntent.getBroadcast(app, ALARM_REQUEST, intent, flags | PendingIntent.FLAG_IMMUTABLE);
    }

    private static void clearAlarm(Context app) {
        PendingIntent existing = alarmIntent(app, null, PendingIntent.FLAG_NO_CREATE);
        if (existing != null) {
            AlarmManager manager = app.getSystemService(AlarmManager.class);
            if (manager != null) manager.cancel(existing);
            existing.cancel();
        }
        SharedPreferences prefs = preferences(app);
        if (prefs.contains("scheduledToken") || prefs.contains("scheduledAt") || prefs.contains("scheduleMode")) {
            prefs.edit().remove("scheduledToken").remove("scheduledAt").remove("scheduleMode").commit();
        }
    }

    private static boolean wasDelivered(SharedPreferences prefs, String token) {
        return deliveredTokens(prefs).contains(token);
    }

    private static Set<String> deliveredTokens(SharedPreferences prefs) {
        Set<String> tokens = new LinkedHashSet<>();
        for (String token : prefs.getString("deliveredTokens", "").split("\n")) {
            if (!token.isEmpty()) tokens.add(token);
        }
        return tokens;
    }

    private static SharedPreferences preferences(Context app) {
        return app.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private static boolean notificationPermission(Context app) {
        return Build.VERSION.SDK_INT < 33 || app.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean notificationsEnabled(Context app) {
        NotificationManager manager = app.getSystemService(NotificationManager.class);
        return notificationPermission(app) && manager != null && manager.areNotificationsEnabled() && channelEnabled(app);
    }

    private static boolean channelEnabled(Context app) {
        NotificationManager manager = app.getSystemService(NotificationManager.class);
        if (manager == null) return false;
        NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
        return channel != null && channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
    }

    private static boolean exactAllowed(Context app) {
        AlarmManager manager = app.getSystemService(AlarmManager.class);
        return manager != null && (Build.VERSION.SDK_INT < 31 || manager.canScheduleExactAlarms());
    }

    private static void ensureChannel(Context app) {
        NotificationManager manager = app.getSystemService(NotificationManager.class);
        if (manager == null) return;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "孵化完成提醒", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("鸡宝或鸭宝整批孵化完成后提醒一次，点击回到厨房收取。");
        manager.createNotificationChannel(channel);
    }

    private static void put(JSONObject object, String key, Object value) {
        try { object.put(key, value); } catch (JSONException impossible) { throw new IllegalStateException(impossible); }
    }

    static final class Batch {
        final String token;
        final long readyAt;
        final int egg;

        Batch(String token, long readyAt, int egg) { this.token = token; this.readyAt = readyAt; this.egg = egg; }

        static Batch from(JSONObject state) {
            if (state == null) return null;
            JSONObject batch = state.optJSONObject("batch");
            if (batch == null) return null;
            JSONArray eggs = batch.optJSONArray("eggs");
            long ends = batch.optLong("ends", 0);
            if (eggs == null || eggs.length() == 0 || ends <= 0 || ends > Long.MAX_VALUE - 3000) return null;
            boolean remaining = false;
            long ready = 0;
            for (int i = 0; i < eggs.length(); i++) {
                JSONObject item = eggs.optJSONObject(i);
                if (item == null || item.optBoolean("collected", false)) continue;
                remaining = true;
                long opens = item.optLong("openAt", ends);
                if (opens <= 0) opens = ends;
                if (opens > Long.MAX_VALUE - 3000) return null;
                ready = Math.max(ready, opens);
            }
            if (!remaining) return null;
            String identity = batch.optLong("started", 0) + ":" + ends + ":" + batch.optInt("tool", -1)
                    + ":" + batch.optInt("level", 0) + ":" + batch.optInt("egg", 0);
            return new Batch(hash(identity), ready + 3000, batch.optInt("egg", 0));
        }

        private static String hash(String text) {
            try {
                byte[] bytes = MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();
                for (byte value : bytes) result.append(String.format(java.util.Locale.ROOT, "%02x", value & 0xff));
                return result.toString();
            } catch (NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
        }
    }
}

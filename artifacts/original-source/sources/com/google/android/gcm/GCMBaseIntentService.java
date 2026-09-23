package com.google.android.gcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class GCMBaseIntentService extends IntentService {
    private static final String EXTRA_TOKEN = "token";
    public static final String TAG = "GCMBaseIntentService";
    private static final String WAKELOCK_KEY = "GCM_LIB";
    private static PowerManager.WakeLock sWakeLock;
    private final String[] mSenderIds;
    private static final Object LOCK = GCMBaseIntentService.class;
    private static int sCounter = 0;
    private static final Random sRandom = new Random();
    private static final int MAX_BACKOFF_MS = (int) TimeUnit.SECONDS.toMillis(3600);
    private static final String TOKEN = Long.toBinaryString(sRandom.nextLong());

    protected abstract void onError(Context context, String str);

    protected abstract void onMessage(Context context, Intent intent);

    protected abstract void onRegistered(Context context, String str);

    protected abstract void onUnregistered(Context context, String str);

    protected GCMBaseIntentService() {
        this(getName("DynamicSenderIds"), null);
    }

    protected GCMBaseIntentService(String... senderIds) {
        this(getName(senderIds), senderIds);
    }

    private GCMBaseIntentService(String name, String[] senderIds) {
        super(name);
        this.mSenderIds = senderIds;
    }

    private static String getName(String senderId) {
        StringBuilder sbAppend = new StringBuilder().append("GCMIntentService-").append(senderId).append("-");
        int i = sCounter + 1;
        sCounter = i;
        String name = sbAppend.append(i).toString();
        Log.v(TAG, "Intent service name: " + name);
        return name;
    }

    private static String getName(String[] senderIds) {
        String flatSenderIds = GCMRegistrar.getFlatSenderIds(senderIds);
        return getName(flatSenderIds);
    }

    protected String[] getSenderIds(Context context) {
        if (this.mSenderIds == null) {
            throw new IllegalStateException("sender id not set on constructor");
        }
        return this.mSenderIds;
    }

    protected void onDeletedMessages(Context context, int total) {
    }

    protected boolean onRecoverableError(Context context, String errorId) {
        return true;
    }

    @Override // android.app.IntentService
    public final void onHandleIntent(Intent intent) {
        try {
            Context context = getApplicationContext();
            String action = intent.getAction();
            if (action.equals(GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK)) {
                GCMRegistrar.setRetryBroadcastReceiver(context);
                handleRegistration(context, intent);
            } else if (action.equals(GCMConstants.INTENT_FROM_GCM_MESSAGE)) {
                String messageType = intent.getStringExtra(GCMConstants.EXTRA_SPECIAL_MESSAGE);
                if (messageType == null) {
                    onMessage(context, intent);
                } else if (messageType.equals("deleted_messages")) {
                    String sTotal = intent.getStringExtra(GCMConstants.EXTRA_TOTAL_DELETED);
                    if (sTotal != null) {
                        try {
                            int total = Integer.parseInt(sTotal);
                            Log.v(TAG, "Received deleted messages notification: " + total);
                            onDeletedMessages(context, total);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "GCM returned invalid number of deleted messages: " + sTotal);
                        }
                    }
                } else {
                    Log.e(TAG, "Received unknown special message: " + messageType);
                }
            } else if (action.equals(GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY)) {
                String token = intent.getStringExtra(EXTRA_TOKEN);
                if (!TOKEN.equals(token)) {
                    Log.e(TAG, "Received invalid token: " + token);
                    synchronized (LOCK) {
                        if (sWakeLock != null) {
                            Log.v(TAG, "Releasing wakelock");
                            sWakeLock.release();
                        } else {
                            Log.e(TAG, "Wakelock reference is null");
                        }
                    }
                    return;
                }
                if (GCMRegistrar.isRegistered(context)) {
                    GCMRegistrar.internalUnregister(context);
                } else {
                    String[] senderIds = getSenderIds(context);
                    GCMRegistrar.internalRegister(context, senderIds);
                }
            }
            synchronized (LOCK) {
                if (sWakeLock != null) {
                    Log.v(TAG, "Releasing wakelock");
                    sWakeLock.release();
                } else {
                    Log.e(TAG, "Wakelock reference is null");
                }
            }
        } catch (Throwable th) {
            synchronized (LOCK) {
                if (sWakeLock != null) {
                    Log.v(TAG, "Releasing wakelock");
                    sWakeLock.release();
                } else {
                    Log.e(TAG, "Wakelock reference is null");
                }
                throw th;
            }
        }
    }

    static void runIntentInService(Context context, Intent intent, String className) {
        synchronized (LOCK) {
            if (sWakeLock == null) {
                PowerManager pm = (PowerManager) context.getSystemService("power");
                sWakeLock = pm.newWakeLock(1, WAKELOCK_KEY);
            }
        }
        Log.v(TAG, "Acquiring wakelock");
        sWakeLock.acquire();
        intent.setClassName(context, className);
        context.startService(intent);
    }

    private void handleRegistration(Context context, Intent intent) throws PackageManager.NameNotFoundException {
        String registrationId = intent.getStringExtra(GCMConstants.EXTRA_REGISTRATION_ID);
        String error = intent.getStringExtra("error");
        String unregistered = intent.getStringExtra(GCMConstants.EXTRA_UNREGISTERED);
        Log.d(TAG, "handleRegistration: registrationId = " + registrationId + ", error = " + error + ", unregistered = " + unregistered);
        if (registrationId != null) {
            GCMRegistrar.resetBackoff(context);
            GCMRegistrar.setRegistrationId(context, registrationId);
            onRegistered(context, registrationId);
            return;
        }
        if (unregistered != null) {
            GCMRegistrar.resetBackoff(context);
            String oldRegistrationId = GCMRegistrar.clearRegistrationId(context);
            onUnregistered(context, oldRegistrationId);
            return;
        }
        Log.d(TAG, "Registration error: " + error);
        if ("SERVICE_NOT_AVAILABLE".equals(error)) {
            boolean retry = onRecoverableError(context, error);
            if (retry) {
                int backoffTimeMs = GCMRegistrar.getBackoff(context);
                int nextAttempt = (backoffTimeMs / 2) + sRandom.nextInt(backoffTimeMs);
                Log.d(TAG, "Scheduling registration retry, backoff = " + nextAttempt + " (" + backoffTimeMs + ")");
                Intent retryIntent = new Intent(GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY);
                retryIntent.putExtra(EXTRA_TOKEN, TOKEN);
                PendingIntent retryPendingIntent = PendingIntent.getBroadcast(context, 0, retryIntent, 0);
                AlarmManager am = (AlarmManager) context.getSystemService("alarm");
                am.set(3, SystemClock.elapsedRealtime() + nextAttempt, retryPendingIntent);
                if (backoffTimeMs < MAX_BACKOFF_MS) {
                    GCMRegistrar.setBackoff(context, backoffTimeMs * 2);
                    return;
                }
                return;
            }
            Log.d(TAG, "Not retrying failed operation");
            return;
        }
        onError(context, error);
    }
}

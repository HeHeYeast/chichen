package com.jibao.kitchen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.*;
import android.widget.FrameLayout;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/** Only packaged, trusted HTTPS-origin content can reach the native bridge. */
public final class MainActivity extends Activity {
    static volatile boolean hasWebGame;
    private static final String HOST = "appassets.androidplatform.net";
    private static final String ENTRY = "https://" + HOST + "/web/index.html";
    private static final int IMPORT = 40, EXPORT = 41, NOTIFY = 42;
    private WebView web;
    private String pendingExport, pendingDocumentId, pendingPermissionId;
    private boolean ready, bridgeReady, goKitchen, systemScreenPending, notificationResumePending;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasWebGame = true;
        FrameLayout root = new FrameLayout(this); root.setBackgroundColor(Color.rgb(255, 242, 200));
        web = new WebView(this); web.setBackgroundColor(Color.rgb(255, 242, 200));
        root.addView(web, new FrameLayout.LayoutParams(-1, -1)); setContentView(root);
        getWindow().setStatusBarColor(Color.rgb(255, 242, 200));
        getWindow().setNavigationBarColor(Color.rgb(255, 242, 200));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        root.setOnApplyWindowInsetsListener((view, insets) -> {
            if (Build.VERSION.SDK_INT >= 30) {
                android.graphics.Insets bars = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            } else view.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            // The parent applies these bars. Forward explicit zeroes so WebView
            // clears any insets received during its first layout pass.
            if (Build.VERSION.SDK_INT >= 30) return new WindowInsets.Builder(insets)
                    .setInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout(), android.graphics.Insets.NONE).build();
            return insets.replaceSystemWindowInsets(0, 0, 0, 0);
        });
        root.requestApplyInsets();
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true); settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false); settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false); settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setSupportMultipleWindows(false); settings.setJavaScriptCanOpenWindowsAutomatically(false);
        web.addJavascriptInterface(new Bridge(), "ChickNative");
        web.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                ready = false; bridgeReady = false;
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !ENTRY.equals(request.getUrl().toString());
            }
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl(); String path = uri.getPath();
                if (!"https".equals(uri.getScheme()) || !HOST.equals(uri.getHost()) ||
                    path == null || path.contains("..") || !(path.startsWith("/web/") || path.startsWith("/assets/") || path.startsWith("/res/"))) return missing();
                try { return new WebResourceResponse(mime(path), "UTF-8", 200, "OK",
                        Collections.singletonMap("Cache-Control", "no-cache"), getAssets().open(path.substring(1))); }
                catch (IOException error) { return missing(); }
            }
            @Override public void onPageFinished(WebView view, String url) {
                // The game loads sprites and fonts asynchronously. JS explicitly
                // acknowledges readiness before we consume notification navigation.
                ready = ENTRY.equals(url);
            }
        });
        web.setWebChromeClient(new WebChromeClient());
        goKitchen = getIntent().getBooleanExtra("goKitchen", false);
        notificationResumePending = goKitchen;
        web.loadUrl(ENTRY);
        if (Build.VERSION.SDK_INT >= 33) getOnBackInvokedDispatcher().registerOnBackInvokedCallback(0, () -> emit("back", null, null));
    }
    private static WebResourceResponse missing() {
        return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found", Collections.emptyMap(), new ByteArrayInputStream(new byte[0]));
    }
    private static String mime(String path) {
        if (path.endsWith(".js")) return "text/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".woff2")) return "font/woff2";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".mp3")) return "audio/mpeg";
        return "application/octet-stream";
    }
    private void emit(String type, String id, JSONObject result) {
        runOnUiThread(() -> {
            if (web == null) return;
            try {
                JSONObject event = new JSONObject().put("type", type).put("id", id).put("result", result);
                web.evaluateJavascript("window.dispatchEvent(new CustomEvent('chick:native',{detail:" + event + "}));", null);
            } catch (Exception ignored) { }
        });
    }
    private JSONObject result(boolean ok, String message) {
        JSONObject data = new JSONObject(); try { data.put("ok", ok).put("message", message); } catch (Exception ignored) { } return data;
    }
    private void documentError(String message) {
        emit("response", pendingDocumentId, result(false, message)); pendingDocumentId = null; pendingExport = null;
    }
    @Override protected void onResume() {
        super.onResume(); HatchScheduler.restore(this);
        JSONObject navigation = result(true, "");
        // A notification may be delivered before onResume or before the web
        // bridge is ready. Preserve its kitchen destination in either order.
        try { navigation.put("keepPage", systemScreenPending || notificationResumePending); } catch (Exception ignored) { }
        systemScreenPending = false; notificationResumePending = false;
        if (web != null) { web.onResume(); emit("resume", null, navigation); }
    }
    @Override protected void onPause() { emit("pause", null, null); if (web != null) web.onPause(); super.onPause(); }
    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent); setIntent(intent);
        if (intent.getBooleanExtra("goKitchen", false)) {
            systemScreenPending = false;
            goKitchen = true; notificationResumePending = true;
            if (bridgeReady) { emit("kitchen", null, null); goKitchen = false; intent.removeExtra("goKitchen"); }
        } else if (Intent.ACTION_MAIN.equals(intent.getAction()) && intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {
            systemScreenPending = false; goKitchen = false; notificationResumePending = false;
            // Bringing an existing activity forward must preserve the live
            // game screen. A new WebView starts at the cover on its own.
        }
    }
    @Override public void onBackPressed() { if (web != null && ready) emit("back", null, null); else super.onBackPressed(); }
    @Override protected void onDestroy() { if (web != null) { web.removeJavascriptInterface("ChickNative"); web.destroy(); web = null; } hasWebGame = false; super.onDestroy(); }
    @Override public void onRequestPermissionsResult(int request, String[] permissions, int[] grants) {
        super.onRequestPermissionsResult(request, permissions, grants);
        if (request == NOTIFY) { HatchScheduler.restore(this); emit("response", pendingPermissionId, HatchScheduler.status(this)); pendingPermissionId = null; }
    }
    @Override protected void onActivityResult(int request, int code, Intent data) {
        super.onActivityResult(request, code, data);
        if (request != IMPORT && request != EXPORT) return;
        if (pendingDocumentId == null) return;
        if (code != RESULT_OK || data == null || data.getData() == null) { documentError("已取消"); return; }
        try {
            if (request == EXPORT) {
                if (pendingExport == null) throw new IOException("导出已中断，请重试");
                try (OutputStream out = getContentResolver().openOutputStream(data.getData(), "wt")) {
                    if (out == null) throw new IOException("无法写入此位置");
                    out.write(pendingExport.getBytes(StandardCharsets.UTF_8)); out.flush();
                }
                emit("response", pendingDocumentId, result(true, "备份已导出。请妥善保管此文件。"));
            } else {
                try (InputStream in = getContentResolver().openInputStream(data.getData())) {
                    if (in == null) throw new IOException("无法读取此文件");
                    emit("response", pendingDocumentId, result(true, "").put("raw", SaveRepository.readBounded(in)));
                }
            }
        } catch (Exception error) { documentError("文件操作失败：" + error.getMessage()); return; }
        pendingDocumentId = null; pendingExport = null;
    }
    private void openSystemSettings(Intent intent) {
        runOnUiThread(() -> {
            systemScreenPending = true;
            try { startActivity(intent); }
            catch (Exception error) { systemScreenPending = false; Toast.makeText(this, "无法打开系统设置，请在手机设置中调整。", Toast.LENGTH_LONG).show(); }
        });
    }
    public final class Bridge {
        @JavascriptInterface public void ready() {
            runOnUiThread(() -> {
                bridgeReady = true;
                if (goKitchen) { emit("kitchen", null, null); goKitchen = false; getIntent().removeExtra("goKitchen"); }
            });
        }
        @JavascriptInterface public String platformInfo() {
            try { return new JSONObject().put("android", true).put("version", BuildConfig.VERSION_NAME).put("versionCode", BuildConfig.VERSION_CODE).toString(); }
            catch (Exception ignored) { return "{}"; }
        }
        @JavascriptInterface public String loadSave() { return SaveRepository.load(MainActivity.this).toString(); }
        @JavascriptInterface public String saveGame(String raw) { return write(raw, false); }
        @JavascriptInterface public String commitGame(String raw, long expectedRevision) {
            return SaveBridge.write(MainActivity.this,raw,false,expectedRevision,
                    state -> HatchScheduler.syncFromSave(MainActivity.this,state)).toString();
        }
        @JavascriptInterface public String importGame(String raw) { return write(raw, true); }
        private String write(String raw, boolean importing) {
            return SaveBridge.write(MainActivity.this,raw,importing,null,
                    state -> HatchScheduler.syncFromSave(MainActivity.this,state)).toString();
        }
        @JavascriptInterface public String notificationStatus() { return HatchScheduler.status(MainActivity.this).toString(); }
        @JavascriptInterface public void requestNotifications(String id) {
            runOnUiThread(() -> {
                if (pendingPermissionId != null) { emit("response", id, result(false, "正在请求通知权限")); return; }
                if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    pendingPermissionId = id; systemScreenPending = true;
                    requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFY);
                } else emit("response", id, HatchScheduler.status(MainActivity.this));
            });
        }
        @JavascriptInterface public void openNotificationSettings() { openSystemSettings(new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName())); }
        @JavascriptInterface public void openExactAlarmSettings() { if (Build.VERSION.SDK_INT >= 31) openSystemSettings(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:" + getPackageName()))); }
        @JavascriptInterface public void openAppSettings() { openSystemSettings(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()))); }
        @JavascriptInterface public String testDelayedNotification() {
            try { HatchScheduler.testDelayedNotification(MainActivity.this); return result(true, "已安排 1 分钟后的后台测试。请返回桌面或锁屏等待，正式孵化提醒继续保留。若锁屏后收不到，请查看设置中的“后台提醒帮助”。").toString(); }
            catch (Exception error) { return result(false, error.getMessage()).toString(); }
        }
        @JavascriptInterface public String testNotification() {
            try { HatchScheduler.testNotification(MainActivity.this); return result(true, "测试提醒已发送，请查看通知栏。").toString(); }
            catch (Exception error) { return result(false, error.getMessage()).toString(); }
        }
        @JavascriptInterface public void exportSave(String id, String raw) {
            runOnUiThread(() -> {
                if (pendingDocumentId != null) { emit("response", id, result(false, "已有文件操作进行中")); return; }
                pendingDocumentId = id;
                if (raw == null || raw.getBytes(StandardCharsets.UTF_8).length > SaveRepository.MAX_BYTES) { documentError("备份文件过大"); return; }
                pendingExport = raw;
                systemScreenPending = true;
                try { startActivityForResult(new Intent(Intent.ACTION_CREATE_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("application/json").putExtra(Intent.EXTRA_TITLE, "鸡宝厨房备份-" + new java.text.SimpleDateFormat("yyyyMMdd-HHmmss", java.util.Locale.ROOT).format(new java.util.Date()) + ".json"), EXPORT); }
                catch (Exception error) { systemScreenPending = false; documentError("无法打开文件选择器"); }
            });
        }
        @JavascriptInterface public void importSave(String id) {
            runOnUiThread(() -> {
                if (pendingDocumentId != null) { emit("response", id, result(false, "已有文件操作进行中")); return; }
                pendingDocumentId = id;
                systemScreenPending = true;
                try { startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("*/*"), IMPORT); }
                catch (Exception error) { systemScreenPending = false; documentError("无法打开文件选择器"); }
            });
        }
        @JavascriptInterface public void closeApp() { runOnUiThread(() -> finish()); }
    }
}

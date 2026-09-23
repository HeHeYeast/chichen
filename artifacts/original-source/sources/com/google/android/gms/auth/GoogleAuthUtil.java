package com.google.android.gms.auth;

import android.R;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.a;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.o;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class GoogleAuthUtil {
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    public static final String KEY_ANDROID_PACKAGE_NAME;
    public static final String KEY_CALLER_UID;
    public static final String KEY_CLIENT_PACKAGE_NAME = "clientPackageName";
    public static final String KEY_REQUEST_ACTIONS = "request_visible_actions";

    @Deprecated
    public static final String KEY_REQUEST_VISIBLE_ACTIVITIES = "request_visible_actions";
    public static final String KEY_SUPPRESS_PROGRESS_SCREEN = "suppressProgressScreen";
    private static final ComponentName iH;
    private static final ComponentName iI;
    private static final Intent iJ;
    private static final Intent iK;

    static {
        KEY_CALLER_UID = Build.VERSION.SDK_INT >= 11 ? "callerUid" : "callerUid";
        KEY_ANDROID_PACKAGE_NAME = Build.VERSION.SDK_INT >= 14 ? "androidPackageName" : "androidPackageName";
        iH = new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.auth.GetToken");
        iI = new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.recovery.RecoveryService");
        iJ = new Intent().setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE).setComponent(iH);
        iK = new Intent().setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE).setComponent(iI);
    }

    private GoogleAuthUtil() {
    }

    private static String a(Context context, String str, String str2, Bundle bundle) throws Resources.NotFoundException, IOException, GoogleAuthException {
        int i;
        if (bundle == null) {
            bundle = new Bundle();
        }
        try {
            return getToken(context, str, str2, bundle);
        } catch (GooglePlayServicesAvailabilityException e) {
            PendingIntent errorPendingIntent = GooglePlayServicesUtil.getErrorPendingIntent(e.getConnectionStatusCode(), context, 0);
            Resources resources = context.getResources();
            Notification notification = new Notification(R.drawable.stat_sys_warning, resources.getString(com.google.android.gms.R.string.auth_client_play_services_err_notification_msg), System.currentTimeMillis());
            notification.flags |= 16;
            String packageName = context.getApplicationInfo().name;
            if (TextUtils.isEmpty(packageName)) {
                packageName = context.getPackageName();
            }
            String string = resources.getString(com.google.android.gms.R.string.auth_client_requested_by_msg, packageName);
            switch (e.getConnectionStatusCode()) {
                case 1:
                    i = com.google.android.gms.R.string.auth_client_needs_installation_title;
                    break;
                case 2:
                    i = com.google.android.gms.R.string.auth_client_needs_update_title;
                    break;
                case 3:
                    i = com.google.android.gms.R.string.auth_client_needs_enabling_title;
                    break;
                default:
                    i = com.google.android.gms.R.string.auth_client_using_bad_version_title;
                    break;
            }
            notification.setLatestEventInfo(context, resources.getString(i), string, errorPendingIntent);
            ((NotificationManager) context.getSystemService("notification")).notify(39789, notification);
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        } catch (UserRecoverableAuthException e2) {
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        }
    }

    private static void b(Intent intent) throws URISyntaxException {
        if (intent == null) {
            throw new IllegalArgumentException("Callack cannot be null.");
        }
        try {
            Intent.parseUri(intent.toUri(1), 1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
        }
    }

    public static String getToken(Context context, String accountName, String scope) throws IOException, GoogleAuthException {
        return getToken(context, accountName, scope, new Bundle());
    }

    public static String getToken(Context context, String accountName, String scope, Bundle extras) throws PackageManager.NameNotFoundException, GoogleAuthException, IOException, CertificateException {
        Context applicationContext = context.getApplicationContext();
        dm.x("Calling this from your main thread can lead to deadlock");
        m(applicationContext);
        Bundle extras2 = extras == null ? new Bundle() : new Bundle(extras);
        String str = context.getApplicationInfo().packageName;
        extras2.putString(KEY_CLIENT_PACKAGE_NAME, str);
        if (!extras2.containsKey(KEY_ANDROID_PACKAGE_NAME)) {
            extras2.putString(KEY_ANDROID_PACKAGE_NAME, str);
        }
        a aVar = new a();
        try {
            if (!applicationContext.bindService(iJ, aVar, 1)) {
                throw new IOException("Could not bind to service with the given context.");
            }
            try {
                try {
                    Bundle bundleA = o.a.a(aVar.aG()).a(accountName, scope, extras2);
                    String string = bundleA.getString("authtoken");
                    if (!TextUtils.isEmpty(string)) {
                        return string;
                    }
                    String string2 = bundleA.getString("Error");
                    Intent intent = (Intent) bundleA.getParcelable("userRecoveryIntent");
                    if (s(string2)) {
                        throw new UserRecoverableAuthException(string2, intent);
                    }
                    if (r(string2)) {
                        throw new IOException(string2);
                    }
                    throw new GoogleAuthException(string2);
                } catch (InterruptedException e) {
                    throw new GoogleAuthException("Interrupted");
                }
            } catch (RemoteException e2) {
                Log.i("GoogleAuthUtil", "GMS remote exception ", e2);
                throw new IOException("remote exception");
            }
        } finally {
            applicationContext.unbindService(aVar);
        }
    }

    public static String getTokenWithNotification(Context context, String accountName, String scope, Bundle extras) throws IOException, GoogleAuthException {
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putBoolean("handle_notification", true);
        return a(context, accountName, scope, extras);
    }

    public static String getTokenWithNotification(Context context, String accountName, String scope, Bundle extras, Intent callback) throws URISyntaxException, IOException, GoogleAuthException {
        b(callback);
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putParcelable("callback_intent", callback);
        extras.putBoolean("handle_notification", true);
        return a(context, accountName, scope, extras);
    }

    public static String getTokenWithNotification(Context context, String accountName, String scope, Bundle extras, String authority, Bundle syncBundle) throws IOException, GoogleAuthException {
        if (TextUtils.isEmpty(authority)) {
            throw new IllegalArgumentException("Authority cannot be empty or null.");
        }
        if (extras == null) {
            extras = new Bundle();
        }
        if (syncBundle == null) {
            syncBundle = new Bundle();
        }
        ContentResolver.validateSyncExtrasBundle(syncBundle);
        extras.putString("authority", authority);
        extras.putBundle("sync_extras", syncBundle);
        extras.putBoolean("handle_notification", true);
        return a(context, accountName, scope, extras);
    }

    public static void invalidateToken(Context context, String token) {
        AccountManager.get(context).invalidateAuthToken(GOOGLE_ACCOUNT_TYPE, token);
    }

    private static void m(Context context) throws PackageManager.NameNotFoundException, GoogleAuthException, CertificateException {
        try {
            GooglePlayServicesUtil.m(context);
        } catch (GooglePlayServicesNotAvailableException e) {
            throw new GoogleAuthException(e.getMessage());
        } catch (GooglePlayServicesRepairableException e2) {
            throw new GooglePlayServicesAvailabilityException(e2.getConnectionStatusCode(), e2.getMessage(), e2.getIntent());
        }
    }

    private static boolean r(String str) {
        return "NetworkError".equals(str) || "ServiceUnavailable".equals(str) || "Timeout".equals(str);
    }

    private static boolean s(String str) {
        return "BadAuthentication".equals(str) || "CaptchaRequired".equals(str) || "DeviceManagementRequiredOrSyncDisabled".equals(str) || "NeedPermission".equals(str) || "NeedsBrowser".equals(str) || "UserCancel".equals(str) || "AppDownloadRequired".equals(str);
    }
}

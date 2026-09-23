package vpadn;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.RFC2109Spec;
import org.apache.http.message.BasicHeader;

@TargetApi(16)
/* renamed from: vpadn.a, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class C0086a {
    public static void a(HttpClient httpClient) {
        new Thread(new aw(httpClient)).start();
    }

    public static String j(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator().substring(0, 3);
        } catch (Exception e) {
            return null;
        }
    }

    public static void a(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (inputStream == null || outputStream == null) {
            ab.b("Streams", "copyContent: Unable to copy from or to a null stream.");
            throw new IOException("Unable to copy from or to a null stream.");
        }
        byte[] bArr = new byte[AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED];
        while (true) {
            int i = inputStream.read(bArr);
            if (i != -1) {
                outputStream.write(bArr, 0, i);
            } else {
                return;
            }
        }
    }

    public static String e(String str) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes());
            byte[] bArrDigest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bArrDigest) {
                stringBuffer.append(Integer.toHexString((b & 255) | 256).substring(1));
            }
            return stringBuffer.toString();
        } catch (NullPointerException e) {
            ab.a("Utils", "sha1 throw NullPointerException", e);
            return "";
        } catch (NoSuchAlgorithmException e2) {
            ab.a("Utils", "sha1 throw NoSuchAlgorithmException", e2);
            return "";
        }
    }

    public static File c() {
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Android/data/", "/vpadn006");
        file.mkdirs();
        return new File(file, "vpadnguid006");
    }

    public static void a(InputStream inputStream, OutputStream outputStream, long j) throws IOException {
        if (inputStream == null || outputStream == null) {
            ab.b("Streams", "copyContent: Unable to copy from or to a null stream.");
            throw new IOException("Unable to copy from or to a null stream.");
        }
        byte[] bArr = new byte[AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED];
        long j2 = 0;
        while (true) {
            int i = inputStream.read(bArr);
            if (i != -1) {
                j2 += i;
                if (j2 >= 70000000) {
                    ab.b("Streams", "(for video cache) Error copying content: attempted to copy " + j2 + " bytes, with 70000000 maximum.");
                    throw new IOException("Error copying content: attempted to copy " + j2 + " bytes, with 70000000 maximum.");
                }
                outputStream.write(bArr, 0, i);
            } else {
                return;
            }
        }
    }

    public static boolean c(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String k(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSimOperator().substring(0, 3);
        } catch (Exception e) {
            return null;
        }
    }

    public static void c(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    public static String a(String str, String str2) throws Exception {
        byte[] bArrA = X.a(str, 0);
        byte[] bytes = str2.getBytes();
        if (bArrA.length != 16) {
            throw new IllegalArgumentException("Key length should be 16.");
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArrA, "AES");
        byte[] bArr = new byte[16];
        Arrays.fill(bArr, (byte) 0);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(1, secretKeySpec, ivParameterSpec);
        return X.a(cipher.doFinal(bytes), 2);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0059 A[Catch: IOException -> 0x004d, NullPointerException -> 0x005d, Exception -> 0x0066, TRY_ENTER, TryCatch #5 {IOException -> 0x004d, NullPointerException -> 0x005d, Exception -> 0x0066, blocks: (B:2:0x0000, B:4:0x0011, B:5:0x001f, B:7:0x0029, B:11:0x003a, B:23:0x0059, B:24:0x005c, B:17:0x0049), top: B:38:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void d(java.lang.String r5) throws java.lang.Throwable {
        /*
            java.io.File r0 = new java.io.File     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            java.io.File r1 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            java.lang.String r2 = "/vpadn006"
            r0.<init>(r1, r2)     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            boolean r0 = r0.exists()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            if (r0 == 0) goto L1f
            java.io.File r0 = new java.io.File     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            java.io.File r1 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            java.lang.String r2 = "/vpadn006"
            r0.<init>(r1, r2)     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            a(r0)     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
        L1f:
            java.io.File r2 = c()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            boolean r0 = r2.exists()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            if (r0 != 0) goto L2c
            r2.createNewFile()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
        L2c:
            r1 = 0
            java.io.PrintStream r0 = new java.io.PrintStream     // Catch: java.io.FileNotFoundException -> L3e java.lang.Throwable -> L56
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch: java.io.FileNotFoundException -> L3e java.lang.Throwable -> L56
            r3.<init>(r2)     // Catch: java.io.FileNotFoundException -> L3e java.lang.Throwable -> L56
            r0.<init>(r3)     // Catch: java.io.FileNotFoundException -> L3e java.lang.Throwable -> L56
            r0.print(r5)     // Catch: java.lang.Throwable -> L6f java.io.FileNotFoundException -> L74
            r0.close()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
        L3d:
            return
        L3e:
            r0 = move-exception
            r0 = r1
        L40:
            java.lang.String r1 = "VponGuid"
            java.lang.String r2 = "saveGuid throw FileNotFoundException"
            vpadn.ab.d(r1, r2)     // Catch: java.lang.Throwable -> L6f
            if (r0 == 0) goto L3d
            r0.close()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
            goto L3d
        L4d:
            r0 = move-exception
            java.lang.String r0 = "VponGuid"
            java.lang.String r1 = "saveGuid throw IOException"
            vpadn.ab.d(r0, r1)
            goto L3d
        L56:
            r0 = move-exception
        L57:
            if (r1 == 0) goto L5c
            r1.close()     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
        L5c:
            throw r0     // Catch: java.io.IOException -> L4d java.lang.NullPointerException -> L5d java.lang.Exception -> L66
        L5d:
            r0 = move-exception
            java.lang.String r0 = "VponGuid"
            java.lang.String r1 = "saveGuid throw NullPointerException"
            vpadn.ab.d(r0, r1)
            goto L3d
        L66:
            r0 = move-exception
            java.lang.String r0 = "VponGuid"
            java.lang.String r1 = "saveGuid throw Exception"
            vpadn.ab.d(r0, r1)
            goto L3d
        L6f:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L57
        L74:
            r1 = move-exception
            goto L40
        */
        throw new UnsupportedOperationException("Method not decompiled: vpadn.C0086a.d(java.lang.String):void");
    }

    public static T b(Context context) throws Exception {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ab.b("AdvertisingIdClient", "Cannot be called from the main thread");
            throw new IllegalStateException("Cannot be called from the main thread");
        }
        try {
            context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
            U u = new U((byte) 0);
            Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
            intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
            if (context.bindService(intent, u, 1)) {
                try {
                    try {
                        V v = new V(u.a());
                        return new T(v.a(), v.a(true));
                    } catch (Exception e) {
                        throw e;
                    }
                } finally {
                    context.unbindService(u);
                }
            }
            ab.b("AdvertisingIdClient", "Google Play connection failed");
            throw new IOException("Google Play connection failed");
        } catch (Exception e2) {
            ab.a("AdvertisingIdClient", "getPackageManager throw Exception", e2);
            throw e2;
        }
    }

    public static DefaultHttpClient a(DefaultHttpClient defaultHttpClient) {
        defaultHttpClient.addRequestInterceptor(new Y());
        defaultHttpClient.addResponseInterceptor(new Z());
        return defaultHttpClient;
    }

    public static String l(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator().substring(3);
        } catch (Exception e) {
            return null;
        }
    }

    public static void a(String str, DefaultHttpClient defaultHttpClient) throws Throwable {
        ab.a("CookieUtil", " ------->>CookieManager -> Cookie Store URL:" + str);
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        defaultHttpClient.setCookieStore(basicCookieStore);
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager != null) {
            RFC2109Spec rFC2109Spec = new RFC2109Spec();
            try {
                URL url = new URL(str);
                String cookie = cookieManager.getCookie(url.getHost());
                if (cookie != null) {
                    List<Cookie> list = rFC2109Spec.parse(new BasicHeader("set-cookie", cookie), new CookieOrigin(url.getHost(), url.getPort() == -1 ? url.getDefaultPort() : url.getPort(), "/", false));
                    boolean z = false;
                    boolean z2 = false;
                    for (Cookie cookie2 : list) {
                        if (cookie2.getName().equals("Vpadn-Guid") && cookie2.getDomain() != null && cookie2.getDomain().indexOf("vpon.com") >= 0) {
                            ab.a("CookieUtil", "Vpadn-Guid Cookie Found!! (domain name: vpon.com) " + cookie2.toString());
                            BasicClientCookie basicClientCookie = new BasicClientCookie("Vpadn-Guid", cookie2.getValue());
                            basicClientCookie.setDomain("vpon.com");
                            basicClientCookie.setPath("/");
                            basicCookieStore.addCookie(basicClientCookie);
                            z2 = true;
                        } else if (cookie2.getName().equals("Vpadn-Guid") && cookie2.getDomain() != null && cookie2.getDomain().indexOf("vpadn.com") >= 0) {
                            ab.a("CookieUtil", "Vpadn-Guid Cookie Found!! (domain name: vpadn.com) " + cookie2.toString());
                            BasicClientCookie basicClientCookie2 = new BasicClientCookie("Vpadn-Guid", cookie2.getValue());
                            basicClientCookie2.setDomain("vpadn.com");
                            basicClientCookie2.setPath("/");
                            basicCookieStore.addCookie(basicClientCookie2);
                            z = true;
                        } else {
                            basicCookieStore.addCookie(cookie2);
                        }
                    }
                    if (!z2) {
                        String strD = d();
                        ab.a("CookieUtil", "1.savedGuid:" + strD);
                        if (strD != null) {
                            BasicClientCookie basicClientCookie3 = new BasicClientCookie("Vpadn-Guid", strD);
                            basicClientCookie3.setDomain("vpon.com");
                            basicClientCookie3.setPath("/");
                            list.add(basicClientCookie3);
                        }
                    } else {
                        ab.a("CookieUtil", "2.save cookie:" + ((String) null));
                        d((String) null);
                    }
                    if (!z) {
                        String strD2 = d();
                        ab.a("CookieUtil", "3.savedGuid:" + strD2);
                        if (strD2 != null) {
                            BasicClientCookie basicClientCookie4 = new BasicClientCookie("Vpadn-Guid", strD2);
                            basicClientCookie4.setDomain("vpadn.com");
                            basicClientCookie4.setPath("/");
                            list.add(basicClientCookie4);
                            return;
                        }
                        return;
                    }
                    ab.a("CookieUtil", "4.save cookie:" + ((String) null));
                    d((String) null);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (MalformedCookieException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static boolean a(String str) {
        if (a() && !str.equals("")) {
            String string = Environment.getExternalStorageDirectory().toString();
            return (str.startsWith(string) ? new File(str) : new File(String.valueOf(string) + "/" + str)).exists();
        }
        return false;
    }

    public static void a(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static String m(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSimOperator().substring(3);
        } catch (Exception e) {
            return null;
        }
    }

    public static long a(boolean z) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return b(Environment.getExternalStorageDirectory().getPath());
        }
        if (z) {
            return b("/");
        }
        return -1L;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.io.InputStreamReader] */
    /* JADX WARN: Type inference failed for: r1v22 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r2v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v13, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v17, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v21, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9, types: [java.io.InputStream] */
    public static String d() throws Throwable {
        InputStreamReader inputStreamReader;
        String string = null;
        ?? C = c();
        if (C == 0 || C.exists()) {
            ?? CanRead = C.canRead();
            try {
                if (CanRead != 0) {
                    try {
                        CanRead = new FileInputStream((File) C);
                        try {
                            inputStreamReader = new InputStreamReader((InputStream) CanRead, "UTF-8");
                            try {
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                StringBuffer stringBuffer = new StringBuffer();
                                while (true) {
                                    String line = bufferedReader.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    stringBuffer.append(line);
                                }
                                string = stringBuffer.toString();
                                bufferedReader.close();
                                try {
                                    CanRead.close();
                                } catch (IOException e) {
                                    ab.d("VponGuid", "loadGuid in.close throw IOException");
                                }
                                try {
                                    inputStreamReader.close();
                                } catch (IOException e2) {
                                    ab.d("VponGuid", "loadGuid ir.close throw IOException");
                                }
                            } catch (FileNotFoundException e3) {
                                ab.d("VponGuid", "loadGuid throw FileNotFoundException");
                                if (CanRead != 0) {
                                    try {
                                        CanRead.close();
                                    } catch (IOException e4) {
                                        ab.d("VponGuid", "loadGuid in.close throw IOException");
                                    }
                                }
                                if (inputStreamReader != null) {
                                    try {
                                        inputStreamReader.close();
                                    } catch (IOException e5) {
                                        ab.d("VponGuid", "loadGuid ir.close throw IOException");
                                    }
                                }
                                return string;
                            } catch (UnsupportedEncodingException e6) {
                                ab.d("VponGuid", "loadGuid throw UnsupportedEncodingException");
                                if (CanRead != 0) {
                                    try {
                                        CanRead.close();
                                    } catch (IOException e7) {
                                        ab.d("VponGuid", "loadGuid in.close throw IOException");
                                    }
                                }
                                if (inputStreamReader != null) {
                                    try {
                                        inputStreamReader.close();
                                    } catch (IOException e8) {
                                        ab.d("VponGuid", "loadGuid ir.close throw IOException");
                                    }
                                }
                                return string;
                            } catch (IOException e9) {
                                ab.d("VponGuid", "loadGuid throw IOException");
                                if (CanRead != 0) {
                                    try {
                                        CanRead.close();
                                    } catch (IOException e10) {
                                        ab.d("VponGuid", "loadGuid in.close throw IOException");
                                    }
                                }
                                if (inputStreamReader != null) {
                                    try {
                                        inputStreamReader.close();
                                    } catch (IOException e11) {
                                        ab.d("VponGuid", "loadGuid ir.close throw IOException");
                                    }
                                }
                                return string;
                            }
                        } catch (FileNotFoundException e12) {
                            inputStreamReader = null;
                        } catch (UnsupportedEncodingException e13) {
                            inputStreamReader = null;
                        } catch (IOException e14) {
                            inputStreamReader = null;
                        } catch (Throwable th) {
                            C = 0;
                            th = th;
                            if (CanRead != 0) {
                                try {
                                    CanRead.close();
                                } catch (IOException e15) {
                                    ab.d("VponGuid", "loadGuid in.close throw IOException");
                                }
                            }
                            if (C != 0) {
                                try {
                                    C.close();
                                } catch (IOException e16) {
                                    ab.d("VponGuid", "loadGuid ir.close throw IOException");
                                }
                            }
                            throw th;
                        }
                    } catch (FileNotFoundException e17) {
                        inputStreamReader = null;
                        CanRead = 0;
                    } catch (UnsupportedEncodingException e18) {
                        inputStreamReader = null;
                        CanRead = 0;
                    } catch (IOException e19) {
                        inputStreamReader = null;
                        CanRead = 0;
                    } catch (Throwable th2) {
                        CanRead = 0;
                        th = th2;
                        C = 0;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
        return string;
    }

    public static String n(Context context) {
        try {
            return String.valueOf(((GsmCellLocation) ((TelephonyManager) context.getSystemService("phone")).getCellLocation()).getCid());
        } catch (Exception e) {
            return null;
        }
    }

    static long b(String str) {
        StatFs statFs = new StatFs(str);
        return (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1024;
    }

    public static String o(Context context) {
        try {
            return String.valueOf(((GsmCellLocation) ((TelephonyManager) context.getSystemService("phone")).getCellLocation()).getLac());
        } catch (Exception e) {
            return null;
        }
    }

    public static String d(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            ab.d("VponDevice", "get Imei fail");
            return null;
        }
    }

    public static boolean a() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static String e(Context context) {
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            ab.c("macAddress", "not use wifi for internet");
            return null;
        }
    }

    public static String f(Context context) {
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getBSSID();
        } catch (Exception e) {
            ab.c("macAddress", "not use wifi for internet");
            return null;
        }
    }

    public static boolean a(File file) {
        if (file.exists()) {
            File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles == null) {
                return true;
            }
            for (int i = 0; i < fileArrListFiles.length; i++) {
                if (fileArrListFiles[i].isDirectory()) {
                    a(fileArrListFiles[i]);
                } else {
                    fileArrListFiles[i].delete();
                }
            }
        }
        return file.delete();
    }

    public static String a(Context context) {
        File cacheDir;
        if (Environment.getExternalStorageState().equals("mounted")) {
            cacheDir = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Android/data/" + context.getPackageName() + "/cache/");
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    public static void b(String str, DefaultHttpClient defaultHttpClient) throws Throwable {
        String str2;
        String str3;
        String str4;
        boolean z = false;
        ab.a("CookieUtil", " ------->>Cookie Store -> CookieManager  URL:" + str);
        String value = null;
        List<Cookie> cookies = defaultHttpClient.getCookieStore().getCookies();
        if (cookies.size() <= 0) {
            ab.a("CookieUtil", "cookies.size() < 1");
            return;
        }
        ab.a("CookieUtil", "cookie size:" + cookies.size());
        CookieSyncManager cookieSyncManager = CookieSyncManager.getInstance();
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager == null) {
            ab.a("CookieUtil", "appCookieManager is null");
            return;
        }
        boolean z2 = false;
        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("$Version")) {
                String name = cookie.getName();
                String domain = cookie.getDomain();
                String path = cookie.getPath();
                if (!name.equals("Vpadn-Guid") || domain == null || (domain.indexOf("vpon.com") < 0 && domain.indexOf("vpadn.com") < 0)) {
                    str2 = str;
                } else {
                    if (domain.indexOf("vpon.com") >= 0) {
                        str3 = "vpon.com";
                        str4 = "vpon.com";
                        z2 = true;
                    } else {
                        str3 = "vpadn.com";
                        str4 = "vpadn.com";
                        z = true;
                    }
                    String str5 = str3;
                    path = "/";
                    domain = str4;
                    value = cookie.getValue();
                    str2 = str5;
                }
                String str6 = String.valueOf(name) + "=" + value;
                String str7 = domain != null ? String.valueOf(str6) + "; Domain=" + domain : str6;
                String str8 = path != null ? String.valueOf(str7) + "; Path=" + path : str7;
                ab.a("CookieUtil", "appCookieManager.setCookie:" + str8);
                cookieManager.setCookie(str2, str8);
            }
        }
        if (!z2) {
            String strD = d();
            ab.a("CookieUtil", "a.savedGuid:" + strD);
            if (strD != null) {
                cookieManager.setCookie("vpon.com", "Vpadn-Guid=" + strD + "; Domain=vpon.com; Path=/");
            }
        } else {
            ab.a("CookieUtil", "b.save cookie:" + value);
            d(value);
        }
        if (!z) {
            String strD2 = d();
            ab.a("CookieUtil", "c.savedGuid:" + strD2);
            if (strD2 != null) {
                cookieManager.setCookie("vpadn.com", "Vpadn-Guid=" + strD2 + "; Domain=vpadn.com; Path=/");
            }
        } else {
            ab.a("CookieUtil", "d.save cookie:" + value);
            d(value);
        }
        cookieSyncManager.sync();
    }

    public static int p(Context context) {
        try {
            int type = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getType();
            switch (type) {
                case 0:
                    return 1;
                case 1:
                    return 0;
                default:
                    return type;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static String b() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public static DisplayMetrics g(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String h(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String i(Context context) {
        String strH;
        try {
            ac.a(context);
            if (ac.b()) {
                strH = ac.a();
            } else {
                strH = h(context);
            }
            return strH;
        } catch (Exception e) {
            e.printStackTrace();
            return h(context);
        }
    }
}

package c;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0088b;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FileTransfer extends C0103q {
    public static int FILE_NOT_FOUND_ERR = 1;
    public static int INVALID_URL_ERR = 2;
    public static int CONNECTION_ERR = 3;
    public static int ABORTED_ERR = 4;
    private static HashMap<String, b> a = new HashMap<>();
    private static final HostnameVerifier b = new HostnameVerifier() { // from class: c.FileTransfer.1
        @Override // javax.net.ssl.HostnameVerifier
        public final boolean verify(String str, SSLSession sSLSession) {
            return true;
        }
    };

    /* renamed from: c, reason: collision with root package name */
    private static final TrustManager[] f170c = {new X509TrustManager() { // from class: c.FileTransfer.2
        @Override // javax.net.ssl.X509TrustManager
        public final X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override // javax.net.ssl.X509TrustManager
        public final void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public final void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }
    }};

    static final class b {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        File f173c;
        InputStream d;
        OutputStream e;
        boolean f;
        private C0101o g;

        b(String str, String str2, C0101o c0101o) {
            this.a = str;
            this.b = str2;
            this.g = c0101o;
        }

        final void a(C0108v c0108v) {
            synchronized (this) {
                if (!this.f) {
                    this.g.a(c0108v);
                }
            }
        }
    }

    static final class a extends FilterInputStream {
        private boolean a;

        public a(InputStream inputStream) {
            super(inputStream);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public final int read() throws IOException {
            int i = this.a ? -1 : super.read();
            this.a = i == -1;
            return i;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public final int read(byte[] bArr) throws IOException {
            int i = this.a ? -1 : super.read(bArr);
            this.a = i == -1;
            return i;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public final int read(byte[] bArr, int i, int i2) throws IOException {
            int i3 = this.a ? -1 : super.read(bArr, i, i2);
            this.a = i3 == -1;
            return i3;
        }
    }

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException, UnsupportedEncodingException {
        final b bVarRemove;
        if (str.equals("upload") || str.equals("download")) {
            final String string = jSONArray.getString(0);
            final String string2 = jSONArray.getString(1);
            if (str.equals("upload")) {
                try {
                    final String strDecode = URLDecoder.decode(string, "UTF-8");
                    Log.d("FileTransfer", "upload " + strDecode + " to " + string2);
                    final String strA = a(jSONArray, 2, "file");
                    final String strA2 = a(jSONArray, 3, "image.jpg");
                    final String strA3 = a(jSONArray, 4, "image/jpeg");
                    final JSONObject jSONObject = jSONArray.optJSONObject(5) == null ? new JSONObject() : jSONArray.optJSONObject(5);
                    final boolean zOptBoolean = jSONArray.optBoolean(6);
                    final boolean z = jSONArray.optBoolean(7) || jSONArray.isNull(7);
                    final JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(8) == null ? jSONObject.optJSONObject("headers") : jSONArray.optJSONObject(8);
                    final String string3 = jSONArray.getString(9);
                    Log.d("FileTransfer", "fileKey: " + strA);
                    Log.d("FileTransfer", "fileName: " + strA2);
                    Log.d("FileTransfer", "mimeType: " + strA3);
                    Log.d("FileTransfer", "params: " + jSONObject);
                    Log.d("FileTransfer", "trustEveryone: " + zOptBoolean);
                    Log.d("FileTransfer", "chunkedMode: " + z);
                    Log.d("FileTransfer", "headers: " + jSONObjectOptJSONObject);
                    Log.d("FileTransfer", "objectId: " + string3);
                    try {
                        final URL url = new URL(string2);
                        final boolean zEquals = url.getProtocol().equals("https");
                        final b bVar = new b(strDecode, string2, c0101o);
                        synchronized (a) {
                            a.put(string3, bVar);
                        }
                        this.cordova.e().execute(new Runnable() { // from class: c.FileTransfer.3
                            /* JADX WARN: Multi-variable type inference failed */
                            /* JADX WARN: Removed duplicated region for block: B:279:0x0608 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:292:0x027b A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:294:0x02fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:296:0x0418 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:298:0x05c8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Type inference failed for: r2v1, types: [boolean] */
                            /* JADX WARN: Type inference failed for: r2v105, types: [java.util.HashMap] */
                            /* JADX WARN: Type inference failed for: r2v11 */
                            /* JADX WARN: Type inference failed for: r2v110, types: [java.util.HashMap] */
                            /* JADX WARN: Type inference failed for: r2v119, types: [java.util.HashMap] */
                            /* JADX WARN: Type inference failed for: r2v12 */
                            /* JADX WARN: Type inference failed for: r2v146 */
                            /* JADX WARN: Type inference failed for: r2v152 */
                            /* JADX WARN: Type inference failed for: r2v154 */
                            /* JADX WARN: Type inference failed for: r2v16 */
                            /* JADX WARN: Type inference failed for: r2v162 */
                            /* JADX WARN: Type inference failed for: r2v166 */
                            /* JADX WARN: Type inference failed for: r2v167 */
                            /* JADX WARN: Type inference failed for: r2v168 */
                            /* JADX WARN: Type inference failed for: r2v169 */
                            /* JADX WARN: Type inference failed for: r2v170 */
                            /* JADX WARN: Type inference failed for: r2v171 */
                            /* JADX WARN: Type inference failed for: r2v172 */
                            /* JADX WARN: Type inference failed for: r2v173 */
                            /* JADX WARN: Type inference failed for: r2v174 */
                            /* JADX WARN: Type inference failed for: r2v175 */
                            /* JADX WARN: Type inference failed for: r2v176 */
                            /* JADX WARN: Type inference failed for: r2v177 */
                            /* JADX WARN: Type inference failed for: r2v19, types: [java.net.URLConnection] */
                            /* JADX WARN: Type inference failed for: r2v27 */
                            /* JADX WARN: Type inference failed for: r2v34, types: [java.net.HttpURLConnection] */
                            /* JADX WARN: Type inference failed for: r2v39 */
                            /* JADX WARN: Type inference failed for: r2v41 */
                            /* JADX WARN: Type inference failed for: r2v5 */
                            /* JADX WARN: Type inference failed for: r3v100 */
                            /* JADX WARN: Type inference failed for: r3v101 */
                            /* JADX WARN: Type inference failed for: r3v102 */
                            /* JADX WARN: Type inference failed for: r3v103 */
                            /* JADX WARN: Type inference failed for: r3v104 */
                            /* JADX WARN: Type inference failed for: r3v105 */
                            /* JADX WARN: Type inference failed for: r3v106 */
                            /* JADX WARN: Type inference failed for: r3v24, types: [java.io.Closeable] */
                            /* JADX WARN: Type inference failed for: r3v25 */
                            /* JADX WARN: Type inference failed for: r3v26 */
                            /* JADX WARN: Type inference failed for: r3v27 */
                            /* JADX WARN: Type inference failed for: r3v50, types: [java.io.Closeable, java.io.InputStream] */
                            /* JADX WARN: Type inference failed for: r3v51, types: [java.io.Closeable] */
                            /* JADX WARN: Type inference failed for: r3v52 */
                            /* JADX WARN: Type inference failed for: r3v61 */
                            /* JADX WARN: Type inference failed for: r3v62, types: [java.io.Closeable, java.io.InputStream] */
                            /* JADX WARN: Type inference failed for: r3v68 */
                            /* JADX WARN: Type inference failed for: r3v83 */
                            /* JADX WARN: Type inference failed for: r3v84 */
                            /* JADX WARN: Type inference failed for: r3v85 */
                            /* JADX WARN: Type inference failed for: r3v86 */
                            /* JADX WARN: Type inference failed for: r3v87 */
                            /* JADX WARN: Type inference failed for: r3v88 */
                            /* JADX WARN: Type inference failed for: r3v89 */
                            /* JADX WARN: Type inference failed for: r3v90 */
                            /* JADX WARN: Type inference failed for: r3v91 */
                            /* JADX WARN: Type inference failed for: r3v92 */
                            /* JADX WARN: Type inference failed for: r3v93 */
                            /* JADX WARN: Type inference failed for: r3v94 */
                            /* JADX WARN: Type inference failed for: r3v95 */
                            /* JADX WARN: Type inference failed for: r3v96 */
                            /* JADX WARN: Type inference failed for: r3v97 */
                            /* JADX WARN: Type inference failed for: r3v98 */
                            /* JADX WARN: Type inference failed for: r3v99 */
                            /* JADX WARN: Type inference failed for: r5v47, types: [java.lang.Object, java.lang.String] */
                            /* JADX WARN: Type inference failed for: r5v49, types: [java.lang.Object, java.lang.String] */
                            /* JADX WARN: Type inference failed for: r5v51, types: [java.lang.Object, java.lang.String] */
                            @Override // java.lang.Runnable
                            /*
                                Code decompiled incorrectly, please refer to instructions dump.
                                To view partially-correct add '--show-bad-code' argument
                            */
                            public final void run() throws java.lang.Throwable {
                                /*
                                    Method dump skipped, instructions count: 1715
                                    To view this dump add '--comments-level debug' option
                                */
                                throw new UnsupportedOperationException("Method not decompiled: c.FileTransfer.AnonymousClass3.run():void");
                            }
                        });
                    } catch (MalformedURLException e) {
                        JSONObject jSONObjectA = a(INVALID_URL_ERR, strDecode, string2, (Integer) 0);
                        Log.e("FileTransfer", jSONObjectA.toString(), e);
                        c0101o.a(new C0108v(C0108v.a.IO_EXCEPTION, jSONObjectA));
                    }
                } catch (UnsupportedEncodingException e2) {
                    c0101o.a(new C0108v(C0108v.a.MALFORMED_URL_EXCEPTION, "UTF-8 error."));
                }
            } else {
                Log.d("FileTransfer", "download " + string + " to " + string2);
                final boolean zOptBoolean2 = jSONArray.optBoolean(2);
                final String string4 = jSONArray.getString(3);
                try {
                    final URL url2 = new URL(string);
                    final boolean zEquals2 = url2.getProtocol().equals("https");
                    if (C0088b.a(string)) {
                        final b bVar2 = new b(string, string2, c0101o);
                        synchronized (a) {
                            a.put(string4, bVar2);
                        }
                        this.cordova.e().execute(new Runnable() { // from class: c.FileTransfer.4
                            /* JADX WARN: Multi-variable type inference failed */
                            /* JADX WARN: Removed duplicated region for block: B:243:0x0390 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:254:0x0284 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:258:0x0346 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:267:0x01fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Removed duplicated region for block: B:269:0x02de A[EXC_TOP_SPLITTER, SYNTHETIC] */
                            /* JADX WARN: Type inference failed for: r2v1, types: [boolean] */
                            /* JADX WARN: Type inference failed for: r2v120 */
                            /* JADX WARN: Type inference failed for: r2v121 */
                            /* JADX WARN: Type inference failed for: r2v122 */
                            /* JADX WARN: Type inference failed for: r2v2 */
                            /* JADX WARN: Type inference failed for: r2v31 */
                            /* JADX WARN: Type inference failed for: r2v81, types: [java.util.HashMap] */
                            /* JADX WARN: Type inference failed for: r2v83, types: [c.FileTransfer$b] */
                            /* JADX WARN: Type inference failed for: r3v42, types: [int] */
                            /* JADX WARN: Type inference failed for: r3v47, types: [c.FileTransfer$b] */
                            /* JADX WARN: Type inference failed for: r4v13, types: [int] */
                            /* JADX WARN: Type inference failed for: r4v19, types: [int] */
                            /* JADX WARN: Type inference failed for: r4v33, types: [java.util.HashMap] */
                            /* JADX WARN: Type inference failed for: r4v38, types: [int] */
                            /* JADX WARN: Type inference failed for: r4v8, types: [int] */
                            /* JADX WARN: Type inference failed for: r5v32, types: [vpadn.v] */
                            /* JADX WARN: Type inference failed for: r5v35, types: [int] */
                            /* JADX WARN: Type inference failed for: r6v27, types: [java.lang.Object, java.lang.String] */
                            /* JADX WARN: Type inference failed for: r6v29, types: [java.lang.String] */
                            /* JADX WARN: Type inference failed for: r8v30, types: [java.lang.String] */
                            @Override // java.lang.Runnable
                            /*
                                Code decompiled incorrectly, please refer to instructions dump.
                                To view partially-correct add '--show-bad-code' argument
                            */
                            public final void run() throws java.lang.Throwable {
                                /*
                                    Method dump skipped, instructions count: 1167
                                    To view this dump add '--comments-level debug' option
                                */
                                throw new UnsupportedOperationException("Method not decompiled: c.FileTransfer.AnonymousClass4.run():void");
                            }
                        });
                    } else {
                        Log.w("FileTransfer", "Source URL is not in white list: '" + string + "'");
                        c0101o.a(new C0108v(C0108v.a.IO_EXCEPTION, a(CONNECTION_ERR, string, string2, (Integer) 401)));
                    }
                } catch (MalformedURLException e3) {
                    JSONObject jSONObjectA2 = a(INVALID_URL_ERR, string, string2, (Integer) 0);
                    Log.e("FileTransfer", jSONObjectA2.toString(), e3);
                    c0101o.a(new C0108v(C0108v.a.IO_EXCEPTION, jSONObjectA2));
                }
            }
            return true;
        }
        if (str.equals("abort")) {
            String string5 = jSONArray.getString(0);
            synchronized (a) {
                bVarRemove = a.remove(string5);
            }
            if (bVarRemove != null) {
                File file = bVarRemove.f173c;
                if (file != null) {
                    file.delete();
                }
                JSONObject jSONObjectA3 = a(ABORTED_ERR, bVarRemove.a, bVarRemove.b, (Integer) (-1));
                synchronized (bVarRemove) {
                    bVarRemove.a(new C0108v(C0108v.a.ERROR, jSONObjectA3));
                    bVarRemove.f = true;
                }
                this.cordova.e().execute(new Runnable(this) { // from class: c.FileTransfer.5
                    @Override // java.lang.Runnable
                    public final void run() {
                        synchronized (bVarRemove) {
                            FileTransfer.a(bVarRemove.d);
                            FileTransfer.a(bVarRemove.e);
                        }
                    }
                });
            }
            c0101o.b();
            return true;
        }
        return false;
    }

    static /* synthetic */ void a(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    static /* synthetic */ InputStream a(URLConnection uRLConnection) throws IOException {
        return Build.VERSION.SDK_INT < 11 ? new a(uRLConnection.getInputStream()) : uRLConnection.getInputStream();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SSLSocketFactory b(HttpsURLConnection httpsURLConnection) throws NoSuchAlgorithmException, KeyManagementException {
        SSLSocketFactory sSLSocketFactory = httpsURLConnection.getSSLSocketFactory();
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, f170c, new SecureRandom());
            httpsURLConnection.setSSLSocketFactory(sSLContext.getSocketFactory());
        } catch (Exception e) {
            Log.e("FileTransfer", e.getMessage(), e);
        }
        return sSLSocketFactory;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JSONObject b(int i, String str, String str2, URLConnection uRLConnection) throws IOException {
        int responseCode = 0;
        if (uRLConnection != null) {
            try {
                if (uRLConnection instanceof HttpURLConnection) {
                    responseCode = ((HttpURLConnection) uRLConnection).getResponseCode();
                }
            } catch (IOException e) {
                Log.w("FileTransfer", "Error getting HTTP status code from connection.", e);
            }
        }
        return a(i, str, str2, Integer.valueOf(responseCode));
    }

    private static JSONObject a(int i, String str, String str2, Integer num) throws JSONException {
        JSONObject jSONObject;
        JSONException e;
        try {
            jSONObject = new JSONObject();
            try {
                jSONObject.put("code", i);
                jSONObject.put("source", str);
                jSONObject.put("target", str2);
                if (num != null) {
                    jSONObject.put("http_status", num);
                }
            } catch (JSONException e2) {
                e = e2;
                Log.e("FileTransfer", e.getMessage(), e);
                return jSONObject;
            }
        } catch (JSONException e3) {
            jSONObject = null;
            e = e3;
        }
        return jSONObject;
    }

    private static String a(JSONArray jSONArray, int i, String str) {
        String strOptString;
        return (jSONArray.length() < i || (strOptString = jSONArray.optString(i)) == null || "null".equals(strOptString)) ? str : strOptString;
    }

    static /* synthetic */ InputStream a(FileTransfer fileTransfer, String str) throws FileNotFoundException {
        if (str.startsWith("content:")) {
            return fileTransfer.cordova.a().getContentResolver().openInputStream(Uri.parse(str));
        }
        if (!str.startsWith("file://")) {
            return new FileInputStream(str);
        }
        int iIndexOf = str.indexOf("?");
        return iIndexOf == -1 ? new FileInputStream(str.substring(7)) : new FileInputStream(str.substring(7, iIndexOf));
    }

    static /* synthetic */ File b(FileTransfer fileTransfer, String str) throws FileNotFoundException {
        File file = str.startsWith("file://") ? new File(str.substring("file://".length())) : new File(str);
        if (file.getParent() == null) {
            throw new FileNotFoundException();
        }
        return file;
    }
}

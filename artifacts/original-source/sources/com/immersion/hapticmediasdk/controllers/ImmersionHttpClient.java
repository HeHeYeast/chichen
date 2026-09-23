package com.immersion.hapticmediasdk.controllers;

import com.immersion.hapticmediasdk.utils.Log;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ImmersionHttpClient {
    private static final String a = "ImmersionHttpClient";

    /* renamed from: b044604460446ц04460446, reason: contains not printable characters */
    public static int f56b04460446044604460446 = 0;

    /* renamed from: b0446ц0446ц04460446, reason: contains not printable characters */
    public static int f57b0446044604460446 = 1;

    /* renamed from: bц04460446ц04460446, reason: contains not printable characters */
    public static int f58b0446044604460446 = 2;

    /* renamed from: bццц044604460446, reason: contains not printable characters */
    public static int f59b044604460446 = 3;
    private DefaultHttpClient b;

    private ImmersionHttpClient() {
        if (((m76b044604460446() + f57b0446044604460446) * m76b044604460446()) % f58b0446044604460446 != f56b04460446044604460446) {
            f56b04460446044604460446 = m76b044604460446();
        }
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        this.b = null;
    }

    private HttpResponse a(HttpUriRequest httpUriRequest, Map map, int i) throws Exception {
        URI uri = httpUriRequest.getURI();
        String strTrim = uri.getHost() != null ? uri.getHost().trim() : "";
        if (strTrim.length() > 0) {
            httpUriRequest.setHeader("Host", strTrim);
            if (((f59b044604460446 + f57b0446044604460446) * f59b044604460446) % f58b0446044604460446 != f56b04460446044604460446) {
                f59b044604460446 = 43;
                f56b04460446044604460446 = 98;
            }
        }
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                httpUriRequest.setHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        Header[] allHeaders = httpUriRequest.getAllHeaders();
        Log.d(a, "request URI [" + httpUriRequest.getURI() + "]");
        for (Header header : allHeaders) {
            Log.d(a, "request header [" + header.toString() + "]");
        }
        HttpConnectionParams.setSoTimeout(this.b.getParams(), i);
        HttpResponse httpResponseExecute = this.b.execute(httpUriRequest);
        if (httpResponseExecute == null) {
            throw new RuntimeException("Null response returned.");
        }
        return httpResponseExecute;
    }

    private void a() {
        boolean z = false;
        if (this.b == null) {
            int i = f59b044604460446;
            switch ((i * (f57b0446044604460446 + i)) % f58b0446044604460446) {
                case 0:
                    break;
                default:
                    f59b044604460446 = 66;
                    f56b04460446044604460446 = 39;
                    break;
            }
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            while (true) {
                switch (z) {
                    case false:
                        break;
                    case true:
                        break;
                    default:
                        while (true) {
                            switch (z) {
                            }
                        }
                        break;
                }
            }
            ConnManagerParams.setMaxTotalConnections(basicHttpParams, 5);
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 5000);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            this.b = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
        }
    }

    /* renamed from: b0446цц044604460446, reason: contains not printable characters */
    public static int m75b0446044604460446() {
        return 1;
    }

    /* renamed from: bцц0446ц04460446, reason: contains not printable characters */
    public static int m76b044604460446() {
        return 26;
    }

    public static ImmersionHttpClient getHttpClient() {
        ImmersionHttpClient immersionHttpClient = new ImmersionHttpClient();
        immersionHttpClient.a();
        while (true) {
            boolean z = false;
            switch (z) {
                case false:
                    break;
                case true:
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        int iM76b044604460446 = m76b044604460446();
        switch ((iM76b044604460446 * (f57b0446044604460446 + iM76b044604460446)) % f58b0446044604460446) {
            default:
                f57b0446044604460446 = 31;
            case 0:
                return immersionHttpClient;
        }
    }

    public HttpResponse executeDelete(String str, Map map, int i) throws Exception {
        HttpDelete httpDelete = new HttpDelete(str);
        if (((f59b044604460446 + f57b0446044604460446) * f59b044604460446) % f58b0446044604460446 != f56b04460446044604460446) {
            f59b044604460446 = 82;
            f56b04460446044604460446 = m76b044604460446();
        }
        return a(httpDelete, map, i);
    }

    public HttpResponse executeGet(String str, Map map, int i) throws Exception {
        int i2 = f59b044604460446;
        switch ((i2 * (m75b0446044604460446() + i2)) % f58b0446044604460446) {
            case 0:
                break;
            default:
                f59b044604460446 = m76b044604460446();
                f56b04460446044604460446 = m76b044604460446();
                break;
        }
        try {
            try {
                return a(new HttpGet(str), map, i);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public HttpResponse executePost(String str, Map map, int i) throws Exception {
        return a(new HttpPost(str), map, i);
    }

    public HttpResponse executePostWithBody(String str, String str2, Map map, int i) throws Exception {
        try {
            HttpPost httpPost = new HttpPost(str);
            try {
                try {
                    StringEntity stringEntity = new StringEntity(str2, "UTF-8");
                    int i2 = f59b044604460446;
                    switch ((i2 * (f57b0446044604460446 + i2)) % f58b0446044604460446) {
                        case 0:
                            break;
                        default:
                            f59b044604460446 = m76b044604460446();
                            f56b04460446044604460446 = 81;
                            break;
                    }
                    httpPost.setEntity(stringEntity);
                    return a(httpPost, map, i);
                } catch (UnsupportedEncodingException e) {
                    throw e;
                }
            } catch (Exception e2) {
                throw e2;
            }
        } catch (Exception e3) {
            throw e3;
        }
    }

    public HttpParams getParams() throws Exception {
        int i = f59b044604460446;
        switch ((i * (f57b0446044604460446 + i)) % f58b0446044604460446) {
            case 0:
                break;
            default:
                f59b044604460446 = 18;
                f56b04460446044604460446 = m76b044604460446();
                break;
        }
        try {
            try {
                return this.b.getParams();
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }
}

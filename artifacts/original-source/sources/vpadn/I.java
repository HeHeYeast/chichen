package vpadn;

import android.os.AsyncTask;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class I extends AsyncTask<Object, Integer, G> {
    private static /* synthetic */ int[] e;
    private static /* synthetic */ int[] f;
    private L a;
    private Q b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f311c;
    private String d;

    @Override // android.os.AsyncTask
    protected /* synthetic */ G doInBackground(Object... objArr) {
        return a();
    }

    @Override // android.os.AsyncTask
    protected /* synthetic */ void onPostExecute(G g) {
        switch (b()[g.ordinal()]) {
            case 25:
                if (this.a != null) {
                    this.a.b(this.b);
                    break;
                }
                break;
            default:
                ab.b("VponRequestAsyncTask", "onPostExecute is failded!");
                break;
        }
        this.a = null;
    }

    private static /* synthetic */ int[] b() {
        int[] iArr = e;
        if (iArr == null) {
            iArr = new int[G.valuesCustom().length];
            try {
                iArr[G.API_ERR_APP_IS_APPROVING.ordinal()] = 11;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[G.API_ERR_BLOCKED_LICENSE_KEY.ordinal()] = 7;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[G.API_ERR_DATABASE_DATA_ACCESS.ordinal()] = 14;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[G.API_ERR_DEVICE_NOT_SUPPORTED.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[G.API_ERR_GEO_DECODING_FAILED.ordinal()] = 19;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[G.API_ERR_IMP_CLICK_LIVE_AD_WEIGHT_ZERO.ordinal()] = 24;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[G.API_ERR_INVALID_IMPRESSION_AD_ID.ordinal()] = 15;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[G.API_ERR_INVALID_LICENSE_KEY.ordinal()] = 8;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[G.API_ERR_INVALID_REQ_PARAMETERS.ordinal()] = 16;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[G.API_ERR_INVALID_TS.ordinal()] = 22;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[G.API_ERR_NO_AD_FOUND_IN_ONLINE_QUEUE.ordinal()] = 2;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[G.API_ERR_NO_MATCHED_AD_IMAGE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[G.API_ERR_NO_MATCHED_SCREEN_SIZE.ordinal()] = 12;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[G.API_ERR_ONLINE_QUEUED_AD_NOT_FOUND.ordinal()] = 20;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[G.API_ERR_PERMISSION.ordinal()] = 23;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[G.API_ERR_SDK_DEPRECATED.ordinal()] = 3;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[G.API_ERR_TARGET_OS_NOT_SUPPORTED.ordinal()] = 10;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr[G.API_ERR_TEST_AD_NOT_FOUND.ordinal()] = 21;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr[G.API_ERR_UNABLE_TO_DETEMINE_QUADKEY.ordinal()] = 5;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr[G.API_ERR_UNKNOWN_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr[G.API_ERR_UNKNOWN_LICENSE_KEY.ordinal()] = 6;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr[G.API_ERR_WRONG_AD_TYPE.ordinal()] = 17;
            } catch (NoSuchFieldError e23) {
            }
            try {
                iArr[G.API_ERR_WRONG_APP_STATUS.ordinal()] = 9;
            } catch (NoSuchFieldError e24) {
            }
            try {
                iArr[G.API_ERR_WRONG_CLICK_TYPE.ordinal()] = 18;
            } catch (NoSuchFieldError e25) {
            }
            try {
                iArr[G.API_SUCCESS.ordinal()] = 25;
            } catch (NoSuchFieldError e26) {
            }
            e = iArr;
        }
        return iArr;
    }

    private static /* synthetic */ int[] c() {
        int[] iArr = f;
        if (iArr == null) {
            iArr = new int[AsyncTask.Status.values().length];
            try {
                iArr[AsyncTask.Status.FINISHED.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AsyncTask.Status.PENDING.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[AsyncTask.Status.RUNNING.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            f = iArr;
        }
        return iArr;
    }

    public I(String str, Object... objArr) {
        this.f311c = null;
        this.d = null;
        synchronized (I.class) {
            this.d = str;
            this.a = (L) objArr[0];
            this.f311c = (String) objArr[1];
        }
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        switch (c()[getStatus().ordinal()]) {
            case 1:
                ab.b("onCancelled", "FINISHED");
                break;
            case 2:
                ab.b("onCancelled", "PENDING");
                break;
            case 3:
                ab.b("onCancelled", "RUNNING");
                break;
        }
    }

    private G a() throws Throwable {
        G gA;
        try {
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 30000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
            HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            URI uri = new URI(this.d);
            ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(threadSafeClientConnManager, basicHttpParams);
            C0086a.a(defaultHttpClient);
            C0086a.a(this.d, defaultHttpClient);
            defaultHttpClient.getParams().setParameter("http.useragent", this.f311c);
            HttpResponse httpResponseExecute = defaultHttpClient.execute(new HttpGet(uri));
            C0086a.b(this.d, defaultHttpClient);
            threadSafeClientConnManager.closeExpiredConnections();
            if (httpResponseExecute.getStatusLine().getStatusCode() != 200) {
                String str = "httpResponse.getStatusLine().getStatusCode():" + httpResponseExecute.getStatusLine().getStatusCode();
                ab.b("VponRequestAsyncTask", str);
                S.a().b(str, true);
                S.a().b(httpResponseExecute, true);
                gA = G.a(-1);
            } else {
                S.a().a(httpResponseExecute, true);
                this.b = new Q();
                this.b.a = EntityUtils.toString(httpResponseExecute.getEntity(), "UTF-8");
                S.a().a(this.b.a, true);
                gA = G.a(0);
            }
            return gA;
        } catch (Exception e2) {
            e2.printStackTrace();
            ab.a("VponRequestAsyncTask", "request()  throw Exception:" + e2.getMessage(), e2);
            return G.a(-1);
        }
    }
}

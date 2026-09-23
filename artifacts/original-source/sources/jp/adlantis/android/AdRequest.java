package jp.adlantis.android;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import jp.adlantis.android.utils.ADLAssetUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdRequest extends NetworkRequest implements AdRequestNotifier {
    protected static String DEBUG_TASK = "AdRequest";
    protected AdlantisAdsModel adModel;
    protected AdRequestListeners listeners = new AdRequestListeners();

    public interface AdRequestManagerCallback {
        void adsLoaded();
    }

    public AdRequest(AdlantisAdsModel adlantisAdsModel) {
        this.adModel = adlantisAdsModel;
    }

    Uri adRequestUri(Context context, Map<String, String> map) {
        return getAdNetworkConnection().adRequestUri(adManager(), context, map);
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void addRequestListener(AdRequestListener adRequestListener) {
        this.listeners.addRequestListener(adRequestListener);
    }

    public void addRequestListeners(AdRequestListeners adRequestListeners) {
        this.listeners.addRequestListeners(adRequestListeners.listeners);
    }

    public AdlantisAd[] adsForRequestUri(Context context, Uri uri) throws IOException {
        return AdlantisAd.adsFromJSONInputStream(inputStreamForUri(context, uri));
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [jp.adlantis.android.AdRequest$3] */
    public void connect(final Context context, final Map<String, String> map, final AdRequestManagerCallback adRequestManagerCallback) {
        if (Looper.getMainLooper() == null) {
            log_e("Looper.getMainLooper() == null connect() failed.");
        }
        final Handler handler = new Handler(Looper.getMainLooper()) { // from class: jp.adlantis.android.AdRequest.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (adRequestManagerCallback != null) {
                    adRequestManagerCallback.adsLoaded();
                }
            }
        };
        new Thread() { // from class: jp.adlantis.android.AdRequest.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                AdRequest.this.doAdRequest(context, map);
                handler.sendMessage(handler.obtainMessage(0, this));
            }
        }.start();
    }

    public boolean doAdRequest(Context context, Map<String, String> map) {
        boolean z;
        try {
            AdlantisAd[] adlantisAdArrAdsForRequestUri = adsForRequestUri(context, adRequestUri(context, map));
            if (adlantisAdArrAdsForRequestUri != null) {
                z = adlantisAdArrAdsForRequestUri.length > 0;
                getAdsModel().setAds(adlantisAdArrAdsForRequestUri);
                log_d(adlantisAdArrAdsForRequestUri.length + " ads loaded");
            }
            z = z;
        } catch (MalformedURLException e) {
            z = z;
            log_e(e.toString());
        } catch (IOException e2) {
            z = z;
            log_e(e2.toString());
        }
        if (z) {
            notifyListenersAdReceived(null);
        } else {
            notifyListenersFailedToReceiveAd(null);
        }
        return z;
    }

    AdlantisAdsModel getAdsModel() {
        return this.adModel;
    }

    InputStream inputStreamForHttpUri(Uri uri, String str, String str2) throws IOException {
        HttpRequestInterceptor httpRequestInterceptor = new HttpRequestInterceptor() { // from class: jp.adlantis.android.AdRequest.1
            @Override // org.apache.http.HttpRequestInterceptor
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                Credentials credentials;
                AuthState authState = (AuthState) httpContext.getAttribute("http.auth.target-scope");
                CredentialsProvider credentialsProvider = (CredentialsProvider) httpContext.getAttribute("http.auth.credentials-provider");
                HttpHost httpHost = (HttpHost) httpContext.getAttribute("http.target_host");
                if (authState.getAuthScheme() != null || (credentials = credentialsProvider.getCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort()))) == null) {
                    return;
                }
                authState.setAuthScheme(new BasicScheme());
                authState.setCredentials(credentials);
            }
        };
        HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        AbstractHttpClient abstractHttpClientHttpClientFactory = httpClientFactory();
        abstractHttpClientHttpClientFactory.addRequestInterceptor(httpRequestInterceptor, 0);
        abstractHttpClientHttpClientFactory.getCredentialsProvider().setCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort()), new UsernamePasswordCredentials(str, str2));
        String string = uri.toString();
        log_d(string);
        return abstractHttpClientHttpClientFactory.execute(new HttpGet(string)).getEntity().getContent();
    }

    InputStream inputStreamForUri(Context context, Uri uri) throws IOException {
        if (isHttp(uri)) {
            return inputStreamForHttpUri(uri, "", "");
        }
        if (isFile(uri) && ADLAssetUtils.isAssetUri(uri)) {
            return ADLAssetUtils.inputStreamFromAssetUri(context, uri);
        }
        return null;
    }

    public boolean isFile(Uri uri) {
        return uri.getScheme().equals("file");
    }

    public boolean isHttp(Uri uri) {
        return uri.getScheme().equals("http") || uri.getScheme().equals("https");
    }

    public void notifyListenersAdReceived(AdRequestNotifier adRequestNotifier) {
        this.listeners.notifyListenersAdReceived(adRequestNotifier);
    }

    public void notifyListenersFailedToReceiveAd(AdRequestNotifier adRequestNotifier) {
        this.listeners.notifyListenersFailedToReceiveAd(adRequestNotifier);
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void removeRequestListener(AdRequestListener adRequestListener) {
        this.listeners.removeRequestListener(adRequestListener);
    }
}

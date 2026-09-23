package jp.adlantis.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import jp.adlantis.android.AdService;
import jp.adlantis.android.utils.ADLStringUtils;
import jp.adlantis.android.utils.AsyncImageLoader;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdManager {
    private static String DEBUG_TASK = "AdManager";
    AdServiceManager adServiceManager = new AdServiceManager();
    private AdService.TargetingParams targetingParams = new AdService.TargetingParams();
    private long _adFetchInterval = 60000;
    private long _adDisplayInterval = 10000;
    private AsyncImageLoader _asyncImageLoader = new AsyncImageLoader();
    private String _conversionTag = null;
    private boolean _conversionTagSent = false;
    private boolean _connectionChangeReceiverRegistered = false;
    private AdNetworkConnection _adNetworkConnection = createConnection();

    protected static class AdManagerHolder {
        protected static AdManager INSTANCE;

        static {
            INSTANCE = AdManager.isGreeSdk() ? new GreeAdManager() : new AdManager();
        }

        protected AdManagerHolder() {
        }
    }

    public interface AdManagerRedirectUrlProcessedCallback {
        void redirectProcessed(Uri uri);
    }

    private class ConnectionChangeReceiver extends BroadcastReceiver {
        private ConnectionChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (AdManager.isNetworkAvailable(context)) {
                AdManager.this.onNetworkAvailable(context);
            }
        }
    }

    protected AdManager() {
        addService(new ADLAdService(null));
    }

    public static AdManager getInstance() {
        return AdManagerHolder.INSTANCE;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [jp.adlantis.android.AdManager$4] */
    private void handleHttpClickRequest(String str, final AdManagerRedirectUrlProcessedCallback adManagerRedirectUrlProcessedCallback) {
        final String strBuildCompleteHttpUri = this._adNetworkConnection.buildCompleteHttpUri(null, str);
        Log.d(DEBUG_TASK, "handleHttpClickRequest=" + strBuildCompleteHttpUri);
        final Handler handler = new Handler(Looper.getMainLooper()) { // from class: jp.adlantis.android.AdManager.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (adManagerRedirectUrlProcessedCallback != null) {
                    adManagerRedirectUrlProcessedCallback.redirectProcessed((Uri) message.obj);
                }
            }
        };
        final DefaultRedirectHandler defaultRedirectHandler = new DefaultRedirectHandler() { // from class: jp.adlantis.android.AdManager.3
            @Override // org.apache.http.impl.client.DefaultRedirectHandler, org.apache.http.client.RedirectHandler
            public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
                String value = null;
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode >= 300 && statusCode < 400) {
                    Header[] headers = httpResponse.getHeaders("Location");
                    if (headers.length > 0) {
                        value = headers[0].getValue();
                        Log.d(AdManager.DEBUG_TASK, "location=" + value);
                    }
                }
                if (value == null) {
                    value = ((HttpHost) httpContext.getAttribute("http.target_host")).toURI() + ((HttpUriRequest) httpContext.getAttribute("http.request")).getURI();
                }
                handler.sendMessage(handler.obtainMessage(0, Uri.parse(value)));
                return false;
            }
        };
        new Thread() { // from class: jp.adlantis.android.AdManager.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws IOException {
                try {
                    AbstractHttpClient abstractHttpClientHttpClientFactory = NetworkRequest.httpClientFactory();
                    abstractHttpClientHttpClientFactory.setRedirectHandler(defaultRedirectHandler);
                    abstractHttpClientHttpClientFactory.execute(new HttpGet(strBuildCompleteHttpUri));
                } catch (MalformedURLException e) {
                    Log.e(AdManager.DEBUG_TASK, e.toString());
                } catch (IOException e2) {
                    Log.e(AdManager.DEBUG_TASK, e2.toString());
                }
            }
        }.start();
    }

    public static boolean isGreeSdk() {
        return GreeApiDelegator.greePlatformAvailable();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            try {
                NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
                if (allNetworkInfo != null) {
                    for (NetworkInfo networkInfo : allNetworkInfo) {
                        if (networkInfo.isConnected()) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(DEBUG_TASK, e.toString());
            }
        }
        return false;
    }

    public static void main(String[] strArr) {
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [jp.adlantis.android.AdManager$1] */
    private void sendConversionTagInternal(final Context context, final String str, final boolean z) {
        registerConnectionChangeReceiver(context);
        if (!str.equals(this._conversionTag)) {
            this._conversionTag = str;
            this._conversionTagSent = false;
        }
        if (this._conversionTagSent) {
            return;
        }
        new Thread() { // from class: jp.adlantis.android.AdManager.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    AbstractHttpClient abstractHttpClientHttpClientFactory = NetworkRequest.httpClientFactory();
                    String string = AdManager.this._adNetworkConnection.conversionTagRequestUri(context, str, z).toString();
                    Log.d(AdManager.DEBUG_TASK, "sendConversionTag url=" + string);
                    int statusCode = abstractHttpClientHttpClientFactory.execute(new HttpGet(string)).getStatusLine().getStatusCode();
                    if (statusCode < 300 || statusCode >= 501) {
                        return;
                    }
                    AdManager.this._conversionTagSent = true;
                } catch (MalformedURLException e) {
                    Log.e(AdManager.DEBUG_TASK, "sendConversionTag exception=" + e.toString());
                } catch (IOException e2) {
                    Log.e(AdManager.DEBUG_TASK, "sendConversionTag exception=" + e2.toString());
                }
            }
        }.start();
    }

    protected static void setInstance(AdManager adManager) {
        AdManagerHolder.INSTANCE = adManager;
    }

    public long adDisplayInterval() {
        return this._adDisplayInterval;
    }

    public long adFetchInterval() {
        return this._adFetchInterval;
    }

    public String adTapUrlCompleteString(AdlantisAd adlantisAd) {
        String strTapUrlString = adlantisAd.tapUrlString();
        return (adlantisAd.isWebLink() && ADLStringUtils.isHttpUrl(strTapUrlString)) ? this._adNetworkConnection.buildCompleteHttpUri(null, strTapUrlString) : strTapUrlString;
    }

    public void addService(AdService adService) {
        this.adServiceManager.addService(adService);
    }

    public AsyncImageLoader asyncImageLoader() {
        return this._asyncImageLoader;
    }

    public String byline() {
        return "Ads by AdLantis";
    }

    AdNetworkConnection createConnection() {
        return new AdLantisConnection();
    }

    public AdService getActiveAdService(Context context) {
        return this.adServiceManager.getActiveAdService(context);
    }

    public AdNetworkConnection getAdNetworkConnection() {
        return this._adNetworkConnection;
    }

    public ADLAdService getAdlAdService() {
        return this.adServiceManager.getAdlAdService();
    }

    public InternationalAdService getInternationalAdService() {
        return this.adServiceManager.getInternationalAdService();
    }

    public String getPublisherID() {
        return this.adServiceManager.getAdlAdService().getPublisherId();
    }

    public AdService.TargetingParams getTargetingParam() {
        return this.targetingParams;
    }

    public String gitSha() {
        return SDKVersion.GIT_SHA;
    }

    public void handleClickRequest(AdlantisAd adlantisAd, final AdManagerRedirectUrlProcessedCallback adManagerRedirectUrlProcessedCallback) {
        if (adManagerRedirectUrlProcessedCallback != null) {
            if (adlantisAd.shouldHandleRedirect()) {
                handleHttpClickRequest(adlantisAd.tapUriRedirect(), adManagerRedirectUrlProcessedCallback);
            } else {
                Handler handler = new Handler(Looper.getMainLooper()) { // from class: jp.adlantis.android.AdManager.5
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        if (adManagerRedirectUrlProcessedCallback != null) {
                            adManagerRedirectUrlProcessedCallback.redirectProcessed((Uri) message.obj);
                        }
                    }
                };
                handler.sendMessage(handler.obtainMessage(0, Uri.parse(adTapUrlCompleteString(adlantisAd))));
            }
        }
    }

    boolean hasTestAdRequestUrls() {
        return this._adNetworkConnection.hasTestAdRequestUrls();
    }

    public String keywords() {
        return this.targetingParams.getKeywords();
    }

    protected void onNetworkAvailable(Context context) {
        if (this._conversionTag == null || this._conversionTagSent) {
            return;
        }
        sendConversionTag(context, this._conversionTag);
    }

    public String publisherIDMetadataKey() {
        return this._adNetworkConnection.publisherIDMetadataKey();
    }

    protected void registerConnectionChangeReceiver(Context context) {
        if (this._connectionChangeReceiverRegistered) {
            return;
        }
        context.getApplicationContext().registerReceiver(new ConnectionChangeReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this._connectionChangeReceiverRegistered = true;
    }

    public String sdkBuild() {
        return SDKVersion.BUILDNUMBER;
    }

    public String sdkDescription() {
        return "AdLantis SDK";
    }

    public String sdkFullVersion() {
        return sdkDescription() + " " + sdkVersion() + " (" + gitSha() + ")";
    }

    public String sdkVersion() {
        return SDKVersion.VERSION;
    }

    public void sendConversionTag(Context context, String str) {
        sendConversionTagInternal(context, str, false);
    }

    public void sendConversionTagTest(Context context, String str) {
        sendConversionTagInternal(context, str, true);
    }

    public void setAdDisplayInterval(long j) {
        this._adDisplayInterval = j;
    }

    public void setAdFetchInterval(long j) {
        this._adFetchInterval = j;
    }

    public void setAdNetworkConnection(AdNetworkConnection adNetworkConnection) {
        this._adNetworkConnection = adNetworkConnection;
    }

    public void setCountry(String str) {
        this.targetingParams.setCountry(str);
    }

    public void setGapPublisherID(String str) {
        setPublisherID(str);
    }

    public void setHost(String str) {
        this._adNetworkConnection.setHost(str);
    }

    public void setKeywords(String str) {
        this.targetingParams.setKeywords(str);
    }

    public void setLocation(Location location) {
        this.targetingParams.setLocation(location);
    }

    public void setPublisherID(String str) {
        this.adServiceManager.getAdlAdService().setPublisherId(str);
    }

    public void setTestAdRequestUrls(String[] strArr) {
        Log.d(DEBUG_TASK, "setting test AdRequestUrls");
        this._adNetworkConnection.setTestAdRequestUrls(strArr);
    }
}

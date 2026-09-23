package jp.adlantis.android;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jp.adlantis.android.utils.ADLStringUtils;
import jp.adlantis.android.utils.AdlantisUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisAd extends HashMap<String, Object> implements Map<String, Object>, Serializable {
    public static final int ADTYPE_BANNER = 1;
    public static final int ADTYPE_TEXT = 2;
    private static final long IMPRESSION_COUNT_INTERVAL_NANOSECONDS = 2000000000;
    private static final String LOG_TAG = "AdlantisAd";
    private static final long NANOSECONDS_IN_SECOND = 1000000000;
    private static final long serialVersionUID = -94783938293849L;
    protected Handler _impressionHandler;
    protected boolean _isBeingViewed;
    protected boolean _sendImpressionCountFailed;
    protected boolean _sendingCountExpand;
    protected boolean _sendingImpressionCount;
    protected boolean _sentCountExpand;
    protected boolean _sentImpressionCount;
    protected long _viewStartTime;
    protected long _viewedTime;

    public AdlantisAd(HashMap<String, Object> map) {
        super(map);
        this._sendImpressionCountFailed = false;
    }

    public AdlantisAd(JSONObject jSONObject) {
        this(jsonObjectToHashMap(jSONObject));
    }

    public static AdlantisAd[] adsFromJSONInputStream(InputStream inputStream) {
        return adsFromJSONString(AdlantisUtils.convertInputToString(inputStream));
    }

    public static AdlantisAd[] adsFromJSONString(String str) {
        AdlantisAd[] adlantisAdArr = null;
        try {
            JSONArray jSONArrayExtractJSONAdArray = extractJSONAdArray(str);
            if (jSONArrayExtractJSONAdArray != null) {
                adlantisAdArr = new AdlantisAd[jSONArrayExtractJSONAdArray.length()];
                for (int i = 0; i < jSONArrayExtractJSONAdArray.length(); i++) {
                    adlantisAdArr[i] = new AdlantisAd(jSONArrayExtractJSONAdArray.getJSONObject(i));
                }
            } else {
                Log.i(LOG_TAG, "Adlantis: no ads received (this is not an error)");
            }
        } catch (Exception e) {
            logError("exception parsing JSON data " + e);
        }
        return adlantisAdArr;
    }

    private Uri buildURIFrom(Context context, String str) {
        if (str == null) {
            return null;
        }
        return AdManager.getInstance().getAdNetworkConnection().defaultRequestBuilder(context, Uri.parse(str)).build();
    }

    private Uri buildURIFromProperty(Context context, String str) {
        return buildURIFrom(context, (String) get(str));
    }

    private int currentOrientation(View view) {
        return view.getResources().getConfiguration().orientation;
    }

    private static JSONArray extractJSONAdArray(String str) {
        JSONArray jSONArray;
        try {
            jSONArray = new JSONArray(str);
        } catch (JSONException e) {
            jSONArray = null;
        }
        if (jSONArray != null) {
            return jSONArray;
        }
        try {
            return new JSONObject(str).getJSONArray("ads");
        } catch (JSONException e2) {
            return jSONArray;
        }
    }

    private boolean hasHighResolutionDisplay(Context context) {
        return AdlantisUtils.hasHighResolutionDisplay(context);
    }

    private static String iphone_orientationKey(int i) {
        return orientationIsLandscape(i) ? "iphone_landscape" : "iphone_portrait";
    }

    protected static HashMap<String, Object> jsonObjectToHashMap(JSONObject jSONObject) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            try {
                Object objJsonObjectToHashMap = jSONObject.get(next);
                if (objJsonObjectToHashMap instanceof JSONObject) {
                    objJsonObjectToHashMap = jsonObjectToHashMap((JSONObject) objJsonObjectToHashMap);
                }
                map.put(next, objJsonObjectToHashMap);
            } catch (JSONException e) {
                logDebug("exception parsing object ex = " + e);
            }
        }
        return map;
    }

    protected static void logDebug(String str) {
        Log.d(LOG_TAG, str);
    }

    protected static void logError(String str) {
        Log.e(LOG_TAG, str);
    }

    protected static void logWarn(String str) {
        Log.w(LOG_TAG, str);
    }

    private static boolean orientationIsLandscape(int i) {
        return i == 2;
    }

    private static String orientationKey(int i) {
        return orientationIsLandscape(i) ? "landscape" : "portrait";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSendImpressionCountFailed(boolean z) {
        this._sendImpressionCountFailed = z;
        this._sendingImpressionCount = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSendingImpressionCount(boolean z) {
        this._sendingImpressionCount = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSentImpressionCount(boolean z) {
        this._sentImpressionCount = z;
        this._sendingImpressionCount = false;
    }

    public int adType() {
        return "sp_banner".compareTo((String) get("type")) == 0 ? 1 : 2;
    }

    public String altTextString(int i) {
        Map<?, ?> mapBannerInfoForOrientation = bannerInfoForOrientation(i);
        if (mapBannerInfoForOrientation == null) {
            return null;
        }
        String str = (String) mapBannerInfoForOrientation.get("alt");
        return str != null ? Uri.decode(str) : str;
    }

    public String altTextString(View view) {
        Map<?, ?> mapBannerInfoForCurrentOrientation = bannerInfoForCurrentOrientation(view);
        if (mapBannerInfoForCurrentOrientation == null) {
            return null;
        }
        String str = (String) mapBannerInfoForCurrentOrientation.get("alt");
        return str != null ? Uri.decode(str) : str;
    }

    public Map<?, ?> bannerInfoForCurrentOrientation(View view) {
        if (adType() == 1) {
            return bannerInfoForOrientation(currentOrientation(view));
        }
        return null;
    }

    public Map<?, ?> bannerInfoForOrientation(int i) {
        if (adType() == 1) {
            Object obj = (Map) get(orientationKey(i));
            if (obj == null) {
                obj = get(iphone_orientationKey(i));
            }
            if (obj instanceof Map) {
                return (Map) obj;
            }
        }
        return null;
    }

    public String bannerURLForCurrentOrientation(View view) {
        return bannerURLForOrientation(currentOrientation(view), view.getContext());
    }

    public String bannerURLForOrientation(int i, Context context) {
        return bannerURLForOrientation(i, hasHighResolutionDisplay(context));
    }

    public String bannerURLForOrientation(int i, boolean z) {
        Map<?, ?> mapBannerInfoForOrientation;
        if (adType() != 1 || (mapBannerInfoForOrientation = bannerInfoForOrientation(i)) == null) {
            return null;
        }
        String str = z ? (String) mapBannerInfoForOrientation.get("src_2x") : null;
        return str == null ? (String) mapBannerInfoForOrientation.get("src") : str;
    }

    protected void clearImpressionHandler() {
        this._impressionHandler = null;
    }

    public Uri countExpandUri(Context context) {
        return buildURIFromProperty(context, "count_expand");
    }

    public Uri countImpressionUri(Context context) {
        return buildURIFromProperty(context, "count_impression");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [jp.adlantis.android.AdlantisAd$2] */
    protected void doSendImpressionCountThread() {
        new Thread() { // from class: jp.adlantis.android.AdlantisAd.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                boolean zSendRequestForProperty = AdlantisAd.this.sendRequestForProperty("count_impression", "sendImpressionCount");
                AdlantisAd.this.setSendingImpressionCount(false);
                if (zSendRequestForProperty) {
                    AdlantisAd.this.setSentImpressionCount(true);
                } else {
                    AdlantisAd.this.setSendImpressionCountFailed(true);
                }
            }
        }.start();
    }

    public boolean hasAdForOrientation(int i) {
        if (adType() == 2) {
            return true;
        }
        return adType() == 1 && bannerInfoForOrientation(i) != null;
    }

    public HashMap<String, Object> hashMapRepresentation() {
        return new HashMap<>(this);
    }

    protected AbstractHttpClient httpClientFactory() {
        return new DefaultHttpClient();
    }

    public String iconURL(View view) {
        return iconURL(hasHighResolutionDisplay(view.getContext()));
    }

    public String iconURL(boolean z) {
        Map map;
        if (adType() != 2 || (map = (Map) get("iphone_icon")) == null) {
            return null;
        }
        String str = z ? (String) map.get("src_2x") : null;
        return str == null ? (String) map.get("src") : str;
    }

    public String imageURL(int i, boolean z) {
        int iAdType = adType();
        if (iAdType == 2) {
            return iconURL(z);
        }
        if (iAdType == 1) {
            return bannerURLForOrientation(i, z);
        }
        return null;
    }

    public String imageURL(View view) {
        return bannerURLForOrientation(currentOrientation(view), hasHighResolutionDisplay(view.getContext()));
    }

    protected long impressionCountIntervalMilliseconds() {
        return TimeUnit.NANOSECONDS.toMillis(impressionCountIntervalNanoseconds());
    }

    protected long impressionCountIntervalNanoseconds() {
        return IMPRESSION_COUNT_INTERVAL_NANOSECONDS;
    }

    protected boolean impressionCountIntervalPassed() {
        return viewedTime() >= impressionCountIntervalNanoseconds();
    }

    boolean isRedirectingUrl(String str) {
        return ADLStringUtils.isHttpUrl(tapUrlString()) && str.indexOf("url=") != -1;
    }

    public boolean isWebLink() {
        return "web".equals(linkType());
    }

    public String linkType() {
        return (String) get("link_type");
    }

    protected void sendImpressionCount() {
        if (shouldSendImpressionCount()) {
            setSendingImpressionCount(true);
            doSendImpressionCountThread();
        }
    }

    protected boolean sendRequestForProperty(String str, String str2) {
        try {
            String string = buildURIFromProperty(null, str).toString();
            if (string == null) {
                return false;
            }
            int statusCode = httpClientFactory().execute(new HttpGet(string)).getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 400) {
                return true;
            }
            logError(str2 + " status=" + statusCode);
            return false;
        } catch (MalformedURLException e) {
            logError(str2 + " exception=" + e.toString());
            return false;
        } catch (IOException e2) {
            logError(str2 + " exception=" + e2.toString());
            return false;
        } catch (OutOfMemoryError e3) {
            logError(str2 + " OutOfMemoryError=" + e3.toString());
            return false;
        }
    }

    public boolean shouldHandleRedirect() {
        return isRedirectingUrl(tapUrlString()) && ("appstore".equals(linkType()) || "itunes".equals(linkType()));
    }

    protected boolean shouldSendImpressionCount() {
        return (this._sentImpressionCount || this._sendingImpressionCount || this._sendImpressionCountFailed) ? false : true;
    }

    public String tapUriRedirect() {
        Uri.Builder builderBuildUpon = Uri.parse(tapUrlString()).buildUpon();
        builderBuildUpon.appendQueryParameter("adlDoRedirect", "1");
        return builderBuildUpon.toString();
    }

    public String tapUrlString() {
        return (String) get("href");
    }

    public String textAdString() {
        return Uri.decode((String) get("string"));
    }

    public String urlString() {
        return tapUrlString();
    }

    protected long viewedTime() {
        if (this._isBeingViewed) {
            long jNanoTime = System.nanoTime();
            this._viewedTime += jNanoTime - this._viewStartTime;
            this._viewStartTime = jNanoTime;
        }
        return this._viewedTime;
    }

    public void viewingEnded() {
        if (!this._isBeingViewed) {
            logWarn("viewingEnded() called without matching viewingStarted()");
        }
        this._isBeingViewed = false;
        clearImpressionHandler();
        if (impressionCountIntervalPassed()) {
            sendImpressionCount();
        }
    }

    public void viewingStarted() {
        this._viewStartTime = System.nanoTime();
        this._isBeingViewed = true;
        if (shouldSendImpressionCount()) {
            this._impressionHandler = new Handler(Looper.getMainLooper());
            this._impressionHandler.postDelayed(new Runnable() { // from class: jp.adlantis.android.AdlantisAd.1
                @Override // java.lang.Runnable
                public void run() {
                    if (AdlantisAd.this.impressionCountIntervalPassed()) {
                        AdlantisAd.this.sendImpressionCount();
                    }
                }
            }, impressionCountIntervalMilliseconds());
        }
    }
}

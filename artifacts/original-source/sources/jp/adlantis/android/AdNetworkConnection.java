package jp.adlantis.android;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import jp.adlantis.android.AdService;
import jp.adlantis.android.utils.AdlantisUtils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class AdNetworkConnection {
    protected String _conversionTagHost;
    protected String _conversionTagTestHost;
    private HashMap<String, String> _defaultParamMap;
    protected String _host;
    private int _testAdRequestUrlIndex;
    private String[] _testAdRequestUrls;

    private Uri adRequestURI_internal(AdManager adManager, Context context, Map<String, String> map) {
        Uri.Builder builderDefaultRequestBuilder = defaultRequestBuilder(context, null);
        builderDefaultRequestBuilder.scheme("http");
        builderDefaultRequestBuilder.authority(getHost());
        builderDefaultRequestBuilder.path("/sp/load_app_ads");
        builderDefaultRequestBuilder.appendQueryParameter("callbackid", "0");
        builderDefaultRequestBuilder.appendQueryParameter("zid", adManager.getPublisherID());
        builderDefaultRequestBuilder.appendQueryParameter("adl_app_flg", "1");
        if (adManager.keywords() != null) {
            builderDefaultRequestBuilder.appendQueryParameter("keywords", adManager.keywords());
        }
        if (map != null) {
            AdlantisUtils.setUriParamsFromMap(builderDefaultRequestBuilder, map);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        builderDefaultRequestBuilder.appendQueryParameter("displaySize", displayMetrics.widthPixels + "x" + displayMetrics.heightPixels);
        builderDefaultRequestBuilder.appendQueryParameter("displayDensity", Float.toString(displayMetrics.density));
        return builderDefaultRequestBuilder.build();
    }

    private Uri.Builder appendTargetingParameters(Uri.Builder builder) {
        AdService.TargetingParams targetingParam = AdManager.getInstance().getTargetingParam();
        String country = targetingParam.getCountry();
        if (country != null) {
            builder.appendQueryParameter("country", country);
        }
        String locale = targetingParam.getLocale();
        if (locale != null) {
            builder.appendQueryParameter("locale", locale);
        }
        Location location = targetingParam.getLocation();
        if (location != null) {
            builder.appendQueryParameter("lat", Double.toString(location.getLatitude()));
            builder.appendQueryParameter("lng", Double.toString(location.getLongitude()));
        }
        return builder;
    }

    private HashMap<String, String> defaultParameters(Context context) {
        synchronized (this) {
            if (this._defaultParamMap != null) {
                return this._defaultParamMap;
            }
            this._defaultParamMap = new HashMap<>();
            if (context != null) {
                this._defaultParamMap.put("appIdentifier", context.getPackageName());
            }
            this._defaultParamMap.put("deviceClass", "android");
            String str = Build.VERSION.RELEASE;
            if (str != null) {
                this._defaultParamMap.put("deviceOsVersionFull", str);
                try {
                    this._defaultParamMap.put("deviceOsVersion", NumberFormat.getNumberInstance().parse(str).toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String str2 = Build.MODEL;
            if (str2 != null) {
                if (str2.compareTo("sdk") == 0) {
                    str2 = "simulator";
                }
                this._defaultParamMap.put("deviceFamily", str2);
            }
            if (Build.BRAND != null) {
                this._defaultParamMap.put("deviceBrand", Build.BRAND);
            }
            if (Build.DEVICE != null) {
                this._defaultParamMap.put("deviceName", Build.DEVICE);
            }
            String strMd5_uniqueID = md5_uniqueID(context);
            if (strMd5_uniqueID != null) {
                this._defaultParamMap.put("udid", strMd5_uniqueID);
            }
            this._defaultParamMap.put("sdkVersion", sdkVersion());
            this._defaultParamMap.put("sdkBuild", sdkBuild());
            this._defaultParamMap.put("adlProtocolVersion", "2");
            return this._defaultParamMap;
        }
    }

    public Uri adRequestUri(AdManager adManager, Context context, Map<String, String> map) {
        if (this._testAdRequestUrls == null || this._testAdRequestUrls.length <= 0) {
            return adRequestURI_internal(adManager, context, map);
        }
        adRequestURI_internal(adManager, context, map);
        Uri uri = Uri.parse(this._testAdRequestUrls[this._testAdRequestUrlIndex]);
        this._testAdRequestUrlIndex = (this._testAdRequestUrlIndex + 1) % this._testAdRequestUrls.length;
        return uri;
    }

    public String androidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public Uri.Builder appendParameters(Uri.Builder builder) {
        return builder;
    }

    public String buildCompleteHttpUri(Context context, String str) {
        return defaultRequestBuilder(context, Uri.parse(str)).build().toString();
    }

    public Uri conversionTagRequestUri(Context context, String str, boolean z) {
        Uri.Builder builderDefaultRequestBuilder = defaultRequestBuilder(context, null);
        builderDefaultRequestBuilder.scheme("http");
        if (z) {
            builderDefaultRequestBuilder.authority(getConversionTagTestHost());
            builderDefaultRequestBuilder.path("/ctt");
        } else {
            builderDefaultRequestBuilder.authority(getConversionTagHost());
            builderDefaultRequestBuilder.path("/sp/conv");
        }
        builderDefaultRequestBuilder.appendQueryParameter("tid", str);
        builderDefaultRequestBuilder.appendQueryParameter("output", "js");
        return builderDefaultRequestBuilder.build();
    }

    public Uri.Builder defaultRequestBuilder(Context context, Uri uri) {
        Uri.Builder builderBuildUpon = uri != null ? uri.buildUpon() : new Uri.Builder();
        AdlantisUtils.setUriParamsFromMap(builderBuildUpon, defaultParameters(context));
        return appendParameters(appendTargetingParameters(builderBuildUpon));
    }

    protected String deviceId(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return null;
        }
    }

    public String getConversionTagHost() {
        return this._conversionTagHost;
    }

    public String getConversionTagTestHost() {
        return this._conversionTagTestHost;
    }

    public String getHost() {
        return this._host;
    }

    public int getPort() {
        return 80;
    }

    boolean hasTestAdRequestUrls() {
        return this._testAdRequestUrls != null;
    }

    public String md5_uniqueID(Context context) {
        String strUniqueID;
        if (context == null || (strUniqueID = uniqueID(context)) == null) {
            return null;
        }
        return AdlantisUtils.md5(strUniqueID);
    }

    public abstract String publisherIDMetadataKey();

    public String sdkBuild() {
        return AdManager.getInstance().sdkBuild();
    }

    public String sdkVersion() {
        return AdManager.getInstance().sdkVersion();
    }

    public void setHost(String str) {
        this._host = str;
    }

    public void setTestAdRequestUrls(String[] strArr) {
        this._testAdRequestUrls = strArr;
    }

    public String uniqueID(Context context) {
        String strAndroidId = androidId(context);
        return strAndroidId == null ? deviceId(context) : strAndroidId;
    }
}

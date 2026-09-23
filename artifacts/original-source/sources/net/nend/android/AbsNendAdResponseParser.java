package net.nend.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.SparseArray;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
abstract class AbsNendAdResponseParser<T> {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected static final String RESPONSE_ENCODING = "UTF-8";
    private final PackageManager mPackageManager;

    abstract T getResponseObject(ResponseType responseType, JSONObject jSONObject) throws JSONException, NendException;

    static {
        $assertionsDisabled = !AbsNendAdResponseParser.class.desiredAssertionStatus();
    }

    protected static final class JsonParam {
        protected static final String CLICK_URL = "click_url";
        protected static final String CONDITIONS = "conditions";
        protected static final String DEFAULT_AD = "default_ad";
        protected static final String DEFAULT_ADS = "default_ads";
        protected static final String HEIGHT = "height";
        protected static final String ICON_ID = "icon_id";
        protected static final String IMAGE_URL = "image_url";
        protected static final String IMPRESSHON_COUNT_URL = "impression_count_url";
        protected static final String LOGICAL_OPERATOR = "logical_operator";
        protected static final String MESSAGE = "message";
        protected static final String PACKAGE_NAME = "url_scheme";
        protected static final String RELOAD = "reload";
        protected static final String RESPONSE_TYPE = "response_type";
        protected static final String STATUS_CODE = "status_code";
        protected static final String TARGETING_ADS = "targeting_ads";
        protected static final String TITLE_TEXT = "icon_text";
        protected static final String WEB_VIEW_URL = "web_view_url";
        protected static final String WIDTH = "width";

        private JsonParam() {
        }
    }

    protected enum ResponseType {
        UNSUPPORTED(0),
        BANNER_NORMAL(1),
        BANNER_WEB_VIEW(2),
        BANNER_APP_TARGETING(3),
        ICON_NORMAL(11),
        ICON_APP_TARGETING(13);

        private static final SparseArray<ResponseType> intToEnum = new SparseArray<>();
        private int type;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static ResponseType[] valuesCustom() {
            ResponseType[] responseTypeArrValuesCustom = values();
            int length = responseTypeArrValuesCustom.length;
            ResponseType[] responseTypeArr = new ResponseType[length];
            System.arraycopy(responseTypeArrValuesCustom, 0, responseTypeArr, 0, length);
            return responseTypeArr;
        }

        static {
            for (ResponseType responseType : valuesCustom()) {
                intToEnum.put(responseType.type, responseType);
            }
        }

        ResponseType(int type) {
            this.type = type;
        }

        protected static ResponseType valueOf(int type) {
            return intToEnum.get(type, UNSUPPORTED);
        }
    }

    public AbsNendAdResponseParser(Context context) {
        if (context == null) {
            throw new NullPointerException(NendStatus.ERR_INVALID_CONTEXT.getMsg());
        }
        this.mPackageManager = context.getPackageManager();
    }

    T parseResponse(String responseStr) throws NendException, UnsupportedEncodingException {
        if (responseStr != null) {
            try {
                if (responseStr.length() != 0) {
                    String decodedResposeStr = URLDecoder.decode(responseStr, RESPONSE_ENCODING);
                    JSONObject responseJson = new JSONObject(decodedResposeStr);
                    if (responseJson.getInt("status_code") != NendStatus.SUCCESS.getCode()) {
                        throw new NendException(NendStatus.ERR_INVALID_AD_STATUS, "Ad status : " + responseJson.getInt("status_code") + ", Message : " + responseJson.getString("message"));
                    }
                    ResponseType responseType = ResponseType.valueOf(responseJson.getInt("response_type"));
                    return getResponseObject(responseType, responseJson);
                }
            } catch (UnsupportedEncodingException e) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return null;
            } catch (IllegalArgumentException e2) {
                NendLog.w(NendStatus.ERR_FAILED_TO_PARSE, e2);
                return null;
            } catch (NendException e3) {
                NendLog.w(NendStatus.ERR_FAILED_TO_PARSE, e3);
                return null;
            } catch (JSONException e4) {
                NendLog.w(NendStatus.ERR_FAILED_TO_PARSE, e4);
                return null;
            }
        }
        throw new IllegalArgumentException(NendStatus.ERR_INVALID_RESPONSE.getMsg());
    }

    protected boolean isTarget(JSONArray ruleArray) throws JSONException, PackageManager.NameNotFoundException {
        if (!$assertionsDisabled && ruleArray == null) {
            throw new AssertionError();
        }
        int end = ruleArray.length();
        for (int i = 0; i < end; i++) {
            JSONObject rule = ruleArray.getJSONObject(i);
            int logicalOperator = rule.getInt("logical_operator");
            if (logicalOperator == 1) {
                try {
                    this.mPackageManager.getPackageInfo(rule.getString("url_scheme"), 1);
                } catch (PackageManager.NameNotFoundException e) {
                    return false;
                }
            } else {
                if (logicalOperator != 2) {
                    return false;
                }
                try {
                    this.mPackageManager.getPackageInfo(rule.getString("url_scheme"), 1);
                    return false;
                } catch (PackageManager.NameNotFoundException e2) {
                }
            }
        }
        return true;
    }
}

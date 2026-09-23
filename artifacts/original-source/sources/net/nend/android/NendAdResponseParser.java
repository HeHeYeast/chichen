package net.nend.android;

import android.content.Context;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import net.nend.android.AbsNendAdResponseParser;
import net.nend.android.AdParameter;
import net.nend.android.NendAdResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendAdResponseParser extends AbsNendAdResponseParser<AdParameter> {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AbsNendAdResponseParser$ResponseType;

    static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AbsNendAdResponseParser$ResponseType() {
        int[] iArr = $SWITCH_TABLE$net$nend$android$AbsNendAdResponseParser$ResponseType;
        if (iArr == null) {
            iArr = new int[AbsNendAdResponseParser.ResponseType.valuesCustom().length];
            try {
                iArr[AbsNendAdResponseParser.ResponseType.BANNER_APP_TARGETING.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AbsNendAdResponseParser.ResponseType.BANNER_NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AbsNendAdResponseParser.ResponseType.BANNER_WEB_VIEW.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[AbsNendAdResponseParser.ResponseType.ICON_APP_TARGETING.ordinal()] = 6;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[AbsNendAdResponseParser.ResponseType.ICON_NORMAL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[AbsNendAdResponseParser.ResponseType.UNSUPPORTED.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$net$nend$android$AbsNendAdResponseParser$ResponseType = iArr;
        }
        return iArr;
    }

    public NendAdResponseParser(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // net.nend.android.AbsNendAdResponseParser
    public AdParameter getResponseObject(AbsNendAdResponseParser.ResponseType responseType, JSONObject responseJson) throws JSONException, NendException {
        switch ($SWITCH_TABLE$net$nend$android$AbsNendAdResponseParser$ResponseType()[responseType.ordinal()]) {
            case 2:
                return getNormalAd(responseJson);
            case 3:
                return getWebViewAd(responseJson);
            case 4:
                return getAppTargetingAd(responseJson);
            default:
                throw new NendException(NendStatus.ERR_INVALID_RESPONSE_TYPE);
        }
    }

    private AdParameter getNormalAd(JSONObject response) throws JSONException {
        JSONObject defaultAd = response.getJSONObject("default_ad");
        NendAdResponse.Builder builder = new NendAdResponse.Builder().setViewType(AdParameter.ViewType.ADVIEW).setImageUrl(defaultAd.getString("image_url")).setClickUrl(defaultAd.getString("click_url")).setHeight(response.getInt(FluctConstants.XML_NODE_HEIGHT)).setWidth(response.getInt(FluctConstants.XML_NODE_WIDTH));
        if (!response.isNull("reload")) {
            builder.setReloadIntervalInSeconds(response.getInt("reload"));
        }
        return builder.build();
    }

    private AdParameter getWebViewAd(JSONObject response) throws JSONException {
        return new NendAdResponse.Builder().setViewType(AdParameter.ViewType.WEBVIEW).setWebViewUrl(response.getString("web_view_url")).setHeight(response.getInt(FluctConstants.XML_NODE_HEIGHT)).setWidth(response.getInt(FluctConstants.XML_NODE_WIDTH)).build();
    }

    private AdParameter getAppTargetingAd(JSONObject response) throws JSONException, NendException {
        JSONArray targetingAdArray = response.getJSONArray("targeting_ads");
        int iEnd = targetingAdArray.length();
        for (int i = 0; i < iEnd; i++) {
            JSONObject targetingAd = targetingAdArray.getJSONObject(i);
            JSONArray conditionArray = targetingAd.getJSONArray("conditions");
            int jEnd = conditionArray.length();
            for (int j = 0; j < jEnd; j++) {
                if (isTarget(conditionArray.getJSONArray(j))) {
                    NendAdResponse.Builder builder = new NendAdResponse.Builder().setViewType(AdParameter.ViewType.ADVIEW).setImageUrl(targetingAd.getString("image_url")).setClickUrl(targetingAd.getString("click_url")).setHeight(response.getInt(FluctConstants.XML_NODE_HEIGHT)).setWidth(response.getInt(FluctConstants.XML_NODE_WIDTH));
                    if (!response.isNull("reload")) {
                        builder.setReloadIntervalInSeconds(response.getInt("reload"));
                    }
                    return builder.build();
                }
            }
        }
        if (response.isNull("default_ad")) {
            throw new NendException(NendStatus.ERR_OUT_OF_STOCK);
        }
        return getNormalAd(response);
    }
}

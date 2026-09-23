package net.nend.android;

import android.content.Context;
import java.util.ArrayList;
import net.nend.android.AbsNendAdResponseParser;
import net.nend.android.AdParameter;
import net.nend.android.NendAdIconResponse;
import net.nend.android.NendAdResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendAdIconResponseParser extends AbsNendAdResponseParser<NendAdIconResponse> {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AbsNendAdResponseParser$ResponseType;
    private final int mIconViewCount;

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

    NendAdIconResponseParser(Context context, int count) {
        super(context);
        this.mIconViewCount = count;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // net.nend.android.AbsNendAdResponseParser
    public NendAdIconResponse getResponseObject(AbsNendAdResponseParser.ResponseType responseType, JSONObject responseJson) throws JSONException, NendException {
        switch ($SWITCH_TABLE$net$nend$android$AbsNendAdResponseParser$ResponseType()[responseType.ordinal()]) {
            case 5:
                return getNormalAd(responseJson);
            case 6:
                return getAppTargetingAd(responseJson);
            default:
                throw new NendException(NendStatus.ERR_INVALID_RESPONSE_TYPE);
        }
    }

    private NendAdIconResponse getNormalAd(JSONObject response) throws JSONException, NendException {
        ArrayList<AdParameter> adList = new ArrayList<>();
        NendAdIconResponse.Builder iconBuilder = new NendAdIconResponse.Builder();
        JSONArray defaultAdArray = response.getJSONArray("default_ads");
        for (int i = 0; i < defaultAdArray.length(); i++) {
            JSONObject defaultAd = defaultAdArray.getJSONObject(i);
            NendAdResponse.Builder builder = new NendAdResponse.Builder().setViewType(AdParameter.ViewType.ADVIEW).setIconId(defaultAd.getString("icon_id")).setImageUrl(defaultAd.getString("image_url")).setClickUrl(defaultAd.getString("click_url"));
            if (!defaultAd.isNull("icon_text")) {
                builder.setTitleText(defaultAd.getString("icon_text"));
            }
            adList.add(builder.build());
            if (adList.size() >= this.mIconViewCount) {
                break;
            }
        }
        if (adList.size() == 0) {
            throw new NendException(NendStatus.ERR_OUT_OF_STOCK);
        }
        iconBuilder.setAdParameterList(adList);
        if (!response.isNull("status_code")) {
            iconBuilder.setStatusCode(response.getInt("status_code"));
        }
        if (!response.isNull("message")) {
            iconBuilder.setMessage(response.getString("message"));
        }
        if (!response.isNull("reload")) {
            iconBuilder.setReloadIntervalInSeconds(response.getInt("reload"));
        }
        if (!response.isNull("impression_count_url")) {
            iconBuilder.setImpressionCountUrl(response.getString("impression_count_url"));
        }
        iconBuilder.setViewType(AdParameter.ViewType.ADVIEW);
        return iconBuilder.build();
    }

    private NendAdIconResponse getAppTargetingAd(JSONObject response) throws JSONException, NendException {
        ArrayList<AdParameter> adList = new ArrayList<>();
        NendAdIconResponse.Builder iconBuilder = new NendAdIconResponse.Builder();
        JSONArray targetingAdArray = response.getJSONArray("targeting_ads");
        int iEnd = targetingAdArray.length();
        for (int i = 0; i < iEnd; i++) {
            JSONObject targetingAd = targetingAdArray.getJSONObject(i);
            JSONArray conditionArray = targetingAd.getJSONArray("conditions");
            int j = 0;
            int jEnd = conditionArray.length();
            while (true) {
                if (j >= jEnd) {
                    break;
                }
                if (!isTarget(conditionArray.getJSONArray(j))) {
                    j++;
                } else {
                    NendAdResponse.Builder builder = new NendAdResponse.Builder().setViewType(AdParameter.ViewType.ADVIEW).setIconId(targetingAd.getString("icon_id")).setImageUrl(targetingAd.getString("image_url")).setClickUrl(targetingAd.getString("click_url"));
                    if (!targetingAd.isNull("icon_text")) {
                        builder.setTitleText(targetingAd.getString("icon_text"));
                    }
                    adList.add(builder.build());
                }
            }
            if (adList.size() >= this.mIconViewCount) {
                break;
            }
        }
        if (adList.size() < this.mIconViewCount && !response.isNull("default_ads")) {
            JSONArray defaultAdArray = response.getJSONArray("default_ads");
            for (int i2 = 0; i2 < defaultAdArray.length(); i2++) {
                JSONObject defaultAd = defaultAdArray.getJSONObject(i2);
                NendAdResponse.Builder builder2 = new NendAdResponse.Builder().setViewType(AdParameter.ViewType.ADVIEW).setIconId(defaultAd.getString("icon_id")).setImageUrl(defaultAd.getString("image_url")).setClickUrl(defaultAd.getString("click_url"));
                if (!defaultAd.isNull("icon_text")) {
                    builder2.setTitleText(defaultAd.getString("icon_text"));
                }
                adList.add(builder2.build());
                if (adList.size() >= this.mIconViewCount) {
                    break;
                }
            }
        }
        if (adList.size() == 0) {
            throw new NendException(NendStatus.ERR_OUT_OF_STOCK);
        }
        iconBuilder.setAdParameterList(adList);
        if (!response.isNull("status_code")) {
            iconBuilder.setStatusCode(response.getInt("status_code"));
        }
        if (!response.isNull("message")) {
            iconBuilder.setMessage(response.getString("message"));
        }
        if (!response.isNull("reload")) {
            iconBuilder.setReloadIntervalInSeconds(response.getInt("reload"));
        }
        if (!response.isNull("impression_count_url")) {
            iconBuilder.setImpressionCountUrl(response.getString("impression_count_url"));
        }
        iconBuilder.setViewType(AdParameter.ViewType.ADVIEW);
        return iconBuilder.build();
    }
}

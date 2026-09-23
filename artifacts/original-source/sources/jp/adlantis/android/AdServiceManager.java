package jp.adlantis.android;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.util.Locale;
import jp.adlantis.android.AdService;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdServiceManager {
    ADLAdService adlAdService;
    InternationalAdService internationalAdService;

    private String guessCountry(Context context) {
        if (getTargetingParams().getCountry() != null) {
            return getTargetingParams().getCountry();
        }
        String userCountry = GreeApiDelegator.getUserCountry();
        if (userCountry != null) {
            return userCountry;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            String simCountryIso = telephonyManager.getSimCountryIso();
            if (simCountryIso != null && !"".equals(simCountryIso)) {
                return simCountryIso;
            }
            String networkCountryIso = telephonyManager.getNetworkCountryIso();
            if (networkCountryIso != null && !"".equals(networkCountryIso)) {
                return networkCountryIso;
            }
        }
        Locale locale = Locale.getDefault();
        return locale != null ? locale.getCountry() : "JP";
    }

    public void addService(AdService adService) {
        if (adService instanceof ADLAdService) {
            setAdlAdService((ADLAdService) adService);
        } else {
            setInternationalAdService((InternationalAdService) adService);
        }
    }

    public AdService getActiveAdService(Context context) {
        if (isGreeSdk() && getInternationalAdService() != null) {
            String strGuessCountry = guessCountry(context);
            return ("JP".equalsIgnoreCase(strGuessCountry) || "JPN".equalsIgnoreCase(strGuessCountry)) ? getAdlAdService() : getInternationalAdService();
        }
        return getAdlAdService();
    }

    ADLAdService getAdlAdService() {
        return this.adlAdService;
    }

    InternationalAdService getInternationalAdService() {
        return this.internationalAdService;
    }

    AdService.TargetingParams getTargetingParams() {
        return AdManager.getInstance().getTargetingParam();
    }

    boolean isGreeSdk() {
        return AdManager.isGreeSdk();
    }

    void setAdlAdService(ADLAdService aDLAdService) {
        this.adlAdService = aDLAdService;
    }

    void setInternationalAdService(InternationalAdService internationalAdService) {
        this.internationalAdService = internationalAdService;
    }
}

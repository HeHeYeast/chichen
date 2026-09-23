package jp.adlantis.android;

import android.content.Context;
import android.location.Location;
import android.view.View;
import java.util.Locale;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class AdService {
    protected TargetingParams targetingParam = null;

    public static final class TargetingParams {
        private String country;
        private String keywords;
        private Location location;

        public String getCountry() {
            return this.country;
        }

        public String getKeywords() {
            return this.keywords;
        }

        public String getLocale() {
            Locale locale = Locale.getDefault();
            return locale != null ? locale.getLanguage() : "";
        }

        public Location getLocation() {
            return this.location;
        }

        public void setCountry(String str) {
            this.country = str != null ? str.toLowerCase() : null;
        }

        public void setKeywords(String str) {
            this.keywords = str;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public abstract AdViewAdapter adViewAdapter(Context context);

    public abstract View createAdView(Context context);

    public TargetingParams getTargetingParam() {
        return this.targetingParam;
    }

    public abstract void pause();

    public abstract void resume();

    public void setTargetingParam(TargetingParams targetingParams) {
        this.targetingParam = targetingParams;
    }
}

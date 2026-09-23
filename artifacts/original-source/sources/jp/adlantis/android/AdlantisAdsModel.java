package jp.adlantis.android;

import java.util.Vector;
import jp.adlantis.android.model.OnChangeListener;
import jp.adlantis.android.model.SimpleObservable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisAdsModel extends SimpleObservable<AdlantisAdsModel> {
    private AdlantisAd[] _ads;

    public interface AdlantisAdsModelListener extends OnChangeListener<AdlantisAdsModel> {
    }

    public AdlantisAdsModel() {
        this._ads = null;
    }

    public AdlantisAdsModel(AdlantisAd[] adlantisAdArr) {
        this._ads = null;
        this._ads = adlantisAdArr;
    }

    private AdlantisAd[] filteredAdsForOrientation(int i) {
        Vector<AdlantisAd> vectorFilteredAdsForOrientationVector = filteredAdsForOrientationVector(i);
        AdlantisAd[] adlantisAdArr = new AdlantisAd[vectorFilteredAdsForOrientationVector.size()];
        vectorFilteredAdsForOrientationVector.copyInto(adlantisAdArr);
        return adlantisAdArr;
    }

    public int adCount() {
        if (this._ads == null) {
            return 0;
        }
        return this._ads.length;
    }

    public int adCountForOrientation(int i) {
        return filteredAdsForOrientation(i).length;
    }

    public synchronized AdlantisAd[] adsForOrientation(int i) {
        return i == 2 ? filteredAdsForOrientation(2) : filteredAdsForOrientation(1);
    }

    public void clearAds() {
        setAds(null);
    }

    public synchronized Vector<AdlantisAd> filteredAdsForOrientationVector(int i) {
        Vector<AdlantisAd> vector;
        vector = new Vector<>();
        if (this._ads != null) {
            for (int i2 = 0; i2 < this._ads.length; i2++) {
                if (this._ads[i2].hasAdForOrientation(i)) {
                    vector.addElement(this._ads[i2]);
                }
            }
        }
        return vector;
    }

    public AdlantisAd[] getAds() {
        return this._ads;
    }

    protected void notifyListeners() {
        notifyListeners(this);
    }

    public void setAds(AdlantisAd[] adlantisAdArr) {
        synchronized (this) {
            this._ads = adlantisAdArr;
        }
        notifyListeners();
    }
}

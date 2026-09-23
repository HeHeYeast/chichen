package jp.adlantis.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;
import jp.adlantis.android.utils.AdlantisUtils;
import jp.adlantis.android.utils.AsyncImageLoader;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class AdlantisAdView extends ViewSwitcher {
    private static final float AD_TEXT_SIZE = 20.0f;
    private static final int BANNER_ALTTEXT_VIEW = 1;
    private static final int BANNER_IMAGE_VIEW = 0;
    private static final int BANNER_VIEW = 0;
    private static final float BYLINE_TEXT_SIZE = 12.0f;
    private static final float TEXTAD_ICON_DIMENSION = 32.0f;
    private static final int TEXTAD_VIEW = 1;
    private AdlantisAd _ad;
    private TextView _adAltText;
    private ImageView _adBanner;
    private ViewFlipper _adBannerViewFlipper;
    protected AdlantisAdsModel _adsModel;
    private SizeFitTextView _adtext;
    private ImageView _adtextIconView;

    public AdlantisAdView(Context context) {
        super(context);
        commonInitLayout();
    }

    public AdlantisAdView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        commonInitLayout();
    }

    private AdManager adManager() {
        return AdManager.getInstance();
    }

    private AdlantisAd[] adsForCurrentOrientation() {
        return adsModel().adsForOrientation(orientation());
    }

    private void commonInitLayout() {
        this._adBannerViewFlipper = new AdlantisViewFlipper(getContext());
        addView(this._adBannerViewFlipper, 0, new ViewGroup.LayoutParams(-1, -1));
        this._adBannerViewFlipper.setInAnimation(AdlantisAdViewContainer.fadeInAnimation());
        this._adBannerViewFlipper.setOutAnimation(AdlantisAdViewContainer.fadeOutAnimation());
        this._adBanner = createAdBannerView();
        this._adBannerViewFlipper.addView(this._adBanner, 0, new ViewGroup.LayoutParams(-1, -1));
        this._adAltText = createAdAltText();
        this._adBannerViewFlipper.addView(this._adAltText, 1, new ViewGroup.LayoutParams(-1, -1));
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        addView(relativeLayout, 1, new ViewGroup.LayoutParams(-1, -1));
        this._adtextIconView = new ImageView(getContext());
        relativeLayout.addView(this._adtextIconView, createAdTextIconViewLayout());
        this._adtext = createAdTextView();
        relativeLayout.addView(this._adtext, createAdTextRelativeLayout());
        relativeLayout.addView(createBylineTextView(), createBylineTextRelativeLayout());
    }

    private float displayDensity() {
        return AdlantisUtils.displayDensity(getContext());
    }

    private int orientation() {
        return AdlantisUtils.orientation(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBannerDrawable(Drawable drawable, boolean z) {
        View currentView = this._adBannerViewFlipper.getCurrentView();
        if (this._adBanner != null) {
            this._adBanner.setImageDrawable(drawable);
        }
        if (this._adBanner == currentView || drawable == null) {
            return;
        }
        if (z) {
            this._adBannerViewFlipper.showNext();
        } else {
            this._adBannerViewFlipper.setDisplayedChild(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIconDrawable(Drawable drawable) {
        if (this._adtextIconView != null) {
            this._adtextIconView.setImageDrawable(drawable);
        }
    }

    public AdlantisAdsModel adsModel() {
        return this._adsModel;
    }

    protected TextView createAdAltText() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(AD_TEXT_SIZE);
        textView.setTextColor(-1);
        textView.setGravity(17);
        return textView;
    }

    protected ImageView createAdBannerView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }

    protected RelativeLayout.LayoutParams createAdTextIconViewLayout() {
        float fDisplayDensity = displayDensity();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (TEXTAD_ICON_DIMENSION * fDisplayDensity), (int) (TEXTAD_ICON_DIMENSION * fDisplayDensity));
        layoutParams.addRule(15, -1);
        layoutParams.setMargins((int) (fDisplayDensity * 5.0f), 0, 0, 0);
        return layoutParams;
    }

    protected RelativeLayout.LayoutParams createAdTextRelativeLayout() {
        float fDisplayDensity = displayDensity();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(13, -1);
        layoutParams.addRule(9, -1);
        layoutParams.setMargins((int) (fDisplayDensity * 42.0f), 0, 0, 0);
        return layoutParams;
    }

    protected SizeFitTextView createAdTextView() {
        SizeFitTextView sizeFitTextView = new SizeFitTextView(getContext());
        sizeFitTextView.setTextSize(AD_TEXT_SIZE);
        sizeFitTextView.setTextColor(-1);
        sizeFitTextView.setLines(1);
        sizeFitTextView.setMaxLines(1);
        return sizeFitTextView;
    }

    protected RelativeLayout.LayoutParams createBylineTextRelativeLayout() {
        float fDisplayDensity = displayDensity();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(11, -1);
        layoutParams.addRule(12, -1);
        layoutParams.setMargins(0, 0, (int) (4.0f * fDisplayDensity), (int) (fDisplayDensity * 1.0f));
        return layoutParams;
    }

    protected TextView createBylineTextView() {
        TextView textView = new TextView(getContext());
        textView.setText(adManager().byline());
        textView.setTextSize(BYLINE_TEXT_SIZE);
        textView.setTextColor(-1);
        return textView;
    }

    protected Drawable loadBannerDrawable() {
        Drawable drawableLoadDrawable = loadDrawable(this._ad.bannerURLForCurrentOrientation(this), new AsyncImageLoader.ImageLoadedCallback() { // from class: jp.adlantis.android.AdlantisAdView.2
            @Override // jp.adlantis.android.utils.AsyncImageLoader.ImageLoadedCallback
            public void imageLoaded(Drawable drawable, String str) {
                if (drawable != null) {
                    AdlantisAdView.this.setBannerDrawable(drawable, true);
                }
            }
        });
        setBannerDrawable(drawableLoadDrawable, false);
        return drawableLoadDrawable;
    }

    protected Drawable loadDrawable(String str, AsyncImageLoader.ImageLoadedCallback imageLoadedCallback) {
        return adManager().asyncImageLoader().loadDrawable(getContext(), str, imageLoadedCallback);
    }

    protected Drawable loadIconDrawable() {
        Drawable drawableLoadDrawable = loadDrawable(this._ad.iconURL(this), new AsyncImageLoader.ImageLoadedCallback() { // from class: jp.adlantis.android.AdlantisAdView.1
            @Override // jp.adlantis.android.utils.AsyncImageLoader.ImageLoadedCallback
            public void imageLoaded(Drawable drawable, String str) {
                AdlantisAdView.this.setIconDrawable(drawable);
            }
        });
        setIconDrawable(drawableLoadDrawable);
        return drawableLoadDrawable;
    }

    protected void logD(String str) {
        Log.d(getClass().getSimpleName(), str);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setupAdDisplay();
    }

    public void setAdByIndex(int i) {
        int length;
        AdlantisAd[] adlantisAdArrAdsForCurrentOrientation = adsForCurrentOrientation();
        if (adlantisAdArrAdsForCurrentOrientation == null || (length = adlantisAdArrAdsForCurrentOrientation.length) == 0) {
            return;
        }
        if (i >= length) {
            i = 0;
        }
        AdlantisAd adlantisAd = adlantisAdArrAdsForCurrentOrientation[i];
        AdlantisAd adlantisAd2 = adlantisAdArrAdsForCurrentOrientation[(i + 1) % length];
        if (this._ad != null) {
            this._ad.viewingEnded();
        }
        this._ad = adlantisAd;
        this._ad.viewingStarted();
        setupAdDisplay();
        loadDrawable(adlantisAd2.imageURL(this), null);
    }

    public void setAdsModel(AdlantisAdsModel adlantisAdsModel) {
        this._adsModel = adlantisAdsModel;
    }

    protected void setupAdDisplay() {
        if (this._ad == null) {
            return;
        }
        int iAdType = this._ad.adType();
        if (iAdType == 1) {
            setDisplayedChild(0);
            setupBannerAdDisplay();
        } else if (iAdType == 2) {
            setDisplayedChild(1);
            setupTextAdDisplay();
        }
    }

    protected void setupBannerAdDisplay() {
        this._adAltText.setText(this._ad.altTextString(this));
        if (loadBannerDrawable() == null) {
            if (this._adAltText != this._adBannerViewFlipper.getCurrentView()) {
                this._adBannerViewFlipper.setDisplayedChild(1);
            }
        }
    }

    protected void setupTextAdDisplay() {
        loadIconDrawable();
        this._adtext.setTextAndSize(this._ad.textAdString());
    }
}

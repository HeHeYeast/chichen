package com.vpadn.ads;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vpadn.ads.VpadnAdRequest;
import org.json.JSONException;
import vpadn.D;
import vpadn.J;
import vpadn.S;
import vpadn.ab;
import vpadn.ad;
import vpadn.ae;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VpadnBanner extends RelativeLayout implements LocationListener, VpadnAd, J {
    public static final int DEFAULT_BACKGROUND_COLOR = 17170457;
    private D a;
    private VpadnAdListener b;

    /* renamed from: c, reason: collision with root package name */
    private Activity f269c;
    private boolean d;
    private int e;
    private int f;

    public VpadnBanner(Context context, AttributeSet attributeSet) throws JSONException {
        super(context, attributeSet);
        this.a = null;
        this.b = null;
        this.d = false;
        this.f269c = (Activity) context;
        setBackgroundColor(17170457);
        ad.a(context);
        this.a = new D((Activity) context, this);
        a(attributeSet);
    }

    private void a(AttributeSet attributeSet) throws JSONException {
        VpadnAdSize vpadnAdSize = VpadnAdSize.SMART_BANNER;
        String attributeValue = null;
        int attributeCount = attributeSet.getAttributeCount();
        boolean z = false;
        String attributeValue2 = "TW";
        boolean z2 = false;
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attributeSet.getAttributeName(i);
            if (attributeName.equals("bannerId")) {
                attributeValue = attributeSet.getAttributeValue(i);
            } else if (attributeName.equals("platform")) {
                attributeValue2 = attributeSet.getAttributeValue(i);
            } else if (attributeName.equals("adSize")) {
                String attributeValue3 = attributeSet.getAttributeValue(i);
                if (attributeValue3.equals("BANNER")) {
                    vpadnAdSize = VpadnAdSize.BANNER;
                } else if (attributeValue3.equals("IAB_BANNER")) {
                    vpadnAdSize = VpadnAdSize.IAB_BANNER;
                } else if (attributeValue3.equals("IAB_LEADERBOARD")) {
                    vpadnAdSize = VpadnAdSize.IAB_LEADERBOARD;
                } else if (attributeValue3.equals("IAB_MRECT")) {
                    vpadnAdSize = VpadnAdSize.IAB_MRECT;
                } else if (attributeValue3.equals("IAB_WIDE_SKYSCRAPER")) {
                    vpadnAdSize = VpadnAdSize.IAB_WIDE_SKYSCRAPER;
                }
            } else if (attributeName.equals("loadAdOnCreate")) {
                if (attributeSet.getAttributeValue(i).toLowerCase().equals("true")) {
                    z = true;
                }
            } else if (attributeName.equals("autoFresh") && attributeSet.getAttributeValue(i).toLowerCase().equals("true")) {
                z2 = true;
            }
        }
        if (attributeValue == null) {
            this.d = true;
            return;
        }
        this.a.d(attributeValue);
        this.a.a(vpadnAdSize);
        if (attributeValue2 == null) {
            this.d = true;
            return;
        }
        this.a.e(attributeValue2);
        if (z && z2) {
            VpadnAdRequest vpadnAdRequest = new VpadnAdRequest();
            vpadnAdRequest.setEnableAutoRefresh(true);
            loadAd(vpadnAdRequest);
        } else if (z && !z2) {
            loadAd(new VpadnAdRequest());
        }
    }

    public VpadnBanner(Activity activity, String str, VpadnAdSize vpadnAdSize, String str2) {
        super(activity);
        this.a = null;
        this.b = null;
        this.d = false;
        this.f269c = activity;
        setBackgroundColor(17170457);
        ad.a(activity);
        this.a = new D(activity, this);
        if (str == null) {
            this.d = true;
            return;
        }
        this.a.d(str);
        if (vpadnAdSize == null) {
            ab.d("VponBanner", "adSize is Null, use SMART_BANNER");
            this.a.a(VpadnAdSize.SMART_BANNER);
        } else {
            this.a.a(vpadnAdSize);
        }
        if (str2 == null) {
            this.d = true;
        } else {
            this.a.e(str2);
        }
    }

    public void request(boolean z) {
        if (!this.d) {
            this.a.b(z);
        }
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        if (this.a != null) {
            this.a.a(location);
        }
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    @Override // vpadn.J
    public void onVponAdReceived() {
        if (this.b != null) {
            this.b.onVpadnReceiveAd(this);
        }
    }

    @Override // vpadn.J
    public void onVponAdFailed(VpadnAdRequest.VpadnErrorCode vpadnErrorCode) {
        ab.d("VponBanner", "onVponAdFailed VponErrorCode code:" + vpadnErrorCode.toString());
        if (this.b != null) {
            this.b.onVpadnFailedToReceiveAd(this, vpadnErrorCode);
        }
        S.a().d();
    }

    @Override // vpadn.J
    public void onVponPresent() {
        if (this.b != null) {
            this.b.onVpadnPresentScreen(this);
        }
    }

    @Override // vpadn.J
    public void onVponDismiss() {
        if (this.b != null) {
            this.b.onVpadnDismissScreen(this);
        }
    }

    @Override // vpadn.J
    public void onVponLeaveApplication() {
        if (this.b != null) {
            this.b.onVpadnLeaveApplication(this);
        }
    }

    @Override // vpadn.J
    public void onControllerWebViewReady(int i, int i2) {
        this.e = i;
        this.f = i2;
        removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.e, this.f);
        layoutParams.addRule(13);
        if (this.a != null && this.a.s() != null) {
            addView(this.a.s(), layoutParams);
        } else {
            ab.b("VponBanner", "mController IS NULL or mController.getWebView() IS NULL");
        }
        ab.a("VponBanner", "this.getChildCount():" + getChildCount());
    }

    @Override // vpadn.J
    public void onPrepareExpandMode() {
        removeAllViews();
        if (this.a.s() != null) {
            TextView textView = new TextView(this.f269c);
            textView.setWidth(this.e);
            textView.setHeight(this.f);
            textView.setTextColor(0);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.e, this.f);
            layoutParams.addRule(13);
            addView(textView, layoutParams);
        }
    }

    @Override // vpadn.J
    public void onLeaveExpandMode() {
        removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.e, this.f);
        layoutParams.addRule(13);
        if (this.a != null && this.a.s() != null) {
            addView(this.a.s(), layoutParams);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        try {
            ab.a("VponBanner", "enter onDetachedFromWindow in VponBanner");
            super.onDetachedFromWindow();
            ad.a(this.f269c).b();
            if (this.a != null) {
                this.a.x();
                this.a.b();
                this.a.y();
                this.a = null;
            }
        } catch (Exception e) {
            ab.a("VponBanner", "onDetachedFromWindow throws Exception", e);
        }
    }

    public void destroy() {
        S.a().b();
        ad.a(this.f269c).b();
        new Handler().post(new Runnable() { // from class: com.vpadn.ads.VpadnBanner.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (VpadnBanner.this.a != null) {
                        VpadnBanner.this.a.x();
                        VpadnBanner.this.a.b();
                        VpadnBanner.this.a.y();
                        VpadnBanner.this.a = null;
                    }
                } catch (Exception e) {
                    ab.a("VponBanner", "destroy() throws Exception!", e);
                }
            }
        });
    }

    @Override // com.vpadn.ads.VpadnAd
    public boolean isReady() {
        if (this.a != null) {
            return this.a.z();
        }
        return false;
    }

    @Override // com.vpadn.ads.VpadnAd
    public void loadAd(VpadnAdRequest vpadnAdRequest) throws JSONException {
        if (!ae.d(this.f269c)) {
            ab.b("VponBanner", "permission-checking is failed in loadAd!!");
            if (this.b != null) {
                this.b.onVpadnFailedToReceiveAd(this, VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
                return;
            }
            return;
        }
        if (!this.d) {
            this.a.a(vpadnAdRequest);
        }
    }

    @Override // com.vpadn.ads.VpadnAd
    public void setAdListener(VpadnAdListener vpadnAdListener) {
        this.b = vpadnAdListener;
    }

    @Override // com.vpadn.ads.VpadnAd
    public void stopLoading() {
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        if (i == 0) {
            ab.a("VponBanner", "VponBanner visibility: VISIBLE");
            if (this.a != null) {
                this.a.B();
            }
        } else if (4 == i) {
            ab.a("VponBanner", "VponBanner visibility: INVISIBLE");
        } else if (8 == i) {
            ab.a("VponBanner", "VponBanner visibility: GONE");
            ad.a(this.f269c).b();
            if (this.a != null) {
                this.a.A();
            }
        }
        super.onWindowVisibilityChanged(i);
    }

    public void testSendJsonToVponCordovaPlugin(String str, String str2, String str3) {
        if (this.a != null) {
            this.a.a(str, str2, str3);
        }
    }
}

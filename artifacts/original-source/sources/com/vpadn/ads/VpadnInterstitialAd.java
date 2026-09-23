package com.vpadn.ads;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import com.vpadn.ads.VpadnAdRequest;
import org.json.JSONException;
import vpadn.F;
import vpadn.J;
import vpadn.S;
import vpadn.ab;
import vpadn.ad;
import vpadn.ae;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VpadnInterstitialAd extends RelativeLayout implements LocationListener, VpadnAd, J {
    protected F a;
    private VpadnAdListener b;

    /* renamed from: c, reason: collision with root package name */
    private Activity f270c;
    private boolean d;

    public VpadnInterstitialAd(Activity activity, String str, String str2) {
        super(activity);
        this.a = null;
        this.b = null;
        this.d = false;
        this.f270c = activity;
        setBackgroundColor(0);
        ad.a(activity);
        this.a = new F(activity, this);
        if (str == null) {
            this.d = true;
            return;
        }
        this.a.d(str);
        if (str2 == null) {
            this.d = true;
        } else {
            this.a.e(str2);
        }
    }

    public void show() {
        if (this.a != null && this.a.t()) {
            this.a.u();
        } else {
            ab.b("VponInterstitialAd", "call show() but is not ready!");
        }
    }

    @Override // com.vpadn.ads.VpadnAd
    public boolean isReady() {
        if (this.a != null) {
            return this.a.t();
        }
        return false;
    }

    @Override // com.vpadn.ads.VpadnAd
    public void loadAd(VpadnAdRequest vpadnAdRequest) throws JSONException {
        if (!ae.d(this.f270c)) {
            ab.b("VponInterstitialAd", "[interstitial] permission-checking  is failde in loadAd!!");
            if (this.b != null) {
                this.b.onVpadnFailedToReceiveAd(this, VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
                return;
            }
            return;
        }
        if (!this.d) {
            this.a.a(vpadnAdRequest);
            return;
        }
        ab.b("VponInterstitialAd", "[interstitial] invalid parameters in loadAd!!");
        if (this.b != null) {
            this.b.onVpadnFailedToReceiveAd(this, VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
        }
    }

    @Override // com.vpadn.ads.VpadnAd
    public void setAdListener(VpadnAdListener vpadnAdListener) {
        this.b = vpadnAdListener;
    }

    @Override // com.vpadn.ads.VpadnAd
    public void stopLoading() {
    }

    @Override // vpadn.J
    public void onVponAdReceived() {
        if (this.b != null) {
            this.b.onVpadnReceiveAd(this);
        }
    }

    @Override // vpadn.J
    public void onVponAdFailed(VpadnAdRequest.VpadnErrorCode vpadnErrorCode) {
        if (this.b != null) {
            this.b.onVpadnFailedToReceiveAd(this, vpadnErrorCode);
        }
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
    }

    @Override // vpadn.J
    public void onPrepareExpandMode() {
    }

    @Override // vpadn.J
    public void onLeaveExpandMode() {
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
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

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        ab.a("VponInterstitialAd", "onDetachedFromWindow in VponInterstitialAd");
        super.onDetachedFromWindow();
        ad.a(this.f270c).b();
        if (this.a != null) {
            this.a.b();
            this.a.v();
            this.a = null;
        }
    }

    public void destroy() {
        S.a().b();
        ad.a(this.f270c).b();
        new Handler().post(new Runnable() { // from class: com.vpadn.ads.VpadnInterstitialAd.1
            @Override // java.lang.Runnable
            public void run() {
                if (VpadnInterstitialAd.this.a != null) {
                    VpadnInterstitialAd.this.a.b();
                    VpadnInterstitialAd.this.a.v();
                    VpadnInterstitialAd.this.a = null;
                }
            }
        });
    }

    public void testSendJsonToVponCordovaPlugin(String str, String str2, String str3) {
        if (this.a != null) {
            this.a.a(str, str2, str3);
        }
    }
}

package jp.co.imobile.sdkads.android;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ImobileIconParams {
    private int a = 4;
    private int b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f291c = -1;
    private boolean d = true;
    private String e = "";
    private int f = -1;
    private int g = -1;
    private boolean h = true;
    private String i = "";
    private int j = -1;
    private int k = -1;

    final JSONObject a() throws JSONException, y {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("iconNumber", this.a);
            jSONObject.put("iconSize", this.b);
            jSONObject.put("iconViewLayoutWidth", this.f291c);
            jSONObject.put("iconTitleEnable", Boolean.toString(this.d));
            jSONObject.put("iconTitleFontColor", this.e);
            jSONObject.put("iconTitleFontSize", this.f);
            jSONObject.put("iconTitleOffset", this.g);
            jSONObject.put("iconTitleShadowEnable", Boolean.toString(this.h));
            jSONObject.put("iconTitleShadowColor", this.i);
            jSONObject.put("iconTitleShadowDx", this.j);
            jSONObject.put("iconTitleShadowDy", this.k);
            return jSONObject;
        } catch (JSONException e) {
            e.getMessage();
            x.b("Spot data to ad view data create.", "parse");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    public void setIconNumber(int iconNumber) {
        this.a = iconNumber;
    }

    public void setIconSize(int iconSize) {
        this.b = iconSize;
    }

    public void setIconTitleEnable(boolean iconTitleEnable) {
        this.d = iconTitleEnable;
    }

    public void setIconTitleFontColor(String iconTitleFontColor) {
        this.e = iconTitleFontColor;
    }

    public void setIconTitleFontSize(int iconTitleFontSize) {
        this.f = iconTitleFontSize;
    }

    public void setIconTitleOffset(int iconTitleOffset) {
        this.g = iconTitleOffset;
    }

    public void setIconTitleShadowColor(String iconTitleShadowColor) {
        this.i = iconTitleShadowColor;
    }

    public void setIconTitleShadowDx(int iconTitleShadowDx) {
        this.j = iconTitleShadowDx;
    }

    public void setIconTitleShadowDy(int iconTitleShadowDy) {
        this.k = iconTitleShadowDy;
    }

    public void setIconTitleShadowEnable(boolean iconTitleShadowEnable) {
        this.h = iconTitleShadowEnable;
    }

    public void setIconViewLayoutWidth(int iconViewLayoutWidth) {
        setIconViewLayoutWidth(iconViewLayoutWidth, true);
    }

    public void setIconViewLayoutWidth(int adIconViewLayoutWidth, boolean convert) {
        if (!convert) {
            this.f291c = adIconViewLayoutWidth;
        } else {
            r.a();
            this.f291c = r.a(adIconViewLayoutWidth);
        }
    }
}

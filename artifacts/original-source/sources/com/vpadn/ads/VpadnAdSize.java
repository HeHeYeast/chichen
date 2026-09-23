package com.vpadn.ads;

import android.content.Context;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VpadnAdSize {
    public static final int AUTO_HEIGHT = -1;
    public static final int FULL_WIDTH = -2;
    public static final int LANDSCAPE_AD_HEIGHT = -3;
    public static final int LARGE_AD_HEIGHT = -4;
    public static final String MIX_CUSTOME_HEIGHT = "mix_custom_height";
    public static final String MIX_CUSTOME_WIDTH = "mix_custom_width";
    public static final int PORTRAIT_AD_HEIGHT = -5;
    private int a;
    private int b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f268c;
    private boolean d;
    private String e;
    public static final VpadnAdSize BANNER = new VpadnAdSize(320, 50, false);
    public static final VpadnAdSize IAB_BANNER = new VpadnAdSize(468, 60, false);
    public static final VpadnAdSize IAB_LEADERBOARD = new VpadnAdSize(728, 90, false);
    public static final VpadnAdSize IAB_MRECT = new VpadnAdSize(300, 250, false);
    public static final VpadnAdSize IAB_WIDE_SKYSCRAPER = new VpadnAdSize(60, 468, false);
    public static final VpadnAdSize SMART_BANNER = new VpadnAdSize(-2, -1, false);

    public VpadnAdSize(int i, int i2) {
        this.f268c = false;
        this.d = false;
        this.e = "";
        if (i == -1 || i2 == -2) {
            throw new UnsupportedOperationException("width == AUTO_HEIGHT || height == FULL_WIDTH");
        }
        this.a = i;
        this.b = i2;
        if (i == -2 && i2 == -1) {
            this.f268c = false;
            return;
        }
        if ((i == -2 && i2 != -1) || (i != -2 && i2 == -1)) {
            this.f268c = false;
            this.d = true;
            if (i == -2) {
                this.e = MIX_CUSTOME_HEIGHT;
                return;
            } else {
                this.e = MIX_CUSTOME_WIDTH;
                return;
            }
        }
        this.f268c = true;
    }

    private VpadnAdSize(int i, int i2, boolean z) {
        this.f268c = false;
        this.d = false;
        this.e = "";
        this.a = i;
        this.b = i2;
        this.f268c = false;
    }

    public boolean isMix() {
        return this.d;
    }

    public String getMixModeCustomStr() {
        return this.e;
    }

    public boolean isSizeAppropriate(int i, int i2) {
        if (i == 320 && i2 == 50) {
            return true;
        }
        if (i == 468 && i2 == 60) {
            return true;
        }
        if (i == 728 && i2 == 90) {
            return true;
        }
        if (i == 300 && i2 == 250) {
            return true;
        }
        return i == 60 && i2 == 468;
    }

    public int getWidth() {
        if (this.a < 0 || this.b < 0) {
            throw new UnsupportedOperationException("Cannot get ad width");
        }
        return this.a;
    }

    public int getHeight() {
        if (this.a < 0 || this.b < 0) {
            throw new UnsupportedOperationException("Cannot get ad height");
        }
        return this.b;
    }

    public boolean isAutoHeight() {
        return this.a == -1;
    }

    public boolean isFullWidth() {
        return this.b == -2;
    }

    public boolean isCustomAdSize() {
        return this.f268c;
    }

    public int getWidthInPixels(Context context) {
        if (this.a > 0) {
            return (int) convertDpToPixel(this.a, context);
        }
        return 0;
    }

    public int getHeightInPixels(Context context) {
        if (this.b > 0) {
            return (int) convertDpToPixel(this.b, context);
        }
        return 0;
    }

    public static float convertDpToPixel(float f, Context context) {
        return context.getResources().getDisplayMetrics().density * f;
    }

    public static float convertPixelsToDp(float f, Context context) {
        return f / context.getResources().getDisplayMetrics().density;
    }

    public VpadnAdSize findBestSize(VpadnAdSize... vpadnAdSizeArr) {
        return null;
    }

    public String toString() {
        return "[VponAdSize] width" + this.a + " height:" + this.b + " isCustomSize:" + this.f268c;
    }

    public int hashCode() {
        return (((this.f268c ? 1231 : 1237) + ((this.b + 31) * 31)) * 31) + this.a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            VpadnAdSize vpadnAdSize = (VpadnAdSize) obj;
            return this.b == vpadnAdSize.b && this.f268c == vpadnAdSize.f268c && this.a == vpadnAdSize.a;
        }
        return false;
    }
}

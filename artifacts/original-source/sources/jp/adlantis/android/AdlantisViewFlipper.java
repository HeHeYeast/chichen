package jp.adlantis.android;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ViewFlipper;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisViewFlipper extends ViewFlipper {
    public AdlantisViewFlipper(Context context) {
        super(context);
    }

    public AdlantisViewFlipper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void logD(String str) {
        Log.d(getClass().getSimpleName(), str);
    }

    @Override // android.widget.ViewFlipper, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
            Log.d("AdlantisViewFlipper", "AdlantisViewFlipper ignoring IllegalArgumentException");
            stopFlipping();
        }
    }
}

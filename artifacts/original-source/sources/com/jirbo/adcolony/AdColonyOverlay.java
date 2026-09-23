package com.jirbo.adcolony;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdColonyOverlay extends ADCVideo {
    Rect U = new Rect();
    int V = 0;

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration new_config) {
        super.onConfigurationChanged(new_config);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        this.t = defaultDisplay.getWidth();
        this.u = defaultDisplay.getHeight();
        a.B = true;
        final View view = new View(this);
        view.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        if (d && this.F.Q) {
            this.O.setLayoutParams(new FrameLayout.LayoutParams(this.t, this.u - this.F.m, 17));
            this.N.addView(view, new FrameLayout.LayoutParams(this.t, this.u, 17));
            new Handler().postDelayed(new Runnable() { // from class: com.jirbo.adcolony.AdColonyOverlay.1
                @Override // java.lang.Runnable
                public void run() {
                    AdColonyOverlay.this.N.removeView(view);
                }
            }, 1500L);
        }
        this.F.a();
    }
}

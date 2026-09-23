package com.vpon.video;

import android.graphics.drawable.Drawable;
import vpadn.ag;
import vpadn.as;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ChangeSoundActionButton extends ActionButton {
    public ChangeSoundActionButton(as asVar, Drawable drawable, ag agVar) {
        super(asVar, drawable, agVar);
    }

    @Override // com.vpon.video.ActionButton
    public final void a() {
        if (this.b.c()) {
            setBackgroundDrawable(a("/vpon_video2_s-off.png"));
        } else {
            setBackgroundDrawable(a("/vpon_video2_s-on.png"));
        }
    }
}

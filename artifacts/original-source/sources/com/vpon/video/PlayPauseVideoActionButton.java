package com.vpon.video;

import android.graphics.drawable.Drawable;
import vpadn.ag;
import vpadn.as;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class PlayPauseVideoActionButton extends ActionButton {
    public PlayPauseVideoActionButton(as asVar, Drawable drawable, ag agVar) {
        super(asVar, drawable, agVar);
    }

    @Override // com.vpon.video.ActionButton
    public void a() {
        if (this.b.b().isPlaying()) {
            setBackgroundDrawable(a("/vpon_video2_pause.png"));
        } else {
            setBackgroundDrawable(a("/vpon_video2_play.png"));
        }
    }
}

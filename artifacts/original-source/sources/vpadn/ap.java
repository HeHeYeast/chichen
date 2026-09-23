package vpadn;

import android.app.Activity;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ap extends af {
    private as a;

    ap(as asVar, Activity activity, String str) {
        super(asVar, activity, str);
        this.a = asVar;
    }

    @Override // vpadn.af
    public final void b() throws IllegalStateException {
        if (this.a.b() != null) {
            if (this.a.b().isPlaying()) {
                this.a.b().pause();
                this.a.a("video_pause", (JSONObject) null);
            } else {
                this.a.b().start();
                this.a.a("video_play", (JSONObject) null);
            }
        }
    }
}

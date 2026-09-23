package vpadn;

import android.media.MediaPlayer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import org.json.JSONException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ah implements ag {
    private as a;

    ah(as asVar) {
        this.a = asVar;
    }

    @Override // vpadn.ag
    public final void a() throws JSONException {
        MediaPlayer mediaPlayerB = this.a.b();
        if (this.a.c()) {
            mediaPlayerB.setVolume(0.6f, 0.6f);
            this.a.a(false);
        } else {
            mediaPlayerB.setVolume(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
            this.a.a(true);
        }
    }
}

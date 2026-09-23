package rrrrrr;

import com.immersion.hapticmediasdk.controllers.MediaController;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class rcrrrr implements Runnable {

    /* renamed from: b04150415Е0415Е0415, reason: contains not printable characters */
    public static int f138b0415041504150415 = 1;

    /* renamed from: b0415ЕЕ0415Е0415, reason: contains not printable characters */
    public static int f139b041504150415 = 92;

    /* renamed from: bЕ0415Е0415Е0415, reason: contains not printable characters */
    public static int f140b041504150415 = 0;

    /* renamed from: bЕЕ04150415Е0415, reason: contains not printable characters */
    public static int f141b041504150415 = 2;

    /* renamed from: b0417З0417041704170417, reason: contains not printable characters */
    public final /* synthetic */ MediaController f142b04170417041704170417;

    public rcrrrr(MediaController mediaController) throws Exception {
        if (((f139b041504150415 + f138b0415041504150415) * f139b041504150415) % f141b041504150415 != f140b041504150415) {
            f139b041504150415 = 15;
            f140b041504150415 = 7;
        }
        try {
            this.f142b04170417041704170417 = mediaController;
            try {
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    /* renamed from: b0415Е04150415Е0415, reason: contains not printable characters */
    public static int m137b0415041504150415() {
        return 6;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (!this.f142b04170417041704170417.isPlaying() || MediaController.m83b043B043B(this.f142b04170417041704170417) == null) {
            return;
        }
        MediaController.m83b043B043B(this.f142b04170417041704170417).syncUpdate(this.f142b04170417041704170417.getCurrentPosition(), this.f142b04170417041704170417.getReferenceTimeForCurrentPosition());
        MediaController mediaController = this.f142b04170417041704170417;
        int i = f139b041504150415;
        switch ((i * (f138b0415041504150415 + i)) % f141b041504150415) {
            case 0:
                break;
            default:
                f139b041504150415 = m137b0415041504150415();
                f140b041504150415 = 99;
                break;
        }
        MediaController.m83b043B043B(mediaController).getHandler().removeCallbacks(this);
        MediaController.m83b043B043B(this.f142b04170417041704170417).getHandler().postDelayed(this, 1000L);
    }
}

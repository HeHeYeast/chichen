package rrrrrr;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.immersion.hapticmediasdk.HapticContentSDK;
import com.immersion.hapticmediasdk.MediaTaskManager;
import com.immersion.hapticmediasdk.controllers.MediaController;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class crrrrr extends Handler {

    /* renamed from: b04210421СС04210421, reason: contains not printable characters */
    public static int f125b0421042104210421 = 0;

    /* renamed from: b0421С0421С04210421, reason: contains not printable characters */
    public static int f126b0421042104210421 = 2;

    /* renamed from: bС0421СС04210421, reason: contains not printable characters */
    public static int f127b042104210421 = 1;

    /* renamed from: bСС0421С04210421, reason: contains not printable characters */
    public static int f128b042104210421 = 1;

    /* renamed from: b0417З0417З0417З, reason: contains not printable characters */
    public final /* synthetic */ MediaController f129b041704170417;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public crrrrr(MediaController mediaController, Looper looper) {
        super(looper);
        this.f129b041704170417 = mediaController;
    }

    /* renamed from: bС04210421С04210421, reason: contains not printable characters */
    public static int m131b0421042104210421() {
        return 35;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) throws Exception {
        try {
            try {
                switch (message.what) {
                    case 6:
                        if (MediaController.m82b04110411041104110411(this.f129b041704170417).get() == message.arg1 && MediaController.m79b043B(this.f129b041704170417).get() == message.arg2) {
                            MediaTaskManager mediaTaskManagerM84b043B = MediaController.m84b043B(this.f129b041704170417);
                            int i = f127b042104210421;
                            switch ((i * (f128b042104210421 + i)) % f126b0421042104210421) {
                                case 0:
                                    break;
                                default:
                                    f127b042104210421 = 55;
                                    f125b0421042104210421 = m131b0421042104210421();
                                    break;
                            }
                            if (mediaTaskManagerM84b043B.getSDKStatus() != HapticContentSDK.SDKStatus.PAUSED_DUE_TO_BUFFERING) {
                                MediaController.m77b043B043B(this.f129b041704170417, MediaController.m82b04110411041104110411(this.f129b041704170417).get(), SystemClock.uptimeMillis());
                                this.f129b041704170417.playbackStarted();
                                break;
                            } else {
                                MediaController.m84b043B(this.f129b041704170417).transitToState(HapticContentSDK.SDKStatus.PLAYING);
                                break;
                            }
                        } else {
                            return;
                        }
                        break;
                    case 7:
                        this.f129b041704170417.a(message.arg1);
                        return;
                    case 8:
                        MediaController.m78b043B043B(this.f129b041704170417, message);
                        break;
                    default:
                        return;
                }
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }
}

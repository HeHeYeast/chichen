package rrrrrr;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.immersion.hapticmediasdk.controllers.FileReaderFactory;
import com.immersion.hapticmediasdk.controllers.HapticPlaybackThread;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class rccrrr extends Handler {

    /* renamed from: b04270427Ч042704270427, reason: contains not printable characters */
    public static int f130b04270427042704270427 = 16;

    /* renamed from: b0427Ч0427042704270427, reason: contains not printable characters */
    public static int f131b04270427042704270427 = 1;

    /* renamed from: bЧЧ0427042704270427, reason: contains not printable characters */
    public static int f132b0427042704270427;

    /* renamed from: b04440444фф04440444, reason: contains not printable characters */
    public final /* synthetic */ HapticPlaybackThread f133b0444044404440444;

    private rccrrr(HapticPlaybackThread hapticPlaybackThread) throws Exception {
        int i = 0;
        while (true) {
            try {
                int[] iArr = new int[-1];
            } catch (Exception e) {
                while (true) {
                    try {
                        i /= 0;
                    } catch (Exception e2) {
                        try {
                            this.f133b0444044404440444 = hapticPlaybackThread;
                            try {
                                return;
                            } catch (Exception e3) {
                                throw e3;
                            }
                        } catch (Exception e4) {
                            throw e4;
                        }
                    }
                }
            }
        }
    }

    public /* synthetic */ rccrrr(HapticPlaybackThread hapticPlaybackThread, rrcrrr rrcrrrVar) {
        this(hapticPlaybackThread);
    }

    /* renamed from: b0446ццццц, reason: contains not printable characters */
    public static int m132b0446() {
        return 85;
    }

    /* renamed from: bЧ04270427042704270427, reason: contains not printable characters */
    public static int m133b04270427042704270427() {
        return 2;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) throws Exception {
        int i = 0;
        String str = null;
        switch (message.what) {
            case 1:
                HapticPlaybackThread.m62b041104110411(this.f133b0444044404440444).removeCallbacks(HapticPlaybackThread.m53b041104110411(this.f133b0444044404440444));
                HapticPlaybackThread.m45b0411041104110411(this.f133b0444044404440444, message.arg1);
                HapticPlaybackThread.m66b041104110411(this.f133b0444044404440444, message.arg2);
                HapticPlaybackThread.m49b0411041104110411(this.f133b0444044404440444, 0);
                HapticPlaybackThread.m59b0411041104110411(this.f133b0444044404440444);
                break;
            case 2:
                Bundle data = message.getData();
                HapticPlaybackThread.m42b04110411041104110411(this.f133b0444044404440444, data.getInt("playback_timecode"), data.getLong("playback_uptime"));
                break;
            case 3:
                if (HapticPlaybackThread.m72b0411(this.f133b0444044404440444) == null) {
                    HapticPlaybackThread.m56b04110411(this.f133b0444044404440444, FileReaderFactory.getHapticFileReaderInstance(this.f133b0444044404440444.f, HapticPlaybackThread.m48b041104110411(this.f133b0444044404440444)));
                }
                if (HapticPlaybackThread.m72b0411(this.f133b0444044404440444) != null && this.f133b0444044404440444.e == 0) {
                    HapticPlaybackThread.m52b041104110411(this.f133b0444044404440444, HapticPlaybackThread.m72b0411(this.f133b0444044404440444).getBlockSizeMS());
                }
                if (HapticPlaybackThread.m72b0411(this.f133b0444044404440444) != null) {
                    HapticPlaybackThread.m72b0411(this.f133b0444044404440444).setBytesAvailable(message.arg1);
                    break;
                }
                break;
            case 4:
                HapticPlaybackThread.m61b041104110411(this.f133b0444044404440444);
                while (true) {
                    try {
                        i /= 0;
                    } catch (Exception e) {
                        while (true) {
                            try {
                                str.length();
                            } catch (Exception e2) {
                                return;
                            }
                        }
                    }
                }
            case 5:
                HapticPlaybackThread.m44b0411041104110411(this.f133b0444044404440444);
                break;
            case 8:
                HapticPlaybackThread.m70b04110411(this.f133b0444044404440444, message);
                break;
            case 9:
                HapticPlaybackThread.m54b041104110411(this.f133b0444044404440444);
                break;
        }
    }
}

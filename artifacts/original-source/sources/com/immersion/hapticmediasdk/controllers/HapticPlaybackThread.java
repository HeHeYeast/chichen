package com.immersion.hapticmediasdk.controllers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import com.immersion.content.EndpointWarp;
import com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException;
import com.immersion.hapticmediasdk.utils.FileManager;
import com.immersion.hapticmediasdk.utils.Log;
import com.immersion.hapticmediasdk.utils.Profiler;
import com.immersion.hapticmediasdk.utils.RuntimeInfo;
import java.util.ArrayList;
import java.util.Iterator;
import rrrrrr.ccrcrr;
import rrrrrr.crcrrr;
import rrrrrr.rccrrr;
import rrrrrr.rrcrrr;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HapticPlaybackThread extends Thread {
    private static final long D = 100;
    private static final int E = 5;
    public static final int HAPTIC_BYTES_AVAILABLE_TO_DOWNLOAD = 3;
    public static final int HAPTIC_DOWNLOAD_ERROR = 8;
    public static final String HAPTIC_DOWNLOAD_EXCEPTION_KEY = "haptic_download_exception";
    public static final int HAPTIC_PAUSE_PLAYBACK = 5;
    public static final int HAPTIC_PLAYBACK_FOR_TIME_CODE = 2;
    public static final int HAPTIC_PLAYBACK_IS_READY = 6;
    public static final int HAPTIC_QUIT_PLAYBACK = 9;
    public static final int HAPTIC_SET_BUFFERING_POSITION = 1;
    public static final int HAPTIC_STOP_PLAYBACK = 4;
    public static final int PAUSE_AV_FOR_HAPTIC_BUFFERING = 7;
    private static final String a = "HapticPlaybackThread";
    private static final int b = Integer.MIN_VALUE;

    /* renamed from: b0427042704270427ЧЧ, reason: contains not printable characters */
    public static int f49b0427042704270427 = 1;

    /* renamed from: b0427Ч0427Ч0427Ч, reason: contains not printable characters */
    public static int f50b042704270427 = 0;

    /* renamed from: bЧ042704270427ЧЧ, reason: contains not printable characters */
    public static int f51b042704270427 = 86;

    /* renamed from: bЧЧЧЧ0427Ч, reason: contains not printable characters */
    public static int f52b0427 = 2;

    /* renamed from: c, reason: collision with root package name */
    private static final String f193c = "playback_timecode";
    private static final String d = "playback_uptime";
    private RuntimeInfo A;
    private boolean B;
    private FileManager C;
    private final Runnable F;
    private final Runnable G;

    /* renamed from: b044404440444фф0444, reason: contains not printable characters */
    public volatile boolean f53b0444044404440444;

    /* renamed from: bф04440444фф0444, reason: contains not printable characters */
    public Context f54b044404440444;

    /* renamed from: bффф0444ф0444, reason: contains not printable characters */
    public volatile boolean f55b04440444;
    private int e;
    private final String f;
    private Handler g;
    private final Handler h;
    private HapticDownloadThread i;
    private Looper j;
    private IHapticFileReader k;
    private EndpointWarp l;
    private final Profiler m;
    private Object n;
    private Object o;
    private int p;
    private int q;
    private int r;
    private long s;
    private int t;
    private int u;
    private int v;
    private long w;
    private boolean x;
    private boolean y;
    private ArrayList z;

    public HapticPlaybackThread(Context context, String str, Handler handler, boolean z, RuntimeInfo runtimeInfo) {
        super(a);
        this.e = 0;
        this.m = new Profiler();
        this.n = new Object();
        this.o = new Object();
        int i = f51b042704270427;
        switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = m58b04270427();
                f49b0427042704270427 = m58b04270427();
                break;
        }
        this.x = false;
        this.y = false;
        this.f53b0444044404440444 = false;
        this.f55b04440444 = false;
        this.B = false;
        this.F = new rrcrrr(this);
        this.G = new crcrrr(this);
        this.f = str;
        this.h = handler;
        this.f54b044404440444 = context;
        this.B = z;
        this.C = new FileManager(context);
        this.A = runtimeInfo;
        this.z = new ArrayList();
    }

    private void a() {
        while (this.i.isAlive()) {
            if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != m74b04270427()) {
                f51b042704270427 = m58b04270427();
                f49b0427042704270427 = 65;
            }
            this.i.terminate();
            this.i.interrupt();
            Thread.currentThread();
            while (true) {
                boolean z = false;
                switch (z) {
                    case false:
                        break;
                    case true:
                    default:
                        while (true) {
                            switch (1) {
                            }
                        }
                        break;
                }
            }
            Thread.yield();
        }
    }

    private void a(int i, long j) throws Exception {
        if (!this.y) {
            try {
                if (this.k == null) {
                    return;
                }
                if (this.l == null) {
                    byte[] encryptedHapticHeader = this.k.getEncryptedHapticHeader();
                    if (encryptedHapticHeader == null) {
                        Log.e(a, "corrupted hapt file or unsupported format");
                        return;
                    }
                    this.l = new EndpointWarp(this.f54b044404440444, encryptedHapticHeader, encryptedHapticHeader.length);
                    if (this.l == null) {
                        Log.d(a, "Error creating endpointwarp");
                        return;
                    }
                }
                this.l.start();
            } catch (Error e) {
                Log.e(a, e.getMessage());
                return;
            }
        }
        this.f55b04440444 = false;
        this.y = true;
        this.v = 0;
        synchronized (this.n) {
            try {
                this.u = i;
                this.t = this.u;
                if (this.w != 0) {
                    this.w = SystemClock.uptimeMillis();
                }
            } catch (Throwable th) {
                while (true) {
                    switch (1) {
                        case 0:
                            break;
                        case 1:
                            break;
                        default:
                            while (true) {
                                switch (1) {
                                }
                            }
                            break;
                    }
                }
                throw th;
            }
        }
        this.s = j;
        h();
    }

    private void a(Message message) {
        this.x = true;
        Message messageObtainMessage = this.h.obtainMessage(8);
        messageObtainMessage.setData(message.getData());
        this.h.sendMessage(messageObtainMessage);
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = 41;
            f50b042704270427 = m58b04270427();
        }
    }

    private void b() throws Exception {
        if (this.i != null) {
            a();
            this.i = null;
        }
        synchronized (this.o) {
            this.g.removeCallbacksAndMessages(null);
        }
        if (this.j != null) {
            this.j.quit();
            this.j = null;
        }
        if (this.k != null) {
            this.k.close();
            this.k = null;
        }
        if (this.l != null) {
            this.l.stop();
            this.l.dispose();
            while (true) {
                boolean z = false;
                switch (z) {
                    case false:
                        break;
                    case true:
                    default:
                        while (true) {
                            switch (1) {
                            }
                        }
                        break;
                }
            }
            this.l = null;
        }
        this.C.deleteHapticStorage();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x001a. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x001e. Please report as an issue. */
    /* renamed from: b04110411041104110411Б, reason: contains not printable characters */
    public static /* synthetic */ void m42b04110411041104110411(HapticPlaybackThread hapticPlaybackThread, int i, long j) throws Exception {
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = 45;
        }
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        hapticPlaybackThread.a(i, j);
    }

    /* renamed from: b0411041104110411Б0411, reason: contains not printable characters */
    public static /* synthetic */ EndpointWarp m43b04110411041104110411(HapticPlaybackThread hapticPlaybackThread) {
        boolean z = false;
        int i = 4;
        while (true) {
            try {
                i /= 0;
            } catch (Exception e) {
                f51b042704270427 = m58b04270427();
                while (true) {
                    try {
                        int[] iArr = new int[-1];
                    } catch (Exception e2) {
                        f51b042704270427 = m58b04270427();
                        EndpointWarp endpointWarp = hapticPlaybackThread.l;
                        while (true) {
                            switch (z) {
                                case false:
                                    break;
                                case true:
                                    break;
                                default:
                                    while (true) {
                                        switch (z) {
                                        }
                                    }
                                    break;
                            }
                        }
                        return endpointWarp;
                    }
                }
            }
        }
    }

    /* renamed from: b041104110411ББ0411, reason: contains not printable characters */
    public static /* synthetic */ void m44b0411041104110411(HapticPlaybackThread hapticPlaybackThread) throws Exception {
        int i = f51b042704270427;
        switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = 25;
                f50b042704270427 = 36;
                break;
        }
        try {
            hapticPlaybackThread.g();
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: b04110411Б04110411Б, reason: contains not printable characters */
    public static /* synthetic */ int m45b0411041104110411(HapticPlaybackThread hapticPlaybackThread, int i) {
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        int i2 = f51b042704270427;
        switch ((i2 * (f49b0427042704270427 + i2)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = 74;
                f50b042704270427 = m58b04270427();
                break;
        }
        hapticPlaybackThread.p = i;
        return i;
    }

    /* renamed from: b04110411Б0411Б0411, reason: contains not printable characters */
    public static /* synthetic */ boolean m46b0411041104110411(HapticPlaybackThread hapticPlaybackThread) throws Exception {
        try {
            boolean z = hapticPlaybackThread.y;
            if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
                f51b042704270427 = m58b04270427();
                f50b042704270427 = m58b04270427();
            }
            return z;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: b04110411ББ04110411, reason: contains not printable characters */
    public static /* synthetic */ int m47b0411041104110411(HapticPlaybackThread hapticPlaybackThread) {
        while (true) {
            boolean z = false;
            switch (z) {
                case false:
                    break;
                case true:
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        if (((m58b04270427() + f49b0427042704270427) * m58b04270427()) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = m58b04270427();
        }
        return hapticPlaybackThread.u;
    }

    /* renamed from: b04110411БББ0411, reason: contains not printable characters */
    public static /* synthetic */ FileManager m48b041104110411(HapticPlaybackThread hapticPlaybackThread) {
        while (true) {
            try {
                int[] iArr = new int[-1];
            } catch (Exception e) {
                f51b042704270427 = 90;
                return hapticPlaybackThread.C;
            }
        }
    }

    /* renamed from: b0411Б041104110411Б, reason: contains not printable characters */
    public static /* synthetic */ int m49b0411041104110411(HapticPlaybackThread hapticPlaybackThread, int i) {
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = 87;
        }
        hapticPlaybackThread.r = i;
        return i;
    }

    /* renamed from: b0411Б04110411Б0411, reason: contains not printable characters */
    public static /* synthetic */ ArrayList m50b0411041104110411(HapticPlaybackThread hapticPlaybackThread) throws Exception {
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = 92;
        }
        try {
            return hapticPlaybackThread.z;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: b0411Б0411Б04110411, reason: contains not printable characters */
    public static /* synthetic */ Runnable m51b0411041104110411(HapticPlaybackThread hapticPlaybackThread) throws Exception {
        if (((m58b04270427() + f49b0427042704270427) * m58b04270427()) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = m58b04270427();
        }
        try {
            return hapticPlaybackThread.G;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: b0411Б0411ББ0411, reason: contains not printable characters */
    public static /* synthetic */ int m52b041104110411(HapticPlaybackThread hapticPlaybackThread, int i) {
        boolean z = false;
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = 23;
        }
        while (true) {
            switch (z) {
                case false:
                    break;
                case true:
                default:
                    while (true) {
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        hapticPlaybackThread.e = i;
        return i;
    }

    /* renamed from: b0411ББ04110411Б, reason: contains not printable characters */
    public static /* synthetic */ Runnable m53b041104110411(HapticPlaybackThread hapticPlaybackThread) {
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        Runnable runnable = hapticPlaybackThread.F;
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != m74b04270427()) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = 18;
        }
        return runnable;
    }

    /* renamed from: b0411ББ0411Б0411, reason: contains not printable characters */
    public static /* synthetic */ void m54b041104110411(HapticPlaybackThread hapticPlaybackThread) {
        String str = null;
        hapticPlaybackThread.e();
        while (true) {
            try {
                str.length();
            } catch (Exception e) {
                f51b042704270427 = m58b04270427();
                return;
            }
        }
    }

    /* renamed from: b0411БББ04110411, reason: contains not printable characters */
    public static /* synthetic */ int m55b041104110411(HapticPlaybackThread hapticPlaybackThread, int i) {
        int i2 = hapticPlaybackThread.u + i;
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = 55;
            f50b042704270427 = 1;
        }
        hapticPlaybackThread.u = i2;
        return i2;
    }

    /* renamed from: b0411ББББ0411, reason: contains not printable characters */
    public static /* synthetic */ IHapticFileReader m56b04110411(HapticPlaybackThread hapticPlaybackThread, IHapticFileReader iHapticFileReader) {
        boolean z = false;
        while (true) {
            switch (z) {
                case false:
                    break;
                case true:
                    break;
                default:
                    while (true) {
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = m58b04270427();
        }
        hapticPlaybackThread.k = iHapticFileReader;
        return iHapticFileReader;
    }

    /* renamed from: b04270427Ч04270427Ч, reason: contains not printable characters */
    public static int m57b0427042704270427() {
        return 2;
    }

    /* renamed from: b0427ЧЧЧ0427Ч, reason: contains not printable characters */
    public static int m58b04270427() {
        return 41;
    }

    /* renamed from: bБ0411041104110411Б, reason: contains not printable characters */
    public static /* synthetic */ void m59b0411041104110411(HapticPlaybackThread hapticPlaybackThread) {
        hapticPlaybackThread.d();
        int i = f51b042704270427;
        switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = m58b04270427();
                f50b042704270427 = m58b04270427();
                break;
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0017. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x001b. Please report as an issue. */
    /* renamed from: bБ041104110411Б0411, reason: contains not printable characters */
    public static /* synthetic */ RuntimeInfo m60b0411041104110411(HapticPlaybackThread hapticPlaybackThread) {
        int i = f51b042704270427;
        switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = m58b04270427();
                f50b042704270427 = 64;
                break;
        }
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        return hapticPlaybackThread.A;
    }

    /* renamed from: bБ04110411ББ0411, reason: contains not printable characters */
    public static /* synthetic */ void m61b041104110411(HapticPlaybackThread hapticPlaybackThread) {
        int iM58b04270427 = m58b04270427();
        switch ((iM58b04270427 * (f49b0427042704270427 + iM58b04270427)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = m58b04270427();
                f50b042704270427 = m58b04270427();
                break;
        }
        hapticPlaybackThread.f();
    }

    /* renamed from: bБ0411Б04110411Б, reason: contains not printable characters */
    public static /* synthetic */ Handler m62b041104110411(HapticPlaybackThread hapticPlaybackThread) throws Exception {
        try {
            Handler handler = hapticPlaybackThread.g;
            int i = f51b042704270427;
            switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
                default:
                    f51b042704270427 = 9;
                    f50b042704270427 = 62;
                case 0:
                    return handler;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: bБ0411Б0411Б0411, reason: contains not printable characters */
    public static /* synthetic */ void m63b041104110411(HapticPlaybackThread hapticPlaybackThread) {
        hapticPlaybackThread.h();
        while (true) {
            try {
                int[] iArr = new int[-1];
            } catch (Exception e) {
                f51b042704270427 = m58b04270427();
                return;
            }
        }
    }

    /* renamed from: bБ0411ББ04110411, reason: contains not printable characters */
    public static /* synthetic */ int m64b041104110411(HapticPlaybackThread hapticPlaybackThread, int i) throws Exception {
        while (true) {
            try {
                int[] iArr = new int[-1];
            } catch (Exception e) {
                f51b042704270427 = m58b04270427();
                while (true) {
                    try {
                        int[] iArr2 = new int[-1];
                    } catch (Exception e2) {
                        f51b042704270427 = m58b04270427();
                        try {
                            hapticPlaybackThread.t = i;
                            return i;
                        } catch (Exception e3) {
                            throw e3;
                        }
                    }
                }
            }
        }
    }

    /* renamed from: bББ041104110411Б, reason: contains not printable characters */
    public static /* synthetic */ int m66b041104110411(HapticPlaybackThread hapticPlaybackThread, int i) throws Exception {
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % m57b0427042704270427() != f50b042704270427) {
            f51b042704270427 = 8;
            f50b042704270427 = m58b04270427();
        }
        try {
            hapticPlaybackThread.q = i;
            return i;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: bББ0411Б04110411, reason: contains not printable characters */
    public static /* synthetic */ long m68b041104110411(HapticPlaybackThread hapticPlaybackThread, long j) {
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = m58b04270427();
            f50b042704270427 = 90;
        }
        hapticPlaybackThread.w = j;
        return j;
    }

    /* renamed from: bБББ0411Б0411, reason: contains not printable characters */
    public static /* synthetic */ void m70b04110411(HapticPlaybackThread hapticPlaybackThread, Message message) {
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        int i = f51b042704270427;
        switch ((i * (m73b042704270427() + i)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = 56;
                f50b042704270427 = 92;
                break;
        }
        hapticPlaybackThread.a(message);
    }

    /* renamed from: bББББ04110411, reason: contains not printable characters */
    public static /* synthetic */ Object m71b04110411(HapticPlaybackThread hapticPlaybackThread) throws Exception {
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = 18;
            f50b042704270427 = m58b04270427();
        }
        try {
            return hapticPlaybackThread.n;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: bБББББ0411, reason: contains not printable characters */
    public static /* synthetic */ IHapticFileReader m72b0411(HapticPlaybackThread hapticPlaybackThread) {
        int i = f51b042704270427;
        switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = m58b04270427();
                f50b042704270427 = 19;
                break;
        }
        IHapticFileReader iHapticFileReader = hapticPlaybackThread.k;
        while (true) {
            boolean z = false;
            switch (z) {
                case false:
                    break;
                case true:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        return iHapticFileReader;
    }

    /* renamed from: bЧ0427Ч04270427Ч, reason: contains not printable characters */
    public static int m73b042704270427() {
        return 1;
    }

    /* renamed from: bЧЧ0427Ч0427Ч, reason: contains not printable characters */
    public static int m74b04270427() {
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0009 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void c() {
        /*
            r1 = this;
            r0 = 0
            monitor-enter(r1)
        L2:
            switch(r0) {
                case 0: goto L9;
                case 1: goto L2;
                default: goto L5;
            }
        L5:
            switch(r0) {
                case 0: goto L9;
                case 1: goto L2;
                default: goto L8;
            }
        L8:
            goto L5
        L9:
            r1.notifyAll()     // Catch: java.lang.Throwable -> Le
            monitor-exit(r1)     // Catch: java.lang.Throwable -> Le
            return
        Le:
            r0 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> Le
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.immersion.hapticmediasdk.controllers.HapticPlaybackThread.c():void");
    }

    private void d() {
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
            f51b042704270427 = 74;
            f50b042704270427 = 21;
        }
        if (this.x) {
            return;
        }
        int i = this.r;
        this.r = i + 1;
        if (i == 5) {
            this.h.sendMessage(this.h.obtainMessage(7, this.p, 0));
            this.g.postDelayed(this.F, D);
        } else if (this.k == null || !this.k.bufferAtPlaybackPosition(this.p)) {
            this.g.postDelayed(this.F, D);
        } else if (this.q != b) {
            this.h.sendMessage(this.h.obtainMessage(6, this.p, this.q));
        }
    }

    private void e() {
        boolean z = false;
        String str = null;
        try {
            b();
            while (true) {
                try {
                    str.length();
                } catch (Exception e) {
                    f51b042704270427 = m58b04270427();
                    while (true) {
                        try {
                            str.length();
                        } catch (Exception e2) {
                            f51b042704270427 = m58b04270427();
                            while (true) {
                                try {
                                    str.length();
                                } catch (Exception e3) {
                                    f51b042704270427 = 38;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e4) {
            Log.e(a, "quit() : " + e4.getMessage());
            while (true) {
                switch (z) {
                    case false:
                        break;
                    case true:
                        break;
                    default:
                        while (true) {
                            switch (1) {
                            }
                        }
                        break;
                }
            }
        } finally {
            this.f53b0444044404440444 = false;
            c();
        }
    }

    private void f() {
        boolean z = false;
        this.y = false;
        if (this.l != null) {
            this.l.stop();
        }
        while (true) {
            switch (z) {
                case false:
                    break;
                case true:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        this.g.removeCallbacks(this.F);
        removePlaybackCallbacks();
        synchronized (this.n) {
            this.u = 0;
            this.t = 0;
            this.w = 0L;
        }
        this.v = 0;
        this.s = 0L;
        this.f55b04440444 = true;
    }

    private void g() {
        this.y = false;
        removePlaybackCallbacks();
        int i = f51b042704270427;
        switch ((i * (m73b042704270427() + i)) % f52b0427) {
            case 0:
                break;
            default:
                f51b042704270427 = m58b04270427();
                f50b042704270427 = m58b04270427();
                break;
        }
    }

    private void h() {
        int i;
        int i2;
        boolean z = false;
        if (this.y) {
            synchronized (this.n) {
                i = this.u;
                i2 = this.t;
            }
            try {
                byte[] bufferForPlaybackPosition = this.k.getBufferForPlaybackPosition(i);
                int hapticBlockIndex = this.k.getHapticBlockIndex(i);
                long blockOffset = this.k.getBlockOffset(i);
                if (bufferForPlaybackPosition == null) {
                    synchronized (this.n) {
                        this.u = 0;
                        this.t = 0;
                    }
                    this.v = 0;
                    this.s = 0L;
                    this.y = false;
                    return;
                }
                long j = this.v + this.s;
                ccrcrr ccrcrrVar = new ccrcrr(this, i, i2, bufferForPlaybackPosition, hapticBlockIndex, blockOffset);
                Object obj = this.o;
                while (true) {
                    switch (1) {
                        case 0:
                            break;
                        case 1:
                            break;
                        default:
                            while (true) {
                                switch (z) {
                                }
                            }
                            break;
                    }
                }
                synchronized (obj) {
                    this.z.add(ccrcrrVar);
                }
                this.g.postAtTime(ccrcrrVar, this.e + j);
                this.v += this.e;
                this.m.startTimingII();
            } catch (NotEnoughHapticBytesAvailableException e) {
                this.y = false;
                this.h.sendMessage(this.h.obtainMessage(7, i, 0));
            }
        }
    }

    public Handler getHandler() throws Exception {
        try {
            Handler handler = this.g;
            if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % f52b0427 != f50b042704270427) {
                f51b042704270427 = m58b04270427();
                f50b042704270427 = 58;
            }
            return handler;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isStarted() {
        boolean z = this.f53b0444044404440444;
        int i = f51b042704270427;
        switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
            default:
                f51b042704270427 = 69;
                f50b042704270427 = m58b04270427();
            case 0:
                return z;
        }
    }

    public boolean isStopped() {
        boolean z = false;
        boolean z2 = this.f55b04440444;
        while (true) {
            switch (z) {
                case false:
                    break;
                case true:
                    break;
                default:
                    while (true) {
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        int i = f51b042704270427;
        switch ((i * (m73b042704270427() + i)) % f52b0427) {
            default:
                f51b042704270427 = 11;
                f50b042704270427 = m58b04270427();
            case 0:
                return z2;
        }
    }

    public void pauseHapticPlayback() {
        if (((f51b042704270427 + f49b0427042704270427) * f51b042704270427) % m57b0427042704270427() != m74b04270427()) {
            f51b042704270427 = 98;
            f50b042704270427 = 68;
        }
        this.g.sendEmptyMessage(5);
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    return;
                default:
                    while (true) {
                        switch (1) {
                            case 1:
                                return;
                        }
                    }
                    break;
            }
        }
    }

    public void playHapticForPlaybackPosition(int i, long j) {
        int i2 = 0;
        String str = null;
        removePlaybackCallbacks();
        this.g.removeMessages(2);
        while (true) {
            switch (i2) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        switch (i2) {
                        }
                    }
                    break;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt(f193c, i);
        bundle.putLong(d, j);
        Message messageObtainMessage = this.g.obtainMessage(2);
        while (true) {
            try {
                str.length();
            } catch (Exception e) {
                f51b042704270427 = 75;
                while (true) {
                    try {
                        i2 /= 0;
                    } catch (Exception e2) {
                        f51b042704270427 = m58b04270427();
                        while (true) {
                            try {
                                str.length();
                            } catch (Exception e3) {
                                f51b042704270427 = 38;
                                messageObtainMessage.setData(bundle);
                                this.g.sendMessage(messageObtainMessage);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void prepareHapticPlayback(int i, int i2) {
        this.g.removeMessages(1);
        this.g.sendMessage(this.g.obtainMessage(1, i, i2));
    }

    public void quitHapticPlayback() throws Exception {
        try {
            if (this.g.sendEmptyMessage(9)) {
                return;
            }
            if (((f51b042704270427 + m73b042704270427()) * f51b042704270427) % f52b0427 != f50b042704270427) {
                f51b042704270427 = m58b04270427();
                f50b042704270427 = 16;
            }
            this.f53b0444044404440444 = false;
            try {
                c();
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public void removePlaybackCallbacks() {
        synchronized (this.o) {
            Iterator it = this.z.iterator();
            while (it.hasNext()) {
                this.g.removeCallbacks((ccrcrr) it.next());
            }
            this.z.clear();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws SecurityException, IllegalArgumentException {
        String str = null;
        Process.setThreadPriority(-19);
        Looper.prepare();
        this.j = Looper.myLooper();
        this.g = new rccrrr(this, null);
        while (true) {
            try {
                str.length();
            } catch (Exception e) {
                f51b042704270427 = m58b04270427();
                this.i = new HapticDownloadThread(this.f, this.g, this.B, this.C);
                this.i.start();
                this.f53b0444044404440444 = true;
                c();
                Looper.loop();
                return;
            }
        }
    }

    public void stopHapticPlayback() throws Exception {
        try {
            this.g.sendEmptyMessage(4);
            int i = f51b042704270427;
            switch ((i * (f49b0427042704270427 + i)) % f52b0427) {
                case 0:
                    return;
                default:
                    f51b042704270427 = 35;
                    f50b042704270427 = 24;
                    return;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x0024. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0027. Please report as an issue. */
    public void syncUpdate(int i, long j) {
        synchronized (this.n) {
            long jUptimeMillis = SystemClock.uptimeMillis();
            int i2 = (int) (i + (jUptimeMillis - j));
            int i3 = i2 - (((int) (jUptimeMillis - this.w)) + this.u);
            if (50 < Math.abs(i3)) {
                this.u = i3 + this.u;
                while (true) {
                    boolean z = false;
                    switch (z) {
                        case false:
                            break;
                        case true:
                        default:
                            while (true) {
                                switch (1) {
                                }
                            }
                            break;
                    }
                }
                this.t = this.u;
                this.g.sendMessage(this.g.obtainMessage(1, i2, b));
            }
        }
    }
}

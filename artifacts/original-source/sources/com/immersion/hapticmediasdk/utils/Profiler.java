package com.immersion.hapticmediasdk.utils;

import android.os.SystemClock;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Profiler {

    /* renamed from: b044A044A044A044Aъ044A, reason: contains not printable characters */
    public static int f100b044A044A044A044A044A = 0;

    /* renamed from: b044A044Aъ044Aъ044A, reason: contains not printable characters */
    public static int f101b044A044A044A044A = 1;

    /* renamed from: bъ044Aъ044Aъ044A, reason: contains not printable characters */
    public static int f102b044A044A044A = 89;

    /* renamed from: bъъ044A044Aъ044A, reason: contains not printable characters */
    public static int f103b044A044A044A = 2;
    public long mStartTime;
    public long mStartTimeII;

    public Profiler() throws Exception {
        int i = f102b044A044A044A;
        switch ((i * (f101b044A044A044A044A + i)) % f103b044A044A044A) {
            case 0:
                break;
            default:
                f102b044A044A044A = 10;
                f101b044A044A044A044A = 87;
                break;
        }
        try {
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: bъъъъ044A044A, reason: contains not printable characters */
    public static int m124b044A044A() {
        return 4;
    }

    public long getDuration() throws Exception {
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mStartTime;
            int i = f102b044A044A044A;
            switch ((i * (f101b044A044A044A044A + i)) % f103b044A044A044A) {
                default:
                    f102b044A044A044A = m124b044A044A();
                    f100b044A044A044A044A044A = 69;
                case 0:
                    return jElapsedRealtime;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public long getDurationII() {
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
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        int i = f102b044A044A044A;
        switch ((i * (f101b044A044A044A044A + i)) % f103b044A044A044A) {
            case 0:
                break;
            default:
                f102b044A044A044A = 72;
                f100b044A044A044A044A044A = 11;
                break;
        }
        return jElapsedRealtime - this.mStartTimeII;
    }

    public void startTiming() throws Exception {
        int i = 3;
        while (true) {
            try {
                i /= 0;
            } catch (Exception e) {
                f102b044A044A044A = 75;
                try {
                    this.mStartTime = SystemClock.elapsedRealtime();
                    return;
                } catch (Exception e2) {
                    throw e2;
                }
            }
        }
    }

    public void startTimingII() {
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
        if (((f102b044A044A044A + f101b044A044A044A044A) * f102b044A044A044A) % f103b044A044A044A != f100b044A044A044A044A044A) {
            f102b044A044A044A = 81;
            f100b044A044A044A044A044A = 31;
        }
        this.mStartTimeII = SystemClock.elapsedRealtime();
    }
}

package com.google.android.gms.internal;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import c.Globalization;
import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ak implements ai {
    private static int a(DisplayMetrics displayMetrics, Map<String, String> map, String str, int i) {
        String str2 = map.get(str);
        if (str2 == null) {
            return i;
        }
        try {
            return cm.a(displayMetrics, Integer.parseInt(str2));
        } catch (NumberFormatException e) {
            cn.q("Could not parse " + str + " in a video GMSG: " + str2);
            return i;
        }
    }

    @Override // com.google.android.gms.internal.ai
    public void a(cq cqVar, Map<String, String> map) {
        String str = map.get("action");
        if (str == null) {
            cn.q("Action missing from video GMSG.");
            return;
        }
        bf bfVarAu = cqVar.au();
        if (bfVarAu == null) {
            cn.q("Could not get ad overlay for a video GMSG.");
            return;
        }
        boolean zEqualsIgnoreCase = "new".equalsIgnoreCase(str);
        boolean zEqualsIgnoreCase2 = "position".equalsIgnoreCase(str);
        if (zEqualsIgnoreCase || zEqualsIgnoreCase2) {
            DisplayMetrics displayMetrics = cqVar.getContext().getResources().getDisplayMetrics();
            int iA = a(displayMetrics, map, "x", 0);
            int iA2 = a(displayMetrics, map, "y", 0);
            int iA3 = a(displayMetrics, map, "w", -1);
            int iA4 = a(displayMetrics, map, "h", -1);
            if (zEqualsIgnoreCase && bfVarAu.Q() == null) {
                bfVarAu.c(iA, iA2, iA3, iA4);
                return;
            } else {
                bfVarAu.b(iA, iA2, iA3, iA4);
                return;
            }
        }
        bj bjVarQ = bfVarAu.Q();
        if (bjVarQ == null) {
            bj.a(cqVar, "no_video_view", (String) null);
            return;
        }
        if ("click".equalsIgnoreCase(str)) {
            DisplayMetrics displayMetrics2 = cqVar.getContext().getResources().getDisplayMetrics();
            int iA5 = a(displayMetrics2, map, "x", 0);
            int iA6 = a(displayMetrics2, map, "y", 0);
            long jUptimeMillis = SystemClock.uptimeMillis();
            MotionEvent motionEventObtain = MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 0, iA5, iA6, 0);
            bjVarQ.b(motionEventObtain);
            motionEventObtain.recycle();
            return;
        }
        if ("controls".equalsIgnoreCase(str)) {
            String str2 = map.get("enabled");
            if (str2 == null) {
                cn.q("Enabled parameter missing from controls video GMSG.");
                return;
            } else {
                bjVarQ.f(Boolean.parseBoolean(str2));
                return;
            }
        }
        if ("currentTime".equalsIgnoreCase(str)) {
            String str3 = map.get(Globalization.TIME);
            if (str3 == null) {
                cn.q("Time parameter missing from currentTime video GMSG.");
                return;
            }
            try {
                bjVarQ.seekTo((int) (Float.parseFloat(str3) * 1000.0f));
                return;
            } catch (NumberFormatException e) {
                cn.q("Could not parse time parameter from currentTime video GMSG: " + str3);
                return;
            }
        }
        if ("hide".equalsIgnoreCase(str)) {
            bjVarQ.setVisibility(4);
            return;
        }
        if ("load".equalsIgnoreCase(str)) {
            bjVarQ.Z();
            return;
        }
        if ("pause".equalsIgnoreCase(str)) {
            bjVarQ.pause();
            return;
        }
        if ("play".equalsIgnoreCase(str)) {
            bjVarQ.play();
            return;
        }
        if ("show".equalsIgnoreCase(str)) {
            bjVarQ.setVisibility(0);
        } else if ("src".equalsIgnoreCase(str)) {
            bjVarQ.i(map.get("src"));
        } else {
            cn.q("Unknown video action: " + str);
        }
    }
}

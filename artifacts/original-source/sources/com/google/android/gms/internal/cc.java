package com.google.android.gms.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class cc {
    private String gX;
    private String gY;
    private List<String> gZ;
    private List<String> ha;
    private List<String> he;
    private long hb = -1;
    private boolean hc = false;
    private final long hd = -1;
    private long hf = -1;
    private int hg = -1;

    private static long a(Map<String, List<String>> map, String str) {
        List<String> list = map.get(str);
        if (list != null && !list.isEmpty()) {
            String str2 = list.get(0);
            try {
                return (long) (Float.parseFloat(str2) * 1000.0f);
            } catch (NumberFormatException e) {
                cn.q("Could not parse float from " + str + " header: " + str2);
            }
        }
        return -1L;
    }

    private static List<String> b(Map<String, List<String>> map, String str) {
        String str2;
        List<String> list = map.get(str);
        if (list == null || list.isEmpty() || (str2 = list.get(0)) == null) {
            return null;
        }
        return Arrays.asList(str2.trim().split("\\s+"));
    }

    private void e(Map<String, List<String>> map) {
        List<String> listB = b(map, "X-Afma-Click-Tracking-Urls");
        if (listB != null) {
            this.gZ = listB;
        }
    }

    private void f(Map<String, List<String>> map) {
        List<String> listB = b(map, "X-Afma-Tracking-Urls");
        if (listB != null) {
            this.ha = listB;
        }
    }

    private void g(Map<String, List<String>> map) {
        long jA = a(map, "X-Afma-Interstitial-Timeout");
        if (jA != -1) {
            this.hb = jA;
        }
    }

    private void h(Map<String, List<String>> map) {
        List<String> list = map.get("X-Afma-Mediation");
        if (list == null || list.isEmpty()) {
            return;
        }
        this.hc = Boolean.valueOf(list.get(0)).booleanValue();
    }

    private void i(Map<String, List<String>> map) {
        List<String> listB = b(map, "X-Afma-Manual-Tracking-Urls");
        if (listB != null) {
            this.he = listB;
        }
    }

    private void j(Map<String, List<String>> map) {
        long jA = a(map, "X-Afma-Refresh-Rate");
        if (jA != -1) {
            this.hf = jA;
        }
    }

    private void k(Map<String, List<String>> map) {
        List<String> list = map.get("X-Afma-Orientation");
        if (list == null || list.isEmpty()) {
            return;
        }
        String str = list.get(0);
        if ("portrait".equalsIgnoreCase(str)) {
            this.hg = ci.ao();
        } else if ("landscape".equalsIgnoreCase(str)) {
            this.hg = ci.an();
        }
    }

    public void a(String str, Map<String, List<String>> map, String str2) {
        this.gX = str;
        this.gY = str2;
        d(map);
    }

    public bw ak() {
        return new bw(this.gX, this.gY, this.gZ, this.ha, this.hb, this.hc, -1L, this.he, this.hf, this.hg);
    }

    public void d(Map<String, List<String>> map) {
        e(map);
        f(map);
        g(map);
        h(map);
        i(map);
        j(map);
        k(map);
    }
}

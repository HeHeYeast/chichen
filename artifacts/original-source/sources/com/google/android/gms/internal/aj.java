package com.google.android.gms.internal;

import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class aj implements ai {
    private static boolean a(Map<String, String> map) {
        return "1".equals(map.get("custom_close"));
    }

    private static int b(Map<String, String> map) {
        String str = map.get("o");
        if (str != null) {
            if ("p".equalsIgnoreCase(str)) {
                return ci.ao();
            }
            if ("l".equalsIgnoreCase(str)) {
                return ci.an();
            }
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.ai
    public void a(cq cqVar, Map<String, String> map) {
        String str = map.get("a");
        if (str == null) {
            cn.q("Action missing from an open GMSG.");
            return;
        }
        cr crVarAw = cqVar.aw();
        if ("expand".equalsIgnoreCase(str)) {
            if (cqVar.az()) {
                cn.q("Cannot expand WebView that is already expanded.");
                return;
            } else {
                crVarAw.a(a(map), b(map));
                return;
            }
        }
        if (!"webapp".equalsIgnoreCase(str)) {
            crVarAw.a(new be(map.get("i"), map.get("u"), map.get("m"), map.get("p"), map.get("c"), map.get("f"), map.get("e")));
            return;
        }
        String str2 = map.get("u");
        if (str2 != null) {
            crVarAw.a(a(map), b(map), str2);
        } else {
            crVarAw.a(a(map), b(map), map.get("html"), map.get("baseurl"));
        }
    }
}

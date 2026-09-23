package com.google.android.gms.internal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.util.HashMap;
import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ah {
    public static final ai ez = new ai() { // from class: com.google.android.gms.internal.ah.1
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            String str = map.get("urls");
            if (str == null) {
                cn.q("URLs missing in canOpenURLs GMSG.");
                return;
            }
            String[] strArrSplit = str.split(",");
            HashMap map2 = new HashMap();
            PackageManager packageManager = cqVar.getContext().getPackageManager();
            for (String str2 : strArrSplit) {
                String[] strArrSplit2 = str2.split(";", 2);
                map2.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(strArrSplit2.length > 1 ? strArrSplit2[1].trim() : "android.intent.action.VIEW", Uri.parse(strArrSplit2[0].trim())), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) != null));
            }
            cqVar.a("openableURLs", map2);
        }
    };
    public static final ai eA = new ai() { // from class: com.google.android.gms.internal.ah.2
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            h hVarAx;
            String str = map.get("u");
            if (str == null) {
                cn.q("URL missing from click GMSG.");
                return;
            }
            Uri uri = Uri.parse(str);
            try {
                hVarAx = cqVar.ax();
            } catch (i e) {
                cn.q("Unable to append parameter to URL: " + str);
            }
            Uri uriA = (hVarAx == null || !hVarAx.a(uri)) ? uri : hVarAx.a(uri, cqVar.getContext());
            new cl(cqVar.getContext(), cqVar.ay().hP, uriA.toString()).start();
        }
    };
    public static final ai eB = new ai() { // from class: com.google.android.gms.internal.ah.3
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            bf bfVarAu = cqVar.au();
            if (bfVarAu == null) {
                cn.q("A GMSG tried to close something that wasn't an overlay.");
            } else {
                bfVarAu.close();
            }
        }
    };
    public static final ai eC = new ai() { // from class: com.google.android.gms.internal.ah.4
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            bf bfVarAu = cqVar.au();
            if (bfVarAu == null) {
                cn.q("A GMSG tried to use a custom close button on something that wasn't an overlay.");
            } else {
                bfVarAu.d("1".equals(map.get("custom_close")));
            }
        }
    };
    public static final ai eD = new ai() { // from class: com.google.android.gms.internal.ah.5
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            String str = map.get("u");
            if (str == null) {
                cn.q("URL missing from httpTrack GMSG.");
            } else {
                new cl(cqVar.getContext(), cqVar.ay().hP, str).start();
            }
        }
    };
    public static final ai eE = new ai() { // from class: com.google.android.gms.internal.ah.6
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            cn.o("Received log message: " + map.get("string"));
        }
    };
    public static final ai eF = new aj();
    public static final ai eG = new ai() { // from class: com.google.android.gms.internal.ah.7
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) throws NumberFormatException {
            String str = map.get("tx");
            String str2 = map.get("ty");
            String str3 = map.get("td");
            try {
                int i = Integer.parseInt(str);
                int i2 = Integer.parseInt(str2);
                int i3 = Integer.parseInt(str3);
                h hVarAx = cqVar.ax();
                if (hVarAx != null) {
                    hVarAx.g().a(i, i2, i3);
                }
            } catch (NumberFormatException e) {
                cn.q("Could not parse touch parameters from gmsg.");
            }
        }
    };
    public static final ai eH = new ak();
}

package com.google.android.gms.internal;

import android.location.Location;
import android.text.TextUtils;
import c.Globalization;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ca {
    private static final SimpleDateFormat gS = new SimpleDateFormat("yyyyMMdd");

    public static String a(bu buVar, cd cdVar, Location location) throws JSONException {
        try {
            HashMap map = new HashMap();
            if (buVar.gA != null) {
                map.put("ad_pos", buVar.gA);
            }
            a((HashMap<String, Object>) map, buVar.gB);
            map.put("format", buVar.ed.ew);
            if (buVar.ed.width == -1) {
                map.put("smart_w", Globalization.FULL);
            }
            if (buVar.ed.height == -2) {
                map.put("smart_h", "auto");
            }
            map.put("slotname", buVar.adUnitId);
            map.put("pn", buVar.applicationInfo.packageName);
            if (buVar.gC != null) {
                map.put("vc", Integer.valueOf(buVar.gC.versionCode));
            }
            map.put("ms", buVar.gD);
            map.put("seq_num", buVar.gE);
            map.put("session_id", buVar.gF);
            map.put("js", buVar.eg.hP);
            a((HashMap<String, Object>) map, cdVar);
            a((HashMap<String, Object>) map, location);
            if (cn.k(2)) {
                cn.p("Ad Request JSON: " + ci.l(map).toString(2));
            }
            return ci.l(map).toString();
        } catch (JSONException e) {
            cn.q("Problem serializing ad request to JSON: " + e.getMessage());
            return null;
        }
    }

    private static void a(HashMap<String, Object> map, Location location) {
        if (location == null) {
            return;
        }
        HashMap map2 = new HashMap();
        Float fValueOf = Float.valueOf(location.getAccuracy() * 1000.0f);
        Long lValueOf = Long.valueOf(location.getTime() * 1000);
        Long lValueOf2 = Long.valueOf((long) (location.getLatitude() * 1.0E7d));
        Long lValueOf3 = Long.valueOf((long) (location.getLongitude() * 1.0E7d));
        map2.put("radius", fValueOf);
        map2.put("lat", lValueOf2);
        map2.put(Globalization.LONG, lValueOf3);
        map2.put(Globalization.TIME, lValueOf);
        map.put("loc", map2);
    }

    private static void a(HashMap<String, Object> map, cd cdVar) {
        map.put("am", Integer.valueOf(cdVar.hh));
        map.put("cog", g(cdVar.hi));
        map.put("coh", g(cdVar.hj));
        if (!TextUtils.isEmpty(cdVar.hk)) {
            map.put("carrier", cdVar.hk);
        }
        map.put("gl", cdVar.hl);
        if (cdVar.hm) {
            map.put("simulator", 1);
        }
        map.put("ma", g(cdVar.hn));
        map.put("sp", g(cdVar.ho));
        map.put("hl", cdVar.hp);
        if (!TextUtils.isEmpty(cdVar.hq)) {
            map.put("mv", cdVar.hq);
        }
        map.put("muv", Integer.valueOf(cdVar.hr));
        if (cdVar.hs != -2) {
            map.put("cnt", Integer.valueOf(cdVar.hs));
        }
        map.put("gnt", Integer.valueOf(cdVar.ht));
        map.put("pt", Integer.valueOf(cdVar.hu));
        map.put("rm", Integer.valueOf(cdVar.hv));
        map.put("riv", Integer.valueOf(cdVar.hw));
        map.put("u_sd", Float.valueOf(cdVar.hx));
        map.put("sh", Integer.valueOf(cdVar.hz));
        map.put("sw", Integer.valueOf(cdVar.hy));
    }

    private static void a(HashMap<String, Object> map, v vVar) {
        if (vVar.es != -1) {
            map.put("cust_age", gS.format(new Date(vVar.es)));
        }
        if (vVar.extras != null) {
            map.put("extras", vVar.extras);
        }
        if (vVar.et != -1) {
            map.put("cust_gender", Integer.valueOf(vVar.et));
        }
        if (vVar.eu != null) {
            map.put("kw", vVar.eu);
        }
        if (vVar.tagForChildDirectedTreatment != -1) {
            map.put("tag_for_child_directed_treatment", Integer.valueOf(vVar.tagForChildDirectedTreatment));
        }
        if (vVar.ev) {
            map.put("adtest", "on");
        }
    }

    private static Integer g(boolean z) {
        return Integer.valueOf(z ? 1 : 0);
    }
}

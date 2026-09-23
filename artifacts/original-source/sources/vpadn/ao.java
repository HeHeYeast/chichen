package vpadn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.ar;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ao {
    String a;
    String b;
    String e;

    /* renamed from: c, reason: collision with root package name */
    int f326c = 0;
    boolean d = false;
    List<ar.a> f = new ArrayList();
    Map<String, List<String>> g = new HashMap();

    public ao(String str) {
        this.a = str;
    }

    public final void a(JSONObject jSONObject) {
        try {
            if (jSONObject.has(ar.e)) {
                a(ar.e, jSONObject.getJSONArray(ar.e));
            }
        } catch (JSONException e) {
        }
        try {
            if (jSONObject.has(ar.f)) {
                a(ar.f, jSONObject.getJSONArray(ar.f));
            }
        } catch (JSONException e2) {
        }
        try {
            if (jSONObject.has(ar.g)) {
                a(ar.g, jSONObject.getJSONArray(ar.g));
            }
        } catch (JSONException e3) {
        }
        try {
            if (jSONObject.has(ar.h)) {
                a(ar.h, jSONObject.getJSONArray(ar.h));
            }
        } catch (JSONException e4) {
        }
        try {
            if (jSONObject.has(ar.i)) {
                a(ar.i, jSONObject.getJSONArray(ar.i));
            }
        } catch (JSONException e5) {
        }
    }

    void a(String str, JSONArray jSONArray) {
        int length;
        if (jSONArray != null && (length = jSONArray.length()) > 0) {
            List<String> arrayList = this.g.get(str);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.g.put(str, arrayList);
            }
            for (int i = 0; i < length; i++) {
                try {
                    arrayList.add(jSONArray.getString(i));
                } catch (Exception e) {
                    ab.b("PlayNextVideoData", "urlList.add(trackingUrlJsonArray.getString(i)) throw Exception");
                }
            }
        }
    }
}

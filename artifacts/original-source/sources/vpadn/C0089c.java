package vpadn;

import org.json.JSONArray;
import org.json.JSONException;

/* renamed from: vpadn.c, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0089c {
    public JSONArray a;

    public C0089c(JSONArray jSONArray) {
        this.a = jSONArray;
    }

    public final String a(int i) throws JSONException {
        return this.a.getString(0);
    }

    public final boolean b(int i) {
        return this.a.isNull(0);
    }
}

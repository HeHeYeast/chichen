package vpadn;

import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: vpadn.h, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0094h {
    public boolean a = false;
    public long b = 0;

    /* renamed from: c, reason: collision with root package name */
    public long f342c = 0;

    public final JSONObject a() throws JSONException {
        return new JSONObject("{loaded:" + this.b + ",total:" + this.f342c + ",lengthComputable:" + (this.a ? "true" : "false") + "}");
    }
}

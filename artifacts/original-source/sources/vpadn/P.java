package vpadn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class P {
    private static P a = new P();
    private Map<String, Object> b = Collections.synchronizedMap(new HashMap());

    /* renamed from: c, reason: collision with root package name */
    private Map<String, JSONObject> f314c = Collections.synchronizedMap(new HashMap());

    private P() {
    }

    public static P a() {
        return a;
    }

    public final Object a(String str) {
        return this.b.get(str);
    }

    public final void a(String str, Object obj) {
        this.b.put(str, obj);
    }

    public final void b(String str) {
        this.b.remove(str);
    }

    public final boolean c(String str) {
        return this.b.containsKey(str);
    }

    public final void a(String str, JSONObject jSONObject) {
        this.f314c.put(str, jSONObject);
    }

    public final JSONObject d(String str) {
        return this.f314c.get(str);
    }

    public final void e(String str) {
        this.f314c.remove(str);
    }
}

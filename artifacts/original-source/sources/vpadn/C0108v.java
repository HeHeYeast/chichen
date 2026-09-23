package vpadn;

import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: vpadn.v, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0108v {
    private static String[] f = {"No result", "OK", "Class not found", "Illegal access", "Instantiation error", "Malformed url", "IO error", "Invalid action", "JSON error", "Error"};
    private final int a;
    private final int b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f350c;
    private String d;
    private String e;

    /* renamed from: vpadn.v$a */
    public enum a {
        NO_RESULT,
        OK,
        CLASS_NOT_FOUND_EXCEPTION,
        ILLEGAL_ACCESS_EXCEPTION,
        INSTANTIATION_EXCEPTION,
        MALFORMED_URL_EXCEPTION,
        IO_EXCEPTION,
        INVALID_ACTION,
        JSON_EXCEPTION,
        ERROR;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static a[] valuesCustom() {
            a[] aVarArrValuesCustom = values();
            int length = aVarArrValuesCustom.length;
            a[] aVarArr = new a[length];
            System.arraycopy(aVarArrValuesCustom, 0, aVarArr, 0, length);
            return aVarArr;
        }
    }

    public C0108v(a aVar) {
        this(aVar, f[aVar.ordinal()]);
    }

    public C0108v(a aVar, String str) {
        this.f350c = false;
        this.a = aVar.ordinal();
        this.b = str == null ? 5 : 1;
        this.d = str;
    }

    public C0108v(a aVar, JSONArray jSONArray) {
        this.f350c = false;
        this.a = aVar.ordinal();
        this.b = 2;
        this.e = jSONArray.toString();
    }

    public C0108v(a aVar, JSONObject jSONObject) {
        this.f350c = false;
        this.a = aVar.ordinal();
        this.b = 2;
        this.e = jSONObject.toString();
    }

    public C0108v(a aVar, int i) {
        this.f350c = false;
        this.a = aVar.ordinal();
        this.b = 3;
        this.e = String.valueOf(i);
    }

    public C0108v(a aVar, float f2) {
        this.f350c = false;
        this.a = aVar.ordinal();
        this.b = 3;
        this.e = String.valueOf(f2);
    }

    public C0108v(a aVar, boolean z) {
        this.f350c = false;
        this.a = aVar.ordinal();
        this.b = 4;
        this.e = Boolean.toString(z);
    }

    public C0108v(a aVar, byte[] bArr) {
        this.f350c = false;
        this.a = aVar.ordinal();
        this.b = 6;
        this.e = Base64.encodeToString(bArr, 2);
    }

    public final void a(boolean z) {
        this.f350c = z;
    }

    public final int a() {
        return this.a;
    }

    public final int b() {
        return this.b;
    }

    public final String c() {
        if (this.e == null) {
            this.e = JSONObject.quote(this.d);
        }
        return this.e;
    }

    public final String d() {
        return this.d;
    }

    public final boolean e() {
        return this.f350c;
    }
}

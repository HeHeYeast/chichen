package vpadn;

import android.content.Intent;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class an extends af {
    private JSONObject a;
    private String b;

    /* renamed from: c, reason: collision with root package name */
    private as f325c;

    an(as asVar, JSONObject jSONObject, String str) {
        super(asVar, asVar.d(), str);
        this.a = jSONObject;
        this.f325c = asVar;
        if (this.a != null && this.a.has("tel")) {
            try {
                this.b = this.a.getString("tel");
            } catch (JSONException e) {
            }
        }
    }

    @Override // vpadn.af
    public final void b() {
        if (C0086a.c(this.b) || !this.b.startsWith("tel:")) {
            ab.b("PlaceCallCommand", "TEL number format is wrong");
            return;
        }
        try {
            this.b = this.b.replaceAll("\\s+", "");
            this.b = this.b.replaceAll("\\(", "");
            this.b = this.b.replaceAll("\\)", "");
            this.b = this.b.replaceAll("\\-", "");
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(this.b));
            intent.addFlags(268435456);
            this.f325c.d().startActivity(intent);
        } catch (Exception e) {
            ab.b("PlaceCallCommand", "PlaceCallCommand throw Exception!!");
        }
    }
}

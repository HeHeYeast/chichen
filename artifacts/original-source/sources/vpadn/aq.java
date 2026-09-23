package vpadn;

import android.content.Intent;
import android.net.Uri;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class aq extends af {
    private JSONObject a;
    private String b;

    /* renamed from: c, reason: collision with root package name */
    private String f327c;
    private as d;

    aq(as asVar, JSONObject jSONObject, String str) {
        super(asVar, asVar.d(), str);
        this.a = jSONObject;
        this.d = asVar;
        if (this.a != null && this.a.has("tel")) {
            try {
                this.b = this.a.getString("tel");
                this.f327c = this.a.getString("b");
                try {
                    this.f327c = URLDecoder.decode(this.f327c, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    ab.b("SendSMSCommand", "URLDecoder.decode(body, UTF-8); throw Exception body:" + this.f327c);
                }
            } catch (JSONException e2) {
            }
        }
    }

    @Override // vpadn.af
    public final void b() {
        if (C0086a.c(this.b) || C0086a.c(this.f327c)) {
            ab.b("SendSMSCommand", "TEL number format is wrong or body is empty");
            return;
        }
        try {
            this.b = this.b.replaceAll("\\s+", "");
            this.b = this.b.replaceAll("\\(", "");
            this.b = this.b.replaceAll("\\)", "");
            this.b = this.b.replaceAll("\\-", "");
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.putExtra("sms_body", this.f327c);
            intent.setData(Uri.parse("sms:" + this.b));
            intent.putExtra("address", this.b);
            intent.setType("vnd.android-dir/mms-sms");
            intent.addFlags(268435456);
            this.d.d().startActivity(intent);
        } catch (Exception e) {
            ab.b("SendSMSCommand", "SendSMSCommand throw Exception!!");
        }
    }
}

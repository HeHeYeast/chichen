package vpadn;

import android.content.Intent;
import com.google.android.gms.plus.PlusShare;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class aj extends af {
    private JSONObject a;
    private String b;

    /* renamed from: c, reason: collision with root package name */
    private String f322c;
    private String d;
    private String e;
    private String f;
    private Date g;
    private Date h;
    private DateFormat i;
    private as j;

    aj(as asVar, JSONObject jSONObject, String str) throws JSONException {
        super(asVar, asVar.d(), str);
        this.b = null;
        this.f322c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.a = jSONObject;
        this.j = asVar;
        try {
            this.i = new SimpleDateFormat(this, "yyyy-MM-dd'T'HH:mm:ssZ") { // from class: vpadn.aj.1
                @Override // java.text.SimpleDateFormat, java.text.DateFormat
                public final Date parse(String str2, ParsePosition parsePosition) {
                    return super.parse(str2.replaceFirst(":(?=[0-9]{2}$)", ""), parsePosition);
                }
            };
            JSONObject jSONObject2 = this.a.getJSONObject("e");
            if (jSONObject2 != null) {
                if (jSONObject2.has(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION)) {
                    this.f = jSONObject2.getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION);
                    this.f = a(this.f);
                }
                if (jSONObject2.has("location")) {
                    this.f322c = jSONObject2.getString("location");
                    this.f322c = a(this.f322c);
                }
                if (jSONObject2.has("start")) {
                    this.d = jSONObject2.getString("start");
                }
                if (jSONObject2.has("end")) {
                    this.e = jSONObject2.getString("end");
                }
                if (jSONObject2.has("summary")) {
                    this.b = jSONObject2.getString("summary");
                    this.b = a(this.b);
                }
            }
        } catch (JSONException e) {
        }
    }

    private static String a(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ab.a("CreateCalendarCommand", "URLDecoder.decode(body, UTF-8); throw Exception description:" + str, e);
            return str;
        }
    }

    @Override // vpadn.af
    final void b() {
        if (this.d == null || this.e == null) {
            ab.b("CreateCalendarCommand", "Cannot get start or end at CreateCalendarCommand");
            return;
        }
        if (this.f == null) {
            ab.b("CreateCalendarCommand", "Cannot get title (description) at CreateCalendarCommand");
            return;
        }
        try {
            this.g = this.i.parse(this.d);
            this.h = this.i.parse(this.e);
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            Intent intent = new Intent("android.intent.action.EDIT");
            intent.setType("vnd.android.cursor.item/event");
            gregorianCalendar.setTime(this.g);
            intent.putExtra("beginTime", gregorianCalendar.getTimeInMillis());
            gregorianCalendar.setTime(this.h);
            intent.putExtra("endTime", gregorianCalendar.getTimeInMillis());
            if (this.b != null) {
                intent.putExtra(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, this.b);
            }
            if (this.f322c != null) {
                intent.putExtra("eventLocation", this.f322c);
            }
            if (this.f != null) {
                intent.putExtra(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, this.f);
            }
            intent.addFlags(268435456);
            this.j.d().startActivity(intent);
        } catch (Exception e) {
            ab.a("CreateCalendarCommand", "doExecute() throw Exception at CreateCalendarCommand", e);
        }
    }
}

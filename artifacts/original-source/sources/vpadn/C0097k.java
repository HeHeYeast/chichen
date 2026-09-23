package vpadn;

import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: vpadn.k, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0097k extends Exception {
    private static final long serialVersionUID = 1;
    private int a;

    public C0097k() {
        this.a = 0;
    }

    public C0097k(String str) {
        this.a = 0;
        if (str.equalsIgnoreCase("FORMATTING_ERROR")) {
            this.a = 1;
        } else if (str.equalsIgnoreCase("PARSING_ERROR")) {
            this.a = 2;
        } else if (str.equalsIgnoreCase("PATTERN_ERROR")) {
            this.a = 3;
        }
    }

    public final JSONObject a() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", this.a);
            String str = "";
            switch (this.a) {
                case 0:
                    str = "UNKNOWN_ERROR";
                    break;
                case 1:
                    str = "FORMATTING_ERROR";
                    break;
                case 2:
                    str = "PARSING_ERROR";
                    break;
                case 3:
                    str = "PATTERN_ERROR";
                    break;
            }
            jSONObject.put("message", str);
        } catch (JSONException e) {
        }
        return jSONObject;
    }
}

package jp.co.imobile.sdkads.android;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class k extends l {
    private ArrayList d = new ArrayList();

    k() {
    }

    static k a(JSONObject jSONObject, k kVar) throws JSONException, y {
        Boolean boolValueOf;
        k kVar2 = null;
        try {
            if (jSONObject.has("conditions")) {
                kVar2 = new k();
                kVar2.a = jSONObject.getString("operator");
                kVar2.b = Boolean.valueOf(jSONObject.getBoolean("not"));
                JSONArray jSONArray = jSONObject.getJSONArray("conditions");
                for (int i = 0; i < jSONArray.length(); i++) {
                    k kVarA = a(jSONArray.getJSONObject(i), kVar2);
                    if (kVarA != null) {
                        kVar2.a(kVarA);
                    }
                }
            } else if (jSONObject.has("conditionDetails")) {
                n nVar = new n();
                nVar.a = jSONObject.getString("operator");
                nVar.b = Boolean.valueOf(jSONObject.getBoolean("not"));
                JSONArray jSONArray2 = jSONObject.getJSONArray("conditionDetails");
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    m mVar = new m();
                    JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                    mVar.a = false;
                    Boolean.valueOf(false);
                    switch (jSONObject2.getInt("ct")) {
                        case 1:
                            boolValueOf = Boolean.valueOf(r.a().b(jSONObject2.getString("nm")));
                            break;
                        case 2:
                            boolValueOf = Boolean.valueOf(r.a(jSONObject2.getString("nm")));
                            break;
                        default:
                            boolValueOf = false;
                            break;
                    }
                    Boolean boolValueOf2 = Boolean.valueOf(jSONObject2.getBoolean("contain"));
                    mVar.a = Boolean.valueOf((boolValueOf.booleanValue() && boolValueOf2.booleanValue()) || boolValueOf == boolValueOf2);
                    if (!jSONObject2.getBoolean("action")) {
                        mVar.a = Boolean.valueOf(!mVar.a.booleanValue());
                    }
                    nVar.a(mVar);
                }
                kVar.a(nVar);
            }
            return kVar2;
        } catch (JSONException e) {
            e.getMessage();
            x.b("Ad targeting condition format error.", "parse");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    private void a(l lVar) {
        this.d.add(lVar);
    }

    @Override // jp.co.imobile.sdkads.android.l
    final Boolean a() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.d.iterator();
        while (it.hasNext()) {
            arrayList.add(((l) it.next()).a());
        }
        return a(arrayList);
    }
}

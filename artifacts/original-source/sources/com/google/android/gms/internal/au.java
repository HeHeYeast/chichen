package com.google.android.gms.internal;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class au {
    public static List<String> a(JSONObject jSONObject, String str) throws JSONException {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(str);
        if (jSONArrayOptJSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(jSONArrayOptJSONArray.length());
        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
            arrayList.add(jSONArrayOptJSONArray.getString(i));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public static void a(Context context, String str, ce ceVar, String str2, boolean z, List<String> list) {
        String str3 = z ? "1" : "0";
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String strReplaceAll = it.next().replaceAll("@gw_adlocid@", str2).replaceAll("@gw_adnetrefresh@", str3).replaceAll("@gw_qdata@", ceVar.hA.eZ).replaceAll("@gw_sdkver@", str).replaceAll("@gw_sessid@", cf.hB).replaceAll("@gw_seqnum@", ceVar.gE);
            if (ceVar.fm != null) {
                strReplaceAll = strReplaceAll.replaceAll("@gw_adnetid@", ceVar.fm.eP).replaceAll("@gw_allocid@", ceVar.fm.eR);
            }
            new cl(context, str, strReplaceAll).start();
        }
    }
}

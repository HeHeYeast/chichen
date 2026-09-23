package c;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0104r;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class App extends C0103q {
    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException, NumberFormatException {
        C0108v.a aVar = C0108v.a.OK;
        try {
            if (str.equals("clearCache")) {
                clearCache();
            } else if (str.equals("show")) {
                this.cordova.a().runOnUiThread(new Runnable() { // from class: c.App.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        App.this.webView.a("spinner", (Object) "stop");
                    }
                });
            } else if (str.equals("loadUrl")) {
                loadUrl(jSONArray.getString(0), jSONArray.optJSONObject(1));
            } else if (!str.equals("cancelLoadUrl")) {
                if (str.equals("clearHistory")) {
                    clearHistory();
                } else if (str.equals("backHistory")) {
                    backHistory();
                } else if (str.equals("overrideButton")) {
                    overrideButton(jSONArray.getString(0), jSONArray.getBoolean(1));
                } else if (str.equals("overrideBackbutton")) {
                    overrideBackbutton(jSONArray.getBoolean(0));
                } else if (str.equals("exitApp")) {
                    exitApp();
                }
            }
            c0101o.a(new C0108v(aVar, ""));
            return true;
        } catch (JSONException e) {
            c0101o.a(new C0108v(C0108v.a.JSON_EXCEPTION));
            return false;
        }
    }

    public void clearCache() {
        this.webView.clearCache(true);
    }

    public void loadUrl(String str, JSONObject jSONObject) throws JSONException, NumberFormatException {
        boolean z;
        boolean z2;
        int i;
        C0104r.b("App", "App.loadUrl(" + str + "," + jSONObject + ")");
        HashMap map = new HashMap();
        if (jSONObject != null) {
            JSONArray jSONArrayNames = jSONObject.names();
            z = false;
            z2 = false;
            i = 0;
            for (int i2 = 0; i2 < jSONArrayNames.length(); i2++) {
                String string = jSONArrayNames.getString(i2);
                if (string.equals("wait")) {
                    i = jSONObject.getInt(string);
                } else if (string.equalsIgnoreCase("openexternal")) {
                    z2 = jSONObject.getBoolean(string);
                } else if (string.equalsIgnoreCase("clearhistory")) {
                    z = jSONObject.getBoolean(string);
                } else {
                    Object obj = jSONObject.get(string);
                    if (obj != null) {
                        if (obj.getClass().equals(String.class)) {
                            map.put(string, (String) obj);
                        } else if (obj.getClass().equals(Boolean.class)) {
                            map.put(string, (Boolean) obj);
                        } else if (obj.getClass().equals(Integer.class)) {
                            map.put(string, (Integer) obj);
                        }
                    }
                }
            }
        } else {
            z = false;
            z2 = false;
            i = 0;
        }
        if (i > 0) {
            try {
                synchronized (this) {
                    wait(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.webView.a(str, z2, z);
    }

    public void clearHistory() {
        this.webView.clearHistory();
    }

    public void backHistory() {
        this.cordova.a().runOnUiThread(new Runnable() { // from class: c.App.2
            @Override // java.lang.Runnable
            public final void run() throws NumberFormatException {
                App.this.webView.c();
            }
        });
    }

    public void overrideBackbutton(boolean z) {
        C0104r.c("App", "WARNING: Back Button Default Behaviour will be overridden.  The backbutton event will be fired!");
        this.webView.a(z);
    }

    public void overrideButton(String str, boolean z) {
        C0104r.c("DroidGap", "WARNING: Volume Button Default Behaviour will be overridden.  The volume event will be fired!");
        this.webView.e(str);
    }

    public boolean isBackbuttonOverridden() {
        return this.webView.d();
    }

    public void exitApp() {
        this.webView.a("exit", (Object) null);
    }
}

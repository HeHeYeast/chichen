package vpadn;

import android.app.Activity;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/* renamed from: vpadn.b, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0088b {

    /* renamed from: c, reason: collision with root package name */
    private static C0088b f337c = null;
    private ArrayList<Pattern> a = new ArrayList<>();
    private HashMap<String, Boolean> b = new HashMap<>();

    public static void a(Activity activity) {
        if (f337c == null) {
            f337c = new C0088b(activity);
        }
    }

    private C0088b() {
    }

    private C0088b(Activity activity) {
        Log.e("Config", "----->>Config constructor");
        if (activity == null) {
            C0104r.c("CordovaLog", "There is no activity. Is this on the lock screen?");
            return;
        }
        a("http://127.0.0.1*", false);
        a("*", false);
        C0104r.a("DEBUG");
        activity.getIntent().putExtra("useBrowserHistory", "false");
        activity.getIntent().putExtra("exit-on-suspend", "false");
    }

    private void a(String str, boolean z) {
        try {
            if (str.compareTo("*") == 0) {
                C0104r.b("Config", "Unlimited access to network resources");
                this.a.add(Pattern.compile(".*"));
            } else {
                if (z) {
                    if (str.startsWith("http")) {
                        this.a.add(Pattern.compile(str.replaceFirst("https?://", "^https?://(.*\\.)?")));
                    } else {
                        this.a.add(Pattern.compile("^https?://(.*\\.)?" + str));
                    }
                    C0104r.b("Config", "Origin to allow with subdomains: %s", str);
                    return;
                }
                if (str.startsWith("http")) {
                    this.a.add(Pattern.compile(str.replaceFirst("https?://", "^https?://")));
                } else {
                    this.a.add(Pattern.compile("^https?://" + str));
                }
                C0104r.b("Config", "Origin to allow: %s", str);
            }
        } catch (Exception e) {
            C0104r.b("Config", "Failed to add origin %s", str);
        }
    }

    public static boolean a(String str) {
        if (f337c == null) {
            return false;
        }
        if (f337c.b.get(str) != null) {
            return true;
        }
        Iterator<Pattern> it = f337c.a.iterator();
        while (it.hasNext()) {
            if (it.next().matcher(str).find()) {
                f337c.b.put(str, true);
                return true;
            }
        }
        return false;
    }
}

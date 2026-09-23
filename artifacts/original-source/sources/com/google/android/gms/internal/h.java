package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class h {
    private d dz;
    private String dw = "googleads.g.doubleclick.net";
    private String dx = "/pagead/ads";
    private String[] dy = {".doubleclick.net", ".googleadservices.com", ".googlesyndication.com"};
    private final c dA = new c();

    public h(d dVar) {
        this.dz = dVar;
    }

    private Uri a(Uri uri, Context context, String str, boolean z) throws i {
        try {
            if (uri.getQueryParameter("ms") != null) {
                throw new i("Query parameter already exists: ms");
            }
            return a(uri, "ms", z ? this.dz.a(context, str) : this.dz.a(context));
        } catch (UnsupportedOperationException e) {
            throw new i("Provided Uri is not in a valid state");
        }
    }

    private Uri a(Uri uri, String str, String str2) throws UnsupportedOperationException {
        String string = uri.toString();
        int iIndexOf = string.indexOf("&adurl");
        if (iIndexOf == -1) {
            iIndexOf = string.indexOf("?adurl");
        }
        return iIndexOf != -1 ? Uri.parse(string.substring(0, iIndexOf + 1) + str + "=" + str2 + "&" + string.substring(iIndexOf + 1)) : uri.buildUpon().appendQueryParameter(str, str2).build();
    }

    public Uri a(Uri uri, Context context) throws i {
        try {
            return a(uri, context, uri.getQueryParameter("ai"), true);
        } catch (UnsupportedOperationException e) {
            throw new i("Provided Uri is not in a valid state");
        }
    }

    public void a(MotionEvent motionEvent) {
        this.dz.a(motionEvent);
    }

    public boolean a(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }
        try {
            String host = uri.getHost();
            for (String str : this.dy) {
                if (host.endsWith(str)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public d g() {
        return this.dz;
    }
}

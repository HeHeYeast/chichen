package com.jirbo.adcolony;

import java.io.File;
import java.io.IOException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class f {
    static byte[] a = new byte[1024];
    String b;

    f(String str) {
        this.b = a.l.f.d + str;
    }

    y a() {
        return new y(this.b);
    }

    s b() {
        try {
            return new s(new x(this.b));
        } catch (IOException e) {
            return null;
        }
    }

    void a(String str) {
        y yVarA = a();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            yVarA.a(str.charAt(i));
        }
        yVarA.b();
    }

    void c() {
        new File(this.b).delete();
    }
}

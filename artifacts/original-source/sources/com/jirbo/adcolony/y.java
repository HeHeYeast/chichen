package com.jirbo.adcolony;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class y extends af {
    static final int a = 1024;
    String b;

    /* renamed from: c, reason: collision with root package name */
    OutputStream f266c;
    byte[] d;
    int e;
    int f;
    int g;

    y(String str) throws IOException {
        this.d = new byte[1024];
        this.e = 0;
        this.b = str;
        if (a.n != 0) {
            this.g = 23;
            this.f = this.g;
        }
        try {
            if (a.l != null && a.l.f != null) {
                a.l.f.b();
            }
            this.f266c = new FileOutputStream(str);
        } catch (IOException e) {
            a(e);
        }
    }

    y(String str, OutputStream outputStream) {
        this.d = new byte[1024];
        this.e = 0;
        this.b = str;
        this.f266c = outputStream;
    }

    @Override // com.jirbo.adcolony.af
    void a(char c2) throws IOException {
        this.d[this.e] = (byte) (this.f ^ c2);
        this.f += this.g;
        int i = this.e + 1;
        this.e = i;
        if (i == 1024) {
            a();
        }
    }

    void a() throws IOException {
        if (this.e > 0 && this.f266c != null) {
            try {
                this.f266c.write(this.d, 0, this.e);
                this.e = 0;
                this.f266c.flush();
            } catch (IOException e) {
                this.e = 0;
                a(e);
            }
        }
    }

    void b() throws IOException {
        a();
        try {
            if (this.f266c != null) {
                this.f266c.close();
                this.f266c = null;
            }
        } catch (IOException e) {
            this.f266c = null;
            a(e);
        }
    }

    void a(IOException iOException) throws IOException {
        l.d.a("Error writing \"").a(this.b).b((Object) "\":");
        l.d.b((Object) iOException.toString());
        b();
    }

    public static void a(String[] strArr) throws IOException {
        y yVar = new y("test.txt");
        yVar.b("A king who was mad at the time");
        yVar.b("Declared limerick writing a crime");
        yVar.i += 2;
        yVar.b("So late in the night");
        yVar.b("All the poets would write");
        yVar.i -= 2;
        yVar.b("Verses without any rhyme or meter");
        yVar.d();
        yVar.i += 4;
        yVar.b("David\nGerrold");
        yVar.i += 2;
        yVar.b(4.0d);
        yVar.i += 2;
        yVar.b(0.0d);
        yVar.i += 2;
        yVar.b(-100023.0d);
        yVar.i += 2;
        yVar.c(-6L);
        yVar.i += 2;
        yVar.c(0L);
        yVar.i += 2;
        yVar.c(234L);
        yVar.i += 2;
        yVar.c(Long.MIN_VALUE);
        yVar.i += 2;
        yVar.b(true);
        yVar.i += 2;
        yVar.b(false);
        yVar.i += 2;
        yVar.b();
    }
}

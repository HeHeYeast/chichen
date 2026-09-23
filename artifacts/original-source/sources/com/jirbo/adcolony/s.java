package com.jirbo.adcolony;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class s {
    char[] a;
    int b;

    /* renamed from: c, reason: collision with root package name */
    int f259c;

    s(String str) {
        this.f259c = str.length();
        StringBuilder sb = new StringBuilder(this.f259c);
        int i = 0;
        while (i < this.f259c) {
            char cCharAt = str.charAt(i);
            if ((cCharAt >= ' ' && cCharAt <= '~') || cCharAt == '\n') {
                sb.append(cCharAt);
            } else if ((cCharAt & 128) != 0) {
                if ((cCharAt & 224) == 192 && i + 1 < this.f259c) {
                    sb.append((char) (((cCharAt & 31) << 6) | (str.charAt(i + 1) & '?')));
                    i++;
                } else if (i + 2 < this.f259c) {
                    sb.append((char) (((cCharAt & 15) << 12) | ((str.charAt(i + 1) & '?') << 6) | (str.charAt(i + 2) & '?')));
                    i += 2;
                } else {
                    sb.append('?');
                }
            } else {
                sb.append(' ');
            }
            i++;
        }
        this.f259c = sb.length();
        this.a = new char[this.f259c];
        sb.getChars(0, this.f259c, this.a, 0);
    }

    s(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder(inputStream.available());
        int i = inputStream.read();
        while (i != -1) {
            if ((i >= 32 && i <= 126) || i == 10) {
                sb.append((char) i);
            } else if ((i & 128) != 0) {
                if ((i & 224) == 192) {
                    sb.append((char) (((i & 31) << 6) | (inputStream.read() & 63)));
                } else {
                    sb.append((char) (((i & 15) << 12) | ((inputStream.read() & 63) << 6) | (inputStream.read() & 63)));
                }
            } else {
                sb.append(' ');
            }
            i = inputStream.read();
        }
        inputStream.close();
        this.f259c = sb.length();
        this.a = new char[this.f259c];
        sb.getChars(0, this.f259c, this.a, 0);
    }

    boolean a() {
        return this.b < this.f259c;
    }

    char b() {
        if (this.b == this.f259c) {
            return (char) 0;
        }
        return this.a[this.b];
    }

    char c() {
        char[] cArr = this.a;
        int i = this.b;
        this.b = i + 1;
        return cArr[i];
    }

    boolean a(char c2) {
        if (this.b == this.f259c || this.a[this.b] != c2) {
            return false;
        }
        this.b++;
        return true;
    }

    void b(char c2) {
        if (!a(c2)) {
            throw new AdColonyException("'" + c2 + "' expected.");
        }
    }

    boolean a(String str) {
        int length = str.length();
        if (this.b + length > this.f259c) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) != this.a[this.b + i]) {
                return false;
            }
        }
        this.b += length;
        return true;
    }

    void b(String str) {
        if (!a(str)) {
            throw new AdColonyException("\"" + str + "\" expected.");
        }
    }

    void d() {
        while (this.b != this.f259c) {
            char c2 = this.a[this.b];
            if (c2 != ' ' && c2 != '\n') {
                return;
            } else {
                this.b++;
            }
        }
    }

    public static void a(String[] strArr) {
    }
}

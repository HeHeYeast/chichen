package com.jirbo.adcolony;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class x extends InputStream {
    InputStream a;
    byte[] b = new byte[1024];

    /* renamed from: c, reason: collision with root package name */
    int f265c;
    int d;
    int e;
    int f;
    int g;
    int h;

    x(String str) throws IOException {
        if (a.n != 0) {
            this.h = 23;
            this.g = 23;
        }
        this.f265c = (int) new File(str).length();
        this.a = new FileInputStream(str);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return (this.e - this.f) + this.a.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.a.close();
    }

    @Override // java.io.InputStream
    public void mark(int read_limit) {
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.d == this.f265c) {
            return -1;
        }
        if (this.f >= this.e) {
            a();
        }
        this.d++;
        byte[] bArr = this.b;
        int i = this.f;
        this.f = i + 1;
        return bArr[i];
    }

    @Override // java.io.InputStream
    public int read(byte[] array) throws IOException {
        return read(array, 0, array.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] array, int offset, int count) throws IOException {
        if (this.d == this.f265c) {
            return -1;
        }
        int count2 = this.f265c - this.d;
        if (count > count2) {
            count = count2;
        }
        int i = 0;
        while (count > 0) {
            if (this.f == this.e) {
                a();
            }
            int i2 = count < this.e ? count : this.e;
            int i3 = 0;
            int offset2 = offset;
            while (i3 < i2) {
                byte[] bArr = this.b;
                int i4 = this.f;
                this.f = i4 + 1;
                array[offset2] = bArr[i4];
                i3++;
                offset2++;
            }
            count -= i2;
            i += i2;
            this.d = i2 + this.d;
            offset = offset2;
        }
        return i;
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        throw new IOException("ADCStreamReader does not support reset().");
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        throw new IOException("ADCStreamReader does not support skip().");
    }

    void a() throws IOException {
        this.e = 0;
        while (this.e == 0) {
            this.e = this.a.read(this.b, 0, 1024);
        }
        for (int i = 0; i < this.e; i++) {
            this.b[i] = (byte) (this.b[i] ^ this.g);
            this.g += this.h;
        }
        this.f = 0;
    }
}

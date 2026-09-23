package com.google.android.gms.internal;

import java.io.IOException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class m implements k {
    private gl dD;
    private byte[] dE;
    private final int dF;

    public m(int i) {
        this.dF = i;
        reset();
    }

    @Override // com.google.android.gms.internal.k
    public void b(int i, long j) throws IOException {
        this.dD.b(i, j);
    }

    @Override // com.google.android.gms.internal.k
    public void b(int i, String str) throws IOException {
        this.dD.b(i, str);
    }

    @Override // com.google.android.gms.internal.k
    public byte[] h() throws IOException {
        int iEc = this.dD.ec();
        if (iEc < 0) {
            throw new IOException();
        }
        if (iEc == 0) {
            return this.dE;
        }
        byte[] bArr = new byte[this.dE.length - iEc];
        System.arraycopy(this.dE, 0, bArr, 0, bArr.length);
        return bArr;
    }

    @Override // com.google.android.gms.internal.k
    public void reset() {
        this.dE = new byte[this.dF];
        this.dD = gl.g(this.dE);
    }
}

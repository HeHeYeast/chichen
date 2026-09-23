package com.google.android.gms.internal;

import android.net.LocalSocket;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import com.google.android.gms.games.multiplayer.realtime.RealTimeSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class et implements RealTimeSocket {
    private ParcelFileDescriptor jN;
    private final String nd;
    private final LocalSocket nt;

    et(LocalSocket localSocket, String str) {
        this.nt = localSocket;
        this.nd = str;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.RealTimeSocket
    public void close() throws IOException {
        this.nt.close();
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.RealTimeSocket
    public InputStream getInputStream() throws IOException {
        return this.nt.getInputStream();
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.RealTimeSocket
    public OutputStream getOutputStream() throws IOException {
        return this.nt.getOutputStream();
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.RealTimeSocket
    public ParcelFileDescriptor getParcelFileDescriptor() throws IOException {
        if (this.jN == null && !isClosed()) {
            Parcel parcelObtain = Parcel.obtain();
            parcelObtain.writeFileDescriptor(this.nt.getFileDescriptor());
            parcelObtain.setDataPosition(0);
            this.jN = parcelObtain.readFileDescriptor();
        }
        return this.jN;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.RealTimeSocket
    public boolean isClosed() {
        return (this.nt.isConnected() || this.nt.isBound()) ? false : true;
    }
}

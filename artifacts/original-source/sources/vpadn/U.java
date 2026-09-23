package vpadn;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class U implements ServiceConnection {
    private boolean a;
    private final LinkedBlockingQueue<IBinder> b;

    private U() {
        this.a = false;
        this.b = new LinkedBlockingQueue<>(1);
    }

    public /* synthetic */ U(byte b) {
        this();
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) throws InterruptedException {
        try {
            this.b.put(iBinder);
        } catch (InterruptedException e) {
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
    }

    public final IBinder a() throws InterruptedException {
        if (this.a) {
            throw new IllegalStateException();
        }
        this.a = true;
        return this.b.take();
    }
}

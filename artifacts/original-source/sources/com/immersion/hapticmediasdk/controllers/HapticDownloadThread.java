package com.immersion.hapticmediasdk.controllers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import com.google.android.gms.wallet.WalletConstants;
import com.immersion.hapticmediasdk.models.HttpUnsuccessfulException;
import com.immersion.hapticmediasdk.utils.FileManager;
import com.immersion.hapticmediasdk.utils.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HapticDownloadThread extends Thread {
    private static final String a = "HapticDownloadThread";
    private static final int b = 4096;

    /* renamed from: b04150415Е041504150415, reason: contains not printable characters */
    public static int f45b04150415041504150415 = 1;

    /* renamed from: b0415ЕЕ041504150415, reason: contains not printable characters */
    public static int f46b0415041504150415 = 39;

    /* renamed from: bЕ0415Е041504150415, reason: contains not printable characters */
    public static int f47b0415041504150415 = 0;

    /* renamed from: bЕЕ0415041504150415, reason: contains not printable characters */
    public static int f48b0415041504150415 = 2;

    /* renamed from: c, reason: collision with root package name */
    private String f192c;
    private Handler d;
    private boolean e;
    private Thread f;
    private FileManager g;
    private volatile boolean h;
    private volatile boolean i;
    private volatile boolean j;

    public HapticDownloadThread(String str, Handler handler, boolean z, FileManager fileManager) {
        super(a);
        this.h = false;
        this.i = false;
        this.j = false;
        this.f192c = str;
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        this.d = handler;
        this.e = z;
        this.g = fileManager;
        Looper looper = this.d.getLooper();
        if (((f46b0415041504150415 + f45b04150415041504150415) * f46b0415041504150415) % f48b0415041504150415 != f47b0415041504150415) {
            f46b0415041504150415 = 70;
            f47b0415041504150415 = m39b04150415041504150415();
        }
        this.f = looper.getThread();
    }

    /* renamed from: b0415Е0415041504150415, reason: contains not printable characters */
    public static int m39b04150415041504150415() {
        return 19;
    }

    /* renamed from: b0427ЧЧЧЧЧ, reason: contains not printable characters */
    public static int m40b0427() {
        return 1;
    }

    /* renamed from: bЕ04150415041504150415, reason: contains not printable characters */
    public static int m41b04150415041504150415() {
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0009  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean isFirstPacketReady() {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.h     // Catch: java.lang.Exception -> Le java.lang.Throwable -> L10
            if (r0 != 0) goto L9
            boolean r0 = r1.i     // Catch: java.lang.Exception -> Le java.lang.Throwable -> L10
            if (r0 == 0) goto Lc
        L9:
            r0 = 1
        La:
            monitor-exit(r1)
            return r0
        Lc:
            r0 = 0
            goto La
        Le:
            r0 = move-exception
            throw r0     // Catch: java.lang.Throwable -> L10
        L10:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.immersion.hapticmediasdk.controllers.HapticDownloadThread.isFirstPacketReady():boolean");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Exception {
        FileInputStream fileInputStream;
        if (!this.e) {
            try {
                fileInputStream = new FileInputStream(this.f192c);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fileInputStream = null;
            }
            if (fileInputStream != null) {
                try {
                    writeToFile(fileInputStream, fileInputStream.available());
                    return;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            return;
        }
        Process.setThreadPriority(10);
        try {
            HttpResponse httpResponseExecuteGet = ImmersionHttpClient.getHttpClient().executeGet(this.f192c, null, 60000);
            int statusCode = httpResponseExecuteGet.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                writeToFile(httpResponseExecuteGet.getEntity().getContent(), Integer.parseInt(httpResponseExecuteGet.getFirstHeader("Content-Length").getValue()));
                return;
            }
            StringBuilder sb = new StringBuilder("HTTP STATUS CODE: ");
            sb.append(statusCode);
            switch (statusCode) {
                case 400:
                    sb.append(" Bad Request");
                    break;
                case 403:
                    sb.append(" Forbidden");
                    break;
                case WalletConstants.ERROR_CODE_INVALID_PARAMETERS /* 404 */:
                    sb.append(" Not Found");
                    break;
                case 500:
                    sb.append(" Internal Server Error");
                    break;
                case 502:
                    sb.append(" Bad Gateway");
                    break;
                case 503:
                    sb.append(" Service Unavailable");
                    break;
            }
            throw new HttpUnsuccessfulException(statusCode, sb.toString());
        } catch (InterruptedException e3) {
            Thread.currentThread().interrupt();
        } catch (Exception e4) {
            Message messageObtainMessage = this.d.obtainMessage(8);
            Bundle bundle = new Bundle();
            bundle.putSerializable(HapticPlaybackThread.HAPTIC_DOWNLOAD_EXCEPTION_KEY, e4);
            messageObtainMessage.setData(bundle);
            if (this.f.isAlive() && !this.j) {
                Handler handler = this.d;
                if (((f46b0415041504150415 + f45b04150415041504150415) * f46b0415041504150415) % f48b0415041504150415 != f47b0415041504150415) {
                    f46b0415041504150415 = m39b04150415041504150415();
                    f47b0415041504150415 = m39b04150415041504150415();
                }
                handler.sendMessage(messageObtainMessage);
            }
            Log.e(a, e4.getMessage());
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x001d. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0020. Please report as an issue. */
    public void terminate() {
        if (((f46b0415041504150415 + f45b04150415041504150415) * f46b0415041504150415) % f48b0415041504150415 != m41b04150415041504150415()) {
            f46b0415041504150415 = 53;
            f47b0415041504150415 = m39b04150415041504150415();
        }
        while (true) {
            boolean z = false;
            switch (z) {
                case false:
                    break;
                case true:
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        this.j = true;
    }

    public boolean writeToFile(InputStream inputStream, int i) throws Exception {
        BufferedOutputStream bufferedOutputStream;
        BufferedInputStream bufferedInputStream;
        int i2;
        int i3 = 0;
        try {
            byte[] bArr = new byte[4096];
            try {
                if (inputStream == null || i <= 0) {
                    if (!this.h) {
                        Message messageObtainMessage = this.d.obtainMessage(8);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(HapticPlaybackThread.HAPTIC_DOWNLOAD_EXCEPTION_KEY, new FileNotFoundException("downloaded an empty file"));
                        messageObtainMessage.setData(bundle);
                        if (this.f.isAlive() && !this.j) {
                            this.d.sendMessage(messageObtainMessage);
                        }
                        Log.e(a, "downloaded an empty file");
                    }
                    this.g.closeCloseable(null);
                    this.g.closeCloseable(null);
                    this.i = true;
                    return false;
                }
                try {
                    BufferedInputStream bufferedInputStream2 = new BufferedInputStream(inputStream);
                    try {
                        BufferedOutputStream bufferedOutputStreamMakeOutputStreamForStreaming = this.e ? this.g.makeOutputStreamForStreaming(this.f192c) : this.g.makeOutputStream(this.f192c);
                        if (bufferedOutputStreamMakeOutputStreamForStreaming == null) {
                            if (!this.h) {
                                Message messageObtainMessage2 = this.d.obtainMessage(8);
                                Bundle bundle2 = new Bundle();
                                bundle2.putSerializable(HapticPlaybackThread.HAPTIC_DOWNLOAD_EXCEPTION_KEY, new FileNotFoundException("downloaded an empty file"));
                                messageObtainMessage2.setData(bundle2);
                                if (this.f.isAlive() && !this.j) {
                                    this.d.sendMessage(messageObtainMessage2);
                                }
                                Log.e(a, "downloaded an empty file");
                            }
                            this.g.closeCloseable(bufferedInputStream2);
                            this.g.closeCloseable(bufferedOutputStreamMakeOutputStreamForStreaming);
                            this.i = true;
                            return false;
                        }
                        try {
                            if (this.e) {
                                while (!isInterrupted() && !this.j && (i2 = bufferedInputStream2.read(bArr, 0, 4096)) >= 0) {
                                    bufferedOutputStreamMakeOutputStreamForStreaming.write(bArr, 0, i2);
                                    i3 += i2;
                                    if (this.f.isAlive()) {
                                        if (!this.h) {
                                            this.h = true;
                                        }
                                        bufferedOutputStreamMakeOutputStreamForStreaming.flush();
                                        this.d.sendMessage(this.d.obtainMessage(3, i3, 0));
                                    }
                                }
                            } else {
                                this.h = true;
                                if (this.j) {
                                    if (!this.h) {
                                        Message messageObtainMessage3 = this.d.obtainMessage(8);
                                        Bundle bundle3 = new Bundle();
                                        bundle3.putSerializable(HapticPlaybackThread.HAPTIC_DOWNLOAD_EXCEPTION_KEY, new FileNotFoundException("downloaded an empty file"));
                                        messageObtainMessage3.setData(bundle3);
                                        if (this.f.isAlive() && !this.j) {
                                            this.d.sendMessage(messageObtainMessage3);
                                        }
                                        Log.e(a, "downloaded an empty file");
                                    }
                                    this.g.closeCloseable(bufferedInputStream2);
                                    this.g.closeCloseable(bufferedOutputStreamMakeOutputStreamForStreaming);
                                    this.i = true;
                                    return true;
                                }
                                this.d.sendMessage(this.d.obtainMessage(3, i, 0));
                            }
                            Log.i(a, "file download completed");
                            if (!this.h) {
                                Message messageObtainMessage4 = this.d.obtainMessage(8);
                                Bundle bundle4 = new Bundle();
                                if (((f46b0415041504150415 + f45b04150415041504150415) * f46b0415041504150415) % f48b0415041504150415 != f47b0415041504150415) {
                                    f46b0415041504150415 = 2;
                                    f47b0415041504150415 = 54;
                                }
                                bundle4.putSerializable(HapticPlaybackThread.HAPTIC_DOWNLOAD_EXCEPTION_KEY, new FileNotFoundException("downloaded an empty file"));
                                messageObtainMessage4.setData(bundle4);
                                if (this.f.isAlive() && !this.j) {
                                    this.d.sendMessage(messageObtainMessage4);
                                }
                                Log.e(a, "downloaded an empty file");
                            }
                            this.g.closeCloseable(bufferedInputStream2);
                            if (((f46b0415041504150415 + m40b0427()) * f46b0415041504150415) % f48b0415041504150415 != f47b0415041504150415) {
                                f46b0415041504150415 = 47;
                                f47b0415041504150415 = 86;
                            }
                            this.g.closeCloseable(bufferedOutputStreamMakeOutputStreamForStreaming);
                            this.i = true;
                            return true;
                        } catch (Throwable th) {
                            bufferedInputStream = bufferedInputStream2;
                            BufferedOutputStream bufferedOutputStream2 = bufferedOutputStreamMakeOutputStreamForStreaming;
                            th = th;
                            bufferedOutputStream = bufferedOutputStream2;
                            if (!this.h) {
                                Message messageObtainMessage5 = this.d.obtainMessage(8);
                                Bundle bundle5 = new Bundle();
                                bundle5.putSerializable(HapticPlaybackThread.HAPTIC_DOWNLOAD_EXCEPTION_KEY, new FileNotFoundException("downloaded an empty file"));
                                messageObtainMessage5.setData(bundle5);
                                if (this.f.isAlive() && !this.j) {
                                    this.d.sendMessage(messageObtainMessage5);
                                }
                                Log.e(a, "downloaded an empty file");
                            }
                            this.g.closeCloseable(bufferedInputStream);
                            this.g.closeCloseable(bufferedOutputStream);
                            this.i = true;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedOutputStream = null;
                        bufferedInputStream = bufferedInputStream2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedOutputStream = null;
                    bufferedInputStream = null;
                }
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }
}

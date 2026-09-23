package com.jirbo.adcolony;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class ADCDownload extends j implements Runnable {
    d a;
    Listener b;

    /* renamed from: c, reason: collision with root package name */
    String f200c;
    File d;
    Object e;
    String f;
    String g;
    boolean h;
    boolean i;
    boolean j;
    Map<String, List<String>> k;
    SSLContext l;
    int m;
    String n;

    public interface Listener {
        void on_download_finished(ADCDownload aDCDownload);
    }

    ADCDownload(d controller, String url, Listener listener) {
        this(controller, url, listener, null);
    }

    ADCDownload(d controller, String url, Listener listener, String filepath) {
        super(controller, false);
        this.f200c = url;
        this.b = listener;
        if (filepath != null) {
            this.d = new File(filepath);
        }
    }

    ADCDownload a(Object obj) {
        this.e = obj;
        return this;
    }

    ADCDownload a(String str, String str2) {
        this.f = str;
        this.g = str2;
        return this;
    }

    public void b() {
        aa.a(this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:163:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0096, code lost:
    
        if (r11.j == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0098, code lost:
    
        r3 = r4.getInputStream();
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x009d, code lost:
    
        r6 = new java.lang.StringBuilder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a4, code lost:
    
        if (r11.j == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a6, code lost:
    
        r0 = r4.getHeaderFields();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00aa, code lost:
    
        r11.k = r0;
        r4 = new byte[1024];
        r1 = r3.read(r4, 0, 1024);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b8, code lost:
    
        if (r1 == (-1)) goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00ba, code lost:
    
        r0 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00bb, code lost:
    
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00bd, code lost:
    
        if (r0 >= r1) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00bf, code lost:
    
        r6.append((char) r4[r0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00f0, code lost:
    
        r11.i = false;
        com.jirbo.adcolony.a.a(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00f5, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0113, code lost:
    
        r3 = r0.getInputStream();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0119, code lost:
    
        r0 = r0.getHeaderFields();
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x011e, code lost:
    
        r1 = r3.read(r4, 0, 1024);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0127, code lost:
    
        r3.close();
        r11.n = r6.toString();
        r11.m = r11.n.length();
        r11.i = true;
        com.jirbo.adcolony.a.a(r11);
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() throws java.lang.InterruptedException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 781
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jirbo.adcolony.ADCDownload.run():void");
    }

    @Override // com.jirbo.adcolony.j
    void a() {
        this.b.on_download_finished(this);
    }
}

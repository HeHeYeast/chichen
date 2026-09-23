package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import jp.co.imobile.sdkads.android.ImobileSdkAd;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
abstract class z {
    private static /* synthetic */ int[] x;
    private static /* synthetic */ int[] y;
    protected Date j;
    protected String m;
    protected String n;
    protected ImobileSdkAdListener p;
    protected ImobileSdkAdListener q;
    private volatile am u = am.NONE;
    private final Handler v = new Handler();
    private ImobileSdkAd.AdShowType w = null;
    protected String a = "";
    protected String b = "";

    /* renamed from: c, reason: collision with root package name */
    protected String f306c = "";
    protected int d = 0;
    protected int e = 0;
    protected int f = 0;
    protected int g = 0;
    protected int h = 0;
    protected int i = 0;
    protected int k = 0;
    protected int l = 0;
    protected ArrayList o = new ArrayList();
    protected CopyOnWriteArrayList r = new CopyOnWriteArrayList();
    protected AtomicBoolean s = new AtomicBoolean();
    protected ImobileSdkAdListener t = new aa(this);

    z() {
    }

    private static /* synthetic */ int[] n() {
        int[] iArr = x;
        if (iArr == null) {
            iArr = new int[am.a().length];
            try {
                iArr[am.ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[am.LODING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[am.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[am.PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[am.START.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[am.STOP.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            x = iArr;
        }
        return iArr;
    }

    private static /* synthetic */ int[] o() {
        int[] iArr = y;
        if (iArr == null) {
            iArr = new int[j.a().length];
            try {
                iArr[j.DISPLAING.ordinal()] = 7;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[j.DISPLAYABLE.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[j.DISPLAYED.ordinal()] = 6;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[j.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[j.EXPIRED.ordinal()] = 8;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[j.LODING.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[j.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[j.SCRIPT_ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e8) {
            }
            y = iArr;
        }
        return iArr;
    }

    final String a(ImobileIconParams imobileIconParams, Boolean bool) throws JSONException, y {
        JSONObject jSONObjectE = r.e();
        try {
            JSONObject jSONObject = jSONObjectE.getJSONObject("result");
            jSONObject.put("pid", this.a);
            jSONObject.put("mid", this.b);
            jSONObject.put("sid", this.f306c);
            jSONObject.put("test", ImobileSdkAd.b().toString());
            if (imobileIconParams != null) {
                jSONObject.put("iconParams", imobileIconParams.a());
            }
            if (bool.booleanValue()) {
                jSONObject.put(FluctConstants.XML_NODE_REFRESHTIME, 0);
            } else {
                jSONObject.put(FluctConstants.XML_NODE_REFRESHTIME, this.g);
            }
            jSONObjectE.put("status", "succeed");
            return jSONObjectE.toString();
        } catch (JSONException e) {
            e.getMessage();
            x.b("Spot data to ad view data create.", "parse");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    final am a() {
        return this.u;
    }

    abstract void a(Activity activity, ImobileSdkAdListener imobileSdkAdListener, Point point, Boolean bool, ViewGroup viewGroup, ImobileIconParams imobileIconParams, Boolean bool2);

    final void a(String str, String str2, String str3) {
        this.a = str;
        this.b = str2;
        this.f306c = str3;
    }

    public final void a(ImobileSdkAd.AdShowType adShowType) {
        this.w = adShowType;
    }

    final void a(ImobileSdkAdListener imobileSdkAdListener) {
        this.p = imobileSdkAdListener;
    }

    final void a(am amVar) {
        new StringBuilder("status : ").append(amVar);
        x.a(null);
        this.u = amVar;
        if (am.START == amVar && this.r.size() > 0) {
            Iterator it = this.r.iterator();
            while (it.hasNext()) {
                ((ImobileSdkAdListener) it.next()).onAdReadyCompleted();
            }
            this.r.clear();
        }
        new StringBuilder("Wait show ad execute.").append(amVar);
        x.a(null);
    }

    public final ImobileSdkAd.AdShowType b() {
        return this.w;
    }

    final void b(ImobileSdkAdListener imobileSdkAdListener) {
        if (imobileSdkAdListener != null) {
            this.r.add(imobileSdkAdListener);
        } else {
            this.r.clear();
        }
    }

    final String c() {
        return this.a;
    }

    final String d() {
        return this.b;
    }

    final String e() {
        return this.f306c;
    }

    final String f() {
        return this.m;
    }

    final String g() {
        return this.n;
    }

    final void h() {
        switch (n()[this.u.ordinal()]) {
            case 1:
            case 5:
            case 6:
                a(am.LODING);
                Executors.newCachedThreadPool().submit(new ak(this, Executors.newCachedThreadPool().submit(new aj(this, this)), this));
                break;
            case 4:
                a(am.START);
                break;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0083  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final void i() {
        /*
            r11 = this;
            r10 = 60
            r9 = 13
            r2 = 0
            r8 = 0
            r4 = 1
            int r0 = r11.k
            if (r0 == 0) goto L17
            jp.co.imobile.sdkads.android.am r0 = r11.u
            jp.co.imobile.sdkads.android.am r1 = jp.co.imobile.sdkads.android.am.STOP
            if (r0 == r1) goto L17
            jp.co.imobile.sdkads.android.am r0 = r11.u
            jp.co.imobile.sdkads.android.am r1 = jp.co.imobile.sdkads.android.am.ERROR
            if (r0 != r1) goto L18
        L17:
            return
        L18:
            java.util.Calendar r5 = java.util.Calendar.getInstance()
            java.util.ArrayList r0 = r11.o
            java.util.Iterator r6 = r0.iterator()
            r1 = r2
        L23:
            boolean r0 = r6.hasNext()
            if (r0 != 0) goto L4f
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "available ad:"
            r0.<init>(r2)
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "/"
            java.lang.StringBuilder r0 = r0.append(r1)
            int r1 = r11.k
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " on spot:"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = r11.f306c
            r0.append(r1)
            jp.co.imobile.sdkads.android.x.a(r8)
            goto L17
        L4f:
            java.lang.Object r0 = r6.next()
            jp.co.imobile.sdkads.android.f r0 = (jp.co.imobile.sdkads.android.f) r0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r7 = "ad status:"
            r3.<init>(r7)
            jp.co.imobile.sdkads.android.j r7 = r0.d()
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r7 = " on spot:"
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r7 = r11.f306c
            r3.append(r7)
            jp.co.imobile.sdkads.android.x.a(r8)
            int[] r3 = o()
            jp.co.imobile.sdkads.android.j r7 = r0.d()
            int r7 = r7.ordinal()
            r3 = r3[r7]
            switch(r3) {
                case 1: goto L8a;
                case 2: goto Lcd;
                case 3: goto Lb8;
                case 4: goto L8c;
                case 5: goto L8c;
                case 6: goto L8a;
                case 7: goto L83;
                case 8: goto L8a;
                default: goto L83;
            }
        L83:
            r3 = r2
        L84:
            if (r3 == 0) goto L23
            r0.a(r11)
            goto L23
        L8a:
            r3 = r4
            goto L84
        L8c:
            java.util.Date r3 = r0.c()
            r5.setTime(r3)
            r5.add(r9, r10)
            java.util.Date r3 = new java.util.Date
            r3.<init>()
            java.util.Date r7 = r5.getTime()
            int r3 = r3.compareTo(r7)
            if (r3 <= 0) goto La7
            r3 = r4
            goto L84
        La7:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r7 = "Error retry not reach time. on spot:"
            r3.<init>(r7)
            java.lang.String r7 = r11.f306c
            r3.append(r7)
            jp.co.imobile.sdkads.android.x.a(r8)
            r3 = r2
            goto L84
        Lb8:
            java.util.Date r3 = new java.util.Date
            r3.<init>()
            java.util.Date r7 = r0.b()
            int r3 = r3.compareTo(r7)
            if (r3 <= 0) goto Lc9
            r3 = r4
            goto L84
        Lc9:
            int r1 = r1 + 1
            r3 = r2
            goto L84
        Lcd:
            java.util.Date r3 = r0.c()
            r5.setTime(r3)
            r5.add(r9, r10)
            java.util.Date r3 = new java.util.Date
            r3.<init>()
            java.util.Date r7 = r5.getTime()
            int r3 = r3.compareTo(r7)
            if (r3 <= 0) goto L83
            r3 = r4
            goto L84
        */
        throw new UnsupportedOperationException("Method not decompiled: jp.co.imobile.sdkads.android.z.i():void");
    }

    final f j() {
        Iterator it = this.o.iterator();
        f fVar = null;
        while (it.hasNext()) {
            f fVar2 = (f) it.next();
            if (fVar2.d() == j.DISPLAYABLE) {
                if (new Date().compareTo(fVar2.b()) > 0) {
                    fVar2.a(j.EXPIRED);
                } else if (fVar == null) {
                    fVar = fVar2;
                } else if (fVar.b().after(fVar2.b())) {
                    fVar = fVar2;
                }
            }
        }
        return fVar;
    }

    abstract boolean k();

    abstract void l();

    abstract void m();
}

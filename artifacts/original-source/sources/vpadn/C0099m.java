package vpadn;

import android.os.Message;
import android.webkit.WebView;
import c.CordovaWebView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import vpadn.C0108v;

/* renamed from: vpadn.m, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0099m {
    private static int a = -1;
    private int b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f344c;
    private final LinkedList<b> d = new LinkedList<>();
    private final a[] e = new a[4];
    private final InterfaceC0102p f;
    private final CordovaWebView g;

    /* renamed from: vpadn.m$a */
    interface a {
        void a();
    }

    public C0099m(CordovaWebView cordovaWebView, InterfaceC0102p interfaceC0102p) {
        byte b2 = 0;
        this.f = interfaceC0102p;
        this.g = cordovaWebView;
        this.e[0] = null;
        this.e[1] = new c(this, b2);
        this.e[2] = new d();
        this.e[3] = new e(this, b2);
        a();
    }

    public final void a(int i) {
        if (i < 0 || i >= this.e.length) {
            ab.a("JsMessageQueue", "Invalid NativeToJsBridgeMode: " + i);
            return;
        }
        if (i != this.b) {
            ab.a("JsMessageQueue", "Set native->JS mode to " + i);
            synchronized (this) {
                this.b = i;
                a aVar = this.e[i];
                if (!this.f344c && !this.d.isEmpty() && aVar != null) {
                    aVar.a();
                }
            }
        }
    }

    public final void a() {
        synchronized (this) {
            this.d.clear();
            a(2);
        }
    }

    public final String b() {
        synchronized (this) {
            if (this.d.isEmpty()) {
                return null;
            }
            Iterator<b> it = this.d.iterator();
            int i = 0;
            int i2 = 0;
            while (it.hasNext()) {
                int iA = it.next().a();
                int length = iA + String.valueOf(iA).length() + 1;
                if (i > 0 && i2 + length > a && a > 0) {
                    break;
                }
                i2 += length;
                i++;
            }
            StringBuilder sb = new StringBuilder(i2);
            for (int i3 = 0; i3 < i; i3++) {
                b bVarRemoveFirst = this.d.removeFirst();
                sb.append(bVarRemoveFirst.a()).append(' ');
                if (bVarRemoveFirst.b != null) {
                    int iA2 = bVarRemoveFirst.b.a();
                    sb.append(((iA2 == C0108v.a.NO_RESULT.ordinal()) || (iA2 == C0108v.a.OK.ordinal())) ? 'S' : 'F').append(bVarRemoveFirst.b.e() ? '1' : '0').append(iA2).append(' ').append(bVarRemoveFirst.a).append(' ');
                    switch (bVarRemoveFirst.b.b()) {
                        case 1:
                            sb.append('s');
                            sb.append(bVarRemoveFirst.b.d());
                            break;
                        case 2:
                        default:
                            sb.append(bVarRemoveFirst.b.c());
                            break;
                        case 3:
                            sb.append('n').append(bVarRemoveFirst.b.c());
                            break;
                        case 4:
                            sb.append(bVarRemoveFirst.b.c().charAt(0));
                            break;
                        case 5:
                            sb.append('N');
                            break;
                        case 6:
                            sb.append('A');
                            sb.append(bVarRemoveFirst.b.c());
                            break;
                    }
                } else {
                    sb.append('J').append(bVarRemoveFirst.a);
                }
            }
            if (!this.d.isEmpty()) {
                sb.append('*');
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String c() {
        synchronized (this) {
            if (this.d.size() == 0) {
                return null;
            }
            Iterator<b> it = this.d.iterator();
            int i = 0;
            int i2 = 0;
            while (it.hasNext()) {
                int iA = it.next().a() + 50;
                if (i > 0 && i2 + iA > a && a > 0) {
                    break;
                }
                i2 += iA;
                i++;
            }
            boolean z = i == this.d.size();
            StringBuilder sb = new StringBuilder((z ? 0 : 100) + i2);
            for (int i3 = 0; i3 < i; i3++) {
                b bVarRemoveFirst = this.d.removeFirst();
                if (z && i3 + 1 == i) {
                    bVarRemoveFirst.a(sb);
                } else {
                    sb.append("try{");
                    bVarRemoveFirst.a(sb);
                    sb.append("}finally{");
                }
            }
            if (!z) {
                sb.append("window.setTimeout(function(){cordova.require('cordova/plugin/android/polling').pollOnce();},0);");
            }
            for (int i4 = z ? 1 : 0; i4 < i; i4++) {
                sb.append('}');
            }
            return sb.toString();
        }
    }

    public final void a(String str) {
        a(new b(str));
    }

    public final void a(C0108v c0108v, String str) {
        if (str == null) {
            ab.a("JsMessageQueue", "Got plugin result with no callbackId", new Throwable());
            return;
        }
        boolean z = c0108v.a() == C0108v.a.NO_RESULT.ordinal();
        boolean zE = c0108v.e();
        if (!z || !zE) {
            a(new b(c0108v, str));
        }
    }

    private void a(b bVar) {
        synchronized (this) {
            this.d.add(bVar);
            if (!this.f344c && this.e[this.b] != null) {
                this.e[this.b].a();
            }
        }
    }

    public final void a(boolean z) {
        if (this.f344c && z) {
            ab.a("JsMessageQueue", "nested call to setPaused detected.", new Throwable());
        }
        this.f344c = z;
        if (!z) {
            synchronized (this) {
                if (!this.d.isEmpty() && this.e[this.b] != null) {
                    this.e[this.b].a();
                }
            }
        }
    }

    /* renamed from: vpadn.m$c */
    class c implements a {
        private Runnable b;

        private c() {
            this.b = new Runnable() { // from class: vpadn.m.c.1
                @Override // java.lang.Runnable
                public final void run() {
                    String strC = C0099m.this.c();
                    if (strC != null) {
                        C0099m.this.g.b("javascript:" + strC);
                    }
                }
            };
        }

        /* synthetic */ c(C0099m c0099m, byte b) {
            this();
        }

        @Override // vpadn.C0099m.a
        public final void a() {
            C0099m.this.f.a().runOnUiThread(this.b);
        }
    }

    /* renamed from: vpadn.m$d */
    class d implements a {
        boolean a = true;

        /* renamed from: c, reason: collision with root package name */
        private Runnable f345c = new Runnable() { // from class: vpadn.m.d.1
            @Override // java.lang.Runnable
            public final void run() {
                if (!C0099m.this.d.isEmpty()) {
                    d.this.a = d.this.a ? false : true;
                    try {
                        C0099m.this.g.setNetworkAvailable(d.this.a);
                        if (!d.this.a) {
                            d.this.a = true;
                            C0099m.this.g.setNetworkAvailable(d.this.a);
                        }
                    } catch (Exception e) {
                        ab.b("JsMessageQueue", "OnlineEventsBridgeMode class throw Exception:" + e.getMessage(), e);
                    }
                }
            }
        };

        d() {
            try {
                C0099m.this.g.setNetworkAvailable(true);
            } catch (Exception e) {
                ab.b("JsMessageQueue", "OnlineEventsBridgeMode class throw Exception:" + e.getMessage(), e);
            }
        }

        @Override // vpadn.C0099m.a
        public final void a() {
            C0099m.this.f.a().runOnUiThread(this.f345c);
        }
    }

    /* renamed from: vpadn.m$e */
    class e implements a {
        private Method a;
        private Object b;

        /* renamed from: c, reason: collision with root package name */
        private boolean f346c;

        private e() {
        }

        /* synthetic */ e(C0099m c0099m, byte b) {
            this();
        }

        @Override // vpadn.C0099m.a
        public final void a() {
            Object obj;
            Class<?> cls;
            if (this.a == null && !this.f346c) {
                Object obj2 = C0099m.this.g;
                try {
                    Field declaredField = WebView.class.getDeclaredField("mProvider");
                    declaredField.setAccessible(true);
                    obj2 = declaredField.get(C0099m.this.g);
                    obj = obj2;
                    cls = obj2.getClass();
                } catch (Throwable th) {
                    obj = obj2;
                    cls = WebView.class;
                }
                try {
                    Field declaredField2 = cls.getDeclaredField("mWebViewCore");
                    declaredField2.setAccessible(true);
                    this.b = declaredField2.get(obj);
                    if (this.b != null) {
                        this.a = this.b.getClass().getDeclaredMethod("sendMessage", Message.class);
                        this.a.setAccessible(true);
                    }
                } catch (Throwable th2) {
                    this.f346c = true;
                    ab.a("JsMessageQueue", "PrivateApiBridgeMode failed to find the expected APIs.", th2);
                }
            }
            if (this.a != null) {
                try {
                    this.a.invoke(this.b, Message.obtain(null, 194, C0099m.this.c()));
                } catch (Throwable th3) {
                    ab.a("JsMessageQueue", "Reflection message bridge failed.", th3);
                }
            }
        }
    }

    /* renamed from: vpadn.m$b */
    static class b {
        final String a;
        final C0108v b;

        b(String str) {
            if (str == null) {
                throw new NullPointerException();
            }
            this.a = str;
            this.b = null;
        }

        b(C0108v c0108v, String str) {
            if (str == null || c0108v == null) {
                throw new NullPointerException();
            }
            this.a = str;
            this.b = c0108v;
        }

        final int a() {
            if (this.b == null) {
                return this.a.length() + 1;
            }
            int length = String.valueOf(this.b.a()).length() + 2 + 1 + this.a.length() + 1;
            switch (this.b.b()) {
                case 1:
                    return length + this.b.d().length() + 1;
                case 2:
                default:
                    return length + this.b.c().length();
                case 3:
                    return length + this.b.c().length() + 1;
                case 4:
                case 5:
                    return length + 1;
                case 6:
                    return length + this.b.c().length() + 1;
            }
        }

        final void a(StringBuilder sb) {
            if (this.b == null) {
                sb.append(this.a);
            } else {
                int iA = this.b.a();
                sb.append("cordova.callbackFromNative('").append(this.a).append("',").append(iA == C0108v.a.OK.ordinal() || iA == C0108v.a.NO_RESULT.ordinal()).append(",").append(iA).append(",").append(this.b.c()).append(",").append(this.b.e()).append(");");
            }
        }
    }
}

package c;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.plus.PlusShare;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0088b;
import vpadn.C0090d;
import vpadn.C0092f;
import vpadn.C0098l;
import vpadn.C0103q;
import vpadn.C0104r;
import vpadn.InterfaceC0102p;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class DroidGap extends Activity implements InterfaceC0102p {
    private static String e = "DroidGap";
    private static int h = 0;
    private static int i = 1;
    private static int j = 2;
    protected CordovaWebView a;
    protected Dialog d;
    private LinearLayout f;
    private boolean m;
    private String q;
    protected ProgressDialog b = null;
    private final ExecutorService g = Executors.newCachedThreadPool();
    private int k = 0;
    private C0103q l = null;
    private int n = FluctConstants.FRAME_ALPHA_COLOR;

    /* renamed from: c, reason: collision with root package name */
    protected int f166c = 0;
    private int o = GamesClient.STATUS_ACHIEVEMENT_UNLOCK_FAILURE;
    private boolean p = true;

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        C0088b.a(this);
        C0104r.b(e, "DroidGap.onCreate()");
        super.onCreate(bundle);
        if (bundle != null) {
            this.q = bundle.getString("callbackClass");
        }
        if (!a("showTitle", false)) {
            getWindow().requestFeature(1);
        }
        if (a("setFullscreen", false)) {
            getWindow().setFlags(1024, 1024);
        } else {
            getWindow().setFlags(2048, 2048);
        }
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        this.f = new LinearLayoutSoftKeyboardDetect(this, defaultDisplay.getWidth(), defaultDisplay.getHeight());
        this.f.setOrientation(1);
        this.f.setBackgroundColor(this.n);
        this.f.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, BitmapDescriptorFactory.HUE_RED));
        setVolumeControlStream(3);
    }

    @Override // vpadn.InterfaceC0102p
    public final Activity a() {
        return this;
    }

    public final void a(String str) throws NumberFormatException {
        if (this.a == null) {
            CordovaWebView cordovaWebView = new CordovaWebView(this);
            C0092f c0092f = Build.VERSION.SDK_INT < 11 ? new C0092f(this, cordovaWebView) : new C0098l(this, cordovaWebView);
            C0090d c0090d = new C0090d(this, cordovaWebView);
            C0104r.b(e, "DroidGap.init()");
            this.a = cordovaWebView;
            this.a.setId(100);
            this.a.setWebViewClient(c0092f);
            this.a.setWebChromeClient(c0090d);
            c0092f.a(this.a);
            c0090d.a(this.a);
            this.a.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1.0f));
            this.a.setVisibility(4);
            this.f.addView(this.a);
            setContentView(this.f);
        }
        this.n = a("backgroundColor", FluctConstants.FRAME_ALPHA_COLOR);
        this.f.setBackgroundColor(this.n);
        this.p = a("keepRunning", true);
        String strA = (this.a == null || !this.a.canGoBack()) ? a("loadingDialog", (String) null) : a("loadingPageDialog", (String) null);
        if (strA != null) {
            String strSubstring = "";
            if (strA.length() > 0) {
                int iIndexOf = strA.indexOf(44);
                if (iIndexOf > 0) {
                    strSubstring = strA.substring(0, iIndexOf);
                    strA = strA.substring(iIndexOf + 1);
                } else {
                    strSubstring = "";
                }
            } else {
                strA = "Loading Application...";
            }
            if (this.b != null) {
                this.b.dismiss();
                this.b = null;
            }
            this.b = ProgressDialog.show(this, strSubstring, strA, true, true, new DialogInterface.OnCancelListener(this) { // from class: c.DroidGap.1
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    this.b = null;
                }
            });
        }
        this.a.loadUrl(str);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    private boolean a(String str, boolean z) {
        Boolean bool;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                bool = (Boolean) extras.get(str);
            } catch (ClassCastException e2) {
                if ("true".equals(extras.get(str).toString())) {
                    bool = true;
                } else {
                    bool = false;
                }
            }
            return bool != null ? bool.booleanValue() : z;
        }
        return z;
    }

    public final int a(String str, int i2) {
        Integer numValueOf;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                numValueOf = (Integer) extras.get(str);
            } catch (ClassCastException e2) {
                numValueOf = Integer.valueOf(Integer.parseInt(extras.get(str).toString()));
            }
            return numValueOf != null ? numValueOf.intValue() : i2;
        }
        return i2;
    }

    private String a(String str, String str2) {
        String string;
        Bundle extras = getIntent().getExtras();
        if (extras == null || (string = extras.getString(str)) == null) {
            return null;
        }
        return string;
    }

    @Override // android.app.Activity
    protected void onPause() throws NumberFormatException {
        super.onPause();
        C0104r.b(e, "Paused the application!");
        if (this.k != j && this.a != null) {
            this.a.b(this.p);
            d();
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (this.a != null) {
            this.a.a(intent);
        }
    }

    @Override // android.app.Activity
    protected void onResume() throws NumberFormatException {
        super.onResume();
        C0104r.b(e, "Resuming the App");
        if (this.k == 0) {
            this.k = i;
            return;
        }
        if (this.a != null) {
            this.a.a(this.p, this.m);
            if ((!this.p || this.m) && this.m) {
                this.p = this.m;
                this.m = false;
            }
        }
    }

    @Override // android.app.Activity
    public void onDestroy() throws NumberFormatException {
        C0104r.b(e, "onDestroy()");
        super.onDestroy();
        d();
        if (this.a != null) {
            this.a.e();
        } else {
            c();
        }
    }

    private void b(String str, Object obj) {
        if (this.a != null) {
            this.a.a(str, obj);
        }
    }

    public final void b() {
        if (this.b != null && this.b.isShowing()) {
            this.b.dismiss();
            this.b = null;
        }
    }

    public final void c() {
        this.k = j;
        super.finish();
    }

    @Override // vpadn.InterfaceC0102p
    public final void a(C0103q c0103q, Intent intent, int i2) {
        this.l = c0103q;
        this.m = this.p;
        if (c0103q != null) {
            this.p = false;
        }
        super.startActivityForResult(intent, i2);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i2, int i3, Intent intent) {
        C0104r.b(e, "Incoming Result");
        super.onActivityResult(i2, i3, intent);
        Log.d(e, "Request code = " + i2);
        ValueCallback<Uri> valueCallbackA = this.a.a().a();
        if (i2 == 5173) {
            Log.d(e, "did we get here?");
            if (valueCallbackA != null) {
                Uri data = (intent == null || i3 != -1) ? null : intent.getData();
                Log.d(e, "result = " + data);
                valueCallbackA.onReceiveValue(data);
            } else {
                return;
            }
        }
        C0103q c0103q = this.l;
        if (c0103q == null) {
            if (this.q != null) {
                this.l = this.a.a.a(this.q);
                c0103q = this.l;
            } else {
                return;
            }
        }
        C0104r.b(e, "We have a callback to send this result to");
        c0103q.onActivityResult(i2, i3, intent);
    }

    public final void a(final String str, final String str2, final String str3, final boolean z) {
        runOnUiThread(new Runnable() { // from class: c.DroidGap.4
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(str2);
                    builder.setTitle(str);
                    builder.setCancelable(false);
                    String str4 = str3;
                    final boolean z2 = z;
                    final DroidGap droidGap = this;
                    builder.setPositiveButton(str4, new DialogInterface.OnClickListener(this) { // from class: c.DroidGap.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i2) {
                            dialogInterface.dismiss();
                            if (z2) {
                                droidGap.c();
                            }
                        }
                    });
                    builder.create();
                    builder.show();
                } catch (Exception e2) {
                    DroidGap.this.finish();
                }
            }
        });
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        b("onCreateOptionsMenu", menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        b("onPrepareOptionsMenu", menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        b("onOptionsItemSelected", menuItem);
        return true;
    }

    public final void d() {
        if (this.d != null && this.d.isShowing()) {
            this.d.dismiss();
            this.d = null;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i2, KeyEvent keyEvent) {
        return ((this.a.g() || this.a.getFocusedChild() != null) && i2 == 4) ? this.a.onKeyUp(i2, keyEvent) : super.onKeyUp(i2, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        return (this.a.getFocusedChild() == null || i2 != 4) ? super.onKeyDown(i2, keyEvent) : this.a.onKeyDown(i2, keyEvent);
    }

    @Override // vpadn.InterfaceC0102p
    public final Object a(String str, Object obj) throws JSONException {
        C0104r.b(e, "onMessage(" + str + "," + obj + ")");
        if ("splashscreen".equals(str)) {
            if ("hide".equals(obj.toString())) {
                d();
            } else if (this.d == null || !this.d.isShowing()) {
                this.f166c = a("splashscreen", 0);
                final int i2 = this.o;
                runOnUiThread(new Runnable() { // from class: c.DroidGap.5
                    @Override // java.lang.Runnable
                    public final void run() {
                        Display defaultDisplay = DroidGap.this.getWindowManager().getDefaultDisplay();
                        LinearLayout linearLayout = new LinearLayout(this.a());
                        linearLayout.setMinimumHeight(defaultDisplay.getHeight());
                        linearLayout.setMinimumWidth(defaultDisplay.getWidth());
                        linearLayout.setOrientation(1);
                        linearLayout.setBackgroundColor(this.a("backgroundColor", FluctConstants.FRAME_ALPHA_COLOR));
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, BitmapDescriptorFactory.HUE_RED));
                        linearLayout.setBackgroundResource(this.f166c);
                        DroidGap.this.d = new Dialog(this, 16973840);
                        if ((DroidGap.this.getWindow().getAttributes().flags & 1024) == 1024) {
                            DroidGap.this.d.getWindow().setFlags(1024, 1024);
                        }
                        DroidGap.this.d.setContentView(linearLayout);
                        DroidGap.this.d.setCancelable(false);
                        DroidGap.this.d.show();
                        new Handler().postDelayed(new Runnable() { // from class: c.DroidGap.5.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                DroidGap.this.d();
                            }
                        }, i2);
                    }
                });
            }
        } else if ("spinner".equals(str)) {
            if ("stop".equals(obj.toString())) {
                b();
                this.a.setVisibility(0);
            }
        } else if ("onReceivedError".equals(str)) {
            JSONObject jSONObject = (JSONObject) obj;
            try {
                int i3 = jSONObject.getInt("errorCode");
                final String string = jSONObject.getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION);
                final String string2 = jSONObject.getString("url");
                final String strA = a("errorUrl", (String) null);
                if (strA == null || (!(strA.startsWith("file://") || C0088b.a(strA)) || string2.equals(strA))) {
                    final boolean z = i3 != -2;
                    runOnUiThread(new Runnable(this) { // from class: c.DroidGap.3
                        @Override // java.lang.Runnable
                        public final void run() {
                            if (z) {
                                this.a.setVisibility(8);
                                this.a("Application Error", String.valueOf(string) + " (" + string2 + ")", "OK", z);
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable(this) { // from class: c.DroidGap.2
                        @Override // java.lang.Runnable
                        public final void run() throws NumberFormatException {
                            this.b();
                            this.a.a(strA, false, true);
                        }
                    });
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        } else if ("exit".equals(str)) {
            c();
        }
        return null;
    }

    @Override // vpadn.InterfaceC0102p
    public final ExecutorService e() {
        return this.g;
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.l != null) {
            bundle.putString("callbackClass", this.l.getClass().getName());
        }
    }
}

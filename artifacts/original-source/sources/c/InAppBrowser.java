package c;

import android.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0088b;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0104r;
import vpadn.C0108v;

@SuppressLint({"SetJavaScriptEnabled"})
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class InAppBrowser extends C0103q {
    protected static final String LOG_TAG = "InAppBrowser";
    private Dialog b;

    /* renamed from: c, reason: collision with root package name */
    private WebView f175c;
    private EditText d;
    private C0101o f;
    private long a = 104857600;
    private boolean e = true;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException, NumberFormatException {
        C0108v.a aVar;
        String strShowWebPage;
        HashMap<String, Boolean> map;
        C0108v.a aVar2 = C0108v.a.OK;
        this.f = c0101o;
        try {
            if (str.equals("open")) {
                String string = jSONArray.getString(0);
                String strOptString = jSONArray.optString(1);
                String str2 = (strOptString == null || strOptString.equals("") || strOptString.equals("null")) ? "_self" : strOptString;
                String strOptString2 = jSONArray.optString(2);
                if (strOptString2.equals("null")) {
                    map = null;
                } else {
                    HashMap<String, Boolean> map2 = new HashMap<>();
                    StringTokenizer stringTokenizer = new StringTokenizer(strOptString2, ",");
                    while (stringTokenizer.hasMoreElements()) {
                        StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), "=");
                        if (stringTokenizer2.hasMoreElements()) {
                            map2.put(stringTokenizer2.nextToken(), stringTokenizer2.nextToken().equals("no") ? Boolean.FALSE : Boolean.TRUE);
                        }
                    }
                    map = map2;
                }
                Log.d(LOG_TAG, "target = " + str2);
                String str3 = Uri.parse(string).isRelative() ? String.valueOf(this.webView.getUrl().substring(0, this.webView.getUrl().lastIndexOf("/") + 1)) + string : string;
                if ("_self".equals(str2)) {
                    Log.d(LOG_TAG, "in self");
                    if (str3.startsWith("file://") || str3.startsWith("javascript:") || C0088b.a(str3)) {
                        this.webView.loadUrl(str3);
                        strShowWebPage = "";
                        aVar = aVar2;
                    } else if (str3.startsWith("tel:")) {
                        try {
                            Intent intent = new Intent("android.intent.action.DIAL");
                            intent.setData(Uri.parse(str3));
                            this.cordova.a().startActivity(intent);
                            strShowWebPage = "";
                            aVar = aVar2;
                        } catch (ActivityNotFoundException e) {
                            C0104r.e(LOG_TAG, "Error dialing " + str3 + ": " + e.toString());
                            strShowWebPage = "";
                            aVar = aVar2;
                        }
                    } else {
                        strShowWebPage = showWebPage(str3, map);
                        aVar = aVar2;
                    }
                } else if ("_system".equals(str2)) {
                    Log.d(LOG_TAG, "in system");
                    strShowWebPage = openExternal(str3);
                    aVar = aVar2;
                } else {
                    Log.d(LOG_TAG, "in blank");
                    strShowWebPage = showWebPage(str3, map);
                    aVar = aVar2;
                }
            } else if (str.equals("close")) {
                a();
                C0108v c0108v = new C0108v(C0108v.a.OK);
                c0108v.a(false);
                this.f.a(c0108v);
                strShowWebPage = "";
                aVar = aVar2;
            } else {
                aVar = C0108v.a.INVALID_ACTION;
                strShowWebPage = "";
            }
            C0108v c0108v2 = new C0108v(aVar, strShowWebPage);
            c0108v2.a(true);
            this.f.a(c0108v2);
        } catch (JSONException e2) {
            this.f.a(new C0108v(C0108v.a.JSON_EXCEPTION));
        }
        return true;
    }

    public String openExternal(String str) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            this.cordova.a().startActivity(intent);
            return "";
        } catch (ActivityNotFoundException e) {
            Log.d(LOG_TAG, "InAppBrowser: Error loading url " + str + ":" + e.toString());
            return e.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "exit");
            a(jSONObject, false);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Should never happen");
        }
        if (this.b != null) {
            this.b.dismiss();
        }
    }

    static /* synthetic */ void c(InAppBrowser inAppBrowser) {
        if (inAppBrowser.f175c.canGoBack()) {
            inAppBrowser.f175c.goBack();
        }
    }

    static /* synthetic */ void d(InAppBrowser inAppBrowser) {
        if (inAppBrowser.f175c.canGoForward()) {
            inAppBrowser.f175c.goForward();
        }
    }

    static /* synthetic */ void a(InAppBrowser inAppBrowser, String str) {
        ((InputMethodManager) inAppBrowser.cordova.a().getSystemService("input_method")).hideSoftInputFromWindow(inAppBrowser.d.getWindowToken(), 0);
        if (str.startsWith("http") || str.startsWith("file:")) {
            inAppBrowser.f175c.loadUrl(str);
        } else {
            inAppBrowser.f175c.loadUrl("http://" + str);
        }
        inAppBrowser.f175c.requestFocus();
    }

    public String showWebPage(final String str, HashMap<String, Boolean> map) {
        this.e = true;
        if (map != null) {
            this.e = map.get("location").booleanValue();
        }
        final CordovaWebView cordovaWebView = this.webView;
        this.cordova.a().runOnUiThread(new Runnable() { // from class: c.InAppBrowser.1
            private int a(int i) {
                return (int) TypedValue.applyDimension(1, i, InAppBrowser.this.cordova.a().getResources().getDisplayMetrics());
            }

            @Override // java.lang.Runnable
            public final void run() {
                InAppBrowser.this.b = new Dialog(InAppBrowser.this.cordova.a(), R.style.Theme.NoTitleBar);
                InAppBrowser.this.b.getWindow().getAttributes().windowAnimations = R.style.Animation.Dialog;
                InAppBrowser.this.b.requestWindowFeature(1);
                InAppBrowser.this.b.setCancelable(true);
                InAppBrowser.this.b.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: c.InAppBrowser.1.1
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) throws JSONException {
                        try {
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put("type", "exit");
                            InAppBrowser.this.a(jSONObject, false);
                        } catch (JSONException e) {
                            Log.d(InAppBrowser.LOG_TAG, "Should never happen");
                        }
                    }
                });
                LinearLayout linearLayout = new LinearLayout(InAppBrowser.this.cordova.a());
                linearLayout.setOrientation(1);
                RelativeLayout relativeLayout = new RelativeLayout(InAppBrowser.this.cordova.a());
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, a(44)));
                relativeLayout.setPadding(a(2), a(2), a(2), a(2));
                relativeLayout.setHorizontalGravity(3);
                relativeLayout.setVerticalGravity(48);
                RelativeLayout relativeLayout2 = new RelativeLayout(InAppBrowser.this.cordova.a());
                relativeLayout2.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
                relativeLayout2.setHorizontalGravity(3);
                relativeLayout2.setVerticalGravity(16);
                relativeLayout2.setId(1);
                Button button = new Button(InAppBrowser.this.cordova.a());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -1);
                layoutParams.addRule(5);
                button.setLayoutParams(layoutParams);
                button.setContentDescription("Back Button");
                button.setId(2);
                button.setText("<");
                button.setOnClickListener(new View.OnClickListener() { // from class: c.InAppBrowser.1.2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        InAppBrowser.c(InAppBrowser.this);
                    }
                });
                Button button2 = new Button(InAppBrowser.this.cordova.a());
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -1);
                layoutParams2.addRule(1, 2);
                button2.setLayoutParams(layoutParams2);
                button2.setContentDescription("Forward Button");
                button2.setId(3);
                button2.setText(">");
                button2.setOnClickListener(new View.OnClickListener() { // from class: c.InAppBrowser.1.3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        InAppBrowser.d(InAppBrowser.this);
                    }
                });
                InAppBrowser.this.d = new EditText(InAppBrowser.this.cordova.a());
                RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -1);
                layoutParams3.addRule(1, 1);
                layoutParams3.addRule(0, 5);
                InAppBrowser.this.d.setLayoutParams(layoutParams3);
                InAppBrowser.this.d.setId(4);
                InAppBrowser.this.d.setSingleLine(true);
                InAppBrowser.this.d.setText(str);
                InAppBrowser.this.d.setInputType(16);
                InAppBrowser.this.d.setImeOptions(2);
                InAppBrowser.this.d.setInputType(0);
                InAppBrowser.this.d.setOnKeyListener(new View.OnKeyListener() { // from class: c.InAppBrowser.1.4
                    @Override // android.view.View.OnKeyListener
                    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() != 0 || i != 66) {
                            return false;
                        }
                        InAppBrowser.a(InAppBrowser.this, InAppBrowser.this.d.getText().toString());
                        return true;
                    }
                });
                Button button3 = new Button(InAppBrowser.this.cordova.a());
                RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-2, -1);
                layoutParams4.addRule(11);
                button3.setLayoutParams(layoutParams4);
                button2.setContentDescription("Close Button");
                button3.setId(5);
                button3.setText("Done");
                button3.setOnClickListener(new View.OnClickListener() { // from class: c.InAppBrowser.1.5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) throws JSONException {
                        InAppBrowser.this.a();
                    }
                });
                InAppBrowser.this.f175c = new WebView(InAppBrowser.this.cordova.a());
                InAppBrowser.this.f175c.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                InAppBrowser.this.f175c.setWebChromeClient(InAppBrowser.this.new b());
                InAppBrowser.this.f175c.setWebViewClient(InAppBrowser.this.new a(cordovaWebView, InAppBrowser.this.d));
                WebSettings settings = InAppBrowser.this.f175c.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setBuiltInZoomControls(true);
                settings.setDatabaseEnabled(true);
                settings.setDatabasePath(InAppBrowser.this.cordova.a().getApplicationContext().getDir("inAppBrowserDB", 0).getPath());
                settings.setDomStorageEnabled(true);
                InAppBrowser.this.f175c.loadUrl(str);
                InAppBrowser.this.f175c.setId(6);
                InAppBrowser.this.f175c.getSettings().setLoadWithOverviewMode(true);
                InAppBrowser.this.f175c.getSettings().setUseWideViewPort(true);
                InAppBrowser.this.f175c.requestFocus();
                InAppBrowser.this.f175c.requestFocusFromTouch();
                relativeLayout2.addView(button);
                relativeLayout2.addView(button2);
                relativeLayout.addView(relativeLayout2);
                relativeLayout.addView(InAppBrowser.this.d);
                relativeLayout.addView(button3);
                if (InAppBrowser.this.e) {
                    linearLayout.addView(relativeLayout);
                }
                linearLayout.addView(InAppBrowser.this.f175c);
                WindowManager.LayoutParams layoutParams5 = new WindowManager.LayoutParams();
                layoutParams5.copyFrom(InAppBrowser.this.b.getWindow().getAttributes());
                layoutParams5.width = -1;
                layoutParams5.height = -1;
                InAppBrowser.this.b.setContentView(linearLayout);
                InAppBrowser.this.b.show();
                InAppBrowser.this.b.getWindow().setAttributes(layoutParams5);
            }
        });
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(JSONObject jSONObject, boolean z) {
        C0108v c0108v = new C0108v(C0108v.a.OK, jSONObject);
        c0108v.a(z);
        this.f.a(c0108v);
    }

    public class b extends WebChromeClient {
        public b() {
        }

        @Override // android.webkit.WebChromeClient
        public final void onExceededDatabaseQuota(String str, String str2, long j, long j2, long j3, WebStorage.QuotaUpdater quotaUpdater) {
            C0104r.b(InAppBrowser.LOG_TAG, "onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", Long.valueOf(j2), Long.valueOf(j), Long.valueOf(j3));
            if (j2 < InAppBrowser.this.a) {
                C0104r.b(InAppBrowser.LOG_TAG, "calling quotaUpdater.updateQuota newQuota: %d", Long.valueOf(j2));
                quotaUpdater.updateQuota(j2);
            } else {
                quotaUpdater.updateQuota(j);
            }
        }
    }

    public class a extends WebViewClient {
        private EditText a;

        public a(CordovaWebView cordovaWebView, EditText editText) {
            this.a = editText;
        }

        @Override // android.webkit.WebViewClient
        public final void onPageStarted(WebView webView, String str, Bitmap bitmap) throws JSONException {
            super.onPageStarted(webView, str, bitmap);
            if (!str.startsWith("http:") && !str.startsWith("https:") && !str.startsWith("file:")) {
                str = "http://" + str;
            }
            if (!str.equals(this.a.getText().toString())) {
                this.a.setText(str);
            }
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", "loadstart");
                jSONObject.put("url", str);
                InAppBrowser.this.a(jSONObject, true);
            } catch (JSONException e) {
                Log.d(InAppBrowser.LOG_TAG, "Should never happen");
            }
        }

        @Override // android.webkit.WebViewClient
        public final void onPageFinished(WebView webView, String str) throws JSONException {
            super.onPageFinished(webView, str);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", "loadstop");
                jSONObject.put("url", str);
                InAppBrowser.this.a(jSONObject, true);
            } catch (JSONException e) {
                Log.d(InAppBrowser.LOG_TAG, "Should never happen");
            }
        }
    }
}

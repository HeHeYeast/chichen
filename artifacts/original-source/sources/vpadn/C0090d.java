package vpadn;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import c.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/* renamed from: vpadn.d, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class C0090d extends WebChromeClient {
    private String a = "CordovaLog";
    private long b = 104857600;

    /* renamed from: c, reason: collision with root package name */
    private InterfaceC0102p f338c;
    private CordovaWebView d;
    private View e;

    public C0090d(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        this.f338c = interfaceC0102p;
        this.d = cordovaWebView;
    }

    public final void a(CordovaWebView cordovaWebView) {
        this.d = cordovaWebView;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsAlert(WebView webView, String str, String str2, final JsResult jsResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.f338c.a());
        builder.setMessage(str2);
        builder.setTitle("Alert");
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(this) { // from class: vpadn.d.1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                jsResult.confirm();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener(this) { // from class: vpadn.d.2
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                jsResult.cancel();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener(this) { // from class: vpadn.d.3
            @Override // android.content.DialogInterface.OnKeyListener
            public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return true;
                }
                jsResult.confirm();
                return false;
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsConfirm(WebView webView, String str, String str2, final JsResult jsResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.f338c.a());
        builder.setMessage(str2);
        builder.setTitle("Confirm");
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(this) { // from class: vpadn.d.4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                jsResult.confirm();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(this) { // from class: vpadn.d.5
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                jsResult.cancel();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener(this) { // from class: vpadn.d.6
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                jsResult.cancel();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener(this) { // from class: vpadn.d.7
            @Override // android.content.DialogInterface.OnKeyListener
            public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return true;
                }
                jsResult.cancel();
                return false;
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, final JsPromptResult jsPromptResult) {
        boolean z = str.startsWith("file://") || str.indexOf(this.d.f160c) == 0 || C0088b.a(str);
        if (z && str3 != null && str3.length() > 3 && str3.substring(0, 4).equals("gap:")) {
            try {
                JSONArray jSONArray = new JSONArray(str3.substring(4));
                String strExec = this.d.g.exec(jSONArray.getString(0), jSONArray.getString(1), jSONArray.getString(2), str2);
                if (strExec == null) {
                    strExec = "";
                }
                jsPromptResult.confirm(strExec);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (z && str3 != null && str3.equals("gap_bridge_mode:")) {
            this.d.g.setNativeToJsBridgeMode(Integer.parseInt(str2));
            jsPromptResult.confirm("");
        } else if (z && str3 != null && str3.equals("gap_poll:")) {
            String strRetrieveJsMessages = this.d.g.retrieveJsMessages();
            if (strRetrieveJsMessages == null) {
                strRetrieveJsMessages = "";
            }
            jsPromptResult.confirm(strRetrieveJsMessages);
        } else if (str3 != null && str3.equals("gap_init:")) {
            jsPromptResult.confirm("OK");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.f338c.a());
            builder.setMessage(str2);
            final EditText editText = new EditText(this.f338c.a());
            if (str3 != null) {
                editText.setText(str3);
            }
            builder.setView(editText);
            builder.setCancelable(false);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(this) { // from class: vpadn.d.8
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    jsPromptResult.confirm(editText.getText().toString());
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(this) { // from class: vpadn.d.9
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    jsPromptResult.cancel();
                }
            });
            builder.create();
            builder.show();
        }
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public void onExceededDatabaseQuota(String str, String str2, long j, long j2, long j3, WebStorage.QuotaUpdater quotaUpdater) {
        C0104r.b(this.a, "DroidGap:  onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", Long.valueOf(j2), Long.valueOf(j), Long.valueOf(j3));
        if (j2 < this.b) {
            C0104r.b(this.a, "calling quotaUpdater.updateQuota newQuota: %d", Long.valueOf(j2));
            quotaUpdater.updateQuota(j2);
        } else {
            quotaUpdater.updateQuota(j);
        }
    }

    @Override // android.webkit.WebChromeClient
    public void onConsoleMessage(String str, int i, String str2) {
        if (Build.VERSION.SDK_INT == 7) {
            C0104r.b(this.a, "%s: Line %d : %s", str2, Integer.valueOf(i), str);
            super.onConsoleMessage(str, i, str2);
        }
    }

    @Override // android.webkit.WebChromeClient
    @TargetApi(8)
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (consoleMessage.message() != null) {
            C0104r.b(this.a, consoleMessage.message());
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override // android.webkit.WebChromeClient
    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(str, callback);
        callback.invoke(str, true, false);
    }

    @Override // android.webkit.WebChromeClient
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        this.d.a(view, customViewCallback);
    }

    @Override // android.webkit.WebChromeClient
    public void onHideCustomView() {
        this.d.f();
    }

    @Override // android.webkit.WebChromeClient
    public View getVideoLoadingProgressView() {
        if (this.e == null) {
            LinearLayout linearLayout = new LinearLayout(this.d.getContext());
            linearLayout.setOrientation(1);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            linearLayout.setLayoutParams(layoutParams);
            ProgressBar progressBar = new ProgressBar(this.d.getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.gravity = 17;
            progressBar.setLayoutParams(layoutParams2);
            linearLayout.addView(progressBar);
            this.e = linearLayout;
        }
        return this.e;
    }

    public final ValueCallback<Uri> a() {
        return null;
    }
}

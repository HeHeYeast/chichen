package com.jirbo.adcolony;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import c.Globalization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class m {
    WebView a;
    Activity b;

    /* renamed from: c, reason: collision with root package name */
    ADCVideo f227c;
    Handler d = new Handler();
    Runnable e = new Runnable() { // from class: com.jirbo.adcolony.m.1
        @Override // java.lang.Runnable
        public void run() {
            a.A = false;
        }
    };
    AdColonyAd f;
    String g;

    public m(ADCVideo aDCVideo, WebView webView, Activity activity) {
        this.a = webView;
        this.b = activity;
        this.f227c = aDCVideo;
    }

    void a(String str) {
        String str2;
        String[] strArr;
        String strReplace = str.replace("mraid://", "");
        if (strReplace.contains("?")) {
            String[] strArrSplit = strReplace.split("\\?");
            str2 = strArrSplit[0];
            strArr = strArrSplit;
        } else {
            str2 = strReplace;
            strArr = null;
        }
        String[] strArrSplit2 = strArr != null ? strArr[1].split("&") : new String[0];
        HashMap map = new HashMap();
        for (String str3 : strArrSplit2) {
            map.put(str3.split("=")[0], str3.split("=")[1]);
        }
        this.f = a.J;
        this.g = "{\"ad_slot\":" + this.f.h.k.d + "}";
        if (str2.equals("send_adc_event")) {
            b((String) map.get("type"));
        } else if (str2.equals("close")) {
            b();
        } else if (str2.equals("open_store") && !a.A) {
            c((String) map.get(Globalization.ITEM));
        } else if (str2.equals("open") && !a.A) {
            d((String) map.get("url"));
        } else if (str2.equals("expand")) {
            e((String) map.get("url"));
        } else if (str2.equals("create_calendar_event") && !a.A) {
            c(map);
        } else if (str2.equals("mail") && !a.A) {
            d(map);
        } else if (str2.equals("sms") && !a.A) {
            e(map);
        } else if (str2.equals("tel") && !a.A) {
            f(map);
        } else if (str2.equals("custom_event")) {
            g(map);
        } else if (str2.equals("launch_app") && !a.A) {
            h(map);
        } else if (str2.equals("check_app_presence")) {
            i(map);
        } else if (str2.equals("auto_play")) {
            j(map);
        } else if (str2.equals("save_screenshot")) {
            a();
        } else if (str2.equals("social_post") && !a.A) {
            b(map);
        } else if (str2.equals("make_in_app_purchase") && !a.A) {
            a(map);
        }
        f("adc_bridge.nativeCallComplete()");
    }

    void a(HashMap map) throws NumberFormatException {
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        a.a("html5_interaction", this.g, this.f227c.G);
        String strG = g((String) map.get("product"));
        Integer.parseInt(g((String) map.get("quantity")));
        this.b.finish();
        this.f227c.G.m = strG;
        this.f227c.G.u = AdColonyIAPEngagement.END_CARD;
        a.M.a(this.f227c.G);
    }

    void b(HashMap map) {
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        String strG = g((String) map.get("text"));
        String strG2 = g((String) map.get("url"));
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", strG + " " + strG2);
        this.b.startActivity(Intent.createChooser(intent, "Share this post using..."));
    }

    void a() throws IOException {
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        a.a("html5_interaction", this.g, this.f227c.G);
        String str = Environment.getExternalStorageDirectory().toString() + "/Pictures/AdColony_Screenshots/AdColony_Screenshot_" + System.currentTimeMillis() + ".jpg";
        View rootView = this.a.getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures");
        File file2 = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/AdColony_Screenshots");
        try {
            file.mkdir();
            file2.mkdir();
        } catch (Exception e) {
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
            bitmapCreateBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            MediaScannerConnection.scanFile(this.b, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.jirbo.adcolony.m.2
                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public void onScanCompleted(String path, Uri uri) {
                    Toast.makeText(m.this.b, "Screenshot saved to Gallery!", 0).show();
                }
            });
        } catch (FileNotFoundException e2) {
            Toast.makeText(this.b, "Error saving screenshot.", 0).show();
            l.a.a("ADC [info] FileNotFoundException in MRAIDCommandTakeScreenshot");
        } catch (IOException e3) {
            Toast.makeText(this.b, "Error saving screenshot.", 0).show();
            l.a.a("ADC [info] IOException in MRAIDCommandTakeScreenshot");
        }
    }

    void b(String str) {
        l.a.a("ADC [info] MRAIDCommandSendADCEvent called with type: ").b((Object) str);
        a.a(str, this.f227c.G);
    }

    void b() {
        l.a.b((Object) "ADC [info] MRAIDCommandClose called");
        this.b.finish();
        a.M.a(this.f227c.G);
    }

    void c(String str) {
        l.a.a("ADC [info] MRAIDCommandOpenStore called with item: ").b((Object) str);
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        a.a("html5_interaction", this.g, this.f227c.G);
        try {
            this.b.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(g(str))));
        } catch (Exception e) {
            Toast.makeText(this.b, "Unable to open store.", 0).show();
        }
    }

    void d(String str) {
        l.a.a("ADC [info] MRAIDCommandOpen called with url: ").b((Object) str);
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        String strG = g(str);
        if (strG.startsWith("adcvideo")) {
            this.f227c.a(strG.replace("adcvideo", "http"));
            return;
        }
        if (str.contains("youtube")) {
            try {
                this.b.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("vnd.youtube:" + strG.substring(strG.indexOf(118) + 2))));
                return;
            } catch (Exception e) {
                String strG2 = g(str);
                if (strG2.contains("safari")) {
                    strG2 = strG2.replace("safari", "http");
                }
                this.b.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strG2)));
                return;
            }
        }
        if (strG.startsWith(FluctConstants.XML_NODE_BROWSER)) {
            a.a("html5_interaction", this.f227c.G);
            this.b.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strG.replace(FluctConstants.XML_NODE_BROWSER, "http"))));
        } else {
            a.a("html5_interaction", this.g, this.f227c.G);
            AdColonyBrowser.url = strG;
            this.b.startActivity(new Intent(this.b, (Class<?>) AdColonyBrowser.class));
        }
    }

    void e(String str) {
        l.a.a("ADC [info] MRAIDCommandExpand called with url: ").b((Object) str);
        f("adc_bridge.fireChangeEvent({state:'expanded'});");
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:0|2|(4:4|(1:6)|70|7)|8|(1:10)|60|11|(2:66|12)|(3:64|14|15)(1:57)|62|16|(2:58|18)|(2:20|21)(9:29|(1:31)|32|(1:34)(2:42|(1:44)(2:45|(1:47)(2:48|(1:50)(1:56))))|35|(1:37)(1:51)|68|38|72)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x01d8, code lost:
    
        r4 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x02e9  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01bb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void c(java.util.HashMap r26) throws java.text.ParseException {
        /*
            Method dump skipped, instructions count: 748
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jirbo.adcolony.m.c(java.util.HashMap):void");
    }

    void d(HashMap map) {
        l.a.a("ADC [info] MRAIDCommandMail called with parameters: ").b(map);
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        String strG = g((String) map.get("subject"));
        String strG2 = g((String) map.get("body"));
        String strG3 = g((String) map.get("to"));
        a.a("html5_interaction", this.g, this.f227c.G);
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("plain/text");
            intent.putExtra("android.intent.extra.SUBJECT", strG).putExtra("android.intent.extra.TEXT", strG2).putExtra("android.intent.extra.EMAIL", new String[]{strG3});
            this.b.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.b, "Unable to launch email client.", 0).show();
        }
    }

    void e(HashMap map) {
        l.a.a("ADC [info] MRAIDCommandSMS called with parameters: ").b(map);
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        String strG = g((String) map.get("to"));
        String strG2 = g((String) map.get("body"));
        a.a("html5_interaction", this.g, this.f227c.G);
        try {
            this.b.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("sms:" + strG)).putExtra("sms_body", strG2));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.b, "Failed to create sms.", 0).show();
        }
    }

    void f(HashMap map) {
        l.a.a("ADC [info] MRAIDCommandTel called with parameters: ").b(map);
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        String strG = g((String) map.get(Globalization.NUMBER));
        a.a("html5_interaction", this.g, this.f227c.G);
        try {
            this.b.startActivity(new Intent("android.intent.action.DIAL").setData(Uri.parse("tel:" + strG)));
        } catch (Exception e) {
            Toast.makeText(this.b, "Failed to dial number.", 0).show();
        }
    }

    void g(HashMap map) {
        l.a.a("ADC [info] MRAIDCommandSendCustomADCEvent called with parameters: ").b(map);
        a.a("custom_event", "{\"event_type\":\"" + g((String) map.get("event_type")) + "\",\"ad_slot\":" + this.f.h.k.d + "}", this.f227c.G);
    }

    void h(HashMap map) {
        l.a.a("ADC [info] MRAIDCommandLaunchApp called with parameters: ").b(map);
        a.A = true;
        this.d.postDelayed(this.e, 1000L);
        String strG = g((String) map.get("handle"));
        a.a("html5_interaction", this.g, this.f227c.G);
        try {
            this.b.startActivity(this.b.getPackageManager().getLaunchIntentForPackage(strG));
        } catch (Exception e) {
            Toast.makeText(this.b, "Failed to launch external application.", 0).show();
        }
    }

    void i(HashMap map) throws PackageManager.NameNotFoundException {
        l.a.a("ADC [info] MRAIDCommandCheckAppPresence called with parameters: ").b(map);
        String strG = g((String) map.get("handle"));
        f("adc_bridge.fireAppPresenceEvent('" + strG + "'," + ab.a(strG) + ")");
    }

    void j(HashMap map) {
        l.a.a("ADC [info] MRAIDCommandCheckAutoPlay called with parameters: ").b(map);
    }

    void f(String str) {
        this.a.loadUrl("javascript:" + str);
    }

    String g(String str) {
        return str == null ? "" : URLDecoder.decode(str);
    }
}

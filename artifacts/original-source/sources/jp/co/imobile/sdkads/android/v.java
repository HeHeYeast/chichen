package jp.co.imobile.sdkads.android;

import android.net.Uri;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class v {
    v() {
    }

    private static Uri.Builder a(z zVar, String str) throws y {
        try {
            URL url = new URL(str);
            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();
            String path = url.getPath();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(protocol);
            if (port > 0) {
                builder.encodedAuthority(String.valueOf(host) + ":" + Integer.toString(port));
            } else {
                builder.encodedAuthority(host);
            }
            builder.path(path);
            builder.appendQueryParameter("pid", zVar.c());
            builder.appendQueryParameter("mid", zVar.d());
            builder.appendQueryParameter("asid", zVar.e());
            r.a().a(builder);
            builder.appendQueryParameter("test", Boolean.toString(ImobileSdkAd.b().booleanValue()));
            return builder;
        } catch (MalformedURLException e) {
            new StringBuilder("url MalformedURLException.").append(str);
            x.b("Network request uri format error.", "");
            throw new y(FailNotificationReason.PARAM);
        }
    }

    static JSONObject a(String str) {
        try {
            return d(c(str));
        } catch (y e) {
            new StringBuilder("send uri:").append(str);
            x.b("Network Imp Request send Error.", "");
            return null;
        }
    }

    static JSONObject a(z zVar) {
        return d(c(a(zVar, "http://spapi.i-mobile.co.jp/app/apiRich/ad_spot_environment.ashx").toString()));
    }

    static String b(String str) {
        try {
            return c(str).toString();
        } catch (y e) {
            new StringBuilder("send uri:").append(str);
            x.b("Network RequestFromHtml Request send Error.", "");
            return "error";
        }
    }

    static String c(String str) throws IOException, y {
        r.a();
        if (r.b().equals("")) {
            x.b("Network Condition.", "");
            throw new y(FailNotificationReason.NETWORK_NOT_READY);
        }
        new StringBuilder("Request uri:").append(str);
        x.a(null);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpParams params = defaultHttpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HapticContentSDK.f17b04440444044404440444);
        HttpConnectionParams.setSoTimeout(params, HapticContentSDK.f17b04440444044404440444);
        HttpGet httpGet = new HttpGet(str);
        try {
            HttpResponse httpResponseExecute = defaultHttpClient.execute(httpGet);
            String string = "";
            if (httpResponseExecute != null && httpResponseExecute.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponseExecute.getEntity();
                try {
                    try {
                        try {
                            string = EntityUtils.toString(entity, "utf-8");
                            x.a(null);
                        } finally {
                            try {
                                entity.consumeContent();
                            } catch (IOException e) {
                                new StringBuilder("IOException.").append(entity.toString());
                                x.a(e);
                            }
                        }
                    } catch (ParseException e2) {
                        new StringBuilder("httpEntity toString ParseException.").append(entity.toString());
                        x.b("Network response data error", "PARSE");
                        throw new y(FailNotificationReason.RESPONSE);
                    }
                } catch (IOException e3) {
                    new StringBuilder("IOException.").append(entity.toString());
                    x.b("Network response data error", "IO");
                    throw new y(FailNotificationReason.RESPONSE);
                }
            }
            defaultHttpClient.getConnectionManager().shutdown();
            return string;
        } catch (ClientProtocolException e4) {
            new StringBuilder("ClientProtocolException.").append(httpGet.toString());
            x.b("Network Connection error.", "Timeout.");
            throw new y(FailNotificationReason.NETWORK);
        } catch (IOException e5) {
            new StringBuilder("IOException.").append(httpGet.toString());
            x.b("SdkConnection requestSend Error", "Connection.");
            throw new y(FailNotificationReason.NETWORK);
        }
    }

    private static JSONObject d(String str) throws y {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            new StringBuilder("jsonObject ParseException.").append(str);
            x.b("SdkConnection requestSend response data error", "DATA");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }
}

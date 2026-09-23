package jp.co.imobile.sdkads.android;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class ak implements Runnable {
    final /* synthetic */ z a;
    private final /* synthetic */ Future b;

    /* renamed from: c, reason: collision with root package name */
    private final /* synthetic */ z f293c;

    ak(z zVar, Future future, z zVar2) {
        this.a = zVar;
        this.b = future;
        this.f293c = zVar2;
    }

    @Override // java.lang.Runnable
    public final void run() throws JSONException {
        try {
            JSONObject jSONObject = (JSONObject) this.b.get();
            if (jSONObject == null) {
                x.b("Spot response data Error from c.", "empty");
                this.a.t.onFailed(FailNotificationReason.RESPONSE);
            }
            try {
                if (!jSONObject.getString("error").equals("null")) {
                    if (jSONObject.getJSONObject("error").getString("code").equals("0002")) {
                        x.b("Ad response data Error from c.", "authority");
                        this.a.t.onFailed(FailNotificationReason.AUTHORITY);
                    } else {
                        x.b("Ad response data Error from c.", "response");
                        this.a.t.onFailed(FailNotificationReason.RESPONSE);
                    }
                }
                JSONObject jSONObject2 = jSONObject.getJSONObject("result");
                this.a.f = jSONObject2.getInt("intervalTime");
                new StringBuilder("from spot info value:").append(jSONObject2.getInt("intervalTime"));
                x.a(null);
                this.a.d = jSONObject2.getInt("skipCount");
                new StringBuilder("from spot info value:").append(jSONObject2.getInt("skipCount"));
                x.a(null);
                this.a.g = jSONObject2.getInt(FluctConstants.XML_NODE_REFRESHTIME);
                new StringBuilder("from spot info value:").append(jSONObject2.getInt(FluctConstants.XML_NODE_REFRESHTIME));
                x.a(null);
                z zVar = this.a;
                r.a();
                zVar.h = r.a(jSONObject2.getInt("displayWidth"));
                z zVar2 = this.a;
                r.a();
                zVar2.i = r.a(jSONObject2.getInt("displayHeight"));
                new StringBuilder("from spot info value: width : ").append(jSONObject2.getInt("displayWidth")).append("height : ").append(jSONObject2.getInt("displayHeight"));
                x.a(null);
                this.a.k = jSONObject2.getInt("stockCount");
                new StringBuilder("from spot info value:").append(jSONObject2.getInt("stockCount"));
                x.a(null);
                this.a.l = jSONObject2.getInt("adReadDelayTime");
                new StringBuilder("from spot info value:").append(jSONObject2.getInt("adReadDelayTime"));
                x.a(null);
                this.a.m = jSONObject2.getString("templateRequestUrl");
                new StringBuilder("from spot info value:").append(jSONObject2.getString("templateRequestUrl"));
                x.a(null);
                this.a.n = v.c(this.f293c.f());
            } catch (y e) {
                e.getMessage();
                x.b("Ad response data Error from h.", "data");
                this.a.t.onFailed(e.a());
            } catch (JSONException e2) {
                e2.getMessage();
                x.b("Ad response data Error from j.", "parse");
                this.a.t.onFailed(FailNotificationReason.RESPONSE);
            }
            if (this.a.a() == am.LODING) {
                this.a.v.post(new al(this, this.f293c));
            }
        } catch (InterruptedException e3) {
            this.a.a(am.ERROR);
            new StringBuilder("Callable InterruptedException.").append(e3.getMessage());
            x.b("Ad request get ad data.", "Interrupt");
            this.a.t.onFailed(FailNotificationReason.UNKNOWN);
        } catch (ExecutionException e4) {
            this.a.a(am.ERROR);
            if (e4.getCause().getClass() != y.class) {
                new StringBuilder("Callable ExecutionException.").append(e4.getMessage());
                x.b("Ad request get ad data.", "Execution");
                this.a.t.onFailed(FailNotificationReason.UNKNOWN);
            } else {
                y yVar = (y) e4.getCause();
                new StringBuilder("Callable NotificationException. reason:").append(yVar.a());
                x.b("Ad request get ad data.", "Notification");
                this.a.t.onFailed(yVar.a());
            }
        }
    }
}

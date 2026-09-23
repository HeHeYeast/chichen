package jp.co.imobile.sdkads.android;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class i implements Runnable {
    final /* synthetic */ f a;
    private final /* synthetic */ Future b;

    /* renamed from: c, reason: collision with root package name */
    private final /* synthetic */ String f299c;

    i(f fVar, Future future, String str) {
        this.a = fVar;
        this.b = future;
        this.f299c = str;
    }

    @Override // java.lang.Runnable
    public final void run() throws JSONException {
        try {
            JSONObject jSONObject = (JSONObject) this.b.get();
            if (jSONObject == null) {
                x.b("Ad View imp send not complete.", "ErroCode(no response).");
                return;
            }
            try {
                this.a.b.clear();
                JSONArray jSONArray = jSONObject.getJSONObject("result").getJSONArray("viewHashes");
                for (int i = 0; i < jSONArray.length() - 1; i++) {
                    this.a.b.put(jSONArray.getJSONObject(i).getString("advertisementId"), jSONArray.getJSONObject(i).getString("viewHash"));
                }
            } catch (JSONException e) {
                new StringBuilder("parse error value:").append(this.f299c);
                x.b("Ad response error.", "imp");
            }
            new StringBuilder("viewHash:").append(jSONObject.toString());
            x.b("Ad View imp send complete.", "");
        } catch (InterruptedException e2) {
            new StringBuilder("Callable InterruptedException.").append(e2.getMessage());
            x.b("Ad View imp send not complete.", "Interrupt");
        } catch (ExecutionException e3) {
            if (e3.getCause().getClass() != y.class) {
                new StringBuilder("Callable ExecutionException.").append(e3.getMessage());
                x.b("Ad View imp send not complete.", "Execution");
            } else {
                new StringBuilder("Callable NotificationException. reason:").append(((y) e3.getCause()).a());
                x.b("Ad View imp send not complete.", "Notification");
            }
        }
    }
}

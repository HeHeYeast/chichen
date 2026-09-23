package vpadn;

import android.app.Activity;
import android.os.AsyncTask;
import com.google.android.gms.games.GamesClient;
import java.text.DecimalFormat;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class af implements ag {
    private static String a = "AbstractClickTrackingButtonCommand";
    private Activity b;

    /* renamed from: c, reason: collision with root package name */
    private String f321c;
    private as d;

    abstract void b();

    public af(as asVar, Activity activity, String str) {
        this.d = asVar;
        this.b = activity;
        this.f321c = str;
    }

    @Override // vpadn.ag
    public final void a() {
        if (this.f321c != null && this.b != null) {
            ar arVarJ = this.d.j();
            if (arVarJ != null) {
                if (this.f321c.contains("{CurrentTime}")) {
                    this.f321c = this.f321c.replace("{CurrentTime}", new DecimalFormat("#.##").format(this.d.k() / 1000.0d));
                }
                if (this.f321c.contains("{TotalTime}")) {
                    this.f321c = this.f321c.replace("{TotalTime}", new DecimalFormat("#.##").format(this.d.l() / 1000.0d));
                }
                if (this.f321c.contains("{Vpadn-Guid}")) {
                    this.f321c = this.f321c.replace("{Vpadn-Guid}", arVarJ.p());
                }
                if (this.f321c.contains("{Vpadn-Sid}")) {
                    this.f321c = this.f321c.replace("{Vpadn-Sid}", new StringBuilder().append(arVarJ.n()).toString());
                }
                if (this.f321c.contains("{Vpadn-Seq}")) {
                    this.f321c = this.f321c.replace("{Vpadn-Seq}", new StringBuilder().append(arVarJ.o()).toString());
                }
            }
            ab.a(a, "Button trackingUrl:" + this.f321c);
            final String str = this.f321c;
            try {
                if (C0086a.c(str)) {
                    ab.b(a, "sendButtonTrackingUrl StringUtils.isBlank(url) is True");
                } else if (str.toLowerCase().startsWith("http://") || str.toLowerCase().startsWith("https://")) {
                    this.b.runOnUiThread(new Runnable(this) { // from class: vpadn.af.1
                        /* JADX WARN: Type inference failed for: r0v1, types: [vpadn.af$1$1] */
                        @Override // java.lang.Runnable
                        public final void run() {
                            try {
                                final String str2 = str;
                                new AsyncTask<Object, Integer, Integer>(this) { // from class: vpadn.af.1.1
                                    private int a = -1;

                                    @Override // android.os.AsyncTask
                                    protected final /* synthetic */ Integer doInBackground(Object... objArr) {
                                        return a();
                                    }

                                    private Integer a() throws Throwable {
                                        try {
                                            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                                            C0086a.a(defaultHttpClient);
                                            C0086a.a(str2, defaultHttpClient);
                                            ab.a(af.a, "sendButtonTrackingUrl timeout ms:3000");
                                            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(GamesClient.STATUS_ACHIEVEMENT_UNLOCK_FAILURE));
                                            Object objA = P.a().a("user-agent");
                                            if (objA != null) {
                                                ab.a(af.a, "userAgent:" + objA);
                                                defaultHttpClient.getParams().setParameter("http.useragent", objA);
                                            } else {
                                                ab.b(af.a, "Cannot get user agent from StaticStorage.instance().get(StaticStorage.USER_AGENT) in sendButtonTrackingUrl");
                                            }
                                            HttpResponse httpResponseExecute = defaultHttpClient.execute(new HttpGet(str2));
                                            C0086a.b(str2, defaultHttpClient);
                                            this.a = httpResponseExecute.getStatusLine().getStatusCode();
                                            ab.a(af.a, "sendButtonTrackingUrl return status code:" + this.a);
                                            return 1;
                                        } catch (Exception e) {
                                            try {
                                                ab.a(af.a, "sendButtonTrackingUrl throw Exception:" + e.getMessage(), e);
                                            } catch (Exception e2) {
                                            }
                                            return 1;
                                        }
                                    }
                                }.execute(new Object[0]);
                            } catch (Exception e) {
                                ab.a(af.a, "sendButtonTrackingUrl throw Exception:", e);
                            }
                        }
                    });
                } else {
                    ab.b(a, "!url.toLowerCase().startsWith(http://) && !url.toLowerCase().startsWith(https://)");
                }
            } catch (Exception e) {
                ab.a(a, "throw exception at sendButtonTrackingUrl Exception:" + e.getMessage(), e);
            }
        } else {
            ab.a(a, "Cannot find Button trackingUrl");
        }
        b();
    }
}

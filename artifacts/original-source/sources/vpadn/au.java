package vpadn;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import com.google.android.gms.games.GamesClient;
import com.vpadn.widget.VpadnActivity;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class au {
    MediaPlayer a;
    ar b;
    int h;
    int i;
    int j;
    private VpadnActivity o;

    /* renamed from: c, reason: collision with root package name */
    List<String> f334c = new ArrayList();
    List<String> d = new ArrayList();
    List<String> e = new ArrayList();
    List<String> f = new ArrayList();
    List<String> g = new ArrayList();
    boolean k = false;
    String l = null;
    int m = 3;
    int n = 0;

    public au(VpadnActivity vpadnActivity) {
        this.o = vpadnActivity;
    }

    public final void a(ar arVar, MediaPlayer mediaPlayer) {
        if (arVar.e() != null && arVar.x().isEmpty()) {
            ab.d("VideoTrackingManager", "Use old method for video tracking!!");
            ab.d("VideoTrackingManager", "videoData.getTrackingUrl():" + arVar.e() + " videoData.getTrackingInterval()" + arVar.f());
            this.k = true;
        }
        if (arVar.e() == null && arVar.x().isEmpty()) {
            ab.b("VideoTrackingManager", "videoData.getTrackingUrl() == null && videoData.getTrackingDataMap().isEmpty()");
            return;
        }
        this.b = arVar;
        this.a = mediaPlayer;
        if (this.k) {
            this.l = arVar.e();
            if (arVar.f() > 0) {
                this.m = arVar.f();
                return;
            } else {
                ab.b("VideoTrackingManager", "videoData.getTrackingInterval():" + arVar.f());
                return;
            }
        }
        a();
    }

    private void a() {
        try {
            int duration = this.a.getDuration() / 4;
            this.h = duration;
            this.i = this.h + duration;
            this.j = duration + this.i;
            this.f334c.clear();
            this.d.clear();
            this.e.clear();
            this.f.clear();
            this.g.clear();
            List<String> listK = this.b.k(ar.e);
            if (listK != null && listK.size() > 0) {
                Iterator<String> it = this.b.k(ar.e).iterator();
                while (it.hasNext()) {
                    this.f334c.add(it.next());
                }
            }
            List<String> listK2 = this.b.k(ar.f);
            if (listK2 != null && listK2.size() > 0) {
                Iterator<String> it2 = this.b.k(ar.f).iterator();
                while (it2.hasNext()) {
                    this.d.add(it2.next());
                }
            }
            List<String> listK3 = this.b.k(ar.g);
            if (listK3 != null && listK3.size() > 0) {
                Iterator<String> it3 = this.b.k(ar.g).iterator();
                while (it3.hasNext()) {
                    this.e.add(it3.next());
                }
            }
            List<String> listK4 = this.b.k(ar.h);
            if (listK4 != null && listK4.size() > 0) {
                Iterator<String> it4 = this.b.k(ar.h).iterator();
                while (it4.hasNext()) {
                    this.f.add(it4.next());
                }
            }
            List<String> listK5 = this.b.k(ar.i);
            if (listK5 != null && listK5.size() > 0) {
                Iterator<String> it5 = this.b.k(ar.i).iterator();
                while (it5.hasNext()) {
                    this.g.add(it5.next());
                }
            }
        } catch (Exception e) {
            ab.a("VideoTrackingManager", "init throws Exception", e);
        }
    }

    String a(String str) {
        Exception e;
        String strReplace;
        try {
            String str2 = new DecimalFormat("#.##").format(this.a.getCurrentPosition() / 1000.0d);
            String str3 = new DecimalFormat("#.##").format(this.a.getDuration() / 1000.0d);
            if (str.contains("{CurrentTime}")) {
                str = str.replace("{CurrentTime}", str2);
            }
            if (str.contains("{TotalTime}")) {
                str = str.replace("{TotalTime}", str3);
            }
            if (str.contains("{Vpadn-Guid}")) {
                str = str.replace("{Vpadn-Guid}", this.b.p());
            }
            strReplace = str.contains("{Vpadn-Sid}") ? str.replace("{Vpadn-Sid}", new StringBuilder().append(this.b.n()).toString()) : str;
        } catch (Exception e2) {
            e = e2;
            strReplace = str;
        }
        try {
            if (strReplace.contains("{Vpadn-Seq}")) {
                return strReplace.replace("{Vpadn-Seq}", new StringBuilder().append(this.b.o()).toString());
            }
            return strReplace;
        } catch (Exception e3) {
            e = e3;
            ab.a("VideoTrackingManager", "replaceTrackingUrl throw Exception", e);
            return strReplace;
        }
    }

    void b(final String str) {
        try {
            if (C0086a.c(str)) {
                ab.b("VideoTrackingManager", "sendHttpGet StringUtils.isBlank(url) is True");
            } else if (!str.toLowerCase().startsWith("http://") && !str.toLowerCase().startsWith("https://")) {
                ab.b("VideoTrackingManager", "!url.toLowerCase().startsWith(http://) && !url.toLowerCase().startsWith(https://)");
            } else {
                this.o.runOnUiThread(new Runnable(this) { // from class: vpadn.au.1
                    /* JADX WARN: Type inference failed for: r0v1, types: [vpadn.au$1$1] */
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            final String str2 = str;
                            new AsyncTask<Object, Integer, Integer>(this) { // from class: vpadn.au.1.1
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
                                        ab.c("VideoTrackingManager", "timeout ms:3000");
                                        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(GamesClient.STATUS_ACHIEVEMENT_UNLOCK_FAILURE));
                                        Object objA = P.a().a("user-agent");
                                        if (objA != null) {
                                            ab.c("VideoTrackingManager", "userAgent:" + objA);
                                            defaultHttpClient.getParams().setParameter("http.useragent", objA);
                                        } else {
                                            ab.b("VideoTrackingManager", "Cannot get user agent from StaticStorage.instance().get(StaticStorage.USER_AGENT)");
                                        }
                                        HttpResponse httpResponseExecute = defaultHttpClient.execute(new HttpGet(str2));
                                        C0086a.b(str2, defaultHttpClient);
                                        this.a = httpResponseExecute.getStatusLine().getStatusCode();
                                        if (this.a > 399) {
                                            ab.b("VideoTrackingManager", "sendHttpGet return status code:" + this.a);
                                        }
                                        return 1;
                                    } catch (Exception e) {
                                        try {
                                            ab.a("VideoTrackingManager", "sendHttpGet throw Exception:" + e.getMessage(), e);
                                        } catch (Exception e2) {
                                        }
                                        return 1;
                                    }
                                }
                            }.execute(new Object[0]);
                        } catch (Exception e) {
                            ab.a("VideoTrackingManager", "sendHttpGet throw Exception:", e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            ab.a("VideoTrackingManager", "throw exception at sendHttpGet Exception:" + e.getMessage(), e);
        }
    }
}

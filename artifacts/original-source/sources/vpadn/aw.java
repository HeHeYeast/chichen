package vpadn;

import org.apache.http.client.HttpClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class aw implements Runnable {
    private final /* synthetic */ HttpClient a;

    public aw(HttpClient httpClient) {
        this.a = httpClient;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.a != null && this.a.getConnectionManager() != null) {
            this.a.getConnectionManager().shutdown();
        }
    }
}

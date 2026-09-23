package jp.co.imobile.sdkads.android;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class u implements Runnable {
    private final /* synthetic */ Future a;
    private final /* synthetic */ String b;

    /* renamed from: c, reason: collision with root package name */
    private final /* synthetic */ e f304c;

    u(Future future, String str, e eVar) {
        this.a = future;
        this.b = str;
        this.f304c = eVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            String str = (String) this.a.get();
            if (this.b.equals("")) {
                new StringBuilder("response:").append(str);
                x.b("Send request from html complete.", "");
            } else {
                this.f304c.a("javascript:" + this.b + "('" + str + "');");
                new StringBuilder("callbackFunctionName:").append(this.b).append("response:").append(str);
                x.b("Send request from html complete.", "");
            }
        } catch (InterruptedException e) {
            new StringBuilder("Callable InterruptedException.").append(e.getMessage());
            x.b("Send request from html not complete.", "Interrupt");
        } catch (ExecutionException e2) {
            if (e2.getCause().getClass() != y.class) {
                new StringBuilder("Callable ExecutionException.").append(e2.getMessage());
                x.b("Send request from html not complete.", "Execution");
            } else {
                new StringBuilder("Callable NotificationException. reason:").append(((y) e2.getCause()).a());
                x.b("Send request from html not complete.", "Notification");
            }
        }
    }
}

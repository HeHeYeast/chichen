package vpadn;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class O {

    /* renamed from: c, reason: collision with root package name */
    private static O f313c = new O();
    private long a = System.currentTimeMillis();
    private long b = 0;

    private O() {
    }

    public static O a() {
        return f313c;
    }

    public final synchronized long b() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.a > 1800000) {
            this.a = jCurrentTimeMillis;
            this.b = 0L;
        }
        return this.a;
    }

    public final synchronized long c() {
        long j;
        j = this.b;
        this.b = 1 + j;
        return j;
    }
}

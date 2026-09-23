package com.jirbo.adcolony;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdColonyV4VCReward {
    boolean a;
    String b;

    /* renamed from: c, reason: collision with root package name */
    int f211c;

    AdColonyV4VCReward(boolean success, String name, int amount) {
        this.a = success;
        this.b = name;
        this.f211c = amount;
    }

    public boolean success() {
        return this.a;
    }

    public String name() {
        return this.b;
    }

    public int amount() {
        return this.f211c;
    }

    public String toString() {
        return this.a ? this.b + ":" + this.f211c : "no reward";
    }
}

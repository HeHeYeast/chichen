package com.immersion.hapticmediasdk.models;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HttpUnsuccessfulException extends Exception {

    /* renamed from: b04270427Ч0427ЧЧ, reason: contains not printable characters */
    public static int f84b042704270427 = 2;

    /* renamed from: b0427ЧЧ0427ЧЧ, reason: contains not printable characters */
    public static int f85b04270427 = 0;

    /* renamed from: bЧ0427Ч0427ЧЧ, reason: contains not printable characters */
    public static int f86b04270427 = 1;

    /* renamed from: bЧЧЧ0427ЧЧ, reason: contains not printable characters */
    public static int f87b0427 = 24;
    private static final long serialVersionUID = -251711421440827767L;
    private int a;

    public HttpUnsuccessfulException(int i, String str) {
        super(str);
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        if (((f87b0427 + f86b04270427) * f87b0427) % f84b042704270427 != f85b04270427) {
            f87b0427 = 74;
            f85b04270427 = 98;
        }
        this.a = i;
    }

    /* renamed from: b0427Ч04270427ЧЧ, reason: contains not printable characters */
    public static int m116b042704270427() {
        return 75;
    }

    /* renamed from: bЧЧ04270427ЧЧ, reason: contains not printable characters */
    public static int m117b04270427() {
        return 1;
    }

    public int getHttpStatusCode() throws Exception {
        if (((f87b0427 + m117b04270427()) * f87b0427) % f84b042704270427 != f85b04270427) {
            f87b0427 = m116b042704270427();
            f85b04270427 = m116b042704270427();
        }
        try {
            return this.a;
        } catch (Exception e) {
            throw e;
        }
    }
}

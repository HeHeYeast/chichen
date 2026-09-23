package com.immersion.hapticmediasdk.models;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NotEnoughHapticBytesAvailableException extends Exception {

    /* renamed from: b04210421С0421С0421, reason: contains not printable characters */
    public static int f88b0421042104210421 = 0;

    /* renamed from: b0421С04210421С0421, reason: contains not printable characters */
    public static int f89b0421042104210421 = 2;

    /* renamed from: bС0421С0421С0421, reason: contains not printable characters */
    public static int f90b042104210421 = 89;

    /* renamed from: bСС04210421С0421, reason: contains not printable characters */
    public static int f91b042104210421 = 1;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotEnoughHapticBytesAvailableException(String str) throws Exception {
        super(str);
        if (((f90b042104210421 + f91b042104210421) * f90b042104210421) % f89b0421042104210421 != f88b0421042104210421) {
            f90b042104210421 = 11;
            f88b0421042104210421 = 70;
        }
        try {
        } catch (Exception e) {
            throw e;
        }
    }
}

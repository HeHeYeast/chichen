package com.google.android.gms.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class dd implements SafeParcelable {
    private boolean kC = false;
    private static final Object kz = new Object();
    private static ClassLoader kA = null;
    private static Integer kB = null;

    private static boolean a(Class<?> cls) {
        try {
            return SafeParcelable.NULL.equals(cls.getField("NULL").get(null));
        } catch (IllegalAccessException e) {
            return false;
        } catch (NoSuchFieldException e2) {
            return false;
        }
    }

    protected static ClassLoader aV() {
        ClassLoader classLoader;
        synchronized (kz) {
            classLoader = kA;
        }
        return classLoader;
    }

    protected static Integer aW() {
        Integer num;
        synchronized (kz) {
            num = kB;
        }
        return num;
    }

    protected static boolean y(String str) {
        ClassLoader classLoaderAV = aV();
        if (classLoaderAV == null) {
            return true;
        }
        try {
            return a(classLoaderAV.loadClass(str));
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean aX() {
        return this.kC;
    }
}

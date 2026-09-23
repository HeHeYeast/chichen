package com.jirbo.adcolony;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class aa {
    static String a = new String("mutex");
    static ArrayList<a> b = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    static ArrayList<a> f213c = new ArrayList<>();
    static ArrayList<Runnable> d = new ArrayList<>();
    static ArrayList<Runnable> e = new ArrayList<>();
    static volatile boolean f;

    aa() {
    }

    static void a() {
        c();
        synchronized (a) {
            d.clear();
        }
        b();
    }

    static void a(Runnable runnable) {
        synchronized (a) {
            if (f) {
                d.add(runnable);
                return;
            }
            int size = b.size();
            a aVarRemove = size > 0 ? b.remove(size - 1) : null;
            if (aVarRemove == null) {
                a aVar = new a();
                synchronized (a) {
                    f213c.add(aVar);
                }
                aVar.a = runnable;
                aVar.start();
                return;
            }
            synchronized (aVarRemove) {
                aVarRemove.a = runnable;
                aVarRemove.notify();
            }
        }
    }

    static void b() {
        synchronized (a) {
            f = false;
            e.clear();
            e.addAll(d);
            d.clear();
            f213c.clear();
        }
        Iterator<Runnable> it = e.iterator();
        while (it.hasNext()) {
            a(it.next());
        }
    }

    static void c() {
        synchronized (a) {
            f = true;
            Iterator<a> it = b.iterator();
            while (it.hasNext()) {
                a next = it.next();
                synchronized (next) {
                    next.notify();
                }
            }
            synchronized (a) {
                b.clear();
            }
        }
    }

    static class a extends Thread {
        Runnable a;

        a() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                if (this.a != null) {
                    try {
                        this.a.run();
                    } catch (RuntimeException e) {
                        com.jirbo.adcolony.a.e("Exception caught in reusable thread.");
                        com.jirbo.adcolony.a.e(e + "");
                        e.printStackTrace();
                    }
                    this.a = null;
                }
                if (aa.f) {
                    return;
                }
                synchronized (this) {
                    synchronized (aa.a) {
                        aa.b.add(this);
                    }
                    try {
                        wait();
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
    }
}

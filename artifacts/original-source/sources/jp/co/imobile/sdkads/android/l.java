package jp.co.imobile.sdkads.android;

import java.util.List;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
abstract class l {
    String a = null;
    Boolean b = null;

    /* renamed from: c, reason: collision with root package name */
    Boolean f301c = null;

    l() {
    }

    abstract Boolean a();

    final Boolean a(List list) {
        if (list.size() > 1) {
            if (this.a.equals("AND")) {
                this.f301c = Boolean.valueOf(!list.contains(false));
            } else {
                this.f301c = Boolean.valueOf(list.contains(true));
            }
        } else {
            if (list.size() != 1) {
                this.f301c = false;
                return this.f301c;
            }
            this.f301c = (Boolean) list.get(0);
        }
        if (this.b.booleanValue()) {
            this.f301c = Boolean.valueOf(this.f301c.booleanValue() ? false : true);
        }
        return this.f301c;
    }
}

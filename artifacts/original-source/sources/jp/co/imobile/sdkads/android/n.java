package jp.co.imobile.sdkads.android;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class n extends l {
    private ArrayList d = new ArrayList();

    n() {
    }

    @Override // jp.co.imobile.sdkads.android.l
    final Boolean a() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.d.iterator();
        while (it.hasNext()) {
            arrayList.add(((m) it.next()).a);
        }
        return a(arrayList);
    }

    final void a(m mVar) {
        this.d.add(mVar);
    }
}

package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class dl {

    public static final class a {
        private final List<String> lj;
        private final Object lk;

        private a(Object obj) {
            this.lk = dm.e(obj);
            this.lj = new ArrayList();
        }

        public a a(String str, Object obj) {
            this.lj.add(((String) dm.e(str)) + "=" + String.valueOf(obj));
            return this;
        }

        public String toString() {
            StringBuilder sbAppend = new StringBuilder(100).append(this.lk.getClass().getSimpleName()).append('{');
            int size = this.lj.size();
            for (int i = 0; i < size; i++) {
                sbAppend.append(this.lj.get(i));
                if (i < size - 1) {
                    sbAppend.append(", ");
                }
            }
            return sbAppend.append('}').toString();
        }
    }

    public static a d(Object obj) {
        return new a(obj);
    }

    public static boolean equal(Object a2, Object b) {
        return a2 == b || (a2 != null && a2.equals(b));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }
}

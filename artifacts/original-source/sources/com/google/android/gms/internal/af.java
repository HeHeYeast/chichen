package com.google.android.gms.internal;

import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class af implements ai {
    private final ag ey;

    public af(ag agVar) {
        this.ey = agVar;
    }

    @Override // com.google.android.gms.internal.ai
    public void a(cq cqVar, Map<String, String> map) {
        String str = map.get("name");
        if (str == null) {
            cn.q("App event with no name parameter.");
        } else {
            this.ey.a(str, map.get("info"));
        }
    }
}

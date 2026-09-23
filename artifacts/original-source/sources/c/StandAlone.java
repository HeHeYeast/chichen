package c;

import android.os.Bundle;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class StandAlone extends DroidGap {
    @Override // c.DroidGap, android.app.Activity
    public void onCreate(Bundle bundle) throws NumberFormatException {
        super.onCreate(bundle);
        super.a("file:///android_asset/www/index.html");
    }
}

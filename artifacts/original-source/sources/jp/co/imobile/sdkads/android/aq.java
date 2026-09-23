package jp.co.imobile.sdkads.android;

import android.content.DialogInterface;
import android.view.KeyEvent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class aq implements DialogInterface.OnKeyListener {
    final /* synthetic */ ap a;

    aq(ap apVar) {
        this.a = apVar;
    }

    @Override // android.content.DialogInterface.OnKeyListener
    public final boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case 4:
            case 84:
                return true;
            default:
                return false;
        }
    }
}

package com.idtinc.ckgetdata;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.AppMainActivity;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GetDataActivity extends Activity {
    AppDelegate appDelegate = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws NumberFormatException {
        super.onCreate(savedInstanceState);
        if (this.appDelegate == null) {
            this.appDelegate = (AppDelegate) getApplicationContext();
        }
        Bundle bundle = getIntent().getExtras();
        if (this.appDelegate != null && bundle != null) {
            processExtraData(bundle);
            goToAppMainActivity(bundle);
        }
        finish();
    }

    private void processExtraData(Bundle _bundle) throws NumberFormatException {
        if (_bundle != null && this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                int getOmikujiShort = -2;
                boolean gift_tool_2_68_unlockF = false;
                String getOmikujiString = _bundle.getString("CallComIdtncCkChickAndDuck:CallComIdtIncCkKb_Character_");
                if (getOmikujiString != null && getOmikujiString.length() > 0) {
                    getOmikujiShort = Integer.parseInt(getOmikujiString);
                    gift_tool_2_68_unlockF = true;
                }
                if (this.appDelegate != null) {
                    this.appDelegate.set_gift_tool_2((short) 68, gift_tool_2_68_unlockF);
                    if (getOmikujiShort >= 0 && getOmikujiShort <= 14 && this.appDelegate.defaultSharedPreferences != null) {
                        SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                        Toast.makeText(this, "gift_tool_2_68_character_id:" + getOmikujiShort, 0).show();
                        editor.putInt("gift_tool_2_68_egg_id", 0);
                        editor.putInt("gift_tool_2_68_character_id", getOmikujiShort + 89);
                        editor.commit();
                    }
                }
            }
        }
    }

    public void goToAppMainActivity(Bundle _bundle) {
        Intent intent = new Intent();
        if (_bundle != null) {
            _bundle.putString("123", "321");
            intent.putExtras(_bundle);
        }
        intent.setClass(this, AppMainActivity.class);
        startActivity(intent);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        if (this.appDelegate != null) {
            this.appDelegate = null;
        }
        Toast.makeText(this, "onDestroy:", 0).show();
        System.gc();
        super.onDestroy();
    }
}

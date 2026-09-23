package com.google.android.gms.internal;

import android.R;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class bk extends FrameLayout implements View.OnClickListener {
    private final Activity fD;
    private final ImageButton gk;

    public bk(Activity activity, int i) {
        super(activity);
        this.fD = activity;
        setOnClickListener(this);
        this.gk = new ImageButton(activity);
        this.gk.setImageResource(R.drawable.btn_dialog);
        this.gk.setBackgroundColor(0);
        this.gk.setOnClickListener(this);
        this.gk.setPadding(0, 0, 0, 0);
        int iA = cm.a(activity, i);
        addView(this.gk, new FrameLayout.LayoutParams(iA, iA, 17));
    }

    public void d(boolean z) {
        this.gk.setVisibility(z ? 4 : 0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.fD.finish();
    }
}

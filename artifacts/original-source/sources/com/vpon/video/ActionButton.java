package com.vpon.video;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import vpadn.ag;
import vpadn.as;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ActionButton extends ImageView {
    protected ag a;
    protected as b;

    /* renamed from: c, reason: collision with root package name */
    private Drawable f287c;
    private a d;

    public interface a {
        void a(ActionButton actionButton);
    }

    public ActionButton(as asVar, Drawable drawable, ag agVar) {
        super(asVar.d());
        this.f287c = drawable;
        this.a = agVar;
        this.b = asVar;
        setVisibility(0);
        setBackgroundDrawable(this.f287c);
        setOnClickListener(new View.OnClickListener() { // from class: com.vpon.video.ActionButton.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                try {
                    ActionButton.this.a.a();
                    ActionButton.this.a();
                    if (ActionButton.this.d != null) {
                        ActionButton.this.d.a(ActionButton.this);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void a() {
    }

    public void setAfterPressButtonListener(a aVar) {
        this.d = aVar;
    }

    public void setCommand(ag agVar) {
        this.a = agVar;
    }

    public void setButtonIcon(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    public void setButtonIcon(String str) {
        setBackgroundDrawable(a(str));
    }

    final Drawable a(String str) {
        return new BitmapDrawable(getClass().getResourceAsStream(str));
    }
}

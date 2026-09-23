package com.vpon.video;

import android.view.View;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import vpadn.R;
import vpadn.ab;
import vpadn.ag;
import vpadn.as;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FuncButton extends TextView {
    protected ag a;
    private String b;

    /* renamed from: c, reason: collision with root package name */
    private float f288c;
    private R d;

    public FuncButton(as asVar, String str, ag agVar) {
        super(asVar.d());
        this.f288c = 16.0f;
        this.a = agVar;
        this.b = str.trim();
        try {
            this.b = URLDecoder.decode(this.b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ab.b("FuncButton", "URLDecoder.decode(body, UTF-8); throw Exception mButtonText:" + this.b);
        }
        setText(String.valueOf(this.b) + "  ");
        setTextSize(this.f288c);
        setShadowLayer(1.5f, 2.0f, -2.0f, -1442840576);
        setVisibility(0);
        setOnClickListener(new View.OnClickListener() { // from class: com.vpon.video.FuncButton.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                try {
                    FuncButton.this.a.a();
                    FuncButton funcButton = FuncButton.this;
                    if (FuncButton.this.d != null) {
                        R unused = FuncButton.this.d;
                        FuncButton funcButton2 = FuncButton.this;
                    }
                } catch (Exception e2) {
                }
            }
        });
    }

    public void setAfterPressButtonListener$47298a05(R r) {
        this.d = r;
    }

    public void setCommand(ag agVar) {
        this.a = agVar;
    }
}

package jp.adlantis.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NullAdView extends RelativeLayout {
    public NullAdView(Context context) {
        super(context);
        setupView();
    }

    public NullAdView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupView();
    }

    protected TextView createLabelTextView() {
        TextView textView = new TextView(getContext());
        textView.setText(AdlantisView.class.getSimpleName());
        textView.setTextSize(20.0f);
        textView.setTextColor(-1);
        textView.setGravity(17);
        return textView;
    }

    protected void setupView() {
        setBackgroundColor(ViewSettings.defaultBackgroundColor());
        addView(createLabelTextView(), new ViewGroup.LayoutParams(-1, -1));
    }
}

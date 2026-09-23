package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ImobileInlineView extends RelativeLayout {
    public ImobileInlineView(Context context) {
        this(context, null, 0);
    }

    public ImobileInlineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImobileInlineView(Context context, AttributeSet attrs, int defStyle) throws PackageManager.NameNotFoundException {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            TextView textView = new TextView(context);
            textView.setText(" ImobileInlineView V1.3");
            textView.setPadding(5, 5, 5, 5);
            textView.setTextColor(FluctConstants.FRAME_ALPHA_COLOR);
            textView.setTextSize(26.0f);
            addView(textView, new RelativeLayout.LayoutParams(-2, -2));
            setBackgroundColor(-3355444);
            return;
        }
        ImobileIconParams imobileIconParams = new ImobileIconParams();
        if (attrs == null) {
            x.a("ImobileInlineView XML Parameter Error", "Please ser spotId.");
            return;
        }
        String attributeValue = attrs.getAttributeValue(null, "pid");
        if (attributeValue == null || attributeValue.trim().equals("")) {
            x.a("ImobileInlineView XML Parameter Error", "Please set publisherId.");
            return;
        }
        String attributeValue2 = attrs.getAttributeValue(null, "mid");
        if (attributeValue2 == null || attributeValue2.trim().equals("")) {
            x.a("ImobileInlineView XML Parameter Error", "Please set mediaId.");
            return;
        }
        String attributeValue3 = attrs.getAttributeValue(null, "sid");
        if (attributeValue3 == null || attributeValue3.trim().equals("")) {
            x.a("ImobileInlineView XML Parameter Error", "Please set mediaId.");
            return;
        }
        Activity activity = (Activity) context;
        ImobileSdkAd.registerSpotInline(activity, attributeValue, attributeValue2, attributeValue3);
        ImobileSdkAd.start(attributeValue3);
        imobileIconParams.setIconNumber(attrs.getAttributeIntValue(null, "iconNumber", 4));
        imobileIconParams.setIconSize(attrs.getAttributeIntValue(null, "iconSize", -1));
        if (attrs.getAttributeIntValue(null, "iconViewLayoutWidth", 0) != 0) {
            imobileIconParams.setIconViewLayoutWidth(attrs.getAttributeIntValue(null, "iconViewLayoutWidth", -1));
        }
        imobileIconParams.setIconTitleEnable(attrs.getAttributeBooleanValue(null, "iconTitleEnable", true));
        if (attrs.getAttributeValue(null, "iconTitleFontColor") != null) {
            imobileIconParams.setIconTitleFontColor(attrs.getAttributeValue(null, "iconTitleFontColor"));
        }
        imobileIconParams.setIconTitleFontSize(attrs.getAttributeIntValue(null, "iconTitleFontSize", -1));
        imobileIconParams.setIconTitleOffset(attrs.getAttributeIntValue(null, "iconTitleOffset", -1));
        imobileIconParams.setIconTitleShadowEnable(attrs.getAttributeBooleanValue(null, "iconTitleShadowEnable", true));
        if (attrs.getAttributeValue(null, "iconTitleShadowColor") != null) {
            imobileIconParams.setIconTitleShadowColor(attrs.getAttributeValue(null, "iconTitleShadowColor"));
        }
        imobileIconParams.setIconTitleShadowDx(attrs.getAttributeIntValue(null, "iconTitleShadowDx", -1));
        imobileIconParams.setIconTitleShadowDy(attrs.getAttributeIntValue(null, "iconTitleShadowDy", -1));
        ImobileSdkAd.showAd(activity, attributeValue3, this, imobileIconParams);
    }
}

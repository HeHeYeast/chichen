package net.nend.android;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import net.nend.android.NendAdIconLoader;
import net.nend.android.NendConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NendAdIconLayout extends LinearLayout implements NendAdIconLoader.OnClickListner, NendAdIconLoader.OnFailedListner, NendAdIconLoader.OnReceiveListner {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private final int WC;
    private String mApiKey;
    private NendAdIconLoader.OnClickListner mClickListner;
    private NendAdIconLoader.OnFailedListner mFailedListner;
    private int mIconCount;
    private NendAdIconLoader mIconLoader;
    private boolean mIconSpaceEnabled;
    private NendAdIconLoader.OnReceiveListner mReceiveListner;
    private int mSpotId;
    private int mTitleColor;
    private boolean mTitleVisible;

    public NendAdIconLayout(Context context, int spotId, String apiKey, int iconCount) {
        super(context);
        this.mIconCount = 0;
        this.mTitleVisible = true;
        this.mTitleColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.mIconSpaceEnabled = true;
        this.WC = -2;
        this.mSpotId = spotId;
        this.mApiKey = apiKey;
        this.mIconCount = iconCount;
    }

    public NendAdIconLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIconCount = 0;
        this.mTitleVisible = true;
        this.mTitleColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.mIconSpaceEnabled = true;
        this.WC = -2;
        if (attrs == null) {
            throw new NullPointerException(NendStatus.ERR_INVALID_ATTRIBUTE_SET.getMsg());
        }
        String count = attrs.getAttributeValue(null, NendConstants.IconAttribute.ICON_COUNT.getName());
        if (count == null) {
            throw new NullPointerException(NendStatus.ERR_INVALID_ICON_COUNT.getMsg());
        }
        String orientation = attrs.getAttributeValue(null, NendConstants.IconAttribute.ICON_ORIENTATION.getName());
        if (orientation != null && "vertical".equals(orientation)) {
            setOrientation(1);
        }
        String titleColor = attrs.getAttributeValue(null, NendConstants.IconAttribute.TITLE_COLOR.getName());
        if (titleColor != null) {
            try {
                this.mTitleColor = Color.parseColor(titleColor);
            } catch (Exception e) {
                this.mTitleColor = FluctConstants.FRAME_ALPHA_COLOR;
            }
        }
        this.mTitleVisible = attrs.getAttributeBooleanValue(null, NendConstants.IconAttribute.TITLE_VISIBLE.getName(), true);
        this.mIconSpaceEnabled = attrs.getAttributeBooleanValue(null, NendConstants.IconAttribute.ICON_SPACE.getName(), true);
        this.mIconCount = Integer.parseInt(count);
        this.mSpotId = Integer.parseInt(attrs.getAttributeValue(null, NendConstants.Attribute.SPOT_ID.getName()));
        this.mApiKey = attrs.getAttributeValue(null, NendConstants.Attribute.API_KEY.getName());
        loadAd();
    }

    public void loadAd() {
        this.mIconLoader = new NendAdIconLoader(getContext(), this.mSpotId, this.mApiKey);
        for (int i = 0; i < this.mIconCount && i <= 7; i++) {
            NendAdIconView iconView = new NendAdIconView(getContext());
            iconView.setTitleColor(this.mTitleColor);
            iconView.setTitleVisible(this.mTitleVisible);
            iconView.setIconSpaceEnabled(this.mIconSpaceEnabled);
            this.mIconLoader.addIconView(iconView);
            addView(iconView, new LinearLayout.LayoutParams(-2, -2));
        }
        this.mIconLoader.loadAd();
        this.mIconLoader.setOnClickListner(this);
        this.mIconLoader.setOnFailedListner(this);
        this.mIconLoader.setOnReceiveLisner(this);
    }

    public void resume() {
        this.mIconLoader.resume();
    }

    public void pause() {
        this.mIconLoader.pause();
    }

    public void setIconOrientation(int orientation) {
        setOrientation(orientation);
    }

    public void setTitleColor(int color) {
        this.mTitleColor = color;
    }

    public void setTitleVisible(boolean titleVisible) {
        this.mTitleVisible = titleVisible;
    }

    public void setIconSpaceEnabled(boolean enabled) {
        this.mIconSpaceEnabled = enabled;
    }

    @Override // net.nend.android.NendAdIconLoader.OnReceiveListner
    public void onReceiveAd(NendAdIconView iconView) {
        if (this.mReceiveListner != null) {
            this.mReceiveListner.onReceiveAd(iconView);
        }
    }

    public void setOnReceiveLisner(NendAdIconLoader.OnReceiveListner onReceiveListner) {
        this.mReceiveListner = onReceiveListner;
    }

    @Override // net.nend.android.NendAdIconLoader.OnFailedListner
    public void onFailedToReceiveAd(NendIconError error) {
        if (this.mFailedListner != null) {
            this.mFailedListner.onFailedToReceiveAd(error);
        }
    }

    public void setOnFailedListner(NendAdIconLoader.OnFailedListner onFailedListner) {
        this.mFailedListner = onFailedListner;
    }

    @Override // net.nend.android.NendAdIconLoader.OnClickListner
    public void onClick(NendAdIconView iconView) {
        if (this.mClickListner != null) {
            this.mClickListner.onClick(iconView);
        }
    }

    public void setOnClickListner(NendAdIconLoader.OnClickListner onClickListner) {
        this.mClickListner = onClickListner;
    }
}

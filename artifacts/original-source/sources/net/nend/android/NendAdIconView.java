package net.nend.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.io.IOException;
import java.io.InputStream;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import net.nend.android.DownloadTask;
import net.nend.android.NendAdView;
import net.nend.android.NendConstants;
import net.nend.android.NendHelper;
import org.apache.http.HttpEntity;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NendAdIconView extends View implements DownloadTask.Downloadable<Bitmap> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int DEFAULT_FONT_SIZE = 10;
    private static final int ICON_CONTAINER_SIZE = 75;
    private static final int ICON_SIZE = 57;
    private static final int MINIMUM_FONT_SIZE = 6;
    private static final String NAME_SPACE = "http://schemas.android.com/apk/res/android";
    private static final int OPTOUT_MARGIN = 3;
    private static final int OPTOUT_SIZE = 12;
    private static final int WP = -2;
    private boolean isDefaultSize;
    private AdParameter mAdParameter;
    private float mDensity;
    private String mDisplayTitle;
    private DownloadTask<Bitmap> mDownloadTask;
    private float mHorizontalIconMargin;
    private BitmapDrawable mIconBitmap;
    private float mIconSize;
    private boolean mIconSpaceEnabled;
    private AdListener mListener;
    private BitmapDrawable mOptoutBitmap;
    private float mOptoutMargin;
    private Rect mRect;
    private int mSpotId;
    private Paint mTextPaint;
    private int mTextPosX;
    private int mTextPosY;
    private int mTitleColor;
    private boolean mTitleVisible;
    private float mVerticalIconMargin;

    interface AdListener {
        void onClick(View view);

        void onFailedToReceive(NendIconError nendIconError);

        void onReceive(View view);

        void onWindowFocusChanged(boolean z);
    }

    static {
        $assertionsDisabled = !NendAdIconView.class.desiredAssertionStatus();
    }

    public NendAdIconView(Context context) throws IOException {
        super(context);
        this.isDefaultSize = false;
        this.mTitleVisible = true;
        this.mIconSpaceEnabled = true;
        this.mTitleColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.mDisplayTitle = "";
        this.mOptoutBitmap = null;
        this.mIconBitmap = null;
        init(context);
    }

    public NendAdIconView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);
        this.isDefaultSize = false;
        this.mTitleVisible = true;
        this.mIconSpaceEnabled = true;
        this.mTitleColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.mDisplayTitle = "";
        this.mOptoutBitmap = null;
        this.mIconBitmap = null;
        if (Integer.toString(-2).equals(attrs.getAttributeValue(NAME_SPACE, "layout_width")) && Integer.toString(-2).equals(attrs.getAttributeValue(NAME_SPACE, "layout_height"))) {
            this.isDefaultSize = true;
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
        init(context);
    }

    private void init(Context context) throws IOException {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
        this.mDensity = metrics.density;
        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(this.mTitleColor);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mOptoutMargin = 3.0f * this.mDensity;
        this.mRect = new Rect();
        try {
            InputStream is = getResources().getAssets().open("nend_information_icon.png");
            Bitmap optoutImage = BitmapFactory.decodeStream(is);
            this.mOptoutBitmap = new BitmapDrawable(getResources(), optoutImage);
        } catch (IOException e) {
        }
    }

    void loadImage(AdParameter adParameter, int spotId) {
        if (adParameter == null) {
            if (this.mListener != null) {
                NendIconError error = new NendIconError();
                error.setErrorType(1);
                error.setIconView(this);
                error.setNendError(NendAdView.NendError.FAILED_AD_DOWNLOAD);
                this.mListener.onFailedToReceive(error);
                return;
            }
            return;
        }
        this.mAdParameter = adParameter;
        this.mSpotId = spotId;
        this.mDisplayTitle = "";
        this.mDownloadTask = new DownloadTask<>(this);
        NendHelper.AsyncTaskHelper.execute(this.mDownloadTask, new Void[0]);
    }

    public void deallocate() {
        if (this.mDownloadTask != null && !this.mDownloadTask.isCancelled()) {
            this.mDownloadTask.cancel(true);
        }
        setOnClickListener(null);
        this.mListener = null;
        this.mIconBitmap = null;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null && params.width == -2 && params.height == -2) {
            this.isDefaultSize = true;
        }
        if (this.isDefaultSize) {
            float width = 75.0f;
            float height = 75.0f;
            if (!this.mIconSpaceEnabled) {
                width = 57.0f;
                if (!this.mTitleVisible) {
                    height = 57.0f;
                }
            }
            setMeasuredDimension((int) (this.mDensity * width), (int) (this.mDensity * height));
            return;
        }
        float width2 = Math.min(params.width, params.height);
        float height2 = Math.min(params.width, params.height);
        if (!this.mIconSpaceEnabled && this.mTitleVisible) {
            float scale = height2 / (75.0f * this.mDensity);
            height2 = (int) ((18.0f * scale * this.mDensity) + height2);
        }
        setMeasuredDimension((int) width2, (int) height2);
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public String getRequestUrl() {
        return this.mAdParameter.getImageUrl();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.nend.android.DownloadTask.Downloadable
    public Bitmap makeResponse(HttpEntity entity) {
        if (entity != null && !this.mDownloadTask.isCancelled()) {
            try {
                return BitmapFactory.decodeStream(entity.getContent());
            } catch (IOException e) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                NendLog.d(NendStatus.ERR_HTTP_REQUEST, e);
            } catch (IllegalStateException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                NendLog.d(NendStatus.ERR_HTTP_REQUEST, e2);
            }
        }
        return null;
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public void onDownload(Bitmap response) {
        if (!this.mDownloadTask.isCancelled()) {
            if (response != null) {
                NendLog.d("onImageDownload!");
                this.mIconBitmap = new BitmapDrawable(getResources(), response);
                postInvalidate();
                if (this.mListener != null) {
                    this.mListener.onReceive(this);
                    return;
                }
                return;
            }
            NendLog.d("onFailedToImageDownload!");
            if (this.mListener != null) {
                NendIconError error = new NendIconError();
                error.setIconView(this);
                error.setErrorType(1);
                error.setNendError(NendAdView.NendError.FAILED_AD_DOWNLOAD);
                this.mListener.onFailedToReceive(error);
            }
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIconBitmap != null) {
            this.mIconBitmap.setBounds((int) this.mHorizontalIconMargin, 0, (int) (this.mHorizontalIconMargin + this.mIconSize), (int) this.mIconSize);
            this.mIconBitmap.draw(canvas);
            if (this.mTitleVisible && this.mAdParameter.getTitleText() != null) {
                adjustTitleText();
                canvas.drawText(this.mDisplayTitle, this.mTextPosX, this.mTextPosY, this.mTextPaint);
            }
            if (this.mOptoutBitmap != null) {
                this.mOptoutBitmap.setBounds(this.mRect);
                this.mOptoutBitmap.draw(canvas);
            }
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int width = right - left;
            int height = bottom - top;
            float scale = this.mIconSpaceEnabled ? width / (75.0f * this.mDensity) : width / (57.0f * this.mDensity);
            this.mIconSize = 57.0f * this.mDensity * scale;
            this.mHorizontalIconMargin = this.mIconSpaceEnabled ? (width - this.mIconSize) / 2.0f : BitmapDescriptorFactory.HUE_RED;
            this.mVerticalIconMargin = this.mTitleVisible ? height - this.mIconSize : BitmapDescriptorFactory.HUE_RED;
            this.mTextPosX = width / 2;
            float iconScale = this.mIconSize / (57.0f * this.mDensity);
            int optoutSize = (int) (12.0f * this.mDensity * iconScale);
            int r = (int) (this.mIconSize + this.mHorizontalIconMargin);
            int l = r - optoutSize;
            this.mRect.top = 0;
            this.mRect.left = l;
            this.mRect.right = r;
            this.mRect.bottom = optoutSize;
        }
    }

    public void setTitleVisible(boolean visible) {
        this.mTitleVisible = visible;
        invalidate();
    }

    public void setTitleColor(int color) {
        this.mTitleColor = color;
        this.mTextPaint.setColor(this.mTitleColor);
        invalidate();
    }

    public void setIconSpaceEnabled(boolean enabled) {
        this.mIconSpaceEnabled = enabled;
    }

    void setListner(AdListener listner) {
        this.mListener = listner;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 1:
                if (this.mIconBitmap != null) {
                    if (this.mOptoutBitmap != null && x > this.mRect.left - this.mOptoutMargin && x < this.mRect.right && y > this.mRect.top && y < this.mRect.bottom + this.mOptoutMargin) {
                        NendHelper.startBrowser(getContext(), "http://nend.net/privacy/optsdkgate?uid=" + NendHelper.makeUid(getContext()) + "&spot=" + this.mSpotId);
                        return false;
                    }
                    if (x > this.mHorizontalIconMargin && x < this.mIconSize && y > BitmapDescriptorFactory.HUE_RED && y < this.mIconSize) {
                        NendLog.v("click!! url: " + this.mAdParameter.getClickUrl());
                        if (this.mListener != null) {
                            this.mListener.onClick(this);
                        }
                        NendHelper.startBrowser(getContext(), this.mAdParameter.getClickUrl());
                        return false;
                    }
                }
                break;
            default:
                return true;
        }
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (this.mListener != null) {
            this.mListener.onWindowFocusChanged(hasWindowFocus);
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        deallocate();
    }

    private void adjustTitleText() {
        this.mDisplayTitle = this.mAdParameter.getTitleText();
        float textSize = getDisplayableTextSize(this.mDisplayTitle, this.mTextPaint);
        if (BitmapDescriptorFactory.HUE_RED != textSize) {
            this.mTextPaint.setTextSize(textSize);
        } else {
            this.mTextPaint.setTextSize(6.0f * this.mDensity);
            this.mDisplayTitle = TextUtils.ellipsize(this.mDisplayTitle, new TextPaint(this.mTextPaint), getWidth(), TextUtils.TruncateAt.END).toString();
        }
        Paint.FontMetrics metrics = this.mTextPaint.getFontMetrics();
        float textAreaCenterY = this.mIconSize + (this.mVerticalIconMargin / 2.0f);
        this.mTextPosY = (int) (textAreaCenterY - ((metrics.descent + metrics.ascent) / 2.0f));
    }

    private float getDisplayableTextSize(String text, Paint paint) {
        Paint workPaint = new Paint(paint);
        workPaint.setTextSize(10.0f * this.mDensity);
        while (true) {
            Paint.FontMetrics metrics = workPaint.getFontMetrics();
            float textWidth = workPaint.measureText(text);
            if (getWidth() > textWidth && this.mVerticalIconMargin > Math.abs(metrics.ascent + metrics.descent)) {
                float result = workPaint.getTextSize();
                return result;
            }
            float nextTextSize = workPaint.getTextSize() - 1.0f;
            if (6.0f * this.mDensity > nextTextSize) {
                return BitmapDescriptorFactory.HUE_RED;
            }
            workPaint.setTextSize(nextTextSize);
        }
    }
}

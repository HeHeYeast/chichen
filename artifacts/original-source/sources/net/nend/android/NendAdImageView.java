package net.nend.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

@SuppressLint({"ViewConstructor"})
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendAdImageView extends ImageView implements View.OnClickListener {
    private OnAdImageClickListener listener;
    private Bitmap mBitmap;
    private String mClickUrl;

    interface OnAdImageClickListener {
        void onAdImageClick(View view);
    }

    NendAdImageView(Context context) {
        super(context);
        this.mClickUrl = "";
        setScaleType(ImageView.ScaleType.FIT_XY);
        setOnClickListener(this);
    }

    void setAdInfo(Bitmap adImage, String clickUrl) {
        this.mBitmap = adImage;
        setImageBitmap(this.mBitmap);
        if (clickUrl != null) {
            this.mClickUrl = clickUrl;
        }
    }

    void setOnAdImageClickListener(OnAdImageClickListener listener) {
        this.listener = listener;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        NendLog.v("click!! url: " + this.mClickUrl);
        if (this.listener != null) {
            this.listener.onAdImageClick(v);
        }
        NendHelper.startBrowser(getContext(), this.mClickUrl);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
        setImageDrawable(null);
    }
}

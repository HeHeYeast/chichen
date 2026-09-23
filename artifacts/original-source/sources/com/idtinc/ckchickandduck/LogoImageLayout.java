package com.idtinc.ckchickandduck;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.widget.FrameLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class LogoImageLayout extends FrameLayout {
    private AppDelegate appDelegate;
    private Bitmap backGroundBitmap;
    private int dispHeight;
    private int finalHeight;
    private int finalWidth;

    public LogoImageLayout(Context context, int _dispWidth, int _dispHeight, int _finalwidth, int _finalheight, float _zoomrate) throws IOException {
        super(context);
        this.dispHeight = 0;
        this.finalWidth = 0;
        this.finalHeight = 0;
        this.appDelegate = null;
        this.backGroundBitmap = null;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.dispHeight = _dispHeight;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        refreshBackGround();
    }

    public void clearDrawable() {
        if (this.backGroundBitmap != null) {
            if (!this.backGroundBitmap.isRecycled()) {
                this.backGroundBitmap.recycle();
            }
            this.backGroundBitmap = null;
        }
        System.gc();
    }

    public void refreshBackGround() throws IOException {
        clearDrawable();
        AssetManager asm = this.appDelegate.getAssets();
        try {
            InputStream inputStream = asm.open("png/Default@2x.jpg");
            BufferedInputStream buf = new BufferedInputStream(inputStream);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(buf, null, opt);
            int scale = 1;
            if (this.appDelegate != null) {
                scale = this.appDelegate.getBitmapScale(opt.outWidth, this.finalWidth);
            }
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inSampleSize = scale;
            opt2.inPreferredConfig = Bitmap.Config.RGB_565;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            this.backGroundBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
            inputStream.close();
        } catch (IOException e) {
        }
        System.gc();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(-1);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.dispHeight, bitmapPaint);
        if (this.backGroundBitmap != null) {
            bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
            if (this.dispHeight > this.finalHeight) {
                int drawableBackGroundBitmapOffsetY = (this.dispHeight - this.finalHeight) / 2;
                canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, drawableBackGroundBitmapOffsetY, this.finalWidth, this.finalHeight + drawableBackGroundBitmapOffsetY), bitmapPaint);
            } else {
                canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, this.finalWidth, this.finalHeight), bitmapPaint);
            }
        }
    }

    public void onDestroy() {
        clearDrawable();
        this.appDelegate = null;
    }
}

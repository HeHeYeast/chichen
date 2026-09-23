package com.idtinc.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ContentPopUnitBackView {
    private ContentPopUnit contentPopUnit;
    private float finalHeight;
    private float finalWidth;
    MyDraw myDraw = new MyDraw();
    private float zoomRate;

    public ContentPopUnitBackView(float _finalwidth, float _finalheight, float _zoomrate, ContentPopUnit _contentPopUnit) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.contentPopUnit = _contentPopUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        if (this.contentPopUnit.nowStatus >= 0 && this.contentPopUnit.nowStatus <= 3) {
            bitmapPaint.setColor(1711276032);
            canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
            MyDraw.drawStrokeRect(canvas, this.contentPopUnit.backViewOffsetX, this.contentPopUnit.backViewOffsetY, this.contentPopUnit.backViewWidth, this.contentPopUnit.backViewHeight, this.contentPopUnit.backViewColor0, this.contentPopUnit.backViewStrokeWidth1, this.contentPopUnit.backViewColor1, this.contentPopUnit.backViewStrokeWidth2, this.contentPopUnit.backViewColor2, this.contentPopUnit.backViewStrokeWidth3, this.contentPopUnit.backViewColor3, this.contentPopUnit.backViewRadius);
            canvas.save();
            canvas.clipRect(this.contentPopUnit.blockImageCutOffsetX, this.contentPopUnit.blockImageCutOffsetY, this.contentPopUnit.blockImageCutWidth, this.contentPopUnit.blockImageCutHeight);
            if (this.contentPopUnit.appDelegate != null && this.contentPopUnit.appDelegate.optionBackGroundBitmap != null) {
                bitmapPaint.setAlpha(77);
                canvas.drawBitmap(this.contentPopUnit.appDelegate.optionBackGroundBitmap, new Rect(0, 0, this.contentPopUnit.appDelegate.optionBackGroundBitmap.getWidth(), this.contentPopUnit.appDelegate.optionBackGroundBitmap.getHeight()), new Rect(this.contentPopUnit.blockImageOffsetX, this.contentPopUnit.blockImageOffsetY, this.contentPopUnit.blockImageWidth, this.contentPopUnit.blockImageHeight), bitmapPaint);
            }
            canvas.restore();
            MyDraw.drawOnlyStrokeRect(canvas, this.contentPopUnit.backViewOffsetX, this.contentPopUnit.backViewOffsetY, this.contentPopUnit.backViewWidth, this.contentPopUnit.backViewHeight, 0, this.contentPopUnit.backViewStrokeWidth1, this.contentPopUnit.backViewColor1, this.contentPopUnit.backViewStrokeWidth2, this.contentPopUnit.backViewColor2, this.contentPopUnit.backViewStrokeWidth3, this.contentPopUnit.backViewColor3, this.contentPopUnit.backViewRadius);
            MyDraw.drawStrokeRect(canvas, this.contentPopUnit.backStrokeViewOffsetX, this.contentPopUnit.backStrokeViewOffsetY, this.contentPopUnit.backStrokeViewWidth, this.contentPopUnit.backStrokeViewHeight, this.contentPopUnit.backStrokeViewColor0, this.contentPopUnit.backStrokeViewStrokeWidth1, this.contentPopUnit.backStrokeViewColor1, this.contentPopUnit.backStrokeViewStrokeWidth2, this.contentPopUnit.backStrokeViewColor2, this.contentPopUnit.backStrokeViewStrokeWidth3, this.contentPopUnit.backStrokeViewColor3, this.contentPopUnit.backStrokeViewRadius);
            MyDraw.drawStrokeRect(canvas, this.contentPopUnit.titleBackViewOffsetX, this.contentPopUnit.titleBackViewOffsetY, this.contentPopUnit.titleBackViewWidth, this.contentPopUnit.titleBackViewHeight, this.contentPopUnit.titleBackViewColor0, this.contentPopUnit.titleBackViewStrokeWidth1, this.contentPopUnit.titleBackViewColor1, this.contentPopUnit.titleBackViewStrokeWidth2, this.contentPopUnit.titleBackViewColor2, this.contentPopUnit.titleBackViewStrokeWidth3, this.contentPopUnit.titleBackViewColor3, this.contentPopUnit.titleBackViewRadius);
            MyDraw.drawStrokeRect(canvas, this.contentPopUnit.imageView0BackViewOffsetX, this.contentPopUnit.imageView0BackViewOffsetY, this.contentPopUnit.imageView0BackViewWidth, this.contentPopUnit.imageView0BackViewHeight, this.contentPopUnit.imageView0BackViewColor0, this.contentPopUnit.imageView0BackViewStrokeWidth1, this.contentPopUnit.imageView0BackViewColor1, this.contentPopUnit.imageView0BackViewStrokeWidth2, this.contentPopUnit.imageView0BackViewColor2, this.contentPopUnit.imageView0BackViewStrokeWidth3, this.contentPopUnit.imageView0BackViewColor3, this.contentPopUnit.imageView0BackViewRadius);
            if (this.contentPopUnit.imageBitmap0 != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.contentPopUnit.imageBitmap0, new Rect(0, 0, this.contentPopUnit.imageBitmap0.getWidth(), this.contentPopUnit.imageBitmap0.getHeight()), new Rect((int) this.contentPopUnit.smallImage0OffsetX, (int) this.contentPopUnit.smallImage0OffsetY, (int) (this.contentPopUnit.smallImage0OffsetX + this.contentPopUnit.smallImage0Width), (int) (this.contentPopUnit.smallImage0OffsetY + this.contentPopUnit.smallImage0Height)), bitmapPaint);
            }
            if (this.contentPopUnit.titleLabelString.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.contentPopUnit.titleLabelOffsetX, this.contentPopUnit.titleLabelOffsetY, this.contentPopUnit.titleLabelTypeface, this.contentPopUnit.titleLabelString, this.contentPopUnit.titleLabelFontSize, this.contentPopUnit.titleLabelColor0, this.contentPopUnit.titleLabelStroke1Width, this.contentPopUnit.titleLabelColor1, this.contentPopUnit.titleLabelStroke2Width, this.contentPopUnit.titleLabelColor2);
            }
            if (this.contentPopUnit.contentLabelString0.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.contentPopUnit.contentLabelOffsetX0, this.contentPopUnit.contentLabelOffsetY, this.contentPopUnit.contentLabelTypeface, this.contentPopUnit.contentLabelString0, this.contentPopUnit.contentLabelFontSize, this.contentPopUnit.contentLabelColor0, this.contentPopUnit.contentLabelStroke1Width, this.contentPopUnit.contentLabelColor1, this.contentPopUnit.contentLabelStroke2Width, this.contentPopUnit.contentLabelColor2);
            }
            if (this.contentPopUnit.contentLabelString1.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.contentPopUnit.contentLabelOffsetX1, this.contentPopUnit.contentLabelOffsetY + (this.contentPopUnit.contentLabelSpaceY * 1.0f), this.contentPopUnit.contentLabelTypeface, this.contentPopUnit.contentLabelString1, this.contentPopUnit.contentLabelFontSize, this.contentPopUnit.contentLabelColor0, this.contentPopUnit.contentLabelStroke1Width, this.contentPopUnit.contentLabelColor1, this.contentPopUnit.contentLabelStroke2Width, this.contentPopUnit.contentLabelColor2);
            }
            if (this.contentPopUnit.contentLabelString2.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.contentPopUnit.contentLabelOffsetX2, this.contentPopUnit.contentLabelOffsetY + (this.contentPopUnit.contentLabelSpaceY * 2.0f), this.contentPopUnit.contentLabelTypeface, this.contentPopUnit.contentLabelString2, this.contentPopUnit.contentLabelFontSize, this.contentPopUnit.contentLabelColor0, this.contentPopUnit.contentLabelStroke1Width, this.contentPopUnit.contentLabelColor1, this.contentPopUnit.contentLabelStroke2Width, this.contentPopUnit.contentLabelColor2);
            }
            if (this.contentPopUnit.contentLabelString3.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.contentPopUnit.contentLabelOffsetX3, this.contentPopUnit.contentLabelOffsetY + (this.contentPopUnit.contentLabelSpaceY * 3.0f), this.contentPopUnit.contentLabelTypeface, this.contentPopUnit.contentLabelString3, this.contentPopUnit.contentLabelFontSize, this.contentPopUnit.contentLabelColor0, this.contentPopUnit.contentLabelStroke1Width, this.contentPopUnit.contentLabelColor1, this.contentPopUnit.contentLabelStroke2Width, this.contentPopUnit.contentLabelColor2);
            }
            if (this.contentPopUnit.contentLabelString4.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.contentPopUnit.contentLabelOffsetX4, this.contentPopUnit.contentLabelOffsetY + (this.contentPopUnit.contentLabelSpaceY * 4.0f), this.contentPopUnit.contentLabelTypeface, this.contentPopUnit.contentLabelString4, this.contentPopUnit.contentLabelFontSize, this.contentPopUnit.contentLabelColor0, this.contentPopUnit.contentLabelStroke1Width, this.contentPopUnit.contentLabelColor1, this.contentPopUnit.contentLabelStroke2Width, this.contentPopUnit.contentLabelColor2);
            }
            if (this.contentPopUnit.facebookButtonStatus == 0) {
                if (this.contentPopUnit.facebookButtonBitmap != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.contentPopUnit.facebookButtonBitmap, new Rect(0, 0, this.contentPopUnit.facebookButtonBitmap.getWidth(), this.contentPopUnit.facebookButtonBitmap.getHeight()), new Rect((int) this.contentPopUnit.facebookButtonOffsetX, (int) this.contentPopUnit.facebookButtonOffsetY, (int) (this.contentPopUnit.facebookButtonOffsetX + this.contentPopUnit.facebookButtonWidth), (int) (this.contentPopUnit.facebookButtonOffsetY + this.contentPopUnit.facebookButtonHeight)), bitmapPaint);
                }
                if (this.contentPopUnit.selectedButtonIndex == 10) {
                    MyDraw.drawStrokeRect(canvas, this.contentPopUnit.facebookButtonOffsetX, this.contentPopUnit.facebookButtonOffsetY, this.contentPopUnit.facebookButtonWidth, this.contentPopUnit.facebookButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.contentPopUnit.facebookButtonRadius);
                }
            }
            if (this.contentPopUnit.lineButtonStatus == 0) {
                if (this.contentPopUnit.lineButtonBitmap != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.contentPopUnit.lineButtonBitmap, new Rect(0, 0, this.contentPopUnit.lineButtonBitmap.getWidth(), this.contentPopUnit.lineButtonBitmap.getHeight()), new Rect((int) this.contentPopUnit.lineButtonOffsetX, (int) this.contentPopUnit.lineButtonOffsetY, (int) (this.contentPopUnit.lineButtonOffsetX + this.contentPopUnit.lineButtonWidth), (int) (this.contentPopUnit.lineButtonOffsetY + this.contentPopUnit.lineButtonHeight)), bitmapPaint);
                }
                if (this.contentPopUnit.selectedButtonIndex == 11) {
                    MyDraw.drawStrokeRect(canvas, this.contentPopUnit.lineButtonOffsetX, this.contentPopUnit.lineButtonOffsetY, this.contentPopUnit.lineButtonWidth, this.contentPopUnit.lineButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.contentPopUnit.lineButtonRadius);
                }
            } else if (this.contentPopUnit.lineButtonStatus == -1 && this.contentPopUnit.lineButtonBitmap != null) {
                bitmapPaint.setAlpha(76);
                canvas.drawBitmap(this.contentPopUnit.lineButtonBitmap, new Rect(0, 0, this.contentPopUnit.lineButtonBitmap.getWidth(), this.contentPopUnit.lineButtonBitmap.getHeight()), new Rect((int) this.contentPopUnit.lineButtonOffsetX, (int) this.contentPopUnit.lineButtonOffsetY, (int) (this.contentPopUnit.lineButtonOffsetX + this.contentPopUnit.lineButtonWidth), (int) (this.contentPopUnit.lineButtonOffsetY + this.contentPopUnit.lineButtonHeight)), bitmapPaint);
            }
            if (this.contentPopUnit.mailButtonStatus == 0) {
                if (this.contentPopUnit.mailButtonBitmap != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.contentPopUnit.mailButtonBitmap, new Rect(0, 0, this.contentPopUnit.mailButtonBitmap.getWidth(), this.contentPopUnit.mailButtonBitmap.getHeight()), new Rect((int) this.contentPopUnit.mailButtonOffsetX, (int) this.contentPopUnit.mailButtonOffsetY, (int) (this.contentPopUnit.mailButtonOffsetX + this.contentPopUnit.mailButtonWidth), (int) (this.contentPopUnit.mailButtonOffsetY + this.contentPopUnit.mailButtonHeight)), bitmapPaint);
                }
                if (this.contentPopUnit.selectedButtonIndex == 12) {
                    MyDraw.drawStrokeRect(canvas, this.contentPopUnit.mailButtonOffsetX, this.contentPopUnit.mailButtonOffsetY, this.contentPopUnit.mailButtonWidth, this.contentPopUnit.mailButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.contentPopUnit.mailButtonRadius);
                }
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.contentPopUnit = null;
    }
}

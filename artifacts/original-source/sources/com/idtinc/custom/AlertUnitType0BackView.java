package com.idtinc.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AlertUnitType0BackView {
    private AlertUnitType0 alertUnitType0;
    private float finalHeight;
    private float finalWidth;
    MyDraw myDraw = new MyDraw();
    private float zoomRate;

    public AlertUnitType0BackView(float _finalwidth, float _finalheight, float _zoomrate, AlertUnitType0 _alertUnitType0) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.alertUnitType0 = _alertUnitType0;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        if (this.alertUnitType0.nowStatus >= 0 && this.alertUnitType0.nowStatus <= 3) {
            bitmapPaint.setColor(1711276032);
            canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
            MyDraw.drawStrokeRect(canvas, this.alertUnitType0.backViewOffsetX, this.alertUnitType0.backViewOffsetY, this.alertUnitType0.backViewWidth, this.alertUnitType0.backViewHeight, this.alertUnitType0.backViewColor0, this.alertUnitType0.backViewStrokeWidth1, this.alertUnitType0.backViewColor1, this.alertUnitType0.backViewStrokeWidth2, this.alertUnitType0.backViewColor2, this.alertUnitType0.backViewStrokeWidth3, this.alertUnitType0.backViewColor3, this.alertUnitType0.backViewRadius);
            if (this.alertUnitType0.imageBitmap1 != null && this.alertUnitType0.smallImage1FadeOutAlpha > 0) {
                bitmapPaint.setAlpha(this.alertUnitType0.smallImage1FadeOutAlpha);
                canvas.drawBitmap(this.alertUnitType0.imageBitmap1, new Rect(0, 0, this.alertUnitType0.imageBitmap1.getWidth(), this.alertUnitType0.imageBitmap1.getHeight()), new Rect((int) this.alertUnitType0.smallImage1OffsetX, (int) this.alertUnitType0.smallImage1OffsetY, (int) (this.alertUnitType0.smallImage1OffsetX + this.alertUnitType0.smallImage1Width), (int) (this.alertUnitType0.smallImage1OffsetY + this.alertUnitType0.smallImage1Height)), bitmapPaint);
                AlertUnitType0 alertUnitType0 = this.alertUnitType0;
                alertUnitType0.smallImage1FadeOutAlpha -= 25;
                if (this.alertUnitType0.smallImage1FadeOutAlpha <= 0) {
                    this.alertUnitType0.smallImage1FadeOutAlpha = 0;
                }
            }
            if (this.alertUnitType0.imageBitmap0 != null) {
                if (this.alertUnitType0.smallImage1FadeOutAlpha > 0) {
                    bitmapPaint.setAlpha(255 - this.alertUnitType0.smallImage1FadeOutAlpha);
                    canvas.drawBitmap(this.alertUnitType0.imageBitmap0, new Rect(0, 0, this.alertUnitType0.imageBitmap0.getWidth(), this.alertUnitType0.imageBitmap0.getHeight()), new Rect((int) this.alertUnitType0.smallImage0OffsetX, (int) this.alertUnitType0.smallImage0OffsetY, (int) (this.alertUnitType0.smallImage0OffsetX + this.alertUnitType0.smallImage0Width), (int) (this.alertUnitType0.smallImage0OffsetY + this.alertUnitType0.smallImage0Height)), bitmapPaint);
                } else {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.alertUnitType0.imageBitmap0, new Rect(0, 0, this.alertUnitType0.imageBitmap0.getWidth(), this.alertUnitType0.imageBitmap0.getHeight()), new Rect((int) this.alertUnitType0.smallImage0OffsetX, (int) this.alertUnitType0.smallImage0OffsetY, (int) (this.alertUnitType0.smallImage0OffsetX + this.alertUnitType0.smallImage0Width), (int) (this.alertUnitType0.smallImage0OffsetY + this.alertUnitType0.smallImage0Height)), bitmapPaint);
                }
            }
            if (this.alertUnitType0.titleLabelString.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.alertUnitType0.titleLabelOffsetX, this.alertUnitType0.titleLabelOffsetY, this.alertUnitType0.titleLabelTypeface, this.alertUnitType0.titleLabelString, this.alertUnitType0.titleLabelFontSize, this.alertUnitType0.titleLabelColor0, this.alertUnitType0.titleLabelStroke1Width, this.alertUnitType0.titleLabelColor1, this.alertUnitType0.titleLabelStroke2Width, this.alertUnitType0.titleLabelColor2);
            }
            if (this.alertUnitType0.contentLabelString0.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.alertUnitType0.contentLabelOffsetX0, this.alertUnitType0.contentLabelOffsetY, this.alertUnitType0.contentLabelTypeface, this.alertUnitType0.contentLabelString0, this.alertUnitType0.contentLabelFontSize, this.alertUnitType0.contentLabelColor0, this.alertUnitType0.contentLabelStroke1Width, this.alertUnitType0.contentLabelColor1, this.alertUnitType0.contentLabelStroke2Width, this.alertUnitType0.contentLabelColor2);
            }
            if (this.alertUnitType0.contentLabelString1.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.alertUnitType0.contentLabelOffsetX1, this.alertUnitType0.contentLabelOffsetY + (this.alertUnitType0.contentLabelSpaceY * 1.0f), this.alertUnitType0.contentLabelTypeface, this.alertUnitType0.contentLabelString1, this.alertUnitType0.contentLabelFontSize, this.alertUnitType0.contentLabelColor0, this.alertUnitType0.contentLabelStroke1Width, this.alertUnitType0.contentLabelColor1, this.alertUnitType0.contentLabelStroke2Width, this.alertUnitType0.contentLabelColor2);
            }
            if (this.alertUnitType0.contentLabelString2.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.alertUnitType0.contentLabelOffsetX2, this.alertUnitType0.contentLabelOffsetY + (this.alertUnitType0.contentLabelSpaceY * 2.0f), this.alertUnitType0.contentLabelTypeface, this.alertUnitType0.contentLabelString2, this.alertUnitType0.contentLabelFontSize, this.alertUnitType0.contentLabelColor0, this.alertUnitType0.contentLabelStroke1Width, this.alertUnitType0.contentLabelColor1, this.alertUnitType0.contentLabelStroke2Width, this.alertUnitType0.contentLabelColor2);
            }
            if (this.alertUnitType0.contentLabelString3.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.alertUnitType0.contentLabelOffsetX3, this.alertUnitType0.contentLabelOffsetY + (this.alertUnitType0.contentLabelSpaceY * 3.0f), this.alertUnitType0.contentLabelTypeface, this.alertUnitType0.contentLabelString3, this.alertUnitType0.contentLabelFontSize, this.alertUnitType0.contentLabelColor0, this.alertUnitType0.contentLabelStroke1Width, this.alertUnitType0.contentLabelColor1, this.alertUnitType0.contentLabelStroke2Width, this.alertUnitType0.contentLabelColor2);
            }
            if (this.alertUnitType0.contentLabelString4.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.alertUnitType0.contentLabelOffsetX4, this.alertUnitType0.contentLabelOffsetY + (this.alertUnitType0.contentLabelSpaceY * 4.0f), this.alertUnitType0.contentLabelTypeface, this.alertUnitType0.contentLabelString4, this.alertUnitType0.contentLabelFontSize, this.alertUnitType0.contentLabelColor0, this.alertUnitType0.contentLabelStroke1Width, this.alertUnitType0.contentLabelColor1, this.alertUnitType0.contentLabelStroke2Width, this.alertUnitType0.contentLabelColor2);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.alertUnitType0 = null;
    }
}

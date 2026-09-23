package com.idtinc.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MyDraw {
    public static void drawStrokeText1(Canvas canvas, float _offsetX, float _offsetY, Typeface _typeface, String _textString, float _fontSize, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2) {
        Paint paint = new Paint();
        paint.setFlags(257);
        paint.setTextSize(_fontSize);
        paint.setTypeface(_typeface);
        paint.setColor(_color2);
        paint.setStrokeWidth(_strokeWidth2);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
        paint.setColor(_color1);
        paint.setStrokeWidth(_strokeWidth1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
        paint.setColor(_color0);
        paint.setStrokeWidth(BitmapDescriptorFactory.HUE_RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
    }

    public static void drawStrokeText(Canvas canvas, float _offsetX, float _offsetY, Typeface _typeface, String _textString, float _fontSize, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2) {
        Paint paint = new Paint(257);
        paint.setTextSize(_fontSize);
        paint.setTypeface(_typeface);
        paint.setColor(_color2);
        paint.setStrokeWidth(_strokeWidth2);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
        paint.setColor(_color1);
        paint.setStrokeWidth(_strokeWidth1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
        paint.setColor(_color0);
        paint.setStrokeWidth(BitmapDescriptorFactory.HUE_RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
    }

    public static void drawShadowStrokeText(Canvas canvas, float _offsetX, float _offsetY, Typeface _typeface, String _textString, float _fontSize, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, int _shadowColor, int _shadowOpacity, float _shadowOffsetX, float _shadowOffsetY) {
        Paint shadowPaint = new Paint(257);
        shadowPaint.setTextSize(_fontSize);
        shadowPaint.setTypeface(_typeface);
        shadowPaint.setShadowLayer(_shadowOpacity, _shadowOffsetX, _shadowOffsetY, _shadowColor);
        shadowPaint.setColor(_color2);
        shadowPaint.setStrokeWidth(_strokeWidth2);
        shadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(_textString, _offsetX, _offsetY, shadowPaint);
        Paint paint = new Paint(257);
        paint.setTextSize(_fontSize);
        paint.setTypeface(_typeface);
        paint.setColor(_color1);
        paint.setStrokeWidth(_strokeWidth1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
        paint.setColor(_color0);
        paint.setStrokeWidth(BitmapDescriptorFactory.HUE_RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(_textString, _offsetX, _offsetY, paint);
    }

    public static void drawStrokeRect(Canvas canvas, float _offsetX, float _offsetY, float _width, float _height, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, float _strokeWidth3, int _color3, float _radius) {
        Paint paint = new Paint(257);
        RectF rectF3 = new RectF(_offsetX, _offsetY, _offsetX + _width, _offsetY + _height);
        paint.setColor(_color3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF3, _radius, _radius, paint);
        RectF rectF2 = new RectF(_offsetX + _strokeWidth3, _offsetY + _strokeWidth3, (_offsetX + _width) - _strokeWidth3, (_offsetY + _height) - _strokeWidth3);
        paint.setColor(_color2);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF2, _radius - _strokeWidth3, _radius - _strokeWidth3, paint);
        RectF rectF1 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2, _offsetY + _strokeWidth3 + _strokeWidth2, ((_offsetX + _width) - _strokeWidth3) - _strokeWidth2, ((_offsetY + _height) - _strokeWidth3) - _strokeWidth2);
        paint.setColor(_color1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF1, (_radius - _strokeWidth3) - _strokeWidth2, (_radius - _strokeWidth3) - _strokeWidth2, paint);
        RectF rectF0 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, _offsetY + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, (((_offsetX + _width) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, (((_offsetY + _height) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1);
        paint.setColor(_color0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF0, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, paint);
    }

    public static void drawStrokeRectWithShadow(Canvas canvas, float _offsetX, float _offsetY, float _width, float _height, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, float _strokeWidth3, int _color3, float _radius, float shadowOpacity, float shadowOffsetX, float shadowOffsetY, int shadowColor) {
        Paint shadowPaint = new Paint(257);
        shadowPaint.setShadowLayer(shadowOpacity, shadowOffsetX, shadowOffsetY, shadowColor);
        RectF rectF3 = new RectF(_offsetX, _offsetY, _offsetX + _width, _offsetY + _height);
        shadowPaint.setColor(_color3);
        shadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF3, _radius, _radius, shadowPaint);
        Paint paint = new Paint(257);
        RectF rectF2 = new RectF(_offsetX + _strokeWidth3, _offsetY + _strokeWidth3, (_offsetX + _width) - _strokeWidth3, (_offsetY + _height) - _strokeWidth3);
        paint.setColor(_color2);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF2, _radius - _strokeWidth3, _radius - _strokeWidth3, paint);
        RectF rectF1 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2, _offsetY + _strokeWidth3 + _strokeWidth2, ((_offsetX + _width) - _strokeWidth3) - _strokeWidth2, ((_offsetY + _height) - _strokeWidth3) - _strokeWidth2);
        paint.setColor(_color1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF1, (_radius - _strokeWidth3) - _strokeWidth2, (_radius - _strokeWidth3) - _strokeWidth2, paint);
        RectF rectF0 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, _offsetY + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, (((_offsetX + _width) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, (((_offsetY + _height) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1);
        paint.setColor(_color0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF0, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, paint);
    }

    public static void drawBitmapWithShadow(Canvas canvas, Bitmap _bitmap, Rect _src, RectF _drc, float _shadowRadius, float _shadowOpacity, float _shadowOffsetX, float _shadowOffsetY, int _shadowColor) {
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(0);
        shadowPaint.setShadowLayer(_shadowOpacity, _shadowOpacity, _shadowOffsetY, _shadowColor);
        shadowPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(_drc, _shadowRadius, _shadowRadius, shadowPaint);
        Paint paint = new Paint(257);
        canvas.drawBitmap(_bitmap, _src, _drc, paint);
    }

    public static void drawOnlyStrokeRect(Canvas canvas, float _offsetX, float _offsetY, float _width, float _height, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, float _strokeWidth3, int _color3, float _radius) {
        Paint paint = new Paint(257);
        RectF rectF3 = new RectF(_offsetX, _offsetY, _offsetX + _width, _offsetY + _height);
        paint.setColor(_color3);
        paint.setStrokeWidth(_strokeWidth3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF3, _radius, _radius, paint);
        RectF rectF2 = new RectF(_offsetX + _strokeWidth3, _offsetY + _strokeWidth3, (_offsetX + _width) - _strokeWidth3, (_offsetY + _height) - _strokeWidth3);
        paint.setColor(_color2);
        paint.setStrokeWidth(_strokeWidth2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF2, _radius - _strokeWidth3, _radius - _strokeWidth3, paint);
        RectF rectF1 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2, _offsetY + _strokeWidth3 + _strokeWidth2, ((_offsetX + _width) - _strokeWidth3) - _strokeWidth2, ((_offsetY + _height) - _strokeWidth3) - _strokeWidth2);
        paint.setColor(_color1);
        paint.setStrokeWidth(_strokeWidth1);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF1, (_radius - _strokeWidth3) - _strokeWidth2, (_radius - _strokeWidth3) - _strokeWidth2, paint);
        RectF rectF0 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, _offsetY + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, (((_offsetX + _width) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, (((_offsetY + _height) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1);
        paint.setColor(_color0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF0, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, paint);
    }

    public static void drawOnly2StrokeRect(Canvas canvas, float _offsetX, float _offsetY, float _width, float _height, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, float _strokeWidth3, int _color3, float _radius) {
        Paint paint = new Paint(257);
        RectF rectF3 = new RectF(_offsetX, _offsetY, _offsetX + _width, _offsetY + _height);
        paint.setColor(_color3);
        paint.setStrokeWidth(_strokeWidth3);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF3, _radius, _radius, paint);
        RectF rectF2 = new RectF(_offsetX + _strokeWidth3, _offsetY + _strokeWidth3, (_offsetX + _width) - _strokeWidth3, (_offsetY + _height) - _strokeWidth3);
        paint.setColor(_color2);
        paint.setStrokeWidth(_strokeWidth2);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF2, _radius - _strokeWidth3, _radius - _strokeWidth3, paint);
        RectF rectF1 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2, _offsetY + _strokeWidth3 + _strokeWidth2, ((_offsetX + _width) - _strokeWidth3) - _strokeWidth2, ((_offsetY + _height) - _strokeWidth3) - _strokeWidth2);
        paint.setColor(_color1);
        paint.setStrokeWidth(_strokeWidth1);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF1, (_radius - _strokeWidth3) - _strokeWidth2, (_radius - _strokeWidth3) - _strokeWidth2, paint);
        RectF rectF0 = new RectF(_offsetX + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, _offsetY + _strokeWidth3 + _strokeWidth2 + _strokeWidth1, (((_offsetX + _width) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, (((_offsetY + _height) - _strokeWidth3) - _strokeWidth2) - _strokeWidth1);
        paint.setColor(_color0);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF0, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, ((_radius - _strokeWidth3) - _strokeWidth2) - _strokeWidth1, paint);
    }

    public static void drawOneRect(Canvas canvas, float _offsetX, float _offsetY, float _width, float _height, int _color, float _radius) {
        Paint paint = new Paint(257);
        RectF rectF = new RectF(_offsetX, _offsetY, _offsetX + _width, _offsetY + _height);
        paint.setColor(_color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF, _radius, _radius, paint);
    }

    public static void drawShadowRect(Canvas canvas, float _offsetX, float _offsetY, float _width, float _height, int _shadowColor, int _shadowOpacity, float _shadowOffsetX, float _shadowOffsetY, float _radius) {
        Paint paint = new Paint(257);
        RectF rect = new RectF(_offsetX, _offsetY, _offsetX + _width, _offsetY + _height);
        paint.setColor(0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setShadowLayer(_shadowOpacity, _shadowOffsetX, _shadowOffsetY, _shadowColor);
        canvas.drawRoundRect(rect, _radius, _radius, paint);
    }

    public static void onDestroy() {
    }
}

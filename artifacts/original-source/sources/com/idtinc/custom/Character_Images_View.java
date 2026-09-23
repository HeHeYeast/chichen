package com.idtinc.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import java.lang.reflect.Array;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Character_Images_View {
    private AlertUnitType0 alertUnitType0;
    private AppDelegate appDelegate;
    private float height;
    public boolean hidden;
    private float imageHeight;
    private short[][] imageInfosArray;
    private float imageSpaceX;
    private float imageSpaceY;
    private float[][] imageViewsArray;
    private float imageWidth;
    public short imagesTotal;
    MyDraw myDraw;
    private float offsetX;
    private float offsetY;
    public int textLabelColor0;
    public int textLabelColor1;
    public int textLabelColor2;
    public float textLabelFontSize;
    public float textLabelStroke1Width;
    public float textLabelStroke2Width;
    public Typeface textLabelTypeface;
    private float[][] textLabelsArray;
    private float width;
    private float zoomRate;

    public Character_Images_View(float _offsetX, float _offsetY, float _width, float _height, float _zoomrate, short _imagesTotal, AppDelegate _appDelegate, AlertUnitType0 _alertUnitType0) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.width = 1.0f;
        this.height = 1.0f;
        this.zoomRate = 1.0f;
        this.hidden = true;
        this.imagesTotal = (short) 0;
        this.imageSpaceX = BitmapDescriptorFactory.HUE_RED;
        this.imageSpaceY = BitmapDescriptorFactory.HUE_RED;
        this.imageWidth = BitmapDescriptorFactory.HUE_RED;
        this.imageHeight = BitmapDescriptorFactory.HUE_RED;
        this.textLabelFontSize = 16.0f;
        this.textLabelColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.textLabelStroke1Width = 6.0f;
        this.textLabelColor1 = 0;
        this.textLabelStroke2Width = 10.0f;
        this.textLabelColor2 = 0;
        this.appDelegate = _appDelegate;
        this.alertUnitType0 = _alertUnitType0;
        this.zoomRate = _zoomrate;
        this.offsetX = this.zoomRate * _offsetX;
        this.offsetY = this.zoomRate * _offsetY;
        this.width = this.zoomRate * _width;
        this.height = this.zoomRate * _height;
        this.hidden = true;
        this.imagesTotal = _imagesTotal;
        this.imageInfosArray = (short[][]) Array.newInstance((Class<?>) Short.TYPE, this.imagesTotal, 2);
        this.imageViewsArray = (float[][]) Array.newInstance((Class<?>) Float.TYPE, this.imagesTotal, 4);
        this.textLabelsArray = (float[][]) Array.newInstance((Class<?>) Float.TYPE, this.imagesTotal, 2);
        this.imageSpaceY = this.height / 2.0f;
        this.imageHeight = this.imageSpaceY + (8.0f * this.zoomRate);
        this.imageWidth = this.imageHeight;
        this.imageSpaceX = this.imageWidth + (11.0f * this.zoomRate);
        for (int i = 0; i < this.imagesTotal; i++) {
            this.imageInfosArray[i][0] = -1;
            this.imageInfosArray[i][1] = -1;
            this.imageViewsArray[i][0] = 0.0f;
            this.imageViewsArray[i][1] = 0.0f;
            this.imageViewsArray[i][2] = 0.0f;
            this.imageViewsArray[i][3] = 0.0f;
            this.textLabelsArray[i][0] = 0.0f;
            this.textLabelsArray[i][1] = 0.0f;
        }
        this.textLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.textLabelFontSize = 16.0f * this.zoomRate;
        this.textLabelColor0 = -419430656;
        this.textLabelStroke1Width = 2.0f * this.zoomRate;
        this.textLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.textLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.textLabelColor2 = 0;
        this.myDraw = new MyDraw();
    }

    public void gameDraw(Canvas canvas) {
        if (!this.hidden) {
            Paint bitmapPaint = new Paint();
            for (int i = 0; i < this.imagesTotal; i++) {
                if (this.imageInfosArray[i][0] >= 0 && this.appDelegate.character0Image0ArrayList != null && this.imageInfosArray[i][0] < this.appDelegate.character0Image0ArrayList.size()) {
                    Bitmap characterBitmap = this.appDelegate.character0Image0ArrayList.get(this.imageInfosArray[i][0]);
                    if (characterBitmap != null) {
                        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        canvas.drawBitmap(characterBitmap, new Rect(0, 0, characterBitmap.getWidth(), characterBitmap.getHeight()), new Rect((int) this.imageViewsArray[i][0], (int) this.imageViewsArray[i][1], (int) this.imageViewsArray[i][2], (int) this.imageViewsArray[i][3]), bitmapPaint);
                    }
                    MyDraw.drawStrokeText(canvas, this.textLabelsArray[i][0], this.textLabelsArray[i][1], this.textLabelTypeface, "\u3000x" + ((int) this.imageInfosArray[i][1]), this.textLabelFontSize, this.textLabelColor0, this.textLabelStroke1Width, this.textLabelColor1, this.textLabelStroke2Width, this.textLabelColor2);
                }
            }
        }
    }

    public short setInfos(String _infosString) {
        if (_infosString != null && _infosString.length() > 0) {
            String[] infosStringItems0 = _infosString.split(",");
            short returnCount = (short) infosStringItems0.length;
            float newOffsetX = this.alertUnitType0.backViewOffsetX + this.offsetX;
            float newOffsetY = this.alertUnitType0.backViewOffsetY + this.offsetY;
            if (returnCount > 0) {
                if (returnCount == 1) {
                    newOffsetX = this.alertUnitType0.backViewOffsetX + this.offsetX + (this.imageSpaceX * 2.0f);
                    newOffsetY = this.alertUnitType0.backViewOffsetY + this.offsetY + (this.imageSpaceY / 2.0f);
                } else if (returnCount == 2) {
                    newOffsetX = this.alertUnitType0.backViewOffsetX + this.offsetX + (this.imageSpaceX * 1.5f);
                    newOffsetY = this.alertUnitType0.backViewOffsetY + this.offsetY + (this.imageSpaceY / 2.0f);
                } else if (returnCount == 3) {
                    newOffsetX = this.alertUnitType0.backViewOffsetX + this.offsetX + (this.imageSpaceX * 1.0f);
                    newOffsetY = this.alertUnitType0.backViewOffsetY + this.offsetY + (this.imageSpaceY / 2.0f);
                } else if (returnCount == 4) {
                    newOffsetX = this.alertUnitType0.backViewOffsetX + this.offsetX + (this.imageSpaceX * 0.5f);
                    newOffsetY = this.alertUnitType0.backViewOffsetY + this.offsetY + (this.imageSpaceY / 2.0f);
                } else if (returnCount == 5) {
                    newOffsetX = this.alertUnitType0.backViewOffsetX + this.offsetX;
                    newOffsetY = this.alertUnitType0.backViewOffsetY + this.offsetY + (this.imageSpaceY / 2.0f);
                }
            }
            for (int i = 0; i < this.imagesTotal; i++) {
                this.imageInfosArray[i][0] = -1;
                this.imageInfosArray[i][1] = -1;
                this.imageViewsArray[i][0] = 0.0f;
                this.imageViewsArray[i][1] = 0.0f;
                this.imageViewsArray[i][2] = 0.0f;
                this.imageViewsArray[i][3] = 0.0f;
                this.textLabelsArray[i][0] = 0.0f;
                this.textLabelsArray[i][1] = 0.0f;
                if (i < returnCount && infosStringItems0[i] != null && infosStringItems0[i].length() >= 0) {
                    String[] infosStringItems1 = infosStringItems0[i].split("_");
                    if (infosStringItems1.length == 2) {
                        int characterID = -1;
                        if (infosStringItems1[0] != null) {
                            characterID = Integer.valueOf(infosStringItems1[0]).intValue();
                        }
                        int getCount = -1;
                        if (infosStringItems1[1] != null) {
                            getCount = Integer.valueOf(infosStringItems1[1]).intValue();
                        }
                        if (characterID >= 0 && getCount > 0) {
                            this.imageInfosArray[i][0] = (short) characterID;
                            this.imageInfosArray[i][1] = (short) getCount;
                            this.imageViewsArray[i][0] = (this.imageSpaceX * (i % 5)) + newOffsetX;
                            this.imageViewsArray[i][1] = (this.imageSpaceY * (i / 5)) + newOffsetY;
                            this.imageViewsArray[i][2] = this.imageViewsArray[i][0] + this.imageWidth;
                            this.imageViewsArray[i][3] = this.imageViewsArray[i][1] + this.imageHeight;
                            this.textLabelsArray[i][0] = this.imageViewsArray[i][0] + (25.0f * this.zoomRate);
                            this.textLabelsArray[i][1] = this.imageViewsArray[i][1] + (43.0f * this.zoomRate);
                        }
                    }
                }
            }
            this.hidden = false;
            return returnCount;
        }
        return (short) 0;
    }

    public void onDestroy() {
        this.myDraw = null;
        this.alertUnitType0 = null;
        this.appDelegate = null;
    }
}

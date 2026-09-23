package com.idtinc.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CPDisplayUnit {
    private AppDelegate appDelegate;
    private int backRectColor0;
    private int backRectColor1;
    private int backRectColor2;
    private int backRectColor3;
    private float backRectHeight;
    private float backRectOffsetX;
    private float backRectOffsetY;
    private float backRectRadius;
    private float backRectStrokeWidth1;
    private float backRectStrokeWidth2;
    private float backRectStrokeWidth3;
    private float backRectWidth;
    private int cpLabelColor0;
    private int cpLabelColor1;
    private int cpLabelColor2;
    private float cpLabelFontSize;
    private float cpLabelOffsetX;
    private float cpLabelOffsetY;
    String cpLabelString;
    private float cpLabelStroke1Width;
    private float cpLabelStroke2Width;
    Typeface cpLabelTypeface;
    private int cpWordLabelColor0;
    private int cpWordLabelColor1;
    private int cpWordLabelColor2;
    private float cpWordLabelFontSize;
    private float cpWordLabelOffsetX;
    private float cpWordLabelOffsetY;
    private float cpWordLabelStroke1Width;
    private float cpWordLabelStroke2Width;
    Typeface cpWordLabelTypeface;
    private float finalHeight;
    private float finalWidth;
    private float offsetX;
    private float offsetY;
    private float zoomRate;

    public CPDisplayUnit(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.cpLabelString = "-";
        this.backRectOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backRectOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backRectWidth = 130.0f;
        this.backRectHeight = 36.0f;
        this.backRectColor0 = Integer.MIN_VALUE;
        this.backRectStrokeWidth1 = 2.0f;
        this.backRectColor1 = -1291845648;
        this.backRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.backRectColor2 = 0;
        this.backRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backRectColor3 = 0;
        this.backRectRadius = 18.0f;
        this.cpLabelFontSize = 28.0f;
        this.cpLabelColor0 = -6106;
        this.cpLabelStroke1Width = 3.0f;
        this.cpLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.cpLabelStroke2Width = 5.0f;
        this.cpLabelColor2 = -16;
        this.cpLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.cpLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.cpWordLabelFontSize = 16.0f;
        this.cpWordLabelColor0 = -6106;
        this.cpWordLabelStroke1Width = 3.0f;
        this.cpWordLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.cpWordLabelStroke2Width = 5.0f;
        this.cpWordLabelColor2 = -16;
        this.cpWordLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.cpWordLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.cpLabelString = "-";
        this.backRectOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backRectOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backRectWidth = 130.0f * this.zoomRate;
        this.backRectHeight = 28.0f * this.zoomRate;
        this.backRectColor0 = Integer.MIN_VALUE;
        this.backRectStrokeWidth1 = 2.0f * this.zoomRate;
        this.backRectColor1 = -16;
        this.backRectStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backRectColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backRectColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backRectRadius = 14.0f * this.zoomRate;
        this.cpLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.cpLabelFontSize = 21.0f * this.zoomRate;
        this.cpLabelColor0 = -6106;
        this.cpLabelStroke1Width = 2.5f * this.zoomRate;
        this.cpLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.cpLabelStroke2Width = this.zoomRate * 5.0f;
        this.cpLabelColor2 = -16;
        this.cpLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.cpLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.cpWordLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.cpWordLabelFontSize = 16.0f * this.zoomRate;
        this.cpWordLabelColor0 = -6106;
        this.cpWordLabelStroke1Width = 2.5f * this.zoomRate;
        this.cpWordLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.cpWordLabelStroke2Width = this.zoomRate * 5.0f;
        this.cpWordLabelColor2 = -16;
        this.cpWordLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.cpWordLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
    }

    public void refresh() {
        int nowCp;
        this.cpLabelString = "-";
        if (this.appDelegate.timeSaveDictionary != null && (nowCp = (int) this.appDelegate.timeSaveDictionary.getPoint()) >= 0) {
            this.cpLabelString = new StringBuilder().append(nowCp).toString();
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.cpLabelTypeface);
        newPaint.setTextSize(this.cpLabelFontSize);
        this.cpLabelOffsetX = ((this.backRectOffsetX + this.backRectWidth) - newPaint.measureText(this.cpLabelString)) - (40.0f * this.zoomRate);
    }

    public void setBackViewParams(float _offsetX, float _offsetY) {
        this.backRectOffsetX = this.zoomRate * _offsetX;
        this.backRectOffsetY = this.zoomRate * _offsetY;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.cpLabelTypeface);
        newPaint.setTextSize(this.cpLabelFontSize);
        this.cpLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.cpLabelOffsetY = (this.backRectOffsetY + this.backRectHeight) - (this.cpLabelFontSize * 0.29f);
        newPaint.setTypeface(this.cpWordLabelTypeface);
        newPaint.setTextSize(this.cpWordLabelFontSize);
        this.cpWordLabelOffsetX = ((this.backRectOffsetX + this.backRectWidth) - newPaint.measureText("cp")) - (22.0f * this.zoomRate);
        this.cpWordLabelOffsetY = (this.backRectOffsetY + this.backRectHeight) - (this.cpWordLabelFontSize * 0.3f);
    }

    public void gameDraw(Canvas canvas) {
        MyDraw.drawOnlyStrokeRect(canvas, this.backRectOffsetX, this.backRectOffsetY, this.backRectWidth, this.backRectHeight, this.backRectColor0, this.backRectStrokeWidth1, this.backRectColor1, this.backRectStrokeWidth2, this.backRectColor2, this.backRectStrokeWidth3, this.backRectColor3, this.backRectRadius);
        MyDraw.drawStrokeText(canvas, this.cpWordLabelOffsetX, this.cpWordLabelOffsetY, this.cpWordLabelTypeface, "cp", this.cpWordLabelFontSize, this.cpWordLabelColor0, this.cpWordLabelStroke1Width, this.cpWordLabelColor1, this.cpWordLabelStroke2Width, this.cpWordLabelColor2);
        MyDraw.drawStrokeText(canvas, this.cpLabelOffsetX, this.cpLabelOffsetY, this.cpLabelTypeface, this.cpLabelString, this.cpLabelFontSize, this.cpLabelColor0, this.cpLabelStroke1Width, this.cpLabelColor1, this.cpLabelStroke2Width, this.cpLabelColor2);
    }

    public void onDestroy() {
        this.appDelegate = null;
    }
}

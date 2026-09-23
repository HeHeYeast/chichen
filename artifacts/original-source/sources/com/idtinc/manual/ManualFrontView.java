package com.idtinc.manual;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ManualFrontView extends View {
    private float BIGBACKVIEW_HEIGHT;
    private float BIGBACKVIEW_OFFSET_X;
    private float BIGBACKVIEW_OFFSET_Y;
    private float BIGBACKVIEW_WIDTH;
    private AppDelegate appDelegate;
    public int backButtonColor0;
    public int backButtonColor1;
    public int backButtonColor2;
    public int backButtonColor3;
    public float backButtonHeight;
    public float backButtonOffsetX;
    public float backButtonOffsetY;
    public float backButtonRadius;
    private short backButtonStatus;
    public float backButtonStrokeWidth1;
    public float backButtonStrokeWidth2;
    public float backButtonStrokeWidth3;
    public int backButtonTitleLabelColor0;
    public int backButtonTitleLabelColor1;
    public int backButtonTitleLabelColor2;
    public float backButtonTitleLabelFontSize;
    public float backButtonTitleLabelOffsetX;
    public float backButtonTitleLabelOffsetY;
    public String backButtonTitleLabelString;
    public float backButtonTitleLabelStroke1Width;
    public float backButtonTitleLabelStroke2Width;
    Typeface backButtonTitleLabelTypeface;
    public float backButtonWidth;
    public int backViewColor0;
    public int backViewColor1;
    public int backViewColor2;
    public int backViewColor3;
    public float backViewRadius;
    public float backViewStrokeWidth1;
    public float backViewStrokeWidth2;
    public float backViewStrokeWidth3;
    private short buttonClickCnt;
    private float finalHeight;
    private float finalWidth;
    private ManualLayout manualLayout;
    private MyDraw myDraw;
    private int titleBackRectColor0;
    private int titleBackRectColor1;
    private int titleBackRectColor2;
    private int titleBackRectColor3;
    private float titleBackRectHeight;
    private float titleBackRectOffsetX;
    private float titleBackRectOffsetY;
    private float titleBackRectRadius;
    private float titleBackRectStrokeWidth1;
    private float titleBackRectStrokeWidth2;
    private float titleBackRectStrokeWidth3;
    private float titleBackRectWidth;
    private int titleLabelColor0;
    private int titleLabelColor1;
    private int titleLabelColor2;
    private float titleLabelFontSize;
    private float titleLabelOffsetX;
    private float titleLabelOffsetY;
    String titleLabelString;
    private float titleLabelStroke1Width;
    private float titleLabelStroke2Width;
    Typeface titleLabelTypeface;
    private short touchButtonIndex;
    private float zoomRate;

    public ManualFrontView(Context context, float _finalwidth, float _finalheight, float _zoomrate, ManualLayout _manualLayout) {
        super(context);
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.backButtonStatus = (short) -1;
        this.BIGBACKVIEW_OFFSET_X = 25.0f;
        this.BIGBACKVIEW_OFFSET_Y = 47.0f;
        this.BIGBACKVIEW_WIDTH = 270.0f;
        this.BIGBACKVIEW_HEIGHT = 342.0f;
        this.backViewColor0 = 0;
        this.backViewStrokeWidth1 = 2.0f;
        this.backViewColor1 = -7576502;
        this.backViewStrokeWidth2 = 2.0f;
        this.backViewColor2 = -16;
        this.backViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor3 = 0;
        this.backViewRadius = 10.0f;
        this.titleLabelString = "";
        this.titleBackRectOffsetX = 60.0f;
        this.titleBackRectOffsetY = 12.0f;
        this.titleBackRectWidth = 200.0f;
        this.titleBackRectHeight = 30.0f;
        this.titleBackRectColor0 = -3355444;
        this.titleBackRectStrokeWidth1 = 1.0f;
        this.titleBackRectColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleBackRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor2 = 0;
        this.titleBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor3 = 0;
        this.titleBackRectRadius = 15.0f;
        this.titleLabelFontSize = 24.0f;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 3.0f;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        this.titleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backButtonWidth = 90.0f;
        this.backButtonHeight = 30.0f;
        this.backButtonOffsetX = 115.0f;
        this.backButtonOffsetY = 395.0f;
        this.backButtonColor0 = -227838;
        this.backButtonStrokeWidth1 = 2.0f;
        this.backButtonColor1 = -436207632;
        this.backButtonStrokeWidth2 = 2.0f;
        this.backButtonColor2 = -7576502;
        this.backButtonStrokeWidth3 = 1.0f;
        this.backButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonRadius = 10.0f;
        this.backButtonTitleLabelString = "";
        this.backButtonTitleLabelFontSize = 20.0f;
        this.backButtonTitleLabelColor0 = -1;
        this.backButtonTitleLabelStroke1Width = 2.0f;
        this.backButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelColor2 = 0;
        this.backButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.manualLayout = _manualLayout;
        setBackgroundColor(0);
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.backButtonStatus = (short) -1;
        this.BIGBACKVIEW_OFFSET_X = 23.0f * this.zoomRate;
        this.BIGBACKVIEW_OFFSET_Y = 47.0f * this.zoomRate;
        this.BIGBACKVIEW_WIDTH = 274.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.BIGBACKVIEW_HEIGHT = 342.0f * this.zoomRate;
        } else {
            this.BIGBACKVIEW_HEIGHT = 430.0f * this.zoomRate;
        }
        this.backViewColor0 = 0;
        this.backViewStrokeWidth1 = this.zoomRate * 2.0f;
        this.backViewColor1 = -3109815;
        this.backViewStrokeWidth2 = 4.0f * this.zoomRate;
        this.backViewColor2 = -3889;
        this.backViewStrokeWidth3 = 3.0f * this.zoomRate;
        this.backViewColor3 = -3109815;
        this.backViewRadius = 10.0f * this.zoomRate;
        this.titleLabelString = "";
        this.titleBackRectOffsetX = 60.0f * this.zoomRate;
        this.titleBackRectOffsetY = 12.0f * this.zoomRate;
        this.titleBackRectWidth = 200.0f * this.zoomRate;
        this.titleBackRectHeight = 30.0f * this.zoomRate;
        this.titleBackRectColor0 = -3889;
        this.titleBackRectStrokeWidth1 = this.zoomRate * 2.0f;
        this.titleBackRectColor1 = -3109815;
        this.titleBackRectStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor2 = 0;
        this.titleBackRectStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor3 = 0;
        this.titleBackRectRadius = 15.0f * this.zoomRate;
        this.titleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.titleLabelFontSize = 24.0f * this.zoomRate;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 3.0f * this.zoomRate;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        this.titleLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backButtonWidth = 90.0f * this.zoomRate;
        this.backButtonHeight = 30.0f * this.zoomRate;
        this.backButtonOffsetX = 115.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.backButtonOffsetY = 395.0f * this.zoomRate;
        } else {
            this.backButtonOffsetY = 483.0f * this.zoomRate;
        }
        this.backButtonColor0 = -227838;
        this.backButtonStrokeWidth1 = this.zoomRate * 2.0f;
        this.backButtonColor1 = -436207632;
        this.backButtonStrokeWidth2 = this.zoomRate * 2.0f;
        this.backButtonColor2 = -7576502;
        this.backButtonStrokeWidth3 = 1.0f * this.zoomRate;
        this.backButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonRadius = 10.0f * this.zoomRate;
        this.backButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.Back);
        this.backButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.backButtonTitleLabelFontSize = 20.0f * this.zoomRate;
        this.backButtonTitleLabelColor0 = -1;
        this.backButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.backButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelColor2 = 0;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.backButtonTitleLabelTypeface);
        newPaint.setTextSize(this.backButtonTitleLabelFontSize);
        this.backButtonTitleLabelOffsetX = this.backButtonOffsetX + ((this.backButtonWidth - newPaint.measureText(this.backButtonTitleLabelString)) / 2.0f);
        this.backButtonTitleLabelOffsetY = this.backButtonOffsetY + ((this.backButtonHeight + (this.backButtonTitleLabelFontSize * 0.8f)) / 2.0f);
        this.myDraw = new MyDraw();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        MyDraw.drawOnlyStrokeRect(canvas, this.BIGBACKVIEW_OFFSET_X, this.BIGBACKVIEW_OFFSET_Y, this.BIGBACKVIEW_WIDTH, this.BIGBACKVIEW_HEIGHT, this.backViewColor0, this.backViewStrokeWidth1, this.backViewColor1, this.backViewStrokeWidth2, this.backViewColor2, this.backViewStrokeWidth3, this.backViewColor3, this.backViewRadius);
        MyDraw.drawStrokeRect(canvas, this.titleBackRectOffsetX, this.titleBackRectOffsetY, this.titleBackRectWidth, this.titleBackRectHeight, this.titleBackRectColor0, this.titleBackRectStrokeWidth1, this.titleBackRectColor1, this.titleBackRectStrokeWidth2, this.titleBackRectColor2, this.titleBackRectStrokeWidth3, this.titleBackRectColor3, this.titleBackRectRadius);
        MyDraw.drawStrokeText(canvas, this.titleLabelOffsetX, this.titleLabelOffsetY, this.titleLabelTypeface, this.titleLabelString, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        if (this.backButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.backButtonOffsetX, this.backButtonOffsetY, this.backButtonWidth, this.backButtonHeight, this.backButtonColor0, this.backButtonStrokeWidth1, this.backButtonColor1, this.backButtonStrokeWidth2, this.backButtonColor2, this.backButtonStrokeWidth3, this.backButtonColor3, this.backButtonRadius);
            MyDraw.drawStrokeText(canvas, this.backButtonTitleLabelOffsetX, this.backButtonTitleLabelOffsetY, this.backButtonTitleLabelTypeface, this.backButtonTitleLabelString, this.backButtonTitleLabelFontSize, this.backButtonTitleLabelColor0, this.backButtonTitleLabelStroke1Width, this.backButtonTitleLabelColor1, this.backButtonTitleLabelStroke2Width, this.backButtonTitleLabelColor2);
            if (this.touchButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.backButtonOffsetX, this.backButtonOffsetY, this.backButtonWidth, this.backButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.backButtonRadius);
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        Log.d("StoreListSelectLayout", "onTouchEvent");
        if (event.getAction() == 0 && this.backButtonStatus == 0 && event.getY() > this.backButtonOffsetY && event.getY() < this.backButtonOffsetY + this.backButtonHeight && event.getX() > this.backButtonOffsetX && event.getX() < this.backButtonOffsetX + this.backButtonWidth) {
            this.touchButtonIndex = (short) 0;
            this.buttonClickCnt = (short) 2;
            Log.d("StoreListSelectLayout", "X=" + event.getX() + ", Y=  " + event.getY());
            Log.d("StoreListSelectLayout", "touchButtonIndex:" + ((int) this.touchButtonIndex));
            invalidate();
        }
        return false;
    }

    public void changManualIndex(short _manualIndex) {
        this.titleLabelString = this.appDelegate.getResources().getString(R.string.KitchenManual);
        if (_manualIndex == 1) {
            this.titleLabelString = this.appDelegate.getResources().getString(R.string.FarmManual);
        } else if (_manualIndex == 2) {
            this.titleLabelString = this.appDelegate.getResources().getString(R.string.StoreManual);
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.titleLabelTypeface);
        newPaint.setTextSize(this.titleLabelFontSize);
        this.titleLabelOffsetX = this.titleBackRectOffsetX + ((this.titleBackRectWidth - newPaint.measureText(this.titleLabelString)) / 2.0f);
        this.titleLabelOffsetY = (this.titleBackRectOffsetY + this.titleBackRectHeight) - (this.titleLabelFontSize * 0.25f);
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.backButtonStatus = (short) 0;
        invalidate();
    }

    public void doLoop() {
        if (this.touchButtonIndex == 0) {
            if (this.buttonClickCnt > 0) {
                this.buttonClickCnt = (short) (this.buttonClickCnt - 1);
                if (this.buttonClickCnt == 1) {
                    this.manualLayout.doClose();
                    return;
                } else {
                    if (this.buttonClickCnt <= 0) {
                        this.touchButtonIndex = (short) -1;
                        this.buttonClickCnt = (short) -1;
                        return;
                    }
                    return;
                }
            }
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            return;
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void onDestroy() {
        this.myDraw = null;
        this.manualLayout = null;
        this.appDelegate = null;
    }
}

package com.idtinc.ck_bonus;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class BonusScrollViewUnit {
    private AppDelegate appDelegate;
    private BonusUnitView bonusUnitView;
    public float cellSpaceLineHeight;
    public float cellSpaceY;
    public float contentLabelFontSize;
    public float contentLabelOffsetX;
    public float contentLabelOffsetY;
    public String contentLabelString;
    public float contentLabelStroke1Width;
    public float contentLabelStroke2Width;
    public Typeface contentLabelTypeface;
    private float finalHeight;
    private float finalWidth;
    private short goButtonClickCnt;
    public int goButtonColor0;
    public int goButtonColor1;
    public int goButtonColor2;
    public int goButtonColor3;
    public float goButtonHeight;
    public float goButtonOffsetX;
    public float goButtonOffsetY;
    public float goButtonRadius;
    public float goButtonStrokeWidth1;
    public float goButtonStrokeWidth2;
    public float goButtonStrokeWidth3;
    public int goButtonTitleLabelColor0;
    public int goButtonTitleLabelColor1;
    public int goButtonTitleLabelColor2;
    public float goButtonTitleLabelFontSize;
    public float goButtonTitleLabelOffsetX;
    public float goButtonTitleLabelOffsetY;
    public String goButtonTitleLabelString;
    public float goButtonTitleLabelStroke1Width;
    public float goButtonTitleLabelStroke2Width;
    Typeface goButtonTitleLabelTypeface;
    public float goButtonWidth;
    private int loadingBackRectColor0;
    private int loadingBackRectColor1;
    private int loadingBackRectColor2;
    private int loadingBackRectColor3;
    private float loadingBackRectHeight;
    private float loadingBackRectOffsetX;
    private float loadingBackRectOffsetY;
    private float loadingBackRectRadius;
    private float loadingBackRectStrokeWidth1;
    private float loadingBackRectStrokeWidth2;
    private float loadingBackRectStrokeWidth3;
    private float loadingBackRectWidth;
    private short loadingCnt;
    private int loadingLabelColor0;
    private int loadingLabelColor1;
    private int loadingLabelColor2;
    private float loadingLabelFontSize;
    private float loadingLabelOffsetX;
    private float loadingLabelOffsetY;
    private String loadingLabelString;
    private float loadingLabelStroke1Width;
    private float loadingLabelStroke2Width;
    Typeface loadingLabelTypeface;
    public float offsetScrollY;
    private float offsetScrollYMax;
    private float offsetX;
    private float offsetY;
    private float originHeight;
    private float preScrollX;
    private float preScrollY;
    private short selectedGoButtonIndex;
    public float smallImageViewHeight;
    public float smallImageViewOffsetX;
    public float smallImageViewOffsetY;
    public float smallImageViewWidth;
    public float titleLabel0OffsetX;
    public float titleLabel0OffsetY;
    public String titleLabel0String;
    public float titleLabel0Stroke1Width;
    public float titleLabel0Stroke2Width;
    public float titleLabel1OffsetX;
    public float titleLabel1OffsetY;
    public String titleLabel1String;
    public float titleLabel1Stroke1Width;
    public float titleLabel1Stroke2Width;
    public float titleLabelFontSize;
    public Typeface titleLabelTypeface;
    private float zoomRate;

    public BonusScrollViewUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, BonusUnitView _bonusUnitView, AppDelegate _appDelegate) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.originHeight = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.loadingCnt = (short) 0;
        this.preScrollX = -9999.0f;
        this.preScrollY = -9999.0f;
        this.selectedGoButtonIndex = (short) -1;
        this.goButtonClickCnt = (short) -1;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectOffsetX = 95.0f;
        this.loadingBackRectOffsetY = 174.0f;
        this.loadingBackRectWidth = 130.0f;
        this.loadingBackRectHeight = 130.0f;
        this.loadingBackRectColor0 = 1426063360;
        this.loadingBackRectStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor1 = 0;
        this.loadingBackRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor2 = 0;
        this.loadingBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor3 = 0;
        this.loadingBackRectRadius = 10.0f;
        this.loadingLabelString = "";
        this.loadingLabelFontSize = 16.0f;
        this.loadingLabelColor0 = -1;
        this.loadingLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelColor1 = -1;
        this.loadingLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelColor2 = 0;
        this.loadingLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.smallImageViewOffsetX = 10.0f;
        this.smallImageViewOffsetY = 9.0f;
        this.smallImageViewWidth = 60.0f;
        this.smallImageViewHeight = 60.0f;
        this.cellSpaceY = 78.0f;
        this.cellSpaceLineHeight = 1.0f;
        this.titleLabelFontSize = 14.0f;
        this.titleLabel0String = "";
        this.titleLabel0Stroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabel0Stroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabel0OffsetX = 75.0f;
        this.titleLabel0OffsetY = 6.0f;
        this.titleLabel1String = "";
        this.titleLabel1Stroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabel1Stroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabel1OffsetX = 75.0f;
        this.titleLabel1OffsetY = 6.0f;
        this.contentLabelString = "";
        this.contentLabelFontSize = 14.0f;
        this.contentLabelStroke1Width = 2.0f;
        this.contentLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.contentLabelOffsetX = 75.0f;
        this.contentLabelOffsetY = 6.0f;
        this.goButtonWidth = 56.0f;
        this.goButtonHeight = 30.0f;
        this.goButtonOffsetX = 255.0f;
        this.goButtonOffsetY = 24.0f;
        this.goButtonColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.goButtonStrokeWidth1 = 1.0f;
        this.goButtonColor1 = 855638016;
        this.goButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.goButtonColor2 = 0;
        this.goButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.goButtonColor3 = 0;
        this.goButtonRadius = 10.0f;
        this.goButtonTitleLabelString = "";
        this.goButtonTitleLabelFontSize = 14.0f;
        this.goButtonTitleLabelColor0 = -1;
        this.goButtonTitleLabelStroke1Width = 2.0f;
        this.goButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.goButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.goButtonTitleLabelColor2 = 0;
        this.goButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.goButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        this.bonusUnitView = _bonusUnitView;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.originHeight = _finalheight;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.preScrollX = -9999.0f;
        this.preScrollY = -9999.0f;
        this.loadingCnt = (short) 0;
        this.selectedGoButtonIndex = (short) -1;
        this.goButtonClickCnt = (short) -1;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        Paint newPaint = new Paint(257);
        this.loadingBackRectOffsetX = 95.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.loadingBackRectOffsetY = 130.0f * this.zoomRate;
        } else {
            this.loadingBackRectOffsetY = 174.0f * this.zoomRate;
        }
        this.loadingBackRectWidth = 130.0f * this.zoomRate;
        this.loadingBackRectHeight = 130.0f * this.zoomRate;
        this.loadingBackRectColor0 = 1426063360;
        this.loadingBackRectStrokeWidth1 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.loadingBackRectColor1 = 0;
        this.loadingBackRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.loadingBackRectColor2 = 0;
        this.loadingBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.loadingBackRectColor3 = 0;
        this.loadingBackRectRadius = 10.0f * this.zoomRate;
        this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + "...";
        this.loadingLabelTypeface = Typeface.DEFAULT_BOLD;
        this.loadingLabelFontSize = 16.0f * this.zoomRate;
        this.loadingLabelColor0 = -1;
        this.loadingLabelStroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.loadingLabelColor1 = -1;
        this.loadingLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.loadingLabelColor2 = 0;
        newPaint.setTypeface(this.loadingLabelTypeface);
        newPaint.setTextSize(this.loadingLabelFontSize);
        this.loadingLabelOffsetX = this.loadingBackRectOffsetX + ((this.loadingBackRectWidth - newPaint.measureText(this.loadingLabelString)) / 2.0f);
        this.loadingLabelOffsetY = this.loadingBackRectOffsetY + (this.loadingBackRectHeight / 2.0f) + (4.0f * this.zoomRate);
        this.smallImageViewOffsetX = 10.0f * this.zoomRate;
        this.smallImageViewOffsetY = 9.0f * this.zoomRate;
        this.smallImageViewWidth = 60.0f * this.zoomRate;
        this.smallImageViewHeight = 60.0f * this.zoomRate;
        this.cellSpaceY = 78.0f * this.zoomRate;
        this.cellSpaceLineHeight = 1.0f * this.zoomRate;
        this.titleLabelTypeface = Typeface.DEFAULT_BOLD;
        this.titleLabelFontSize = 14.0f * this.zoomRate;
        this.titleLabel0String = "";
        this.titleLabel0Stroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.titleLabel0Stroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.titleLabel0OffsetX = 75.0f * this.zoomRate;
        this.titleLabel0OffsetY = 24.0f * this.zoomRate;
        this.titleLabel1String = "";
        this.titleLabel1Stroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.titleLabel1Stroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.titleLabel1OffsetX = 75.0f * this.zoomRate;
        this.titleLabel1OffsetY = 42.0f * this.zoomRate;
        this.contentLabelString = "";
        this.contentLabelTypeface = Typeface.DEFAULT_BOLD;
        this.contentLabelFontSize = 14.0f * this.zoomRate;
        this.contentLabelStroke1Width = 2.0f * this.zoomRate;
        this.contentLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.contentLabelOffsetX = 75.0f * this.zoomRate;
        this.contentLabelOffsetY = 66.0f * this.zoomRate;
        this.goButtonWidth = 56.0f * this.zoomRate;
        this.goButtonHeight = 30.0f * this.zoomRate;
        this.goButtonOffsetX = 255.0f * this.zoomRate;
        this.goButtonOffsetY = 24.0f * this.zoomRate;
        this.goButtonColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.goButtonStrokeWidth1 = 1.0f * this.zoomRate;
        this.goButtonColor1 = 855638016;
        this.goButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.goButtonColor2 = 0;
        this.goButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.goButtonColor3 = 0;
        this.goButtonRadius = 10.0f * this.zoomRate;
        this.goButtonTitleLabelString = "";
        this.goButtonTitleLabelTypeface = Typeface.DEFAULT_BOLD;
        this.goButtonTitleLabelFontSize = 14.0f * this.zoomRate;
        newPaint.setTypeface(this.goButtonTitleLabelTypeface);
        newPaint.setTextSize(this.goButtonTitleLabelFontSize);
        this.goButtonTitleLabelColor0 = -1;
        this.goButtonTitleLabelStroke1Width = 2.0f * this.zoomRate;
        this.goButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.goButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.goButtonTitleLabelColor2 = 0;
        this.goButtonTitleLabelOffsetX = this.goButtonOffsetX + ((this.goButtonWidth - newPaint.measureText(this.goButtonTitleLabelString)) / 2.0f);
        this.goButtonTitleLabelOffsetY = this.goButtonOffsetY + ((this.goButtonHeight + (this.goButtonTitleLabelFontSize * 0.7f)) / 2.0f);
    }

    public void reload() {
        this.selectedGoButtonIndex = (short) -1;
        this.goButtonClickCnt = (short) -1;
        if (this.bonusUnitView.bonusPagesArrayList != null) {
            for (int i = 0; i < this.bonusUnitView.bonusPagesArrayList.size(); i++) {
                BonusPage bonusPage = this.bonusUnitView.bonusPagesArrayList.get(i);
                if (bonusPage != null) {
                    bonusPage.buttonStatus = (short) 0;
                    bonusPage.clickedF = false;
                    if (bonusPage.openType != 1000 && this.appDelegate.defaultSharedPreferences != null) {
                        this.appDelegate.defaultSharedPreferences.edit();
                        if (bonusPage.openDateKeyString.length() >= 3) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            SimpleDateFormat sdfday = new SimpleDateFormat("yyyy/MM/dd");
                            Date nowDate = new Date();
                            String nowDateString = sdfday.format(nowDate);
                            String openDateString = this.appDelegate.defaultSharedPreferences.getString(bonusPage.openDateKeyString, "");
                            if (openDateString.length() > 0) {
                                String openDateDayString = "";
                                try {
                                    openDateDayString = sdfday.format(sdf.parse(openDateString));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (openDateDayString.equals(nowDateString)) {
                                    bonusPage.buttonStatus = (short) 1;
                                }
                            }
                            String getCpBlockDateString = this.appDelegate.get_get_cp_block_date();
                            if (getCpBlockDateString != null && getCpBlockDateString.length() > 0 && getCpBlockDateString.equals(nowDateString)) {
                                bonusPage.buttonStatus = (short) 1;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        this.selectedGoButtonIndex = (short) -1;
        this.goButtonClickCnt = (short) -1;
        if (this.bonusUnitView.getVisibility() == 0 && this.bonusUnitView.bonusPagesArrayList != null) {
            this.preScrollY = event.getY();
            if (event.getX() > this.goButtonOffsetX && event.getX() < this.goButtonOffsetX + this.goButtonWidth && event.getY() > this.offsetY && event.getY() < this.offsetY + this.originHeight) {
                short buttonIndex = (short) (((this.offsetScrollY + event.getY()) - this.offsetY) / this.cellSpaceY);
                float checkOffsetY = ((this.offsetScrollY + event.getY()) - this.offsetY) - (this.cellSpaceY * buttonIndex);
                if (checkOffsetY > this.goButtonOffsetY && checkOffsetY < this.goButtonOffsetY + this.goButtonHeight && buttonIndex >= 0 && buttonIndex < this.bonusUnitView.bonusPagesArrayList.size()) {
                    this.bonusUnitView.goToGetWithIndex(buttonIndex);
                }
            }
        }
        return true;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        MyDraw.drawOneRect(canvas, BitmapDescriptorFactory.HUE_RED, this.offsetY, this.finalWidth, this.finalHeight + this.offsetY, -1, BitmapDescriptorFactory.HUE_RED);
        if (this.bonusUnitView.nowStatus != 1) {
            if (this.bonusUnitView.nowStatus == 0) {
                this.loadingCnt = (short) (this.loadingCnt - 1);
                if (this.loadingCnt <= 0) {
                    this.loadingCnt = (short) 15;
                    this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + ".";
                } else if (this.loadingCnt == 10) {
                    this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + "..";
                } else if (this.loadingCnt == 5) {
                    this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + "...";
                }
                MyDraw.drawStrokeRect(canvas, this.loadingBackRectOffsetX, this.loadingBackRectOffsetY + this.offsetY, this.loadingBackRectWidth, this.loadingBackRectHeight, this.loadingBackRectColor0, this.loadingBackRectStrokeWidth1, this.loadingBackRectColor1, this.loadingBackRectStrokeWidth2, this.loadingBackRectColor2, this.loadingBackRectStrokeWidth3, this.loadingBackRectColor3, this.loadingBackRectRadius);
                MyDraw.drawStrokeText(canvas, this.loadingLabelOffsetX, this.loadingLabelOffsetY + this.offsetY, this.loadingLabelTypeface, this.loadingLabelString, this.loadingLabelFontSize, this.loadingLabelColor0, this.loadingLabelStroke1Width, this.loadingLabelColor1, this.loadingLabelStroke2Width, 0);
                return;
            }
            return;
        }
        if (this.bonusUnitView.bonusPagesArrayList != null) {
            for (int i = 0; i < this.bonusUnitView.bonusPagesArrayList.size(); i++) {
                float cellOffsetY = this.offsetY + (this.cellSpaceY * i);
                BonusPage bonusPage = this.bonusUnitView.bonusPagesArrayList.get(i);
                if (bonusPage != null) {
                    MyDraw.drawOneRect(canvas, BitmapDescriptorFactory.HUE_RED, (this.cellSpaceY * i) + this.offsetY, this.finalWidth, this.cellSpaceY, -16, BitmapDescriptorFactory.HUE_RED);
                    MyDraw.drawOneRect(canvas, BitmapDescriptorFactory.HUE_RED, ((this.cellSpaceY + cellOffsetY) - this.cellSpaceLineHeight) - BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.cellSpaceLineHeight, 570425344, BitmapDescriptorFactory.HUE_RED);
                    if (getByteLength(bonusPage.contentString) < 38) {
                        this.titleLabel0OffsetY = 33.0f * this.zoomRate;
                        this.titleLabel0String = bonusPage.contentString;
                        this.titleLabel1String = "";
                    } else {
                        int nowByteLength = 0;
                        int titleLabel0EndIndex = 0;
                        int titleLabel1EndIndex = bonusPage.contentString.length();
                        for (int k = 0; k < bonusPage.contentString.length(); k++) {
                            int preByteLength = nowByteLength;
                            nowByteLength += getByteLength(bonusPage.contentString.substring(k, k + 1));
                            if (preByteLength < 38 && nowByteLength >= 38) {
                                titleLabel0EndIndex = k;
                            }
                            if (preByteLength < 76 && nowByteLength >= 76) {
                                titleLabel1EndIndex = k;
                            }
                        }
                        this.titleLabel0OffsetY = 24.0f * this.zoomRate;
                        this.titleLabel0String = bonusPage.contentString.substring(0, titleLabel0EndIndex);
                        this.titleLabel1String = bonusPage.contentString.substring(titleLabel0EndIndex, titleLabel1EndIndex);
                    }
                    if (this.titleLabel0String.length() > 0) {
                        MyDraw.drawStrokeText(canvas, this.titleLabel0OffsetX, this.titleLabel0OffsetY + cellOffsetY, this.titleLabelTypeface, this.titleLabel0String, this.titleLabelFontSize, FluctConstants.FRAME_BASE_COLOR, this.titleLabel0Stroke1Width, FluctConstants.FRAME_BASE_COLOR, this.titleLabel0Stroke2Width, 0);
                    }
                    if (this.titleLabel1String.length() > 0) {
                        MyDraw.drawStrokeText(canvas, this.titleLabel1OffsetX, this.titleLabel1OffsetY + cellOffsetY, this.titleLabelTypeface, this.titleLabel1String, this.titleLabelFontSize, FluctConstants.FRAME_BASE_COLOR, this.titleLabel1Stroke1Width, FluctConstants.FRAME_BASE_COLOR, this.titleLabel1Stroke2Width, 0);
                    }
                    String languageString = this.appDelegate.getLocaleLanguage();
                    if (bonusPage.buttonStatus == 0) {
                        if (languageString.equals("ja-JP")) {
                            this.contentLabelString = "【 " + bonusPage.bonus + " cp  獲得 】";
                            this.goButtonTitleLabelString = "獲得";
                        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                            this.contentLabelString = "【 獲得  " + bonusPage.bonus + " cp 】";
                            this.goButtonTitleLabelString = "獲得";
                        } else if (languageString.equals("zh-CN")) {
                            this.contentLabelString = "【 获得  " + bonusPage.bonus + " cp 】";
                            this.goButtonTitleLabelString = "获得";
                        } else {
                            this.contentLabelString = "【 Get  " + bonusPage.bonus + " cp 】";
                            this.goButtonTitleLabelString = "Get";
                        }
                        if (this.contentLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, this.contentLabelOffsetX, this.contentLabelOffsetY + cellOffsetY, this.contentLabelTypeface, this.contentLabelString, this.contentLabelFontSize, -256, this.contentLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.contentLabelStroke2Width, 0);
                        }
                        Paint newPaint = new Paint(257);
                        newPaint.setTypeface(this.goButtonTitleLabelTypeface);
                        newPaint.setTextSize(this.goButtonTitleLabelFontSize);
                        this.goButtonTitleLabelOffsetX = this.goButtonOffsetX + ((this.goButtonWidth - newPaint.measureText(this.goButtonTitleLabelString)) / 2.0f);
                        MyDraw.drawStrokeRect(canvas, this.goButtonOffsetX, this.goButtonOffsetY + cellOffsetY, this.goButtonWidth, this.goButtonHeight, this.goButtonColor0, this.goButtonStrokeWidth1, this.goButtonColor1, this.goButtonStrokeWidth2, this.goButtonColor2, this.goButtonStrokeWidth3, this.goButtonColor3, this.goButtonRadius);
                        MyDraw.drawStrokeText(canvas, this.goButtonTitleLabelOffsetX, this.goButtonTitleLabelOffsetY + cellOffsetY, this.goButtonTitleLabelTypeface, this.goButtonTitleLabelString, this.goButtonTitleLabelFontSize, -256, this.goButtonTitleLabelStroke1Width, this.goButtonTitleLabelColor1, this.goButtonTitleLabelStroke2Width, this.goButtonTitleLabelColor2);
                    } else {
                        if (languageString.equals("ja-JP")) {
                            this.contentLabelString = "【 獲得済 】";
                            this.goButtonTitleLabelString = "Go";
                        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                            this.contentLabelString = "【 已獲得 】";
                            this.goButtonTitleLabelString = "前往";
                        } else if (languageString.equals("zh-CN")) {
                            this.contentLabelString = "【 已获得 】";
                            this.goButtonTitleLabelString = "前往";
                        } else {
                            this.contentLabelString = "【  -  】";
                            this.goButtonTitleLabelString = "Go";
                        }
                        if (this.contentLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, this.contentLabelOffsetX, this.contentLabelOffsetY + cellOffsetY, this.contentLabelTypeface, this.contentLabelString, this.contentLabelFontSize, -1, this.contentLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.contentLabelStroke2Width, 0);
                        }
                        Paint newPaint2 = new Paint(257);
                        newPaint2.setTypeface(this.goButtonTitleLabelTypeface);
                        newPaint2.setTextSize(this.goButtonTitleLabelFontSize);
                        this.goButtonTitleLabelOffsetX = this.goButtonOffsetX + ((this.goButtonWidth - newPaint2.measureText(this.goButtonTitleLabelString)) / 2.0f);
                        MyDraw.drawStrokeRect(canvas, this.goButtonOffsetX, this.goButtonOffsetY + cellOffsetY, this.goButtonWidth, this.goButtonHeight, this.goButtonColor0, this.goButtonStrokeWidth1, this.goButtonColor1, this.goButtonStrokeWidth2, this.goButtonColor2, this.goButtonStrokeWidth3, this.goButtonColor3, this.goButtonRadius);
                        MyDraw.drawStrokeText(canvas, this.goButtonTitleLabelOffsetX, this.goButtonTitleLabelOffsetY + cellOffsetY, this.goButtonTitleLabelTypeface, this.goButtonTitleLabelString, this.goButtonTitleLabelFontSize, -1, this.goButtonTitleLabelStroke1Width, this.goButtonTitleLabelColor1, this.goButtonTitleLabelStroke2Width, this.goButtonTitleLabelColor2);
                    }
                    if (bonusPage.smallImageBitmap != null && !bonusPage.smallImageLoadingF) {
                        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        float smallImageBitmapOffsetY = cellOffsetY + this.smallImageViewOffsetY;
                        canvas.drawBitmap(bonusPage.smallImageBitmap, new Rect(0, 0, bonusPage.smallImageBitmap.getWidth(), bonusPage.smallImageBitmap.getHeight()), new Rect((int) this.smallImageViewOffsetX, (int) smallImageBitmapOffsetY, (int) (this.smallImageViewOffsetX + this.smallImageViewWidth), (int) (this.smallImageViewHeight + smallImageBitmapOffsetY)), bitmapPaint);
                    }
                }
            }
        }
    }

    public int getByteLength(String tgtStr) {
        byte[] bytes = tgtStr.getBytes();
        return bytes.length;
    }

    public void onDestroy() {
        this.bonusUnitView = null;
        this.appDelegate = null;
    }
}

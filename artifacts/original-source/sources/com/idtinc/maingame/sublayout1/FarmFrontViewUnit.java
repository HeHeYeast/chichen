package com.idtinc.maingame.sublayout1;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.MyDraw;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmFrontViewUnit {
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
    private short buttonClickCnt;
    private FarmUnit farmUnit;
    private float finalHeight;
    private float finalWidth;
    public float fixFarmButtonHeight;
    public float fixFarmButtonOffsetX;
    public float fixFarmButtonOffsetY;
    private short fixFarmButtonStatus;
    public float fixFarmButtonWidth;
    private short hp;
    private MyDraw myDraw;
    private short rate;
    private int rateLabelColor0;
    private int rateLabelColor1;
    private int rateLabelColor2;
    private float rateLabelFontSize;
    private float rateLabelOffsetX;
    private float rateLabelOffsetY;
    String rateLabelString;
    private float rateLabelStroke1Width;
    private float rateLabelStroke2Width;
    Typeface rateLabelTypeface;
    private short refreshCount;
    private short touchButtonIndex;
    private float zoomRate;
    public short farmFrontBitmapIndex = -1;
    public Bitmap farmFrontBitmap0 = null;
    public Bitmap farmFrontBitmap1 = null;
    private Bitmap fixFarmButtonBitmap0 = null;
    private Bitmap fixFarmButtonBitmap1 = null;

    public FarmFrontViewUnit(float _finalwidth, float _finalheight, float _zoomrate, FarmUnit _farmUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.refreshCount = (short) 0;
        this.hp = (short) 100;
        this.rate = (short) 0;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.fixFarmButtonStatus = (short) -1;
        this.rateLabelString = "";
        this.backRectOffsetX = -20.0f;
        this.backRectOffsetY = 394.0f;
        this.backRectWidth = 120.0f;
        this.backRectHeight = 36.0f;
        this.backRectColor0 = Integer.MIN_VALUE;
        this.backRectStrokeWidth1 = 2.0f;
        this.backRectColor1 = -1291845648;
        this.backRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.backRectColor2 = 0;
        this.backRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backRectColor3 = 0;
        this.backRectRadius = 18.0f;
        this.rateLabelFontSize = 28.0f;
        this.rateLabelColor0 = -6106;
        this.rateLabelStroke1Width = 3.0f;
        this.rateLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.rateLabelStroke2Width = 5.0f;
        this.rateLabelColor2 = -16;
        this.rateLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.rateLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.fixFarmButtonOffsetX = -6.0f;
        this.fixFarmButtonOffsetY = 480.0f;
        this.fixFarmButtonWidth = 40.0f;
        this.fixFarmButtonHeight = 40.0f;
        this.appDelegate = _appDelegate;
        this.farmUnit = _farmUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.refreshCount = (short) 0;
        this.hp = (short) 100;
        this.rate = (short) 0;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.fixFarmButtonStatus = (short) 0;
        this.rateLabelString = "";
        this.backRectOffsetX = (-20.0f) * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.backRectOffsetY = 398.0f * this.zoomRate;
        } else {
            this.backRectOffsetY = 486.0f * this.zoomRate;
        }
        this.backRectWidth = 90.0f * this.zoomRate;
        this.backRectHeight = 28.0f * this.zoomRate;
        this.backRectColor0 = Integer.MIN_VALUE;
        this.backRectStrokeWidth1 = 2.0f * this.zoomRate;
        this.backRectColor1 = -16;
        this.backRectStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backRectColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backRectColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backRectRadius = 14.0f * this.zoomRate;
        this.rateLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.rateLabelFontSize = 21.0f * this.zoomRate;
        this.rateLabelColor0 = -6106;
        this.rateLabelStroke1Width = 2.5f * this.zoomRate;
        this.rateLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.rateLabelStroke2Width = 5.0f * this.zoomRate;
        this.rateLabelColor2 = -16;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.rateLabelTypeface);
        newPaint.setTextSize(this.rateLabelFontSize);
        this.rateLabelOffsetX = ((this.backRectOffsetX + this.backRectWidth) - (5.0f * this.zoomRate)) - newPaint.measureText(this.rateLabelString);
        this.rateLabelOffsetY = (this.backRectOffsetY + this.backRectHeight) - (this.rateLabelFontSize * 0.3f);
        this.fixFarmButtonOffsetX = (-6.0f) * this.zoomRate;
        this.fixFarmButtonOffsetY = 480.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.fixFarmButtonOffsetY -= 88.0f * this.zoomRate;
        }
        this.fixFarmButtonWidth = this.zoomRate * 40.0f;
        this.fixFarmButtonHeight = this.zoomRate * 40.0f;
        clearBitmap();
        this.myDraw = new MyDraw();
    }

    public void clearBitmap() {
        this.farmFrontBitmapIndex = (short) -1;
        if (this.farmFrontBitmap0 != null) {
            if (!this.farmFrontBitmap0.isRecycled()) {
                this.farmFrontBitmap0.recycle();
            }
            this.farmFrontBitmap0 = null;
        }
        if (this.farmFrontBitmap1 != null) {
            if (!this.farmFrontBitmap1.isRecycled()) {
                this.farmFrontBitmap1.recycle();
            }
            this.farmFrontBitmap1 = null;
        }
        if (this.fixFarmButtonBitmap0 != null) {
            if (!this.fixFarmButtonBitmap0.isRecycled()) {
                this.fixFarmButtonBitmap0.recycle();
            }
            this.fixFarmButtonBitmap0 = null;
        }
        if (this.fixFarmButtonBitmap1 != null) {
            if (!this.fixFarmButtonBitmap1.isRecycled()) {
                this.fixFarmButtonBitmap1.recycle();
            }
            this.fixFarmButtonBitmap1 = null;
        }
    }

    public void refreshBitmap() {
        clearBitmap();
        if (this.appDelegate != null) {
            AssetManager asm = this.appDelegate.getAssets();
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inSampleSize = 1;
            opt2.inPreferredConfig = Bitmap.Config.RGB_565;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            try {
                InputStream inputStream = asm.open("png/Tool/Tool0/tool_0_1_0_front_0.png");
                this.farmFrontBitmap0 = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
            try {
                InputStream inputStream2 = asm.open("png/Tool/Tool0/tool_0_1_0_front_1.png");
                this.farmFrontBitmap1 = BitmapFactory.decodeStream(inputStream2, null, opt2);
                inputStream2.close();
            } catch (IOException e2) {
            }
            try {
                InputStream inputStream3 = asm.open("png/MainGame/farm_fix_0_0.png");
                this.fixFarmButtonBitmap0 = BitmapFactory.decodeStream(inputStream3, null, opt2);
                inputStream3.close();
            } catch (IOException e3) {
            }
            try {
                InputStream inputStream4 = asm.open("png/MainGame/farm_fix_0_1.png");
                this.fixFarmButtonBitmap1 = BitmapFactory.decodeStream(inputStream4, null, opt2);
                inputStream4.close();
            } catch (IOException e4) {
            }
        }
    }

    public boolean refresh() {
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList;
        boolean saveF = false;
        this.hp = (short) 100;
        this.fixFarmButtonStatus = (short) -1;
        this.farmFrontBitmapIndex = (short) -1;
        if (this.appDelegate.timeSaveDictionary == null) {
            return false;
        }
        ToolUnitDictionary toolUnitDictionary = null;
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 0 && (toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && toolUnitDictionarysArrayList.size() > 1) {
            toolUnitDictionary = toolUnitDictionarysArrayList.get(1);
        }
        if (toolUnitDictionary != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date nowDate = new Date();
            short levelShort = toolUnitDictionary.getLevel();
            short hpShort = toolUnitDictionary.getHp();
            short newHp = 0;
            if (hpShort > 0) {
                newHp = 100;
                String fixedDateString = null;
                if (levelShort >= 0) {
                    fixedDateString = toolUnitDictionary.getFixedDate();
                }
                if (fixedDateString != null && fixedDateString.length() > 0) {
                    Date fixedDate = null;
                    try {
                        fixedDate = sdf.parse(fixedDateString);
                    } catch (ParseException e) {
                    }
                    boolean correctF = false;
                    if (fixedDate != null) {
                        int i = 100;
                        while (true) {
                            if (i < 0) {
                                break;
                            }
                            if (!fixedDate.before(new Date(nowDate.getTime() - ((((this.appDelegate.FARM_PER_DIRTY_HOURS * 60) * 60) * 1000) * i)))) {
                                i--;
                            } else {
                                newHp = (short) (100 - i);
                                if (newHp < 0) {
                                    newHp = 0;
                                } else if (newHp > 100) {
                                    newHp = 100;
                                }
                                if (newHp != hpShort) {
                                    saveF = true;
                                }
                                correctF = true;
                            }
                        }
                    }
                    if (!correctF) {
                        toolUnitDictionary.setFixedDate(sdf.format(new Date(nowDate.getTime())));
                    }
                } else {
                    toolUnitDictionary.setFixedDate(sdf.format(new Date(nowDate.getTime())));
                }
            }
            if (newHp < 0 || newHp > 100) {
                newHp = 100;
            }
            Log.d("FarmUnitView", "newHp1111:" + ((int) newHp));
            if (newHp < 100) {
                this.hp = newHp;
                setNewRate(this.hp);
                if (this.hp < 60) {
                    this.farmFrontBitmapIndex = (short) 1;
                } else {
                    this.farmFrontBitmapIndex = (short) 0;
                }
                this.fixFarmButtonStatus = (short) 0;
            } else {
                this.hp = newHp;
                setNewRate(this.hp);
                this.farmFrontBitmapIndex = (short) 0;
                this.fixFarmButtonStatus = (short) -1;
            }
            toolUnitDictionary.setHp(newHp);
        } else {
            this.hp = (short) 100;
            setNewRate(this.hp);
            this.farmFrontBitmapIndex = (short) 0;
            this.fixFarmButtonStatus = (short) -1;
        }
        refreshFixButton();
        return saveF;
    }

    public void checkLoseCharacters() throws IOException {
        String fixedDateString;
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList;
        if (this.appDelegate.timeSaveDictionary != null) {
            ToolUnitDictionary toolUnitDictionary = null;
            if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 0 && (toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && toolUnitDictionarysArrayList.size() > 1) {
                toolUnitDictionary = toolUnitDictionarysArrayList.get(1);
            }
            Log.d("toolUnitDictionary", "psps");
            if (toolUnitDictionary != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date nowDate = new Date();
                short levelShort = toolUnitDictionary.getLevel();
                short nowHp = toolUnitDictionary.getHp();
                if (nowHp < 0) {
                    nowHp = 0;
                } else if (nowHp > 100) {
                    nowHp = 100;
                }
                Log.d("FarmUnitView", "nowHp:" + ((int) nowHp));
                int loseRate = 0;
                if (nowHp < 30) {
                    loseRate = (short) (60 - nowHp);
                } else if (nowHp < 60) {
                    loseRate = (short) (((60 - nowHp) / 2) + ((60 - nowHp) % 2));
                }
                if (loseRate > 0) {
                    if (levelShort >= 0 && ((fixedDateString = toolUnitDictionary.getFixedDate()) == null || fixedDateString.length() <= 0)) {
                        toolUnitDictionary.setFixedDate(sdf.format(new Date(nowDate.getTime())));
                        toolUnitDictionary.getFixedDate();
                    }
                    String checkedDateString = null;
                    if (levelShort >= 0 && ((checkedDateString = toolUnitDictionary.getCheckedDate()) == null || checkedDateString.length() <= 0)) {
                        toolUnitDictionary.setCheckedDate(sdf.format(new Date(nowDate.getTime())));
                        checkedDateString = toolUnitDictionary.getCheckedDate();
                    }
                    Date checkedDate = null;
                    try {
                        checkedDate = sdf.parse(checkedDateString);
                    } catch (ParseException e) {
                    }
                    if (checkedDate != null) {
                        Log.i("FarmUnitView", "checkedDate:" + checkedDate);
                        Log.i("FarmUnitView", "nowDate:" + nowDate);
                        if (checkedDate.before(new Date(nowDate.getTime() - (((this.appDelegate.FARM_PER_LOSE_HOURS * 60) * 60) * 1000)))) {
                            Log.i("FarmUnitView", "1 day:" + nowDate);
                            if (loseRate >= 60) {
                                Log.i("FarmUnitView", "2 day:" + nowDate);
                                if (checkedDate.before(new Date(nowDate.getTime() - ((((this.appDelegate.FARM_PER_LOSE_HOURS * 60) * 60) * 1000) * 2)))) {
                                    loseRate = 68;
                                    if (checkedDate.before(new Date(nowDate.getTime() - ((((this.appDelegate.FARM_PER_LOSE_HOURS * 60) * 60) * 1000) * 3)))) {
                                        loseRate = 80;
                                        if (checkedDate.before(new Date(nowDate.getTime() - ((((this.appDelegate.FARM_PER_LOSE_HOURS * 60) * 60) * 1000) * 4)))) {
                                            loseRate = 100;
                                        }
                                    }
                                }
                            }
                            toolUnitDictionary.setCheckedDate(sdf.format(new Date(nowDate.getTime())));
                            this.farmUnit.loseCharactersWithRate(loseRate);
                        }
                    }
                }
            }
        }
    }

    public boolean fixFarmWithCP(int _fixcp) {
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList;
        boolean saveF = false;
        if (this.appDelegate.timeSaveDictionary == null) {
            return false;
        }
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        float fixNeedCP = getFixNeedCP(this.hp);
        if (nowPoint < fixNeedCP || _fixcp != ((int) fixNeedCP)) {
            this.farmUnit.fixFarmWithCP(fixNeedCP);
            this.fixFarmButtonStatus = (short) 0;
            return false;
        }
        this.hp = (short) 100;
        this.fixFarmButtonStatus = (short) -1;
        this.farmFrontBitmapIndex = (short) -1;
        ToolUnitDictionary toolUnitDictionary = null;
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 0 && (toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && toolUnitDictionarysArrayList.size() > 1) {
            toolUnitDictionary = toolUnitDictionarysArrayList.get(1);
        }
        if (toolUnitDictionary != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date nowDate = new Date();
            toolUnitDictionary.getLevel();
            short hpShort = toolUnitDictionary.getHp();
            short newHp = 0;
            if (hpShort < 100) {
                float nowPoint2 = this.appDelegate.timeSaveDictionary.getPoint();
                if (nowPoint2 >= fixNeedCP) {
                    this.appDelegate.timeSaveDictionary.setPoint(nowPoint2 - fixNeedCP);
                    saveF = true;
                    newHp = 100;
                }
            }
            this.hp = newHp;
            if (this.hp < 0) {
                this.hp = (short) 0;
            } else if (this.hp > 100) {
                this.hp = (short) 100;
            }
            if (this.hp < 100) {
                setNewRate(this.hp);
                if (this.hp < 60) {
                    this.farmFrontBitmapIndex = (short) 1;
                } else {
                    this.farmFrontBitmapIndex = (short) 0;
                }
            } else {
                setNewRate(this.hp);
                this.farmFrontBitmapIndex = (short) 0;
            }
            toolUnitDictionary.setHp(this.hp);
            toolUnitDictionary.setFixedDate(sdf.format(new Date(nowDate.getTime())));
            toolUnitDictionary.setCheckedDate(sdf.format(new Date(nowDate.getTime())));
        } else {
            this.hp = (short) 100;
            setNewRate(this.hp);
            this.farmFrontBitmapIndex = (short) 0;
        }
        refreshFixButton();
        return saveF;
    }

    public void refreshFixButton() {
        if (this.hp < 100) {
            this.fixFarmButtonStatus = (short) 0;
        } else {
            this.fixFarmButtonStatus = (short) -1;
        }
    }

    public float getFixNeedCP(short _hp) {
        float fixNeedCP;
        if (_hp <= 0) {
            this.appDelegate.getClass();
            fixNeedCP = (100 - _hp) * 1.0f * 2.0f;
        } else if (_hp < 30) {
            this.appDelegate.getClass();
            fixNeedCP = (100 - _hp) * 1.0f * 1.5f;
        } else if (_hp < 60) {
            this.appDelegate.getClass();
            fixNeedCP = (100 - _hp) * 1.0f * 1.2f;
        } else {
            this.appDelegate.getClass();
            fixNeedCP = (100 - _hp) * 1.0f * 1.0f;
        }
        if (fixNeedCP < BitmapDescriptorFactory.HUE_RED) {
            return BitmapDescriptorFactory.HUE_RED;
        }
        this.appDelegate.getClass();
        if (fixNeedCP > 200.0f) {
            this.appDelegate.getClass();
            return 200.0f;
        }
        return fixNeedCP;
    }

    public void setNewRate(short _rate) {
        this.rate = _rate;
        if (this.rate < 0) {
            this.rate = (short) 0;
        } else if (this.rate > 100) {
            this.rate = (short) 100;
        }
        if (this.rate <= 0 || this.rate < 30) {
            this.rateLabelColor0 = -65536;
            this.rateLabelColor1 = -16;
            this.rateLabelColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        } else if (this.rate < 60) {
            this.rateLabelColor0 = -32768;
            this.rateLabelColor1 = -16;
            this.rateLabelColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        } else {
            this.rateLabelColor0 = -7576502;
            this.rateLabelColor1 = -16;
            this.rateLabelColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        }
        this.rateLabelString = String.valueOf((int) this.rate) + "%";
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.rateLabelTypeface);
        newPaint.setTextSize(this.rateLabelFontSize);
        this.rateLabelOffsetX = ((this.backRectOffsetX + this.backRectWidth) - (10.0f * this.zoomRate)) - newPaint.measureText(this.rateLabelString);
    }

    public void readyfixFarm() {
        float fixNeedCP = getFixNeedCP(this.hp);
        this.farmUnit.fixFarmWithCP(fixNeedCP);
    }

    public void doRefreshLoop() {
        refresh();
        this.refreshCount = (short) 30;
    }

    public void doLoop() {
        this.refreshCount = (short) (this.refreshCount - 1);
        if (this.refreshCount <= 0) {
            doRefreshLoop();
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("FarmUnitView", "onTouchEvent");
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (this.fixFarmButtonStatus == 0 && event.getY() > this.fixFarmButtonOffsetY && event.getY() < this.fixFarmButtonOffsetY + this.fixFarmButtonHeight && event.getX() > this.fixFarmButtonOffsetX && event.getX() < this.fixFarmButtonOffsetX + this.fixFarmButtonWidth) {
                this.touchButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 3;
                Log.d("FarmBackView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("FarmBackView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmFrontViewUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        }
        return false;
    }

    public void doClick() {
        if (this.touchButtonIndex == 0) {
            readyfixFarm();
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        MyDraw.drawOnlyStrokeRect(canvas, this.backRectOffsetX, this.backRectOffsetY, this.backRectWidth, this.backRectHeight, this.backRectColor0, this.backRectStrokeWidth1, this.backRectColor1, this.backRectStrokeWidth2, this.backRectColor2, this.backRectStrokeWidth3, this.backRectColor3, this.backRectRadius);
        MyDraw.drawStrokeText(canvas, this.rateLabelOffsetX, this.rateLabelOffsetY, this.rateLabelTypeface, this.rateLabelString, this.rateLabelFontSize, this.rateLabelColor0, this.rateLabelStroke1Width, this.rateLabelColor1, this.rateLabelStroke2Width, this.rateLabelColor2);
        if (this.fixFarmButtonStatus == 0) {
            if (this.touchButtonIndex == 0) {
                if (this.fixFarmButtonBitmap1 != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.fixFarmButtonBitmap1, new Rect(0, 0, this.fixFarmButtonBitmap1.getWidth(), this.fixFarmButtonBitmap1.getHeight()), new Rect((int) this.fixFarmButtonOffsetX, (int) this.fixFarmButtonOffsetY, (int) (this.fixFarmButtonOffsetX + this.fixFarmButtonWidth), (int) (this.fixFarmButtonOffsetY + this.fixFarmButtonHeight)), bitmapPaint);
                }
            } else if (this.fixFarmButtonBitmap0 != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.fixFarmButtonBitmap0, new Rect(0, 0, this.fixFarmButtonBitmap0.getWidth(), this.fixFarmButtonBitmap0.getHeight()), new Rect((int) this.fixFarmButtonOffsetX, (int) this.fixFarmButtonOffsetY, (int) (this.fixFarmButtonOffsetX + this.fixFarmButtonWidth), (int) (this.fixFarmButtonOffsetY + this.fixFarmButtonHeight)), bitmapPaint);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        clearBitmap();
        this.farmUnit = null;
        this.appDelegate = null;
    }
}

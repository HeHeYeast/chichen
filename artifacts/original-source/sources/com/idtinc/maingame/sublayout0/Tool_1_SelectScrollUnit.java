package com.idtinc.maingame.sublayout0;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.ckunit.ToolDataDictionary;
import com.idtinc.ckunit.ToolLevelDictionary;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.MyDraw;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Tool_1_SelectScrollUnit {
    private float SELECTED_Y;
    private float UNSELECTED_Y;
    private AppDelegate appDelegate;
    private float backViewHeight;
    private float backViewOffsetY;
    private float button0TitleLabelOffsetX;
    public String button0TitleLabelString;
    private float button1TitleLabelOffsetX;
    public String button1TitleLabelString;
    private float button2TitleLabelOffsetX;
    public String button2TitleLabelString;
    private float button3TitleLabelOffsetX;
    public String button3TitleLabelString;
    private float buttonHeight;
    private float buttonOffsetX;
    private float buttonRadius;
    private float buttonSpaceX;
    private float buttonStrokeWidth1;
    private float buttonStrokeWidth2;
    private float buttonStrokeWidth3;
    private float buttonTitleLabelFontSize;
    private float buttonTitleLabelOffsetY;
    private float buttonTitleLabelStroke1Width;
    private float buttonTitleLabelStroke2Width;
    Typeface buttonTitleLabelTypeface;
    private float buttonWidth;
    private float cpLabelFontSize;
    private float cpLabelOffsetX;
    private float cpLabelOffsetY;
    private float cpLabelStroke1Width;
    private float cpLabelStroke2Width;
    Typeface cpLabelTypeface;
    private float finalHeight;
    public float finalWidth;
    private short goButtonIndex;
    private float goButtonSize;
    private float goLeftButtonOffsetX;
    private float goLeftButtonOffsetY;
    private float goRightButtonOffsetX;
    private float goRightButtonOffsetY;
    private float levelLabelFontSize;
    private float levelLabelOffsetX;
    private float levelLabelOffsetY;
    private float levelLabelStroke1Width;
    private float levelLabelStroke2Width;
    Typeface levelLabelTypeface;
    private MainGameUnit mainGameUnit;
    private MyDraw myDraw;
    public float offsetScrollX;
    private float offsetScrollXMax;
    private float originWidth;
    private short selectedButtonIndex;
    private float smallImageHeight;
    private float smallImageOffsetX;
    private float smallImageOffsetY;
    private float smallImageWidth;
    private float starHeight;
    private float starOffsetX;
    private float starOffsetY;
    private float starWidth;
    private float timeLabelFontSize;
    private float timeLabelOffsetX;
    private float timeLabelOffsetY;
    private float timeLabelStroke1Width;
    private float timeLabelStroke2Width;
    Typeface timeLabelTypeface;
    public float touchMoveUnlockUnit;
    private float touchRangeYMax;
    private float touchRangeYMin;
    private float zoomRate;
    public final float BUTTONSVIEW_WIDTH = 64.0f;
    public final float BUTTONSVIEW_HEIGHT = 100.0f;
    private float preScrollX = -9999.0f;
    private float preScrollY = -9999.0f;
    private Bitmap goRightButtonBitmap = null;
    private Bitmap goLeftButtonBitmap = null;

    public Tool_1_SelectScrollUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameUnit _mainGameUnit, AppDelegate _appDelegate) {
        this.originWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchRangeYMin = BitmapDescriptorFactory.HUE_RED;
        this.touchRangeYMax = 58.0f;
        this.backViewOffsetY = 372.0f;
        this.backViewHeight = 58.0f;
        this.offsetScrollXMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollX = BitmapDescriptorFactory.HUE_RED;
        this.touchMoveUnlockUnit = 5.0f;
        this.selectedButtonIndex = (short) -1;
        this.buttonOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buttonSpaceX = BitmapDescriptorFactory.HUE_RED;
        this.buttonWidth = BitmapDescriptorFactory.HUE_RED;
        this.buttonHeight = BitmapDescriptorFactory.HUE_RED;
        this.buttonStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.buttonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.buttonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.buttonRadius = BitmapDescriptorFactory.HUE_RED;
        this.button0TitleLabelString = "";
        this.button1TitleLabelString = "";
        this.button2TitleLabelString = "";
        this.button3TitleLabelString = "";
        this.buttonTitleLabelFontSize = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.button0TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button1TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button2TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button3TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.SELECTED_Y = BitmapDescriptorFactory.HUE_RED;
        this.UNSELECTED_Y = 6.0f;
        this.smallImageOffsetX = 5.0f;
        this.smallImageOffsetY = 10.0f;
        this.smallImageWidth = 54.0f;
        this.smallImageHeight = 54.0f;
        this.starOffsetX = 5.0f;
        this.starOffsetY = 3.0f;
        this.starWidth = 25.0f;
        this.starHeight = 25.0f;
        this.levelLabelFontSize = 14.0f;
        this.levelLabelStroke1Width = 2.5f;
        this.levelLabelStroke2Width = 3.5f;
        this.levelLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.levelLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.cpLabelFontSize = 14.0f;
        this.cpLabelStroke1Width = 2.5f;
        this.cpLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.cpLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.cpLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.timeLabelFontSize = 14.0f;
        this.timeLabelStroke1Width = 2.5f;
        this.timeLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.timeLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.timeLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.goButtonIndex = (short) -1;
        this.goButtonSize = 18.0f;
        this.goRightButtonOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.goRightButtonOffsetY = 390.0f;
        this.goLeftButtonOffsetX = 302.0f;
        this.goLeftButtonOffsetY = 390.0f;
        this.appDelegate = _appDelegate;
        this.mainGameUnit = _mainGameUnit;
        this.originWidth = _finalwidth;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        if (!this.appDelegate.isRetina4) {
            this.touchRangeYMin = 348.0f * this.zoomRate;
        } else {
            this.touchRangeYMin = 436.0f * this.zoomRate;
        }
        this.touchRangeYMax = this.touchRangeYMin + (82.0f * this.zoomRate);
        this.backViewOffsetY = this.touchRangeYMin + (24.0f * this.zoomRate);
        this.backViewHeight = this.backViewOffsetY + (58.0f * this.zoomRate);
        this.offsetScrollXMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollX = BitmapDescriptorFactory.HUE_RED;
        this.touchMoveUnlockUnit = this.zoomRate * 5.0f;
        setPreScrollPoint(-9999.0f, -9999.0f);
        this.selectedButtonIndex = (short) -1;
        this.SELECTED_Y = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.UNSELECTED_Y = 6.0f * this.zoomRate;
        this.buttonOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.buttonSpaceX = 80.0f * this.zoomRate;
        this.buttonWidth = 64.0f * this.zoomRate;
        this.buttonHeight = 100.0f * this.zoomRate;
        this.buttonStrokeWidth1 = this.zoomRate * 2.0f;
        this.buttonStrokeWidth2 = this.zoomRate * 2.0f;
        this.buttonStrokeWidth3 = 1.0f * this.zoomRate;
        this.buttonRadius = 20.0f * this.zoomRate;
        this.button0TitleLabelString = this.appDelegate.getResources().getString(R.string.Kitchen);
        this.button1TitleLabelString = this.appDelegate.getResources().getString(R.string.Farm);
        this.button2TitleLabelString = this.appDelegate.getResources().getString(R.string.Store);
        this.button3TitleLabelString = this.appDelegate.getResources().getString(R.string.Other);
        this.buttonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.buttonTitleLabelFontSize = 68.0f * this.zoomRate;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.buttonTitleLabelTypeface);
        newPaint.setTextSize(this.buttonTitleLabelFontSize);
        this.buttonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.buttonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.button0TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * BitmapDescriptorFactory.HUE_RED) + ((this.buttonWidth - newPaint.measureText(this.button0TitleLabelString)) / 2.0f);
        this.button1TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * 1.0f) + ((this.buttonWidth - newPaint.measureText(this.button1TitleLabelString)) / 2.0f);
        this.button2TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * 2.0f) + ((this.buttonWidth - newPaint.measureText(this.button2TitleLabelString)) / 2.0f);
        this.button3TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * 3.0f) + ((this.buttonWidth - newPaint.measureText(this.button3TitleLabelString)) / 2.0f);
        this.buttonTitleLabelOffsetY = 44.0f * this.zoomRate;
        this.smallImageOffsetX = this.zoomRate * 5.0f;
        this.smallImageOffsetY = 10.0f * this.zoomRate;
        this.smallImageWidth = 54.0f * this.zoomRate;
        this.smallImageHeight = 54.0f * this.zoomRate;
        this.starOffsetX = this.zoomRate * 5.0f;
        this.starOffsetY = 3.0f * this.zoomRate;
        this.starWidth = 25.0f * this.zoomRate;
        this.starHeight = 25.0f * this.zoomRate;
        this.levelLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.levelLabelFontSize = this.zoomRate * 14.0f;
        this.levelLabelStroke1Width = this.zoomRate * 2.5f;
        this.levelLabelStroke2Width = 3.5f * this.zoomRate;
        newPaint.setTypeface(this.levelLabelTypeface);
        newPaint.setTextSize(this.levelLabelFontSize);
        this.levelLabelOffsetX = 17.5f * this.zoomRate;
        this.levelLabelOffsetY = 21.5f * this.zoomRate;
        this.cpLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.cpLabelFontSize = this.zoomRate * 14.0f;
        this.cpLabelStroke1Width = this.zoomRate * 2.5f;
        this.cpLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        newPaint.setTypeface(this.cpLabelTypeface);
        newPaint.setTextSize(this.cpLabelFontSize);
        this.cpLabelOffsetX = 43.0f * this.zoomRate;
        this.cpLabelOffsetY = 23.0f * this.zoomRate;
        this.timeLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.timeLabelFontSize = this.zoomRate * 14.0f;
        this.timeLabelStroke1Width = this.zoomRate * 2.5f;
        this.timeLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        newPaint.setTypeface(this.timeLabelTypeface);
        newPaint.setTextSize(this.timeLabelFontSize);
        this.timeLabelOffsetX = 32.0f * this.zoomRate;
        this.timeLabelOffsetY = 73.0f * this.zoomRate;
        this.goButtonIndex = (short) -1;
        this.goButtonSize = 18.0f * this.zoomRate;
        this.goRightButtonOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        if (!this.appDelegate.isRetina4) {
            this.goRightButtonOffsetY = 390.0f * this.zoomRate;
        } else {
            this.goRightButtonOffsetY = 478.0f * this.zoomRate;
        }
        this.goLeftButtonOffsetX = 302.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.goLeftButtonOffsetY = 390.0f * this.zoomRate;
        } else {
            this.goLeftButtonOffsetY = 478.0f * this.zoomRate;
        }
        clearBitmap();
        this.myDraw = new MyDraw();
    }

    public void clearBitmap() {
        if (this.goRightButtonBitmap != null) {
            if (!this.goRightButtonBitmap.isRecycled()) {
                this.goRightButtonBitmap.recycle();
            }
            this.goRightButtonBitmap = null;
        }
        if (this.goLeftButtonBitmap != null) {
            if (!this.goLeftButtonBitmap.isRecycled()) {
                this.goLeftButtonBitmap.recycle();
            }
            this.goLeftButtonBitmap = null;
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
                InputStream inputStream = asm.open("png/Button/go_right.png");
                this.goRightButtonBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                if (this.goRightButtonBitmap != null) {
                    Matrix mx = new Matrix();
                    mx.setScale(-1.0f, 1.0f);
                    this.goLeftButtonBitmap = Bitmap.createBitmap(this.goRightButtonBitmap, 0, 0, this.goRightButtonBitmap.getWidth(), this.goRightButtonBitmap.getHeight(), mx, true);
                }
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public void refresh() {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        if (this.appDelegate.timeSaveDictionary != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null) {
            Log.d("Tool_1_SelectScrollLayout", "appMainActivity.timeSaveDictionary.toolUnitDictionarysArrayList.size():" + this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size());
            if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 2 && (toolDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(1)) != null) {
                short displayButtonCnt = 0;
                for (int i = 0; i < toolDictionarysArrayList.size(); i++) {
                    ToolUnitDictionary toolUnitDictionary = toolDictionarysArrayList.get(i);
                    if (toolUnitDictionary != null) {
                        short levelShort = this.appDelegate.getTool1LevelWithIndex((short) i);
                        if (levelShort < 0) {
                            break;
                        } else {
                            displayButtonCnt = (short) (i + 1);
                        }
                    }
                }
                this.finalWidth = this.originWidth;
                this.offsetScrollXMax = BitmapDescriptorFactory.HUE_RED;
                if (displayButtonCnt > 5) {
                    this.offsetScrollXMax = this.buttonWidth * (displayButtonCnt - 5);
                    this.finalWidth = ((int) this.originWidth) + ((int) this.offsetScrollXMax);
                }
                if (this.offsetScrollX < BitmapDescriptorFactory.HUE_RED) {
                    this.offsetScrollX = BitmapDescriptorFactory.HUE_RED;
                } else if (this.offsetScrollX > this.offsetScrollXMax) {
                    this.offsetScrollX = this.offsetScrollXMax;
                }
                refreshGoImageButton();
            }
        }
    }

    public void doClickSelectedButton() {
        ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 1, this.selectedButtonIndex);
        short copySelectedButtonIndex = this.selectedButtonIndex;
        unclickAllButton();
        if (toolUnitDictionary != null) {
            short levelShort = toolUnitDictionary.getLevel();
            if (levelShort >= 0) {
                selectButtonWithIndex(copySelectedButtonIndex);
            }
        }
    }

    public void selectButtonWithIndex(short _buttonIndex) {
        unclickAllButton();
        Log.d("doClickSelectedButton", "_buttonIndex=" + ((int) _buttonIndex));
        if (_buttonIndex == -1) {
            changeButtonWithIndex((short) -1, (short) -1, (short) -1, (short) -1);
        } else {
            Log.d("doClickSelectedButton", "_buttonIndex=" + ((int) _buttonIndex));
            this.mainGameUnit.tool_1_ButtonChangeWithIndex(_buttonIndex);
        }
    }

    public void changeButtonWithIndex(short _buttonIndex, short _tool20Index, short _tool21Index, short _tool22Index) {
        unclickAllButton();
        if (this.appDelegate.timeSaveDictionary != null) {
            this.appDelegate.timeSaveDictionary.setTool1SelectViewNowButtonIndex(_buttonIndex);
            if (_tool20Index > -2) {
                this.appDelegate.timeSaveDictionary.setTool1SelectViewTool2_0Index(_tool20Index);
            }
            if (_tool21Index > -2) {
                this.appDelegate.timeSaveDictionary.setTool1SelectViewTool2_1Index(_tool21Index);
            }
            if (_tool22Index > -2) {
                this.appDelegate.timeSaveDictionary.setTool1SelectViewTool2_2Index(_tool22Index);
            }
        }
        if (this.appDelegate.getShort_tool_1_selectview_nowbuttonindex() < 0 && this.appDelegate.timeSaveDictionary != null) {
            this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate("");
            this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(-1.0f);
        }
    }

    public void unclickAllButton() {
        this.selectedButtonIndex = (short) -1;
    }

    public void refreshGoImageButton() {
        if (this.finalWidth <= this.originWidth) {
            this.goButtonIndex = (short) -1;
            return;
        }
        this.goButtonIndex = (short) -1;
        if (this.offsetScrollX > this.zoomRate * 32.0f) {
            this.goButtonIndex = (short) 0;
        } else if (this.offsetScrollX < this.offsetScrollXMax - (this.zoomRate * 32.0f)) {
            this.goButtonIndex = (short) 1;
        }
    }

    public void setPreScrollPoint(float _preScrollX, float _preScrollY) {
        this.preScrollX = _preScrollX;
        this.preScrollY = _preScrollY;
    }

    public float getPreScrollX() {
        return this.preScrollX;
    }

    public float getPreScrollY() {
        return this.preScrollY;
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("StoreListScrollLayout01", "onTouchEvent");
        if (event.getAction() == 1) {
            doClickSelectedButton();
            setPreScrollPoint(event.getX(), event.getY());
            Log.i("scrollView.setOnTouchListener", "Up setPreScrollX=" + getPreScrollX());
            Log.i("scrollView.setOnTouchListener", "Up setPreScrollY=" + getPreScrollY());
        } else if (event.getAction() == 0) {
            setPreScrollPoint(event.getX(), event.getY());
            Log.i("scrollView.setOnTouchListener", "Up setPreScrollX=" + getPreScrollX());
            Log.i("scrollView.setOnTouchListener", "Up setPreScrollY=" + getPreScrollY());
            this.selectedButtonIndex = (short) -1;
            if (event.getX() > BitmapDescriptorFactory.HUE_RED && event.getX() < this.finalWidth && event.getY() > this.touchRangeYMin && event.getY() < this.touchRangeYMax) {
                short buttonIndex = (short) ((this.offsetScrollX + event.getX()) / this.buttonWidth);
                ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 1, buttonIndex);
                if (toolUnitDictionary == null) {
                    return true;
                }
                short levelShort = toolUnitDictionary.getLevel();
                if (levelShort < 0) {
                    return true;
                }
                this.selectedButtonIndex = buttonIndex;
                return true;
            }
        } else if (event.getAction() == 2) {
            if (getPreScrollY() != event.getY()) {
                Math.abs(getPreScrollY() - event.getY());
            }
            if (this.selectedButtonIndex >= 0) {
                if (this.selectedButtonIndex != ((short) ((this.offsetScrollX + event.getX()) / this.buttonWidth))) {
                    unclickAllButton();
                }
            }
            if (this.selectedButtonIndex >= 0) {
                float subScrollX = BitmapDescriptorFactory.HUE_RED;
                if (getPreScrollX() != event.getX()) {
                    subScrollX = Math.abs(getPreScrollX() - event.getX());
                }
                if (subScrollX > this.touchMoveUnlockUnit) {
                    unclickAllButton();
                }
            }
            float addOffScrollX = this.preScrollX - event.getX();
            this.offsetScrollX += addOffScrollX;
            if (this.offsetScrollX < BitmapDescriptorFactory.HUE_RED) {
                this.offsetScrollX = BitmapDescriptorFactory.HUE_RED;
            } else if (this.offsetScrollX > this.offsetScrollXMax) {
                this.offsetScrollX = this.offsetScrollXMax;
            }
            this.preScrollX = event.getX();
            Log.d("StoreListScrollLayout01", "ACTION_MOVE   X=" + event.getX() + ", Y=  " + event.getY());
            Log.d("StoreListScrollLayout01", "preScrollX =  " + this.preScrollX);
            Log.d("StoreListScrollLayout01", "offsetScrollX =  " + this.offsetScrollX);
            refreshGoImageButton();
            return true;
        }
        return false;
    }

    public void doLoop() {
    }

    public void gameDraw(Canvas canvas) {
        short levelShort;
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(637534208);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, this.backViewOffsetY, this.finalWidth, this.backViewHeight, bitmapPaint);
        float drawOffsetScrollX = this.offsetScrollX;
        if (this.appDelegate != null) {
            int toolDictionarysArrayListCount = this.appDelegate.getToolDictionarysArrayCountWithTypeId((short) 1);
            int indexMin = (int) (drawOffsetScrollX / this.buttonWidth);
            if (indexMin < 0) {
                indexMin = 0;
            } else if (indexMin > toolDictionarysArrayListCount - 6) {
                indexMin = toolDictionarysArrayListCount - 6;
            }
            int indexMax = indexMin + 6;
            if (indexMax > toolDictionarysArrayListCount) {
            }
            int tool_1_selectview_nowbuttonindex = this.appDelegate.getShort_tool_1_selectview_nowbuttonindex();
            for (int i = 0; i < toolDictionarysArrayListCount; i++) {
                ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 1, (short) i);
                if (toolUnitDictionary != null && (levelShort = toolUnitDictionary.getLevel()) >= 0 && levelShort <= 2) {
                    ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId((short) 1, (short) i);
                    float nowButtonOffsetX = (this.buttonOffsetX + (this.buttonWidth * i)) - drawOffsetScrollX;
                    float buttonOffsetY = this.touchRangeYMin + this.UNSELECTED_Y;
                    int buttonColor0 = -16;
                    int buttonColor1 = -3109815;
                    int buttonColor2 = -2248316;
                    int buttonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
                    float nowStarOffsetX = nowButtonOffsetX + this.starOffsetX;
                    Paint newPaint = new Paint(257);
                    newPaint.setTypeface(this.levelLabelTypeface);
                    newPaint.setTextSize(this.levelLabelFontSize);
                    String levelLabelString = new StringBuilder().append(levelShort + 1).toString();
                    float nowLevelLabelOffsetX = (this.levelLabelOffsetX + nowButtonOffsetX) - (newPaint.measureText(levelLabelString) / 2.0f);
                    float nowLevelLabelOffsetY = buttonOffsetY + this.levelLabelOffsetY;
                    newPaint.setTypeface(this.cpLabelTypeface);
                    newPaint.setTextSize(this.cpLabelFontSize);
                    String cpLabelString = "";
                    int cookCp = -1;
                    if (toolDataDictionary != null && toolDataDictionary.toolLevelsArrayList != null && levelShort < toolDataDictionary.toolLevelsArrayList.size()) {
                        ToolLevelDictionary newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(levelShort);
                        cookCp = newToolLevelDictionary.getLvCookCp();
                    }
                    if (cookCp >= 0) {
                        cpLabelString = String.valueOf(cookCp) + "cp";
                    }
                    float nowCpLabelOffsetX = (this.cpLabelOffsetX + nowButtonOffsetX) - (newPaint.measureText(cpLabelString) / 2.0f);
                    float nowCpLabelOffsetY = buttonOffsetY + this.cpLabelOffsetY;
                    newPaint.setTypeface(this.timeLabelTypeface);
                    newPaint.setTextSize(this.timeLabelFontSize);
                    String timeLabelString = "";
                    short cookMin = -1;
                    if (toolDataDictionary != null && toolDataDictionary.toolLevelsArrayList != null && levelShort < toolDataDictionary.toolLevelsArrayList.size()) {
                        ToolLevelDictionary newToolLevelDictionary2 = toolDataDictionary.toolLevelsArrayList.get(levelShort);
                        cookMin = newToolLevelDictionary2.getLvMin();
                    }
                    if (cookMin >= 60) {
                        if (cookMin % 60 == 0) {
                            timeLabelString = String.valueOf(cookMin / 60) + "hr";
                        } else {
                            timeLabelString = String.valueOf(cookMin / 60) + "hr " + (cookMin % 60) + "m";
                        }
                    } else if (cookMin > 0) {
                        timeLabelString = String.valueOf((int) cookMin) + "m";
                    }
                    float nowTimeLabelOffsetX = (this.timeLabelOffsetX + nowButtonOffsetX) - (newPaint.measureText(timeLabelString) / 2.0f);
                    float nowTimeLabelOffsetY = buttonOffsetY + this.timeLabelOffsetY;
                    float nowSmallImageOffsetX = nowButtonOffsetX + this.smallImageOffsetX;
                    if (i == tool_1_selectview_nowbuttonindex) {
                        buttonOffsetY = this.touchRangeYMin + this.SELECTED_Y;
                        buttonColor0 = -200082;
                        buttonColor1 = -227838;
                        buttonColor2 = -12988;
                        buttonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
                        nowLevelLabelOffsetY = buttonOffsetY + this.levelLabelOffsetY;
                        nowCpLabelOffsetY = buttonOffsetY + this.cpLabelOffsetY;
                        nowTimeLabelOffsetY = buttonOffsetY + this.timeLabelOffsetY;
                    }
                    MyDraw.drawStrokeRect(canvas, nowButtonOffsetX, buttonOffsetY, this.buttonWidth, this.buttonHeight, buttonColor0, this.buttonStrokeWidth1, buttonColor1, this.buttonStrokeWidth2, buttonColor2, this.buttonStrokeWidth3, buttonColor3, this.buttonRadius);
                    if (i != tool_1_selectview_nowbuttonindex) {
                        Bitmap toolBitmap = null;
                        if (levelShort == 2) {
                            if (this.appDelegate.tool1Level2Image0ArrayList != null && this.appDelegate.tool1Level2Image0ArrayList.size() > i) {
                                toolBitmap = this.appDelegate.tool1Level2Image0ArrayList.get(i);
                            }
                        } else if (levelShort == 1) {
                            if (this.appDelegate.tool1Level1Image0ArrayList != null && this.appDelegate.tool1Level1Image0ArrayList.size() > i) {
                                toolBitmap = this.appDelegate.tool1Level1Image0ArrayList.get(i);
                            }
                        } else if (this.appDelegate.tool1Level0Image0ArrayList != null && this.appDelegate.tool1Level0Image0ArrayList.size() > i) {
                            toolBitmap = this.appDelegate.tool1Level0Image0ArrayList.get(i);
                        }
                        if (toolBitmap != null) {
                            bitmapPaint.setAlpha(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                            canvas.drawBitmap(toolBitmap, new Rect(0, 0, toolBitmap.getWidth(), toolBitmap.getHeight()), new Rect((int) nowSmallImageOffsetX, (int) (this.smallImageOffsetY + buttonOffsetY), (int) (this.smallImageWidth + nowSmallImageOffsetX), (int) (this.smallImageOffsetY + buttonOffsetY + this.smallImageHeight)), bitmapPaint);
                        }
                        if (this.appDelegate.starOffBitmap != null) {
                            bitmapPaint.setAlpha(128);
                            canvas.drawBitmap(this.appDelegate.starOffBitmap, new Rect(0, 0, this.appDelegate.starOffBitmap.getWidth(), this.appDelegate.starOffBitmap.getHeight()), new Rect((int) nowStarOffsetX, (int) (this.starOffsetY + buttonOffsetY), (int) (this.starWidth + nowStarOffsetX), (int) (this.starOffsetY + buttonOffsetY + this.starHeight)), bitmapPaint);
                        }
                        if (levelLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, nowLevelLabelOffsetX, nowLevelLabelOffsetY, this.levelLabelTypeface, levelLabelString, this.levelLabelFontSize, -570425345, this.levelLabelStroke1Width, Integer.MIN_VALUE, this.levelLabelStroke2Width, -2130706433);
                        }
                        if (cpLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, nowCpLabelOffsetX, nowCpLabelOffsetY, this.cpLabelTypeface, cpLabelString, this.cpLabelFontSize, -1710619, this.cpLabelStroke1Width, -11776948, this.cpLabelStroke2Width, 0);
                        }
                        if (timeLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, nowTimeLabelOffsetX, nowTimeLabelOffsetY, this.timeLabelTypeface, timeLabelString, this.timeLabelFontSize, -1710619, this.timeLabelStroke1Width, -11776948, this.timeLabelStroke2Width, 0);
                        }
                    } else {
                        Bitmap toolBitmap2 = null;
                        if (levelShort == 2) {
                            if (this.appDelegate.tool1Level2Image1ArrayList != null && this.appDelegate.tool1Level2Image1ArrayList.size() > i) {
                                toolBitmap2 = this.appDelegate.tool1Level2Image1ArrayList.get(i);
                            }
                        } else if (levelShort == 1) {
                            if (this.appDelegate.tool1Level1Image1ArrayList != null && this.appDelegate.tool1Level1Image1ArrayList.size() > i) {
                                toolBitmap2 = this.appDelegate.tool1Level1Image1ArrayList.get(i);
                            }
                        } else if (this.appDelegate.tool1Level0Image1ArrayList != null && this.appDelegate.tool1Level0Image1ArrayList.size() > i) {
                            toolBitmap2 = this.appDelegate.tool1Level0Image1ArrayList.get(i);
                        }
                        if (toolBitmap2 != null) {
                            bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                            canvas.drawBitmap(toolBitmap2, new Rect(0, 0, toolBitmap2.getWidth(), toolBitmap2.getHeight()), new Rect((int) nowSmallImageOffsetX, (int) (this.smallImageOffsetY + buttonOffsetY), (int) (this.smallImageWidth + nowSmallImageOffsetX), (int) (this.smallImageOffsetY + buttonOffsetY + this.smallImageHeight)), bitmapPaint);
                        }
                        if (this.appDelegate.starOnBitmap != null) {
                            bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                            canvas.drawBitmap(this.appDelegate.starOnBitmap, new Rect(0, 0, this.appDelegate.starOnBitmap.getWidth(), this.appDelegate.starOnBitmap.getHeight()), new Rect((int) nowStarOffsetX, (int) (this.starOffsetY + buttonOffsetY), (int) (this.starWidth + nowStarOffsetX), (int) (this.starOffsetY + buttonOffsetY + this.starHeight)), bitmapPaint);
                        }
                        if (levelLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, nowLevelLabelOffsetX, nowLevelLabelOffsetY, this.levelLabelTypeface, levelLabelString, this.levelLabelFontSize, -1, this.levelLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.levelLabelStroke2Width, -1);
                        }
                        if (cpLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, nowCpLabelOffsetX, nowCpLabelOffsetY, this.cpLabelTypeface, cpLabelString, this.cpLabelFontSize, -1, this.cpLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.cpLabelStroke2Width, 0);
                        }
                        if (timeLabelString.length() > 0) {
                            MyDraw.drawStrokeText(canvas, nowTimeLabelOffsetX, nowTimeLabelOffsetY, this.timeLabelTypeface, timeLabelString, this.timeLabelFontSize, -1, this.timeLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.timeLabelStroke2Width, 0);
                        }
                    }
                    if (i == this.selectedButtonIndex) {
                        MyDraw.drawStrokeRect(canvas, nowButtonOffsetX, buttonOffsetY, this.buttonWidth, this.buttonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buttonRadius);
                    }
                }
            }
            if (this.goButtonIndex == 0) {
                bitmapPaint.setAlpha(178);
                canvas.drawBitmap(this.goRightButtonBitmap, new Rect(0, 0, this.goRightButtonBitmap.getWidth(), this.goRightButtonBitmap.getHeight()), new Rect((int) this.goRightButtonOffsetX, (int) this.goRightButtonOffsetY, (int) (this.goRightButtonOffsetX + this.goButtonSize), (int) (this.goRightButtonOffsetY + this.goButtonSize)), bitmapPaint);
            } else if (this.goButtonIndex == 1) {
                bitmapPaint.setAlpha(178);
                canvas.drawBitmap(this.goLeftButtonBitmap, new Rect(0, 0, this.goLeftButtonBitmap.getWidth(), this.goLeftButtonBitmap.getHeight()), new Rect((int) this.goLeftButtonOffsetX, (int) this.goLeftButtonOffsetY, (int) (this.goLeftButtonOffsetX + this.goButtonSize), (int) (this.goLeftButtonOffsetY + this.goButtonSize)), bitmapPaint);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        clearBitmap();
        this.mainGameUnit = null;
        this.appDelegate = null;
    }
}

package com.idtinc.maingame.sublayout3;

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
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import java.io.IOException;
import java.io.InputStream;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HelpScrollViewUnit {
    private float BGMLABEL_HEIGHT;
    private float BGMLABEL_OFFSET_X;
    private float BGMLABEL_OFFSET_Y;
    private float BGMLABEL_WIDTH;
    private float FACEBOOKLABEL_HEIGHT;
    private float FACEBOOKLABEL_OFFSET_X;
    private float FACEBOOKLABEL_OFFSET_Y;
    private float FACEBOOKLABEL_WIDTH;
    private float IDTLABEL_HEIGHT;
    private float IDTLABEL_OFFSET_X;
    private float IDTLABEL_OFFSET_Y;
    private float IDTLABEL_WIDTH;
    private float SOUNDLABEL_HEIGHT;
    private float SOUNDLABEL_OFFSET_X;
    private float SOUNDLABEL_OFFSET_Y;
    private float SOUNDLABEL_WIDTH;
    private float TITLELABEL00_HEIGHT;
    private float TITLELABEL00_OFFSET_X;
    private float TITLELABEL00_OFFSET_Y;
    private float TITLELABEL00_WIDTH;
    private float TITLELABEL01_HEIGHT;
    private float TITLELABEL01_OFFSET_X;
    private float TITLELABEL01_OFFSET_Y;
    private float TITLELABEL01_WIDTH;
    private float TITLELABEL02_HEIGHT;
    private float TITLELABEL02_OFFSET_X;
    private float TITLELABEL02_OFFSET_Y;
    private float TITLELABEL02_WIDTH;
    private float TITLELABEL03_HEIGHT;
    private float TITLELABEL03_OFFSET_X;
    private float TITLELABEL03_OFFSET_Y;
    private float TITLELABEL03_WIDTH;
    private float TITLELABEL04_HEIGHT;
    private float TITLELABEL04_OFFSET_X;
    private float TITLELABEL04_OFFSET_Y;
    private float TITLELABEL04_WIDTH;
    private float TITLELABEL05_HEIGHT;
    private float TITLELABEL05_OFFSET_X;
    private float TITLELABEL05_OFFSET_Y;
    private float TITLELABEL05_WIDTH;
    private float TITLELABEL06_HEIGHT;
    private float TITLELABEL06_OFFSET_X;
    private float TITLELABEL06_OFFSET_Y;
    private float TITLELABEL06_WIDTH;
    private AppDelegate appDelegate;
    private int backToMainMenuButtonColor0;
    private int backToMainMenuButtonColor1;
    private int backToMainMenuButtonColor2;
    private int backToMainMenuButtonColor3;
    private float backToMainMenuButtonHeight;
    private float backToMainMenuButtonOffsetX;
    private float backToMainMenuButtonOffsetY;
    private float backToMainMenuButtonRadius;
    private short backToMainMenuButtonStatus;
    private float backToMainMenuButtonStrokeWidth1;
    private float backToMainMenuButtonStrokeWidth2;
    private float backToMainMenuButtonStrokeWidth3;
    private int backToMainMenuButtonTitleLabelColor0;
    private int backToMainMenuButtonTitleLabelColor1;
    private int backToMainMenuButtonTitleLabelColor2;
    private float backToMainMenuButtonTitleLabelFontSize;
    private float backToMainMenuButtonTitleLabelOffsetX;
    private float backToMainMenuButtonTitleLabelOffsetY;
    public String backToMainMenuButtonTitleLabelString;
    private float backToMainMenuButtonTitleLabelStroke1Width;
    private float backToMainMenuButtonTitleLabelStroke2Width;
    Typeface backToMainMenuButtonTitleLabelTypeface;
    private float backToMainMenuButtonWidth;
    public int bgmLabelColor0;
    public int bgmLabelColor1;
    public int bgmLabelColor2;
    float bgmLabelFontSize;
    float bgmLabelOffsetX;
    float bgmLabelOffsetY;
    String bgmLabelString;
    public float bgmLabelStroke1Width;
    public float bgmLabelStroke2Width;
    Typeface bgmLabelTypeface;
    private int bgmSwitchButtonColor0;
    private int bgmSwitchButtonColor1;
    private int bgmSwitchButtonColor2;
    private int bgmSwitchButtonColor3;
    private float bgmSwitchButtonHeight;
    private float bgmSwitchButtonOffsetX;
    private float bgmSwitchButtonOffsetY;
    private float bgmSwitchButtonRadius;
    private short bgmSwitchButtonStatus;
    private float bgmSwitchButtonStrokeWidth1;
    private float bgmSwitchButtonStrokeWidth2;
    private float bgmSwitchButtonStrokeWidth3;
    private int bgmSwitchButtonTitleLabelColor0;
    private int bgmSwitchButtonTitleLabelColor1;
    private int bgmSwitchButtonTitleLabelColor2;
    private float bgmSwitchButtonTitleLabelFontSize;
    private float bgmSwitchButtonTitleLabelOffsetX;
    private float bgmSwitchButtonTitleLabelOffsetY;
    public String bgmSwitchButtonTitleLabelString;
    private float bgmSwitchButtonTitleLabelStroke1Width;
    private float bgmSwitchButtonTitleLabelStroke2Width;
    Typeface bgmSwitchButtonTitleLabelTypeface;
    private float bgmSwitchButtonWidth;
    private short buttonClickCnt;
    public int buttonShadowColor;
    public float buttonShadowOffsetX;
    public float buttonShadowOffsetY;
    public float buttonShadowOpacity;
    private float clipRectHeight;
    private float clipRectWidth;
    private short contactUsButtonStatus;
    Typeface contactUsButtonTitleLabelTypeface;
    private int copyrightLabelColor0;
    private int copyrightLabelColor1;
    private int copyrightLabelColor2;
    private float copyrightLabelFontSize;
    private float copyrightLabelOffsetX;
    private float copyrightLabelOffsetY;
    int copyrightLabelShadowColor;
    float copyrightLabelShadowOffsetX;
    float copyrightLabelShadowOffsetY;
    float copyrightLabelShadowOpacity;
    public String copyrightLabelString;
    private float copyrightLabelStroke1Width;
    private float copyrightLabelStroke2Width;
    Typeface copyrightLabelTypeface;
    public float facebookButtonHeight;
    public float facebookButtonOffsetX;
    public float facebookButtonOffsetY;
    public float facebookButtonRadius;
    private short facebookButtonStatus;
    public float facebookButtonWidth;
    public int facebookLabelColor0;
    public int facebookLabelColor1;
    public int facebookLabelColor2;
    float facebookLabelFontSize;
    float facebookLabelOffsetX;
    float facebookLabelOffsetY;
    String facebookLabelString;
    public float facebookLabelStroke1Width;
    public float facebookLabelStroke2Width;
    Typeface facebookLabelTypeface;
    private int facebookLogoutButtonColor0;
    private int facebookLogoutButtonColor1;
    private int facebookLogoutButtonColor2;
    private int facebookLogoutButtonColor3;
    private float facebookLogoutButtonHeight;
    private float facebookLogoutButtonOffsetX;
    private float facebookLogoutButtonOffsetY;
    private float facebookLogoutButtonRadius;
    public short facebookLogoutButtonStatus;
    private float facebookLogoutButtonStrokeWidth1;
    private float facebookLogoutButtonStrokeWidth2;
    private float facebookLogoutButtonStrokeWidth3;
    private int facebookLogoutButtonTitleLabelColor0;
    private int facebookLogoutButtonTitleLabelColor1;
    private int facebookLogoutButtonTitleLabelColor2;
    private float facebookLogoutButtonTitleLabelFontSize;
    private float facebookLogoutButtonTitleLabelOffsetX;
    private float facebookLogoutButtonTitleLabelOffsetY;
    public String facebookLogoutButtonTitleLabelString;
    private float facebookLogoutButtonTitleLabelStroke1Width;
    private float facebookLogoutButtonTitleLabelStroke2Width;
    Typeface facebookLogoutButtonTitleLabelTypeface;
    private float facebookLogoutButtonWidth;
    private float finalHeight;
    private float finalWidth;
    private HelpUnit helpUnit;
    public int iDTLabelColor0;
    public int iDTLabelColor1;
    public int iDTLabelColor2;
    float iDTLabelFontSize;
    float iDTLabelOffsetX;
    float iDTLabelOffsetY;
    String iDTLabelString;
    public float iDTLabelStroke1Width;
    public float iDTLabelStroke2Width;
    Typeface iDTLabelTypeface;
    private int iDTLogoutButtonColor0;
    private int iDTLogoutButtonColor1;
    private int iDTLogoutButtonColor2;
    private int iDTLogoutButtonColor3;
    private float iDTLogoutButtonHeight;
    private float iDTLogoutButtonOffsetX;
    private float iDTLogoutButtonOffsetY;
    private float iDTLogoutButtonRadius;
    public short iDTLogoutButtonStatus;
    private float iDTLogoutButtonStrokeWidth1;
    private float iDTLogoutButtonStrokeWidth2;
    private float iDTLogoutButtonStrokeWidth3;
    private int iDTLogoutButtonTitleLabelColor0;
    private int iDTLogoutButtonTitleLabelColor1;
    private int iDTLogoutButtonTitleLabelColor2;
    private float iDTLogoutButtonTitleLabelFontSize;
    private float iDTLogoutButtonTitleLabelOffsetX;
    private float iDTLogoutButtonTitleLabelOffsetY;
    public String iDTLogoutButtonTitleLabelString;
    private float iDTLogoutButtonTitleLabelStroke1Width;
    private float iDTLogoutButtonTitleLabelStroke2Width;
    Typeface iDTLogoutButtonTitleLabelTypeface;
    private float iDTLogoutButtonWidth;
    public float lineButtonHeight;
    public float lineButtonOffsetX;
    public float lineButtonOffsetY;
    public float lineButtonRadius;
    private short lineButtonStatus;
    public float lineButtonWidth;
    public float mailButtonHeight;
    public float mailButtonOffsetX;
    public float mailButtonOffsetY;
    public float mailButtonRadius;
    private short mailButtonStatus;
    public float mailButtonWidth;
    private float manualButton00OffsetX;
    private float manualButton00OffsetY;
    private short manualButton00Status;
    private float manualButton00TitleLabelOffsetX;
    private float manualButton00TitleLabelOffsetY;
    public String manualButton00TitleLabelString;
    private float manualButton01OffsetX;
    private float manualButton01OffsetY;
    private short manualButton01Status;
    private float manualButton01TitleLabelOffsetX;
    private float manualButton01TitleLabelOffsetY;
    public String manualButton01TitleLabelString;
    private float manualButton02OffsetX;
    private float manualButton02OffsetY;
    private short manualButton02Status;
    private float manualButton02TitleLabelOffsetX;
    private float manualButton02TitleLabelOffsetY;
    public String manualButton02TitleLabelString;
    private int manualButtonColor0;
    private int manualButtonColor1;
    private int manualButtonColor2;
    private int manualButtonColor3;
    private float manualButtonHeight;
    private float manualButtonRadius;
    private float manualButtonStrokeWidth1;
    private float manualButtonStrokeWidth2;
    private float manualButtonStrokeWidth3;
    private int manualButtonTitleLabelColor0;
    private int manualButtonTitleLabelColor1;
    private int manualButtonTitleLabelColor2;
    private float manualButtonTitleLabelFontSize;
    private float manualButtonTitleLabelStroke1Width;
    private float manualButtonTitleLabelStroke2Width;
    Typeface manualButtonTitleLabelTypeface;
    private float manualButtonWidth;
    private MyDraw myDraw;
    public float offsetScrollY;
    private float offsetScrollYMax;
    private float offsetX;
    private float offsetY;
    private float originHeight;
    public float scrollBarHeight;
    public float scrollBarOffsetX;
    public float scrollBarOffsetY;
    public float scrollBarRadius;
    public float scrollBarWidth;
    private float scrollCancelLimit;
    private short selectedButtonIndex;
    public float slosButtonHeight;
    public float slosButtonOffsetX;
    public float slosButtonOffsetY;
    public float slosButtonRadius;
    private short slosButtonStatus;
    public float slosButtonWidth;
    public int soundLabelColor0;
    public int soundLabelColor1;
    public int soundLabelColor2;
    float soundLabelFontSize;
    float soundLabelOffsetX;
    float soundLabelOffsetY;
    String soundLabelString;
    public float soundLabelStroke1Width;
    public float soundLabelStroke2Width;
    Typeface soundLabelTypeface;
    private int soundSwitchButtonColor0;
    private int soundSwitchButtonColor1;
    private int soundSwitchButtonColor2;
    private int soundSwitchButtonColor3;
    private float soundSwitchButtonHeight;
    private float soundSwitchButtonOffsetX;
    private float soundSwitchButtonOffsetY;
    private float soundSwitchButtonRadius;
    private short soundSwitchButtonStatus;
    private float soundSwitchButtonStrokeWidth1;
    private float soundSwitchButtonStrokeWidth2;
    private float soundSwitchButtonStrokeWidth3;
    private int soundSwitchButtonTitleLabelColor0;
    private int soundSwitchButtonTitleLabelColor1;
    private int soundSwitchButtonTitleLabelColor2;
    private float soundSwitchButtonTitleLabelFontSize;
    private float soundSwitchButtonTitleLabelOffsetX;
    private float soundSwitchButtonTitleLabelOffsetY;
    public String soundSwitchButtonTitleLabelString;
    private float soundSwitchButtonTitleLabelStroke1Width;
    private float soundSwitchButtonTitleLabelStroke2Width;
    Typeface soundSwitchButtonTitleLabelTypeface;
    private float soundSwitchButtonWidth;
    float titleLabel00OffsetX;
    float titleLabel00OffsetY;
    String titleLabel00String;
    float titleLabel01OffsetX;
    float titleLabel01OffsetY;
    String titleLabel01String;
    float titleLabel02OffsetX;
    float titleLabel02OffsetY;
    String titleLabel02String;
    float titleLabel03OffsetX;
    float titleLabel03OffsetY;
    String titleLabel03String;
    float titleLabel04OffsetX;
    float titleLabel04OffsetY;
    String titleLabel04String;
    float titleLabel05OffsetX;
    float titleLabel05OffsetY;
    String titleLabel05String;
    float titleLabel06OffsetX;
    float titleLabel06OffsetY;
    String titleLabel06String;
    public int titleLabelColor0;
    public int titleLabelColor1;
    public int titleLabelColor2;
    float titleLabelFontSize;
    public float titleLabelStroke1Width;
    public float titleLabelStroke2Width;
    Typeface titleLabelTypeface;
    private int uploadButtonColor0;
    private int uploadButtonColor1;
    private int uploadButtonColor2;
    private int uploadButtonColor3;
    private float uploadButtonHeight;
    private float uploadButtonOffsetX;
    private float uploadButtonOffsetY;
    private float uploadButtonRadius;
    private short uploadButtonStatus;
    private float uploadButtonStrokeWidth1;
    private float uploadButtonStrokeWidth2;
    private float uploadButtonStrokeWidth3;
    private int uploadButtonTitleLabelColor0;
    private int uploadButtonTitleLabelColor1;
    private int uploadButtonTitleLabelColor2;
    private float uploadButtonTitleLabelFontSize;
    private float uploadButtonTitleLabelOffsetX;
    private float uploadButtonTitleLabelOffsetY;
    public String uploadButtonTitleLabelString;
    private float uploadButtonTitleLabelStroke1Width;
    private float uploadButtonTitleLabelStroke2Width;
    Typeface uploadButtonTitleLabelTypeface;
    private float uploadButtonWidth;
    private float zoomRate;
    private float preScrollX = -9999.0f;
    private float preScrollY = -9999.0f;
    private float contactUsButtonWidth = 200.0f;
    private float contactUsButtonHeight = 45.0f;
    private float contactUsButtonOffsetX = 35.0f;
    private float contactUsButtonOffsetY = BitmapDescriptorFactory.HUE_RED;
    private int contactUsButtonColor0 = -3171731;
    private float contactUsButtonStrokeWidth1 = 2.0f;
    private int contactUsButtonColor1 = -7576502;
    private float contactUsButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
    private int contactUsButtonColor2 = 0;
    private float contactUsButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
    private int contactUsButtonColor3 = 0;
    private float contactUsButtonRadius = 10.0f;
    public String contactUsButtonTitleLabelString = "";
    private float contactUsButtonTitleLabelFontSize = 24.0f;
    private int contactUsButtonTitleLabelColor0 = -16;
    private float contactUsButtonTitleLabelStroke1Width = 2.0f;
    private int contactUsButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
    private float contactUsButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
    private int contactUsButtonTitleLabelColor2 = 0;
    private float contactUsButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
    private float contactUsButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    private Bitmap slosButtonBitmap = null;
    private Bitmap facebookButtonBitmap = null;
    private Bitmap lineButtonBitmap = null;
    private Bitmap mailButtonBitmap = null;

    public HelpScrollViewUnit(float _offsetX, float _offsetY, float _finalwidth, float _originheight, float _finalheight, float _zoomrate, HelpUnit _helpUnit, AppDelegate _appDelegate) throws IOException {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.originHeight = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.clipRectWidth = BitmapDescriptorFactory.HUE_RED;
        this.clipRectHeight = BitmapDescriptorFactory.HUE_RED;
        this.scrollCancelLimit = 5.0f;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarWidth = 6.0f;
        this.scrollBarHeight = 60.0f;
        this.scrollBarRadius = 2.0f;
        this.titleLabelTypeface = null;
        this.titleLabelFontSize = 24.0f;
        this.titleLabelColor0 = -872415232;
        this.titleLabelStroke1Width = 2.0f;
        this.titleLabelColor1 = -1;
        this.titleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        this.TITLELABEL00_OFFSET_X = 4.0f;
        this.TITLELABEL00_OFFSET_Y = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL00_WIDTH = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL00_HEIGHT = 30.0f;
        this.titleLabel00String = "";
        this.titleLabel00OffsetX = this.TITLELABEL00_OFFSET_X;
        this.titleLabel00OffsetY = this.TITLELABEL00_OFFSET_Y;
        this.TITLELABEL01_OFFSET_X = 4.0f;
        this.TITLELABEL01_OFFSET_Y = 205.0f;
        this.TITLELABEL01_WIDTH = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL01_HEIGHT = 30.0f;
        this.titleLabel01String = "";
        this.titleLabel01OffsetX = this.TITLELABEL01_OFFSET_X;
        this.titleLabel01OffsetY = this.TITLELABEL01_OFFSET_Y;
        this.TITLELABEL02_OFFSET_X = 4.0f;
        this.TITLELABEL02_OFFSET_Y = this.TITLELABEL01_OFFSET_Y + 100.0f;
        this.TITLELABEL02_WIDTH = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL02_HEIGHT = 30.0f;
        this.titleLabel02String = "";
        this.titleLabel02OffsetX = this.TITLELABEL02_OFFSET_X;
        this.titleLabel02OffsetY = this.TITLELABEL02_OFFSET_Y;
        this.TITLELABEL03_OFFSET_X = 4.0f;
        this.TITLELABEL03_OFFSET_Y = this.TITLELABEL02_OFFSET_Y + 110.0f;
        this.TITLELABEL03_WIDTH = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL03_HEIGHT = 30.0f;
        this.titleLabel03String = "";
        this.titleLabel03OffsetX = this.TITLELABEL03_OFFSET_X;
        this.titleLabel03OffsetY = this.TITLELABEL03_OFFSET_Y;
        this.TITLELABEL04_OFFSET_X = 4.0f;
        this.TITLELABEL04_OFFSET_Y = this.TITLELABEL03_OFFSET_Y + 105.0f;
        this.TITLELABEL04_WIDTH = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL04_HEIGHT = 30.0f;
        this.titleLabel04String = "";
        this.titleLabel04OffsetX = this.TITLELABEL04_OFFSET_X;
        this.titleLabel04OffsetY = this.TITLELABEL04_OFFSET_Y;
        this.TITLELABEL05_OFFSET_X = 4.0f;
        this.TITLELABEL05_OFFSET_Y = this.TITLELABEL04_OFFSET_Y + 105.0f;
        this.TITLELABEL05_WIDTH = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL05_HEIGHT = 30.0f;
        this.titleLabel05String = "";
        this.titleLabel05OffsetX = this.TITLELABEL05_OFFSET_X;
        this.titleLabel05OffsetY = this.TITLELABEL05_OFFSET_Y;
        this.TITLELABEL06_OFFSET_X = 4.0f;
        this.TITLELABEL06_OFFSET_Y = this.TITLELABEL05_OFFSET_Y + 105.0f;
        this.TITLELABEL06_WIDTH = BitmapDescriptorFactory.HUE_RED;
        this.TITLELABEL06_HEIGHT = 30.0f;
        this.titleLabel06String = "";
        this.titleLabel06OffsetX = this.TITLELABEL06_OFFSET_X;
        this.titleLabel06OffsetY = this.TITLELABEL06_OFFSET_Y;
        this.bgmLabelTypeface = null;
        this.bgmLabelFontSize = 24.0f;
        this.bgmLabelColor0 = -1;
        this.bgmLabelStroke1Width = 3.0f;
        this.bgmLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.bgmLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.bgmLabelColor2 = 0;
        this.BGMLABEL_OFFSET_X = 40.0f;
        this.BGMLABEL_OFFSET_Y = this.TITLELABEL01_OFFSET_Y + 25.0f + 15.0f;
        this.BGMLABEL_WIDTH = 200.0f;
        this.BGMLABEL_HEIGHT = 45.0f;
        this.bgmLabelString = "";
        this.bgmLabelOffsetX = this.BGMLABEL_OFFSET_X;
        this.bgmLabelOffsetY = this.BGMLABEL_OFFSET_Y;
        this.soundLabelTypeface = null;
        this.soundLabelFontSize = 24.0f;
        this.soundLabelColor0 = -1;
        this.soundLabelStroke1Width = 3.0f;
        this.soundLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.soundLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.soundLabelColor2 = 0;
        this.SOUNDLABEL_OFFSET_X = 40.0f;
        this.SOUNDLABEL_OFFSET_Y = this.TITLELABEL01_OFFSET_Y + 25.0f + 15.0f;
        this.SOUNDLABEL_WIDTH = 200.0f;
        this.SOUNDLABEL_HEIGHT = 45.0f;
        this.soundLabelString = "";
        this.soundLabelOffsetX = this.SOUNDLABEL_OFFSET_X;
        this.soundLabelOffsetY = this.SOUNDLABEL_OFFSET_Y;
        this.buttonShadowOpacity = 2.0f;
        this.buttonShadowOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buttonShadowOffsetY = 1.0f;
        this.buttonShadowColor = -8947849;
        this.manualButtonWidth = 200.0f;
        this.manualButtonHeight = 45.0f;
        this.manualButton00OffsetX = 35.0f;
        this.manualButton00OffsetY = 40.0f;
        this.manualButton01OffsetX = 35.0f;
        this.manualButton01OffsetY = 95.0f;
        this.manualButton02OffsetX = 35.0f;
        this.manualButton02OffsetY = 150.0f;
        this.manualButtonColor0 = -3171731;
        this.manualButtonStrokeWidth1 = 2.0f;
        this.manualButtonColor1 = -7576502;
        this.manualButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.manualButtonColor2 = 0;
        this.manualButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.manualButtonColor3 = 0;
        this.manualButtonRadius = 10.0f;
        this.manualButton00TitleLabelString = "";
        this.manualButton01TitleLabelString = "";
        this.manualButton02TitleLabelString = "";
        this.manualButtonTitleLabelFontSize = 24.0f;
        this.manualButtonTitleLabelColor0 = -16;
        this.manualButtonTitleLabelStroke1Width = 2.0f;
        this.manualButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.manualButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.manualButtonTitleLabelColor2 = 0;
        this.manualButton00TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.manualButton00TitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.manualButton01TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.manualButton01TitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.manualButton02TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.manualButton02TitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.bgmSwitchButtonWidth = 200.0f;
        this.bgmSwitchButtonHeight = 45.0f;
        this.bgmSwitchButtonOffsetX = 35.0f;
        this.bgmSwitchButtonOffsetY = 40.0f;
        this.bgmSwitchButtonColor0 = -3171731;
        this.bgmSwitchButtonStrokeWidth1 = 2.0f;
        this.bgmSwitchButtonColor1 = -7576502;
        this.bgmSwitchButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.bgmSwitchButtonColor2 = 0;
        this.bgmSwitchButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.bgmSwitchButtonColor3 = 0;
        this.bgmSwitchButtonRadius = 10.0f;
        this.bgmSwitchButtonTitleLabelString = "";
        this.bgmSwitchButtonTitleLabelFontSize = 24.0f;
        this.bgmSwitchButtonTitleLabelColor0 = -16;
        this.bgmSwitchButtonTitleLabelStroke1Width = 3.0f;
        this.bgmSwitchButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.bgmSwitchButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.bgmSwitchButtonTitleLabelColor2 = 0;
        this.bgmSwitchButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.bgmSwitchButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.soundSwitchButtonWidth = 200.0f;
        this.soundSwitchButtonHeight = 45.0f;
        this.soundSwitchButtonOffsetX = 35.0f;
        this.soundSwitchButtonOffsetY = 40.0f;
        this.soundSwitchButtonColor0 = -3171731;
        this.soundSwitchButtonStrokeWidth1 = 2.0f;
        this.soundSwitchButtonColor1 = -7576502;
        this.soundSwitchButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.soundSwitchButtonColor2 = 0;
        this.soundSwitchButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.soundSwitchButtonColor3 = 0;
        this.soundSwitchButtonRadius = 10.0f;
        this.soundSwitchButtonTitleLabelString = "";
        this.soundSwitchButtonTitleLabelFontSize = 24.0f;
        this.soundSwitchButtonTitleLabelColor0 = -16;
        this.soundSwitchButtonTitleLabelStroke1Width = 3.0f;
        this.soundSwitchButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.soundSwitchButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.soundSwitchButtonTitleLabelColor2 = 0;
        this.soundSwitchButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.soundSwitchButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.slosButtonWidth = 26.0f;
        this.slosButtonHeight = 26.0f;
        this.slosButtonOffsetX = 105.0f;
        this.slosButtonOffsetY = 150.0f;
        this.slosButtonRadius = 10.0f;
        this.uploadButtonWidth = 200.0f;
        this.uploadButtonHeight = 45.0f;
        this.uploadButtonOffsetX = 35.0f;
        this.uploadButtonOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.uploadButtonColor0 = -3171731;
        this.uploadButtonStrokeWidth1 = 2.0f;
        this.uploadButtonColor1 = -7576502;
        this.uploadButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.uploadButtonColor2 = 0;
        this.uploadButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.uploadButtonColor3 = 0;
        this.uploadButtonRadius = 10.0f;
        this.uploadButtonTitleLabelString = "";
        this.uploadButtonTitleLabelFontSize = 24.0f;
        this.uploadButtonTitleLabelColor0 = -16;
        this.uploadButtonTitleLabelStroke1Width = 2.0f;
        this.uploadButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.uploadButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.uploadButtonTitleLabelColor2 = 0;
        this.uploadButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.uploadButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.facebookButtonWidth = 50.0f;
        this.facebookButtonHeight = 50.0f;
        this.facebookButtonOffsetX = 105.0f;
        this.facebookButtonOffsetY = 150.0f;
        this.facebookButtonRadius = 7.0f;
        this.lineButtonWidth = 50.0f;
        this.lineButtonHeight = 50.0f;
        this.lineButtonOffsetX = 105.0f;
        this.lineButtonOffsetY = 150.0f;
        this.lineButtonRadius = 7.0f;
        this.mailButtonWidth = 50.0f;
        this.mailButtonHeight = 50.0f;
        this.mailButtonOffsetX = 105.0f;
        this.mailButtonOffsetY = 150.0f;
        this.mailButtonRadius = 7.0f;
        this.iDTLabelTypeface = null;
        this.iDTLabelFontSize = 24.0f;
        this.iDTLabelColor0 = -1;
        this.iDTLabelStroke1Width = 3.0f;
        this.iDTLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.iDTLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.iDTLabelColor2 = 0;
        this.IDTLABEL_OFFSET_X = 40.0f;
        this.IDTLABEL_OFFSET_Y = this.TITLELABEL03_OFFSET_Y + 25.0f + 15.0f;
        this.IDTLABEL_WIDTH = 200.0f;
        this.IDTLABEL_HEIGHT = 45.0f;
        this.iDTLabelString = "";
        this.iDTLabelOffsetX = this.IDTLABEL_OFFSET_X;
        this.iDTLabelOffsetY = this.IDTLABEL_OFFSET_Y;
        this.iDTLogoutButtonWidth = 95.0f;
        this.iDTLogoutButtonHeight = 35.0f;
        this.iDTLogoutButtonOffsetX = 145.0f;
        this.iDTLogoutButtonOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.iDTLogoutButtonColor0 = -3171731;
        this.iDTLogoutButtonStrokeWidth1 = 2.0f;
        this.iDTLogoutButtonColor1 = -7576502;
        this.iDTLogoutButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.iDTLogoutButtonColor2 = 0;
        this.iDTLogoutButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.iDTLogoutButtonColor3 = 0;
        this.iDTLogoutButtonRadius = 10.0f;
        this.iDTLogoutButtonTitleLabelString = "";
        this.iDTLogoutButtonTitleLabelFontSize = 24.0f;
        this.iDTLogoutButtonTitleLabelColor0 = -16;
        this.iDTLogoutButtonTitleLabelStroke1Width = 2.0f;
        this.iDTLogoutButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.iDTLogoutButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.iDTLogoutButtonTitleLabelColor2 = 0;
        this.iDTLogoutButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.iDTLogoutButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.facebookLabelTypeface = null;
        this.facebookLabelFontSize = 24.0f;
        this.facebookLabelColor0 = -1;
        this.facebookLabelStroke1Width = 3.0f;
        this.facebookLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.facebookLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.facebookLabelColor2 = 0;
        this.FACEBOOKLABEL_OFFSET_X = 40.0f;
        this.FACEBOOKLABEL_OFFSET_Y = this.TITLELABEL03_OFFSET_Y + 25.0f + 15.0f;
        this.FACEBOOKLABEL_WIDTH = 200.0f;
        this.FACEBOOKLABEL_HEIGHT = 45.0f;
        this.facebookLabelString = "";
        this.facebookLabelOffsetX = this.FACEBOOKLABEL_OFFSET_X;
        this.facebookLabelOffsetY = this.FACEBOOKLABEL_OFFSET_Y;
        this.facebookLogoutButtonWidth = 95.0f;
        this.facebookLogoutButtonHeight = 35.0f;
        this.facebookLogoutButtonOffsetX = 145.0f;
        this.facebookLogoutButtonOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.facebookLogoutButtonColor0 = -3171731;
        this.facebookLogoutButtonStrokeWidth1 = 2.0f;
        this.facebookLogoutButtonColor1 = -7576502;
        this.facebookLogoutButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.facebookLogoutButtonColor2 = 0;
        this.facebookLogoutButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.facebookLogoutButtonColor3 = 0;
        this.facebookLogoutButtonRadius = 10.0f;
        this.facebookLogoutButtonTitleLabelString = "";
        this.facebookLogoutButtonTitleLabelFontSize = 24.0f;
        this.facebookLogoutButtonTitleLabelColor0 = -16;
        this.facebookLogoutButtonTitleLabelStroke1Width = 2.0f;
        this.facebookLogoutButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.facebookLogoutButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.facebookLogoutButtonTitleLabelColor2 = 0;
        this.facebookLogoutButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.facebookLogoutButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backToMainMenuButtonWidth = 200.0f;
        this.backToMainMenuButtonHeight = 45.0f;
        this.backToMainMenuButtonOffsetX = 35.0f;
        this.backToMainMenuButtonOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backToMainMenuButtonColor0 = -3171731;
        this.backToMainMenuButtonStrokeWidth1 = 2.0f;
        this.backToMainMenuButtonColor1 = -7576502;
        this.backToMainMenuButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.backToMainMenuButtonColor2 = 0;
        this.backToMainMenuButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backToMainMenuButtonColor3 = 0;
        this.backToMainMenuButtonRadius = 10.0f;
        this.backToMainMenuButtonTitleLabelString = "";
        this.backToMainMenuButtonTitleLabelFontSize = 24.0f;
        this.backToMainMenuButtonTitleLabelColor0 = -16;
        this.backToMainMenuButtonTitleLabelStroke1Width = 2.0f;
        this.backToMainMenuButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backToMainMenuButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.backToMainMenuButtonTitleLabelColor2 = 0;
        this.backToMainMenuButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backToMainMenuButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelString = "";
        this.copyrightLabelFontSize = 12.0f;
        this.copyrightLabelColor0 = 0;
        this.copyrightLabelStroke1Width = 2.0f;
        this.copyrightLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.copyrightLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelColor2 = 0;
        this.copyrightLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelShadowColor = -8947849;
        this.copyrightLabelShadowOpacity = 1.0f;
        this.copyrightLabelShadowOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelShadowOffsetY = 1.0f;
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.manualButton00Status = (short) 0;
        this.manualButton01Status = (short) 0;
        this.manualButton02Status = (short) 0;
        this.bgmSwitchButtonStatus = (short) 0;
        this.soundSwitchButtonStatus = (short) 0;
        this.slosButtonStatus = (short) 0;
        this.uploadButtonStatus = (short) 0;
        this.facebookButtonStatus = (short) 0;
        this.lineButtonStatus = (short) 0;
        this.mailButtonStatus = (short) 0;
        this.iDTLogoutButtonStatus = (short) -1;
        this.facebookLogoutButtonStatus = (short) -1;
        this.backToMainMenuButtonStatus = (short) 0;
        this.contactUsButtonStatus = (short) 0;
        this.appDelegate = null;
        this.helpUnit = null;
        this.appDelegate = _appDelegate;
        this.helpUnit = _helpUnit;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.originHeight = _originheight;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.clipRectWidth = this.offsetX + this.finalWidth;
        this.clipRectHeight = this.offsetY + this.originHeight;
        this.scrollCancelLimit = 10.0f * this.zoomRate;
        this.offsetScrollYMax = this.finalHeight - this.originHeight;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        setPreScrollPoint(-9999.0f, -9999.0f);
        this.scrollBarWidth = 6.0f * this.zoomRate;
        this.scrollBarHeight = this.originHeight * (this.originHeight / this.finalHeight);
        this.scrollBarOffsetX = ((this.offsetX + this.finalWidth) - this.scrollBarWidth) - (4.0f * this.zoomRate);
        this.scrollBarOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarRadius = this.scrollBarWidth / 2.0f;
        this.titleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.titleLabelFontSize = 24.0f * this.zoomRate;
        this.titleLabelColor0 = -872415232;
        this.titleLabelStroke1Width = 2.0f * this.zoomRate;
        this.titleLabelColor1 = -1;
        this.titleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.titleLabelColor2 = 0;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.titleLabelTypeface);
        newPaint.setTextSize(this.titleLabelFontSize);
        this.TITLELABEL00_OFFSET_X = this.offsetX + (4.0f * this.zoomRate);
        this.TITLELABEL00_OFFSET_Y = this.offsetY + (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        this.TITLELABEL00_WIDTH = this.finalWidth - (8.0f * this.zoomRate);
        this.TITLELABEL00_HEIGHT = 30.0f * this.zoomRate;
        this.titleLabel00String = this.appDelegate.getResources().getString(R.string.Manual);
        this.titleLabel00OffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.titleLabel00String)) / 2.0f);
        this.titleLabel00OffsetY = this.TITLELABEL00_OFFSET_Y + this.titleLabelFontSize;
        this.TITLELABEL01_OFFSET_X = this.offsetX + (4.0f * this.zoomRate);
        this.TITLELABEL01_OFFSET_Y = this.TITLELABEL00_OFFSET_Y + (205.0f * this.zoomRate);
        this.TITLELABEL01_WIDTH = this.finalWidth - (8.0f * this.zoomRate);
        this.TITLELABEL01_HEIGHT = 30.0f * this.zoomRate;
        this.titleLabel01String = this.appDelegate.getResources().getString(R.string.Sound);
        this.titleLabel01OffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.titleLabel01String)) / 2.0f);
        this.titleLabel01OffsetY = this.TITLELABEL01_OFFSET_Y + this.titleLabelFontSize;
        this.TITLELABEL02_OFFSET_X = this.offsetX + (4.0f * this.zoomRate);
        this.TITLELABEL02_OFFSET_Y = this.TITLELABEL01_OFFSET_Y + (125.0f * this.zoomRate);
        this.TITLELABEL02_WIDTH = this.finalWidth - (8.0f * this.zoomRate);
        this.TITLELABEL02_HEIGHT = 30.0f * this.zoomRate;
        this.titleLabel02String = this.appDelegate.getResources().getString(R.string.iDTOnlineSynchronous);
        this.titleLabel02OffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.titleLabel02String)) / 2.0f);
        this.titleLabel02OffsetY = this.TITLELABEL02_OFFSET_Y + this.titleLabelFontSize;
        this.TITLELABEL03_OFFSET_X = this.offsetX + (4.0f * this.zoomRate);
        this.TITLELABEL03_OFFSET_Y = this.TITLELABEL02_OFFSET_Y + (95.0f * this.zoomRate);
        this.TITLELABEL03_WIDTH = this.finalWidth - (8.0f * this.zoomRate);
        this.TITLELABEL03_HEIGHT = 30.0f * this.zoomRate;
        this.titleLabel03String = this.appDelegate.getResources().getString(R.string.Share);
        this.titleLabel03OffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.titleLabel03String)) / 2.0f);
        this.titleLabel03OffsetY = this.TITLELABEL03_OFFSET_Y + this.titleLabelFontSize;
        this.TITLELABEL04_OFFSET_X = this.offsetX + (4.0f * this.zoomRate);
        this.TITLELABEL04_OFFSET_Y = this.TITLELABEL03_OFFSET_Y + (100.0f * this.zoomRate);
        this.TITLELABEL04_WIDTH = this.finalWidth - (8.0f * this.zoomRate);
        this.TITLELABEL04_HEIGHT = 30.0f * this.zoomRate;
        this.titleLabel04String = this.appDelegate.getResources().getString(R.string.Account);
        this.titleLabel04OffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.titleLabel04String)) / 2.0f);
        this.titleLabel04OffsetY = this.TITLELABEL04_OFFSET_Y + this.titleLabelFontSize;
        this.TITLELABEL05_OFFSET_X = this.offsetX + (4.0f * this.zoomRate);
        this.TITLELABEL05_OFFSET_Y = this.TITLELABEL04_OFFSET_Y + (145.0f * this.zoomRate);
        this.TITLELABEL05_WIDTH = this.finalWidth - (8.0f * this.zoomRate);
        this.TITLELABEL05_HEIGHT = 30.0f * this.zoomRate;
        this.titleLabel05String = this.appDelegate.getResources().getString(R.string.BacktoMainMenu);
        this.titleLabel05OffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.titleLabel05String)) / 2.0f);
        this.titleLabel05OffsetY = this.TITLELABEL05_OFFSET_Y + this.titleLabelFontSize;
        this.TITLELABEL06_OFFSET_X = this.offsetX + (4.0f * this.zoomRate);
        this.TITLELABEL06_OFFSET_Y = this.TITLELABEL05_OFFSET_Y + (125.0f * this.zoomRate);
        this.TITLELABEL06_WIDTH = this.finalWidth - (8.0f * this.zoomRate);
        this.TITLELABEL06_HEIGHT = 30.0f * this.zoomRate;
        this.titleLabel06String = this.appDelegate.getResources().getString(R.string.ContactUs);
        this.titleLabel06OffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.titleLabel06String)) / 2.0f);
        this.titleLabel06OffsetY = this.TITLELABEL06_OFFSET_Y + this.titleLabelFontSize;
        this.bgmLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.bgmLabelFontSize = 24.0f * this.zoomRate;
        this.bgmLabelColor0 = -1;
        this.bgmLabelStroke1Width = 3.0f * this.zoomRate;
        this.bgmLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.bgmLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.bgmLabelColor2 = 0;
        newPaint.setTypeface(this.bgmLabelTypeface);
        newPaint.setTextSize(this.bgmLabelFontSize);
        this.BGMLABEL_OFFSET_X = this.offsetX + (40.0f * this.zoomRate);
        this.BGMLABEL_OFFSET_Y = this.TITLELABEL01_OFFSET_Y + (40.0f * this.zoomRate);
        this.BGMLABEL_WIDTH = 200.0f * this.zoomRate;
        this.BGMLABEL_HEIGHT = 35.0f * this.zoomRate;
        this.bgmLabelString = this.appDelegate.getResources().getString(R.string.BGM);
        this.bgmLabelOffsetX = this.BGMLABEL_OFFSET_X;
        this.bgmLabelOffsetY = this.BGMLABEL_OFFSET_Y + this.bgmLabelFontSize;
        this.soundLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.soundLabelFontSize = 24.0f * this.zoomRate;
        this.soundLabelColor0 = -1;
        this.soundLabelStroke1Width = 3.0f * this.zoomRate;
        this.soundLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.soundLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.soundLabelColor2 = 0;
        newPaint.setTypeface(this.soundLabelTypeface);
        newPaint.setTextSize(this.soundLabelFontSize);
        this.SOUNDLABEL_OFFSET_X = this.offsetX + (40.0f * this.zoomRate);
        this.SOUNDLABEL_OFFSET_Y = this.TITLELABEL01_OFFSET_Y + (85.0f * this.zoomRate);
        this.SOUNDLABEL_WIDTH = 200.0f * this.zoomRate;
        this.SOUNDLABEL_HEIGHT = 45.0f * this.zoomRate;
        this.soundLabelString = this.appDelegate.getResources().getString(R.string.SE);
        this.soundLabelOffsetX = this.SOUNDLABEL_OFFSET_X;
        this.soundLabelOffsetY = this.SOUNDLABEL_OFFSET_Y + this.soundLabelFontSize;
        this.buttonShadowOpacity = 3.0f * this.zoomRate;
        this.buttonShadowOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buttonShadowOffsetY = 1.0f * this.zoomRate;
        this.buttonShadowColor = -1728053248;
        this.manualButtonWidth = 200.0f * this.zoomRate;
        this.manualButtonHeight = 45.0f * this.zoomRate;
        this.manualButton00OffsetX = this.offsetX + (35.0f * this.zoomRate);
        this.manualButton00OffsetY = this.offsetY + (40.0f * this.zoomRate);
        this.manualButton01OffsetX = this.offsetX + (35.0f * this.zoomRate);
        this.manualButton01OffsetY = this.offsetY + (95.0f * this.zoomRate);
        this.manualButton02OffsetX = this.offsetX + (35.0f * this.zoomRate);
        this.manualButton02OffsetY = this.offsetY + (150.0f * this.zoomRate);
        this.manualButtonColor0 = -3889;
        this.manualButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.manualButtonColor1 = -3109815;
        this.manualButtonStrokeWidth2 = 1.0f * this.zoomRate;
        this.manualButtonColor2 = -16;
        this.manualButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.manualButtonColor3 = 0;
        this.manualButtonRadius = 10.0f * this.zoomRate;
        this.manualButton00TitleLabelString = this.appDelegate.getResources().getString(R.string.KitchenManual);
        this.manualButton01TitleLabelString = this.appDelegate.getResources().getString(R.string.FarmManual);
        this.manualButton02TitleLabelString = this.appDelegate.getResources().getString(R.string.StoreManual);
        this.manualButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.manualButtonTitleLabelFontSize = 24.0f * this.zoomRate;
        this.manualButtonTitleLabelColor0 = -16;
        this.manualButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.manualButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.manualButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.manualButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.manualButtonTitleLabelTypeface);
        newPaint.setTextSize(this.manualButtonTitleLabelFontSize);
        this.manualButton00TitleLabelOffsetX = this.manualButton00OffsetX + ((this.manualButtonWidth - newPaint.measureText(this.manualButton00TitleLabelString)) / 2.0f);
        this.manualButton00TitleLabelOffsetY = (this.manualButton00OffsetY + this.manualButtonHeight) - (this.manualButtonTitleLabelFontSize * 0.6f);
        this.manualButton01TitleLabelOffsetX = this.manualButton01OffsetX + ((this.manualButtonWidth - newPaint.measureText(this.manualButton01TitleLabelString)) / 2.0f);
        this.manualButton01TitleLabelOffsetY = (this.manualButton01OffsetY + this.manualButtonHeight) - (this.manualButtonTitleLabelFontSize * 0.6f);
        this.manualButton02TitleLabelOffsetX = this.manualButton02OffsetX + ((this.manualButtonWidth - newPaint.measureText(this.manualButton02TitleLabelString)) / 2.0f);
        this.manualButton02TitleLabelOffsetY = (this.manualButton02OffsetY + this.manualButtonHeight) - (this.manualButtonTitleLabelFontSize * 0.6f);
        this.bgmSwitchButtonWidth = 90.0f * this.zoomRate;
        this.bgmSwitchButtonHeight = 35.0f * this.zoomRate;
        this.bgmSwitchButtonOffsetX = this.offsetX + (145.0f * this.zoomRate);
        this.bgmSwitchButtonOffsetY = this.TITLELABEL01_OFFSET_Y + (37.0f * this.zoomRate);
        this.bgmSwitchButtonColor0 = -3171731;
        this.bgmSwitchButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.bgmSwitchButtonColor1 = -7576502;
        this.bgmSwitchButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.bgmSwitchButtonColor2 = 0;
        this.bgmSwitchButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.bgmSwitchButtonColor3 = 0;
        this.bgmSwitchButtonRadius = 10.0f * this.zoomRate;
        this.bgmSwitchButtonTitleLabelString = "";
        this.bgmSwitchButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.bgmSwitchButtonTitleLabelFontSize = 24.0f * this.zoomRate;
        this.bgmSwitchButtonTitleLabelColor0 = -16;
        this.bgmSwitchButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.bgmSwitchButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.bgmSwitchButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.bgmSwitchButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.bgmSwitchButtonTitleLabelTypeface);
        newPaint.setTextSize(this.bgmSwitchButtonTitleLabelFontSize);
        this.bgmSwitchButtonTitleLabelOffsetX = this.bgmSwitchButtonOffsetX + ((this.bgmSwitchButtonWidth - newPaint.measureText(this.bgmSwitchButtonTitleLabelString)) / 2.0f);
        this.bgmSwitchButtonTitleLabelOffsetY = (this.bgmSwitchButtonOffsetY + this.bgmSwitchButtonHeight) - (this.bgmSwitchButtonTitleLabelFontSize * 0.4f);
        this.soundSwitchButtonWidth = 90.0f * this.zoomRate;
        this.soundSwitchButtonHeight = 35.0f * this.zoomRate;
        this.soundSwitchButtonOffsetX = this.offsetX + (145.0f * this.zoomRate);
        this.soundSwitchButtonOffsetY = this.TITLELABEL01_OFFSET_Y + (82.0f * this.zoomRate);
        this.soundSwitchButtonColor0 = -3171731;
        this.soundSwitchButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.soundSwitchButtonColor1 = -7576502;
        this.soundSwitchButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.soundSwitchButtonColor2 = 0;
        this.soundSwitchButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.soundSwitchButtonColor3 = 0;
        this.soundSwitchButtonRadius = 10.0f * this.zoomRate;
        this.soundSwitchButtonTitleLabelString = "";
        this.soundSwitchButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.soundSwitchButtonTitleLabelFontSize = 24.0f * this.zoomRate;
        this.soundSwitchButtonTitleLabelColor0 = -16;
        this.soundSwitchButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.soundSwitchButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.soundSwitchButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.soundSwitchButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.soundSwitchButtonTitleLabelTypeface);
        newPaint.setTextSize(this.soundSwitchButtonTitleLabelFontSize);
        this.soundSwitchButtonTitleLabelOffsetX = this.soundSwitchButtonOffsetX + ((this.soundSwitchButtonWidth - newPaint.measureText(this.soundSwitchButtonTitleLabelString)) / 2.0f);
        this.soundSwitchButtonTitleLabelOffsetY = (this.soundSwitchButtonOffsetY + this.soundSwitchButtonHeight) - (this.soundSwitchButtonTitleLabelFontSize * 0.4f);
        this.slosButtonWidth = 26.0f * this.zoomRate;
        this.slosButtonHeight = 26.0f * this.zoomRate;
        this.slosButtonOffsetX = ((this.offsetX + this.finalWidth) - this.slosButtonWidth) - (12.0f * this.zoomRate);
        this.slosButtonOffsetY = this.TITLELABEL01_OFFSET_Y + (2.0f * this.zoomRate);
        this.slosButtonRadius = 10.0f * this.zoomRate;
        this.uploadButtonWidth = 200.0f * this.zoomRate;
        this.uploadButtonHeight = 35.0f * this.zoomRate;
        this.uploadButtonOffsetX = this.offsetX + (35.0f * this.zoomRate);
        this.uploadButtonOffsetY = this.TITLELABEL02_OFFSET_Y + (45.0f * this.zoomRate);
        this.uploadButtonColor0 = -3889;
        this.uploadButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.uploadButtonColor1 = -3109815;
        this.uploadButtonStrokeWidth2 = 1.0f * this.zoomRate;
        this.uploadButtonColor2 = -16;
        this.uploadButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.uploadButtonColor3 = 0;
        this.uploadButtonRadius = 10.0f * this.zoomRate;
        this.uploadButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.UploadSaveData);
        this.uploadButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.uploadButtonTitleLabelFontSize = 24.0f * this.zoomRate;
        this.uploadButtonTitleLabelColor0 = -16;
        this.uploadButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.uploadButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.uploadButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.uploadButtonTitleLabelColor2 = 0;
        this.uploadButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.uploadButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        newPaint.setTypeface(this.uploadButtonTitleLabelTypeface);
        newPaint.setTextSize(this.uploadButtonTitleLabelFontSize);
        this.uploadButtonTitleLabelOffsetX = this.uploadButtonOffsetX + ((this.uploadButtonWidth - newPaint.measureText(this.uploadButtonTitleLabelString)) / 2.0f);
        this.uploadButtonTitleLabelOffsetY = (this.uploadButtonOffsetY + this.uploadButtonHeight) - (this.uploadButtonTitleLabelFontSize * 0.35f);
        this.facebookButtonWidth = 50.0f * this.zoomRate;
        this.facebookButtonHeight = 50.0f * this.zoomRate;
        this.facebookButtonOffsetX = this.offsetX + (35.0f * this.zoomRate);
        this.facebookButtonOffsetY = this.TITLELABEL03_OFFSET_Y + (40.0f * this.zoomRate);
        this.facebookButtonRadius = 7.0f * this.zoomRate;
        this.lineButtonWidth = 50.0f * this.zoomRate;
        this.lineButtonHeight = 50.0f * this.zoomRate;
        this.lineButtonOffsetX = this.offsetX + (110.0f * this.zoomRate);
        this.lineButtonOffsetY = this.TITLELABEL03_OFFSET_Y + (40.0f * this.zoomRate);
        this.lineButtonRadius = 7.0f * this.zoomRate;
        this.mailButtonWidth = 50.0f * this.zoomRate;
        this.mailButtonHeight = 50.0f * this.zoomRate;
        this.mailButtonOffsetX = this.offsetX + (185.0f * this.zoomRate);
        this.mailButtonOffsetY = this.TITLELABEL03_OFFSET_Y + (40.0f * this.zoomRate);
        this.mailButtonRadius = 7.0f * this.zoomRate;
        this.iDTLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.iDTLabelFontSize = 24.0f * this.zoomRate;
        this.iDTLabelColor0 = -256;
        this.iDTLabelStroke1Width = 3.0f * this.zoomRate;
        this.iDTLabelColor1 = -12284725;
        this.iDTLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.iDTLabelColor2 = 0;
        newPaint.setTypeface(this.iDTLabelTypeface);
        newPaint.setTextSize(this.iDTLabelFontSize);
        this.IDTLABEL_OFFSET_X = this.offsetX + (40.0f * this.zoomRate);
        this.IDTLABEL_OFFSET_Y = this.TITLELABEL04_OFFSET_Y + (47.0f * this.zoomRate);
        this.IDTLABEL_WIDTH = 200.0f * this.zoomRate;
        this.IDTLABEL_HEIGHT = 35.0f * this.zoomRate;
        this.iDTLabelString = "iDT";
        this.iDTLabelOffsetX = this.IDTLABEL_OFFSET_X;
        this.iDTLabelOffsetY = this.IDTLABEL_OFFSET_Y + this.iDTLabelFontSize;
        this.iDTLogoutButtonWidth = 100.0f * this.zoomRate;
        this.iDTLogoutButtonHeight = 35.0f * this.zoomRate;
        this.iDTLogoutButtonOffsetX = this.offsetX + (135.0f * this.zoomRate);
        this.iDTLogoutButtonOffsetY = this.TITLELABEL04_OFFSET_Y + (45.0f * this.zoomRate);
        this.iDTLogoutButtonColor0 = -3889;
        this.iDTLogoutButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.iDTLogoutButtonColor1 = -3109815;
        this.iDTLogoutButtonStrokeWidth2 = 1.0f * this.zoomRate;
        this.iDTLogoutButtonColor2 = -16;
        this.iDTLogoutButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.iDTLogoutButtonColor3 = 0;
        this.iDTLogoutButtonRadius = 10.0f * this.zoomRate;
        this.iDTLogoutButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.LogOut);
        this.iDTLogoutButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.iDTLogoutButtonTitleLabelFontSize = 24.0f * this.zoomRate;
        this.iDTLogoutButtonTitleLabelColor0 = -16;
        this.iDTLogoutButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.iDTLogoutButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.iDTLogoutButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.iDTLogoutButtonTitleLabelColor2 = 0;
        this.iDTLogoutButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.iDTLogoutButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        newPaint.setTypeface(this.iDTLogoutButtonTitleLabelTypeface);
        newPaint.setTextSize(this.iDTLogoutButtonTitleLabelFontSize);
        this.iDTLogoutButtonTitleLabelOffsetX = this.iDTLogoutButtonOffsetX + ((this.iDTLogoutButtonWidth - newPaint.measureText(this.iDTLogoutButtonTitleLabelString)) / 2.0f);
        this.iDTLogoutButtonTitleLabelOffsetY = (this.iDTLogoutButtonOffsetY + this.iDTLogoutButtonHeight) - (this.iDTLogoutButtonTitleLabelFontSize * 0.35f);
        this.facebookLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.facebookLabelFontSize = 24.0f * this.zoomRate;
        this.facebookLabelColor0 = -1;
        this.facebookLabelStroke1Width = 3.0f * this.zoomRate;
        this.facebookLabelColor1 = -12887913;
        this.facebookLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.facebookLabelColor2 = 0;
        newPaint.setTypeface(this.facebookLabelTypeface);
        newPaint.setTextSize(this.facebookLabelFontSize);
        this.FACEBOOKLABEL_OFFSET_X = this.offsetX + (40.0f * this.zoomRate);
        this.FACEBOOKLABEL_OFFSET_Y = this.TITLELABEL04_OFFSET_Y + (97.0f * this.zoomRate);
        this.FACEBOOKLABEL_WIDTH = 200.0f * this.zoomRate;
        this.FACEBOOKLABEL_HEIGHT = 35.0f * this.zoomRate;
        this.facebookLabelString = this.appDelegate.getResources().getString(R.string.facebook);
        this.facebookLabelOffsetX = this.FACEBOOKLABEL_OFFSET_X;
        this.facebookLabelOffsetY = this.FACEBOOKLABEL_OFFSET_Y + this.facebookLabelFontSize;
        this.facebookLogoutButtonWidth = 100.0f * this.zoomRate;
        this.facebookLogoutButtonHeight = 35.0f * this.zoomRate;
        this.facebookLogoutButtonOffsetX = this.offsetX + (135.0f * this.zoomRate);
        this.facebookLogoutButtonOffsetY = this.TITLELABEL04_OFFSET_Y + (95.0f * this.zoomRate);
        this.facebookLogoutButtonColor0 = -3889;
        this.facebookLogoutButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.facebookLogoutButtonColor1 = -3109815;
        this.facebookLogoutButtonStrokeWidth2 = 1.0f * this.zoomRate;
        this.facebookLogoutButtonColor2 = -16;
        this.facebookLogoutButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.facebookLogoutButtonColor3 = 0;
        this.facebookLogoutButtonRadius = 10.0f * this.zoomRate;
        this.facebookLogoutButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.LogOut);
        this.facebookLogoutButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.facebookLogoutButtonTitleLabelFontSize = 24.0f * this.zoomRate;
        this.facebookLogoutButtonTitleLabelColor0 = -16;
        this.facebookLogoutButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.facebookLogoutButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.facebookLogoutButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.facebookLogoutButtonTitleLabelColor2 = 0;
        this.facebookLogoutButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.facebookLogoutButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        newPaint.setTypeface(this.facebookLogoutButtonTitleLabelTypeface);
        newPaint.setTextSize(this.facebookLogoutButtonTitleLabelFontSize);
        this.facebookLogoutButtonTitleLabelOffsetX = this.facebookLogoutButtonOffsetX + ((this.facebookLogoutButtonWidth - newPaint.measureText(this.facebookLogoutButtonTitleLabelString)) / 2.0f);
        this.facebookLogoutButtonTitleLabelOffsetY = (this.facebookLogoutButtonOffsetY + this.facebookLogoutButtonHeight) - (this.facebookLogoutButtonTitleLabelFontSize * 0.35f);
        this.backToMainMenuButtonWidth = 200.0f * this.zoomRate;
        this.backToMainMenuButtonHeight = 35.0f * this.zoomRate;
        this.backToMainMenuButtonOffsetX = this.offsetX + (35.0f * this.zoomRate);
        this.backToMainMenuButtonOffsetY = this.TITLELABEL05_OFFSET_Y + (45.0f * this.zoomRate);
        this.backToMainMenuButtonColor0 = -3889;
        this.backToMainMenuButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.backToMainMenuButtonColor1 = -3109815;
        this.backToMainMenuButtonStrokeWidth2 = 1.0f * this.zoomRate;
        this.backToMainMenuButtonColor2 = -16;
        this.backToMainMenuButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backToMainMenuButtonColor3 = 0;
        this.backToMainMenuButtonRadius = 10.0f * this.zoomRate;
        this.backToMainMenuButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.Back);
        this.backToMainMenuButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.backToMainMenuButtonTitleLabelFontSize = 24.0f * this.zoomRate;
        this.backToMainMenuButtonTitleLabelColor0 = -16;
        this.backToMainMenuButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.backToMainMenuButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backToMainMenuButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backToMainMenuButtonTitleLabelColor2 = 0;
        this.backToMainMenuButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backToMainMenuButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        newPaint.setTypeface(this.backToMainMenuButtonTitleLabelTypeface);
        newPaint.setTextSize(this.backToMainMenuButtonTitleLabelFontSize);
        this.backToMainMenuButtonTitleLabelOffsetX = this.backToMainMenuButtonOffsetX + ((this.backToMainMenuButtonWidth - newPaint.measureText(this.backToMainMenuButtonTitleLabelString)) / 2.0f);
        this.backToMainMenuButtonTitleLabelOffsetY = (this.backToMainMenuButtonOffsetY + this.backToMainMenuButtonHeight) - (this.backToMainMenuButtonTitleLabelFontSize * 0.35f);
        StringBuilder sb = new StringBuilder("Version ");
        this.appDelegate.getClass();
        this.copyrightLabelString = sb.append("2.3.0").append("   ").append("©2013 iDT Digital Inc.").toString();
        this.copyrightLabelTypeface = Typeface.DEFAULT_BOLD;
        this.copyrightLabelFontSize = 12.0f * this.zoomRate;
        this.copyrightLabelColor0 = -1;
        this.copyrightLabelStroke1Width = 2.0f * this.zoomRate;
        this.copyrightLabelColor1 = FluctConstants.FRAME_BASE_COLOR;
        this.copyrightLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.copyrightLabelColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        newPaint.setTypeface(this.copyrightLabelTypeface);
        newPaint.setTextSize(this.copyrightLabelFontSize);
        this.copyrightLabelOffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.copyrightLabelString)) / 2.0f);
        this.copyrightLabelOffsetY = (this.offsetY + this.finalHeight) - (14.0f * this.zoomRate);
        this.copyrightLabelShadowColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.copyrightLabelShadowOpacity = 2.0f * this.zoomRate;
        this.copyrightLabelShadowOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.copyrightLabelShadowOffsetY = 1.0f * this.zoomRate;
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.manualButton00Status = (short) 0;
        this.manualButton01Status = (short) 0;
        this.manualButton02Status = (short) 0;
        this.bgmSwitchButtonStatus = (short) 0;
        this.soundSwitchButtonStatus = (short) 0;
        this.slosButtonStatus = (short) 0;
        this.uploadButtonStatus = (short) 0;
        this.facebookButtonStatus = (short) 0;
        this.lineButtonStatus = (short) -1;
        this.mailButtonStatus = (short) 0;
        this.iDTLogoutButtonStatus = (short) -1;
        this.facebookLogoutButtonStatus = (short) -1;
        this.backToMainMenuButtonStatus = (short) 0;
        this.contactUsButtonStatus = (short) 0;
        refreshSubBitmap();
        this.myDraw = new MyDraw();
    }

    public void clearSubBitmap() {
        if (this.slosButtonBitmap != null) {
            if (!this.slosButtonBitmap.isRecycled()) {
                this.slosButtonBitmap.recycle();
            }
            this.slosButtonBitmap = null;
        }
        if (this.facebookButtonBitmap != null) {
            if (!this.facebookButtonBitmap.isRecycled()) {
                this.facebookButtonBitmap.recycle();
            }
            this.facebookButtonBitmap = null;
        }
        if (this.lineButtonBitmap != null) {
            if (!this.lineButtonBitmap.isRecycled()) {
                this.lineButtonBitmap.recycle();
            }
            this.lineButtonBitmap = null;
        }
        if (this.mailButtonBitmap != null) {
            if (!this.mailButtonBitmap.isRecycled()) {
                this.mailButtonBitmap.recycle();
            }
            this.mailButtonBitmap = null;
        }
    }

    public void refreshSubBitmap() throws IOException {
        clearSubBitmap();
        if (this.appDelegate != null) {
            AssetManager asm = this.appDelegate.getAssets();
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inSampleSize = 1;
            opt2.inPreferredConfig = Bitmap.Config.RGB_565;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            try {
                InputStream inputStream = asm.open("png/Share/slos_icon.png");
                this.slosButtonBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
            try {
                InputStream inputStream2 = asm.open("png/Share/facebook_icon.png");
                this.facebookButtonBitmap = BitmapFactory.decodeStream(inputStream2, null, opt2);
                inputStream2.close();
            } catch (IOException e2) {
            }
            try {
                InputStream inputStream3 = asm.open("png/Share/line_icon.png");
                this.lineButtonBitmap = BitmapFactory.decodeStream(inputStream3, null, opt2);
                inputStream3.close();
            } catch (IOException e3) {
            }
            try {
                InputStream inputStream4 = asm.open("png/Share/mail_icon.png");
                this.mailButtonBitmap = BitmapFactory.decodeStream(inputStream4, null, opt2);
                inputStream4.close();
            } catch (IOException e4) {
            }
        }
    }

    public boolean checkLineButtonStatuse() {
        if (this.appDelegate == null) {
            return false;
        }
        if (this.appDelegate.checkPackageInstalled("jp.naver.line.android")) {
            this.lineButtonStatus = (short) 0;
            return true;
        }
        this.lineButtonStatus = (short) -1;
        return false;
    }

    public void reset() {
        checkLineButtonStatuse();
        refreshBGMSwitchButton();
        refreshSoundSwitchButton();
        unclickAllButton();
    }

    public void refreshBGMSwitchButton() {
        if (this.appDelegate.defaultSharedPreferences != null) {
            if (this.appDelegate.defaultSharedPreferences.getBoolean("bgm_switch", false)) {
                setBGMSwitchButtonStatus((short) 1);
            } else {
                setBGMSwitchButtonStatus((short) 0);
            }
        }
    }

    public void setBGMSwitchButtonStatus(short _status) {
        if (_status == 0) {
            this.bgmSwitchButtonColor0 = -1118482;
            this.bgmSwitchButtonStrokeWidth1 = this.zoomRate * 1.0f;
            this.bgmSwitchButtonColor1 = -7829368;
            this.bgmSwitchButtonTitleLabelString = "OFF";
            this.bgmSwitchButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.bgmSwitchButtonTitleLabelFontSize = this.zoomRate * 24.0f;
            this.bgmSwitchButtonTitleLabelColor0 = -3355444;
            this.bgmSwitchButtonTitleLabelStroke1Width = this.zoomRate * 3.0f;
            this.bgmSwitchButtonTitleLabelColor1 = -12303292;
        } else {
            this.bgmSwitchButtonColor0 = -11740828;
            this.bgmSwitchButtonStrokeWidth1 = this.zoomRate * 1.0f;
            this.bgmSwitchButtonColor1 = -7829368;
            this.bgmSwitchButtonTitleLabelString = "ON";
            this.bgmSwitchButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.bgmSwitchButtonTitleLabelFontSize = this.zoomRate * 24.0f;
            this.bgmSwitchButtonTitleLabelColor0 = -1;
            this.bgmSwitchButtonTitleLabelStroke1Width = this.zoomRate * 3.0f;
            this.bgmSwitchButtonTitleLabelColor1 = -14540254;
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.bgmSwitchButtonTitleLabelTypeface);
        newPaint.setTextSize(this.bgmSwitchButtonTitleLabelFontSize);
        this.bgmSwitchButtonTitleLabelOffsetX = this.bgmSwitchButtonOffsetX + ((this.bgmSwitchButtonWidth - newPaint.measureText(this.bgmSwitchButtonTitleLabelString)) / 2.0f);
        this.bgmSwitchButtonTitleLabelOffsetY = (this.bgmSwitchButtonOffsetY + this.bgmSwitchButtonHeight) - (this.bgmSwitchButtonTitleLabelFontSize * 0.4f);
    }

    public void refreshSoundSwitchButton() {
        if (this.appDelegate.defaultSharedPreferences != null) {
            if (this.appDelegate.defaultSharedPreferences.getBoolean("sound_switch", false)) {
                setSoundSwitchButtonStatus((short) 1);
            } else {
                setSoundSwitchButtonStatus((short) 0);
            }
        }
    }

    public void setSoundSwitchButtonStatus(short _status) {
        if (_status == 0) {
            this.soundSwitchButtonColor0 = -1118482;
            this.soundSwitchButtonStrokeWidth1 = this.zoomRate * 1.0f;
            this.soundSwitchButtonColor1 = -7829368;
            this.soundSwitchButtonTitleLabelString = "OFF";
            this.soundSwitchButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.soundSwitchButtonTitleLabelFontSize = this.zoomRate * 24.0f;
            this.soundSwitchButtonTitleLabelColor0 = -3355444;
            this.soundSwitchButtonTitleLabelStroke1Width = this.zoomRate * 3.0f;
            this.soundSwitchButtonTitleLabelColor1 = -12303292;
        } else {
            this.soundSwitchButtonColor0 = -11740828;
            this.soundSwitchButtonStrokeWidth1 = this.zoomRate * 1.0f;
            this.soundSwitchButtonColor1 = -7829368;
            this.soundSwitchButtonTitleLabelString = "ON";
            this.soundSwitchButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.soundSwitchButtonTitleLabelFontSize = this.zoomRate * 24.0f;
            this.soundSwitchButtonTitleLabelColor0 = -1;
            this.soundSwitchButtonTitleLabelStroke1Width = this.zoomRate * 3.0f;
            this.soundSwitchButtonTitleLabelColor1 = -14540254;
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.soundSwitchButtonTitleLabelTypeface);
        newPaint.setTextSize(this.soundSwitchButtonTitleLabelFontSize);
        this.soundSwitchButtonTitleLabelOffsetX = this.soundSwitchButtonOffsetX + ((this.soundSwitchButtonWidth - newPaint.measureText(this.soundSwitchButtonTitleLabelString)) / 2.0f);
        this.soundSwitchButtonTitleLabelOffsetY = (this.soundSwitchButtonOffsetY + this.soundSwitchButtonHeight) - (this.soundSwitchButtonTitleLabelFontSize * 0.4f);
    }

    public void unclickAllButton() {
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
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
        if (event.getX() < this.offsetX || event.getX() > this.clipRectWidth || event.getY() < this.offsetY || event.getY() > this.clipRectHeight) {
            unclickAllButton();
            return false;
        }
        if (event.getAction() == 1) {
            setPreScrollPoint(event.getX(), event.getY());
            doClick();
        } else if (event.getAction() == 0) {
            setPreScrollPoint(event.getX(), event.getY());
            this.selectedButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            this.preScrollY = event.getY();
            if (event.getX() > this.manualButton00OffsetX && event.getX() < this.manualButton00OffsetX + this.manualButtonWidth && event.getY() >= this.manualButton00OffsetY - this.offsetScrollY && event.getY() <= (this.manualButton00OffsetY + this.manualButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.manualButton01OffsetX && event.getX() < this.manualButton01OffsetX + this.manualButtonWidth && event.getY() >= this.manualButton01OffsetY - this.offsetScrollY && event.getY() <= (this.manualButton01OffsetY + this.manualButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 1;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.manualButton02OffsetX && event.getX() < this.manualButton02OffsetX + this.manualButtonWidth && event.getY() >= this.manualButton02OffsetY - this.offsetScrollY && event.getY() <= (this.manualButton02OffsetY + this.manualButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 2;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.3
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.bgmSwitchButtonOffsetX && event.getX() < this.bgmSwitchButtonOffsetX + this.bgmSwitchButtonWidth && event.getY() >= this.bgmSwitchButtonOffsetY - this.offsetScrollY && event.getY() <= (this.bgmSwitchButtonOffsetY + this.bgmSwitchButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 10;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.4
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.soundSwitchButtonOffsetX && event.getX() < this.soundSwitchButtonOffsetX + this.soundSwitchButtonWidth && event.getY() >= this.soundSwitchButtonOffsetY - this.offsetScrollY && event.getY() <= (this.soundSwitchButtonOffsetY + this.soundSwitchButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 11;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.5
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.slosButtonOffsetX && event.getX() < this.slosButtonOffsetX + this.slosButtonWidth && event.getY() >= this.slosButtonOffsetY - this.offsetScrollY && event.getY() <= (this.slosButtonOffsetY + this.slosButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 12;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.6
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.uploadButtonOffsetX && event.getX() < this.uploadButtonOffsetX + this.uploadButtonWidth && event.getY() >= this.uploadButtonOffsetY - this.offsetScrollY && event.getY() <= (this.uploadButtonOffsetY + this.uploadButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 20;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.7
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.facebookButtonOffsetX && event.getX() < this.facebookButtonOffsetX + this.facebookButtonWidth && event.getY() >= this.facebookButtonOffsetY - this.offsetScrollY && event.getY() <= (this.facebookButtonOffsetY + this.facebookButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 30;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.8
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.lineButtonStatus == 0 && event.getX() > this.lineButtonOffsetX && event.getX() < this.lineButtonOffsetX + this.lineButtonWidth && event.getY() >= this.lineButtonOffsetY - this.offsetScrollY && event.getY() <= (this.lineButtonOffsetY + this.lineButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 31;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.9
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.mailButtonOffsetX && event.getX() < this.mailButtonOffsetX + this.mailButtonWidth && event.getY() >= this.mailButtonOffsetY - this.offsetScrollY && event.getY() <= (this.mailButtonOffsetY + this.mailButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 32;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.10
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.iDTLogoutButtonStatus == 0 && event.getX() > this.iDTLogoutButtonOffsetX && event.getX() < this.iDTLogoutButtonOffsetX + this.iDTLogoutButtonWidth && event.getY() >= this.iDTLogoutButtonOffsetY - this.offsetScrollY && event.getY() <= (this.iDTLogoutButtonOffsetY + this.iDTLogoutButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 40;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.11
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.facebookLogoutButtonStatus == 0 && event.getX() > this.facebookLogoutButtonOffsetX && event.getX() < this.facebookLogoutButtonOffsetX + this.facebookLogoutButtonWidth && event.getY() >= this.facebookLogoutButtonOffsetY - this.offsetScrollY && event.getY() <= (this.facebookLogoutButtonOffsetY + this.facebookLogoutButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 41;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.12
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.backToMainMenuButtonOffsetX && event.getX() < this.backToMainMenuButtonOffsetX + this.backToMainMenuButtonWidth && event.getY() >= this.backToMainMenuButtonOffsetY - this.offsetScrollY && event.getY() <= (this.backToMainMenuButtonOffsetY + this.backToMainMenuButtonHeight) - this.offsetScrollY) {
                this.selectedButtonIndex = (short) 50;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpScrollViewUnit.13
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpScrollViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        } else if (event.getAction() == 2) {
            float addOffScrollY = this.preScrollY - event.getY();
            this.offsetScrollY += addOffScrollY;
            if (this.offsetScrollY < BitmapDescriptorFactory.HUE_RED) {
                this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
            } else if (this.offsetScrollY > this.offsetScrollYMax) {
                this.offsetScrollY = this.offsetScrollYMax;
            }
            float subScrollY = BitmapDescriptorFactory.HUE_RED;
            if (getPreScrollY() != event.getY()) {
                subScrollY = Math.abs(getPreScrollY() - event.getY());
            }
            if (subScrollY > this.scrollCancelLimit) {
                unclickAllButton();
                Log.i("scrollView.setOnTouchListener", "unclick button");
            } else {
                float subScrollX = BitmapDescriptorFactory.HUE_RED;
                if (getPreScrollX() != event.getX()) {
                    subScrollX = Math.abs(getPreScrollX() - event.getX());
                }
                if (subScrollX > this.scrollCancelLimit) {
                    unclickAllButton();
                    Log.i("scrollView.setOnTouchListener", "unclick button");
                }
                Log.i("scrollView.setOnTouchListener", "subScrollX=" + subScrollX);
            }
            setPreScrollPoint(event.getX(), event.getY());
            Log.d("FarmListScrollLayout", "ACTION_MOVE   X=" + event.getX() + ", Y=  " + event.getY());
            Log.d("FarmListScrollLayout", "preScrollY =  " + this.preScrollY);
            Log.d("FarmListScrollLayout", "offsetScrollY =  " + this.offsetScrollY);
        }
        return false;
    }

    public void doClick() {
        if (this.selectedButtonIndex >= 0) {
            if (this.selectedButtonIndex == 0 || this.selectedButtonIndex == 1 || this.selectedButtonIndex == 2 || this.selectedButtonIndex == 10 || this.selectedButtonIndex == 11 || this.selectedButtonIndex == 12 || this.selectedButtonIndex == 20 || this.selectedButtonIndex == 30) {
                this.helpUnit.doButtonClick(this.selectedButtonIndex);
            } else if (this.selectedButtonIndex == 31) {
                if (checkLineButtonStatuse()) {
                    this.helpUnit.doButtonClick(this.selectedButtonIndex);
                }
            } else if (this.selectedButtonIndex == 32) {
                this.helpUnit.doButtonClick(this.selectedButtonIndex);
            } else if (this.selectedButtonIndex == 40) {
                if (this.iDTLogoutButtonStatus == 0) {
                    this.helpUnit.doButtonClick(this.selectedButtonIndex);
                }
            } else if (this.selectedButtonIndex == 41) {
                if (this.facebookLogoutButtonStatus == 0) {
                    this.helpUnit.doButtonClick(this.selectedButtonIndex);
                }
            } else if (this.selectedButtonIndex == 50) {
                this.helpUnit.doButtonClick(this.selectedButtonIndex);
            }
        }
        unclickAllButton();
    }

    public void gameDraw(Canvas canvas) {
        canvas.save();
        canvas.clipRect(this.offsetX, this.offsetY, this.clipRectWidth, this.clipRectHeight);
        Paint bitmapPaint = new Paint(257);
        bitmapPaint.setColor(-1);
        canvas.drawRect(this.offsetX, this.offsetY, this.clipRectWidth, this.clipRectHeight, bitmapPaint);
        float drawOffsetScrollY = this.offsetScrollY;
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        bitmapPaint.setStyle(Paint.Style.FILL);
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        bitmapPaint.setAlpha(25);
        canvas.drawRect(this.TITLELABEL00_OFFSET_X, this.TITLELABEL00_OFFSET_Y - drawOffsetScrollY, this.TITLELABEL00_WIDTH + this.TITLELABEL00_OFFSET_X, (this.TITLELABEL00_OFFSET_Y + this.TITLELABEL00_HEIGHT) - drawOffsetScrollY, bitmapPaint);
        MyDraw.drawStrokeText(canvas, this.titleLabel00OffsetX, this.titleLabel00OffsetY - drawOffsetScrollY, this.titleLabelTypeface, this.titleLabel00String, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        bitmapPaint.setAlpha(25);
        canvas.drawRect(this.TITLELABEL01_OFFSET_X, this.TITLELABEL01_OFFSET_Y - drawOffsetScrollY, this.TITLELABEL01_WIDTH + this.TITLELABEL01_OFFSET_X, (this.TITLELABEL01_OFFSET_Y + this.TITLELABEL01_HEIGHT) - drawOffsetScrollY, bitmapPaint);
        MyDraw.drawStrokeText(canvas, this.titleLabel01OffsetX, this.titleLabel01OffsetY - drawOffsetScrollY, this.titleLabelTypeface, this.titleLabel01String, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        bitmapPaint.setAlpha(25);
        canvas.drawRect(this.TITLELABEL02_OFFSET_X, this.TITLELABEL02_OFFSET_Y - drawOffsetScrollY, this.TITLELABEL02_WIDTH + this.TITLELABEL02_OFFSET_X, (this.TITLELABEL02_OFFSET_Y + this.TITLELABEL02_HEIGHT) - drawOffsetScrollY, bitmapPaint);
        MyDraw.drawStrokeText(canvas, this.titleLabel02OffsetX, this.titleLabel02OffsetY - drawOffsetScrollY, this.titleLabelTypeface, this.titleLabel02String, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        bitmapPaint.setAlpha(25);
        canvas.drawRect(this.TITLELABEL03_OFFSET_X, this.TITLELABEL03_OFFSET_Y - drawOffsetScrollY, this.TITLELABEL03_WIDTH + this.TITLELABEL03_OFFSET_X, (this.TITLELABEL03_OFFSET_Y + this.TITLELABEL03_HEIGHT) - drawOffsetScrollY, bitmapPaint);
        MyDraw.drawStrokeText(canvas, this.titleLabel03OffsetX, this.titleLabel03OffsetY - drawOffsetScrollY, this.titleLabelTypeface, this.titleLabel03String, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        bitmapPaint.setAlpha(25);
        canvas.drawRect(this.TITLELABEL04_OFFSET_X, this.TITLELABEL04_OFFSET_Y - drawOffsetScrollY, this.TITLELABEL04_WIDTH + this.TITLELABEL04_OFFSET_X, (this.TITLELABEL04_OFFSET_Y + this.TITLELABEL04_HEIGHT) - drawOffsetScrollY, bitmapPaint);
        MyDraw.drawStrokeText(canvas, this.titleLabel04OffsetX, this.titleLabel04OffsetY - drawOffsetScrollY, this.titleLabelTypeface, this.titleLabel04String, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        bitmapPaint.setAlpha(25);
        canvas.drawRect(this.TITLELABEL05_OFFSET_X, this.TITLELABEL05_OFFSET_Y - drawOffsetScrollY, this.TITLELABEL05_WIDTH + this.TITLELABEL05_OFFSET_X, (this.TITLELABEL05_OFFSET_Y + this.TITLELABEL05_HEIGHT) - drawOffsetScrollY, bitmapPaint);
        MyDraw.drawStrokeText(canvas, this.titleLabel05OffsetX, this.titleLabel05OffsetY - drawOffsetScrollY, this.titleLabelTypeface, this.titleLabel05String, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        MyDraw.drawStrokeText(canvas, this.bgmLabelOffsetX, this.bgmLabelOffsetY - drawOffsetScrollY, this.bgmLabelTypeface, this.bgmLabelString, this.bgmLabelFontSize, this.bgmLabelColor0, this.bgmLabelStroke1Width, this.bgmLabelColor1, this.bgmLabelStroke2Width, this.bgmLabelColor2);
        MyDraw.drawStrokeText(canvas, this.soundLabelOffsetX, this.soundLabelOffsetY - drawOffsetScrollY, this.soundLabelTypeface, this.soundLabelString, this.soundLabelFontSize, this.soundLabelColor0, this.soundLabelStroke1Width, this.soundLabelColor1, this.soundLabelStroke2Width, this.soundLabelColor2);
        if (this.manualButton00Status == 0) {
            MyDraw.drawStrokeRectWithShadow(canvas, this.manualButton00OffsetX, this.manualButton00OffsetY - drawOffsetScrollY, this.manualButtonWidth, this.manualButtonHeight, this.manualButtonColor0, this.manualButtonStrokeWidth1, this.manualButtonColor1, this.manualButtonStrokeWidth2, this.manualButtonColor2, this.manualButtonStrokeWidth3, this.manualButtonColor3, this.manualButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
            MyDraw.drawStrokeText(canvas, this.manualButton00TitleLabelOffsetX, this.manualButton00TitleLabelOffsetY - drawOffsetScrollY, this.manualButtonTitleLabelTypeface, this.manualButton00TitleLabelString, this.manualButtonTitleLabelFontSize, this.manualButtonTitleLabelColor0, this.manualButtonTitleLabelStroke1Width, this.manualButtonTitleLabelColor1, this.manualButtonTitleLabelStroke2Width, this.manualButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.manualButton00OffsetX, this.manualButton00OffsetY - drawOffsetScrollY, this.manualButtonWidth, this.manualButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.manualButtonRadius);
            }
        }
        if (this.manualButton01Status == 0) {
            MyDraw.drawStrokeRectWithShadow(canvas, this.manualButton01OffsetX, this.manualButton01OffsetY - drawOffsetScrollY, this.manualButtonWidth, this.manualButtonHeight, this.manualButtonColor0, this.manualButtonStrokeWidth1, this.manualButtonColor1, this.manualButtonStrokeWidth2, this.manualButtonColor2, this.manualButtonStrokeWidth3, this.manualButtonColor3, this.manualButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
            MyDraw.drawStrokeText(canvas, this.manualButton01TitleLabelOffsetX, this.manualButton01TitleLabelOffsetY - drawOffsetScrollY, this.manualButtonTitleLabelTypeface, this.manualButton01TitleLabelString, this.manualButtonTitleLabelFontSize, this.manualButtonTitleLabelColor0, this.manualButtonTitleLabelStroke1Width, this.manualButtonTitleLabelColor1, this.manualButtonTitleLabelStroke2Width, this.manualButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 1) {
                MyDraw.drawStrokeRect(canvas, this.manualButton01OffsetX, this.manualButton01OffsetY - drawOffsetScrollY, this.manualButtonWidth, this.manualButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.manualButtonRadius);
            }
        }
        if (this.manualButton02Status == 0) {
            MyDraw.drawStrokeRectWithShadow(canvas, this.manualButton02OffsetX, this.manualButton02OffsetY - drawOffsetScrollY, this.manualButtonWidth, this.manualButtonHeight, this.manualButtonColor0, this.manualButtonStrokeWidth1, this.manualButtonColor1, this.manualButtonStrokeWidth2, this.manualButtonColor2, this.manualButtonStrokeWidth3, this.manualButtonColor3, this.manualButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
            MyDraw.drawStrokeText(canvas, this.manualButton02TitleLabelOffsetX, this.manualButton02TitleLabelOffsetY - drawOffsetScrollY, this.manualButtonTitleLabelTypeface, this.manualButton02TitleLabelString, this.manualButtonTitleLabelFontSize, this.manualButtonTitleLabelColor0, this.manualButtonTitleLabelStroke1Width, this.manualButtonTitleLabelColor1, this.manualButtonTitleLabelStroke2Width, this.manualButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 2) {
                MyDraw.drawStrokeRect(canvas, this.manualButton02OffsetX, this.manualButton02OffsetY - drawOffsetScrollY, this.manualButtonWidth, this.manualButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.manualButtonRadius);
            }
        }
        if (this.bgmSwitchButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.bgmSwitchButtonOffsetX, this.bgmSwitchButtonOffsetY - drawOffsetScrollY, this.bgmSwitchButtonWidth, this.bgmSwitchButtonHeight, this.bgmSwitchButtonColor0, this.bgmSwitchButtonStrokeWidth1, this.bgmSwitchButtonColor1, this.bgmSwitchButtonStrokeWidth2, this.bgmSwitchButtonColor2, this.bgmSwitchButtonStrokeWidth3, this.bgmSwitchButtonColor3, this.bgmSwitchButtonRadius);
            MyDraw.drawStrokeText(canvas, this.bgmSwitchButtonTitleLabelOffsetX, this.bgmSwitchButtonTitleLabelOffsetY - drawOffsetScrollY, this.bgmSwitchButtonTitleLabelTypeface, this.bgmSwitchButtonTitleLabelString, this.bgmSwitchButtonTitleLabelFontSize, this.bgmSwitchButtonTitleLabelColor0, this.bgmSwitchButtonTitleLabelStroke1Width, this.bgmSwitchButtonTitleLabelColor1, this.bgmSwitchButtonTitleLabelStroke2Width, this.bgmSwitchButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 10) {
                MyDraw.drawStrokeRect(canvas, this.bgmSwitchButtonOffsetX, this.bgmSwitchButtonOffsetY - drawOffsetScrollY, this.bgmSwitchButtonWidth, this.bgmSwitchButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.bgmSwitchButtonRadius);
            }
        }
        if (this.soundSwitchButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.soundSwitchButtonOffsetX, this.soundSwitchButtonOffsetY - drawOffsetScrollY, this.soundSwitchButtonWidth, this.soundSwitchButtonHeight, this.soundSwitchButtonColor0, this.soundSwitchButtonStrokeWidth1, this.soundSwitchButtonColor1, this.soundSwitchButtonStrokeWidth2, this.soundSwitchButtonColor2, this.soundSwitchButtonStrokeWidth3, this.soundSwitchButtonColor3, this.soundSwitchButtonRadius);
            MyDraw.drawStrokeText(canvas, this.soundSwitchButtonTitleLabelOffsetX, this.soundSwitchButtonTitleLabelOffsetY - drawOffsetScrollY, this.soundSwitchButtonTitleLabelTypeface, this.soundSwitchButtonTitleLabelString, this.soundSwitchButtonTitleLabelFontSize, this.soundSwitchButtonTitleLabelColor0, this.soundSwitchButtonTitleLabelStroke1Width, this.soundSwitchButtonTitleLabelColor1, this.soundSwitchButtonTitleLabelStroke2Width, this.soundSwitchButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 11) {
                MyDraw.drawStrokeRect(canvas, this.soundSwitchButtonOffsetX, this.soundSwitchButtonOffsetY - drawOffsetScrollY, this.soundSwitchButtonWidth, this.soundSwitchButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.soundSwitchButtonRadius);
            }
        }
        if (this.slosButtonStatus == 0) {
            if (this.slosButtonBitmap != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.slosButtonBitmap, new Rect(0, 0, this.slosButtonBitmap.getWidth(), this.slosButtonBitmap.getHeight()), new Rect((int) this.slosButtonOffsetX, (int) (this.slosButtonOffsetY - drawOffsetScrollY), (int) (this.slosButtonOffsetX + this.slosButtonWidth), (int) ((this.slosButtonOffsetY - drawOffsetScrollY) + this.slosButtonHeight)), bitmapPaint);
            }
            if (this.selectedButtonIndex == 12) {
                MyDraw.drawStrokeRect(canvas, this.slosButtonOffsetX, this.slosButtonOffsetY - drawOffsetScrollY, this.slosButtonWidth, this.slosButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.slosButtonRadius);
            }
        }
        if (this.uploadButtonStatus == 0) {
            MyDraw.drawStrokeRectWithShadow(canvas, this.uploadButtonOffsetX, this.uploadButtonOffsetY - drawOffsetScrollY, this.uploadButtonWidth, this.uploadButtonHeight, this.uploadButtonColor0, this.uploadButtonStrokeWidth1, this.uploadButtonColor1, this.uploadButtonStrokeWidth2, this.uploadButtonColor2, this.uploadButtonStrokeWidth3, this.uploadButtonColor3, this.uploadButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
            MyDraw.drawStrokeText(canvas, this.uploadButtonTitleLabelOffsetX, this.uploadButtonTitleLabelOffsetY - drawOffsetScrollY, this.uploadButtonTitleLabelTypeface, this.uploadButtonTitleLabelString, this.uploadButtonTitleLabelFontSize, this.uploadButtonTitleLabelColor0, this.uploadButtonTitleLabelStroke1Width, this.uploadButtonTitleLabelColor1, this.uploadButtonTitleLabelStroke2Width, this.uploadButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 20) {
                MyDraw.drawStrokeRect(canvas, this.uploadButtonOffsetX, this.uploadButtonOffsetY - drawOffsetScrollY, this.uploadButtonWidth, this.uploadButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.uploadButtonRadius);
            }
        }
        if (this.facebookButtonStatus == 0) {
            if (this.facebookButtonBitmap != null) {
                MyDraw.drawStrokeRectWithShadow(canvas, this.facebookButtonOffsetX, this.facebookButtonOffsetY - drawOffsetScrollY, this.facebookButtonWidth, this.facebookButtonHeight, -1, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, this.facebookButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.facebookButtonBitmap, new Rect(0, 0, this.facebookButtonBitmap.getWidth(), this.facebookButtonBitmap.getHeight()), new Rect((int) this.facebookButtonOffsetX, (int) (this.facebookButtonOffsetY - drawOffsetScrollY), (int) (this.facebookButtonOffsetX + this.facebookButtonWidth), (int) ((this.facebookButtonOffsetY - drawOffsetScrollY) + this.facebookButtonHeight)), bitmapPaint);
            }
            if (this.selectedButtonIndex == 30) {
                MyDraw.drawStrokeRect(canvas, this.facebookButtonOffsetX, this.facebookButtonOffsetY - drawOffsetScrollY, this.facebookButtonWidth, this.facebookButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.facebookButtonRadius);
            }
        }
        if (this.lineButtonStatus == 0) {
            if (this.lineButtonBitmap != null) {
                MyDraw.drawStrokeRectWithShadow(canvas, this.lineButtonOffsetX, this.lineButtonOffsetY - drawOffsetScrollY, this.lineButtonWidth, this.lineButtonHeight, -1, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, this.lineButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.lineButtonBitmap, new Rect(0, 0, this.lineButtonBitmap.getWidth(), this.lineButtonBitmap.getHeight()), new Rect((int) this.lineButtonOffsetX, (int) (this.lineButtonOffsetY - drawOffsetScrollY), (int) (this.lineButtonOffsetX + this.lineButtonWidth), (int) ((this.lineButtonOffsetY - drawOffsetScrollY) + this.lineButtonHeight)), bitmapPaint);
            }
            if (this.selectedButtonIndex == 31) {
                MyDraw.drawStrokeRect(canvas, this.lineButtonOffsetX, this.lineButtonOffsetY - drawOffsetScrollY, this.lineButtonWidth, this.lineButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.lineButtonRadius);
            }
        } else if (this.lineButtonStatus == -1 && this.lineButtonBitmap != null) {
            bitmapPaint.setAlpha(76);
            canvas.drawBitmap(this.lineButtonBitmap, new Rect(0, 0, this.lineButtonBitmap.getWidth(), this.lineButtonBitmap.getHeight()), new Rect((int) this.lineButtonOffsetX, (int) (this.lineButtonOffsetY - drawOffsetScrollY), (int) (this.lineButtonOffsetX + this.lineButtonWidth), (int) ((this.lineButtonOffsetY - drawOffsetScrollY) + this.lineButtonHeight)), bitmapPaint);
        }
        if (this.mailButtonStatus == 0) {
            if (this.mailButtonBitmap != null) {
                MyDraw.drawStrokeRectWithShadow(canvas, this.mailButtonOffsetX, this.mailButtonOffsetY - drawOffsetScrollY, this.mailButtonWidth, this.mailButtonHeight, -1, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, this.mailButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.mailButtonBitmap, new Rect(0, 0, this.mailButtonBitmap.getWidth(), this.mailButtonBitmap.getHeight()), new Rect((int) this.mailButtonOffsetX, (int) (this.mailButtonOffsetY - drawOffsetScrollY), (int) (this.mailButtonOffsetX + this.mailButtonWidth), (int) ((this.mailButtonOffsetY - drawOffsetScrollY) + this.mailButtonHeight)), bitmapPaint);
            }
            if (this.selectedButtonIndex == 32) {
                MyDraw.drawStrokeRect(canvas, this.mailButtonOffsetX, this.mailButtonOffsetY - drawOffsetScrollY, this.mailButtonWidth, this.mailButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.mailButtonRadius);
            }
        }
        MyDraw.drawStrokeText(canvas, this.iDTLabelOffsetX, this.iDTLabelOffsetY - drawOffsetScrollY, this.iDTLabelTypeface, this.iDTLabelString, this.iDTLabelFontSize, this.iDTLabelColor0, this.iDTLabelStroke1Width, this.iDTLabelColor1, this.iDTLabelStroke2Width, this.iDTLabelColor2);
        MyDraw.drawStrokeRectWithShadow(canvas, this.iDTLogoutButtonOffsetX, this.iDTLogoutButtonOffsetY - drawOffsetScrollY, this.iDTLogoutButtonWidth, this.iDTLogoutButtonHeight, this.iDTLogoutButtonColor0, this.iDTLogoutButtonStrokeWidth1, this.iDTLogoutButtonColor1, this.iDTLogoutButtonStrokeWidth2, this.iDTLogoutButtonColor2, this.iDTLogoutButtonStrokeWidth3, this.iDTLogoutButtonColor3, this.iDTLogoutButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
        MyDraw.drawStrokeText(canvas, this.iDTLogoutButtonTitleLabelOffsetX, this.iDTLogoutButtonTitleLabelOffsetY - drawOffsetScrollY, this.iDTLogoutButtonTitleLabelTypeface, this.iDTLogoutButtonTitleLabelString, this.iDTLogoutButtonTitleLabelFontSize, this.iDTLogoutButtonTitleLabelColor0, this.iDTLogoutButtonTitleLabelStroke1Width, this.iDTLogoutButtonTitleLabelColor1, this.iDTLogoutButtonTitleLabelStroke2Width, this.iDTLogoutButtonTitleLabelColor0);
        if (this.iDTLogoutButtonStatus != 0 || this.selectedButtonIndex == 40) {
            MyDraw.drawStrokeRect(canvas, this.iDTLogoutButtonOffsetX, this.iDTLogoutButtonOffsetY - drawOffsetScrollY, this.iDTLogoutButtonWidth, this.iDTLogoutButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.iDTLogoutButtonRadius);
        }
        MyDraw.drawStrokeText(canvas, this.facebookLabelOffsetX, this.facebookLabelOffsetY - drawOffsetScrollY, this.facebookLabelTypeface, this.facebookLabelString, this.facebookLabelFontSize, this.facebookLabelColor0, this.facebookLabelStroke1Width, this.facebookLabelColor1, this.facebookLabelStroke2Width, this.facebookLabelColor2);
        MyDraw.drawStrokeRectWithShadow(canvas, this.facebookLogoutButtonOffsetX, this.facebookLogoutButtonOffsetY - drawOffsetScrollY, this.facebookLogoutButtonWidth, this.facebookLogoutButtonHeight, this.facebookLogoutButtonColor0, this.facebookLogoutButtonStrokeWidth1, this.facebookLogoutButtonColor1, this.facebookLogoutButtonStrokeWidth2, this.facebookLogoutButtonColor2, this.facebookLogoutButtonStrokeWidth3, this.facebookLogoutButtonColor3, this.facebookLogoutButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
        MyDraw.drawStrokeText(canvas, this.facebookLogoutButtonTitleLabelOffsetX, this.facebookLogoutButtonTitleLabelOffsetY - drawOffsetScrollY, this.facebookLogoutButtonTitleLabelTypeface, this.facebookLogoutButtonTitleLabelString, this.facebookLogoutButtonTitleLabelFontSize, this.facebookLogoutButtonTitleLabelColor0, this.facebookLogoutButtonTitleLabelStroke1Width, this.facebookLogoutButtonTitleLabelColor1, this.facebookLogoutButtonTitleLabelStroke2Width, this.facebookLogoutButtonTitleLabelColor0);
        if (this.facebookLogoutButtonStatus != 0 || this.selectedButtonIndex == 41) {
            MyDraw.drawStrokeRect(canvas, this.facebookLogoutButtonOffsetX, this.facebookLogoutButtonOffsetY - drawOffsetScrollY, this.facebookLogoutButtonWidth, this.facebookLogoutButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.facebookLogoutButtonRadius);
        }
        if (this.backToMainMenuButtonStatus == 0) {
            MyDraw.drawStrokeRectWithShadow(canvas, this.backToMainMenuButtonOffsetX, this.backToMainMenuButtonOffsetY - drawOffsetScrollY, this.backToMainMenuButtonWidth, this.backToMainMenuButtonHeight, this.backToMainMenuButtonColor0, this.backToMainMenuButtonStrokeWidth1, this.backToMainMenuButtonColor1, this.backToMainMenuButtonStrokeWidth2, this.backToMainMenuButtonColor2, this.backToMainMenuButtonStrokeWidth3, this.backToMainMenuButtonColor3, this.backToMainMenuButtonRadius, this.buttonShadowOpacity, this.buttonShadowOffsetX, this.buttonShadowOffsetY, this.buttonShadowColor);
            MyDraw.drawStrokeText(canvas, this.backToMainMenuButtonTitleLabelOffsetX, this.backToMainMenuButtonTitleLabelOffsetY - drawOffsetScrollY, this.backToMainMenuButtonTitleLabelTypeface, this.backToMainMenuButtonTitleLabelString, this.backToMainMenuButtonTitleLabelFontSize, this.backToMainMenuButtonTitleLabelColor0, this.backToMainMenuButtonTitleLabelStroke1Width, this.backToMainMenuButtonTitleLabelColor1, this.backToMainMenuButtonTitleLabelStroke2Width, this.backToMainMenuButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 50) {
                MyDraw.drawStrokeRect(canvas, this.backToMainMenuButtonOffsetX, this.backToMainMenuButtonOffsetY - drawOffsetScrollY, this.backToMainMenuButtonWidth, this.backToMainMenuButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.backToMainMenuButtonRadius);
            }
        }
        MyDraw.drawShadowStrokeText(canvas, this.copyrightLabelOffsetX, this.copyrightLabelOffsetY - drawOffsetScrollY, this.copyrightLabelTypeface, this.copyrightLabelString, this.copyrightLabelFontSize, this.copyrightLabelColor0, this.copyrightLabelStroke1Width, this.copyrightLabelColor1, this.copyrightLabelStroke2Width, this.copyrightLabelColor2, this.copyrightLabelShadowColor, (int) this.copyrightLabelShadowOpacity, this.copyrightLabelShadowOffsetX, this.copyrightLabelShadowOffsetY);
        this.scrollBarOffsetY = this.offsetY + ((this.scrollBarHeight * drawOffsetScrollY) / this.originHeight);
        MyDraw.drawStrokeRect(canvas, this.scrollBarOffsetX, this.scrollBarOffsetY, this.scrollBarWidth, this.scrollBarHeight, 1711276032, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.scrollBarRadius);
        canvas.restore();
    }

    public void onDestroy() {
        clearSubBitmap();
        this.myDraw = null;
        this.helpUnit = null;
        this.appDelegate = null;
    }
}

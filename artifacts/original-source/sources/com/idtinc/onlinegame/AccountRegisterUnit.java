package com.idtinc.onlinegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AccountRegisterUnit {
    public float ACCOUNTFIRSTNAMETEXTFIELD_FONT_SIZE;
    public float ACCOUNTFIRSTNAMETEXTFIELD_HEIGHT;
    public float ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_X;
    public float ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_Y;
    public float ACCOUNTFIRSTNAMETEXTFIELD_WIDTH;
    public float ACCOUNTIDTEXTFIELD_FONT_SIZE;
    public float ACCOUNTIDTEXTFIELD_HEIGHT;
    public float ACCOUNTIDTEXTFIELD_OFFSET_X;
    public float ACCOUNTIDTEXTFIELD_OFFSET_Y;
    public float ACCOUNTIDTEXTFIELD_WIDTH;
    public float ACCOUNTLASTNAMETEXTFIELD_FONT_SIZE;
    public float ACCOUNTLASTNAMETEXTFIELD_HEIGHT;
    public float ACCOUNTLASTNAMETEXTFIELD_OFFSET_X;
    public float ACCOUNTLASTNAMETEXTFIELD_OFFSET_Y;
    public float ACCOUNTLASTNAMETEXTFIELD_WIDTH;
    public float ACCOUNTPWTEXTFIELD_FONT_SIZE;
    public float ACCOUNTPWTEXTFIELD_HEIGHT;
    public float ACCOUNTPWTEXTFIELD_OFFSET_X;
    public float ACCOUNTPWTEXTFIELD_OFFSET_Y;
    public float ACCOUNTPWTEXTFIELD_WIDTH;
    public float ACCOUNTREPWTEXTFIELD_FONT_SIZE;
    public float ACCOUNTREPWTEXTFIELD_HEIGHT;
    public float ACCOUNTREPWTEXTFIELD_OFFSET_X;
    public float ACCOUNTREPWTEXTFIELD_OFFSET_Y;
    public float ACCOUNTREPWTEXTFIELD_WIDTH;
    private float IDLABEL_HEIGHT;
    private float IDLABEL_OFFSET_X;
    private float IDLABEL_OFFSET_Y;
    private float IDLABEL_WIDTH;
    private float NAMELABEL_HEIGHT;
    private float NAMELABEL_OFFSET_X;
    private float NAMELABEL_OFFSET_Y;
    private float NAMELABEL_WIDTH;
    private float PWLABEL_HEIGHT;
    private float PWLABEL_OFFSET_X;
    private float PWLABEL_OFFSET_Y;
    private float PWLABEL_WIDTH;
    private float REPWLABEL_HEIGHT;
    private float REPWLABEL_OFFSET_X;
    private float REPWLABEL_OFFSET_Y;
    private float REPWLABEL_WIDTH;
    private float TOPTITLELABEL_HEIGHT;
    private float TOPTITLELABEL_OFFSET_X;
    private float TOPTITLELABEL_OFFSET_Y;
    private float TOPTITLELABEL_WIDTH;
    public EditText accountFirstNameEditText;
    public EditText accountIDEditText;
    public EditText accountLastNameEditText;
    public EditText accountPWEditText;
    public EditText accountRePWEditText;
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
    private short buttonClickCnt;
    private AccountLoginUnit delegate;
    private float finalHeight;
    private float finalWidth;
    public Boolean hidden;
    public int idLabelColor0;
    public int idLabelColor1;
    public int idLabelColor2;
    float idLabelFontSize;
    float idLabelOffsetX;
    float idLabelOffsetY;
    String idLabelString;
    public float idLabelStroke1Width;
    public float idLabelStroke2Width;
    Typeface idLabelTypeface;
    public int idSampleLabelColor0;
    public int idSampleLabelColor1;
    public int idSampleLabelColor2;
    float idSampleLabelFontSize;
    float idSampleLabelHeight;
    float idSampleLabelOffsetX;
    float idSampleLabelOffsetY0;
    float idSampleLabelOffsetY1;
    String idSampleLabelString0;
    String idSampleLabelString1;
    public float idSampleLabelStroke1Width;
    public float idSampleLabelStroke2Width;
    Typeface idSampleLabelTypeface;
    float idSampleLabelWidth;
    private MyDraw myDraw;
    public int nameLabelColor0;
    public int nameLabelColor1;
    public int nameLabelColor2;
    float nameLabelFontSize;
    float nameLabelOffsetX;
    float nameLabelOffsetY;
    String nameLabelString;
    public float nameLabelStroke1Width;
    public float nameLabelStroke2Width;
    Typeface nameLabelTypeface;
    public short nowStatus;
    public int pwLabelColor0;
    public int pwLabelColor1;
    public int pwLabelColor2;
    float pwLabelFontSize;
    float pwLabelOffsetX;
    float pwLabelOffsetY;
    String pwLabelString;
    public float pwLabelStroke1Width;
    public float pwLabelStroke2Width;
    Typeface pwLabelTypeface;
    public int pwSampleLabelColor0;
    public int pwSampleLabelColor1;
    public int pwSampleLabelColor2;
    float pwSampleLabelFontSize;
    float pwSampleLabelHeight;
    float pwSampleLabelOffsetX;
    float pwSampleLabelOffsetY0;
    float pwSampleLabelOffsetY1;
    String pwSampleLabelString0;
    String pwSampleLabelString1;
    public float pwSampleLabelStroke1Width;
    public float pwSampleLabelStroke2Width;
    Typeface pwSampleLabelTypeface;
    float pwSampleLabelWidth;
    public int rePwLabelColor0;
    public int rePwLabelColor1;
    public int rePwLabelColor2;
    float rePwLabelFontSize;
    float rePwLabelOffsetX;
    float rePwLabelOffsetY;
    String rePwLabelString;
    public float rePwLabelStroke1Width;
    public float rePwLabelStroke2Width;
    Typeface rePwLabelTypeface;
    public int registerButtonColor0;
    public int registerButtonColor1;
    public int registerButtonColor2;
    public int registerButtonColor3;
    public float registerButtonHeight;
    public float registerButtonOffsetX;
    public float registerButtonOffsetY;
    public float registerButtonRadius;
    private short registerButtonStatus;
    public float registerButtonStrokeWidth1;
    public float registerButtonStrokeWidth2;
    public float registerButtonStrokeWidth3;
    public int registerButtonTitleLabelColor0;
    public int registerButtonTitleLabelColor1;
    public int registerButtonTitleLabelColor2;
    public float registerButtonTitleLabelFontSize;
    public float registerButtonTitleLabelOffsetX;
    public float registerButtonTitleLabelOffsetY;
    public String registerButtonTitleLabelString;
    public float registerButtonTitleLabelStroke1Width;
    public float registerButtonTitleLabelStroke2Width;
    Typeface registerButtonTitleLabelTypeface;
    public float registerButtonWidth;
    private short selectedButtonIndex;
    public int termsOfServiceButtonColor0;
    public int termsOfServiceButtonColor1;
    public int termsOfServiceButtonColor2;
    public int termsOfServiceButtonColor3;
    public float termsOfServiceButtonHeight;
    public float termsOfServiceButtonOffsetX;
    public float termsOfServiceButtonOffsetY;
    public float termsOfServiceButtonRadius;
    private short termsOfServiceButtonStatus;
    public float termsOfServiceButtonStrokeWidth1;
    public float termsOfServiceButtonStrokeWidth2;
    public float termsOfServiceButtonStrokeWidth3;
    public int termsOfServiceButtonTitleLabelColor0;
    public int termsOfServiceButtonTitleLabelColor1;
    public int termsOfServiceButtonTitleLabelColor2;
    public float termsOfServiceButtonTitleLabelFontSize;
    public float termsOfServiceButtonTitleLabelOffsetX;
    public float termsOfServiceButtonTitleLabelOffsetY;
    public String termsOfServiceButtonTitleLabelString;
    public float termsOfServiceButtonTitleLabelStroke1Width;
    public float termsOfServiceButtonTitleLabelStroke2Width;
    Typeface termsOfServiceButtonTitleLabelTypeface;
    public float termsOfServiceButtonWidth;
    public int termsOfServiceLongLabelColor0;
    public int termsOfServiceLongLabelColor1;
    public int termsOfServiceLongLabelColor2;
    float termsOfServiceLongLabelFontSize;
    float termsOfServiceLongLabelHeight;
    float termsOfServiceLongLabelOffsetX;
    float termsOfServiceLongLabelOffsetY0;
    float termsOfServiceLongLabelOffsetY1;
    String termsOfServiceLongLabelString0;
    String termsOfServiceLongLabelString1;
    public float termsOfServiceLongLabelStroke1Width;
    public float termsOfServiceLongLabelStroke2Width;
    Typeface termsOfServiceLongLabelTypeface;
    float termsOfServiceLongLabelWidth;
    public int topTitleLabelColor0;
    public int topTitleLabelColor1;
    public int topTitleLabelColor2;
    float topTitleLabelFontSize;
    float topTitleLabelOffsetX;
    float topTitleLabelOffsetY;
    String topTitleLabelString;
    public float topTitleLabelStroke1Width;
    public float topTitleLabelStroke2Width;
    Typeface topTitleLabelTypeface;
    private float zoomRate;
    private final String REGISTER_REQUEST_URL = "http://www.idtgame.com/idtgameserver/idt/account/config/register/";
    private final String TERMS_OF_SERVICE_URL = "http://www.idtfun.com/idt/account/termsofservice/";
    private final int REGISTER_REQUEST_TIMEOUT_INTERVAL = 30000;
    private RequestAsyncTask requestAsyncTask = null;

    public AccountRegisterUnit(float _finalwidth, float _finalheight, float _zoomrate, AccountLoginUnit _delegate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.nowStatus = (short) 0;
        this.hidden = false;
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.backButtonStatus = (short) 0;
        this.registerButtonStatus = (short) 0;
        this.termsOfServiceButtonStatus = (short) 0;
        this.topTitleLabelTypeface = null;
        this.topTitleLabelFontSize = 28.0f;
        this.topTitleLabelColor0 = -1;
        this.topTitleLabelStroke1Width = 4.0f;
        this.topTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.topTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.topTitleLabelColor2 = 0;
        this.TOPTITLELABEL_OFFSET_X = 25.0f;
        this.TOPTITLELABEL_OFFSET_Y = 40.0f;
        this.TOPTITLELABEL_WIDTH = 300.0f;
        this.TOPTITLELABEL_HEIGHT = 30.0f;
        this.topTitleLabelString = "";
        this.topTitleLabelOffsetX = this.TOPTITLELABEL_OFFSET_X;
        this.topTitleLabelOffsetY = this.TOPTITLELABEL_OFFSET_Y;
        this.idLabelTypeface = null;
        this.idLabelFontSize = 17.0f;
        this.idLabelColor0 = -1;
        this.idLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.idLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.idLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.idLabelColor2 = 0;
        this.IDLABEL_OFFSET_X = 35.0f;
        this.IDLABEL_OFFSET_Y = 80.0f;
        this.IDLABEL_WIDTH = 300.0f;
        this.IDLABEL_HEIGHT = 20.0f;
        this.idLabelString = "";
        this.idLabelOffsetX = this.IDLABEL_OFFSET_X;
        this.idLabelOffsetY = this.IDLABEL_OFFSET_Y;
        this.ACCOUNTIDTEXTFIELD_OFFSET_X = 30.0f;
        this.ACCOUNTIDTEXTFIELD_OFFSET_Y = this.IDLABEL_OFFSET_Y + 22.0f;
        this.ACCOUNTIDTEXTFIELD_WIDTH = 260.0f;
        this.ACCOUNTIDTEXTFIELD_HEIGHT = 26.0f;
        this.ACCOUNTIDTEXTFIELD_FONT_SIZE = 18.0f;
        this.pwLabelTypeface = null;
        this.pwLabelFontSize = 17.0f;
        this.pwLabelColor0 = -1;
        this.pwLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.pwLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.pwLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.pwLabelColor2 = 0;
        this.PWLABEL_OFFSET_X = 35.0f;
        this.PWLABEL_OFFSET_Y = this.IDLABEL_OFFSET_Y + 60.0f;
        this.PWLABEL_WIDTH = 300.0f;
        this.PWLABEL_HEIGHT = 20.0f;
        this.pwLabelString = "";
        this.pwLabelOffsetX = this.PWLABEL_OFFSET_X;
        this.pwLabelOffsetY = this.PWLABEL_OFFSET_Y;
        this.ACCOUNTPWTEXTFIELD_OFFSET_X = 30.0f;
        this.ACCOUNTPWTEXTFIELD_OFFSET_Y = this.PWLABEL_OFFSET_Y + 22.0f;
        this.ACCOUNTPWTEXTFIELD_WIDTH = 260.0f;
        this.ACCOUNTPWTEXTFIELD_HEIGHT = 26.0f;
        this.ACCOUNTPWTEXTFIELD_FONT_SIZE = 18.0f;
        this.rePwLabelTypeface = null;
        this.rePwLabelFontSize = 17.0f;
        this.rePwLabelColor0 = -1;
        this.rePwLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.rePwLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.rePwLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.rePwLabelColor2 = 0;
        this.REPWLABEL_OFFSET_X = 35.0f;
        this.REPWLABEL_OFFSET_Y = this.PWLABEL_OFFSET_Y + 55.0f;
        this.REPWLABEL_WIDTH = 300.0f;
        this.REPWLABEL_HEIGHT = 20.0f;
        this.rePwLabelString = "";
        this.rePwLabelOffsetX = this.REPWLABEL_OFFSET_X;
        this.rePwLabelOffsetY = this.REPWLABEL_OFFSET_Y;
        this.ACCOUNTREPWTEXTFIELD_OFFSET_X = 30.0f;
        this.ACCOUNTREPWTEXTFIELD_OFFSET_Y = this.REPWLABEL_OFFSET_Y + 22.0f;
        this.ACCOUNTREPWTEXTFIELD_WIDTH = 260.0f;
        this.ACCOUNTREPWTEXTFIELD_HEIGHT = 26.0f;
        this.ACCOUNTREPWTEXTFIELD_FONT_SIZE = 18.0f;
        this.nameLabelTypeface = null;
        this.nameLabelFontSize = 17.0f;
        this.nameLabelColor0 = -1;
        this.nameLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.nameLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.nameLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.nameLabelColor2 = 0;
        this.NAMELABEL_OFFSET_X = 35.0f;
        this.NAMELABEL_OFFSET_Y = this.REPWLABEL_OFFSET_Y + 60.0f;
        this.NAMELABEL_WIDTH = 300.0f;
        this.NAMELABEL_HEIGHT = 20.0f;
        this.nameLabelString = "";
        this.nameLabelOffsetX = this.NAMELABEL_OFFSET_X;
        this.nameLabelOffsetY = this.NAMELABEL_OFFSET_Y;
        this.ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_X = 30.0f;
        this.ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_Y = this.NAMELABEL_OFFSET_Y + 22.0f;
        this.ACCOUNTFIRSTNAMETEXTFIELD_WIDTH = 260.0f;
        this.ACCOUNTFIRSTNAMETEXTFIELD_HEIGHT = 26.0f;
        this.ACCOUNTFIRSTNAMETEXTFIELD_FONT_SIZE = 18.0f;
        this.ACCOUNTLASTNAMETEXTFIELD_OFFSET_X = 30.0f;
        this.ACCOUNTLASTNAMETEXTFIELD_OFFSET_Y = this.NAMELABEL_OFFSET_Y + 22.0f;
        this.ACCOUNTLASTNAMETEXTFIELD_WIDTH = 128.0f;
        this.ACCOUNTLASTNAMETEXTFIELD_HEIGHT = 26.0f;
        this.ACCOUNTLASTNAMETEXTFIELD_FONT_SIZE = 18.0f;
        this.idSampleLabelTypeface = null;
        this.idSampleLabelFontSize = 17.0f;
        this.idSampleLabelColor0 = -6710887;
        this.idSampleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.idSampleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.idSampleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.idSampleLabelColor2 = 0;
        this.idSampleLabelString0 = "";
        this.idSampleLabelString1 = "";
        this.idSampleLabelOffsetX = 30.0f;
        this.idSampleLabelOffsetY0 = 310.0f;
        this.idSampleLabelOffsetY1 = 310.0f;
        this.idSampleLabelWidth = 260.0f;
        this.idSampleLabelHeight = 30.0f;
        this.pwSampleLabelTypeface = null;
        this.pwSampleLabelFontSize = 17.0f;
        this.pwSampleLabelColor0 = -6710887;
        this.pwSampleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.pwSampleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.pwSampleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.pwSampleLabelColor2 = 0;
        this.pwSampleLabelString0 = "";
        this.pwSampleLabelString1 = "";
        this.pwSampleLabelOffsetX = 30.0f;
        this.pwSampleLabelOffsetY0 = 310.0f;
        this.pwSampleLabelOffsetY1 = 310.0f;
        this.pwSampleLabelWidth = 260.0f;
        this.pwSampleLabelHeight = 30.0f;
        this.termsOfServiceLongLabelTypeface = null;
        this.termsOfServiceLongLabelFontSize = 17.0f;
        this.termsOfServiceLongLabelColor0 = -6710887;
        this.termsOfServiceLongLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.termsOfServiceLongLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.termsOfServiceLongLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.termsOfServiceLongLabelColor2 = 0;
        this.termsOfServiceLongLabelString0 = "";
        this.termsOfServiceLongLabelString1 = "";
        this.termsOfServiceLongLabelOffsetX = 30.0f;
        this.termsOfServiceLongLabelOffsetY0 = 310.0f;
        this.termsOfServiceLongLabelOffsetY1 = 310.0f;
        this.termsOfServiceLongLabelWidth = 260.0f;
        this.termsOfServiceLongLabelHeight = 30.0f;
        this.termsOfServiceButtonWidth = 120.0f;
        this.termsOfServiceButtonHeight = 32.0f;
        this.termsOfServiceButtonOffsetX = 390.0f;
        this.termsOfServiceButtonOffsetY = -70.0f;
        this.termsOfServiceButtonColor0 = -227838;
        this.termsOfServiceButtonStrokeWidth1 = 2.0f;
        this.termsOfServiceButtonColor1 = -16;
        this.termsOfServiceButtonStrokeWidth2 = 2.0f;
        this.termsOfServiceButtonColor2 = -7576502;
        this.termsOfServiceButtonStrokeWidth3 = 1.0f;
        this.termsOfServiceButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.termsOfServiceButtonRadius = 10.0f;
        this.termsOfServiceButtonTitleLabelString = "";
        this.termsOfServiceButtonTitleLabelFontSize = 20.0f;
        this.termsOfServiceButtonTitleLabelColor0 = -16;
        this.termsOfServiceButtonTitleLabelStroke1Width = 3.0f;
        this.termsOfServiceButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.termsOfServiceButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.termsOfServiceButtonTitleLabelColor2 = 0;
        this.termsOfServiceButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.termsOfServiceButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backButtonWidth = 120.0f;
        this.backButtonHeight = 32.0f;
        this.backButtonOffsetX = 390.0f;
        this.backButtonOffsetY = -70.0f;
        this.backButtonColor0 = -227838;
        this.backButtonStrokeWidth1 = 2.0f;
        this.backButtonColor1 = -16;
        this.backButtonStrokeWidth2 = 2.0f;
        this.backButtonColor2 = -7576502;
        this.backButtonStrokeWidth3 = 1.0f;
        this.backButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonRadius = 10.0f;
        this.backButtonTitleLabelString = "";
        this.backButtonTitleLabelFontSize = 20.0f;
        this.backButtonTitleLabelColor0 = -16;
        this.backButtonTitleLabelStroke1Width = 3.0f;
        this.backButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelColor2 = 0;
        this.backButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.registerButtonWidth = 120.0f;
        this.registerButtonHeight = 32.0f;
        this.registerButtonOffsetX = 390.0f;
        this.registerButtonOffsetY = -70.0f;
        this.registerButtonColor0 = -227838;
        this.registerButtonStrokeWidth1 = 2.0f;
        this.registerButtonColor1 = -16;
        this.registerButtonStrokeWidth2 = 2.0f;
        this.registerButtonColor2 = -7576502;
        this.registerButtonStrokeWidth3 = 1.0f;
        this.registerButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.registerButtonRadius = 10.0f;
        this.registerButtonTitleLabelString = "";
        this.registerButtonTitleLabelFontSize = 20.0f;
        this.registerButtonTitleLabelColor0 = -16;
        this.registerButtonTitleLabelStroke1Width = 3.0f;
        this.registerButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.registerButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.registerButtonTitleLabelColor2 = 0;
        this.registerButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.registerButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = null;
        this.delegate = null;
        this.appDelegate = _appDelegate;
        this.delegate = _delegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("zh-CN")) {
        }
        this.nowStatus = (short) 0;
        this.hidden = false;
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.backButtonStatus = (short) 0;
        this.registerButtonStatus = (short) 0;
        this.termsOfServiceButtonStatus = (short) 0;
        Paint newPaint = new Paint(257);
        this.topTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.topTitleLabelFontSize = 28.0f * this.zoomRate;
        this.topTitleLabelColor0 = -1;
        this.topTitleLabelStroke1Width = 4.0f * this.zoomRate;
        this.topTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.topTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.topTitleLabelColor2 = 0;
        newPaint.setTypeface(this.topTitleLabelTypeface);
        newPaint.setTextSize(this.topTitleLabelFontSize);
        this.TOPTITLELABEL_OFFSET_X = 25.0f * this.zoomRate;
        this.TOPTITLELABEL_OFFSET_Y = 40.0f * this.zoomRate;
        this.TOPTITLELABEL_WIDTH = 300.0f * this.zoomRate;
        this.TOPTITLELABEL_HEIGHT = 30.0f * this.zoomRate;
        this.topTitleLabelString = " " + this.appDelegate.getResources().getString(R.string.Registration);
        this.topTitleLabelOffsetX = this.TOPTITLELABEL_OFFSET_X;
        this.topTitleLabelOffsetY = this.TOPTITLELABEL_OFFSET_Y + this.topTitleLabelFontSize;
        this.idLabelTypeface = Typeface.DEFAULT_BOLD;
        this.idLabelFontSize = 17.0f * this.zoomRate;
        this.idLabelColor0 = FluctConstants.FRAME_BASE_COLOR;
        this.idLabelStroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.idLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.idLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.idLabelColor2 = 0;
        newPaint.setTypeface(this.idLabelTypeface);
        newPaint.setTextSize(this.idLabelFontSize);
        this.IDLABEL_OFFSET_X = 35.0f * this.zoomRate;
        this.IDLABEL_OFFSET_Y = 80.0f * this.zoomRate;
        this.IDLABEL_WIDTH = 300.0f * this.zoomRate;
        this.IDLABEL_HEIGHT = 20.0f * this.zoomRate;
        this.idLabelString = this.appDelegate.getResources().getString(R.string.ID);
        this.idLabelOffsetX = this.IDLABEL_OFFSET_X;
        this.idLabelOffsetY = this.IDLABEL_OFFSET_Y + this.idLabelFontSize;
        this.ACCOUNTIDTEXTFIELD_OFFSET_X = 30.0f * this.zoomRate;
        this.ACCOUNTIDTEXTFIELD_OFFSET_Y = this.IDLABEL_OFFSET_Y + (22.0f * this.zoomRate);
        this.ACCOUNTIDTEXTFIELD_WIDTH = 260.0f * this.zoomRate;
        this.ACCOUNTIDTEXTFIELD_HEIGHT = 38.0f * this.zoomRate;
        this.ACCOUNTIDTEXTFIELD_FONT_SIZE = 18.0f;
        this.pwLabelTypeface = Typeface.DEFAULT_BOLD;
        this.pwLabelFontSize = 17.0f * this.zoomRate;
        this.pwLabelColor0 = FluctConstants.FRAME_BASE_COLOR;
        this.pwLabelStroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.pwLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.pwLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.pwLabelColor2 = 0;
        newPaint.setTypeface(this.pwLabelTypeface);
        newPaint.setTextSize(this.pwLabelFontSize);
        this.PWLABEL_OFFSET_X = 35.0f * this.zoomRate;
        this.PWLABEL_OFFSET_Y = this.IDLABEL_OFFSET_Y + (60.0f * this.zoomRate);
        this.PWLABEL_WIDTH = 300.0f * this.zoomRate;
        this.PWLABEL_HEIGHT = 20.0f * this.zoomRate;
        this.pwLabelString = this.appDelegate.getResources().getString(R.string.Password);
        this.pwLabelOffsetX = this.PWLABEL_OFFSET_X;
        this.pwLabelOffsetY = this.PWLABEL_OFFSET_Y + this.pwLabelFontSize;
        this.ACCOUNTPWTEXTFIELD_OFFSET_X = 30.0f * this.zoomRate;
        this.ACCOUNTPWTEXTFIELD_OFFSET_Y = this.PWLABEL_OFFSET_Y + (22.0f * this.zoomRate);
        this.ACCOUNTPWTEXTFIELD_WIDTH = 260.0f * this.zoomRate;
        this.ACCOUNTPWTEXTFIELD_HEIGHT = 38.0f * this.zoomRate;
        this.ACCOUNTPWTEXTFIELD_FONT_SIZE = 18.0f;
        this.rePwLabelTypeface = Typeface.DEFAULT_BOLD;
        this.rePwLabelFontSize = 17.0f * this.zoomRate;
        this.rePwLabelColor0 = FluctConstants.FRAME_BASE_COLOR;
        this.rePwLabelStroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.rePwLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.rePwLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.rePwLabelColor2 = 0;
        newPaint.setTypeface(this.rePwLabelTypeface);
        newPaint.setTextSize(this.rePwLabelFontSize);
        this.REPWLABEL_OFFSET_X = 35.0f * this.zoomRate;
        this.REPWLABEL_OFFSET_Y = this.PWLABEL_OFFSET_Y + (55.0f * this.zoomRate);
        this.REPWLABEL_WIDTH = 300.0f * this.zoomRate;
        this.REPWLABEL_HEIGHT = 20.0f * this.zoomRate;
        this.rePwLabelString = this.appDelegate.getResources().getString(R.string.ConfirmYourPassword);
        this.rePwLabelOffsetX = this.REPWLABEL_OFFSET_X;
        this.rePwLabelOffsetY = this.REPWLABEL_OFFSET_Y + this.rePwLabelFontSize;
        this.ACCOUNTREPWTEXTFIELD_OFFSET_X = 30.0f * this.zoomRate;
        this.ACCOUNTREPWTEXTFIELD_OFFSET_Y = this.REPWLABEL_OFFSET_Y + (22.0f * this.zoomRate);
        this.ACCOUNTREPWTEXTFIELD_WIDTH = 260.0f * this.zoomRate;
        this.ACCOUNTREPWTEXTFIELD_HEIGHT = 38.0f * this.zoomRate;
        this.ACCOUNTREPWTEXTFIELD_FONT_SIZE = 18.0f;
        this.nameLabelTypeface = Typeface.DEFAULT_BOLD;
        this.nameLabelFontSize = 17.0f * this.zoomRate;
        this.nameLabelColor0 = FluctConstants.FRAME_BASE_COLOR;
        this.nameLabelStroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.nameLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.nameLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.nameLabelColor2 = 0;
        newPaint.setTypeface(this.nameLabelTypeface);
        newPaint.setTextSize(this.nameLabelFontSize);
        this.NAMELABEL_OFFSET_X = 35.0f * this.zoomRate;
        this.NAMELABEL_OFFSET_Y = this.REPWLABEL_OFFSET_Y + (60.0f * this.zoomRate);
        this.NAMELABEL_WIDTH = 300.0f * this.zoomRate;
        this.NAMELABEL_HEIGHT = 20.0f * this.zoomRate;
        this.nameLabelString = this.appDelegate.getResources().getString(R.string.Name);
        this.nameLabelOffsetX = this.NAMELABEL_OFFSET_X;
        this.nameLabelOffsetY = this.NAMELABEL_OFFSET_Y + this.nameLabelFontSize;
        this.ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_Y = this.NAMELABEL_OFFSET_Y + (22.0f * this.zoomRate);
        this.ACCOUNTFIRSTNAMETEXTFIELD_WIDTH = 128.0f * this.zoomRate;
        this.ACCOUNTFIRSTNAMETEXTFIELD_HEIGHT = 38.0f * this.zoomRate;
        this.ACCOUNTFIRSTNAMETEXTFIELD_FONT_SIZE = 18.0f;
        this.ACCOUNTLASTNAMETEXTFIELD_OFFSET_Y = this.NAMELABEL_OFFSET_Y + (22.0f * this.zoomRate);
        this.ACCOUNTLASTNAMETEXTFIELD_WIDTH = 128.0f * this.zoomRate;
        this.ACCOUNTLASTNAMETEXTFIELD_HEIGHT = 38.0f * this.zoomRate;
        this.ACCOUNTLASTNAMETEXTFIELD_FONT_SIZE = 18.0f;
        if (languageString.equals("ja-JP") || languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
            this.ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_X = 162.0f * this.zoomRate;
            this.ACCOUNTLASTNAMETEXTFIELD_OFFSET_X = 30.0f * this.zoomRate;
        } else {
            this.ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_X = 30.0f * this.zoomRate;
            this.ACCOUNTLASTNAMETEXTFIELD_OFFSET_X = 162.0f * this.zoomRate;
        }
        this.accountIDEditText = new EditText(this.appDelegate);
        this.accountIDEditText.getBackground().setAlpha(128);
        this.accountIDEditText.setHint(this.appDelegate.getResources().getString(R.string.iDTID));
        this.accountIDEditText.setTextSize(this.ACCOUNTIDTEXTFIELD_FONT_SIZE);
        this.accountIDEditText.setTextColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.accountIDEditText.setTypeface(Typeface.DEFAULT_BOLD, 1);
        this.accountIDEditText.setVisibility(8);
        this.accountIDEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        this.accountIDEditText.setSingleLine(true);
        this.accountIDEditText.setImeOptions(6);
        this.accountIDEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId != 0 && actionId != 6) {
                    return false;
                }
                AccountRegisterUnit.this.hiddenSoftInput(_textView);
                return true;
            }
        });
        this.accountPWEditText = new EditText(this.appDelegate);
        this.accountPWEditText.getBackground().setAlpha(128);
        this.accountPWEditText.setHint(this.appDelegate.getResources().getString(R.string.Password));
        this.accountPWEditText.setTextSize(this.ACCOUNTPWTEXTFIELD_FONT_SIZE);
        this.accountPWEditText.setTextColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.accountPWEditText.setTypeface(Typeface.DEFAULT_BOLD, 1);
        this.accountPWEditText.setVisibility(8);
        this.accountPWEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        this.accountPWEditText.setSingleLine(true);
        this.accountPWEditText.setInputType(128);
        this.accountPWEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.accountPWEditText.setImeOptions(6);
        this.accountPWEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId != 0 && actionId != 6) {
                    return false;
                }
                AccountRegisterUnit.this.hiddenSoftInput(_textView);
                return true;
            }
        });
        this.accountRePWEditText = new EditText(this.appDelegate);
        this.accountRePWEditText.getBackground().setAlpha(128);
        this.accountRePWEditText.setHint(this.appDelegate.getResources().getString(R.string.Password));
        this.accountRePWEditText.setTextSize(this.ACCOUNTREPWTEXTFIELD_FONT_SIZE);
        this.accountRePWEditText.setTextColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.accountRePWEditText.setTypeface(Typeface.DEFAULT_BOLD, 1);
        this.accountRePWEditText.setVisibility(8);
        this.accountRePWEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        this.accountRePWEditText.setSingleLine(true);
        this.accountRePWEditText.setInputType(128);
        this.accountRePWEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.accountRePWEditText.setImeOptions(6);
        this.accountRePWEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.3
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId != 0 && actionId != 6) {
                    return false;
                }
                AccountRegisterUnit.this.hiddenSoftInput(_textView);
                return true;
            }
        });
        this.accountFirstNameEditText = new EditText(this.appDelegate);
        this.accountFirstNameEditText.getBackground().setAlpha(128);
        this.accountFirstNameEditText.setHint(this.appDelegate.getResources().getString(R.string.FirstName));
        this.accountFirstNameEditText.setTextSize(this.ACCOUNTFIRSTNAMETEXTFIELD_FONT_SIZE);
        this.accountFirstNameEditText.setTextColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.accountFirstNameEditText.setTypeface(Typeface.DEFAULT_BOLD, 1);
        this.accountFirstNameEditText.setVisibility(8);
        this.accountFirstNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        this.accountFirstNameEditText.setSingleLine(true);
        this.accountFirstNameEditText.setImeOptions(6);
        this.accountFirstNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.4
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId != 0 && actionId != 6) {
                    return false;
                }
                AccountRegisterUnit.this.hiddenSoftInput(_textView);
                return true;
            }
        });
        this.accountLastNameEditText = new EditText(this.appDelegate);
        this.accountLastNameEditText.getBackground().setAlpha(128);
        this.accountLastNameEditText.setHint(this.appDelegate.getResources().getString(R.string.LastName));
        this.accountLastNameEditText.setTextSize(this.ACCOUNTLASTNAMETEXTFIELD_FONT_SIZE);
        this.accountLastNameEditText.setTextColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.accountLastNameEditText.setTypeface(Typeface.DEFAULT_BOLD, 1);
        this.accountLastNameEditText.setVisibility(8);
        this.accountLastNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        this.accountLastNameEditText.setSingleLine(true);
        this.accountLastNameEditText.setImeOptions(6);
        this.accountLastNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.5
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId != 0 && actionId != 6) {
                    return false;
                }
                AccountRegisterUnit.this.hiddenSoftInput(_textView);
                return true;
            }
        });
        this.idSampleLabelTypeface = Typeface.DEFAULT_BOLD;
        this.idSampleLabelFontSize = 10.0f * this.zoomRate;
        this.idSampleLabelColor0 = -6710887;
        this.idSampleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.idSampleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.idSampleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.idSampleLabelColor2 = 0;
        this.idSampleLabelString0 = this.appDelegate.getResources().getString(R.string.IDSample0);
        this.idSampleLabelString1 = this.appDelegate.getResources().getString(R.string.IDSample1);
        this.idSampleLabelOffsetX = 160.0f * this.zoomRate;
        this.idSampleLabelOffsetY0 = (this.idLabelOffsetY - (18.0f * this.zoomRate)) + this.idSampleLabelFontSize;
        this.idSampleLabelOffsetY1 = this.idSampleLabelOffsetY0 + (10.0f * this.zoomRate);
        this.idSampleLabelWidth = 260.0f * this.zoomRate;
        this.idSampleLabelHeight = 30.0f * this.zoomRate;
        this.pwSampleLabelTypeface = Typeface.DEFAULT_BOLD;
        this.pwSampleLabelFontSize = 10.0f * this.zoomRate;
        this.pwSampleLabelColor0 = -6710887;
        this.pwSampleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.pwSampleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.pwSampleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.pwSampleLabelColor2 = 0;
        this.pwSampleLabelString0 = this.appDelegate.getResources().getString(R.string.PasswordSample0);
        this.pwSampleLabelString1 = this.appDelegate.getResources().getString(R.string.PasswordSample1);
        this.pwSampleLabelOffsetX = 160.0f * this.zoomRate;
        this.pwSampleLabelOffsetY0 = (this.pwLabelOffsetY - (18.0f * this.zoomRate)) + this.idSampleLabelFontSize;
        this.pwSampleLabelOffsetY1 = this.pwSampleLabelOffsetY0 + (10.0f * this.zoomRate);
        this.pwSampleLabelWidth = 260.0f * this.zoomRate;
        this.pwSampleLabelHeight = 30.0f * this.zoomRate;
        this.termsOfServiceLongLabelTypeface = Typeface.DEFAULT_BOLD;
        this.termsOfServiceLongLabelFontSize = 13.0f * this.zoomRate;
        this.termsOfServiceLongLabelColor0 = -6710887;
        this.termsOfServiceLongLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.termsOfServiceLongLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.termsOfServiceLongLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.termsOfServiceLongLabelColor2 = 0;
        this.termsOfServiceLongLabelString0 = this.appDelegate.getResources().getString(R.string.PleaseDontClickRegister0);
        this.termsOfServiceLongLabelString1 = this.appDelegate.getResources().getString(R.string.PleaseDontClickRegister1);
        this.termsOfServiceLongLabelOffsetX = 30.0f * this.zoomRate;
        this.termsOfServiceLongLabelOffsetY0 = (325.0f * this.zoomRate) + this.termsOfServiceLongLabelFontSize;
        this.termsOfServiceLongLabelOffsetY1 = this.termsOfServiceLongLabelOffsetY0 + (20.0f * this.zoomRate);
        this.termsOfServiceLongLabelWidth = 260.0f * this.zoomRate;
        this.termsOfServiceLongLabelHeight = 30.0f * this.zoomRate;
        if (languageString.equals("ja-JP")) {
            this.termsOfServiceButtonOffsetX = 223.0f * this.zoomRate;
            this.termsOfServiceButtonOffsetY = this.termsOfServiceLongLabelOffsetY0 - (12.0f * this.zoomRate);
            this.termsOfServiceButtonWidth = 57.0f * this.zoomRate;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
            this.termsOfServiceButtonOffsetX = 117.0f * this.zoomRate;
            this.termsOfServiceButtonOffsetY = this.termsOfServiceLongLabelOffsetY0 - (12.0f * this.zoomRate);
            this.termsOfServiceButtonWidth = 56.0f * this.zoomRate;
        } else {
            this.termsOfServiceButtonOffsetX = 155.0f * this.zoomRate;
            this.termsOfServiceButtonOffsetY = this.termsOfServiceLongLabelOffsetY1 - (12.0f * this.zoomRate);
            this.termsOfServiceButtonWidth = 101.0f * this.zoomRate;
        }
        this.termsOfServiceButtonHeight = 14.0f * this.zoomRate;
        this.termsOfServiceButtonColor0 = -16;
        this.termsOfServiceButtonStrokeWidth1 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.termsOfServiceButtonColor1 = 0;
        this.termsOfServiceButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.termsOfServiceButtonColor2 = 0;
        this.termsOfServiceButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.termsOfServiceButtonColor3 = 0;
        this.termsOfServiceButtonRadius = 3.0f * this.zoomRate;
        this.termsOfServiceButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.TermsOfService);
        this.termsOfServiceButtonTitleLabelTypeface = Typeface.DEFAULT_BOLD;
        this.termsOfServiceButtonTitleLabelFontSize = 13.0f * this.zoomRate;
        this.termsOfServiceButtonTitleLabelColor0 = -16776961;
        this.termsOfServiceButtonTitleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.termsOfServiceButtonTitleLabelColor1 = 0;
        this.termsOfServiceButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.termsOfServiceButtonTitleLabelColor2 = 0;
        this.termsOfServiceButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.termsOfServiceButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        newPaint.setTypeface(this.termsOfServiceButtonTitleLabelTypeface);
        newPaint.setTextSize(this.termsOfServiceButtonTitleLabelFontSize);
        this.termsOfServiceButtonTitleLabelOffsetX = this.termsOfServiceButtonOffsetX + ((this.termsOfServiceButtonWidth - newPaint.measureText(this.termsOfServiceButtonTitleLabelString)) / 2.0f);
        this.termsOfServiceButtonTitleLabelOffsetY = (this.termsOfServiceButtonOffsetY + this.termsOfServiceButtonHeight) - (2.0f * this.zoomRate);
        this.backButtonWidth = 120.0f * this.zoomRate;
        this.backButtonHeight = 32.0f * this.zoomRate;
        this.backButtonOffsetX = (90.0f * this.zoomRate) - (this.backButtonWidth / 2.0f);
        this.backButtonOffsetY = (390.0f * this.zoomRate) - (this.backButtonHeight / 2.0f);
        this.backButtonColor0 = -227838;
        this.backButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.backButtonColor1 = -16;
        this.backButtonStrokeWidth2 = 2.0f * this.zoomRate;
        this.backButtonColor2 = -7576502;
        this.backButtonStrokeWidth3 = 1.0f * this.zoomRate;
        this.backButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonRadius = 10.0f * this.zoomRate;
        this.backButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.Back);
        this.backButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.backButtonTitleLabelFontSize = 20.0f * this.zoomRate;
        this.backButtonTitleLabelColor0 = -16;
        this.backButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.backButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backButtonTitleLabelColor2 = 0;
        this.backButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        newPaint.setTypeface(this.backButtonTitleLabelTypeface);
        newPaint.setTextSize(this.backButtonTitleLabelFontSize);
        this.backButtonTitleLabelOffsetX = this.backButtonOffsetX + ((this.backButtonWidth - newPaint.measureText(this.backButtonTitleLabelString)) / 2.0f);
        this.backButtonTitleLabelOffsetY = (this.backButtonOffsetY + this.backButtonHeight) - (this.backButtonTitleLabelFontSize * 0.42f);
        this.registerButtonWidth = 120.0f * this.zoomRate;
        this.registerButtonHeight = 32.0f * this.zoomRate;
        this.registerButtonOffsetX = (230.0f * this.zoomRate) - (this.registerButtonWidth / 2.0f);
        this.registerButtonOffsetY = (390.0f * this.zoomRate) - (this.registerButtonHeight / 2.0f);
        this.registerButtonColor0 = -227838;
        this.registerButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.registerButtonColor1 = -16;
        this.registerButtonStrokeWidth2 = 2.0f * this.zoomRate;
        this.registerButtonColor2 = -7576502;
        this.registerButtonStrokeWidth3 = 1.0f * this.zoomRate;
        this.registerButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.registerButtonRadius = 10.0f * this.zoomRate;
        this.registerButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.Register);
        this.registerButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.registerButtonTitleLabelFontSize = 20.0f * this.zoomRate;
        this.registerButtonTitleLabelColor0 = -16;
        this.registerButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.registerButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.registerButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.registerButtonTitleLabelColor2 = 0;
        this.registerButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.registerButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        newPaint.setTypeface(this.registerButtonTitleLabelTypeface);
        newPaint.setTextSize(this.registerButtonTitleLabelFontSize);
        this.registerButtonTitleLabelOffsetX = this.registerButtonOffsetX + ((this.registerButtonWidth - newPaint.measureText(this.registerButtonTitleLabelString)) / 2.0f);
        this.registerButtonTitleLabelOffsetY = (this.registerButtonOffsetY + this.registerButtonHeight) - (this.registerButtonTitleLabelFontSize * 0.42f);
        clearRequestAsyncTask();
        this.myDraw = new MyDraw();
    }

    public void clear() {
        this.nowStatus = (short) 0;
        clearRequestAsyncTask();
        hiddenSoftInput(this.accountIDEditText);
        this.accountIDEditText.setText("");
        hiddenSoftInput(this.accountPWEditText);
        this.accountPWEditText.setText("");
        hiddenSoftInput(this.accountRePWEditText);
        this.accountRePWEditText.setText("");
        hiddenSoftInput(this.accountLastNameEditText);
        this.accountLastNameEditText.setText("");
        hiddenSoftInput(this.accountFirstNameEditText);
        this.accountFirstNameEditText.setText("");
    }

    public void reset() {
        this.hidden = true;
        clear();
        this.backButtonStatus = (short) 0;
        this.registerButtonStatus = (short) 0;
        this.termsOfServiceButtonStatus = (short) 0;
        if (this.delegate != null && this.delegate.onlineGameViewController != null) {
            this.delegate.onlineGameViewController.stopLoading();
        }
    }

    public void close() {
        this.hidden = true;
        reset();
        this.nowStatus = (short) -1;
    }

    public void open() {
        this.nowStatus = (short) 0;
        reset();
        this.hidden = false;
    }

    public void doRegister() {
        RequestAsyncTask requestAsyncTask = null;
        if (this.appDelegate != null) {
            clearRequestAsyncTask();
            if (this.delegate != null && this.delegate.onlineGameViewController != null) {
                this.delegate.onlineGameViewController.stopLoading();
            }
            hiddenSoftInput(this.accountIDEditText);
            hiddenSoftInput(this.accountPWEditText);
            hiddenSoftInput(this.accountRePWEditText);
            hiddenSoftInput(this.accountLastNameEditText);
            hiddenSoftInput(this.accountFirstNameEditText);
            if (!this.appDelegate.checkInterNet()) {
                this.appDelegate.showNoInternetAlertDialog();
                return;
            }
            if (this.accountIDEditText.getText().toString().length() <= 0) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.PleaseInputID)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.accountPWEditText.getText().toString().length() <= 0) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.PleaseInputPassword)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.accountRePWEditText.getText().toString().length() <= 0) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.PleaseConfirmYourPassword)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.accountLastNameEditText.getText().toString().length() <= 0) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.PleaseInputLastName)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.accountFirstNameEditText.getText().toString().length() <= 0) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.PleaseInputFirstName)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (!this.appDelegate.doValidateEmail(this.accountIDEditText.getText().toString()).booleanValue()) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.InvalidIDFormat)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.accountPWEditText.getText().toString().length() < 8) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.InvalidPasswordFormat)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (!this.accountPWEditText.getText().toString().equals(this.accountRePWEditText.getText().toString())) {
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ThesePasswordsDontMatch)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                this.delegate.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.Registering));
            }
            this.requestAsyncTask = new RequestAsyncTask(this, requestAsyncTask);
            this.requestAsyncTask.execute("http://www.idtgame.com/idtgameserver/idt/account/config/register/");
        }
    }

    public void clearRequestAsyncTask() {
        if (this.requestAsyncTask != null) {
            if (!this.requestAsyncTask.isCancelled()) {
                this.requestAsyncTask.cancel(true);
            }
            this.requestAsyncTask = null;
        }
    }

    private class RequestAsyncTask extends AsyncTask<String, Integer, JSONObject> {
        private RequestAsyncTask() {
        }

        /* synthetic */ RequestAsyncTask(AccountRegisterUnit accountRegisterUnit, RequestAsyncTask requestAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(String... params) throws IOException {
            if (AccountRegisterUnit.this.appDelegate == null) {
                return null;
            }
            JSONObject json = null;
            try {
                URL url = new URL("http://www.idtgame.com/idtgameserver/idt/account/config/register/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestProperty(MIME.CONTENT_TYPE, "text/plain; charset=utf-8");
                conn.setRequestProperty("account", AccountRegisterUnit.this.accountIDEditText.getText().toString());
                conn.setRequestProperty("password", AccountRegisterUnit.this.accountPWEditText.getText().toString());
                conn.setRequestProperty("mail_address", AccountRegisterUnit.this.accountIDEditText.getText().toString());
                String languageString = AccountRegisterUnit.this.appDelegate.getLocaleLanguage();
                if (languageString.equals("ja-JP") || languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
                    conn.setRequestProperty("name", String.valueOf(URLEncoder.encode(AccountRegisterUnit.this.accountLastNameEditText.getText().toString(), "UTF-8")) + " " + URLEncoder.encode(AccountRegisterUnit.this.accountFirstNameEditText.getText().toString(), "UTF-8"));
                } else {
                    conn.setRequestProperty("name", String.valueOf(URLEncoder.encode(AccountRegisterUnit.this.accountFirstNameEditText.getText().toString(), "UTF-8")) + " " + URLEncoder.encode(AccountRegisterUnit.this.accountLastNameEditText.getText().toString(), "UTF-8"));
                }
                conn.setRequestProperty("last_name", URLEncoder.encode(AccountRegisterUnit.this.accountLastNameEditText.getText().toString(), "UTF-8"));
                conn.setRequestProperty("first_name", URLEncoder.encode(AccountRegisterUnit.this.accountFirstNameEditText.getText().toString(), "UTF-8"));
                conn.setRequestMethod("POST");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                if (jsonString != null) {
                    if (jsonString.length() > 2) {
                        String firstString = jsonString.substring(0, 1);
                        if (firstString.equals("[")) {
                            jsonString = jsonString.substring(1, jsonString.length());
                        }
                        String lastString = jsonString.substring(jsonString.length() - 1, jsonString.length());
                        if (lastString.equals("]")) {
                            jsonString = jsonString.substring(0, jsonString.length() - 1);
                        }
                    }
                    try {
                        json = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                reader.close();
                return json;
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
                return json;
            } catch (IOException e3) {
                return json;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate((Object[]) progress);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(JSONObject _json) throws JSONException {
            String successString = "-1";
            if (_json != null) {
                try {
                    successString = _json.getString("success");
                } catch (JSONException e) {
                    successString = "-1";
                }
            }
            AccountRegisterUnit.this.doLoginResponse(successString);
            super.onPostExecute((RequestAsyncTask) _json);
        }
    }

    public void doLoginResponse(String _successString) {
        if (this.delegate != null && this.delegate.onlineGameViewController != null) {
            this.delegate.onlineGameViewController.stopLoading();
        }
        if (_successString.equals("0")) {
            if (this.appDelegate != null) {
                if (this.accountIDEditText.getText().toString().length() > 0 && this.appDelegate.set_idt_account_id(this.accountIDEditText.getText().toString()).booleanValue() && this.accountPWEditText.getText().toString().length() > 0 && this.appDelegate.set_idt_account_password(this.accountPWEditText.getText().toString()).booleanValue()) {
                    if (this.delegate != null) {
                        this.delegate.openWithAutoLogIn(true);
                    }
                    clearRequestAsyncTask();
                    close();
                    return;
                }
                if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                }
            }
        } else if (_successString.equals("99")) {
            if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.RegistrationFailedPleaseTryAgain)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (_successString.equals("100")) {
            if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.InvalidIDFormat)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (_successString.equals("101")) {
            if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.InvalidPasswordFormat)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (_successString.equals("900")) {
            if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.TheIDYouEnteredHasAlreadyBeenUsed)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (this.delegate.onlineGameViewController != null && this.delegate.onlineGameViewController.appMainActivity != null) {
            new AlertDialog.Builder(this.delegate.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
        }
        clearRequestAsyncTask();
    }

    public void makeNewText(String _newText) {
        Toast.makeText(this.appDelegate, "successString:" + _newText, 0).show();
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (event.getAction() == 0) {
            this.selectedButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (event.getX() > this.backButtonOffsetX && event.getX() < this.backButtonOffsetX + this.backButtonWidth && event.getY() >= this.backButtonOffsetY && event.getY() <= this.backButtonOffsetY + this.backButtonHeight) {
                this.selectedButtonIndex = (short) 100;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.6
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountRegisterUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.registerButtonOffsetX && event.getX() < this.registerButtonOffsetX + this.registerButtonWidth && event.getY() >= this.registerButtonOffsetY && event.getY() <= this.registerButtonOffsetY + this.registerButtonHeight) {
                this.selectedButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.7
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountRegisterUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.termsOfServiceButtonOffsetX && event.getX() < this.termsOfServiceButtonOffsetX + this.termsOfServiceButtonWidth && event.getY() >= this.termsOfServiceButtonOffsetY && event.getY() <= this.termsOfServiceButtonOffsetY + this.termsOfServiceButtonHeight) {
                this.selectedButtonIndex = (short) 10;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.onlinegame.AccountRegisterUnit.8
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountRegisterUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        }
        return false;
    }

    public void doClick() {
        String urlString;
        if (this.selectedButtonIndex == 100) {
            if (!this.hidden.booleanValue()) {
                this.appDelegate.doSoundPoolPlay(2);
            }
            close();
        } else if (this.selectedButtonIndex == 0) {
            if (!this.hidden.booleanValue()) {
                this.appDelegate.doSoundPoolPlay(1);
            }
            doRegister();
        } else if (this.selectedButtonIndex == 10) {
            if (!this.hidden.booleanValue()) {
                this.appDelegate.doSoundPoolPlay(1);
            }
            if (this.appDelegate != null) {
                String languageString = this.appDelegate.getLocaleLanguage();
                if (languageString.equals("ja-JP")) {
                    urlString = "http://www.idtfun.com/idt/account/termsofservice/termsofservice_ja.html";
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    urlString = "http://www.idtfun.com/idt/account/termsofservice/termsofservice_tw.html";
                } else if (languageString.equals("zh-CN")) {
                    urlString = "http://www.idtfun.com/idt/account/termsofservice/termsofservice_cn.html";
                } else {
                    urlString = "http://www.idtfun.com/idt/account/termsofservice/termsofservice_en.html";
                }
                this.appDelegate.goToTermsOfService(urlString);
            }
        }
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        new Paint();
        MyDraw.drawStrokeText(canvas, this.topTitleLabelOffsetX, this.topTitleLabelOffsetY, this.topTitleLabelTypeface, this.topTitleLabelString, this.topTitleLabelFontSize, this.topTitleLabelColor0, this.topTitleLabelStroke1Width, this.topTitleLabelColor1, this.topTitleLabelStroke2Width, this.topTitleLabelColor2);
        MyDraw.drawStrokeText(canvas, this.idLabelOffsetX, this.idLabelOffsetY, this.idLabelTypeface, this.idLabelString, this.idLabelFontSize, this.idLabelColor0, this.idLabelStroke1Width, this.idLabelColor1, this.idLabelStroke2Width, this.idLabelColor2);
        MyDraw.drawStrokeText(canvas, this.pwLabelOffsetX, this.pwLabelOffsetY, this.pwLabelTypeface, this.pwLabelString, this.pwLabelFontSize, this.pwLabelColor0, this.pwLabelStroke1Width, this.pwLabelColor1, this.pwLabelStroke2Width, this.pwLabelColor2);
        MyDraw.drawStrokeText(canvas, this.rePwLabelOffsetX, this.rePwLabelOffsetY, this.rePwLabelTypeface, this.rePwLabelString, this.rePwLabelFontSize, this.rePwLabelColor0, this.rePwLabelStroke1Width, this.rePwLabelColor1, this.rePwLabelStroke2Width, this.rePwLabelColor2);
        MyDraw.drawStrokeText(canvas, this.nameLabelOffsetX, this.nameLabelOffsetY, this.nameLabelTypeface, this.nameLabelString, this.nameLabelFontSize, this.nameLabelColor0, this.nameLabelStroke1Width, this.nameLabelColor1, this.nameLabelStroke2Width, this.nameLabelColor2);
        MyDraw.drawStrokeText(canvas, this.idSampleLabelOffsetX, this.idSampleLabelOffsetY0, this.idSampleLabelTypeface, this.idSampleLabelString0, this.idSampleLabelFontSize, this.idSampleLabelColor0, this.idSampleLabelStroke1Width, this.idSampleLabelColor1, this.idSampleLabelStroke2Width, this.idSampleLabelColor2);
        MyDraw.drawStrokeText(canvas, this.idSampleLabelOffsetX, this.idSampleLabelOffsetY1, this.idSampleLabelTypeface, this.idSampleLabelString1, this.idSampleLabelFontSize, this.idSampleLabelColor0, this.idSampleLabelStroke1Width, this.idSampleLabelColor1, this.idSampleLabelStroke2Width, this.idSampleLabelColor2);
        MyDraw.drawStrokeText(canvas, this.pwSampleLabelOffsetX, this.pwSampleLabelOffsetY0, this.pwSampleLabelTypeface, this.pwSampleLabelString0, this.pwSampleLabelFontSize, this.pwSampleLabelColor0, this.pwSampleLabelStroke1Width, this.pwSampleLabelColor1, this.pwSampleLabelStroke2Width, this.pwSampleLabelColor2);
        MyDraw.drawStrokeText(canvas, this.pwSampleLabelOffsetX, this.pwSampleLabelOffsetY1, this.pwSampleLabelTypeface, this.pwSampleLabelString1, this.pwSampleLabelFontSize, this.pwSampleLabelColor0, this.pwSampleLabelStroke1Width, this.pwSampleLabelColor1, this.pwSampleLabelStroke2Width, this.pwSampleLabelColor2);
        MyDraw.drawStrokeText(canvas, this.termsOfServiceLongLabelOffsetX, this.termsOfServiceLongLabelOffsetY0, this.termsOfServiceLongLabelTypeface, this.termsOfServiceLongLabelString0, this.termsOfServiceLongLabelFontSize, this.termsOfServiceLongLabelColor0, this.termsOfServiceLongLabelStroke1Width, this.termsOfServiceLongLabelColor1, this.termsOfServiceLongLabelStroke2Width, this.termsOfServiceLongLabelColor2);
        MyDraw.drawStrokeText(canvas, this.termsOfServiceLongLabelOffsetX, this.termsOfServiceLongLabelOffsetY1, this.termsOfServiceLongLabelTypeface, this.termsOfServiceLongLabelString1, this.termsOfServiceLongLabelFontSize, this.termsOfServiceLongLabelColor0, this.termsOfServiceLongLabelStroke1Width, this.termsOfServiceLongLabelColor1, this.termsOfServiceLongLabelStroke2Width, this.termsOfServiceLongLabelColor2);
        if (this.termsOfServiceButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.termsOfServiceButtonOffsetX, this.termsOfServiceButtonOffsetY, this.termsOfServiceButtonWidth, this.termsOfServiceButtonHeight, this.termsOfServiceButtonColor0, this.termsOfServiceButtonStrokeWidth1, this.termsOfServiceButtonColor1, this.termsOfServiceButtonStrokeWidth2, this.termsOfServiceButtonColor2, this.termsOfServiceButtonStrokeWidth3, this.termsOfServiceButtonColor3, this.termsOfServiceButtonRadius);
            MyDraw.drawStrokeText(canvas, this.termsOfServiceButtonTitleLabelOffsetX, this.termsOfServiceButtonTitleLabelOffsetY, this.termsOfServiceButtonTitleLabelTypeface, this.termsOfServiceButtonTitleLabelString, this.termsOfServiceButtonTitleLabelFontSize, this.termsOfServiceButtonTitleLabelColor0, this.termsOfServiceButtonTitleLabelStroke1Width, this.termsOfServiceButtonTitleLabelColor1, this.termsOfServiceButtonTitleLabelStroke2Width, this.termsOfServiceButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 10) {
                MyDraw.drawStrokeRect(canvas, this.termsOfServiceButtonOffsetX, this.termsOfServiceButtonOffsetY, this.termsOfServiceButtonWidth, this.termsOfServiceButtonHeight, -855638017, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.termsOfServiceButtonRadius);
            }
        }
        if (this.backButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.backButtonOffsetX, this.backButtonOffsetY, this.backButtonWidth, this.backButtonHeight, this.backButtonColor0, this.backButtonStrokeWidth1, this.backButtonColor1, this.backButtonStrokeWidth2, this.backButtonColor2, this.backButtonStrokeWidth3, this.backButtonColor3, this.backButtonRadius);
            MyDraw.drawStrokeText(canvas, this.backButtonTitleLabelOffsetX, this.backButtonTitleLabelOffsetY, this.backButtonTitleLabelTypeface, this.backButtonTitleLabelString, this.backButtonTitleLabelFontSize, this.backButtonTitleLabelColor0, this.backButtonTitleLabelStroke1Width, this.backButtonTitleLabelColor1, this.backButtonTitleLabelStroke2Width, this.backButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 100) {
                MyDraw.drawStrokeRect(canvas, this.backButtonOffsetX, this.backButtonOffsetY, this.backButtonWidth, this.backButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.backButtonRadius);
            }
        }
        if (this.termsOfServiceButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.registerButtonOffsetX, this.registerButtonOffsetY, this.registerButtonWidth, this.registerButtonHeight, this.registerButtonColor0, this.registerButtonStrokeWidth1, this.registerButtonColor1, this.registerButtonStrokeWidth2, this.registerButtonColor2, this.registerButtonStrokeWidth3, this.registerButtonColor3, this.registerButtonRadius);
            MyDraw.drawStrokeText(canvas, this.registerButtonTitleLabelOffsetX, this.registerButtonTitleLabelOffsetY, this.registerButtonTitleLabelTypeface, this.registerButtonTitleLabelString, this.registerButtonTitleLabelFontSize, this.registerButtonTitleLabelColor0, this.registerButtonTitleLabelStroke1Width, this.registerButtonTitleLabelColor1, this.registerButtonTitleLabelStroke2Width, this.registerButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.registerButtonOffsetX, this.registerButtonOffsetY, this.registerButtonWidth, this.registerButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.registerButtonRadius);
            }
        }
    }

    public void hiddenSoftInput(TextView _textView) {
        if (_textView != null) {
            InputMethodManager imm = (InputMethodManager) _textView.getContext().getSystemService("input_method");
            imm.hideSoftInputFromWindow(_textView.getWindowToken(), 0);
        }
    }

    public static String toUtf8(String str) {
        try {
            String utf8String = new String(str.getBytes(), "UTF-8");
            return utf8String;
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void onDestroy() {
        clearRequestAsyncTask();
        this.myDraw = null;
        this.delegate = null;
        this.appDelegate = null;
    }
}

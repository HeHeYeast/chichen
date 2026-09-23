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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AccountLoginUnit {
    public float ACCOUNTIDTEXTFIELD_FONT_SIZE;
    public float ACCOUNTIDTEXTFIELD_HEIGHT;
    public float ACCOUNTIDTEXTFIELD_OFFSET_X;
    public float ACCOUNTIDTEXTFIELD_OFFSET_Y;
    public float ACCOUNTIDTEXTFIELD_WIDTH;
    public float ACCOUNTPWTEXTFIELD_FONT_SIZE;
    public float ACCOUNTPWTEXTFIELD_HEIGHT;
    public float ACCOUNTPWTEXTFIELD_OFFSET_X;
    public float ACCOUNTPWTEXTFIELD_OFFSET_Y;
    public float ACCOUNTPWTEXTFIELD_WIDTH;
    private float IDLABEL_HEIGHT;
    private float IDLABEL_OFFSET_X;
    private float IDLABEL_OFFSET_Y;
    private float IDLABEL_WIDTH;
    private float ORLABEL_HEIGHT;
    private float ORLABEL_OFFSET_X;
    private float ORLABEL_OFFSET_Y;
    private float ORLABEL_WIDTH;
    private float ORLEFTLINE_HEIGHT;
    private float ORLEFTLINE_OFFSET_X;
    private float ORLEFTLINE_OFFSET_Y;
    private float ORLEFTLINE_WIDTH;
    private float ORRIGHTLINE_HEIGHT;
    private float ORRIGHTLINE_OFFSET_X;
    private float ORRIGHTLINE_OFFSET_Y;
    private float ORRIGHTLINE_WIDTH;
    private float PWLABEL_HEIGHT;
    private float PWLABEL_OFFSET_X;
    private float PWLABEL_OFFSET_Y;
    private float PWLABEL_WIDTH;
    private float TOPTITLELABEL_HEIGHT;
    private float TOPTITLELABEL_OFFSET_X;
    private float TOPTITLELABEL_OFFSET_Y;
    private float TOPTITLELABEL_WIDTH;
    public EditText accountIDEditText;
    public EditText accountPWEditText;
    public AccountRegisterUnit accountRegisterUnit;
    private AppDelegate appDelegate;
    private short buttonClickCnt;
    public int createNewAccountButtonColor0;
    public int createNewAccountButtonColor1;
    public int createNewAccountButtonColor2;
    public int createNewAccountButtonColor3;
    public float createNewAccountButtonHeight;
    public float createNewAccountButtonOffsetX;
    public float createNewAccountButtonOffsetY;
    public float createNewAccountButtonRadius;
    private short createNewAccountButtonStatus;
    public float createNewAccountButtonStrokeWidth1;
    public float createNewAccountButtonStrokeWidth2;
    public float createNewAccountButtonStrokeWidth3;
    public int createNewAccountButtonTitleLabelColor0;
    public int createNewAccountButtonTitleLabelColor1;
    public int createNewAccountButtonTitleLabelColor2;
    public float createNewAccountButtonTitleLabelFontSize;
    public float createNewAccountButtonTitleLabelOffsetX;
    public float createNewAccountButtonTitleLabelOffsetY;
    public String createNewAccountButtonTitleLabelString;
    public float createNewAccountButtonTitleLabelStroke1Width;
    public float createNewAccountButtonTitleLabelStroke2Width;
    Typeface createNewAccountButtonTitleLabelTypeface;
    public float createNewAccountButtonWidth;
    public int fbLoginButtonColor0;
    public int fbLoginButtonColor1;
    public int fbLoginButtonColor2;
    public int fbLoginButtonColor3;
    public float fbLoginButtonHeight;
    public float fbLoginButtonOffsetX;
    public float fbLoginButtonOffsetY;
    public float fbLoginButtonRadius;
    private short fbLoginButtonStatus;
    public float fbLoginButtonStrokeWidth1;
    public float fbLoginButtonStrokeWidth2;
    public float fbLoginButtonStrokeWidth3;
    public int fbLoginButtonTitleLabelColor0;
    public int fbLoginButtonTitleLabelColor1;
    public int fbLoginButtonTitleLabelColor2;
    public float fbLoginButtonTitleLabelFontSize;
    public float fbLoginButtonTitleLabelOffsetX;
    public float fbLoginButtonTitleLabelOffsetY;
    public String fbLoginButtonTitleLabelString;
    public float fbLoginButtonTitleLabelStroke1Width;
    public float fbLoginButtonTitleLabelStroke2Width;
    Typeface fbLoginButtonTitleLabelTypeface;
    public float fbLoginButtonWidth;
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
    public int loginButtonColor0;
    public int loginButtonColor1;
    public int loginButtonColor2;
    public int loginButtonColor3;
    public float loginButtonHeight;
    public float loginButtonOffsetX;
    public float loginButtonOffsetY;
    public float loginButtonRadius;
    private short loginButtonStatus;
    public float loginButtonStrokeWidth1;
    public float loginButtonStrokeWidth2;
    public float loginButtonStrokeWidth3;
    public int loginButtonTitleLabelColor0;
    public int loginButtonTitleLabelColor1;
    public int loginButtonTitleLabelColor2;
    public float loginButtonTitleLabelFontSize;
    public float loginButtonTitleLabelOffsetX;
    public float loginButtonTitleLabelOffsetY;
    public String loginButtonTitleLabelString;
    public float loginButtonTitleLabelStroke1Width;
    public float loginButtonTitleLabelStroke2Width;
    Typeface loginButtonTitleLabelTypeface;
    public float loginButtonWidth;
    public short loginType;
    private MyDraw myDraw;
    public short nowStatus;
    public OnlineGameViewController onlineGameViewController;
    public int orLabelColor0;
    public int orLabelColor1;
    public int orLabelColor2;
    float orLabelFontSize;
    float orLabelOffsetX;
    float orLabelOffsetY;
    String orLabelString;
    public float orLabelStroke1Width;
    public float orLabelStroke2Width;
    Typeface orLabelTypeface;
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
    private short selectedButtonIndex;
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
    private final String LOGIN_REQUEST_URL = "http://www.idtgame.com/idtgameserver/idt/account/config/login/";
    private final String FB_LOGIN_REQUEST_URL = "http://www.idtgame.com/idtgameserver/idt/account/config/login_ck_cd_fb/";
    private final int LOGIN_REQUEST_TIMEOUT_INTERVAL = 30000;
    private RequestAsyncTask requestAsyncTask = null;

    public AccountLoginUnit(float _finalwidth, float _finalheight, float _zoomrate, OnlineGameViewController _onlineGameViewController, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.nowStatus = (short) 0;
        this.loginType = (short) -1;
        this.hidden = false;
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.loginButtonStatus = (short) 0;
        this.fbLoginButtonStatus = (short) 0;
        this.createNewAccountButtonStatus = (short) 0;
        this.topTitleLabelTypeface = null;
        this.topTitleLabelFontSize = 28.0f;
        this.topTitleLabelColor0 = -1;
        this.topTitleLabelStroke1Width = 4.0f;
        this.topTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.topTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.topTitleLabelColor2 = 0;
        this.TOPTITLELABEL_OFFSET_X = 25.0f;
        this.TOPTITLELABEL_OFFSET_Y = 45.0f;
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
        this.IDLABEL_OFFSET_Y = 90.0f;
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
        this.PWLABEL_OFFSET_Y = this.IDLABEL_OFFSET_Y + 70.0f;
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
        this.orLabelTypeface = null;
        this.orLabelFontSize = 24.0f;
        this.orLabelColor0 = -3355444;
        this.orLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.orLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.orLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.orLabelColor2 = 0;
        this.ORLABEL_OFFSET_X = 160.0f;
        this.ORLABEL_OFFSET_Y = 340.0f;
        this.ORLABEL_WIDTH = 300.0f;
        this.ORLABEL_HEIGHT = 30.0f;
        this.orLabelString = "";
        this.orLabelOffsetX = this.ORLABEL_OFFSET_X;
        this.orLabelOffsetY = this.ORLABEL_OFFSET_Y;
        this.ORLEFTLINE_OFFSET_X = 35.0f;
        this.ORLEFTLINE_OFFSET_Y = this.ORLABEL_OFFSET_Y + 15.0f;
        this.ORLEFTLINE_WIDTH = 95.0f;
        this.ORLEFTLINE_HEIGHT = 2.0f;
        this.ORRIGHTLINE_OFFSET_X = 190.0f;
        this.ORRIGHTLINE_OFFSET_Y = this.ORLEFTLINE_OFFSET_Y;
        this.ORRIGHTLINE_WIDTH = this.ORLEFTLINE_OFFSET_Y;
        this.ORRIGHTLINE_HEIGHT = this.ORLEFTLINE_HEIGHT;
        this.loginButtonWidth = 180.0f;
        this.loginButtonHeight = 32.0f;
        this.loginButtonOffsetX = 230.0f;
        this.loginButtonOffsetY = 244.0f;
        this.loginButtonColor0 = -227838;
        this.loginButtonStrokeWidth1 = 2.0f;
        this.loginButtonColor1 = -16;
        this.loginButtonStrokeWidth2 = 2.0f;
        this.loginButtonColor2 = -7576502;
        this.loginButtonStrokeWidth3 = 1.0f;
        this.loginButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.loginButtonRadius = 10.0f;
        this.loginButtonTitleLabelString = "";
        this.loginButtonTitleLabelFontSize = 20.0f;
        this.loginButtonTitleLabelColor0 = -16;
        this.loginButtonTitleLabelStroke1Width = 3.0f;
        this.loginButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.loginButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.loginButtonTitleLabelColor2 = 0;
        this.loginButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.loginButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.fbLoginButtonWidth = 180.0f;
        this.fbLoginButtonHeight = 32.0f;
        this.fbLoginButtonOffsetX = 230.0f;
        this.fbLoginButtonOffsetY = 244.0f;
        this.fbLoginButtonColor0 = -227838;
        this.fbLoginButtonStrokeWidth1 = 2.0f;
        this.fbLoginButtonColor1 = -16;
        this.fbLoginButtonStrokeWidth2 = 2.0f;
        this.fbLoginButtonColor2 = -7576502;
        this.fbLoginButtonStrokeWidth3 = 1.0f;
        this.fbLoginButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.fbLoginButtonRadius = 10.0f;
        this.fbLoginButtonTitleLabelString = "";
        this.fbLoginButtonTitleLabelFontSize = 20.0f;
        this.fbLoginButtonTitleLabelColor0 = -16;
        this.fbLoginButtonTitleLabelStroke1Width = 3.0f;
        this.fbLoginButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.fbLoginButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.fbLoginButtonTitleLabelColor2 = 0;
        this.fbLoginButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.fbLoginButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.createNewAccountButtonWidth = 180.0f;
        this.createNewAccountButtonHeight = 32.0f;
        this.createNewAccountButtonOffsetX = 230.0f;
        this.createNewAccountButtonOffsetY = 244.0f;
        this.createNewAccountButtonColor0 = -227838;
        this.createNewAccountButtonStrokeWidth1 = 2.0f;
        this.createNewAccountButtonColor1 = -16;
        this.createNewAccountButtonStrokeWidth2 = 2.0f;
        this.createNewAccountButtonColor2 = -7576502;
        this.createNewAccountButtonStrokeWidth3 = 1.0f;
        this.createNewAccountButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.createNewAccountButtonRadius = 10.0f;
        this.createNewAccountButtonTitleLabelString = "";
        this.createNewAccountButtonTitleLabelFontSize = 20.0f;
        this.createNewAccountButtonTitleLabelColor0 = -16;
        this.createNewAccountButtonTitleLabelStroke1Width = 3.0f;
        this.createNewAccountButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.createNewAccountButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.createNewAccountButtonTitleLabelColor2 = 0;
        this.createNewAccountButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.createNewAccountButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = null;
        this.onlineGameViewController = null;
        this.accountRegisterUnit = null;
        this.appDelegate = _appDelegate;
        this.onlineGameViewController = _onlineGameViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.nowStatus = (short) 0;
        this.loginType = (short) -1;
        this.hidden = false;
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.loginButtonStatus = (short) 0;
        this.fbLoginButtonStatus = (short) 0;
        this.createNewAccountButtonStatus = (short) 0;
        Paint newPaint = new Paint(257);
        this.topTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.topTitleLabelFontSize = 28.0f * this.zoomRate;
        this.topTitleLabelColor0 = -1;
        this.topTitleLabelStroke1Width = 4.0f * this.zoomRate;
        this.topTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.topTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.topTitleLabelColor2 = 0;
        newPaint.setTypeface(this.topTitleLabelTypeface);
        newPaint.setTextSize(this.topTitleLabelFontSize);
        this.TOPTITLELABEL_OFFSET_X = 25.0f * this.zoomRate;
        this.TOPTITLELABEL_OFFSET_Y = 45.0f * this.zoomRate;
        this.TOPTITLELABEL_WIDTH = 300.0f * this.zoomRate;
        this.TOPTITLELABEL_HEIGHT = 30.0f * this.zoomRate;
        this.topTitleLabelString = " " + this.appDelegate.getResources().getString(R.string.SignIn);
        this.topTitleLabelOffsetX = this.TOPTITLELABEL_OFFSET_X;
        this.topTitleLabelOffsetY = this.TOPTITLELABEL_OFFSET_Y + this.topTitleLabelFontSize;
        this.idLabelTypeface = Typeface.DEFAULT_BOLD;
        this.idLabelFontSize = 17.0f * this.zoomRate;
        this.idLabelColor0 = FluctConstants.FRAME_BASE_COLOR;
        this.idLabelStroke1Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.idLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.idLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.idLabelColor2 = 0;
        newPaint.setTypeface(this.idLabelTypeface);
        newPaint.setTextSize(this.idLabelFontSize);
        this.IDLABEL_OFFSET_X = 35.0f * this.zoomRate;
        this.IDLABEL_OFFSET_Y = 90.0f * this.zoomRate;
        this.IDLABEL_WIDTH = 300.0f * this.zoomRate;
        this.IDLABEL_HEIGHT = 20.0f * this.zoomRate;
        this.idLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.ID)) + "(" + this.appDelegate.getResources().getString(R.string.Email) + ")";
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
        this.pwLabelStroke1Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.pwLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.pwLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.pwLabelColor2 = 0;
        newPaint.setTypeface(this.pwLabelTypeface);
        newPaint.setTextSize(this.pwLabelFontSize);
        this.PWLABEL_OFFSET_X = 35.0f * this.zoomRate;
        this.PWLABEL_OFFSET_Y = this.IDLABEL_OFFSET_Y + (70.0f * this.zoomRate);
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
        this.accountIDEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.onlinegame.AccountLoginUnit.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId != 0 && actionId != 6) {
                    return false;
                }
                AccountLoginUnit.this.hiddenSoftInput(_textView);
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
        this.accountPWEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.onlinegame.AccountLoginUnit.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId != 0 && actionId != 6) {
                    return false;
                }
                AccountLoginUnit.this.hiddenSoftInput(_textView);
                return true;
            }
        });
        this.orLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.orLabelFontSize = 24.0f * this.zoomRate;
        this.orLabelColor0 = -3355444;
        this.orLabelStroke1Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.orLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.orLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.orLabelColor2 = 0;
        newPaint.setTypeface(this.orLabelTypeface);
        newPaint.setTextSize(this.orLabelFontSize);
        this.ORLABEL_OFFSET_X = this.finalWidth / 2.0f;
        if (!this.appDelegate.isRetina4) {
            this.ORLABEL_OFFSET_Y = 326.0f * this.zoomRate;
        } else {
            this.ORLABEL_OFFSET_Y = 370.0f * this.zoomRate;
        }
        this.ORLABEL_WIDTH = 300.0f * this.zoomRate;
        this.ORLABEL_HEIGHT = 30.0f * this.zoomRate;
        this.orLabelString = this.appDelegate.getResources().getString(R.string.OR);
        this.orLabelOffsetX = this.ORLABEL_OFFSET_X - (newPaint.measureText(this.orLabelString) / 2.0f);
        this.orLabelOffsetY = this.ORLABEL_OFFSET_Y + this.orLabelFontSize;
        this.ORLEFTLINE_OFFSET_X = 35.0f * this.zoomRate;
        this.ORLEFTLINE_OFFSET_Y = this.ORLABEL_OFFSET_Y + (15.0f * this.zoomRate);
        this.ORLEFTLINE_WIDTH = 95.0f * this.zoomRate;
        this.ORLEFTLINE_HEIGHT = this.zoomRate * 2.0f;
        this.ORRIGHTLINE_OFFSET_X = 190.0f * this.zoomRate;
        this.ORRIGHTLINE_OFFSET_Y = this.ORLEFTLINE_OFFSET_Y;
        this.ORRIGHTLINE_WIDTH = this.ORLEFTLINE_WIDTH;
        this.ORRIGHTLINE_HEIGHT = this.ORLEFTLINE_HEIGHT;
        this.loginButtonWidth = 220.0f * this.zoomRate;
        this.loginButtonHeight = 36.0f * this.zoomRate;
        this.loginButtonOffsetX = (this.finalWidth - this.loginButtonWidth) / 2.0f;
        this.loginButtonOffsetY = (260.0f * this.zoomRate) - (this.loginButtonHeight / 2.0f);
        if (!this.appDelegate.isRetina4) {
            this.loginButtonOffsetY -= 11.0f * this.zoomRate;
        }
        this.loginButtonColor0 = -227838;
        this.loginButtonStrokeWidth1 = this.zoomRate * 2.0f;
        this.loginButtonColor1 = -16;
        this.loginButtonStrokeWidth2 = this.zoomRate * 2.0f;
        this.loginButtonColor2 = -7576502;
        this.loginButtonStrokeWidth3 = 1.0f * this.zoomRate;
        this.loginButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.loginButtonRadius = 10.0f * this.zoomRate;
        this.loginButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.SignIn);
        this.loginButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.loginButtonTitleLabelFontSize = 20.0f * this.zoomRate;
        this.loginButtonTitleLabelColor0 = -16;
        this.loginButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.loginButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.loginButtonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.loginButtonTitleLabelColor2 = 0;
        this.loginButtonTitleLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.loginButtonTitleLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        newPaint.setTypeface(this.loginButtonTitleLabelTypeface);
        newPaint.setTextSize(this.loginButtonTitleLabelFontSize);
        this.loginButtonTitleLabelOffsetX = this.loginButtonOffsetX + ((this.loginButtonWidth - newPaint.measureText(this.loginButtonTitleLabelString)) / 2.0f);
        this.loginButtonTitleLabelOffsetY = (this.loginButtonOffsetY + this.loginButtonHeight) - (this.loginButtonTitleLabelFontSize * 0.5f);
        this.fbLoginButtonWidth = 220.0f * this.zoomRate;
        this.fbLoginButtonHeight = 36.0f * this.zoomRate;
        this.fbLoginButtonOffsetX = (this.finalWidth - this.fbLoginButtonWidth) / 2.0f;
        this.fbLoginButtonOffsetY = (320.0f * this.zoomRate) - (this.fbLoginButtonHeight / 2.0f);
        if (!this.appDelegate.isRetina4) {
            this.fbLoginButtonOffsetY -= 22.0f * this.zoomRate;
        }
        this.fbLoginButtonColor0 = -227838;
        this.fbLoginButtonStrokeWidth1 = this.zoomRate * 2.0f;
        this.fbLoginButtonColor1 = -16;
        this.fbLoginButtonStrokeWidth2 = this.zoomRate * 2.0f;
        this.fbLoginButtonColor2 = -7576502;
        this.fbLoginButtonStrokeWidth3 = 1.0f * this.zoomRate;
        this.fbLoginButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.fbLoginButtonRadius = 10.0f * this.zoomRate;
        this.fbLoginButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.FacebookSignIn);
        this.fbLoginButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.fbLoginButtonTitleLabelFontSize = 20.0f * this.zoomRate;
        this.fbLoginButtonTitleLabelColor0 = -16;
        this.fbLoginButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.fbLoginButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.fbLoginButtonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.fbLoginButtonTitleLabelColor2 = 0;
        this.fbLoginButtonTitleLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.fbLoginButtonTitleLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        newPaint.setTypeface(this.fbLoginButtonTitleLabelTypeface);
        newPaint.setTextSize(this.fbLoginButtonTitleLabelFontSize);
        this.fbLoginButtonTitleLabelOffsetX = this.fbLoginButtonOffsetX + ((this.fbLoginButtonWidth - newPaint.measureText(this.fbLoginButtonTitleLabelString)) / 2.0f);
        this.fbLoginButtonTitleLabelOffsetY = (this.fbLoginButtonOffsetY + this.fbLoginButtonHeight) - (this.fbLoginButtonTitleLabelFontSize * 0.5f);
        this.createNewAccountButtonWidth = 220.0f * this.zoomRate;
        this.createNewAccountButtonHeight = 36.0f * this.zoomRate;
        this.createNewAccountButtonOffsetX = (this.finalWidth - this.createNewAccountButtonWidth) / 2.0f;
        this.createNewAccountButtonOffsetY = ((this.finalHeight - (50.0f * this.zoomRate)) - (70.0f * this.zoomRate)) - (this.createNewAccountButtonHeight / 2.0f);
        if (!this.appDelegate.isRetina4) {
            this.createNewAccountButtonOffsetY += 22.0f * this.zoomRate;
        }
        this.createNewAccountButtonColor0 = -227838;
        this.createNewAccountButtonStrokeWidth1 = this.zoomRate * 2.0f;
        this.createNewAccountButtonColor1 = -16;
        this.createNewAccountButtonStrokeWidth2 = this.zoomRate * 2.0f;
        this.createNewAccountButtonColor2 = -7576502;
        this.createNewAccountButtonStrokeWidth3 = 1.0f * this.zoomRate;
        this.createNewAccountButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.createNewAccountButtonRadius = 10.0f * this.zoomRate;
        this.createNewAccountButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.CreateNewAccount);
        this.createNewAccountButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.createNewAccountButtonTitleLabelFontSize = 20.0f * this.zoomRate;
        this.createNewAccountButtonTitleLabelColor0 = -16;
        this.createNewAccountButtonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.createNewAccountButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.createNewAccountButtonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.createNewAccountButtonTitleLabelColor2 = 0;
        this.createNewAccountButtonTitleLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.createNewAccountButtonTitleLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        newPaint.setTypeface(this.createNewAccountButtonTitleLabelTypeface);
        newPaint.setTextSize(this.createNewAccountButtonTitleLabelFontSize);
        this.createNewAccountButtonTitleLabelOffsetX = this.createNewAccountButtonOffsetX + ((this.createNewAccountButtonWidth - newPaint.measureText(this.createNewAccountButtonTitleLabelString)) / 2.0f);
        this.createNewAccountButtonTitleLabelOffsetY = (this.createNewAccountButtonOffsetY + this.createNewAccountButtonHeight) - (this.createNewAccountButtonTitleLabelFontSize * 0.5f);
        this.accountRegisterUnit = new AccountRegisterUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        clearRequestAsyncTask();
        this.myDraw = new MyDraw();
    }

    public void clear() {
        this.nowStatus = (short) 0;
        this.loginType = (short) -1;
        clearRequestAsyncTask();
        hiddenSoftInput(this.accountIDEditText);
        this.accountIDEditText.setText("");
        hiddenSoftInput(this.accountPWEditText);
        this.accountPWEditText.setText("");
    }

    public void reset() {
        this.hidden = true;
        clear();
        this.loginButtonStatus = (short) 0;
        this.fbLoginButtonStatus = (short) 0;
        this.createNewAccountButtonStatus = (short) 0;
        this.accountRegisterUnit.reset();
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.stopLoading();
        }
    }

    public void close() {
        this.hidden = true;
        reset();
        this.nowStatus = (short) -1;
    }

    public void openWithAutoLogIn(Boolean _autoLogInF) {
        this.nowStatus = (short) 0;
        reset();
        String fb_account_id = "";
        if (this.appDelegate != null) {
            fb_account_id = this.appDelegate.get_fb_account_id();
            this.accountIDEditText.setText(this.appDelegate.get_idt_account_id());
            this.accountPWEditText.setText(this.appDelegate.get_idt_account_password());
        }
        this.hidden = false;
        if (_autoLogInF.booleanValue()) {
            if (fb_account_id != null && fb_account_id.length() > 0) {
                doFbLogin();
            } else if (this.accountIDEditText.getText().toString() != null && this.accountPWEditText.getText().toString() != null && this.accountIDEditText.getText().toString().length() > 0 && this.accountPWEditText.getText().toString().length() > 0) {
                doLogin();
            }
        }
    }

    public void doFbLogin() {
        RequestAsyncTask requestAsyncTask = null;
        if (this.appDelegate != null) {
            clearRequestAsyncTask();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.stopLoading();
            }
            hiddenSoftInput(this.accountIDEditText);
            hiddenSoftInput(this.accountPWEditText);
            this.appDelegate.set_idt_account_logged_in(false);
            if (!this.appDelegate.checkInterNet()) {
                this.appDelegate.showNoInternetAlertDialog();
                return;
            }
            String fb_account_id = this.appDelegate.get_fb_account_id();
            if (fb_account_id.length() <= 0) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("facebook").setMessage(this.appDelegate.getResources().getString(R.string.SignInFailedPleaseTryAgain)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                return;
            }
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.SigningIn));
            }
            this.loginType = (short) 1;
            String urlString = "http://www.idtgame.com/idtgameserver/idt/account/config/login_ck_cd_fb/?rd=" + this.appDelegate.getNowDateString();
            this.requestAsyncTask = new RequestAsyncTask(this, requestAsyncTask);
            this.requestAsyncTask.execute(urlString);
        }
    }

    public void doLogin() {
        RequestAsyncTask requestAsyncTask = null;
        if (this.appDelegate != null) {
            clearRequestAsyncTask();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.stopLoading();
            }
            hiddenSoftInput(this.accountIDEditText);
            hiddenSoftInput(this.accountPWEditText);
            this.appDelegate.set_idt_account_logged_in(false);
            if (!this.appDelegate.checkInterNet()) {
                this.appDelegate.showNoInternetAlertDialog();
                return;
            }
            if (this.accountIDEditText.getText().toString().length() <= 0) {
                if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.PleaseInputiDTID)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.accountPWEditText.getText().toString().length() <= 0) {
                if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.PleaseInputPassword)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                    return;
                }
                return;
            }
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.SigningIn));
            }
            this.loginType = (short) 0;
            String urlString = "http://www.idtgame.com/idtgameserver/idt/account/config/login/?rd=" + this.appDelegate.getNowDateString();
            this.requestAsyncTask = new RequestAsyncTask(this, requestAsyncTask);
            this.requestAsyncTask.execute(urlString);
        }
    }

    public void readyDoFbLogin() {
        if (this.appDelegate != null && this.onlineGameViewController != null) {
            this.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.SigningIn));
            this.appDelegate.readyDoFbLogin(this.onlineGameViewController.nextActiveStatus);
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

        /* synthetic */ RequestAsyncTask(AccountLoginUnit accountLoginUnit, RequestAsyncTask requestAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(String... params) throws IOException {
            URL url;
            HttpURLConnection conn;
            if (AccountLoginUnit.this.appDelegate == null) {
                return null;
            }
            JSONObject json = null;
            try {
                try {
                    if (AccountLoginUnit.this.loginType == 1) {
                        String urlString = "http://www.idtgame.com/idtgameserver/idt/account/config/login_ck_cd_fb/?rd=" + AccountLoginUnit.this.appDelegate.getNowDateString();
                        url = new URL(urlString);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(30000);
                        conn.setConnectTimeout(30000);
                        conn.setRequestProperty(MIME.CONTENT_TYPE, "text/plain; charset=utf-8");
                        conn.setRequestProperty("fb_id", AccountLoginUnit.this.appDelegate.get_fb_account_id());
                        conn.setRequestProperty("password", AccountLoginUnit.this.appDelegate.get_fb_account_password());
                        conn.setRequestProperty("account", URLEncoder.encode(AccountLoginUnit.this.appDelegate.get_fb_account_user_name(), "UTF-8"));
                        conn.setRequestProperty("mail_address", AccountLoginUnit.this.appDelegate.get_fb_account_email());
                        conn.setRequestProperty("birthday", AccountLoginUnit.this.appDelegate.get_fb_account_birthday());
                        conn.setRequestProperty("name", URLEncoder.encode(AccountLoginUnit.this.appDelegate.get_fb_account_name(), "UTF-8"));
                        conn.setRequestProperty("first_name", URLEncoder.encode(AccountLoginUnit.this.appDelegate.get_fb_account_first_name(), "UTF-8"));
                        conn.setRequestProperty("last_name", URLEncoder.encode(AccountLoginUnit.this.appDelegate.get_fb_account_last_name(), "UTF-8"));
                    } else {
                        String str = "http://www.idtgame.com/idtgameserver/idt/account/config/login/?rd=" + AccountLoginUnit.this.appDelegate.getNowDateString();
                        url = new URL("http://www.idtgame.com/idtgameserver/idt/account/config/login/");
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(30000);
                        conn.setConnectTimeout(30000);
                        conn.setRequestProperty(MIME.CONTENT_TYPE, "text/plain; charset=utf-8");
                        conn.setRequestProperty("account", AccountLoginUnit.this.accountIDEditText.getText().toString());
                        conn.setRequestProperty("password", AccountLoginUnit.this.accountPWEditText.getText().toString());
                        conn.setRequestProperty("fb_id", "");
                    }
                    String languageString = AccountLoginUnit.this.appDelegate.getLocaleLanguage();
                    if (languageString.equals("ja-JP")) {
                        conn.setRequestProperty("language", "ja");
                    } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                        conn.setRequestProperty("language", "tw");
                    } else if (languageString.equals("zh-CN")) {
                        conn.setRequestProperty("language", "cn");
                    } else {
                        conn.setRequestProperty("language", "en");
                    }
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
                    e = e2;
                    e.printStackTrace();
                    return json;
                } catch (IOException e3) {
                    return null;
                }
            } catch (MalformedURLException e4) {
                e = e4;
                e.printStackTrace();
                return json;
            } catch (IOException e5) {
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
            AccountLoginUnit.this.doLoginResponse(successString);
            super.onPostExecute((RequestAsyncTask) _json);
        }
    }

    public void doLoginResponse(String _successString) {
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.stopLoading();
        }
        if (_successString.equals("0")) {
            if (this.loginType == 1) {
                this.appDelegate.do_idt_account_logout();
                if (this.onlineGameViewController != null) {
                    this.onlineGameViewController.doNextActiveStatus((short) 0);
                }
            } else if (this.loginType == 0) {
                if (this.appDelegate != null) {
                    if (this.accountIDEditText.getText().toString().length() > 0 && this.appDelegate.set_idt_account_id(this.accountIDEditText.getText().toString()).booleanValue() && this.accountPWEditText.getText().toString().length() > 0 && this.appDelegate.set_idt_account_password(this.accountPWEditText.getText().toString()).booleanValue()) {
                        this.appDelegate.set_idt_account_logged_in(true);
                    } else {
                        this.appDelegate.do_idt_account_logout();
                    }
                }
                if (this.onlineGameViewController != null) {
                    this.onlineGameViewController.doNextActiveStatus((short) 0);
                }
            }
        } else if (_successString.equals("100")) {
            this.appDelegate.set_idt_account_logged_in(false);
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.SignInFailedPleaseTryAgain)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (_successString.equals("101")) {
            this.appDelegate.set_idt_account_logged_in(false);
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.IDDoesNotExist)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (_successString.equals("102")) {
            this.appDelegate.set_idt_account_logged_in(false);
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.IncorrectPassword)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else {
            this.appDelegate.set_idt_account_logged_in(false);
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        }
        clearRequestAsyncTask();
    }

    public void makeNewText(String _newText) {
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (event.getAction() == 0) {
            this.selectedButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (event.getX() > this.loginButtonOffsetX && event.getX() < this.loginButtonOffsetX + this.loginButtonWidth && event.getY() >= this.loginButtonOffsetY && event.getY() <= this.loginButtonOffsetY + this.loginButtonHeight) {
                this.selectedButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.onlinegame.AccountLoginUnit.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountLoginUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.fbLoginButtonOffsetX && event.getX() < this.fbLoginButtonOffsetX + this.fbLoginButtonWidth && event.getY() >= this.fbLoginButtonOffsetY && event.getY() <= this.fbLoginButtonOffsetY + this.fbLoginButtonHeight) {
                this.selectedButtonIndex = (short) 1;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.onlinegame.AccountLoginUnit.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountLoginUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (event.getX() > this.createNewAccountButtonOffsetX && event.getX() < this.createNewAccountButtonOffsetX + this.createNewAccountButtonWidth && event.getY() >= this.createNewAccountButtonOffsetY && event.getY() <= this.createNewAccountButtonOffsetY + this.createNewAccountButtonHeight) {
                this.selectedButtonIndex = (short) 10;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.onlinegame.AccountLoginUnit.5
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountLoginUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        }
        return false;
    }

    public void doClick() {
        if (this.selectedButtonIndex == 0) {
            if (!this.hidden.booleanValue()) {
                this.appDelegate.doSoundPoolPlay(1);
            }
            doLogin();
        } else if (this.selectedButtonIndex == 1) {
            if (!this.hidden.booleanValue()) {
                this.appDelegate.doSoundPoolPlay(1);
            }
            readyDoFbLogin();
        } else if (this.selectedButtonIndex == 10) {
            if (!this.hidden.booleanValue()) {
                this.appDelegate.doSoundPoolPlay(1);
            }
            if (this.accountRegisterUnit != null) {
                this.accountRegisterUnit.open();
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
        MyDraw.drawStrokeText(canvas, this.orLabelOffsetX, this.orLabelOffsetY, this.orLabelTypeface, this.orLabelString, this.orLabelFontSize, this.orLabelColor0, this.orLabelStroke1Width, this.orLabelColor1, this.orLabelStroke2Width, this.orLabelColor2);
        MyDraw.drawStrokeRect(canvas, this.ORLEFTLINE_OFFSET_X, this.ORLEFTLINE_OFFSET_Y, this.ORLEFTLINE_WIDTH, this.ORLEFTLINE_HEIGHT, this.orLabelColor0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED);
        MyDraw.drawStrokeRect(canvas, this.ORRIGHTLINE_OFFSET_X, this.ORRIGHTLINE_OFFSET_Y, this.ORRIGHTLINE_WIDTH, this.ORRIGHTLINE_HEIGHT, this.orLabelColor0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED);
        if (this.loginButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.loginButtonOffsetX, this.loginButtonOffsetY, this.loginButtonWidth, this.loginButtonHeight, this.loginButtonColor0, this.loginButtonStrokeWidth1, this.loginButtonColor1, this.loginButtonStrokeWidth2, this.loginButtonColor2, this.loginButtonStrokeWidth3, this.loginButtonColor3, this.loginButtonRadius);
            MyDraw.drawStrokeText(canvas, this.loginButtonTitleLabelOffsetX, this.loginButtonTitleLabelOffsetY, this.loginButtonTitleLabelTypeface, this.loginButtonTitleLabelString, this.loginButtonTitleLabelFontSize, this.loginButtonTitleLabelColor0, this.loginButtonTitleLabelStroke1Width, this.loginButtonTitleLabelColor1, this.loginButtonTitleLabelStroke2Width, this.loginButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.loginButtonOffsetX, this.loginButtonOffsetY, this.loginButtonWidth, this.loginButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.loginButtonRadius);
            }
        }
        if (this.fbLoginButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.fbLoginButtonOffsetX, this.fbLoginButtonOffsetY, this.fbLoginButtonWidth, this.fbLoginButtonHeight, this.fbLoginButtonColor0, this.fbLoginButtonStrokeWidth1, this.fbLoginButtonColor1, this.fbLoginButtonStrokeWidth2, this.fbLoginButtonColor2, this.fbLoginButtonStrokeWidth3, this.fbLoginButtonColor3, this.fbLoginButtonRadius);
            MyDraw.drawStrokeText(canvas, this.fbLoginButtonTitleLabelOffsetX, this.fbLoginButtonTitleLabelOffsetY, this.fbLoginButtonTitleLabelTypeface, this.fbLoginButtonTitleLabelString, this.fbLoginButtonTitleLabelFontSize, this.fbLoginButtonTitleLabelColor0, this.fbLoginButtonTitleLabelStroke1Width, this.fbLoginButtonTitleLabelColor1, this.fbLoginButtonTitleLabelStroke2Width, this.fbLoginButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 1) {
                MyDraw.drawStrokeRect(canvas, this.fbLoginButtonOffsetX, this.fbLoginButtonOffsetY, this.fbLoginButtonWidth, this.fbLoginButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.fbLoginButtonRadius);
            }
        }
        if (this.createNewAccountButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.createNewAccountButtonOffsetX, this.createNewAccountButtonOffsetY, this.createNewAccountButtonWidth, this.createNewAccountButtonHeight, this.createNewAccountButtonColor0, this.createNewAccountButtonStrokeWidth1, this.createNewAccountButtonColor1, this.createNewAccountButtonStrokeWidth2, this.createNewAccountButtonColor2, this.createNewAccountButtonStrokeWidth3, this.createNewAccountButtonColor3, this.createNewAccountButtonRadius);
            MyDraw.drawStrokeText(canvas, this.createNewAccountButtonTitleLabelOffsetX, this.createNewAccountButtonTitleLabelOffsetY, this.createNewAccountButtonTitleLabelTypeface, this.createNewAccountButtonTitleLabelString, this.createNewAccountButtonTitleLabelFontSize, this.createNewAccountButtonTitleLabelColor0, this.createNewAccountButtonTitleLabelStroke1Width, this.createNewAccountButtonTitleLabelColor1, this.createNewAccountButtonTitleLabelStroke2Width, this.createNewAccountButtonTitleLabelColor0);
            if (this.selectedButtonIndex == 10) {
                MyDraw.drawStrokeRect(canvas, this.createNewAccountButtonOffsetX, this.createNewAccountButtonOffsetY, this.createNewAccountButtonWidth, this.createNewAccountButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.createNewAccountButtonRadius);
            }
        }
    }

    public void hiddenSoftInput(TextView _textView) {
        if (_textView != null) {
            InputMethodManager imm = (InputMethodManager) _textView.getContext().getSystemService("input_method");
            imm.hideSoftInputFromWindow(_textView.getWindowToken(), 0);
        }
    }

    public void onDestroy() {
        clearRequestAsyncTask();
        this.myDraw = null;
        if (this.accountRegisterUnit != null) {
            this.accountRegisterUnit.onDestroy();
            this.accountRegisterUnit = null;
        }
        this.onlineGameViewController = null;
        this.appDelegate = null;
    }
}

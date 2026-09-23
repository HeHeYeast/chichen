package com.idtinc.ckchickandduck;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck_alarm.CallAlarm;
import com.idtinc.ckunit.CharacterDataDictionary;
import com.idtinc.ckunit.CharacterDataDictionarySAXService;
import com.idtinc.ckunit.CharacterUnitDictionary;
import com.idtinc.ckunit.CharactersDataDictionary;
import com.idtinc.ckunit.FarmUnitDictionary;
import com.idtinc.ckunit.MainSavesDictionary;
import com.idtinc.ckunit.TimeSaveDictionary;
import com.idtinc.ckunit.ToolDataDictionary;
import com.idtinc.ckunit.ToolDataDictionarySAXService;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.ckunit.ToolsDataDictionary;
import com.idtinc.onlinegame.PostDeviceToken;
import com.idtinc.request.CampaignJsonRequest;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AppDelegate extends Application {
    private Handler initImageHandler;
    private HandlerThread initImageHandlerThread;
    private Handler saveMainSavesDictionaryHandler;
    private HandlerThread saveMainSavesDictionaryHandlerThread;
    private Handler saveTimeSaveDictionaryHandler;
    private HandlerThread saveTimeSaveDictionaryHandlerThread;
    public float zoomRate;
    private AppDelegateInterface nowAppMainActivity = null;
    public final short VERSION_LEVEL = 30;
    public final String VERSION_NUMBER = "2.3.0";
    public short NEED_RELOAD_HOURS = 120;
    public final int CHARACTERS_DATA_FILES_TOTAL = 2;
    private final int TOOLS_DATA_FILES_TOTAL = 4;
    private final int TIMER_SPACE = 100;
    public short CHECK_GAME_LOOP_CNT = 10;
    public short SAVE_CNT_MAX = 3;
    public short MAIN_PAGES_CNT = 4;
    public short CHARACTERUNITVIEW_TOTAL = 24;
    public short CHARACTE_0_ALL_CNT = 114;
    public short CHARACTE_1_ALL_CNT = 57;
    public short TOOL_1_ALL_CNT = 8;
    public short TOOL_1_SELECTBUTTON_CNT = 5;
    public short TOOL_2_ALL_CNT = 75;
    public short TOOL_2_SELECTBUTTON_CNT = 3;
    public final float TIME_GAUGE_SEIDO = 20.0f;
    public final short SICK_RATE = 400;
    public long KITCHEN_DIRTY_HOURS = 24;
    public long FARM_PER_DIRTY_HOURS = 2;
    public long FARM_PER_LOSE_HOURS = 24;
    public final short SAVE_TIME_SPACE_HOUES = 48;
    public final short SAVE_FILES_MAX = 3;
    public final short KITCHEN_FIX_CP_0 = 100;
    public final short KITCHEN_FIX_CP_1 = 150;
    public final short KITCHEN_FIX_CP_2 = 200;
    public final short KITCHEN_FIX_CP_3 = 250;
    public final int KITCHEN_LEVELUP_CP_0 = HapticContentSDK.f17b04440444044404440444;
    public final int KITCHEN_LEVELUP_CP_1 = 20000;
    public final int KITCHEN_LEVELUP_CP_2 = 30000;
    public final float FARM_FIX_PER_CP_0 = 1.0f;
    public final float FARM_FIX_CP_0_MAX = 200.0f;
    public final short SEND_TOTALT_CNT_MAX = 10;
    private final int SOUND_COUNT = 18;
    public final String FONTNAME_00 = "APJapanesefontK.ttf";
    private Boolean saveTimeSaveDictionaryNeedDestoryF = false;
    private Boolean saveTimeSaveDictionaryLockF = false;
    private Boolean saveMainSavesDictionaryNeedDestoryF = false;
    private Boolean saveMainSavesDictionaryLockF = false;
    private MediaPlayer mediaPlayer = null;
    private SoundPool soundPool = null;
    private HashMap<Integer, Integer> soundMap = null;
    public SharedPreferences defaultSharedPreferences = null;
    public PostDeviceToken postDeviceToken = null;
    public ArrayList<CharactersDataDictionary> charactersDataFilesArrayList = null;
    public ArrayList<ToolsDataDictionary> toolsDataFilesArrayList = null;
    public MainSavesDictionary mainSavesDictionary = null;
    public TimeSaveDictionary timeSaveDictionary = null;
    public final short DRAW_SLEEP_SECONDS = 20;
    public short drawSleepSeconds = 20;
    public boolean isRetina4 = false;
    public float density = 1.0f;
    int dispWidth = 0;
    int dispHeight = 0;
    public float finalWidth = BitmapDescriptorFactory.HUE_RED;
    public float finalHeight = BitmapDescriptorFactory.HUE_RED;
    public float notRetina4Height = BitmapDescriptorFactory.HUE_RED;
    public float isRetina4Height = BitmapDescriptorFactory.HUE_RED;
    public float offset44 = -44.0f;
    public boolean gcmCheckF = false;
    public boolean adColonyV4VCF = false;
    public Typeface typeface_FONTNAME_00 = null;
    private boolean onPauseF = true;
    private int nowStatus = -2;
    public short soundPlayCnt = -1;
    private boolean initImageF = false;
    CampaignJsonRequest campaignJsonRequest = null;
    public Bitmap optionBackGroundBitmap = null;
    public Bitmap starOnBitmap = null;
    public Bitmap starOffBitmap = null;
    public Bitmap shadow_0_Bitmap = null;
    public Bitmap shadow_1_Bitmap = null;
    public Bitmap listview_fastscrolldragview_Bitmap = null;
    public Bitmap tool_locked_mark_Bitmap = null;
    public ArrayList<Bitmap> egg0ImageArrayList = null;
    public ArrayList<Bitmap> egg1ImageArrayList = null;
    public ArrayList<Bitmap> character0Image0ArrayList = null;
    public ArrayList<Bitmap> character0RevImage0ArrayList = null;
    public ArrayList<Bitmap> character1Image0ArrayList = null;
    public ArrayList<Bitmap> tool1Level0Image0ArrayList = null;
    public ArrayList<Bitmap> tool1Level0Image1ArrayList = null;
    public ArrayList<Bitmap> tool1Level1Image0ArrayList = null;
    public ArrayList<Bitmap> tool1Level1Image1ArrayList = null;
    public ArrayList<Bitmap> tool1Level2Image0ArrayList = null;
    public ArrayList<Bitmap> tool1Level2Image1ArrayList = null;
    public ArrayList<Bitmap> tool2Level0Image0ArrayList = null;
    public ArrayList<Bitmap> tool2Level0Image1ArrayList = null;
    private Handler mainTimerHandler = null;
    private MainTimerRunnable mainTimerRunnable = null;
    private Thread mainTimerThread = null;
    private Runnable initImageRunnable = new Runnable() { // from class: com.idtinc.ckchickandduck.AppDelegate.1
        @Override // java.lang.Runnable
        public void run() throws IOException {
            AppDelegate.this.initImage();
        }
    };
    private Runnable saveTimeSaveDictionaryRunnable = new Runnable() { // from class: com.idtinc.ckchickandduck.AppDelegate.2
        @Override // java.lang.Runnable
        public void run() throws Throwable {
            AppDelegate.this.saveTimeSaveDictionaryLockF = true;
            AppDelegate.this.saveTimeSaveDictionary();
            AppDelegate.this.saveTimeSaveDictionaryLockF = false;
            Log.d("saveTimeSaveDictionaryRunnable", "run...saveTimeSaveDictionaryRunnable");
            if (AppDelegate.this.saveTimeSaveDictionaryNeedDestoryF.booleanValue()) {
                AppDelegate.this.saveTimeSaveDictionaryDestroy();
                AppDelegate.this.saveTimeSaveDictionaryNeedDestoryF = false;
            }
        }
    };
    private Runnable saveMainSavesDictionaryRunnable0 = new Runnable() { // from class: com.idtinc.ckchickandduck.AppDelegate.3
        @Override // java.lang.Runnable
        public void run() throws Throwable {
            Log.d("saveMainSavesDictionaryRunnable0", "saveMainSavesDictionaryLockF10: " + AppDelegate.this.saveMainSavesDictionaryLockF);
            AppDelegate.this.saveMainSavesDictionaryLockF = true;
            Log.d("saveMainSavesDictionaryRunnable0", "saveMainSavesDictionaryLockF11: " + AppDelegate.this.saveMainSavesDictionaryLockF);
            AppDelegate.this.saveMainSavesDictionaryWithType((short) 0);
            Log.d("saveMainSavesDictionaryRunnable0", "saveMainSavesDictionaryLockF12 :" + AppDelegate.this.saveMainSavesDictionaryLockF);
            AppDelegate.this.saveMainSavesDictionaryLockF = false;
            Log.d("saveMainSavesDictionaryRunnable0", "saveMainSavesDictionaryLockF13 :" + AppDelegate.this.saveMainSavesDictionaryLockF);
            Log.d("saveMainSavesDictionaryRunnable0", "run...saveMainSavesDictionaryRunnable0");
            if (AppDelegate.this.saveMainSavesDictionaryNeedDestoryF.booleanValue()) {
                AppDelegate.this.saveMainSavesDictionaryDestroy();
                AppDelegate.this.saveMainSavesDictionaryNeedDestoryF = false;
            }
        }
    };
    private Runnable saveMainSavesDictionaryRunnable1 = new Runnable() { // from class: com.idtinc.ckchickandduck.AppDelegate.4
        @Override // java.lang.Runnable
        public void run() throws Throwable {
            AppDelegate.this.saveMainSavesDictionaryLockF = true;
            AppDelegate.this.saveMainSavesDictionaryWithType((short) 1);
            AppDelegate.this.saveMainSavesDictionaryLockF = false;
            Log.d("saveMainSavesDictionaryRunnable1", "run...saveMainSavesDictionaryRunnable1");
            if (AppDelegate.this.saveMainSavesDictionaryNeedDestoryF.booleanValue()) {
                AppDelegate.this.saveMainSavesDictionaryDestroy();
                AppDelegate.this.saveMainSavesDictionaryNeedDestoryF = false;
            }
        }
    };

    public AppDelegateInterface getCurrentActivity() {
        return this.nowAppMainActivity;
    }

    public void setCurrentActivity(AppDelegateInterface mCurrentActivity) {
        this.nowAppMainActivity = mCurrentActivity;
    }

    public void initFirst(Display _defDisp, DisplayMetrics _dispMet) {
        this.onPauseF = false;
        this.nowStatus = -2;
        this.soundPlayCnt = (short) -1;
        this.initImageF = false;
        this.density = _dispMet.density;
        this.dispWidth = _defDisp.getWidth();
        this.dispHeight = _defDisp.getHeight();
        Log.d("MyApp", "Width=" + this.dispWidth);
        Log.d("MyApp", "Height=" + this.dispHeight);
        float floatWidth = this.dispWidth;
        float floatHeight = this.dispHeight;
        float screenRate = floatHeight / floatWidth;
        if (screenRate >= 1.775f) {
            this.isRetina4 = true;
            this.finalWidth = this.dispWidth;
            this.finalHeight = this.dispWidth * 1.775f;
        } else {
            this.isRetina4 = false;
            float checkFloatHeight = floatWidth * 1.5f;
            Log.d("MyApp", "checkFloatHeight=" + checkFloatHeight);
            if (checkFloatHeight <= floatHeight) {
                this.finalWidth = this.dispWidth;
                this.finalHeight = checkFloatHeight;
            } else {
                this.finalHeight = floatHeight;
                this.finalWidth = (this.finalHeight * 2.0f) / 3.0f;
            }
        }
        this.zoomRate = this.finalWidth / 320.0f;
        Log.d("MyApp", "finalWidth=" + this.finalWidth);
        Log.d("MyApp", "finalHeight=" + this.finalHeight);
        Log.d("MyApp", "zoomRate=" + this.zoomRate);
        this.offset44 = 44.0f * this.zoomRate;
        this.notRetina4Height = this.finalWidth * 1.5f;
        this.isRetina4Height = this.dispWidth * 1.775f;
        this.gcmCheckF = false;
        this.adColonyV4VCF = false;
        this.typeface_FONTNAME_00 = Typeface.createFromAsset(getAssets(), "APJapanesefontK.ttf");
    }

    public void readyDoInitSecond() throws IOException {
        initSecond();
    }

    public void initSecond() throws IOException {
        this.initImageHandlerThread = new HandlerThread("saveTimeSaveDictionary");
        this.initImageHandlerThread.start();
        this.initImageHandler = new Handler(this.initImageHandlerThread.getLooper());
        this.saveTimeSaveDictionaryNeedDestoryF = false;
        this.saveTimeSaveDictionaryLockF = false;
        this.saveTimeSaveDictionaryHandlerThread = new HandlerThread("saveTimeSaveDictionary");
        this.saveTimeSaveDictionaryHandlerThread.start();
        this.saveTimeSaveDictionaryHandler = new Handler(this.saveTimeSaveDictionaryHandlerThread.getLooper());
        this.saveMainSavesDictionaryNeedDestoryF = false;
        this.saveMainSavesDictionaryLockF = false;
        this.saveMainSavesDictionaryHandlerThread = new HandlerThread("saveMainSavesDictionary");
        this.saveMainSavesDictionaryHandlerThread.start();
        this.saveMainSavesDictionaryHandler = new Handler(this.saveMainSavesDictionaryHandlerThread.getLooper());
        getDeviceID();
        loadSavesFile();
        initBGMSound();
        initSeSound();
        initCampaignJsonRequest();
        this.mainTimerHandler = new Handler() { // from class: com.idtinc.ckchickandduck.AppDelegate.5
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AppDelegate.this.doMainLoop();
            }
        };
        this.mainTimerRunnable = new MainTimerRunnable();
        this.mainTimerRunnable.start();
        this.mainTimerThread = new Thread(this.mainTimerRunnable);
        this.mainTimerThread.start();
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.readyDoInitViews();
        }
    }

    class MainTimerRunnable implements Runnable {
        public boolean runFlag;

        public MainTimerRunnable() {
            this.runFlag = false;
            this.runFlag = false;
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            while (this.runFlag) {
                try {
                    Message msg = new Message();
                    AppDelegate.this.mainTimerHandler.sendMessage(msg);
                    Thread.sleep(100L);
                } catch (Exception e) {
                }
            }
        }

        public void stop() {
            this.runFlag = false;
        }

        public void start() {
            this.runFlag = true;
        }
    }

    public void doMainLoop() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.doMainLoop();
        }
    }

    public void initCampaignJsonRequest() {
        if (this.campaignJsonRequest == null) {
            this.campaignJsonRequest = new CampaignJsonRequest();
            this.campaignJsonRequest.init(this);
            this.campaignJsonRequest.startRequest(false);
        }
    }

    public void doInitImage() {
        if (this.initImageHandler != null) {
            this.initImageHandler.post(this.initImageRunnable);
        }
    }

    public void initIconImage() throws IOException {
        Bitmap iconBitmap = null;
        AssetManager asm = getAssets();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.Options opt2 = new BitmapFactory.Options();
        opt2.inJustDecodeBounds = false;
        opt2.inPurgeable = true;
        opt2.inInputShareable = true;
        try {
            InputStream inputStream = asm.open("png/icon.png");
            BufferedInputStream buf = new BufferedInputStream(inputStream);
            BitmapFactory.decodeStream(buf, null, opt);
            iconBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
            if (iconBitmap != null) {
                String filePath = "/data/data/" + getPackageName() + "/files";
                savePic(iconBitmap, filePath, "icon.png");
            }
            inputStream.close();
        } catch (IOException e) {
        }
        if (iconBitmap != null) {
            if (!iconBitmap.isRecycled()) {
                iconBitmap.recycle();
            }
        }
    }

    public void initImage() throws IOException {
        if (!this.initImageF) {
            this.initImageF = true;
            initIconImage();
            AssetManager asm = getAssets();
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inSampleSize = 1;
            opt2.inPreferredConfig = Bitmap.Config.RGB_565;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            if (this.optionBackGroundBitmap != null) {
                if (!this.optionBackGroundBitmap.isRecycled()) {
                    this.optionBackGroundBitmap.recycle();
                }
                this.optionBackGroundBitmap = null;
            }
            try {
                InputStream inputStream = asm.open("png/MainGame/option_bg.png");
                this.optionBackGroundBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
            if (this.starOnBitmap != null) {
                if (!this.starOnBitmap.isRecycled()) {
                    this.starOnBitmap.recycle();
                }
                this.starOnBitmap = null;
            }
            try {
                InputStream inputStream2 = asm.open("png/MainGame/level_star_on.png");
                this.starOnBitmap = BitmapFactory.decodeStream(inputStream2, null, opt2);
                inputStream2.close();
            } catch (IOException e2) {
            }
            if (this.starOffBitmap != null) {
                if (!this.starOffBitmap.isRecycled()) {
                    this.starOffBitmap.recycle();
                }
                this.starOffBitmap = null;
            }
            try {
                InputStream inputStream3 = asm.open("png/MainGame/level_star_off.png");
                this.starOffBitmap = BitmapFactory.decodeStream(inputStream3, null, opt2);
                inputStream3.close();
            } catch (IOException e3) {
            }
            if (this.shadow_0_Bitmap != null) {
                if (!this.shadow_0_Bitmap.isRecycled()) {
                    this.shadow_0_Bitmap.recycle();
                }
                this.shadow_0_Bitmap = null;
            }
            try {
                InputStream inputStream4 = asm.open("png/Character/character_shadow_0.png");
                this.shadow_0_Bitmap = BitmapFactory.decodeStream(inputStream4, null, opt2);
                inputStream4.close();
            } catch (IOException e4) {
            }
            if (this.shadow_1_Bitmap != null) {
                if (!this.shadow_1_Bitmap.isRecycled()) {
                    this.shadow_1_Bitmap.recycle();
                }
                this.shadow_1_Bitmap = null;
            }
            try {
                InputStream inputStream5 = asm.open("png/Character/character_shadow_1.png");
                this.shadow_1_Bitmap = BitmapFactory.decodeStream(inputStream5, null, opt2);
                inputStream5.close();
            } catch (IOException e5) {
            }
            if (this.listview_fastscrolldragview_Bitmap != null) {
                if (!this.listview_fastscrolldragview_Bitmap.isRecycled()) {
                    this.listview_fastscrolldragview_Bitmap.recycle();
                }
                this.listview_fastscrolldragview_Bitmap = null;
            }
            try {
                InputStream inputStream6 = asm.open("png/Button/fast_scroll.png");
                this.listview_fastscrolldragview_Bitmap = BitmapFactory.decodeStream(inputStream6, null, opt2);
                inputStream6.close();
            } catch (IOException e6) {
            }
            if (this.tool_locked_mark_Bitmap != null) {
                if (!this.tool_locked_mark_Bitmap.isRecycled()) {
                    this.tool_locked_mark_Bitmap.recycle();
                }
                this.tool_locked_mark_Bitmap = null;
            }
            try {
                InputStream inputStream7 = asm.open("png/Tool/tool_locked_mark.png");
                this.tool_locked_mark_Bitmap = BitmapFactory.decodeStream(inputStream7, null, opt2);
                inputStream7.close();
            } catch (IOException e7) {
            }
            if (this.egg0ImageArrayList != null) {
                while (this.egg0ImageArrayList.size() > 0) {
                    Bitmap bitmap = this.egg0ImageArrayList.get(0);
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                    }
                    this.egg0ImageArrayList.remove(0);
                }
                this.egg0ImageArrayList.clear();
                this.egg0ImageArrayList = null;
            }
            this.egg0ImageArrayList = new ArrayList<>();
            if (this.egg1ImageArrayList != null) {
                while (this.egg1ImageArrayList.size() > 0) {
                    Bitmap bitmap2 = this.egg1ImageArrayList.get(0);
                    if (bitmap2 != null) {
                        if (!bitmap2.isRecycled()) {
                            bitmap2.recycle();
                        }
                    }
                    this.egg1ImageArrayList.remove(0);
                }
                this.egg1ImageArrayList.clear();
                this.egg1ImageArrayList = null;
            }
            this.egg1ImageArrayList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                try {
                    InputStream inputStream8 = asm.open("png/Egg/egg_0_0_" + i + ".png");
                    Bitmap newBitmap0 = BitmapFactory.decodeStream(inputStream8, null, opt2);
                    this.egg0ImageArrayList.add(newBitmap0);
                    inputStream8.close();
                } catch (IOException e8) {
                }
                try {
                    InputStream inputStream9 = asm.open("png/Egg/egg_1_0_" + i + ".png");
                    Bitmap newBitmap02 = BitmapFactory.decodeStream(inputStream9, null, opt2);
                    this.egg1ImageArrayList.add(newBitmap02);
                    inputStream9.close();
                } catch (IOException e9) {
                }
            }
            if (this.character0Image0ArrayList != null) {
                while (this.character0Image0ArrayList.size() > 0) {
                    Bitmap bitmap3 = this.character0Image0ArrayList.get(0);
                    if (bitmap3 != null) {
                        if (!bitmap3.isRecycled()) {
                            bitmap3.recycle();
                        }
                    }
                    this.character0Image0ArrayList.remove(0);
                }
                this.character0Image0ArrayList.clear();
                this.character0Image0ArrayList = null;
            }
            this.character0Image0ArrayList = new ArrayList<>();
            if (this.character0RevImage0ArrayList != null) {
                while (this.character0RevImage0ArrayList.size() > 0) {
                    Bitmap bitmap4 = this.character0RevImage0ArrayList.get(0);
                    if (bitmap4 != null) {
                        if (!bitmap4.isRecycled()) {
                            bitmap4.recycle();
                        }
                    }
                    this.character0RevImage0ArrayList.remove(0);
                }
                this.character0RevImage0ArrayList.clear();
                this.character0RevImage0ArrayList = null;
            }
            this.character0RevImage0ArrayList = new ArrayList<>();
            if (this.character1Image0ArrayList != null) {
                while (this.character1Image0ArrayList.size() > 0) {
                    Bitmap bitmap5 = this.character1Image0ArrayList.get(0);
                    if (bitmap5 != null) {
                        if (!bitmap5.isRecycled()) {
                            bitmap5.recycle();
                        }
                    }
                    this.character1Image0ArrayList.remove(0);
                }
                this.character1Image0ArrayList.clear();
                this.character1Image0ArrayList = null;
            }
            this.character1Image0ArrayList = new ArrayList<>();
            for (int i2 = 0; i2 < this.CHARACTE_0_ALL_CNT; i2++) {
                if (i2 == 84 || i2 == 89 || i2 == 90 || i2 == 91 || i2 == 92 || i2 == 93 || i2 == 94 || i2 == 95 || i2 == 96 || i2 == 97 || i2 == 98 || i2 == 99 || i2 == 100 || i2 == 101 || i2 == 102 || i2 == 103) {
                    try {
                        InputStream inputStream10 = asm.open("png/Character/character_rev/character_0_" + i2 + "_0_1.png");
                        Bitmap newBitmap03 = BitmapFactory.decodeStream(inputStream10, null, opt2);
                        this.character0RevImage0ArrayList.add(newBitmap03);
                        inputStream10.close();
                    } catch (IOException e10) {
                    }
                }
                try {
                    InputStream inputStream11 = asm.open("png/Character/character_0/character_0_" + i2 + "_0_0.png");
                    Bitmap newBitmap04 = BitmapFactory.decodeStream(inputStream11, null, opt2);
                    this.character0Image0ArrayList.add(newBitmap04);
                    inputStream11.close();
                } catch (IOException e11) {
                }
            }
            for (int i3 = 0; i3 < this.CHARACTE_1_ALL_CNT; i3++) {
                try {
                    InputStream inputStream12 = asm.open("png/Character/character_1/character_1_" + i3 + "_0_0.png");
                    Bitmap newBitmap05 = BitmapFactory.decodeStream(inputStream12, null, opt2);
                    this.character1Image0ArrayList.add(newBitmap05);
                    inputStream12.close();
                } catch (IOException e12) {
                }
            }
            if (this.tool1Level0Image0ArrayList != null) {
                while (this.tool1Level0Image0ArrayList.size() > 0) {
                    Bitmap bitmap6 = this.tool1Level0Image0ArrayList.get(0);
                    if (bitmap6 != null) {
                        if (!bitmap6.isRecycled()) {
                            bitmap6.recycle();
                        }
                    }
                    this.tool1Level0Image0ArrayList.remove(0);
                }
                this.tool1Level0Image0ArrayList.clear();
                this.tool1Level0Image0ArrayList = null;
            }
            this.tool1Level0Image0ArrayList = new ArrayList<>();
            if (this.tool1Level0Image1ArrayList != null) {
                while (this.tool1Level0Image1ArrayList.size() > 0) {
                    Bitmap bitmap7 = this.tool1Level0Image1ArrayList.get(0);
                    if (bitmap7 != null) {
                        if (!bitmap7.isRecycled()) {
                            bitmap7.recycle();
                        }
                    }
                    this.tool1Level0Image1ArrayList.remove(0);
                }
                this.tool1Level0Image1ArrayList.clear();
                this.tool1Level0Image1ArrayList = null;
            }
            this.tool1Level0Image1ArrayList = new ArrayList<>();
            if (this.tool1Level1Image0ArrayList != null) {
                while (this.tool1Level1Image0ArrayList.size() > 0) {
                    Bitmap bitmap8 = this.tool1Level1Image0ArrayList.get(0);
                    if (bitmap8 != null) {
                        if (!bitmap8.isRecycled()) {
                            bitmap8.recycle();
                        }
                    }
                    this.tool1Level1Image0ArrayList.remove(0);
                }
                this.tool1Level1Image0ArrayList.clear();
                this.tool1Level1Image0ArrayList = null;
            }
            this.tool1Level1Image0ArrayList = new ArrayList<>();
            if (this.tool1Level1Image1ArrayList != null) {
                while (this.tool1Level1Image1ArrayList.size() > 0) {
                    Bitmap bitmap9 = this.tool1Level1Image1ArrayList.get(0);
                    if (bitmap9 != null) {
                        if (!bitmap9.isRecycled()) {
                            bitmap9.recycle();
                        }
                    }
                    this.tool1Level1Image1ArrayList.remove(0);
                }
                this.tool1Level1Image1ArrayList.clear();
                this.tool1Level1Image1ArrayList = null;
            }
            this.tool1Level1Image1ArrayList = new ArrayList<>();
            if (this.tool1Level2Image0ArrayList != null) {
                while (this.tool1Level2Image0ArrayList.size() > 0) {
                    Bitmap bitmap10 = this.tool1Level2Image0ArrayList.get(0);
                    if (bitmap10 != null) {
                        if (!bitmap10.isRecycled()) {
                            bitmap10.recycle();
                        }
                    }
                    this.tool1Level2Image0ArrayList.remove(0);
                }
                this.tool1Level2Image0ArrayList.clear();
                this.tool1Level2Image0ArrayList = null;
            }
            this.tool1Level2Image0ArrayList = new ArrayList<>();
            if (this.tool1Level2Image1ArrayList != null) {
                while (this.tool1Level2Image1ArrayList.size() > 0) {
                    Bitmap bitmap11 = this.tool1Level2Image1ArrayList.get(0);
                    if (bitmap11 != null) {
                        if (!bitmap11.isRecycled()) {
                            bitmap11.recycle();
                        }
                    }
                    this.tool1Level2Image1ArrayList.remove(0);
                }
                this.tool1Level2Image1ArrayList.clear();
                this.tool1Level2Image1ArrayList = null;
            }
            this.tool1Level2Image1ArrayList = new ArrayList<>();
            for (int i4 = 0; i4 < this.TOOL_1_ALL_CNT; i4++) {
                try {
                    InputStream inputStream13 = asm.open("png/Tool/Tool1/tool_1_" + i4 + "_0_0.png");
                    Bitmap bitmap12 = BitmapFactory.decodeStream(inputStream13, null, opt2);
                    this.tool1Level0Image0ArrayList.add(bitmap12);
                    inputStream13.close();
                } catch (IOException e13) {
                }
                try {
                    InputStream inputStream14 = asm.open("png/Tool/Tool1/tool_1_" + i4 + "_0_1.png");
                    Bitmap bitmap13 = BitmapFactory.decodeStream(inputStream14, null, opt2);
                    this.tool1Level0Image1ArrayList.add(bitmap13);
                    inputStream14.close();
                } catch (IOException e14) {
                }
                try {
                    InputStream inputStream15 = asm.open("png/Tool/Tool1/tool_1_" + i4 + "_1_0.png");
                    Bitmap bitmap14 = BitmapFactory.decodeStream(inputStream15, null, opt2);
                    this.tool1Level1Image0ArrayList.add(bitmap14);
                    inputStream15.close();
                } catch (IOException e15) {
                }
                try {
                    InputStream inputStream16 = asm.open("png/Tool/Tool1/tool_1_" + i4 + "_1_1.png");
                    Bitmap bitmap15 = BitmapFactory.decodeStream(inputStream16, null, opt2);
                    this.tool1Level1Image1ArrayList.add(bitmap15);
                    inputStream16.close();
                } catch (IOException e16) {
                }
                try {
                    InputStream inputStream17 = asm.open("png/Tool/Tool1/tool_1_" + i4 + "_2_0.png");
                    Bitmap bitmap16 = BitmapFactory.decodeStream(inputStream17, null, opt2);
                    this.tool1Level2Image0ArrayList.add(bitmap16);
                    inputStream17.close();
                } catch (IOException e17) {
                }
                try {
                    InputStream inputStream18 = asm.open("png/Tool/Tool1/tool_1_" + i4 + "_2_1.png");
                    Bitmap bitmap17 = BitmapFactory.decodeStream(inputStream18, null, opt2);
                    this.tool1Level2Image1ArrayList.add(bitmap17);
                    inputStream18.close();
                } catch (IOException e18) {
                }
            }
            if (this.tool2Level0Image0ArrayList != null) {
                while (this.tool2Level0Image0ArrayList.size() > 0) {
                    Bitmap bitmap18 = this.tool2Level0Image0ArrayList.get(0);
                    if (bitmap18 != null) {
                        if (!bitmap18.isRecycled()) {
                            bitmap18.recycle();
                        }
                    }
                    this.tool2Level0Image0ArrayList.remove(0);
                }
                this.tool2Level0Image0ArrayList.clear();
                this.tool2Level0Image0ArrayList = null;
            }
            this.tool2Level0Image0ArrayList = new ArrayList<>();
            if (this.tool2Level0Image1ArrayList != null) {
                while (this.tool2Level0Image1ArrayList.size() > 0) {
                    Bitmap bitmap19 = this.tool2Level0Image1ArrayList.get(0);
                    if (bitmap19 != null) {
                        if (!bitmap19.isRecycled()) {
                            bitmap19.recycle();
                        }
                    }
                    this.tool2Level0Image1ArrayList.remove(0);
                }
                this.tool2Level0Image1ArrayList.clear();
                this.tool2Level0Image1ArrayList = null;
            }
            this.tool2Level0Image1ArrayList = new ArrayList<>();
            for (int i5 = 0; i5 < this.TOOL_2_ALL_CNT; i5++) {
                try {
                    InputStream inputStream19 = asm.open("png/Tool/Tool2/tool_2_" + i5 + "_0_0.png");
                    Bitmap bitmap20 = BitmapFactory.decodeStream(inputStream19, null, opt2);
                    this.tool2Level0Image0ArrayList.add(bitmap20);
                    inputStream19.close();
                } catch (IOException e19) {
                }
                try {
                    InputStream inputStream20 = asm.open("png/Tool/Tool2/tool_2_" + i5 + "_0_1.png");
                    Bitmap bitmap21 = BitmapFactory.decodeStream(inputStream20, null, opt2);
                    this.tool2Level0Image1ArrayList.add(bitmap21);
                    inputStream20.close();
                } catch (IOException e20) {
                }
            }
            System.gc();
        }
    }

    public void initBGMSound() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
        }
    }

    public void doBGMStop() throws IllegalStateException {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
        }
    }

    public void doBGMPlay(int _bgmIndex) throws IllegalStateException, IOException, IllegalArgumentException {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
            if (_bgmIndex >= 0 && _bgmIndex <= 2) {
                if (this.defaultSharedPreferences == null) {
                    this.defaultSharedPreferences = getSharedPreferences("default", 0);
                }
                if (this.defaultSharedPreferences != null && this.defaultSharedPreferences.getBoolean("bgm_switch", false)) {
                    try {
                        String path = "music/bgm/bgm_00" + _bgmIndex + ".mp3";
                        AssetFileDescriptor afd = getAssets().openFd(path);
                        this.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        afd.close();
                        this.mediaPlayer.setLooping(true);
                        this.mediaPlayer.prepare();
                        this.mediaPlayer.start();
                    } catch (IOException e) {
                    } catch (IllegalArgumentException e2) {
                    } catch (SecurityException e3) {
                    }
                }
            }
        }
    }

    public void initSeSound() {
        if (this.soundPool == null) {
            this.soundPool = new SoundPool(18, 3, 5);
            this.soundMap = new HashMap<>();
            this.soundMap.put(0, Integer.valueOf(this.soundPool.load(this, R.raw.se000_start, 1)));
            this.soundMap.put(1, Integer.valueOf(this.soundPool.load(this, R.raw.se001_yes, 1)));
            this.soundMap.put(2, Integer.valueOf(this.soundPool.load(this, R.raw.se002_no, 1)));
            this.soundMap.put(3, Integer.valueOf(this.soundPool.load(this, R.raw.se003_click, 1)));
            this.soundMap.put(4, Integer.valueOf(this.soundPool.load(this, R.raw.se004_open, 1)));
            this.soundMap.put(5, Integer.valueOf(this.soundPool.load(this, R.raw.se005_alert0, 1)));
            this.soundMap.put(6, Integer.valueOf(this.soundPool.load(this, R.raw.se006_alert1, 1)));
            this.soundMap.put(7, Integer.valueOf(this.soundPool.load(this, R.raw.se007_buy, 1)));
            this.soundMap.put(8, Integer.valueOf(this.soundPool.load(this, R.raw.se008_sell, 1)));
            this.soundMap.put(9, Integer.valueOf(this.soundPool.load(this, R.raw.se009_clean, 1)));
            this.soundMap.put(10, Integer.valueOf(this.soundPool.load(this, R.raw.se010_fix, 1)));
            this.soundMap.put(11, Integer.valueOf(this.soundPool.load(this, R.raw.se011_break, 1)));
            this.soundMap.put(12, Integer.valueOf(this.soundPool.load(this, R.raw.se012_chick0, 1)));
            this.soundMap.put(13, Integer.valueOf(this.soundPool.load(this, R.raw.se013_error, 1)));
            this.soundMap.put(14, Integer.valueOf(this.soundPool.load(this, R.raw.se014_duck0, 1)));
            this.soundMap.put(15, Integer.valueOf(this.soundPool.load(this, R.raw.se015_timedooropen, 1)));
            this.soundMap.put(16, Integer.valueOf(this.soundPool.load(this, R.raw.se016_timedoorclick, 1)));
            this.soundMap.put(17, Integer.valueOf(this.soundPool.load(this, R.raw.se017_timedoorreceive, 1)));
        }
    }

    public void doSePoolPlay(int _seIndex) {
        if (this.soundPool != null && this.soundMap != null && this.soundPlayCnt < 0 && _seIndex >= 0 && _seIndex < 18) {
            AudioManager am = (AudioManager) getSystemService("audio");
            float maxVolume = am.getStreamMaxVolume(3);
            float currentVolume = am.getStreamVolume(3);
            float playVolume = currentVolume / maxVolume;
            if (playVolume < BitmapDescriptorFactory.HUE_RED) {
                playVolume = BitmapDescriptorFactory.HUE_RED;
            } else if (playVolume > 1.0f) {
                playVolume = 1.0f;
            }
            this.soundPool.play(this.soundMap.get(Integer.valueOf(_seIndex)).intValue(), playVolume, playVolume, 0, 0, 1.0f);
            this.soundPlayCnt = (short) 0;
        }
    }

    public void doSoundPoolPlay(int _seIndex) {
        Log.d("changeMainMenuLayoutNowStatus", "pppppppppppppppppppppppp =" + _seIndex);
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences != null && this.defaultSharedPreferences.getBoolean("sound_switch", false)) {
            doSePoolPlay(_seIndex);
        }
    }

    public void do_idt_account_logout() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences != null) {
            set_idt_account_id("");
            set_idt_account_password("");
            set_idt_account_logged_in(false);
        }
    }

    public Boolean set_idt_account_id(String idt_account_id) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || idt_account_id == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("idt_account_id", idt_account_id);
        editor.commit();
        return true;
    }

    public String get_idt_account_id() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String idt_account_id = this.defaultSharedPreferences.getString("idt_account_id", "");
        return idt_account_id;
    }

    public Boolean set_idt_account_password(String idt_account_password) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || idt_account_password == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("idt_account_password", idt_account_password);
        editor.commit();
        return true;
    }

    public String get_idt_account_password() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String idt_account_password = this.defaultSharedPreferences.getString("idt_account_password", "");
        return idt_account_password;
    }

    public Boolean set_idt_account_logged_in(Boolean idt_account_logged_in) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putBoolean("idt_account_logged_in", idt_account_logged_in.booleanValue());
        editor.commit();
        return true;
    }

    public Boolean get_idt_account_logged_in() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return false;
        }
        Boolean idt_account_logged_in = Boolean.valueOf(this.defaultSharedPreferences.getBoolean("idt_account_logged_in", false));
        return idt_account_logged_in;
    }

    public Boolean set_fb_account_logged_in(Boolean _fb_account_logged_in) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putBoolean("fb_account_logged_in", _fb_account_logged_in.booleanValue());
        editor.commit();
        return true;
    }

    public Boolean get_fb_account_logged_in() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return false;
        }
        Boolean fb_account_logged_in = Boolean.valueOf(this.defaultSharedPreferences.getBoolean("fb_account_logged_in", false));
        return fb_account_logged_in;
    }

    public void do_fb_account_logout() {
        set_fb_account_id("");
        set_fb_account_user_name("");
        set_fb_account_first_name("");
        set_fb_account_last_name("");
        set_fb_account_name("");
        set_fb_account_email("");
        set_fb_account_gender("");
        set_fb_account_birthday("");
        set_fb_account_logged_in(false);
    }

    public Boolean set_fb_account_id(String fb_account_id) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_id == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_id", fb_account_id);
        editor.commit();
        return true;
    }

    public String get_fb_account_id() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_id = this.defaultSharedPreferences.getString("fb_account_id", "");
        return fb_account_id;
    }

    public String get_fb_account_password() {
        String fb_account_password = "";
        String fb_account_id = get_fb_account_id();
        if (fb_account_id != null && fb_account_id.length() > 0) {
            for (int i = 0; i < 20; i++) {
                if (fb_account_id.length() >= i + 1) {
                    String fb_account_id_next_word = fb_account_id.substring(i, i + 1);
                    if (fb_account_id_next_word.equals("1")) {
                        fb_account_password = String.valueOf(fb_account_password) + "8";
                    } else if (fb_account_id_next_word.equals("2")) {
                        fb_account_password = String.valueOf(fb_account_password) + "i";
                    } else if (fb_account_id_next_word.equals("3")) {
                        fb_account_password = String.valueOf(fb_account_password) + "6";
                    } else if (fb_account_id_next_word.equals("4")) {
                        fb_account_password = String.valueOf(fb_account_password) + "g";
                    } else if (fb_account_id_next_word.equals("5")) {
                        fb_account_password = String.valueOf(fb_account_password) + "4";
                    } else if (fb_account_id_next_word.equals("6")) {
                        fb_account_password = String.valueOf(fb_account_password) + "e";
                    } else if (fb_account_id_next_word.equals("7")) {
                        fb_account_password = String.valueOf(fb_account_password) + "2";
                    } else if (fb_account_id_next_word.equals("8")) {
                        fb_account_password = String.valueOf(fb_account_password) + "c";
                    } else if (fb_account_id_next_word.equals("9")) {
                        fb_account_password = String.valueOf(fb_account_password) + "0";
                    } else {
                        fb_account_password = String.valueOf(fb_account_password) + "a";
                    }
                } else {
                    fb_account_password = String.valueOf(fb_account_password) + "a";
                }
            }
        }
        return fb_account_password;
    }

    public Boolean set_fb_account_user_name(String fb_account_user_name) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_user_name == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_user_name", fb_account_user_name);
        editor.commit();
        return true;
    }

    public String get_fb_account_user_name() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_user_name = this.defaultSharedPreferences.getString("fb_account_user_name", "");
        return fb_account_user_name;
    }

    public Boolean set_fb_account_first_name(String fb_account_first_name) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_first_name == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_first_name", fb_account_first_name);
        editor.commit();
        return true;
    }

    public String get_fb_account_first_name() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_first_name = this.defaultSharedPreferences.getString("fb_account_first_name", "");
        return fb_account_first_name;
    }

    public Boolean set_fb_account_last_name(String fb_account_last_name) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_last_name == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_last_name", fb_account_last_name);
        editor.commit();
        return true;
    }

    public String get_fb_account_last_name() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_last_name = this.defaultSharedPreferences.getString("fb_account_last_name", "");
        return fb_account_last_name;
    }

    public Boolean set_fb_account_name(String fb_account_name) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_name == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_name", fb_account_name);
        editor.commit();
        return true;
    }

    public String get_fb_account_name() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_name = this.defaultSharedPreferences.getString("fb_account_name", "");
        return fb_account_name;
    }

    public Boolean set_fb_account_email(String fb_account_email) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_email == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_email", fb_account_email);
        editor.commit();
        return true;
    }

    public String get_fb_account_email() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_email = this.defaultSharedPreferences.getString("fb_account_email", "");
        return fb_account_email;
    }

    public Boolean set_fb_account_gender(String fb_account_gender) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_gender == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_gender", fb_account_gender);
        editor.commit();
        return true;
    }

    public String get_fb_account_gender() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_gender = this.defaultSharedPreferences.getString("fb_account_gender", "");
        return fb_account_gender;
    }

    public Boolean set_fb_account_birthday(String fb_account_birthday) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || fb_account_birthday == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("fb_account_birthday", fb_account_birthday);
        editor.commit();
        return true;
    }

    public String get_fb_account_birthday() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String fb_account_birthday = this.defaultSharedPreferences.getString("fb_account_birthday", "");
        return fb_account_birthday;
    }

    public void post_device_token() {
        if (this.postDeviceToken == null) {
            this.postDeviceToken = new PostDeviceToken(this);
        }
        if (this.postDeviceToken != null) {
            this.postDeviceToken.doUpload();
        }
    }

    public Boolean set_device_token(String device_token) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || device_token == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("device_token", device_token);
        editor.commit();
        return true;
    }

    public String get_device_token() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String device_token = this.defaultSharedPreferences.getString("device_token", "");
        return device_token;
    }

    public Boolean set_get_cp_block_date(String get_cp_block_date) {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null || get_cp_block_date == null) {
            return false;
        }
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        editor.putString("get_cp_block_date", get_cp_block_date);
        editor.commit();
        return true;
    }

    public String get_get_cp_block_date() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String get_cp_block_date = this.defaultSharedPreferences.getString("get_cp_block_date", "");
        return get_cp_block_date;
    }

    public boolean uninstallSoftware(Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, 0);
            return pInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMessageToStoreMessagesArray0(short _type_id, short _tool_id) {
        if (this.defaultSharedPreferences == null) {
            return false;
        }
        String storeMessagesString = this.defaultSharedPreferences.getString("store_messages_string0", null);
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        int messageInt = (_type_id * 1000) + _tool_id;
        if (storeMessagesString == null || storeMessagesString.length() <= 0) {
            editor.putString("store_messages_string0", new StringBuilder().append(messageInt).toString());
        } else {
            editor.putString("store_messages_string0", storeMessagesString + "," + messageInt);
        }
        editor.commit();
        return true;
    }

    public void loadSavesFile() throws IOException {
        this.defaultSharedPreferences = getSharedPreferences("default", 0);
        if (this.defaultSharedPreferences != null) {
            SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
            set_idt_account_logged_in(false);
            set_fb_account_logged_in(false);
            editor.putBoolean("campaign_char_0_26", false);
            editor.putBoolean("campaign_char_0_27", false);
            editor.putBoolean("campaign_char_0_32", false);
            editor.putBoolean("campaign_char_0_48", false);
            editor.putBoolean("campaign_char_0_49", false);
            editor.putBoolean("campaign_char_0_60", false);
            editor.putBoolean("campaign_char_0_61", false);
            editor.putBoolean("campaign_char_0_62", false);
            editor.putBoolean("campaign_char_0_63", false);
            editor.putBoolean("campaign_char_0_64", false);
            editor.putBoolean("campaign_char_0_65", false);
            editor.putBoolean("campaign_char_0_66", false);
            editor.putBoolean("campaign_char_0_67", false);
            editor.putBoolean("campaign_char_0_78", false);
            editor.putBoolean("campaign_char_0_79", false);
            editor.putBoolean("campaign_char_0_80", false);
            editor.putBoolean("campaign_char_0_82", false);
            editor.putBoolean("campaign_char_0_83", false);
            editor.putBoolean("campaign_char_0_84", false);
            editor.putBoolean("campaign_char_0_85", false);
            editor.putBoolean("campaign_char_0_86", false);
            editor.putBoolean("campaign_char_0_87", false);
            editor.putBoolean("campaign_char_1_35", false);
            editor.putBoolean("campaign_char_1_47", false);
            editor.putBoolean("campaign_char_1_48", false);
            editor.putBoolean("campaign_char_1_49", false);
            editor.putBoolean("campaign_char_1_50", false);
            editor.putBoolean("campaign_char_1_51", false);
            editor.putBoolean("nend_full_active", false);
            editor.putBoolean("zucks_full_active", false);
            editor.putBoolean("imobile_full_active", false);
            editor.putBoolean("imobile_wall_active", false);
            editor.putBoolean("adcolony_interstisial_active", false);
            editor.putBoolean("aoto_open_bonus_page", false);
            editor.putBoolean("full_ad_active", true);
            editor.commit();
        }
        checkCampaignChickenUnlock();
        loadMainSavesDictionary();
        if (this.defaultSharedPreferences != null && this.defaultSharedPreferences.getInt("version_level", -1) < 0 && this.mainSavesDictionary != null && this.mainSavesDictionary.getVersionLevel() >= 0) {
            SharedPreferences.Editor editor2 = this.defaultSharedPreferences.edit();
            editor2.putInt("version_level", this.mainSavesDictionary.getVersionLevel());
            editor2.putString("version_number", this.mainSavesDictionary.getVersionNumber());
            editor2.commit();
        }
        Boolean initF = false;
        Boolean versionUpF = false;
        if (this.defaultSharedPreferences == null || this.defaultSharedPreferences.getInt("version_level", -1) < 0) {
            initF = true;
            versionUpF = true;
        } else if (this.defaultSharedPreferences.getInt("version_level", -1) < 30) {
            initF = false;
            versionUpF = true;
        }
        Log.d("versionUpF", "versionUpF:" + versionUpF);
        Log.d("initF", "initF:" + initF);
        if (versionUpF.booleanValue()) {
            initCharactersDataFilesArray();
            initToolsDataFilesArray();
            if (this.defaultSharedPreferences != null) {
                SharedPreferences.Editor editor3 = this.defaultSharedPreferences.edit();
                if (initF.booleanValue()) {
                    set_idt_account_id("");
                    set_idt_account_password("");
                    editor3.putBoolean("idt_account_keep_me_signed_in", true);
                    editor3.putBoolean("bgm_switch", true);
                    editor3.putBoolean("sound_switch", true);
                    editor3.putBoolean("vibration_switch", true);
                    editor3.putBoolean("cook_alarm", true);
                    editor3.putBoolean("gameCenter_auto_send", false);
                    editor3.putBoolean("openFeint_auto_send", false);
                    editor3.putBoolean("init_manual_kitchen", false);
                    editor3.putBoolean("init_manual_farm", false);
                    editor3.putBoolean("init_manual_store", false);
                    set_get_cp_block_date("");
                    editor3.putBoolean("gift_tool_2_35", false);
                }
                editor3.commit();
            }
            if (initF.booleanValue()) {
                if (this.defaultSharedPreferences != null) {
                    SharedPreferences.Editor editor4 = this.defaultSharedPreferences.edit();
                    editor4.putInt("version_level", 30);
                    editor4.putString("version_number", "2.3.0");
                    editor4.putString("time_save_dictionary", "");
                    editor4.commit();
                }
                initMainSavesDictionary();
            } else {
                if (this.defaultSharedPreferences != null) {
                    SharedPreferences.Editor editor5 = this.defaultSharedPreferences.edit();
                    editor5.putInt("version_level", 30);
                    editor5.putString("version_number", "2.3.0");
                    editor5.commit();
                    Log.d("version_level", "defaultSharedPreferences version_level:" + this.defaultSharedPreferences.getInt("version_level", -1));
                    Log.d("version_number", "defaultSharedPreferences version_number:" + this.defaultSharedPreferences.getString("version_number", ""));
                }
                if (this.mainSavesDictionary != null) {
                    this.mainSavesDictionary.setVersionLevel((short) 30);
                    this.mainSavesDictionary.setVersionNumber("2.3.0");
                } else {
                    initMainSavesDictionary();
                }
            }
        } else {
            if (!loadCharactersDataFilesArray().booleanValue()) {
                initCharactersDataFilesArray();
            }
            if (!loadToolsDataFilesArray().booleanValue()) {
                initToolsDataFilesArray();
            }
            if (this.mainSavesDictionary == null) {
                initMainSavesDictionary();
            }
        }
        if (this.mainSavesDictionary.timeSavesArrayList == null) {
            this.mainSavesDictionary.initTimeSavesArrayList();
        }
        if (this.mainSavesDictionary != null) {
            this.mainSavesDictionary.setVersionLevel((short) 30);
            this.mainSavesDictionary.setVersionNumber("2.3.0");
        }
        Log.e("version_level", "defaultSharedPreferences version_level:" + this.defaultSharedPreferences.getInt("version_level", -1));
        Log.e("version_number", "defaultSharedPreferences version_number:" + this.defaultSharedPreferences.getString("version_number", ""));
        doSaveMainSavesDictionaryOperationWithType((short) 0);
    }

    public void initCharactersDataFilesArray() {
        this.charactersDataFilesArrayList = null;
        this.charactersDataFilesArrayList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            try {
                CharactersDataDictionary newCharactersDataDictionary = initNewCharactersDataFileDictionaryWithFileName("characters_file_" + i + ".xml");
                if (newCharactersDataDictionary != null) {
                    newCharactersDataDictionary.setEggId((short) i);
                    this.charactersDataFilesArrayList.add(newCharactersDataDictionary);
                }
            } catch (Throwable th) {
            }
        }
        Log.d("charactersDataFilesArrayList", "charactersDataFilesArrayList.size=" + this.charactersDataFilesArrayList.size());
        if (this.charactersDataFilesArrayList != null && saveCharactersDataFilesArray().booleanValue()) {
            Log.d("charactersDataFilesArrayList", "charactersDataFilesArrayList.size" + this.charactersDataFilesArrayList.size());
        }
    }

    public CharactersDataDictionary initNewCharactersDataFileDictionaryWithFileName(String _filename) throws Throwable {
        Log.d("initNewCharactersDataFileDictionaryWithFileName", _filename);
        CharactersDataDictionary newCharactersDataDictionary = new CharactersDataDictionary();
        InputStream inputStream = getResources().getAssets().open(_filename);
        if (inputStream != null) {
            Log.d("<CharacterData>", "<CharacterData>");
            CharacterDataDictionarySAXService characterDataSAXService = new CharacterDataDictionarySAXService();
            newCharactersDataDictionary.charactersDataArrayList = null;
            newCharactersDataDictionary.charactersDataArrayList = characterDataSAXService.getCharacters(inputStream);
        } else {
            Log.d("characterData", "inputStream == null");
        }
        return newCharactersDataDictionary;
    }

    public Boolean loadCharactersDataFilesArray() throws IOException {
        this.charactersDataFilesArrayList = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            try {
                fileInputStream = openFileInput("characters_data_files.dat");
                ObjectInputStream objectInputStream2 = new ObjectInputStream(fileInputStream);
                if (objectInputStream2 != null) {
                    try {
                        this.charactersDataFilesArrayList = (ArrayList) objectInputStream2.readObject();
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e) {
                        }
                    } catch (OptionalDataException e2) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e3) {
                        }
                    } catch (IOException e4) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e5) {
                        }
                    } catch (ClassNotFoundException e6) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e7) {
                        }
                    } catch (Throwable th) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e8) {
                        }
                        throw th;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e9) {
                    }
                }
            } catch (IOException e10) {
                Log.d("loadCharactersDataFilesArray", "load fis is not exists");
                if (0 != 0) {
                    try {
                        this.charactersDataFilesArrayList = (ArrayList) objectInputStream.readObject();
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e11) {
                        }
                    } catch (OptionalDataException e12) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e13) {
                        }
                    } catch (IOException e14) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e15) {
                        }
                    } catch (ClassNotFoundException e16) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e17) {
                        }
                    } catch (Throwable th2) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e18) {
                        }
                        throw th2;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e19) {
                    }
                }
            }
            return this.charactersDataFilesArrayList != null;
        } catch (Throwable th3) {
            if (0 != 0) {
                try {
                    this.charactersDataFilesArrayList = (ArrayList) objectInputStream.readObject();
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e20) {
                    }
                } catch (OptionalDataException e21) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e22) {
                    }
                } catch (IOException e23) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e24) {
                    }
                } catch (ClassNotFoundException e25) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e26) {
                    }
                } catch (Throwable th4) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e27) {
                    }
                    throw th4;
                }
            }
            if (fileInputStream == null) {
                throw th3;
            }
            try {
                fileInputStream.close();
                throw th3;
            } catch (IOException e28) {
                throw th3;
            }
        }
    }

    public Boolean saveCharactersDataFilesArray() throws Throwable {
        ObjectOutputStream objectOutputStream;
        if (this.charactersDataFilesArrayList == null) {
            return false;
        }
        Boolean.valueOf(false);
        ObjectOutputStream objectOutputStream2 = null;
        FileOutputStream fileOutputStream = null;
        try {
            try {
                fileOutputStream = openFileOutput("characters_data_files.dat", 0);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            objectOutputStream.writeObject(this.charactersDataFilesArrayList.clone());
            objectOutputStream.flush();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.reset();
                    objectOutputStream.close();
                } catch (IOException e2) {
                }
            }
            if (fileOutputStream == null) {
                return true;
            }
            try {
                fileOutputStream.close();
                return true;
            } catch (IOException e3) {
                return true;
            }
        } catch (Exception e4) {
            e = e4;
            objectOutputStream2 = objectOutputStream;
            Log.d("saveCharactersDataFilesArray", "saveCharactersDataFilesArray: error:" + e);
            if (objectOutputStream2 != null) {
                try {
                    objectOutputStream2.reset();
                    objectOutputStream2.close();
                } catch (IOException e5) {
                }
            }
            if (fileOutputStream == null) {
                return false;
            }
            try {
                fileOutputStream.close();
                return false;
            } catch (IOException e6) {
                return false;
            }
        } catch (Throwable th2) {
            th = th2;
            objectOutputStream2 = objectOutputStream;
            if (objectOutputStream2 != null) {
                try {
                    objectOutputStream2.reset();
                    objectOutputStream2.close();
                } catch (IOException e7) {
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    throw th;
                } catch (IOException e8) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public void initToolsDataFilesArray() {
        this.toolsDataFilesArrayList = null;
        this.toolsDataFilesArrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            try {
                ToolsDataDictionary newToolsDataDictionary = initNewToolsDataFileDictionaryWithFileName("tools_file_" + i + ".xml");
                if (newToolsDataDictionary != null) {
                    newToolsDataDictionary.setToolId((short) i);
                    this.toolsDataFilesArrayList.add(newToolsDataDictionary);
                    ToolsDataDictionary checkToolsDataDictionary = this.toolsDataFilesArrayList.get(i);
                    Iterator<ToolDataDictionary> it = checkToolsDataDictionary.toolsDataArrayList.iterator();
                    while (it.hasNext()) {
                        ToolDataDictionary toolDataDictionary = it.next();
                        Log.d("toolDataDictionary", "id: " + ((int) toolDataDictionary.getId()));
                        Log.d("toolDataDictionary", "title_ja: " + toolDataDictionary.getTitleJa());
                    }
                }
            } catch (Throwable th) {
            }
        }
        Log.d("toolsDataFilesArrayList", "toolsDataFilesArrayList.size=" + this.toolsDataFilesArrayList.size());
        if (this.toolsDataFilesArrayList != null && saveToolsDataFilesArray().booleanValue()) {
            Log.d("toolsDataFilesArrayList", "toolsDataFilesArrayList.size" + this.toolsDataFilesArrayList.size());
        }
    }

    public ToolsDataDictionary initNewToolsDataFileDictionaryWithFileName(String _filename) throws Throwable {
        Log.d("initNewToolsDataFileDictionaryWithFileName", _filename);
        ToolsDataDictionary newToolsDataDictionary = new ToolsDataDictionary();
        InputStream inputStream = getResources().getAssets().open(_filename);
        if (inputStream != null) {
            ToolDataDictionarySAXService toolDataSAXService = new ToolDataDictionarySAXService();
            newToolsDataDictionary.toolsDataArrayList = null;
            newToolsDataDictionary.toolsDataArrayList = toolDataSAXService.getTools(inputStream);
        } else {
            Log.d("toolData", "inputStream == null");
        }
        return newToolsDataDictionary;
    }

    public Boolean loadToolsDataFilesArray() throws IOException {
        this.toolsDataFilesArrayList = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            try {
                fileInputStream = openFileInput("tools_data_files.dat");
                ObjectInputStream objectInputStream2 = new ObjectInputStream(fileInputStream);
                if (objectInputStream2 != null) {
                    try {
                        this.toolsDataFilesArrayList = (ArrayList) objectInputStream2.readObject();
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e) {
                        }
                    } catch (OptionalDataException e2) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e3) {
                        }
                    } catch (IOException e4) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e5) {
                        }
                    } catch (ClassNotFoundException e6) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e7) {
                        }
                    } catch (Throwable th) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e8) {
                        }
                        throw th;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e9) {
                    }
                }
            } catch (IOException e10) {
                Log.d("loadToolsDataFilesArray", "load fis is not exists");
                if (0 != 0) {
                    try {
                        this.toolsDataFilesArrayList = (ArrayList) objectInputStream.readObject();
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e11) {
                        }
                    } catch (OptionalDataException e12) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e13) {
                        }
                    } catch (IOException e14) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e15) {
                        }
                    } catch (ClassNotFoundException e16) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e17) {
                        }
                    } catch (Throwable th2) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e18) {
                        }
                        throw th2;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e19) {
                    }
                }
            }
            return this.charactersDataFilesArrayList != null;
        } catch (Throwable th3) {
            if (0 != 0) {
                try {
                    this.toolsDataFilesArrayList = (ArrayList) objectInputStream.readObject();
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e20) {
                    }
                } catch (OptionalDataException e21) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e22) {
                    }
                } catch (IOException e23) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e24) {
                    }
                } catch (ClassNotFoundException e25) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e26) {
                    }
                } catch (Throwable th4) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e27) {
                    }
                    throw th4;
                }
            }
            if (fileInputStream == null) {
                throw th3;
            }
            try {
                fileInputStream.close();
                throw th3;
            } catch (IOException e28) {
                throw th3;
            }
        }
    }

    public Boolean saveToolsDataFilesArray() throws Throwable {
        boolean okF;
        ObjectOutputStream objectOutputStream;
        if (this.toolsDataFilesArrayList == null) {
            return false;
        }
        Boolean.valueOf(false);
        ObjectOutputStream objectOutputStream2 = null;
        FileOutputStream fileOutputStream = null;
        try {
            try {
                fileOutputStream = openFileOutput("tools_data_files.dat", 0);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            okF = true;
            objectOutputStream.writeObject(this.toolsDataFilesArrayList.clone());
            objectOutputStream.flush();
            Log.d("saveToolsDataFilesArray", "saveToolsDataFilesArray: size:" + this.toolsDataFilesArrayList.size());
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.reset();
                    objectOutputStream.close();
                } catch (IOException e2) {
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    objectOutputStream2 = objectOutputStream;
                } catch (IOException e3) {
                    objectOutputStream2 = objectOutputStream;
                }
            } else {
                objectOutputStream2 = objectOutputStream;
            }
        } catch (Exception e4) {
            e = e4;
            objectOutputStream2 = objectOutputStream;
            okF = false;
            Log.d("saveToolsDataFilesArray", "saveToolsDataFilesArray: error:" + e);
            if (objectOutputStream2 != null) {
                try {
                    objectOutputStream2.reset();
                    objectOutputStream2.close();
                } catch (IOException e5) {
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e6) {
                }
            }
            return okF;
        } catch (Throwable th2) {
            th = th2;
            objectOutputStream2 = objectOutputStream;
            if (objectOutputStream2 != null) {
                try {
                    objectOutputStream2.reset();
                    objectOutputStream2.close();
                } catch (IOException e7) {
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    throw th;
                } catch (IOException e8) {
                    throw th;
                }
            }
            throw th;
        }
        return okF;
    }

    public void initTimeSaveDictionary() {
        this.timeSaveDictionary = null;
        this.timeSaveDictionary = new TimeSaveDictionary(this);
        if (this.timeSaveDictionary != null) {
            Log.d("initTimeSaveDictionary", "initTimeSaveDictionary: true");
        } else {
            Log.d("initTimeSaveDictionary", "initTimeSaveDictionary: false");
        }
    }

    public boolean doJSONBackUpTimeSaveDictionaryToSharedPreferencesOperation() {
        TimeSaveDictionary cloneTimeSaveDictionary;
        if (this.timeSaveDictionary == null || this.defaultSharedPreferences == null) {
            return false;
        }
        try {
            cloneTimeSaveDictionary = this.timeSaveDictionary.m7clone();
        } catch (CloneNotSupportedException e) {
            cloneTimeSaveDictionary = null;
        }
        if (cloneTimeSaveDictionary == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        cloneTimeSaveDictionary.setDate(sdf.format(nowDate));
        if (cloneTimeSaveDictionary.getFirstDate() == null || cloneTimeSaveDictionary.getFirstDate().length() <= 0) {
            cloneTimeSaveDictionary.setFirstDate(sdf.format(nowDate));
        }
        this.timeSaveDictionary.setFirstDate(cloneTimeSaveDictionary.getFirstDate());
        this.timeSaveDictionary.setDate(cloneTimeSaveDictionary.getDate());
        SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
        String saveFileString = cloneTimeSaveDictionary.changeToJSONObject(this).toString();
        if (saveFileString != null && saveFileString.length() > 10) {
            editor.putString("time_save_dictionary", saveFileString.replaceAll("<time_slash>", "/"));
        }
        editor.commit();
        return true;
    }

    public boolean doSaveTimeSaveDictionaryOperation() {
        doJSONBackUpTimeSaveDictionaryToSharedPreferencesOperation();
        if (this.timeSaveDictionary == null || this.saveTimeSaveDictionaryLockF.booleanValue() || this.saveTimeSaveDictionaryHandler == null) {
            return false;
        }
        this.saveTimeSaveDictionaryHandler.post(this.saveTimeSaveDictionaryRunnable);
        return true;
    }

    public boolean saveTimeSaveDictionary() throws Throwable {
        ObjectOutputStream objectOutputStream;
        boolean okF = false;
        if (this.timeSaveDictionary != null) {
            Log.d("saveTimeSaveDictionary", "pgpgpgpgpgpg");
            ObjectOutputStream objectOutputStream2 = null;
            FileOutputStream fileOutputStream = null;
            try {
                try {
                    fileOutputStream = openFileOutput("timeSaveDictionary.dat", 0);
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    okF = true;
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                TimeSaveDictionary cloneTimeSaveDictionary = this.timeSaveDictionary.m7clone();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date nowDate = new Date();
                cloneTimeSaveDictionary.setDate(sdf.format(nowDate));
                if (cloneTimeSaveDictionary.getFirstDate() == null || cloneTimeSaveDictionary.getFirstDate().length() <= 0) {
                    cloneTimeSaveDictionary.setFirstDate(sdf.format(nowDate));
                }
                this.timeSaveDictionary.setFirstDate(cloneTimeSaveDictionary.getFirstDate());
                this.timeSaveDictionary.setDate(cloneTimeSaveDictionary.getDate());
                objectOutputStream.writeObject(cloneTimeSaveDictionary);
                objectOutputStream.flush();
                Log.d("saveTimeSaveDictionary", "save i= now Date:" + this.timeSaveDictionary.getDate() + "First Date:" + this.timeSaveDictionary.getFirstDate());
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.reset();
                        objectOutputStream.close();
                    } catch (IOException e2) {
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Exception e4) {
                e = e4;
                objectOutputStream2 = objectOutputStream;
                okF = false;
                Log.d("timeSaveDictionary", "timeSaveDictionary: error:" + e);
                if (objectOutputStream2 != null) {
                    try {
                        objectOutputStream2.reset();
                        objectOutputStream2.close();
                    } catch (IOException e5) {
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e6) {
                    }
                }
                return okF;
            } catch (Throwable th2) {
                th = th2;
                objectOutputStream2 = objectOutputStream;
                if (objectOutputStream2 != null) {
                    try {
                        objectOutputStream2.reset();
                        objectOutputStream2.close();
                    } catch (IOException e7) {
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        throw th;
                    } catch (IOException e8) {
                        throw th;
                    }
                }
                throw th;
            }
        }
        return okF;
    }

    public boolean loadTimeSaveDictionary() throws IOException {
        this.timeSaveDictionary = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            try {
                fileInputStream = openFileInput("timeSaveDictionary.dat");
                ObjectInputStream objectInputStream2 = new ObjectInputStream(fileInputStream);
                if (objectInputStream2 != null) {
                    try {
                        this.timeSaveDictionary = (TimeSaveDictionary) objectInputStream2.readObject();
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e) {
                        }
                    } catch (OptionalDataException e2) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e3) {
                        }
                    } catch (IOException e4) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e5) {
                        }
                    } catch (ClassNotFoundException e6) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e7) {
                        }
                    } catch (Throwable th) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e8) {
                        }
                        throw th;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e9) {
                    }
                }
            } catch (IOException e10) {
                Log.d("timeSaveDictionaryFileInputStream", "fis is not exists");
                if (0 != 0) {
                    try {
                        this.timeSaveDictionary = (TimeSaveDictionary) objectInputStream.readObject();
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e11) {
                        }
                    } catch (OptionalDataException e12) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e13) {
                        }
                    } catch (IOException e14) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e15) {
                        }
                    } catch (ClassNotFoundException e16) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e17) {
                        }
                    } catch (Throwable th2) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e18) {
                        }
                        throw th2;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e19) {
                    }
                }
            }
            return this.timeSaveDictionary != null;
        } catch (Throwable th3) {
            if (0 != 0) {
                try {
                    this.timeSaveDictionary = (TimeSaveDictionary) objectInputStream.readObject();
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e20) {
                    }
                } catch (OptionalDataException e21) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e22) {
                    }
                } catch (IOException e23) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e24) {
                    }
                } catch (ClassNotFoundException e25) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e26) {
                    }
                } catch (Throwable th4) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e27) {
                    }
                    throw th4;
                }
            }
            if (fileInputStream == null) {
                throw th3;
            }
            try {
                fileInputStream.close();
                throw th3;
            } catch (IOException e28) {
                throw th3;
            }
        }
    }

    public void initMainSavesDictionary() {
        this.mainSavesDictionary = null;
        this.mainSavesDictionary = new MainSavesDictionary((short) 30, "2.3.0");
        Log.d("version_level", "this.mainSavesDictionary.version_level:" + ((int) this.mainSavesDictionary.getVersionLevel()));
        Log.d("version_number", "this.mainSavesDictionary.version_number:" + this.mainSavesDictionary.getVersionNumber());
    }

    public boolean doSaveMainSavesDictionaryOperationWithType(short _type) {
        if (this.saveMainSavesDictionaryLockF.booleanValue() || this.mainSavesDictionary == null || this.saveMainSavesDictionaryHandler == null) {
            return false;
        }
        if (_type != 0 && _type != 1) {
            return false;
        }
        if (_type == 0) {
            this.saveMainSavesDictionaryHandler.post(this.saveMainSavesDictionaryRunnable0);
        } else if (_type == 1) {
            this.saveMainSavesDictionaryHandler.post(this.saveMainSavesDictionaryRunnable1);
        }
        return true;
    }

    public boolean saveMainSavesDictionaryWithType(short _type) throws Throwable {
        boolean okF;
        TimeSaveDictionary checkRemoveTimeSaveDictionary;
        String checkFirstDateString;
        if (this.mainSavesDictionary == null) {
            return false;
        }
        if (_type != 0 && _type != 1) {
            return false;
        }
        Log.d("saveMainSavesDictionary", "pgpgpgpgpgpg");
        ObjectOutputStream objectOutputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            try {
                fileOutputStream = openFileOutput("mainSavesDictionary.dat", 0);
                ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(fileOutputStream);
                if (_type == 1) {
                    try {
                        if (this.timeSaveDictionary != null) {
                            TimeSaveDictionary cloneTimeSaveDictionary = this.timeSaveDictionary.m7clone();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date nowDate = new Date();
                            String nowDateString = sdf.format(nowDate);
                            if (this.mainSavesDictionary.timeSavesArrayList.size() >= 2 && (checkRemoveTimeSaveDictionary = this.mainSavesDictionary.timeSavesArrayList.get(0)) != null && (checkFirstDateString = checkRemoveTimeSaveDictionary.getFirstDate()) != null && checkFirstDateString.length() > 0) {
                                Date checkFirstDate = sdf.parse(checkFirstDateString);
                                Log.d("saveMainSavesDictionaryWithType", "checkFirstDate:" + sdf.format(checkFirstDate));
                                Log.d("saveMainSavesDictionaryWithType", "nowDate:" + sdf.format(nowDate));
                                Log.d("saveMainSavesDictionaryWithType", "oldDate:" + sdf.format(new Date(nowDate.getTime() - 172800000)));
                                if (checkFirstDate.before(new Date(nowDate.getTime() - 172800000))) {
                                    cloneTimeSaveDictionary.setFirstDate(nowDateString);
                                    Log.d("saveMainSavesDictionaryWithType", "Dont Combin");
                                } else {
                                    Log.d("saveMainSavesDictionaryWithType", "Need Combin");
                                    cloneTimeSaveDictionary.setFirstDate(checkFirstDateString);
                                    this.mainSavesDictionary.timeSavesArrayList.remove(0);
                                }
                            }
                            if (cloneTimeSaveDictionary.getFirstDate() == null || cloneTimeSaveDictionary.getFirstDate().length() <= 0) {
                                cloneTimeSaveDictionary.setFirstDate(nowDateString);
                            }
                            cloneTimeSaveDictionary.setDate(nowDateString);
                            this.timeSaveDictionary.setFirstDate(cloneTimeSaveDictionary.getFirstDate());
                            this.timeSaveDictionary.setDate(cloneTimeSaveDictionary.getDate());
                            this.mainSavesDictionary.timeSavesArrayList.add(0, cloneTimeSaveDictionary);
                            while (this.mainSavesDictionary.timeSavesArrayList.size() > 2) {
                                this.mainSavesDictionary.timeSavesArrayList.remove(this.mainSavesDictionary.timeSavesArrayList.size() - 1);
                            }
                            for (int i = 0; i < this.mainSavesDictionary.timeSavesArrayList.size(); i++) {
                                TimeSaveDictionary checkTimeSaveDictionary = this.mainSavesDictionary.timeSavesArrayList.get(i);
                                Log.e("saveMainSavesDictionaryWithType", "save i= " + i + " Date:" + checkTimeSaveDictionary.getDate() + " First Date:" + checkTimeSaveDictionary.getFirstDate());
                            }
                        }
                    } catch (Exception e) {
                        e = e;
                        objectOutputStream = objectOutputStream2;
                        okF = false;
                        Log.d("saveMainSavesDictionary", "saveMainSavesDictionary: error:" + e);
                        if (objectOutputStream != null) {
                            try {
                                objectOutputStream.reset();
                                objectOutputStream.close();
                            } catch (IOException e2) {
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        return okF;
                    } catch (Throwable th) {
                        th = th;
                        objectOutputStream = objectOutputStream2;
                        if (objectOutputStream != null) {
                            try {
                                objectOutputStream.reset();
                                objectOutputStream.close();
                            } catch (IOException e4) {
                            }
                        }
                        if (fileOutputStream == null) {
                            throw th;
                        }
                        try {
                            fileOutputStream.close();
                            throw th;
                        } catch (IOException e5) {
                            e5.printStackTrace();
                            throw th;
                        }
                    }
                }
                okF = true;
                objectOutputStream2.writeObject(this.mainSavesDictionary.m6clone());
                objectOutputStream2.flush();
                if (objectOutputStream2 != null) {
                    try {
                        objectOutputStream2.reset();
                        objectOutputStream2.close();
                    } catch (IOException e6) {
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        objectOutputStream = objectOutputStream2;
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                } else {
                    objectOutputStream = objectOutputStream2;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e8) {
            e = e8;
        }
        return okF;
    }

    public boolean loadMainSavesDictionary() throws IOException {
        this.mainSavesDictionary = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            try {
                fileInputStream = openFileInput("mainSavesDictionary.dat");
                ObjectInputStream objectInputStream2 = new ObjectInputStream(fileInputStream);
                if (objectInputStream2 != null) {
                    try {
                        this.mainSavesDictionary = (MainSavesDictionary) objectInputStream2.readObject();
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e) {
                        }
                    } catch (OptionalDataException e2) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e3) {
                        }
                    } catch (IOException e4) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e5) {
                        }
                    } catch (ClassNotFoundException e6) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e7) {
                        }
                    } catch (Throwable th) {
                        try {
                            objectInputStream2.reset();
                            objectInputStream2.close();
                        } catch (IOException e8) {
                        }
                        throw th;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e9) {
                    }
                }
            } catch (IOException e10) {
                Log.d("mainSavesDictionaryFileInputStream", "fis is not exists");
                if (0 != 0) {
                    try {
                        this.mainSavesDictionary = (MainSavesDictionary) objectInputStream.readObject();
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e11) {
                        }
                    } catch (OptionalDataException e12) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e13) {
                        }
                    } catch (IOException e14) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e15) {
                        }
                    } catch (ClassNotFoundException e16) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e17) {
                        }
                    } catch (Throwable th2) {
                        try {
                            objectInputStream.reset();
                            objectInputStream.close();
                        } catch (IOException e18) {
                        }
                        throw th2;
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e19) {
                    }
                }
            }
            return this.mainSavesDictionary != null;
        } catch (Throwable th3) {
            if (0 != 0) {
                try {
                    this.mainSavesDictionary = (MainSavesDictionary) objectInputStream.readObject();
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e20) {
                    }
                } catch (OptionalDataException e21) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e22) {
                    }
                } catch (IOException e23) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e24) {
                    }
                } catch (ClassNotFoundException e25) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e26) {
                    }
                } catch (Throwable th4) {
                    try {
                        objectInputStream.reset();
                        objectInputStream.close();
                    } catch (IOException e27) {
                    }
                    throw th4;
                }
            }
            if (fileInputStream == null) {
                throw th3;
            }
            try {
                fileInputStream.close();
                throw th3;
            } catch (IOException e28) {
                throw th3;
            }
        }
    }

    public short checkTimeWithTimeSaveDictionary(Date _date) throws IOException {
        String timeSaveDictionaryJSONString;
        short saveIndex = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        Log.d("checkTimeWithTimeSaveDictionary", "nowDate:" + nowDate);
        loadTimeSaveDictionary();
        if (this.timeSaveDictionary != null && checkTimeSaveDictionaryCorrect(this.timeSaveDictionary).booleanValue()) {
            String checkDateString = this.timeSaveDictionary.getDate();
            Date checkDate = null;
            try {
                checkDate = sdf.parse(checkDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (checkDate != null && _date.after(checkDate)) {
                saveIndex = -2;
                Log.d("checkTimeWithTimeSaveDictionary", "okokokokokokok  index:-2 Date:" + checkDateString);
            }
        } else {
            saveIndex = -999;
        }
        if ((saveIndex == -1 || saveIndex == -999) && this.defaultSharedPreferences != null && (timeSaveDictionaryJSONString = this.defaultSharedPreferences.getString("time_save_dictionary", "")) != null && timeSaveDictionaryJSONString.length() > 10) {
            Date checkDate2 = null;
            try {
                checkDate2 = sdf.parse(getTimeSaveDictionaryJSONStringDate(timeSaveDictionaryJSONString));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            if (checkDate2 != null && _date.after(checkDate2)) {
                saveIndex = -3;
            }
        }
        if ((saveIndex == -1 || saveIndex == -999) && this.mainSavesDictionary != null && this.mainSavesDictionary.timeSavesArrayList != null) {
            short i = 0;
            while (true) {
                if (i >= this.mainSavesDictionary.timeSavesArrayList.size()) {
                    break;
                }
                TimeSaveDictionary checkTimeSaveDictionary = this.mainSavesDictionary.timeSavesArrayList.get(i);
                if (checkTimeSaveDictionaryCorrect(checkTimeSaveDictionary).booleanValue()) {
                    String checkDateString2 = checkTimeSaveDictionary.getDate();
                    Date checkDate3 = null;
                    try {
                        checkDate3 = sdf.parse(checkDateString2);
                    } catch (ParseException e3) {
                        e3.printStackTrace();
                    }
                    if (checkDate3 != null && _date.after(checkDate3)) {
                        saveIndex = i;
                        Log.d("checkTimeWithTimeSaveDictionary", "okokokokokokok  index:" + ((int) saveIndex) + " Date:" + checkDateString2);
                        break;
                    }
                } else {
                    this.mainSavesDictionary.timeSavesArrayList.remove(i);
                    i = (short) (i - 1);
                }
                i = (short) (i + 1);
            }
        }
        Log.d("checkTimeWithTimeSaveDictionary", "returnSaveIndex:" + ((int) saveIndex));
        return saveIndex;
    }

    public String getTimeSaveDictionaryJSONStringDate(String _jasonString) throws JSONException {
        JSONObject jsonObject;
        String returnDateString = "";
        if (_jasonString == null) {
            return "";
        }
        try {
            jsonObject = new JSONObject(_jasonString);
        } catch (JSONException e) {
            jsonObject = null;
        }
        if (jsonObject != null) {
            try {
                returnDateString = jsonObject.getString("d");
            } catch (JSONException e2) {
                returnDateString = "";
            }
        }
        if (returnDateString != null && returnDateString.length() > 0) {
            returnDateString = returnDateString.replaceAll("<time_slash>", "/");
        }
        return returnDateString;
    }

    public Boolean checkTimeSaveDictionaryCorrect(TimeSaveDictionary _timeSaveDictionary) {
        boolean correctF = true;
        if (_timeSaveDictionary == null) {
            correctF = false;
        } else {
            String checkDateString = _timeSaveDictionary.getDate();
            Log.d("checkTimeSaveDictionaryCorrect", "checkDateString:" + checkDateString);
            Log.d("checkTimeSaveDictionaryCorrect", "checkDateString.length:" + checkDateString.length());
            if (checkDateString == null || checkDateString.length() < 10) {
                correctF = false;
            } else {
                if (_timeSaveDictionary.characterUnitDictionarysArrayList == null) {
                    correctF = false;
                }
                if (_timeSaveDictionary.farmUnitDictionarysArrayList == null) {
                    correctF = false;
                }
                if (_timeSaveDictionary.toolUnitDictionarysArrayList == null) {
                    correctF = false;
                }
            }
        }
        Log.d("======================================================", "======================================================");
        Log.d("======================================================", "======================================================");
        return correctF;
    }

    public short getShort_egg_id_select_index() {
        if (this.timeSaveDictionary == null) {
            return (short) 0;
        }
        short return_egg_id_select_index = this.timeSaveDictionary.getEggIDSelectIndex();
        if (return_egg_id_select_index < 0 || return_egg_id_select_index >= getTool3ArrayCount() || getTool3CountWithIndex(return_egg_id_select_index) < 1) {
            return_egg_id_select_index = 0;
        }
        this.timeSaveDictionary.setEggIDSelectIndex(return_egg_id_select_index);
        return return_egg_id_select_index;
    }

    public short getShort_tool_1_selectview_nowbuttonindex() {
        if (this.timeSaveDictionary == null) {
            return (short) -1;
        }
        return this.timeSaveDictionary.getTool1SelectViewNowButtonIndex();
    }

    public short getNowCookingEggID() {
        short checkEggID;
        if (this.timeSaveDictionary != null && this.timeSaveDictionary.characterUnitDictionarysArrayList != null) {
            for (int i = 0; i < this.timeSaveDictionary.characterUnitDictionarysArrayList.size(); i++) {
                CharacterUnitDictionary characterUnitDictionary = this.timeSaveDictionary.characterUnitDictionarysArrayList.get(i);
                if (characterUnitDictionary != null && (checkEggID = characterUnitDictionary.getEggId()) >= 0) {
                    return checkEggID;
                }
            }
            return (short) -1;
        }
        return (short) -1;
    }

    public short getTool0LevelWithIndex(short _index) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        short levelShort = -1;
        if (this.timeSaveDictionary == null) {
            return (short) -1;
        }
        if (_index < 0 || _index > 2) {
            return (short) -1;
        }
        if (this.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 1 && (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && _index < toolDictionarysArrayList.size()) {
            ToolUnitDictionary toolUnitDictionary = toolDictionarysArrayList.get(_index);
            if (toolUnitDictionary != null) {
                levelShort = toolUnitDictionary.getLevel();
                if (_index == 0) {
                    if (levelShort >= 4) {
                        levelShort = -2;
                    }
                } else if (_index == 1 && levelShort >= 1) {
                    levelShort = -2;
                }
            }
            return levelShort;
        }
        return (short) -1;
    }

    public short getTool1LevelWithIndex(short _index) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        short levelShort = -2;
        if (this.timeSaveDictionary == null) {
            return (short) -2;
        }
        if (_index < 0 || _index >= this.TOOL_1_ALL_CNT) {
            return (short) -2;
        }
        if (this.timeSaveDictionary.toolUnitDictionarysArrayList == null) {
            return (short) -2;
        }
        Log.d("AppMainActivity", "this.timeSaveDictionary.toolUnitDictionarysArrayList.size():%d" + this.timeSaveDictionary.toolUnitDictionarysArrayList.size());
        if (this.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 2 && (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(1)) != null && _index < toolDictionarysArrayList.size()) {
            ToolUnitDictionary toolUnitDictionary = toolDictionarysArrayList.get(_index);
            if (toolUnitDictionary != null && (levelShort = toolUnitDictionary.getLevel()) >= 3) {
                levelShort = -2;
            }
            return levelShort;
        }
        return (short) -2;
    }

    public short getAllTool1LowestLevel(short _totalIndex) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        if (this.timeSaveDictionary != null && this.timeSaveDictionary.toolUnitDictionarysArrayList != null) {
            Log.d("AppMainActivity", "this.timeSaveDictionary.toolUnitDictionarysArrayList.size():%d" + this.timeSaveDictionary.toolUnitDictionarysArrayList.size());
            if (this.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 2 && (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(1)) != null && toolDictionarysArrayList.size() > 0) {
                short lowestLevelShort = 9999;
                for (int i = 0; i < toolDictionarysArrayList.size() && i < _totalIndex; i++) {
                    ToolUnitDictionary toolUnitDictionary = toolDictionarysArrayList.get(i);
                    if (toolUnitDictionary != null) {
                        short levelShort = toolUnitDictionary.getLevel();
                        if (levelShort >= 3) {
                            levelShort = -2;
                        }
                        if (levelShort < lowestLevelShort) {
                            lowestLevelShort = levelShort;
                        }
                    }
                }
                if (lowestLevelShort >= 9999) {
                    lowestLevelShort = -2;
                }
                return lowestLevelShort;
            }
            return (short) -2;
        }
        return (short) -2;
    }

    public short getTool2TotalPurchasedCount() {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        short totalCount = 0;
        if (this.timeSaveDictionary != null && this.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 3 && (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(2)) != null) {
            for (int i = 0; i < toolDictionarysArrayList.size(); i++) {
                ToolUnitDictionary toolUnitDictionary = toolDictionarysArrayList.get(i);
                if (toolUnitDictionary != null) {
                    short countShort = toolUnitDictionary.getCount();
                    if (countShort <= 0) {
                        countShort = -1;
                    }
                    if (countShort > 0) {
                        totalCount = (short) (totalCount + 1);
                    }
                }
            }
            return totalCount;
        }
        return (short) 0;
    }

    public short getTool3ArrayCount() {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        if (this.timeSaveDictionary == null || this.timeSaveDictionary.toolUnitDictionarysArrayList == null || this.timeSaveDictionary.toolUnitDictionarysArrayList.size() < 4 || (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(3)) == null) {
            return (short) 0;
        }
        return (short) toolDictionarysArrayList.size();
    }

    public short getTool3CountWithIndex(short _index) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        short countShort = -1;
        if (this.timeSaveDictionary != null && this.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 4 && (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(3)) != null) {
            if (_index < 0 || _index >= toolDictionarysArrayList.size()) {
                return (short) -1;
            }
            ToolUnitDictionary toolUnitDictionary = toolDictionarysArrayList.get(_index);
            if (toolUnitDictionary != null && (countShort = toolUnitDictionary.getCount()) > 1) {
                countShort = -1;
            }
            if (countShort < 0 || countShort > 1) {
                countShort = -1;
            }
            return countShort;
        }
        return (short) -1;
    }

    public CharacterDataDictionary getCharacterDataDictionaryWithId(short _eggID, short _characterID) {
        CharactersDataDictionary charactersDataDictionary;
        CharacterDataDictionary characterDataDictionary = null;
        if (_eggID < 0 || _characterID < 0) {
            return null;
        }
        if (this.charactersDataFilesArrayList == null) {
            initCharactersDataFilesArray();
        }
        if (this.charactersDataFilesArrayList != null && _eggID < this.charactersDataFilesArrayList.size() && (charactersDataDictionary = this.charactersDataFilesArrayList.get(_eggID)) != null && charactersDataDictionary.charactersDataArrayList != null && _characterID < charactersDataDictionary.charactersDataArrayList.size()) {
            characterDataDictionary = charactersDataDictionary.charactersDataArrayList.get(_characterID);
        }
        return characterDataDictionary;
    }

    public ToolDataDictionary getToolDataDictionaryWithId(short _typeID, short _toolID) {
        ToolsDataDictionary toolsDataDictionary;
        ToolDataDictionary toolDataDictionary = null;
        if (_typeID < 0 || _toolID < 0) {
            return null;
        }
        if (this.toolsDataFilesArrayList == null) {
            initToolsDataFilesArray();
        }
        if (this.toolsDataFilesArrayList != null && _typeID < this.toolsDataFilesArrayList.size() && (toolsDataDictionary = this.toolsDataFilesArrayList.get(_typeID)) != null && toolsDataDictionary.toolsDataArrayList != null && _toolID < toolsDataDictionary.toolsDataArrayList.size()) {
            toolDataDictionary = toolsDataDictionary.toolsDataArrayList.get(_toolID);
        }
        return toolDataDictionary;
    }

    public short getCharacterUnitDictionarysArrayCountWithEggId(short _eggID) {
        ArrayList<Object> unitDictionarysArrayList;
        short returnCount = 0;
        if (_eggID >= 0 && this.timeSaveDictionary != null) {
            if (this.timeSaveDictionary.farmUnitDictionarysArrayList != null && _eggID < this.timeSaveDictionary.farmUnitDictionarysArrayList.size() && (unitDictionarysArrayList = (ArrayList) this.timeSaveDictionary.farmUnitDictionarysArrayList.get(_eggID)) != null) {
                returnCount = (short) unitDictionarysArrayList.size();
            }
            if (returnCount < 0) {
                returnCount = 0;
            }
            return returnCount;
        }
        return (short) 0;
    }

    public FarmUnitDictionary getCharacterUnitDictionaryWithId(short _eggID, short _characterID) {
        ArrayList<FarmUnitDictionary> unitDictionarysArrayList;
        FarmUnitDictionary unitDictionary = null;
        if (_eggID < 0 || _characterID < 0) {
            return null;
        }
        if (this.timeSaveDictionary == null) {
            return null;
        }
        if (this.timeSaveDictionary.farmUnitDictionarysArrayList != null && _eggID < this.timeSaveDictionary.farmUnitDictionarysArrayList.size() && (unitDictionarysArrayList = (ArrayList) this.timeSaveDictionary.farmUnitDictionarysArrayList.get(_eggID)) != null && _characterID < unitDictionarysArrayList.size()) {
            unitDictionary = unitDictionarysArrayList.get(_characterID);
        }
        return unitDictionary;
    }

    public short getToolDictionarysArrayCountWithTypeId(short _typeID) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        short returnCount = 0;
        if (_typeID >= 0 && this.timeSaveDictionary != null) {
            if (this.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.timeSaveDictionary.toolUnitDictionarysArrayList.size() > _typeID && (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(_typeID)) != null) {
                returnCount = (short) toolDictionarysArrayList.size();
            }
            if (returnCount < 0) {
                returnCount = 0;
            }
            return returnCount;
        }
        return (short) 0;
    }

    public ToolUnitDictionary getToolDictionaryWithId(short _typeID, short _toolID) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        ToolUnitDictionary toolUnitDictionary = null;
        if (_typeID < 0 || _toolID < 0) {
            return null;
        }
        if (this.timeSaveDictionary == null) {
            return null;
        }
        if (this.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.timeSaveDictionary.toolUnitDictionarysArrayList.size() > _typeID && (toolDictionarysArrayList = (ArrayList) this.timeSaveDictionary.toolUnitDictionarysArrayList.get(_typeID)) != null && toolDictionarysArrayList.size() > _toolID) {
            toolUnitDictionary = toolDictionarysArrayList.get(_toolID);
        }
        return toolUnitDictionary;
    }

    public CharacterUnitDictionary getCharacterUnitDictionaryWithIndex(short _index) {
        if (this.timeSaveDictionary != null && this.timeSaveDictionary.characterUnitDictionarysArrayList != null && _index >= 0 && _index < this.timeSaveDictionary.characterUnitDictionarysArrayList.size()) {
            return this.timeSaveDictionary.characterUnitDictionarysArrayList.get(_index);
        }
        return null;
    }

    public void openOnlineGameViewControllerWithAutoLogIn(Boolean _autoLogInF, short _nextActiveStstus) {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.openOnlineGameViewControllerWithAutoLogIn(_autoLogInF, _nextActiveStstus);
        }
    }

    public Bitmap takeScreenShot(float _takeOffsetX, float _takeOffsetY, float _takeWidth, float _takeHeight) {
        if (this.nowAppMainActivity == null) {
            return null;
        }
        Bitmap returnBitmap = this.nowAppMainActivity.takeScreenShot(_takeOffsetX, _takeOffsetY, _takeWidth, _takeHeight);
        return returnBitmap;
    }

    public boolean savePic(Bitmap b, String strFilePath, String strFileName) {
        if (this.nowAppMainActivity == null) {
            return false;
        }
        boolean successF = this.nowAppMainActivity.savePic(b, strFilePath, strFileName);
        return successF;
    }

    public void hiddenAdControlLayout(boolean _hiddenF) {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.hiddenAdControlLayout(_hiddenF);
        }
    }

    public void hiddenBonusUnitViewAd(boolean _hiddenF) {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.hiddenBonusUnitViewAd(_hiddenF);
        }
    }

    public boolean checkAdColonyV4VCF() {
        this.adColonyV4VCF = false;
        if (this.nowAppMainActivity != null) {
            this.adColonyV4VCF = this.nowAppMainActivity.checkAdColonyV4VCF();
        }
        return this.adColonyV4VCF;
    }

    public boolean showAdColonyV4VCF() {
        if (this.nowAppMainActivity == null) {
            return false;
        }
        boolean showAdColonyV4VCF = this.nowAppMainActivity.showAdColonyV4VCF();
        return showAdColonyV4VCF;
    }

    public void loadAdMobFullScreenAd() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.loadAdMobFullScreenAd();
        }
    }

    public void displayFullAdView() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.displayFullAdView();
        }
    }

    public void delayDisplayFullAdView(int _delaySeconds) {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.delayDisplayFullAdView(_delaySeconds);
        }
    }

    public void returnToMainGame() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.returnToMainGame();
        }
    }

    public void goToMainGame() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.goToMainGame();
        }
    }

    public void backToSavesCheck() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.backToSavesCheck();
        }
    }

    public void goToSavesCheck() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.goToSavesCheck();
        }
    }

    public void backToMainMenu() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.backToMainMenu();
        }
    }

    public void changeOnPauseF(boolean _onPauseF) {
        this.onPauseF = _onPauseF;
    }

    public boolean getOnPauseF() {
        return this.onPauseF;
    }

    public void changeNowStatus(int _newStatus) {
        this.nowStatus = _newStatus;
    }

    public int getNowStatus() {
        return this.nowStatus;
    }

    public void deleteTimeSaveDictionaryToIndex(short _index) {
        if (this.mainSavesDictionary != null && this.mainSavesDictionary.timeSavesArrayList != null) {
            short deleteCnt = _index;
            while (deleteCnt > 0) {
                if (this.mainSavesDictionary.timeSavesArrayList.size() > 0) {
                    this.mainSavesDictionary.timeSavesArrayList.remove(0);
                    deleteCnt = (short) (deleteCnt - 1);
                } else {
                    deleteCnt = 0;
                }
            }
        }
    }

    protected short checkTimeRangeWithTimeSaveDictionary(Date _date) throws NumberFormatException {
        short timeRangeIndex = 0;
        if (_date != null) {
            SimpleDateFormat sdfyyyy = new SimpleDateFormat("yyyy");
            int yearInt = Integer.parseInt(sdfyyyy.format(_date));
            Log.d("yearInt", "yearInt " + yearInt);
            sdfyyyy.applyLocalizedPattern("MM");
            int monthIntT = Integer.parseInt(sdfyyyy.format(_date));
            Log.d("monthInt", "monthInt " + monthIntT);
            sdfyyyy.applyPattern("MM");
            int monthInt1T = Integer.parseInt(sdfyyyy.format(_date));
            Log.d("monthInt1", "monthInt1 " + monthInt1T);
            if (yearInt == 2014) {
                SimpleDateFormat sdfMM = new SimpleDateFormat("MM");
                int monthInt = Integer.parseInt(sdfMM.format(_date));
                if (monthInt < 5) {
                    timeRangeIndex = -1;
                }
            } else if (yearInt != 2015) {
                if (yearInt == 2016) {
                    SimpleDateFormat sdfMM2 = new SimpleDateFormat("MM");
                    int monthInt2 = Integer.parseInt(sdfMM2.format(_date));
                    if (monthInt2 > 12) {
                        timeRangeIndex = 1;
                    }
                } else {
                    timeRangeIndex = -1;
                }
            }
        } else {
            timeRangeIndex = -1;
        }
        Log.d("timeRangeIndex", "timeRangeIndex = " + ((int) timeRangeIndex));
        return timeRangeIndex;
    }

    public String getNowDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nowDate = new Date();
        String createDateString = sdf.format(nowDate);
        if (createDateString == null) {
            createDateString = "";
        }
        return createDateString;
    }

    public void setCookAlarmWithDateString(String _dateString) {
        removeAllAlarmNotification();
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        notificationManager.cancel(0);
    }

    public void setCookAlarmOnWithFireDateString(String _dateString, String _bodyString, String _nowCookingEggIDString) {
        removeAllAlarmNotification();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date fireDate = null;
        try {
            fireDate = sdf.parse(_dateString);
        } catch (ParseException e) {
        }
        if (fireDate != null) {
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(fireDate.getTime());
            Bundle bundle = new Bundle();
            bundle.putString("body_string", _bodyString);
            bundle.putString("egg_id", _nowCookingEggIDString);
            Intent intent = new Intent(this, (Class<?>) CallAlarm.class);
            intent.putExtras(bundle);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 134217728);
            AlarmManager am = (AlarmManager) getSystemService("alarm");
            am.set(0, c2.getTimeInMillis(), sender);
        }
    }

    public void removeAllAlarmNotification() {
        Intent intent = new Intent(this, (Class<?>) CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService("alarm");
        am.cancel(sender);
    }

    public void removeAlarmNotificationWithNotificationID(String _notificationIDString) {
        Intent intent = new Intent(this, (Class<?>) CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService("alarm");
        am.cancel(sender);
    }

    public String getLocaleLanguage() {
        Locale local = Locale.getDefault();
        return String.format("%s-%s", local.getLanguage(), local.getCountry());
    }

    public void initImageDestroy() {
        if (this.initImageHandler != null) {
            this.initImageHandler.removeCallbacks(this.initImageRunnable);
        }
        if (this.initImageHandlerThread != null) {
            this.initImageHandlerThread.quit();
        }
    }

    public void saveTimeSaveDictionaryDestroy() {
        if (this.saveTimeSaveDictionaryHandler != null) {
            this.saveTimeSaveDictionaryHandler.removeCallbacks(this.saveTimeSaveDictionaryRunnable);
        }
        if (this.saveTimeSaveDictionaryHandlerThread != null) {
            this.saveTimeSaveDictionaryHandlerThread.quit();
        }
    }

    public void saveMainSavesDictionaryDestroy() {
        if (this.saveMainSavesDictionaryHandler != null) {
            this.saveMainSavesDictionaryHandler.removeCallbacks(this.saveMainSavesDictionaryRunnable0);
            this.saveMainSavesDictionaryHandler.removeCallbacks(this.saveMainSavesDictionaryRunnable1);
        }
        if (this.saveMainSavesDictionaryHandlerThread != null) {
            this.saveMainSavesDictionaryHandlerThread.quit();
        }
    }

    public void onDestroy() throws IllegalStateException {
        if (this.saveTimeSaveDictionaryLockF.booleanValue()) {
            this.saveTimeSaveDictionaryNeedDestoryF = true;
        } else {
            saveTimeSaveDictionaryDestroy();
        }
        if (this.saveMainSavesDictionaryLockF.booleanValue()) {
            this.saveMainSavesDictionaryNeedDestoryF = true;
        } else {
            saveMainSavesDictionaryDestroy();
        }
        initImageDestroy();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        if (this.soundPool != null) {
            this.soundPool.release();
            this.soundPool = null;
        }
        if (this.soundMap != null) {
            this.soundMap.clear();
            this.soundMap = null;
        }
        if (this.postDeviceToken != null) {
            this.postDeviceToken.onDestroy();
            this.postDeviceToken = null;
        }
        if (this.optionBackGroundBitmap != null) {
            if (!this.optionBackGroundBitmap.isRecycled()) {
                this.optionBackGroundBitmap.recycle();
            }
            this.optionBackGroundBitmap = null;
        }
        if (this.starOnBitmap != null) {
            if (!this.starOnBitmap.isRecycled()) {
                this.starOnBitmap.recycle();
            }
            this.starOnBitmap = null;
        }
        if (this.starOffBitmap != null) {
            if (!this.starOffBitmap.isRecycled()) {
                this.starOffBitmap.recycle();
            }
            this.starOffBitmap = null;
        }
        if (this.shadow_0_Bitmap != null) {
            if (!this.shadow_0_Bitmap.isRecycled()) {
                this.shadow_0_Bitmap.recycle();
            }
            this.shadow_0_Bitmap = null;
        }
        if (this.shadow_1_Bitmap != null) {
            if (!this.shadow_1_Bitmap.isRecycled()) {
                this.shadow_1_Bitmap.recycle();
            }
            this.shadow_1_Bitmap = null;
        }
        if (this.listview_fastscrolldragview_Bitmap != null) {
            if (!this.listview_fastscrolldragview_Bitmap.isRecycled()) {
                this.listview_fastscrolldragview_Bitmap.recycle();
            }
            this.listview_fastscrolldragview_Bitmap = null;
        }
        if (this.tool_locked_mark_Bitmap != null) {
            if (!this.tool_locked_mark_Bitmap.isRecycled()) {
                this.tool_locked_mark_Bitmap.recycle();
            }
            this.tool_locked_mark_Bitmap = null;
        }
        if (this.egg0ImageArrayList != null) {
            while (this.egg0ImageArrayList.size() > 0) {
                Bitmap bitmap = this.egg0ImageArrayList.get(0);
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                this.egg0ImageArrayList.remove(0);
            }
            this.egg0ImageArrayList.clear();
            this.egg0ImageArrayList = null;
        }
        if (this.egg1ImageArrayList != null) {
            while (this.egg1ImageArrayList.size() > 0) {
                Bitmap bitmap2 = this.egg1ImageArrayList.get(0);
                if (bitmap2 != null) {
                    if (!bitmap2.isRecycled()) {
                        bitmap2.recycle();
                    }
                }
                this.egg1ImageArrayList.remove(0);
            }
            this.egg1ImageArrayList.clear();
            this.egg1ImageArrayList = null;
        }
        if (this.character0Image0ArrayList != null) {
            while (this.character0Image0ArrayList.size() > 0) {
                Bitmap bitmap3 = this.character0Image0ArrayList.get(0);
                if (bitmap3 != null) {
                    if (!bitmap3.isRecycled()) {
                        bitmap3.recycle();
                    }
                }
                this.character0Image0ArrayList.remove(0);
            }
            this.character0Image0ArrayList.clear();
            this.character0Image0ArrayList = null;
        }
        if (this.character0RevImage0ArrayList != null) {
            while (this.character0RevImage0ArrayList.size() > 0) {
                Bitmap bitmap4 = this.character0RevImage0ArrayList.get(0);
                if (bitmap4 != null) {
                    if (!bitmap4.isRecycled()) {
                        bitmap4.recycle();
                    }
                }
                this.character0RevImage0ArrayList.remove(0);
            }
            this.character0RevImage0ArrayList.clear();
            this.character0RevImage0ArrayList = null;
        }
        if (this.character1Image0ArrayList != null) {
            while (this.character1Image0ArrayList.size() > 0) {
                Bitmap bitmap5 = this.character1Image0ArrayList.get(0);
                if (bitmap5 != null) {
                    if (!bitmap5.isRecycled()) {
                        bitmap5.recycle();
                    }
                }
                this.character1Image0ArrayList.remove(0);
            }
            this.character1Image0ArrayList.clear();
            this.character1Image0ArrayList = null;
        }
        if (this.tool1Level0Image0ArrayList != null) {
            while (this.tool1Level0Image0ArrayList.size() > 0) {
                Bitmap bitmap6 = this.tool1Level0Image0ArrayList.get(0);
                if (bitmap6 != null) {
                    if (!bitmap6.isRecycled()) {
                        bitmap6.recycle();
                    }
                }
                this.tool1Level0Image0ArrayList.remove(0);
            }
            this.tool1Level0Image0ArrayList.clear();
            this.tool1Level0Image0ArrayList = null;
        }
        if (this.tool1Level0Image1ArrayList != null) {
            while (this.tool1Level0Image1ArrayList.size() > 0) {
                Bitmap bitmap7 = this.tool1Level0Image1ArrayList.get(0);
                if (bitmap7 != null) {
                    if (!bitmap7.isRecycled()) {
                        bitmap7.recycle();
                    }
                }
                this.tool1Level0Image1ArrayList.remove(0);
            }
            this.tool1Level0Image1ArrayList.clear();
            this.tool1Level0Image1ArrayList = null;
        }
        if (this.tool1Level1Image0ArrayList != null) {
            while (this.tool1Level1Image0ArrayList.size() > 0) {
                Bitmap bitmap8 = this.tool1Level1Image0ArrayList.get(0);
                if (bitmap8 != null) {
                    if (!bitmap8.isRecycled()) {
                        bitmap8.recycle();
                    }
                }
                this.tool1Level1Image0ArrayList.remove(0);
            }
            this.tool1Level1Image0ArrayList.clear();
            this.tool1Level1Image0ArrayList = null;
        }
        if (this.tool1Level1Image1ArrayList != null) {
            while (this.tool1Level1Image1ArrayList.size() > 0) {
                Bitmap bitmap9 = this.tool1Level1Image1ArrayList.get(0);
                if (bitmap9 != null) {
                    if (!bitmap9.isRecycled()) {
                        bitmap9.recycle();
                    }
                }
                this.tool1Level1Image1ArrayList.remove(0);
            }
            this.tool1Level1Image1ArrayList.clear();
            this.tool1Level1Image1ArrayList = null;
        }
        if (this.tool1Level2Image0ArrayList != null) {
            while (this.tool1Level2Image0ArrayList.size() > 0) {
                Bitmap bitmap10 = this.tool1Level2Image0ArrayList.get(0);
                if (bitmap10 != null) {
                    if (!bitmap10.isRecycled()) {
                        bitmap10.recycle();
                    }
                }
                this.tool1Level2Image0ArrayList.remove(0);
            }
            this.tool1Level2Image0ArrayList.clear();
            this.tool1Level2Image0ArrayList = null;
        }
        if (this.tool1Level2Image1ArrayList != null) {
            while (this.tool1Level2Image1ArrayList.size() > 0) {
                Bitmap bitmap11 = this.tool1Level2Image1ArrayList.get(0);
                if (bitmap11 != null) {
                    if (!bitmap11.isRecycled()) {
                        bitmap11.recycle();
                    }
                }
                this.tool1Level2Image1ArrayList.remove(0);
            }
            this.tool1Level2Image1ArrayList.clear();
            this.tool1Level2Image1ArrayList = null;
        }
        if (this.tool2Level0Image0ArrayList != null) {
            while (this.tool2Level0Image0ArrayList.size() > 0) {
                Bitmap bitmap12 = this.tool2Level0Image0ArrayList.get(0);
                if (bitmap12 != null) {
                    if (!bitmap12.isRecycled()) {
                        bitmap12.recycle();
                    }
                }
                this.tool2Level0Image0ArrayList.remove(0);
            }
            this.tool2Level0Image0ArrayList.clear();
            this.tool2Level0Image0ArrayList = null;
        }
        if (this.tool2Level0Image1ArrayList != null) {
            while (this.tool2Level0Image1ArrayList.size() > 0) {
                Bitmap bitmap13 = this.tool2Level0Image1ArrayList.get(0);
                if (bitmap13 != null) {
                    if (!bitmap13.isRecycled()) {
                        bitmap13.recycle();
                    }
                }
                this.tool2Level0Image1ArrayList.remove(0);
            }
            this.tool2Level0Image1ArrayList.clear();
            this.tool2Level0Image1ArrayList = null;
        }
        if (this.campaignJsonRequest != null) {
            this.campaignJsonRequest.onDestroy();
            this.campaignJsonRequest = null;
        }
        if (this.mainTimerRunnable != null) {
            this.mainTimerRunnable.stop();
            Log.d("mainTimerRunnable", "mainTimerRunnable:" + this.mainTimerRunnable.runFlag);
            this.mainTimerRunnable = null;
        }
        if (this.mainTimerThread != null) {
            this.mainTimerThread.interrupt();
            Log.d("mainTimerThread", "mainTimerThread:" + this.mainTimerThread.isInterrupted());
            this.mainTimerThread = null;
        }
        if (this.mainTimerHandler != null) {
            this.mainTimerHandler.removeCallbacksAndMessages(null);
            Log.d("mainTimerHandler", "mainTimerHandler:mainTimerHandler.removeCallbacksAndMessages");
            this.mainTimerHandler = null;
        }
        this.defaultSharedPreferences = null;
        this.nowAppMainActivity = null;
    }

    public void doWillEnterForeground() {
        checkCampaignChickenUnlock();
        clearNotification();
        if (this.campaignJsonRequest != null) {
            this.campaignJsonRequest.startRequest(false);
        }
    }

    public void shareToLineWithText(String _sendTextString) {
        if (checkPackageInstalled("jp.naver.line.android")) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setClassName("jp.naver.line.android", "jp.naver.line.android.activity.selectchat.SelectChatActivity");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", _sendTextString);
            Context gotAppMainActivity = (Context) this.nowAppMainActivity;
            gotAppMainActivity.startActivity(intent);
        }
    }

    public void shareToLineWithImage(Uri _uri) {
        if (checkPackageInstalled("jp.naver.line.android") && _uri != null) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setClassName("jp.naver.line.android", "jp.naver.line.android.activity.selectchat.SelectChatActivity");
            intent.setType("image/png");
            intent.putExtra("android.intent.extra.STREAM", _uri);
            Context gotAppMainActivity = (Context) this.nowAppMainActivity;
            gotAppMainActivity.startActivity(intent);
        }
    }

    public void shareMailWithUriImage(String _intentTitle, String _subject, String _text, Uri _uriToImage) {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.shareMailWithUriImage(_intentTitle, _subject, _text, _uriToImage);
        }
    }

    public void emailUs() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.emailUs();
        }
    }

    public void goToSlosPage() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.goToSlosPage();
        }
    }

    public void goToCKKB() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.goToCKKB();
        }
    }

    public void goToCKMV() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.goToCKMV();
        }
    }

    public int getBitmapScale(float bitmapSize, float screenSize) {
        int scale = (int) (bitmapSize / bitmapSize);
        if (scale < 2) {
            return 1;
        }
        if (scale < 4) {
            return 2;
        }
        return 8;
    }

    public void readyDoShareImage(short _type) {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.readyDoShareImage(_type);
        }
    }

    public void readyDoFbLogin(short _type) {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.readyDoFbLogin(_type);
        }
    }

    public void readyDoFacebookLogout() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.readyDoFacebookLogout();
        }
    }

    public boolean getLogoutF() {
        if (this.nowAppMainActivity == null) {
            return false;
        }
        boolean logoutF = this.nowAppMainActivity.getLogoutF();
        return logoutF;
    }

    public Boolean doValidateEmail(String email) {
        Boolean.valueOf(true);
        if (email == null) {
            return false;
        }
        Boolean isValid = Boolean.valueOf(email.matches("^([\\w]+)(([-\\.][\\w]+)?)*@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"));
        return isValid;
    }

    public String getTimeSaveDictionaryKeyNumber(TimeSaveDictionary _timeSaveDictionary) {
        String key_number = "";
        if (_timeSaveDictionary == null) {
            return "";
        }
        String check_key_number = _timeSaveDictionary.getKeyNumber();
        if (check_key_number == null || check_key_number.length() <= 0) {
            _timeSaveDictionary.setKeyNumber(createNewKeyNumber());
        }
        if (_timeSaveDictionary.getKeyNumber() != null) {
            key_number = _timeSaveDictionary.getKeyNumber();
        }
        if (key_number == null) {
            key_number = "";
        }
        return key_number;
    }

    public String createNewKeyNumber() {
        String createNewKeyNumber = getRandToken() + getCreateDate() + getRandToken();
        return createNewKeyNumber;
    }

    public String getDeviceID() {
        if (this.defaultSharedPreferences == null) {
            this.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        if (this.defaultSharedPreferences == null) {
            return "";
        }
        String check_device_id = this.defaultSharedPreferences.getString("device_id", "");
        if (check_device_id == null) {
            SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
            editor.putString("device_id", createNewDeviceID());
            editor.commit();
        } else if (check_device_id.length() <= 0) {
            SharedPreferences.Editor editor2 = this.defaultSharedPreferences.edit();
            editor2.putString("device_id", createNewDeviceID());
            editor2.commit();
        }
        String device_id = this.defaultSharedPreferences.getString("device_id", "");
        return device_id;
    }

    public String createNewDeviceID() {
        String device_id = String.valueOf(getMachineName()) + "_" + getCreateDate() + getRandToken();
        return device_id;
    }

    public String getMachineName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String tags = Build.TAGS;
        String type = Build.TYPE;
        String machineNameString = manufacturer + "_" + model + "_" + product + "_" + tags + "_" + type;
        return machineNameString;
    }

    public String getCreateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        Date nowDate = new Date();
        String createDateString = sdf.format(nowDate);
        if (createDateString == null) {
            createDateString = "";
        }
        return createDateString;
    }

    public String getRandToken() {
        short randIndex0 = (short) (Math.random() * "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
        short randIndex1 = (short) (Math.random() * "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
        short randIndex2 = (short) (Math.random() * "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
        short randIndex3 = (short) (Math.random() * "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
        String randTokenString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(randIndex0, randIndex0 + 1) + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(randIndex1, randIndex1 + 1) + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(randIndex2, randIndex2 + 1) + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(randIndex3, randIndex3 + 1);
        return randTokenString;
    }

    public void initTimeSaveDictionaryWithJSONString(String _jsonString) throws JSONException {
        String messageString;
        JSONObject json;
        short version_level;
        Date saveDate;
        short successCode = 0;
        TimeSaveDictionary newTimeSaveDictionary = null;
        if (_jsonString.length() > 1000) {
            try {
                json = new JSONObject(_jsonString);
            } catch (JSONException e) {
                json = null;
            }
            if (json != null) {
                String saveDateString = "";
                try {
                    version_level = (short) json.getInt("vl");
                } catch (JSONException e2) {
                    version_level = -1;
                }
                if (version_level <= 0 || version_level > 30) {
                    successCode = 101;
                }
                if (successCode == 0) {
                    try {
                        saveDateString = json.getString("d");
                    } catch (JSONException e3) {
                        saveDateString = "";
                    }
                    if (saveDateString == null) {
                        saveDateString = "";
                    }
                    if (saveDateString.length() > 0) {
                        Boolean saveDateOkF = false;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date nowDate = new Date();
                        try {
                            saveDate = sdf.parse(saveDateString);
                        } catch (ParseException e4) {
                            saveDate = null;
                        }
                        if (saveDate != null && saveDate.before(nowDate)) {
                            saveDateOkF = true;
                        }
                        if (!saveDateOkF.booleanValue()) {
                            successCode = 102;
                        }
                    } else {
                        successCode = 102;
                    }
                }
                if (successCode == 0 && saveDateString.length() > 0) {
                    newTimeSaveDictionary = new TimeSaveDictionary(this, _jsonString);
                    newTimeSaveDictionary.setDate(saveDateString);
                }
            } else {
                successCode = 100;
            }
        } else {
            successCode = 100;
        }
        if (successCode == 0 && newTimeSaveDictionary != null) {
            this.timeSaveDictionary = null;
            if (this.mainSavesDictionary != null) {
                if (this.mainSavesDictionary.timeSavesArrayList != null) {
                    this.mainSavesDictionary.timeSavesArrayList.clear();
                }
            } else {
                initMainSavesDictionary();
            }
            this.timeSaveDictionary = newTimeSaveDictionary;
            doSaveTimeSaveDictionaryOperation();
            SimpleDateFormat sdfday = new SimpleDateFormat("yyyy/MM/dd");
            Date nowDate2 = new Date();
            String getCpBlockDateString = sdfday.format(nowDate2);
            if (getCpBlockDateString == null) {
                getCpBlockDateString = "";
            }
            if (getCpBlockDateString.length() > 0) {
                set_get_cp_block_date(getCpBlockDateString);
            }
            if (this.nowAppMainActivity != null) {
                new AlertDialog.Builder((Context) this.nowAppMainActivity).setTitle("").setMessage(getResources().getString(R.string.SaveSucceeded)).setPositiveButton(getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (successCode == 100) {
            if (this.nowAppMainActivity != null) {
                new AlertDialog.Builder((Context) this.nowAppMainActivity).setTitle("").setMessage(getResources().getString(R.string.ThisSaveFileWasCorrupted)).setPositiveButton(getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (successCode == 101) {
            if (this.nowAppMainActivity != null) {
                new AlertDialog.Builder((Context) this.nowAppMainActivity).setTitle("").setMessage(getResources().getString(R.string.ThisSaveFileCanNotBeUsedInTheCurrentVersion)).setPositiveButton(getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (successCode == 102) {
            String languageString = getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                messageString = "セーブデータまたは端末の日付\nと時刻が正しくないようです。\n正しく設定して、もう一度\nダウンロードしてください。";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                messageString = "記錄存檔或手機的日期與\n時間是不正確的。\n請調整後再次下載。";
            } else if (languageString.equals("zh-CN")) {
                messageString = "记录存档或手机的日期与\n时间是不正确的。\n请调整後再次下载。";
            } else {
                messageString = "Please correct the system date and\ntime,and then download\nagain.";
            }
            if (this.nowAppMainActivity != null) {
                new AlertDialog.Builder((Context) this.nowAppMainActivity).setTitle("").setMessage(messageString).setPositiveButton(getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (successCode == 200 && this.nowAppMainActivity != null) {
            new AlertDialog.Builder((Context) this.nowAppMainActivity).setTitle("").setMessage(getResources().getString(R.string.ThisSaveFileWasCorrupted)).setPositiveButton(getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
        }
    }

    public String changTimeSaveDictionaryToJSONwithDateString(String _dateString) {
        if (this.timeSaveDictionary != null && _dateString != null) {
            this.timeSaveDictionary.setDate(_dateString);
            String jsonString = this.timeSaveDictionary.changeToJSONObject(this).toString();
            return jsonString.replaceAll("<time_slash>", "/");
        }
        return "";
    }

    public void goToTermsOfService(String _termsOfServiceString) {
        if (!checkInterNet()) {
            showNoInternetAlertDialog();
            return;
        }
        if (_termsOfServiceString != null && _termsOfServiceString.length() >= 10) {
            Uri uri = Uri.parse(_termsOfServiceString);
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            if (this.nowAppMainActivity != null) {
                Context gotAppMainActivity = (Context) this.nowAppMainActivity;
                gotAppMainActivity.startActivity(intent);
            }
        }
    }

    public short getNowTimeIndex() throws NumberFormatException {
        short nowTimeIndex;
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        Date nowDate = new Date();
        int hourInt = Integer.parseInt(sdf.format(nowDate));
        if (hourInt >= 5 && hourInt < 8) {
            nowTimeIndex = 0;
        } else if (hourInt >= 8 && hourInt < 17) {
            nowTimeIndex = 10;
        } else if (hourInt >= 17 && hourInt < 19) {
            nowTimeIndex = 20;
        } else {
            nowTimeIndex = 30;
        }
        return nowTimeIndex;
    }

    public boolean isInstallSoftware(String packageName) {
        if (this.nowAppMainActivity == null) {
            return false;
        }
        boolean returnF = this.nowAppMainActivity.isInstallSoftware(packageName);
        return returnF;
    }

    public void checkCampaignChickenUnlock() {
        if (this.defaultSharedPreferences != null) {
            SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
            editor.putBoolean("campaign_char_0_88", isInstallSoftware("com.idtinc.ckkujibiki"));
            editor.putBoolean("campaign_char_0_105", isInstallSoftware("com.idtinc.ckmonstervillage"));
            editor.commit();
        }
    }

    public void set_gift_tool_2(short _toolIndex, boolean _unlockF) {
        if (this.defaultSharedPreferences != null) {
            SharedPreferences.Editor editor = this.defaultSharedPreferences.edit();
            if (_toolIndex == 68) {
                editor.putBoolean("gift_tool_2_68", _unlockF);
            }
            if (_toolIndex == 69) {
                editor.putBoolean("gift_tool_2_69", _unlockF);
            }
            if (_toolIndex == 70) {
                editor.putBoolean("gift_tool_2_70", _unlockF);
            }
            editor.commit();
        }
    }

    public boolean checkPackageInstalled(String _packageNameString) {
        boolean lineInstallFlag = false;
        if (_packageNameString != null && _packageNameString.length() > 0) {
            PackageManager pm = getPackageManager();
            List<ApplicationInfo> m_appList = pm.getInstalledApplications(0);
            Iterator<ApplicationInfo> it = m_appList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ApplicationInfo ai = it.next();
                if (ai.packageName.equals(_packageNameString)) {
                    lineInstallFlag = true;
                    break;
                }
            }
            return lineInstallFlag;
        }
        return false;
    }

    public boolean checkInterNet() {
        ConnectivityManager myConnMgr = (ConnectivityManager) getSystemService("connectivity");
        NetworkInfo networkInfo = myConnMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void showNoInternetAlertDialog() {
        if (this.nowAppMainActivity != null) {
            this.nowAppMainActivity.showNoInternetAlertDialog();
        }
    }
}

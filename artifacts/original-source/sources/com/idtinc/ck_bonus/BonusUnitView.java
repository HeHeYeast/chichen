package com.idtinc.ck_bonus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.maingame.MainGameViewController;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class BonusUnitView extends View {
    private String GET_CP_JSON_REQUEST_URL;
    public float SCROLLVIEW_HEIGHT;
    public float SCROLLVIEW_OFFSET_X;
    public float SCROLLVIEW_OFFSET_Y;
    public float SCROLLVIEW_WIDTH;
    private AppDelegate appDelegate;
    private BonusFrontViewUnit bonusFrontViewUnit;
    public ArrayList<BonusPage> bonusPagesArrayList;
    private BonusScrollViewUnit bonusScrollViewUnit;
    private float finalHeight;
    private float finalWidth;
    public MainGameViewController mainGameControllerLayout;
    public short nowStatus;
    private RequestAsyncTask requestAsyncTask;
    private float zoomRate;

    public BonusUnitView(Context context, float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameControllerLayout) {
        super(context);
        this.GET_CP_JSON_REQUEST_URL = "http://idtinc.home.dyndns.org/GetCPAndroid/json_ckcd.php";
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.nowStatus = (short) -1;
        this.SCROLLVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.SCROLLVIEW_OFFSET_Y = 40.0f;
        this.SCROLLVIEW_WIDTH = 320.0f;
        this.SCROLLVIEW_HEIGHT = 390.0f;
        this.appDelegate = null;
        this.mainGameControllerLayout = null;
        this.requestAsyncTask = null;
        this.bonusPagesArrayList = null;
        this.bonusScrollViewUnit = null;
        this.bonusFrontViewUnit = null;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.mainGameControllerLayout = _mainGameControllerLayout;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.SCROLLVIEW_OFFSET_X = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.SCROLLVIEW_OFFSET_Y = this.zoomRate * 40.0f;
        this.SCROLLVIEW_WIDTH = this.finalWidth;
        if (!this.appDelegate.isRetina4) {
            this.SCROLLVIEW_HEIGHT = this.zoomRate * 390.0f;
        } else {
            this.SCROLLVIEW_HEIGHT = 478.0f * this.zoomRate;
        }
        this.nowStatus = (short) -1;
        this.bonusScrollViewUnit = new BonusScrollViewUnit((int) this.SCROLLVIEW_OFFSET_X, (int) this.SCROLLVIEW_OFFSET_Y, (int) this.SCROLLVIEW_WIDTH, (int) this.SCROLLVIEW_HEIGHT, this.zoomRate, this, this.appDelegate);
        this.bonusFrontViewUnit = new BonusFrontViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        clearRequestAsyncTask();
    }

    public void goToGetWithIndex(short _index) {
        BonusPage bonusPage;
        doBonusLayoutHidden();
        if (this.appDelegate != null) {
            if (!this.appDelegate.checkInterNet()) {
                this.appDelegate.showNoInternetAlertDialog();
                return;
            }
            if (this.bonusPagesArrayList != null && _index >= 0 && _index < this.bonusPagesArrayList.size() && (bonusPage = this.bonusPagesArrayList.get(_index)) != null && !bonusPage.clickedF) {
                bonusPage.clickedF = true;
                if (bonusPage.buttonStatus == 0) {
                    this.mainGameControllerLayout.goToGetWithIndex(bonusPage, (short) 0);
                } else {
                    this.mainGameControllerLayout.goToGetWithIndex(bonusPage, (short) 1);
                }
                if (getVisibility() == 0) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                bonusPage.clickedF = true;
            }
        }
    }

    public void initRequest() {
        if (this.appDelegate != null) {
            this.nowStatus = (short) -1;
            clearRequestAsyncTask();
            if (this.bonusPagesArrayList == null) {
                this.bonusPagesArrayList = new ArrayList<>();
            }
        }
    }

    public void requestStart() {
        if (this.appDelegate != null) {
            if (!this.appDelegate.checkInterNet()) {
                this.appDelegate.showNoInternetAlertDialog();
                return;
            }
            if (this.nowStatus != 0) {
                initRequest();
                this.nowStatus = (short) 0;
                clearRequestAsyncTask();
                if (this.appDelegate.checkInterNet()) {
                    this.requestAsyncTask = new RequestAsyncTask(this, null);
                    this.requestAsyncTask.execute(this.GET_CP_JSON_REQUEST_URL);
                }
            }
        }
    }

    public boolean getThisHidden() {
        return getVisibility() != 0;
    }

    public void doBonusLayoutHidden() {
        this.mainGameControllerLayout.doBonusLayoutHidden(true);
    }

    public void doLoop() {
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public void doDisplay() throws IOException {
        if (this.appDelegate != null) {
            this.appDelegate.hiddenAdControlLayout(true);
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ck_bonus.BonusUnitView.1
                @Override // java.lang.Runnable
                public void run() {
                    BonusUnitView.this.hiddenBonusUnitViewAd(false);
                }
            }, 1000L);
        }
        refreshBitmap();
        requestStart();
        setVisibility(0);
    }

    public void doHidden() {
        initRequest();
        setVisibility(8);
        if (this.appDelegate != null) {
            this.appDelegate.hiddenBonusUnitViewAd(true);
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ck_bonus.BonusUnitView.2
                @Override // java.lang.Runnable
                public void run() {
                    BonusUnitView.this.hiddenAdControlLayout(false);
                }
            }, 1000L);
        }
    }

    public void hiddenAdControlLayout(boolean _hddenF) {
        if (this.appDelegate != null) {
            this.appDelegate.hiddenAdControlLayout(_hddenF);
        }
    }

    public void hiddenBonusUnitViewAd(boolean _hddenF) {
        if (this.appDelegate != null) {
            this.appDelegate.hiddenBonusUnitViewAd(_hddenF);
        }
    }

    public void clearBitmap() {
        if (this.bonusFrontViewUnit != null) {
            this.bonusFrontViewUnit.clearBitmap();
        }
    }

    public void refreshBitmap() throws IOException {
        if (this.bonusFrontViewUnit != null) {
            this.bonusFrontViewUnit.refreshBitmap();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (getVisibility() != 0) {
            return false;
        }
        if (this.bonusFrontViewUnit != null) {
            boolean returnF = this.bonusFrontViewUnit.gameOnTouch(event);
            if (returnF) {
                return true;
            }
        }
        if (this.bonusScrollViewUnit != null) {
            this.bonusScrollViewUnit.gameOnTouch(event);
        }
        return true;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        if (this.bonusScrollViewUnit != null) {
            this.bonusScrollViewUnit.gameDraw(canvas);
        }
        if (this.bonusFrontViewUnit != null) {
            this.bonusFrontViewUnit.gameDraw(canvas);
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

        /* synthetic */ RequestAsyncTask(BonusUnitView bonusUnitView, RequestAsyncTask requestAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(String... params) throws IOException {
            if (BonusUnitView.this.appDelegate == null) {
                return null;
            }
            JSONObject json = null;
            if (BonusUnitView.this.nowStatus != 0) {
                return null;
            }
            try {
                URL url = new URL(BonusUnitView.this.GET_CP_JSON_REQUEST_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(HapticContentSDK.f17b04440444044404440444);
                conn.setRequestMethod("GET");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                if (jsonString != null) {
                    Log.e("jsonString", "jsonString:" + jsonString);
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
            if (BonusUnitView.this.appDelegate != null) {
                if ((BonusUnitView.this.nowStatus != 0 && BonusUnitView.this.nowStatus != 1) || _json == null) {
                    BonusUnitView.this.initRequest();
                } else {
                    Log.i("BonusLayout", "onSuccess: JSONObject");
                    Log.i("BonusLayout", _json.toString());
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = (JSONArray) _json.get("items");
                    } catch (JSONException e) {
                    }
                    if (jsonArray != null) {
                        Log.i("jsonArray", "jsonArray.length():" + jsonArray.length());
                        int bonusPagesArrayListIndex = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = null;
                            try {
                                itemObject = (JSONObject) jsonArray.get(i);
                            } catch (JSONException e2) {
                            }
                            if (itemObject != null) {
                                int itemID = -1;
                                try {
                                    itemID = itemObject.getInt("itemID");
                                } catch (JSONException e3) {
                                }
                                String openDateKeyString = null;
                                try {
                                    openDateKeyString = itemObject.getString("openDateKey");
                                } catch (JSONException e4) {
                                }
                                if (openDateKeyString != null && openDateKeyString.length() >= 3) {
                                    int openType = 0;
                                    try {
                                        openType = itemObject.getInt("openType");
                                    } catch (JSONException e5) {
                                    }
                                    if (openType < 0) {
                                        openType = 0;
                                    }
                                    int bonus = -1;
                                    try {
                                        bonus = itemObject.getInt("bonus");
                                    } catch (JSONException e6) {
                                    }
                                    if (bonus < 0) {
                                        int coins = -1;
                                        if (BonusUnitView.this.appDelegate != null && BonusUnitView.this.appDelegate.checkAdColonyV4VCF()) {
                                            try {
                                                coins = itemObject.getInt("coins");
                                            } catch (JSONException e7) {
                                            }
                                        }
                                        if (coins >= 0) {
                                            bonus = coins;
                                        }
                                    }
                                    if (bonus >= 0) {
                                        String languageString = BonusUnitView.this.appDelegate.getLocaleLanguage();
                                        String urlKeyString = "enUrl";
                                        String iconUrlKeyString = "enIconUrl";
                                        String titleKeyString = "enTitle";
                                        if (languageString.equals("ja-JP")) {
                                            urlKeyString = "jaUrl";
                                            iconUrlKeyString = "jaIconUrl";
                                            titleKeyString = "jaTitle";
                                        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                                            urlKeyString = "zhTWUrl";
                                            iconUrlKeyString = "zhTWIconUrl";
                                            titleKeyString = "zhTWTitle";
                                        } else if (languageString.equals("zh-CN")) {
                                            urlKeyString = "zhCNUrl";
                                            iconUrlKeyString = "zhCNIconUrl";
                                            titleKeyString = "zhCNTitle";
                                        }
                                        String urlString = null;
                                        try {
                                            urlString = itemObject.getString(urlKeyString);
                                        } catch (JSONException e8) {
                                        }
                                        if (urlString != null && urlString.length() >= 10) {
                                            String iconUrlString = null;
                                            try {
                                                iconUrlString = itemObject.getString(iconUrlKeyString);
                                            } catch (JSONException e9) {
                                            }
                                            if (iconUrlString == null) {
                                                iconUrlString = "";
                                            }
                                            String titleString = null;
                                            try {
                                                titleString = itemObject.getString(titleKeyString);
                                            } catch (JSONException e10) {
                                            }
                                            if (titleString == null) {
                                                titleString = "";
                                            }
                                            String contentString = null;
                                            try {
                                                contentString = itemObject.getString("zhTWContent");
                                            } catch (JSONException e11) {
                                            }
                                            if (contentString == null) {
                                                contentString = "";
                                            }
                                            if (BonusUnitView.this.bonusPagesArrayList != null) {
                                                if (bonusPagesArrayListIndex < BonusUnitView.this.bonusPagesArrayList.size()) {
                                                    boolean reloadSmallImageF = true;
                                                    BonusPage bonusPage = BonusUnitView.this.bonusPagesArrayList.get(bonusPagesArrayListIndex);
                                                    if (bonusPage.iconUrlString.equals(iconUrlString) && bonusPage.smallImageBitmap != null) {
                                                        reloadSmallImageF = false;
                                                    }
                                                    bonusPage.reSet(itemID, openDateKeyString, openType, bonus, urlString, iconUrlString, titleString, contentString, BonusUnitView.this.appDelegate);
                                                    if (reloadSmallImageF) {
                                                        bonusPage.startRequest();
                                                    }
                                                } else {
                                                    BonusPage bonusPage2 = new BonusPage(itemID, openDateKeyString, openType, bonus, urlString, iconUrlString, titleString, contentString, BonusUnitView.this.appDelegate);
                                                    BonusUnitView.this.bonusPagesArrayList.add(bonusPage2);
                                                    bonusPage2.startRequest();
                                                }
                                            }
                                            bonusPagesArrayListIndex++;
                                        }
                                    }
                                }
                            }
                        }
                        if (BonusUnitView.this.bonusPagesArrayList != null) {
                            BonusUnitView.this.nowStatus = (short) 1;
                            while (BonusUnitView.this.bonusPagesArrayList.size() > bonusPagesArrayListIndex) {
                                BonusUnitView.this.bonusPagesArrayList.get(BonusUnitView.this.bonusPagesArrayList.size() - 1).onDestroy();
                                BonusUnitView.this.bonusPagesArrayList.remove(BonusUnitView.this.bonusPagesArrayList.size() - 1);
                            }
                            BonusUnitView.this.bonusScrollViewUnit.reload();
                        }
                    }
                }
                super.onPostExecute((RequestAsyncTask) _json);
            }
        }
    }

    public void onDestroy() {
        this.nowStatus = (short) -999;
        clearRequestAsyncTask();
        if (this.bonusPagesArrayList != null) {
            while (this.bonusPagesArrayList.size() > 0) {
                BonusPage bonusPage = this.bonusPagesArrayList.get(0);
                bonusPage.onDestroy();
                this.bonusPagesArrayList.remove(0);
            }
            this.bonusPagesArrayList.clear();
        }
        if (this.bonusFrontViewUnit != null) {
            this.bonusFrontViewUnit.onDestroy();
            this.bonusFrontViewUnit = null;
        }
        if (this.bonusScrollViewUnit != null) {
            this.bonusScrollViewUnit.onDestroy();
            this.bonusScrollViewUnit = null;
        }
        this.mainGameControllerLayout = null;
        this.appDelegate = null;
    }
}

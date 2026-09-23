package com.idtinc.onlinegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.AlertUnitType0;
import com.idtinc.custom.AlertUnitType0Delegate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class DownloadSaveFileUnit implements AlertUnitType0Delegate {
    private AlertUnitType0 alertUnitType0;
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public Boolean hidden;
    public short loginType;
    private OnlineGameViewController onlineGameViewController;
    public String saveFileString;
    private float zoomRate;
    String testString = "";
    private final String DOWNLOAD_SAVE_FILE_REQUEST_URL = "http://www.idtgame.com/idtgameserver/games/ckcd/downloadCkdata/";
    private final String FB_DOWNLOAD_SAVE_FILE_REQUEST_URL = "http://www.idtgame.com/idtgameserver/games/ckcd/fbdownloadCkdata/";
    private final int DOWNLOAD_SAVE_FILE_REQUEST_TIMEOUT_INTERVAL = 30000;
    private RequestAsyncTask requestAsyncTask = null;

    public DownloadSaveFileUnit(float _finalwidth, float _finalheight, float _zoomrate, OnlineGameViewController _onlineGameViewController, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.loginType = (short) -1;
        this.appDelegate = null;
        this.onlineGameViewController = null;
        this.saveFileString = null;
        this.appDelegate = _appDelegate;
        this.onlineGameViewController = _onlineGameViewController;
        this.saveFileString = null;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.loginType = (short) -1;
        this.alertUnitType0 = new AlertUnitType0(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 140.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        } else {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 184.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        }
        this.alertUnitType0.delegate = this;
        clearRequestAsyncTask();
    }

    public void clear() {
        this.loginType = (short) -1;
        this.saveFileString = null;
        clearRequestAsyncTask();
    }

    public void reset() {
        this.hidden = true;
        clear();
        hiddenAlert();
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.stopLoading();
        }
    }

    public void close() {
        this.hidden = true;
        reset();
    }

    public void openWithAutoDownload(Boolean _autoDownloadF) {
        Boolean canDownloadF = false;
        reset();
        if (this.appDelegate != null) {
            String fb_account_id = this.appDelegate.get_fb_account_id();
            String idt_account_id = this.appDelegate.get_idt_account_id();
            String idt_account_password = this.appDelegate.get_idt_account_password();
            if (fb_account_id.length() > 0) {
                canDownloadF = true;
            } else if (idt_account_id != null && idt_account_password != null && idt_account_id.length() > 0 && idt_account_password.length() > 0) {
                canDownloadF = true;
            }
        }
        if (_autoDownloadF.booleanValue() && canDownloadF.booleanValue()) {
            doDownload();
            this.hidden = false;
            return;
        }
        close();
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.doNextActiveStatus((short) 200);
        }
        if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
        }
    }

    public void doDownload() {
        RequestAsyncTask requestAsyncTask = null;
        if (this.appDelegate == null) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.doNextActiveStatus((short) 200);
                return;
            }
            return;
        }
        clearRequestAsyncTask();
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.stopLoading();
        }
        if (!this.appDelegate.checkInterNet()) {
            this.appDelegate.showNoInternetAlertDialog();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.doNextActiveStatus((short) 200);
                return;
            }
            return;
        }
        Boolean okF = false;
        String fb_account_id = "";
        if (this.appDelegate != null) {
            fb_account_id = this.appDelegate.get_fb_account_id();
        }
        if (fb_account_id != null && fb_account_id.length() > 0) {
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.Downloading));
            }
            this.loginType = (short) 1;
            String urlString = "http://www.idtgame.com/idtgameserver/games/ckcd/fbdownloadCkdata/?rd=" + this.appDelegate.getNowDateString();
            Log.e("test_urlString", "urlString_fb_d:" + urlString);
            this.requestAsyncTask = new RequestAsyncTask(this, requestAsyncTask);
            this.requestAsyncTask.execute(urlString);
            Boolean.valueOf(true);
            return;
        }
        if (!okF.booleanValue()) {
            String IDString = this.appDelegate.get_idt_account_id();
            String PWString = this.appDelegate.get_idt_account_password();
            if (IDString != null && PWString != null && IDString.length() > 0 && PWString.length() > 0) {
                if (this.onlineGameViewController != null) {
                    this.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.Downloading));
                }
                this.loginType = (short) 0;
                String urlString2 = "http://www.idtgame.com/idtgameserver/games/ckcd/downloadCkdata/?rd=" + this.appDelegate.getNowDateString();
                Log.e("test_urlString", "urlString_d:" + urlString2);
                this.requestAsyncTask = new RequestAsyncTask(this, requestAsyncTask);
                this.requestAsyncTask.execute(urlString2);
                Boolean.valueOf(true);
                return;
            }
        }
        if (!okF.booleanValue()) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.doNextActiveStatus((short) 200);
            }
        }
    }

    public void readyDoSaveNewSaveFile() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "ダウンロードしたセーブデータ";
            contentLabelString2 = "使いますか？";
            contentLabelString3 = "(現在のセーブデータを上書きします)";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "你是否要使用下載的記錄存檔？";
            contentLabelString2 = "(將會覆蓋目前的記錄存檔)";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "你是否要使用下载的记录存档？";
            contentLabelString2 = "(将会覆盖目前的记录存档)";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "Do you want to use downloaded";
            contentLabelString2 = "save file?";
            contentLabelString3 = "(Will overwrite current save file.)";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, -1, 4.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, -200082, 3.0f, -227838, 3.0f, -12988, BitmapDescriptorFactory.HUE_RED, 0, 18.0f, 2, 1, false);
        this.alertUnitType0.tag = (short) 200;
        this.alertUnitType0.subTag = (short) -1;
        popAlert();
        if (!this.hidden.booleanValue()) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void doSaveNewSaveFile() throws JSONException {
        if (this.appDelegate != null && this.saveFileString != null) {
            this.appDelegate.initTimeSaveDictionaryWithJSONString(this.saveFileString);
        }
    }

    public void popAlert() {
        this.alertUnitType0.pop();
    }

    public void hiddenAlert() {
        this.alertUnitType0.reset();
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) throws JSONException {
        if (_tag == 200) {
            if (_buttonIndex == 0) {
                if (this.onlineGameViewController != null) {
                    this.onlineGameViewController.doNextActiveStatus((short) 200);
                }
            } else if (_buttonIndex == 1) {
                doSaveNewSaveFile();
                this.appDelegate.displayFullAdView();
                if (this.onlineGameViewController != null) {
                    this.onlineGameViewController.doNextActiveStatus((short) 200);
                }
            }
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

        /* synthetic */ RequestAsyncTask(DownloadSaveFileUnit downloadSaveFileUnit, RequestAsyncTask requestAsyncTask) {
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
            if (DownloadSaveFileUnit.this.appDelegate == null) {
                return null;
            }
            JSONObject json = null;
            try {
            } catch (MalformedURLException e) {
                e = e;
            } catch (IOException e2) {
                return json;
            }
            try {
                if (DownloadSaveFileUnit.this.loginType == 1) {
                    String urlString = "http://www.idtgame.com/idtgameserver/games/ckcd/fbdownloadCkdata/?rd=" + DownloadSaveFileUnit.this.appDelegate.getNowDateString();
                    url = new URL(urlString);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(30000);
                    conn.setConnectTimeout(30000);
                    conn.setRequestProperty("fb_id", DownloadSaveFileUnit.this.appDelegate.get_fb_account_id());
                    conn.setRequestProperty("password", DownloadSaveFileUnit.this.appDelegate.get_fb_account_password());
                } else {
                    String urlString2 = "http://www.idtgame.com/idtgameserver/games/ckcd/downloadCkdata/?rd=" + DownloadSaveFileUnit.this.appDelegate.getNowDateString();
                    url = new URL(urlString2);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(30000);
                    conn.setConnectTimeout(30000);
                    conn.setRequestProperty("account", DownloadSaveFileUnit.this.appDelegate.get_idt_account_id());
                    conn.setRequestProperty("password", DownloadSaveFileUnit.this.appDelegate.get_idt_account_password());
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
                    } catch (JSONException e3) {
                        e3.printStackTrace();
                    }
                }
                reader.close();
                return json;
            } catch (MalformedURLException e4) {
                e = e4;
                e.printStackTrace();
                return json;
            } catch (IOException e5) {
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate((Object[]) progress);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x006c -> B:26:0x003e). Please report as a decompilation issue!!! */
        @Override // android.os.AsyncTask
        public void onPostExecute(JSONObject _json) throws JSONException {
            String successString = "-1";
            String saveDateString = "";
            String keyNumberString = "";
            DownloadSaveFileUnit.this.saveFileString = null;
            if (_json != null) {
                try {
                    successString = _json.getString("success");
                } catch (JSONException e) {
                    successString = "-1";
                }
                try {
                    saveDateString = _json.getString("save_date");
                } catch (JSONException e2) {
                    saveDateString = "";
                }
                if (saveDateString == null) {
                    saveDateString = "";
                }
                try {
                    keyNumberString = _json.getString("key_number");
                } catch (JSONException e3) {
                    keyNumberString = null;
                }
                try {
                    DownloadSaveFileUnit.this.saveFileString = _json.getString("save_file");
                    if (DownloadSaveFileUnit.this.saveFileString.length() <= 1000) {
                        DownloadSaveFileUnit.this.saveFileString = null;
                    } else {
                        Log.d("saveFileString", "saveFileString:" + DownloadSaveFileUnit.this.saveFileString);
                    }
                } catch (JSONException e4) {
                    DownloadSaveFileUnit.this.saveFileString = null;
                }
            }
            DownloadSaveFileUnit.this.doLoginResponse(successString, saveDateString, keyNumberString);
            super.onPostExecute((RequestAsyncTask) _json);
        }
    }

    public void doLoginResponse(String _successString, String _saveDateString, String _keyNumberString) {
        Date saveDate;
        String messageString;
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.stopLoading();
        }
        if (_successString.equals("0")) {
            if (_saveDateString.length() > 0 && this.saveFileString != null) {
                Boolean saveDateOkF = false;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date nowDate = new Date();
                try {
                    saveDate = sdf.parse(_saveDateString);
                } catch (ParseException e) {
                    saveDate = null;
                }
                if (saveDate != null && saveDate.before(nowDate)) {
                    saveDateOkF = true;
                }
                if (saveDateOkF.booleanValue()) {
                    readyDoSaveNewSaveFile();
                    clearRequestAsyncTask();
                    return;
                }
                String languageString = this.appDelegate.getLocaleLanguage();
                if (languageString.equals("ja-JP")) {
                    messageString = "セーブデータまたは端末の日付\nと時刻が正しくないようです。\n正しく設定して、もう一度\nダウンロードしてください。";
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    messageString = "記錄存檔或手機的日期與\n時間是不正確的。\n請調整後再次下載。";
                } else if (languageString.equals("zh-CN")) {
                    messageString = "记录存档或手机的日期与\n时间是不正确的。\n请调整後再次下载。";
                } else {
                    messageString = "Please correct the system date and\ntime,and then download\nagain.";
                }
                if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                    new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(messageString).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                }
            } else if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.DownloadFailed)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (_successString.equals("103") || _successString.equals("900") || _successString.equals("901")) {
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.NoSaveFileCanBeDownloaded)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.DownloadFailed)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
        }
        clearRequestAsyncTask();
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.doNextActiveStatus((short) 200);
        }
    }

    public void makeNewText(String _newText) {
        Toast.makeText(this.appDelegate, "successString:" + _newText, 0).show();
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (this.alertUnitType0 == null || this.alertUnitType0.hidden) {
            return false;
        }
        this.alertUnitType0.gameOnTouch(event);
        return true;
    }

    public void gameDraw(Canvas canvas) {
        if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
            this.alertUnitType0.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        clearRequestAsyncTask();
        if (this.alertUnitType0 != null) {
            this.alertUnitType0.onDestroy();
            this.alertUnitType0 = null;
        }
        this.onlineGameViewController = null;
        this.appDelegate = null;
    }
}

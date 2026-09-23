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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MIME;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class UploadSaveFileUnit implements AlertUnitType0Delegate {
    private AlertUnitType0 alertUnitType0;
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public Boolean hidden;
    public short loginType;
    private OnlineGameViewController onlineGameViewController;
    private float zoomRate;
    private final String UPLOAD_SAVE_FILE_REQUEST_URL = "http://www.idtgame.com/idtgameserver/games/ckcd/uploadCkdata/";
    private final String FB_UPLOAD_SAVE_FILE_REQUEST_URL = "http://www.idtgame.com/idtgameserver/games/ckcd/fbuploadCkdata/";
    private final int UPLOAD_SAVE_FILE_REQUEST_TIMEOUT_INTERVAL = 30000;
    private RequestAsyncTask requestAsyncTask = null;

    public UploadSaveFileUnit(float _finalwidth, float _finalheight, float _zoomrate, OnlineGameViewController _onlineGameViewController, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.loginType = (short) -1;
        this.appDelegate = null;
        this.onlineGameViewController = null;
        this.appDelegate = _appDelegate;
        this.onlineGameViewController = _onlineGameViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.loginType = (short) -1;
        this.alertUnitType0 = new AlertUnitType0(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 140.0f, 320.0f, 200.0f, -1, 3.0f, -227838, 3.0f, -12988, 3.0f, -1, 25.0f);
        } else {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 184.0f, 320.0f, 200.0f, -1, 3.0f, -227838, 3.0f, -12988, 3.0f, -1, 25.0f);
        }
        this.alertUnitType0.delegate = this;
        clearRequestAsyncTask();
    }

    public void clear() {
        this.loginType = (short) -1;
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

    public void openWithAutoUpload(Boolean _autoUploadF) {
        Boolean canUploadF = false;
        reset();
        if (this.appDelegate != null) {
            String fb_account_id = this.appDelegate.get_fb_account_id();
            String idt_account_id = this.appDelegate.get_idt_account_id();
            String idt_account_password = this.appDelegate.get_idt_account_password();
            if (fb_account_id.length() > 0) {
                canUploadF = true;
            } else if (idt_account_id != null && idt_account_password != null && idt_account_id.length() > 0 && idt_account_password.length() > 0) {
                canUploadF = true;
            }
        }
        if (_autoUploadF.booleanValue()) {
            if (canUploadF.booleanValue()) {
                doUpload();
                this.hidden = false;
                return;
            }
            close();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.doNextActiveStatus((short) 100);
            }
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                return;
            }
            return;
        }
        this.hidden = false;
        readyDoUploadNewSaveFile();
    }

    public void readyDoUploadNewSaveFile() {
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
            contentLabelString1 = "";
            contentLabelString2 = "セーブデータをアップロードしますか？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你是否要上傳目前記錄存檔？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你是否要上传目前记录存档？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "Do you want to upload";
            contentLabelString2 = "save file?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, -1, 4.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, -200082, 3.0f, -227838, 3.0f, -12988, BitmapDescriptorFactory.HUE_RED, 0, 18.0f, 2, 1, false);
        this.alertUnitType0.tag = (short) 100;
        this.alertUnitType0.subTag = (short) -1;
        popAlert();
        if (!this.hidden.booleanValue()) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void doUploadNewSaveFile() {
        Boolean canUploadF = false;
        if (this.appDelegate != null) {
            String fb_account_id = this.appDelegate.get_fb_account_id();
            String idt_account_id = this.appDelegate.get_idt_account_id();
            String idt_account_password = this.appDelegate.get_idt_account_password();
            if (fb_account_id.length() > 0) {
                canUploadF = true;
            } else if (idt_account_id != null && idt_account_password != null && idt_account_id.length() > 0 && idt_account_password.length() > 0) {
                canUploadF = true;
            }
        }
        if (canUploadF.booleanValue()) {
            doUpload();
            this.hidden = false;
            return;
        }
        close();
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.doNextActiveStatus((short) 100);
        }
        if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
        }
    }

    public void doUpload() {
        RequestAsyncTask requestAsyncTask = null;
        if (this.appDelegate == null) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.doNextActiveStatus((short) 100);
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
                this.onlineGameViewController.doNextActiveStatus((short) 100);
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
                this.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.Uploading));
            }
            this.loginType = (short) 1;
            String urlString = "http://www.idtgame.com/idtgameserver/games/ckcd/fbuploadCkdata/?ut=1&rd=" + this.appDelegate.getNowDateString();
            Log.e("test_urlString", "urlString_fb_u:" + urlString);
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
                    this.onlineGameViewController.startLoading(this.appDelegate.getResources().getString(R.string.Uploading));
                }
                this.loginType = (short) 0;
                String urlString2 = "http://www.idtgame.com/idtgameserver/games/ckcd/uploadCkdata/?ut=1&rd=" + this.appDelegate.getNowDateString();
                Log.e("test_urlString", "urlString_u:" + urlString2);
                this.requestAsyncTask = new RequestAsyncTask(this, requestAsyncTask);
                this.requestAsyncTask.execute(urlString2);
                Boolean.valueOf(true);
                return;
            }
        }
        if (!okF.booleanValue()) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.ErrorEstablishingADatabaseConnection)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            if (this.onlineGameViewController != null) {
                this.onlineGameViewController.doNextActiveStatus((short) 100);
            }
        }
    }

    public void popAlert() {
        this.alertUnitType0.pop();
    }

    public void hiddenAlert() {
        this.alertUnitType0.reset();
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) {
        if (_tag == 100) {
            if (_buttonIndex == 0) {
                if (this.onlineGameViewController != null) {
                    this.onlineGameViewController.doNextActiveStatus((short) 100);
                }
            } else if (_buttonIndex == 1) {
                doUploadNewSaveFile();
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

        /* synthetic */ RequestAsyncTask(UploadSaveFileUnit uploadSaveFileUnit, RequestAsyncTask requestAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(String... params) throws ParseException, IOException {
            HttpPost postMethod;
            HttpPost postMethod2;
            if (UploadSaveFileUnit.this.appDelegate == null) {
                return null;
            }
            HttpClient mHttp = new DefaultHttpClient();
            mHttp.getParams().setParameter("http.connection.timeout", 30000);
            mHttp.getParams().setParameter("http.socket.timeout", 30000);
            try {
                try {
                    if (UploadSaveFileUnit.this.loginType == 1) {
                        String urlString = "http://www.idtgame.com/idtgameserver/games/ckcd/fbuploadCkdata/?ut=1&rd=" + UploadSaveFileUnit.this.appDelegate.getNowDateString();
                        postMethod = new HttpPost(urlString);
                        postMethod.setHeader(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded");
                        postMethod.setHeader("fb_id", UploadSaveFileUnit.this.appDelegate.get_fb_account_id());
                        postMethod.setHeader("password", UploadSaveFileUnit.this.appDelegate.get_fb_account_password());
                        postMethod2 = postMethod;
                    } else {
                        String urlString2 = "http://www.idtgame.com/idtgameserver/games/ckcd/uploadCkdata/?ut=1&rd=" + UploadSaveFileUnit.this.appDelegate.getNowDateString();
                        postMethod = new HttpPost(urlString2);
                        postMethod.setHeader(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded");
                        postMethod.setHeader("account", UploadSaveFileUnit.this.appDelegate.get_idt_account_id());
                        postMethod.setHeader("password", UploadSaveFileUnit.this.appDelegate.get_idt_account_password());
                        postMethod2 = postMethod;
                    }
                    postMethod2.setHeader("os_type", "1");
                    String device_id = UploadSaveFileUnit.this.appDelegate.getDeviceID();
                    postMethod2.setHeader("device_id", device_id);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date nowDate = new Date();
                    String dateString = sdf.format(nowDate);
                    if (dateString == null) {
                        dateString = "";
                    }
                    postMethod2.setHeader("save_date", dateString);
                    List<NameValuePair> postParams = new ArrayList<>();
                    String save_file_String = UploadSaveFileUnit.this.appDelegate.changTimeSaveDictionaryToJSONwithDateString(dateString);
                    if (save_file_String != null) {
                        postParams.add(new BasicNameValuePair("body_save_file", save_file_String));
                    }
                    Log.e("save_file_String", "save_file:" + save_file_String);
                    String key_number_String = UploadSaveFileUnit.this.appDelegate.getTimeSaveDictionaryKeyNumber(UploadSaveFileUnit.this.appDelegate.timeSaveDictionary);
                    if (key_number_String != null) {
                        postMethod2.setHeader("key_number", key_number_String);
                    }
                    String device_token_String = UploadSaveFileUnit.this.appDelegate.get_device_token();
                    if (device_token_String != null && device_token_String.length() > 0) {
                        postMethod2.setHeader("device_token", device_token_String);
                    }
                    UrlEncodedFormEntity sendData = new UrlEncodedFormEntity(postParams, "UTF-8");
                    postMethod2.setEntity(sendData);
                    HttpResponse mResponse = mHttp.execute(postMethod2);
                    int resCode = mResponse.getStatusLine().getStatusCode();
                    mResponse.getEntity().getContentType().getValue();
                    HttpEntity httpEntity = mResponse.getEntity();
                    String jsonString = EntityUtils.toString(httpEntity);
                    if (resCode != 200) {
                        jsonString = null;
                    }
                    if (jsonString == null) {
                        return null;
                    }
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
                        JSONObject json = new JSONObject(jsonString);
                        return json;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } catch (IOException e2) {
                    return null;
                }
            } catch (IOException e3) {
                return null;
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
            UploadSaveFileUnit.this.doLoginResponse(successString);
            super.onPostExecute((RequestAsyncTask) _json);
        }
    }

    public void doLoginResponse(String _successString) {
        String messageString;
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.stopLoading();
        }
        if (_successString.equals("0")) {
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.UploadSucceeded)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
                this.appDelegate.displayFullAdView();
            }
        } else if (_successString.equals("105")) {
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                messageString = "端末の日付と時刻が正しくない\nようです。正しく設定して、もう\n一度アップロードしてください。";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                messageString = "手機的日期與時間是不正確的。\n請調整後再次上傳。";
            } else if (languageString.equals("zh-CN")) {
                messageString = "手机的日期与时间是不正确的。\n请调整後再次上传。";
            } else {
                messageString = "Please correct the system date and\ntime,and then upload again.";
            }
            if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
                new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(messageString).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
            }
        } else if (this.onlineGameViewController != null && this.onlineGameViewController.appMainActivity != null) {
            new AlertDialog.Builder(this.onlineGameViewController.appMainActivity).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.UploadFailed)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
        }
        clearRequestAsyncTask();
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.doNextActiveStatus((short) 100);
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

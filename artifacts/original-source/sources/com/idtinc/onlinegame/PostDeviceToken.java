package com.idtinc.onlinegame;

import android.os.AsyncTask;
import android.widget.Toast;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class PostDeviceToken {
    private AppDelegate appDelegate;
    private MyDraw myDraw;
    private final String POST_DEVICE_TOKEN_REQUEST_URL = "http://www.idtgame.com/idtgameserver/games/ckcd/uploadDeviceid";
    private final int POST_DEVICE_REQUEST_TIMEOUT_INTERVAL = 30000;
    private RequestAsyncTask requestAsyncTask = null;

    public PostDeviceToken(AppDelegate _appDelegate) {
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        clearRequestAsyncTask();
    }

    public void doUpload() {
        String device_token_String;
        clearRequestAsyncTask();
        if (this.appDelegate != null && this.appDelegate.checkInterNet() && (device_token_String = this.appDelegate.get_device_token()) != null && device_token_String.length() > 0) {
            String urlString = "http://www.idtgame.com/idtgameserver/games/ckcd/uploadDeviceid?type=1&rd=" + this.appDelegate.getNowDateString();
            this.requestAsyncTask = new RequestAsyncTask(this, null);
            this.requestAsyncTask.execute(urlString);
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

        /* synthetic */ RequestAsyncTask(PostDeviceToken postDeviceToken, RequestAsyncTask requestAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(String... params) throws IOException {
            if (PostDeviceToken.this.appDelegate == null) {
                return null;
            }
            JSONObject json = null;
            try {
                String urlString = "http://www.idtgame.com/idtgameserver/games/ckcd/uploadDeviceid?type=1&rd=" + PostDeviceToken.this.appDelegate.getNowDateString();
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestProperty(MIME.CONTENT_TYPE, "text/plain; charset=utf-8");
                String device_token_String = PostDeviceToken.this.appDelegate.get_device_token();
                if (device_token_String != null && device_token_String.length() > 0) {
                    conn.setRequestProperty("device_token", device_token_String);
                }
                String languageString = PostDeviceToken.this.appDelegate.getLocaleLanguage();
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
        public void onPostExecute(JSONObject _json) {
            super.onPostExecute((RequestAsyncTask) _json);
        }
    }

    public void makeNewText(String _newText) {
        Toast.makeText(this.appDelegate, "successString:" + _newText, 0).show();
    }

    public void onDestroy() {
        clearRequestAsyncTask();
        this.appDelegate = null;
    }
}

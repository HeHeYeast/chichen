package com.idtinc.request;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.idtinc.ckchickandduck.AppDelegate;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CampaignJsonRequest {
    private final String CP_JSON_REQUEST_URL = "http://idtinc.home.dyndns.org/Campaign/json.php";
    private boolean startF = false;
    private AppDelegate appDelegate = null;
    private RequestAsyncTask requestAsyncTask = null;

    public void init(AppDelegate _appDelegate) {
        this.appDelegate = _appDelegate;
        this.startF = true;
        clearRequestAsyncTask();
    }

    public void startRequest(boolean refreshF) {
        if (this.startF && this.appDelegate != null && this.appDelegate.defaultSharedPreferences != null) {
            SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
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
            editor.commit();
            clearRequestAsyncTask();
            if (this.appDelegate.checkInterNet()) {
                this.requestAsyncTask = new RequestAsyncTask(this, null);
                this.requestAsyncTask.execute("http://idtinc.home.dyndns.org/Campaign/json.php");
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

        /* synthetic */ RequestAsyncTask(CampaignJsonRequest campaignJsonRequest, RequestAsyncTask requestAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(String... params) throws IOException {
            JSONObject json = null;
            if (CampaignJsonRequest.this.startF && CampaignJsonRequest.this.appDelegate != null && CampaignJsonRequest.this.appDelegate.defaultSharedPreferences != null) {
                SharedPreferences.Editor editor = CampaignJsonRequest.this.appDelegate.defaultSharedPreferences.edit();
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
                editor.commit();
                json = null;
                try {
                    URL url = new URL("http://idtinc.home.dyndns.org/Campaign/json.php");
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
                } catch (MalformedURLException e2) {
                    e2.printStackTrace();
                } catch (IOException e3) {
                }
            }
            return json;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate((Object[]) progress);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(JSONObject _json) {
            if (CampaignJsonRequest.this.startF && CampaignJsonRequest.this.appDelegate != null && CampaignJsonRequest.this.appDelegate.defaultSharedPreferences != null) {
                SharedPreferences.Editor editor = CampaignJsonRequest.this.appDelegate.defaultSharedPreferences.edit();
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
                if (_json != null) {
                    try {
                        if (_json.getInt("campaign_char_0_26") == 1) {
                            editor.putBoolean("campaign_char_0_26", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_26:" + _json.getInt("campaign_char_0_26"));
                    } catch (JSONException e) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_27") == 1) {
                            editor.putBoolean("campaign_char_0_27", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_27:" + _json.getInt("campaign_char_0_27"));
                    } catch (JSONException e2) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_32") == 1) {
                            editor.putBoolean("campaign_char_0_32", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_32:" + _json.getInt("campaign_char_0_32"));
                    } catch (JSONException e3) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_48") == 1) {
                            editor.putBoolean("campaign_char_0_48", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_48:" + _json.getInt("campaign_char_0_48"));
                    } catch (JSONException e4) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_49") == 1) {
                            editor.putBoolean("campaign_char_0_49", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_49:" + _json.getInt("campaign_char_0_49"));
                    } catch (JSONException e5) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_60") == 1) {
                            editor.putBoolean("campaign_char_0_60", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_60:" + _json.getInt("campaign_char_0_60"));
                    } catch (JSONException e6) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_61") == 1) {
                            editor.putBoolean("campaign_char_0_61", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_61:" + _json.getInt("campaign_char_0_61"));
                    } catch (JSONException e7) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_62") == 1) {
                            editor.putBoolean("campaign_char_0_62", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_62:" + _json.getInt("campaign_char_0_62"));
                    } catch (JSONException e8) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_63") == 1) {
                            editor.putBoolean("campaign_char_0_63", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_63:" + _json.getInt("campaign_char_0_63"));
                    } catch (JSONException e9) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_64") == 1) {
                            editor.putBoolean("campaign_char_0_64", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_64:" + _json.getInt("campaign_char_0_64"));
                    } catch (JSONException e10) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_65") == 1) {
                            editor.putBoolean("campaign_char_0_65", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_65:" + _json.getInt("campaign_char_0_65"));
                    } catch (JSONException e11) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_66") == 1) {
                            editor.putBoolean("campaign_char_0_66", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_66:" + _json.getInt("campaign_char_0_66"));
                    } catch (JSONException e12) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_67") == 1) {
                            editor.putBoolean("campaign_char_0_67", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_67:" + _json.getInt("campaign_char_0_67"));
                    } catch (JSONException e13) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_78") == 1) {
                            editor.putBoolean("campaign_char_0_78", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_78:" + _json.getInt("campaign_char_0_78"));
                    } catch (JSONException e14) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_79") == 1) {
                            editor.putBoolean("campaign_char_0_79", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_79:" + _json.getInt("campaign_char_0_79"));
                    } catch (JSONException e15) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_80") == 1) {
                            editor.putBoolean("campaign_char_0_80", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_80:" + _json.getInt("campaign_char_0_80"));
                    } catch (JSONException e16) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_82") == 1) {
                            editor.putBoolean("campaign_char_0_82", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_82:" + _json.getInt("campaign_char_0_82"));
                    } catch (JSONException e17) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_83") == 1) {
                            editor.putBoolean("campaign_char_0_83", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_83:" + _json.getInt("campaign_char_0_83"));
                    } catch (JSONException e18) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_84") == 1) {
                            editor.putBoolean("campaign_char_0_84", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_84:" + _json.getInt("campaign_char_0_84"));
                    } catch (JSONException e19) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_85") == 1) {
                            editor.putBoolean("campaign_char_0_85", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_85:" + _json.getInt("campaign_char_0_85"));
                    } catch (JSONException e20) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_86") == 1) {
                            editor.putBoolean("campaign_char_0_86", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_86:" + _json.getInt("campaign_char_0_86"));
                    } catch (JSONException e21) {
                    }
                    try {
                        if (_json.getInt("campaign_char_0_87") == 1) {
                            editor.putBoolean("campaign_char_0_87", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_0_87:" + _json.getInt("campaign_char_0_87"));
                    } catch (JSONException e22) {
                    }
                    try {
                        if (_json.getInt("campaign_char_1_35") == 1) {
                            editor.putBoolean("campaign_char_1_35", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_1_35:" + _json.getInt("campaign_char_1_35"));
                    } catch (JSONException e23) {
                    }
                    try {
                        if (_json.getInt("campaign_char_1_47") == 1) {
                            editor.putBoolean("campaign_char_1_47", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_1_47:" + _json.getInt("campaign_char_1_47"));
                    } catch (JSONException e24) {
                    }
                    try {
                        if (_json.getInt("campaign_char_1_48") == 1) {
                            editor.putBoolean("campaign_char_1_48", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_1_48:" + _json.getInt("campaign_char_1_48"));
                    } catch (JSONException e25) {
                    }
                    try {
                        if (_json.getInt("campaign_char_1_49") == 1) {
                            editor.putBoolean("campaign_char_1_49", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_1_49:" + _json.getInt("campaign_char_1_49"));
                    } catch (JSONException e26) {
                    }
                    try {
                        if (_json.getInt("campaign_char_1_50") == 1) {
                            editor.putBoolean("campaign_char_1_50", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_1_50:" + _json.getInt("campaign_char_1_50"));
                    } catch (JSONException e27) {
                    }
                    try {
                        if (_json.getInt("campaign_char_1_51") == 1) {
                            editor.putBoolean("campaign_char_1_51", true);
                        }
                        Log.d("CampaignJsonRequest", "campaign_char_1_51:" + _json.getInt("campaign_char_1_51"));
                    } catch (JSONException e28) {
                    }
                }
                editor.commit();
                super.onPostExecute((RequestAsyncTask) _json);
            }
        }
    }

    public void onDestroy() {
        this.startF = false;
        clearRequestAsyncTask();
        this.appDelegate = null;
    }
}

package com.loopj.android.http;

import android.os.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class JsonHttpResponseHandler extends AsyncHttpResponseHandler {
    protected static final int SUCCESS_JSON_MESSAGE = 100;

    public void onSuccess(JSONObject jSONObject) {
    }

    public void onSuccess(JSONArray jSONArray) {
    }

    public void onSuccess(int i, JSONObject jSONObject) {
        onSuccess(jSONObject);
    }

    public void onSuccess(int i, JSONArray jSONArray) {
        onSuccess(jSONArray);
    }

    public void onFailure(Throwable th, JSONObject jSONObject) {
    }

    public void onFailure(Throwable th, JSONArray jSONArray) {
    }

    @Override // com.loopj.android.http.AsyncHttpResponseHandler
    protected void sendSuccessMessage(int i, String str) {
        if (i != 204) {
            try {
                sendMessage(obtainMessage(100, new Object[]{Integer.valueOf(i), parseResponse(str)}));
                return;
            } catch (JSONException e) {
                sendFailureMessage(e, str);
                return;
            }
        }
        sendMessage(obtainMessage(100, new Object[]{Integer.valueOf(i), new JSONObject()}));
    }

    @Override // com.loopj.android.http.AsyncHttpResponseHandler
    protected void handleMessage(Message message) {
        switch (message.what) {
            case 100:
                Object[] objArr = (Object[]) message.obj;
                handleSuccessJsonMessage(((Integer) objArr[0]).intValue(), objArr[1]);
                break;
            default:
                super.handleMessage(message);
                break;
        }
    }

    protected void handleSuccessJsonMessage(int i, Object obj) {
        if (obj instanceof JSONObject) {
            onSuccess(i, (JSONObject) obj);
        } else if (obj instanceof JSONArray) {
            onSuccess(i, (JSONArray) obj);
        } else {
            onFailure(new JSONException("Unexpected type " + obj.getClass().getName()), (JSONObject) null);
        }
    }

    protected Object parseResponse(String str) throws JSONException {
        Object objNextValue = null;
        String strTrim = str.trim();
        if (strTrim.startsWith("{") || strTrim.startsWith("[")) {
            objNextValue = new JSONTokener(strTrim).nextValue();
        }
        return objNextValue == null ? strTrim : objNextValue;
    }

    @Override // com.loopj.android.http.AsyncHttpResponseHandler
    protected void handleFailureMessage(Throwable th, String str) {
        try {
            if (str != null) {
                Object response = parseResponse(str);
                if (response instanceof JSONObject) {
                    onFailure(th, (JSONObject) response);
                } else if (response instanceof JSONArray) {
                    onFailure(th, (JSONArray) response);
                } else {
                    onFailure(th, str);
                }
            } else {
                onFailure(th, "");
            }
        } catch (JSONException e) {
            onFailure(th, str);
        }
    }
}

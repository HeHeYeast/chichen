package com.loopj.android.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AsyncHttpResponseHandler {
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int START_MESSAGE = 2;
    protected static final int SUCCESS_MESSAGE = 0;
    private Handler handler;

    public AsyncHttpResponseHandler() {
        if (Looper.myLooper() != null) {
            this.handler = new Handler() { // from class: com.loopj.android.http.AsyncHttpResponseHandler.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    AsyncHttpResponseHandler.this.handleMessage(message);
                }
            };
        }
    }

    public void onStart() {
    }

    public void onFinish() {
    }

    public void onSuccess(String str) {
    }

    public void onSuccess(int i, String str) {
        onSuccess(str);
    }

    public void onFailure(Throwable th) {
    }

    public void onFailure(Throwable th, String str) {
        onFailure(th);
    }

    protected void sendSuccessMessage(int i, String str) {
        sendMessage(obtainMessage(0, new Object[]{new Integer(i), str}));
    }

    protected void sendFailureMessage(Throwable th, String str) {
        sendMessage(obtainMessage(1, new Object[]{th, str}));
    }

    protected void sendFailureMessage(Throwable th, byte[] bArr) {
        sendMessage(obtainMessage(1, new Object[]{th, bArr}));
    }

    protected void sendStartMessage() {
        sendMessage(obtainMessage(2, null));
    }

    protected void sendFinishMessage() {
        sendMessage(obtainMessage(3, null));
    }

    protected void handleSuccessMessage(int i, String str) {
        onSuccess(i, str);
    }

    protected void handleFailureMessage(Throwable th, String str) {
        onFailure(th, str);
    }

    protected void handleMessage(Message message) {
        switch (message.what) {
            case 0:
                Object[] objArr = (Object[]) message.obj;
                handleSuccessMessage(((Integer) objArr[0]).intValue(), (String) objArr[1]);
                break;
            case 1:
                Object[] objArr2 = (Object[]) message.obj;
                handleFailureMessage((Throwable) objArr2[0], (String) objArr2[1]);
                break;
            case 2:
                onStart();
                break;
            case 3:
                onFinish();
                break;
        }
    }

    protected void sendMessage(Message message) {
        if (this.handler != null) {
            this.handler.sendMessage(message);
        } else {
            handleMessage(message);
        }
    }

    protected Message obtainMessage(int i, Object obj) {
        if (this.handler != null) {
            return this.handler.obtainMessage(i, obj);
        }
        Message message = new Message();
        message.what = i;
        message.obj = obj;
        return message;
    }

    void sendResponseMessage(HttpResponse httpResponse) throws ParseException, IOException {
        String string = null;
        StatusLine statusLine = httpResponse.getStatusLine();
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                string = EntityUtils.toString(new BufferedHttpEntity(entity), "UTF-8");
            }
        } catch (IOException e) {
            sendFailureMessage(e, (String) null);
        }
        if (statusLine.getStatusCode() >= 300) {
            sendFailureMessage(new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase()), string);
        } else {
            sendSuccessMessage(statusLine.getStatusCode(), string);
        }
    }
}

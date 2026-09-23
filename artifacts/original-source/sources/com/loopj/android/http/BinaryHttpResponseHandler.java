package com.loopj.android.http;

import android.os.Message;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MIME;
import org.apache.http.util.EntityUtils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class BinaryHttpResponseHandler extends AsyncHttpResponseHandler {
    private static String[] mAllowedContentTypes = {"image/jpeg", "image/png"};

    public BinaryHttpResponseHandler() {
    }

    public BinaryHttpResponseHandler(String[] strArr) {
        this();
        mAllowedContentTypes = strArr;
    }

    public void onSuccess(byte[] bArr) {
    }

    public void onSuccess(int i, byte[] bArr) {
        onSuccess(bArr);
    }

    public void onFailure(Throwable th, byte[] bArr) {
        onFailure(th);
    }

    protected void sendSuccessMessage(int i, byte[] bArr) {
        sendMessage(obtainMessage(0, new Object[]{Integer.valueOf(i), bArr}));
    }

    @Override // com.loopj.android.http.AsyncHttpResponseHandler
    protected void sendFailureMessage(Throwable th, byte[] bArr) {
        sendMessage(obtainMessage(1, new Object[]{th, bArr}));
    }

    protected void handleSuccessMessage(int i, byte[] bArr) {
        onSuccess(i, bArr);
    }

    protected void handleFailureMessage(Throwable th, byte[] bArr) {
        onFailure(th, bArr);
    }

    @Override // com.loopj.android.http.AsyncHttpResponseHandler
    protected void handleMessage(Message message) {
        switch (message.what) {
            case 0:
                Object[] objArr = (Object[]) message.obj;
                handleSuccessMessage(((Integer) objArr[0]).intValue(), (byte[]) objArr[1]);
                break;
            case 1:
                Object[] objArr2 = (Object[]) message.obj;
                handleFailureMessage((Throwable) objArr2[0], (byte[]) objArr2[1]);
                break;
            default:
                super.handleMessage(message);
                break;
        }
    }

    @Override // com.loopj.android.http.AsyncHttpResponseHandler
    void sendResponseMessage(HttpResponse httpResponse) throws IOException {
        byte[] byteArray = null;
        boolean z = false;
        StatusLine statusLine = httpResponse.getStatusLine();
        Header[] headers = httpResponse.getHeaders(MIME.CONTENT_TYPE);
        if (headers.length != 1) {
            sendFailureMessage(new HttpResponseException(statusLine.getStatusCode(), "None, or more than one, Content-Type Header found!"), (byte[]) null);
            return;
        }
        Header header = headers[0];
        for (String str : mAllowedContentTypes) {
            if (str.equals(header.getValue())) {
                z = true;
            }
        }
        if (!z) {
            sendFailureMessage(new HttpResponseException(statusLine.getStatusCode(), "Content-Type not allowed!"), (byte[]) null);
            return;
        }
        try {
            HttpEntity entity = httpResponse.getEntity();
            byteArray = EntityUtils.toByteArray(entity != null ? new BufferedHttpEntity(entity) : null);
        } catch (IOException e) {
            sendFailureMessage(e, (byte[]) null);
        }
        if (statusLine.getStatusCode() >= 300) {
            sendFailureMessage(new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase()), byteArray);
        } else {
            sendSuccessMessage(statusLine.getStatusCode(), byteArray);
        }
    }
}

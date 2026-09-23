package com.loopj.android.http;

import android.content.Context;
import android.os.Message;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MIME;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class SyncHttpClient extends AsyncHttpClient {
    private int responseCode;
    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() { // from class: com.loopj.android.http.SyncHttpClient.1
        @Override // com.loopj.android.http.AsyncHttpResponseHandler
        void sendResponseMessage(HttpResponse httpResponse) throws ParseException, IOException {
            SyncHttpClient.this.responseCode = httpResponse.getStatusLine().getStatusCode();
            super.sendResponseMessage(httpResponse);
        }

        @Override // com.loopj.android.http.AsyncHttpResponseHandler
        protected void sendMessage(Message message) {
            handleMessage(message);
        }

        @Override // com.loopj.android.http.AsyncHttpResponseHandler
        public void onSuccess(String str) {
            SyncHttpClient.this.result = str;
        }

        @Override // com.loopj.android.http.AsyncHttpResponseHandler
        public void onFailure(Throwable th, String str) {
            SyncHttpClient.this.result = SyncHttpClient.this.onRequestFailed(th, str);
        }
    };
    private String result;

    public abstract String onRequestFailed(Throwable th, String str);

    public int getResponseCode() {
        return this.responseCode;
    }

    @Override // com.loopj.android.http.AsyncHttpClient
    protected void sendRequest(DefaultHttpClient defaultHttpClient, HttpContext httpContext, HttpUriRequest httpUriRequest, String str, AsyncHttpResponseHandler asyncHttpResponseHandler, Context context) throws ParseException {
        if (str != null) {
            httpUriRequest.addHeader(MIME.CONTENT_TYPE, str);
        }
        new AsyncHttpRequest(defaultHttpClient, httpContext, httpUriRequest, asyncHttpResponseHandler).run();
    }

    public void delete(String str, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        delete(str, asyncHttpResponseHandler);
    }

    public String get(String str, RequestParams requestParams) {
        get(str, requestParams, this.responseHandler);
        return this.result;
    }

    public String get(String str) {
        get(str, (RequestParams) null, this.responseHandler);
        return this.result;
    }

    public String put(String str, RequestParams requestParams) {
        put(str, requestParams, this.responseHandler);
        return this.result;
    }

    public String put(String str) {
        put(str, null, this.responseHandler);
        return this.result;
    }

    public String post(String str, RequestParams requestParams) {
        post(str, requestParams, this.responseHandler);
        return this.result;
    }

    public String post(String str) {
        post(str, null, this.responseHandler);
        return this.result;
    }

    public String delete(String str, RequestParams requestParams) {
        delete(str, requestParams, this.responseHandler);
        return this.result;
    }

    public String delete(String str) {
        delete(str, (RequestParams) null, this.responseHandler);
        return this.result;
    }
}

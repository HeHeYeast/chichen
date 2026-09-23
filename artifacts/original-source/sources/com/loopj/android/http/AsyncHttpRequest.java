package com.loopj.android.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class AsyncHttpRequest implements Runnable {
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount;
    private boolean isBinaryRequest;
    private final HttpUriRequest request;
    private final AsyncHttpResponseHandler responseHandler;

    public AsyncHttpRequest(AbstractHttpClient abstractHttpClient, HttpContext httpContext, HttpUriRequest httpUriRequest, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        this.client = abstractHttpClient;
        this.context = httpContext;
        this.request = httpUriRequest;
        this.responseHandler = asyncHttpResponseHandler;
        if (asyncHttpResponseHandler instanceof BinaryHttpResponseHandler) {
            this.isBinaryRequest = true;
        }
    }

    @Override // java.lang.Runnable
    public void run() throws ParseException {
        try {
            if (this.responseHandler != null) {
                this.responseHandler.sendStartMessage();
            }
            makeRequestWithRetries();
            if (this.responseHandler != null) {
                this.responseHandler.sendFinishMessage();
            }
        } catch (IOException e) {
            if (this.responseHandler != null) {
                this.responseHandler.sendFinishMessage();
                if (this.isBinaryRequest) {
                    this.responseHandler.sendFailureMessage(e, (byte[]) null);
                } else {
                    this.responseHandler.sendFailureMessage(e, (String) null);
                }
            }
        }
    }

    private void makeRequest() throws ParseException, IOException {
        if (!Thread.currentThread().isInterrupted()) {
            HttpResponse httpResponseExecute = this.client.execute(this.request, this.context);
            if (!Thread.currentThread().isInterrupted() && this.responseHandler != null) {
                this.responseHandler.sendResponseMessage(httpResponseExecute);
            }
        }
    }

    private void makeRequestWithRetries() throws ParseException, ConnectException {
        boolean zRetryRequest = true;
        IOException e = null;
        HttpRequestRetryHandler httpRequestRetryHandler = this.client.getHttpRequestRetryHandler();
        while (zRetryRequest) {
            try {
                makeRequest();
                return;
            } catch (NullPointerException e2) {
                e = new IOException("NPE in HttpClient" + e2.getMessage());
                int i = this.executionCount + 1;
                this.executionCount = i;
                zRetryRequest = httpRequestRetryHandler.retryRequest(e, i, this.context);
            } catch (SocketException e3) {
                if (this.responseHandler != null) {
                    this.responseHandler.sendFailureMessage(e3, "can't resolve host");
                    return;
                }
                return;
            } catch (SocketTimeoutException e4) {
                if (this.responseHandler != null) {
                    this.responseHandler.sendFailureMessage(e4, "socket time out");
                    return;
                }
                return;
            } catch (UnknownHostException e5) {
                if (this.responseHandler != null) {
                    this.responseHandler.sendFailureMessage(e5, "can't resolve host");
                    return;
                }
                return;
            } catch (IOException e6) {
                e = e6;
                int i2 = this.executionCount + 1;
                this.executionCount = i2;
                zRetryRequest = httpRequestRetryHandler.retryRequest(e, i2, this.context);
            }
        }
        ConnectException connectException = new ConnectException();
        connectException.initCause(e);
        throw connectException;
    }
}

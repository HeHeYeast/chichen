package com.facebook.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class DialogError extends Throwable {
    private static final long serialVersionUID = 1;
    private int mErrorCode;
    private String mFailingUrl;

    @Deprecated
    public DialogError(String message, int errorCode, String failingUrl) {
        super(message);
        this.mErrorCode = errorCode;
        this.mFailingUrl = failingUrl;
    }

    @Deprecated
    public int getErrorCode() {
        return this.mErrorCode;
    }

    @Deprecated
    public String getFailingUrl() {
        return this.mFailingUrl;
    }
}

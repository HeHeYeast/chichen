package net.nend.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendException extends Exception {
    private static final long serialVersionUID = -1250523971139030161L;

    NendException() {
    }

    NendException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    NendException(String detailMessage) {
        super(detailMessage);
    }

    NendException(Throwable throwable) {
        super(throwable);
    }

    NendException(NendStatus status) {
        this(status.getMsg());
    }

    NendException(NendStatus status, String message) {
        this(status.getMsg(message));
    }
}

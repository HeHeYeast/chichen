package net.nend.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
interface Ad extends AdParameter {
    void cancelRequest();

    String getUid();

    boolean isRequestable();

    void removeListener();

    boolean requestAd();

    void setListener(AdListener adListener);
}

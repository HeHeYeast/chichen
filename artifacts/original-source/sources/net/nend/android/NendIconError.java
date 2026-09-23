package net.nend.android;

import net.nend.android.NendAdView;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NendIconError {
    public static final int ERROR_ICONVIEW = 1;
    public static final int ERROR_LOADER = 0;
    private int errorType;
    private NendAdView.NendError nendError;
    private NendAdIconLoader loader = null;
    private NendAdIconView iconView = null;

    public NendAdIconLoader getLoader() {
        return this.loader;
    }

    void setLoader(NendAdIconLoader loader) {
        this.loader = loader;
    }

    public NendAdIconView getIconView() {
        return this.iconView;
    }

    void setIconView(NendAdIconView iconView) {
        this.iconView = iconView;
    }

    public NendAdView.NendError getNendError() {
        return this.nendError;
    }

    public void setNendError(NendAdView.NendError nendError) {
        this.nendError = nendError;
    }

    public int getErrorType() {
        return this.errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }
}

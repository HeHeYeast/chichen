package net.nend.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
interface AdParameter {

    public enum ViewType {
        NONE,
        ADVIEW,
        WEBVIEW;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static ViewType[] valuesCustom() {
            ViewType[] viewTypeArrValuesCustom = values();
            int length = viewTypeArrValuesCustom.length;
            ViewType[] viewTypeArr = new ViewType[length];
            System.arraycopy(viewTypeArrValuesCustom, 0, viewTypeArr, 0, length);
            return viewTypeArr;
        }
    }

    String getClickUrl();

    int getHeight();

    String getIconId();

    String getImageUrl();

    int getReloadIntervalInSeconds();

    String getTitleText();

    ViewType getViewType();

    String getWebViewUrl();

    int getWidth();
}

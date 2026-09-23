package jp.adlantis.android.model;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface EasyObservable<T> {
    void addListener(OnChangeListener<T> onChangeListener);

    void removeListener(OnChangeListener<T> onChangeListener);
}

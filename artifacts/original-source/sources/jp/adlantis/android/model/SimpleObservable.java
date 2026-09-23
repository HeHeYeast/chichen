package jp.adlantis.android.model;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class SimpleObservable<T> implements EasyObservable<T> {
    private final ArrayList<OnChangeListener<T>> listeners = new ArrayList<>();

    @Override // jp.adlantis.android.model.EasyObservable
    public void addListener(OnChangeListener<T> onChangeListener) {
        synchronized (this.listeners) {
            this.listeners.add(onChangeListener);
        }
    }

    protected void notifyListeners(T t) {
        synchronized (this.listeners) {
            Iterator<OnChangeListener<T>> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().onChange(t);
            }
        }
    }

    @Override // jp.adlantis.android.model.EasyObservable
    public void removeListener(OnChangeListener<T> onChangeListener) {
        synchronized (this.listeners) {
            this.listeners.remove(onChangeListener);
        }
    }
}

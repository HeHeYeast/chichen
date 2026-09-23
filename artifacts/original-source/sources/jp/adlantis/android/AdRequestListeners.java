package jp.adlantis.android;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdRequestListeners implements AdRequestNotifier {
    protected ArrayList<AdRequestListener> listeners = new ArrayList<>();

    @Override // jp.adlantis.android.AdRequestNotifier
    public void addRequestListener(AdRequestListener adRequestListener) {
        synchronized (this.listeners) {
            this.listeners.add(adRequestListener);
        }
    }

    public void addRequestListeners(ArrayList<AdRequestListener> arrayList) {
        synchronized (this.listeners) {
            this.listeners.addAll(arrayList);
        }
    }

    public void addRequestListeners(AdRequestListeners adRequestListeners) {
        addRequestListeners(adRequestListeners.listeners);
    }

    public void notifyListenersAdReceived(AdRequestNotifier adRequestNotifier) {
        synchronized (this.listeners) {
            Iterator<AdRequestListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().onReceiveAd(adRequestNotifier);
            }
        }
    }

    public void notifyListenersAdTouched(AdRequestNotifier adRequestNotifier) {
        synchronized (this.listeners) {
            Iterator<AdRequestListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().onTouchAd(adRequestNotifier);
            }
        }
    }

    public void notifyListenersFailedToReceiveAd(AdRequestNotifier adRequestNotifier) {
        synchronized (this.listeners) {
            Iterator<AdRequestListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().onFailedToReceiveAd(adRequestNotifier);
            }
        }
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void removeRequestListener(AdRequestListener adRequestListener) {
        synchronized (this.listeners) {
            this.listeners.remove(adRequestListener);
        }
    }

    public void removeRequestListeners(ArrayList<AdRequestListener> arrayList) {
        synchronized (this.listeners) {
            this.listeners.removeAll(arrayList);
        }
    }

    public void removeRequestListeners(AdRequestListeners adRequestListeners) {
        removeRequestListeners(adRequestListeners.listeners);
    }
}

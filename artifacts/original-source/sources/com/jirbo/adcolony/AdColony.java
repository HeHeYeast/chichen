package com.jirbo.adcolony;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.ViewGroup;
import c.NetworkManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.jirbo.adcolony.ADCData;
import com.jirbo.adcolony.n;
import java.util.HashMap;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdColony {
    static boolean b;
    boolean a = false;

    public static void disable() {
        com.jirbo.adcolony.a.q = true;
    }

    public static void configure(Activity activity, String client_options, String app_id, String... zone_ids) {
        b = false;
        if (Build.VERSION.SDK_INT >= 11) {
            new a(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {
            new a(activity).execute(new Void[0]);
        }
        com.jirbo.adcolony.a.ag.clear();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() { // from class: com.jirbo.adcolony.AdColony.1
            @Override // java.lang.Runnable
            public void run() {
                com.jirbo.adcolony.a.y = false;
            }
        };
        if (!com.jirbo.adcolony.a.y || com.jirbo.adcolony.a.z) {
            if (!com.jirbo.adcolony.a.q) {
                if (app_id == null) {
                    com.jirbo.adcolony.a.a("Null App ID - disabling AdColony.");
                    return;
                }
                if (zone_ids == null) {
                    com.jirbo.adcolony.a.a("Null Zone IDs array - disabling AdColony.");
                    return;
                }
                if (zone_ids.length == 0) {
                    com.jirbo.adcolony.a.a("No Zone IDs provided - disabling AdColony.");
                    return;
                }
                com.jirbo.adcolony.a.b(activity);
                com.jirbo.adcolony.a.l.a(client_options, app_id, zone_ids);
                com.jirbo.adcolony.a.o = true;
                com.jirbo.adcolony.a.y = true;
                handler.postDelayed(runnable, 120000L);
            } else {
                return;
            }
        }
        if (com.jirbo.adcolony.a.K == null) {
            com.jirbo.adcolony.a.v = true;
        }
        com.jirbo.adcolony.a.ae.clear();
        com.jirbo.adcolony.a.af.clear();
        com.jirbo.adcolony.a.ah = new HashMap();
        for (String str : zone_ids) {
            com.jirbo.adcolony.a.ah.put(str, false);
        }
    }

    public static void setCustomID(String new_custom_id) {
        if (!new_custom_id.equals(com.jirbo.adcolony.a.l.a.x)) {
            com.jirbo.adcolony.a.l.a.x = new_custom_id;
            com.jirbo.adcolony.a.y = false;
            com.jirbo.adcolony.a.l.b.d = true;
            com.jirbo.adcolony.a.l.b.b = false;
            com.jirbo.adcolony.a.l.b.f217c = true;
        }
    }

    public static String getCustomID() {
        return com.jirbo.adcolony.a.l.a.x;
    }

    public static void setDeviceID(String new_device_id) {
        if (!new_device_id.equals(com.jirbo.adcolony.a.l.a.y)) {
            com.jirbo.adcolony.a.l.a.y = new_device_id;
            com.jirbo.adcolony.a.y = false;
            com.jirbo.adcolony.a.l.b.d = true;
            com.jirbo.adcolony.a.l.b.b = false;
            com.jirbo.adcolony.a.l.b.f217c = true;
        }
    }

    public static String getDeviceID() {
        return com.jirbo.adcolony.a.l.a.y;
    }

    public static boolean isTablet() {
        return g.i();
    }

    public static void resume(final Activity activity) {
        l.f226c.b((Object) "[ADC] AdColony resume called.");
        com.jirbo.adcolony.a.t = false;
        com.jirbo.adcolony.a.a(activity);
        com.jirbo.adcolony.a.s = false;
        if (activity == null) {
            l.d.b((Object) "Activity reference is null. Disabling AdColony.");
            disable();
        } else {
            new Thread(new Runnable() { // from class: com.jirbo.adcolony.AdColony.2
                @Override // java.lang.Runnable
                public void run() {
                    activity.runOnUiThread(new Runnable() { // from class: com.jirbo.adcolony.AdColony.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            for (int i = 0; i < com.jirbo.adcolony.a.ag.size(); i++) {
                                AdColonyNativeAdView adColonyNativeAdView = com.jirbo.adcolony.a.ag.get(i);
                                if (adColonyNativeAdView != null && com.jirbo.adcolony.a.b() == adColonyNativeAdView.d && !adColonyNativeAdView.u) {
                                    adColonyNativeAdView.A = false;
                                    adColonyNativeAdView.invalidate();
                                    if (adColonyNativeAdView.R != null) {
                                        adColonyNativeAdView.R.a = false;
                                        adColonyNativeAdView.R.invalidate();
                                    }
                                }
                            }
                        }
                    });
                }
            }).start();
            com.jirbo.adcolony.a.D = false;
        }
    }

    public static void pause() throws IllegalStateException {
        l.f226c.b((Object) "[ADC] AdColony pause called.");
        com.jirbo.adcolony.a.t = true;
        for (int i = 0; i < com.jirbo.adcolony.a.ag.size(); i++) {
            if (com.jirbo.adcolony.a.ag.get(i) != null) {
                AdColonyNativeAdView adColonyNativeAdView = com.jirbo.adcolony.a.ag.get(i);
                adColonyNativeAdView.A = true;
                if (adColonyNativeAdView.ad != null && !adColonyNativeAdView.u && adColonyNativeAdView.ad.isPlaying()) {
                    if (com.jirbo.adcolony.a.v) {
                        adColonyNativeAdView.R.setVisibility(0);
                    }
                    adColonyNativeAdView.c();
                }
            }
        }
    }

    public static void onBackPressed() {
        if (com.jirbo.adcolony.a.I != null) {
            if ((com.jirbo.adcolony.a.I instanceof ac) || (com.jirbo.adcolony.a.I instanceof ad)) {
                ((ViewGroup) com.jirbo.adcolony.a.I.getParent()).removeView(com.jirbo.adcolony.a.I);
                com.jirbo.adcolony.a.v = true;
                com.jirbo.adcolony.a.I.G.c(false);
                for (int i = 0; i < com.jirbo.adcolony.a.ad.size(); i++) {
                    com.jirbo.adcolony.a.ad.get(i).recycle();
                }
                com.jirbo.adcolony.a.ad.clear();
                com.jirbo.adcolony.a.I = null;
            }
        }
    }

    public static Activity activity() {
        return com.jirbo.adcolony.a.b();
    }

    public static boolean isZoneV4VC(String zone_id) {
        if (com.jirbo.adcolony.a.l == null || com.jirbo.adcolony.a.l.b == null || com.jirbo.adcolony.a.l.b.j == null || com.jirbo.adcolony.a.l.b.j.n == null) {
            return false;
        }
        return com.jirbo.adcolony.a.l.b.a(zone_id, false);
    }

    public static boolean isZoneNative(String zone_id) {
        if (com.jirbo.adcolony.a.l == null || com.jirbo.adcolony.a.l.b == null || com.jirbo.adcolony.a.l.b.j == null || com.jirbo.adcolony.a.l.b.j.n == null || com.jirbo.adcolony.a.l.b.j.n.a(zone_id) == null || com.jirbo.adcolony.a.l.b.j.n.a(zone_id).i == null || com.jirbo.adcolony.a.l.b.j.n.a(zone_id).i.a == null) {
            return false;
        }
        for (int i = 0; i < com.jirbo.adcolony.a.l.b.j.n.a(zone_id).i.a.size(); i++) {
            if (com.jirbo.adcolony.a.l.b.j.n.a(zone_id).i.a(i).w.a) {
                return true;
            }
        }
        return false;
    }

    public static int getRemainingV4VCForZone(String zone_id) {
        if (com.jirbo.adcolony.a.l == null || com.jirbo.adcolony.a.l.h == null || com.jirbo.adcolony.a.l.b == null || com.jirbo.adcolony.a.l.b.j == null || com.jirbo.adcolony.a.l.b.j.n == null) {
            return l.f226c.c("getRemainingV4VCForZone called before AdColony has finished configuring.");
        }
        n.ab abVarA = com.jirbo.adcolony.a.l.b.j.n.a(zone_id);
        return abVarA == null ? l.f226c.c("getRemainingV4VCForZone called before AdColony has finished configuring, or the zone passed in is invalid.") : !abVarA.j.a ? l.f226c.c("getRemainingV4VCForZone called with non-V4VC zone.") : abVarA.j.b.a - com.jirbo.adcolony.a.l.h.b(zone_id);
    }

    public static void addV4VCListener(AdColonyV4VCListener listener) {
        if (!com.jirbo.adcolony.a.ae.contains(listener)) {
            com.jirbo.adcolony.a.ae.add(listener);
        }
    }

    public static void removeV4VCListener(AdColonyV4VCListener listener) {
        com.jirbo.adcolony.a.ae.remove(listener);
    }

    public static void addAdAvailabilityListener(AdColonyAdAvailabilityListener listener) {
        if (!com.jirbo.adcolony.a.af.contains(listener)) {
            com.jirbo.adcolony.a.af.add(listener);
        }
    }

    public static void removeAdAvailabilityListener(AdColonyAdAvailabilityListener listener) {
        com.jirbo.adcolony.a.af.remove(listener);
    }

    public static void notifyIAPComplete(String product_id, String trans_id) {
        notifyIAPComplete(product_id, trans_id, null, 0.0d);
    }

    public static void notifyIAPComplete(String product_id, String trans_id, String currency_code, double price) {
        l.f226c.b((Object) "notifyIAPComplete() called.");
        ADCData.g gVar = new ADCData.g();
        gVar.b("product_id", product_id);
        if (price != 0.0d) {
            gVar.b("price", price);
        }
        gVar.b("trans_id", trans_id);
        gVar.b("quantity", 1);
        if (currency_code != null) {
            gVar.b("price_currency_code", currency_code);
        }
        if (com.jirbo.adcolony.a.F) {
            com.jirbo.adcolony.a.l.d.a("in_app_purchase", gVar);
        } else {
            com.jirbo.adcolony.a.Z.a(gVar);
        }
    }

    public static void cancelVideo() {
        if (com.jirbo.adcolony.a.K != null) {
            com.jirbo.adcolony.a.K.finish();
            com.jirbo.adcolony.a.aa = true;
            com.jirbo.adcolony.a.M.b(null);
        }
    }

    public static String statusForZone(String zone_id) {
        if (com.jirbo.adcolony.a.l == null || com.jirbo.adcolony.a.l.b == null || com.jirbo.adcolony.a.l.b.j == null || com.jirbo.adcolony.a.l.b.j.n == null || com.jirbo.adcolony.a.q) {
            return NetworkManager.TYPE_UNKNOWN;
        }
        n.ab abVarA = com.jirbo.adcolony.a.l.b.j.n.a(zone_id);
        return abVarA != null ? !abVarA.e ? "off" : (abVarA.f && com.jirbo.adcolony.a.l.b.c(zone_id, true)) ? "active" : "loading" : !com.jirbo.adcolony.a.p ? NetworkManager.TYPE_UNKNOWN : "invalid";
    }

    public static void get_images(String zone_id) {
        com.jirbo.adcolony.a.l.a.b(zone_id);
    }

    public static void disableDECOverride() {
        com.jirbo.adcolony.a.e = null;
    }

    public static void forceMobileCache() {
        if (!com.jirbo.adcolony.a.E) {
            com.jirbo.adcolony.a.E = true;
            com.jirbo.adcolony.a.y = false;
            com.jirbo.adcolony.a.l.b.d = true;
            com.jirbo.adcolony.a.l.b.b = false;
            com.jirbo.adcolony.a.l.b.f217c = true;
        }
    }

    private static class a extends AsyncTask<Void, Void, Void> {
        Activity a;
        String b = "";

        /* renamed from: c, reason: collision with root package name */
        boolean f204c;

        a(Activity activity) {
            this.a = activity;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(Void... voidArr) {
            try {
                AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.a);
                this.b = advertisingIdInfo.getId();
                this.f204c = advertisingIdInfo.isLimitAdTrackingEnabled();
            } catch (Exception e) {
                l.d.b((Object) "Advertising Id not available! Collecting Android Id instead of Advertising Id.");
                e.printStackTrace();
            } catch (NoClassDefFoundError e2) {
                l.d.b((Object) "Google Play Services SDK not installed! Collecting Android Id instead of Advertising Id.");
            } catch (NoSuchMethodError e3) {
                l.d.b((Object) "Google Play Services SDK is out of date! Collecting Android Id instead of Advertising Id.");
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(Void r2) {
            g.a = this.b;
            g.b = this.f204c;
            AdColony.b = true;
        }
    }
}

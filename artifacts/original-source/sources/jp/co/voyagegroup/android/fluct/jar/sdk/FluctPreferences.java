package jp.co.voyagegroup.android.fluct.jar.sdk;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.IOException;
import java.security.cert.CertificateException;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctPreferences {
    private static final String TAG = "FluctPreferences";
    private static FluctPreferences mInstance = new FluctPreferences();
    private static String mAdid = null;
    private static String mNotAdTrackingParam = null;
    private static Boolean mInitialized = false;

    private FluctPreferences() {
        Log.d(TAG, TAG);
    }

    public static FluctPreferences getInstance() {
        return mInstance;
    }

    public void makeFluctPreferences(Context context) throws IllegalStateException, PackageManager.NameNotFoundException, CertificateException {
        Log.d(TAG, "makeFluctPreferences : mInitialized? " + mInitialized);
        if (!mInitialized.booleanValue()) {
            AdvertisingIdClient.Info adInfo = null;
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            switch (resultCode) {
                case 0:
                    try {
                        adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                    } catch (GooglePlayServicesNotAvailableException e) {
                        mAdid = "";
                        mNotAdTrackingParam = "";
                    } catch (GooglePlayServicesRepairableException e2) {
                        mAdid = "";
                        mNotAdTrackingParam = "";
                    } catch (IOException e3) {
                        mAdid = "";
                        mNotAdTrackingParam = "";
                    }
                    if (adInfo != null) {
                        mNotAdTrackingParam = adInfo.isLimitAdTrackingEnabled() ? "1" : "0";
                        if (mNotAdTrackingParam.equals("0")) {
                            mAdid = adInfo.getId();
                            break;
                        } else {
                            mAdid = "";
                            break;
                        }
                    } else {
                        mAdid = "";
                        mNotAdTrackingParam = "";
                        break;
                    }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case HapticContentSDK.f20b0444044404440444 /* 1500 */:
                    Log.v(TAG, "makeFluctPreferences : Google Play Services connection error occerred. resultCode:" + resultCode);
                    mAdid = "";
                    mNotAdTrackingParam = "";
                    break;
                default:
                    Log.v(TAG, "makeFluctPreferences : Google Play Services unknown error occerred. resultCode:" + resultCode);
                    mAdid = "";
                    mNotAdTrackingParam = "";
                    break;
            }
            Log.v(TAG, "makeFluctPreferences : ADID is " + mAdid + " mNotAdTrackingParam is " + mNotAdTrackingParam);
            mInitialized = true;
        }
    }

    public void dispose() {
        Log.d(TAG, "dispose : ");
        mInitialized = false;
    }

    public String getAdid() {
        Log.d(TAG, "getAdid : mAdid is " + mAdid);
        return mAdid;
    }

    public String getNotAdTrackingParam() {
        Log.d(TAG, "getNotAdTrackingParam : mNotAdTrackingParam is " + mNotAdTrackingParam);
        return mNotAdTrackingParam;
    }
}

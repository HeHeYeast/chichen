package jp.co.voyagegroup.android.fluct.jar.sdk;

import android.content.Context;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import jp.co.voyagegroup.android.fluct.jar.db.FluctDbAccess;
import jp.co.voyagegroup.android.fluct.jar.db.FluctInterstitialTable;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.FluctXMLParser;
import jp.co.voyagegroup.android.fluct.jar.util.Log;
import org.w3c.dom.Document;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctConfig {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String TAG = "FluctConfig";
    private static FluctConfig sInstance;
    private String mErrorMessage = null;
    private Map<String, ReadWriteLock> mMediaLocks;
    private Map<String, FluctSetting> mSettingsMap;

    static {
        $assertionsDisabled = !FluctConfig.class.desiredAssertionStatus();
    }

    private FluctConfig() {
        Log.d(TAG, "FluctConfig : ");
        this.mSettingsMap = new Hashtable();
        this.mMediaLocks = new HashMap();
    }

    public static FluctConfig getInstance() {
        Log.d(TAG, "getInstance : ");
        if (sInstance == null) {
            sInstance = new FluctConfig();
        }
        return sInstance;
    }

    private ReadWriteLock getLock(String mediaId) {
        Log.d(TAG, "getLock : ");
        if (!this.mMediaLocks.containsKey(mediaId)) {
            ReadWriteLock lock = new ReentrantReadWriteLock();
            this.mMediaLocks.put(mediaId, lock);
            return lock;
        }
        return this.mMediaLocks.get(mediaId);
    }

    public FluctSetting getNetConfig(Context context, String mediaId) {
        Log.d(TAG, "getNetConfig : ");
        String requestConfigUrl = FluctUtils.getConfigURL(mediaId);
        Log.v(TAG, "getNetConfig : requestConfigUrl is " + requestConfigUrl);
        Document configDocument = FluctXMLParser.executeUrl(context, requestConfigUrl);
        FluctSetting setting = FluctXMLParser.parserConfig(context, configDocument, mediaId);
        if (setting != null) {
            Log.v(TAG, "getNetConfig : setting is " + setting.toString());
            if (setting.getErrorMessages() == null) {
                setting.setMediaId(mediaId);
                this.mErrorMessage = null;
                return setting;
            }
            this.mErrorMessage = setting.getErrorMessages();
            Log.e(TAG, "getNetConfig : ErrorMessages is " + setting.getErrorMessages());
            return null;
        }
        return setting;
    }

    public FluctSetting getConfigFromMap(String mediaId) {
        Log.d(TAG, "getConfigFromMap : ");
        FluctSetting setting = this.mSettingsMap.get(mediaId);
        if ($assertionsDisabled || setting != null) {
            return setting;
        }
        throw new AssertionError();
    }

    public FluctSetting getFromDB(Context context, String mediaId) {
        Log.d(TAG, "getFromDB : ");
        FluctSetting setting = FluctDbAccess.getConfig(context, mediaId);
        if (setting != null) {
            this.mSettingsMap.put(mediaId, setting);
        }
        return setting;
    }

    public FluctSetting getFromDBWithResultLock(Context context, String mediaId) {
        Log.d(TAG, "getFromDBWithResultLock : ");
        ReadWriteLock lock = getLock(mediaId);
        lock.readLock().lock();
        FluctSetting setting = FluctDbAccess.getConfig(context, mediaId);
        if (setting != null) {
            this.mSettingsMap.put(mediaId, setting);
        }
        lock.readLock().unlock();
        return setting;
    }

    public FluctSetting getFromNet(Context context, String mediaId) {
        Log.d(TAG, "getFromNet : ");
        FluctSetting setting = getFluctSetting(context, mediaId);
        return setting;
    }

    public String getFromNetErrorMsg(Context context, String mediaId) {
        Log.d(TAG, "getFromNetErrorMsg : ");
        getFluctSetting(context, mediaId);
        return this.mErrorMessage;
    }

    private FluctSetting getFluctSetting(Context context, String mediaId) {
        FluctSetting setting = null;
        ReadWriteLock lock = getLock(mediaId);
        if (lock.writeLock().tryLock()) {
            setting = getNetConfig(context, mediaId);
            FluctInterstitialTable interstitial = null;
            if (setting != null) {
                FluctDbAccess.saveConfig(context, setting);
                interstitial = setting.getFluctInterstitial();
            }
            if (interstitial != null) {
                FluctDbAccess.setInterstitial(context, interstitial);
            } else if (FluctDbAccess.checkInterstitialData(context, mediaId)) {
                FluctDbAccess.deleteInterstitialData(context, mediaId);
            }
            if (setting != null) {
                this.mSettingsMap.put(mediaId, setting);
            }
            lock.writeLock().unlock();
        }
        return setting;
    }
}

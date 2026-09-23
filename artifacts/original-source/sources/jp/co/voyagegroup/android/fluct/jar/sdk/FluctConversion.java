package jp.co.voyagegroup.android.fluct.jar.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import java.util.List;
import jp.co.voyagegroup.android.fluct.jar.db.FluctDbAccess;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctConversionEntity;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.Log;
import jp.co.voyagegroup.android.fluct.jar.web.FluctHttpAccess;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctConversion {
    private static final String TAG = "FluctConversion";
    private static FluctConversionThread sSetConversionThread = null;

    public static void setConversion(Context context) {
        Log.d(TAG, "setConversion : ");
        if (sSetConversionThread == null) {
            sSetConversionThread = new FluctConversionThread(context);
            sSetConversionThread.start();
        }
    }

    private static class FluctConversionThread extends Thread {
        private Context mContext;

        public FluctConversionThread(Context context) {
            Log.d(FluctConversion.TAG, "FluctConversionThread : ");
            this.mContext = context;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws PackageManager.NameNotFoundException {
            Log.d(FluctConversion.TAG, "FluctConversionThread : run ");
            String defaultMediaId = FluctUtils.getDefaultMediaId(this.mContext);
            try {
                int conversionFlag = FluctDbAccess.getConversionFlag(this.mContext);
                Log.v(FluctConversion.TAG, "FluctConversionThread : status is " + conversionFlag);
                if (conversionFlag == 0) {
                    FluctSetting netConfig = FluctConfig.getInstance().getNetConfig(this.mContext, defaultMediaId);
                    if (netConfig == null) {
                        this.mContext = null;
                        FluctConversion.sSetConversionThread = null;
                    } else {
                        checkConversionEntity(this.mContext, netConfig.getFluctConversion());
                        this.mContext = null;
                        FluctConversion.sSetConversionThread = null;
                    }
                }
            } catch (Exception e) {
                Log.e(FluctConversion.TAG, "FluctConversionThread : Exception is " + e.getLocalizedMessage());
            } finally {
                this.mContext = null;
                FluctConversion.sSetConversionThread = null;
            }
        }

        private void checkConversionEntity(Context context, FluctConversionEntity fluctConversion) {
            Log.d(FluctConversion.TAG, "checkConversionEntity : ");
            if (fluctConversion == null) {
                Log.e(FluctConversion.TAG, "checkConversionEntity : fluctConversion is null");
                return;
            }
            String browserOpenUrl = fluctConversion.getBrowserOpenUrl();
            List<String> convUrls = fluctConversion.getConvUrl();
            if (convUrls == null && browserOpenUrl == null) {
                Log.e(FluctConversion.TAG, "checkConversionEntity : urls is null and browserOpenUrl is null");
            } else {
                executeConversion(context, convUrls, browserOpenUrl);
            }
        }

        private void executeConversion(Context context, List<String> convUrls, String browserOpenUrl) {
            Log.d(FluctConversion.TAG, "executeConversion : ");
            boolean result = false;
            if (convUrls.size() == 0 && browserOpenUrl == null) {
                result = true;
            } else if (convUrls.size() > 0) {
                Log.v(FluctConversion.TAG, "executeConversion : execute conversion urls");
                FluctHttpAccess httpAccess = new FluctHttpAccess();
                for (int loop = 0; loop < convUrls.size(); loop++) {
                    String convUrl = convUrls.get(loop);
                    convUrls.set(loop, FluctUtils.replaceParams(context, convUrl));
                }
                result = httpAccess.executeUrls(convUrls);
            } else if (browserOpenUrl != null && FluctUtils.isNetWorkAvailable(context)) {
                Uri browserOpenUri = Uri.parse(FluctUtils.replaceParams(context, browserOpenUrl));
                Intent intent = new Intent("android.intent.action.VIEW", browserOpenUri);
                boolean isActivity = context instanceof Activity;
                if (!isActivity) {
                    intent.setFlags(268435456);
                }
                Log.v(FluctConversion.TAG, "executeConversion : startActivity call");
                context.startActivity(intent);
                result = true;
            }
            if (result) {
                FluctDbAccess.setConversionFlag(context, 1);
                Log.v(FluctConversion.TAG, "executeConversion : set conversion success");
            } else {
                Log.e(FluctConversion.TAG, "executeConversion : set conversion failed");
            }
        }
    }
}

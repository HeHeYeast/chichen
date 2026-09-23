package jp.co.voyagegroup.android.fluct.jar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctConversion;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctViewHelper;
import jp.co.voyagegroup.android.fluct.jar.task.FluctAdapterThread;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctView extends RelativeLayout {
    private static final String TAG = "FluctView";
    private FluctAdapterThread mAdapterThread;
    private FluctViewHelper mFluctViewHelper;
    private Handler mHandler;
    private String mMediaId;

    public FluctView(Context context, AttributeSet attributeSet) throws PackageManager.NameNotFoundException {
        super(context, attributeSet);
        this.mHandler = new Handler();
        Log.d(TAG, "FluctView : AttributeSet");
        Log.x("AD", "Create AdView:");
        createView(attributeSet.getAttributeValue(null, FluctConstants.META_DATA_MEDIA_ID));
    }

    public FluctView(Context context, String mediaId) throws PackageManager.NameNotFoundException {
        super(context);
        this.mHandler = new Handler();
        Log.d(TAG, "FluctView : MediaID is " + mediaId);
        Log.x("AD", "Create AdView : Media");
        createView(mediaId);
    }

    public FluctView(Context context) {
        this(context, (String) null);
        Log.d(TAG, "FluctView : none MediaID ");
        Log.x("AD", "Create AdView : none media");
    }

    public void destroy() {
        Log.d(TAG, "destroy : ");
        Log.x("AD", "Destroy AdView : ");
        deleteView(0L);
    }

    public static void prepareConfig(Context context, String mediaId) throws PackageManager.NameNotFoundException {
        Log.d(TAG, "prepareConfig : ");
        Log.x("AD", "AdView prepare : ");
        if (mediaId == null || "".equals(mediaId)) {
            mediaId = FluctUtils.getDefaultMediaId(context);
        }
        Log.v(TAG, "prepareConfig : mediaId is " + mediaId);
        new FluctAdapterThread(context, mediaId, 1, null).start();
    }

    public static void prepareConfig(Context context) throws PackageManager.NameNotFoundException {
        Log.d(TAG, "prepareConfig : ");
        prepareConfig(context, null);
    }

    public static void setConversion(Context context) {
        Log.d(TAG, "setConversion : ");
        Log.x("AD", "AdView Conversion : ");
        FluctConversion.setConversion(context);
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        Log.d(TAG, "setVisibility : visibility is " + visibility);
        Log.x("AD", "AdView set visibility : " + visibility);
        int fluctViewStatus = getVisibility();
        if (fluctViewStatus != visibility) {
            makeSelfVisibility(visibility);
            super.setVisibility(visibility);
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        Log.d(TAG, "onWindowVisibilityChanged : visibility is " + visibility);
        makeVisibility(visibility, 900L);
        super.onWindowVisibilityChanged(visibility);
    }

    private void makeVisibility(int visibility, long sleepTime) {
        Log.d(TAG, "makeVisibility : visibility is " + visibility);
        if (visibility == 4 || visibility == 8) {
            deleteView(sleepTime);
            return;
        }
        if (visibility == 0 && getVisibility() == 0) {
            if (this.mFluctViewHelper == null) {
                Log.x("AD", "Start");
                this.mFluctViewHelper = new FluctViewHelper(getContext().getApplicationContext(), this, this.mMediaId);
            }
            if (this.mAdapterThread == null) {
                this.mAdapterThread = new FluctAdapterThread(getContext().getApplicationContext(), this.mMediaId, 2, this.mFluctViewHelper);
                this.mAdapterThread.start();
            }
        }
    }

    private void makeSelfVisibility(int visibility) {
        Log.d(TAG, "makeSelfVisibility : visibility is " + visibility);
        if (visibility == 4 || visibility == 8) {
            if (this.mAdapterThread != null) {
                this.mAdapterThread.halt();
                this.mAdapterThread = null;
            }
            if (this.mFluctViewHelper != null) {
                this.mFluctViewHelper.stopAnimation();
                this.mFluctViewHelper.stopTimer();
                return;
            }
            return;
        }
        if (visibility == 0) {
            if (this.mFluctViewHelper == null) {
                this.mFluctViewHelper = new FluctViewHelper(getContext().getApplicationContext(), this, this.mMediaId);
            }
            if (this.mAdapterThread == null) {
                this.mAdapterThread = new FluctAdapterThread(getContext().getApplicationContext(), this.mMediaId, 2, this.mFluctViewHelper);
                this.mAdapterThread.start();
            }
        }
    }

    private void deleteView(final long sleepTime) {
        Log.d(TAG, "deleteView : SleepTime is " + sleepTime);
        new Thread(new Runnable() { // from class: jp.co.voyagegroup.android.fluct.jar.FluctView.1
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                Log.d(FluctView.TAG, "deleteView : Thread run() ");
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Log.e(FluctView.TAG, "deleteView : Sleep Exception");
                }
                int visibility = FluctView.this.getWindowVisibility();
                Log.v(FluctView.TAG, "deleteView : Window visibility is " + visibility);
                if (visibility == 4 || visibility == 8) {
                    FluctView.this.mHandler.post(new Runnable() { // from class: jp.co.voyagegroup.android.fluct.jar.FluctView.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (FluctView.this.mAdapterThread != null) {
                                FluctView.this.mAdapterThread.halt();
                                FluctView.this.mAdapterThread = null;
                            }
                            if (FluctView.this.mFluctViewHelper != null) {
                                Log.x("AD", "Stop");
                                FluctView.this.mFluctViewHelper.stopTimerAndDelWebView();
                                FluctView.this.mFluctViewHelper = null;
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @SuppressLint({"NewApi"})
    private void createView(String mediaId) throws PackageManager.NameNotFoundException {
        Log.d(TAG, "createView :");
        Log.x("AD", "AD version : 3.2.0");
        if (mediaId == null || "".equals(mediaId)) {
            mediaId = FluctUtils.getDefaultMediaId(getContext().getApplicationContext());
        }
        this.mMediaId = mediaId;
        Log.v(TAG, "createView : mediaId is " + this.mMediaId);
        int sdkVerion = Build.VERSION.SDK_INT;
        if (sdkVerion >= 11) {
            setLayerType(1, null);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        Log.d(TAG, "requestChildFocus : ");
        if (!(focused instanceof WebView)) {
            super.requestChildFocus(child, focused);
        }
    }
}

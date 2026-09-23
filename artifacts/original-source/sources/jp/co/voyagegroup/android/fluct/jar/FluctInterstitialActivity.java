package jp.co.voyagegroup.android.fluct.jar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.db.FluctDbAccess;
import jp.co.voyagegroup.android.fluct.jar.db.FluctInterstitialTable;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctConfig;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctInterstitialManager;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctWebView;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctInterstitialActivity extends Activity {
    private static final String TAG = "FluctInterstitialActivity";
    private int mAdHeight;
    private int mAdWidth;
    private AnimationDrawable mAnimation;
    private ArrayList<Bitmap> mBitmapArray;
    View.OnClickListener mCloseButton = new View.OnClickListener() { // from class: jp.co.voyagegroup.android.fluct.jar.FluctInterstitialActivity.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FluctInterstitialManager.callback(2);
            FluctInterstitialActivity.this.stopAnimation();
            FluctInterstitialActivity.this.cleanupImage();
            FluctInterstitialActivity.this.finish();
        }
    };
    private int mCloseButtonSize;
    private int mFrameColor;
    private int mFrameHeight;
    private int mFrameWidth;
    private FrameLayout mFullScreenLayout;
    private ArrayList<ImageView> mImageArray;
    private String mMediaId;
    private Point mScreen;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate : ");
        Intent intent = getIntent();
        this.mMediaId = intent.getStringExtra("media_id");
        this.mFrameColor = intent.getIntExtra("frame_color", FluctConstants.FRAME_BASE_COLOR);
        this.mFullScreenLayout = new FrameLayout(getApplicationContext());
        this.mFullScreenLayout.setBackgroundColor(FluctConstants.SCREEN_BASE_COLOR);
        this.mFullScreenLayout.setForegroundGravity(17);
        setContentView(this.mFullScreenLayout);
        this.mBitmapArray = new ArrayList<>();
        this.mImageArray = new ArrayList<>();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged : hasFocus " + hasFocus);
        if (hasFocus) {
            Context context = getApplicationContext();
            this.mScreen = new Point();
            this.mScreen.x = this.mFullScreenLayout.getWidth();
            this.mScreen.y = this.mFullScreenLayout.getHeight();
            FluctInterstitialTable data = FluctDbAccess.getInterstitial(getApplicationContext(), this.mMediaId);
            float density = getResources().getDisplayMetrics().density;
            this.mAdWidth = (int) (data.getWidth() * density);
            this.mAdHeight = (int) (data.getHeight() * density);
            this.mCloseButtonSize = (int) (36.0f * density);
            if (this.mAdWidth + (this.mCloseButtonSize * 2) > this.mScreen.x && this.mAdHeight + (this.mCloseButtonSize * 2) > this.mScreen.y) {
                FluctInterstitialManager.callback(7);
                stopAnimation();
                cleanupImage();
                finish();
                return;
            }
            FluctWebView fluctView = setFluctWebView(context, data);
            FrameLayout frameView = setFrameView(context, density);
            ImageView loadingView = setLoadingImage(context, density);
            ImageView closeView = setCloseButton(context, density);
            frameView.addView(loadingView);
            this.mFullScreenLayout.addView(frameView);
            this.mFullScreenLayout.addView(closeView);
            this.mFullScreenLayout.addView(fluctView);
            this.mAnimation.start();
            FluctInterstitialManager.callback(0);
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged : orientation is " + newConfig.orientation);
        FluctInterstitialManager.callback(2);
        stopAnimation();
        cleanupImage();
        finish();
    }

    public void callback(int status) {
        Log.d(TAG, "callback : status " + status);
        FluctInterstitialManager.callback(status);
        stopAnimation();
        cleanupImage();
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupImage() {
        Log.d(TAG, "cleanupImage : ");
        if (this.mBitmapArray != null && this.mBitmapArray.size() > 0) {
            for (int loop = 0; this.mBitmapArray.size() > loop; loop++) {
                Bitmap bitmap = this.mBitmapArray.get(loop);
                Log.v(TAG, "cleanupImage : bitmap is " + bitmap);
                bitmap.recycle();
            }
            this.mBitmapArray.clear();
            this.mBitmapArray = null;
        }
        if (this.mImageArray != null && this.mImageArray.size() > 0) {
            for (int loop2 = 0; this.mImageArray.size() > loop2; loop2++) {
                ImageView image = this.mImageArray.get(loop2);
                Log.v(TAG, "cleanupImage : image is " + image);
                image.setImageDrawable(null);
            }
            this.mImageArray.clear();
            this.mImageArray = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAnimation() {
        Log.d(TAG, "stopAnimation : ");
        if (this.mAnimation != null && this.mAnimation.isRunning()) {
            this.mAnimation.stop();
        }
    }

    private FluctWebView setFluctWebView(Context context, FluctInterstitialTable data) {
        Log.d(TAG, "setFluctWebView : ");
        FluctSetting setting = FluctConfig.getInstance().getFromDB(context, this.mMediaId);
        FluctWebView result = new FluctWebView(context, setting, this);
        result.setAdHtml(data.getAdHtml());
        result.setVisibility(0);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(this.mAdWidth, this.mAdHeight);
        params.gravity = 17;
        result.setLayoutParams(params);
        return result;
    }

    private FrameLayout setFrameView(Context context, float density) {
        Log.d(TAG, "setFrameView : ");
        FrameLayout result = new FrameLayout(context);
        this.mFrameWidth = (int) (this.mAdWidth + (40.0f * density));
        this.mFrameHeight = (int) (this.mAdHeight + (40.0f * density));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(this.mFrameWidth, this.mFrameHeight);
        params.gravity = 17;
        result.setLayoutParams(params);
        result.setBackgroundColor(this.mFrameColor);
        return result;
    }

    private ImageView setLoadingImage(Context context, float density) {
        Log.d(TAG, "setLoadingImage : ");
        ImageView result = new ImageView(context);
        this.mAnimation = new AnimationDrawable();
        ArrayList<byte[]> loadingList = FluctDbAccess.getLoadingImage(context);
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (int loop = 0; loop < loadingList.size(); loop++) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(loadingList.get(loop), 0, loadingList.get(loop).length, options);
            this.mAnimation.addFrame(new BitmapDrawable(getResources(), bitmap), 100);
            this.mBitmapArray.add(bitmap);
        }
        this.mAnimation.setOneShot(false);
        int width = (int) (36.0f * density);
        int height = (int) (36.0f * density);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.gravity = 17;
        result.setLayoutParams(params);
        result.setImageDrawable(this.mAnimation);
        this.mImageArray.add(result);
        return result;
    }

    private ImageView setCloseButton(Context context, float density) {
        Log.d(TAG, "setCloseButton : ");
        ImageView result = new ImageView(context);
        int rectX = (((this.mScreen.x - this.mFrameWidth) / 2) + this.mFrameWidth) - (this.mCloseButtonSize - (this.mCloseButtonSize / 4));
        int rectY = ((this.mScreen.y - this.mFrameHeight) / 2) - (this.mCloseButtonSize / 2);
        if (this.mAdHeight + (this.mCloseButtonSize * 2) > this.mScreen.y) {
            rectY = (this.mScreen.y - this.mAdHeight) / 2;
            rectX = this.mAdWidth + ((this.mScreen.x - this.mAdWidth) / 2);
        } else if (this.mAdWidth + (this.mCloseButtonSize * 2) > this.mScreen.x) {
            rectX = this.mScreen.x - this.mCloseButtonSize;
        }
        FrameLayout.LayoutParams closeParames = new FrameLayout.LayoutParams(this.mCloseButtonSize, this.mCloseButtonSize);
        closeParames.setMargins(rectX, rectY, 0, 0);
        closeParames.gravity = 48;
        result.setLayoutParams(closeParames);
        byte[] data = FluctDbAccess.getCloseButtonImage(context);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        result.setImageBitmap(bitmap);
        result.setOnClickListener(this.mCloseButton);
        this.mBitmapArray.add(bitmap);
        this.mImageArray.add(result);
        return result;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        FluctInterstitialManager.callback(2);
        stopAnimation();
        cleanupImage();
        finish();
        return true;
    }
}

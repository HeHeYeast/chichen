package net.nend.android;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.nend.android.DownloadTask;
import net.nend.android.NendAdIconView;
import net.nend.android.NendAdView;
import net.nend.android.NendHelper;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NendAdIconLoader implements DownloadTask.Downloadable<NendAdIconResponse>, NendAdIconView.AdListener {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int MESSAGE_CODE = 719;
    private NendAdIconRequest mAdRequest;
    private OnClickListner mClickListner;
    private Context mContext;
    private DownloadTask<NendAdIconResponse> mDownloadTask;
    private OnFailedListner mFailedListner;
    private Handler mHandler;
    private boolean mHasWindowForcus;
    private List<NendAdIconView> mIconViewList;
    private ImpressionCountTask mImpressionTask;
    private OnReceiveListner mReceiveListner;
    private int mSpotId;
    private int mReloadIntervalInSeconds = 60;
    private boolean isStarted = false;
    private boolean mReloadable = true;

    public interface OnClickListner {
        void onClick(NendAdIconView nendAdIconView);
    }

    public interface OnFailedListner {
        void onFailedToReceiveAd(NendIconError nendIconError);
    }

    public interface OnReceiveListner {
        void onReceiveAd(NendAdIconView nendAdIconView);
    }

    static {
        $assertionsDisabled = !NendAdIconLoader.class.desiredAssertionStatus();
    }

    public NendAdIconLoader(Context context, int spotId, String apiKey) {
        this.mContext = context;
        this.mSpotId = spotId;
        if (spotId <= 0) {
            throw new IllegalArgumentException(NendStatus.ERR_INVALID_SPOT_ID.getMsg("spot id : " + spotId));
        }
        if (TextUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException(NendStatus.ERR_INVALID_API_KEY.getMsg("api key : " + apiKey));
        }
        NendHelper.setDebuggable(context);
        this.mIconViewList = new ArrayList();
        this.mHandler = new Handler() { // from class: net.nend.android.NendAdIconLoader.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (NendAdIconLoader.this.mIconViewList.size() > 0) {
                    NendAdIconLoader.this.mAdRequest.setRequestCount(NendAdIconLoader.this.mIconViewList.size());
                    NendAdIconLoader.this.mDownloadTask = new DownloadTask(NendAdIconLoader.this);
                    NendHelper.AsyncTaskHelper.execute(NendAdIconLoader.this.mDownloadTask, new Void[0]);
                    return;
                }
                NendLog.d("");
            }
        };
        this.mAdRequest = new NendAdIconRequest(context, spotId, apiKey);
    }

    public void addIconView(NendAdIconView iconView) {
        if (iconView != null && this.mIconViewList.size() < 8 && !this.mIconViewList.contains(iconView)) {
            if (this.isStarted) {
                loadAd();
            }
            this.mReloadable = true;
            this.mIconViewList.add(iconView);
            iconView.setListner(this);
        }
    }

    public void removeIconView(NendAdIconView iconView) {
        if (iconView != null) {
            iconView.deallocate();
            this.mIconViewList.remove(iconView);
            if (this.mIconViewList.size() == 0) {
                pause();
            }
        }
    }

    public int getIconCount() {
        return this.mIconViewList.size();
    }

    public void loadAd() {
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        this.mHandler.removeMessages(MESSAGE_CODE);
        this.mHandler.sendEmptyMessage(MESSAGE_CODE);
        this.isStarted = true;
    }

    public void resume() {
        this.mReloadable = true;
        startLoading();
    }

    private void startLoading() {
        if (this.mReloadable && !this.mHandler.hasMessages(MESSAGE_CODE)) {
            this.mHandler.sendEmptyMessageDelayed(MESSAGE_CODE, this.mReloadIntervalInSeconds * 1000);
        }
    }

    public void pause() {
        this.mReloadable = false;
        stopLoading();
    }

    private void stopLoading() {
        if (this.mDownloadTask != null && !this.mDownloadTask.isCancelled()) {
            this.mDownloadTask.cancel(true);
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(MESSAGE_CODE);
        }
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public void onDownload(NendAdIconResponse response) {
        if (response != null) {
            this.mReloadIntervalInSeconds = NendHelper.setReloadIntervalInSeconds(response.getReloadIntervalInSeconds());
            String impressionCountUrl = response.getImpressionCountUrl();
            ArrayList<AdParameter> iconParamList = response.getAdParameterList();
            for (int i = 0; i < this.mIconViewList.size(); i++) {
                if (iconParamList.size() > i) {
                    AdParameter param = iconParamList.get(i);
                    if (!TextUtils.isEmpty(impressionCountUrl)) {
                        impressionCountUrl = String.valueOf(impressionCountUrl) + String.format("&ic[]=%s", param.getIconId());
                    }
                    this.mIconViewList.get(i).loadImage(param, this.mSpotId);
                }
            }
            this.mImpressionTask = new ImpressionCountTask();
            NendHelper.AsyncTaskHelper.execute(this.mImpressionTask, impressionCountUrl);
        } else {
            NendLog.d("onFailedToImageDownload!");
            if (this.mFailedListner != null) {
                NendIconError error = new NendIconError();
                error.setLoader(this);
                error.setErrorType(0);
                error.setNendError(NendAdView.NendError.FAILED_AD_REQUEST);
                this.mFailedListner.onFailedToReceiveAd(error);
            }
        }
        if (this.mReloadable && !this.mHandler.hasMessages(MESSAGE_CODE)) {
            this.mHandler.sendEmptyMessageDelayed(MESSAGE_CODE, this.mReloadIntervalInSeconds * 1000);
        }
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public NendAdIconResponse makeResponse(HttpEntity entity) {
        if (entity != null) {
            NendAdIconResponseParser parser = new NendAdIconResponseParser(this.mContext, this.mIconViewList.size());
            try {
                return parser.parseResponse(EntityUtils.toString(entity));
            } catch (IOException e) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                NendLog.d(NendStatus.ERR_HTTP_REQUEST, e);
            } catch (ParseException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                NendLog.d(NendStatus.ERR_HTTP_REQUEST, e2);
            }
        }
        return null;
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public String getRequestUrl() {
        return this.mAdRequest.getRequestUrl(NendHelper.makeUid(this.mContext));
    }

    public void setOnClickListner(OnClickListner onClickListner) {
        this.mClickListner = onClickListner;
    }

    @Override // net.nend.android.NendAdIconView.AdListener
    public void onClick(View v) {
        if (this.mClickListner != null) {
            this.mClickListner.onClick((NendAdIconView) v);
        }
    }

    public void setOnReceiveLisner(OnReceiveListner onReceiveListner) {
        this.mReceiveListner = onReceiveListner;
    }

    @Override // net.nend.android.NendAdIconView.AdListener
    public void onReceive(View v) {
        if (this.mReceiveListner != null) {
            this.mReceiveListner.onReceiveAd((NendAdIconView) v);
        }
    }

    public void setOnFailedListner(OnFailedListner onFailedListner) {
        this.mFailedListner = onFailedListner;
    }

    @Override // net.nend.android.NendAdIconView.AdListener
    public void onFailedToReceive(NendIconError error) {
        if (this.mFailedListner != null) {
            this.mFailedListner.onFailedToReceiveAd(error);
        }
    }

    @Override // net.nend.android.NendAdIconView.AdListener
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            if (!this.mHasWindowForcus) {
                this.mHasWindowForcus = true;
                resume();
                return;
            }
            return;
        }
        if (this.mHasWindowForcus) {
            this.mHasWindowForcus = false;
            pause();
        }
    }
}

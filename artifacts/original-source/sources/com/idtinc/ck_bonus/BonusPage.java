package com.idtinc.ck_bonus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.idtinc.ckchickandduck.AppDelegate;
import com.loopj.android.http.BinaryHttpResponseHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class BonusPage {
    private AppDelegate appDelegate;
    public int bonus;
    public short buttonStatus;
    public boolean clickedF;
    public String contentString;
    public String iconUrlString;
    public int itemID;
    public String openDateKeyString;
    public int openType;
    public String titleString;
    public String urlString;
    DownloadBitmapAsyncTask downloadBitmapAsyncTask = null;
    public Bitmap smallImageBitmap = null;
    public boolean smallImageLoadingF = false;

    public BonusPage(int _itemID, String _openDateKeyString, int _openType, int _bonus, String _urlString, String _iconUrlString, String _titleString, String _contentString, AppDelegate _appDelegate) {
        this.itemID = -1;
        this.openDateKeyString = "";
        this.openType = 0;
        this.bonus = -1;
        this.urlString = "";
        this.iconUrlString = "";
        this.titleString = "";
        this.contentString = "";
        this.buttonStatus = (short) -1;
        this.clickedF = false;
        this.appDelegate = _appDelegate;
        this.itemID = -_itemID;
        this.openDateKeyString = _openDateKeyString;
        this.openType = _openType;
        this.bonus = _bonus;
        this.urlString = _urlString;
        this.iconUrlString = _iconUrlString;
        this.titleString = _titleString;
        this.contentString = _contentString;
        this.buttonStatus = (short) -1;
        this.clickedF = false;
        clearDownloadBitmapAsyncTask();
        clearBitmap();
    }

    public void reSet(int _itemID, String _openDateKeyString, int _openType, int _bonus, String _urlString, String _iconUrlString, String _titleString, String _contentString, AppDelegate _appDelegate) {
        this.itemID = -_itemID;
        this.openDateKeyString = _openDateKeyString;
        this.openType = _openType;
        this.bonus = _bonus;
        this.urlString = _urlString;
        this.iconUrlString = _iconUrlString;
        this.titleString = _titleString;
        this.contentString = _contentString;
        this.buttonStatus = (short) -1;
        this.clickedF = false;
        clearDownloadBitmapAsyncTask();
    }

    public void startRequest() {
        clearBitmap();
        clearDownloadBitmapAsyncTask();
        if (this.appDelegate != null && this.appDelegate.checkInterNet() && this.iconUrlString != null && this.iconUrlString.length() >= 10) {
            this.downloadBitmapAsyncTask = new DownloadBitmapAsyncTask(this, null);
            this.downloadBitmapAsyncTask.execute(this.iconUrlString);
        }
    }

    public BinaryHttpResponseHandler initNewBinaryHttpResponseHandler() {
        String[] allowedContentTypes = {"image/png", "image/jpeg"};
        return new BinaryHttpResponseHandler(allowedContentTypes) { // from class: com.idtinc.ck_bonus.BonusPage.1
            @Override // com.loopj.android.http.BinaryHttpResponseHandler
            public void onSuccess(byte[] fileData) {
                BonusPage.this.refreshBitmapWithData(fileData);
            }

            @Override // com.loopj.android.http.BinaryHttpResponseHandler
            public void onFailure(Throwable error, byte[] binaryData) {
                BonusPage.this.clearBitmap();
            }
        };
    }

    public void clearDownloadBitmapAsyncTask() {
        if (this.downloadBitmapAsyncTask != null) {
            if (!this.downloadBitmapAsyncTask.isCancelled()) {
                this.downloadBitmapAsyncTask.cancel(true);
            }
            this.downloadBitmapAsyncTask = null;
        }
    }

    public void clearBitmap() {
        this.smallImageLoadingF = true;
        if (this.smallImageBitmap != null) {
            if (!this.smallImageBitmap.isRecycled()) {
                this.smallImageBitmap.recycle();
            }
            this.smallImageBitmap = null;
        }
        this.smallImageLoadingF = false;
    }

    public void refreshBitmapWithData(byte[] fileData) {
        clearBitmap();
        this.smallImageLoadingF = true;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.Options opt2 = new BitmapFactory.Options();
        opt2.inJustDecodeBounds = false;
        opt2.inPurgeable = true;
        opt2.inInputShareable = true;
        BitmapFactory.decodeByteArray(fileData, 0, fileData.length, opt);
        opt2.inSampleSize = 1;
        this.smallImageBitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length, opt2);
        this.smallImageLoadingF = false;
    }

    public void stopLoading() {
        clearDownloadBitmapAsyncTask();
    }

    private class DownloadBitmapAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        private DownloadBitmapAsyncTask() {
        }

        /* synthetic */ DownloadBitmapAsyncTask(BonusPage bonusPage, DownloadBitmapAsyncTask downloadBitmapAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(String... params) throws IOException {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.connect();
                InputStream inputStream = con.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return bitmap;
            } catch (IOException e2) {
                return bitmap;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate((Object[]) progress);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap _bitmap) {
            BonusPage.this.clearBitmap();
            BonusPage.this.smallImageBitmap = _bitmap;
            super.onPostExecute((DownloadBitmapAsyncTask) _bitmap);
        }
    }

    public void onDestroy() {
        clearDownloadBitmapAsyncTask();
        clearBitmap();
        this.appDelegate = null;
    }
}

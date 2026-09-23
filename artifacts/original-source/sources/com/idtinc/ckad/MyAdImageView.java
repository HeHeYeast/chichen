package com.idtinc.ckad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.idtinc.ckchickandduck.AppDelegate;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MyAdImageView extends ImageView {
    private AppDelegate appDelegate;
    private float displaySeconds;
    private DownloadBitmapAsyncTask downloadBitmapAsyncTask;
    private int finalHeight;
    private int finalWidth;
    private String uriPkString;
    private String uriString;
    private String url0String;
    private String url1String;
    private float zoomRate;

    public MyAdImageView(Context context, int _finalwidth, int _finalheight, float _zoomrate) {
        super(context);
        this.finalWidth = 0;
        this.finalHeight = 0;
        this.zoomRate = 1.0f;
        this.url0String = "";
        this.url1String = "";
        this.uriString = "";
        this.uriPkString = "";
        this.displaySeconds = -1.0f;
        this.downloadBitmapAsyncTask = null;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.url0String = "";
        this.url1String = "";
        this.uriString = "";
        this.uriPkString = "";
        this.displaySeconds = -1.0f;
    }

    public void requestStart(String _url0String, String _url1String, String _uriString, String _uriPkString, float _displaySeconds) {
        clearDownloadBitmapAsyncTask();
        if (this.appDelegate != null && _url0String.length() >= 10 && _url1String.length() >= 10 && _displaySeconds >= 1.0f && this.appDelegate.checkInterNet()) {
            this.url0String = _url0String;
            this.url1String = _url1String;
            this.uriString = _uriString;
            this.uriPkString = _uriPkString;
            this.displaySeconds = _displaySeconds;
            this.downloadBitmapAsyncTask = new DownloadBitmapAsyncTask(this, null);
            this.downloadBitmapAsyncTask.execute(this.url0String);
        }
    }

    public void clearDownloadBitmapAsyncTask() {
        setVisibility(8);
        this.url0String = "";
        this.url1String = "";
        this.uriString = "";
        this.uriPkString = "";
        this.displaySeconds = -1.0f;
        if (this.downloadBitmapAsyncTask != null) {
            if (!this.downloadBitmapAsyncTask.isCancelled()) {
                this.downloadBitmapAsyncTask.cancel(true);
            }
            this.downloadBitmapAsyncTask = null;
        }
    }

    private class DownloadBitmapAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        private DownloadBitmapAsyncTask() {
        }

        /* synthetic */ DownloadBitmapAsyncTask(MyAdImageView myAdImageView, DownloadBitmapAsyncTask downloadBitmapAsyncTask) {
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
            MyAdImageView.this.doDisplay(_bitmap);
            super.onPostExecute((DownloadBitmapAsyncTask) _bitmap);
        }
    }

    public void doDisplay(Bitmap _bitmap) {
        if (_bitmap != null && this.url1String.length() >= 10 && this.displaySeconds >= 1.0f) {
            setImageBitmap(_bitmap);
            setVisibility(0);
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckad.MyAdImageView.1
                @Override // java.lang.Runnable
                public void run() {
                    MyAdImageView.this.doHidden();
                }
            }, (int) (this.displaySeconds * 1000.0f));
            return;
        }
        clearDownloadBitmapAsyncTask();
    }

    public void doHidden() {
        setVisibility(8);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (getVisibility() != 0) {
            return false;
        }
        if (event.getAction() == 0) {
            if (this.appDelegate != null && this.appDelegate.checkInterNet()) {
                Boolean uriOkF = false;
                if (this.uriString != null && this.uriPkString != null && this.uriString.length() >= 5 && this.uriPkString.length() >= 5 && this.appDelegate.checkPackageInstalled(this.uriPkString)) {
                    uriOkF = true;
                }
                if (uriOkF.booleanValue()) {
                    Uri uri = Uri.parse(this.uriString);
                    Intent intent = new Intent("android.intent.action.VIEW", uri);
                    getContext().startActivity(intent);
                } else if (this.url1String.length() >= 10) {
                    Uri uri2 = Uri.parse(this.url1String);
                    Intent intent2 = new Intent("android.intent.action.VIEW", uri2);
                    getContext().startActivity(intent2);
                }
            }
            doHidden();
        } else {
            event.getAction();
        }
        return true;
    }

    public void onDestroy() {
        clearDownloadBitmapAsyncTask();
        this.appDelegate = null;
    }
}

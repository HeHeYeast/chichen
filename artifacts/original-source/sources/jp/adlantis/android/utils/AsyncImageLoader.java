package jp.adlantis.android.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AsyncImageLoader {
    private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<>();

    public interface ImageLoadedCallback {
        void imageLoaded(Drawable drawable, String str);
    }

    protected static InputStream inputStreamForUrl(String str) throws IOException {
        return new URL(str).openStream();
    }

    public static Drawable loadImageFromUrl(String str) {
        InputStream inputStreamInputStreamForUrl;
        Log.d("AsyncImageLoader", "loadImageFromUrl=" + str);
        if (str == null) {
            return null;
        }
        try {
            inputStreamInputStreamForUrl = inputStreamForUrl(str);
        } catch (IOException e) {
            System.out.println(e);
            inputStreamInputStreamForUrl = null;
        }
        if (inputStreamInputStreamForUrl == null) {
            return null;
        }
        try {
            return Drawable.createFromStream(inputStreamInputStreamForUrl, "src");
        } catch (OutOfMemoryError e2) {
            Log.e("AsyncImageLoader", "exception calling Drawable.createFromStream() " + e2);
            return null;
        }
    }

    public void clear() {
        this.imageCache.clear();
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [jp.adlantis.android.utils.AsyncImageLoader$2] */
    public Drawable loadDrawable(Context context, final String str, final ImageLoadedCallback imageLoadedCallback) {
        Drawable drawableCreateFromStream;
        IOException e;
        Drawable drawable;
        if (this.imageCache.containsKey(str) && (drawable = this.imageCache.get(str).get()) != null) {
            return drawable;
        }
        if (context == null || !ADLAssetUtils.isAssetUrl(str)) {
            final Handler handler = new Handler(Looper.getMainLooper()) { // from class: jp.adlantis.android.utils.AsyncImageLoader.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    if (imageLoadedCallback != null) {
                        imageLoadedCallback.imageLoaded((Drawable) message.obj, str);
                    }
                }
            };
            new Thread() { // from class: jp.adlantis.android.utils.AsyncImageLoader.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Drawable drawableLoadImageFromUrl = AsyncImageLoader.loadImageFromUrl(str);
                    if (drawableLoadImageFromUrl != null) {
                        AsyncImageLoader.this.putDrawableInCache(str, drawableLoadImageFromUrl);
                        handler.sendMessage(handler.obtainMessage(0, drawableLoadImageFromUrl));
                    }
                }
            }.start();
            return null;
        }
        try {
            drawableCreateFromStream = Drawable.createFromStream(ADLAssetUtils.inputStreamFromAssetUri(context, Uri.parse(str)), str);
        } catch (IOException e2) {
            drawableCreateFromStream = null;
            e = e2;
        }
        try {
            putDrawableInCache(str, drawableCreateFromStream);
            return drawableCreateFromStream;
        } catch (IOException e3) {
            e = e3;
            Log.e(getClass().getSimpleName(), "exception calling Drawable.createFromStream() " + e);
            return drawableCreateFromStream;
        }
    }

    public void putDrawableInCache(String str, Drawable drawable) {
        this.imageCache.put(str, new SoftReference<>(drawable));
        Log.d(getClass().getSimpleName(), "imageCache.size()=" + this.imageCache.size());
    }
}

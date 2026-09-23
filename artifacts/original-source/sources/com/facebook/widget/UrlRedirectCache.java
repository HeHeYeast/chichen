package com.facebook.widget;

import android.content.Context;
import com.facebook.internal.FileLruCache;
import com.facebook.internal.Utility;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class UrlRedirectCache {
    private static volatile FileLruCache urlRedirectCache;
    static final String TAG = UrlRedirectCache.class.getSimpleName();
    private static final String REDIRECT_CONTENT_TAG = String.valueOf(TAG) + "_Redirect";

    UrlRedirectCache() {
    }

    static synchronized FileLruCache getCache(Context context) throws IOException {
        if (urlRedirectCache == null) {
            urlRedirectCache = new FileLruCache(context.getApplicationContext(), TAG, new FileLruCache.Limits());
        }
        return urlRedirectCache;
    }

    static URL getRedirectedUrl(Context context, URL url) throws Throwable {
        if (url == null) {
            return null;
        }
        String urlString = url.toString();
        URL finalUrl = null;
        InputStreamReader reader = null;
        try {
            FileLruCache cache = getCache(context);
            boolean redirectExists = false;
            InputStreamReader reader2 = null;
            while (true) {
                try {
                    InputStream stream = cache.get(urlString, REDIRECT_CONTENT_TAG);
                    if (stream == null) {
                        break;
                    }
                    redirectExists = true;
                    reader = new InputStreamReader(stream);
                    char[] buffer = new char[128];
                    StringBuilder urlBuilder = new StringBuilder();
                    while (true) {
                        int bufferLength = reader.read(buffer, 0, buffer.length);
                        if (bufferLength <= 0) {
                            break;
                        }
                        urlBuilder.append(buffer, 0, bufferLength);
                    }
                    Utility.closeQuietly(reader);
                    urlString = urlBuilder.toString();
                    reader2 = reader;
                } catch (MalformedURLException e) {
                    reader = reader2;
                    Utility.closeQuietly(reader);
                    return null;
                } catch (IOException e2) {
                    reader = reader2;
                    Utility.closeQuietly(reader);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    reader = reader2;
                    Utility.closeQuietly(reader);
                    throw th;
                }
            }
            if (redirectExists) {
                URL finalUrl2 = new URL(urlString);
                finalUrl = finalUrl2;
            }
            Utility.closeQuietly(reader2);
            return finalUrl;
        } catch (MalformedURLException e3) {
        } catch (IOException e4) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    static void cacheUrlRedirect(Context context, URL fromUrl, URL toUrl) throws IOException {
        if (fromUrl != null && toUrl != null) {
            OutputStream redirectStream = null;
            try {
                FileLruCache cache = getCache(context);
                redirectStream = cache.openPutStream(fromUrl.toString(), REDIRECT_CONTENT_TAG);
                redirectStream.write(toUrl.toString().getBytes());
            } catch (IOException e) {
            } finally {
                Utility.closeQuietly(redirectStream);
            }
        }
    }
}

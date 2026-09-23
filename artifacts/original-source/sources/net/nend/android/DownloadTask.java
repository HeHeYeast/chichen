package net.nend.android;

import android.os.AsyncTask;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.IOException;
import java.lang.ref.WeakReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class DownloadTask<T> extends AsyncTask<Void, Void, T> {
    private final WeakReference<Downloadable<T>> mReference;

    interface Downloadable<T> {
        String getRequestUrl();

        T makeResponse(HttpEntity httpEntity);

        void onDownload(T t);
    }

    DownloadTask(Downloadable<T> downloadable) {
        this.mReference = new WeakReference<>(downloadable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public T doInBackground(Void... voidArr) {
        T t = null;
        Thread.currentThread().setPriority(10);
        if (!isCancelled()) {
            Downloadable<T> downloadable = this.mReference.get();
            if (downloadable == null || downloadable.getRequestUrl() == null || downloadable.getRequestUrl().length() <= 0) {
                NendLog.w(NendStatus.ERR_INVALID_URL);
            } else {
                final String requestUrl = downloadable.getRequestUrl();
                NendLog.v("Download from " + requestUrl);
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                try {
                    HttpParams params = defaultHttpClient.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, HapticContentSDK.f17b04440444044404440444);
                    HttpConnectionParams.setSoTimeout(params, HapticContentSDK.f17b04440444044404440444);
                    NendLog.d("start request!");
                    t = (T) defaultHttpClient.execute(new HttpGet(requestUrl), new ResponseHandler<T>() { // from class: net.nend.android.DownloadTask.1
                        @Override // org.apache.http.client.ResponseHandler
                        public T handleResponse(HttpResponse response) throws IOException {
                            Downloadable<T> downloadable2;
                            NendLog.d("get response!");
                            if (!DownloadTask.this.isCancelled() && response.getStatusLine().getStatusCode() == 200 && (downloadable2 = (Downloadable) DownloadTask.this.mReference.get()) != null) {
                                return downloadable2.makeResponse(response.getEntity());
                            }
                            if (!DownloadTask.this.isCancelled()) {
                                NendLog.w(NendStatus.ERR_HTTP_REQUEST, "http status : " + response.getStatusLine().getStatusCode());
                            }
                            return null;
                        }
                    });
                } catch (IOException e) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, e);
                } catch (IllegalStateException e2) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, e2);
                } catch (IllegalArgumentException e3) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, e3);
                } catch (ClientProtocolException e4) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, e4);
                } finally {
                    defaultHttpClient.getConnectionManager().shutdown();
                }
            }
        }
        return t;
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(T response) {
        Downloadable<T> downloadable = this.mReference.get();
        if (!isCancelled() && downloadable != null) {
            downloadable.onDownload(response);
        }
    }

    boolean isFinished() {
        return getStatus() == AsyncTask.Status.FINISHED;
    }
}

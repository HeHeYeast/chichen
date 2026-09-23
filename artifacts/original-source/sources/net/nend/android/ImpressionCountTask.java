package net.nend.android;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class ImpressionCountTask extends AsyncTask<String, Void, Void> {
    ImpressionCountTask() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(String... params) {
        Thread.currentThread().setPriority(10);
        if (isCancelled()) {
            return null;
        }
        String requestUrl = params[0];
        if (TextUtils.isEmpty(requestUrl)) {
            NendLog.w(NendStatus.ERR_INVALID_URL);
        } else {
            DefaultHttpClient client = new DefaultHttpClient();
            try {
                HttpParams httpParams = client.getParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, HapticContentSDK.f17b04440444044404440444);
                HttpConnectionParams.setSoTimeout(httpParams, HapticContentSDK.f17b04440444044404440444);
                NendLog.d("start request!");
                return (Void) client.execute(new HttpGet(requestUrl), new ResponseHandler<Void>() { // from class: net.nend.android.ImpressionCountTask.1
                    @Override // org.apache.http.client.ResponseHandler
                    public Void handleResponse(HttpResponse response) throws IOException {
                        return null;
                    }
                });
            } catch (IllegalArgumentException e) {
                NendLog.w(NendStatus.ERR_HTTP_REQUEST, e);
            } catch (ClientProtocolException e2) {
                NendLog.w(NendStatus.ERR_HTTP_REQUEST, e2);
            } catch (IOException e3) {
                NendLog.w(NendStatus.ERR_HTTP_REQUEST, e3);
            } catch (IllegalStateException e4) {
                NendLog.w(NendStatus.ERR_HTTP_REQUEST, e4);
            } finally {
                client.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Void response) {
    }
}

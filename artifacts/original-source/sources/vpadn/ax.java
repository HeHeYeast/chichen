package vpadn;

import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ax extends AsyncTask<String, Void, Boolean> {
    private final DefaultHttpClient a = av.a(20000);
    private final az b;

    /* renamed from: c, reason: collision with root package name */
    private final a f335c;

    public interface a {
        void x();

        void y();
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ Boolean doInBackground(String... strArr) {
        String[] strArr2 = strArr;
        if (strArr2 == null || strArr2[0] == null) {
            return false;
        }
        return a(strArr2[0]);
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ void onPostExecute(Boolean bool) {
        if (bool.booleanValue()) {
            if (this.f335c != null) {
                this.f335c.x();
            }
        } else if (this.f335c != null) {
            this.f335c.y();
        }
    }

    public ax(a aVar, az azVar) {
        this.f335c = aVar;
        this.b = azVar;
    }

    private Boolean a(String str) {
        boolean zA;
        Exception e;
        ab.a("VideoDownloadTask", "call downloadToCache for cache video");
        try {
            try {
            } finally {
                C0086a.a((HttpClient) this.a);
            }
        } catch (Exception e2) {
            zA = false;
            e = e2;
        }
        if (str == null) {
            throw new IOException("Unable to connect to null url.");
        }
        HttpResponse httpResponseExecute = this.a.execute(new HttpGet(str));
        if (httpResponseExecute == null || httpResponseExecute.getEntity() == null) {
            ab.b("VideoDownloadTask", "Obtained null response from video url: " + str);
            throw new IOException("Obtained null response from video url: " + str);
        }
        File fileA = a(httpResponseExecute.getEntity().getContent());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(fileA));
        zA = this.b.a(str, (InputStream) bufferedInputStream);
        C0086a.a(bufferedInputStream);
        try {
            ab.a("VideoDownloadTask", "result of cache video -> savedSuccessfully:" + zA);
            fileA.delete();
            C0086a.a((HttpClient) this.a);
        } catch (Exception e3) {
            e = e3;
            ab.a("VideoDownloadTask", "Failed to downloadToCache. Exception:" + e.toString(), e);
            return Boolean.valueOf(zA);
        }
        return Boolean.valueOf(zA);
    }

    private File a(InputStream inputStream) throws IOException {
        File fileCreateTempFile = File.createTempFile("vpadn-video-tmp", null, this.b.a);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileCreateTempFile));
        try {
            try {
                C0086a.a(inputStream, bufferedOutputStream, 70000000L);
                return fileCreateTempFile;
            } catch (IOException e) {
                fileCreateTempFile.delete();
                ab.a("VideoDownloadTask", "(for video cache) copyInputStreamToTempFile throw IOException:", e);
                throw e;
            }
        } finally {
            C0086a.a(inputStream);
            C0086a.a(bufferedOutputStream);
        }
    }
}

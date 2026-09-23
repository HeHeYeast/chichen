package vpadn;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class av {
    private static av a = new av();

    public static DefaultHttpClient a(int i) {
        av avVar = a;
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        return new DefaultHttpClient(basicHttpParams);
    }
}

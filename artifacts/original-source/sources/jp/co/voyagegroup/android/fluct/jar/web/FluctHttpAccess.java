package jp.co.voyagegroup.android.fluct.jar.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jp.co.voyagegroup.android.fluct.jar.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctHttpAccess {
    private static final String TAG = "FluctHttpAccess";

    public static Document getDocument(String url) throws ParserConfigurationException, SAXException, IOException {
        Log.d(TAG, "getDocument : ");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            HttpUriRequest httpGet = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpConnectionParams.setSoTimeout(httpParams, 30000);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            Document document = documentBuilder.parse(httpResponse.getEntity().getContent());
            return document;
        } catch (ClientProtocolException e) {
            Log.e(TAG, "getDocument : ClientProtocolException is " + e.getLocalizedMessage());
            return null;
        } catch (IOException e2) {
            Log.e(TAG, "getDocument : IOException is " + e2.getLocalizedMessage());
            return null;
        } catch (IllegalStateException e3) {
            Log.e(TAG, "getDocument : IllegalStateException is " + e3.getLocalizedMessage());
            return null;
        } catch (ParserConfigurationException e4) {
            Log.e(TAG, "getDocument : ParserConfigurationException is " + e4.getLocalizedMessage());
            return null;
        } catch (SAXException e5) {
            Log.e(TAG, "getDocument : SAXException is " + e5.getLocalizedMessage());
            return null;
        }
    }

    public boolean executeUrls(List<String> convUrls) {
        Log.d(TAG, "executeUrls : ");
        boolean result = true;
        for (int i = 0; i < convUrls.size(); i++) {
            Log.v(TAG, "executeUrls : url is " + convUrls.get(i));
            String convUrl = convUrls.get(i);
            if (!doGetRequest(convUrl)) {
                result = false;
            }
        }
        return result;
    }

    private boolean doGetRequest(String url) throws IOException {
        Log.d(TAG, "doGetRequest : url is " + url);
        boolean result = false;
        HttpUriRequest httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams httpParams = httpClient.getParams();
        InputStream inpurStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        try {
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    result = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "doGetRequest : Exception is " + e.getLocalizedMessage());
                if (0 != 0) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "doGetRequest : IOException is " + e2.getLocalizedMessage());
                    }
                }
                if (0 != 0) {
                    inputStreamReader.close();
                }
                if (0 != 0) {
                    inpurStream.close();
                }
            }
            return result;
        } finally {
            if (0 != 0) {
                try {
                    bufferedReader.close();
                } catch (IOException e3) {
                    Log.e(TAG, "doGetRequest : IOException is " + e3.getLocalizedMessage());
                }
            }
            if (0 != 0) {
                inputStreamReader.close();
            }
            if (0 != 0) {
                inpurStream.close();
            }
        }
    }
}

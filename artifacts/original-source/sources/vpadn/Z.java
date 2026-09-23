package vpadn;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.ParseException;
import org.apache.http.protocol.HttpContext;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class Z implements HttpResponseInterceptor {
    @Override // org.apache.http.HttpResponseInterceptor
    public final void process(HttpResponse httpResponse, HttpContext httpContext) throws ParseException {
        Header contentEncoding;
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null && (contentEncoding = entity.getContentEncoding()) != null) {
            HeaderElement[] elements = contentEncoding.getElements();
            for (HeaderElement headerElement : elements) {
                if (headerElement.getName().equalsIgnoreCase("gzip")) {
                    httpResponse.setEntity(new C0087aa(httpResponse.getEntity()));
                    return;
                }
            }
        }
    }
}

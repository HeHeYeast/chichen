package vpadn;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/* renamed from: vpadn.aa, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class C0087aa extends HttpEntityWrapper {
    public C0087aa(HttpEntity httpEntity) {
        super(httpEntity);
    }

    @Override // org.apache.http.entity.HttpEntityWrapper, org.apache.http.HttpEntity
    public final InputStream getContent() throws IOException {
        return new GZIPInputStream(this.wrappedEntity.getContent());
    }

    @Override // org.apache.http.entity.HttpEntityWrapper, org.apache.http.HttpEntity
    public final long getContentLength() {
        return -1L;
    }
}

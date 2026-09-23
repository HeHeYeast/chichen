package org.apache.http.entity.mime.content;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface ContentDescriptor {
    String getCharset();

    long getContentLength();

    String getMediaType();

    String getMimeType();

    String getSubType();

    String getTransferEncoding();
}

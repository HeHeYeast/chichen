package org.apache.http.entity.mime.content;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class AbstractContentBody implements ContentBody {
    private final String mediaType;
    private final String mimeType;
    private final String subType;

    public AbstractContentBody(String mimeType) {
        if (mimeType == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        this.mimeType = mimeType;
        int i = mimeType.indexOf(47);
        if (i != -1) {
            this.mediaType = mimeType.substring(0, i);
            this.subType = mimeType.substring(i + 1);
        } else {
            this.mediaType = mimeType;
            this.subType = null;
        }
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getMimeType() {
        return this.mimeType;
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getMediaType() {
        return this.mediaType;
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getSubType() {
        return this.subType;
    }
}

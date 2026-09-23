package org.apache.http.entity.mime.content;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface ContentBody extends ContentDescriptor {
    String getFilename();

    void writeTo(OutputStream outputStream) throws IOException;
}

package com.loopj.android.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MIME;
import org.apache.http.message.BasicHeader;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class SimpleMultipartEntity implements HttpEntity {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private String boundary;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean isSetLast = false;
    boolean isSetFirst = false;

    public SimpleMultipartEntity() {
        this.boundary = null;
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            stringBuffer.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = stringBuffer.toString();
    }

    public void writeFirstBoundaryIfNeeds() {
        if (!this.isSetFirst) {
            try {
                this.out.write(("--" + this.boundary + "\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (!this.isSetLast) {
            try {
                this.out.write(("\r\n--" + this.boundary + "--\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.isSetLast = true;
        }
    }

    public void addPart(String str, String str2) {
        writeFirstBoundaryIfNeeds();
        try {
            this.out.write(("Content-Disposition: form-data; name=\"" + str + "\"\r\n\r\n").getBytes());
            this.out.write(str2.getBytes());
            this.out.write(("\r\n--" + this.boundary + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(String str, String str2, InputStream inputStream, boolean z) throws IOException {
        addPart(str, str2, inputStream, "application/octet-stream", z);
    }

    public void addPart(String str, String str2, InputStream inputStream, String str3, boolean z) throws IOException {
        writeFirstBoundaryIfNeeds();
        try {
            try {
                this.out.write(("Content-Disposition: form-data; name=\"" + str + "\"; filename=\"" + str2 + "\"\r\n").getBytes());
                this.out.write(("Content-Type: " + str3 + "\r\n").getBytes());
                this.out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
                byte[] bArr = new byte[4096];
                while (true) {
                    int i = inputStream.read(bArr);
                    if (i == -1) {
                        break;
                    } else {
                        this.out.write(bArr, 0, i);
                    }
                }
                if (!z) {
                    this.out.write(("\r\n--" + this.boundary + "\r\n").getBytes());
                }
                this.out.flush();
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void addPart(String str, File file, boolean z) throws IOException {
        try {
            addPart(str, file.getName(), new FileInputStream(file), z);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override // org.apache.http.HttpEntity
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return this.out.toByteArray().length;
    }

    @Override // org.apache.http.HttpEntity
    public Header getContentType() {
        return new BasicHeader(MIME.CONTENT_TYPE, "multipart/form-data; boundary=" + this.boundary);
    }

    @Override // org.apache.http.HttpEntity
    public boolean isChunked() {
        return false;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isRepeatable() {
        return false;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isStreaming() {
        return false;
    }

    @Override // org.apache.http.HttpEntity
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.out.toByteArray());
    }

    @Override // org.apache.http.HttpEntity
    public Header getContentEncoding() {
        return null;
    }

    @Override // org.apache.http.HttpEntity
    public void consumeContent() throws UnsupportedOperationException, IOException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    @Override // org.apache.http.HttpEntity
    public InputStream getContent() throws UnsupportedOperationException, IOException {
        return new ByteArrayInputStream(this.out.toByteArray());
    }
}

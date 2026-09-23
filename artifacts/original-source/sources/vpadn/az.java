package vpadn;

import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class az extends aA<String, File> {
    final File a;
    private final Context b;

    /* renamed from: c, reason: collision with root package name */
    private final String f336c;

    @Override // vpadn.aA
    protected final /* synthetic */ int a(String str, File file) {
        String str2 = str;
        File file2 = file;
        if (file2 == null || !file2.exists() || file2.length() <= 0) {
            return super.a((az) str2, (String) file2);
        }
        if (file2 == null) {
            ab.b("Files", "intLength: file == null");
            return 0;
        }
        long length = file2.length();
        if (length < 2147483647L) {
            return (int) length;
        }
        ab.b("Files", "intLength: length > Integer.MAX_VALUE");
        return Integer.MAX_VALUE;
    }

    @Override // vpadn.aA
    protected final /* synthetic */ void a(boolean z, String str, File file, File file2) {
        File file3 = file;
        super.a(z, str, file3, file2);
        if (file3 == null || file3.delete()) {
            return;
        }
        ab.d("VpadnDiskLruCache", "Unable to delete file from cache: " + file3.getName());
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public az(Context context, String str, int i) throws IOException, IllegalArgumentException {
        super(100000000);
        File file = null;
        if (context == null) {
            throw new IllegalArgumentException("context may not be null.");
        }
        if (str == null) {
            throw new IllegalArgumentException("cacheDirectoryName may not be null.");
        }
        this.b = context;
        this.f336c = str;
        String str2 = context.getFilesDir() + File.separator + this.f336c;
        if (str2 == null) {
            ab.b("Files", "createDirectory: absolutePath IS NULL");
        } else {
            File file2 = new File(str2);
            if ((file2.exists() && file2.isDirectory()) || (file2.mkdirs() && file2.isDirectory())) {
                file = file2;
            } else {
                ab.b("Files", "createDirectory: create directory failed");
            }
        }
        this.a = file;
        if (this.a == null) {
            throw new IOException("Unable to obtain access to directory " + this.f336c);
        }
        a();
    }

    public final Uri a(String str) {
        File fileA = a((az) C0086a.e(str));
        if (fileA == null) {
            return null;
        }
        return Uri.parse(fileA.getAbsolutePath());
    }

    final synchronized boolean a(String str, InputStream inputStream) {
        File fileB;
        boolean z = false;
        synchronized (this) {
            if (str != null && inputStream != null) {
                String strE = C0086a.e(str);
                if (a(strE) == null && (fileB = b(strE, inputStream)) != null && fileB.exists()) {
                    b((az) strE, (String) fileB);
                    z = true;
                }
            }
        }
        return z;
    }

    private File b(String str, InputStream inputStream) throws IOException {
        File file = new File(this.b.getFilesDir() + File.separator + this.f336c + File.separator + str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                try {
                    C0086a.a(inputStream, fileOutputStream);
                } catch (IOException e) {
                    file.delete();
                    C0086a.a(fileOutputStream);
                    file = null;
                }
                return file;
            } finally {
                C0086a.a(fileOutputStream);
            }
        } catch (FileNotFoundException e2) {
            return null;
        }
    }

    private void a() {
        File[] fileArrListFiles = this.a.listFiles();
        if (fileArrListFiles != null) {
            for (File file : fileArrListFiles) {
                b((az) file.getName(), (String) file);
            }
        }
    }
}

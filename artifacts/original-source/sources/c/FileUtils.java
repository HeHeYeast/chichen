package c;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.A;
import vpadn.C0086a;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;
import vpadn.C0109w;
import vpadn.C0110x;
import vpadn.C0111y;
import vpadn.C0112z;
import vpadn.InterfaceC0102p;
import vpadn.W;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FileUtils extends C0103q {
    public static int NOT_FOUND_ERR = 1;
    public static int SECURITY_ERR = 2;
    public static int ABORT_ERR = 3;
    public static int NOT_READABLE_ERR = 4;
    public static int ENCODING_ERR = 5;
    public static int NO_MODIFICATION_ALLOWED_ERR = 6;
    public static int INVALID_STATE_ERR = 7;
    public static int SYNTAX_ERR = 8;
    public static int INVALID_MODIFICATION_ERR = 9;
    public static int QUOTA_EXCEEDED_ERR = 10;
    public static int TYPE_MISMATCH_ERR = 11;
    public static int PATH_EXISTS_ERR = 12;
    public static int TEMPORARY = 0;
    public static int PERSISTENT = 1;
    public static int RESOURCE = 2;
    public static int APPLICATION = 3;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException, C0111y, C0112z, IOException, IllegalArgumentException {
        File file;
        int i;
        int i2;
        try {
            if (str.equals("testSaveLocationExists")) {
                c0101o.a(new C0108v(C0108v.a.OK, C0086a.a()));
            } else if (str.equals("getFreeDiskSpace")) {
                c0101o.a(new C0108v(C0108v.a.OK, C0086a.a(false)));
            } else if (str.equals("testFileExists") || str.equals("testDirectoryExists")) {
                c0101o.a(new C0108v(C0108v.a.OK, C0086a.a(jSONArray.getString(0))));
            } else if (str.equals("readAsText")) {
                int i3 = jSONArray.length() >= 3 ? jSONArray.getInt(2) : 0;
                if (jSONArray.length() < 4) {
                    i2 = Integer.MAX_VALUE;
                } else {
                    i2 = jSONArray.getInt(3);
                }
                c0101o.a(new C0108v(C0108v.a.OK, readAsText(jSONArray.getString(0), jSONArray.getString(1), i3, i2)));
            } else if (str.equals("readAsDataURL")) {
                int i4 = jSONArray.length() >= 2 ? jSONArray.getInt(1) : 0;
                if (jSONArray.length() < 3) {
                    i = Integer.MAX_VALUE;
                } else {
                    i = jSONArray.getInt(2);
                }
                c0101o.a(new C0108v(C0108v.a.OK, readAsDataURL(jSONArray.getString(0), i4, i)));
            } else if (str.equals("write")) {
                c0101o.a(new C0108v(C0108v.a.OK, write(jSONArray.getString(0), jSONArray.getString(1), jSONArray.getInt(2))));
            } else if (str.equals("truncate")) {
                c0101o.a(new C0108v(C0108v.a.OK, a(jSONArray.getString(0), jSONArray.getLong(1))));
            } else if (str.equals("requestFileSystem")) {
                long jOptLong = jSONArray.optLong(1);
                if (jOptLong != 0 && jOptLong > C0086a.a(true) * 1024) {
                    c0101o.a(new C0108v(C0108v.a.ERROR, QUOTA_EXCEEDED_ERR));
                } else {
                    int i5 = jSONArray.getInt(0);
                    JSONObject jSONObject = new JSONObject();
                    if (i5 == TEMPORARY) {
                        jSONObject.put("name", "temporary");
                        if (Environment.getExternalStorageState().equals("mounted")) {
                            new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Android/data/" + this.cordova.a().getPackageName() + "/cache/").mkdirs();
                            jSONObject.put("root", e(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Android/data/" + this.cordova.a().getPackageName() + "/cache/"));
                        } else {
                            new File("/data/data/" + this.cordova.a().getPackageName() + "/cache/").mkdirs();
                            jSONObject.put("root", e("/data/data/" + this.cordova.a().getPackageName() + "/cache/"));
                        }
                    } else {
                        if (i5 != PERSISTENT) {
                            throw new IOException("No filesystem of type requested");
                        }
                        jSONObject.put("name", "persistent");
                        if (Environment.getExternalStorageState().equals("mounted")) {
                            jSONObject.put("root", getEntry(Environment.getExternalStorageDirectory()));
                        } else {
                            jSONObject.put("root", e("/data/data/" + this.cordova.a().getPackageName()));
                        }
                    }
                    c0101o.a(jSONObject);
                }
            } else if (str.equals("resolveLocalFileSystemURI")) {
                String strDecode = URLDecoder.decode(jSONArray.getString(0), "UTF-8");
                if (strDecode.startsWith("content:")) {
                    Cursor cursorManagedQuery = this.cordova.a().managedQuery(Uri.parse(strDecode), new String[]{"_data"}, null, null, null);
                    int columnIndexOrThrow = cursorManagedQuery.getColumnIndexOrThrow("_data");
                    cursorManagedQuery.moveToFirst();
                    file = new File(cursorManagedQuery.getString(columnIndexOrThrow));
                } else {
                    new URL(strDecode);
                    if (strDecode.startsWith("file://")) {
                        int iIndexOf = strDecode.indexOf("?");
                        file = iIndexOf < 0 ? new File(strDecode.substring(7, strDecode.length())) : new File(strDecode.substring(7, iIndexOf));
                    } else {
                        file = new File(strDecode);
                    }
                }
                if (!file.exists()) {
                    throw new FileNotFoundException();
                }
                if (!file.canRead()) {
                    throw new IOException();
                }
                c0101o.a(getEntry(file));
            } else if (str.equals("getMetadata")) {
                C0108v.a aVar = C0108v.a.OK;
                if (!d(jSONArray.getString(0)).exists()) {
                    throw new FileNotFoundException("Failed to find file in getMetadata");
                }
                c0101o.a(new C0108v(aVar, r2.lastModified()));
            } else if (str.equals("getFileMetadata")) {
                String string = jSONArray.getString(0);
                File fileD = d(string);
                if (!fileD.exists()) {
                    throw new FileNotFoundException("File: " + string + " does not exist.");
                }
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("size", fileD.length());
                jSONObject2.put("type", getMimeType(string));
                jSONObject2.put("name", fileD.getName());
                jSONObject2.put("fullPath", string);
                jSONObject2.put("lastModifiedDate", fileD.lastModified());
                c0101o.a(jSONObject2);
            } else if (str.equals("getParent")) {
                String realPathFromURI = getRealPathFromURI(Uri.parse(jSONArray.getString(0)), this.cordova);
                c0101o.a(c(realPathFromURI) ? e(realPathFromURI) : e(new File(realPathFromURI).getParent()));
            } else if (str.equals("getDirectory")) {
                c0101o.a(a(jSONArray.getString(0), jSONArray.getString(1), jSONArray.optJSONObject(2), true));
            } else if (str.equals("getFile")) {
                c0101o.a(a(jSONArray.getString(0), jSONArray.getString(1), jSONArray.optJSONObject(2), false));
            } else if (str.equals("remove")) {
                String string2 = jSONArray.getString(0);
                File fileD2 = d(string2);
                if (c(string2)) {
                    throw new C0112z("You can't delete the root directory");
                }
                if (fileD2.isDirectory() && fileD2.list().length > 0) {
                    throw new C0111y("You can't delete a directory that is not empty.");
                }
                if (fileD2.delete()) {
                    a(jSONArray.getString(0));
                    c0101o.b();
                } else {
                    c0101o.a(NO_MODIFICATION_ALLOWED_ERR);
                }
            } else if (str.equals("removeRecursively")) {
                String string3 = jSONArray.getString(0);
                if (c(string3) ? false : a(d(string3))) {
                    c0101o.b();
                } else {
                    c0101o.a(NO_MODIFICATION_ALLOWED_ERR);
                }
            } else if (str.equals("moveTo")) {
                c0101o.a(a(jSONArray.getString(0), jSONArray.getString(1), jSONArray.getString(2), true));
            } else if (str.equals("copyTo")) {
                c0101o.a(a(jSONArray.getString(0), jSONArray.getString(1), jSONArray.getString(2), false));
            } else {
                if (!str.equals("readEntries")) {
                    return false;
                }
                c0101o.a(b(jSONArray.getString(0)));
            }
        } catch (FileNotFoundException e) {
            c0101o.a(NOT_FOUND_ERR);
        } catch (MalformedURLException e2) {
            c0101o.a(ENCODING_ERR);
        } catch (IOException e3) {
            c0101o.a(INVALID_MODIFICATION_ERR);
        } catch (A e4) {
            c0101o.a(TYPE_MISMATCH_ERR);
        } catch (C0109w e5) {
            c0101o.a(ENCODING_ERR);
        } catch (C0110x e6) {
            c0101o.a(PATH_EXISTS_ERR);
        } catch (C0111y e7) {
            c0101o.a(INVALID_MODIFICATION_ERR);
        } catch (C0112z e8) {
            c0101o.a(NO_MODIFICATION_ALLOWED_ERR);
        }
        return true;
    }

    private void a(String str) throws IllegalArgumentException {
        try {
            this.cordova.a().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data = ?", new String[]{getRealPathFromURI(Uri.parse(str), this.cordova)});
        } catch (UnsupportedOperationException e) {
        }
    }

    private JSONArray b(String str) throws JSONException, FileNotFoundException {
        File fileD = d(str);
        if (!fileD.exists()) {
            throw new FileNotFoundException();
        }
        JSONArray jSONArray = new JSONArray();
        if (fileD.isDirectory()) {
            File[] fileArrListFiles = fileD.listFiles();
            for (int i = 0; i < fileArrListFiles.length; i++) {
                if (fileArrListFiles[i].canRead()) {
                    jSONArray.put(getEntry(fileArrListFiles[i]));
                }
            }
        }
        return jSONArray;
    }

    private JSONObject a(String str, String str2, String str3, boolean z) throws JSONException, C0111y, C0112z, IOException, IllegalArgumentException, C0109w, C0110x {
        String realPathFromURI = getRealPathFromURI(Uri.parse(str), this.cordova);
        String realPathFromURI2 = getRealPathFromURI(Uri.parse(str2), this.cordova);
        if (str3 != null && str3.contains(":")) {
            throw new C0109w("Bad file name");
        }
        File file = new File(realPathFromURI);
        if (!file.exists()) {
            throw new FileNotFoundException("The source does not exist");
        }
        File file2 = new File(realPathFromURI2);
        if (!file2.exists()) {
            throw new FileNotFoundException("The source does not exist");
        }
        if ("null".equals(str3) || "".equals(str3)) {
            str3 = null;
        }
        File file3 = str3 != null ? new File(String.valueOf(file2.getAbsolutePath()) + File.separator + str3) : new File(String.valueOf(file2.getAbsolutePath()) + File.separator + file.getName());
        if (file.getAbsolutePath().equals(file3.getAbsolutePath())) {
            throw new C0111y("Can't copy a file onto itself");
        }
        if (file.isDirectory()) {
            if (z) {
                if (file3.exists() && file3.isFile()) {
                    throw new C0111y("Can't rename a file to a directory");
                }
                if (a(file.getAbsolutePath(), file3.getAbsolutePath())) {
                    throw new C0111y("Can't move itself into itself");
                }
                if (file3.exists() && file3.list().length > 0) {
                    throw new C0111y("directory is not empty");
                }
                if (!file.renameTo(file3)) {
                    c(file, file3);
                    if (!file3.exists()) {
                        throw new IOException("moved failed");
                    }
                    a(file);
                }
                return getEntry(file3);
            }
            return c(file, file3);
        }
        if (z) {
            if (file3.exists() && file3.isDirectory()) {
                throw new C0111y("Can't rename a file to a directory");
            }
            if (!file.renameTo(file3)) {
                b(file, file3);
                if (!file3.exists()) {
                    throw new IOException("moved failed");
                }
                file.delete();
            }
            JSONObject entry = getEntry(file3);
            if (str.startsWith("content://")) {
                a(str);
                return entry;
            }
            return entry;
        }
        return a(file, file3);
    }

    private JSONObject a(File file, File file2) throws JSONException, C0111y, IOException {
        if (file2.exists() && file2.isDirectory()) {
            throw new C0111y("Can't rename a file to a directory");
        }
        b(file, file2);
        return getEntry(file2);
    }

    private static void b(File file, File file2) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        FileChannel channel = fileInputStream.getChannel();
        FileChannel channel2 = fileOutputStream.getChannel();
        try {
            channel.transferTo(0L, channel.size(), channel2);
        } finally {
            fileInputStream.close();
            fileOutputStream.close();
            channel.close();
            channel2.close();
        }
    }

    private JSONObject c(File file, File file2) throws JSONException, C0111y, C0112z, IOException {
        if (file2.exists() && file2.isFile()) {
            throw new C0111y("Can't rename a file to a directory");
        }
        if (a(file.getAbsolutePath(), file2.getAbsolutePath())) {
            throw new C0111y("Can't copy itself into itself");
        }
        if (!file2.exists() && !file2.mkdir()) {
            throw new C0112z("Couldn't create the destination directory");
        }
        for (File file3 : file.listFiles()) {
            if (file3.isDirectory()) {
                c(file3, file2);
            } else {
                a(file3, new File(file2.getAbsoluteFile() + File.separator + file3.getName()));
            }
        }
        return getEntry(file2);
    }

    private static boolean a(String str, String str2) {
        return str2.startsWith(str) && str2.indexOf(File.separator, str.length() + (-1)) != -1;
    }

    private boolean a(File file) throws C0110x {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                a(file2);
            }
        }
        if (!file.delete()) {
            throw new C0110x("could not delete: " + file.getName());
        }
        return true;
    }

    private JSONObject a(String str, String str2, JSONObject jSONObject, boolean z) throws A, JSONException, IOException, C0109w, C0110x {
        boolean zOptBoolean;
        boolean zOptBoolean2 = false;
        if (jSONObject != null) {
            zOptBoolean = jSONObject.optBoolean("create");
            if (zOptBoolean) {
                zOptBoolean2 = jSONObject.optBoolean("exclusive");
            }
        } else {
            zOptBoolean = false;
        }
        if (str2.contains(":")) {
            throw new C0109w("This file has a : in it's name");
        }
        File file = str2.startsWith("/") ? new File(str2) : new File(String.valueOf(getRealPathFromURI(Uri.parse(str), this.cordova)) + File.separator + str2);
        if (zOptBoolean) {
            if (zOptBoolean2 && file.exists()) {
                throw new C0110x("create/exclusive fails");
            }
            if (z) {
                file.mkdir();
            } else {
                file.createNewFile();
            }
            if (!file.exists()) {
                throw new C0110x("create fails");
            }
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("path does not exist");
            }
            if (z) {
                if (file.isFile()) {
                    throw new A("path doesn't exist or is file");
                }
            } else if (file.isDirectory()) {
                throw new A("path doesn't exist or is directory");
            }
        }
        return getEntry(file);
    }

    private boolean c(String str) throws IllegalArgumentException {
        String realPathFromURI = getRealPathFromURI(Uri.parse(str), this.cordova);
        return realPathFromURI.equals(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/Android/data/").append(this.cordova.a().getPackageName()).append("/cache").toString()) || realPathFromURI.equals(Environment.getExternalStorageDirectory().getAbsolutePath()) || realPathFromURI.equals(new StringBuilder("/data/data/").append(this.cordova.a().getPackageName()).toString());
    }

    public static String stripFileProtocol(String str) {
        if (str.startsWith("file://")) {
            return str.substring(7);
        }
        return str;
    }

    private File d(String str) {
        return new File(getRealPathFromURI(Uri.parse(str), this.cordova));
    }

    public JSONObject getEntry(File file) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("isFile", file.isFile());
        jSONObject.put("isDirectory", file.isDirectory());
        jSONObject.put("name", file.getName());
        jSONObject.put("fullPath", "file://" + file.getAbsolutePath());
        return jSONObject;
    }

    private JSONObject e(String str) throws JSONException {
        return getEntry(new File(str));
    }

    public boolean isSynch(String str) {
        return str.equals("testSaveLocationExists") || str.equals("getFreeDiskSpace") || str.equals("testFileExists") || str.equals("testDirectoryExists");
    }

    public String readAsText(String str, String str2, int i, int i2) throws IOException {
        int i3 = i2 - i;
        byte[] bArr = new byte[1000];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(f(str), 1024);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (i > 0) {
            bufferedInputStream.skip(i);
        }
        while (i3 > 0) {
            int i4 = bufferedInputStream.read(bArr, 0, Math.min(1000, i3));
            if (i4 < 0) {
                break;
            }
            i3 -= i4;
            byteArrayOutputStream.write(bArr, 0, i4);
        }
        return new String(byteArrayOutputStream.toByteArray(), str2);
    }

    public String readAsDataURL(String str, int i, int i2) throws IOException {
        String mimeType;
        int i3 = i2 - i;
        byte[] bArr = new byte[1000];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(f(str), 1024);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (i > 0) {
            bufferedInputStream.skip(i);
        }
        while (i3 > 0) {
            int i4 = bufferedInputStream.read(bArr, 0, Math.min(1000, i3));
            if (i4 < 0) {
                break;
            }
            i3 -= i4;
            byteArrayOutputStream.write(bArr, 0, i4);
        }
        if (str.startsWith("content:")) {
            mimeType = this.cordova.a().getContentResolver().getType(Uri.parse(str));
        } else {
            mimeType = getMimeType(str);
        }
        return "data:" + mimeType + ";base64," + new String(W.a(byteArrayOutputStream.toByteArray(), true));
    }

    public static String getMimeType(String str) {
        if (str != null) {
            String lowerCase = str.replace(" ", "%20").toLowerCase();
            MimeTypeMap singleton = MimeTypeMap.getSingleton();
            String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(lowerCase);
            if (fileExtensionFromUrl.toLowerCase().equals("3ga")) {
                return "audio/3gpp";
            }
            return singleton.getMimeTypeFromExtension(fileExtensionFromUrl);
        }
        return "";
    }

    public long write(String str, String str2, int i) throws C0112z, IOException, IllegalArgumentException {
        boolean z;
        if (str.startsWith("content://")) {
            throw new C0112z("Couldn't write to file given its content URI");
        }
        String realPathFromURI = getRealPathFromURI(Uri.parse(str), this.cordova);
        if (i > 0) {
            a(realPathFromURI, i);
            z = true;
        } else {
            z = false;
        }
        byte[] bytes = str2.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        FileOutputStream fileOutputStream = new FileOutputStream(realPathFromURI, z);
        byte[] bArr = new byte[bytes.length];
        byteArrayInputStream.read(bArr, 0, bArr.length);
        fileOutputStream.write(bArr, 0, bytes.length);
        fileOutputStream.flush();
        fileOutputStream.close();
        return bytes.length;
    }

    private long a(String str, long j) throws C0112z, IOException {
        if (str.startsWith("content://")) {
            throw new C0112z("Couldn't truncate file given its content URI");
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(getRealPathFromURI(Uri.parse(str), this.cordova), "rw");
        try {
            if (randomAccessFile.length() >= j) {
                randomAccessFile.getChannel().truncate(j);
            } else {
                j = randomAccessFile.length();
            }
            return j;
        } finally {
            randomAccessFile.close();
        }
    }

    private InputStream f(String str) throws FileNotFoundException {
        if (str.startsWith("content")) {
            return this.cordova.a().getContentResolver().openInputStream(Uri.parse(str));
        }
        return new FileInputStream(getRealPathFromURI(Uri.parse(str), this.cordova));
    }

    protected static String getRealPathFromURI(Uri uri, InterfaceC0102p interfaceC0102p) throws IllegalArgumentException {
        String scheme = uri.getScheme();
        if (scheme == null) {
            return uri.toString();
        }
        if (scheme.compareTo("content") == 0) {
            Cursor cursorManagedQuery = interfaceC0102p.a().managedQuery(uri, new String[]{"_data"}, null, null, null);
            int columnIndexOrThrow = cursorManagedQuery.getColumnIndexOrThrow("_data");
            cursorManagedQuery.moveToFirst();
            return cursorManagedQuery.getString(columnIndexOrThrow);
        }
        if (scheme.compareTo("file") == 0) {
            return uri.getPath();
        }
        return uri.toString();
    }
}

package c;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import vpadn.C0086a;
import vpadn.C0093g;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0104r;
import vpadn.C0108v;
import vpadn.W;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CameraLauncher extends C0103q implements MediaScannerConnection.MediaScannerConnectionClient {
    private int a;
    private int b;

    /* renamed from: c, reason: collision with root package name */
    private int f157c;
    public C0101o callbackContext;
    private Uri d;
    private int e;
    private int f;
    private boolean g;
    private boolean h;
    private int i;
    private MediaScannerConnection j;
    private Uri k;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException {
        this.callbackContext = c0101o;
        if (!str.equals("takePicture")) {
            return false;
        }
        this.g = false;
        this.f157c = 0;
        this.b = 0;
        this.e = 0;
        this.f = 0;
        this.a = 80;
        this.a = jSONArray.getInt(0);
        int i = jSONArray.getInt(1);
        int i2 = jSONArray.getInt(2);
        this.b = jSONArray.getInt(3);
        this.f157c = jSONArray.getInt(4);
        this.e = jSONArray.getInt(5);
        this.f = jSONArray.getInt(6);
        this.h = jSONArray.getBoolean(8);
        this.g = jSONArray.getBoolean(9);
        if (this.b <= 0) {
            this.b = -1;
        }
        if (this.f157c <= 0) {
            this.f157c = -1;
        }
        if (i2 == 1) {
            takePicture(i, this.e);
        } else if (i2 == 0 || i2 == 2) {
            getImage(i2, i);
        }
        C0108v c0108v = new C0108v(C0108v.a.NO_RESULT);
        c0108v.a(true);
        c0101o.a(c0108v);
        return true;
    }

    public void takePicture(int i, int i2) {
        File file;
        this.i = b(b()).getCount();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (i2 == 0) {
            file = new File(C0086a.a(this.cordova.a()), ".Pic.jpg");
        } else {
            if (i2 != 1) {
                throw new IllegalArgumentException("Invalid Encoding Type: " + i2);
            }
            file = new File(C0086a.a(this.cordova.a()), ".Pic.png");
        }
        intent.putExtra("output", Uri.fromFile(file));
        this.d = Uri.fromFile(file);
        if (this.cordova != null) {
            this.cordova.a(this, intent, i + 32 + 1);
        }
    }

    public void getImage(int i, int i2) {
        Intent intent = new Intent();
        String str = "Get Picture";
        if (this.f == 0) {
            intent.setType("image/*");
        } else if (this.f == 1) {
            intent.setType("video/*");
            str = "Get Video";
        } else if (this.f == 2) {
            intent.setType("*/*");
            str = "Get All";
        }
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (this.cordova != null) {
            this.cordova.a(this, Intent.createChooser(intent, new String(str)), ((i + 1) * 16) + i2 + 1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x031c  */
    @Override // vpadn.C0103q
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onActivityResult(int r12, int r13, android.content.Intent r14) throws java.io.IOException, java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 806
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: c.CameraLauncher.onActivityResult(int, int, android.content.Intent):void");
    }

    private static Bitmap a(int i, Bitmap bitmap, C0093g c0093g) {
        Matrix matrix = new Matrix();
        if (i == 180) {
            matrix.setRotate(i);
        } else {
            matrix.setRotate(i, bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        c0093g.a = "1";
        return bitmapCreateBitmap;
    }

    private void a(Uri uri) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FileUtils.stripFileProtocol(this.d.toString()));
        OutputStream outputStreamOpenOutputStream = this.cordova.a().getContentResolver().openOutputStream(uri);
        byte[] bArr = new byte[4096];
        while (true) {
            int i = fileInputStream.read(bArr);
            if (i != -1) {
                outputStreamOpenOutputStream.write(bArr, 0, i);
            } else {
                outputStreamOpenOutputStream.flush();
                outputStreamOpenOutputStream.close();
                fileInputStream.close();
                return;
            }
        }
    }

    private Uri a() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mime_type", "image/jpeg");
        try {
            return this.cordova.a().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } catch (UnsupportedOperationException e) {
            C0104r.b("CameraLauncher", "Can't write to external media storage.");
            try {
                return this.cordova.a().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues);
            } catch (UnsupportedOperationException e2) {
                C0104r.b("CameraLauncher", "Can't write to internal media storage.");
                return null;
            }
        }
    }

    private Bitmap a(String str) {
        if (this.b <= 0 && this.f157c <= 0) {
            return BitmapFactory.decodeFile(str);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        if (options.outWidth == 0 || options.outHeight == 0) {
            return null;
        }
        int[] iArrCalculateAspectRatio = calculateAspectRatio(options.outWidth, options.outHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, this.b, this.f157c);
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str, options);
        if (bitmapDecodeFile != null) {
            return Bitmap.createScaledBitmap(bitmapDecodeFile, iArrCalculateAspectRatio[0], iArrCalculateAspectRatio[1], true);
        }
        return null;
    }

    public int[] calculateAspectRatio(int i, int i2) {
        int i3 = this.b;
        int i4 = this.f157c;
        if (i3 > 0 || i4 > 0) {
            if (i3 > 0 && i4 <= 0) {
                i2 = (i3 * i2) / i;
                i = i3;
            } else if (i3 <= 0 && i4 > 0) {
                i = (i4 * i) / i2;
                i2 = i4;
            } else {
                double d = i3 / i4;
                double d2 = i / i2;
                if (d2 > d) {
                    i2 = (i3 * i2) / i;
                    i = i3;
                } else if (d2 < d) {
                    i = (i4 * i) / i2;
                    i2 = i4;
                } else {
                    i2 = i4;
                    i = i3;
                }
            }
        }
        return new int[]{i, i2};
    }

    public static int calculateSampleSize(int i, int i2, int i3, int i4) {
        return ((float) i) / ((float) i2) > ((float) i3) / ((float) i4) ? i / i3 : i2 / i4;
    }

    private Cursor b(Uri uri) {
        return this.cordova.a().getContentResolver().query(uri, new String[]{"_id"}, null, null, null);
    }

    private void a(int i) {
        int i2 = 1;
        Uri uriB = b();
        Cursor cursorB = b(uriB);
        int count = cursorB.getCount();
        if (i == 1 && this.g) {
            i2 = 2;
        }
        if (count - this.i == i2) {
            cursorB.moveToLast();
            int iIntValue = Integer.valueOf(cursorB.getString(cursorB.getColumnIndex("_id"))).intValue();
            this.cordova.a().getContentResolver().delete(Uri.parse(uriB + "/" + (i2 == 2 ? iIntValue - 1 : iIntValue)), null, null);
        }
    }

    private static Uri b() {
        return Environment.getExternalStorageState().equals("mounted") ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    }

    public void processPicture(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, this.a, byteArrayOutputStream)) {
                this.callbackContext.a(new String(W.a(byteArrayOutputStream.toByteArray(), true)));
            }
        } catch (Exception e) {
            failPicture("Error compressing image.");
        }
    }

    public void failPicture(String str) {
        this.callbackContext.b(str);
    }

    @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
    public void onMediaScannerConnected() {
        try {
            this.j.scanFile(this.k.toString(), "image/*");
        } catch (IllegalStateException e) {
            C0104r.e("CameraLauncher", "Can't scan file in MediaScanner after taking picture");
        }
    }

    @Override // android.media.MediaScannerConnection.OnScanCompletedListener
    public void onScanCompleted(String str, Uri uri) {
        this.j.disconnect();
    }
}

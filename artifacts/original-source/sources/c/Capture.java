package c;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0086a;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0104r;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Capture extends C0103q {
    private C0101o a;
    private long b;

    /* renamed from: c, reason: collision with root package name */
    private double f158c;
    private JSONArray d;
    private int e;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException {
        JSONObject jSONObjectA;
        this.a = c0101o;
        this.b = 1L;
        this.f158c = 0.0d;
        this.d = new JSONArray();
        JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(0);
        if (jSONObjectOptJSONObject != null) {
            this.b = jSONObjectOptJSONObject.optLong("limit", 1L);
            this.f158c = jSONObjectOptJSONObject.optDouble("duration", 0.0d);
        }
        if (str.equals("getFormatData")) {
            String string = jSONArray.getString(0);
            String string2 = jSONArray.getString(1);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(FluctConstants.XML_NODE_HEIGHT, 0);
            jSONObject.put(FluctConstants.XML_NODE_WIDTH, 0);
            jSONObject.put("bitrate", 0);
            jSONObject.put("duration", 0);
            jSONObject.put("codecs", "");
            if (string2 == null || string2.equals("") || "null".equals(string2)) {
                string2 = FileUtils.getMimeType(string);
            }
            Log.d("Capture", "Mime type = " + string2);
            if (string2.equals("image/jpeg") || string.endsWith(".jpg")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(FileUtils.stripFileProtocol(string), options);
                jSONObject.put(FluctConstants.XML_NODE_HEIGHT, options.outHeight);
                jSONObject.put(FluctConstants.XML_NODE_WIDTH, options.outWidth);
                jSONObjectA = jSONObject;
            } else {
                jSONObjectA = string2.endsWith("audio/3gpp") ? a(string, jSONObject, false) : (string2.equals("video/3gpp") || string2.equals("video/mp4")) ? a(string, jSONObject, true) : jSONObject;
            }
            c0101o.a(jSONObjectA);
            return true;
        }
        if (str.equals("captureAudio")) {
            a();
        } else if (str.equals("captureImage")) {
            b();
        } else {
            if (!str.equals("captureVideo")) {
                return false;
            }
            double d = this.f158c;
            c();
        }
        return true;
    }

    private static JSONObject a(String str, JSONObject jSONObject, boolean z) throws IllegalStateException, JSONException, IOException, SecurityException, IllegalArgumentException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(str);
            mediaPlayer.prepare();
            jSONObject.put("duration", mediaPlayer.getDuration() / 1000);
            if (z) {
                jSONObject.put(FluctConstants.XML_NODE_HEIGHT, mediaPlayer.getVideoHeight());
                jSONObject.put(FluctConstants.XML_NODE_WIDTH, mediaPlayer.getVideoWidth());
            }
        } catch (IOException e) {
            Log.d("Capture", "Error: loading video file");
        }
        return jSONObject;
    }

    private void a() {
        this.cordova.a(this, new Intent("android.provider.MediaStore.RECORD_SOUND"), 0);
    }

    private void b() {
        this.e = b(d()).getCount();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", Uri.fromFile(new File(C0086a.a(this.cordova.a()), "Capture.jpg")));
        this.cordova.a(this, intent, 1);
    }

    private void c() {
        this.cordova.a(this, new Intent("android.media.action.VIDEO_CAPTURE"), 2);
    }

    @Override // vpadn.C0103q
    public void onActivityResult(int i, int i2, Intent intent) throws IOException {
        Uri uriInsert;
        if (i2 == -1) {
            if (i == 0) {
                this.d.put(a(intent.getData()));
                if (this.d.length() >= this.b) {
                    this.a.a(new C0108v(C0108v.a.OK, this.d));
                    return;
                } else {
                    a();
                    return;
                }
            }
            if (i == 1) {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("mime_type", "image/jpeg");
                    try {
                        uriInsert = this.cordova.a().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } catch (UnsupportedOperationException e) {
                        C0104r.b("Capture", "Can't write to external media storage.");
                        try {
                            uriInsert = this.cordova.a().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues);
                        } catch (UnsupportedOperationException e2) {
                            C0104r.b("Capture", "Can't write to internal media storage.");
                            fail(a(0, "Error capturing image - no media storage found."));
                            return;
                        }
                    }
                    FileInputStream fileInputStream = new FileInputStream(String.valueOf(C0086a.a(this.cordova.a())) + "/Capture.jpg");
                    OutputStream outputStreamOpenOutputStream = this.cordova.a().getContentResolver().openOutputStream(uriInsert);
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int i3 = fileInputStream.read(bArr);
                        if (i3 == -1) {
                            break;
                        } else {
                            outputStreamOpenOutputStream.write(bArr, 0, i3);
                        }
                    }
                    outputStreamOpenOutputStream.flush();
                    outputStreamOpenOutputStream.close();
                    fileInputStream.close();
                    this.d.put(a(uriInsert));
                    Uri uriD = d();
                    Cursor cursorB = b(uriD);
                    if (cursorB.getCount() - this.e == 2) {
                        cursorB.moveToLast();
                        this.cordova.a().getContentResolver().delete(Uri.parse(uriD + "/" + (Integer.valueOf(cursorB.getString(cursorB.getColumnIndex("_id"))).intValue() - 1)), null, null);
                    }
                    if (this.d.length() >= this.b) {
                        this.a.a(new C0108v(C0108v.a.OK, this.d));
                        return;
                    } else {
                        b();
                        return;
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    fail(a(0, "Error capturing image."));
                    return;
                }
            }
            if (i == 2) {
                this.d.put(a(intent.getData()));
                if (this.d.length() >= this.b) {
                    this.a.a(new C0108v(C0108v.a.OK, this.d));
                    return;
                } else {
                    double d = this.f158c;
                    c();
                    return;
                }
            }
            return;
        }
        if (i2 == 0) {
            if (this.d.length() > 0) {
                this.a.a(new C0108v(C0108v.a.OK, this.d));
                return;
            } else {
                fail(a(3, "Canceled."));
                return;
            }
        }
        if (this.d.length() > 0) {
            this.a.a(new C0108v(C0108v.a.OK, this.d));
        } else {
            fail(a(3, "Did not complete!"));
        }
    }

    private JSONObject a(Uri uri) throws JSONException {
        File file = new File(FileUtils.getRealPathFromURI(uri, this.cordova));
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", file.getName());
            jSONObject.put("fullPath", "file://" + file.getAbsolutePath());
            if (file.getAbsoluteFile().toString().endsWith(".3gp") || file.getAbsoluteFile().toString().endsWith(".3gpp")) {
                if (uri.toString().contains("/audio/")) {
                    jSONObject.put("type", "audio/3gpp");
                } else {
                    jSONObject.put("type", "video/3gpp");
                }
            } else {
                jSONObject.put("type", FileUtils.getMimeType(file.getAbsolutePath()));
            }
            jSONObject.put("lastModifiedDate", file.lastModified());
            jSONObject.put("size", file.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    private static JSONObject a(int i, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", i);
            jSONObject.put("message", str);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public void fail(JSONObject jSONObject) {
        this.a.b(jSONObject);
    }

    private Cursor b(Uri uri) {
        return this.cordova.a().getContentResolver().query(uri, new String[]{"_id"}, null, null, null);
    }

    private static Uri d() {
        return Environment.getExternalStorageState().equals("mounted") ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    }
}

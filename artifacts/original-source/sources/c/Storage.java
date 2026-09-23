package c;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0101o;
import vpadn.C0103q;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Storage extends C0103q {
    private SQLiteDatabase a = null;
    private String b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f183c = null;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException, SQLException {
        String[] strArr;
        if (str.equals("openDatabase")) {
            openDatabase(jSONArray.getString(0), jSONArray.getString(1), jSONArray.getString(2), jSONArray.getLong(3));
        } else {
            if (!str.equals("executeSql")) {
                return false;
            }
            if (jSONArray.isNull(1)) {
                strArr = new String[0];
            } else {
                JSONArray jSONArray2 = jSONArray.getJSONArray(1);
                int length = jSONArray2.length();
                strArr = new String[length];
                for (int i = 0; i < length; i++) {
                    strArr[i] = jSONArray2.getString(i);
                }
            }
            executeSql(jSONArray.getString(0), strArr, jSONArray.getString(2));
        }
        c0101o.b();
        return true;
    }

    @Override // vpadn.C0103q
    public void onDestroy() {
        if (this.a != null) {
            this.a.close();
            this.a = null;
        }
    }

    @Override // vpadn.C0103q
    public void onReset() {
        onDestroy();
    }

    public void openDatabase(String str, String str2, String str3, long j) {
        if (this.a != null) {
            this.a.close();
        }
        if (this.b == null) {
            this.b = this.cordova.a().getApplicationContext().getDir("database", 0).getPath();
        }
        this.f183c = String.valueOf(this.b) + File.separator + str + ".db";
        File file = new File(String.valueOf(this.b) + File.pathSeparator + str + ".db");
        if (file.exists()) {
            File file2 = new File(this.b);
            File file3 = new File(this.f183c);
            file2.mkdirs();
            file.renameTo(file3);
        }
        this.a = SQLiteDatabase.openOrCreateDatabase(this.f183c, (SQLiteDatabase.CursorFactory) null);
    }

    public void executeSql(String str, String[] strArr, String str2) throws JSONException, SQLException {
        try {
            String lowerCase = str.toLowerCase();
            if (lowerCase.startsWith("drop") || lowerCase.startsWith("create") || lowerCase.startsWith("alter") || lowerCase.startsWith("truncate")) {
                this.a.execSQL(str);
                this.webView.c("cordova.require('cordova/plugin/android/storage').completeQuery('" + str2 + "', '');");
            } else {
                Cursor cursorRawQuery = this.a.rawQuery(str, strArr);
                processResults(cursorRawQuery, str2);
                cursorRawQuery.close();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            System.out.println("Storage.executeSql(): Error=" + e.getMessage());
            this.webView.c("cordova.require('cordova/plugin/android/storage').failQuery('" + e.getMessage() + "','" + str2 + "');");
        }
    }

    public void processResults(Cursor cursor, String str) throws JSONException {
        String string = "[]";
        if (cursor.moveToFirst()) {
            JSONArray jSONArray = new JSONArray();
            int columnCount = cursor.getColumnCount();
            do {
                JSONObject jSONObject = new JSONObject();
                for (int i = 0; i < columnCount; i++) {
                    jSONObject.put(cursor.getColumnName(i), cursor.getString(i));
                }
                try {
                    jSONArray.put(jSONObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            string = jSONArray.toString();
        }
        this.webView.c("cordova.require('cordova/plugin/android/storage').completeQuery('" + str + "', " + string + ");");
    }
}

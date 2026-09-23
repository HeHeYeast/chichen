package jp.co.voyagegroup.android.fluct.jar.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctDbHelper extends SQLiteOpenHelper {
    private static final String CREATE_SQL = "fluctsdk_create.sql";
    private static final String DATABASE_NAME = "FluctSDK.sqlite";
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = "FluctDbHelper";
    private static final String UPDATE_SQL_V1_V2 = "fluctsdk_update_ver1_ver2.sql";
    private AssetManager mAssetManager;

    public FluctDbHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 2);
        Log.d(TAG, "FluctDbHelper : ");
        this.mAssetManager = context.getResources().getAssets();
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sqliteDatabase) throws Throwable {
        Log.d(TAG, "onCreate : ");
        try {
            String sqlString = readFile(this.mAssetManager.open(CREATE_SQL));
            for (String sql : sqlString.split(";")) {
                if (!sql.trim().equals("")) {
                    sqliteDatabase.execSQL(String.valueOf(sql) + ";");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate : Exception is " + e.getLocalizedMessage());
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion) throws Throwable {
        Log.d(TAG, "onUpgrade : old ver is " + oldVersion + " new ver is " + newVersion);
        try {
            String sqlString = readFile(this.mAssetManager.open(UPDATE_SQL_V1_V2));
            for (String sql : sqlString.split(";")) {
                if (!sql.trim().equals("")) {
                    sqliteDatabase.execSQL(String.valueOf(sql) + ";");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onUpgrade : Exception is " + e.getLocalizedMessage());
        }
    }

    private String readFile(InputStream inputStream) throws Throwable {
        Log.d(TAG, "readFile : ");
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "SJIS"));
            try {
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    String line = bufferedReader2.readLine();
                    if (line == null) {
                        break;
                    }
                    stringBuilder.append(String.valueOf(line) + "\n");
                }
                String string = stringBuilder.toString();
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
                return string;
            } catch (Throwable th) {
                th = th;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}

package jp.co.voyagegroup.android.fluct.jar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctDbAccess {
    private static final String CLOSE_IMAGE_TABLE_NAME = "tbl_close_image";
    private static final String CONFIG_TABLE_NAME = "tbl_config_info";
    private static final String CONVERSION_TABLE_NAME = "tbl_conversion_status";
    private static final String INTERSTITIAL_TABLE_NAME = "tbl_interstitial_info";
    private static final String LOADING_IMAGE_TABLE_NAME = "tbl_loading_image";
    private static final String TAG = "FluctDbAccess";

    private static synchronized int queryCount(SQLiteDatabase sqliteDatabase, String table, String selection, String[] selectionArgs) {
        int result;
        Log.d(TAG, "queryCount : table is " + table);
        result = 0;
        Cursor cursor = null;
        try {
            try {
                cursor = sqliteDatabase.query(table, new String[]{"count(1)"}, selection, selectionArgs, null, null, null);
                if (cursor.moveToFirst()) {
                    result = cursor.getInt(0);
                }
            } catch (Exception e) {
                Log.e(TAG, "queryCount : Exception is " + e.getLocalizedMessage());
                if (cursor != null) {
                    cursor.close();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public static synchronized void saveConfig(Context context, FluctSetting setting) {
        Log.d(TAG, "saveConfig : setting is " + setting);
        SQLiteDatabase sqliteDatabase = null;
        try {
            try {
                sqliteDatabase = new FluctDbHelper(context).getWritableDatabase();
                int count = queryCount(sqliteDatabase, CONFIG_TABLE_NAME, "mediaId like ?", new String[]{setting.getMediaId()});
                ContentValues values = new ContentValues();
                values.put("mediaId", setting.getMediaId());
                values.put("config", FluctUtils.objectToBytes(setting));
                values.put("update_time", Long.valueOf(System.currentTimeMillis()));
                if (count == 0) {
                    sqliteDatabase.insert(CONFIG_TABLE_NAME, null, values);
                } else {
                    sqliteDatabase.update(CONFIG_TABLE_NAME, values, "mediaId=?", new String[]{setting.getMediaId()});
                }
            } catch (Exception e) {
                Log.e(TAG, "saveConfig : Exception is " + e.getLocalizedMessage());
                if (sqliteDatabase != null) {
                    sqliteDatabase.close();
                }
            }
        } finally {
            if (sqliteDatabase != null) {
                sqliteDatabase.close();
            }
        }
    }

    public static synchronized FluctSetting getConfig(Context context, String mediaId) {
        FluctSetting result;
        Log.d(TAG, "getConfig : ");
        result = null;
        Cursor curor = null;
        SQLiteDatabase sqliteDatabase = null;
        try {
            try {
                sqliteDatabase = new FluctDbHelper(context).getReadableDatabase();
                curor = sqliteDatabase.query(CONFIG_TABLE_NAME, new String[]{"mediaId", "config"}, "mediaId=?", new String[]{mediaId}, null, null, null);
                if (curor.moveToFirst()) {
                    result = (FluctSetting) FluctUtils.bytesToObject(curor.getBlob(curor.getColumnIndex("config")));
                    Log.v(TAG, "getConfig : setting is " + result);
                }
            } finally {
                if (0 != 0) {
                    curor.close();
                }
                if (0 != 0) {
                    sqliteDatabase.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getConfig : Exception is " + e.getLocalizedMessage());
            if (curor != null) {
                curor.close();
            }
            if (sqliteDatabase != null) {
                sqliteDatabase.close();
            }
        }
        return result;
    }

    public static synchronized void setConversionFlag(Context context, int flag) {
        Log.d(TAG, "setConversionFlag : flag is " + flag);
        SQLiteDatabase sqliteDatabase = null;
        try {
            try {
                sqliteDatabase = new FluctDbHelper(context).getWritableDatabase();
                int count = queryCount(sqliteDatabase, CONVERSION_TABLE_NAME, "1=1", null);
                if (count == 0) {
                    ContentValues values = new ContentValues();
                    values.put("status", Integer.valueOf(flag));
                    values.put("update_time", Long.valueOf(System.currentTimeMillis()));
                    sqliteDatabase.insert(CONVERSION_TABLE_NAME, null, values);
                }
            } finally {
                if (0 != 0) {
                    sqliteDatabase.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "setConversionFlag : Exception is " + e.getLocalizedMessage());
            if (sqliteDatabase != null) {
                sqliteDatabase.close();
            }
        }
    }

    public static synchronized int getConversionFlag(Context context) {
        int result;
        Log.d(TAG, "getConversionFlag : ");
        result = 0;
        Cursor cursor = null;
        SQLiteDatabase sqliteDatabase = null;
        try {
            try {
                sqliteDatabase = new FluctDbHelper(context).getReadableDatabase();
                cursor = sqliteDatabase.query(CONVERSION_TABLE_NAME, new String[]{"id,status"}, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    result = cursor.getInt(1);
                    Log.v(TAG, "getConversionFlag : conversionFlag is " + result);
                }
            } finally {
                if (0 != 0) {
                    cursor.close();
                }
                if (0 != 0) {
                    sqliteDatabase.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getConversionFlag : Exception is " + e.getLocalizedMessage());
            if (cursor != null) {
                cursor.close();
            }
            if (sqliteDatabase != null) {
                sqliteDatabase.close();
            }
        }
        return result;
    }

    public static synchronized void setInterstitial(Context context, FluctInterstitialTable interstitial) {
        Log.d(TAG, "setInterstitial : ");
        SQLiteDatabase database = null;
        try {
            try {
                database = new FluctDbHelper(context).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("media_id", interstitial.getMediaId());
                values.put("rate", Integer.valueOf(interstitial.getRate()));
                values.put(FluctConstants.XML_NODE_WIDTH, Integer.valueOf(interstitial.getWidth()));
                values.put(FluctConstants.XML_NODE_HEIGHT, Integer.valueOf(interstitial.getHeight()));
                values.put("adhtml", interstitial.getAdHtml());
                values.put("update_time", Integer.valueOf(interstitial.getUpdateTime()));
                int count = queryCount(database, INTERSTITIAL_TABLE_NAME, "media_id = ?", new String[]{interstitial.getMediaId()});
                if (count == 0) {
                    database.insert(INTERSTITIAL_TABLE_NAME, null, values);
                } else {
                    database.update(INTERSTITIAL_TABLE_NAME, values, "media_id = ?", new String[]{interstitial.getMediaId()});
                }
            } catch (Exception e) {
                Log.e(TAG, "setInterstitial : Exception is " + e.getLocalizedMessage());
                if (database != null) {
                    database.close();
                }
            }
            Log.v(TAG, "setInterstitial : end");
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public static synchronized FluctInterstitialTable getInterstitial(Context context, String mediaId) {
        FluctInterstitialTable result;
        Log.d(TAG, "getInterstitial : MediaId is " + mediaId);
        result = null;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            try {
                database = new FluctDbHelper(context).getReadableDatabase();
                cursor = database.query(INTERSTITIAL_TABLE_NAME, new String[]{"id, media_id, rate, width, height, adhtml, update_time"}, "media_id = ?", new String[]{mediaId}, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    FluctInterstitialTable result2 = new FluctInterstitialTable();
                    try {
                        result2.setRate(cursor.getInt(cursor.getColumnIndex("rate")));
                        result2.setWidth(cursor.getInt(cursor.getColumnIndex(FluctConstants.XML_NODE_WIDTH)));
                        result2.setHeight(cursor.getInt(cursor.getColumnIndex(FluctConstants.XML_NODE_HEIGHT)));
                        result2.setAdHtml(cursor.getString(cursor.getColumnIndex("adhtml")));
                        result2.setUpdateTime(cursor.getInt(cursor.getColumnIndex("update_time")));
                        result = result2;
                    } catch (Exception e) {
                        e = e;
                        result = result2;
                        Log.e(TAG, "getInterstitial : Exception is " + e.getLocalizedMessage());
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (database != null) {
                            database.close();
                        }
                        return result;
                    } catch (Throwable th) {
                        th = th;
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (database != null) {
                            database.close();
                        }
                        throw th;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (database != null) {
                    database.close();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
        return result;
    }

    public static synchronized boolean checkInterstitialData(Context context, String mediaId) {
        boolean result;
        Log.d(TAG, "checkInterstitialData : MediaId is " + mediaId);
        result = false;
        SQLiteDatabase database = null;
        try {
            try {
                database = new FluctDbHelper(context).getReadableDatabase();
                int count = queryCount(database, INTERSTITIAL_TABLE_NAME, "media_id = ?", new String[]{mediaId});
                if (count != 0) {
                    result = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "checkInterstitialData : Exception is " + e.getLocalizedMessage());
                if (database != null) {
                    database.close();
                }
            }
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return result;
    }

    public static synchronized void deleteInterstitialData(Context context, String mediaId) {
        Log.d(TAG, "deleteInterstitialData : MediaId is " + mediaId);
        SQLiteDatabase database = null;
        try {
            try {
                database = new FluctDbHelper(context).getReadableDatabase();
                database.delete(INTERSTITIAL_TABLE_NAME, "media_id = ?", new String[]{mediaId});
            } finally {
                if (database != null) {
                    database.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deleteInterstitialData : Exception is " + e.getLocalizedMessage());
            if (database != null) {
                database.close();
            }
        }
    }

    public static synchronized byte[] getCloseButtonImage(Context context) {
        byte[] result;
        Log.d(TAG, "getCloseButtonImage : ");
        result = null;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            try {
                database = new FluctDbHelper(context).getReadableDatabase();
                cursor = database.query(CLOSE_IMAGE_TABLE_NAME, new String[]{"id", "close_image"}, null, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    result = cursor.getBlob(cursor.getColumnIndex("close_image"));
                }
            } catch (Exception e) {
                Log.e(TAG, "getCloseButtonImage : Exception is " + e.getLocalizedMessage());
                if (cursor != null) {
                    cursor.close();
                }
                if (database != null) {
                    database.close();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
        return result;
    }

    public static synchronized ArrayList<byte[]> getLoadingImage(Context context) {
        ArrayList<byte[]> result;
        Log.d(TAG, "getLoadingImage : ");
        result = null;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            try {
                database = new FluctDbHelper(context).getReadableDatabase();
                cursor = database.query(LOADING_IMAGE_TABLE_NAME, new String[]{"id", "loading"}, null, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    int count = cursor.getCount();
                    cursor.moveToFirst();
                    ArrayList<byte[]> result2 = new ArrayList<>();
                    for (int loop = 0; loop < count; loop++) {
                        try {
                            result2.add(cursor.getBlob(cursor.getColumnIndex("loading")));
                            cursor.moveToNext();
                        } catch (Exception e) {
                            e = e;
                            result = result2;
                            Log.e(TAG, "getLoadingImage : Exception is " + e.getLocalizedMessage());
                            if (cursor != null) {
                                cursor.close();
                            }
                            if (database != null) {
                                database.close();
                            }
                            return result;
                        } catch (Throwable th) {
                            th = th;
                            if (cursor != null) {
                                cursor.close();
                            }
                            if (database != null) {
                                database.close();
                            }
                            throw th;
                        }
                    }
                    result = result2;
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (database != null) {
                    database.close();
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        return result;
    }
}

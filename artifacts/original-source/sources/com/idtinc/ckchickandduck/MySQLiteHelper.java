package com.idtinc.ckchickandduck;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "com_idt_chick_kitchen.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.db = getWritableDatabase();
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL("create table timeSaveDictionary (_JSON LongText);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS config");
        onCreate(db);
    }
}

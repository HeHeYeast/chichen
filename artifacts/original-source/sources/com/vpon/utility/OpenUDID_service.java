package com.vpon.utility;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class OpenUDID_service extends Service {
    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new Binder() { // from class: com.vpon.utility.OpenUDID_service.1
            @Override // android.os.Binder
            public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
                SharedPreferences sharedPreferences = OpenUDID_service.this.getSharedPreferences("openudid_prefs", 0);
                parcel2.writeInt(parcel.readInt());
                parcel2.writeString(sharedPreferences.getString("openudid", null));
                return true;
            }
        };
    }
}

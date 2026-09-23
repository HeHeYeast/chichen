package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class c implements Parcelable.Creator<PlayerEntity> {
    static void a(PlayerEntity playerEntity, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, playerEntity.getPlayerId(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, playerEntity.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, playerEntity.getDisplayName(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) playerEntity.getIconImageUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) playerEntity.getHiResImageUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, playerEntity.getRetrievedTimestamp());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: L, reason: merged with bridge method [inline-methods] */
    public PlayerEntity[] newArray(int i) {
        return new PlayerEntity[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: u, reason: merged with bridge method [inline-methods] */
    public PlayerEntity createFromParcel(Parcel parcel) {
        Uri uri = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        long jG = 0;
        Uri uri2 = null;
        String strL = null;
        String strL2 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    uri2 = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, Uri.CREATOR);
                    break;
                case 4:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, Uri.CREATOR);
                    break;
                case 5:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 1000:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new PlayerEntity(iF, strL2, strL, uri2, uri, jG);
    }
}

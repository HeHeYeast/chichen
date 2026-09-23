package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class TileCreator implements Parcelable.Creator<Tile> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(Tile tile, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, tile.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 2, tile.width);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, tile.height);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, tile.data, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Tile createFromParcel(Parcel parcel) {
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        byte[] bArrO = null;
        int iF2 = 0;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 3:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 4:
                    bArrO = com.google.android.gms.common.internal.safeparcel.a.o(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new Tile(iF3, iF2, iF, bArrO);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Tile[] newArray(int size) {
        return new Tile[size];
    }
}

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ed implements Parcelable.Creator<ec> {
    static void a(ec ecVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, ecVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, ecVar.bH(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) ecVar.bI(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: G, reason: merged with bridge method [inline-methods] */
    public ec[] newArray(int i) {
        return new ec[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: s, reason: merged with bridge method [inline-methods] */
    public ec createFromParcel(Parcel parcel) {
        dz dzVar = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        Parcel parcelY = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    parcelY = com.google.android.gms.common.internal.safeparcel.a.y(parcel, i);
                    break;
                case 3:
                    dzVar = (dz) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, dz.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new ec(iF, parcelY, dzVar);
    }
}

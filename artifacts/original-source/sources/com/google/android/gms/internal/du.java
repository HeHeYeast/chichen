package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dt;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class du implements Parcelable.Creator<dt> {
    static void a(dt dtVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, dtVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, dtVar.bm(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: A, reason: merged with bridge method [inline-methods] */
    public dt[] newArray(int i) {
        return new dt[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public dt createFromParcel(Parcel parcel) {
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        ArrayList arrayListC = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, dt.a.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new dt(iF, arrayListC);
    }
}

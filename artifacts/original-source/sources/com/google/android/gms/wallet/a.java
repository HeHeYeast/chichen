package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class a implements Parcelable.Creator<Address> {
    static void a(Address address, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, address.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, address.name, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, address.tu, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, address.tv, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, address.tw, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, address.hl, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, address.tx, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, address.ty, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, address.tz, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, address.tA, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, address.tB);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 12, address.tC, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: N, reason: merged with bridge method [inline-methods] */
    public Address createFromParcel(Parcel parcel) {
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL = null;
        String strL2 = null;
        String strL3 = null;
        String strL4 = null;
        String strL5 = null;
        String strL6 = null;
        String strL7 = null;
        String strL8 = null;
        String strL9 = null;
        boolean zC = false;
        String strL10 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 4:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 5:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 6:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 7:
                    strL6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 8:
                    strL7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 9:
                    strL8 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 10:
                    strL9 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 11:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 12:
                    strL10 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new Address(iF, strL, strL2, strL3, strL4, strL5, strL6, strL7, strL8, strL9, zC, strL10);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: as, reason: merged with bridge method [inline-methods] */
    public Address[] newArray(int i) {
        return new Address[i];
    }
}

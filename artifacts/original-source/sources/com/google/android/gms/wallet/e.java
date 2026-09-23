package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class e implements Parcelable.Creator<LineItem> {
    static void a(LineItem lineItem, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, lineItem.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, lineItem.description, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, lineItem.tQ, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, lineItem.tR, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, lineItem.tD, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, lineItem.tS);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, lineItem.tE, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: R, reason: merged with bridge method [inline-methods] */
    public LineItem createFromParcel(Parcel parcel) {
        int iF = 0;
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String strL2 = null;
        String strL3 = null;
        String strL4 = null;
        String strL5 = null;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 4:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 5:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 6:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 7:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new LineItem(iF2, strL5, strL4, strL3, strL2, iF, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aw, reason: merged with bridge method [inline-methods] */
    public LineItem[] newArray(int i) {
        return new LineItem[i];
    }
}

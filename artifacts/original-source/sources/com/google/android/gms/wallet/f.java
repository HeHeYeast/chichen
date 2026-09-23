package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class f implements Parcelable.Creator<LoyaltyWalletObject> {
    static void a(LoyaltyWalletObject loyaltyWalletObject, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, loyaltyWalletObject.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, loyaltyWalletObject.tU, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, loyaltyWalletObject.tV, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, loyaltyWalletObject.tW, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, loyaltyWalletObject.tX, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, loyaltyWalletObject.tY, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: S, reason: merged with bridge method [inline-methods] */
    public LoyaltyWalletObject createFromParcel(Parcel parcel) {
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL2 = null;
        String strL3 = null;
        String strL4 = null;
        String strL5 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
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
        return new LoyaltyWalletObject(iF, strL5, strL4, strL3, strL2, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ax, reason: merged with bridge method [inline-methods] */
    public LoyaltyWalletObject[] newArray(int i) {
        return new LoyaltyWalletObject[i];
    }
}

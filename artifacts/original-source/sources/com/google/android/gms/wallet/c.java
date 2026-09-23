package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class c implements Parcelable.Creator<FullWallet> {
    static void a(FullWallet fullWallet, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fullWallet.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fullWallet.tH, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fullWallet.tI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) fullWallet.tJ, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fullWallet.tK, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) fullWallet.tL, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) fullWallet.tM, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fullWallet.tN, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: P, reason: merged with bridge method [inline-methods] */
    public FullWallet createFromParcel(Parcel parcel) {
        String[] strArrW = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        Address address = null;
        Address address2 = null;
        String strL = null;
        ProxyCard proxyCard = null;
        String strL2 = null;
        String strL3 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 4:
                    proxyCard = (ProxyCard) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, ProxyCard.CREATOR);
                    break;
                case 5:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 6:
                    address2 = (Address) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, Address.CREATOR);
                    break;
                case 7:
                    address = (Address) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, Address.CREATOR);
                    break;
                case 8:
                    strArrW = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new FullWallet(iF, strL3, strL2, proxyCard, strL, address2, address, strArrW);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: au, reason: merged with bridge method [inline-methods] */
    public FullWallet[] newArray(int i) {
        return new FullWallet[i];
    }
}

package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class g implements Parcelable.Creator<MaskedWallet> {
    static void a(MaskedWallet maskedWallet, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, maskedWallet.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, maskedWallet.tH, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, maskedWallet.tI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, maskedWallet.tN, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, maskedWallet.tK, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) maskedWallet.tL, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) maskedWallet.tM, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, (Parcelable[]) maskedWallet.tZ, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, (Parcelable[]) maskedWallet.ua, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: T, reason: merged with bridge method [inline-methods] */
    public MaskedWallet createFromParcel(Parcel parcel) {
        OfferWalletObject[] offerWalletObjectArr = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        LoyaltyWalletObject[] loyaltyWalletObjectArr = null;
        Address address = null;
        Address address2 = null;
        String strL = null;
        String[] strArrW = null;
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
                    strArrW = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i);
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
                    loyaltyWalletObjectArr = (LoyaltyWalletObject[]) com.google.android.gms.common.internal.safeparcel.a.b(parcel, i, LoyaltyWalletObject.CREATOR);
                    break;
                case 9:
                    offerWalletObjectArr = (OfferWalletObject[]) com.google.android.gms.common.internal.safeparcel.a.b(parcel, i, OfferWalletObject.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new MaskedWallet(iF, strL3, strL2, strArrW, strL, address2, address, loyaltyWalletObjectArr, offerWalletObjectArr);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ay, reason: merged with bridge method [inline-methods] */
    public MaskedWallet[] newArray(int i) {
        return new MaskedWallet[i];
    }
}

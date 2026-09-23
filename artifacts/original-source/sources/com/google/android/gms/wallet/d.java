package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class d implements Parcelable.Creator<FullWalletRequest> {
    static void a(FullWalletRequest fullWalletRequest, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fullWalletRequest.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fullWalletRequest.tH, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fullWalletRequest.tI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) fullWalletRequest.tO, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: Q, reason: merged with bridge method [inline-methods] */
    public FullWalletRequest createFromParcel(Parcel parcel) {
        Cart cart = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL = null;
        String strL2 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 4:
                    cart = (Cart) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, Cart.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new FullWalletRequest(iF, strL2, strL, cart);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: av, reason: merged with bridge method [inline-methods] */
    public FullWalletRequest[] newArray(int i) {
        return new FullWalletRequest[i];
    }
}

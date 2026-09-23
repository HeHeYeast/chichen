package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class h implements Parcelable.Creator<MaskedWalletRequest> {
    static void a(MaskedWalletRequest maskedWalletRequest, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, maskedWalletRequest.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, maskedWalletRequest.tI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, maskedWalletRequest.ub);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, maskedWalletRequest.uc);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, maskedWalletRequest.ud);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, maskedWalletRequest.ue, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, maskedWalletRequest.tE, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, maskedWalletRequest.uf, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, (Parcelable) maskedWalletRequest.tO, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, maskedWalletRequest.ug);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, maskedWalletRequest.uh);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: U, reason: merged with bridge method [inline-methods] */
    public MaskedWalletRequest createFromParcel(Parcel parcel) {
        Cart cart = null;
        boolean zC = false;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        boolean zC2 = false;
        String strL = null;
        String strL2 = null;
        String strL3 = null;
        boolean zC3 = false;
        boolean zC4 = false;
        boolean zC5 = false;
        String strL4 = null;
        int iF = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    zC5 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 4:
                    zC4 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 5:
                    zC3 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 6:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 7:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 8:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 9:
                    cart = (Cart) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, Cart.CREATOR);
                    break;
                case 10:
                    zC2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 11:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new MaskedWalletRequest(iF, strL4, zC5, zC4, zC3, strL3, strL2, strL, cart, zC2, zC);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: az, reason: merged with bridge method [inline-methods] */
    public MaskedWalletRequest[] newArray(int i) {
        return new MaskedWalletRequest[i];
    }
}

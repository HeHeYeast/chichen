package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class k implements Parcelable.Creator<ProxyCard> {
    static void a(ProxyCard proxyCard, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, proxyCard.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, proxyCard.um, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, proxyCard.un, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, proxyCard.uo);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, proxyCard.up);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public ProxyCard createFromParcel(Parcel parcel) {
        String strL = null;
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF2 = 0;
        String strL2 = null;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 4:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new ProxyCard(iF3, strL2, strL, iF2, iF);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aC, reason: merged with bridge method [inline-methods] */
    public ProxyCard[] newArray(int i) {
        return new ProxyCard[i];
    }
}

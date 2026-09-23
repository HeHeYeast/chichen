package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class b implements Parcelable.Creator<Cart> {
    static void a(Cart cart, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, cart.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, cart.tD, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, cart.tE, false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 4, cart.tF, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: O, reason: merged with bridge method [inline-methods] */
    public Cart createFromParcel(Parcel parcel) {
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        ArrayList arrayList = new ArrayList();
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
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, LineItem.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new Cart(iF, strL2, strL, arrayList);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: at, reason: merged with bridge method [inline-methods] */
    public Cart[] newArray(int i) {
        return new Cart[i];
    }
}

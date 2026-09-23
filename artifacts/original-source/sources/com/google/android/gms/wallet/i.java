package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class i implements Parcelable.Creator<NotifyTransactionStatusRequest> {
    static void a(NotifyTransactionStatusRequest notifyTransactionStatusRequest, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, notifyTransactionStatusRequest.iM);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, notifyTransactionStatusRequest.tH, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, notifyTransactionStatusRequest.status);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, notifyTransactionStatusRequest.uj, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: V, reason: merged with bridge method [inline-methods] */
    public NotifyTransactionStatusRequest createFromParcel(Parcel parcel) {
        String strL = null;
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String strL2 = null;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 4:
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
        return new NotifyTransactionStatusRequest(iF2, strL2, iF, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aA, reason: merged with bridge method [inline-methods] */
    public NotifyTransactionStatusRequest[] newArray(int i) {
        return new NotifyTransactionStatusRequest[i];
    }
}

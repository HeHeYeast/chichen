package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class b implements Parcelable.Creator<RoomEntity> {
    static void a(RoomEntity roomEntity, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, roomEntity.getRoomId(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, roomEntity.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, roomEntity.getCreatorId(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, roomEntity.getCreationTimestamp());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, roomEntity.getStatus());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, roomEntity.getDescription(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, roomEntity.getVariant());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, roomEntity.getAutoMatchCriteria(), false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 8, roomEntity.getParticipants(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 9, roomEntity.getAutoMatchWaitEstimateSeconds());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: V, reason: merged with bridge method [inline-methods] */
    public RoomEntity[] newArray(int i) {
        return new RoomEntity[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: y */
    public RoomEntity createFromParcel(Parcel parcel) {
        int iF = 0;
        ArrayList arrayListC = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        long jG = 0;
        Bundle bundleN = null;
        int iF2 = 0;
        String strL = null;
        int iF3 = 0;
        String strL2 = null;
        String strL3 = null;
        int iF4 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 2:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 4:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 6:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 7:
                    bundleN = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i);
                    break;
                case 8:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, ParticipantEntity.CREATOR);
                    break;
                case 9:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 1000:
                    iF4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new RoomEntity(iF4, strL3, strL2, jG, iF3, strL, iF2, bundleN, arrayListC, iF);
    }
}

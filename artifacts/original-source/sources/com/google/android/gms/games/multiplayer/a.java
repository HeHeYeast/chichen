package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.games.GameEntity;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class a implements Parcelable.Creator<InvitationEntity> {
    static void a(InvitationEntity invitationEntity, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, (Parcelable) invitationEntity.getGame(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, invitationEntity.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, invitationEntity.getInvitationId(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, invitationEntity.getCreationTimestamp());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, invitationEntity.ch());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, (Parcelable) invitationEntity.getInviter(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 6, invitationEntity.getParticipants(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, invitationEntity.getVariant());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: S, reason: merged with bridge method [inline-methods] */
    public InvitationEntity[] newArray(int i) {
        return new InvitationEntity[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: v, reason: merged with bridge method [inline-methods] */
    public InvitationEntity createFromParcel(Parcel parcel) {
        int iF = 0;
        ArrayList arrayListC = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        long jG = 0;
        ParticipantEntity participantEntity = null;
        int iF2 = 0;
        String strL = null;
        GameEntity gameEntity = null;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    gameEntity = (GameEntity) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, GameEntity.CREATOR);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 4:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    participantEntity = (ParticipantEntity) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, ParticipantEntity.CREATOR);
                    break;
                case 6:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, ParticipantEntity.CREATOR);
                    break;
                case 7:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 1000:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new InvitationEntity(iF3, gameEntity, strL, jG, iF2, participantEntity, arrayListC, iF);
    }
}

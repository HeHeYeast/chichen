package com.google.android.gms.auth;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class RecoveryDecisionCreator implements Parcelable.Creator<RecoveryDecision> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(RecoveryDecision recoveryDecision, Parcel parcel, int i) {
        int iK = b.k(parcel);
        b.c(parcel, 1, recoveryDecision.iM);
        b.a(parcel, 2, (Parcelable) recoveryDecision.recoveryIntent, i, false);
        b.a(parcel, 3, recoveryDecision.showRecoveryInterstitial);
        b.a(parcel, 4, recoveryDecision.isRecoveryInfoNeeded);
        b.a(parcel, 5, recoveryDecision.isRecoveryInterstitialAllowed);
        b.a(parcel, 6, (Parcelable) recoveryDecision.recoveryIntentWithoutIntro, i, false);
        b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public RecoveryDecision createFromParcel(Parcel parcel) {
        PendingIntent pendingIntent = null;
        boolean zC = false;
        int iJ = a.j(parcel);
        boolean zC2 = false;
        boolean zC3 = false;
        PendingIntent pendingIntent2 = null;
        int iF = 0;
        while (parcel.dataPosition() < iJ) {
            int i = a.i(parcel);
            switch (a.y(i)) {
                case 1:
                    iF = a.f(parcel, i);
                    break;
                case 2:
                    pendingIntent2 = (PendingIntent) a.a(parcel, i, PendingIntent.CREATOR);
                    break;
                case 3:
                    zC3 = a.c(parcel, i);
                    break;
                case 4:
                    zC2 = a.c(parcel, i);
                    break;
                case 5:
                    zC = a.c(parcel, i);
                    break;
                case 6:
                    pendingIntent = (PendingIntent) a.a(parcel, i, PendingIntent.CREATOR);
                    break;
                default:
                    a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new RecoveryDecision(iF, pendingIntent2, zC3, zC2, zC, pendingIntent);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public RecoveryDecision[] newArray(int size) {
        return new RecoveryDecision[size];
    }
}

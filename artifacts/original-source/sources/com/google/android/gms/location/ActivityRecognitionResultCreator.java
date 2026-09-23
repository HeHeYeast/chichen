package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ActivityRecognitionResultCreator implements Parcelable.Creator<ActivityRecognitionResult> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(ActivityRecognitionResult activityRecognitionResult, Parcel parcel, int i) {
        int iK = b.k(parcel);
        b.b(parcel, 1, activityRecognitionResult.ov, false);
        b.c(parcel, 1000, activityRecognitionResult.getVersionCode());
        b.a(parcel, 2, activityRecognitionResult.ow);
        b.a(parcel, 3, activityRecognitionResult.ox);
        b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ActivityRecognitionResult createFromParcel(Parcel parcel) {
        long jG = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        ArrayList arrayListC = null;
        long jG2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, DetectedActivity.CREATOR);
                    break;
                case 2:
                    jG2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 3:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 1000:
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
        return new ActivityRecognitionResult(iF, arrayListC, jG2, jG);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ActivityRecognitionResult[] newArray(int size) {
        return new ActivityRecognitionResult[size];
    }
}

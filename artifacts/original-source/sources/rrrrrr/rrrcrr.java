package rrrrrr;

import android.os.Parcel;
import android.os.Parcelable;
import com.immersion.hapticmediasdk.models.HapticFileInformation;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class rrrcrr implements Parcelable.Creator {

    /* renamed from: b0446ц04460446ц0446, reason: contains not printable characters */
    public static int f152b0446044604460446 = 0;

    /* renamed from: bц044604460446ц0446, reason: contains not printable characters */
    public static int f153b0446044604460446 = 1;

    /* renamed from: bц0446цц04460446, reason: contains not printable characters */
    public static int f154b044604460446 = 2;

    /* renamed from: bцц04460446ц0446, reason: contains not printable characters */
    public static int f155b044604460446 = 84;

    public rrrcrr() throws Exception {
        if (((f155b044604460446 + f153b0446044604460446) * f155b044604460446) % m140b04460446044604460446() != f152b0446044604460446) {
            f155b044604460446 = m143b04460446();
            f152b0446044604460446 = m143b04460446();
        }
        try {
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: b0446044604460446ц0446, reason: contains not printable characters */
    public static int m140b04460446044604460446() {
        return 2;
    }

    /* renamed from: b04460446цц04460446, reason: contains not printable characters */
    public static int m141b0446044604460446() {
        return 0;
    }

    /* renamed from: b0446ццц04460446, reason: contains not printable characters */
    public static int m142b044604460446() {
        return 1;
    }

    /* renamed from: bцццц04460446, reason: contains not printable characters */
    public static int m143b04460446() {
        return 54;
    }

    @Override // android.os.Parcelable.Creator
    public HapticFileInformation createFromParcel(Parcel parcel) {
        if (((m143b04460446() + m142b044604460446()) * m143b04460446()) % f154b044604460446 != f152b0446044604460446) {
            f155b044604460446 = m143b04460446();
            f152b0446044604460446 = m143b04460446();
        }
        HapticFileInformation hapticFileInformation = new HapticFileInformation(parcel);
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        return hapticFileInformation;
    }

    @Override // android.os.Parcelable.Creator
    public /* synthetic */ Object createFromParcel(Parcel parcel) throws Exception {
        try {
            HapticFileInformation hapticFileInformationCreateFromParcel = createFromParcel(parcel);
            if (((f155b044604460446 + f153b0446044604460446) * f155b044604460446) % m140b04460446044604460446() != m141b0446044604460446()) {
                f155b044604460446 = 46;
                f152b0446044604460446 = m143b04460446();
            }
            return hapticFileInformationCreateFromParcel;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override // android.os.Parcelable.Creator
    public HapticFileInformation[] newArray(int i) {
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        HapticFileInformation[] hapticFileInformationArr = new HapticFileInformation[i];
        if (((f155b044604460446 + f153b0446044604460446) * f155b044604460446) % f154b044604460446 != f152b0446044604460446) {
            f155b044604460446 = 32;
            f152b0446044604460446 = 80;
        }
        return hapticFileInformationArr;
    }

    @Override // android.os.Parcelable.Creator
    public /* synthetic */ Object[] newArray(int i) {
        int i2 = f155b044604460446;
        switch ((i2 * (f153b0446044604460446 + i2)) % f154b044604460446) {
            case 0:
                break;
            default:
                f155b044604460446 = m143b04460446();
                f152b0446044604460446 = 47;
                break;
        }
        HapticFileInformation[] hapticFileInformationArrNewArray = newArray(i);
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        return hapticFileInformationArrNewArray;
    }
}

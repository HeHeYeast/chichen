package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;
import com.google.android.gms.internal.aw;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface ad extends IInterface {

    public static abstract class a extends Binder implements ad {

        /* renamed from: com.google.android.gms.internal.ad$a$a, reason: collision with other inner class name */
        private static class C0009a implements ad {
            private IBinder dG;

            C0009a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.internal.ad
            public IBinder a(com.google.android.gms.dynamic.b bVar, x xVar, String str, aw awVar, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    parcelObtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    if (xVar != null) {
                        parcelObtain.writeInt(1);
                        xVar.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(awVar != null ? awVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    this.dG.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readStrongBinder();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.dG;
            }
        }

        public static ad g(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof ad)) ? new C0009a(iBinder) : (ad) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    IBinder iBinderA = a(b.a.z(data.readStrongBinder()), data.readInt() != 0 ? x.CREATOR.createFromParcel(data) : null, data.readString(), aw.a.i(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(iBinderA);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IBinder a(com.google.android.gms.dynamic.b bVar, x xVar, String str, aw awVar, int i) throws RemoteException;
}

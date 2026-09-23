package com.google.android.gms.maps.model.internal;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface a extends IInterface {

    /* renamed from: com.google.android.gms.maps.model.internal.a$a, reason: collision with other inner class name */
    public static abstract class AbstractBinderC0065a extends Binder implements a {

        /* renamed from: com.google.android.gms.maps.model.internal.a$a$a, reason: collision with other inner class name */
        private static class C0066a implements a {
            private IBinder dG;

            C0066a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.maps.model.internal.a
            public com.google.android.gms.dynamic.b S(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    parcelObtain.writeString(str);
                    this.dG.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.model.internal.a
            public com.google.android.gms.dynamic.b T(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    parcelObtain.writeString(str);
                    this.dG.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.model.internal.a
            public com.google.android.gms.dynamic.b U(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    parcelObtain.writeString(str);
                    this.dG.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.model.internal.a
            public com.google.android.gms.dynamic.b a(Bitmap bitmap) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    if (bitmap != null) {
                        parcelObtain.writeInt(1);
                        bitmap.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.model.internal.a
            public com.google.android.gms.dynamic.b ad(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    parcelObtain.writeInt(i);
                    this.dG.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.dG;
            }

            @Override // com.google.android.gms.maps.model.internal.a
            public com.google.android.gms.dynamic.b c(float f) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    parcelObtain.writeFloat(f);
                    this.dG.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.model.internal.a
            public com.google.android.gms.dynamic.b cQ() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    this.dG.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static a ac(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof a)) ? new C0066a(iBinder) : (a) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    com.google.android.gms.dynamic.b bVarAd = ad(data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarAd != null ? bVarAd.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    com.google.android.gms.dynamic.b bVarS = S(data.readString());
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarS != null ? bVarS.asBinder() : null);
                    return true;
                case 3:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    com.google.android.gms.dynamic.b bVarT = T(data.readString());
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarT != null ? bVarT.asBinder() : null);
                    return true;
                case 4:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    com.google.android.gms.dynamic.b bVarCQ = cQ();
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarCQ != null ? bVarCQ.asBinder() : null);
                    return true;
                case 5:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    com.google.android.gms.dynamic.b bVarC = c(data.readFloat());
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarC != null ? bVarC.asBinder() : null);
                    return true;
                case 6:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    com.google.android.gms.dynamic.b bVarA = a(data.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarA != null ? bVarA.asBinder() : null);
                    return true;
                case 7:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    com.google.android.gms.dynamic.b bVarU = U(data.readString());
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarU != null ? bVarU.asBinder() : null);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    com.google.android.gms.dynamic.b S(String str) throws RemoteException;

    com.google.android.gms.dynamic.b T(String str) throws RemoteException;

    com.google.android.gms.dynamic.b U(String str) throws RemoteException;

    com.google.android.gms.dynamic.b a(Bitmap bitmap) throws RemoteException;

    com.google.android.gms.dynamic.b ad(int i) throws RemoteException;

    com.google.android.gms.dynamic.b c(float f) throws RemoteException;

    com.google.android.gms.dynamic.b cQ() throws RemoteException;
}

package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;
import com.google.android.gms.maps.model.internal.d;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface d extends IInterface {

    public static abstract class a extends Binder implements d {

        /* renamed from: com.google.android.gms.maps.internal.d$a$a, reason: collision with other inner class name */
        private static class C0052a implements d {
            private IBinder dG;

            C0052a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.dG;
            }

            @Override // com.google.android.gms.maps.internal.d
            public com.google.android.gms.dynamic.b f(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    parcelObtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                    this.dG.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.d
            public com.google.android.gms.dynamic.b g(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    parcelObtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                    this.dG.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return b.a.z(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.maps.internal.IInfoWindowAdapter");
        }

        public static d L(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof d)) ? new C0052a(iBinder) : (d) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    com.google.android.gms.dynamic.b bVarF = f(d.a.af(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarF != null ? bVarF.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    com.google.android.gms.dynamic.b bVarG = g(d.a.af(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(bVarG != null ? bVarG.asBinder() : null);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    com.google.android.gms.dynamic.b f(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException;

    com.google.android.gms.dynamic.b g(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException;
}

package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.model.internal.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface c extends IInterface {

    public static abstract class a extends Binder implements c {

        /* renamed from: com.google.android.gms.maps.internal.c$a$a, reason: collision with other inner class name */
        private static class C0051a implements c {
            private IBinder dG;

            C0051a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.maps.internal.c
            public IMapViewDelegate a(com.google.android.gms.dynamic.b bVar, GoogleMapOptions googleMapOptions) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    parcelObtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    if (googleMapOptions != null) {
                        parcelObtain.writeInt(1);
                        googleMapOptions.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return IMapViewDelegate.a.O(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.c
            public void a(com.google.android.gms.dynamic.b bVar, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    parcelObtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    this.dG.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.dG;
            }

            @Override // com.google.android.gms.maps.internal.c
            public ICameraUpdateFactoryDelegate cG() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.dG.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return ICameraUpdateFactoryDelegate.a.H(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.c
            public com.google.android.gms.maps.model.internal.a cH() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.dG.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return a.AbstractBinderC0065a.ac(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.c
            public void e(com.google.android.gms.dynamic.b bVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    parcelObtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    this.dG.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.c
            public IMapFragmentDelegate f(com.google.android.gms.dynamic.b bVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    parcelObtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    this.dG.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return IMapFragmentDelegate.a.N(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static c J(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof c)) ? new C0051a(iBinder) : (c) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    e(b.a.z(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapFragmentDelegate iMapFragmentDelegateF = f(b.a.z(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(iMapFragmentDelegateF != null ? iMapFragmentDelegateF.asBinder() : null);
                    return true;
                case 3:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapViewDelegate iMapViewDelegateA = a(b.a.z(data.readStrongBinder()), data.readInt() != 0 ? GoogleMapOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeStrongBinder(iMapViewDelegateA != null ? iMapViewDelegateA.asBinder() : null);
                    return true;
                case 4:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    ICameraUpdateFactoryDelegate iCameraUpdateFactoryDelegateCG = cG();
                    reply.writeNoException();
                    reply.writeStrongBinder(iCameraUpdateFactoryDelegateCG != null ? iCameraUpdateFactoryDelegateCG.asBinder() : null);
                    return true;
                case 5:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    com.google.android.gms.maps.model.internal.a aVarCH = cH();
                    reply.writeNoException();
                    reply.writeStrongBinder(aVarCH != null ? aVarCH.asBinder() : null);
                    return true;
                case 6:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    a(b.a.z(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.ICreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IMapViewDelegate a(com.google.android.gms.dynamic.b bVar, GoogleMapOptions googleMapOptions) throws RemoteException;

    void a(com.google.android.gms.dynamic.b bVar, int i) throws RemoteException;

    ICameraUpdateFactoryDelegate cG() throws RemoteException;

    com.google.android.gms.maps.model.internal.a cH() throws RemoteException;

    void e(com.google.android.gms.dynamic.b bVar) throws RemoteException;

    IMapFragmentDelegate f(com.google.android.gms.dynamic.b bVar) throws RemoteException;
}

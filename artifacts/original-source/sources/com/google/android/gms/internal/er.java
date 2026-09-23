package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.internal.eq;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface er extends IInterface {

    public static abstract class a extends Binder implements er {

        /* renamed from: com.google.android.gms.internal.er$a$a, reason: collision with other inner class name */
        private static class C0026a implements er {
            private IBinder dG;

            C0026a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.internal.er
            public String L(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    this.dG.transact(5064, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public String M(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    this.dG.transact(5035, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void N(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    this.dG.transact(5050, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public int O(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    this.dG.transact(5060, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public Uri P(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    this.dG.transact(5066, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public int a(eq eqVar, byte[] bArr, String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeByteArray(bArr);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.dG.transact(5033, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(5005, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5002, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, int i, int i2, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(5044, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, int i, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(5015, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeLong(j);
                    this.dG.transact(5058, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, Bundle bundle, int i, int i2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    this.dG.transact(5021, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, IBinder iBinder, int i, String[] strArr, Bundle bundle, boolean z, long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeStrongBinder(iBinder);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeStringArray(strArr);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeLong(j);
                    this.dG.transact(5030, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, IBinder iBinder, String str, boolean z, long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeStrongBinder(iBinder);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeLong(j);
                    this.dG.transact(5031, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5008, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, int i, int i2, int i3, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeInt(i3);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(5019, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(5025, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, int i, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(5045, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, int i, boolean z, boolean z2, boolean z3, boolean z4) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    parcelObtain.writeInt(z3 ? 1 : 0);
                    parcelObtain.writeInt(z4 ? 1 : 0);
                    this.dG.transact(6501, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeLong(j);
                    this.dG.transact(5016, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, long j, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeLong(j);
                    parcelObtain.writeString(str2);
                    this.dG.transact(GamesClient.STATUS_INVALID_REAL_TIME_ROOM_ID, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(5023, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.dG.transact(5009, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeInt(i3);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(5039, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, String str2, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(6002, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(5054, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, String str, boolean z, long[] jArr) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeLongArray(jArr);
                    this.dG.transact(5011, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(eq eqVar, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(5063, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void a(String str, String str2, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(i);
                    this.dG.transact(5051, parcelObtain, parcelObtain2, 0);
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

            @Override // com.google.android.gms.internal.er
            public int b(byte[] bArr, String str, String[] strArr) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeByteArray(bArr);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStringArray(strArr);
                    this.dG.transact(5034, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5017, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, int i, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(5046, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5010, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, int i, int i2, int i3, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeInt(i3);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(5020, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(GamesClient.STATUS_PARTICIPANT_NOT_CONNECTED, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, int i, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(5501, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(5024, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.dG.transact(5038, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeInt(i3);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(5040, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, String str2, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(6506, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, String str, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(6502, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void b(eq eqVar, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(GamesClient.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void bT() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5006, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public com.google.android.gms.common.data.d bU() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5013, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? com.google.android.gms.common.data.d.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public boolean bV() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5067, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public com.google.android.gms.common.data.d bW() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5502, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? com.google.android.gms.common.data.d.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public Bundle ba() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5004, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void c(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5022, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void c(eq eqVar, int i, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(5048, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void c(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5014, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void c(eq eqVar, String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.dG.transact(5041, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void c(eq eqVar, String str, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(6504, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void c(eq eqVar, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(6503, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void clearNotifications(int notificationTypes) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeInt(notificationTypes);
                    this.dG.transact(5036, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void d(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5026, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void d(eq eqVar, int i, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(6003, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void d(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5018, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void d(eq eqVar, String str, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.dG.transact(6505, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public ParcelFileDescriptor e(Uri uri) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    if (uri != null) {
                        parcelObtain.writeInt(1);
                        uri.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.dG.transact(6507, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void e(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5027, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void e(eq eqVar, int i, boolean z, boolean z2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeInt(z2 ? 1 : 0);
                    this.dG.transact(6004, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void e(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5032, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void f(long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeLong(j);
                    this.dG.transact(5001, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void f(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5047, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void f(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5037, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void f(String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.dG.transact(5065, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void g(long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeLong(j);
                    this.dG.transact(5059, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void g(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5049, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void g(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5042, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public String getAppId() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5003, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public String getCurrentAccountName() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5007, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public String getCurrentPlayerId() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.dG.transact(5012, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void h(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5056, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void h(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5043, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void i(eq eqVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    this.dG.transact(5062, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void i(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5052, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void i(String str, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    this.dG.transact(5029, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public int j(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5053, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void j(String str, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    this.dG.transact(5028, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void k(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5061, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void k(String str, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    this.dG.transact(5055, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void l(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(5057, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void m(eq eqVar, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeStrongBinder(eqVar != null ? eqVar.asBinder() : null);
                    parcelObtain.writeString(str);
                    this.dG.transact(GamesClient.STATUS_REAL_TIME_MESSAGE_SEND_FAILED, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.er
            public void setUseNewPlayerNotificationsFirstParty(boolean newPlayerStyle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    parcelObtain.writeInt(newPlayerStyle ? 1 : 0);
                    this.dG.transact(5068, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static er C(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.games.internal.IGamesService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof er)) ? new C0026a(iBinder) : (er) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 5001:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5002:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5003:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    String appId = getAppId();
                    parcel2.writeNoException();
                    parcel2.writeString(appId);
                    return true;
                case 5004:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    Bundle bundleBa = ba();
                    parcel2.writeNoException();
                    if (bundleBa == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    bundleBa.writeToParcel(parcel2, 1);
                    return true;
                case 5005:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(parcel.readStrongBinder(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5006:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    bT();
                    parcel2.writeNoException();
                    return true;
                case 5007:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    String currentAccountName = getCurrentAccountName();
                    parcel2.writeNoException();
                    parcel2.writeString(currentAccountName);
                    return true;
                case 5008:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5009:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5010:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5011:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0, parcel.createLongArray());
                    parcel2.writeNoException();
                    return true;
                case 5012:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    String currentPlayerId = getCurrentPlayerId();
                    parcel2.writeNoException();
                    parcel2.writeString(currentPlayerId);
                    return true;
                case 5013:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    com.google.android.gms.common.data.d dVarBU = bU();
                    parcel2.writeNoException();
                    if (dVarBU == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    dVarBU.writeToParcel(parcel2, 1);
                    return true;
                case 5014:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5015:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5016:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5017:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5018:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5019:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5020:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5021:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5022:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5023:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readStrongBinder(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5024:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readStrongBinder(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5025:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readStrongBinder(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5026:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5027:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    e(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5028:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    j(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5029:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    i(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5030:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readStrongBinder(), parcel.readInt(), parcel.createStringArray(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0, parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5031:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readStrongBinder(), parcel.readString(), parcel.readInt() != 0, parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5032:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    e(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5033:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    int iA = a(eq.a.B(parcel.readStrongBinder()), parcel.createByteArray(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(iA);
                    return true;
                case 5034:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    int iB = b(parcel.createByteArray(), parcel.readString(), parcel.createStringArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(iB);
                    return true;
                case 5035:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    String strM = M(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(strM);
                    return true;
                case 5036:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    clearNotifications(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5037:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5038:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5039:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5040:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5041:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5042:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    g(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5043:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    h(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5044:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5045:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5046:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5047:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5048:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(eq.a.B(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5049:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    g(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5050:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    N(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5051:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(parcel.readString(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5052:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    i(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5053:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    int iJ = j(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(iJ);
                    return true;
                case 5054:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5055:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    k(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5056:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    h(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5057:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    l(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5058:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5059:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    g(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5060:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    int iO = O(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(iO);
                    return true;
                case 5061:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    k(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5062:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    i(eq.a.B(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5063:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5064:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    String strL = L(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(strL);
                    return true;
                case 5065:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5066:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    Uri uriP = P(parcel.readString());
                    parcel2.writeNoException();
                    if (uriP == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    uriP.writeToParcel(parcel2, 1);
                    return true;
                case 5067:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    boolean zBV = bV();
                    parcel2.writeNoException();
                    parcel2.writeInt(zBV ? 1 : 0);
                    return true;
                case 5068:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    setUseNewPlayerNotificationsFirstParty(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5501:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5502:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    com.google.android.gms.common.data.d dVarBW = bW();
                    parcel2.writeNoException();
                    if (dVarBW == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    dVarBW.writeToParcel(parcel2, 1);
                    return true;
                case GamesClient.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER /* 6001 */:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6002:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6003:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(eq.a.B(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6004:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    e(eq.a.B(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6501:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0, parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6502:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6503:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(eq.a.B(parcel.readStrongBinder()), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6504:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6505:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6506:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6507:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    ParcelFileDescriptor parcelFileDescriptorE = e(parcel.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (parcelFileDescriptorE == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    parcelFileDescriptorE.writeToParcel(parcel2, 1);
                    return true;
                case GamesClient.STATUS_REAL_TIME_MESSAGE_SEND_FAILED /* 7001 */:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    m(eq.a.B(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case GamesClient.STATUS_INVALID_REAL_TIME_ROOM_ID /* 7002 */:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readLong(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case GamesClient.STATUS_PARTICIPANT_NOT_CONNECTED /* 7003 */:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(eq.a.B(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readStrongBinder(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.games.internal.IGamesService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    String L(String str) throws RemoteException;

    String M(String str) throws RemoteException;

    void N(String str) throws RemoteException;

    int O(String str) throws RemoteException;

    Uri P(String str) throws RemoteException;

    int a(eq eqVar, byte[] bArr, String str, String str2) throws RemoteException;

    void a(IBinder iBinder, Bundle bundle) throws RemoteException;

    void a(eq eqVar) throws RemoteException;

    void a(eq eqVar, int i, int i2, boolean z, boolean z2) throws RemoteException;

    void a(eq eqVar, int i, boolean z, boolean z2) throws RemoteException;

    void a(eq eqVar, long j) throws RemoteException;

    void a(eq eqVar, Bundle bundle, int i, int i2) throws RemoteException;

    void a(eq eqVar, IBinder iBinder, int i, String[] strArr, Bundle bundle, boolean z, long j) throws RemoteException;

    void a(eq eqVar, IBinder iBinder, String str, boolean z, long j) throws RemoteException;

    void a(eq eqVar, String str) throws RemoteException;

    void a(eq eqVar, String str, int i, int i2, int i3, boolean z) throws RemoteException;

    void a(eq eqVar, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException;

    void a(eq eqVar, String str, int i, boolean z, boolean z2) throws RemoteException;

    void a(eq eqVar, String str, int i, boolean z, boolean z2, boolean z3, boolean z4) throws RemoteException;

    void a(eq eqVar, String str, long j) throws RemoteException;

    void a(eq eqVar, String str, long j, String str2) throws RemoteException;

    void a(eq eqVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void a(eq eqVar, String str, String str2) throws RemoteException;

    void a(eq eqVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException;

    void a(eq eqVar, String str, String str2, boolean z) throws RemoteException;

    void a(eq eqVar, String str, boolean z) throws RemoteException;

    void a(eq eqVar, String str, boolean z, long[] jArr) throws RemoteException;

    void a(eq eqVar, boolean z) throws RemoteException;

    void a(String str, String str2, int i) throws RemoteException;

    int b(byte[] bArr, String str, String[] strArr) throws RemoteException;

    void b(eq eqVar) throws RemoteException;

    void b(eq eqVar, int i, boolean z, boolean z2) throws RemoteException;

    void b(eq eqVar, String str) throws RemoteException;

    void b(eq eqVar, String str, int i, int i2, int i3, boolean z) throws RemoteException;

    void b(eq eqVar, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException;

    void b(eq eqVar, String str, int i, boolean z, boolean z2) throws RemoteException;

    void b(eq eqVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void b(eq eqVar, String str, String str2) throws RemoteException;

    void b(eq eqVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException;

    void b(eq eqVar, String str, String str2, boolean z) throws RemoteException;

    void b(eq eqVar, String str, boolean z) throws RemoteException;

    void b(eq eqVar, boolean z) throws RemoteException;

    void bT() throws RemoteException;

    com.google.android.gms.common.data.d bU() throws RemoteException;

    boolean bV() throws RemoteException;

    com.google.android.gms.common.data.d bW() throws RemoteException;

    Bundle ba() throws RemoteException;

    void c(eq eqVar) throws RemoteException;

    void c(eq eqVar, int i, boolean z, boolean z2) throws RemoteException;

    void c(eq eqVar, String str) throws RemoteException;

    void c(eq eqVar, String str, String str2) throws RemoteException;

    void c(eq eqVar, String str, boolean z) throws RemoteException;

    void c(eq eqVar, boolean z) throws RemoteException;

    void clearNotifications(int i) throws RemoteException;

    void d(eq eqVar) throws RemoteException;

    void d(eq eqVar, int i, boolean z, boolean z2) throws RemoteException;

    void d(eq eqVar, String str) throws RemoteException;

    void d(eq eqVar, String str, boolean z) throws RemoteException;

    ParcelFileDescriptor e(Uri uri) throws RemoteException;

    void e(eq eqVar) throws RemoteException;

    void e(eq eqVar, int i, boolean z, boolean z2) throws RemoteException;

    void e(eq eqVar, String str) throws RemoteException;

    void f(long j) throws RemoteException;

    void f(eq eqVar) throws RemoteException;

    void f(eq eqVar, String str) throws RemoteException;

    void f(String str, String str2) throws RemoteException;

    void g(long j) throws RemoteException;

    void g(eq eqVar) throws RemoteException;

    void g(eq eqVar, String str) throws RemoteException;

    String getAppId() throws RemoteException;

    String getCurrentAccountName() throws RemoteException;

    String getCurrentPlayerId() throws RemoteException;

    void h(eq eqVar) throws RemoteException;

    void h(eq eqVar, String str) throws RemoteException;

    void i(eq eqVar) throws RemoteException;

    void i(eq eqVar, String str) throws RemoteException;

    void i(String str, int i) throws RemoteException;

    int j(eq eqVar, String str) throws RemoteException;

    void j(String str, int i) throws RemoteException;

    void k(eq eqVar, String str) throws RemoteException;

    void k(String str, int i) throws RemoteException;

    void l(eq eqVar, String str) throws RemoteException;

    void m(eq eqVar, String str) throws RemoteException;

    void setUseNewPlayerNotificationsFirstParty(boolean z) throws RemoteException;
}

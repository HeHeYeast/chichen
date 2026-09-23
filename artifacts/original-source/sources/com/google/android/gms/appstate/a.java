package com.google.android.gms.appstate;

import com.google.android.gms.internal.dl;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class a implements AppState {
    private final int iu;
    private final String iv;
    private final byte[] iw;
    private final boolean ix;
    private final String iy;
    private final byte[] iz;

    public a(AppState appState) {
        this.iu = appState.getKey();
        this.iv = appState.getLocalVersion();
        this.iw = appState.getLocalData();
        this.ix = appState.hasConflict();
        this.iy = appState.getConflictVersion();
        this.iz = appState.getConflictData();
    }

    static int a(AppState appState) {
        return dl.hashCode(Integer.valueOf(appState.getKey()), appState.getLocalVersion(), appState.getLocalData(), Boolean.valueOf(appState.hasConflict()), appState.getConflictVersion(), appState.getConflictData());
    }

    static boolean a(AppState appState, Object obj) {
        if (!(obj instanceof AppState)) {
            return false;
        }
        if (appState == obj) {
            return true;
        }
        AppState appState2 = (AppState) obj;
        return dl.equal(Integer.valueOf(appState2.getKey()), Integer.valueOf(appState.getKey())) && dl.equal(appState2.getLocalVersion(), appState.getLocalVersion()) && dl.equal(appState2.getLocalData(), appState.getLocalData()) && dl.equal(Boolean.valueOf(appState2.hasConflict()), Boolean.valueOf(appState.hasConflict())) && dl.equal(appState2.getConflictVersion(), appState.getConflictVersion()) && dl.equal(appState2.getConflictData(), appState.getConflictData());
    }

    static String b(AppState appState) {
        return dl.d(appState).a("Key", Integer.valueOf(appState.getKey())).a("LocalVersion", appState.getLocalVersion()).a("LocalData", appState.getLocalData()).a("HasConflict", Boolean.valueOf(appState.hasConflict())).a("ConflictVersion", appState.getConflictVersion()).a("ConflictData", appState.getConflictData()).toString();
    }

    @Override // com.google.android.gms.common.data.Freezable
    /* renamed from: aE, reason: merged with bridge method [inline-methods] */
    public AppState freeze() {
        return this;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    @Override // com.google.android.gms.appstate.AppState
    public byte[] getConflictData() {
        return this.iz;
    }

    @Override // com.google.android.gms.appstate.AppState
    public String getConflictVersion() {
        return this.iy;
    }

    @Override // com.google.android.gms.appstate.AppState
    public int getKey() {
        return this.iu;
    }

    @Override // com.google.android.gms.appstate.AppState
    public byte[] getLocalData() {
        return this.iw;
    }

    @Override // com.google.android.gms.appstate.AppState
    public String getLocalVersion() {
        return this.iv;
    }

    @Override // com.google.android.gms.appstate.AppState
    public boolean hasConflict() {
        return this.ix;
    }

    public int hashCode() {
        return a(this);
    }

    @Override // com.google.android.gms.common.data.Freezable
    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return b(this);
    }
}

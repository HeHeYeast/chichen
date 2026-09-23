package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.r;
import com.google.android.gms.maps.model.CameraPosition;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class GoogleMapOptions implements SafeParcelable {
    public static final GoogleMapOptionsCreator CREATOR = new GoogleMapOptionsCreator();
    private final int iM;
    private CameraPosition pA;
    private Boolean pB;
    private Boolean pC;
    private Boolean pD;
    private Boolean pE;
    private Boolean pF;
    private Boolean pG;
    private Boolean px;
    private Boolean py;
    private int pz;

    public GoogleMapOptions() {
        this.pz = -1;
        this.iM = 1;
    }

    GoogleMapOptions(int versionCode, byte zOrderOnTop, byte useViewLifecycleInFragment, int mapType, CameraPosition camera, byte zoomControlsEnabled, byte compassEnabled, byte scrollGesturesEnabled, byte zoomGesturesEnabled, byte tiltGesturesEnabled, byte rotateGesturesEnabled) {
        this.pz = -1;
        this.iM = versionCode;
        this.px = com.google.android.gms.maps.internal.a.a(zOrderOnTop);
        this.py = com.google.android.gms.maps.internal.a.a(useViewLifecycleInFragment);
        this.pz = mapType;
        this.pA = camera;
        this.pB = com.google.android.gms.maps.internal.a.a(zoomControlsEnabled);
        this.pC = com.google.android.gms.maps.internal.a.a(compassEnabled);
        this.pD = com.google.android.gms.maps.internal.a.a(scrollGesturesEnabled);
        this.pE = com.google.android.gms.maps.internal.a.a(zoomGesturesEnabled);
        this.pF = com.google.android.gms.maps.internal.a.a(tiltGesturesEnabled);
        this.pG = com.google.android.gms.maps.internal.a.a(rotateGesturesEnabled);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray typedArrayObtainAttributes = context.getResources().obtainAttributes(attrs, R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (typedArrayObtainAttributes.hasValue(0)) {
            googleMapOptions.mapType(typedArrayObtainAttributes.getInt(0, -1));
        }
        if (typedArrayObtainAttributes.hasValue(13)) {
            googleMapOptions.zOrderOnTop(typedArrayObtainAttributes.getBoolean(13, false));
        }
        if (typedArrayObtainAttributes.hasValue(12)) {
            googleMapOptions.useViewLifecycleInFragment(typedArrayObtainAttributes.getBoolean(12, false));
        }
        if (typedArrayObtainAttributes.hasValue(6)) {
            googleMapOptions.compassEnabled(typedArrayObtainAttributes.getBoolean(6, true));
        }
        if (typedArrayObtainAttributes.hasValue(7)) {
            googleMapOptions.rotateGesturesEnabled(typedArrayObtainAttributes.getBoolean(7, true));
        }
        if (typedArrayObtainAttributes.hasValue(8)) {
            googleMapOptions.scrollGesturesEnabled(typedArrayObtainAttributes.getBoolean(8, true));
        }
        if (typedArrayObtainAttributes.hasValue(9)) {
            googleMapOptions.tiltGesturesEnabled(typedArrayObtainAttributes.getBoolean(9, true));
        }
        if (typedArrayObtainAttributes.hasValue(11)) {
            googleMapOptions.zoomGesturesEnabled(typedArrayObtainAttributes.getBoolean(11, true));
        }
        if (typedArrayObtainAttributes.hasValue(10)) {
            googleMapOptions.zoomControlsEnabled(typedArrayObtainAttributes.getBoolean(10, true));
        }
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attrs));
        typedArrayObtainAttributes.recycle();
        return googleMapOptions;
    }

    byte cA() {
        return com.google.android.gms.maps.internal.a.b(this.pE);
    }

    byte cB() {
        return com.google.android.gms.maps.internal.a.b(this.pF);
    }

    byte cC() {
        return com.google.android.gms.maps.internal.a.b(this.pG);
    }

    public GoogleMapOptions camera(CameraPosition camera) {
        this.pA = camera;
        return this;
    }

    public GoogleMapOptions compassEnabled(boolean enabled) {
        this.pC = Boolean.valueOf(enabled);
        return this;
    }

    byte cv() {
        return com.google.android.gms.maps.internal.a.b(this.px);
    }

    byte cw() {
        return com.google.android.gms.maps.internal.a.b(this.py);
    }

    byte cx() {
        return com.google.android.gms.maps.internal.a.b(this.pB);
    }

    byte cy() {
        return com.google.android.gms.maps.internal.a.b(this.pC);
    }

    byte cz() {
        return com.google.android.gms.maps.internal.a.b(this.pD);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CameraPosition getCamera() {
        return this.pA;
    }

    public Boolean getCompassEnabled() {
        return this.pC;
    }

    public int getMapType() {
        return this.pz;
    }

    public Boolean getRotateGesturesEnabled() {
        return this.pG;
    }

    public Boolean getScrollGesturesEnabled() {
        return this.pD;
    }

    public Boolean getTiltGesturesEnabled() {
        return this.pF;
    }

    public Boolean getUseViewLifecycleInFragment() {
        return this.py;
    }

    int getVersionCode() {
        return this.iM;
    }

    public Boolean getZOrderOnTop() {
        return this.px;
    }

    public Boolean getZoomControlsEnabled() {
        return this.pB;
    }

    public Boolean getZoomGesturesEnabled() {
        return this.pE;
    }

    public GoogleMapOptions mapType(int mapType) {
        this.pz = mapType;
        return this;
    }

    public GoogleMapOptions rotateGesturesEnabled(boolean enabled) {
        this.pG = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions scrollGesturesEnabled(boolean enabled) {
        this.pD = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions tiltGesturesEnabled(boolean enabled) {
        this.pF = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions useViewLifecycleInFragment(boolean useViewLifecycleInFragment) {
        this.py = Boolean.valueOf(useViewLifecycleInFragment);
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            a.a(this, out, flags);
        } else {
            GoogleMapOptionsCreator.a(this, out, flags);
        }
    }

    public GoogleMapOptions zOrderOnTop(boolean zOrderOnTop) {
        this.px = Boolean.valueOf(zOrderOnTop);
        return this;
    }

    public GoogleMapOptions zoomControlsEnabled(boolean enabled) {
        this.pB = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions zoomGesturesEnabled(boolean enabled) {
        this.pE = Boolean.valueOf(enabled);
        return this;
    }
}

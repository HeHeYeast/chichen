package com.vpadn.ads;

import android.content.Context;
import android.location.Location;
import android.telephony.TelephonyManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VpadnAdRequest {
    private Gender a = Gender.UNKNOWN;
    private Set<String> b = new HashSet();

    /* renamed from: c, reason: collision with root package name */
    private Set<String> f267c = new HashSet();
    private Date d = null;
    private boolean e = false;
    private Location f = null;
    private boolean g;
    private int h;

    public enum Gender {
        FEMALE,
        MALE,
        UNKNOWN;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static Gender[] valuesCustom() {
            Gender[] genderArrValuesCustom = values();
            int length = genderArrValuesCustom.length;
            Gender[] genderArr = new Gender[length];
            System.arraycopy(genderArrValuesCustom, 0, genderArr, 0, length);
            return genderArr;
        }
    }

    public enum VpadnErrorCode {
        INTERNAL_ERROR,
        INVALID_REQUEST,
        NO_FILL,
        NETWORK_ERROR;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static VpadnErrorCode[] valuesCustom() {
            VpadnErrorCode[] vpadnErrorCodeArrValuesCustom = values();
            int length = vpadnErrorCodeArrValuesCustom.length;
            VpadnErrorCode[] vpadnErrorCodeArr = new VpadnErrorCode[length];
            System.arraycopy(vpadnErrorCodeArrValuesCustom, 0, vpadnErrorCodeArr, 0, length);
            return vpadnErrorCodeArr;
        }
    }

    public int getAge() {
        return this.h;
    }

    public void setAge(int i) {
        this.h = i;
    }

    public boolean isAutoRefresh() {
        return this.g;
    }

    public VpadnAdRequest() {
        this.g = false;
        this.g = false;
    }

    public void setEnableAutoRefresh(boolean z) {
        this.g = z;
    }

    public VpadnAdRequest setAutoRefresh(boolean z) {
        this.g = z;
        return this;
    }

    @Deprecated
    public VpadnAdRequest addExtra(String str, Object obj) {
        return this;
    }

    public VpadnAdRequest addKeyword(String str) {
        this.b.add(str);
        return this;
    }

    public VpadnAdRequest addKeywords(Set<String> set) {
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            this.b.add(it.next());
        }
        return this;
    }

    public VpadnAdRequest addMediationExtra(String str, Object obj) {
        return this;
    }

    public VpadnAdRequest addTestDevice(String str) {
        this.f267c.add(str);
        return this;
    }

    public VpadnAdRequest clearBirthday() {
        this.d = null;
        return this;
    }

    public Date getBirthday() {
        return this.d;
    }

    public Gender getGender() {
        return this.a;
    }

    public Set<String> getKeywords() {
        return this.b;
    }

    public Location getLocation() {
        return this.f;
    }

    @Deprecated
    public <T> T getNetworkExtras(Class<T> cls) {
        return null;
    }

    public boolean getPlusOneOptOut() {
        return false;
    }

    public Map<String, Object> getRequestMap(Context context) {
        return null;
    }

    public boolean isTestDevice(Context context) {
        if (this.f267c.contains(((TelephonyManager) context.getSystemService("phone")).getDeviceId())) {
            this.e = true;
        } else {
            this.e = false;
        }
        return this.e;
    }

    public VpadnAdRequest removeNetworkExtras(Class<?> cls) {
        return null;
    }

    public VpadnAdRequest setBirthday(Calendar calendar) {
        this.d = calendar.getTime();
        return this;
    }

    public VpadnAdRequest setBirthday(Date date) {
        this.d = date;
        return this;
    }

    @Deprecated
    public VpadnAdRequest setBirthday(String str) {
        try {
            this.d = new SimpleDateFormat("yyyy-MM-DD").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Deprecated
    public VpadnAdRequest setExtras(Map<String, Object> map) {
        return this;
    }

    public VpadnAdRequest setGender(Gender gender) {
        this.a = gender;
        return this;
    }

    public VpadnAdRequest setKeywords(Set<String> set) {
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            this.b.add(it.next());
        }
        return this;
    }

    public VpadnAdRequest setLocation(Location location) {
        this.f = location;
        return this;
    }

    public VpadnAdRequest setMediationExtras(Map<String, Object> map) {
        return this;
    }

    @Deprecated
    public VpadnAdRequest setPlusOneOptOut(boolean z) {
        return this;
    }

    public VpadnAdRequest setTestDevices(Set<String> set) {
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            this.f267c.add(it.next());
        }
        return this;
    }

    @Deprecated
    public VpadnAdRequest setTesting(boolean z) {
        this.e = z;
        return this;
    }
}

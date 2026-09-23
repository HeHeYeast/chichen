package vpadn;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Settings;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ac implements ServiceConnection {
    private static String f = null;
    private static boolean g = false;
    private final Context a;
    private List<ResolveInfo> b;
    private final SharedPreferences d;
    private final Random e = new Random();

    /* renamed from: c, reason: collision with root package name */
    private Map<String, Integer> f319c = new HashMap();

    private ac(Context context) {
        this.d = context.getSharedPreferences("openudid_prefs", 0);
        this.a = context;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) throws RemoteException {
        String string;
        try {
            Parcel parcelObtain = Parcel.obtain();
            parcelObtain.writeInt(this.e.nextInt());
            Parcel parcelObtain2 = Parcel.obtain();
            iBinder.transact(1, Parcel.obtain(), parcelObtain2, 0);
            if (parcelObtain.readInt() == parcelObtain2.readInt() && (string = parcelObtain2.readString()) != null) {
                if (this.f319c.containsKey(string)) {
                    this.f319c.put(string, Integer.valueOf(this.f319c.get(string).intValue() + 1));
                } else {
                    this.f319c.put(string, 1);
                }
            }
        } catch (RemoteException e) {
        }
        this.a.unbindService(this);
        c();
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
    }

    private void c() {
        byte b = 0;
        if (this.b.size() > 0) {
            ServiceInfo serviceInfo = this.b.get(0).serviceInfo;
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(serviceInfo.applicationInfo.packageName, serviceInfo.name));
            this.a.bindService(intent, this, 1);
            this.b.remove(0);
            return;
        }
        if (!this.f319c.isEmpty()) {
            TreeMap treeMap = new TreeMap(new a(this, b));
            treeMap.putAll(this.f319c);
            f = (String) treeMap.firstKey();
        }
        if (f == null) {
            String string = Settings.Secure.getString(this.a.getContentResolver(), "android_id");
            f = string;
            if (string == null || f.equals("9774d56d682e549c") || f.length() < 15) {
                f = new BigInteger(64, new SecureRandom()).toString(16);
            }
        }
        SharedPreferences.Editor editorEdit = this.d.edit();
        editorEdit.putString("openudid", f);
        editorEdit.commit();
        g = true;
    }

    public static String a() {
        if (!g) {
            ab.b("OpenUDID", "Initialisation isn't done");
        }
        return f;
    }

    public static boolean b() {
        return g;
    }

    public static void a(Context context) {
        ac acVar = new ac(context);
        String string = acVar.d.getString("openudid", null);
        f = string;
        if (string == null) {
            acVar.b = context.getPackageManager().queryIntentServices(new Intent("org.OpenUDID.GETUDID"), 0);
            if (acVar.b != null) {
                acVar.c();
                return;
            }
            return;
        }
        g = true;
    }

    class a implements Comparator {
        private a() {
        }

        /* synthetic */ a(ac acVar, byte b) {
            this();
        }

        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            if (((Integer) ac.this.f319c.get(obj)).intValue() >= ((Integer) ac.this.f319c.get(obj2)).intValue()) {
                if (ac.this.f319c.get(obj) == ac.this.f319c.get(obj2)) {
                    return 0;
                }
                return -1;
            }
            return 1;
        }
    }
}

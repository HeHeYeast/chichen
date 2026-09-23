package c;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;
import vpadn.InterfaceC0102p;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CompassListener extends C0103q implements SensorEventListener {
    private long d;
    private SensorManager e;
    private Sensor f;
    public static int STOPPED = 0;
    public static int STARTING = 1;
    public static int RUNNING = 2;
    public static int ERROR_FAILED_TO_START = 3;
    public long TIMEOUT = 30000;
    private float b = BitmapDescriptorFactory.HUE_RED;

    /* renamed from: c, reason: collision with root package name */
    private long f159c = 0;
    private int a = STOPPED;

    @Override // vpadn.C0103q
    public void initialize(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        super.initialize(interfaceC0102p, cordovaWebView);
        this.e = (SensorManager) interfaceC0102p.a().getSystemService("sensor");
    }

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException {
        if (str.equals("start")) {
            start();
            return true;
        }
        if (str.equals("stop")) {
            stop();
            return true;
        }
        if (str.equals("getStatus")) {
            c0101o.a(new C0108v(C0108v.a.OK, getStatus()));
            return true;
        }
        if (str.equals("getHeading")) {
            if (this.a != RUNNING) {
                if (start() == ERROR_FAILED_TO_START) {
                    c0101o.a(new C0108v(C0108v.a.IO_EXCEPTION, ERROR_FAILED_TO_START));
                    return true;
                }
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: c.CompassListener.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        CompassListener.a(CompassListener.this);
                    }
                }, 2000L);
            }
            C0108v.a aVar = C0108v.a.OK;
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("magneticHeading", getHeading());
            jSONObject.put("trueHeading", getHeading());
            jSONObject.put("headingAccuracy", 0);
            jSONObject.put("timestamp", this.f159c);
            c0101o.a(new C0108v(aVar, jSONObject));
            return true;
        }
        if (str.equals("setTimeout")) {
            setTimeout(jSONArray.getLong(0));
            return true;
        }
        if (!str.equals("getTimeout")) {
            return false;
        }
        c0101o.a(new C0108v(C0108v.a.OK, getTimeout()));
        return true;
    }

    @Override // vpadn.C0103q
    public void onDestroy() {
        stop();
    }

    @Override // vpadn.C0103q
    public void onReset() {
        stop();
    }

    public int start() {
        if (this.a == RUNNING || this.a == STARTING) {
            return this.a;
        }
        List<Sensor> sensorList = this.e.getSensorList(3);
        if (sensorList != null && sensorList.size() > 0) {
            this.f = sensorList.get(0);
            this.e.registerListener(this, this.f, 3);
            this.d = System.currentTimeMillis();
            this.a = STARTING;
        } else {
            this.a = ERROR_FAILED_TO_START;
        }
        return this.a;
    }

    public void stop() {
        if (this.a != STOPPED) {
            this.e.unregisterListener(this);
        }
        this.a = STOPPED;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    static /* synthetic */ void a(CompassListener compassListener) {
        if (compassListener.a == STARTING) {
            compassListener.a = ERROR_FAILED_TO_START;
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        float f = sensorEvent.values[0];
        this.f159c = System.currentTimeMillis();
        this.b = f;
        this.a = RUNNING;
        if (this.f159c - this.d > this.TIMEOUT) {
            stop();
        }
    }

    public int getStatus() {
        return this.a;
    }

    public float getHeading() {
        this.d = System.currentTimeMillis();
        return this.b;
    }

    public void setTimeout(long j) {
        this.TIMEOUT = j;
    }

    public long getTimeout() {
        return this.TIMEOUT;
    }
}

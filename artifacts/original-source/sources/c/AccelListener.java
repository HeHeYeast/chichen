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
public class AccelListener extends C0103q implements SensorEventListener {
    private SensorManager g;
    private Sensor h;
    private C0101o i;
    public static int STOPPED = 0;
    public static int STARTING = 1;
    public static int RUNNING = 2;
    public static int ERROR_FAILED_TO_START = 3;
    private int f = 0;
    private float a = BitmapDescriptorFactory.HUE_RED;
    private float b = BitmapDescriptorFactory.HUE_RED;

    /* renamed from: c, reason: collision with root package name */
    private float f156c = BitmapDescriptorFactory.HUE_RED;
    private long d = 0;
    private int e = STOPPED;

    @Override // vpadn.C0103q
    public void initialize(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        super.initialize(interfaceC0102p, cordovaWebView);
        this.g = (SensorManager) interfaceC0102p.a().getSystemService("sensor");
    }

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException {
        if (str.equals("start")) {
            this.i = c0101o;
            if (this.e != RUNNING) {
                if (this.e == RUNNING || this.e == STARTING) {
                    int i = this.e;
                } else {
                    this.e = STARTING;
                    List<Sensor> sensorList = this.g.getSensorList(1);
                    if (sensorList == null || sensorList.size() <= 0) {
                        this.e = ERROR_FAILED_TO_START;
                        a(ERROR_FAILED_TO_START, "No sensors found to register accelerometer listening to.");
                        int i2 = this.e;
                    } else {
                        this.h = sensorList.get(0);
                        this.g.registerListener(this, this.h, 2);
                        this.e = STARTING;
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: c.AccelListener.1
                            @Override // java.lang.Runnable
                            public final void run() throws JSONException {
                                AccelListener.a(AccelListener.this);
                            }
                        }, 2000L);
                        int i3 = this.e;
                    }
                }
            }
        } else {
            if (!str.equals("stop")) {
                return false;
            }
            if (this.e == RUNNING) {
                a();
            }
        }
        C0108v c0108v = new C0108v(C0108v.a.NO_RESULT, "");
        c0108v.a(true);
        c0101o.a(c0108v);
        return true;
    }

    @Override // vpadn.C0103q
    public void onDestroy() {
        a();
    }

    private void a() {
        if (this.e != STOPPED) {
            this.g.unregisterListener(this);
        }
        this.e = STOPPED;
        this.f = 0;
    }

    static /* synthetic */ void a(AccelListener accelListener) throws JSONException {
        if (accelListener.e == STARTING) {
            accelListener.e = ERROR_FAILED_TO_START;
            accelListener.a(ERROR_FAILED_TO_START, "Accelerometer could not be started.");
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (sensor.getType() == 1 && this.e != STOPPED) {
            this.f = i;
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 1 && this.e != STOPPED) {
            this.e = RUNNING;
            if (this.f >= 2) {
                this.d = System.currentTimeMillis();
                this.a = sensorEvent.values[0];
                this.b = sensorEvent.values[1];
                this.f156c = sensorEvent.values[2];
                C0108v c0108v = new C0108v(C0108v.a.OK, b());
                c0108v.a(true);
                this.i.a(c0108v);
            }
        }
    }

    @Override // vpadn.C0103q
    public void onReset() {
        if (this.e == RUNNING) {
            a();
        }
    }

    private void a(int i, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", i);
            jSONObject.put("message", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        C0108v c0108v = new C0108v(C0108v.a.ERROR, jSONObject);
        c0108v.a(true);
        this.i.a(c0108v);
    }

    private JSONObject b() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("x", this.a);
            jSONObject.put("y", this.b);
            jSONObject.put("z", this.f156c);
            jSONObject.put("timestamp", this.d);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}

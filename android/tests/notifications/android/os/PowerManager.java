package android.os;
public class PowerManager {
    public boolean ignoring=false;
    public boolean isIgnoringBatteryOptimizations(String packageName) { return ignoring; }
}

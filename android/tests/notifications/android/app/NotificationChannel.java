package android.app;
public class NotificationChannel {
    public final String id; public int importance;
    public NotificationChannel(String id,String name,int importance) { this.id=id;this.importance=importance; }
    public int lockscreenVisibility;
    public void setDescription(String text) {}
    public void setLockscreenVisibility(int value) { lockscreenVisibility=value; }
    public boolean vibration; public void enableVibration(boolean value) { vibration=value; }
    public int getImportance() { return importance; }
}

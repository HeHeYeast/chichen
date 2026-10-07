package android.app;
import android.content.Context;
public class Notification {
    public static final String CATEGORY_REMINDER="reminder"; public static final int VISIBILITY_PRIVATE=0,VISIBILITY_PUBLIC=1;
    public String title,text; public int visibility; public PendingIntent content;
    public static class BigTextStyle { public BigTextStyle bigText(String text) { return this; } }
    public static class Builder {
        private final Notification n=new Notification();
        public Builder(Context context,String channel) {}
        public Builder setSmallIcon(int icon) { return this; }
        public Builder setContentTitle(String title) { n.title=title;return this; }
        public Builder setContentText(String text) { n.text=text;return this; }
        public Builder setStyle(BigTextStyle style) { return this; }
        public Builder setContentIntent(PendingIntent content) { n.content=content;return this; }
        public Builder setAutoCancel(boolean value) { return this; }
        public Builder setCategory(String value) { return this; }
        public Builder setVisibility(int value) { n.visibility=value;return this; }
        public Builder setOnlyAlertOnce(boolean value) { return this; }
        public Notification build() { return n; }
    }
}

package c;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;
import org.json.JSONArray;
import org.json.JSONException;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;
import vpadn.InterfaceC0102p;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Notification extends C0103q {
    public int confirmResult = -1;
    public ProgressDialog spinnerDialog = null;
    public ProgressDialog progressDialog = null;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException, InterruptedException {
        if (str.equals("beep")) {
            beep(jSONArray.getLong(0));
        } else if (str.equals("vibrate")) {
            vibrate(jSONArray.getLong(0));
        } else {
            if (str.equals("alert")) {
                alert(jSONArray.getString(0), jSONArray.getString(1), jSONArray.getString(2), c0101o);
                return true;
            }
            if (str.equals("confirm")) {
                confirm(jSONArray.getString(0), jSONArray.getString(1), jSONArray.getString(2), c0101o);
                return true;
            }
            if (str.equals("activityStart")) {
                activityStart(jSONArray.getString(0), jSONArray.getString(1));
            } else if (str.equals("activityStop")) {
                activityStop();
            } else if (str.equals("progressStart")) {
                progressStart(jSONArray.getString(0), jSONArray.getString(1));
            } else if (str.equals("progressValue")) {
                progressValue(jSONArray.getInt(0));
            } else {
                if (!str.equals("progressStop")) {
                    return false;
                }
                progressStop();
            }
        }
        c0101o.b();
        return true;
    }

    public void beep(long j) throws InterruptedException {
        Ringtone ringtone = RingtoneManager.getRingtone(this.cordova.a().getBaseContext(), RingtoneManager.getDefaultUri(2));
        if (ringtone != null) {
            for (long j2 = 0; j2 < j; j2 = 1 + j2) {
                ringtone.play();
                long j3 = 5000;
                while (ringtone.isPlaying() && j3 > 0) {
                    j3 -= 100;
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void vibrate(long j) {
        if (j == 0) {
            j = 500;
        }
        ((Vibrator) this.cordova.a().getSystemService("vibrator")).vibrate(j);
    }

    public synchronized void alert(final String str, final String str2, final String str3, final C0101o c0101o) {
        final InterfaceC0102p interfaceC0102p = this.cordova;
        this.cordova.a().runOnUiThread(new Runnable(this) { // from class: c.Notification.1
            @Override // java.lang.Runnable
            public final void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(interfaceC0102p.a());
                builder.setMessage(str);
                builder.setTitle(str2);
                builder.setCancelable(true);
                String str4 = str3;
                final C0101o c0101o2 = c0101o;
                builder.setPositiveButton(str4, new DialogInterface.OnClickListener(this) { // from class: c.Notification.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        c0101o2.a(new C0108v(C0108v.a.OK, 0));
                    }
                });
                final C0101o c0101o3 = c0101o;
                builder.setOnCancelListener(new DialogInterface.OnCancelListener(this) { // from class: c.Notification.1.2
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                        c0101o3.a(new C0108v(C0108v.a.OK, 0));
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    public synchronized void confirm(final String str, final String str2, String str3, final C0101o c0101o) {
        final InterfaceC0102p interfaceC0102p = this.cordova;
        final String[] strArrSplit = str3.split(",");
        this.cordova.a().runOnUiThread(new Runnable(this) { // from class: c.Notification.2
            @Override // java.lang.Runnable
            public final void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(interfaceC0102p.a());
                builder.setMessage(str);
                builder.setTitle(str2);
                builder.setCancelable(true);
                if (strArrSplit.length > 0) {
                    String str4 = strArrSplit[0];
                    final C0101o c0101o2 = c0101o;
                    builder.setNegativeButton(str4, new DialogInterface.OnClickListener(this) { // from class: c.Notification.2.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            c0101o2.a(new C0108v(C0108v.a.OK, 1));
                        }
                    });
                }
                if (strArrSplit.length > 1) {
                    String str5 = strArrSplit[1];
                    final C0101o c0101o3 = c0101o;
                    builder.setNeutralButton(str5, new DialogInterface.OnClickListener(this) { // from class: c.Notification.2.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            c0101o3.a(new C0108v(C0108v.a.OK, 2));
                        }
                    });
                }
                if (strArrSplit.length > 2) {
                    String str6 = strArrSplit[2];
                    final C0101o c0101o4 = c0101o;
                    builder.setPositiveButton(str6, new DialogInterface.OnClickListener(this) { // from class: c.Notification.2.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            c0101o4.a(new C0108v(C0108v.a.OK, 3));
                        }
                    });
                }
                final C0101o c0101o5 = c0101o;
                builder.setOnCancelListener(new DialogInterface.OnCancelListener(this) { // from class: c.Notification.2.4
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                        c0101o5.a(new C0108v(C0108v.a.OK, 0));
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    public synchronized void activityStart(final String str, final String str2) {
        if (this.spinnerDialog != null) {
            this.spinnerDialog.dismiss();
            this.spinnerDialog = null;
        }
        final InterfaceC0102p interfaceC0102p = this.cordova;
        this.cordova.a().runOnUiThread(new Runnable() { // from class: c.Notification.3
            @Override // java.lang.Runnable
            public final void run() {
                Notification.this.spinnerDialog = ProgressDialog.show(interfaceC0102p.a(), str, str2, true, true, new DialogInterface.OnCancelListener() { // from class: c.Notification.3.1
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        Notification.this.spinnerDialog = null;
                    }
                });
            }
        });
    }

    public synchronized void activityStop() {
        if (this.spinnerDialog != null) {
            this.spinnerDialog.dismiss();
            this.spinnerDialog = null;
        }
    }

    public synchronized void progressStart(final String str, final String str2) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
        final InterfaceC0102p interfaceC0102p = this.cordova;
        this.cordova.a().runOnUiThread(new Runnable(this) { // from class: c.Notification.4
            @Override // java.lang.Runnable
            public final void run() {
                this.progressDialog = new ProgressDialog(interfaceC0102p.a());
                this.progressDialog.setProgressStyle(1);
                this.progressDialog.setTitle(str);
                this.progressDialog.setMessage(str2);
                this.progressDialog.setCancelable(true);
                this.progressDialog.setMax(100);
                this.progressDialog.setProgress(0);
                ProgressDialog progressDialog = this.progressDialog;
                final Notification notification = this;
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(this) { // from class: c.Notification.4.1
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        notification.progressDialog = null;
                    }
                });
                this.progressDialog.show();
            }
        });
    }

    public synchronized void progressValue(int i) {
        if (this.progressDialog != null) {
            this.progressDialog.setProgress(i);
        }
    }

    public synchronized void progressStop() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }
}

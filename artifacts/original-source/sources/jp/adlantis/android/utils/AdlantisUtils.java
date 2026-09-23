package jp.adlantis.android.utils;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisUtils {
    public static int adHeightForOrientation(int i) {
        return adSizeForOrientation(i).height();
    }

    public static int adHeightPixels(Context context) {
        return displayPointsToPixels(context, adHeightForOrientation(context.getResources().getConfiguration().orientation));
    }

    public static Rect adSizeForOrientation(int i) {
        switch (i) {
            case 2:
                return new Rect(0, 0, 480, 32);
            default:
                return new Rect(0, 0, 320, 50);
        }
    }

    public static String convertInputToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                break;
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        inputStream.close();
        return sb.toString();
    }

    public static float displayDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    static float displayDensityDpi(Context context) throws NoSuchFieldException {
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(new DisplayMetrics());
        try {
            return DisplayMetrics.class.getField("densityDpi") != null ? r0.getInt(r2) : -1.0f;
        } catch (IllegalAccessException e) {
            return -1.0f;
        } catch (NoSuchFieldException e2) {
            return -1.0f;
        }
    }

    public static int displayPointsToPixels(Context context, int i) {
        return (int) (displayDensity(context) * i);
    }

    public static boolean hasHighResolutionDisplay(Context context) {
        return displayDensityDpi(context) >= 240.0f;
    }

    public static final String md5(String str) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] bArrDigest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bArrDigest) {
                String hexString = Integer.toHexString(b & 255);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int orientation(View view) {
        return view.getResources().getConfiguration().orientation;
    }

    public static String orientationToString(int i) {
        switch (i) {
            case 2:
                return "landscape";
            default:
                return "portrait";
        }
    }

    public static void setUriParamsFromMap(Uri.Builder builder, Map<String, String> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
    }

    public static void setViewVisibilityOnMainThread(final View view, int i) {
        new Handler(Looper.getMainLooper()) { // from class: jp.adlantis.android.utils.AdlantisUtils.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                view.setVisibility(message.what);
            }
        }.sendEmptyMessage(i);
    }
}

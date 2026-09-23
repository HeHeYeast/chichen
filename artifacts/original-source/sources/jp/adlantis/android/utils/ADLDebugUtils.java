package jp.adlantis.android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ADLDebugUtils {
    static String DEBUG_CONTEXT = "ADLDebugUtils";

    static void dumpMemoryInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        logd(" memoryInfo.availMem " + memoryInfo.availMem);
        logd(" memoryInfo.lowMemory " + memoryInfo.lowMemory);
        logd(" memoryInfo.threshold " + memoryInfo.threshold);
        logd(" Debug.getNativeHeapAllocatedSize " + Debug.getNativeHeapAllocatedSize());
        logd(" Debug.getNativeHeapFreeSize " + Debug.getNativeHeapFreeSize());
    }

    public static void dumpSubviewLayout(View view, int i) {
        dumpViewGeometry(view, i);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                dumpSubviewLayout(viewGroup.getChildAt(i2), i + 2);
            }
        }
    }

    public static void dumpViewGeometry(View view, int i) {
        logd(spaces(i) + "view = " + view + " l = " + view.getLeft() + " t = " + view.getTop() + " r = " + view.getRight() + " b = " + view.getBottom());
    }

    static void dumpViewGroupInfo(ViewGroup viewGroup) {
        logd(" childCount=" + viewGroup.getChildCount());
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            logd("view[" + i + "]=" + childAt);
            Rect rect = new Rect();
            Point point = new Point();
            logd("result=" + viewGroup.getChildVisibleRect(childAt, rect, point) + " childRect=" + rect + " childPoint=" + point);
        }
    }

    static void logd(String str) {
        Log.d(DEBUG_CONTEXT, str);
    }

    public static String spaces(int i) {
        return i > 0 ? String.format("%" + i + "s", "") : "";
    }
}

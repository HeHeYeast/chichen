package com.jibao.kitchen;
import android.content.Context;
import org.json.JSONObject;
public final class SaveRepository { public static JSONObject state; public static JSONObject readState(Context context) { return state; } }

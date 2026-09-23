package com.idtinc.ckunit;

import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmUnitDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private float count;
    private float total_count;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public FarmUnitDictionary m5clone() throws CloneNotSupportedException {
        FarmUnitDictionary clone = (FarmUnitDictionary) super.clone();
        return clone;
    }

    public FarmUnitDictionary(JSONObject _jsonObject, float _count, float _total_count) {
        this.count = -1.0f;
        this.total_count = -1.0f;
        try {
            this.count = _jsonObject.getInt("c");
        } catch (JSONException e) {
            setCount(_count);
            Log.i("FarmUnitDictionary", "Error:count");
        }
        Log.i("FarmUnitDictionary", "count:" + this.count);
        try {
            this.total_count = _jsonObject.getInt("t");
        } catch (JSONException e2) {
            setTotalCount(_total_count);
            Log.i("FarmUnitDictionary", "Error:total_count");
        }
        Log.i("FarmUnitDictionary", "total_count:" + this.total_count);
    }

    public FarmUnitDictionary(float _count, float _total_count) {
        this.count = -1.0f;
        this.total_count = -1.0f;
        setCount(_count);
        setTotalCount(_total_count);
    }

    public void setCount(float _count) {
        if (_count < BitmapDescriptorFactory.HUE_RED) {
            this.count = -1.0f;
        } else {
            this.count = _count;
        }
    }

    public float getCount() {
        return this.count;
    }

    public void setTotalCount(float _total_count) {
        if (_total_count < BitmapDescriptorFactory.HUE_RED) {
            this.total_count = -1.0f;
        } else {
            this.total_count = _total_count;
        }
    }

    public float getTotalCount() {
        return this.total_count;
    }

    public JSONObject changeToJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("c", (int) this.count);
            jsonObject.put("t", (int) this.total_count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

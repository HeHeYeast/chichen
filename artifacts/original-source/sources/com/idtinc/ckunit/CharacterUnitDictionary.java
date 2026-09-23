package com.idtinc.ckunit;

import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CharacterUnitDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private float black_seconds;
    private short character_id;
    private short egg_id;
    private float end_seconds;
    private short now_status;
    private int offset_x;
    private int offset_y;
    private float open_seconds;
    private short open_status;
    private String put_date;
    private short sickness_prevention;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CharacterUnitDictionary m3clone() throws CloneNotSupportedException {
        CharacterUnitDictionary clone = (CharacterUnitDictionary) super.clone();
        return clone;
    }

    public CharacterUnitDictionary(JSONObject _jsonObject) {
        this.egg_id = (short) -1;
        this.character_id = (short) -1;
        this.open_status = (short) 0;
        this.now_status = (short) -1;
        this.put_date = "";
        this.end_seconds = -1.0f;
        this.open_seconds = -1.0f;
        this.black_seconds = -1.0f;
        this.sickness_prevention = (short) 0;
        this.offset_x = -999;
        this.offset_y = -999;
        try {
            this.egg_id = (short) _jsonObject.getInt("ei");
        } catch (JSONException e) {
            this.egg_id = (short) -1;
            Log.i("CharacterUnitDictionary", "Error:egg_id");
        }
        Log.i("CharacterUnitDictionary", "egg_id:" + ((int) this.egg_id));
        try {
            this.character_id = (short) _jsonObject.getInt("ci");
        } catch (JSONException e2) {
            this.character_id = (short) -1;
            Log.i("CharacterUnitDictionary", "Error:character_id");
        }
        Log.i("CharacterUnitDictionary", "character_id:" + ((int) this.character_id));
        try {
            this.open_status = (short) _jsonObject.getInt("ost");
        } catch (JSONException e3) {
            this.open_status = (short) 0;
            Log.i("CharacterUnitDictionary", "Error:open_status");
        }
        Log.i("CharacterUnitDictionary", "open_status:" + ((int) this.open_status));
        try {
            this.now_status = (short) _jsonObject.getInt("nst");
        } catch (JSONException e4) {
            this.now_status = (short) -1;
            Log.i("CharacterUnitDictionary", "Error:now_status");
        }
        Log.i("CharacterUnitDictionary", "now_status:" + ((int) this.now_status));
        try {
            this.put_date = _jsonObject.getString("pd");
        } catch (JSONException e5) {
            this.put_date = "";
            Log.i("CharacterUnitDictionary", "Error:put_date");
        }
        Log.i("CharacterUnitDictionary", "put_date:" + this.put_date);
        try {
            this.end_seconds = _jsonObject.getInt("esec");
        } catch (JSONException e6) {
            this.end_seconds = -1.0f;
            Log.i("CharacterUnitDictionary", "Error:end_seconds");
        }
        Log.i("CharacterUnitDictionary", "end_seconds:" + this.end_seconds);
        try {
            this.open_seconds = _jsonObject.getInt("osec");
        } catch (JSONException e7) {
            this.open_seconds = -1.0f;
            Log.i("CharacterUnitDictionary", "Error:open_seconds");
        }
        Log.i("CharacterUnitDictionary", "open_seconds:" + this.open_seconds);
        try {
            this.black_seconds = _jsonObject.getInt("bsec");
        } catch (JSONException e8) {
            this.black_seconds = -1.0f;
            Log.i("CharacterUnitDictionary", "Error:black_seconds");
        }
        Log.i("CharacterUnitDictionary", "black_seconds:" + this.black_seconds);
        try {
            this.sickness_prevention = (short) _jsonObject.getInt("sp");
        } catch (JSONException e9) {
            this.sickness_prevention = (short) 0;
            Log.i("CharacterUnitDictionary", "Error:sickness_prevention");
        }
        Log.i("CharacterUnitDictionary", "sickness_prevention:" + ((int) this.sickness_prevention));
        try {
            this.offset_x = _jsonObject.getInt("ox");
        } catch (JSONException e10) {
            this.offset_x = -999;
            Log.i("CharacterUnitDictionary", "Error:offset_x");
        }
        Log.i("CharacterUnitDictionary", "offset_x:" + this.offset_x);
        try {
            this.offset_y = _jsonObject.getInt("oy");
        } catch (JSONException e11) {
            this.offset_y = -999;
            Log.i("CharacterUnitDictionary", "Error:offset_y");
        }
        Log.i("CharacterUnitDictionary", "offset_y:" + this.offset_y);
    }

    public CharacterUnitDictionary() {
        this.egg_id = (short) -1;
        this.character_id = (short) -1;
        this.open_status = (short) 0;
        this.now_status = (short) -1;
        this.put_date = "";
        this.end_seconds = -1.0f;
        this.open_seconds = -1.0f;
        this.black_seconds = -1.0f;
        this.sickness_prevention = (short) 0;
        this.offset_x = -999;
        this.offset_y = -999;
        this.egg_id = (short) -1;
        this.character_id = (short) -1;
        this.open_status = (short) 0;
        this.now_status = (short) -1;
        this.put_date = "";
        this.end_seconds = -1.0f;
        this.open_seconds = -1.0f;
        this.black_seconds = -1.0f;
        this.sickness_prevention = (short) 0;
        this.offset_x = -999;
        this.offset_y = -999;
    }

    public void setEggId(short _egg_id) {
        if (_egg_id < -1) {
            this.egg_id = (short) -1;
        } else {
            this.egg_id = _egg_id;
        }
    }

    public short getEggId() {
        return this.egg_id;
    }

    public void setCharacterId(short _character_id) {
        if (_character_id < -1) {
            this.character_id = (short) -1;
        } else {
            this.character_id = _character_id;
        }
    }

    public short getCharacterId() {
        return this.character_id;
    }

    public void setOpenStatus(short _open_status) {
        if (_open_status < 0) {
            this.open_status = (short) 0;
        } else {
            this.open_status = _open_status;
        }
    }

    public short getOpenStatus() {
        return this.open_status;
    }

    public void setNowStatus(short _now_status) {
        if (_now_status < -1) {
            this.now_status = (short) -1;
        } else {
            this.now_status = _now_status;
        }
    }

    public short getNowStatus() {
        return this.now_status;
    }

    public void setPutDate(String _put_date) {
        if (_put_date == null || _put_date.length() <= 0) {
            this.put_date = "";
        } else {
            this.put_date = _put_date;
        }
    }

    public String getPutDate() {
        return this.put_date;
    }

    public void setEndSeconds(float _end_seconds) {
        if (_end_seconds < BitmapDescriptorFactory.HUE_RED) {
            this.end_seconds = -1.0f;
        } else {
            this.end_seconds = _end_seconds;
        }
    }

    public float getEndSeconds() {
        return this.end_seconds;
    }

    public void setOpenSeconds(float _open_seconds) {
        if (_open_seconds < BitmapDescriptorFactory.HUE_RED) {
            this.open_seconds = -1.0f;
        } else {
            this.open_seconds = _open_seconds;
        }
    }

    public float getOpenSeconds() {
        return this.open_seconds;
    }

    public void setBlackSeconds(float _black_seconds) {
        if (_black_seconds < BitmapDescriptorFactory.HUE_RED) {
            this.black_seconds = -1.0f;
        } else {
            this.black_seconds = _black_seconds;
        }
    }

    public float getBlackSeconds() {
        return this.black_seconds;
    }

    public void setSicknessPrevention(short _sickness_prevention) {
        if (_sickness_prevention < 0) {
            this.sickness_prevention = (short) 0;
        } else {
            this.sickness_prevention = _sickness_prevention;
        }
    }

    public short getSicknessPrevention() {
        return this.sickness_prevention;
    }

    public void setOffsetX(int _offset_x) {
        this.offset_x = _offset_x;
    }

    public int getOffsetX() {
        return this.offset_x;
    }

    public void setOffsetY(int _offset_y) {
        this.offset_y = _offset_y;
    }

    public int getOffsetY() {
        return this.offset_y;
    }

    public JSONObject changeToJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ei", (int) this.egg_id);
            jsonObject.put("ci", (int) this.character_id);
            jsonObject.put("ost", (int) this.open_status);
            jsonObject.put("nst", (int) this.now_status);
            jsonObject.put("pd", this.put_date.replaceAll("/", "<time_slash>"));
            jsonObject.put("esec", (int) this.end_seconds);
            jsonObject.put("osec", (int) this.open_seconds);
            jsonObject.put("bsec", (int) this.black_seconds);
            jsonObject.put("sp", (int) this.sickness_prevention);
            jsonObject.put("ox", this.offset_x);
            jsonObject.put("oy", this.offset_y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

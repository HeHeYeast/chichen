package com.idtinc.ckunit;

import android.util.Log;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ToolUnitDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private String checked_date;
    private short count;
    private String fixed_date;
    private short hp;
    private short level;
    private short select;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ToolUnitDictionary m10clone() throws CloneNotSupportedException {
        ToolUnitDictionary clone = (ToolUnitDictionary) super.clone();
        return clone;
    }

    public ToolUnitDictionary(JSONObject _jsonObject, short _level, short _hp, short _count, short _select, String _fixed_date, String _checked_date) {
        this.level = (short) -2;
        this.hp = (short) 100;
        this.count = (short) -1;
        this.select = (short) 0;
        this.fixed_date = "";
        this.checked_date = "";
        try {
            this.level = (short) _jsonObject.getInt("lv");
        } catch (JSONException e) {
            setLevel(_level);
            Log.i("ToolUnitDictionary", "Error:level");
        }
        Log.i("ToolUnitDictionary", "level:" + ((int) this.level));
        try {
            this.hp = (short) _jsonObject.getInt("hp");
        } catch (JSONException e2) {
            setHp(_hp);
            Log.i("ToolUnitDictionary", "Error:hp");
        }
        Log.i("ToolUnitDictionary", "hp:" + ((int) this.hp));
        try {
            this.count = (short) _jsonObject.getInt("ct");
        } catch (JSONException e3) {
            setCount(_count);
            Log.i("ToolUnitDictionary", "Error:count");
        }
        Log.i("ToolUnitDictionary", "count:" + ((int) this.count));
        try {
            this.select = (short) _jsonObject.getInt("sl");
        } catch (JSONException e4) {
            setSelect(_select);
            Log.i("ToolUnitDictionary", "Error:select");
        }
        Log.i("ToolUnitDictionary", "select:" + ((int) this.select));
        try {
            this.fixed_date = _jsonObject.getString("fd");
        } catch (JSONException e5) {
            setFixedDate(_fixed_date);
            Log.i("CharacterUnitDictionary", "Error:fixed_date");
        }
        Log.i("CharacterUnitDictionary", "fixed_date:" + this.fixed_date);
        try {
            this.checked_date = _jsonObject.getString("cd");
        } catch (JSONException e6) {
            setCheckedDate(_checked_date);
            Log.i("CharacterUnitDictionary", "Error:checked_date");
        }
        Log.i("CharacterUnitDictionary", "checked_date:" + this.checked_date);
    }

    public ToolUnitDictionary(short _level, short _hp, short _count, short _select, String _fixed_date, String _checked_date) {
        this.level = (short) -2;
        this.hp = (short) 100;
        this.count = (short) -1;
        this.select = (short) 0;
        this.fixed_date = "";
        this.checked_date = "";
        setLevel(_level);
        setHp(_hp);
        setCount(_count);
        setSelect(_select);
        setFixedDate(_fixed_date);
        setCheckedDate(_checked_date);
    }

    public void setLevel(short _level) {
        if (_level < -2) {
            this.level = (short) -2;
        } else {
            this.level = _level;
        }
    }

    public short getLevel() {
        return this.level;
    }

    public void setHp(short _hp) {
        if (_hp < 0) {
            this.hp = (short) 0;
        } else {
            this.hp = _hp;
        }
    }

    public short getHp() {
        return this.hp;
    }

    public void setCount(short _count) {
        if (_count < -1) {
            this.count = (short) -1;
        } else {
            this.count = _count;
        }
    }

    public short getCount() {
        return this.count;
    }

    public void setSelect(short _select) {
        if (_select < 0) {
            this.select = (short) 0;
        } else {
            this.select = _select;
        }
    }

    public short getSelect() {
        return this.select;
    }

    public void setFixedDate(String _fixed_date) {
        if (_fixed_date == null || _fixed_date.length() <= 0) {
            this.fixed_date = "";
        } else {
            this.fixed_date = _fixed_date;
        }
    }

    public String getFixedDate() {
        return this.fixed_date;
    }

    public void setCheckedDate(String _checked_date) {
        if (_checked_date == null || _checked_date.length() <= 0) {
            this.checked_date = "";
        } else {
            this.checked_date = _checked_date;
        }
    }

    public String getCheckedDate() {
        return this.checked_date;
    }

    public JSONObject changeToJSONObject(short _index) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (_index == 0) {
            jsonObject.put("lv", (int) this.level);
            jsonObject.put("hp", (int) this.hp);
            jsonObject.put("fd", this.fixed_date.replaceAll("/", "<time_slash>"));
            jsonObject.put("cd", this.checked_date.replaceAll("/", "<time_slash>"));
        } else if (_index == 1) {
            jsonObject.put("lv", (int) this.level);
        } else if (_index == 2) {
            jsonObject.put("ct", (int) this.count);
            jsonObject.put("sl", (int) this.select);
        } else {
            if (_index == 3) {
                jsonObject.put("ct", (int) this.count);
            }
            return jsonObject;
        }
        return jsonObject;
    }
}

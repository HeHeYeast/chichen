package com.idtinc.ckunit;

import java.io.Serializable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CharacterDataDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private short id = -1;
    private String title_en = "";
    private String title_ja = "";
    private String title_zh_TW = "";
    private String title_zh_CN = "";
    private short cp_0 = 0;
    private short cp_1 = 0;
    private short rate = 0;
    private short tool_1_0_id = -1;
    private short tool_2_0_id = -1;
    private short tool_2_1_id = -1;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CharacterDataDictionary m2clone() throws CloneNotSupportedException {
        CharacterDataDictionary clone = (CharacterDataDictionary) super.clone();
        return clone;
    }

    public void setId(short _id) {
        if (_id < 0) {
            this.id = (short) -1;
        } else {
            this.id = _id;
        }
    }

    public short getId() {
        return this.id;
    }

    public void setTitleEn(String _title_en) {
        if (_title_en == null || _title_en.length() <= 0) {
            this.title_en = "";
        } else {
            this.title_en = _title_en;
        }
    }

    public String getTitleEn() {
        return this.title_en;
    }

    public void setTitleJa(String _title_ja) {
        if (_title_ja == null || _title_ja.length() <= 0) {
            this.title_ja = "";
        } else {
            this.title_ja = _title_ja;
        }
    }

    public String getTitleJa() {
        return this.title_ja;
    }

    public void setTitleZhTW(String _title_zh_TW) {
        if (_title_zh_TW == null || _title_zh_TW.length() <= 0) {
            this.title_zh_TW = "";
        } else {
            this.title_zh_TW = _title_zh_TW;
        }
    }

    public String getTitleZhTW() {
        return this.title_zh_TW;
    }

    public void setTitleZhCN(String _title_zh_CN) {
        if (_title_zh_CN == null || _title_zh_CN.length() <= 0) {
            this.title_zh_CN = "";
        } else {
            this.title_zh_CN = _title_zh_CN;
        }
    }

    public String getTitleZhCN() {
        return this.title_zh_CN;
    }

    public void setCp0(short _cp_0) {
        if (_cp_0 < 0) {
            this.cp_0 = (short) 0;
        } else {
            this.cp_0 = _cp_0;
        }
    }

    public short getCp0() {
        return this.cp_0;
    }

    public void setCp1(short _cp_1) {
        if (_cp_1 < 0) {
            this.cp_1 = (short) 0;
        } else {
            this.cp_1 = _cp_1;
        }
    }

    public short getCp1() {
        return this.cp_1;
    }

    public void setRate(short _rate) {
        if (_rate < 0) {
            this.rate = (short) 0;
        } else {
            this.rate = _rate;
        }
    }

    public short getRate() {
        return this.rate;
    }

    public void setTool_1_0_Id(short _tool_1_0_id) {
        if (_tool_1_0_id < 0) {
            this.tool_1_0_id = (short) -1;
        } else {
            this.tool_1_0_id = _tool_1_0_id;
        }
    }

    public short getTool_1_0_Id() {
        return this.tool_1_0_id;
    }

    public void setTool_2_0_Id(short _tool_2_0_id) {
        if (_tool_2_0_id < 0) {
            this.tool_2_0_id = (short) -1;
        } else {
            this.tool_2_0_id = _tool_2_0_id;
        }
    }

    public short getTool_2_0_Id() {
        return this.tool_2_0_id;
    }

    public void setTool_2_1_Id(short _tool_2_1_id) {
        if (_tool_2_1_id < 0) {
            this.tool_2_1_id = (short) -1;
        } else {
            this.tool_2_1_id = _tool_2_1_id;
        }
    }

    public short getTool_2_1_Id() {
        return this.tool_2_1_id;
    }
}

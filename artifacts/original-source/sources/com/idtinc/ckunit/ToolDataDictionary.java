package com.idtinc.ckunit;

import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ToolDataDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private int buy_cp;
    private short id;
    private String title_en;
    private String title_ja;
    private String title_zh_CN;
    private String title_zh_TW;
    public ArrayList<ToolLevelDictionary> toolLevelsArrayList;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ToolDataDictionary m8clone() throws CloneNotSupportedException {
        ToolDataDictionary clone = (ToolDataDictionary) super.clone();
        clone.toolLevelsArrayList = (ArrayList) this.toolLevelsArrayList.clone();
        return clone;
    }

    public ToolDataDictionary() {
        this.id = (short) -1;
        this.title_en = "";
        this.title_ja = "";
        this.title_zh_TW = "";
        this.title_zh_CN = "";
        this.buy_cp = -1;
        this.toolLevelsArrayList = null;
        this.id = (short) -1;
        this.title_en = "";
        this.title_ja = "";
        this.title_zh_TW = "";
        this.title_zh_CN = "";
        this.buy_cp = -1;
        this.toolLevelsArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ToolLevelDictionary newToolLevelDictionary = new ToolLevelDictionary();
            this.toolLevelsArrayList.add(newToolLevelDictionary);
        }
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

    public void setBuyCp(int _buy_cp) {
        if (_buy_cp < 0) {
            this.buy_cp = -1;
        } else {
            this.buy_cp = _buy_cp;
        }
    }

    public int getBuyCp() {
        return this.buy_cp;
    }
}

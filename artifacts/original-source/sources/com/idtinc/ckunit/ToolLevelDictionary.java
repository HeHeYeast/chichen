package com.idtinc.ckunit;

import java.io.Serializable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ToolLevelDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private short lv_black_min;
    private int lv_buy_cp;
    private int lv_cook_cp;
    private int lv_fix_cp;
    private short lv_min;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ToolLevelDictionary m9clone() throws CloneNotSupportedException {
        ToolLevelDictionary clone = (ToolLevelDictionary) super.clone();
        return clone;
    }

    public ToolLevelDictionary() {
        this.lv_buy_cp = -1;
        this.lv_fix_cp = -1;
        this.lv_cook_cp = -1;
        this.lv_min = (short) -1;
        this.lv_black_min = (short) -1;
        this.lv_buy_cp = -1;
        this.lv_fix_cp = -1;
        this.lv_cook_cp = -1;
        this.lv_min = (short) -1;
        this.lv_black_min = (short) -1;
    }

    public void setLvBuyCp(int _lv_buy_cp) {
        if (_lv_buy_cp < 0) {
            this.lv_buy_cp = -1;
        } else {
            this.lv_buy_cp = _lv_buy_cp;
        }
    }

    public int getLvBuyCp() {
        return this.lv_buy_cp;
    }

    public void setLvFixCp(int _lv_fix_cp) {
        if (_lv_fix_cp < 0) {
            this.lv_fix_cp = -1;
        } else {
            this.lv_fix_cp = _lv_fix_cp;
        }
    }

    public int getLvFixCp() {
        return this.lv_fix_cp;
    }

    public void setLvCookCp(int _lv_cook_cp) {
        if (_lv_cook_cp < 0) {
            this.lv_cook_cp = -1;
        } else {
            this.lv_cook_cp = _lv_cook_cp;
        }
    }

    public int getLvCookCp() {
        return this.lv_cook_cp;
    }

    public void setLvMin(short _lv_min) {
        if (_lv_min < 0) {
            this.lv_min = (short) -1;
        } else {
            this.lv_min = _lv_min;
        }
    }

    public short getLvMin() {
        return this.lv_min;
    }

    public void setLvBlackMin(short _lv_black_mim) {
        if (_lv_black_mim < 0) {
            this.lv_black_min = (short) -1;
        } else {
            this.lv_black_min = _lv_black_mim;
        }
    }

    public short getLvBlackMin() {
        return this.lv_black_min;
    }
}

package com.idtinc.ckunit;

import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MainSavesDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    public ArrayList<TimeSaveDictionary> timeSavesArrayList;
    private short version_level;
    private String version_number;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public MainSavesDictionary m6clone() throws CloneNotSupportedException {
        MainSavesDictionary clone = (MainSavesDictionary) super.clone();
        clone.timeSavesArrayList = (ArrayList) this.timeSavesArrayList.clone();
        return clone;
    }

    public MainSavesDictionary(Short _version_level, String _version_number) {
        this.version_level = (short) -1;
        this.version_number = "";
        if (_version_level.shortValue() < 0) {
            this.version_level = (short) 0;
        } else {
            this.version_level = _version_level.shortValue();
        }
        if (_version_number == null || _version_number.length() <= 0) {
            this.version_number = "";
        } else {
            this.version_number = _version_number;
        }
        this.timeSavesArrayList = null;
        this.timeSavesArrayList = new ArrayList<>();
    }

    public void setVersionLevel(short _version_level) {
        if (_version_level < 0) {
            this.version_level = (short) -1;
        } else {
            this.version_level = _version_level;
        }
    }

    public short getVersionLevel() {
        return this.version_level;
    }

    public void setVersionNumber(String _version_number) {
        if (_version_number == null || _version_number.length() <= 0) {
            this.version_number = "1.0.0";
        } else {
            this.version_number = _version_number;
        }
    }

    public String getVersionNumber() {
        return this.version_number;
    }

    public void initTimeSavesArrayList() {
        this.timeSavesArrayList = null;
        this.timeSavesArrayList = new ArrayList<>();
    }
}

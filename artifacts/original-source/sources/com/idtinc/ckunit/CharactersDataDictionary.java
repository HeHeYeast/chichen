package com.idtinc.ckunit;

import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CharactersDataDictionary implements Cloneable, Serializable {
    private short egg_id = -1;
    public ArrayList<CharacterDataDictionary> charactersDataArrayList = null;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CharactersDataDictionary m4clone() throws CloneNotSupportedException {
        CharactersDataDictionary clone = (CharactersDataDictionary) super.clone();
        clone.charactersDataArrayList = (ArrayList) this.charactersDataArrayList.clone();
        return clone;
    }

    public void setEggId(short _egg_id) {
        if (_egg_id < 0) {
            this.egg_id = (short) -1;
        } else {
            this.egg_id = _egg_id;
        }
    }

    public short getEggId() {
        return this.egg_id;
    }
}

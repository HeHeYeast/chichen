package com.idtinc.ckunit;

import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ToolsDataDictionary implements Cloneable, Serializable {
    private short tool_id = -1;
    public ArrayList<ToolDataDictionary> toolsDataArrayList = null;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ToolsDataDictionary m11clone() throws CloneNotSupportedException {
        ToolsDataDictionary clone = (ToolsDataDictionary) super.clone();
        clone.toolsDataArrayList = (ArrayList) this.toolsDataArrayList.clone();
        return clone;
    }

    public void setToolId(short _tool_id) {
        if (_tool_id < 0) {
            this.tool_id = (short) -1;
        } else {
            this.tool_id = _tool_id;
        }
    }

    public short getToolId() {
        return this.tool_id;
    }
}

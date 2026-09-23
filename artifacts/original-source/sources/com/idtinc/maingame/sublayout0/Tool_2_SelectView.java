package com.idtinc.maingame.sublayout0;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckunit.ToolUnitDictionary;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Tool_2_SelectView {
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public short tool_2_0;
    public short tool_2_1;
    public short tool_2_2;
    private float zoomRate;

    public Tool_2_SelectView(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.tool_2_0 = (short) -1;
        this.tool_2_1 = (short) -1;
        this.tool_2_2 = (short) -1;
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.tool_2_0 = (short) -1;
        this.tool_2_1 = (short) -1;
        this.tool_2_2 = (short) -1;
    }

    public void refresh() {
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList;
        this.tool_2_0 = (short) -1;
        this.tool_2_1 = (short) -1;
        this.tool_2_2 = (short) -1;
        if (this.appDelegate.timeSaveDictionary != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 3 && (toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(2)) != null) {
            short nowButtonIndex = 0;
            for (short i = 0; i < toolUnitDictionarysArrayList.size(); i = (short) (i + 1)) {
                ToolUnitDictionary toolUnitDictionary = toolUnitDictionarysArrayList.get(i);
                if (toolUnitDictionary != null) {
                    short countShort = toolUnitDictionary.getCount();
                    if (countShort > 0 && nowButtonIndex < this.appDelegate.TOOL_2_SELECTBUTTON_CNT) {
                        short selectShort = toolUnitDictionary.getSelect();
                        if (selectShort != 1) {
                            continue;
                        } else {
                            if (nowButtonIndex == 0) {
                                this.tool_2_0 = i;
                            } else if (nowButtonIndex == 1) {
                                this.tool_2_1 = i;
                            } else if (nowButtonIndex == 2) {
                                this.tool_2_2 = i;
                            }
                            nowButtonIndex = (short) (nowButtonIndex + 1);
                            if (nowButtonIndex >= this.appDelegate.TOOL_2_SELECTBUTTON_CNT) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void useSelectedTool2() {
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList;
        ToolUnitDictionary toolUnitDictionary;
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 3 && (toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(2)) != null) {
            for (int i = 0; i < toolUnitDictionarysArrayList.size(); i++) {
                if ((i == this.tool_2_0 || i == this.tool_2_1 || i == this.tool_2_2) && (toolUnitDictionary = toolUnitDictionarysArrayList.get(i)) != null) {
                    short countShort = toolUnitDictionary.getCount();
                    if (countShort > 0) {
                        toolUnitDictionary.setCount((short) (countShort - 1));
                    }
                    toolUnitDictionary.setSelect((short) 0);
                }
            }
            this.tool_2_0 = (short) -1;
            this.tool_2_1 = (short) -1;
            this.tool_2_2 = (short) -1;
            refresh();
        }
    }

    public void onDestroy() {
        this.appDelegate = null;
    }
}

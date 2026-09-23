package com.idtinc.ckunit;

import android.util.Log;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ToolDataDictionarySAXService {
    public ArrayList<ToolDataDictionary> getTools(InputStream inputStream) throws Throwable {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ToolDataHandel toolDataHandel = new ToolDataHandel(this, null);
        parser.parse(inputStream, toolDataHandel);
        return toolDataHandel.getTools();
    }

    private class ToolDataHandel extends DefaultHandler {
        private String preTAG;
        private ToolDataDictionary toolData;
        private ArrayList<ToolDataDictionary> toolsData;

        private ToolDataHandel() {
            this.toolsData = null;
            this.toolData = null;
        }

        /* synthetic */ ToolDataHandel(ToolDataDictionarySAXService toolDataDictionarySAXService, ToolDataHandel toolDataHandel) {
            this();
        }

        public ArrayList<ToolDataDictionary> getTools() {
            return this.toolsData;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (this.preTAG != null) {
                String data = new String(ch, start, length);
                if ("id".equals(this.preTAG)) {
                    this.toolData.setId(Short.parseShort(data));
                    Log.d(this.preTAG, data);
                    return;
                }
                if ("title_en".equals(this.preTAG)) {
                    this.toolData.setTitleEn(data);
                    Log.d(this.preTAG, data);
                    return;
                }
                if ("title_ja".equals(this.preTAG)) {
                    this.toolData.setTitleJa(data);
                    Log.d(this.preTAG, data);
                    return;
                }
                if ("title_zh_TW".equals(this.preTAG)) {
                    this.toolData.setTitleZhTW(data);
                    Log.d(this.preTAG, data);
                    return;
                }
                if ("title_zh_CN".equals(this.preTAG)) {
                    this.toolData.setTitleZhCN(data);
                    Log.d(this.preTAG, data);
                    return;
                }
                if ("buy_cp".equals(this.preTAG)) {
                    this.toolData.setBuyCp(Integer.parseInt(data));
                    Log.d(this.preTAG, data);
                    return;
                }
                if ("lv_0_buy_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 1 && this.toolData.toolLevelsArrayList.get(0) != null) {
                        this.toolData.toolLevelsArrayList.get(0).setLvBuyCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_1_buy_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 2 && this.toolData.toolLevelsArrayList.get(1) != null) {
                        this.toolData.toolLevelsArrayList.get(1).setLvBuyCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_2_buy_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 3 && this.toolData.toolLevelsArrayList.get(2) != null) {
                        this.toolData.toolLevelsArrayList.get(2).setLvBuyCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_3_buy_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 4 && this.toolData.toolLevelsArrayList.get(3) != null) {
                        this.toolData.toolLevelsArrayList.get(3).setLvBuyCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_4_buy_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 5 && this.toolData.toolLevelsArrayList.get(4) != null) {
                        this.toolData.toolLevelsArrayList.get(4).setLvBuyCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_0_fix_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 1 && this.toolData.toolLevelsArrayList.get(0) != null) {
                        this.toolData.toolLevelsArrayList.get(0).setLvFixCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_1_fix_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 2 && this.toolData.toolLevelsArrayList.get(1) != null) {
                        this.toolData.toolLevelsArrayList.get(1).setLvFixCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_2_fix_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 3 && this.toolData.toolLevelsArrayList.get(2) != null) {
                        this.toolData.toolLevelsArrayList.get(2).setLvFixCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_3_fix_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 4 && this.toolData.toolLevelsArrayList.get(3) != null) {
                        this.toolData.toolLevelsArrayList.get(3).setLvFixCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_4_fix_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 5 && this.toolData.toolLevelsArrayList.get(4) != null) {
                        this.toolData.toolLevelsArrayList.get(4).setLvFixCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_0_cook_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 1 && this.toolData.toolLevelsArrayList.get(0) != null) {
                        this.toolData.toolLevelsArrayList.get(0).setLvCookCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_1_cook_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 2 && this.toolData.toolLevelsArrayList.get(1) != null) {
                        this.toolData.toolLevelsArrayList.get(1).setLvCookCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_2_cook_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 3 && this.toolData.toolLevelsArrayList.get(2) != null) {
                        this.toolData.toolLevelsArrayList.get(2).setLvCookCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_3_cook_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 4 && this.toolData.toolLevelsArrayList.get(3) != null) {
                        this.toolData.toolLevelsArrayList.get(3).setLvCookCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_4_cook_cp".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 5 && this.toolData.toolLevelsArrayList.get(4) != null) {
                        this.toolData.toolLevelsArrayList.get(4).setLvCookCp(Integer.parseInt(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_0_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 1 && this.toolData.toolLevelsArrayList.get(0) != null) {
                        this.toolData.toolLevelsArrayList.get(0).setLvMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_1_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 2 && this.toolData.toolLevelsArrayList.get(1) != null) {
                        this.toolData.toolLevelsArrayList.get(1).setLvMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_2_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 3 && this.toolData.toolLevelsArrayList.get(2) != null) {
                        this.toolData.toolLevelsArrayList.get(2).setLvMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_3_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 4 && this.toolData.toolLevelsArrayList.get(3) != null) {
                        this.toolData.toolLevelsArrayList.get(3).setLvMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_4_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 5 && this.toolData.toolLevelsArrayList.get(4) != null) {
                        this.toolData.toolLevelsArrayList.get(4).setLvMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_0_black_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 1 && this.toolData.toolLevelsArrayList.get(0) != null) {
                        this.toolData.toolLevelsArrayList.get(0).setLvBlackMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_1_black_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 2 && this.toolData.toolLevelsArrayList.get(1) != null) {
                        this.toolData.toolLevelsArrayList.get(1).setLvBlackMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_2_black_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 3 && this.toolData.toolLevelsArrayList.get(2) != null) {
                        this.toolData.toolLevelsArrayList.get(2).setLvBlackMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_3_black_min".equals(this.preTAG)) {
                    if (this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 4 && this.toolData.toolLevelsArrayList.get(3) != null) {
                        this.toolData.toolLevelsArrayList.get(3).setLvBlackMin(Short.parseShort(data));
                        Log.d(this.preTAG, data);
                        return;
                    }
                    return;
                }
                if ("lv_4_black_min".equals(this.preTAG) && this.toolData.toolLevelsArrayList != null && this.toolData.toolLevelsArrayList.size() >= 5 && this.toolData.toolLevelsArrayList.get(4) != null) {
                    this.toolData.toolLevelsArrayList.get(4).setLvBlackMin(Short.parseShort(data));
                    Log.d(this.preTAG, data);
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("tool".equals(localName)) {
                this.toolData = new ToolDataDictionary();
            }
            this.preTAG = localName;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            this.toolsData = new ArrayList<>();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("tool".equals(localName)) {
                this.toolsData.add(this.toolData);
                this.toolData = null;
            }
            this.preTAG = null;
        }
    }
}

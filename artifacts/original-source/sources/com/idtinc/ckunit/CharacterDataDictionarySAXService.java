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
public class CharacterDataDictionarySAXService {
    public ArrayList<CharacterDataDictionary> getCharacters(InputStream inputStream) throws Throwable {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        CharacterDataHandel characterDataHandel = new CharacterDataHandel(this, null);
        parser.parse(inputStream, characterDataHandel);
        return characterDataHandel.getCharacters();
    }

    private class CharacterDataHandel extends DefaultHandler {
        private CharacterDataDictionary characterData;
        private ArrayList<CharacterDataDictionary> charactersData;
        private String preTAG;

        private CharacterDataHandel() {
            this.charactersData = null;
            this.characterData = null;
        }

        /* synthetic */ CharacterDataHandel(CharacterDataDictionarySAXService characterDataDictionarySAXService, CharacterDataHandel characterDataHandel) {
            this();
        }

        public ArrayList<CharacterDataDictionary> getCharacters() {
            return this.charactersData;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (this.preTAG != null) {
                String data = new String(ch, start, length);
                if ("id".equals(this.preTAG)) {
                    this.characterData.setId(Short.parseShort(data));
                    return;
                }
                if ("title_en".equals(this.preTAG)) {
                    this.characterData.setTitleEn(data);
                    return;
                }
                if ("title_ja".equals(this.preTAG)) {
                    this.characterData.setTitleJa(data);
                    return;
                }
                if ("title_zh_TW".equals(this.preTAG)) {
                    this.characterData.setTitleZhTW(data);
                    return;
                }
                if ("title_zh_CN".equals(this.preTAG)) {
                    this.characterData.setTitleZhCN(data);
                    return;
                }
                if ("cp_0".equals(this.preTAG)) {
                    this.characterData.setCp0(Short.parseShort(data));
                    return;
                }
                if ("cp_1".equals(this.preTAG)) {
                    this.characterData.setCp1(Short.parseShort(data));
                    return;
                }
                if ("rate".equals(this.preTAG)) {
                    this.characterData.setRate(Short.parseShort(data));
                    Log.d(this.preTAG, data);
                } else if ("tool_1_0_id".equals(this.preTAG)) {
                    this.characterData.setTool_1_0_Id(Short.parseShort(data));
                } else if ("tool_2_0_id".equals(this.preTAG)) {
                    this.characterData.setTool_2_0_Id(Short.parseShort(data));
                } else if ("tool_2_1_id".equals(this.preTAG)) {
                    this.characterData.setTool_2_1_Id(Short.parseShort(data));
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("character".equals(localName)) {
                this.characterData = new CharacterDataDictionary();
            }
            this.preTAG = localName;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            this.charactersData = new ArrayList<>();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("character".equals(localName)) {
                this.charactersData.add(this.characterData);
                this.characterData = null;
            }
            this.preTAG = null;
        }
    }
}

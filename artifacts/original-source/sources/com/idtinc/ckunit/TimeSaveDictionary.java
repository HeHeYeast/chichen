package com.idtinc.ckunit;

import android.annotation.SuppressLint;
import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class TimeSaveDictionary implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    public ArrayList<CharacterUnitDictionary> characterUnitDictionarysArrayList;
    private String date;
    private short egg_id_select_index;
    public ArrayList<Object> farmUnitDictionarysArrayList;
    private String first_date;
    private String key_number;
    private String player_name;
    private float point;
    public ArrayList<Object> toolUnitDictionarysArrayList;
    private float tool_1_selectview_endseconds;
    private short tool_1_selectview_nowbuttonindex;
    private String tool_1_selectview_startdate;
    private short tool_1_selectview_tool_2_0index;
    private short tool_1_selectview_tool_2_1index;
    private short tool_1_selectview_tool_2_2index;

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public TimeSaveDictionary m7clone() throws CloneNotSupportedException {
        TimeSaveDictionary clone = (TimeSaveDictionary) super.clone();
        clone.characterUnitDictionarysArrayList = (ArrayList) this.characterUnitDictionarysArrayList.clone();
        clone.farmUnitDictionarysArrayList = (ArrayList) this.farmUnitDictionarysArrayList.clone();
        clone.toolUnitDictionarysArrayList = (ArrayList) this.toolUnitDictionarysArrayList.clone();
        return clone;
    }

    public TimeSaveDictionary(AppDelegate _appDelegate, String jsonString) throws JSONException {
        JSONObject jsonObject;
        JSONArray characterUnitDictionarysArrayListJSONArray;
        JSONArray farmUnitDictionarysArrayListJSONArray;
        JSONArray toolUnitDictionarysArrayListJSONArray;
        JSONArray toolDictionarysArrayListJSONArray;
        ToolsDataDictionary toolsDataDictionary;
        JSONObject taljsonObject;
        JSONArray unitDictionarysArrayListJSONArray;
        CharactersDataDictionary charactersDataDictionary;
        JSONObject faljsonObject;
        JSONObject caljsonObject;
        this.key_number = "";
        this.date = "";
        this.first_date = "";
        this.player_name = "Player";
        this.point = BitmapDescriptorFactory.HUE_RED;
        this.egg_id_select_index = (short) 0;
        this.tool_1_selectview_nowbuttonindex = (short) -1;
        this.tool_1_selectview_startdate = "";
        this.tool_1_selectview_endseconds = BitmapDescriptorFactory.HUE_RED;
        this.tool_1_selectview_tool_2_0index = (short) -1;
        this.tool_1_selectview_tool_2_1index = (short) -1;
        this.tool_1_selectview_tool_2_2index = (short) -1;
        this.characterUnitDictionarysArrayList = null;
        this.farmUnitDictionarysArrayList = null;
        this.toolUnitDictionarysArrayList = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            jsonObject = null;
        }
        if (jsonObject != null) {
            try {
                this.key_number = jsonObject.getString("kn");
            } catch (JSONException e2) {
                this.key_number = "";
            }
            try {
                this.date = jsonObject.getString("d");
            } catch (JSONException e3) {
                this.date = "";
            }
            try {
                this.first_date = jsonObject.getString("fd");
            } catch (JSONException e4) {
                this.first_date = "";
            }
            try {
                this.player_name = jsonObject.getString("pn");
            } catch (JSONException e5) {
                this.player_name = "Player";
            }
            try {
                this.point = jsonObject.getInt("pt");
            } catch (JSONException e6) {
                this.point = BitmapDescriptorFactory.HUE_RED;
            }
            if (this.point < BitmapDescriptorFactory.HUE_RED) {
                this.point = BitmapDescriptorFactory.HUE_RED;
            }
            try {
                this.egg_id_select_index = (short) jsonObject.getInt("eis");
            } catch (JSONException e7) {
                this.egg_id_select_index = (short) -1;
            }
            try {
                this.tool_1_selectview_nowbuttonindex = (short) jsonObject.getInt("t1sn");
            } catch (JSONException e8) {
                this.tool_1_selectview_nowbuttonindex = (short) -1;
            }
            try {
                this.tool_1_selectview_startdate = jsonObject.getString("t1ss");
            } catch (JSONException e9) {
                this.tool_1_selectview_startdate = "";
            }
            try {
                this.tool_1_selectview_endseconds = (float) jsonObject.getDouble("t1se");
            } catch (JSONException e10) {
                this.tool_1_selectview_endseconds = BitmapDescriptorFactory.HUE_RED;
            }
            try {
                this.tool_1_selectview_tool_2_0index = (short) jsonObject.getInt("t20");
            } catch (JSONException e11) {
                this.tool_1_selectview_tool_2_0index = (short) -1;
            }
            try {
                this.tool_1_selectview_tool_2_1index = (short) jsonObject.getInt("t21");
            } catch (JSONException e12) {
                this.tool_1_selectview_tool_2_1index = (short) -1;
            }
            try {
                this.tool_1_selectview_tool_2_2index = (short) jsonObject.getInt("t22");
            } catch (JSONException e13) {
                this.tool_1_selectview_tool_2_2index = (short) -1;
            }
            try {
                characterUnitDictionarysArrayListJSONArray = jsonObject.getJSONArray("cal");
            } catch (JSONException e14) {
                characterUnitDictionarysArrayListJSONArray = null;
            }
            if (characterUnitDictionarysArrayListJSONArray != null) {
                this.characterUnitDictionarysArrayList = new ArrayList<>();
                for (int i = 0; i < _appDelegate.CHARACTERUNITVIEW_TOTAL; i++) {
                    Boolean jsonOkF = false;
                    if (i < characterUnitDictionarysArrayListJSONArray.length()) {
                        try {
                            caljsonObject = characterUnitDictionarysArrayListJSONArray.getJSONObject(i);
                        } catch (JSONException e15) {
                            caljsonObject = null;
                        }
                        if (caljsonObject != null) {
                            CharacterUnitDictionary newCharacterUnitDictionary = new CharacterUnitDictionary(caljsonObject);
                            this.characterUnitDictionarysArrayList.add(newCharacterUnitDictionary);
                            jsonOkF = true;
                        }
                    }
                    if (!jsonOkF.booleanValue()) {
                        CharacterUnitDictionary newCharacterUnitDictionary2 = new CharacterUnitDictionary();
                        this.characterUnitDictionarysArrayList.add(newCharacterUnitDictionary2);
                        Log.d("initCharacterUnitDictionarysArrayList", "i=" + i);
                        CharacterUnitDictionary checkCharacterUnitDictionary = this.characterUnitDictionarysArrayList.get(i);
                        Log.d("initCharacterUnitDictionarysArrayList", "offset_x=" + checkCharacterUnitDictionary.getOffsetX());
                        Log.d("initCharacterUnitDictionarysArrayList", "offset_x=" + checkCharacterUnitDictionary.getOffsetY());
                    }
                }
            } else {
                initCharacterUnitDictionarysArrayList(_appDelegate);
            }
            try {
                farmUnitDictionarysArrayListJSONArray = jsonObject.getJSONArray("fal");
            } catch (JSONException e16) {
                farmUnitDictionarysArrayListJSONArray = null;
            }
            if (farmUnitDictionarysArrayListJSONArray != null) {
                this.farmUnitDictionarysArrayList = new ArrayList<>();
                if (_appDelegate.charactersDataFilesArrayList == null) {
                    _appDelegate.initCharactersDataFilesArray();
                }
                if (_appDelegate.charactersDataFilesArrayList != null) {
                    for (int i2 = 0; i2 < _appDelegate.charactersDataFilesArrayList.size(); i2++) {
                        ArrayList<FarmUnitDictionary> unitDictionarysArrayList = new ArrayList<>();
                        Boolean jsonOkF0 = false;
                        if (i2 < farmUnitDictionarysArrayListJSONArray.length()) {
                            try {
                                unitDictionarysArrayListJSONArray = farmUnitDictionarysArrayListJSONArray.getJSONArray(i2);
                            } catch (JSONException e17) {
                                unitDictionarysArrayListJSONArray = null;
                            }
                            if (unitDictionarysArrayListJSONArray != null && (charactersDataDictionary = _appDelegate.charactersDataFilesArrayList.get(i2)) != null && charactersDataDictionary.charactersDataArrayList != null) {
                                for (int j = 0; j < charactersDataDictionary.charactersDataArrayList.size(); j++) {
                                    Boolean jsonOkF1 = false;
                                    if (j < unitDictionarysArrayListJSONArray.length()) {
                                        try {
                                            faljsonObject = unitDictionarysArrayListJSONArray.getJSONObject(j);
                                        } catch (JSONException e18) {
                                            faljsonObject = null;
                                        }
                                        if (faljsonObject != null) {
                                            FarmUnitDictionary newFarmUnitDictionary = new FarmUnitDictionary(faljsonObject, -1.0f, BitmapDescriptorFactory.HUE_RED);
                                            unitDictionarysArrayList.add(newFarmUnitDictionary);
                                            jsonOkF1 = true;
                                        }
                                    }
                                    if (!jsonOkF1.booleanValue()) {
                                        FarmUnitDictionary newFarmUnitDictionary2 = new FarmUnitDictionary(-1.0f, BitmapDescriptorFactory.HUE_RED);
                                        unitDictionarysArrayList.add(newFarmUnitDictionary2);
                                    }
                                }
                                this.farmUnitDictionarysArrayList.add(unitDictionarysArrayList);
                                jsonOkF0 = true;
                            }
                        }
                        if (!jsonOkF0.booleanValue()) {
                            CharactersDataDictionary charactersDataDictionary2 = _appDelegate.charactersDataFilesArrayList.get(i2);
                            if (charactersDataDictionary2 != null && charactersDataDictionary2.charactersDataArrayList != null) {
                                for (int j2 = 0; j2 < charactersDataDictionary2.charactersDataArrayList.size(); j2++) {
                                    FarmUnitDictionary newFarmUnitDictionary3 = new FarmUnitDictionary(-1.0f, BitmapDescriptorFactory.HUE_RED);
                                    unitDictionarysArrayList.add(newFarmUnitDictionary3);
                                    FarmUnitDictionary checkFarmUnitDictionary = unitDictionarysArrayList.get(j2);
                                    Log.d("initFarmUnitDictionarysArrayList", "count=" + checkFarmUnitDictionary.getCount());
                                    Log.d("initFarmUnitDictionarysArrayList", "total_count=" + checkFarmUnitDictionary.getTotalCount());
                                }
                            }
                            this.farmUnitDictionarysArrayList.add(unitDictionarysArrayList);
                        }
                    }
                }
            } else {
                initFarmUnitDictionarysArrayList(_appDelegate);
            }
            try {
                toolUnitDictionarysArrayListJSONArray = jsonObject.getJSONArray("tal");
            } catch (JSONException e19) {
                toolUnitDictionarysArrayListJSONArray = null;
            }
            if (toolUnitDictionarysArrayListJSONArray != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                this.toolUnitDictionarysArrayList = new ArrayList<>();
                if (_appDelegate.toolsDataFilesArrayList == null) {
                    _appDelegate.initToolsDataFilesArray();
                }
                if (_appDelegate.toolsDataFilesArrayList != null) {
                    for (int i3 = 0; i3 < _appDelegate.toolsDataFilesArrayList.size(); i3++) {
                        ArrayList<ToolUnitDictionary> toolDictionarysArrayList = new ArrayList<>();
                        Boolean jsonOkF02 = false;
                        if (i3 < toolUnitDictionarysArrayListJSONArray.length()) {
                            try {
                                toolDictionarysArrayListJSONArray = toolUnitDictionarysArrayListJSONArray.getJSONArray(i3);
                            } catch (JSONException e20) {
                                toolDictionarysArrayListJSONArray = null;
                            }
                            if (toolDictionarysArrayListJSONArray != null && (toolsDataDictionary = _appDelegate.toolsDataFilesArrayList.get(i3)) != null && toolsDataDictionary.toolsDataArrayList != null) {
                                for (int j3 = 0; j3 < toolsDataDictionary.toolsDataArrayList.size(); j3++) {
                                    Boolean jsonOkF12 = false;
                                    if (j3 < toolDictionarysArrayListJSONArray.length()) {
                                        try {
                                            taljsonObject = toolDictionarysArrayListJSONArray.getJSONObject(j3);
                                        } catch (JSONException e21) {
                                            taljsonObject = null;
                                        }
                                        if (taljsonObject != null) {
                                            if (i3 == 0) {
                                                ToolUnitDictionary newToolUnitDictionary = new ToolUnitDictionary(taljsonObject, (short) 0, (short) 100, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                                toolDictionarysArrayList.add(newToolUnitDictionary);
                                            } else if (i3 == 1) {
                                                if (j3 == 0) {
                                                    ToolUnitDictionary newToolUnitDictionary2 = new ToolUnitDictionary(taljsonObject, (short) 0, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary2);
                                                } else if (j3 == 1) {
                                                    ToolUnitDictionary newToolUnitDictionary3 = new ToolUnitDictionary(taljsonObject, (short) -1, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary3);
                                                } else {
                                                    ToolUnitDictionary newToolUnitDictionary4 = new ToolUnitDictionary(taljsonObject, (short) -2, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary4);
                                                }
                                            } else if (i3 == 2) {
                                                if (j3 == 0) {
                                                    ToolUnitDictionary newToolUnitDictionary5 = new ToolUnitDictionary(taljsonObject, (short) 0, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary5);
                                                } else {
                                                    ToolUnitDictionary newToolUnitDictionary6 = new ToolUnitDictionary(taljsonObject, (short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary6);
                                                }
                                            } else if (i3 == 3) {
                                                if (j3 == 0) {
                                                    ToolUnitDictionary newToolUnitDictionary7 = new ToolUnitDictionary(taljsonObject, (short) 0, (short) 0, (short) 1, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary7);
                                                } else {
                                                    ToolUnitDictionary newToolUnitDictionary8 = new ToolUnitDictionary(taljsonObject, (short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary8);
                                                }
                                            }
                                            jsonOkF12 = true;
                                        }
                                        if (!jsonOkF12.booleanValue()) {
                                            if (i3 == 0) {
                                                ToolUnitDictionary newToolUnitDictionary9 = new ToolUnitDictionary((short) 0, (short) 100, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                                toolDictionarysArrayList.add(newToolUnitDictionary9);
                                            } else if (i3 == 1) {
                                                if (j3 == 0) {
                                                    ToolUnitDictionary newToolUnitDictionary10 = new ToolUnitDictionary((short) 0, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary10);
                                                } else if (j3 == 1) {
                                                    ToolUnitDictionary newToolUnitDictionary11 = new ToolUnitDictionary((short) -1, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary11);
                                                } else {
                                                    ToolUnitDictionary newToolUnitDictionary12 = new ToolUnitDictionary((short) -2, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary12);
                                                }
                                            } else if (i3 == 2) {
                                                if (j3 == 0) {
                                                    ToolUnitDictionary newToolUnitDictionary13 = new ToolUnitDictionary((short) 0, (short) 0, (short) 0, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary13);
                                                } else {
                                                    ToolUnitDictionary newToolUnitDictionary14 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary14);
                                                }
                                            } else if (i3 == 3) {
                                                if (j3 == 0) {
                                                    ToolUnitDictionary newToolUnitDictionary15 = new ToolUnitDictionary((short) 0, (short) 0, (short) 1, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary15);
                                                } else {
                                                    ToolUnitDictionary newToolUnitDictionary16 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                                    toolDictionarysArrayList.add(newToolUnitDictionary16);
                                                }
                                            }
                                        }
                                    }
                                }
                                this.toolUnitDictionarysArrayList.add(toolDictionarysArrayList);
                                jsonOkF02 = true;
                            }
                        }
                        if (!jsonOkF02.booleanValue()) {
                            ToolsDataDictionary toolsDataDictionary2 = _appDelegate.toolsDataFilesArrayList.get(i3);
                            if (toolsDataDictionary2 != null && toolsDataDictionary2.toolsDataArrayList != null) {
                                for (int j4 = 0; j4 < toolsDataDictionary2.toolsDataArrayList.size(); j4++) {
                                    if (i3 == 0) {
                                        ToolUnitDictionary newToolUnitDictionary17 = new ToolUnitDictionary((short) 0, (short) 100, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        toolDictionarysArrayList.add(newToolUnitDictionary17);
                                    } else if (i3 == 1) {
                                        if (j4 == 0) {
                                            ToolUnitDictionary newToolUnitDictionary18 = new ToolUnitDictionary((short) 0, (short) 0, (short) 0, (short) 0, "", "");
                                            toolDictionarysArrayList.add(newToolUnitDictionary18);
                                        } else if (j4 == 1) {
                                            ToolUnitDictionary newToolUnitDictionary19 = new ToolUnitDictionary((short) -1, (short) 0, (short) 0, (short) 0, "", "");
                                            toolDictionarysArrayList.add(newToolUnitDictionary19);
                                        } else {
                                            ToolUnitDictionary newToolUnitDictionary20 = new ToolUnitDictionary((short) -2, (short) 0, (short) 0, (short) 0, "", "");
                                            toolDictionarysArrayList.add(newToolUnitDictionary20);
                                        }
                                    } else if (i3 == 2) {
                                        if (j4 == 0) {
                                            ToolUnitDictionary newToolUnitDictionary21 = new ToolUnitDictionary((short) 0, (short) 0, (short) 1, (short) 0, "", "");
                                            toolDictionarysArrayList.add(newToolUnitDictionary21);
                                        } else {
                                            ToolUnitDictionary newToolUnitDictionary22 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                            toolDictionarysArrayList.add(newToolUnitDictionary22);
                                        }
                                    } else if (i3 == 3) {
                                        if (j4 == 0) {
                                            ToolUnitDictionary newToolUnitDictionary23 = new ToolUnitDictionary((short) 0, (short) 0, (short) 1, (short) 0, "", "");
                                            toolDictionarysArrayList.add(newToolUnitDictionary23);
                                        } else {
                                            ToolUnitDictionary newToolUnitDictionary24 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                            toolDictionarysArrayList.add(newToolUnitDictionary24);
                                        }
                                    }
                                    Log.d("initToolUnitDictionarysArrayList", "i=" + i3 + " , j=" + j4);
                                    ToolUnitDictionary checkToolUnitDictionary = toolDictionarysArrayList.get(j4);
                                    Log.d("initToolUnitDictionarysArrayList", "count=" + ((int) checkToolUnitDictionary.getCount()));
                                    Log.d("initToolUnitDictionarysArrayList", "level=" + ((int) checkToolUnitDictionary.getLevel()));
                                    Log.d("initToolUnitDictionarysArrayList", "fixed_date=" + checkToolUnitDictionary.getFixedDate());
                                    Log.d("initToolUnitDictionarysArrayList", "checked_date=" + checkToolUnitDictionary.getCheckedDate());
                                }
                            }
                            this.toolUnitDictionarysArrayList.add(toolDictionarysArrayList);
                        }
                    }
                    return;
                }
                return;
            }
            initToolUnitDictionarysArrayList(_appDelegate);
            return;
        }
        this.date = "";
        this.first_date = "";
        this.player_name = "Player";
        this.point = 600.0f;
        this.tool_1_selectview_nowbuttonindex = (short) -1;
        this.tool_1_selectview_startdate = "";
        this.tool_1_selectview_endseconds = BitmapDescriptorFactory.HUE_RED;
        this.tool_1_selectview_tool_2_0index = (short) -1;
        this.tool_1_selectview_tool_2_1index = (short) -1;
        this.tool_1_selectview_tool_2_2index = (short) -1;
        initCharacterUnitDictionarysArrayList(_appDelegate);
        initFarmUnitDictionarysArrayList(_appDelegate);
        initToolUnitDictionarysArrayList(_appDelegate);
    }

    public TimeSaveDictionary(AppDelegate _appDelegate) {
        this.key_number = "";
        this.date = "";
        this.first_date = "";
        this.player_name = "Player";
        this.point = BitmapDescriptorFactory.HUE_RED;
        this.egg_id_select_index = (short) 0;
        this.tool_1_selectview_nowbuttonindex = (short) -1;
        this.tool_1_selectview_startdate = "";
        this.tool_1_selectview_endseconds = BitmapDescriptorFactory.HUE_RED;
        this.tool_1_selectview_tool_2_0index = (short) -1;
        this.tool_1_selectview_tool_2_1index = (short) -1;
        this.tool_1_selectview_tool_2_2index = (short) -1;
        this.characterUnitDictionarysArrayList = null;
        this.farmUnitDictionarysArrayList = null;
        this.toolUnitDictionarysArrayList = null;
        this.key_number = _appDelegate.createNewKeyNumber();
        this.date = "";
        this.first_date = "";
        this.player_name = "Player";
        this.point = 600.0f;
        this.egg_id_select_index = (short) 0;
        this.tool_1_selectview_nowbuttonindex = (short) -1;
        this.tool_1_selectview_startdate = "";
        this.tool_1_selectview_endseconds = BitmapDescriptorFactory.HUE_RED;
        this.tool_1_selectview_tool_2_0index = (short) -1;
        this.tool_1_selectview_tool_2_1index = (short) -1;
        this.tool_1_selectview_tool_2_2index = (short) -1;
        initCharacterUnitDictionarysArrayList(_appDelegate);
        initFarmUnitDictionarysArrayList(_appDelegate);
        initToolUnitDictionarysArrayList(_appDelegate);
    }

    public void initCharacterUnitDictionarysArrayList(AppDelegate _appDelegate) {
        if (_appDelegate != null) {
            this.characterUnitDictionarysArrayList = new ArrayList<>();
            for (int i = 0; i < _appDelegate.CHARACTERUNITVIEW_TOTAL; i++) {
                CharacterUnitDictionary newCharacterUnitDictionary = new CharacterUnitDictionary();
                this.characterUnitDictionarysArrayList.add(newCharacterUnitDictionary);
                Log.d("initCharacterUnitDictionarysArrayList", "i=" + i);
                CharacterUnitDictionary checkCharacterUnitDictionary = this.characterUnitDictionarysArrayList.get(i);
                Log.d("initCharacterUnitDictionarysArrayList", "offset_x=" + checkCharacterUnitDictionary.getOffsetX());
                Log.d("initCharacterUnitDictionarysArrayList", "offset_x=" + checkCharacterUnitDictionary.getOffsetY());
            }
        }
    }

    public void initFarmUnitDictionarysArrayList(AppDelegate _appDelegate) {
        if (_appDelegate != null) {
            this.farmUnitDictionarysArrayList = new ArrayList<>();
            if (_appDelegate.charactersDataFilesArrayList == null) {
                _appDelegate.initCharactersDataFilesArray();
            }
            if (_appDelegate.charactersDataFilesArrayList != null) {
                for (int i = 0; i < _appDelegate.charactersDataFilesArrayList.size(); i++) {
                    ArrayList<FarmUnitDictionary> unitDictionarysArrayList = new ArrayList<>();
                    Log.d("initFarmUnitDictionarysArrayList", "appMainActivity.charactersDataFilesArrayList.size()" + _appDelegate.charactersDataFilesArrayList.size());
                    CharactersDataDictionary charactersDataDictionary = _appDelegate.charactersDataFilesArrayList.get(i);
                    if (charactersDataDictionary != null && charactersDataDictionary.charactersDataArrayList != null) {
                        Log.d("initFarmUnitDictionarysArrayList", "charactersDataDictionary.charactersDataArrayList.size()" + charactersDataDictionary.charactersDataArrayList.size());
                        for (int j = 0; j < charactersDataDictionary.charactersDataArrayList.size(); j++) {
                            FarmUnitDictionary newFarmUnitDictionary = new FarmUnitDictionary(-1.0f, BitmapDescriptorFactory.HUE_RED);
                            unitDictionarysArrayList.add(newFarmUnitDictionary);
                            FarmUnitDictionary checkFarmUnitDictionary = unitDictionarysArrayList.get(j);
                            Log.d("initFarmUnitDictionarysArrayList", "count=" + checkFarmUnitDictionary.getCount());
                            Log.d("initFarmUnitDictionarysArrayList", "total_count=" + checkFarmUnitDictionary.getTotalCount());
                        }
                    }
                    this.farmUnitDictionarysArrayList.add(unitDictionarysArrayList);
                }
            }
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public void initToolUnitDictionarysArrayList(AppDelegate _appDelegate) {
        if (_appDelegate != null) {
            this.toolUnitDictionarysArrayList = new ArrayList<>();
            if (_appDelegate.toolsDataFilesArrayList == null) {
                _appDelegate.initToolsDataFilesArray();
            }
            if (_appDelegate.toolsDataFilesArrayList != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                for (int i = 0; i < _appDelegate.toolsDataFilesArrayList.size(); i++) {
                    ArrayList<ToolUnitDictionary> toolDictionarysArrayList = new ArrayList<>();
                    Log.d("initToolUnitDictionarysArrayList", "appMainActivity.toolsDataFilesArrayList.size()" + _appDelegate.toolsDataFilesArrayList.size());
                    ToolsDataDictionary toolsDataDictionary = _appDelegate.toolsDataFilesArrayList.get(i);
                    if (toolsDataDictionary != null && toolsDataDictionary.toolsDataArrayList != null) {
                        Log.d("initToolUnitDictionarysArrayList", "toolsDataDictionary.toolsDataArrayList.size()" + toolsDataDictionary.toolsDataArrayList.size());
                        for (int j = 0; j < toolsDataDictionary.toolsDataArrayList.size(); j++) {
                            if (i == 0) {
                                ToolUnitDictionary newToolUnitDictionary = new ToolUnitDictionary((short) 0, (short) 100, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                toolDictionarysArrayList.add(newToolUnitDictionary);
                            } else if (i == 1) {
                                if (j == 0) {
                                    ToolUnitDictionary newToolUnitDictionary2 = new ToolUnitDictionary((short) 0, (short) 0, (short) 0, (short) 0, "", "");
                                    toolDictionarysArrayList.add(newToolUnitDictionary2);
                                } else if (j == 1) {
                                    ToolUnitDictionary newToolUnitDictionary3 = new ToolUnitDictionary((short) -1, (short) 0, (short) 0, (short) 0, "", "");
                                    toolDictionarysArrayList.add(newToolUnitDictionary3);
                                } else {
                                    ToolUnitDictionary newToolUnitDictionary4 = new ToolUnitDictionary((short) -2, (short) 0, (short) 0, (short) 0, "", "");
                                    toolDictionarysArrayList.add(newToolUnitDictionary4);
                                }
                            } else if (i == 2) {
                                if (j == 0) {
                                    ToolUnitDictionary newToolUnitDictionary5 = new ToolUnitDictionary((short) 0, (short) 0, (short) 1, (short) 0, "", "");
                                    toolDictionarysArrayList.add(newToolUnitDictionary5);
                                } else {
                                    ToolUnitDictionary newToolUnitDictionary6 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                    toolDictionarysArrayList.add(newToolUnitDictionary6);
                                }
                            } else if (i == 3) {
                                if (j == 0) {
                                    ToolUnitDictionary newToolUnitDictionary7 = new ToolUnitDictionary((short) 0, (short) 0, (short) 1, (short) 0, "", "");
                                    toolDictionarysArrayList.add(newToolUnitDictionary7);
                                } else {
                                    ToolUnitDictionary newToolUnitDictionary8 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, "", "");
                                    toolDictionarysArrayList.add(newToolUnitDictionary8);
                                }
                            }
                            Log.d("initToolUnitDictionarysArrayList", "i=" + i + " , j=" + j);
                            ToolUnitDictionary checkToolUnitDictionary = toolDictionarysArrayList.get(j);
                            Log.d("initToolUnitDictionarysArrayList", "count=" + ((int) checkToolUnitDictionary.getCount()));
                            Log.d("initToolUnitDictionarysArrayList", "level=" + ((int) checkToolUnitDictionary.getLevel()));
                            Log.d("initToolUnitDictionarysArrayList", "fixed_date=" + checkToolUnitDictionary.getFixedDate());
                            Log.d("initToolUnitDictionarysArrayList", "checked_date=" + checkToolUnitDictionary.getCheckedDate());
                        }
                    }
                    this.toolUnitDictionarysArrayList.add(toolDictionarysArrayList);
                }
            }
        }
    }

    public void checkTimeSaveDictionaryWithAppDelegate(AppDelegate _appDelegate) {
        if (_appDelegate != null) {
            if (this.farmUnitDictionarysArrayList != null) {
                checkFarmUnitDictionarysArrayListWithAppDelegate(_appDelegate);
            } else {
                initFarmUnitDictionarysArrayList(_appDelegate);
            }
            if (this.toolUnitDictionarysArrayList != null) {
                checkToolUnitDictionarysArrayListWithAppDelegate(_appDelegate);
            } else {
                initToolUnitDictionarysArrayList(_appDelegate);
            }
        }
    }

    public void checkFarmUnitDictionarysArrayListWithAppDelegate(AppDelegate _appDelegate) {
        if (_appDelegate != null && this.farmUnitDictionarysArrayList != null) {
            if (_appDelegate.charactersDataFilesArrayList == null) {
                _appDelegate.initCharactersDataFilesArray();
            }
            if (_appDelegate.charactersDataFilesArrayList != null) {
                for (int i = 0; i < _appDelegate.charactersDataFilesArrayList.size() && i < this.farmUnitDictionarysArrayList.size(); i++) {
                    ArrayList<FarmUnitDictionary> _checkFarmUnitDictionarysArrayList = (ArrayList) this.farmUnitDictionarysArrayList.get(i);
                    CharactersDataDictionary charactersDataDictionary = _appDelegate.charactersDataFilesArrayList.get(i);
                    if (charactersDataDictionary != null && _checkFarmUnitDictionarysArrayList != null && charactersDataDictionary.charactersDataArrayList != null) {
                        for (int j = 0; j < charactersDataDictionary.charactersDataArrayList.size(); j++) {
                            if (j >= _checkFarmUnitDictionarysArrayList.size()) {
                                FarmUnitDictionary newFarmUnitDictionary = new FarmUnitDictionary(-1.0f, BitmapDescriptorFactory.HUE_RED);
                                _checkFarmUnitDictionarysArrayList.add(newFarmUnitDictionary);
                            }
                        }
                    }
                }
            }
        }
    }

    public void checkToolUnitDictionarysArrayListWithAppDelegate(AppDelegate _appDelegate) {
        if (_appDelegate != null && this.toolUnitDictionarysArrayList != null) {
            if (_appDelegate.toolsDataFilesArrayList == null) {
                _appDelegate.initToolsDataFilesArray();
            }
            if (_appDelegate.toolsDataFilesArrayList != null) {
                for (int i = 0; i < _appDelegate.toolsDataFilesArrayList.size() && i < this.toolUnitDictionarysArrayList.size(); i++) {
                    ArrayList<ToolUnitDictionary> _checkToolDictionarysArrayList = (ArrayList) this.toolUnitDictionarysArrayList.get(i);
                    ToolsDataDictionary toolsDataDictionary = _appDelegate.toolsDataFilesArrayList.get(i);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    if (toolsDataDictionary != null && _checkToolDictionarysArrayList != null && toolsDataDictionary.toolsDataArrayList != null) {
                        for (int j = 0; j < toolsDataDictionary.toolsDataArrayList.size(); j++) {
                            if (j >= _checkToolDictionarysArrayList.size()) {
                                if (i == 0) {
                                    ToolUnitDictionary newToolUnitDictionary = new ToolUnitDictionary((short) 0, (short) 100, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                    _checkToolDictionarysArrayList.add(newToolUnitDictionary);
                                } else if (i == 1) {
                                    if (j == 0) {
                                        ToolUnitDictionary newToolUnitDictionary2 = new ToolUnitDictionary((short) 0, (short) 0, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        _checkToolDictionarysArrayList.add(newToolUnitDictionary2);
                                    } else if (j == 1) {
                                        ToolUnitDictionary newToolUnitDictionary3 = new ToolUnitDictionary((short) -1, (short) 0, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        _checkToolDictionarysArrayList.add(newToolUnitDictionary3);
                                    } else {
                                        ToolUnitDictionary newToolUnitDictionary4 = new ToolUnitDictionary((short) -2, (short) 0, (short) 0, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        _checkToolDictionarysArrayList.add(newToolUnitDictionary4);
                                    }
                                } else if (i == 2) {
                                    if (j == 0) {
                                        ToolUnitDictionary newToolUnitDictionary5 = new ToolUnitDictionary((short) 0, (short) 0, (short) 1, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        _checkToolDictionarysArrayList.add(newToolUnitDictionary5);
                                    } else {
                                        ToolUnitDictionary newToolUnitDictionary6 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        _checkToolDictionarysArrayList.add(newToolUnitDictionary6);
                                    }
                                } else if (i == 3) {
                                    if (j == 0) {
                                        ToolUnitDictionary newToolUnitDictionary7 = new ToolUnitDictionary((short) 0, (short) 0, (short) 1, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        _checkToolDictionarysArrayList.add(newToolUnitDictionary7);
                                    } else {
                                        ToolUnitDictionary newToolUnitDictionary8 = new ToolUnitDictionary((short) 0, (short) 0, (short) -1, (short) 0, sdf.format(new Date()), sdf.format(new Date()));
                                        _checkToolDictionarysArrayList.add(newToolUnitDictionary8);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setKeyNumber(String _key_number) {
        if (_key_number == null || _key_number.length() <= 0) {
            this.key_number = "";
        } else {
            this.key_number = _key_number;
        }
    }

    public String getKeyNumber() {
        return this.key_number;
    }

    public void setDate(String _date) {
        if (_date == null || _date.length() <= 0) {
            this.date = "";
        } else {
            this.date = _date;
        }
    }

    public String getDate() {
        return this.date;
    }

    public void setFirstDate(String _first_date) {
        if (_first_date == null || _first_date.length() <= 0) {
            this.first_date = "";
        } else {
            this.first_date = _first_date;
        }
    }

    public String getFirstDate() {
        return this.first_date;
    }

    public void setPlayerName(String _player_name) {
        if (_player_name == null || _player_name.length() <= 0) {
            this.player_name = "";
        } else {
            this.player_name = _player_name;
        }
    }

    public String getPlayerName() {
        return this.player_name;
    }

    public void setPoint(float _point) {
        if (_point < BitmapDescriptorFactory.HUE_RED) {
            this.point = BitmapDescriptorFactory.HUE_RED;
        } else {
            this.point = _point;
        }
    }

    public float getPoint() {
        return this.point;
    }

    public void setEggIDSelectIndex(short _egg_id_select_index) {
        if (_egg_id_select_index < 0) {
            this.egg_id_select_index = (short) 0;
        } else {
            this.egg_id_select_index = _egg_id_select_index;
        }
    }

    public short getEggIDSelectIndex() {
        return this.egg_id_select_index;
    }

    public void setTool1SelectViewNowButtonIndex(short _tool_1_selectview_nowbuttonindex) {
        if (_tool_1_selectview_nowbuttonindex < 0) {
            this.tool_1_selectview_nowbuttonindex = (short) -1;
        } else {
            this.tool_1_selectview_nowbuttonindex = _tool_1_selectview_nowbuttonindex;
        }
    }

    public short getTool1SelectViewNowButtonIndex() {
        return this.tool_1_selectview_nowbuttonindex;
    }

    public void setTool1SelectViewStartDate(String _tool_1_selectview_startdate) {
        if (_tool_1_selectview_startdate == null || _tool_1_selectview_startdate.length() <= 0) {
            this.tool_1_selectview_startdate = "";
        } else {
            this.tool_1_selectview_startdate = _tool_1_selectview_startdate;
        }
    }

    public String getTool1SelectViewStartDate() {
        return this.tool_1_selectview_startdate;
    }

    public void setTool1SelectViewEndSeconds(float _tool_1_selectview_endseconds) {
        if (_tool_1_selectview_endseconds < BitmapDescriptorFactory.HUE_RED) {
            this.tool_1_selectview_endseconds = BitmapDescriptorFactory.HUE_RED;
        } else {
            this.tool_1_selectview_endseconds = _tool_1_selectview_endseconds;
        }
    }

    public float getTool1SelectViewEndSeconds() {
        return this.tool_1_selectview_endseconds;
    }

    public void setTool1SelectViewTool2_0Index(short _tool_1_selectview_tool_2_0index) {
        if (_tool_1_selectview_tool_2_0index < 0) {
            this.tool_1_selectview_tool_2_0index = (short) -1;
        } else {
            this.tool_1_selectview_tool_2_0index = _tool_1_selectview_tool_2_0index;
        }
    }

    public short getTool1SelectViewTool2_0Index() {
        return this.tool_1_selectview_tool_2_0index;
    }

    public void setTool1SelectViewTool2_1Index(short _tool_1_selectview_tool_2_1index) {
        if (_tool_1_selectview_tool_2_1index < 0) {
            this.tool_1_selectview_tool_2_1index = (short) -1;
        } else {
            this.tool_1_selectview_tool_2_1index = _tool_1_selectview_tool_2_1index;
        }
    }

    public short getTool1SelectViewTool2_1Index() {
        return this.tool_1_selectview_tool_2_1index;
    }

    public void setTool1SelectViewTool2_2Index(short _tool_1_selectview_tool_2_2index) {
        if (_tool_1_selectview_tool_2_2index < 0) {
            this.tool_1_selectview_tool_2_2index = (short) -1;
        } else {
            this.tool_1_selectview_tool_2_2index = _tool_1_selectview_tool_2_2index;
        }
    }

    public short getTool1SelectViewTool2_2Index() {
        return this.tool_1_selectview_tool_2_2index;
    }

    public JSONObject changeToJSONObject(AppDelegate _appDelegate) throws JSONException {
        if (_appDelegate == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            _appDelegate.getClass();
            jSONObject.put("vl", 30);
            jSONObject.put("did", _appDelegate.getDeviceID());
            jSONObject.put("kn", this.key_number);
            jSONObject.put("d", this.date.replaceAll("/", "<time_slash>"));
            jSONObject.put("fd", this.first_date.replaceAll("/", "<time_slash>"));
            jSONObject.put("pn", this.player_name);
            jSONObject.put("pt", this.point);
            jSONObject.put("eis", (int) this.egg_id_select_index);
            jSONObject.put("t1sn", (int) this.tool_1_selectview_nowbuttonindex);
            jSONObject.put("t1ss", this.tool_1_selectview_startdate);
            jSONObject.put("t1ss", this.tool_1_selectview_startdate.replaceAll("/", "<time_slash>"));
            jSONObject.put("t1se", (int) this.tool_1_selectview_endseconds);
            jSONObject.put("t20", (int) this.tool_1_selectview_tool_2_0index);
            jSONObject.put("t21", (int) this.tool_1_selectview_tool_2_1index);
            jSONObject.put("t22", (int) this.tool_1_selectview_tool_2_2index);
            JSONArray characterUnitDictionarysArrayListJSONArray = new JSONArray();
            for (int i = 0; i < this.characterUnitDictionarysArrayList.size(); i++) {
                CharacterUnitDictionary characterUnitDictionary = this.characterUnitDictionarysArrayList.get(i);
                characterUnitDictionarysArrayListJSONArray.put(characterUnitDictionary.changeToJSONObject());
            }
            jSONObject.put("cal", characterUnitDictionarysArrayListJSONArray);
            JSONArray farmUnitDictionarysArrayListJSONArray = new JSONArray();
            for (int i2 = 0; i2 < this.farmUnitDictionarysArrayList.size(); i2++) {
                ArrayList<FarmUnitDictionary> unitDictionarysArrayList = (ArrayList) this.farmUnitDictionarysArrayList.get(i2);
                JSONArray unitDictionarysArrayListJSONArray = new JSONArray();
                for (int j = 0; j < unitDictionarysArrayList.size(); j++) {
                    FarmUnitDictionary farmUnitDictionary = unitDictionarysArrayList.get(j);
                    unitDictionarysArrayListJSONArray.put(farmUnitDictionary.changeToJSONObject());
                }
                farmUnitDictionarysArrayListJSONArray.put(unitDictionarysArrayListJSONArray);
            }
            jSONObject.put("fal", farmUnitDictionarysArrayListJSONArray);
            JSONArray toolUnitDictionarysArrayListJSONArray = new JSONArray();
            for (int i3 = 0; i3 < this.toolUnitDictionarysArrayList.size(); i3++) {
                ArrayList<ToolUnitDictionary> toolDictionarysArrayList = (ArrayList) this.toolUnitDictionarysArrayList.get(i3);
                JSONArray toolDictionarysArrayListJSONArray = new JSONArray();
                for (int j2 = 0; j2 < toolDictionarysArrayList.size(); j2++) {
                    ToolUnitDictionary toolUnitDictionary = toolDictionarysArrayList.get(j2);
                    toolDictionarysArrayListJSONArray.put(toolUnitDictionary.changeToJSONObject((short) i3));
                }
                toolUnitDictionarysArrayListJSONArray.put(toolDictionarysArrayListJSONArray);
            }
            jSONObject.put("tal", toolUnitDictionarysArrayListJSONArray);
            return jSONObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return jSONObject;
        }
    }
}

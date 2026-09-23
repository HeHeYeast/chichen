// Generated from MainGameUnit.getRateArrayWithID; do not hand-edit.
import { DATA } from './data.js';
export function samplePool(pool, random = Math.random) {
  const remaining = [...pool], result = [];
  for (let i = 0; i < 24; i++) {
    const index = Math.floor(random() * remaining.length);
    result.push(remaining.splice(index, 1)[0]);
  }
  return result;
}
export function originalRecipes(state, egg, tool, ingredients, now = Date.now(), random = Math.random) {
  return originalRules(state,egg,tool,ingredients,now,random,false);
}
export function originalRecipePlan(state,egg,tool,ingredients,now=Date.now()) {
  return originalRules(state,egg,tool,ingredients,now,()=>{throw Error('Preview attempted random sampling');},true);
}
function originalRules(state, _eggID, _tool1_ID, ingredients, now, random, preview) {
  const tool_0_0_level = state.kitchenLevel;
  const totalChars0Cnt = Object.entries(state.total).filter(([k]) => k.startsWith('0:')).reduce((n, [,v]) => n+v, 0);
  const totalChars1Cnt = Object.entries(state.total).filter(([k]) => k.startsWith('1:')).reduce((n, [,v]) => n+v, 0);
  const totalCharsAllCnt = totalChars0Cnt + totalChars1Cnt;
  const allRateArrayList = [];
  const gifts = [];
  let returnRateArrayList = null;
  const add = id => { const c = DATA.characters[_eggID].find(c => c.id === id); if(c) for(let i=0; i<c.rate; i++) allRateArrayList.push(id); };
        if (_eggID == 0) {
            if (_tool1_ID == 0) {
                let tool_1_level = state.toolLevels[_tool1_ID];
                add(0);
                if (totalChars0Cnt >= 1000.0) {
                    add(31);
                }
                if (totalChars0Cnt >= 2000.0) {
                    add(81);
                }
                if (!!state.events?.["campaign_char_0_48"]) {
                    add(48);
                }
                if (!!state.events?.["campaign_char_0_49"]) {
                    add(49);
                }
                if (!!state.events?.["campaign_char_0_60"]) {
                    add(60);
                }
                if (!!state.events?.["campaign_char_0_61"]) {
                    add(61);
                }
                if (!!state.events?.["campaign_char_0_62"]) {
                    add(62);
                }
                if (!!state.events?.["campaign_char_0_63"]) {
                    add(63);
                }
                if (!!state.events?.["campaign_char_0_64"]) {
                    add(64);
                }
                if (!!state.events?.["campaign_char_0_65"]) {
                    add(65);
                }
                if (!!state.events?.["campaign_char_0_66"]) {
                    add(66);
                }
                if (!!state.events?.["campaign_char_0_67"]) {
                    add(67);
                }
                if (!!state.events?.["campaign_char_0_78"]) {
                    add(78);
                }
                if (!!state.events?.["campaign_char_0_79"]) {
                    add(79);
                }
                if (!!state.events?.["campaign_char_0_80"]) {
                    add(80);
                }
                if (!!state.events?.["campaign_char_0_83"]) {
                    add(83);
                }
                if (!!state.events?.["campaign_char_0_84"]) {
                    add(84);
                }
                if (!!state.events?.["campaign_char_0_85"]) {
                    add(85);
                }
                if (!!state.events?.["campaign_char_0_86"]) {
                    add(86);
                }
                if (!!state.events?.["campaign_char_0_87"]) {
                    add(87);
                }
                if (!!state.events?.["campaign_char_0_88"]) {
                    add(88);
                }
                if (!!state.events?.["campaign_char_0_105"]) {
                    add(105);
                }
                if ((ingredients[0] ?? -1) == 0 || (ingredients[1] ?? -1) == 0 || (ingredients[2] ?? -1) == 0) {
                    add(5);
                }
                if ((ingredients[0] ?? -1) == 11 || (ingredients[1] ?? -1) == 11 || (ingredients[2] ?? -1) == 11) {
                    add(20);
                }
                if ((ingredients[0] ?? -1) == 15 || (ingredients[1] ?? -1) == 15 || (ingredients[2] ?? -1) == 15) {
                    add(25);
                    if (!!state.events?.["campaign_char_0_26"]) {
                        add(26);
                    }
                    if (!!state.events?.["campaign_char_0_27"]) {
                        add(27);
                    }
                }
                if ((ingredients[0] ?? -1) == 18 || (ingredients[1] ?? -1) == 18 || (ingredients[2] ?? -1) == 18) {
                    add(30);
                }
                if ((ingredients[0] ?? -1) == 36 || (ingredients[1] ?? -1) == 36 || (ingredients[2] ?? -1) == 36) {
                    add(50);
                }
                if (tool_1_level >= 1) {
                    add(33);
                }
                if (tool_1_level >= 2) {
                    let randIndex = (preview ? 0 : Math.trunc(random() * 10.0));
                    if (randIndex == 0) {
                        let hourInt = new Date(now).getHours();
                        if (hourInt == 10 || hourInt == 11 || hourInt == 12) {
                            add(52);
                        } else {
                            add(51);
                        }
                    }
                }
                if (tool_0_0_level >= 3) {
                    add(108);
                }
                if ((ingredients[0] ?? -1) == 35 || (ingredients[1] ?? -1) == 35 || (ingredients[2] ?? -1) == 35) {
                    add(47);
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
                if ((ingredients[0] ?? -1) == 68 || (ingredients[1] ?? -1) == 68 || (ingredients[2] ?? -1) == 68) {
                    let idShort = -1;
                    if (true) {
                        idShort = (state.events?.["gift_tool_2_68_character_id"] ?? -1);
                    }
                    if (idShort < 89 || idShort > 103) {
                        idShort = -1;
                    }
                    if (idShort >= 0) {
                        if (true) {
                        }
                    } else {
                        let randNum = (preview ? -1 : Math.trunc(random() * 100.0));
                        if (randNum < 1) {
                            idShort = 89;
                        } else if (randNum < 4) {
                            idShort = 90;
                        } else if (randNum < 10) {
                            idShort = 91;
                        } else if (randNum < 19) {
                            idShort = 92;
                        } else if (randNum < 28) {
                            idShort = 93;
                        } else if (randNum < 37) {
                            idShort = 97;
                        } else if (randNum < 46) {
                            idShort = 94;
                        } else if (randNum < 55) {
                            idShort = 98;
                        } else if (randNum < 64) {
                            idShort = 99;
                        } else if (randNum < 73) {
                            idShort = 95;
                        } else if (randNum < 82) {
                            idShort = 100;
                        } else if (randNum < 91) {
                            idShort = 101;
                        } else if (randNum < 97) {
                            idShort = 102;
                        } else {
                            idShort = 96;
                        }
                    }
                    returnRateArrayList.splice(0, 1);
                    let randInsertIndex = (preview ? 0 : random() * returnRateArrayList.length);
                    if(preview){
                      const target=state.events?.gift_tool_2_68_character_id;
                      if(Number.isInteger(target)&&target>=89&&target<=103)gifts.push({id:target,guaranteed:1});
                      else for(let id=89;id<=102;id++)gifts.push({id,guaranteed:0});
                    }else returnRateArrayList.splice(randInsertIndex, 0, idShort);
                }
            } else if (_tool1_ID == 1) {
                let tool_1_level2 = state.toolLevels[_tool1_ID];
                add(3);
                add(4);
                if ((ingredients[0] ?? -1) == 1 || (ingredients[1] ?? -1) == 1 || (ingredients[2] ?? -1) == 1) {
                    add(6);
                }
                if ((ingredients[0] ?? -1) == 2 || (ingredients[1] ?? -1) == 2 || (ingredients[2] ?? -1) == 2) {
                    add(7);
                }
                if (tool_1_level2 >= 1) {
                    if (((ingredients[0] ?? -1) == 19 || (ingredients[1] ?? -1) == 19 || (ingredients[2] ?? -1) == 19) && ((ingredients[0] ?? -1) == 20 || (ingredients[1] ?? -1) == 20 || (ingredients[2] ?? -1) == 20)) {
                        add(36);
                    }
                    if (((ingredients[0] ?? -1) == 30 || (ingredients[1] ?? -1) == 30 || (ingredients[2] ?? -1) == 30) && ((ingredients[0] ?? -1) == 31 || (ingredients[1] ?? -1) == 31 || (ingredients[2] ?? -1) == 31)) {
                        add(43);
                    }
                    if (tool_1_level2 >= 2 && (((ingredients[0] ?? -1) == 25 || (ingredients[1] ?? -1) == 25 || (ingredients[2] ?? -1) == 25) && (((ingredients[0] ?? -1) == 37 || (ingredients[1] ?? -1) == 37 || (ingredients[2] ?? -1) == 37) && ((ingredients[0] ?? -1) == 38 || (ingredients[1] ?? -1) == 38 || (ingredients[2] ?? -1) == 38)))) {
                        add(55);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
                if ((ingredients[0] ?? -1) == 69 || (ingredients[1] ?? -1) == 69 || (ingredients[2] ?? -1) == 69) {
                    returnRateArrayList.splice(0, 1);
                    let randInsertIndex2 = (preview ? 0 : random() * returnRateArrayList.length);
                    if(preview)gifts.push({id:106,guaranteed:1});else returnRateArrayList.splice(randInsertIndex2, 0, 106);
                }
            } else if (_tool1_ID == 2) {
                let tool_1_level3 = state.toolLevels[_tool1_ID];
                add(8);
                add(9);
                if ((ingredients[0] ?? -1) == 3 || (ingredients[1] ?? -1) == 3 || (ingredients[2] ?? -1) == 3) {
                    add(10);
                }
                if ((ingredients[0] ?? -1) == 4 || (ingredients[1] ?? -1) == 4 || (ingredients[2] ?? -1) == 4) {
                    add(11);
                }
                if ((ingredients[0] ?? -1) == 17 || (ingredients[1] ?? -1) == 17 || (ingredients[2] ?? -1) == 17) {
                    add(29);
                    if (!!state.events?.["campaign_char_0_32"]) {
                        add(32);
                    }
                }
                if (tool_1_level3 >= 1) {
                    if (((ingredients[0] ?? -1) == 21 || (ingredients[1] ?? -1) == 21 || (ingredients[2] ?? -1) == 21) && ((ingredients[0] ?? -1) == 22 || (ingredients[1] ?? -1) == 22 || (ingredients[2] ?? -1) == 22)) {
                        add(37);
                    }
                    if (((ingredients[0] ?? -1) == 20 || (ingredients[1] ?? -1) == 20 || (ingredients[2] ?? -1) == 20) && ((ingredients[0] ?? -1) == 23 || (ingredients[1] ?? -1) == 23 || (ingredients[2] ?? -1) == 23)) {
                        add(38);
                    }
                    if (((ingredients[0] ?? -1) == 9 || (ingredients[1] ?? -1) == 9 || (ingredients[2] ?? -1) == 9) && ((ingredients[0] ?? -1) == 24 || (ingredients[1] ?? -1) == 24 || (ingredients[2] ?? -1) == 24)) {
                        add(39);
                    }
                    if (tool_1_level3 >= 2 && (((ingredients[0] ?? -1) == 19 || (ingredients[1] ?? -1) == 19 || (ingredients[2] ?? -1) == 19) && (((ingredients[0] ?? -1) == 39 || (ingredients[1] ?? -1) == 39 || (ingredients[2] ?? -1) == 39) && ((ingredients[0] ?? -1) == 40 || (ingredients[1] ?? -1) == 40 || (ingredients[2] ?? -1) == 40)))) {
                        add(56);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
                if ((ingredients[0] ?? -1) == 70 || (ingredients[1] ?? -1) == 70 || (ingredients[2] ?? -1) == 70) {
                    returnRateArrayList.splice(0, 1);
                    let randInsertIndex3 = (preview ? 0 : random() * returnRateArrayList.length);
                    if(preview)gifts.push({id:107,guaranteed:1});else returnRateArrayList.splice(randInsertIndex3, 0, 107);
                }
            } else if (_tool1_ID == 3) {
                let tool_1_level4 = state.toolLevels[_tool1_ID];
                add(12);
                if ((ingredients[0] ?? -1) == 5 || (ingredients[1] ?? -1) == 5 || (ingredients[2] ?? -1) == 5) {
                    add(13);
                }
                if ((ingredients[0] ?? -1) == 6 || (ingredients[1] ?? -1) == 6 || (ingredients[2] ?? -1) == 6) {
                    add(14);
                }
                if ((ingredients[0] ?? -1) == 7 || (ingredients[1] ?? -1) == 7 || (ingredients[2] ?? -1) == 7) {
                    add(15);
                }
                if (tool_1_level4 >= 1) {
                    if (((ingredients[0] ?? -1) == 14 || (ingredients[1] ?? -1) == 14 || (ingredients[2] ?? -1) == 14) && ((ingredients[0] ?? -1) == 25 || (ingredients[1] ?? -1) == 25 || (ingredients[2] ?? -1) == 25)) {
                        add(40);
                    }
                    if (((ingredients[0] ?? -1) == 25 || (ingredients[1] ?? -1) == 25 || (ingredients[2] ?? -1) == 25) && ((ingredients[0] ?? -1) == 32 || (ingredients[1] ?? -1) == 32 || (ingredients[2] ?? -1) == 32)) {
                        add(44);
                    }
                    if (tool_1_level4 >= 2 && (((ingredients[0] ?? -1) == 9 || (ingredients[1] ?? -1) == 9 || (ingredients[2] ?? -1) == 9) && (((ingredients[0] ?? -1) == 30 || (ingredients[1] ?? -1) == 30 || (ingredients[2] ?? -1) == 30) && ((ingredients[0] ?? -1) == 41 || (ingredients[1] ?? -1) == 41 || (ingredients[2] ?? -1) == 41)))) {
                        add(57);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 4) {
                let tool_1_level5 = state.toolLevels[_tool1_ID];
                add(16);
                if (!!state.events?.["campaign_char_0_82"] && totalChars0Cnt >= 2000.0) {
                    add(82);
                }
                if ((ingredients[0] ?? -1) == 8 || (ingredients[1] ?? -1) == 8 || (ingredients[2] ?? -1) == 8) {
                    add(17);
                }
                if ((ingredients[0] ?? -1) == 9 || (ingredients[1] ?? -1) == 9 || (ingredients[2] ?? -1) == 9) {
                    add(18);
                }
                if ((ingredients[0] ?? -1) == 10 || (ingredients[1] ?? -1) == 10 || (ingredients[2] ?? -1) == 10) {
                    add(19);
                }
                if ((ingredients[0] ?? -1) == 16 || (ingredients[1] ?? -1) == 16 || (ingredients[2] ?? -1) == 16) {
                    add(28);
                }
                if (tool_1_level5 >= 1) {
                    if (((ingredients[0] ?? -1) == 26 || (ingredients[1] ?? -1) == 26 || (ingredients[2] ?? -1) == 26) && ((ingredients[0] ?? -1) == 27 || (ingredients[1] ?? -1) == 27 || (ingredients[2] ?? -1) == 27)) {
                        add(41);
                    }
                    if (((ingredients[0] ?? -1) == 23 || (ingredients[1] ?? -1) == 23 || (ingredients[2] ?? -1) == 23) && ((ingredients[0] ?? -1) == 33 || (ingredients[1] ?? -1) == 33 || (ingredients[2] ?? -1) == 33)) {
                        add(45);
                    }
                    if (tool_1_level5 >= 2 && (((ingredients[0] ?? -1) == 27 || (ingredients[1] ?? -1) == 27 || (ingredients[2] ?? -1) == 27) && (((ingredients[0] ?? -1) == 41 || (ingredients[1] ?? -1) == 41 || (ingredients[2] ?? -1) == 41) && ((ingredients[0] ?? -1) == 42 || (ingredients[1] ?? -1) == 42 || (ingredients[2] ?? -1) == 42)))) {
                        add(58);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 5) {
                let tool_1_level6 = state.toolLevels[_tool1_ID];
                add(21);
                if ((ingredients[0] ?? -1) == 12 || (ingredients[1] ?? -1) == 12 || (ingredients[2] ?? -1) == 12) {
                    add(22);
                }
                if ((ingredients[0] ?? -1) == 13 || (ingredients[1] ?? -1) == 13 || (ingredients[2] ?? -1) == 13) {
                    add(23);
                }
                if ((ingredients[0] ?? -1) == 14 || (ingredients[1] ?? -1) == 14 || (ingredients[2] ?? -1) == 14) {
                    add(24);
                }
                if (tool_1_level6 >= 1) {
                    if (((ingredients[0] ?? -1) == 28 || (ingredients[1] ?? -1) == 28 || (ingredients[2] ?? -1) == 28) && ((ingredients[0] ?? -1) == 29 || (ingredients[1] ?? -1) == 29 || (ingredients[2] ?? -1) == 29)) {
                        add(42);
                    }
                    if (((ingredients[0] ?? -1) == 23 || (ingredients[1] ?? -1) == 23 || (ingredients[2] ?? -1) == 23) && ((ingredients[0] ?? -1) == 34 || (ingredients[1] ?? -1) == 34 || (ingredients[2] ?? -1) == 34)) {
                        add(46);
                    }
                    if (tool_1_level6 >= 2 && (((ingredients[0] ?? -1) == 22 || (ingredients[1] ?? -1) == 22 || (ingredients[2] ?? -1) == 22) && (((ingredients[0] ?? -1) == 24 || (ingredients[1] ?? -1) == 24 || (ingredients[2] ?? -1) == 24) && ((ingredients[0] ?? -1) == 43 || (ingredients[1] ?? -1) == 43 || (ingredients[2] ?? -1) == 43)))) {
                        add(59);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 6) {
                let tool_1_level7 = state.toolLevels[_tool1_ID];
                add(69);
                if ((ingredients[0] ?? -1) == 56 || (ingredients[1] ?? -1) == 56 || (ingredients[2] ?? -1) == 56) {
                    add(71);
                    add(72);
                }
                if (tool_1_level7 >= 1) {
                    if (((ingredients[0] ?? -1) == 56 || (ingredients[1] ?? -1) == 56 || (ingredients[2] ?? -1) == 56) && ((ingredients[0] ?? -1) == 57 || (ingredients[1] ?? -1) == 57 || (ingredients[2] ?? -1) == 57)) {
                        add(73);
                        add(74);
                    }
                    if (tool_1_level7 >= 2) {
                        if (((ingredients[0] ?? -1) == 56 || (ingredients[1] ?? -1) == 56 || (ingredients[2] ?? -1) == 56) && (((ingredients[0] ?? -1) == 57 || (ingredients[1] ?? -1) == 57 || (ingredients[2] ?? -1) == 57) && ((ingredients[0] ?? -1) == 58 || (ingredients[1] ?? -1) == 58 || (ingredients[2] ?? -1) == 58))) {
                            add(75);
                        }
                        if (((ingredients[0] ?? -1) == 56 || (ingredients[1] ?? -1) == 56 || (ingredients[2] ?? -1) == 56) && (((ingredients[0] ?? -1) == 59 || (ingredients[1] ?? -1) == 59 || (ingredients[2] ?? -1) == 59) && ((ingredients[0] ?? -1) == 60 || (ingredients[1] ?? -1) == 60 || (ingredients[2] ?? -1) == 60))) {
                            add(76);
                        }
                        if (((ingredients[0] ?? -1) == 56 || (ingredients[1] ?? -1) == 56 || (ingredients[2] ?? -1) == 56) && (((ingredients[0] ?? -1) == 61 || (ingredients[1] ?? -1) == 61 || (ingredients[2] ?? -1) == 61) && ((ingredients[0] ?? -1) == 62 || (ingredients[1] ?? -1) == 62 || (ingredients[2] ?? -1) == 62))) {
                            add(77);
                        }
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 7) {
                let tool_1_level8 = state.toolLevels[_tool1_ID];
                add(109);
                if ((ingredients[0] ?? -1) == 37 || (ingredients[1] ?? -1) == 37 || (ingredients[2] ?? -1) == 37) {
                    add(111);
                }
                if (tool_1_level8 >= 1) {
                    if (((ingredients[0] ?? -1) == 71 || (ingredients[1] ?? -1) == 71 || (ingredients[2] ?? -1) == 71) && ((ingredients[0] ?? -1) == 72 || (ingredients[1] ?? -1) == 72 || (ingredients[2] ?? -1) == 72)) {
                        add(112);
                    }
                    if (tool_1_level8 >= 2 && (((ingredients[0] ?? -1) == 4 || (ingredients[1] ?? -1) == 4 || (ingredients[2] ?? -1) == 4) && (((ingredients[0] ?? -1) == 22 || (ingredients[1] ?? -1) == 22 || (ingredients[2] ?? -1) == 22) && ((ingredients[0] ?? -1) == 45 || (ingredients[1] ?? -1) == 45 || (ingredients[2] ?? -1) == 45)))) {
                        add(113);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            }
        } else if (_eggID == 1) {
            if (_tool1_ID == 0) {
                let tool_1_level9 = state.toolLevels[_tool1_ID];
                add(0);
                if (totalChars1Cnt >= 1000.0) {
                    add(16);
                }
                if (!!state.events?.["campaign_char_1_35"]) {
                    add(35);
                }
                if (!!state.events?.["campaign_char_1_47"]) {
                    add(47);
                }
                if (!!state.events?.["campaign_char_1_48"]) {
                    add(48);
                }
                if (!!state.events?.["campaign_char_1_49"]) {
                    add(49);
                }
                if (!!state.events?.["campaign_char_1_51"]) {
                    add(51);
                }
                if ((ingredients[0] ?? -1) == 27 || (ingredients[1] ?? -1) == 27 || (ingredients[2] ?? -1) == 27) {
                    add(4);
                }
                if ((ingredients[0] ?? -1) == 11 || (ingredients[1] ?? -1) == 11 || (ingredients[2] ?? -1) == 11) {
                    add(14);
                }
                if ((ingredients[0] ?? -1) == 18 || (ingredients[1] ?? -1) == 18 || (ingredients[2] ?? -1) == 18) {
                    add(15);
                }
                if ((ingredients[0] ?? -1) == 36 || (ingredients[1] ?? -1) == 36 || (ingredients[2] ?? -1) == 36) {
                    add(26);
                }
                if (tool_1_level9 >= 1) {
                    add(17);
                    add(18);
                }
                if (tool_1_level9 >= 2) {
                    let randIndex2 = (preview ? 0 : Math.trunc(random() * 8.0));
                    if (randIndex2 == 0) {
                        add(27);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 1) {
                let tool_1_level10 = state.toolLevels[_tool1_ID];
                add(3);
                if ((ingredients[0] ?? -1) == 44 || (ingredients[1] ?? -1) == 44 || (ingredients[2] ?? -1) == 44) {
                    add(5);
                }
                if (tool_1_level10 >= 1) {
                    if (((ingredients[0] ?? -1) == 23 || (ingredients[1] ?? -1) == 23 || (ingredients[2] ?? -1) == 23) && ((ingredients[0] ?? -1) == 46 || (ingredients[1] ?? -1) == 46 || (ingredients[2] ?? -1) == 46)) {
                        add(21);
                    }
                    if (tool_1_level10 >= 2 && (((ingredients[0] ?? -1) == 34 || (ingredients[1] ?? -1) == 34 || (ingredients[2] ?? -1) == 34) && (((ingredients[0] ?? -1) == 41 || (ingredients[1] ?? -1) == 41 || (ingredients[2] ?? -1) == 41) && ((ingredients[0] ?? -1) == 50 || (ingredients[1] ?? -1) == 50 || (ingredients[2] ?? -1) == 50)))) {
                        add(30);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 2) {
                let tool_1_level11 = state.toolLevels[_tool1_ID];
                add(6);
                if ((ingredients[0] ?? -1) == 27 || (ingredients[1] ?? -1) == 27 || (ingredients[2] ?? -1) == 27) {
                    add(7);
                }
                if (tool_1_level11 >= 1) {
                    if (((ingredients[0] ?? -1) == 47 || (ingredients[1] ?? -1) == 47 || (ingredients[2] ?? -1) == 47) && ((ingredients[0] ?? -1) == 48 || (ingredients[1] ?? -1) == 48 || (ingredients[2] ?? -1) == 48)) {
                        add(22);
                    }
                    if (tool_1_level11 >= 2 && (((ingredients[0] ?? -1) == 46 || (ingredients[1] ?? -1) == 46 || (ingredients[2] ?? -1) == 46) && (((ingredients[0] ?? -1) == 51 || (ingredients[1] ?? -1) == 51 || (ingredients[2] ?? -1) == 51) && ((ingredients[0] ?? -1) == 52 || (ingredients[1] ?? -1) == 52 || (ingredients[2] ?? -1) == 52)))) {
                        add(31);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 3) {
                let tool_1_level12 = state.toolLevels[_tool1_ID];
                add(8);
                if ((ingredients[0] ?? -1) == 45 || (ingredients[1] ?? -1) == 45 || (ingredients[2] ?? -1) == 45) {
                    add(9);
                }
                if (tool_1_level12 >= 1) {
                    if (((ingredients[0] ?? -1) == 25 || (ingredients[1] ?? -1) == 25 || (ingredients[2] ?? -1) == 25) && ((ingredients[0] ?? -1) == 32 || (ingredients[1] ?? -1) == 32 || (ingredients[2] ?? -1) == 32)) {
                        add(23);
                    }
                    if (tool_1_level12 >= 2 && (((ingredients[0] ?? -1) == 29 || (ingredients[1] ?? -1) == 29 || (ingredients[2] ?? -1) == 29) && (((ingredients[0] ?? -1) == 46 || (ingredients[1] ?? -1) == 46 || (ingredients[2] ?? -1) == 46) && ((ingredients[0] ?? -1) == 53 || (ingredients[1] ?? -1) == 53 || (ingredients[2] ?? -1) == 53)))) {
                        add(32);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 4) {
                let tool_1_level13 = state.toolLevels[_tool1_ID];
                add(10);
                if ((ingredients[0] ?? -1) == 33 || (ingredients[1] ?? -1) == 33 || (ingredients[2] ?? -1) == 33) {
                    add(11);
                }
                if (tool_1_level13 >= 1) {
                    if (((ingredients[0] ?? -1) == 26 || (ingredients[1] ?? -1) == 26 || (ingredients[2] ?? -1) == 26) && ((ingredients[0] ?? -1) == 27 || (ingredients[1] ?? -1) == 27 || (ingredients[2] ?? -1) == 27)) {
                        add(24);
                    }
                    if (!!state.events?.["campaign_char_1_50"] && (((ingredients[0] ?? -1) == 9 || (ingredients[1] ?? -1) == 9 || (ingredients[2] ?? -1) == 9) && ((ingredients[0] ?? -1) == 29 || (ingredients[1] ?? -1) == 29 || (ingredients[2] ?? -1) == 29))) {
                        add(50);
                    }
                    if (tool_1_level13 >= 2 && (((ingredients[0] ?? -1) == 46 || (ingredients[1] ?? -1) == 46 || (ingredients[2] ?? -1) == 46) && (((ingredients[0] ?? -1) == 54 || (ingredients[1] ?? -1) == 54 || (ingredients[2] ?? -1) == 54) && ((ingredients[0] ?? -1) == 55 || (ingredients[1] ?? -1) == 55 || (ingredients[2] ?? -1) == 55)))) {
                        add(33);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 5) {
                let tool_1_level14 = state.toolLevels[_tool1_ID];
                add(12);
                if ((ingredients[0] ?? -1) == 27 || (ingredients[1] ?? -1) == 27 || (ingredients[2] ?? -1) == 27) {
                    add(13);
                }
                if (tool_1_level14 >= 1) {
                    if (((ingredients[0] ?? -1) == 45 || (ingredients[1] ?? -1) == 45 || (ingredients[2] ?? -1) == 45) && ((ingredients[0] ?? -1) == 49 || (ingredients[1] ?? -1) == 49 || (ingredients[2] ?? -1) == 49)) {
                        add(25);
                    }
                    if (tool_1_level14 >= 2 && (((ingredients[0] ?? -1) == 12 || (ingredients[1] ?? -1) == 12 || (ingredients[2] ?? -1) == 12) && (((ingredients[0] ?? -1) == 28 || (ingredients[1] ?? -1) == 28 || (ingredients[2] ?? -1) == 28) && ((ingredients[0] ?? -1) == 29 || (ingredients[1] ?? -1) == 29 || (ingredients[2] ?? -1) == 29)))) {
                        add(34);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 6) {
                let tool_1_level15 = state.toolLevels[_tool1_ID];
                add(37);
                if ((ingredients[0] ?? -1) == 63 || (ingredients[1] ?? -1) == 63 || (ingredients[2] ?? -1) == 63) {
                    add(39);
                }
                if ((ingredients[0] ?? -1) == 64 || (ingredients[1] ?? -1) == 64 || (ingredients[2] ?? -1) == 64) {
                    add(40);
                }
                if ((ingredients[0] ?? -1) == 3 || (ingredients[1] ?? -1) == 3 || (ingredients[2] ?? -1) == 3) {
                    add(41);
                }
                if (tool_1_level15 >= 1) {
                    if (((ingredients[0] ?? -1) == 1 || (ingredients[1] ?? -1) == 1 || (ingredients[2] ?? -1) == 1) && ((ingredients[0] ?? -1) == 63 || (ingredients[1] ?? -1) == 63 || (ingredients[2] ?? -1) == 63)) {
                        add(42);
                    }
                    if (((ingredients[0] ?? -1) == 57 || (ingredients[1] ?? -1) == 57 || (ingredients[2] ?? -1) == 57) && ((ingredients[0] ?? -1) == 63 || (ingredients[1] ?? -1) == 63 || (ingredients[2] ?? -1) == 63)) {
                        add(43);
                    }
                    if (((ingredients[0] ?? -1) == 65 || (ingredients[1] ?? -1) == 65 || (ingredients[2] ?? -1) == 65) && ((ingredients[0] ?? -1) == 66 || (ingredients[1] ?? -1) == 66 || (ingredients[2] ?? -1) == 66)) {
                        add(44);
                    }
                    if (tool_1_level15 >= 2) {
                        if (((ingredients[0] ?? -1) == 59 || (ingredients[1] ?? -1) == 59 || (ingredients[2] ?? -1) == 59) && (((ingredients[0] ?? -1) == 60 || (ingredients[1] ?? -1) == 60 || (ingredients[2] ?? -1) == 60) && ((ingredients[0] ?? -1) == 67 || (ingredients[1] ?? -1) == 67 || (ingredients[2] ?? -1) == 67))) {
                            add(45);
                        }
                        if (((ingredients[0] ?? -1) == 29 || (ingredients[1] ?? -1) == 29 || (ingredients[2] ?? -1) == 29) && (((ingredients[0] ?? -1) == 57 || (ingredients[1] ?? -1) == 57 || (ingredients[2] ?? -1) == 57) && ((ingredients[0] ?? -1) == 63 || (ingredients[1] ?? -1) == 63 || (ingredients[2] ?? -1) == 63))) {
                            add(46);
                        }
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            } else if (_tool1_ID == 7) {
                let tool_1_level16 = state.toolLevels[_tool1_ID];
                add(52);
                if ((ingredients[0] ?? -1) == 57 || (ingredients[1] ?? -1) == 57 || (ingredients[2] ?? -1) == 57) {
                    add(54);
                }
                if (tool_1_level16 >= 1) {
                    if (((ingredients[0] ?? -1) == 73 || (ingredients[1] ?? -1) == 73 || (ingredients[2] ?? -1) == 73) && ((ingredients[0] ?? -1) == 74 || (ingredients[1] ?? -1) == 74 || (ingredients[2] ?? -1) == 74)) {
                        add(55);
                    }
                    if (tool_1_level16 >= 2 && (((ingredients[0] ?? -1) == 23 || (ingredients[1] ?? -1) == 23 || (ingredients[2] ?? -1) == 23) && (((ingredients[0] ?? -1) == 24 || (ingredients[1] ?? -1) == 24 || (ingredients[2] ?? -1) == 24) && ((ingredients[0] ?? -1) == 47 || (ingredients[1] ?? -1) == 47 || (ingredients[2] ?? -1) == 47)))) {
                        add(56);
                    }
                }
                returnRateArrayList = (preview ? [] : samplePool(allRateArrayList, random));
            }
        }
        return preview ? {pool:allRateArrayList,gifts} : returnRateArrayList;

}

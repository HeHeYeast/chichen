package com.idtinc.maingame.sublayout0;

import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckunit.CharacterUnitDictionary;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CharacterUnit {
    private MainGameUnit mainGameUnit;
    private float zoomRate = 1.0f;
    public short tag = -1;
    public float frameOriginX = BitmapDescriptorFactory.HUE_RED;
    public float frameOriginY = BitmapDescriptorFactory.HUE_RED;
    public float frameSizeWidth = BitmapDescriptorFactory.HUE_RED;
    public float frameSizeHeight = BitmapDescriptorFactory.HUE_RED;
    public float alpha = 1.0f;
    public boolean hidden = true;
    public float imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
    public float imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
    public float imageView0SizeWidth = this.frameSizeWidth;
    public float imageView0SizeHeight = this.frameSizeHeight;
    public float imageView0Alpha = 1.0f;
    public boolean imageView0Hidden = true;
    public short imageView0Image = -1;
    public float imageView0TransformX = 1.0f;
    public float imageView0TransformY = 1.0f;
    public float imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
    public float imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
    public float imageView1SizeWidth = this.frameSizeWidth;
    public float imageView1SizeHeight = this.frameSizeHeight;
    public float imageView1Alpha = 1.0f;
    public boolean imageView1Hidden = true;
    public short imageView1Image = -1;
    public float imageView1TransformX = 1.0f;
    public float imageView1TransformY = 1.0f;
    public short changeDirCnt = 0;
    public short goToStatusNextCnt = -1;
    public short animeType = 0;
    public short originPositionType = 0;
    public float originPositionX = this.frameOriginX;
    public float originPositionY = this.frameOriginY;

    public void init(float _frameOriginX, float _frameOriginY, float _frameSizeWidth, float _frameSizeHeight, float _zoomrate, MainGameUnit _mainGameUnit) {
        this.mainGameUnit = _mainGameUnit;
        this.zoomRate = _zoomrate;
        this.tag = (short) -1;
        this.frameOriginX = this.zoomRate * _frameOriginX;
        this.frameOriginY = this.zoomRate * _frameOriginY;
        this.frameSizeWidth = this.zoomRate * _frameSizeWidth;
        this.frameSizeHeight = this.zoomRate * _frameSizeHeight;
        this.alpha = 1.0f;
        this.hidden = true;
        this.changeDirCnt = (short) 0;
        cancelAllPreviousCnt();
        this.animeType = (short) 0;
        this.originPositionType = (short) 0;
        this.originPositionX = this.frameOriginX;
        this.originPositionY = this.frameOriginY;
        this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
        this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
        this.imageView0SizeWidth = this.frameSizeWidth;
        this.imageView0SizeHeight = this.frameSizeHeight;
        this.imageView0Alpha = 1.0f;
        this.imageView0Hidden = true;
        this.imageView0Image = (short) -1;
        this.imageView0TransformX = 1.0f;
        this.imageView0TransformY = 1.0f;
        this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
        this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
        this.imageView1SizeWidth = this.frameSizeWidth;
        this.imageView1SizeHeight = this.frameSizeHeight;
        this.imageView1Alpha = 1.0f;
        this.imageView1Hidden = true;
        this.imageView1Image = (short) -1;
        this.imageView1TransformX = 1.0f;
        this.imageView1TransformY = 1.0f;
        setNewStatus((short) -1);
    }

    public void reset() {
        this.changeDirCnt = (short) 0;
        cancelAllPreviousCnt();
        this.animeType = (short) 0;
        this.originPositionType = (short) 0;
        this.alpha = 1.0f;
        this.frameOriginX = this.zoomRate * (-999.0f);
        this.frameOriginY = this.zoomRate * (-999.0f);
        this.frameSizeWidth = this.frameSizeWidth;
        this.frameSizeHeight = this.frameSizeHeight;
        this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
        this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
        this.imageView0SizeWidth = this.frameSizeWidth;
        this.imageView0SizeHeight = this.frameSizeHeight;
        this.imageView0Alpha = 1.0f;
        this.imageView0Hidden = true;
        this.imageView0Image = (short) -1;
        this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
        this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
        this.imageView1SizeWidth = this.frameSizeWidth;
        this.imageView1SizeHeight = this.frameSizeHeight;
        this.imageView1Alpha = 1.0f;
        this.imageView1Hidden = true;
        this.imageView1Image = (short) -1;
        this.hidden = false;
    }

    public void cancelAllPreviousCnt() {
        this.goToStatusNextCnt = (short) -1;
    }

    public void setNewStatus(short _newstatus) {
        cancelAllPreviousCnt();
        CharacterUnitDictionary nowCharacterUnitDictionary = this.mainGameUnit.appDelegate.getCharacterUnitDictionaryWithIndex(this.tag);
        if (nowCharacterUnitDictionary != null) {
            if (_newstatus == -1) {
                nowCharacterUnitDictionary.setEggId((short) -1);
                nowCharacterUnitDictionary.setCharacterId((short) -1);
                nowCharacterUnitDictionary.setNowStatus((short) -1);
                nowCharacterUnitDictionary.setPutDate("");
                nowCharacterUnitDictionary.setEndSeconds(-1.0f);
                nowCharacterUnitDictionary.setOpenSeconds(-1.0f);
                nowCharacterUnitDictionary.setBlackSeconds(-1.0f);
                nowCharacterUnitDictionary.setOffsetX(-999);
                nowCharacterUnitDictionary.setOffsetY(-999);
                this.changeDirCnt = (short) 0;
                this.animeType = (short) 0;
                this.originPositionType = (short) 0;
                this.alpha = 1.0f;
                this.frameOriginX = (-999.0f) * this.zoomRate;
                this.frameOriginY = (-999.0f) * this.zoomRate;
                this.frameSizeWidth = this.frameSizeWidth;
                this.frameSizeHeight = this.frameSizeHeight;
                this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                this.imageView0SizeWidth = this.frameSizeWidth;
                this.imageView0SizeHeight = this.frameSizeHeight;
                this.imageView0Alpha = 1.0f;
                this.imageView0Hidden = true;
                this.imageView0Image = (short) -1;
                this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                this.imageView1SizeWidth = this.frameSizeWidth;
                this.imageView1SizeHeight = this.frameSizeHeight;
                this.imageView1Alpha = 1.0f;
                this.imageView1Hidden = true;
                this.imageView1Image = (short) -1;
                this.hidden = true;
                return;
            }
            if (_newstatus == 0) {
                this.animeType = (short) 0;
                this.originPositionType = (short) 0;
                this.alpha = 1.0f;
                this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                this.imageView0SizeWidth = this.frameSizeWidth;
                this.imageView0SizeHeight = this.frameSizeHeight;
                this.imageView0Alpha = 1.0f;
                this.imageView0Hidden = true;
                this.imageView0Image = (short) -1;
                this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                this.imageView1SizeWidth = this.frameSizeWidth;
                this.imageView1SizeHeight = this.frameSizeHeight;
                this.imageView1Alpha = 1.0f;
                this.imageView1Hidden = false;
                this.imageView1Image = (short) 0;
                short randImageView1DirIndex = (short) (Math.random() * 2.0d);
                if (randImageView1DirIndex <= 0) {
                    this.imageView1TransformX *= -1.0f;
                    this.imageView1TransformY = 1.0f;
                }
                nowCharacterUnitDictionary.setNowStatus(_newstatus);
                this.hidden = false;
                Log.d("zoomRate", "zoomRate=" + this.zoomRate);
                Log.d("imgOffsetX", "imageView1SizeWidth=" + this.imageView1SizeWidth);
                Log.d("imgOffsetY", "imageView1SizeHeight=" + this.imageView1SizeHeight);
                return;
            }
            if (_newstatus == 1) {
                if (nowCharacterUnitDictionary.getNowStatus() == 0) {
                    this.alpha = 1.0f;
                    this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0SizeWidth = this.frameSizeWidth;
                    this.imageView0SizeHeight = this.frameSizeHeight;
                    this.imageView0Alpha = 1.0f;
                    this.imageView0Hidden = true;
                    this.imageView0Image = (short) -1;
                    this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1SizeWidth = this.frameSizeWidth;
                    this.imageView1SizeHeight = this.frameSizeHeight;
                    this.imageView1Alpha = 1.0f;
                    this.imageView1Hidden = false;
                    this.imageView1Image = (short) 1;
                    nowCharacterUnitDictionary.setNowStatus(_newstatus);
                    this.hidden = false;
                    if (!this.mainGameUnit.hidden) {
                        this.mainGameUnit.appDelegate.doSoundPoolPlay(11);
                    }
                    this.goToStatusNextCnt = (short) 20;
                    return;
                }
                return;
            }
            if (_newstatus == 2) {
                if (nowCharacterUnitDictionary.getNowStatus() == 1) {
                    short eggID = nowCharacterUnitDictionary.getEggId();
                    short characterID = nowCharacterUnitDictionary.getCharacterId();
                    this.alpha = 1.0f;
                    this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0SizeWidth = this.frameSizeWidth;
                    this.imageView0SizeHeight = this.frameSizeHeight;
                    this.imageView0Alpha = 1.0f;
                    this.imageView0Hidden = false;
                    this.imageView0Image = characterID;
                    randDirWithEggID(eggID, characterID, true);
                    this.imageView1Alpha = 1.0f;
                    this.imageView1Hidden = false;
                    this.imageView1Image = (short) 2;
                    nowCharacterUnitDictionary.setNowStatus(_newstatus);
                    this.hidden = false;
                    if (!this.mainGameUnit.hidden) {
                        if (eggID == 1) {
                            this.mainGameUnit.appDelegate.doSoundPoolPlay(14);
                            return;
                        } else {
                            this.mainGameUnit.appDelegate.doSoundPoolPlay(12);
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            if (_newstatus == 3) {
                short eggID2 = nowCharacterUnitDictionary.getEggId();
                short characterID2 = nowCharacterUnitDictionary.getCharacterId();
                this.originPositionType = (short) 0;
                this.alpha = 1.0f;
                this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                this.imageView0SizeWidth = this.frameSizeWidth;
                this.imageView0SizeHeight = this.frameSizeHeight;
                this.imageView0Image = nowCharacterUnitDictionary.getCharacterId();
                this.imageView0Hidden = false;
                this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                this.imageView1SizeWidth = this.frameSizeWidth;
                this.imageView1SizeHeight = this.frameSizeHeight;
                randDirWithEggID(eggID2, characterID2, false);
                this.imageView1Alpha = 1.0f;
                this.imageView1Hidden = true;
                this.imageView1Image = (short) -1;
                nowCharacterUnitDictionary.setNowStatus(_newstatus);
                this.hidden = false;
                return;
            }
            if (_newstatus == 4) {
                this.originPositionType = (short) 0;
                this.alpha = 1.0f;
                this.imageView0Hidden = false;
                this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                this.imageView1SizeWidth = this.frameSizeWidth;
                this.imageView1SizeHeight = this.frameSizeHeight;
                this.imageView1Hidden = true;
                nowCharacterUnitDictionary.setNowStatus(_newstatus);
                nowCharacterUnitDictionary.setOffsetX(-999);
                nowCharacterUnitDictionary.setOffsetY(-999);
                this.hidden = false;
                if (!this.mainGameUnit.hidden) {
                    this.mainGameUnit.appDelegate.doSoundPoolPlay(1);
                    return;
                }
                return;
            }
            if (_newstatus == 5) {
                if (nowCharacterUnitDictionary.getNowStatus() == 4) {
                    this.originPositionType = (short) 1;
                    this.alpha = 1.0f;
                    this.imageView0Hidden = false;
                    this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1SizeWidth = this.frameSizeWidth;
                    this.imageView1SizeHeight = this.frameSizeHeight;
                    this.imageView1Hidden = true;
                    nowCharacterUnitDictionary.setNowStatus(_newstatus);
                    nowCharacterUnitDictionary.setOffsetX(-999);
                    nowCharacterUnitDictionary.setOffsetY(-999);
                    this.hidden = false;
                    return;
                }
                return;
            }
            if (_newstatus == 6) {
                if (nowCharacterUnitDictionary.getNowStatus() == 5) {
                    this.originPositionType = (short) 1;
                    this.alpha = 1.0f;
                    this.imageView0OriginX = this.imageView0OriginX;
                    this.imageView0OriginY = this.imageView0OriginY;
                    this.imageView0SizeWidth = this.frameSizeWidth;
                    this.imageView0SizeHeight = this.frameSizeHeight;
                    this.imageView0Hidden = false;
                    this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1SizeWidth = this.frameSizeWidth;
                    this.imageView1SizeHeight = this.frameSizeHeight;
                    this.imageView1Hidden = true;
                    nowCharacterUnitDictionary.setNowStatus(_newstatus);
                    nowCharacterUnitDictionary.setOffsetX(-999);
                    nowCharacterUnitDictionary.setOffsetY(-999);
                    this.hidden = false;
                    return;
                }
                return;
            }
            if (_newstatus == 7) {
                if (nowCharacterUnitDictionary.getNowStatus() == 6) {
                    this.originPositionType = (short) 1;
                    this.alpha = 1.0f;
                    this.imageView0Hidden = false;
                    this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1SizeWidth = this.frameSizeWidth;
                    this.imageView1SizeHeight = this.frameSizeHeight;
                    this.imageView1Hidden = true;
                    nowCharacterUnitDictionary.setNowStatus(_newstatus);
                    nowCharacterUnitDictionary.setOffsetX(-999);
                    nowCharacterUnitDictionary.setOffsetY(-999);
                    this.hidden = false;
                    return;
                }
                return;
            }
            if (_newstatus == 8) {
                if (nowCharacterUnitDictionary.getNowStatus() == 7) {
                    this.originPositionType = (short) 0;
                    this.alpha = 1.0f;
                    this.imageView0Hidden = false;
                    this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1SizeWidth = this.frameSizeWidth;
                    this.imageView1SizeHeight = this.frameSizeHeight;
                    this.imageView1Hidden = true;
                    nowCharacterUnitDictionary.setNowStatus(_newstatus);
                    nowCharacterUnitDictionary.setOffsetX(-999);
                    nowCharacterUnitDictionary.setOffsetY(-999);
                    this.hidden = false;
                    if (!this.mainGameUnit.hidden) {
                        this.mainGameUnit.appDelegate.doSoundPoolPlay(1);
                        return;
                    }
                    return;
                }
                return;
            }
            if (_newstatus == 9) {
                this.originPositionType = (short) 1;
                this.alpha = 1.0f;
                nowCharacterUnitDictionary.setNowStatus(_newstatus);
                nowCharacterUnitDictionary.setOffsetX(-999);
                nowCharacterUnitDictionary.setOffsetY(-999);
                this.hidden = true;
                this.mainGameUnit.getPointWithCharacter(this, false);
                setNewStatus((short) -1);
            }
        }
    }

    public void directToCP() {
        this.mainGameUnit.getPointWithCharacter(this, true);
        setNewStatus((short) -1);
    }

    public void goToStatus2() {
        CharacterUnitDictionary nowCharacterUnitDictionary = this.mainGameUnit.appDelegate.getCharacterUnitDictionaryWithIndex(this.tag);
        if (nowCharacterUnitDictionary != null && nowCharacterUnitDictionary.getNowStatus() == 1) {
            setNewStatus((short) 2);
        }
    }

    public void goToStatus6() {
        CharacterUnitDictionary nowCharacterUnitDictionary = this.mainGameUnit.appDelegate.getCharacterUnitDictionaryWithIndex(this.tag);
        if (nowCharacterUnitDictionary != null && nowCharacterUnitDictionary.getNowStatus() == 5) {
            setNewStatus((short) 6);
        }
    }

    public void goToStatus7() {
        CharacterUnitDictionary nowCharacterUnitDictionary = this.mainGameUnit.appDelegate.getCharacterUnitDictionaryWithIndex(this.tag);
        if (nowCharacterUnitDictionary != null && nowCharacterUnitDictionary.getNowStatus() == 6) {
            setNewStatus((short) 7);
        }
    }

    public void animeLoop() {
        CharacterUnitDictionary nowCharacterUnitDictionary = this.mainGameUnit.appDelegate.getCharacterUnitDictionaryWithIndex(this.tag);
        if (nowCharacterUnitDictionary != null) {
            if (nowCharacterUnitDictionary.getNowStatus() == 0) {
                if (this.animeType == 0) {
                    float height = (float) (this.imageView1SizeHeight + (1.0d * this.zoomRate));
                    if (height - this.frameSizeHeight >= 5.0f * this.zoomRate) {
                        this.animeType = (short) 1;
                        return;
                    }
                    float width = this.imageView1SizeWidth;
                    if (this.originPositionType == 0) {
                        this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                        this.imageView1OriginY = this.frameSizeHeight - height;
                        this.imageView1SizeWidth = width;
                        this.imageView1SizeHeight = height;
                        return;
                    }
                    this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1SizeWidth = width;
                    this.imageView1SizeHeight = height;
                    return;
                }
                if (this.animeType == 1) {
                    float height2 = (float) (this.imageView1SizeHeight - (1.0d * this.zoomRate));
                    if (height2 - this.frameSizeHeight <= BitmapDescriptorFactory.HUE_RED) {
                        this.animeType = (short) 0;
                        return;
                    }
                    float width2 = this.imageView1SizeWidth;
                    if (this.originPositionType == 0) {
                        this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                        this.imageView1OriginY = this.frameSizeHeight - height2;
                        this.imageView1SizeWidth = width2;
                        this.imageView1SizeHeight = height2;
                        return;
                    }
                    this.imageView1OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView1SizeWidth = width2;
                    this.imageView1SizeHeight = height2;
                    return;
                }
                return;
            }
            if (nowCharacterUnitDictionary.getNowStatus() == 1) {
                this.goToStatusNextCnt = (short) (this.goToStatusNextCnt - 1);
                if (this.goToStatusNextCnt <= 0) {
                    this.goToStatusNextCnt = (short) -1;
                    goToStatus2();
                    return;
                }
                return;
            }
            if (nowCharacterUnitDictionary.getNowStatus() >= 2 && nowCharacterUnitDictionary.getNowStatus() <= 3) {
                short randAddCnt = (short) (Math.random() * 2.0d);
                this.changeDirCnt = (short) (this.changeDirCnt + randAddCnt);
                if (this.changeDirCnt >= 30) {
                    this.changeDirCnt = (short) 0;
                    short eggID = nowCharacterUnitDictionary.getEggId();
                    short characterID = nowCharacterUnitDictionary.getCharacterId();
                    if (eggID == 0) {
                        if (characterID == 0 || characterID == 4 || characterID == 5 || characterID == 8 || characterID == 10 || characterID == 11 || characterID == 12 || characterID == 13 || characterID == 14 || characterID == 15 || characterID == 16 || characterID == 18 || characterID == 21 || characterID == 23 || characterID == 24 || characterID == 26 || characterID == 29 || characterID == 31 || characterID == 32 || characterID == 33 || characterID == 34 || characterID == 36 || characterID == 37 || characterID == 38 || characterID == 39 || characterID == 40 || characterID == 41 || characterID == 42 || characterID == 43 || characterID == 44 || characterID == 45 || characterID == 47 || characterID == 51 || characterID == 52 || characterID == 53 || characterID == 55 || characterID == 57 || characterID == 58 || characterID == 60 || characterID == 62 || characterID == 63 || characterID == 64 || characterID == 65 || characterID == 67 || characterID == 68 || characterID == 69 || characterID == 70 || characterID == 71 || characterID == 72 || characterID == 74 || characterID == 79 || characterID == 80 || characterID == 81 || characterID == 106 || characterID == 107 || characterID == 108) {
                            randDirWithEggID(eggID, characterID, true);
                        }
                    } else if (eggID == 1 && (characterID == 0 || characterID == 4 || characterID == 6 || characterID == 7 || characterID == 8 || characterID == 9 || characterID == 10 || characterID == 11 || characterID == 12 || characterID == 14 || characterID == 16 || characterID == 17 || characterID == 18 || characterID == 19 || characterID == 20 || characterID == 21 || characterID == 24 || characterID == 25 || characterID == 26 || characterID == 27 || characterID == 28 || characterID == 29 || characterID == 31 || characterID == 32 || characterID == 36 || characterID == 37 || characterID == 38 || characterID == 39 || characterID == 40 || characterID == 41 || characterID == 42 || characterID == 44 || characterID == 46 || characterID == 47 || characterID == 48 || characterID == 51)) {
                        randDirWithEggID(eggID, characterID, true);
                    }
                }
                if (this.animeType == 0) {
                    float height3 = this.imageView0SizeHeight + (1.0f * this.zoomRate);
                    if (height3 - this.frameSizeHeight >= 5.0f * this.zoomRate) {
                        this.animeType = (short) 1;
                    } else {
                        float width3 = this.imageView0SizeWidth;
                        if (this.originPositionType == 0) {
                            this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                            this.imageView0OriginY = this.frameSizeHeight - height3;
                            this.imageView0SizeWidth = width3;
                            this.imageView0SizeHeight = height3;
                        } else {
                            this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                            this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                            this.imageView0SizeWidth = width3;
                            this.imageView0SizeHeight = height3;
                        }
                    }
                } else if (this.animeType == 1) {
                    float height4 = (float) (this.imageView0SizeHeight - (1.0d * this.zoomRate));
                    if (height4 - this.frameSizeHeight <= BitmapDescriptorFactory.HUE_RED) {
                        this.animeType = (short) 0;
                    } else {
                        float width4 = this.imageView0SizeWidth;
                        if (this.originPositionType == 0) {
                            this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                            this.imageView0OriginY = this.frameSizeHeight - height4;
                            this.imageView0SizeWidth = width4;
                            this.imageView0SizeHeight = height4;
                        } else {
                            this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                            this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                            this.imageView0SizeWidth = width4;
                            this.imageView0SizeHeight = height4;
                        }
                    }
                }
                if (nowCharacterUnitDictionary.getNowStatus() == 2) {
                    if (this.imageView1Alpha >= 0.9f) {
                        this.imageView1Alpha -= 0.02f;
                    } else {
                        this.imageView1Alpha -= 0.3f;
                    }
                    if (this.imageView1Alpha <= BitmapDescriptorFactory.HUE_RED) {
                        this.imageView1Alpha = BitmapDescriptorFactory.HUE_RED;
                        setNewStatus((short) 3);
                        return;
                    }
                    return;
                }
                return;
            }
            if (nowCharacterUnitDictionary.getNowStatus() == 4) {
                float height5 = (float) (this.imageView0SizeHeight + (3.0d * this.zoomRate));
                float width5 = this.imageView0SizeWidth;
                if (this.originPositionType == 0) {
                    this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0OriginY = this.frameSizeHeight - height5;
                    this.imageView0SizeWidth = width5;
                    this.imageView0SizeHeight = height5;
                } else {
                    this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0OriginY = this.frameSizeHeight - height5;
                    this.imageView0SizeWidth = width5;
                    this.imageView0SizeHeight = height5;
                }
                if (height5 - this.frameSizeHeight >= 12.0d * this.zoomRate) {
                    setNewStatus((short) 5);
                    return;
                }
                return;
            }
            if (nowCharacterUnitDictionary.getNowStatus() == 5) {
                float y = this.imageView0OriginY;
                if (y > (-45.0d) * this.zoomRate) {
                    float y2 = (float) (y - (15.0d * this.zoomRate));
                    float width6 = this.imageView0SizeWidth;
                    float height6 = (float) (this.imageView0SizeHeight - (7.0d * this.zoomRate));
                    if (height6 <= this.frameSizeHeight) {
                        height6 = this.frameSizeHeight;
                    }
                    if (this.originPositionType == 0) {
                        this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                        this.imageView0OriginY = this.frameSizeHeight - height6;
                        this.imageView0SizeWidth = width6;
                        this.imageView0SizeHeight = height6;
                    } else {
                        this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                        this.imageView0OriginY = y2;
                        this.imageView0SizeWidth = width6;
                        this.imageView0SizeHeight = height6;
                    }
                    if (y2 < (-45.0d) * this.zoomRate) {
                        this.goToStatusNextCnt = (short) 2;
                        return;
                    }
                    return;
                }
                this.goToStatusNextCnt = (short) (this.goToStatusNextCnt - 1);
                if (this.goToStatusNextCnt <= 0) {
                    this.goToStatusNextCnt = (short) -1;
                    goToStatus6();
                    return;
                }
                return;
            }
            if (nowCharacterUnitDictionary.getNowStatus() == 6) {
                float xMax = 255.0f * this.zoomRate;
                float x = this.imageView0OriginX;
                if (this.frameOriginX + x < xMax) {
                    float x2 = x + (25.0f * this.zoomRate);
                    float y3 = this.imageView0OriginY + (3.5f * this.zoomRate);
                    if (y3 >= (-35.0f) * this.zoomRate) {
                        y3 = (-35.0f) * this.zoomRate;
                    }
                    float width7 = this.imageView0SizeWidth;
                    float height7 = this.imageView0SizeHeight;
                    if (this.originPositionType == 0) {
                        this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                        this.imageView0OriginY = this.frameSizeHeight - height7;
                        this.imageView0SizeWidth = width7;
                        this.imageView0SizeHeight = height7;
                    } else {
                        this.imageView0OriginX = x2;
                        this.imageView0OriginY = y3;
                        this.imageView0SizeWidth = width7;
                        this.imageView0SizeHeight = height7;
                    }
                    if (this.frameOriginX + x2 >= xMax) {
                        this.goToStatusNextCnt = (short) 2;
                        return;
                    }
                    return;
                }
                this.goToStatusNextCnt = (short) (this.goToStatusNextCnt - 1);
                if (this.goToStatusNextCnt <= 0) {
                    this.goToStatusNextCnt = (short) -1;
                    goToStatus7();
                    return;
                }
                return;
            }
            if (nowCharacterUnitDictionary.getNowStatus() == 7) {
                float x3 = this.imageView0OriginX;
                float y4 = this.imageView0OriginY;
                float maxOffsetY = 90.0f * this.zoomRate;
                if (this.frameOriginY + y4 < maxOffsetY) {
                    y4 += 30.0f * this.zoomRate;
                    float width8 = this.imageView0SizeWidth;
                    float height8 = this.imageView0SizeHeight;
                    if (this.originPositionType == 0) {
                        this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                        this.imageView0OriginY = this.frameSizeHeight - height8;
                        this.imageView0SizeWidth = width8;
                        this.imageView0SizeHeight = height8;
                    } else {
                        this.imageView0OriginX = x3;
                        this.imageView0OriginY = y4;
                        this.imageView0SizeWidth = width8;
                        this.imageView0SizeHeight = height8;
                    }
                }
                if (this.frameOriginY + y4 >= 90.0f * this.zoomRate) {
                    setNewStatus((short) 8);
                    return;
                }
                return;
            }
            if (nowCharacterUnitDictionary.getNowStatus() == 8) {
                this.alpha -= 0.3f;
                float height9 = this.imageView0SizeHeight - (3.0f * this.zoomRate);
                if (height9 <= 30.0f * this.zoomRate) {
                    height9 = 30.0f * this.zoomRate;
                }
                float width9 = this.imageView0SizeWidth;
                if (this.originPositionType == 0) {
                    this.imageView0OriginX = this.imageView0OriginX;
                    this.imageView0OriginY = (this.imageView0OriginY + this.imageView0SizeWidth) - height9;
                    this.imageView0SizeWidth = width9;
                    this.imageView0SizeHeight = height9;
                } else {
                    this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
                    this.imageView0SizeWidth = width9;
                    this.imageView0SizeHeight = height9;
                }
                if (this.alpha <= BitmapDescriptorFactory.HUE_RED) {
                    this.alpha = BitmapDescriptorFactory.HUE_RED;
                    setNewStatus((short) 9);
                }
            }
        }
    }

    public void randDirWithEggID(short _eggID, short _characterID, boolean _againF) {
        short randDirIndex = 0;
        if (this.imageView0TransformX >= BitmapDescriptorFactory.HUE_RED) {
            randDirIndex = 1;
        }
        if (_againF) {
            randDirIndex = (short) (Math.random() * 2.0d);
        }
        if (randDirIndex <= 0) {
            this.imageView0TransformX = -1.0f;
        } else {
            this.imageView0TransformX = 1.0f;
        }
    }

    public void setCenter(float _centerx, float _centery) {
        this.frameOriginX = (this.zoomRate * _centerx) - (this.frameSizeWidth / 2.0f);
        this.frameOriginY = (this.zoomRate * _centery) - (this.frameSizeHeight / 2.0f);
    }

    public void onDestroy() {
        this.mainGameUnit = null;
    }
}

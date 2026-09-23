package com.idtinc.maingame.sublayout1;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CharacterDisplayUnit {
    private float CHARACTERDISPLAYVIEW_OFFSET_Y = 135.0f;
    private float CHARACTERUNITVIEW_SIZE = 48.0f;
    private float CHARACTERUNITVIEW_SPACE_X = 45.0f;
    private float zoomRate = 1.0f;
    public float frameOriginX = BitmapDescriptorFactory.HUE_RED;
    public float frameOriginY = BitmapDescriptorFactory.HUE_RED;
    public float frameOffsetX = BitmapDescriptorFactory.HUE_RED;
    public float frameOffsetY = BitmapDescriptorFactory.HUE_RED;
    public float frameSizeWidth = BitmapDescriptorFactory.HUE_RED;
    public float frameSizeHeight = BitmapDescriptorFactory.HUE_RED;
    float offsetYMax = 0.1f;
    float addY = BitmapDescriptorFactory.HUE_RED;
    public float alpha = 1.0f;
    public boolean hidden = true;
    public short eggID = -1;
    public short characterID = -1;
    public float imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
    public float imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
    public float imageView0SizeWidth = this.frameSizeWidth;
    public float imageView0SizeHeight = this.frameSizeHeight;
    public float imageView0Alpha = 1.0f;
    public boolean imageView0Hidden = true;
    public short imageView0Image = -1;
    public float imageView0TransformX = 1.0f;
    public float imageView0TransformY = 1.0f;
    public float shadowViewOriginX = BitmapDescriptorFactory.HUE_RED;
    public float shadowViewOriginY = BitmapDescriptorFactory.HUE_RED;
    public float shadowViewSizeWidth = this.frameSizeWidth;
    public float shadowViewSizeHeight = this.frameSizeHeight;
    public float shadowViewAlpha = 1.0f;
    public boolean shadowViewHidden = true;
    public short shadowViewImage = -1;
    public float shadowViewTransformX = 1.0f;
    public float shadowViewTransformY = 1.0f;
    public short changeDirCnt = 0;
    public short goToStatusNextCnt = -1;
    public short animeType = 0;
    public short originPositionType = 0;
    public float originPositionX = this.frameOriginX;
    public float originPositionY = this.frameOriginY;

    public void init(float _frameOriginX, float _frameOriginY, float _frameSizeWidth, float _frameSizeHeight, float _zoomrate) {
        this.zoomRate = _zoomrate;
        this.CHARACTERDISPLAYVIEW_OFFSET_Y = 135.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_SIZE = 48.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_SPACE_X = 45.0f * this.zoomRate;
        this.frameOriginX = _frameOriginX;
        this.frameOriginY = _frameOriginY;
        this.frameOffsetX = _frameOriginX;
        this.frameOffsetY = _frameOriginY;
        this.frameSizeWidth = _frameSizeWidth;
        this.frameSizeHeight = _frameSizeHeight;
        this.offsetYMax = this.frameSizeHeight * 0.1f;
        this.addY = this.frameSizeHeight / 180.0f;
        this.alpha = 1.0f;
        this.hidden = true;
        this.eggID = (short) -1;
        this.characterID = (short) -1;
        this.changeDirCnt = (short) 0;
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
        this.shadowViewOriginX = BitmapDescriptorFactory.HUE_RED;
        this.shadowViewOriginY = BitmapDescriptorFactory.HUE_RED;
        this.shadowViewSizeWidth = this.frameSizeWidth;
        this.shadowViewSizeHeight = this.frameSizeHeight;
        this.shadowViewAlpha = 1.0f;
        this.shadowViewHidden = true;
        this.shadowViewImage = (short) -1;
        this.shadowViewTransformX = 1.0f;
        this.shadowViewTransformY = 1.0f;
    }

    public void setWithEggID(short _eggID, short _characterID, float _frameOffsetX, float _frameOffsetY, float frameSizeWidth, float frameSizeHeight, boolean _randOffsetF) {
        this.eggID = _eggID;
        this.characterID = _characterID;
        this.changeDirCnt = (short) 0;
        this.animeType = (short) 0;
        if (this.eggID >= 0) {
            if (_randOffsetF) {
                float size = this.CHARACTERUNITVIEW_SIZE;
                short randY = (short) (Math.random() * ((int) (10.0f * this.zoomRate)));
                short offextY = (short) (this.frameOriginY + randY);
                short randX = (short) (Math.random() * ((int) (9.0f * this.zoomRate)));
                short offextX = (short) (this.frameOriginX + randX);
                this.frameOffsetX = offextX;
                this.frameOffsetY = offextY;
                this.frameSizeWidth = size;
                this.frameSizeHeight = size;
                this.shadowViewOriginX = BitmapDescriptorFactory.HUE_RED;
                this.shadowViewOriginY = this.frameSizeHeight / 20.0f;
                this.shadowViewSizeWidth = this.frameSizeWidth;
                this.shadowViewSizeHeight = this.frameSizeHeight;
            } else {
                this.frameOffsetX = _frameOffsetX;
                this.frameOffsetY = _frameOffsetY;
                this.frameSizeWidth = frameSizeWidth;
                this.frameSizeHeight = frameSizeHeight;
            }
            this.offsetYMax = this.frameSizeHeight * 0.1f;
            this.addY = this.frameSizeHeight / 180.0f;
            this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
            this.imageView0OriginY = BitmapDescriptorFactory.HUE_RED;
            this.imageView0SizeWidth = this.frameSizeWidth;
            this.imageView0SizeHeight = this.frameSizeHeight;
            randDirWithEggID(this.eggID, this.characterID, true);
        }
    }

    public void animeLoop() {
        if (this.eggID >= 0 && this.characterID >= 0) {
            short randAddCnt = (short) (Math.random() * 2.0d);
            this.changeDirCnt = (short) (this.changeDirCnt + randAddCnt);
            if (this.changeDirCnt >= 30) {
                this.changeDirCnt = (short) 0;
                if (this.eggID == 0) {
                    if (this.characterID == 0 || this.characterID == 4 || this.characterID == 5 || this.characterID == 8 || this.characterID == 10 || this.characterID == 11 || this.characterID == 12 || this.characterID == 13 || this.characterID == 14 || this.characterID == 15 || this.characterID == 16 || this.characterID == 18 || this.characterID == 21 || this.characterID == 23 || this.characterID == 24 || this.characterID == 26 || this.characterID == 29 || this.characterID == 31 || this.characterID == 32 || this.characterID == 33 || this.characterID == 34 || this.characterID == 36 || this.characterID == 37 || this.characterID == 38 || this.characterID == 39 || this.characterID == 40 || this.characterID == 41 || this.characterID == 42 || this.characterID == 43 || this.characterID == 44 || this.characterID == 45 || this.characterID == 47 || this.characterID == 51 || this.characterID == 52 || this.characterID == 53 || this.characterID == 55 || this.characterID == 57 || this.characterID == 58 || this.characterID == 60 || this.characterID == 62 || this.characterID == 63 || this.characterID == 64 || this.characterID == 65 || this.characterID == 67 || this.characterID == 68 || this.characterID == 69 || this.characterID == 70 || this.characterID == 71 || this.characterID == 72 || this.characterID == 74 || this.characterID == 79 || this.characterID == 80 || this.characterID == 81 || this.characterID == 106 || this.characterID == 107 || this.characterID == 108) {
                        randDirWithEggID(this.eggID, this.characterID, true);
                    }
                } else if (this.eggID == 1 && (this.characterID == 0 || this.characterID == 4 || this.characterID == 6 || this.characterID == 7 || this.characterID == 8 || this.characterID == 9 || this.characterID == 10 || this.characterID == 11 || this.characterID == 12 || this.characterID == 14 || this.characterID == 16 || this.characterID == 17 || this.characterID == 18 || this.characterID == 19 || this.characterID == 20 || this.characterID == 21 || this.characterID == 24 || this.characterID == 25 || this.characterID == 26 || this.characterID == 27 || this.characterID == 28 || this.characterID == 29 || this.characterID == 31 || this.characterID == 32 || this.characterID == 36 || this.characterID == 37 || this.characterID == 38 || this.characterID == 39 || this.characterID == 40 || this.characterID == 41 || this.characterID == 42 || this.characterID == 44 || this.characterID == 46 || this.characterID == 47 || this.characterID == 48 || this.characterID == 51)) {
                    randDirWithEggID(this.eggID, this.characterID, true);
                }
            }
            if (this.animeType == 0) {
                float width = this.imageView0SizeWidth;
                float height = this.imageView0SizeHeight + this.addY;
                float offsetY = height - this.frameSizeHeight;
                if (offsetY >= this.offsetYMax) {
                    float offsetY2 = this.offsetYMax;
                    height = this.frameSizeHeight + offsetY2;
                    this.animeType = (short) 1;
                }
                this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView0OriginY = this.frameSizeHeight - height;
                this.imageView0SizeWidth = width;
                this.imageView0SizeHeight = height;
                return;
            }
            if (this.animeType == 1) {
                float width2 = this.imageView0SizeWidth;
                float height2 = this.imageView0SizeHeight - this.addY;
                float offsetY3 = height2 - this.frameSizeHeight;
                if (offsetY3 <= 0.0d) {
                    height2 = this.frameSizeHeight;
                    this.animeType = (short) 0;
                }
                this.imageView0OriginX = BitmapDescriptorFactory.HUE_RED;
                this.imageView0OriginY = this.frameSizeHeight - height2;
                this.imageView0SizeWidth = width2;
                this.imageView0SizeHeight = height2;
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
}

package com.google.android.gms.maps.model;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface TileProvider {
    public static final Tile NO_TILE = new Tile(-1, -1, null);

    Tile getTile(int i, int i2, int i3);
}

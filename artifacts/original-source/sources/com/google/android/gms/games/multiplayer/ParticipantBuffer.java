package com.google.android.gms.games.multiplayer;

import com.google.android.gms.common.data.DataBuffer;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ParticipantBuffer extends DataBuffer<Participant> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.gms.common.data.DataBuffer
    public Participant get(int position) {
        return new d(this.jf, position);
    }
}

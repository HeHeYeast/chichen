package com.google.android.gms.games.leaderboard;

import c.NetworkManager;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eu;
import com.google.android.gms.internal.ev;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class e extends com.google.android.gms.common.data.b implements LeaderboardVariant {
    e(com.google.android.gms.common.data.d dVar, int i) {
        super(dVar, i);
    }

    public String ce() {
        return getString("top_page_token_next");
    }

    public String cf() {
        return getString("window_page_token_prev");
    }

    public String cg() {
        return getString("window_page_token_next");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public int getCollection() {
        return getInteger("collection");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public String getDisplayPlayerRank() {
        return getString("player_display_rank");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public String getDisplayPlayerScore() {
        return getString("player_display_score");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public long getNumScores() {
        if (v("total_scores")) {
            return -1L;
        }
        return getLong("total_scores");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public long getPlayerRank() {
        if (v("player_rank")) {
            return -1L;
        }
        return getLong("player_rank");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public String getPlayerScoreTag() {
        return getString("player_score_tag");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public long getRawPlayerScore() {
        if (v("player_raw_score")) {
            return -1L;
        }
        return getLong("player_raw_score");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public int getTimeSpan() {
        return getInteger("timespan");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public boolean hasPlayerInfo() {
        return !v("player_raw_score");
    }

    public String toString() {
        return dl.d(this).a("TimeSpan", ev.R(getTimeSpan())).a("Collection", eu.R(getCollection())).a("RawPlayerScore", hasPlayerInfo() ? Long.valueOf(getRawPlayerScore()) : NetworkManager.TYPE_NONE).a("DisplayPlayerScore", hasPlayerInfo() ? getDisplayPlayerScore() : NetworkManager.TYPE_NONE).a("PlayerRank", hasPlayerInfo() ? Long.valueOf(getPlayerRank()) : NetworkManager.TYPE_NONE).a("DisplayPlayerRank", hasPlayerInfo() ? getDisplayPlayerRank() : NetworkManager.TYPE_NONE).a("NumScores", Long.valueOf(getNumScores())).a("TopPageNextToken", ce()).a("WindowPageNextToken", cg()).a("WindowPagePrevToken", cf()).toString();
    }
}

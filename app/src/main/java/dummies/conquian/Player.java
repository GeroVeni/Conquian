package dummies.conquian;

import java.util.UUID;

/**
 * Created by George on 9/4/2018.
 */

public class Player {

    private UUID mGameUUID;
    private boolean mIsActive;
    private String mName;
    private int mScore;
    private int mHats;
    private int mCrowns;

    public Player(UUID gameUUID, String name) {
        mGameUUID = gameUUID;
        mIsActive = true;
        mName = name;
        mHats = 0;
        mScore = 0;
        mCrowns = 0;
    }

    public UUID getGameUUID() {
        return mGameUUID;
    }

    public void setGameUUID(UUID gameUUID) {
        this.mGameUUID = gameUUID;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getHats() {
        return mHats;
    }

    public void setHats(int hats) {
        mHats = hats;
    }

    public int getCrowns() {
        return mCrowns;
    }

    public void setCrowns(int crowns) {
        mCrowns = crowns;
    }

    public void winner() {
        mCrowns += 1;
    }
}

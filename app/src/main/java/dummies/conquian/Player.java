package dummies.conquian;

/**
 * Created by George on 9/4/2018.
 */

public class Player {

    private boolean mIsActive;
    private String mName;
    private int mScore;
    private int mHats;
    private int mCrowns;

    public Player(String name) {
        mIsActive = true;
        mName = name;
        mHats = 0;
        mScore = 0;
        mCrowns = 0;
    }

    public Player() {
        this("");
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

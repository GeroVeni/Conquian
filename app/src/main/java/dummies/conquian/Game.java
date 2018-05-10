package dummies.conquian;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class Game {

    private UUID mId;
    private String mName;
    private Date mDate;
    private List<Player> mPlayers;

    private boolean mIsToDelete;

    public Game(String name, int playerCount) {
        mId = UUID.randomUUID();
        mName = name;
        mDate = new Date();
        mPlayers = new ArrayList<>();
        mIsToDelete = false;
        for (int i=0; i < playerCount; i++) {
            Player player = new Player("Player #" + i);
            mPlayers.add(player);
        }
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public boolean isToDelete() {
        return mIsToDelete;
    }

    public void setToDelete(boolean toDelete) {
        mIsToDelete = toDelete;
    }
}

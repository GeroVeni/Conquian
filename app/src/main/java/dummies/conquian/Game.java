package dummies.conquian;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Game {

    private UUID mId;
    private String mName;
    private Date mDate;
    private List<Player> mPlayers;

    private boolean mIsToDelete;

    public Game(String name, int playerCount) {
        this(UUID.randomUUID(), name, new Date(), playerCount);
    }

    public Game(UUID id, String name, Date date, int playerCount) {
        mId = id;
        mName = name;
        mDate = date;
        mPlayers = new ArrayList<>();
        mIsToDelete = false;
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player(mId, "Player #" + i);
            addPlayer(player);
        }
    }

    public UUID getId() {
        return mId;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public void addPlayer(Player player) {
        mPlayers.add(player);
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

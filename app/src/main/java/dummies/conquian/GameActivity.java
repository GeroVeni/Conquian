package dummies.conquian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class GameActivity extends SingleFragmentActivity {

    public static String EXTRA_GAME_ID =
            "dummies.conquian.game_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {
        return new GameFragment();
    }

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(EXTRA_GAME_ID, uuid);
        return intent;
    }
}

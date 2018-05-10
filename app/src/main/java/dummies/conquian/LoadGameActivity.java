package dummies.conquian;

import android.support.v4.app.Fragment;

public class LoadGameActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new LoadGameFragment();
    }
}

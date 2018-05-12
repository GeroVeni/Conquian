package dummies.conquian;

import android.support.v4.app.Fragment;

public class InfoActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new InfoFragment();
    }
}

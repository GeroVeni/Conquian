package dummies.conquian;

import android.support.v4.app.Fragment;

public class MenuActivity extends SingleFragmentActivity {

    public static String TAG = "dummies.conquian";

    @Override
    public Fragment createFragment() {
        return new MenuFragment();
    }
}

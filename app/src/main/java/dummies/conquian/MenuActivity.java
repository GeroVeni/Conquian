package dummies.conquian;

import android.support.v4.app.Fragment;

public class MenuActivity extends SingleFragmentActivity {

    public static String TAG = "dummies.conquian";

    // TODO: 11/5/2018 Add info activity, help, options, contact, etc.
    
    @Override
    public Fragment createFragment() {
        return new MenuFragment();
    }
}

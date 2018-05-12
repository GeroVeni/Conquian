package dummies.conquian;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MenuFragment extends Fragment {

    private Button mNewGameButton;
    private Button mLoadGameButton;
    private Button mRulesButton;
    private ImageView mInfoImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mNewGameButton = view.findViewById(R.id.new_game_button);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                NewGameDialog dialog = new NewGameDialog();
                dialog.show(fragmentManager, NewGameDialog.TAG);
            }
        });

        mLoadGameButton = view.findViewById(R.id.load_game_button);
        mLoadGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoadGameActivity.class);
                startActivity(intent);
            }
        });

        // TODO: 12/5/2018 Implement rules activity
        mRulesButton = view.findViewById(R.id.rules_button);
        mRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mRulesButton.setVisibility(View.GONE);

        mInfoImageView = view.findViewById(R.id.info_image_view);
        mInfoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

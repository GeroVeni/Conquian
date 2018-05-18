package dummies.conquian;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by George on 9/4/2018.
 */

public class GameFragment extends Fragment {

    private static final String DIALOG_NAME = "DialogName";

    private static final String ARG_ID = "GameId";

    private static final int REQUEST_NAME = 0;

    private Game mGame;
    private RecyclerView mRecyclerView;
    private PlayerAdapter mAdapter;
    private Button mResetButton;
    private Button mGoButton;

    private boolean mIsDisableMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID gameId = (UUID) getActivity().getIntent()
                .getSerializableExtra(GameActivity.EXTRA_GAME_ID);
        mGame = GameBase.get(getActivity()).getGame(gameId);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsDisableMode = false;
    }

    // TODO: 18/5/2018 Change column width to display greek titles properly 
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        mRecyclerView = view.findViewById(R.id.player_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mResetButton = view.findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeResetDialog();
            }
        });

        mGoButton = view.findViewById(R.id.confirm_button);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player player;
                EditText editText;

                int diffs[]= new int[mRecyclerView.getChildCount()];

                final int playerCount = mRecyclerView.getChildCount();

                int activePlayerCount = 0;          // Number of active players
                int zeroTimes = 0;                  // Only one zero condition
                boolean maxValueExceeded = false;   // Hand value cannot exceed a certain threshold
                final int maxHandScore = 1000;      // Maximum hand value for sanity checks

                for (int i = 0; i < playerCount; i++) {
                    player = mGame.getPlayers().get(i);

                    if (player.isActive()) {
                        activePlayerCount ++;
                    } else {
                        continue;
                    }

                    editText = getScoreEditTextAt(i);
                    diffs[i]= parseScoreDifference(editText.getText().toString());

                    // Conditions update
                    if (diffs[i] == 0) zeroTimes++;
                    if (diffs[i] > maxHandScore) maxValueExceeded = true;
                }

                // Condition check
                if (zeroTimes != 1) {
                    Toast.makeText(getActivity(), R.string.zero_condition_toast, Toast.LENGTH_SHORT)
                            .show();
                } else if (maxValueExceeded) {
                    Toast.makeText(
                            getActivity(), R.string.max_value_condition_toast, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    updateScores(diffs, playerCount, activePlayerCount);
                }

                updateUI();
            }
        });

        updateUI();

        return view;
    }

    private void updateScores(int[] diffs, int count, int activeCount) {
        int maxScore = 0;                   // Maximum score in any hand
        int bustedPlayers = 0;              // Number of busted players
        final int bustedThreshold = 101;    // Maximum points to get busted
        Player player;

        for (int i = 0; i < count; i++) {
            player = mGame.getPlayers().get(i);
            if (!player.isActive()) continue;
            int score = player.getScore() + diffs[i];
            player.setScore(score);
            if (score > maxScore && score < bustedThreshold) maxScore = score;
            if (score >= bustedThreshold) bustedPlayers ++;
        }

        if (bustedPlayers == activeCount - 1) {
            // Winning condition
            for (int i = 0; i < count; i ++) {
                player = mGame.getPlayers().get(i);
                if (!player.isActive()) continue;
                if (player.getScore() < bustedThreshold) {
                    Toast.makeText(
                            getActivity(),
                            player.getName() + " is the winner!", Toast.LENGTH_LONG)
                            .show();
                    player.winner();
                }
                player.setHats(0);
                player.setScore(0);
            }
        } else {
            // Add hats to the busted players
            for (int i = 0; i < count; i++) {
                player = mGame.getPlayers().get(i);
                if (!player.isActive()) continue;
                if (player.getScore() >= bustedThreshold) {
                    player.setScore(maxScore);
                    int hats = player.getHats();
                    player.setHats(hats + 1);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_game, menu);

        MenuItem item = menu.findItem(R.id.menu_item_disable_players);
        int iconRes;
        if (mIsDisableMode) {
            iconRes = R.drawable.ic_menu_done;
            Toast.makeText(getActivity(), R.string.disable_toast, Toast.LENGTH_LONG).show();
        } else {
            iconRes = R.drawable.ic_menu_disable;
        }
        item.setIcon(iconRes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_disable_players:
                setDisableMode(!mIsDisableMode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setDisableMode(boolean disableMode) {
        mIsDisableMode = disableMode;
        mGoButton.setEnabled(!disableMode);
        mResetButton.setEnabled(!disableMode);
        getActivity().invalidateOptionsMenu();
        updateUI();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PlayerAdapter(mGame.getPlayers());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private EditText getScoreEditTextAt(int i) {
        View view = mRecyclerView.getChildAt(i);
        return (EditText) view.findViewById(R.id.list_item_player_edit_text);
    }

    private int parseScoreDifference(String str) {
        if (str.equals("")) return 0;
        return Integer.parseInt(str);
    }

    private class PlayerHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private Player mPlayer;

        private View mItemView;
        private TextView mNameTextView;
        private TextView mScoreTextView;
        private TextView mHatsTextView;
        private TextView mCrownTextView;
        private EditText mEditText;

        public PlayerHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            itemView.setOnClickListener(this);

            mNameTextView = itemView.findViewById(R.id.list_item_player_name_text_view);
            mNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mIsDisableMode) {
                        Toast.makeText(getActivity(), R.string.name_change_toast, Toast.LENGTH_SHORT)
                                .show();
                    }
                    toggleActive();
                }
            });
            mNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    FragmentManager fm = getFragmentManager();
                    EditNameFragment dialog = EditNameFragment.newInstance(mPlayer.getName(), getLayoutPosition());
                    dialog.setTargetFragment(GameFragment.this, REQUEST_NAME);
                    dialog.show(fm, DIALOG_NAME);
                    return true;
                }
            });
            mScoreTextView = itemView
                    .findViewById(R.id.list_item_player_score_text_view);
            mHatsTextView = itemView.findViewById(R.id.list_item_player_hats_text_view);
            mCrownTextView = itemView
                    .findViewById(R.id.list_item_player_crowns_text_view);
            mEditText = itemView.findViewById(R.id.list_item_player_edit_text);
        }

        public void bindPlayer(Player player) {
            mPlayer = player;

            mNameTextView.setText(mPlayer.getName());
            mScoreTextView.setText(String.valueOf(mPlayer.getScore()));
            mHatsTextView.setText(String.valueOf(mPlayer.getHats()));
            mCrownTextView.setText(String.valueOf(mPlayer.getCrowns()));
            mEditText.setText(null);

            if (mPlayer.isActive()) {
                mItemView.setAlpha(1.0f);
            } else {
                mItemView.setAlpha(0.5f);
            }
            mEditText.setEnabled(mPlayer.isActive());
        }

        @Override
        public void onClick(View view) {
            toggleActive();
        }

        private void toggleActive() {
            if (mIsDisableMode) {
                mPlayer.setActive(!mPlayer.isActive());
                updateUI();
            }
        }
    }

    private class PlayerAdapter extends RecyclerView.Adapter<PlayerHolder> {

        private List<Player> mPlayers;

        public PlayerAdapter(List<Player> players) {
            mPlayers = players;
        }

        @Override
        public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_player, parent, false);
            return new PlayerHolder(view);
        }

        @Override
        public void onBindViewHolder(PlayerHolder holder, int position) {
            Player player = mPlayers.get(position);
            holder.bindPlayer(player);
        }

        @Override
        public int getItemCount() {
            return mPlayers.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_NAME) {
            String name = data.getStringExtra(EditNameFragment.EXTRA_NAME);
            int position = data.getIntExtra(EditNameFragment.EXTRA_POSITION, 0);
            mGame.getPlayers().get(position).setName(name);
            updateUI();
        }
    }

    private void makeResetDialog() {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_reset, null);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.reset_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.reset_button_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Player player;
                                EditText editText;

                                for (int j = 0; j < mRecyclerView.getChildCount(); j++) {
                                    player = mGame.getPlayers().get(j);
                                    player.setScore(0);
                                    player.setHats(0);

                                    editText = getScoreEditTextAt(j);
                                    editText.setText(null);
                                }
                                updateUI();
                            }
                        })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }
}

package dummies.conquian;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class LoadGameFragment extends Fragment {

    private RecyclerView mRecycler;
    private GameAdapter mAdapter;
    private boolean mIsEraseMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mIsEraseMode = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_game, container, false);

        mRecycler = view.findViewById(R.id.game_recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_load_game, menu);
        if (mIsEraseMode) {
            menu.setGroupVisible(R.id.menu_group_base, false);
            menu.setGroupVisible(R.id.menu_group_erase, true);
        } else {
            menu.setGroupVisible(R.id.menu_group_base, true);
            menu.setGroupVisible(R.id.menu_group_erase, false);
        }
    }

    @Override
       public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_game:
                FragmentManager fragmentManager = getFragmentManager();
                NewGameDialog dialog = new NewGameDialog();
                dialog.show(fragmentManager, NewGameDialog.TAG);
                return true;
            case R.id.menu_item_erase_games:
                for (Game game : GameBase.get(getActivity()).getGames()) {
                    game.setToDelete(false);
                }
                setEraseMode(true);
                return true;
            case R.id.menu_item_select_all:
                for (Game game : GameBase.get(getActivity()).getGames()) {
                    game.setToDelete(true);
                }
                updateUI();
                return true;
            case R.id.menu_item_accept_erase:
                GameBase gameBase = GameBase.get(getActivity());
                List<Game> games = gameBase.getGames();
                int size = games.size();
                for (int i = size - 1; i >= 0; i--) {
                    Game game = games.get(i);
                    if (game.isToDelete()) gameBase.deleteGame(game);
                }
                setEraseMode(false);
                return true;
            case R.id.menu_item_cancel_erase:
                setEraseMode(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setEraseMode(boolean eraseMode) {
        mIsEraseMode = eraseMode;
        updateUI();
        getActivity().invalidateOptionsMenu();
    }

    public void updateUI() {
        GameBase gameBase = GameBase.get(getActivity());
        List<Game> games = gameBase.getGames();

        if (mAdapter == null) {
            mAdapter = new GameAdapter(games);
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.setGames(games);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class GameHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Game mGame;

        private CheckBox mEraseCheckBox;
        private TextView mNameTextView;
        private TextView mDateTextView;

        public GameHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mEraseCheckBox = itemView.findViewById(R.id.list_item_game_erase_check_box);
            mNameTextView = itemView.findViewById(R.id.list_item_game_name_text_view);
            mDateTextView = itemView.findViewById(R.id.list_item_game_date_text_view);
        }

        public void bindGame(Game game) {
            mGame = game;

            if (mIsEraseMode) {
                mEraseCheckBox.setVisibility(View.VISIBLE);
            } else {
                mEraseCheckBox.setVisibility(View.GONE);
            }
            mEraseCheckBox.setChecked(mGame.isToDelete());
            mEraseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mGame.setToDelete(b);
                }
            });

            mNameTextView.setText(mGame.getName());

            String dateFormat = "dd/MM/yy";
            String dateString = DateFormat.format(dateFormat, mGame.getDate()).toString();
            mDateTextView.setText(dateString);
        }

        @Override
        public void onClick(View view) {
            if (mIsEraseMode) {
                mGame.setToDelete(!mGame.isToDelete());
                mEraseCheckBox.setChecked(mGame.isToDelete());
            } else {
                Intent intent = GameActivity.newIntent(getActivity(), mGame.getId());
                startActivity(intent);
            }
        }
    }

    private class GameAdapter extends RecyclerView.Adapter<GameHolder> {

        private List<Game> mGames;

        public GameAdapter(List<Game> games) {
            mGames = games;
        }

        @NonNull
        @Override
        public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater
                    .inflate(R.layout.list_item_game, parent, false);
            return new GameHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GameHolder holder, int position) {
            Game game = mGames.get(position);
            holder.bindGame(game);
        }

        @Override
        public int getItemCount() {
            return mGames.size();
        }

        public void setGames(List<Game> games) {
            mGames = games;
        }
    }
}

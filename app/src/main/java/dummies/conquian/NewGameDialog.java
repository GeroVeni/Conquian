package dummies.conquian;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class NewGameDialog extends DialogFragment {

    public static final int PLAYER_OFFSET = 3;
    public static final String TAG = "DialogNewGame";

    private EditText mNameEditText;
    private SeekBar mPlayersSeekBar;
    private TextView mPlayerCountTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_new_game, null);

        mNameEditText = (EditText) view.findViewById(R.id.dialog_new_game_game_name);

        mPlayersSeekBar = (SeekBar) view.findViewById(R.id.dialog_new_game_player_seekbar);
        mPlayersSeekBar.setMax(GameBase.MAX_PLAYERS);

        mPlayersSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPlayerCountTextView.setText(String.valueOf(i + PLAYER_OFFSET));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mPlayerCountTextView = (TextView) view.findViewById(R.id.dialog_new_game_player_count);
        mPlayerCountTextView.setText(String.valueOf(mPlayersSeekBar.getProgress() + PLAYER_OFFSET));

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_game)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GameBase gameBase = GameBase.get(getActivity());
                                String gameName = mNameEditText.getText().toString();
                                if (gameName.equals("")) {
                                    gameName = getString(R.string.unnamed_game);
                                }
                                Game game = new Game(gameName,
                                        mPlayersSeekBar.getProgress() + PLAYER_OFFSET);
                                gameBase.addGame(game);
                                Intent intent = GameActivity.newIntent(getActivity(), game.getId());
                                startActivity(intent);
                            }
                        })
                .create();
    }
}

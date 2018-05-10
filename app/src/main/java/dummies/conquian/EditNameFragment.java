package dummies.conquian;

import android.app.Activity;
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

/**
 * Created by George on 9/4/2018.
 */

public class EditNameFragment extends DialogFragment {

    public static final String EXTRA_NAME =
            "dummies.conquian.name";
    public static final String EXTRA_POSITION =
            "dummies.conquian.position";

    private static final String ARG_NAME = "name";
    private static final String ARG_POSITION = "position";

    private EditText mEditText;

    public static EditNameFragment newInstance(String name, int position) {
        
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putInt(ARG_POSITION, position);

        EditNameFragment fragment = new EditNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String name = getArguments().getString(ARG_NAME);
        final int position = getArguments().getInt(ARG_POSITION);

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_name, null);

        mEditText = (EditText) view.findViewById(R.id.dialog_name_edit_text);
        mEditText.setText(name);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.dialog_name_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name = mEditText.getText().toString();
                                if (name.equals("")) {
                                    name = "Player #" + position;
                                }
                                sendResult(Activity.RESULT_OK, name, position);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, String name, int position) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_POSITION, position);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

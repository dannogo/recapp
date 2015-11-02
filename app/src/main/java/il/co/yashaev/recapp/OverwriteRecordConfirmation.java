package il.co.yashaev.recapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by oleh on 11/1/15.
 */
public class OverwriteRecordConfirmation extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String recordID = getArguments().getString("recordID");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Overwrite record?");
        builder.setMessage("The current related audio file will be overwritten");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ((MeetRecActivity)getActivity()).player.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), RecordingCoverActivity.class);
                intent.putExtra("recordID", recordID);
                ((MeetRecActivity) getActivity()).isNowRecording = true;
                getActivity().startActivityForResult(intent, 1);
            }
        });

        Dialog dialog = builder.create();
        return dialog;
    }
}

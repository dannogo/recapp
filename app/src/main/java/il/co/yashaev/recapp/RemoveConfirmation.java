package il.co.yashaev.recapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by oleh on 10/21/15.
 */
public class RemoveConfirmation extends DialogFragment {

    String purpose;
    int itemID;
    int position;
    String message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        purpose = getArguments().getString("purpose");
        itemID = getArguments().getInt("itemID");
        position = getArguments().getInt("position");

        switch (purpose){
            case "contacts":
                message = "This contact will be deleted with all it`s subordinate meetings and records permanently";
                break;
            case "meetings":
                message = "This meeting will be deleted with all it`s subordinate records permanently";
                break;
            case "records":
                message = "The record will be deleted permanently";
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete "+purpose.substring(0, purpose.length()-1)+"?");
        builder.setMessage(message);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (purpose.equals("contacts")){
                    ((ContactActivity)getActivity()).removeContact(itemID, position);
                }else if (purpose.equals("meetings")){
                    boolean isChecked = getArguments().getBoolean("isChecked");
                    ((MeetRecActivity)getActivity()).removeMeeting(itemID, position, isChecked);
                }else if (purpose.equals("records")){
                    ((MeetRecActivity)getActivity()).removeRecord(itemID, position);
                }

            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }
}

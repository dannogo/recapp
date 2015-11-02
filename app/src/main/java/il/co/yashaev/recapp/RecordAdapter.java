package il.co.yashaev.recapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by oleh on 10/5/15.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHloder> {

    private LayoutInflater inflater;
    ArrayList<String> titles;
    ArrayList<String> descriptions;
    protected ArrayList<Integer> ids;
    protected ArrayList<Integer> recordsWithAudio;
    Context context;

    public RecordAdapter(Context context, ArrayList<String> titles,
                         ArrayList<String> descriptions,  ArrayList<Integer> ids,
                         ArrayList<Integer> recordsWithAudio){
        inflater = LayoutInflater.from(context);
        this.titles = new ArrayList<>(titles);
        this.descriptions = new ArrayList<>(descriptions);
        this.ids = new ArrayList<>(ids);
        this.recordsWithAudio = new ArrayList<>(recordsWithAudio);
        this.context = context;

        if (this.titles.isEmpty() && this.descriptions.isEmpty()){
            this.titles.add(((MeetRecActivity)context).DUMMY_RECORD_TITLE + 1);
            this.descriptions.add(((MeetRecActivity)context).DUMMY_RECORD_DESCRIPTION + 1);
            int id = ((MeetRecActivity)this.context).databaseAdapter.insertDummyRecord(((MeetRecActivity)this.context).meetingID);
            this.ids.add(id);
        }
    }

    @Override
    public RecordViewHloder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.record_row, parent, false);
        RecordViewHloder holder = new RecordViewHloder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecordViewHloder  holder, final int position) {
        holder.icon.setImageResource(R.drawable.microphone);
        holder.title.setText(titles.get(position));
        holder.description.setText(descriptions.get(position));
        int databaseID  = ids.get(position);
        holder.databaseID.setText(String.valueOf(databaseID));

        if(recordsWithAudio.contains(databaseID)){
            holder.icon.setImageResource(R.drawable.play);
            holder.rewriteRecord.setVisibility(View.VISIBLE);
        }else{
            holder.icon.setImageResource(R.drawable.microphone);
            holder.rewriteRecord.setVisibility(View.GONE);
        }

        holder.editTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String editTextContent = holder.editTitle.getText().toString();
                    if (!editTextContent.isEmpty()) {
                        int currentID = Integer.parseInt(holder.databaseID.getText().toString());
                        int res = ((MeetRecActivity)context).databaseAdapter.updateRecord(editTextContent, null, null, currentID);
                        if (res == 1) {
                            holder.title.setText(editTextContent);
                            titles.set(position, editTextContent);
                        }
                    }
                    holder.editTitle.setVisibility(View.GONE);
                    holder.title.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        holder.editDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String editTextContent = holder.editDescription.getText().toString();
                    if (!editTextContent.isEmpty()) {
                        int currentID = Integer.parseInt(holder.databaseID.getText().toString());
                        int res = ((MeetRecActivity)context).databaseAdapter.updateRecord(null, editTextContent, null, currentID);
                        if (res == 1) {
                            holder.description.setText(editTextContent);
                            descriptions.set(position, editTextContent);
                        }
                    }
                    holder.editDescription.setVisibility(View.GONE);
                    holder.description.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class RecordViewHloder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener{

        ImageView icon;
        TextView title;
        EditText editTitle;
        TextView description;
        EditText editDescription;
        private TextView databaseID;
        private InputMethodManager imm;
        ImageView edit, rewriteRecord;



        public RecordViewHloder(View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.recordIcon);
            title = (TextView) itemView.findViewById(R.id.recordTitle);
            editTitle = (EditText) itemView.findViewById(R.id.editTitle);
            description = (TextView) itemView.findViewById(R.id.recordDescription);
            editDescription = (EditText) itemView.findViewById(R.id.editDescription);
            databaseID = (TextView) itemView.findViewById(R.id.databaseRecordID);
            edit = (ImageView) itemView.findViewById(R.id.editRecord);
            rewriteRecord = (ImageView) itemView.findViewById(R.id.rewriteRecord);

            imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);

            itemView.setOnClickListener(this);
            title.setOnLongClickListener(this);
            description.setOnLongClickListener(this);
            title.setOnClickListener(this);
            description.setOnClickListener(this);
            edit.setOnClickListener(this);
            rewriteRecord.setOnClickListener(this);

            editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (!editTitle.getText().toString().isEmpty()) {
                            title.setText(editTitle.getText());
                        }
                        editTitle.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                    }
                }
            });

            editDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        if (!editDescription.getText().toString().isEmpty()){
                            description.setText(editDescription.getText());
                        }
                        editDescription.setVisibility(View.GONE);
                        description.setVisibility(View.VISIBLE);
                    }
                }
            });


        }

        @Override
        public void onClick(View v) {
            if (v.getId() == edit.getId()){

                PopupMenu popup = new PopupMenu(context, edit);
                popup.getMenuInflater().inflate(R.menu.edit_item_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete){
                            RemoveConfirmation dialog = new RemoveConfirmation();
                            Bundle data = new Bundle();
                            data.putString("purpose", "records");
                            data.putInt("itemID", Integer.parseInt(databaseID.getText().toString()));
                            data.putInt("position", getPosition());
                            dialog.setArguments(data);
                            FragmentManager fragmentManager = ((MeetRecActivity)context).getFragmentManager();
                            dialog.show(fragmentManager, "Confirmation");
                        }
                        return true;
                    }
                });
                popup.show();

            }else if (v.getId() == rewriteRecord.getId()) {
                if (!((MeetRecActivity)context).isNowRecording) {
                    OverwriteRecordConfirmation dialog = new OverwriteRecordConfirmation();
                    Bundle data = new Bundle();
                    data.putString("recordID", databaseID.getText().toString());
                    dialog.setArguments(data);
                    FragmentManager fragmentManager = ((MeetRecActivity)context).getFragmentManager();
                    dialog.show(fragmentManager, "Overwrite confirmation");
                }else{
                    Toast.makeText(context, "First stop current recording", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (recordsWithAudio.contains(Integer.parseInt(databaseID.getText().toString()))){

                    int playerVisibility = ((MeetRecActivity)context).player.getVisibility();
                    TextView idOfRecordInPlayer = (TextView) ((MeetRecActivity) context).player.findViewById(R.id.idOfRecordInPlayer);
                    if (idOfRecordInPlayer.getText().toString().equals(databaseID.getText().toString())){
                        if (playerVisibility == View.GONE){
                            ((MeetRecActivity)context).player.setVisibility(View.VISIBLE);
                        }else{
                            ((MeetRecActivity)context).player.setVisibility(View.GONE);
                        }
                    }else{
                        idOfRecordInPlayer.setText(databaseID.getText());
                        ((MeetRecActivity)context).player.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (!((MeetRecActivity)context).isNowRecording) {
//                        ((MeetRecActivity)context).player.setVisibility(View.GONE);
                        Intent intent = new Intent(context, RecordingCoverActivity.class);
                        intent.putExtra("recordID", databaseID.getText().toString());
                        ((MeetRecActivity) context).isNowRecording = true;
                        ((MeetRecActivity) context).startActivityForResult(intent, 1);
                    }else{
                        Toast.makeText(context, "First stop current recording", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }


        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == title.getId()){
                title.setVisibility(View.GONE);
                editTitle.setVisibility(View.VISIBLE);

                editTitle.requestFocus();
                if(imm != null){
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }

                if (title.getText().toString().startsWith(((MeetRecActivity)context).DUMMY_RECORD_TITLE)){
                    editTitle.setText("");
                }else{
                    editTitle.setText(title.getText());
                }

                if (!editTitle.getText().toString().isEmpty()){
                    editTitle.setSelection(editTitle.getText().length());
                }
            }else if (v.getId() == description.getId()){
                description.setVisibility(View.GONE);
                editDescription.setVisibility(View.VISIBLE);

                editDescription.requestFocus();
                if(imm != null){
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }

                if (description.getText().toString().startsWith(((MeetRecActivity)context).DUMMY_RECORD_DESCRIPTION)){
                    editDescription.setText("");
                }else{
                    editDescription.setText(description.getText());
                }

                if (!editDescription.getText().toString().isEmpty()){
                    editDescription.setSelection(editDescription.getText().length());
                }

            }
            return true;
        }
    }
}

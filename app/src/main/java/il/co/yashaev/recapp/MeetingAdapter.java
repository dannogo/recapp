package il.co.yashaev.recapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by oleh on 10/5/15.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private LayoutInflater inflater;
    protected ArrayList<String> data = new ArrayList<>();
    protected ArrayList<Integer> ids = new ArrayList<>();
    private Context context;
    protected int checkedMeeting = -1;


    public MeetingAdapter(Context context, ArrayList<String> data, ArrayList<Integer> ids){
        inflater = LayoutInflater.from(context);

        this.data = new ArrayList<>(data);
        this.ids = new ArrayList<>(ids);
        this.context = context;
        if (this.data.isEmpty()){
            this.data.add(((MeetRecActivity) this.context).DUMMY_MEETING_TITLE + ((MeetRecActivity) this.context).meetingCnt++);
            int id = ((MeetRecActivity)this.context).databaseAdapter.insertDummyMeeting(((MeetRecActivity)this.context).contactID);
            this.ids.add(id);
        }

    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.meeting_row, parent, false);
        MeetingViewHolder holder = new MeetingViewHolder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MeetingViewHolder holder, final int position) {
        holder.icon.setImageResource(R.drawable.handshake);
        holder.title.setText(data.get(position));
        holder.databaseID.setText(String.valueOf(ids.get(position)));

        if (position == checkedMeeting){
            ((LinearLayout)holder.title.getParent()).setBackgroundColor(context.getResources().getColor(R.color.checkedItem));
        }else{
            ((LinearLayout)holder.title.getParent()).setBackgroundColor(context.getResources().getColor(R.color.uncheckedItem));
        }

        holder.editTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String editTextContent = holder.editTitle.getText().toString();
                    if (!editTextContent.isEmpty()) {
                        int currentID = Integer.parseInt(holder.databaseID.getText().toString());
                        int res = ((MeetRecActivity)context).databaseAdapter.updateMeeting(editTextContent, currentID);
                        if (res == 1) {
                            holder.title.setText(editTextContent);
                            data.set(position, editTextContent);
                        }
                    }
                    holder.editTitle.setVisibility(View.GONE);
                    holder.title.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener{

        private ImageView icon;
        private TextView title;
        private EditText editTitle;
        private TextView databaseID;
        private InputMethodManager imm;
        ImageView trash;

        public MeetingViewHolder(View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.meetingIcon);
            title = (TextView) itemView.findViewById(R.id.meetingTitle);
            editTitle = (EditText) itemView.findViewById(R.id.editTitle);
            databaseID = (TextView) itemView.findViewById(R.id.databaseMeetingID);
            trash = (ImageView) itemView.findViewById(R.id.deleteMeeting);

            imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);

            itemView.setOnClickListener(this);
            title.setOnLongClickListener(this);
            title.setOnClickListener(this);
            trash.setOnClickListener(this);

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
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == trash.getId()){
                RemoveConfirmation dialog = new RemoveConfirmation();
                Bundle data = new Bundle();
                data.putString("purpose", "meetings");
                data.putInt("itemID", Integer.parseInt(databaseID.getText().toString()));
                data.putInt("position", getPosition());
                dialog.setArguments(data);
                FragmentManager fragmentManager = ((MeetRecActivity)context).getFragmentManager();
                dialog.show(fragmentManager, "Confirmation");
            }else {
                checkedMeeting = getPosition();
                ((MeetRecActivity) context).uncheckSpareItems();
                itemView.setBackgroundColor(context.getResources().getColor(R.color.checkedItem));

                int meetID = Integer.parseInt(databaseID.getText().toString());
                ((MeetRecActivity) context).meetingID = meetID;
                ((MeetRecActivity) context).showRecords(meetID);
                StringBuffer stringBuffer = new StringBuffer(((MeetRecActivity) context).contactsName);
                stringBuffer.append(" / ");
                stringBuffer.append(title.getText());
                ((MeetRecActivity) context).getSupportActionBar().setTitle(stringBuffer.toString());
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == title.getId()) {
                title.setVisibility(View.GONE);
                editTitle.setVisibility(View.VISIBLE);

                editTitle.requestFocus();
                if(imm != null){
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }

                if (title.getText().toString().startsWith(((MeetRecActivity)context).DUMMY_MEETING_TITLE)){
                    editTitle.setText("");
                }else{
                    editTitle.setText(title.getText());
                }

                if (!editTitle.getText().toString().isEmpty()){
                    editTitle.setSelection(editTitle.getText().length());
                }
            }
            return true;
        }
    }
}

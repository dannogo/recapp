package il.co.yashaev.recapp;

import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by oleh on 10/5/15.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private LayoutInflater inflater;
    ArrayList<String> data = new ArrayList<>();
    Context context;


    public MeetingAdapter(Context context, ArrayList<String> data){
        inflater = LayoutInflater.from(context);

        this.data = new ArrayList<>(data);
        if (this.data.isEmpty()){
            this.data.add(((MeetRecActivity)context).DUMMY_MEETING_TITLE + ((MeetRecActivity)context).meetingCnt++);
        }
        this.context = context;

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

        holder.editTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String editTextContent = holder.editTitle.getText().toString();
                    if (!editTextContent.isEmpty()) {
                        holder.title.setText(editTextContent);
                        data.set(position, editTextContent);
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

        ImageView icon;
        TextView title;
        EditText editTitle;
        private InputMethodManager imm;

        public MeetingViewHolder(View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.meetingIcon);
            title = (TextView) itemView.findViewById(R.id.meetingTitle);
            editTitle = (EditText) itemView.findViewById(R.id.editTitle);

            imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);

            itemView.setOnClickListener(this);
            title.setOnLongClickListener(this);
            title.setOnClickListener(this);

            editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        if (!editTitle.getText().toString().isEmpty()){
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
            Log.w("LOG", "CLICK");
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

                if (!editTitle.getText().toString().isEmpty()){
                    editTitle.setSelection(editTitle.getText().length());
                }
            }
            return true;
        }
    }
}

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
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHloder> {

    private LayoutInflater inflater;
    ArrayList<String> titles;
    ArrayList<String> descriptions;
    Context context;

    public RecordAdapter(Context context, ArrayList<String> titles, ArrayList<String> descriptions){
        inflater = LayoutInflater.from(context);

        this.titles = new ArrayList<>(titles);
        this.descriptions = new ArrayList<>(descriptions);
        if (this.titles.isEmpty() && this.descriptions.isEmpty()){
            this.titles.add(((MeetRecActivity)context).DUMMY_RECORD_TITLE + ((MeetRecActivity)context).recordCnt);
            this.descriptions.add(((MeetRecActivity)context).DUMMY_RECORD_DESCRIPTION + ((MeetRecActivity)context).recordCnt++);
        }
        this.context = context;
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

        holder.editTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String editTextContent = holder.editTitle.getText().toString();
                    if (!editTextContent.isEmpty()) {
                        holder.title.setText(editTextContent);
                        titles.set(position, editTextContent);
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
                        holder.description.setText(editTextContent);
                        descriptions.set(position, editTextContent);
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
        private InputMethodManager imm;

        public RecordViewHloder(View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.recordIcon);
            title = (TextView) itemView.findViewById(R.id.recordTitle);
            editTitle = (EditText) itemView.findViewById(R.id.editTitle);
            description = (TextView) itemView.findViewById(R.id.recordDescription);
            editDescription = (EditText) itemView.findViewById(R.id.editDescription);

            imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);

            itemView.setOnClickListener(this);
            title.setOnLongClickListener(this);
            description.setOnLongClickListener(this);
            title.setOnClickListener(this);
            description.setOnClickListener(this);

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
            Log.w("LOG", "CLICK");
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

                if (!editDescription.getText().toString().isEmpty()){
                    editDescription.setSelection(editDescription.getText().length());
                }

            }
            return true;
        }
    }
}

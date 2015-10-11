package il.co.yashaev.recapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
 * Created by oleh on 10/9/15.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private LayoutInflater inflater;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    Context context;

    public ContactAdapter(Context context, ArrayList<String> names, ArrayList<String> descriptions){
        inflater = LayoutInflater.from(context);
        this.names = new ArrayList<>(names);
        this.descriptions = new ArrayList<>(descriptions);
        this.context = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.contact_row, parent, false);
        ContactViewHolder holder = new ContactViewHolder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.icon.setImageResource(R.drawable.contact);
        holder.name.setText(names.get(position));
        holder.description.setText(descriptions.get(position));


        holder.editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String editTextContent = holder.editName.getText().toString();
                    if (!editTextContent.isEmpty()) {
                        holder.name.setText(editTextContent);
                        names.set(position, editTextContent);
                    }
                    holder.editName.setVisibility(View.GONE);
                    holder.name.setVisibility(View.VISIBLE);
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
        return names.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private ImageView icon;
        private TextView name;
        private EditText editName;
        private TextView description;
        private EditText editDescription;
        private InputMethodManager imm;



        public ContactViewHolder(final View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.contactIcon);
            name = (TextView) itemView.findViewById(R.id.contactName);
            editName = (EditText) itemView.findViewById(R.id.editName);
            description = (TextView) itemView.findViewById(R.id.contactDescription);
            editDescription = (EditText) itemView.findViewById(R.id.editDescription);

            imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);

            itemView.setOnClickListener(this);
            name.setOnLongClickListener(this);
            description.setOnLongClickListener(this);
            name.setOnClickListener(this);
            description.setOnClickListener(this);


            editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (!editName.getText().toString().isEmpty()) {
                            name.setText(editName.getText());
                        }
                        editName.setVisibility(View.GONE);
                        name.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(context , MeetRecActivity.class);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == name.getId()) {
                name.setVisibility(View.GONE);
                editName.setVisibility(View.VISIBLE);

                editName.requestFocus();
                if(imm != null){
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                if (!editName.getText().toString().isEmpty()){
                    editName.setSelection(editName.getText().length());
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

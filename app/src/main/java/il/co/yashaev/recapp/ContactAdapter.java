package il.co.yashaev.recapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.icon.setImageResource(R.drawable.user);
        holder.name.setText(names.get(position));
        holder.description.setText(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView icon;
        TextView name;
        TextView description;

        public ContactViewHolder(View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.contactIcon);
            name = (TextView) itemView.findViewById(R.id.contactName);
            description = (TextView) itemView.findViewById(R.id.contactDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context , MeetRecActivity.class);
            context.startActivity(intent);
        }
    }
}

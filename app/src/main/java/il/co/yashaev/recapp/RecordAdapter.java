package il.co.yashaev.recapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oleh on 10/5/15.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHloder> {

    private LayoutInflater inflater;
    ArrayList<String> titles;
    ArrayList<String> descriptions;

    public RecordAdapter(Context context, ArrayList<String> titles, ArrayList<String> descriptions){
        inflater = LayoutInflater.from(context);

        this.titles = new ArrayList<>(titles);
        this.descriptions = new ArrayList<>(descriptions);
    }

    @Override
    public RecordViewHloder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.record_row, parent, false);
        RecordViewHloder holder = new RecordViewHloder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecordViewHloder  holder, int position) {
        holder.icon.setImageResource(R.drawable.cat);
        holder.title.setText(titles.get(position));
        holder.description.setText(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class RecordViewHloder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView title;
        TextView description;

        public RecordViewHloder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.recordIcon);
            title = (TextView) itemView.findViewById(R.id.recordTitle);
            description = (TextView) itemView.findViewById(R.id.recordDescription);
        }
    }
}

package il.co.yashaev.recapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;

/**
 * Created by oleh on 10/5/15.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private LayoutInflater inflater;
    ArrayList<String> data = new ArrayList<>();
    private ActionButton meetingFab;


    public MeetingAdapter(Context context, ArrayList<String> data){
        inflater = LayoutInflater.from(context);

        this.data = new ArrayList<>(data);

    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.meeting_row, parent, false);
        MeetingViewHolder holder = new MeetingViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        holder.icon.setImageResource(R.drawable.dog);
        holder.title.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView title;

        public MeetingViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.meetingIcon);
            title = (TextView) itemView.findViewById(R.id.meetingTitle);
        }
    }
}

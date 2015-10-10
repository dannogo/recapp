package il.co.yashaev.recapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;

/**
 * Created by oleh on 10/7/15.
 */
public class MeetRecActivity extends AppCompatActivity {

    private RecyclerView meetingList;
    private RecyclerView recordList;
    private MeetingAdapter meetingAdapter;
    private RecordAdapter recordAdapter;
    private ActionButton meetingFab;
    private ActionButton recordFab;

    private int meetingCnt = 1;
    private int recordCnt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_rec_activity);

        meetingList = (RecyclerView) findViewById(R.id.meetingList);
        recordList = (RecyclerView) findViewById(R.id.recordList);
        meetingFab = (ActionButton) findViewById(R.id.meetingFab);
        recordFab = (ActionButton) findViewById(R.id.recordFab);



        final ArrayList<String> data = new ArrayList<>();

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        meetingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetingAdapter.data.add("Meeting fab "+ meetingCnt++);
                meetingAdapter.notifyItemRangeChanged(0, meetingAdapter.getItemCount());
                meetingList.scrollToPosition(meetingAdapter.getItemCount()-1);
            }
        });

        recordFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAdapter.titles.add("Record title " + recordCnt);
                recordAdapter.descriptions.add("Description " + recordCnt);
                recordCnt++;
                recordAdapter.notifyItemRangeChanged(0, recordAdapter.getItemCount());
                recordList.scrollToPosition(recordAdapter.getItemCount()-1);
            }
        });

        meetingAdapter = new MeetingAdapter(MeetRecActivity.this, data);
        recordAdapter = new RecordAdapter(MeetRecActivity.this, titles, descriptions);

        meetingList.setAdapter(meetingAdapter);
        meetingList.setLayoutManager(new LinearLayoutManager(MeetRecActivity.this));
        recordList.setAdapter(recordAdapter);
        recordList.setLayoutManager(new LinearLayoutManager(MeetRecActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

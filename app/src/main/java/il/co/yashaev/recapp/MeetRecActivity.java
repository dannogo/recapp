package il.co.yashaev.recapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    protected RecordAdapter recordAdapter;
    private ActionButton meetingFab;
    private ActionButton recordFab;
    protected final String DUMMY_MEETING_TITLE = "Meeting ";
    protected final String DUMMY_RECORD_TITLE = "Record title ";
    protected final String DUMMY_RECORD_DESCRIPTION = "Record description ";
    DatabaseAdapter databaseAdapter;
    protected int contactID = -1;
    protected int meetingID = -1;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<Integer> recordIDs = new ArrayList<>();

    protected int meetingCnt = 1;
    protected int recordCnt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_rec_activity);

        meetingList = (RecyclerView) findViewById(R.id.meetingList);
        recordList = (RecyclerView) findViewById(R.id.recordList);
        meetingFab = (ActionButton) findViewById(R.id.meetingFab);
        recordFab = (ActionButton) findViewById(R.id.recordFab);

        databaseAdapter = new DatabaseAdapter(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            contactID = extras.getInt("contactID");
            if (contactID != -1) {

            }
        }

        final ArrayList<String> data = new ArrayList<>();
        final ArrayList<Integer> meetingIDs = new ArrayList<>();
        ArrayList<String[]> meetings = databaseAdapter.getMeetingsData(contactID);
        for (int i=0; i<meetings.size(); i++){
            meetingIDs.add(Integer.parseInt(meetings.get(i)[0]));
            if (meetings.get(i)[1] == null){
                data.add(DUMMY_MEETING_TITLE+meetingCnt);
            }else{
                data.add(meetings.get(i)[1]);
            }

            meetingCnt++;
        }

        meetingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = databaseAdapter.insertDummyMeeting(contactID);
                if (id >= 0) {
                    meetingAdapter.data.add(DUMMY_MEETING_TITLE + meetingCnt++);
                    meetingAdapter.ids.add(id);
                    meetingAdapter.notifyItemRangeChanged(0, meetingAdapter.getItemCount());
                    meetingList.scrollToPosition(meetingAdapter.getItemCount() - 1);
                }
            }
        });

        recordFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = databaseAdapter.insertDummyRecord(meetingID);
                if (id >= 0) {
                    recordAdapter.titles.add(DUMMY_RECORD_TITLE + recordCnt);
                    recordAdapter.descriptions.add(DUMMY_RECORD_DESCRIPTION + recordCnt);
                    recordAdapter.ids.add(id);
                    recordCnt++;
                    recordAdapter.notifyItemRangeChanged(0, recordAdapter.getItemCount());
                    recordList.scrollToPosition(recordAdapter.getItemCount() - 1);
                }else{
                    Toast.makeText(getBaseContext(), "Try again, please", Toast.LENGTH_SHORT).show();
                    Log.e("Database", "Insert to database was not successful");
                }
            }
        });

        meetingAdapter = new MeetingAdapter(MeetRecActivity.this, data, meetingIDs);
//        recordAdapter = new RecordAdapter(MeetRecActivity.this, titles, descriptions);

        meetingList.setAdapter(meetingAdapter);
        meetingList.setLayoutManager(new LinearLayoutManager(MeetRecActivity.this));
//        recordList.setAdapter(recordAdapter);
        recordList.setLayoutManager(new LinearLayoutManager(MeetRecActivity.this));
    }

    protected void uncheckSpareItems(){
        for (int i=0; i< meetingList.getChildCount(); i++){
            meetingList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.uncheckedItem));
        }
    }

    protected void showRecords(int meetingID){

        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        recordIDs = new ArrayList<>();

        ArrayList<String[]> records = databaseAdapter.getRecordsData(meetingID);

        for (int i=0; i<records.size(); i++){
            recordIDs.add(Integer.parseInt(records.get(i)[0]));
            if (records.get(i)[1] == null){
                titles.add(DUMMY_RECORD_TITLE+recordCnt);
            }else{
                titles.add(records.get(i)[1]);
            }

            if (records.get(i)[2] == null){
                descriptions.add(DUMMY_RECORD_DESCRIPTION+recordCnt);
            }else{
                descriptions.add(records.get(i)[2]);
            }
            recordCnt++;
        }

        recordAdapter = new RecordAdapter(MeetRecActivity.this, titles, descriptions, recordIDs);
        recordList.setAdapter(recordAdapter);
//        recordList.setLayoutManager(new LinearLayoutManager(MeetRecActivity.this));
        recordFab.setVisibility(View.VISIBLE);

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

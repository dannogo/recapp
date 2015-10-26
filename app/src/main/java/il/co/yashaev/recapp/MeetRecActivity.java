package il.co.yashaev.recapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private RelativeLayout recordContent;
    protected LinearLayout player;
    protected final String DUMMY_MEETING_TITLE = "Meeting title ";
    protected final String DUMMY_RECORD_TITLE = "Record title ";
    protected final String DUMMY_RECORD_DESCRIPTION = "Record description ";
    protected DatabaseAdapter databaseAdapter;
    protected int contactID = -1;
    protected int meetingID = -1;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<Integer> recordIDs = new ArrayList<>();

    protected int meetingCnt = 1;
    protected String contactsName = "Undefined";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_rec_activity);

        meetingList = (RecyclerView) findViewById(R.id.meetingList);
        recordList = (RecyclerView) findViewById(R.id.recordList);
        meetingList.addItemDecoration(new DividerItemDecoration(this, null, true, true));
        recordList.addItemDecoration(new DividerItemDecoration(this, null, true, true));

        player = (LinearLayout) findViewById(R.id.player);
        meetingFab = (ActionButton) findViewById(R.id.meetingFab);
        recordFab = (ActionButton) findViewById(R.id.recordFab);
        recordContent = (RelativeLayout) findViewById(R.id.recordContent);

        databaseAdapter = new DatabaseAdapter(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            contactID = extras.getInt("contactID");
            if (contactID != -1) {
                contactsName = databaseAdapter.getContactsName(contactID);
                if (contactsName == null){
                    contactsName = "Unnamed contact "+contactID;
                }

                getSupportActionBar().setTitle(contactsName);
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
                    meetingAdapter.notifyItemRangeInserted(meetingAdapter.getItemCount() - 1, meetingAdapter.getItemCount());
                    meetingList.scrollToPosition(meetingAdapter.getItemCount() - 1);
                }
            }
        });

        recordFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = databaseAdapter.insertDummyRecord(meetingID);
                if (id >= 0) {
                    int numberOfRecordsInCurrentMeeting = databaseAdapter.getRecordCount(meetingID);
                    recordAdapter.titles.add(DUMMY_RECORD_TITLE + numberOfRecordsInCurrentMeeting);
                    recordAdapter.descriptions.add(DUMMY_RECORD_DESCRIPTION + numberOfRecordsInCurrentMeeting);
                    recordAdapter.ids.add(id);
                    recordAdapter.notifyItemRangeInserted(recordAdapter.getItemCount()-1, recordAdapter.getItemCount());
                    recordList.scrollToPosition(recordAdapter.getItemCount() - 1);
                }else{
                    Toast.makeText(getBaseContext(), "Try again, please\nDatabase connection fail", Toast.LENGTH_SHORT).show();
                    Log.e("Database", "Insert to database was not successful");
                }
            }
        });

        meetingAdapter = new MeetingAdapter(MeetRecActivity.this, data, meetingIDs);

        meetingList.setAdapter(meetingAdapter);
        meetingList.setLayoutManager(new LinearLayoutManager(MeetRecActivity.this));
        recordList.setLayoutManager(new LinearLayoutManager(MeetRecActivity.this));
    }

    protected void uncheckSpareItems(){
        for (int i=0; i< meetingList.getChildCount(); i++){
            meetingList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.uncheckedItem));
        }
    }

    protected void removeMeeting(int itemID, int position, boolean isChecked){
        int countOfAffectedRows = databaseAdapter.deleteMeeting(itemID);
        if (countOfAffectedRows == 1){
            meetingAdapter.data.remove(position);
            meetingAdapter.ids.remove(position);
            meetingAdapter.notifyItemRemoved(position);
            meetingCnt--;
            if (isChecked) {
                recordContent.setVisibility(View.GONE);
                getSupportActionBar().setTitle(contactsName);
            }
        }
    }

    protected void removeRecord(int itemID, int position){
        int countOfAffectedRows = databaseAdapter.deleteRecord(itemID);
        if (countOfAffectedRows == 1){
            recordAdapter.titles.remove(position);
            recordAdapter.descriptions.remove(position);
            recordAdapter.ids.remove(position);
            recordAdapter.notifyItemRemoved(position);
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
                titles.add(DUMMY_RECORD_TITLE+(i+1));
            }else{
                titles.add(records.get(i)[1]);
            }

            if (records.get(i)[2] == null){
                descriptions.add(DUMMY_RECORD_DESCRIPTION+(i+1));
            }else{
                descriptions.add(records.get(i)[2]);
            }
        }

        recordAdapter = new RecordAdapter(MeetRecActivity.this, titles, descriptions, recordIDs);
        recordList.setAdapter(recordAdapter);
        recordContent.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

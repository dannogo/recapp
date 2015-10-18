package il.co.yashaev.recapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;

/**
 * Created by oleh on 10/9/15.
 */
public class ContactActivity extends AppCompatActivity {

    private RecyclerView contactList;
    private ContactAdapter contactAdapter;
    private ActionButton contactFab;
    protected final String DUMMY_NAME = "Name fab ";
    protected final String DUMMY_DESCRIPTION = "Description fab ";
    DatabaseAdapter databaseAdapter;
    private ImageView logo;
    private RelativeLayout contentLayout;
    private int contactCnt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);

        databaseAdapter = new DatabaseAdapter(this);

        contactList = (RecyclerView) findViewById(R.id.contactList);
        contactFab = (ActionButton) findViewById(R.id.contactFab);
        logo = (ImageView) findViewById(R.id.logo);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                logo.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }
        }, 1000);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        ArrayList<String[]> contacts = databaseAdapter.getContactsData();
        for (int i=0; i<contacts.size(); i++){
            ids.add(Integer.parseInt(contacts.get(i)[0]));
            if(contacts.get(i)[1] == null) {
                names.add(DUMMY_NAME+contactCnt);
            }else{
                names.add(contacts.get(i)[1]);
            }

            if(contacts.get(i)[2] == null) {
                descriptions.add(DUMMY_DESCRIPTION+contactCnt);
            }else{
                descriptions.add(contacts.get(i)[2]);
            }
            contactCnt++;
        }

        contactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = databaseAdapter.insertDummyContact();
                if (id >= 0) {
                    contactAdapter.names.add(DUMMY_NAME + contactCnt);
                    contactAdapter.descriptions.add(DUMMY_DESCRIPTION + contactCnt);
                    contactAdapter.ids.add(id);
                    contactCnt++;
                    contactAdapter.notifyItemRangeChanged(0, contactAdapter.getItemCount());
                    contactList.scrollToPosition(contactAdapter.getItemCount() - 1);
                }
            }
        });

        contactAdapter = new ContactAdapter(ContactActivity.this, names, descriptions, ids);

        contactList.setAdapter(contactAdapter);
        contactList.setLayoutManager(new LinearLayoutManager(ContactActivity.this));


    }

    @Override
    protected void onPause() {
        super.onPause();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

package il.co.yashaev.recapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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

    private int contactCnt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);

        contactList = (RecyclerView) findViewById(R.id.contactList);
        contactFab = (ActionButton) findViewById(R.id.contactFab);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        contactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactAdapter.names.add(DUMMY_NAME+contactCnt);
                contactAdapter.descriptions.add(DUMMY_DESCRIPTION+contactCnt);
                contactCnt++;
                contactAdapter.notifyItemRangeChanged(0, contactAdapter.getItemCount());
                contactList.scrollToPosition(contactAdapter.getItemCount()-1);
            }
        });

        contactAdapter = new ContactAdapter(ContactActivity.this, names, descriptions);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

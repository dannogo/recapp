package il.co.yashaev.recapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by oleh on 10/17/15.
 */
public class DatabaseAdapter {

    SQLHelper helper;
    public DatabaseAdapter(Context context){
        helper = new SQLHelper(context);
    }

    public ArrayList<String[]> getContactsData(){
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {SQLHelper.CONTACTS_ID, SQLHelper.CONTACTS_NAME, SQLHelper.CONTACTS_DESCRIPTION, SQLHelper.CONTACTS_ICON};
        Cursor cursor = db.query(SQLHelper.TABLE_NAME_CONTACTS, columns, null, null, null, null, null);

        ArrayList<String[]> result = new ArrayList<>();
        while(cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_ID);
            int nameIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_NAME);
            int descIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_DESCRIPTION);
            int iconIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_ICON);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String desc = cursor.getString(descIndex);
            String icon = cursor.getString(iconIndex);

            String[] row = {String.valueOf(id), name, desc, icon};
            result.add(row);

        }
        return result;
    }

    public ArrayList<String[]> getMeetingsData(int contactID){
        ArrayList<String[]> result = new ArrayList<>();
        if (contactID != -1) {
            SQLiteDatabase db = helper.getWritableDatabase();

            String[] columns = {SQLHelper.MEETINGS_ID, SQLHelper.MEETINGS_TITLE,
                    SQLHelper.MEETINGS_ICON, SQLHelper.MEETINGS_RELATED_CONTACT};
            String[] whereArgs = {String.valueOf(contactID)};

            Cursor cursor = db.query(SQLHelper.TABLE_NAME_MEETINGS, columns,
                    SQLHelper.MEETINGS_RELATED_CONTACT + " = ?", whereArgs, null, null, null);

            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(SQLHelper.MEETINGS_ID);
                int titleIndex = cursor.getColumnIndex(SQLHelper.MEETINGS_TITLE);
                int iconIndex = cursor.getColumnIndex(SQLHelper.MEETINGS_ICON);
                int relatedContactIndex = cursor.getColumnIndex(SQLHelper.MEETINGS_RELATED_CONTACT);

                int id = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String icon = cursor.getString(iconIndex);
                int relatedContact = cursor.getInt(relatedContactIndex);

                String[] row = {String.valueOf(id), title, icon, String.valueOf(relatedContact)};
                result.add(row);
            }
        }else{
            Log.e("DATABASE", "Unknown contact ID\nCheck method getMeetingsData in DatabaseAdapter class");
        }

        return result;
    }

    public ArrayList<String[]> getRecordsData(int meetingID){
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {SQLHelper.RECORDS_ID, SQLHelper.RECORDS_TITLE,
            SQLHelper.RECORDS_DESCRIPTION, SQLHelper.RECORDS_ICON, SQLHelper.RECORDS_RELATED_MEETING};
        String[] whereArgs = {String.valueOf(meetingID)};

        Cursor cursor = db.query(SQLHelper.TABLE_NAME_RECORDS, columns,
                SQLHelper.RECORDS_RELATED_MEETING + " =?", whereArgs, null, null, null);

        ArrayList<String[]> result = new ArrayList<>();
        while (cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex(SQLHelper.RECORDS_ID);
            int titleIndex = cursor.getColumnIndex(SQLHelper.RECORDS_TITLE);
            int descIndex = cursor.getColumnIndex(SQLHelper.RECORDS_DESCRIPTION);
            int iconIndex = cursor.getColumnIndex(SQLHelper.RECORDS_ICON);
            int relatedMeetingIndex = cursor.getColumnIndex(SQLHelper.RECORDS_RELATED_MEETING);

            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            String desc = cursor.getString(descIndex);
            String icon = cursor.getString(iconIndex);
            int relatedMeeting = cursor.getInt(relatedMeetingIndex);

            String[] row = {String.valueOf(id), title, desc, icon, String.valueOf(relatedMeeting)};
            result.add(row);
        }
        return result;
    }


    public int insertDummyContact(){
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(SQLHelper.TABLE_NAME_CONTACTS, SQLHelper.CONTACTS_NAME, null);

        return (int)id;
    }

    public int insertDummyMeeting(int contactID){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.MEETINGS_RELATED_CONTACT, contactID);
        long id = db.insert(SQLHelper.TABLE_NAME_MEETINGS, SQLHelper.MEETINGS_TITLE, contentValues);
        return (int)id;
    }

    public int insertDummyRecord(int meetingID){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.RECORDS_RELATED_MEETING, meetingID);
        long id = db.insert(SQLHelper.TABLE_NAME_RECORDS, SQLHelper.RECORDS_TITLE, contentValues);
        return (int)id;
    }

    public int getRecordCount(int meetingID){
        SQLiteDatabase db = helper.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + SQLHelper.TABLE_NAME_RECORDS + " WHERE "
                +SQLHelper.RECORDS_RELATED_MEETING+" ="+meetingID;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public int updateContact(String name, String desc, int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (name != null) {
            contentValues.put(SQLHelper.CONTACTS_NAME, name);
        }
        if (desc != null) {
            contentValues.put(SQLHelper.CONTACTS_DESCRIPTION, desc);
        }
        String[] whereArgs = {String.valueOf(id)};
        int count = db.update(SQLHelper.TABLE_NAME_CONTACTS, contentValues, SQLHelper.CONTACTS_ID + " =?", whereArgs);

        return count;
    }

    public int updateMeeting(String title, int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.MEETINGS_TITLE, title);
        String[] whereArgs = {String.valueOf(id)};
        int count = db.update(SQLHelper.TABLE_NAME_MEETINGS, contentValues, SQLHelper.MEETINGS_ID+" =?", whereArgs);

        return count;
    }

    public int updateRecord(String title, String desc, int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (title != null){
            contentValues.put(SQLHelper.RECORDS_TITLE, title);
        }
        if (desc != null){
            contentValues.put(SQLHelper.RECORDS_DESCRIPTION, desc);
        }
        String[] whereArgs = {String.valueOf(id)};
        int count = db.update(SQLHelper.TABLE_NAME_RECORDS, contentValues, SQLHelper.RECORDS_ID+" =?", whereArgs);

        return count;
    }



    static class SQLHelper extends SQLiteOpenHelper{

        private Context context;
        private static final String DATABASE_NAME = "recapp";
        private static final int DATABASE_VERSION = 3;

        // Table contacts
        private static final String TABLE_NAME_CONTACTS = "contacts";
        private static final String CONTACTS_ID = "_id";
        private static final String CONTACTS_NAME = "name";
        private static final String CONTACTS_DESCRIPTION = "description";
        private static final String CONTACTS_ICON = "icon";

        private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE "+TABLE_NAME_CONTACTS+"("+
                    CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CONTACTS_NAME + " VARCHAR(255), "
                    + CONTACTS_DESCRIPTION + " VARCHAR(255), "
                    + CONTACTS_ICON + " VARCHAR(255)" +
                ");";
        private static final String DROP_TABLE_CONTACTS = "DROP TABLE IF EXISTS "+TABLE_NAME_CONTACTS;

        // Table meetings
        private static final String TABLE_NAME_MEETINGS = "meetings";
        private static final String MEETINGS_ID = "_id";
        private static final String MEETINGS_TITLE = "title";
        private static final String MEETINGS_ICON = "icon";
        private static final String MEETINGS_RELATED_CONTACT = "related_contact";

        private static final String CREATE_TABLE_MEETINGS = "CREATE TABLE "+TABLE_NAME_MEETINGS+"("
                    + MEETINGS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MEETINGS_TITLE+" VARCHAR(255), "
                    + MEETINGS_ICON+" VARCHAR(255), "
                    + MEETINGS_RELATED_CONTACT+" INTEGER NOT NULL, "
                    + "FOREIGN KEY("+MEETINGS_RELATED_CONTACT+") REFERENCES "+TABLE_NAME_CONTACTS+"("+CONTACTS_ID+")"
                + ");";
        private static final String DROP_TABLE_MEETINGS = "DROP TABLE IF EXISTS "+TABLE_NAME_MEETINGS;

        // Table records
        private static final String TABLE_NAME_RECORDS = "records";
        private static final String RECORDS_ID = "_id";
        private static final String RECORDS_TITLE = "title";
        private static final String RECORDS_DESCRIPTION = "description";
        private static final String RECORDS_ICON = "icon";
        private static final String RECORDS_RELATED_MEETING = "related_meeting";

        private static final String CREATE_TABLE_RECORDS = "CREATE TABLE "+TABLE_NAME_RECORDS+"("
                    + RECORDS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RECORDS_TITLE+" VARCHAR(255), "
                    + RECORDS_DESCRIPTION+" VARCHAR(255), "
                    + RECORDS_ICON+" VARCHAR(255), "
                    + RECORDS_RELATED_MEETING+" INTEGER NOT NULL, "
                    + "FOREIGN KEY("+RECORDS_RELATED_MEETING+") REFERENCES "+TABLE_NAME_MEETINGS+"("+MEETINGS_ID+")"
                + ");";

        private static final String DROP_TABLE_RECORDS = "DROP TABLE IF EXISTS "+TABLE_NAME_RECORDS;

        public SQLHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            SQLiteDatabase db = this.getWritableDatabase();
            db.setForeignKeyConstraintsEnabled(true);
//            Toast.makeText(this.context, "Constructor called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_CONTACTS);
                db.execSQL(CREATE_TABLE_MEETINGS);
                db.execSQL(CREATE_TABLE_RECORDS);
//                Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE_CONTACTS);
                db.execSQL(DROP_TABLE_MEETINGS);
                db.execSQL(DROP_TABLE_RECORDS);
                onCreate(db);
//                Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

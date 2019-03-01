package bailiwick.bjpukh.com.vistarak.SqliteDatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bailiwick.bjpukh.com.vistarak.Modals.SavePointsBean;

/**
 * Created by vikasaggarwal on 05/09/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {


    public SQLiteHelper(Context context) {
        super(context, SqliteConfig.DATABASE_NAME, null, SqliteConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SqliteConfig.TABLE_NAME + " ( " + SqliteConfig.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SqliteConfig.COLUMN_LATITUDE + " VARCHAR, " + SqliteConfig.COLUMN_LONGITUDE + " VARCHAR, " + SqliteConfig.COLUMN_DATE_TIME + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SqliteConfig.TABLE_NAME);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    protected String getCurrentTime() {
        Calendar calander = Calendar.getInstance();
        calander.getTime();
        int year = calander.get(Calendar.YEAR);
        int month = calander.get(Calendar.MONTH);
        int day = calander.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calander.getTime());
        ;
        return day + "/" + (month + 1) + "/" + year + "  " + time;
    }

    // Adding new contact
    void addLatLong(SavePointsBean latlong) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteConfig.COLUMN_LATITUDE, latlong.getLat()); // Contact Name
        values.put(SqliteConfig.COLUMN_LONGITUDE, latlong.getLongitude()); // Contact Phone
        values.put(SqliteConfig.COLUMN_DATE_TIME, getCurrentTime()); // Contact Phone

        // Inserting Row
        db.insert(SqliteConfig.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    SavePointsBean getSingleRow(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SqliteConfig.TABLE_NAME, new String[]{SqliteConfig.COLUMN_ID,
                        SqliteConfig.COLUMN_LATITUDE, SqliteConfig.COLUMN_LONGITUDE, SqliteConfig.COLUMN_DATE_TIME}, SqliteConfig.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SavePointsBean getlatlng = new SavePointsBean(cursor.getString(0),
                "", cursor.getString(1), cursor.getString(2), cursor.getString(3), "");
        // return contact
        return getlatlng;
    }

    // Getting All Contacts
    public List<SavePointsBean> getAllLatLong() {
        List<SavePointsBean> contactList = new ArrayList<SavePointsBean>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SqliteConfig.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SavePointsBean contact = new SavePointsBean();
                contact.setId(cursor.getString(0));
                contact.setLat(cursor.getString(1));
                contact.setLongitude(cursor.getString(2));
                contact.setLessTime(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
   /* public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
*/
    // Deleting single contact
    public void deleteLatLong(SavePointsBean contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SqliteConfig.TABLE_NAME, SqliteConfig.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }


    // Getting contacts Count
    public int getTotalLatLongCount() {
        String countQuery = "SELECT  * FROM " + SqliteConfig.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}

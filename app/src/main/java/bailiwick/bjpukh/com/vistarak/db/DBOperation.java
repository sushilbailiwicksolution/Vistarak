package bailiwick.bjpukh.com.vistarak.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import bailiwick.bjpukh.com.vistarak.Getter_Setter.AnusuchitModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Army_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Bike_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothMeeting_modal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothSpinnerModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.District_model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Event_Pramukh_model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Home_visitModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Influence_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Level_model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Man_Ki_BaatModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Ngo_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.PannaPramukhModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Religious_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Shaheed_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.SmartPhoneModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Special_AreaModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.SwachtaModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Whats_AppModal;
import bailiwick.bjpukh.com.vistarak.Modals.KametiModel;

public class DBOperation {
    final static String PREFS_NAME = "MyPrefsFile";

    public static String[] getAllBooth(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        String data[];// = new String[3];
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Booth_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            data = new String[len];
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                data[i] = cur.getString(cur.getColumnIndex(Database_Utils.Booth_name));

                Log.e("booth_name", cur.getString(cur.getColumnIndex(Database_Utils.Booth_name)));
                Log.e("booth_ID", cur.getString(cur.getColumnIndex(Database_Utils.Booth_BID)));

                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<BoothSpinnerModal> getAllBoothList(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<BoothSpinnerModal> boothSpinnerModals;
        boothSpinnerModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Booth_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                boothSpinnerModals.add(new BoothSpinnerModal(cur.getString(cur.getColumnIndex(Database_Utils.Booth_BID)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_name)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_consistency))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.Booth_name)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return boothSpinnerModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Insert Booth in table
    public static boolean insertBooth(Context c, String bid, String booth_name, String consistency_name) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Booth_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("bid", bid);
            Log.e("booth_name", booth_name);
            Log.e("consistency_name", consistency_name);

            newValues.put(Database_Utils.Booth_BID, bid);
            newValues.put(Database_Utils.Booth_name, booth_name);
            newValues.put(Database_Utils.Booth_consistency, consistency_name);

            // Insert the row into your table
            database.insert(Database_Utils.Booth_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // Delete booth All
    public static boolean deleteAll_Booth(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            String sql = "delete from " + Database_Utils.Booth_table;
            database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Insert_Man Ki Baat

    public static boolean insertMan_ki_baat(Context c, String Vistarak_id, String booth_id, String man_pramaukh, String lattitudeLocation, String longitudeLocation, String man_mobile) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Man_Ki_Baat_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("pramukhName", man_pramaukh);

            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.PramukhName, man_pramaukh);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.PramukhMobile, man_mobile);
            newValues.put(Database_Utils.Entry_no, man_pramaukh + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Man_Ki_Baat_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Man Ki Baat
    public static ArrayList<Man_Ki_BaatModal> getMan_ki_baat(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Man_Ki_BaatModal> ManKiBaatModals;
        ManKiBaatModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Man_Ki_Baat_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                ManKiBaatModals.add(new Man_Ki_BaatModal(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return ManKiBaatModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get Man Ki Baat List
    public static boolean deleteMan_ki_baat(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Man_Ki_Baat_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // delete All Man Ki baat
// Delete booth All
    public static boolean deleteAll_man_ki_baat(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            String sql = "delete from " + Database_Utils.Man_Ki_Baat_table;
            database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Insert_ Swachta in Booth
    public static boolean insertSwachta_in_booth(Context c, String Vistarak_id, String booth_id, String man_pramaukh, String lattitudeLocation, String longitudeLocation, String man_mobile) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Swachta_in_Booth_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("pramukhName", man_pramaukh);

            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.PramukhName, man_pramaukh);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.PramukhMobile, man_mobile);
            newValues.put(Database_Utils.Entry_no, man_pramaukh + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Swachta_in_Booth_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Swachta
    public static ArrayList<SwachtaModel> getSwachta(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<SwachtaModel> Swachta_Modals;
        Swachta_Modals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Swachta_in_Booth_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                Swachta_Modals.add(new SwachtaModel(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)),

                        cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return Swachta_Modals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete Swachta
    public static boolean deleteSwachta(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Swachta_in_Booth_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    ///////////////////////////////////////
    // Insert_ panna Pramukh in Booth
    public static boolean insertPannaPramukh_in_booth(Context c, String Vistarak_id, String booth_id, String man_pramaukh, String address, String lattitudeLocation, String longitudeLocation, String man_mobile) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Panna_Pramukh_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("pramukhName", man_pramaukh);

            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.PramukhName, man_pramaukh);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.PramukhMobile, man_mobile);
            newValues.put(Database_Utils.MEM_address, address);

            newValues.put(Database_Utils.Entry_no, man_pramaukh + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Panna_Pramukh_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Panna Pramukh
    public static ArrayList<PannaPramukhModel> getPannaPramukh(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<PannaPramukhModel> pannapramukh_Modals;
        pannapramukh_Modals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Panna_Pramukh_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                pannapramukh_Modals.add(new PannaPramukhModel(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.MEM_address)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)),

                        cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return pannapramukh_Modals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete pramukh
    public static boolean deletePannaPramukh(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Panna_Pramukh_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

// 14 09 2018 Prince

    ///////////////////////////////////////
    // Insert_ panna Pramukh in Booth
    public static boolean insertAnusuchit_in_booth(Context c, String Vistarak_id, String booth_id, String man_pramaukh, String address, String lattitudeLocation, String longitudeLocation, String man_mobile, String catagory) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Add_Anusuchit_member_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("pramukhName", man_pramaukh);

            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.PramukhMobile, man_mobile);
            newValues.put(Database_Utils.PramukhName, man_pramaukh);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.Catagory, catagory);

            //  newValues.put(Database_Utils.MEM_address, address);

            newValues.put(Database_Utils.Entry_no, man_pramaukh + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Add_Anusuchit_member_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Anusuchit
    public static ArrayList<AnusuchitModel> getAnusuchit(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<AnusuchitModel> Anusuchit_Modals;
        Anusuchit_Modals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Add_Anusuchit_member_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                Anusuchit_Modals.add(new AnusuchitModel(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)), cur.getString(cur.getColumnIndex(Database_Utils.Catagory)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return Anusuchit_Modals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete pramukh
    public static boolean deleteAnusuchit(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Add_Anusuchit_member_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    // Insert_ Whats App group
    public static boolean insertWhats_app(Context c, String Vistarak_id, String booth_id, String man_pramaukh, String lattitudeLocation, String longitudeLocation, String man_mobile, String Group_name) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Whats_app_group_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("pramukhName", man_pramaukh);

            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.PramukhName, man_pramaukh);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.PramukhMobile, man_mobile);
            newValues.put(Database_Utils.Group_name, Group_name);
            newValues.put(Database_Utils.Entry_no, man_pramaukh + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Whats_app_group_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    // get Whats App
    public static ArrayList<Whats_AppModal> getWhatsApp(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Whats_AppModal> Whats_AppModals;
        Whats_AppModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Whats_app_group_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                Whats_AppModals.add(new Whats_AppModal(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)), cur.getString(cur.getColumnIndex(Database_Utils.Group_name)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return Whats_AppModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete Whats App group
    public static boolean deleteWhatsApp(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Whats_app_group_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // Insert New Member
    public static boolean insert_new_Member(Context c, String user_name, String booth_id, String full_name, String radioText, String dob, String mobile, String occupation, String address, String religion_, String voter_cast, String member_type, String lattitudeLocation, String longitudeLocation, String voter_id) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.New_Member_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", user_name);
            Log.e("booth_id", booth_id);
            Log.e("pramukhName", full_name);

            newValues.put(Database_Utils.Vistarak_id, user_name);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.MEM_full_name, full_name);
            newValues.put(Database_Utils.MEM_marital_status, radioText);
            newValues.put(Database_Utils.MEM_dob, dob);
            newValues.put(Database_Utils.MEM_mobile, mobile);
            newValues.put(Database_Utils.MEM_occupation, occupation);
            newValues.put(Database_Utils.MEM_address, address);
            newValues.put(Database_Utils.MEM_religion, religion_);
            newValues.put(Database_Utils.MEM_caste, voter_cast);
            newValues.put(Database_Utils.MEM_member_type, member_type);
            newValues.put(Database_Utils.MEM_voterid, voter_id);
            newValues.put(Database_Utils.Entry_no, full_name + (len + 1));

            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);

            // Insert the row into your table
            database.insert(Database_Utils.New_Member_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // insert Home Visit
    public static boolean insertHomevisit(Context c, String user_name, String booth_id, String lattitudeLocation, String longitudeLocation, String family_head, String family_member, String address_member, String membership_number, String voter_id, String visit_type, String membership_number1, String mobile) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Home_Visit_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", user_name);
            Log.e("booth_id", booth_id);
            Log.e("pramukhName", family_head);


            newValues.put(Database_Utils.Vistarak_id, user_name);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.Home_head_name, family_head);
            newValues.put(Database_Utils.Home_family_member, family_member);
            newValues.put(Database_Utils.Home_address, address_member);
            newValues.put(Database_Utils.Home_total_voter, membership_number);
            newValues.put(Database_Utils.Home_head_voter_id, "NA");
            newValues.put(Database_Utils.Home_visitType, visit_type);
            newValues.put(Database_Utils.Home_membership_id, membership_number1);
            newValues.put(Database_Utils.Home_mobile, mobile);

            newValues.put(Database_Utils.Entry_no, family_head + (len + 1));


            // Insert the row into your table
            database.insert(Database_Utils.Home_Visit_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Home Visit Baat
    public static ArrayList<Home_visitModal> getHome_Visit(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Home_visitModal> HomeVisitModals;
        HomeVisitModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Home_Visit_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {

                HomeVisitModals.add(new Home_visitModal(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.Home_head_name)), cur.getString(cur.getColumnIndex(Database_Utils.Home_family_member)), cur.getString(cur.getColumnIndex(Database_Utils.Home_address)), cur.getString(cur.getColumnIndex(Database_Utils.Home_total_voter)), cur.getString(cur.getColumnIndex(Database_Utils.Home_head_voter_id)), cur.getString(cur.getColumnIndex(Database_Utils.Home_visitType)), cur.getString(cur.getColumnIndex(Database_Utils.Home_membership_id)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no)), cur.getString(cur.getColumnIndex(Database_Utils.Home_mobile))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.Home_visitType)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return HomeVisitModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete Home Visit
    public static boolean deleteHome_visit(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Home_Visit_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // Insert Special Area

    public static boolean insertSpecial_area(Context c, String Vistarak_id, String booth_id, String area_name, String lattitudeLocation, String longitudeLocation, String family_count, String type) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Special_area_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("area name", area_name);

            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.Prior_Category, area_name);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.family_count, family_count);
            newValues.put(Database_Utils.Area_type, type);

            newValues.put(Database_Utils.Entry_no, area_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Special_area_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Special Area
    public static ArrayList<Special_AreaModal> getSpecial_Area(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Special_AreaModal> SpecialAreaModals;
        SpecialAreaModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Special_area_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                SpecialAreaModals.add(new Special_AreaModal(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Prior_Category)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.family_count)), cur.getString(cur.getColumnIndex(Database_Utils.Area_type)),

                        cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.Prior_Category)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return SpecialAreaModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete Special Area
    public static boolean deleteSpecial_Area(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Special_area_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    // Insert NGO
    public static boolean insertNGO(Context c, String Vistarak_id, String booth_id, String lattitudeLocation, String longitudeLocation, String key_voter, String ngo_owner_name, String ngo_name, String ngo_work, String ngo_affective, String ngo_mobile) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.key_voter_NGO_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("area name", ngo_owner_name);
            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.VoterType, key_voter);
            newValues.put(Database_Utils.NG0_owner_name, ngo_owner_name);
            newValues.put(Database_Utils.NG0_name, ngo_name);
            newValues.put(Database_Utils.NG0_area_of_work, ngo_work);
            newValues.put(Database_Utils.NG0_affectivity, ngo_affective);
            newValues.put(Database_Utils.NG0_owner_mobile, ngo_mobile);
            newValues.put(Database_Utils.Entry_no, ngo_owner_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.key_voter_NGO_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get NGO
    public static ArrayList<Ngo_Model> getNGO(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Ngo_Model> NGOModals;
        NGOModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.key_voter_NGO_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                NGOModals.add(new Ngo_Model(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.VoterType)), cur.getString(cur.getColumnIndex(Database_Utils.NG0_owner_name)), cur.getString(cur.getColumnIndex(Database_Utils.NG0_name)), cur.getString(cur.getColumnIndex(Database_Utils.NG0_area_of_work)), cur.getString(cur.getColumnIndex(Database_Utils.NG0_affectivity)), cur.getString(cur.getColumnIndex(Database_Utils.NG0_owner_mobile)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.NG0_name)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return NGOModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Insert Religious
    public static boolean insertReligious(Context c, String Vistarak_id, String booth_id, String lattitudeLocation, String longitudeLocation, String key_voter, String ngo_owner_name, String ngo_name, String ngo_work, String ngo_affective, String ngo_mobile) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.key_voter_Religious_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("area name", ngo_owner_name);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.VoterType, key_voter);
            newValues.put(Database_Utils.Religious_owner_name, ngo_owner_name);
            newValues.put(Database_Utils.Religious_name, ngo_name);
            newValues.put(Database_Utils.Religious_area_of_work, ngo_work);
            newValues.put(Database_Utils.Religious_affectivity, ngo_affective);
            newValues.put(Database_Utils.Religious_owner_mobile, ngo_mobile);
            newValues.put(Database_Utils.Entry_no, ngo_owner_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.key_voter_Religious_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Religious
    public static ArrayList<Religious_Model> getRelious(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Religious_Model> ReligiousModals;
        ReligiousModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.key_voter_Religious_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                ReligiousModals.add(new Religious_Model(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.VoterType)), cur.getString(cur.getColumnIndex(Database_Utils.Religious_owner_name)), cur.getString(cur.getColumnIndex(Database_Utils.Religious_name)), cur.getString(cur.getColumnIndex(Database_Utils.Religious_area_of_work)), cur.getString(cur.getColumnIndex(Database_Utils.Religious_affectivity)), cur.getString(cur.getColumnIndex(Database_Utils.Religious_owner_mobile)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.Religious_owner_name)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return ReligiousModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Insert Army
    public static boolean insertArmy(Context c, String Vistarak_id, String booth_id, String lattitudeLocation, String longitudeLocation, String key_voter, String ngo_owner_name, String ngo_work, String ngo_affective, String ngo_mobile) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.key_voter_EX_Army_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("area name", ngo_owner_name);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.VoterType, key_voter);
            newValues.put(Database_Utils.Army_person, ngo_owner_name);
            newValues.put(Database_Utils.Army_total_family_member, ngo_affective);
            newValues.put(Database_Utils.Army_retired_from, ngo_work);
            newValues.put(Database_Utils.Army_mobile, ngo_mobile);
            newValues.put(Database_Utils.Entry_no, ngo_owner_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.key_voter_EX_Army_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Army
    public static ArrayList<Army_Model> getArmy(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Army_Model> ArmyModals;
        ArmyModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.key_voter_EX_Army_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                ArmyModals.add(new Army_Model(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.VoterType)), cur.getString(cur.getColumnIndex(Database_Utils.Army_person)), cur.getString(cur.getColumnIndex(Database_Utils.Army_total_family_member)), cur.getString(cur.getColumnIndex(Database_Utils.Army_retired_from)), cur.getString(cur.getColumnIndex(Database_Utils.Army_mobile)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.Army_person)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return ArmyModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Insert Shaheed
    public static boolean insertShaheed(Context c, String Vistarak_id, String booth_id, String lattitudeLocation, String longitudeLocation, String key_voter, String ngo_owner_name, String family_member, String shaeedDiwas, String ngo_mobile, String ngo_work) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.key_voter_Shaheed__table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("area name", ngo_owner_name);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.VoterType, key_voter);
            newValues.put(Database_Utils.Shaheed_name, ngo_owner_name);
            newValues.put(Database_Utils.Shaheed_family_member, family_member);
            newValues.put(Database_Utils.Shaheed_retired_from, shaeedDiwas);
            newValues.put(Database_Utils.Shaheed_mobile, ngo_mobile);
            newValues.put(Database_Utils.Shaheed_work_for, ngo_work);
            newValues.put(Database_Utils.Entry_no, ngo_owner_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.key_voter_Shaheed__table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Shaheed
    public static ArrayList<Shaheed_Model> getShaheed(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Shaheed_Model> ShaheedModals;
        ShaheedModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.key_voter_Shaheed__table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                ShaheedModals.add(new Shaheed_Model(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.VoterType)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.Shaheed_name)), cur.getString(cur.getColumnIndex(Database_Utils.Shaheed_family_member)), cur.getString(cur.getColumnIndex(Database_Utils.Shaheed_retired_from)), cur.getString(cur.getColumnIndex(Database_Utils.Shaheed_mobile)), cur.getString(cur.getColumnIndex(Database_Utils.Shaheed_work_for)),

                        cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.Army_person)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return ShaheedModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // insert Influence
    public static boolean insertInfluence(Context c, String Vistarak_id, String booth_id, String lattitudeLocation, String longitudeLocation, String key_voter, String ngo_name, String ngo_work, String ngo_affective, String ngo_mobile) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.key_voter_other_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", booth_id);
            Log.e("area name", ngo_name);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, booth_id);
            newValues.put(Database_Utils.VoterType, key_voter);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.person_name, ngo_name);
            newValues.put(Database_Utils.Other_occupation, ngo_work);
            newValues.put(Database_Utils.Other_Affectivity, ngo_affective);
            newValues.put(Database_Utils.Other_Mobile, ngo_mobile);
            newValues.put(Database_Utils.Entry_no, ngo_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.key_voter_other_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Other
    public static ArrayList<Influence_Model> getOther(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Influence_Model> InfluenceModals;
        InfluenceModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.key_voter_other_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                InfluenceModals.add(new Influence_Model(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.VoterType)), cur.getString(cur.getColumnIndex(Database_Utils.person_name)), cur.getString(cur.getColumnIndex(Database_Utils.Other_occupation)), cur.getString(cur.getColumnIndex(Database_Utils.Other_Affectivity)), cur.getString(cur.getColumnIndex(Database_Utils.Other_Mobile)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.person_name)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return InfluenceModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete Key Voter
    public static boolean deleteNGO(Context c, String ENTRY_no, String table_name) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + table_name + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

// Insert Bike

    public static boolean insertBike(Context c, String Vistarak_id, String boothID, String person_name, String lattitudeLocation, String longitudeLocation, String vechicle_no, String contact) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Add_Bike_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", boothID);
            Log.e("area name", person_name);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, boothID);
            newValues.put(Database_Utils.PramukhName, person_name);
            newValues.put(Database_Utils.PramukhMobile, contact);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.vechicle_no, vechicle_no);
            newValues.put(Database_Utils.Entry_no, person_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Add_Bike_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get bike
    public static ArrayList<Bike_Model> getBike(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Bike_Model> BikeModals;
        BikeModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Add_Bike_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                BikeModals.add(new Bike_Model(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.vechicle_no)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return BikeModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Insert Smart Phone
    public static boolean insertSmartPhone(Context c, String Vistarak_id, String boothID, String person_name, String lattitudeLocation, String longitudeLocation, String contact) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Add_Smart_phone_user_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", boothID);
            Log.e("area name", person_name);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, boothID);
            newValues.put(Database_Utils.PramukhName, person_name);
            newValues.put(Database_Utils.PramukhMobile, contact);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.Entry_no, person_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Add_Smart_phone_user_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Smart Phone
    public static ArrayList<SmartPhoneModel> getSmartPhone(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<SmartPhoneModel> SmartPhoneModals;
        SmartPhoneModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Add_Smart_phone_user_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                SmartPhoneModals.add(new SmartPhoneModel(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return SmartPhoneModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete Smart Phone
    public static boolean deleteSmart_Phone(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Add_Smart_phone_user_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    //
    // Insert Event Pramukh
    public static boolean insertEventPramukh_in_booth(Context c, String Vistarak_id, String boothID, String person_name, String lattitudeLocation, String longitudeLocation, String contact) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Add_Event_pramukh_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", boothID);
            Log.e("area name", person_name);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, boothID);
            newValues.put(Database_Utils.PramukhName, person_name);
            newValues.put(Database_Utils.PramukhMobile, contact);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.Entry_no, person_name + (len + 1));

            // Insert the row into your table
            database.insert(Database_Utils.Add_Event_pramukh_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    // get Event Pramukh
    public static ArrayList<Event_Pramukh_model> getEventPramukh(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Event_Pramukh_model> event_model;
        event_model = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Add_Event_pramukh_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                event_model.add(new Event_Pramukh_model(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)), cur.getString(cur.getColumnIndex(Database_Utils.PramukhMobile)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return event_model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete Event
    public static boolean deleteEventPramukh(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Add_Event_pramukh_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    // Insert Kameti
    public static boolean insertKametiMember(Context c, String Vistarak_id, String boothID, String lattitudeLocation, String longitudeLocation, String str_adykash_name, String str_adykash_mobile, String str_palak_name, String str_palak_mobile, String str_bla_name, String str_bla_mobile, String valuetype) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Add_kameti_member_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", boothID);
            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, boothID);
            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);

            newValues.put(Database_Utils.kameti_Adyaksh_name, str_adykash_name);
            newValues.put(Database_Utils.kameti_Adyaksh_mobile, str_adykash_mobile);
            newValues.put(Database_Utils.kameti_palk_name, str_palak_name);
            newValues.put(Database_Utils.kameti_palak_mobile, str_palak_mobile);
            newValues.put(Database_Utils.kameti_BLA_name, str_bla_name);
            newValues.put(Database_Utils.kameti_BLA_mobile, str_bla_mobile);
            newValues.put(Database_Utils.kameti_Value_type, valuetype);
            newValues.put(Database_Utils.Entry_no, str_adykash_name + (len + 1));


            // Insert the row into your table
            database.insert(Database_Utils.Add_kameti_member_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    // get Kameti Member
    public static ArrayList<KametiModel> getKametiMember(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<KametiModel> kametiList;
        kametiList = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Add_kameti_member_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                kametiList.add(new KametiModel(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.kameti_Adyaksh_name)), cur.getString(cur.getColumnIndex(Database_Utils.kameti_Adyaksh_mobile)), cur.getString(cur.getColumnIndex(Database_Utils.kameti_palk_name)), cur.getString(cur.getColumnIndex(Database_Utils.kameti_palak_mobile)), cur.getString(cur.getColumnIndex(Database_Utils.kameti_BLA_name)), cur.getString(cur.getColumnIndex(Database_Utils.kameti_BLA_mobile)), cur.getString(cur.getColumnIndex(Database_Utils.kameti_Value_type)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no))

                ));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.PramukhName)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return kametiList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Delete kameti Member
    public static boolean deleteKameti_member(Context c, String ENTRY_no) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            //   String sql = "DELETE from " + Database_Utils.Man_Ki_Baat_table + " where" + Database_Utils.Entry_no + "='" + ENTRY_no + "' ";
            database.execSQL("delete from " + Database_Utils.Add_kameti_member_table + " where Entry_no='" + ENTRY_no + "'");
            ///   database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    public static boolean insertBoothMeeting(Context c, String Vistarak_id, String boothID, String male_count, String lattitudeLocation, String longitudeLocation, String female_count, String total, byte[] imgBytes, String image_name) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.booth_meeting_table, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("vistarak_id", Vistarak_id);
            Log.e("booth_id", boothID);
            Log.e("area name", male_count);


            newValues.put(Database_Utils.Vistarak_id, Vistarak_id);
            newValues.put(Database_Utils.Booth_id, boothID);

            newValues.put(Database_Utils.Latitude, lattitudeLocation);
            newValues.put(Database_Utils.Logitude, longitudeLocation);
            newValues.put(Database_Utils.Home_male_member, male_count);
            newValues.put(Database_Utils.Home_female_member, female_count);
            newValues.put(Database_Utils.Home_total_member, total);
            newValues.put(Database_Utils.Home_meeting_id, "");
            newValues.put(Database_Utils.Entry_no, image_name + (len + 1));
            newValues.put(Database_Utils.Home_image_name, image_name);
            newValues.put(Database_Utils.Home_image_path, imgBytes);

            // Insert the row into your table
            database.insert(Database_Utils.booth_meeting_table, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    // get Booth Meeting
    public static ArrayList<BoothMeeting_modal> getmeeting(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<BoothMeeting_modal> MeetingModals;
        MeetingModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.booth_meeting_table, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {


                MeetingModals.add(new BoothMeeting_modal(cur.getString(cur.getColumnIndex(Database_Utils.Vistarak_id)), cur.getString(cur.getColumnIndex(Database_Utils.Booth_id)), cur.getString(cur.getColumnIndex(Database_Utils.Latitude)), cur.getString(cur.getColumnIndex(Database_Utils.Logitude)), cur.getString(cur.getColumnIndex(Database_Utils.Home_male_member)), cur.getString(cur.getColumnIndex(Database_Utils.Home_female_member)), cur.getString(cur.getColumnIndex(Database_Utils.Home_total_member)), cur.getString(cur.getColumnIndex(Database_Utils.Entry_no)), cur.getString(cur.getColumnIndex(Database_Utils.Home_image_name)), cur.getBlob(cur.getColumnIndex(Database_Utils.Home_image_path))));

                Log.e("booth detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.Home_male_member)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return MeetingModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////// /*helping APi*/////////////////////
    ///////////////////// /*helping APi*/////////////////////
    // mine
    public static String[] getAlluserquote(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        String data[];// = new String[3];
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                cur = database.rawQuery("select Quotes from LoveQuotes where isFav = 1", null);
            }
            int len = cur.getCount();
            System.out.println("lenght  " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            data = new String[len];
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                data[i] = cur.getString(cur.getColumnIndex("Quotes"));
                cur.moveToNext();

                //	System.out.println("Data is our" +data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getAllUserQuote(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        String data[];// = new String[3];
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                cur = database.rawQuery("select Quotes from LoveQuotes where isFav=1", null);
            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            data = new String[len];
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                data[i] = cur.getString(cur.getColumnIndex("Quotes"));
                cur.moveToNext();
            }
            cur.close();
            DatabaseHelper.closedatabase();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String[]> getAllQuotes(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<String[]> arList = new ArrayList<String[]>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                cur = database.rawQuery("select * from LoveQuotes", null);
            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {

                String data[] = new String[3];
                data[0] = cur.getString(0);
                data[1] = cur.getString(1);
                data[2] = cur.getInt(2) + "";
                arList.add(data);
                cur.moveToNext();
            }
            cur.close();
            DatabaseHelper.closedatabase();
            return arList;
        } catch (Exception e) {
            e.printStackTrace();
            return arList;
        }
    }

    public static String[] getQuotes(Context c, String id) {
        String data[] = new String[3];
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                cur = database.rawQuery("select * from LoveQuotes where ID='" + id + "'", null);
            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {

            } else {
                cur.close();
                DatabaseHelper.closedatabase();
                return null;
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {

                data[0] = cur.getString(0);
                data[1] = cur.getString(1);
                data[2] = cur.getInt(2) + "";
                // cur.moveToNext();
            }
            cur.close();
            database.close();
            DatabaseHelper.closedatabase();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public static String getQuote(Context c, String id) {
        String data = "";
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                cur = database.rawQuery("select Quotes from LoveQuotes where ID='" + id + "'", null);
            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {

            } else {
                cur.close();
                DatabaseHelper.closedatabase();
                return null;
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                data = cur.getString(cur.getColumnIndex("Quotes"));
                // cur.moveToNext();
            }
            cur.close();
            database.close();
            DatabaseHelper.closedatabase();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public static ArrayList<String[]> getAllFavQuotes(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<String[]> arList = new ArrayList<String[]>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                cur = database.rawQuery("select * from LoveQuotes where isFav=1", null);
            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {

            } else {
                cur.close();
                DatabaseHelper.closedatabase();
                return arList;
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {

                String data[] = new String[3];
                data[0] = cur.getString(0);
                data[1] = cur.getString(1);
                data[2] = cur.getInt(2) + "";
                arList.add(data);
                System.out.println(data[0]);
                cur.moveToNext();
            }
            cur.close();
            database.close();
            DatabaseHelper.closedatabase();
            return arList;
        } catch (Exception e) {
            e.printStackTrace();
            return arList;
        }
    }

    public static boolean deleteFavourite(Context c, String id) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            String sql = "update LoveQuotes set isFav=0 where ID='" + id + "' ";
            database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean insertEntry(Context c, String quote, Integer fav) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select Quotes from LoveQuotes", null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            newValues.put("ID", len + 1);
            newValues.put("Quotes", quote);
            newValues.put("isFav", fav);

            // Insert the row into your table
            database.insert("LoveQuotes", null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }
///////////////DISTRICT DATA/////////////////////
    // insert District
    public static boolean insertDistrict(Context c, String district_id, String district_name) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Table_district, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("id", district_id);
            Log.e("district_name", district_name);

            newValues.put(Database_Utils.district_id, district_id);
            newValues.put(Database_Utils.district_name, district_name);

            // Insert the row into your table
            database.insert(Database_Utils.Table_district, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }
// Get District
    public static ArrayList<District_model> getAllDistrict(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<District_model> boothSpinnerModals;
        boothSpinnerModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Table_district, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                boothSpinnerModals.add(new District_model(cur.getString(cur.getColumnIndex(Database_Utils.district_id)),
                        cur.getString(cur.getColumnIndex(Database_Utils.district_name))));
                Log.e("District detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.district_name)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return boothSpinnerModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Delete District
    public static boolean deleteAll_District(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            String sql = "delete from " + Database_Utils.Table_district;
            database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    ///////////////Level DATA/////////////////////
    // insert level
    public static boolean insertlevel(Context c, String level_id, String level_name) {

        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);

        Cursor cur = null;

        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            cur = database.rawQuery("select * from " + Database_Utils.Table_Level, null);


            int len = cur.getCount();
            System.out.println("lenght " + len);

            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            Log.e("id", level_id);
            Log.e("level_name", level_name);

            newValues.put(Database_Utils.level_id, level_id);
            newValues.put(Database_Utils.level_name, level_name);

            // Insert the row into your table
            database.insert(Database_Utils.Table_Level, null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();


            //	database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }


    }
    // Get level
    public static ArrayList<Level_model> getAlllevel(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        Cursor cur = null;
        ArrayList<Level_model> boothSpinnerModals;
        boothSpinnerModals = new ArrayList<>();
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();
            {
                //cur = database.rawQuery("select Quotes from LoveQuotes where isFav =1", null);
                cur = database.rawQuery("select * from " + Database_Utils.Table_Level, null);

            }
            int len = cur.getCount();
            System.out.println("lenght " + len);
            if (len > 0) {
                SharedPreferences pageNumPref = c.getSharedPreferences(PREFS_NAME, 0);
                pageNumPref.edit().putInt("dbsize", len).commit();
            } else {
                cur.close();
                DatabaseHelper.closedatabase();
            }
            cur.moveToNext();
            for (int i = 0; i < len; i++) {
                boothSpinnerModals.add(new Level_model(cur.getString(cur.getColumnIndex(Database_Utils.level_id)),
                        cur.getString(cur.getColumnIndex(Database_Utils.level_name))));
                Log.e("Level detail List", "" + cur.getString(cur.getColumnIndex(Database_Utils.level_name)));
                cur.moveToNext();

                //	System.out.println(data[i]);

            }
            cur.close();
            DatabaseHelper.closedatabase();
            return boothSpinnerModals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Delete Level
    public static boolean deleteAll_level(Context c) {
        DatabaseHelper dbhelperShopCart = new DatabaseHelper(c);
        try {
            dbhelperShopCart.createDataBase();
            SQLiteDatabase database = DatabaseHelper.openDataBase();

            String sql = "delete from " + Database_Utils.Table_Level;
            database.execSQL(sql);
            database.close();
            DatabaseHelper.closedatabase();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

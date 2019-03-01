package bailiwick.bjpukh.com.vistarak.Support;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import bailiwick.bjpukh.com.vistarak.R;
import bailiwick.bjpukh.com.vistarak.app.AppController;


public class SavedData {
    private static final String USER_NAME = "USER_NAME";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String ADDRESS = "ADDRESS";
    private static final String isLogin = "isLogin";
    private static final String PROFILE_PIC = "PROFILE_PIC";
    private static final String MOBILE1 = "MOBILE1";
    private static final String MOBILE2 = "MOBILE2";
    private static final String BOOTH_ID = "BOOTH_ID";

    private static final String LOCATION_SERVICE_START_KEY = "LOCATION_SERVICE_START_KEY";
    private static final String LOCATION_DATA_KEY = "LOCATION_DATA_KEY";
    private static final String LOCATION_mLcation = "LOCATION_mLcation";
    private static final String LOCATION_LAST_UPLOAD_TIME = "LOCATION_LAST_UPLOAD_TIME";
    private static final String LOCATION_Latitude = "LOCATION_Latitude";
    private static final String LOCATION_Longitude = "LOCATION_Longitude";
    private static final String SAVE_TRACKING_ID = "SAVE_TRACKING_ID";
    private static final String SAVE_Leave_Status = "SAVE_Leave_Status";

    private static final String isoperation_start = "isoperation_start";
    private static final String isNotify = "isNotify";
    // Language Control
    private static final String selectedLanguage = "selectedLang";
    private static final String LanguageArrayID = "LanguageArrayID";
// databaseVersion
private static final String DATABASE_VERSION = "DB_version";
    private static final String isDB_updated = "isUpdated";

    static SharedPreferences prefs;

    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance());
        }
        return prefs;
    }

    // get Databse Version
    public static boolean isDBupdated() {
        return getInstance().getBoolean(isDB_updated, false);
    }

    public static void setisDbUpated(Boolean status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(isDB_updated, status);
        editor.apply();
    }
    // End here.....

    // get Databse Version
    public static String getDBversion() {
        return getInstance().getString(DATABASE_VERSION, "null");
    }

    public static void setDBversion(String status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(DATABASE_VERSION, status);
        editor.apply();
    }
    // End here.....

    public static int getSelectedLang() {
        return getInstance().getInt(selectedLanguage, 0);
    }

    public static void setSelectedLang(int status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putInt(selectedLanguage, status);
        editor.apply();
    }

    public static int getLangArray() {
        return getInstance().getInt(LanguageArrayID, R.array.English);
    }

    public static void setLangArray(int status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putInt(LanguageArrayID, status);
        editor.apply();
    }

    public static boolean getNotify() {
        return getInstance().getBoolean(isNotify, false);
    }

    public static void saveNotify(boolean status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(isNotify, status);
        editor.apply();
    }

    public static boolean getOperation_status() {
        return getInstance().getBoolean(isoperation_start, false);
    }

    public static void saveOperation_status(boolean status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(isoperation_start, status);
        editor.apply();
    }


    public static void ClearAll() {
        PreferenceManager.getDefaultSharedPreferences(AppController.getInstance()).
                edit().clear().apply();
    }

    public static boolean getLeaveStatus() {
        return getInstance().getBoolean(SAVE_Leave_Status, false);
    }

    public static void saveLeaveStatus(boolean status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(SAVE_Leave_Status, status);
        editor.apply();
    }

    public static void saveTrackingId(String id) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SAVE_TRACKING_ID, id);
        editor.apply();
    }

    public static String getTrackingId() {
        return getInstance().getString(SAVE_TRACKING_ID, "0");
    }

    public static String getLattitudeLocation() {
        return getInstance().getString(LOCATION_Latitude, "0.0");
    }

    public static void saveLattitudeLocation(String mLat) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LOCATION_Latitude, mLat);
        editor.apply();
    }

    public static String getLongitudeLocation() {
        return getInstance().getString(LOCATION_Longitude, "0.0");
    }

    public static void saveLongitudeLocation(String mLon) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LOCATION_Longitude, mLon);
        editor.apply();
    }


    public static void saveLastLocationUploadTime(String mLocation) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LOCATION_LAST_UPLOAD_TIME, mLocation);
        editor.apply();
    }

    public static String getLastLocationUploadTime() {
        return getInstance().getString(LOCATION_LAST_UPLOAD_TIME, "Not Found");
    }

    public static void saveMLocation(String mLocation) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LOCATION_mLcation, mLocation);
        editor.apply();
    }

    public static String getMLocation() {
        return getInstance().getString(LOCATION_mLcation, "Not Found");
    }

    public static String getLastLocationMessage() {
        return getInstance().getString(LOCATION_DATA_KEY, "No Record Found");
    }

    public static void saveLastLocationMessage(String locData) {
        SharedPreferences.Editor editor = getInstance().edit();

        editor.putString(LOCATION_DATA_KEY, locData);

        editor.apply();
    }

    public static boolean isLocationServiceStarted() {
        return getInstance().getBoolean(LOCATION_SERVICE_START_KEY, false);
    }

    public static void saveLocationService(boolean status) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(LOCATION_SERVICE_START_KEY, status);
        editor.apply();
    }


    public static String getBOOTH_ID() {
        return getInstance().getString(BOOTH_ID, "");
    }

    public static void saveBOOTH_ID(String sBOOTH_ID) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(BOOTH_ID, sBOOTH_ID);
        editor.apply();
    }


    public static String getMOBILE2() {
        return getInstance().getString(MOBILE2, "");
    }

    public static void saveMOBILE2(String sMOBILE2) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(MOBILE2, sMOBILE2);
        editor.apply();
    }


    public static String getMOBILE1() {
        return getInstance().getString(MOBILE1, "");
    }

    public static void saveMOBILE1(String sMOBILE1) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(MOBILE1, sMOBILE1);
        editor.apply();
    }


    public static String getPROFILE_PIC() {
        return getInstance().getString(PROFILE_PIC, "");
    }

    public static void savePROFILE_PIC(String sPROFILE_PIC) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(PROFILE_PIC, sPROFILE_PIC);
        editor.apply();
    }


    public static boolean getLogin() {
        return getInstance().getBoolean(isLogin, false);
    }

    public static void saveLogin(boolean Login) {
        Log.e("In Saved data","i m heere");
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(isLogin, Login);
        editor.apply();
    }


    public static String getADDRESS() {
        return getInstance().getString(ADDRESS, "");
    }

    public static void saveADDRESS(String sADDRESS) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(ADDRESS, sADDRESS);
        editor.apply();
    }


    public static String getUSER_NAME() {
        return getInstance().getString(USER_NAME, "");
    }

    public static void saveUSER_NAME(String sUSER_NAME) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(USER_NAME, sUSER_NAME);
        editor.apply();
    }

    public static String getFULL_NAME() {
        return getInstance().getString(FULL_NAME, "");
    }

    public static void saveFULL_NAME(String sFULL_NAME) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(FULL_NAME, sFULL_NAME);
        editor.apply();
    }
}

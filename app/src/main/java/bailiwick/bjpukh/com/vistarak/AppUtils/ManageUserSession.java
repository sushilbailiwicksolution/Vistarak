package bailiwick.bjpukh.com.vistarak.AppUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Deswal on 18-09-2017.
 */

public class ManageUserSession {
    private String Name;
    private String Email;
    private String User_ID;

    String User_Token;
    String User_Total_Earning;

    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // Shared preferences file name
    private static final String PREF_NAME = "morepay";
    // Shared Pref data
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_USER_ACTIVE = "isUserActive";

    public ManageUserSession(Context context) {
        this.context = context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    //	***********************************Set Login Status********************************************************

    public void setUserStatus(boolean isUserActive) {
        editor.putBoolean(KEY_IS_USER_ACTIVE, isUserActive);
        // commit changes
        editor.commit();

    }
    public boolean isDeviceLogin() {

        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setDeviceLoginStatus(boolean isServiceActive) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isServiceActive);
        // commit changes
        editor.commit();

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Token() {
        return User_Token;
    }

    public void setUser_Token(String user_Token) {
        User_Token = user_Token;
    }

    public String getUser_Total_Earning() {
        return User_Total_Earning;
    }

    public void setUser_Total_Earning(String user_Total_Earning) {
        User_Total_Earning = user_Total_Earning;
    }

    public void ClearLoginData(){
        setDeviceLoginStatus(false);
        setUserStatus(false);
        //setTempEmail("");
       // setTempName("");
        //setTempPassword("");
    }


}

package bailiwick.bjpukh.com.vistarak.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import bailiwick.bjpukh.com.vistarak.R;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;

public class CustomDialogOTPresend extends Dialog {
    public Button btn_apply, btn_cancel;
    public Activity mContext;
    public TextView txt_message, popup_txt_title;
    View v = null;
    TextView mTittle = null;
    SharedPreferences pref;

    public CustomDialogOTPresend(Activity con) {
        super(con, R.style.Theme_Orange);
        this.mContext = con;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cst_dilouge_mobile_verify);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        // String fontPath = "fonts/A_Lolita_Scorned.ttf";
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        popup_txt_title = (TextView) findViewById(R.id.popup_txt_title);
        btn_apply = (Button) findViewById(R.id.btn_Apply);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_message.setText(SavedData.getFULL_NAME() + " You request leave for today \n do you want to cancel it ?");


    }


}
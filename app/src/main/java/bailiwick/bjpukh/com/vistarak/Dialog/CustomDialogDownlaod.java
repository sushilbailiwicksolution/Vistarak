package bailiwick.bjpukh.com.vistarak.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bailiwick.bjpukh.com.vistarak.R;

public class CustomDialogDownlaod extends Dialog {
    public Button btn_apply, btn_cancel;
    public Activity mContext;
    public TextView txt_message;
    public EditText edt_number;
    View v = null;
    TextView mTittle = null;
    SharedPreferences pref;

    public CustomDialogDownlaod(Activity con) {
        super(con, R.style.Theme_Orange);
        this.mContext = con;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cst_dilouge_download);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        // String fontPath = "fonts/A_Lolita_Scorned.ttf";
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        edt_number = (EditText) findViewById(R.id.edt_number);
        btn_apply = (Button) findViewById(R.id.btn_Apply);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        txt_message = (TextView) findViewById(R.id.txt_message);

        edt_number.setHintTextColor(mContext.getResources().getColor(R.color.color_black));

    }


}
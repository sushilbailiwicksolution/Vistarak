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

/**
 * Created by Prince on 09-09-2018.
 */

public class CustomComplaintDialog extends Dialog {
    public Button btn_apply, btn_cancel;
    public Activity mContext;
    public TextView txt_From, txt_To;
    public EditText edt_reason;
    View v = null;
    TextView mTittle = null;
    SharedPreferences pref;

    public CustomComplaintDialog(Activity con) {
        super(con, R.style.Theme_Orange);
        this.mContext = con;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cst_dilouge_leave);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        // String fontPath = "fonts/A_Lolita_Scorned.ttf";
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        edt_reason = (EditText) findViewById(R.id.edt_reason);
        btn_apply = (Button) findViewById(R.id.btn_Apply);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        txt_From = (TextView) findViewById(R.id.txt_From);
        txt_To = (TextView) findViewById(R.id.txt_To);
        edt_reason.setHintTextColor(mContext.getResources().getColor(R.color.color_black));

    }

}
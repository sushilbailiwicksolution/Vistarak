package bailiwick.bjpukh.com.vistarak.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import bailiwick.bjpukh.com.vistarak.LoginActivity;
import bailiwick.bjpukh.com.vistarak.R;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

public class CustomDialogExit extends Dialog implements OnClickListener {
    Button btn_Logout, btn_Exit;
    Activity mContext;
    TextView mMessage = null;
    View v = null;
    TextView mTittle = null;
    SharedPreferences pref;

    public CustomDialogExit(Activity con) {
        super(con, R.style.Theme_Orange);
        this.mContext = con;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cst_dialouge_open_screen);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        // String fontPath = "fonts/A_Lolita_Scorned.ttf";
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        mMessage = (TextView) findViewById(R.id.txt_mesage);
      //  mMessage.setText(SavedData.getFULL_NAME() + "  " + "Do You Want to Logout");
        btn_Exit = (Button) findViewById(R.id.btn_exit);
        btn_Logout = (Button) findViewById(R.id.btn_logout);
        btn_Exit.setOnClickListener(this);
        btn_Logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                dismiss();
                System.exit(0);
                mContext.finish();
                break;

            case R.id.btn_logout:
                boolean isSucess = DBOperation.deleteAll_Booth(mContext);
                Log.e("isSucess", "Deleted : " + isSucess);

                dismiss();
                Intent i = new Intent(mContext, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(i);
                mContext.finish();
                SavedData.ClearAll();
                break;
            default:
                break;
        }

    }

}
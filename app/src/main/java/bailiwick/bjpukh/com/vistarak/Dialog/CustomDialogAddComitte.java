package bailiwick.bjpukh.com.vistarak.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import bailiwick.bjpukh.com.vistarak.R;

public class CustomDialogAddComitte extends Dialog {
    public Button btn_apply, btn_cancel;
    public Activity mContext;
    public TextView txt_From, txt_To;
    View v = null;
    TextView mTittle = null;
    SharedPreferences pref;
    public  Spinner spinner_designation;
    public EditText edt_adyaksh_name, edt_adyaksh_mobile;

    ArrayList<String> spinner_designation_array;
    ArrayAdapter<String> spinner_designation_adaptor;

    public CustomDialogAddComitte(Activity con) {
        super(con);
        this.mContext = con;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cst_dilouge_add_comitee);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        // String fontPath = "fonts/A_Lolita_Scorned.ttf";
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        btn_apply = (Button) findViewById(R.id.btn_Apply);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        txt_From = (TextView) findViewById(R.id.txt_From);
        txt_To = (TextView) findViewById(R.id.txt_To);

        edt_adyaksh_mobile = (EditText) findViewById(R.id.edt_adyaksh_mobile);
        edt_adyaksh_name = (EditText) findViewById(R.id.edt_adyaksh_name);

        spinner_designation = (Spinner) findViewById(R.id.spinner_designation);

        set_DesignationAdapter();

    }

    private void set_DesignationAdapter() {

        spinner_designation_array = new ArrayList<>();
        spinner_designation_array.add("Select Designation");

        spinner_designation_array.add("Adhyaksh");
        spinner_designation_array.add("Upadhyaksh");
        spinner_designation_array.add("Maha-Mantri");
        spinner_designation_array.add("Mantri");
        spinner_designation_array.add("Sadasya");
        spinner_designation_array.add("Sadasya (Mahila)");



        spinner_designation_adaptor = new ArrayAdapter<String>(mContext, R.layout.spinner_layout_white_text, spinner_designation_array);
        spinner_designation_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_designation.setAdapter(spinner_designation_adaptor);
    }


}
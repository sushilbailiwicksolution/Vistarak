package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothSpinnerModal;
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.Support.CheckNetworkState;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 03-09-2017.
 */

public class Add_man_ki_baat extends RootActivity {
    EditText edt_man_mobile, edt_man_pramukh;
    Button button_submit, button_add_member;
    ProgressDialog prg;

    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;

    TextView mManKiBat;
    TextInputLayout mMankiBatPramukh, mNumber;

    TextView txt_member_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_ki_baat);
        createIDs();
        ClickEvent();
        getBoothFromDB();
        Spinne_Click();
    }

    private void Spinne_Click() {
        spinner_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals.get(position - 1).getBid());
                    get_count(boothSpinnerModals.get(position - 1).getBid());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void get_count(final String bid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                try {
                    Log.e("Response : prince ", response);
                    //  Manage_data_login(response);
                    JSONObject jsdata = new JSONObject(response);
                    String msg = jsdata.getString("msg");
                    String status = jsdata.getString("status");
                    SavedData.saveOperation_status(false);

                    if (status.equalsIgnoreCase("1")) {
                        if (jsdata.has("manKiBaat_count")) {

                            txt_member_count.setText("Register Member " + jsdata.getString("manKiBaat_count"));
                        }
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_manKiBaat_count);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", bid);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }

    private void getBoothFromDB() {
        boothSpinnerModals = DBOperation.getAllBoothList(this);

        Log.e("in Database Calling", "in Database Calling");
        spinner_booth_array.add("Select Booth");

        for (int i = 0; i < boothSpinnerModals.size(); i++) {
//            spinner_booth_array.add((i + 1) + ". " + boothSpinnerModals.get(i).getBooth_name());
            spinner_booth_array.add("Booth No . " + boothSpinnerModals.get(i).getBooth_name());

        }
        spinner_booth_adaptor.notifyDataSetChanged();

    }

    private void ClickEvent() {
        button_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Add_man_ki_baat.this, Add_new_Member.class);
                startActivity(i);

                //  String ManKi_baat[] = DBOperation.getMan_ki_baat(Add_man_ki_baat.this);
                //   Log.e("check", "Prince :" + ManKi_baat.length);

                //      boolean isSucess = DBOperation.deleteMan_ki_baat(Add_man_ki_baat.this, "1");
                //    String ManKi_baat[] = DBOperation.getMan_ki_baat(Add_man_ki_baat.this);
                //  Log.e("check", "Prince :" + ManKi_baat.length);

            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        edt_man_mobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    checkValidation();

                    return true;
                }
                return false;
            }
        });
    }

    private void checkValidation() {
        String man_pramaukh, man_mobile;
        man_pramaukh = edt_man_pramukh.getText().toString().trim();
        man_mobile = edt_man_mobile.getText().toString().trim();
        if (spinner_booth.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select Booth", Toast.LENGTH_LONG).show();
            return;
        } else if (man_pramaukh.equalsIgnoreCase("")) {
            edt_man_pramukh.setError(Html.fromHtml("<font color='orange'>Please Enter Pramukh Name</font>"));
            return;
        } else if (man_mobile.equalsIgnoreCase("")) {
            edt_man_mobile.setError(Html.fromHtml("<font color='orange'>Please Enter Mobile No.</font>"));
            return;
        } else if (man_mobile.length() != 10) {
            edt_man_mobile.setError(Html.fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
            return;
        } else {
            if (CheckNetworkState.isNetworkAvailable(Add_man_ki_baat.this)) {
                SaveManPramukh(man_pramaukh, man_mobile, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

            } else {
                DatabASE_SaveManPramukh(man_pramaukh, man_mobile, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

            }
        }
    }

    private void DatabASE_SaveManPramukh(String man_pramaukh, String man_mobile, String booth_id, String user_name) {
        Log.e("in Data Base ", "In Data BAse");

        try {
            boolean isSuccess = DBOperation.insertMan_ki_baat(Add_man_ki_baat.this, SavedData.getUSER_NAME(), booth_id, man_pramaukh, SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), man_mobile);

            if (isSuccess) {
                Toast.makeText(Add_man_ki_baat.this, "Saved in Database", Toast.LENGTH_SHORT).show();
                Clear_Detail();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void SaveManPramukh(final String man_pramaukh, final String man_mobile, final String booth_id, String user_name) {

        prg.setMessage(SelectedLanguage[TextUtils.please_wait]);
        prg.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                try {
                    prg.dismiss();
                    Log.e("Response : prince ", response);
                    //  Manage_data_login(response);
                    JSONObject jsdata = new JSONObject(response);
                    String msg = jsdata.getString("msg");
                    String status = jsdata.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Clear_Detail();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
                Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
                Log.e("Error :", error.toString());
                DatabASE_SaveManPramukh(man_pramaukh, man_mobile, SavedData.getBOOTH_ID(), SavedData.getUSER_NAME());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddMankiBaat);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("pramukhName", man_pramaukh);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());

                params.put("pramukhMobile", man_mobile);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void Clear_Detail() {
        edt_man_pramukh.setText("");
        edt_man_mobile.setText("");
        edt_man_pramukh.requestFocus();
    }

    private void createIDs() {
        prg = new ProgressDialog(this);
        txt_member_count = (TextView) findViewById(R.id.txt_member_count);
        edt_man_mobile = (EditText) findViewById(R.id.edt_man_mobile);
        edt_man_pramukh = (EditText) findViewById(R.id.edt_man_pramukh);
        button_submit = (Button) findViewById(R.id.button_submit);
        button_add_member = (Button) findViewById(R.id.button_add_member);
        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(Add_man_ki_baat.this, R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);
        setLanguage();

    }

    private void setLanguage() {
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());

        mManKiBat = (TextView) findViewById(R.id.txt_man_ki_bat);
        mMankiBatPramukh = (TextInputLayout) findViewById(R.id.til_man_ki_bat_pramukh);
        mNumber = (TextInputLayout) findViewById(R.id.til_number);

        mManKiBat.setText(SelectedLanguage[TextUtils.man_ki_baat]);
        mMankiBatPramukh.setHint(SelectedLanguage[TextUtils.man_ki_bat_pramukh]);
        mNumber.setHint(SelectedLanguage[TextUtils.mobile]);

        button_submit.setText(SelectedLanguage[TextUtils.submit]);
        button_add_member.setText(SelectedLanguage[TextUtils.add_member]);


    }
}

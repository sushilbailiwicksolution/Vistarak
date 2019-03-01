package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import bailiwick.bjpukh.com.vistarak.Support.CheckNetworkState;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

/**
 * Created by Prince on 13-07-2018.
 */

public class AnusuchitReg extends RootActivity {

    private Spinner spinner_booth;
    private EditText edt_anusuchit_number, edt_anuschit_pramukh;

    private Button button_submit;

    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;
    private ProgressDialog prg;
    //private TextView txt_total_anusuchit_pramukh;
    private Context mContext;

    RadioGroup rd_grp_anusuchit;
    RadioButton rdb_sc, rdb_st, rdb_obc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_anusuchit);
        setContentView(R.layout.add_anusuchit);
        mContext = AnusuchitReg.this;

        creadteIDS();
        getBoothFromDB();
        ClickEvent();
        Spinne_Click();
        visiblity_radio(View.GONE);
    }

    private void Spinne_Click() {
        spinner_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals.get(position - 1).getBid());
                    //  txt_total_anusuchit_pramukh.setVisibility(View.VISIBLE);
                    if (CheckNetworkState.isNetworkAvailable(mContext)) {
                        getAnusuchit_count(boothSpinnerModals.get(position - 1).getBid());

                    } else {
                        Toast.makeText(getApplicationContext(), "Offline Mode", Toast.LENGTH_LONG).show();

                        visiblity_radio(View.VISIBLE);

                    }
                } else {
                    //txt_total_anusuchit_pramukh.setVisibility(View.GONE);
                    visiblity_radio(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAnusuchit_count(final String bid) {


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
                        if (jsdata.has("anusuchit_activity_pramukh_count")) {
                            rdb_st.setText("ST(" + jsdata.getString("st_count") + ")");
                            rdb_sc.setText("SC(" + jsdata.getString("sc_count") + ")");
                            rdb_obc.setText("OBC(" + jsdata.getString("obc_count") + ")");

                            visiblity_radio(View.VISIBLE);

                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Offline Mode", Toast.LENGTH_LONG).show();

                    visiblity_radio(View.VISIBLE);
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
                params.put("action", Utils_url.Action_AnusuchitActivityCount);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", bid);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }

    private void visiblity_radio(int isVisible) {

        rd_grp_anusuchit.setVisibility(isVisible);
    }


    private void ClickEvent() {

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String panna_pramaukh, panna_mobile, address;
                panna_pramaukh = edt_anuschit_pramukh.getText().toString().trim();
                panna_mobile = edt_anusuchit_number.getText().toString().trim();

                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Booth", Toast.LENGTH_LONG).show();
                    return;
                } else if (rd_grp_anusuchit.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select Anusuchit Type", Toast.LENGTH_LONG).show();

                } else if (panna_pramaukh.equalsIgnoreCase("")) {
                    edt_anuschit_pramukh.setError(Html.fromHtml("<font color='orange'>Please Enter Pramukh Name</font>"));
                    return;
                } else if (panna_mobile.equalsIgnoreCase("")) {
                    edt_anusuchit_number.setError(Html.fromHtml("<font color='orange'>Please Enter Mobile No.</font>"));
                    return;
                } else if (panna_mobile.length() != 10) {
                    edt_anusuchit_number.setError(Html.fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else {
                    if (CheckNetworkState.isNetworkAvailable(AnusuchitReg.this)) {
                        int selectedId = rd_grp_anusuchit.getCheckedRadioButtonId();
                        int ANType = getANUSUCHIT_type(selectedId);


                        SavePannaPramukh(panna_pramaukh, panna_mobile, "", boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), ANType);

                    } else {
                        Log.e("i m herree", "in database");
                        int selectedId = rd_grp_anusuchit.getCheckedRadioButtonId();
                        int ANType = getANUSUCHIT_type(selectedId);

                        //tbl_anusuchit_member
                        DataBaseSaveAnusuchit(panna_pramaukh, panna_mobile, "", boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), "" + ANType);

                    }
                }
            }
        });
    }

    private int getANUSUCHIT_type(int btID) {
        int value = 0;
        Log.e("prince : ", "" + btID);

        switch (btID) {

            case R.id.rdb_sc:
                value = 1;
                break;

            case R.id.rdb_st:
                value = 2;
                break;
            case R.id.rdb_obc:
                value = 3;
                break;

        }
        return value;

    }

    private void SavePannaPramukh(final String man_pramaukh, final String man_mobile, final String address, final String booth_id, String user_name, final int ANType) {

        prg.setMessage("Please Wait....");
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
                    Log.e("in exception", "prince");
                    ex.printStackTrace();
                    DataBaseSaveAnusuchit(man_pramaukh, man_mobile, address, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), "" + ANType);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
                Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
                Log.e("Error :", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddAnusuchitActivity);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("pramukhName", man_pramaukh);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());
                params.put("pramukhMobile", man_mobile);
                params.put("category", "" + ANType);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void DataBaseSaveAnusuchit(String swachta_pramaukh, String swachta_mobile, String address, String booth_id, String user_name, String catagory) {
        Log.e("in Data Base ", "In Data BAse");

        try {
            boolean isSuccess = DBOperation.insertAnusuchit_in_booth(AnusuchitReg.this, SavedData.getUSER_NAME(), booth_id, swachta_pramaukh, address, SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), swachta_mobile, catagory);

            if (isSuccess) {
                Toast.makeText(getApplicationContext(), "Saved in Database", Toast.LENGTH_SHORT).show();
                Clear_Detail();
            } else {
                Log.e("Data no Saved", "Data not Saved");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void Clear_Detail() {
        edt_anuschit_pramukh.setText("");
        edt_anusuchit_number.setText("");
      //  spinner_booth.setSelection(0);
        edt_anuschit_pramukh.requestFocus();
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

    private void creadteIDS() {
        prg = new ProgressDialog(this);
        edt_anusuchit_number = (EditText) findViewById(R.id.edt_anusuchit_number);
        edt_anuschit_pramukh = (EditText) findViewById(R.id.edt_anusuchit_pramukh);

        button_submit = (Button) findViewById(R.id.button_submit);
        //button_add_member = (Button) findViewById(R.id.button_add_member);
        //txt_total_anusuchit_pramukh = (TextView) findViewById(R.id.txt_total_anusuchit_pramukh);

        // radio button
        rd_grp_anusuchit = (RadioGroup) findViewById(R.id.rd_grp_anusuchit);
        rdb_st = (RadioButton) findViewById(R.id.rdb_st);
        rdb_sc = (RadioButton) findViewById(R.id.rdb_sc);
        rdb_obc = (RadioButton) findViewById(R.id.rdb_obc);

        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(AnusuchitReg.this, R.layout.spinner_layout, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout);

        //  spinner_booth_array.add("Select Booth");

        spinner_booth.setAdapter(spinner_booth_adaptor);
        rd_grp_anusuchit.setVisibility(View.GONE);

    }
}


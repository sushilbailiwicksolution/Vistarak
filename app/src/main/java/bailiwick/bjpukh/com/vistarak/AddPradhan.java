package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
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
import bailiwick.bjpukh.com.vistarak.Support.CheckNetworkState;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

/**
 * Created by Prince on 05-12-2017.
 */

public class AddPradhan extends RootActivity {

    private Spinner spinner_booth;
    private EditText edt_panna_pramukh_number, edt_panna_pramukh, address_1;

    private Button button_submit, button_add_member;

    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;
    private ProgressDialog prg;
    private TextView txt_total_panna_pramukh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pradhan);
        creadteIDS();
        getBoothFromDB();
        ClickEvent();
        Spinne_Click();
    }

    private void Spinne_Click() {
        spinner_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals.get(position - 1).getBid());
                    txt_total_panna_pramukh.setVisibility(View.VISIBLE);

                    getPanna_count(boothSpinnerModals.get(position - 1).getBid());
                } else {
                    txt_total_panna_pramukh.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getPanna_count(final String bid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url,
                new Response.Listener<String>() {
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
                                if (jsdata.has("panna_pramukh_count")) {

                                    txt_total_panna_pramukh.setText("Panna Pramukh in Area " + jsdata.getString("panna_pramukh_count"));
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
                params.put("action", Utils_url.Action_Panna_Pramukh_count);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", bid);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }


    private void ClickEvent() {
        button_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //              getLocation();
                Intent i = new Intent(AddPradhan.this, Add_new_Member.class);
                startActivity(i);
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String panna_pramaukh, panna_mobile, address;
                panna_pramaukh = edt_panna_pramukh.getText().toString().trim();
                panna_mobile = edt_panna_pramukh_number.getText().toString().trim();
                address = address_1.getText().toString();

                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Booth", Toast.LENGTH_LONG).show();
                    return;
                } else if (panna_pramaukh.equalsIgnoreCase("")) {
                    edt_panna_pramukh.setError(Html
                            .fromHtml("<font color='orange'>Please Enter Pramukh Name</font>"));
                    return;
                } else if (panna_mobile.equalsIgnoreCase("")) {
                    edt_panna_pramukh_number.setError(Html
                            .fromHtml("<font color='orange'>Please Enter Mobile No.</font>"));
                    return;
                } else if (panna_mobile.length() != 10) {
                    edt_panna_pramukh_number.setError(Html
                            .fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else if (address.equalsIgnoreCase("")) {
                    address_1.setError(Html
                            .fromHtml("<font color='orange'>Please Enter Address.</font>"));
                    return;
                } else {
                    if (CheckNetworkState.isNetworkAvailable(AddPradhan.this)) {
                        SavePannaPramukh(panna_pramaukh, panna_mobile, address, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                    } else {
                        DataBaseSavePannaPramukh(panna_pramaukh, panna_mobile, address, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                    }
                }
            }
        });
    }

    private void SavePannaPramukh(final String man_pramaukh, final String man_mobile, final String address, final String booth_id, String user_name) {

        prg.setMessage("Saving Panna Pramukh....");
        prg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url,
                new Response.Listener<String>() {
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
                            DataBaseSavePannaPramukh(man_pramaukh, man_mobile, address, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());
                            ex.printStackTrace();
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
                params.put("action", Utils_url.Action_AddPannaPramukh);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("pramukhName", man_pramaukh);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());
                params.put("address", address_1.getText().toString());
                params.put("pramukhMobile", man_mobile);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void DataBaseSavePannaPramukh(String swachta_pramaukh, String swachta_mobile, String address, String booth_id, String user_name) {
        Log.e("in Data Base ", "In Data BAse");

        try {
            boolean isSuccess = DBOperation.insertPannaPramukh_in_booth(AddPradhan.this,
                    SavedData.getUSER_NAME(), booth_id, swachta_pramaukh, address,
                    SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), swachta_mobile);

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
        edt_panna_pramukh.setText("");
        edt_panna_pramukh_number.setText("");
        address_1.setText("");
        spinner_booth.setSelection(0);
        edt_panna_pramukh.requestFocus();
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
        edt_panna_pramukh_number = (EditText) findViewById(R.id.edt_panna_pramukh_number);
        edt_panna_pramukh = (EditText) findViewById(R.id.edt_panna_pramukh);
        address_1 = (EditText) findViewById(R.id.address_1);

        button_submit = (Button) findViewById(R.id.button_submit);
        button_add_member = (Button) findViewById(R.id.button_add_member);
        txt_total_panna_pramukh = (TextView) findViewById(R.id.txt_total_panna_pramukh);

        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(AddPradhan.this,
                R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);

    }
}

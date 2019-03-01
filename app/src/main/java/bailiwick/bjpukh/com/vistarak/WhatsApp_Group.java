package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.Support.CheckNetworkState;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 05-09-2017.
 */

public class WhatsApp_Group extends RootActivity {

    EditText edt_group_name, edt_group_admin, edt_admin_mobile;
    Button button_submit, button_add_member;
    ProgressDialog prg;
    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;

    TextView mWhatsApp, txt_member_count;
    TextInputLayout mGroupName, mGroupAdmin, mAdminContactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whats_app_group);
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
                                if (jsdata.has("whatsAppGroup_count")) {

                                    txt_member_count.setText("Register Member " + jsdata.getString("whatsAppGroup_count"));
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
                params.put("action", Utils_url.Action_WhatssApp_count);
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
                Intent i = new Intent(WhatsApp_Group.this, Add_new_Member.class);
                startActivity(i);
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group_name, group_admin, admin_no;
                group_name = edt_group_name.getText().toString().trim();
                group_admin = edt_group_admin.getText().toString().trim();
                admin_no = edt_admin_mobile.getText().toString().trim();

                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Booth", Toast.LENGTH_LONG).show();
                    return;
                } else if (group_name.equalsIgnoreCase("")) {
                    edt_group_name.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (group_admin.equalsIgnoreCase("")) {
                    edt_group_admin.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (admin_no.equalsIgnoreCase("")) {
                    edt_admin_mobile.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (admin_no.length() != 10) {
                    edt_admin_mobile.setError(Html
                            .fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else {
                    if (CheckNetworkState.isNetworkAvailable(WhatsApp_Group.this)) {
                        SaveWhatsAppgroup(group_name, group_admin, admin_no, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                    } else {
                        DataBaseSaveWhatsApp(group_name, group_admin, admin_no, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                    }
                }

            }
        });
    }

    private void DataBaseSaveWhatsApp(String group_name, String group_admin, String admin_no, String booth_id, String user_name) {

        Log.e("in Data Base ", "In Data BAse");

        try {
            boolean isSuccess = DBOperation.insertWhats_app(WhatsApp_Group.this,
                    SavedData.getUSER_NAME(), booth_id, group_admin,
                    SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), admin_no, group_name);

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

    private void SaveWhatsAppgroup(final String group_name, final String group_admin, final String admin_no, final String booth_id, String user_name) {


        prg.setMessage(SelectedLanguage[TextUtils.please_wait]);

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
                            DataBaseSaveWhatsApp(group_name, group_admin, admin_no, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();

                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddWhatsAppGroup);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("pramukhName", group_admin);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());

                params.put("pramukhMobile", admin_no);
                params.put("groupName", group_name);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }


    private void Clear_Detail() {
        edt_group_name.setText("");
        edt_group_admin.setText("");
        edt_admin_mobile.setText("");
        edt_group_name.requestFocus();
    }

    private void createIDs() {
        prg = new ProgressDialog(this);

        edt_group_name = (EditText) findViewById(R.id.edt_group_name);
        edt_group_admin = (EditText) findViewById(R.id.edt_group_admin);
        edt_admin_mobile = (EditText) findViewById(R.id.edt_admin_mobile);
        button_submit = (Button) findViewById(R.id.button_submit);
        button_add_member = (Button) findViewById(R.id.button_add_member);

        txt_member_count = (TextView) findViewById(R.id.txt_member_count);
        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(WhatsApp_Group.this,
                R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);
        setLanguage();
    }

    private void setLanguage() {
        mWhatsApp = (TextView) findViewById(R.id.text_whats_app);
        mGroupName = (TextInputLayout) findViewById(R.id.til_group_name_whatsapp);
        mGroupAdmin = (TextInputLayout) findViewById(R.id.til_group_admin);
        mAdminContactNumber = (TextInputLayout) findViewById(R.id.til_admin_contact_number);

        mWhatsApp.setText(SelectedLanguage[TextUtils.whatsapp_group]);
        mGroupName.setHint(SelectedLanguage[TextUtils.group_name]);
        mGroupAdmin.setHint(SelectedLanguage[TextUtils.group_admin]);
        mAdminContactNumber.setHint(SelectedLanguage[TextUtils.admin_contact_number]);
        button_submit.setText(SelectedLanguage[TextUtils.submit]);
        button_add_member.setText(SelectedLanguage[TextUtils.add_member]);

    }

}

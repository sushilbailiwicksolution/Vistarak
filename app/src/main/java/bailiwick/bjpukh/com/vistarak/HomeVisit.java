package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class HomeVisit extends RootActivity {

    ProgressDialog prg;
    RadioGroup rdG_member;
    RadioButton rdb_new_member, rdb_new_old_member;
    private EditText edt_family_voter, edt_family_member, edt_head_family, edt_address_1, edt_mobile;
    private EditText edt_member_name, edt_family_member_old, edt_mebership_no, edt_old_address, edt_mobile_old;
    private LinearLayout lnt_new, lnt_old;
    private Button button_submit, button_add_member_new, button_submit_old, button_add_member_old;
    // New Update
    private Spinner spinner_booth_new, spinner_booth_old;

    ArrayList<BoothSpinnerModal> boothSpinnerModals_new;
    ArrayList<String> spinner_booth_array_new;
    ArrayAdapter<String> spinner_booth_adaptor_new;

    ArrayList<BoothSpinnerModal> boothSpinnerModals_old;
    ArrayList<String> spinner_booth_array_old;
    ArrayAdapter<String> spinner_booth_adaptor_old;
    // New Memebr
    TextInputLayout mHeadOfFamily, mFamilyMember, mVotersInFamily, mAddress, mMobile;
    TextView mHomeVisit;
    // Old Member
    TextInputLayout til_old_mobile, til_old_address, til_old_membership_number, til_old_family_member, til_old_member_name;

    private TextView txt_member_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_visit);
        creadteIDS();
        ClickEvent();
        RadioActivity();
        getBoothFromDB();
        Spinne_Click();
    }

    private void Spinne_Click() {
        spinner_booth_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals_new.get(position - 1).getBid());
                    get_count(boothSpinnerModals_new.get(position - 1).getBid());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_booth_old.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals_old.get(position - 1).getBid());
                    get_count(boothSpinnerModals_old.get(position - 1).getBid());
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
                                if (jsdata.has("home_visit")) {

                                    txt_member_count.setText("Register Member " + jsdata.getString("home_visit"));
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
                params.put("action", Utils_url.Action_home_Visit_count);
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
        boothSpinnerModals_new = DBOperation.getAllBoothList(this);
        boothSpinnerModals_old = DBOperation.getAllBoothList(this);

        Log.e("in Database Calling", "in Database Calling");
        spinner_booth_array_new.add("Select Booth");
        spinner_booth_array_old.add("Select Booth");

        for (int i = 0; i < boothSpinnerModals_new.size(); i++) {
         //   spinner_booth_array_new.add((i + 1) + ". " + boothSpinnerModals_new.get(i).getBooth_name());
            spinner_booth_array_new.add("Booth No . " + boothSpinnerModals_new.get(i).getBooth_name());
        }
        spinner_booth_adaptor_new.notifyDataSetChanged();

        for (int i = 0; i < boothSpinnerModals_old.size(); i++) {
           // spinner_booth_array_old.add((i + 1) + ". " + boothSpinnerModals_old.get(i).getBooth_name());
            spinner_booth_array_old.add("Booth No . " + boothSpinnerModals_old.get(i).getBooth_name());

        }
        spinner_booth_adaptor_old.notifyDataSetChanged();

    }

    private void RadioActivity() {

        rdG_member.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View radioButton = rdG_member.findViewById(checkedId);
                int index = rdG_member.indexOfChild(radioButton);
                Log.e("index value", "" + index);
                switch (index) {
                    case 0: // first button

                        lnt_new.setVisibility(View.VISIBLE);
                        lnt_old.setVisibility(View.GONE);

                        break;
                    case 1: // secondbutton

                        lnt_new.setVisibility(View.GONE);
                        lnt_old.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });
    }

    private void ClickEvent() {
        button_add_member_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeVisit.this, Add_new_Member.class);
                startActivity(i);
            }
        });
        button_add_member_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeVisit.this, Add_new_Member.class);
                startActivity(i);
            }
        });

        button_submit_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String family_head, address_member, family_member, membership_number, home_visit_mobile;
                family_head = edt_member_name.getText().toString().trim();
                family_member = edt_family_member_old.getText().toString().trim();
                address_member = edt_old_address.getText().toString().trim();
                membership_number = edt_mebership_no.getText().toString().trim();
                home_visit_mobile = edt_mobile_old.getText().toString();

                if (spinner_booth_old.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Booth", Toast.LENGTH_LONG).show();
                    return;
                } else if (family_head.equalsIgnoreCase("")) {
                    edt_member_name.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (family_member.equalsIgnoreCase("")) {
                    edt_family_member_old.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (address_member.equalsIgnoreCase("")) {
                    edt_old_address.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (home_visit_mobile.equalsIgnoreCase("")) {
                    edt_mobile_old.setError(Html
                            .fromHtml("<font color='orange'>Please Enter Mobile No.</font>"));
                    return;
                } else if (home_visit_mobile.length() != 10) {
                    edt_mobile_old.setError(Html
                            .fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else {

                    if (CheckNetworkState.isNetworkAvailable(HomeVisit.this)) {

                        SaveHomeVisit(family_head, membership_number, family_member, address_member, home_visit_mobile, boothSpinnerModals_old.get(spinner_booth_old.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), false);

                    } else {


                        DatabASE_SaveHome_visit(family_head, membership_number, family_member, address_member, home_visit_mobile, boothSpinnerModals_old.get(spinner_booth_old.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), false);

                    }

                }
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String family_head, family_voter, family_member, address, home_visit_mobile;
                family_head = edt_head_family.getText().toString().trim();
                family_voter = edt_family_voter.getText().toString().trim();
                family_member = edt_family_member.getText().toString().trim();
                address = edt_address_1.getText().toString().trim();
                home_visit_mobile = edt_mobile.getText().toString();

                if (spinner_booth_new.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Booth", Toast.LENGTH_LONG).show();
                    return;
                } else if (family_head.equalsIgnoreCase("")) {
                    edt_head_family.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (family_voter.equalsIgnoreCase("")) {
                    edt_family_voter.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (family_member.equalsIgnoreCase("")) {
                    edt_family_member.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (address.equalsIgnoreCase("")) {
                    edt_address_1.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (home_visit_mobile.equalsIgnoreCase("")) {
                    edt_mobile.setError(Html
                            .fromHtml("<font color='orange'>Please Enter Mobile No.</font>"));
                    return;
                } else if (home_visit_mobile.length() != 10) {
                    edt_mobile.setError(Html
                            .fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else {
                    if (CheckNetworkState.isNetworkAvailable(HomeVisit.this)) {
                        SaveHomeVisit(family_head, family_voter, family_member, address, home_visit_mobile, boothSpinnerModals_new.get(spinner_booth_new.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), true);

                    } else {
                        DatabASE_SaveHome_visit(family_head, family_voter, family_member, address, home_visit_mobile, boothSpinnerModals_new.get(spinner_booth_new.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), true);

                    }
                }
            }
        });
    }

    private void DatabASE_SaveHome_visit(String family_head, String membership_number, String family_member, String address_member, String mobile,
                                         String booth_id, String user_name, boolean isNew) {
        Log.e("in Data Base ", "In Data BAse");
        try {

            if (isNew) {

                boolean isSuccess = DBOperation.insertHomevisit(HomeVisit.this,
                        SavedData.getUSER_NAME(), booth_id, SavedData.getLattitudeLocation(),
                        SavedData.getLongitudeLocation(), family_head, family_member, address_member, membership_number, "NA", "new", membership_number, mobile);


                if (isSuccess) {
                    Toast.makeText(getApplicationContext(), "Saved in Database", Toast.LENGTH_SHORT).show();
                    Clear_Detail(isNew);
                }

            } else {
                boolean isSuccess = DBOperation.insertHomevisit(HomeVisit.this,
                        SavedData.getUSER_NAME(), booth_id, SavedData.getLattitudeLocation(),
                        SavedData.getLongitudeLocation(), family_head, family_member, address_member, "NA", "NA", "old", membership_number, mobile);


                if (isSuccess) {
                    Toast.makeText(getApplicationContext(), "Saved in Database", Toast.LENGTH_SHORT).show();
                    Clear_Detail(isNew);
                }

            }

        } catch (
                Exception ex)

        {
            ex.printStackTrace();
        }

    }

    private void SaveHomeVisit(final String family_head, final String family_voter, final String family_member, final String address, final String mobile,
                               final String booth_id, String user_name, final boolean isNew) {


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
                                Clear_Detail(isNew);
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

                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddHomeVisit);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());
                params.put("head_name", family_head);
                params.put("family_member", family_member);
                params.put("address", address);
                params.put("mobile", mobile);

                if (isNew) {
                    params.put("total_voter", family_voter);
                    params.put("head_voter_id", "NA");
                    params.put("visitType", "new");


                } else {
                    params.put("visitType", "old");
                    params.put("membership_id", family_voter);

                }
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void Clear_Detail(boolean isNew) {
        if (isNew) {
            spinner_booth_new.setSelection(0);
            edt_family_voter.setText("");
            edt_family_member.setText("");
            edt_head_family.setText("");
            edt_mobile.setText("");
            edt_address_1.setText("");
            edt_head_family.requestFocus();

        } else {
            edt_mebership_no.setText("");
            spinner_booth_old.setSelection(0);
            edt_mobile_old.setText("");
            edt_member_name.setText("");
            edt_family_member_old.setText("");
            edt_old_address.setText("");
            edt_member_name.requestFocus();

        }

    }


    private void creadteIDS() {

        prg = new ProgressDialog(this);
        edt_family_voter = (EditText) findViewById(R.id.edt_family_voter);
        edt_family_member = (EditText) findViewById(R.id.edt_family_member);
        edt_head_family = (EditText) findViewById(R.id.edt_head_family);
        edt_address_1 = (EditText) findViewById(R.id.edt_address_1);
        button_submit = (Button) findViewById(R.id.button_submit);
        lnt_new = (LinearLayout) findViewById(R.id.lnt_new);
        button_add_member_new = (Button) findViewById(R.id.button_add_member_new);
        button_add_member_old = (Button) findViewById(R.id.button_add_member_old);
        txt_member_count = (TextView) findViewById(R.id.txt_member_count);
        spinner_booth_new = (Spinner) findViewById(R.id.spinner_booth_new);
        spinner_booth_array_new = new ArrayList<>();
        boothSpinnerModals_new = new ArrayList<>();

        spinner_booth_adaptor_new = new ArrayAdapter<String>(HomeVisit.this,
                R.layout.spinner_layout_white_text, spinner_booth_array_new);
        spinner_booth_adaptor_new.setDropDownViewResource(R.layout.spinner_layout_white);

        spinner_booth_new.setAdapter(spinner_booth_adaptor_new);

        edt_mobile_old = (EditText) findViewById(R.id.edt_mobile_old);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);

        // Old Member

        lnt_old = (LinearLayout) findViewById(R.id.lnt_old);
        button_submit_old = (Button) findViewById(R.id.button_submit_old);
        edt_member_name = (EditText) findViewById(R.id.edt_member_name);
        edt_family_member_old = (EditText) findViewById(R.id.edt_family_member_old);
        edt_mebership_no = (EditText) findViewById(R.id.edt_mebership_no);
        edt_old_address = (EditText) findViewById(R.id.edt_old_address);

        rdG_member = (RadioGroup) findViewById(R.id.radios);
        rdb_new_member = (RadioButton) findViewById(R.id.rdb_new_member);
        rdb_new_old_member = (RadioButton) findViewById(R.id.rdb_old_member);
        spinner_booth_old = (Spinner) findViewById(R.id.spinner_booth_old);
        spinner_booth_array_old = new ArrayList<>();
        boothSpinnerModals_old = new ArrayList<>();
        spinner_booth_adaptor_old = new ArrayAdapter<String>(HomeVisit.this,
                R.layout.spinner_layout_white_text, spinner_booth_array_old);
        spinner_booth_adaptor_old.setDropDownViewResource(R.layout.spinner_layout_white);

        spinner_booth_old.setAdapter(spinner_booth_adaptor_old);
        setLanguage();

    }

    private void setLanguage() {
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());
        mHomeVisit = (TextView) findViewById(R.id.text_home_visit);
        mHeadOfFamily = (TextInputLayout) findViewById(R.id.til_head_of_family);
        mFamilyMember = (TextInputLayout) findViewById(R.id.til_family_member);
        mVotersInFamily = (TextInputLayout) findViewById(R.id.til_voters_in_family);
        mAddress = (TextInputLayout) findViewById(R.id.til_addres);
        mMobile = (TextInputLayout) findViewById(R.id.til_mobil);

// Old member
        til_old_mobile = (TextInputLayout) findViewById(R.id.til_old_mobile);
        til_old_address = (TextInputLayout) findViewById(R.id.til_old_address);
        til_old_membership_number = (TextInputLayout) findViewById(R.id.til_old_membership_number);
        til_old_family_member = (TextInputLayout) findViewById(R.id.til_old_family_member);
        til_old_member_name = (TextInputLayout) findViewById(R.id.til_old_member_name);


        button_submit_old.setText(SelectedLanguage[TextUtils.submit]);

        til_old_mobile.setHint(SelectedLanguage[TextUtils.mobile]);
        til_old_address.setHint(SelectedLanguage[TextUtils.address]);
        til_old_member_name.setHint(SelectedLanguage[TextUtils.full_name]);
        til_old_membership_number.setHint(SelectedLanguage[TextUtils.memberShip_Number]);
        til_old_family_member.setHint(SelectedLanguage[TextUtils.family_member]);

        mHomeVisit.setText(SelectedLanguage[TextUtils.home_visit]);
        mHeadOfFamily.setHint(SelectedLanguage[TextUtils.head_of_family]);
        mFamilyMember.setHint(SelectedLanguage[TextUtils.family_member]);
        mVotersInFamily.setHint(SelectedLanguage[TextUtils.voters_in_family]);
        mAddress.setHint(SelectedLanguage[TextUtils.address]);
        mMobile.setHint(SelectedLanguage[TextUtils.mobile]);
        rdb_new_member.setText(SelectedLanguage[TextUtils.new_record]);
        rdb_new_old_member.setText(SelectedLanguage[TextUtils.old_member]);
        button_submit.setText(SelectedLanguage[TextUtils.submit]);
        button_add_member_new.setText(SelectedLanguage[TextUtils.add_member]);


    }

}


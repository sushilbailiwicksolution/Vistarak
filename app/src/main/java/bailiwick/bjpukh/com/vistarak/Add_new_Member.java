package bailiwick.bjpukh.com.vistarak;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Dialog.CustomDialogOTPresend;
import bailiwick.bjpukh.com.vistarak.Dialog.CustomDialogVerifyMember;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothSpinnerModal;
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


public class Add_new_Member extends RootActivity implements DatePickerDialog.OnDateSetListener {

    final Calendar c = Calendar.getInstance();
    TextView date_of_birth;
    RadioGroup maritial_status;
    RadioButton single, married;
    LinearLayout lnt_cast;
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog;
    ProgressDialog prg;
    ArrayList<String> LIST_occupation;
    Spinner spinner_occupation;
    ArrayAdapter Spinner_Occupation_adapter;
    ArrayList<String> LIST_Religion_Voter;
    Spinner spinner_select_Religion;
    ArrayAdapter Spinner_Religion_adapter;
    ArrayList<String> LIST_Cast_Voter;
    Spinner spinner_cast, spinner_booth;
    ArrayAdapter Spinner_cast_adapter;
    private Button button_add_member;
    private EditText full_name, voter_id, edt_mobile;
    private LinearLayout lnt_voter_id;
    private ImageView image_profile;
    private EditText edt_dateDD, edt_dateMM, edt_dateYYY;
    //private boolean isVerify = false;
    // private boolean isCheckNumber = false;
    //private String otp_message = "";
    TextInputLayout mFullName, mMobile, mOtp;
    TextView mNewMember, txt_member_count;

    // Membership
    private TextView txt_member_ship, txt_newmember;
    private LinearLayout lnt_newmember, lnt_membership, lnt_membership_no_tag, lnt_page2;
    private boolean isNewMember = true;
    private EditText edt_membership_no;


// Booth Spinner

    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;

    public static boolean isDateValid(String date) {
        final String DATE_FORMAT = "dd-MM-yyyy";
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_member);
        initId();
        clickListener();
        setSpinner();
        setOccupation_spinner();
        setOccupation_cast();
        spinnrClick();
        // MobileVerifier();
        Clear_Detail();
        Datechecker();
        getBoothFromDB();
        setMembershipRegister(isNewMember);
        Spinne_Click();
    }

    private void setMembershipRegister(boolean isNewMember) {

        if (isNewMember) {
            lnt_newmember.setBackground(getResources().getDrawable(R.drawable.btn_shape_white));
            txt_newmember.setTextColor(getResources().getColor(R.color.white));
            lnt_membership.setBackgroundResource(0);
            txt_member_ship.setTextColor(getResources().getColor(R.color.color_black));
            lnt_membership_no_tag.setVisibility(View.GONE);
            lnt_page2.setVisibility(View.VISIBLE);

        } else {
            lnt_newmember.setBackgroundColor(0);
            txt_newmember.setTextColor(getResources().getColor(R.color.color_black));
            lnt_membership.setBackground(getResources().getDrawable(R.drawable.btn_shape_white));
            txt_member_ship.setTextColor(getResources().getColor(R.color.white));
            lnt_membership_no_tag.setVisibility(View.VISIBLE);
            lnt_page2.setVisibility(View.GONE);

        }
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
                    String msg="";
                    JSONObject jsdata = new JSONObject(response);
                    if(jsdata.has("msg")){
                         msg = jsdata.getString("msg");

                    }

                    String status = jsdata.getString("status");
                    SavedData.saveOperation_status(false);

                    if (status.equalsIgnoreCase("1")) {
                        if (jsdata.has("member_count")) {

                            txt_member_count.setText("Register Member " + jsdata.getString("member_count"));
                        }
                        if(jsdata.has("count")){
                            txt_member_count.setText("Register Member " + jsdata.getString("count"));
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
                if (isNewMember) {
                    params.put("action", Utils_url.Action_Member_count);

                } else {
                    params.put("action", Utils_url.Action_get_existing_member_count);

                }
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
        if (boothSpinnerModals.size() > 0 && boothSpinnerModals != null) {
            Log.e("in Database Calling", "in Database Calling");
            spinner_booth_array.add("Select Booth");

            for (int i = 0; i < boothSpinnerModals.size(); i++) {
                //   spinner_booth_array.add((i + 1) + ". " + boothSpinnerModals.get(i).getBooth_name());
                spinner_booth_array.add("Booth No . " + boothSpinnerModals.get(i).getBooth_name());

            }
            spinner_booth_adaptor.notifyDataSetChanged();
            SavedData.saveBOOTH_ID(boothSpinnerModals.get(0).getBid());


        } else {
            getBoothFromServer();
        }
    }

    private void getBoothFromServer() {
        Log.e("in Api Calling", "in Api Calling");
        prg.setMessage("");
        prg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.e("Response :", response);
                //  Manage_data_login(response);
                try {
                    boothSpinnerModals.clear();
                    spinner_booth_array.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray boothArray = jsonObject.getJSONArray("boothDetails");
                    boothSpinnerModals.add(new BoothSpinnerModal("", "Select Booth", ""));

                    for (int i = 0; i < boothArray.length(); i++) {

                        boothSpinnerModals.add(new BoothSpinnerModal(boothArray.getJSONObject(i).getString("bid"), boothArray.getJSONObject(i).getString("booth_name"), boothArray.getJSONObject(i).getString("consistency_name")));
                        spinner_booth_array.add((i + 1) + ". " + boothArray.getJSONObject(i).getString("booth_name"));
                        SavedData.saveBOOTH_ID(boothSpinnerModals.get(0).getBid());
                        boolean isSuccess = DBOperation.insertBooth(Add_new_Member.this, boothArray.getJSONObject(i).getString("bid"), boothArray.getJSONObject(i).getString("booth_name"), boothArray.getJSONObject(i).getString("consistency_name"));

                    }

                    spinner_booth_adaptor.notifyDataSetChanged();
                    prg.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("action", Utils_url.Action_GetBooth);
                params.put("userName", SavedData.getUSER_NAME());

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void Datechecker() {

        edt_dateDD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edt_dateDD.setText("");
                    //  edt_dateDD.requestFocus();
                }
            }
        });

        edt_dateDD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);
                if (edt_dateDD.getText().toString().length() >= 2) {
                    int dd = Integer.parseInt(edt_dateDD.getText().toString().trim());
                    if (dd > 31 || dd <= 0) {
                        edt_dateDD.setError("invalid date");
                        edt_dateDD.setText("");
                    }
                    if (edt_dateMM.getText().toString().equalsIgnoreCase("")) {
                        edt_dateMM.requestFocus();

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_dateMM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edt_dateMM.setText("");
                }
            }
        });

        edt_dateMM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);
                if (edt_dateMM.getText().toString().length() >= 2) {
                    int dd = Integer.parseInt(edt_dateMM.getText().toString().trim());
                    if (dd >= 13 || dd <= 0) {
                        edt_dateMM.setError("invalid Month");
                        edt_dateMM.setText("");
                    }
                    if (edt_dateYYY.getText().toString().equalsIgnoreCase("")) {
                        edt_dateYYY.requestFocus();

                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_dateYYY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edt_dateYYY.setText("");
                } else {
                    if (edt_dateYYY.getText().toString().length() != 4 && !edt_dateYYY.getText().toString().equalsIgnoreCase("")) {

                        edt_dateYYY.setError("Invalid year");
                    }
                }
            }
        });

        edt_dateYYY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /*private void MobileVerifier() {

        edt_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    verifyNumber();
                    //  edt_dateDD.requestFocus();
                }
            }
        });
    }*/
// Verify Mobile No of Vistarak comment on 18 - 10 - 2018
  /*  private void verifyNumber() {
        Log.e("i m here", "check 1");

        String str_mobile = edt_mobile.getText().toString().trim();

        if (!str_mobile.equalsIgnoreCase("")) {
            Log.e("i m here", "check 2");

            if (str_mobile.length() < 9 || str_mobile.length() > 11) {
                edt_mobile.setError("Invalid Number");
            } else {
                GetMobileStatus(str_mobile);
            }

        } else {
            edt_mobile.setError("Requried");

        }
        Log.e("i m here", "check 3");

    }*/

   /* private void GetMobileStatus(final String str_mobile) {
        isCheckNumber = false;
        prg.setMessage("Verifying Number...");
        prg.show();
        edt_mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        prg.dismiss();
                        Log.e("Response :", response);
                        //  Manage_data_login(response);
                        try {
                            JSONObject jsdata = new JSONObject(response);
                            otp_message = jsdata.getString("otp");
                            String status = jsdata.getString("status");
//                            Toast.makeText(getApplicationContext(), otp_message, Toast.LENGTH_LONG).show();

                            if (status.equalsIgnoreCase("1")) {
                                Toast.makeText(getApplicationContext(), "Registerd Number Please verify", Toast.LENGTH_LONG).show();
                                isVerify = true;
                                JSONArray jsArray = jsdata.getJSONArray("data");
                                String member_name = "";
                                for (int i = 0; i < jsArray.length(); i++) {
                                    member_name = jsArray.getJSONObject(i).getString("full_name");
                                    break;
                                }
                                VerifyCustomerNumebr(member_name);
                            } else {
                                Toast.makeText(getApplicationContext(), "New Number Verified", Toast.LENGTH_LONG).show();
                                isVerify = false;
                                edt_mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mob_verify, 0);
                                isCheckNumber = true;
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
                params.put("action", Utils_url.Action_Mobile_verification);
                params.put("mobileNumber", str_mobile);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }*/

   /* private void VerifyCustomerNumebr(String member_name) {
        final CustomDialogVerifyMember cstdialog = new CustomDialogVerifyMember(this);
        cstdialog.setCancelable(false);
        cstdialog.show();
        cstdialog.txt_message.setText("Do You know " + member_name + ". \nHe is already register with this number");
        cstdialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Mobile Number is not verified Please try Another Number", Toast.LENGTH_SHORT).show();
                cstdialog.dismiss();
                Clear_Detail();
            }
        });
        cstdialog.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mob_verify, 0);
                isCheckNumber = true;
                isVerify = true;
                lnt_voter_id.setVisibility(View.VISIBLE);
                cstdialog.dismiss();
            }
        });
    }*/

    private void spinnrClick() {
        spinner_select_Religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    lnt_cast.setVisibility(View.VISIBLE);
                } else {
                    lnt_cast.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setOccupation_spinner() {
        LIST_occupation = new ArrayList<>();
        LIST_occupation.add("Select Occupation");
        LIST_occupation.add("Student");
        LIST_occupation.add("Teacher");
        LIST_occupation.add("Doctor");
        LIST_occupation.add("Engineer");
        LIST_occupation.add("Army");
        LIST_occupation.add("Private Job");
        LIST_occupation.add("Govt. Job");
        LIST_occupation.add("Other");
        Spinner_Occupation_adapter = new ArrayAdapter<String>(Add_new_Member.this, R.layout.spinner_layout_white_text, LIST_occupation);
        Spinner_Occupation_adapter.setDropDownViewResource(R.layout.spinner_layout_white);


        spinner_occupation.setAdapter(Spinner_Occupation_adapter);
    }

    private void setOccupation_cast() {
        LIST_Cast_Voter = new ArrayList<>();
        LIST_Cast_Voter.add("General");
        LIST_Cast_Voter.add("OBC");
        LIST_Cast_Voter.add("SC");
        LIST_Cast_Voter.add("ST");

        Spinner_cast_adapter = new ArrayAdapter<String>(Add_new_Member.this, R.layout.spinner_layout_white_text, LIST_Cast_Voter);
        Spinner_cast_adapter.setDropDownViewResource(R.layout.spinner_layout_white);

        spinner_cast.setAdapter(Spinner_cast_adapter);
    }

    private void setSpinner() {
        LIST_Religion_Voter = new ArrayList<>();

        LIST_Religion_Voter.add("Select Religion");
        LIST_Religion_Voter.add("Hindu");
        LIST_Religion_Voter.add("Muslim");
        LIST_Religion_Voter.add("Sikh");
        LIST_Religion_Voter.add("Christian");

        Spinner_Religion_adapter = new ArrayAdapter<String>(Add_new_Member.this, R.layout.spinner_layout_white_text, LIST_Religion_Voter);
//        Spinner_Religion_adapter.setDropDownViewResource(R.layout.spinner_layout);
        Spinner_Religion_adapter.setDropDownViewResource(R.layout.spinner_layout_white);

        spinner_select_Religion.setAdapter(Spinner_Religion_adapter);
    }

    private void clickListener() {

        lnt_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewMember = false;
                setMembershipRegister(isNewMember);
            }
        });

        lnt_newmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewMember = true;
                setMembershipRegister(isNewMember);
            }
        });
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        button_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewMember) {
                    validateFields();
                } else {

                    ValidateMebership();
                }

               /* if (isCheckNumber) {
                    Log.e("i m here", "111");


                } else {
                    Log.e("no not verify", "no not verify");
                    verifyNumber();
                }*/
            }
        });
    }

    private void ValidateMebership() {
        if (spinner_booth.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.select_booth], Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(full_name.getText().toString().trim())) {
            full_name.setError("Required");
            return;
        } else if (TextUtils.isEmpty(edt_mobile.getText().toString().trim())) {
            Log.e("i m here", "Mobile " + edt_mobile.getText().toString().trim());
            edt_mobile.setError("Required");
            return;
        } else {
            if (CheckNetworkState.isNetworkAvailable(this)) {
                SaveMembershipOnline(full_name.getText().toString(), edt_mobile.getText().toString(), edt_membership_no.getText().toString());
            } else {
                Log.e("offline", "off line");

            }
        }
    }

    private void SaveMembershipOnline(final String full_name, final String mobile, final String membership_no) {

        prg.setMessage(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.please_wait]);

        prg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                prg.dismiss();
                Log.e("Response :", response);
                //  Manage_data_login(response);
                try {
                    JSONObject jsdata = new JSONObject(response);
                    String msg = jsdata.getString("msg");
                    String status = jsdata.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Clear_DetailMembership();
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
                params.put("action", Utils_url.Action_add_exisiting_member_varified);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                Log.e("check", boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBooth_name());
                params.put("booth_id", boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid());
                params.put("name", full_name);
                params.put("mobile", mobile);

                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());

                params.put("membership_id", membership_no);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void Clear_DetailMembership() {
        full_name.setText("");
        edt_mobile.setText("");
        edt_membership_no.setText("");
    }

    private void validateFields() {

        Log.e("Rdio", maritial_status.getCheckedRadioButtonId() + "");
        String religion_ = spinner_select_Religion.getSelectedItem().toString();
        Log.e("selected item", religion_);

        if (spinner_booth.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.select_booth], Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(full_name.getText().toString().trim())) {
            full_name.setError("Required");
            return;
        } else if (TextUtils.isEmpty(edt_mobile.getText().toString().trim())) {
            Log.e("i m here", "Mobile " + edt_mobile.getText().toString().trim());
            edt_mobile.setError("Required");
            return;
        } else if (maritial_status.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select Maiatial Status", Toast.LENGTH_SHORT).show();
        } else if (!isdateValidate()) {

            return;
        }
        if (religion_.equalsIgnoreCase("Select Religion")) {
            Toast.makeText(getApplicationContext(), "Please select regligion", Toast.LENGTH_LONG).show();
            return;
        } else if (spinner_occupation.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select Occupation", Toast.LENGTH_LONG).show();
            return;
        } else {
           /* if (isVerify) {
                Log.e("i m here", "222");
                if (voter_id.getText().toString().trim().equalsIgnoreCase("")) {
                    voter_id.setError("Requried");
                    return;
                }
                Log.e("i m here", "444");

            }*/
// find the radiobutton by returned id
            Log.e("i m here", "333");

            String radioText = "";
            int selectedId = maritial_status.getCheckedRadioButtonId();
            if (selectedId == single.getId()) {
                radioText = "Single";
            } else if (selectedId == married.getId()) {
                radioText = "Married";
            }
            if (CheckNetworkState.isNetworkAvailable(Add_new_Member.this)) {
                sendToServer(radioText, religion_);

            } else {

                Toast.makeText(Add_new_Member.this, "Network not avilabel please try again later", Toast.LENGTH_SHORT).show();
                  /*  String voter_cast;
                    String dob = edt_dateDD.getText().toString() + "-" + edt_dateMM.getText().toString() + "-" + edt_dateYYY.getText().toString();
                    if (spinner_cast.getSelectedItemPosition() == 0) {
                        voter_cast = "NA";
                    } else {
                        voter_cast = spinner_cast.getSelectedItem().toString();

                    }
                    DatabASE_SaveNewMember(SavedData.getUSER_NAME(), SavedData.getBOOTH_ID(), full_name.getText().toString(), radioText,
                            dob, edt_mobile.getText().toString(), spinner_occupation.getSelectedItem().toString(), address_1.getText().toString(),
                            religion_, voter_cast, "NA", SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), voter_id.getText().toString().trim());

               */
            }
          /*  if (otp_message.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Mobile No is not validate", Toast.LENGTH_LONG).show();
            } else if (!edt_otp.getText().toString().trim().equalsIgnoreCase(otp_message)) {
                ShowotpDialog();
            } else {

            }*/

        }
    }

    private void DatabASE_SaveNewMember(String user_name, String booth_id, String full_name, String radioText, String dob, String mobile, String occupation, String address, String religion_, String voter_cast, String member_type, String lattitudeLocation, String longitudeLocation, String voter_id) {

        Log.e("in Data Base ", "In Data BAse");
        try {
            boolean isSuccess = DBOperation.insert_new_Member(Add_new_Member.this, user_name, booth_id, full_name, radioText, dob, mobile, occupation, address, religion_, voter_cast, member_type, lattitudeLocation, longitudeLocation, voter_id);

            if (isSuccess) {
                Toast.makeText(getApplicationContext(), "Saved in Database", Toast.LENGTH_SHORT).show();
                Clear_Detail();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private boolean isdateValidate() {
        Log.e("i m heree", "1111");
        boolean isValid = false;
        String Str_dd, Str_mm, Str_yyy;
        Str_dd = edt_dateDD.getText().toString().trim();
        Str_mm = edt_dateMM.getText().toString().trim();
        Str_yyy = edt_dateYYY.getText().toString().trim();


        if (edt_dateDD.getText().toString().trim().equalsIgnoreCase("")) {
            edt_dateDD.setError("Requried");
            return isValid;
        } else if (edt_dateMM.getText().toString().trim().equalsIgnoreCase("")) {
            edt_dateMM.setError("Requried");
            return isValid;
        } else if (edt_dateYYY.getText().toString().trim().equalsIgnoreCase("")) {
            edt_dateYYY.setError("Requried");
            return isValid;
        } else if (!isDateValid(Str_dd + "-" + Str_mm + "-" + Str_yyy)) {
            Toast.makeText(getApplicationContext(), "not valid Date", Toast.LENGTH_SHORT).show();
            return isValid;
        } else {

            isValid = true;
        }

        return isValid;
    }

   /* private void ShowotpDialog() {
        final CustomDialogOTPresend otpDialog = new CustomDialogOTPresend(this);
        otpDialog.show();
        otpDialog.popup_txt_title.setText("OTP Resend");
        otpDialog.txt_message.setText("Do you want to resend OTP");
        otpDialog.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();
                verifyNumber();
            }
        });
        otpDialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();
            }
        });
    }*/

    private void sendToServer(final String radioText, final String religion_) {
        prg.setMessage(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.please_wait]);

        prg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                prg.dismiss();
                Log.e("Response :", response);
                //  Manage_data_login(response);
                try {
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

                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddMember);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                Log.e("check", boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBooth_name());
                params.put("booth_id", boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid());
                params.put("full_name", full_name.getText().toString());
                params.put("marital_status", radioText);
                String dob = edt_dateDD.getText().toString() + "-" + edt_dateMM.getText().toString() + "-" + edt_dateYYY.getText().toString();
                params.put("dob", dob);
                params.put("mobile", edt_mobile.getText().toString());
                params.put("occupation", spinner_occupation.getSelectedItem().toString());
                params.put("address", "");
                params.put("religion", religion_);
                String voter_cast;
                if (spinner_cast.getSelectedItemPosition() == 0) {
                    voter_cast = "NA";
                } else {
                    voter_cast = spinner_cast.getSelectedItem().toString();

                }
                params.put("caste", voter_cast);
                params.put("member_type", "NA");
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());

                params.put("voterid", voter_id.getText().toString().trim());


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void Clear_Detail() {
        full_name.setText("");
        // address_1.setText("");
        spinner_occupation.setSelection(0);
        voter_id.setText("");
        edt_mobile.setText("");
        //  date_of_birth.setText("Date Of Birth");
        edt_dateDD.setText("");
        edt_dateMM.setText("");
        edt_dateYYY.setText("");

        maritial_status.clearCheck();
        voter_id.setText("");
        spinner_booth.setSelection(0);
        spinner_select_Religion.setSelection(0);
        spinner_cast.setSelection(0);
        full_name.requestFocus();
        //   isVerify = false;
        //  isCheckNumber = false;
        edt_mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        // otp_message = "";
        // edt_otp.setText("");
        lnt_voter_id.setVisibility(View.GONE);
    }

    private void initId() {
        button_add_member = (Button) findViewById(R.id.button_add_member);
        full_name = (EditText) findViewById(R.id.full_name);
        //  address_1 = (EditText) findViewById(R.id.address_1);
        spinner_occupation = (Spinner) findViewById(R.id.occupation);
        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();


        txt_member_ship = (TextView) findViewById(R.id.txt_member_ship);
        txt_newmember = (TextView) findViewById(R.id.txt_newmember);
        lnt_membership = (LinearLayout) findViewById(R.id.lnt_membership);
        lnt_newmember = (LinearLayout) findViewById(R.id.lnt_newmember);

        edt_membership_no = (EditText) findViewById(R.id.edt_member_ship_no);

        lnt_membership_no_tag = (LinearLayout) findViewById(R.id.lnt_membership_no_tag);
        lnt_page2 = (LinearLayout) findViewById(R.id.lnt_page2);
        txt_member_count = (TextView) findViewById(R.id.txt_member_count);

        spinner_booth_adaptor = new ArrayAdapter<String>(Add_new_Member.this, R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);

        spinner_booth.setAdapter(spinner_booth_adaptor);


        voter_id = (EditText) findViewById(R.id.voter_id);
        spinner_cast = (Spinner) findViewById(R.id.spnr_cast);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        //  edt_otp = (EditText) findViewById(R.id.edt_otp);
        lnt_cast = (LinearLayout) findViewById(R.id.lnt_cast);
        lnt_voter_id = (LinearLayout) findViewById(R.id.lnt_voter_id);
        spinner_select_Religion = (Spinner) findViewById(R.id.spinner_select_Religion);
        date_of_birth = (TextView) findViewById(R.id.date_of_birth);
        edt_dateDD = (EditText) findViewById(R.id.edt_dateDD);
        edt_dateMM = (EditText) findViewById(R.id.edt_dateMM);
        edt_dateYYY = (EditText) findViewById(R.id.edt_dateYYYY);

        maritial_status = (RadioGroup) findViewById(R.id.maritial_status);

        datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        single = (RadioButton) findViewById(R.id.single);
        married = (RadioButton) findViewById(R.id.married);
        prg = new ProgressDialog(this);
        //image_profile = (ImageView) findViewById(R.id.image_profile);
        if (!SavedData.getPROFILE_PIC().equalsIgnoreCase("")) {

            //  Picasso.with(this).load(SavedData.getPROFILE_PIC())
            //        .placeholder(R.drawable.profile_default).error(R.drawable.profile_default).into(image_profile);

        }
        setLanguage();
    }

    private void setLanguage() {

        mFullName = (TextInputLayout) findViewById(R.id.til_full_name);
        mMobile = (TextInputLayout) findViewById(R.id.til_mobile);
        //   mAddress = (TextInputLayout) findViewById(R.id.til_address);
        //    mOtp = (TextInputLayout) findViewById(R.id.til_otp);
        mNewMember = (TextView) findViewById(R.id.text_new_member);

        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());
      /*  full_name.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.full_name]);
        edt_mobile.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.mobile]);*/
        single.setText(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.single]);
        married.setText(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.married]);
        edt_dateDD.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.date]);
        edt_dateMM.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.month]);
        edt_dateYYY.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.year]);
        date_of_birth.setText(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.d_o_b]);
        /*address_1.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.address_1]);
        edt_otp.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.otp]);*/
        button_add_member.setText(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.add_member]);
        mNewMember.setText(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.new_member]);
        mFullName.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.full_name]);
        mMobile.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.mobile]);
        // mAddress.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.address_1]);
        //  mOtp.setHint(SelectedLanguage[bailiwick.bjpukh.com.vistarak.Language.TextUtils.otp]);


    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String month1 = "", day1 = "";
        if (String.valueOf(month + 1).length() == 1) month1 = "0" + (month + 1);
        else month1 = (month + 1) + "";
        if (String.valueOf(day).length() == 1) day1 = "0" + (day);
        else day1 = (day) + "";

        edt_dateDD.setText(day1);

        edt_dateMM.setText(month1);
        edt_dateYYY.setText("" + year);

        //      date_of_birth.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
    }
}

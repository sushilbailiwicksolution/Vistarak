package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Itag;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 21-02-2018.
 */

public class Activity_Comitee extends RootActivity {
    ProgressDialog prg;

    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;
    private Button button_submit;
    private EditText edt_bla_mobile, edt_bla_name, edt_adyaksh_mobile, edt_adyaksh_name, edt_palak_mobile, edt_palak_name;

    boolean isNewEntry = true;

    TextView txt_booth_bla, txt_booth_palak, txt_kameti_member, txt_adhyaksh;
    TextInputLayout til_bla_mobile, til_bla_name, til_palak_mobile, til_palak_name, til_adyaksh_mobile, til_adyaksh_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_kameti);
        createIDs();
        getBoothFromDB();
        clickEvent();
        spinner_click();
    }

    private void spinner_click() {
        spinner_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals.get(position - 1).getBid());
                    getKametiMember(boothSpinnerModals.get(position - 1).getBid());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getKametiMember(final String bid) {

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
                                isNewEntry = false;
                                if (!jsdata.getString("adyaksh_contact").equalsIgnoreCase("0")) {
                                    edt_adyaksh_mobile.setText(jsdata.getString("adyaksh_contact"));
                                }
                                if (!jsdata.getString("palak_contact").equalsIgnoreCase("0")) {
                                    edt_palak_mobile.setText(jsdata.getString("palak_contact"));

                                }
                                if (!jsdata.getString("bla_contact").equalsIgnoreCase("0")) {
                                    edt_bla_mobile.setText(jsdata.getString("bla_contact"));

                                }


                                edt_adyaksh_name.setText(jsdata.getString("adyaksh_name"));
                                edt_palak_name.setText(jsdata.getString("palak_name"));
                                edt_bla_name.setText(jsdata.getString("bla_name"));


                            } else {
                                isNewEntry = true;
                                Log.e("Response  : ", msg);
                                Clear_Detail(false);
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_GetKameti_Member);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", bid);

                // params.put("booth_name", booth_name);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }

    private void clickEvent() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String booth_name, str_adykash_name, str_adykash_mobile, str_palak_name, str_palak_mobile, str_bla_name, str_bla_mobile;
                str_adykash_name = edt_adyaksh_name.getText().toString().trim();
                str_adykash_mobile = edt_adyaksh_mobile.getText().toString().trim();
                str_palak_name = edt_palak_name.getText().toString().trim();
                str_palak_mobile = edt_palak_mobile.getText().toString().trim();
                str_bla_name = edt_bla_name.getText().toString().trim();
                str_bla_mobile = edt_bla_mobile.getText().toString().trim();

                boolean case1 = Check_validation(str_adykash_mobile, str_adykash_name, edt_adyaksh_mobile, edt_adyaksh_name);
                boolean case2 = Check_validation(str_palak_mobile, str_palak_name, edt_palak_mobile, edt_palak_name);
                boolean case3 = Check_validation(str_bla_mobile, str_bla_name, edt_bla_mobile, edt_bla_name);

                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplication(), "Please select Booth Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!case1 || !case2 || !case3) {
                    return;
                } else if (str_adykash_name.equalsIgnoreCase("") && str_adykash_mobile.equalsIgnoreCase("")
                        && str_palak_name.equalsIgnoreCase("") && str_palak_mobile.equalsIgnoreCase("") && str_bla_name.equalsIgnoreCase("")
                        && str_bla_mobile.equalsIgnoreCase("")) {
                    Toast.makeText(getApplication(), "Please Enter the Adyaks name", Toast.LENGTH_LONG).show();

                } else {
                    booth_name = spinner_booth.getSelectedItem().toString();
                    String boothID = boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid();
                    //SaveComitteeDatabase(booth_name, boothID, str_adykash_name, str_adykash_mobile, str_palak_name, str_palak_mobile, str_bla_name, str_bla_mobile);

                    if (CheckNetworkState.isNetworkAvailable(Activity_Comitee.this)) {
                        SaveComitte(booth_name, boothID, str_adykash_name, str_adykash_mobile, str_palak_name, str_palak_mobile, str_bla_name, str_bla_mobile, isNewEntry);
                    } else {
                        String valuetype = Itag.new_entry;
                        if (isNewEntry) {
                            valuetype = Itag.new_entry;
                        } else {
                            valuetype = Itag.old_entry;
                        }
                        SaveComitteeDatabase(booth_name, boothID, str_adykash_name, str_adykash_mobile, str_palak_name, str_palak_mobile, str_bla_name, str_bla_mobile, valuetype);
                    }
                }


            }
        });

    }

    private void SaveComitteeDatabase(String booth_name, String boothID, String str_adykash_name, String str_adykash_mobile, String str_palak_name, String str_palak_mobile, String str_bla_name, String str_bla_mobile, String valuetype) {
        Log.e("in Data Base ", "In Data BAse");

        try {

            boolean isSuccess = DBOperation.insertKametiMember(Activity_Comitee.this,
                    SavedData.getUSER_NAME(), boothID,
                    SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), str_adykash_name, str_adykash_mobile, str_palak_name, str_palak_mobile, str_bla_name, str_bla_mobile, valuetype);

            if (isSuccess) {
                Toast.makeText(Activity_Comitee.this, "" +
                        " Saved in Database", Toast.LENGTH_SHORT).show();
                Clear_Detail(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void SaveComitte(String booth_name, final String boothID, final String str_adykash_name, final String str_adykash_mobile, final String str_palak_name, final String str_palak_mobile, final String str_bla_name, final String str_bla_mobile, final boolean isNewEntry) {


        prg.setMessage("Saving ....");
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
                                Clear_Detail(true);
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if ((isNewEntry)) {
                    params.put("action", Utils_url.Action_ADDKameti_Member);

                } else {
                    params.put("action", Utils_url.Action_UPDATEKameti_Member);

                }
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", boothID);
                params.put("adyaksh_name", str_adykash_name);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());
                params.put("adyaksh_contact", str_adykash_mobile);

                params.put("palak_name", str_palak_name);
                params.put("palak_contact", str_palak_mobile);

                params.put("bla_name", str_bla_name);
                params.put("bla_contact", str_bla_mobile);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//


    }

    private void Clear_Detail(boolean reset) {
        edt_bla_mobile.setText("");
        edt_bla_name.setText("");
        edt_adyaksh_mobile.setText("");
        edt_adyaksh_name.setText("");
        edt_palak_mobile.setText("");
        edt_palak_name.setText("");
        edt_adyaksh_name.requestFocus();

        if (reset) {
            spinner_booth.setSelection(0);

        }

    }


    private boolean Check_validation(String string_mobile, String str_name, EditText edt_mobile, EditText edt_name) {

        if (!str_name.equalsIgnoreCase("") && string_mobile.equalsIgnoreCase("")) {
            edt_mobile.setError("Requried");
            return false;
        } else if (!string_mobile.equalsIgnoreCase("") && str_name.equalsIgnoreCase("")) {
            edt_name.setError("Requried");
            return false;
        } else if (!str_name.equalsIgnoreCase("") && string_mobile.length() != 10) {
            edt_mobile.setError("Please enter 10 digit number");
            return false;
        } else {
            return true;
        }
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

    private void createIDs() {
        prg = new ProgressDialog(this);

        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        button_submit = (Button) findViewById(R.id.button_submit);

        edt_bla_mobile = (EditText) findViewById(R.id.edt_bla_mobile);
        edt_bla_name = (EditText) findViewById(R.id.edt_bla_name);
        edt_adyaksh_mobile = (EditText) findViewById(R.id.edt_adyaksh_mobile);
        edt_adyaksh_name = (EditText) findViewById(R.id.edt_adyaksh_name);
        edt_palak_mobile = (EditText) findViewById(R.id.edt_palak_mobile);
        edt_palak_name = (EditText) findViewById(R.id.edt_palak_name);
        edt_palak_name = (EditText) findViewById(R.id.edt_palak_name);

        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(Activity_Comitee.this,
                R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);
        setLanguage();

    }

    private void setLanguage() {
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());

        txt_booth_bla = (TextView) findViewById(R.id.txt_booth_bla);
        txt_booth_palak = (TextView) findViewById(R.id.txt_booth_palak);
        txt_kameti_member = (TextView) findViewById(R.id.txt_kameti_member);
        txt_adhyaksh = (TextView) findViewById(R.id.txt_adhyaksh);

        txt_booth_bla.setText(SelectedLanguage[TextUtils.booth_bla]);
        txt_booth_palak.setText(SelectedLanguage[TextUtils.booth_palak]);
       // txt_kameti_member.setText(SelectedLanguage[TextUtils.kameti_member]);
        txt_adhyaksh.setText(SelectedLanguage[TextUtils.booth_adhyaksh]);


        til_bla_mobile = (TextInputLayout) findViewById(R.id.til_bla_mobile);
        til_bla_name = (TextInputLayout) findViewById(R.id.til_bla_name);
        til_palak_mobile = (TextInputLayout) findViewById(R.id.til_palak_mobile);
        til_palak_name = (TextInputLayout) findViewById(R.id.til_palak_name);
        til_adyaksh_mobile = (TextInputLayout) findViewById(R.id.til_adyaksh_mobile);
        til_adyaksh_name = (TextInputLayout) findViewById(R.id.til_adyaksh_name);

        til_bla_mobile.setHint(SelectedLanguage[TextUtils.bla_mobile]);
        til_bla_name.setHint(SelectedLanguage[TextUtils.bla_name]);
        til_palak_mobile.setHint(SelectedLanguage[TextUtils.palak_mobile]);
        til_palak_name.setHint(SelectedLanguage[TextUtils.palak_name]);
        til_adyaksh_mobile.setHint(SelectedLanguage[TextUtils.adyaksh_mobile]);
        til_adyaksh_name.setHint(SelectedLanguage[TextUtils.adyaksh_name]);
        button_submit.setText(SelectedLanguage[TextUtils.submit]);

    }
}

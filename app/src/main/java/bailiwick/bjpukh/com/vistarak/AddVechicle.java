package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
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
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.Support.CheckNetworkState;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 26-09-2017.
 */

public class AddVechicle extends RootActivity {
    ProgressDialog prg;
    ArrayAdapter<String> spinner_booth_adaptor;
    ArrayList<String> spinner_booth_array;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    private EditText edt_person_name, edt_vechile, edt_mobile;
    private Spinner spinner_booth;
    private Button button_submit;
    private TextView txt_total_bike;
    TextView mVehicle, mNameOfPerson, mContact, mVehicleNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());
        createIds();
        clickEvent();
        setSpinner_data();
        Spinne_Click();

    }

    private void Spinne_Click() {
        spinner_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals.get(position - 1).getBid());
                    getBike_count(boothSpinnerModals.get(position - 1).getBid());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getBike_count(final String bid) {


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
                                if (jsdata.has("bike_count")) {

                                    txt_total_bike.setText("Register Bikes Total " + jsdata.getString("bike_count"));
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
                params.put("action", Utils_url.Action_bike_count);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", bid);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }

    private void setSpinner_data() {
        boothSpinnerModals = DBOperation.getAllBoothList(this);
        if (boothSpinnerModals.size() > 0 && boothSpinnerModals != null) {
            Log.e("in Database Calling", "in Database Calling");

            for (int i = 0; i < boothSpinnerModals.size(); i++) {
//                spinner_booth_array.add((i + 1) + ". " + boothSpinnerModals.get(i).getBooth_name());
                spinner_booth_array.add("Booth No . " + boothSpinnerModals.get(i).getBooth_name());

            }
            spinner_booth_adaptor.notifyDataSetChanged();

        }
    }

    private void clickEvent() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String person_name, booth_name, contact, vechicle_no;
                person_name = edt_person_name.getText().toString().trim();
                contact = edt_mobile.getText().toString().trim();
                vechicle_no = edt_vechile.getText().toString().trim();

                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplication(), SelectedLanguage[TextUtils.select_booth], Toast.LENGTH_SHORT).show();
                    return;
                } else if (person_name.equalsIgnoreCase("")) {
                    edt_person_name.setError(SelectedLanguage[TextUtils.required]);
                    edt_person_name.requestFocus();
                    return;
                } else if (contact.equalsIgnoreCase("")) {
                    edt_mobile.setError(SelectedLanguage[TextUtils.required]);
                    edt_mobile.requestFocus();
                    return;
                } else if (contact.length() != 10) {
                    edt_mobile.setError(Html
                            .fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else if (vechicle_no.equalsIgnoreCase("")) {
                    edt_vechile.setError(SelectedLanguage[TextUtils.required]);
                    edt_vechile.requestFocus();
                    return;
                } else {
                    booth_name = spinner_booth.getSelectedItem().toString();
                    String boothID = boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid();

                    if (CheckNetworkState.isNetworkAvailable(AddVechicle.this)) {
                        SaveVechicle(booth_name, boothID, person_name, contact, vechicle_no);
                    } else {
                        SaveBikeDatabase(booth_name, boothID, person_name, contact, vechicle_no);
                    }
                }
            }
        });

    }

    private void SaveBikeDatabase(String booth_name, String boothID, String person_name, String contact, String vechicle_no) {
        Log.e("in Data Base ", "In Data BAse");

        try {

            boolean isSuccess = DBOperation.insertBike(AddVechicle.this,
                    SavedData.getUSER_NAME(), boothID, person_name,
                    SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), vechicle_no, contact);

            if (isSuccess) {
                Toast.makeText(AddVechicle.this, "Saved in Database", Toast.LENGTH_SHORT).show();
                Clear_Detail();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void SaveVechicle(String booth_name, final String boothID, final String person_name, final String contact, final String vechicle_no) {


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
                                txt_total_bike.setText("Register Bikes Total " + jsdata.getString("bike_count"));
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddVechicle);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", boothID);
                params.put("memberName", person_name);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());
                params.put("vehicle_no", vechicle_no);
                params.put("memberContact", contact);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void Clear_Detail() {
        edt_vechile.setText("");
        edt_person_name.setText("");
        edt_mobile.setText("");
        edt_person_name.requestFocus();
    }

    private void createIds() {
        prg = new ProgressDialog(this);
        edt_person_name = (EditText) findViewById(R.id.edt_person_name);
        edt_vechile = (EditText) findViewById(R.id.edt_vechile);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        button_submit = (Button) findViewById(R.id.button_submit);
        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        txt_total_bike = (TextView) findViewById(R.id.txt_total_bike);

        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();
        spinner_booth_adaptor = new ArrayAdapter<String>(AddVechicle.this,
                R.layout.spinner_layout, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout);

        spinner_booth_array.add("Select Booth");

        spinner_booth.setAdapter(spinner_booth_adaptor);

        setLanguage();
    }

    private void setLanguage() {
        mVehicle = (TextView) findViewById(R.id.text_vehicle);
        mNameOfPerson = (TextView) findViewById(R.id.name_of_person);
        mContact = (TextView) findViewById(R.id.text_contact);
        mVehicleNum = (TextView) findViewById(R.id.text_vehicle_number);

        mVehicle.setText(SelectedLanguage[TextUtils.vehicle]);
        mNameOfPerson.setText(SelectedLanguage[TextUtils.name_of_person]);
        mContact.setText(SelectedLanguage[TextUtils.mobile]);
        mVehicleNum.setText(SelectedLanguage[TextUtils.vehicle_number]);
        txt_total_bike.setText(SelectedLanguage[TextUtils.total_bike]);
        button_submit.setText(SelectedLanguage[TextUtils.submit]);

    }
}

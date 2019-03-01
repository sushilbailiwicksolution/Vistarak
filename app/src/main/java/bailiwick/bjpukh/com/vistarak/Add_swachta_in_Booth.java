package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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

public class Add_swachta_in_Booth extends RootActivity {
    private EditText edt_swachta_pramukh_number, edt_swachta_pramukh;

    private Button button_submit, button_add_member;
    private ProgressDialog prg;

    // Test delete it
    private FusedLocationProviderClient mFusedLocationClient;

    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;

    // Language
    TextView mSwachhtaInBooth, txt_member_count;
    TextInputLayout mSwachhtaPramukh, mMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swachta_in_booth);
        creadteIDS();
        ClickEvent();
//        getLocation();
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
                                if (jsdata.has("swachta_count")) {

                                    txt_member_count.setText("Register Member " + jsdata.getString("swachta_count"));
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
                params.put("action", Utils_url.Action_Swachta_count);
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

    private void getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("Longitude", "lon :" + location.getLongitude());
                            Log.e("getLatitude", "lat :" + location.getLatitude());


                        }
                    }
                });
    }

    private void ClickEvent() {
        button_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //              getLocation();
                Intent i = new Intent(Add_swachta_in_Booth.this, Add_new_Member.class);
                startActivity(i);
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String swachta_pramaukh, swachta_mobile;
                swachta_pramaukh = edt_swachta_pramukh.getText().toString().trim();
                swachta_mobile = edt_swachta_pramukh_number.getText().toString().trim();

                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Booth", Toast.LENGTH_LONG).show();
                    return;
                } else if (swachta_pramaukh.equalsIgnoreCase("")) {
                    edt_swachta_pramukh.setError(Html
                            .fromHtml("<font color='orange'>Please Enter Pramukh Name</font>"));
                    return;
                } else if (swachta_mobile.equalsIgnoreCase("")) {
                    edt_swachta_pramukh_number.setError(Html
                            .fromHtml("<font color='orange'>Please Enter Mobile No.</font>"));
                    return;
                } else if (swachta_mobile.length() != 10) {
                    edt_swachta_pramukh_number.setError(Html
                            .fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else {
                    if (CheckNetworkState.isNetworkAvailable(Add_swachta_in_Booth.this)) {
                        SaveSwachtaPramukh(swachta_pramaukh, swachta_mobile, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                    } else {
                        DataBaseSaveSwachtaPramukh(swachta_pramaukh, swachta_mobile, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                    }
                }
            }
        });
    }

    private void DataBaseSaveSwachtaPramukh(String swachta_pramaukh, String swachta_mobile, String booth_id, String user_name) {
        Log.e("in Data Base ", "In Data BAse");

        try {
            boolean isSuccess = DBOperation.insertSwachta_in_booth(Add_swachta_in_Booth.this,
                    SavedData.getUSER_NAME(), booth_id, swachta_pramaukh,
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

    private void SaveSwachtaPramukh(final String man_pramaukh, final String man_mobile, final String booth_id, String user_name) {

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
                            DataBaseSaveSwachtaPramukh(man_pramaukh, man_mobile, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());
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
                params.put("action", Utils_url.Action_AddSwachtaInBooth);
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
        edt_swachta_pramukh.setText("");
        edt_swachta_pramukh_number.setText("");
        edt_swachta_pramukh.requestFocus();
    }

    private void creadteIDS() {
        prg = new ProgressDialog(this);
        edt_swachta_pramukh_number = (EditText) findViewById(R.id.edt_swachta_pramukh_number);
        edt_swachta_pramukh = (EditText) findViewById(R.id.edt_swachta_pramukh);
        button_submit = (Button) findViewById(R.id.button_submit);
        button_add_member = (Button) findViewById(R.id.button_add_member);
        txt_member_count = (TextView) findViewById(R.id.txt_member_count);
        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(Add_swachta_in_Booth.this,
                R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);
        setLanguage();
    }

    private void setLanguage() {
        mSwachhtaInBooth = (TextView) findViewById(R.id.text_swachhta_in_booth);
        mSwachhtaPramukh = (TextInputLayout) findViewById(R.id.til_swachhta_pramukh);
        mMobile = (TextInputLayout) findViewById(R.id.til_contact_number);

        mSwachhtaInBooth.setText(SelectedLanguage[TextUtils.swachhta_in_booth]);
        mSwachhtaPramukh.setHint(SelectedLanguage[TextUtils.swachhta_pramukh]);
        mMobile.setHint(SelectedLanguage[TextUtils.mobile]);

        button_submit.setText(SelectedLanguage[TextUtils.submit]);
        button_add_member.setText(SelectedLanguage[TextUtils.add_member]);
    }
}
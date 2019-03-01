package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Dialog.CustomDialogmap;
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

public class Special_Area extends RootActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {
    private String TAG = "Location Address";
    EditText edt_area_name, edt_family_count;
    Button button_submit;
    ProgressDialog prg;
    FloatingActionButton current_location;
    boolean isCameraStopped = true;
    private GoogleMap mMap;
    //To store longitude and latitude from map
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private TextView location_address;

    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;

    FloatingActionButton flt_fullscreen;
    // Activity for result
    static final int REQUEST_CODE = 0;
    TextView mSpecialArea, mLocation;
    TextInputLayout mNameOfArea, mFamilyCount;


    RadioGroup rdgp_area;
    RadioButton rdb_sc, rdb_st;

    private TextView txt_member_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // ExceptionHandler.register(this, "http://uvcabs.esy.es/crash_log_vistarak/server.php");
        setContentView(R.layout.special_area);
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());
        createIDS();
        ClickEvent();
        MapActivity();
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
                                if (jsdata.has("specialArea_count")) {

                                    txt_member_count.setText("Register Member " + jsdata.getString("specialArea_count"));
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
                params.put("action", Utils_url.Action_SpecialArea_count);
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

    private void MapActivity() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void ClickEvent() {
        flt_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(Special_Area.this, CustomDialogmap.class), REQUEST_CODE);

            }
        });


        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FAB", "Clicked");
                getCurrentLocation();
                moveMap();
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area_name, family_count;
                area_name = edt_area_name.getText().toString().trim();
                family_count = edt_family_count.getText().toString().trim();

                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), SelectedLanguage[TextUtils.select_booth], Toast.LENGTH_LONG).show();
                    return;
                } else if (area_name.equalsIgnoreCase("")) {
                    edt_area_name.setError(Html
                            .fromHtml("<font color='orange'>Enter Area Type</font>"));
                    return;
                } else if (family_count.equalsIgnoreCase("")) {
                    edt_family_count.setError(Html
                            .fromHtml("<font color='orange'>No.of family in Area</font>"));
                    return;
                } else if (rdgp_area.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Special_Area.this, "Please Select Area Type SC or ST", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String radioText = "";
                    int selectedId = rdgp_area.getCheckedRadioButtonId();
                    if (selectedId == rdb_sc.getId()) {
                        radioText = "SC";
                    } else if (selectedId == rdb_st.getId()) {
                        radioText = "ST";
                    }
                    if (CheckNetworkState.isNetworkAvailable(Special_Area.this)) {

                        SaveSpecial_Area(area_name, family_count, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), radioText);
                    } else {
                        DatabASE_SaveSpecial_area(area_name, family_count, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), radioText);

                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Request", "" + REQUEST_CODE);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                String returnValue = data.getStringExtra("some_key");
                Log.e("Value", returnValue);
            }
        }
    }

    private void DatabASE_SaveSpecial_area(String area_name, String family_count, String booth_id, String user_name, String type) {
        Log.e("in Data Base ", "In Data BAse");

        try {

            boolean isSuccess = DBOperation.insertSpecial_area(Special_Area.this,
                    SavedData.getUSER_NAME(), booth_id, area_name,
                    "" + latitude, "" + longitude, family_count, type);

            if (isSuccess) {
                Toast.makeText(getApplicationContext(), "Saved in Database", Toast.LENGTH_SHORT).show();
                Clear_Detail();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void SaveSpecial_Area(final String area_name, final String family_count, final String booth_id, String user_name, final String radioText) {

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
                params.put("action", Utils_url.Action_AddSpecial_Area);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("priorCategory", area_name);
                params.put("latitude", "" + latitude);
                params.put("longitude", "" + longitude);
                params.put("no_of_famliy", family_count);
                params.put("type", radioText);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void Clear_Detail() {
        edt_area_name.setText("");
        edt_family_count.setText("");
        edt_area_name.requestFocus();
    }

    private void createIDS() {

        prg = new ProgressDialog(this);
        location_address = (TextView) findViewById(R.id.location_address);
        edt_area_name = (EditText) findViewById(R.id.edt_area_name);
        edt_family_count = (EditText) findViewById(R.id.edt_family_count);
        button_submit = (Button) findViewById(R.id.button_submit);
        current_location = (FloatingActionButton) findViewById(R.id.tracking_details);
        flt_fullscreen = (FloatingActionButton) findViewById(R.id.flt_fullscreen);

        txt_member_count = (TextView) findViewById(R.id.txt_member_count);

        rdgp_area = (RadioGroup) findViewById(R.id.rdgp_area);

        rdb_sc = (RadioButton) findViewById(R.id.rdb_sc);
        rdb_st = (RadioButton) findViewById(R.id.rdb_st);

        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);

        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(Special_Area.this,
                R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);

        setLanguage();

    }

    private void setLanguage() {
        mSpecialArea = (TextView) findViewById(R.id.text_special_area);
        mNameOfArea = (TextInputLayout) findViewById(R.id.til_name_of_area);
        mFamilyCount = (TextInputLayout) findViewById(R.id.til_family_count);

        mSpecialArea.setText(SelectedLanguage[TextUtils.special_area]);
        mNameOfArea.setHint(SelectedLanguage[TextUtils.name_of_area]);
        mFamilyCount.setHint(SelectedLanguage[TextUtils.family_count]);
        location_address.setText(SelectedLanguage[TextUtils.location]);
        button_submit.setText(SelectedLanguage[TextUtils.submit]);

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        // Check Zoom Function
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //    mMap.getUiSettings().setRotateGesturesEnabled(true);
        //  mMap.getUiSettings().setScrollGesturesEnabled(true);
        // mMap.getUiSettings().setTiltGesturesEnabled(true);

    }


    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", " + longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);
        Log.e("Prince", "Prince :" + latitude + " Lon " + longitude);

        String addess = getLocationAddress();
        location_address.setText("Location : " + addess);
        //Adding marker to map
        // mMap.addMarker(new MarkerOptions()
        //       .position(latLng) //setting position
        //     .draggable(true) //Making the marker draggable
        //   .title(addess)); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        //Displaying current coordinates in toast
        // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private String getLocationAddress() {
        String result = "Address Not Found";
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            Log.e(TAG, "Address");
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            result = address + " , " + city + " , " + state;
            Log.e(TAG, "Result : " + result);

            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            result = "Address Not Found";
        } finally {
            return result;
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == REASON_GESTURE) {
            Log.e("Camera", "Moving");
            isCameraStopped = !isCameraStopped;
        }
    }

    @Override
    public void onCameraIdle() {
        if (isCameraStopped) {
            Log.e("Camera", "Idle");
            latitude = mMap.getCameraPosition().target.latitude;
            longitude = mMap.getCameraPosition().target.longitude;
            moveMap();
            isCameraStopped = !isCameraStopped;
        }
    }
}
package bailiwick.bjpukh.com.vistarak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nullwire.trace.ExceptionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Firebase.MyFirebaseInstanceIDService;
import bailiwick.bjpukh.com.vistarak.Language.LanguageType;
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.LocationService.LocationService;
import bailiwick.bjpukh.com.vistarak.OTP.OtpScreen;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 01-09-2017.
 */

public class LoginActivity extends RootActivity {

    private Context context;

    // list of permissions
    public static final int REQUEST_READ_PHONE_STATE = 22;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Button btn_login;
    EditText edt_user_name, edt_password;
    TextInputLayout tinl_user_name, tinl_password;
    ProgressDialog prg;
    RequestQueue queue;
    Spinner spinner_Language;
    String[] arr_language;
    ArrayAdapter spinner_key_adapter;

    Intent locationService;
    boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        ExceptionHandler.register(this, "http://uvcabs.esy.es/crash_log_vistarak/server.php");

        checkAndRequestPermissions();
        checkPermission();
        prg = new ProgressDialog(this);
        createids();
        setLanguageSpinner();
        setLanguage(SavedData.getSelectedLang());
        click_evnet();

        Volley_Request();
    }

    private void setLanguageSpinner() {
        String compareVal = "Select Language";
        arr_language = getResources().getStringArray(R.array.Language);
        spinner_key_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout_white_text, arr_language);
        spinner_key_adapter.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_Language.setAdapter(spinner_key_adapter);

        if (!compareVal.equals(null)) {
            int spinner_position = spinner_key_adapter.getPosition(compareVal);
            spinner_Language.setSelection(spinner_position);
        }

    }

    private void sendTrackStatus(final String trackingType, final boolean isChecked) {

        prg.setMessage("Please Wait....");
        prg.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            prg.dismiss();
                            Log.e("Response : prince ", response);
                            JSONObject jsdata = new JSONObject(response);
                            String msg = jsdata.getString("msg");
                            String status = jsdata.getString("status");

                            if (status.equalsIgnoreCase("1")) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                if (isChecked) {
                                    SavedData.saveTrackingId(jsdata.getString("trackingId"));
                                    SavedData.saveLocationService(true);
                                    startLocationUpdates();
                                    Intent i = new Intent(LoginActivity.this, Booth_record.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    stopLocationUpdates();
                                    SavedData.saveLocationService(false);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            prg.dismiss();
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
                Map<String, String> params = new HashMap<>();
                params.put("action", Utils_url.Action_timeTracking);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("trackingType", trackingType);
                params.put("trackingTime", getCurrentTime());
                params.put("trackingId", SavedData.getTrackingId());
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void startLocationUpdates() {
        locationService.putExtra("mobile", "1234567890");
        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.startService(locationService);
    }

    private void stopLocationUpdates() {

        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.stopService(locationService);
    }

    protected String getCurrentTime() {
        Calendar calander = Calendar.getInstance();
        calander.getTime();
        int year = calander.get(Calendar.YEAR);
        int month = calander.get(Calendar.MONTH);
        int day = calander.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calander.getTime());
        ;
        return year + "-" + (month + 1) + "-" + day + " " + time;
    }

    private void click_evnet() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("I m here...", "i m heree,,,,");

                checkValidation();
                // Doctorhit();
            }
        });

        spinner_Language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isClicked) {
                    if (position > 0) {
                        setLanguage(position - 1);
                        SavedData.setSelectedLang(position - 1);
                    }
                } else {
                    isClicked = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void Doctorhit() {
        Log.e("prince", "new hit");

        JSONObject js = new JSONObject();
        try {
            js.put("email", "z@gmail.com");
            js.put("password", "z");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("MainActivity", js.toString());
        String url = "http://103.206.248.235:15000/doctorapi/docLogin";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response : prince ", response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("MainActivity", error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        AppController.getInstance().addToRequestQueue(req);

    }

    private void oldDoctorhit() {
        prg.setMessage("Login");
        prg.show();
        JSONObject js = new JSONObject();
        try {
            js.put("emailId", "mehra@indospirit.com");
            js.put("password", "123456");
            //js.put("syncsts",arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://61.246.34.205:8185/salesforceapi/loginRequest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            prg.dismiss();
                            Log.e("Response : prince ", response);
                            //  Manage_data_login(response);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("action", Utils_url.Action_Login);
                params.put("emailId", "mehra@indospirit.com");
                params.put("password", "123456");
                Log.e("Param Response ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void checkValidation() {
        String user_name, password;
        user_name = edt_user_name.getText().toString().trim();
        password = edt_password.getText().toString().trim();

        if (user_name.equalsIgnoreCase("")) {
            tinl_user_name.setError(SelectedLanguage[TextUtils.username]);

        } else if (password.equalsIgnoreCase("")) {
            tinl_password.setError(SelectedLanguage[TextUtils.password]);

        } else {
            Log.e("I m here...", "i m heree,,,,2");

            Log.e("I m here...", "i m heree,,,,3");
            String device_id = getDeviceDetail();
            Log.e("I m here...", "i m heree,,,,4");

            String emi_id = getEmi();
            Log.e("I m here...", "i m heree,,,,5");

            String fcmID = getFcmkey();
            Log.e("I m here...", "i m heree,,,,1");

            Log.e("I m here...", "i m heree,,,,1");

            Logintask(user_name, password, device_id, emi_id, fcmID);
        }
    }

    private void Logintask(final String user_name, final String password, final String device_id, final String emi_id, final String fcmid) {
Log.e("i m heree","in login entry");

        prg.setMessage("Login");
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
                                String user_name = jsdata.getString("user_name");
                                String full_name = jsdata.getString("full_name");
                                String address = jsdata.getString("address");
                                String profile_pic = jsdata.getString("profile_pic");
                                String mobile1 = jsdata.getString("mobile1");
                                String mobile2 = jsdata.getString("mobile2");
                                String note = jsdata.getString("note");
                                //  SavedData.saveLogin(true);
                                SavedData.saveUSER_NAME(user_name);
                                SavedData.saveFULL_NAME(full_name);
                                SavedData.saveADDRESS(address);
                                SavedData.savePROFILE_PIC(profile_pic);
                                SavedData.saveMOBILE1(mobile1);
                                SavedData.saveMOBILE2(mobile2);

                                String leaveDetails = jsdata.getString("leaveDetails");
                                //   leaveDetails="0";
                                if (msg.equalsIgnoreCase("Login Successfully")) {
                                    SavedData.saveLeaveStatus(true);
//                                    Intent i = new Intent(LoginActivity.this, Booth_record.class);
                                    //    SavedData.saveLogin(true);
                                    Intent i = new Intent(LoginActivity.this, OtpScreen.class);
                                    startActivity(i);
                                    finish();
                                    Log.e("Leave", "true");
                                } else {
                                    SavedData.saveLeaveStatus(false);
                                    sendTrackStatus("start", true);
                                    Log.e("Leave", "false");
                                }
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
                Map<String, String> params = new HashMap<>();
                params.put("action", Utils_url.Action_Login);
                params.put("userName", user_name);
                params.put("password", password);
                params.put("cloudId", fcmid);
                params.put("deviceId", device_id);
                params.put("imeiNumber", "Imei");
           //     Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String getFcmkey() {
        Log.e("I m here...", "fcm");

        MyFirebaseInstanceIDService myfcm = new MyFirebaseInstanceIDService();
        String fcmtoken = myfcm.getFcm();
        Log.e("fcm id", fcmtoken);
        if (fcmtoken.isEmpty()) {
            //getFcmkey();
      //      Log.e("Fcm repeat", "empty : " + fcmtoken);
        }
      //  Log.e("I m here...", "fcm" + fcmtoken);

        return fcmtoken;
        //   DB_Function.SavetextFile(Booth_record.this, fcmtoken);
    }

    private String getDeviceDetail() {
        @SuppressLint("HardwareIds")
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        return deviceID;
    }

    @SuppressLint("HardwareIds")
    private String getEmi() {
        String identifier = "";

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm != null)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;


            }
      //  identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        return identifier;
    }

    private void createids() {
        locationService = new Intent(LoginActivity.this, LocationService.class);
        btn_login = (Button) findViewById(R.id.button_login_register);
        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        edt_password = (EditText) findViewById(R.id.edt_password);
        tinl_user_name = (TextInputLayout) findViewById(R.id.tinl_user_name);
        tinl_password = (TextInputLayout) findViewById(R.id.tinl_password);
        spinner_Language = (Spinner) findViewById(R.id.spinner_select_language);

     //  edt_user_name.setText("VISTARAK29026");
       // edt_password.setText("12345678");

        //9718800629


    }

    private void setLanguage(int lang) {

        switch (lang) {
            case LanguageType.ENGLISH:
                SendArray(R.array.English);
                break;
            case LanguageType.HINDI:
                SendArray(R.array.Hindi);

                break;
            case LanguageType.BANGLA:
                SendArray(R.array.Bangla);

                break;
            default:
                SendArray(R.array.English);
                break;
        }

    }

    private void SendArray(int LanguageArray) {

        SavedData.setLangArray(LanguageArray);
        SelectedLanguage = getResources().getStringArray(LanguageArray);
        tinl_user_name.setHint(SelectedLanguage[TextUtils.username]);
        tinl_password.setHint(SelectedLanguage[TextUtils.password]);
        btn_login.setText(SelectedLanguage[TextUtils.sign_in]);


    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private boolean checkAndRequestPermissions() {

        int readSMSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
     //   int readPhoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int readLocationCoarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int Location_fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int External_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
//
//        if (readSMSPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
//        }
//        if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
//        }
        if (External_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (readLocationCoarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (Location_fine != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("SplashScreen", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
              //  perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

          //      perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if ( perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("SplashScreen", "sms & location services permission granted");
                        // process the normal flow

                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("SplashScreen", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (
                                 ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                ) {

                            showDialogOK("Enable Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });

                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            showMessageOKCancel("Enable permissions to access application!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", LoginActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        LoginActivity.this.startActivity(intent);
                    }
                })
                .setNegativeButton
                        ("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                            }
                        })
                .create()
                .show();

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    //**********************************************************************************************************************************
    private void Volley_Request() {
        queue = Volley.newRequestQueue(this);
    }
}

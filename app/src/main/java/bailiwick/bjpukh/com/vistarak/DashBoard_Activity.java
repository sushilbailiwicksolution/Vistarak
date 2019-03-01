package bailiwick.bjpukh.com.vistarak;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nullwire.trace.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Dialog.CustomComplaintDialog;
import bailiwick.bjpukh.com.vistarak.Dialog.CustomDialogLeave;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothSpinnerModal;
import bailiwick.bjpukh.com.vistarak.Language.LanguageType;
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.LocationService.LocationService;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;


/**
 * Created by Prince on 02-09-2017.
 */

public class DashBoard_Activity extends RootActivity implements DatePickerDialog.OnDateSetListener {
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    final Calendar c = Calendar.getInstance();
    String TAG = getClass().getSimpleName();
    CardView crd_booth_visit, crd_home_visit, crd_meeting_in_booth, key_voter_group, crd_download, crd_special_area, crd_wts_app_group, crd_swachta, crd_man_ki_baat, crd_log_out, crd_pradhan, crd_cometiee, crd_smart_user, crd_anusuchit, crd_event_president, crd_comitee_21;

    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog datePickerDialog;

    //  TextView tracking_details;
    Location mCurrentLocation;
    String mLastUpdateTime;
    Intent locationService;
    Spinner spinner_select_booth, spinner_select_language;
    ArrayAdapter<String> spinner_booth_adaptor;
    ArrayList<String> spinner_booth_array;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ProgressDialog prg;
    CustomDialogLeave dialog;
    CustomComplaintDialog complaintDial;
    boolean isFrom = true, isDoneProgramiically = false;
    AlertDialog.Builder trackingBuilder, stopTrackingBuilder;
    AlertDialog trakingDialog, stopTrackingDialog;
    private Button btn_leave, btn_complaint;
    private ImageView img_profile_Image;


    TextView txt_user_name, mNewMember, mHomeVisit, mBoothMeeting, mManKiBat, mKeyVotGroup, mDownload, mScArea, mWhatsAppGroup, mSwachhtainBooth, mMotorCycle, txt_smart_phone_user, txt_booth_kameti, txt_anusuchit;

    String[] arr_language;
    ArrayAdapter spinner_key_adapter;
    boolean isClicked = false;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get extra data included in the Intent
            Log.e("i m in Broadcast recive", "i m in Broadcast recive");
            Double latitude = intent.getDoubleExtra("latitude", 0.0);
            Double longitude = intent.getDoubleExtra("longitude", 0.0);
            int pendingPoints = intent.getIntExtra("pendingPoints", 0);
            String timeStamp = intent.getStringExtra("lastUpdated");
            String message = intent.getStringExtra("response");

            String location = "Hi Your Last Location is \n" + latitude + " - " + longitude + "\nUpdated On -" + timeStamp;

            Log.e(TAG, "Saving ..  " + location);
            //  SavedData.saveLastLocation(location);
            // pref.edit().putInt(Utils_prefrences.pref_pendingPoints, pendingPoints).commit();
            //pref.edit().putString(Utils_prefrences.pref_timeStamp, timeStamp).commit();
            //pref.edit().putString(Utils_prefrences.pref_message, message).commit();
            boolean isBulkUpload = intent.getBooleanExtra("isBulkUpload", false);

            Log.e(TAG, "Message:  " + message);
            Log.e(TAG, "Last Updated On: " + timeStamp);
            Log.e(TAG, "Pending Points: " + pendingPoints);
            Log.e(TAG, "Updating Details: \n" + latitude + " / " + longitude);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionHandler.register(this, "http://uvcabs.esy.es/crash_log_vistarak/server.php");
        setContentView(R.layout.new_dashboard);
        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
/*        if (!isGooglePlayServicesAvailable()) {
            finish();
        }*/
        // createLocationRequest();
        reciveMessage();
        creatIDS();
        click_Event();
        setdetail();
        setLanguageSpinner();
        setLanguage();
        getBoothFromDB();
        Spinner_click();

    }

    private void Spinner_click() {
        spinner_select_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String booth_id = boothSpinnerModals.get(position).getBid();
                SavedData.saveBOOTH_ID(booth_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_select_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isClicked) {
                    if (position > 0) {
                        ChangeLanguage(position - 1);
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

    private void ChangeLanguage(int lang) {


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
        setLanguage();
    }

    private void setdetail() {
        txt_user_name.setText(SavedData.getFULL_NAME());

        if (SavedData.isLocationServiceStarted()) {
            // start_location_updates.setText("Stop");
            // here again starting the service if by chance it is stopped
            // stopLocationUpdates();
            // startLocationUpdates();
        } else {
            // start_location_updates.setText("Start");
            //here stopping the location service if it is still running
            // stopLocationUpdates();
        }

    }

    private void getBoothFromDB() {
        boothSpinnerModals = DBOperation.getAllBoothList(this);
        if (boothSpinnerModals.size() > 0 && boothSpinnerModals != null) {
            Log.e("in Database Calling", "in Database Calling");

            for (int i = 0; i < boothSpinnerModals.size(); i++) {
                spinner_booth_array.add("Booth No . " + boothSpinnerModals.get(i).getBooth_name());

            }
            spinner_booth_adaptor.notifyDataSetChanged();
            SavedData.saveBOOTH_ID(boothSpinnerModals.get(0).getBid());

        } else {
            Log.e("Have to get from server", "Have to get from server");
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

                    for (int i = 0; i < boothArray.length(); i++) {

                        boothSpinnerModals.add(new BoothSpinnerModal(boothArray.getJSONObject(i).getString("bid"), boothArray.getJSONObject(i).getString("booth_name"), boothArray.getJSONObject(i).getString("consistency_name")));
                        spinner_booth_array.add((i + 1) + ". " + boothArray.getJSONObject(i).getString("booth_name"));
                        SavedData.saveBOOTH_ID(boothSpinnerModals.get(0).getBid());
                        boolean isSuccess = DBOperation.insertBooth(DashBoard_Activity.this, boothArray.getJSONObject(i).getString("bid"), boothArray.getJSONObject(i).getString("booth_name"), boothArray.getJSONObject(i).getString("consistency_name"));

                    }
                    Log.e("check 2", "" + SavedData.isDBupdated());
                    SavedData.setisDbUpated(true);
                    Log.e("check 3", "" + SavedData.isDBupdated());


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

    private void startLocationUpdates() {
        locationService.putExtra("mobile", "1234567890");
        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.startService(locationService);
    }

    private void stopLocationUpdates() {

        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.stopService(locationService);
    }

    private void reciveMessage() {
        registerReceiver(mMessageReceiver, new IntentFilter("com.pycitup.BroadcastReceiver"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }

    private void click_Event() {
        img_profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationService loc = new LocationService();
                Log.e("Latitude", "" + loc.mLastLocation.getLatitude());
                Log.e("Longitude", "" + loc.mLastLocation.getLatitude());

            }
        });


        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowLeaveDialog();
            }
        });

        btn_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowComplaintDialog();
            }
        });
        crd_comitee_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Activity_ComiteeTWO_One.class);
                startActivity(i);

            }
        });
        crd_pradhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, AddPradhan.class);
                startActivity(i);


            }
        });

        crd_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CustomDialogExit cstEXIT = new CustomDialogExit(DashBoard_Activity.this);
                cstEXIT.show();*/
                //Logout();
                Intent i = new Intent(DashBoard_Activity.this, AddVechicle.class);
                startActivity(i);


            }
        });
        crd_smart_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CustomDialogExit cstEXIT = new CustomDialogExit(DashBoard_Activity.this);
                cstEXIT.show();*/
                //Logout();
                Intent i = new Intent(DashBoard_Activity.this, Activity_Smart_user.class);
                startActivity(i);


            }
        });
        crd_cometiee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CustomDialogExit cstEXIT = new CustomDialogExit(DashBoard_Activity.this);
                cstEXIT.show();*/
                //Logout();
                Intent i = new Intent(DashBoard_Activity.this, Activity_Comitee.class);
                startActivity(i);


            }
        });
     /*   start_location_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Start Button is CLicked");
                if (SavedData.isLocationServiceStarted()) {

                    stopTrackingDialog.show();
                } else {
                    //todo here we need to call start status tracking api

                    startLocationUpdates();
                    SavedData.saveLocationService(true);
                    start_location_updates.setText("Stop");
                }
            }
        });*/
        crd_booth_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Add_new_Member.class);
                startActivity(i);

            }
        });
        crd_home_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, HomeVisit.class);
                startActivity(i);
            }
        });
        crd_meeting_in_booth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Add_meeting_in_Booth.class);
                startActivity(i);

            }
        });
        crd_swachta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Add_swachta_in_Booth.class);
                startActivity(i);

            }
        });
        crd_man_ki_baat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Add_man_ki_baat.class);
                startActivity(i);

            }
        });

        key_voter_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Add_Key_Voter.class);
                startActivity(i);

            }
        });
        crd_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Download_Activity.class);
                startActivity(i);

            }
        });

        crd_special_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Special_Area.class);
                startActivity(i);

            }
        });
        crd_wts_app_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, WhatsApp_Group.class);
                startActivity(i);

            }
        });
        crd_anusuchit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, AnusuchitReg.class);
                startActivity(i);

            }
        });
        crd_event_president.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Event_Pradhan.class);
                startActivity(i);

            }
        });
        /*crd_benificary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, Benificary.class);
                startActivity(i);

            }
        });
 */
    }

    private void ShowComplaintDialog() {

        complaintDial.show();
        complaintDial.btn_apply.setText("Submit");
        complaintDial.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintDial.dismiss();
            }
        });
        complaintDial.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_text, from_date, to_date;
                message_text = complaintDial.edt_reason.getText().toString().trim();
                from_date = complaintDial.txt_From.getText().toString().trim();
                to_date = complaintDial.txt_To.getText().toString().trim();

                if (message_text.equalsIgnoreCase("")) {
                    complaintDial.edt_reason.setError(Html.fromHtml("<font color='orange'>Please Enter your suggestion</font>"));
                    return;
                }
                /* Abhishek uncomment for leave
                else if (from_date.equalsIgnoreCase("From")) {
                    Toast.makeText(getApplicationContext(), "Please Select leave Date From", Toast.LENGTH_LONG).show();
                    return;
                } else if (to_date.equalsIgnoreCase("To")) {
                    Toast.makeText(getApplicationContext(), "Please Select leave Date To", Toast.LENGTH_LONG).show();
                    return;
                }

                */
                else {
                    // Apply_Leave(message_text, from_date, to_date);
                    sendComplaint(message_text);

                }
            }
        });
    }


    private void Logout() {
        if (SavedData.getLeaveStatus()) {
            stopLocationUpdates();
            SavedData.saveLocationService(false);
            boolean isSucess = DBOperation.deleteAll_Booth(DashBoard_Activity.this);
            Log.e("isSucess", "Deleted : " + isSucess);

            Intent i = new Intent(DashBoard_Activity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            stopLocationUpdates();
            startActivity(i);
            finish();
            SavedData.ClearAll();
        } else {
            sendTrackStatus("stop", false);
        }
    }

    private void sendTrackStatus(final String trackingType, final boolean isChecked) {


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
                        if (isChecked) {
                            SavedData.saveTrackingId(jsdata.getString("trackingId"));
                            startLocationUpdates();
                            SavedData.saveLocationService(true);
                        } else {
                            stopLocationUpdates();
                            SavedData.saveLocationService(false);
                            boolean isSucess = DBOperation.deleteAll_Booth(DashBoard_Activity.this);
                            Log.e("isSucess", "Deleted : " + isSucess);

                            Intent i = new Intent(DashBoard_Activity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            stopLocationUpdates();
                            startActivity(i);
                            finish();
                            SavedData.ClearAll();
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        isDoneProgramiically = true;
                        // start_location_updates.setChecked(!isChecked);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    isDoneProgramiically = true;
                    // start_location_updates.setChecked(!isChecked);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
                isDoneProgramiically = true;
                // start_location_updates.setChecked(!isChecked);
                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    protected String getCurrentTime() {
        Calendar calander = Calendar.getInstance();
        calander.getTime();
        int year = calander.get(Calendar.YEAR);
        int month = calander.get(Calendar.MONTH);
        int day = calander.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calander.getTime());
        ;
        return year + "-" + (month + 1) + "-" + day + " " + time;
    }

    private void ShowLeaveDialog() {

        dialog.show();
        dialog.txt_From.setText("From");
        dialog.txt_To.setText("To");
        dialog.edt_reason.setText("");

        dialog.txt_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom = true;
                ShowDatePicker();
            }
        });

        dialog.txt_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom = false;
                ShowDatePicker();
            }
        });

        dialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_text, from_date, to_date;
                message_text = dialog.edt_reason.getText().toString().trim();
                from_date = dialog.txt_From.getText().toString().trim();
                to_date = dialog.txt_To.getText().toString().trim();

                if (message_text.equalsIgnoreCase("")) {
                    dialog.edt_reason.setError(Html.fromHtml("<font color='orange'>Please Enter Message</font>"));
                    return;
                }
                /* Abhishek uncomment for leave
                else if (from_date.equalsIgnoreCase("From")) {
                    Toast.makeText(getApplicationContext(), "Please Select leave Date From", Toast.LENGTH_LONG).show();
                    return;
                } else if (to_date.equalsIgnoreCase("To")) {
                    Toast.makeText(getApplicationContext(), "Please Select leave Date To", Toast.LENGTH_LONG).show();
                    return;
                }

                */
                else {
                    // Apply_Leave(message_text, from_date, to_date);
                    sendMessage(message_text);

                }
            }
        });
    }

    private void sendComplaint(final String message_text) {

        prg.setMessage("Sending Message....");
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
                        complaintDial.dismiss();
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
                params.put("action", Utils_url.Action_SendComplaint);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("complaint_msg", message_text);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }

    private void sendMessage(final String message_text) {
        prg.setMessage("Sending Message....");
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
                        dialog.dismiss();
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
                params.put("action", Utils_url.Action_SendMssage);
                params.put("userName", SavedData.getUSER_NAME());
                params.put("message", message_text);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }

    private void Apply_Leave(final String reason, final String from_date, final String to_date) {
        prg.setMessage("Apply Leave....");
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
                        dialog.dismiss();
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
                params.put("action", Utils_url.Action_AddLeave);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("leave_reason", reason);
                params.put("to_date", to_date);
                params.put("from_date", from_date);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void ShowDatePicker() {
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void creatIDS() {
        locationService = new Intent(DashBoard_Activity.this, LocationService.class);

        btn_leave = (Button) findViewById(R.id.btn_leave);
        btn_complaint = (Button) findViewById(R.id.btn_complaint);
        dialog = new CustomDialogLeave(this);
        complaintDial = new CustomComplaintDialog(this);
        datePickerDialog = new DatePickerDialog(this, this, year, month, day);

        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        // tracking_details = (TextView) findViewById(R.id.tracking_details);
        prg = new ProgressDialog(this);
        crd_booth_visit = (CardView) findViewById(R.id.crd_booth_visit);
        crd_home_visit = (CardView) findViewById(R.id.crd_home_visit);

        crd_meeting_in_booth = (CardView) findViewById(R.id.crd_meeting_in_booth);
//        crd_benificary = (CardView) findViewById(R.id.crd_benificary);

        key_voter_group = (CardView) findViewById(R.id.key_voter_group);
        crd_download = (CardView) findViewById(R.id.crd_download);
        crd_special_area = (CardView) findViewById(R.id.crd_special_area);
        crd_wts_app_group = (CardView) findViewById(R.id.crd_wts_app_group);
        crd_swachta = (CardView) findViewById(R.id.crd_swachta);
        crd_man_ki_baat = (CardView) findViewById(R.id.crd_man_ki_baat);
        crd_log_out = (CardView) findViewById(R.id.crd_log_out);
        crd_pradhan = (CardView) findViewById(R.id.crd_pradhan);
        crd_cometiee = (CardView) findViewById(R.id.crd_cometiee);
        crd_smart_user = (CardView) findViewById(R.id.crd_smart_user);
        crd_anusuchit = (CardView) findViewById(R.id.crd_anusuchit);
        crd_event_president = (CardView) findViewById(R.id.crd_event_president);

        crd_comitee_21 = (CardView) findViewById(R.id.crd_comitee_21);

        img_profile_Image = (ImageView) findViewById(R.id.img_profile_Image);
        spinner_select_language = (Spinner) findViewById(R.id.spinner_select_language);


        spinner_select_booth = (Spinner) findViewById(R.id.spinner_select_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();
        spinner_booth_adaptor = new ArrayAdapter<String>(DashBoard_Activity.this, R.layout.spinner_layout, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout);
        spinner_select_booth.setAdapter(spinner_booth_adaptor);
        //   pref = PreferenceManager.getDefaultSharedPreferences(this);
        //  locationService = new Intent(getBaseContext(), LocationService.class);


        trackingBuilder = new AlertDialog.Builder(this);
        stopTrackingBuilder = new AlertDialog.Builder(this);

        trackingBuilder.setMessage("Tracking Details").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //finish();
            }
        });

        stopTrackingBuilder.setMessage("Stop Tracking").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //todo here we need to call stop tracking api
                //  stopLocationUpdates();
                SavedData.saveLocationService(false);
                isDoneProgramiically = true;
                //start_location_updates.setChecked(false);

                //   sendTrackStatus("stop", false);

                //  start_location_updates.setText("Start");
                //finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isDoneProgramiically = true;
                //         start_location_updates.setChecked(true);
            }
        });
        stopTrackingDialog = stopTrackingBuilder.create();
        trakingDialog = trackingBuilder.create();
        setLanguage();
    }

    private void setLanguageSpinner() {
        String compareVal = "Select Language";
        arr_language = getResources().getStringArray(R.array.Language);
        spinner_key_adapter = new ArrayAdapter<>(DashBoard_Activity.this, R.layout.spinner_layout, arr_language);
        spinner_key_adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_select_language.setAdapter(spinner_key_adapter);

        if (!compareVal.equals(null)) {
            int spinner_position = spinner_key_adapter.getPosition(compareVal);
            spinner_select_language.setSelection(spinner_position);
        }


    }

    private void setLanguage() {
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());
        mNewMember = (TextView) findViewById(R.id.new_member);
        mHomeVisit = (TextView) findViewById(R.id.home_visit);
        mBoothMeeting = (TextView) findViewById(R.id.tv_booth_meeting);
        mManKiBat = (TextView) findViewById(R.id.tv_man_ki_bat);
        mKeyVotGroup = (TextView) findViewById(R.id.tv_key_voter_group);
        mDownload = (TextView) findViewById(R.id.tv_downloads);
        mScArea = (TextView) findViewById(R.id.tv_sc_area);
        mWhatsAppGroup = (TextView) findViewById(R.id.tv_whatsapp_area);
        mSwachhtainBooth = (TextView) findViewById(R.id.tv_swachhta_in_booth);
        mMotorCycle = (TextView) findViewById(R.id.tv_motor_cycle_in_booth);
        txt_booth_kameti = (TextView) findViewById(R.id.txt_booth_kameti);
        txt_smart_phone_user = (TextView) findViewById(R.id.txt_smart_phone_user);

        btn_leave.setText(SelectedLanguage[TextUtils.write_message]);

        mNewMember.setText(SelectedLanguage[TextUtils.new_member]);
        mHomeVisit.setText(SelectedLanguage[TextUtils.home_visit]);
        mBoothMeeting.setText(SelectedLanguage[TextUtils.booth_meeting]);
        mManKiBat.setText(SelectedLanguage[TextUtils.man_ki_baat]);
        mKeyVotGroup.setText(SelectedLanguage[TextUtils.key_voter_group]);
        mDownload.setText(SelectedLanguage[TextUtils.download]);
        mScArea.setText(SelectedLanguage[TextUtils.special_area]);
        mWhatsAppGroup.setText(SelectedLanguage[TextUtils.whatsapp_group]);
        mSwachhtainBooth.setText(SelectedLanguage[TextUtils.swachhta_in_booth]);
        mMotorCycle.setText(SelectedLanguage[TextUtils.motorcycle_in_booth]);

        txt_booth_kameti.setText(SelectedLanguage[TextUtils.kameti_member]);
        txt_smart_phone_user.setText(SelectedLanguage[TextUtils.smart_phone_user]);

    }


    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            Log.e(TAG, "At Time: " + mLastUpdateTime + "\n" + "Latitude: " + lat + "\n" + "Longitude: " + lng + "\n" + "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" + "Provider: " + mCurrentLocation.getProvider());
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (isFrom) {
            dialog.txt_From.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

        } else {
            dialog.txt_To.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
        }
    }
}

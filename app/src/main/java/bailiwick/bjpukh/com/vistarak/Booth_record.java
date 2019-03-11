package bailiwick.bjpukh.com.vistarak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.iceteck.silicompressorr.SiliCompressor;
import com.nullwire.trace.ExceptionHandler;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Dialog.CustomDialog_Update;
import bailiwick.bjpukh.com.vistarak.Dialog.LvCancelDialog;
import bailiwick.bjpukh.com.vistarak.Firebase.MyFirebaseInstanceIDService;
import bailiwick.bjpukh.com.vistarak.Firebase.WakeLocker;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothSpinnerModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.District_model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Level_model;
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.LocationService.LocationService;
import bailiwick.bjpukh.com.vistarak.Support.DeviceOperation;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Itag;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;
import bailiwick.bjpukh.com.vistarak.db.DB_Function;
import bailiwick.bjpukh.com.vistarak.db.Database_Utils;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 03-09-2017.
 */

public class Booth_record extends RootActivity {
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    LinearLayout lnt_dashboard;
    TextView txt_dashboard, txt_key_voter, txt_Man_ki_baat, txt_special_area, txt_new_member, txt_user_name;
    TextView mNewMember, mSpecialArea, mManKiBat, mKeyVoter;
    String STR_user_id;
    ProgressDialog prg;
    CardView crd_man, crd_booth_visit;
    FloatingActionButton tracking_details;
    AlertDialog.Builder trackingBuilder, stopTrackingBuilder;
    AlertDialog trakingDialog, stopTrackingDialog;
    // Image upload
    Bitmap bitmap;
    String myBase64Image;
    Uri mCropImageUri;
    boolean isprofileupload = false;
    Intent locationService;
    private ImageView image_profile;
    CustomDialog_Update pDialog;
    String deviceID = "";
    boolean backpressed;
    private Button btn_expensive;

    ArrayList<District_model> DistrictModals;
    ArrayList<Level_model> LevelModals;

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionHandler.register(this, "http://uvcabs.esy.es/crash_log_vistarak/server.php");
        setContentView(R.layout.new_booth);
        createIDS();
        click_Event();
        STR_user_id = SavedData.getUSER_NAME();
        profile_click();
        update_profile();
        getFcmkey();
        GetNotifyMessage();
        registerReceiver(mHandlerReciver, new IntentFilter(Itag.DISPLAY_MESSAGE_ACTION));
        SavedData.saveNotify(true);
        getDistrictDB();

    }

    private void GetNotifyMessage() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ShowNotifyDialog(extras.getString(Itag.FCM_Message));

//            Toast.makeText(getApplication(), extras.getString("fcmMessage"), Toast.LENGTH_SHORT).show();
        }
    }


    private void getFcmkey() {
        MyFirebaseInstanceIDService myfcm = new MyFirebaseInstanceIDService();
        String fcmtoken = myfcm.getFcm();
        Log.e("fcm id", fcmtoken);
        if (fcmtoken.isEmpty()) {
            getFcmkey();
            Log.e("Fcm repeat", "empty : " + fcmtoken);
        }
        //  DB_Function.SavetextFile(Booth_record.this, fcmtoken);
    }


    private void update_profile() {
        if (!SavedData.getPROFILE_PIC().equalsIgnoreCase("")) {

            Picasso.with(this).load(SavedData.getPROFILE_PIC()).placeholder(R.drawable.profile_default).error(R.drawable.profile_default).into(image_profile);

        }
        txt_user_name.setText(SavedData.getFULL_NAME());


    }

    private void profile_click() {
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_pic_update();
            }
        });
    }

    private void profile_pic_update() {
        CropImage.startPickImageActivity(Booth_record.this);

    }

    private void UploadImage(final String meetingId) {
        Log.e("i m heree", "i 111");
        prg.setMessage("Upload Image...");
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
                        String profile_pic = jsdata.getString("profile_pic");
                        SavedData.savePROFILE_PIC(profile_pic);
                        update_profile();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Pic upload failed please try again later", Toast.LENGTH_LONG).show();

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
                params.put("action", Utils_url.Action_UpdateProfile_user);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("image", "data:image/jpeg;base64," + myBase64Image);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void getRecord(final String str_user_id, final String device_id, final String emi_id, final String fcmid) {

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

                    String status = jsdata.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        String total_member = jsdata.getString("booth_meeting");
                        String special_area_count = jsdata.getString("motor_cycle_count");
                        String man_ki_bat_count = jsdata.getString("man_ki_bat_count");
                        String key_voter_count = jsdata.getString("whatsapp_group");
                        String version_code = jsdata.getString("appVersion");

                        String db_version = SavedData.getDBversion();
                        String database_version_code = jsdata.getString("data_update_version");

                        if (!db_version.equalsIgnoreCase(database_version_code)) {
                            SavedData.setDBversion(database_version_code);
                            SavedData.setisDbUpated(false);
                            //here i have to clear database
                            Log.e("check 6", "" + database_version_code);

                            Log.e("check 7", "" + SavedData.getDBversion());
                            Log.e("check 8", "" + SavedData.isDBupdated());

                            Log.e("clear db ", "clear db ");

                        }

                        SavedData.savePROFILE_PIC(jsdata.getString("profile_pic"));
                        SavedData.saveFULL_NAME(jsdata.getString("full_name"));
                        SavedData.saveMOBILE1(jsdata.getString("mobile1"));
                        SavedData.saveMOBILE2(jsdata.getString("mobile2"));

                        txt_key_voter.setText(key_voter_count);
                        txt_Man_ki_baat.setText(man_ki_bat_count);
                        txt_special_area.setText(special_area_count);
                        txt_new_member.setText(total_member);
                        ArrayList<BoothSpinnerModal> boothSpinnerModals = new ArrayList<>();
                        //  boothSpinnerModals = DBOperation.getAllBoothList(Booth_record.this);
                              /*  if (boothSpinnerModals.size() == 0) {
                                 //   getBoothFromServer();

                                }*/

                        String leaveDetails = jsdata.getString("leaveDetails");
                        if (leaveDetails.equalsIgnoreCase("1")) {
                            SavedData.saveLeaveStatus(true);
                            sendTrackStatus("start", true);
                            Log.e("Leave", "true");
                        } else {
                            SavedData.saveLeaveStatus(false);
                            sendTrackStatus("start", true);
                            Log.e("Leave", "false");
                        }
                        Log.e("version code", version_code + "  in app  " + Utils_url.Version_Code);
                        if (!version_code.equalsIgnoreCase(Utils_url.Version_Code)) {
                            ShowUpdateDialog();
                        }
                    } else {
                        String msg = jsdata.getString("msg");
                        //         Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                    update_profile();
                    Log.e("check 1", "" + SavedData.isDBupdated());
                    if (!SavedData.isDBupdated()) {
                        clearBooth();
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
                params.put("action", Utils_url.Action_MYRecord);
                params.put("vistarak_id", str_user_id);

                params.put("cloudId", fcmid);
                //   params.put("deviceId", device_id);
                params.put("imeiNumber", "pp");
                Log.e("Param Abhishek pp", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void clearBooth() {
        String booth_size[] = DBOperation.getAllBooth(this);
        Log.e("prince booth size", "" + booth_size.length);
        DBOperation.deleteAll_Booth(this);
        String booth_size_now[] = DBOperation.getAllBooth(this);
        Log.e("prince booth size  now", "" + booth_size_now.length);

    }

    private void ShowUpdateDialog() {
        final CustomDialog_Update dialog = new CustomDialog_Update(this);
        dialog.setCancelable(false);
        dialog.show();
        dialog.txt_message.setText("Please Update the App");
        dialog.btn_cancel.setVisibility(View.GONE);
        dialog.btn_apply.setText("Update");
        dialog.popup_txt_title.setText("Vistarak Alert...!!!");
        dialog.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
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
                            SavedData.saveLocationService(true);
                            startLocationUpdates();


                        } else {
                            stopLocationUpdates();
                            SavedData.saveLocationService(false);
                        }
                        //dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        //isDoneProgramiically = true;
                        //start_location_updates.setChecked(!isChecked);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //isDoneProgramiically = true;
                    // start_location_updates.setChecked(!isChecked);
                } finally {
                    prg.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
                //isDoneProgramiically = true;
                //start_location_updates.setChecked(!isChecked);
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

    private void stopLocationUpdates() {

        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.stopService(locationService);
    }

    private void startLocationUpdates() {
        locationService.putExtra("mobile", "1234567890");
        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.startService(locationService);
    }

    protected String getCurrentTime() {
        Calendar calander = Calendar.getInstance();
        calander.getTime();
        int year = calander.get(Calendar.YEAR);
        int month = calander.get(Calendar.MONTH);
        int day = calander.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calander.getTime());

        return year + "-" + (month + 1) + "-" + day + " " + time;
    }

    private void click_Event() {
        btn_expensive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Booth_record.this, Expensive_Activity.class);
                startActivity(i);

            }
        });
        lnt_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Booth_record.this, DashBoard_Activity.class);
                startActivity(i);

            }
        });
        txt_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Booth_record.this, DashBoard_Activity.class);
                startActivity(i);
            }
        });
        crd_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Booth_record.this, MapsActivity.class);
                startActivity(i);


                DB_Function.ExportDatabasee(Booth_record.this, Database_Utils.DB_NAME);
 /*               //   CustomLeaveCanelDialog();
                boolean isSucess = DBOperation.deleteAll_Booth(Booth_record.this);
                Log.e("isSucess", "Deleted : " + isSucess);
*/
                copyDatabase(Booth_record.this, Database_Utils.DB_NAME);

            }
        });
        crd_booth_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  /*              String[] quotedata = DBOperation.getAllBooth(Booth_record.this);
                Log.e("total legnth", "Check Booth legnth : " + quotedata.length);
  */
                // Intent i = new Intent(Booth_record.this, MapsActivity.class);
                //startActivity(i);
            }
        });

        tracking_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Message = "Hi Your Last Location was ! \n" + SavedData.getMLocation() + "\nUpdated On " + SavedData.getLastLocationUploadTime() + "\nNote : " + SavedData.getLastLocationMessage();

                // Log.e(TAG, "Button click " + Message);
                trackingBuilder.setMessage(Message);
                trakingDialog.setMessage(Message);
                trakingDialog.show();
            }
        });

    }

    public void copyDatabase(Context c, String DATABASE_NAME) {
        String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
        String DB_PATH = "/data/data/bailiwick.bjpukh.com.vistarak/";

        String DB_NAME = Database_Utils.DB_NAME;
        databasePath = DB_PATH + DB_NAME;
        File f = new File(databasePath);
        OutputStream myOutput = null;
        InputStream myInput = null;
        Log.d("testing", " testing db path " + databasePath);
        Log.d("testing", " testing db exist " + f.exists());

        if (f.exists()) {
            try {

                File directory = new File("/mnt/sdcard/Vistraka_prince");
                if (!directory.exists()) directory.mkdir();

                myOutput = new FileOutputStream(directory.getAbsolutePath() + "/" + DATABASE_NAME);
                myInput = new FileInputStream(databasePath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                Toast.makeText(Booth_record.this, "Export Succesfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            } finally {
                try {
                    if (myOutput != null) {
                        myOutput.close();
                        myOutput = null;
                    }
                    if (myInput != null) {
                        myInput.close();
                        myInput = null;
                    }
                } catch (Exception e) {
                }
            }
        }

    }

    private void getDistrictDB() {
        DistrictModals = DBOperation.getAllDistrict(this);
        if (DistrictModals.size() > 0 && DistrictModals != null) {
            Log.e("in Database Calling", "in Database Calling");

            getLevleDB();
        } else {
            Log.e("Have to get from server", "Have to get from server");
            getDistrictFromServer();
        }
    }


    private void getDistrictFromServer() {
        prg.setMessage("Get District");
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
                    SavedData.saveOperation_status(false);

                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsAr = jsdata.getJSONArray("district_list");
                        for (int i = 0; i < jsAr.length(); i++) {
                            Log.e("Level IDs ", "" + jsAr.getJSONObject(i).getString("district_id"));
                            boolean isSuccess = DBOperation.insertDistrict(Booth_record.this, jsAr.getJSONObject(i).getString("district_id"), jsAr.getJSONObject(i).getString("district_name"));
                            Log.e("Success", "" + isSuccess);
                        }
                        getLevleDB();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }


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

                params.put("action", Utils_url.Action_GetDistrict);
                params.put("vistarak_id", SavedData.getUSER_NAME());

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void getLevleDB() {
        LevelModals = DBOperation.getAlllevel(this);
        if (LevelModals.size() > 0 && LevelModals != null) {
            Log.e("in Database Calling", "in Database Calling  LEVEL");


        } else {
            Log.e("Have to get from server", "Have to get from server n  LEVEL");
            getlevelFromServer();
        }
    }

    private void getlevelFromServer() {

        prg.setMessage("Get District");
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
                    SavedData.saveOperation_status(false);

                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsAr = jsdata.getJSONArray("level_list");
                        for (int i = 0; i < jsAr.length(); i++) {

                            Log.e("Level IDs ", "" + jsAr.getJSONObject(i).getString("level_id"));
                            boolean isSuccess = DBOperation.insertlevel(Booth_record.this, jsAr.getJSONObject(i).getString("level_id"), jsAr.getJSONObject(i).getString("level_name"));
                            Log.e("Success", "" + isSuccess);


                        }

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }

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

                params.put("action", Utils_url.Action_GetLevel);
                params.put("vistarak_id", SavedData.getUSER_NAME());

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private String getDeviceDetail() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void CustomLeaveCanelDialog() {
        final LvCancelDialog dialog = new LvCancelDialog(this);
        dialog.setCancelable(false);
        dialog.show();
        dialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void createIDS() {
        pDialog = new CustomDialog_Update(this);
        prg = new ProgressDialog(this);
        locationService = new Intent(Booth_record.this, LocationService.class);
        lnt_dashboard = (LinearLayout) findViewById(R.id.lnt_dashboard);
        txt_dashboard = (TextView) findViewById(R.id.txt_dashboard);
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);

        txt_key_voter = (TextView) findViewById(R.id.txt_key_voter);
        txt_Man_ki_baat = (TextView) findViewById(R.id.txt_Man_ki_baat);
        txt_special_area = (TextView) findViewById(R.id.txt_special_area);
        txt_new_member = (TextView) findViewById(R.id.txt_new_member);
        mNewMember = (TextView) findViewById(R.id.tv_new_member);
        mSpecialArea = (TextView) findViewById(R.id.tv_special_area);
        mManKiBat = (TextView) findViewById(R.id.tv_man_kibat_pramukh);
        mKeyVoter = (TextView) findViewById(R.id.tv_key_voter);

        btn_expensive = (Button) findViewById(R.id.btn_weekely_expensive);

        tracking_details = (FloatingActionButton) findViewById(R.id.tracking_details);
        crd_man = (CardView) findViewById(R.id.crd_man);
        crd_booth_visit = (CardView) findViewById(R.id.crd_booth_visit);
        image_profile = (ImageView) findViewById(R.id.image_profile);
        trackingBuilder = new AlertDialog.Builder(this);

// Language
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());

        mNewMember.setText(SelectedLanguage[TextUtils.booth_meeting]);
        mSpecialArea.setText(SelectedLanguage[TextUtils.motorcycle_in_booth]);
        mManKiBat.setText(SelectedLanguage[TextUtils.man_ki_bat_pramukh]);
        mKeyVoter.setText(SelectedLanguage[TextUtils.whatsapp_group]);
        txt_dashboard.setText(SelectedLanguage[TextUtils.record_activity]);


        trackingBuilder.setMessage("Tracking Details").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //finish();
            }
        });
        trakingDialog = trackingBuilder.create();
    }

    /* @Override
     public void onBackPressed() {
        *//* CustomDialogExit cstEXIT = new CustomDialogExit(this);
        cstEXIT.show();*//*
    }
*/
    @Override
    protected void onResume() {
        checkotheruserLigin();
        STR_user_id = SavedData.getUSER_NAME();
      /*  if (!isprofileupload) {
            String device_id = DeviceOperation.getDeviceDetail(Booth_record.this);
            String emi_id = DeviceOperation.getEmi(Booth_record.this);
            String fcmID = DeviceOperation.getFcmkey();

            getRecord(STR_user_id, deviceID, emi_id, fcmID);

        }
        isprofileupload = false;
*/
        isGpsEnabled(this);
        super.onResume();

    }

    private void checkotheruserLigin() {
        prg.setMessage("Loading..");
        prg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                prg.dismiss();
                Log.e("Response Abhishek :", response);
                //  Manage_data_login(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        deviceID = jsonObject.getString("deviceid");
                        String systemid = getDeviceDetail();
                        backpressed = true;
                        Log.e("Device id", deviceID);

                      /*  Log.e("System id", systemid);
                        STR_user_id = SavedData.getUSER_NAME();
                        Log.e("pp","i m heree "+isprofileupload);
                        if (!isprofileupload) {
                            String device_id = DeviceOperation.getDeviceDetail(Booth_record.this);
                            String emi_id = DeviceOperation.getEmi(Booth_record.this);
                            String fcmID = DeviceOperation.getFcmkey();
                            getRecord(STR_user_id, deviceID, emi_id, fcmID);

                        }
                        isprofileupload = false;*/


                        STR_user_id = SavedData.getUSER_NAME();
                        Log.e("pp", "i m heree " + isprofileupload);
                        if (!isprofileupload) {
                            String device_id = DeviceOperation.getDeviceDetail(Booth_record.this);
                            String emi_id = "";
                            String fcmID = DeviceOperation.getFcmkey();
                            getRecord(STR_user_id, deviceID, emi_id, fcmID);

                        }
                        isprofileupload = false;


                    }


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
                params.put("action", Utils_url.Action_CheckLogin);
                params.put("userName", SavedData.getUSER_NAME());

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    void openDialog() {

        Intent outIntent = new Intent(this, LoginActivity.class);
        outIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        outIntent.putExtra("EXIT", true);
        startActivity(outIntent);
        SavedData.saveLogin(false);
        finish();


       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authentication Error");
        builder.setMessage("This User is Loged In in another device!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backpressed=false;
                SavedData.saveLogin(false);
                finish();
              //  startActivity(new Intent(Booth_record.this,LoginActivity.class));

            }
        });
        builder.show();*/
    }

 /*   @Override
    public void onBackPressed() {
      *//*  if (backpressed){
        }
        else {
            onBackPressed();
        }*//*
    }*/

    private void isGpsEnabled(final Context context) {
        final String TAG = "hi";

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Booth_record.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    /*************************
     * on Activity Result Overridden
     ******************/
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isprofileupload = true;

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    // Log.e("bitmap",bitmap.toString()+"");

                    Bitmap imageBitmap = SiliCompressor.with(Booth_record.this).getCompressBitmap(resultUri.toString(), true);

                    myBase64Image = encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG);
                    // Log.e("bitmapString",myBase64Image.toString()+"");

                    image_profile.setImageBitmap(bitmap);
                    //isImage_loaded = true;

                    UploadImage(SavedData.getUSER_NAME());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // mImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Crop Error", error.getMessage() + "");
            }
        }
    }


    private final BroadcastReceiver mHandlerReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                Log.e("its eneter", "its eneter");
                WakeLocker.acquire(getApplicationContext());
                // Releasing wake lock
                WakeLocker.release();
                String pp = intent.getExtras().getString("message");
                Log.e("pp", pp);
                String msg = intent.getExtras().getString(Itag.FCM_Message);
                Log.e("Response : ", msg);
                ShowNotifyDialog(msg);
                //  Setjsondata_addrequest(msg);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    };

    private void ShowNotifyDialog(String string) {

        if (pDialog != null) {
            pDialog.show();
            pDialog.btn_apply.setVisibility(View.GONE);
            pDialog.btn_cancel.setText("Ok");
            pDialog.popup_txt_title.setText("Vistarak");
            pDialog.txt_message.setText(string);
            pDialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pDialog.dismiss();
                }
            });

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SavedData.saveNotify(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SavedData.saveNotify(true);

    }

    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();

        SavedData.saveNotify(false);

        try {
            unregisterReceiver(mHandlerReciver);
            //   GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            Log.e("UnRegister Rec", "> " + e.getMessage());
        }

        super.onDestroy();
    }
}
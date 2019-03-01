package bailiwick.bjpukh.com.vistarak.LocationService;

/**
 * Created by User on 05-Dec-16.
 */

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Getter_Setter.AnusuchitModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Army_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Bike_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothMeeting_modal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Event_Pramukh_model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Home_visitModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Influence_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Man_Ki_BaatModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Ngo_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.PannaPramukhModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Religious_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Shaheed_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.SmartPhoneModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Special_AreaModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.SwachtaModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Whats_AppModal;
import bailiwick.bjpukh.com.vistarak.Modals.KametiModel;
import bailiwick.bjpukh.com.vistarak.Modals.SavePointsBean;
import bailiwick.bjpukh.com.vistarak.R;
import bailiwick.bjpukh.com.vistarak.Splash;
import bailiwick.bjpukh.com.vistarak.Support.CheckNetworkState;
import bailiwick.bjpukh.com.vistarak.Support.PersonalTimer;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_prefrences;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DB_Function;
import bailiwick.bjpukh.com.vistarak.db.Upload_operation;


public class LocationService extends Service {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String TAG = "BOOMBOOM";
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 10f;
    //fetch location
    public static Location mLastLocation;
    public static String Lat, Lan;
    static Geocoder geocoder;
    private static SharedPreferences pref;
    boolean isInternetAvailable, isAllPointsUploaded = true, isServiceStarted = false;
    ArrayList<SavePointsBean> locationAllPoints;
    //int TIMER_START_TIME_IN_SEC = 5000, updatedTime = 0;
    // countdown driver
    PersonalTimer timer;
    Context ctx;
    int TIMER_START_TIME_IN_SEC = 15000, updatedTime = 0;
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private LocationManager mLocationManager = null;

    private static void sendMessageToActivity(Location l, String lastUpdatedOn, Context ctx, int pendingPoints, boolean isBulkUpload, String responce) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.pycitup.BroadcastReceiver");
        intent.putExtra("data=", l.getLatitude());
        intent.putExtra("longitude", l.getLongitude());
        intent.putExtra("pendingPoints", pendingPoints);
        intent.putExtra("lastUpdated", lastUpdatedOn);
        intent.putExtra("isBulkUpload", isBulkUpload);
        intent.putExtra("response", responce);
        pref.edit().putInt(Utils_prefrences.pref_pendingPoints, pendingPoints).apply();
        pref.edit().putString(Utils_prefrences.pref_timeStamp, lastUpdatedOn).apply();
        ctx.sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
       /* pref.edit().putBoolean(Utils_prefrences.pref_isTaskStarted,true).commit();
        Intent i = new Intent(this,DashBoard_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);*/
        super.onTaskRemoved(rootIntent);
        Log.e(TAG, "Task Is removed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        ctx = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isServiceStarted = pref.getBoolean(Utils_prefrences.pref_isServiceStarted, false);
        if (isServiceStarted) {
            timer.cancel();
            timer.start();
        } else {
            timer.start();
        }
        pref.edit().putBoolean(Utils_prefrences.pref_isServiceStarted, true).commit();
        isServiceStarted = true;
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        // ExceptionHandler.register(this,
        //       "http://uvcabs.esy.es/crash_logs_ambulance/server.php?version="+ Build.VERSION.SDK_INT);
        geocoder = new Geocoder(this, Locale.getDefault());
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e(TAG, "onCreate");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        locationAllPoints = new ArrayList<SavePointsBean>();
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "network provider does not exist, " + ex.getMessage());
        }
      /*  try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }*/
        timerFunction();

    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        timer.cancel();
        super.onDestroy();
        isServiceStarted = false;
        pref.edit().putBoolean(Utils_prefrences.pref_isServiceStarted, false).commit();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.e(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void timerFunction() {

        timer = new PersonalTimer(TIMER_START_TIME_IN_SEC, 1000) {

            public void onTick(long millisUntilFinished) {
             //   Log.e(TAG, "Updating Location in : " + millisUntilFinished / 1000);
                //Log.e(TAG,TIMER_START_TIME_IN_SEC+" tick");
            }

            public void onFinish() {
                // TODO: restart counter with millisInFuture = 4000 ( 4 seconds )
                //cancel();  // there is no need the call the cancel() method here
                String no = pref.getString(Utils_prefrences.pref_mobile, "");
                Log.e(TAG, "No is " + no + ":" + mLastLocation.toString());
                //Todo Please upload data here to server
                sendDataToServer(no, mLastLocation);
                isGpsEnabled(getApplicationContext());
            }
        };

    }

    private void isGpsEnabled(final Context context) {
        final String TAG = "hi";

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
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
                            Intent mIntent = new Intent(LocationService.this, Splash.class);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mIntent);
                        } catch (ActivityNotFoundException e) {
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

    private void sendDataToServer(String no, Location mLastLocation) {
        isInternetAvailable = Utils_prefrences.isNetworkAvailable(getApplicationContext());

        if (isInternetAvailable && isAllPointsUploaded) {
            Log.e("i m here", "i m heree 8");

            //Todo upload single point here
            String lat = Double.toString(mLastLocation.getLatitude());
            String lng = Double.toString(mLastLocation.getLongitude());
            Log.e(TAG, "latitude : " + lat);
            // if location is not genrated
            if (lat.equalsIgnoreCase("0.0") || lng.equalsIgnoreCase("0.0")) {
                Log.e(TAG, "Not Uploaded To Server As location not genrated");

                sendMessageToActivity(mLastLocation, "", getApplicationContext(), locationAllPoints.size(), true, "Location Not Found Generating Again");
                popTheSimpleNotification("Vistarak Err", " Location Not Found Generating Again");
                timer.start();
                //popTheSimpleNotification("Location Error","Location Not generated please check your gps");
                Log.e("i m here", "i m heree 6");
                ChechDB();

            } else {
                updateLocationToServerHere(no, mLastLocation);
            }
        } else {
            if (isInternetAvailable) {
                Log.e("i m here", "i m heree 7");
                //Todo save points for bulk upload make
                // @isAllPoints to true
                String STR_MOBILE = "", STR_LAT = "", STR_LONG = "", STR_LATE_TIME = "";
// Prince Edition;
                String lati = Double.toString(mLastLocation.getLatitude());
                String lngi = Double.toString(mLastLocation.getLongitude());
                Log.e("Sushil sir", "pp Lat +" + lati);
                Log.e("Sushil sir", "pp Lon +" + lngi);
                if (lati.equalsIgnoreCase("0.0") || lngi.equalsIgnoreCase("0.0")) {

                } else {
                    locationAllPoints.add(new SavePointsBean("", no.toString(), lati.toString(), lngi.toString(), getCurrentTime(), "address"));

                }
// prince End Edition
                for (int i = 0; i < locationAllPoints.size(); i++) {
                    String mobile = locationAllPoints.get(i).getMobile();
                    String lat = locationAllPoints.get(i).getLat();
                    String longitude = locationAllPoints.get(i).getLongitude();
                    String latTime = locationAllPoints.get(i).getLessTime();
                    if (i == 0) {
                        STR_MOBILE = STR_MOBILE + mobile;
                        STR_LAT = STR_LAT + lat;
                        STR_LONG = STR_LONG + longitude;
                        STR_LATE_TIME = STR_LATE_TIME + latTime;
                    } else {
                        STR_MOBILE = STR_MOBILE + "," + mobile;
                        STR_LAT = STR_LAT + "," + lat;
                        STR_LONG = STR_LONG + "," + longitude;
                        STR_LATE_TIME = STR_LATE_TIME + "," + latTime;
                    }
                }

                uploadAllPointsInVolley(STR_MOBILE, STR_LAT, STR_LONG, STR_LATE_TIME, mLastLocation);

            } else {
                Log.e("i m here", "i m heree 55");

                //savePointsToLocal
                isAllPointsUploaded = false;
                String lat = Double.toString(mLastLocation.getLatitude());
                String lng = Double.toString(mLastLocation.getLongitude());
                Log.e("Sushil sir", "Lat +" + lat);
                Log.e("Sushil sir", "Lon +" + lng);

                // check if lat long genrated or not
                if (lat.equalsIgnoreCase("0.0") || lng.equalsIgnoreCase("0.0")) {
                    Log.e(TAG, "location data here" + mLastLocation + "" + locationAllPoints.size());
                    sendMessageToActivity(mLastLocation, "", getApplicationContext(), locationAllPoints.size(), true, "Location Not Found Generating Again");

                    SavedData.saveLastLocationMessage("Location Not Found Generating Again Please check if Gps Is on");
                    popTheSimpleNotification("Vistarak Err", "Location Not Found Generating Again Please check if Gps Is on");
                    timer.start();
                    //popTheSimpleNotification("Location Error","Location Not generated please check your gps");
                } else {

                    Log.e(TAG, "pointTime " + getCurrentTime());
                    locationAllPoints.add(new SavePointsBean("", no.toString(), lat.toString(), lng.toString(), getCurrentTime(), "address"));
                    sendMessageToActivity(mLastLocation, "", getApplicationContext(), locationAllPoints.size(), true, "Point Saved Waiting For internet To upload");

                    SavedData.saveLastLocationMessage(locationAllPoints.size() + "Point Saved Waiting For internet To upload");
                    timer.start();
                }
                //// TODO: 06-Dec-16 send for bulk broadcast here

            }
        }
    }

    ///////////////////DATABASE MAINTAINANCE
    private void ChechDB() {

        if (CheckNetworkState.isNetworkAvailable(getApplicationContext())) {

            ArrayList<Man_Ki_BaatModal> ListManKiBaat = new ArrayList<>();
            ArrayList<Home_visitModal> ListHome_visit = new ArrayList<>();
            ArrayList<Special_AreaModal> ListSpecialArea = new ArrayList<>();
            ArrayList<Whats_AppModal> ListWhatsApp = new ArrayList<>();
            ArrayList<SwachtaModel> ListSwachta = new ArrayList<>();
            ArrayList<Ngo_Model> ListNGO = new ArrayList<>();
            ArrayList<Religious_Model> ListReligious = new ArrayList<>();
            ArrayList<Army_Model> ListEXarmy = new ArrayList<>();
            ArrayList<Shaheed_Model> ListShaheed = new ArrayList<>();
            ArrayList<Influence_Model> ListInfleuence = new ArrayList<>();
            ArrayList<Bike_Model> ListBike = new ArrayList<>();
            ArrayList<BoothMeeting_modal> ListMeeting_booth = new ArrayList<>();
            ArrayList<PannaPramukhModel> ListPanna_Pramukh = new ArrayList<>();
            ArrayList<SmartPhoneModel> ListSmartPhone_User = new ArrayList<>();
            ArrayList<KametiModel> ListKameti_member = new ArrayList<>();
            ArrayList<AnusuchitModel> ListAnusuchit_member = new ArrayList<>();
            ArrayList<Event_Pramukh_model> ListEvent_member = new ArrayList<>();

            ListManKiBaat = DB_Function.Get_manKibaat(getApplicationContext());
            ListHome_visit = DB_Function.Get_Home_visit(getApplicationContext());
            ListSpecialArea = DB_Function.Get_Special_Area(getApplicationContext());
            ListWhatsApp = DB_Function.Get_Whats_App(getApplicationContext());
            ListSwachta = DB_Function.Get_Swachta(getApplicationContext());
            ListNGO = DB_Function.Get_Ngo(getApplicationContext());
            ListReligious = DB_Function.Get_Religious(getApplicationContext());
            ListEXarmy = DB_Function.Get_Army(getApplicationContext());
            ListShaheed = DB_Function.Get_Shaheed(getApplicationContext());
            ListInfleuence = DB_Function.Get_Influence(getApplicationContext());
            ListBike = DB_Function.Get_Bike(getApplicationContext());
            ListMeeting_booth = DB_Function.Get_Boothmeeting(getApplicationContext());
            ListPanna_Pramukh = DB_Function.GetPannaPramkh(getApplicationContext());
            ListSmartPhone_User = DB_Function.Get_SmartPhoneUser(getApplicationContext());
            ListKameti_member = DB_Function.Get_KametiMember(getApplicationContext());
            ListAnusuchit_member = DB_Function.Get_Anusuchitmember(getApplicationContext());
            ListEvent_member = DB_Function.Get_Event_member(getApplicationContext());





            // Event Pramukh Member
            if (ListEvent_member != null && ListEvent_member.size() > 0) {

                if (!SavedData.getOperation_status()) {
                    Log.e("location service", "Operation start");

                    Upload_operation.SaveEvent_pramukh(getApplication(), ListEvent_member);
                } else {
                    Log.e("location service", "Operation running");

                }
                Log.e("List Size", "List Size : " + ListEvent_member.size());

            } else {
                Log.e("List Size", "List Size : Empty");

            }

            // Anusuchit Member
            if (ListAnusuchit_member != null && ListAnusuchit_member.size() > 0) {

                if (!SavedData.getOperation_status()) {
                    Log.e("location service", "Operation start");

                    Upload_operation.SaveAnusuchit(getApplication(), ListAnusuchit_member);
                } else {
                    Log.e("location service", "Operation running");

                }
                Log.e("List Size", "List Size : " + ListAnusuchit_member.size());

            } else {
                Log.e("List Size", "List Size : Empty");

            }

// Kameti Mamber
            if (ListKameti_member != null && ListKameti_member.size() > 0) {

                if (!SavedData.getOperation_status()) {
                    Log.e("location service", "Operation start");

                    Upload_operation.SaveKametiMember(getApplication(), ListKameti_member);
                } else {
                    Log.e("location service", "Operation running");

                }
                Log.e("List Size", "List Size : " + ListPanna_Pramukh.size());

            } else {
                Log.e("List Size", "List Size : Empty");

            }

            // Smart Phone User
            if (ListSmartPhone_User != null && ListSmartPhone_User.size() > 0) {

                if (!SavedData.getOperation_status()) {
                    Log.e("location service", "Operation start");

                    Upload_operation.SaveSmartPhone(getApplication(), ListSmartPhone_User);
                } else {
                    Log.e("location service", "Operation running");

                }
                Log.e("List Size", "List Size : " + ListPanna_Pramukh.size());

            } else {
                Log.e("List Size", "List Size : Empty");

            }


            // Panna Pramukh
            if (ListPanna_Pramukh != null && ListPanna_Pramukh.size() > 0) {

                if (!SavedData.getOperation_status()) {
                    Log.e("location service", "Operation start");

                    Upload_operation.SavePannaPramukh(getApplication(), ListPanna_Pramukh);
                } else {
                    Log.e("location service", "Operation running");

                }
                Log.e("List Size", "List Size : " + ListPanna_Pramukh.size());

            } else {
                Log.e("List Size", "List Size : Empty");

            }

            // Home Visit
            if (ListHome_visit != null && ListHome_visit.size() > 0) {

                if (!SavedData.getOperation_status()) {
                    Log.e("location service", "Operation start");

                    Upload_operation.SaveHome_visit(getApplication(), ListHome_visit);
                } else {
                    Log.e("location service", "Operation running");

                }
                Log.e("List Size", "List Size : " + ListHome_visit.size());

            } else {
                Log.e("List Size", "List Size : Empty");

            }
            // Man ki Baat
            if (ListManKiBaat != null && ListManKiBaat.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveManPramukh(getApplication(), ListManKiBaat);
                    Log.e("List Size", "List Size : " + ListManKiBaat.size());

                } else {
                    Log.e("location service", "Operation running");
                }

                Log.e("List Size", "List Size : Empty");

            }
            // Special Area
            if (ListSpecialArea != null && ListSpecialArea.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveSpecialArea(getApplication(), ListSpecialArea);
                    Log.e("List Size", "List Size : " + ListSpecialArea.size());

                } else {
                    Log.e("location service", "Operation running ListSpecialArea");
                }

                Log.e("List Size", "List Size : Empty ListSpecialArea");

            }
            // Whats app
            if (ListWhatsApp != null && ListWhatsApp.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveWhatsApp(getApplication(), ListWhatsApp);
                    Log.e("List Size", "List Size : " + ListWhatsApp.size());

                } else {
                    Log.e("location service", "Operation running ListWhatsApp");
                }

                Log.e("List Size", "List Size : Empty ListSpecialArea");

            }
            // Swachta
            if (ListSwachta != null && ListSwachta.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveSwachta(getApplication(), ListSwachta);
                    Log.e("List Size", "List Size : " + ListSwachta.size());

                } else {
                    Log.e("location service", "Operation running ListSwachta");
                }

                Log.e("List Size", "List Size : Empty ListSpecialArea");

            }
// NGO
            if (ListNGO != null && ListNGO.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveNgo(getApplication(), ListNGO, 1);
                    Log.e("List Size", "List Size : " + ListNGO.size());

                } else {
                    Log.e("location service", "Operation running ListNGO");
                }

                Log.e("List Size", "List Size : Empty ListNGO");

            }
// Religious
            if (ListReligious != null && ListReligious.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveReligious(getApplication(), ListReligious, 1);
                    Log.e("List Size", "List Size : " + ListReligious.size());

                } else {
                    Log.e("location service", "Operation running ListReligious");
                }

                Log.e("List Size", "List Size : Empty ListReligious");

            }
            // Army
            if (ListEXarmy != null && ListEXarmy.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveArmy(getApplication(), ListEXarmy, 1);
                    Log.e("List Size", "List Size : " + ListEXarmy.size());

                } else {
                    Log.e("location service", "Operation running ListEXarmy");
                }

                Log.e("List Size", "List Size : Empty ListEXarmy");

            }

            // Shaheed
            if (ListShaheed != null && ListShaheed.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveShaheed(getApplication(), ListShaheed, 1);
                    Log.e("List Size", "List Size : " + ListShaheed.size());

                } else {
                    Log.e("location service", "Operation running ListShaheed");
                }

                Log.e("List Size", "List Size : Empty ListShaheed");

            }


            // Influence
            if (ListInfleuence != null && ListInfleuence.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveInfluence(getApplication(), ListInfleuence, 1);
                    Log.e("List Size", "List Size : " + ListShaheed.size());

                } else {
                    Log.e("location service", "Operation running ListInfleuence");
                }

                Log.e("List Size", "List Size : Empty ListInfleuence");

            }
            // Bike
            if (ListBike != null && ListBike.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveBike(getApplication(), ListBike);
                    Log.e("List Size", "List Size : " + ListShaheed.size());

                } else {
                    Log.e("location service", "Operation running ListBike");
                }

                Log.e("List Size", "List Size : Empty ListBike");

            }

            // Booth Meeting
            if (ListMeeting_booth != null && ListMeeting_booth.size() > 0) {
                if (!SavedData.getOperation_status()) {
                    Upload_operation.SaveMeeting(getApplication(), ListMeeting_booth);
                    Log.e("List Size", "List Size : " + ListShaheed.size());

                } else {
                    Log.e("location service", "Operation running ListMeeting_booth");
                }

                Log.e("List Size", "List Size : Empty ListMeeting_booth");

            }

        }

        // Influence


    }


    private void popTheSimpleNotification(String title, String desc) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(desc)
                        .setAutoCancel(true);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Splash.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Splash.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        Utils_prefrences.pref_notificationId,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.

        mNotificationManager.notify(Utils_prefrences.pref_notificationId, mBuilder.build());
    }

    private void updateLocationToServerHere(String no, final Location mLastLocation) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        String JsonString = "";
        try {
            jsonObject.put("vistarak_id", SavedData.getUSER_NAME());
            jsonObject.put("latitude", String.valueOf(mLastLocation.getLatitude()));
            jsonObject.put("longitude", String.valueOf(mLastLocation.getLongitude()));
            jsonObject.put("datetime", getCurrentTime());

            jsonArray.put(jsonObject);
            Log.e(TAG, "Array " + jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonString = jsonArray.toString();

        Log.e(TAG, "single array " + JsonString);
        final String finalJsonString = JsonString;
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
                            if (status.equalsIgnoreCase("1")) {

                                SavedData.saveLastLocationMessage("Server Response -" + msg);
                                SavedData.saveMLocation(mLastLocation.getLatitude() + " - " + mLastLocation.getLongitude());
                                SavedData.saveLattitudeLocation("" + mLastLocation.getLatitude());
                                SavedData.saveLongitudeLocation("" + mLastLocation.getLongitude());

                                SavedData.saveLastLocationUploadTime(getCurrentTime());

                            } else {
                                SavedData.saveLastLocationMessage("Server Response -" + msg);
                                SavedData.saveMLocation(mLastLocation.getLatitude() + " - " + mLastLocation.getLongitude());
                                SavedData.saveLattitudeLocation("" + mLastLocation.getLatitude());
                                SavedData.saveLongitudeLocation("" + mLastLocation.getLongitude());

                                SavedData.saveLastLocationUploadTime(getCurrentTime());

                            }
                            ChechDB();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            timer.start();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timer.start();

                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_UploadLocation);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("locationData", finalJsonString);
                params.put("trackingId", SavedData.getTrackingId());
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);


    }

    private void uploadAllPointsInVolley(String str_mobile, String str_lat, String str_long, String str_late_time, final Location mLastLocation) {
        Log.e("i m here", "i m heree 9");

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        String JsonString = "";
        Log.e("Sushil Sir", "List Size new code   :  " + locationAllPoints.size());

        for (int i = 0; i < locationAllPoints.size(); i++) {
            try {
                jsonObject.put("vistarak_id", SavedData.getUSER_NAME());
                jsonObject.put("latitude", locationAllPoints.get(i).getLat());
                jsonObject.put("longitude", locationAllPoints.get(i).getLongitude());
                jsonObject.put("datetime", locationAllPoints.get(i).getLessTime());

                jsonArray.put(jsonObject);
                Log.e(TAG, "Array " + jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JsonString = jsonArray.toString();
        Log.e(TAG, "JsonData " + jsonArray.toString());


        // Request a string response from the provided URL.
        final String finalJsonString = JsonString;
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
                            if (status.equalsIgnoreCase("1")) {
                                isAllPointsUploaded = true;
                                SavedData.saveLastLocationMessage("Server Response - " + msg);
                                SavedData.saveMLocation(mLastLocation.getLatitude() + " - " + mLastLocation.getLongitude());
                                SavedData.saveLattitudeLocation("" + mLastLocation.getLatitude());
                                SavedData.saveLongitudeLocation("" + mLastLocation.getLongitude());

                                SavedData.saveLastLocationUploadTime(getCurrentTime());
                                locationAllPoints.clear();
                            } else {
                                SavedData.saveLastLocationMessage("Server Response - " + msg);
                                SavedData.saveMLocation(mLastLocation.getLatitude() + " - " + mLastLocation.getLongitude());
                                SavedData.saveLattitudeLocation("" + mLastLocation.getLatitude());
                                SavedData.saveLongitudeLocation("" + mLastLocation.getLongitude());

                                SavedData.saveLastLocationUploadTime(getCurrentTime());

                            }
                            Log.e("i m here", "i m heree 4");
                            ChechDB();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            timer.start();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timer.start();

                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_UploadLocation);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("trackingId", SavedData.getTrackingId());
                params.put("locationData", finalJsonString);

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

    private String getLateTime() {
        Calendar calander = Calendar.getInstance();
        calander.add(Calendar.MINUTE, -330);
        calander.getTime();
        int year = calander.get(Calendar.YEAR);
        int month = calander.get(Calendar.MONTH);
        int day = calander.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(calander.getTime());
        return year + "-" + (month + 1) + "-" + day + " " + time;
    }

    private class LocationListener implements android.location.LocationListener {


        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            Lat = "" + location.getLatitude();
            Lan = "" + location.getLongitude();
            Log.e("prince", "hello : " + Lan);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }
    }
}
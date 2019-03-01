package bailiwick.bjpukh.com.vistarak.OTP;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Booth_record;
import bailiwick.bjpukh.com.vistarak.Firebase.MyFirebaseInstanceIDService;
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.R;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 11-12-2017.
 */

public class OtpScreen extends RootActivity {

    private OtpView otpView_otpSix;
    private OtpView otpView;
    private Context context;
    private Button button_submit_otp;
    ProgressDialog prg;
    TextView oTpText,oTpWarningText;
//   SELLER SERVIC,P:01,0000388 (Ref# 09999999980910002563303)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);
        context = OtpScreen.this;
        CreateIds();
        clickListner();
    }

    private void clickListner() {
        button_submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!otpView_otpSix.hasValidOTP()) {
                    Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                    otpView_otpSix.startAnimation(shake);
                } else {
                    String otp_value = otpView_otpSix.getOTP();
                    String device_id = getDeviceDetail();
                    String emi_id = getEmi();
                    String fcmID = getFcmkey();
                    verifyUser(otp_value, SavedData.getUSER_NAME(), fcmID, emi_id, device_id);

                    Log.e("My Otp", otp_value);

                }
            }
        });
    }

    private void verifyUser(String otp_value, final String user_name, final String fcmID, final String emi_id,
                            final String device_id) {
        prg.setMessage("verify....");
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
                            Toast.makeText(context, ""+msg+" "+status, Toast.LENGTH_SHORT).show();
                            if (status.equalsIgnoreCase("1")) {
                                SavedData.saveLogin(true);
                                Intent i = new Intent(OtpScreen.this, Booth_record.class);
                                startActivity(i);
                                finish();

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
                params.put("action", Utils_url.Action_VerifyOTP);
                params.put("userName", user_name);
                params.put("cloudId", fcmID);
                params.put("deviceId", device_id);
                params.put("imeiNumber", "pp");
                params.put("otp",otpView_otpSix.getOTP());
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);

    }

    private void CreateIds() {
        prg = new ProgressDialog(this);

        otpView = new OtpView(context);
        otpView_otpSix = (OtpView) findViewById(R.id.otpView_otpSix);
        button_submit_otp = (Button) findViewById(R.id.button_submit_otp);
        oTpText = (TextView)findViewById(R.id.textView_otp_waitTitle);
        oTpWarningText = (TextView)findViewById(R.id.textView_otp_warrning);

        button_submit_otp.setText(SelectedLanguage[TextUtils.submit]);
        oTpText.setText(SelectedLanguage[TextUtils.enter_otp]);
        oTpWarningText.setText(SelectedLanguage[TextUtils.otp_warning]);

    }

    private String getDeviceDetail() {

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        return deviceID;
    }

    private String getFcmkey() {
        MyFirebaseInstanceIDService myfcm = new MyFirebaseInstanceIDService();
        String fcmtoken = myfcm.getFcm();
        Log.e("fcm id", fcmtoken);
        if (fcmtoken.isEmpty()) {
            getFcmkey();
            Log.e("Fcm repeat", "empty : " + fcmtoken);
        }
        return fcmtoken;
        //   DB_Function.SavetextFile(Booth_record.this, fcmtoken);
    }


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
        identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        return identifier;
    }

}

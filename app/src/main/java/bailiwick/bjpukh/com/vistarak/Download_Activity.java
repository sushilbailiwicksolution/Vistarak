package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Adapter.AdapterDownload;
import bailiwick.bjpukh.com.vistarak.Dialog.CustomDialogDownlaod;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.GET_SET_Download;
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 03-09-2017.
 */

public class Download_Activity extends RootActivity implements AdapterDownload.customProfile_view {
    ListView lst_download;
    ArrayList<GET_SET_Download> ListDOWNLOAD;
    ProgressDialog prg;
    AdapterDownload adapter;
    TextView mDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_activity);
        createIDS();
        CreateList();
    }

    private void CreateList() {
        prg.setMessage("Downloading .....");
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

                            String status = jsdata.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                Log.e("here", "111");
                                JSONArray jsArr = jsdata.getJSONArray("apps");
                                for (int i = 0; i < jsArr.length(); i++) {
                                    Log.e("here", "222");
                                    ListDOWNLOAD.add(new GET_SET_Download(jsArr.getJSONObject(i).getString("id"),
                                            jsArr.getJSONObject(i).getString("app_name"),
                                            jsArr.getJSONObject(i).getString("app_icon"),
                                            jsArr.getJSONObject(i).getString("app_url"),
                                            jsArr.getJSONObject(i).getString("create_date")
                                    ));

                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                String msg = jsdata.getString("msg");
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
                params.put("action", Utils_url.Action_Get_Download);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }


    private void createIDS() {
        prg = new ProgressDialog(this);
        lst_download = (ListView) findViewById(R.id.lst_download);
        ListDOWNLOAD = new ArrayList<>();
        adapter = new AdapterDownload(Download_Activity.this, R.layout.cst_download, ListDOWNLOAD);
        adapter.setcustomProfile_viewListner(this);
        lst_download.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setLanguage();

    }

    private void setLanguage() {
        mDownload = (TextView)findViewById(R.id.text_download);

        mDownload.setText(SelectedLanguage[TextUtils.download]);
    }

    @Override
    public void onButtonClickListner(final int position, String value) {

        Log.e("value", ListDOWNLOAD.get(position).getStr_app_name());
        final CustomDialogDownlaod dialog = new CustomDialogDownlaod(this);
        dialog.show();

        dialog.txt_message.setText("Thanks For Download " + ListDOWNLOAD.get(position).getStr_app_name());

        dialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendorignal_sms("9953091992", 0);
                dialog.dismiss();
            }
        });
        dialog.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.edt_number.getText().toString().equalsIgnoreCase("")) {
                    dialog.edt_number.setError("Required");

                } else if (dialog.edt_number.getText().toString().length() < 9 || dialog.edt_number.getText().toString().length() > 11) {
                    dialog.edt_number.setError(Html
                            .fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
                    return;
                } else {
                    SendMessage(dialog.edt_number.getText().toString(), ListDOWNLOAD.get(position).getStr_id(), position);
                    dialog.dismiss();

                }
            }
        });
    }

    private void SendMessage(final String number, final String appID, final int position) {
        prg.setMessage("Please Wait....");
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
                                // sendorignal_sms(number, position);
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
                params.put("action", Utils_url.Action_sendDownload);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", SavedData.getBOOTH_ID());
                params.put("app_id", appID);
                params.put("latitude", "28.5810336");
                params.put("longitude", "77.3152115");
                params.put("mobile_no", number);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void sendorignal_sms(String number, int pos) {
        String appurl = ListDOWNLOAD.get(pos).getStr_app_url();

        String messageToSend = "Thanks For Download Click on Link : " + appurl;


        SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);

    }
}

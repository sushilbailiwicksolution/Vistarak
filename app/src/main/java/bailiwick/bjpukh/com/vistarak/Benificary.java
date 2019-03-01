package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

import bailiwick.bjpukh.com.vistarak.Adapter.AdapterBenificary;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Get_Set_benificary_Link;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;

/**
 * Created by Prince on 07-09-2017.
 */

public class Benificary extends RootActivity {
    ListView lst_benificary;
    ArrayList<Get_Set_benificary_Link> List_Benificary;
    ProgressDialog prg;
    AdapterBenificary adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.benificary_activity);
        createIDS();
        CreateList();
    }

    private void CreateList() {
        prg.setMessage("Please Wait...");
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
                                JSONArray jsArr = jsdata.getJSONArray("beneficiary");
                                for (int i = 0; i < jsArr.length(); i++) {
                                    Log.e("here", "222");
                                    List_Benificary.add(new Get_Set_benificary_Link(jsArr.getJSONObject(i).getString("id"),
                                            jsArr.getJSONObject(i).getString("name"),
                                            jsArr.getJSONObject(i).getString("icon"),
                                            jsArr.getJSONObject(i).getString("description"),
                                            jsArr.getJSONObject(i).getString("app_id"),
                                            jsArr.getJSONObject(i).getString("url"),
                                            jsArr.getJSONObject(i).getString("created_at")

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
                params.put("action", Utils_url.Action_Get_benificiary);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void createIDS() {
        prg = new ProgressDialog(this);
        lst_benificary = (ListView) findViewById(R.id.lst_benificary);
        List_Benificary = new ArrayList<>();

        adapter = new AdapterBenificary(Benificary.this, R.layout.cst_benificary, List_Benificary);
        lst_benificary.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}

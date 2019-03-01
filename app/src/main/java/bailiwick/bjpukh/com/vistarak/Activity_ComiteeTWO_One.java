package bailiwick.bjpukh.com.vistarak;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

import bailiwick.bjpukh.com.vistarak.Adapter.AdapterComitee;
import bailiwick.bjpukh.com.vistarak.Dialog.CustomDialogAddComitte;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothSpinnerModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Get_Set_Coittee21;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

/**
 * Created by Prince on 17-12-2018.
 */

public class Activity_ComiteeTWO_One extends RootActivity {

    ListView lst_member;
    ArrayList<Get_Set_Coittee21> ListcomiteeMember;
    ProgressDialog prg;
    AdapterComitee adapter;
    Button button_add_more;


    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comitee_two_one);
        createIDS();
        getBoothFromDB();
        Spinne_Click();
        clickEvent();
    }

    private void clickEvent() {
        button_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("i m heree", "spinner position " + spinner_booth.getSelectedItemPosition());
                if (spinner_booth.getSelectedItemPosition() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please select booth", Toast.LENGTH_LONG).show();
                } else {
                    dialog_Comitee();

                }
            }
        });
    }

    private void dialog_Comitee() {

        final CustomDialogAddComitte addComitee = new CustomDialogAddComitte(Activity_ComiteeTWO_One.this);
        addComitee.setCanceledOnTouchOutside(false);
        Window window = addComitee.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        addComitee.show();
        addComitee.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComitee.dismiss();
            }
        });
        addComitee.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_name, str_mobile;
                str_name = addComitee.edt_adyaksh_name.getText().toString().trim();
                str_mobile = addComitee.edt_adyaksh_mobile.getText().toString().trim();

                if (addComitee.spinner_designation.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Designation", Toast.LENGTH_LONG).show();
                    return;
                } else if (str_name.equalsIgnoreCase("")) {
                    addComitee.edt_adyaksh_name.setError("Requried");
                    addComitee.edt_adyaksh_name.requestFocus();
                    return;
                } else if (str_mobile.equalsIgnoreCase("")) {
                    addComitee.edt_adyaksh_mobile.setError("Requried");
                    addComitee.edt_adyaksh_mobile.requestFocus();
                    return;
                } else {
                 //   Toast.makeText(getApplicationContext(), "Api is on process  ..   ", Toast.LENGTH_LONG).show();
                    Log.e("check", "Booth id : " + boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid());
                    saveBootheComitee(str_name, str_mobile, addComitee.spinner_designation.getSelectedItem().toString(), boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME(), addComitee);

                }
            }
        });
    }

    private void saveBootheComitee(final String str_name, final String str_mobile, final String designation, final String booth_id, String user_name, final CustomDialogAddComitte Comiteedialog) {
        prg.setMessage("Add Comitee Member...");
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
                        Comiteedialog.dismiss();
                        get_memberList(booth_id);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    Comiteedialog.dismiss();
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
                Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
                Log.e("Error :", error.toString());
                Comiteedialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddBoothCommitee);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("pramukhName", str_name);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());
                params.put("designation", designation);
                params.put("pramukhMobile", str_mobile);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void Spinne_Click() {
        spinner_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals.get(position - 1).getBid());
                    //  lst_member.setVisibility(View.VISIBLE);

                    get_memberList(boothSpinnerModals.get(position - 1).getBid());
                } else {
                    //    lst_member.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void get_memberList(final String booth_id) {
        prg.setMessage("Get Member");
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
                        JSONArray jsComitee = jsdata.getJSONArray("booth_committee_list");
                        ListcomiteeMember.clear();
                        setSortDetail(jsComitee);


                    } else {
                        ListcomiteeMember.clear();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception ex) {
                    prg.dismiss();
                    //  Comiteedialog.dismiss();
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
                params.put("action", Utils_url.Action_GetBoothCommitee);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void setSortDetail(JSONArray jsComitee) {
        try {
          Log.e("i am heree....","i m heree 1");

            // for Adhyaksh
            for (int i = 0; i < jsComitee.length(); i++) {

                Log.e("i am heree....","values :  loop 1 "+jsComitee.getJSONObject(i).getString("designation"));

                if (jsComitee.getJSONObject(i).getString("designation").equalsIgnoreCase("Adhyaksh")) {
                    ListcomiteeMember.add(new Get_Set_Coittee21("" + i, jsComitee.getJSONObject(i).getString("pramukh_name"), jsComitee.getJSONObject(i).getString("designation"), jsComitee.getJSONObject(i).getString("pramukh_mobile")));
                }

            }
            // for Upadhyaksh
            for (int i = 0; i < jsComitee.length(); i++) {
                if (jsComitee.getJSONObject(i).getString("designation").equalsIgnoreCase("Upadhyaksh")) {
                    ListcomiteeMember.add(new Get_Set_Coittee21("" + i, jsComitee.getJSONObject(i).getString("pramukh_name"), jsComitee.getJSONObject(i).getString("designation"), jsComitee.getJSONObject(i).getString("pramukh_mobile")));
                }

            }
            // for Maha-Mantri
            for (int i = 0; i < jsComitee.length(); i++) {
                if (jsComitee.getJSONObject(i).getString("designation").equalsIgnoreCase("Maha-Mantri")) {
                    ListcomiteeMember.add(new Get_Set_Coittee21("" + i, jsComitee.getJSONObject(i).getString("pramukh_name"), jsComitee.getJSONObject(i).getString("designation"), jsComitee.getJSONObject(i).getString("pramukh_mobile")));
                }

            }
            // for Mantri
            for (int i = 0; i < jsComitee.length(); i++) {
                if (jsComitee.getJSONObject(i).getString("designation").equalsIgnoreCase("Mantri")) {
                    ListcomiteeMember.add(new Get_Set_Coittee21("" + i, jsComitee.getJSONObject(i).getString("pramukh_name"), jsComitee.getJSONObject(i).getString("designation"), jsComitee.getJSONObject(i).getString("pramukh_mobile")));
                }

            }
            // for Sadasya
            for (int i = 0; i < jsComitee.length(); i++) {
                if (jsComitee.getJSONObject(i).getString("designation").equalsIgnoreCase("Sadasya")) {
                    ListcomiteeMember.add(new Get_Set_Coittee21("" + i, jsComitee.getJSONObject(i).getString("pramukh_name"), jsComitee.getJSONObject(i).getString("designation"), jsComitee.getJSONObject(i).getString("pramukh_mobile")));
                }

            }// for Sadasya (Mahila)
            for (int i = 0; i < jsComitee.length(); i++) {
                Log.e("its size","list size after data  in loop "+ListcomiteeMember.size());
                Log.e("i am heree....","values :  loop mahila sadsya  "+jsComitee.getJSONObject(i).getString("designation"));

                if (jsComitee.getJSONObject(i).getString("designation").equalsIgnoreCase("Sadasya (Mahila)")) {
                    ListcomiteeMember.add(new Get_Set_Coittee21("" + i, jsComitee.getJSONObject(i).getString("pramukh_name"), jsComitee.getJSONObject(i).getString("designation"), jsComitee.getJSONObject(i).getString("pramukh_mobile")));
                }

            }


            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();
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


    private void createIDS() {

        prg = new ProgressDialog(this);
        lst_member = (ListView) findViewById(R.id.lst_member);
        button_add_more = (Button) findViewById(R.id.button_add_more);

        ListcomiteeMember = new ArrayList<>();
        adapter = new AdapterComitee(Activity_ComiteeTWO_One.this, R.layout.cst_comitee, ListcomiteeMember);
        lst_member.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(Activity_ComiteeTWO_One.this, R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);
    }
}

package bailiwick.bjpukh.com.vistarak;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
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

public class Add_Key_Voter extends RootActivity implements DatePickerDialog.OnDateSetListener {

    final Calendar c = Calendar.getInstance();
    LinearLayout lnt_ngo, lnt_religious, lnt_army, lnt_shaeed, lnt_influential;
    ArrayList<String> LIST_Key_Voter;
    Spinner spinner_select_key_voter;
    ArrayAdapter Spinner_Key_adapter;
    ProgressDialog prg;
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog;
    //
    ArrayList<String> List_affectivity;
    ArrayAdapter Spinner_Affectivity_adapter;
    ArrayList<String> List_retired_area;
    ArrayAdapter Spinner_retired_adapter;
    private Button button_submit;
    // Ngo Variable
    private EditText edt_ngo_name, edt_ngo_owner_name, edt_ngo_mobile, edt_ngo_workArea;
    private Spinner Spinner_ngo_Affective_Area;
    // Religious Variable
    private EditText edt_religion_name, edt_religion_Owner, edt_religion_mobile, edt_religion_area_of_work;
    private Spinner Spinner_religious_Affective_Area;
    //Ex- Army officer
    private EditText edt_army_head_of_family, edt_army_mobile;
    private Spinner Spinner_army_Affective_Area, Spinner_retired_from;
    // Shaeed Parivar
    private EditText edt_shaeed_name, edt_shaeed_mobile, edt_shaeed_work_in, edt_shaeed_family_member;
    private Spinner Spinner_shaeed_Affective_Area;
    private TextView txt_shaeed_diwas;
    // influential person
    private EditText edt_influential_person_name, edt_influential_mobile, edt_influential_occupation;
    private Spinner Spinner_influence_Affective_Area;
    // Both Spinner
    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;
    TextView mKeyVoter;
    TextInputLayout mGroupName,mNameofOwner,mMobileNumber,mAreaOfWork,mNameOfArmyPerson,mNameOfShaheed,mMobileOfFamily,
            mWorkInDetail,mFamilyMember,mPersonName,mOccupation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_voter);
        createids();
        Create_List();
        spinnerClickEvent();
        click_event();
        createSpinnerAffective();
        Create_SPNR_Retired_from();
        getBoothFromDB();
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



    private void Create_SPNR_Retired_from() {
        List_retired_area = new ArrayList<>();
        List_retired_area.add("Select Retired From");
        List_retired_area.add("Army");
        List_retired_area.add("ITBP");
        List_retired_area.add("BSF");
        List_retired_area.add("CRPF");
        List_retired_area.add("Air Force");
        List_retired_area.add("Navy");
        List_retired_area.add("Other");
        Spinner_retired_adapter = new ArrayAdapter<String>(Add_Key_Voter.this,
                R.layout.spinner_layout_white_text, List_retired_area);
        Spinner_retired_adapter.setDropDownViewResource(R.layout.spinner_layout_white);
        Spinner_retired_from.setAdapter(Spinner_retired_adapter);

    }

    private void createSpinnerAffective() {
        List_affectivity = new ArrayList<>();
        List_affectivity.add("Select Affective Voter");
        List_affectivity.add("A  (100+ Voter)");
        List_affectivity.add("B  (50+ Voter)");
        List_affectivity.add("C  (10+ Voter)");


        Spinner_Affectivity_adapter = new ArrayAdapter<String>(Add_Key_Voter.this,
                R.layout.spinner_layout_white_text, List_affectivity);
        Spinner_Affectivity_adapter.setDropDownViewResource(R.layout.spinner_layout_white);

        Spinner_ngo_Affective_Area.setAdapter(Spinner_Affectivity_adapter);
        Spinner_religious_Affective_Area.setAdapter(Spinner_Affectivity_adapter);
        Spinner_army_Affective_Area.setAdapter(Spinner_Affectivity_adapter);
        Spinner_shaeed_Affective_Area.setAdapter(Spinner_Affectivity_adapter);
        Spinner_influence_Affective_Area.setAdapter(Spinner_Affectivity_adapter);
    }

    private void click_event() {
        txt_shaeed_diwas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), SelectedLanguage[TextUtils.select_booth], Toast.LENGTH_LONG).show();
                    return;
                }
                int position = spinner_select_key_voter.getSelectedItemPosition();
                position = position + 1;
                switch (position) {
                    case 1:
                        ValidationNGO(position);
                        break;
                    case 2:
                        ValidationReligion(position);

                        break;
                    case 3:
                        ValidationArmy(position);

                        break;
                    case 4:
                        ValidationShaeedPariwar(position);

                        break;
                    case 5:
                        ValidationInfluence(position);
                        break;

                }
            }
        });
    }

    private void ValidationInfluence(int position) {
        String influence_name = edt_influential_person_name.getText().toString().trim();
        String influence_mobile = edt_influential_mobile.getText().toString().trim();
        String influence_occupation = edt_influential_occupation.getText().toString().trim();
        //  String influence_affective_area = edt_influential_Affectivity_Area.getText().toString().trim();

        if (influence_name.equalsIgnoreCase("")) {
            edt_influential_person_name.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (influence_mobile.equalsIgnoreCase("")) {
            edt_influential_mobile.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (influence_mobile.length() != 10) {
            edt_influential_mobile.setError(Html
                    .fromHtml("<font color='orange'>Invalid Number</font>"));
            return;
        } else if (influence_occupation.equalsIgnoreCase("")) {
            edt_influential_occupation.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else {
            String influence_affective_area = "NA";
            //  influence_affective_area = Get_affect_count(Spinner_influence_Affective_Area);

            if (CheckNetworkState.isNetworkAvailable(this)) {
                SaveKeyVoter("Please Wait...", position, "influencePerson", "", influence_name, influence_mobile, influence_occupation, influence_affective_area, "", "");

            } else {
                DatabASE_SaveKey_voter("Please Wait...", position, "influencePerson", "", influence_name, influence_mobile, influence_occupation, influence_affective_area, "", "");

            }
        }

    }

    private void ValidationShaeedPariwar(int position) {
        String shaeed_name = edt_shaeed_name.getText().toString().trim();
        String shaeed_mobile = edt_shaeed_mobile.getText().toString().trim();
        String shaeed_retired = edt_shaeed_work_in.getText().toString().trim();
        String shaeed_family_member = edt_shaeed_family_member.getText().toString().trim();
        //  String shaeed_affective = edt_shaeed_Affective_Area.getText().toString().trim();
        String shaeed_diwas = txt_shaeed_diwas.getText().toString().trim();

        if (shaeed_name.equalsIgnoreCase("")) {
            edt_shaeed_name.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (shaeed_mobile.equalsIgnoreCase("")) {
            edt_shaeed_mobile.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (shaeed_mobile.length() != 10) {
            edt_shaeed_mobile.setError(Html
                    .fromHtml("<font color='orange'>Invalid Number</font>"));
            return;
        } else if (shaeed_retired.equalsIgnoreCase("")) {
            edt_shaeed_work_in.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (shaeed_family_member.equalsIgnoreCase("")) {
            edt_shaeed_family_member.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (shaeed_diwas.equalsIgnoreCase("Shaeed Diwas")) {
            Toast.makeText(getApplication(), "Please select Shaeed Diwas Date", Toast.LENGTH_LONG).show();
            return;
        } else {


            String shaeed_affective = "NA";
            //    shaeed_affective = Get_affect_count(Spinner_shaeed_Affective_Area);
            if (CheckNetworkState.isNetworkAvailable(this)) {
                SaveKeyVoter("Please Wait...Add Shaheed Detail", position, "shaheedParivar", "", shaeed_name, shaeed_mobile, shaeed_retired, shaeed_affective, shaeed_family_member, shaeed_diwas);

            } else {

                DatabASE_SaveKey_voter("Please Wait...Add Shaheed Detail", position, "shaheedParivar", "", shaeed_name, shaeed_mobile, shaeed_retired, shaeed_affective, shaeed_family_member, shaeed_diwas);


            }
        }
    }

    private void ValidationArmy(int position) {
        String army_name = edt_army_head_of_family.getText().toString().trim();
        String army_mobile = edt_army_mobile.getText().toString().trim();
        //     String army_retired = edt_army_retired_from.getText().toString().trim();
        //   String army_affective = edt_army_Affective_Area.getText().toString().trim();


        if (army_name.equalsIgnoreCase("")) {
            edt_army_head_of_family.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (army_mobile.equalsIgnoreCase("")) {
            edt_army_mobile.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (army_mobile.length() != 10) {
            edt_army_mobile.setError(Html
                    .fromHtml("<font color='orange'>Invalid Number</font>"));
            return;
        } else if (Spinner_retired_from.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select retired from", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String army_affective = "NA";
            // army_affective = Get_affect_count(Spinner_army_Affective_Area);

            String army_retired = Spinner_retired_from.getSelectedItem().toString();
            if (CheckNetworkState.isNetworkAvailable(this)) {
                SaveKeyVoter("Please Wait...Army officer Detail", position, "exArmy", "", army_name, army_mobile, army_retired, army_affective, "", "");

            } else {

                DatabASE_SaveKey_voter("Please Wait...Army officer Detail", position, "exArmy", "", army_name, army_mobile, army_retired, army_affective, "", "");

            }

        }
    }

    private void ValidationReligion(int position) {
        String religion_name = edt_religion_name.getText().toString().trim();
        String religion_owner_name = edt_religion_Owner.getText().toString().trim();
        String religion_mobile = edt_religion_mobile.getText().toString().trim();
        String religion_work = edt_religion_area_of_work.getText().toString().trim();
//        String religion_affective = edt_religion_Affective_Area.getText().toString().trim();

        if (religion_name.equalsIgnoreCase("")) {
            edt_religion_name.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (religion_owner_name.equalsIgnoreCase("")) {
            edt_religion_Owner.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (religion_mobile.equalsIgnoreCase("")) {
            edt_religion_mobile.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (religion_mobile.length() != 10) {
            edt_religion_mobile.setError(Html
                    .fromHtml("<font color='orange'>Invalid</font>"));
            return;
        } else if (religion_work.equalsIgnoreCase("")) {
            edt_religion_area_of_work.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (Spinner_religious_Affective_Area.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select effected person", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String religion_affective = "";
            religion_affective = Get_affect_count(Spinner_religious_Affective_Area);
            if (CheckNetworkState.isNetworkAvailable(this)) {
                SaveKeyVoter("Please Wait...Saving Religion", position, "religiousGroup", religion_name, religion_owner_name, religion_mobile, religion_work, religion_affective, "", "");

            } else {

                DatabASE_SaveKey_voter("Please Wait...Saving Religion", position, "religiousGroup", religion_name, religion_owner_name, religion_mobile, religion_work, religion_affective, "", "");

            }
        }

    }

    private void ValidationNGO(int position) {
        String ngo_name = edt_ngo_name.getText().toString().trim();
        String ngo_owner_name = edt_ngo_owner_name.getText().toString().trim();
        String ngo_mobile = edt_ngo_mobile.getText().toString().trim();
        String ngo_work = edt_ngo_workArea.getText().toString().trim();
        // String ngo_affective = edt_ngo_Affective_Area.getText().toString().trim();
        if (ngo_name.equalsIgnoreCase("")) {
            edt_ngo_name.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (ngo_owner_name.equalsIgnoreCase("")) {
            edt_ngo_owner_name.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (ngo_mobile.equalsIgnoreCase("")) {
            edt_ngo_mobile.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (ngo_mobile.length() != 10) {
            edt_ngo_mobile.setError(Html
                    .fromHtml("<font color='orange'>Invalid</font>"));
            return;
        } else if (ngo_work.equalsIgnoreCase("")) {
            edt_ngo_workArea.setError(Html
                    .fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (Spinner_ngo_Affective_Area.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select effected person", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String ngo_affective = "";
            ngo_affective = Get_affect_count(Spinner_ngo_Affective_Area);
            if (CheckNetworkState.isNetworkAvailable(this)) {
                SaveKeyVoter("Please Wait...Saving NGO", position, "ngo", ngo_name, ngo_owner_name, ngo_mobile, ngo_work, ngo_affective, "", "");

            } else {

                DatabASE_SaveKey_voter("Please Wait...Saving NGO", position, "ngo", ngo_name, ngo_owner_name, ngo_mobile, ngo_work, ngo_affective, "", "");

            }
        }

    }

    private void DatabASE_SaveKey_voter(String message, final int position, final String Key_voter, final String ngo_name, final String ngo_owner_name, final String ngo_mobile, final String ngo_work, final String ngo_affective, final String family_member, final String ShaeedDiwas) {

        Log.e("in Data Base ", "In Data BAse");
        try {
            boolean isSuccess = false;
            if (position == 1) {
                isSuccess = DBOperation.insertNGO(Add_Key_Voter.this,
                        SavedData.getUSER_NAME(), boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getLattitudeLocation(),
                        SavedData.getLongitudeLocation(), Key_voter, ngo_owner_name, ngo_name, ngo_work, ngo_affective, ngo_mobile);

            }
            if (position == 2) {


                isSuccess = DBOperation.insertReligious(Add_Key_Voter.this,
                        SavedData.getUSER_NAME(), boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getLattitudeLocation(),
                        SavedData.getLongitudeLocation(), Key_voter, ngo_owner_name, ngo_name, ngo_work, ngo_affective, ngo_mobile);
            }

            if (position == 3) {


                isSuccess = DBOperation.insertArmy(Add_Key_Voter.this,
                        SavedData.getUSER_NAME(), boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getLattitudeLocation(),
                        SavedData.getLongitudeLocation(), Key_voter, ngo_owner_name, ngo_affective, ngo_work, ngo_mobile);
            }
            if (position == 4) {


                isSuccess = DBOperation.insertShaheed(Add_Key_Voter.this,
                        SavedData.getUSER_NAME(), boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getLattitudeLocation(),
                        SavedData.getLongitudeLocation(), Key_voter, ngo_owner_name, family_member, ShaeedDiwas, ngo_mobile, ngo_work);
            }
            if (position == 5) {


                isSuccess = DBOperation.insertInfluence(Add_Key_Voter.this,
                        SavedData.getUSER_NAME(), boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getLattitudeLocation(),
                        SavedData.getLongitudeLocation(), Key_voter, ngo_name, ngo_work, ngo_affective, ngo_mobile);

            }
            if (isSuccess) {
                Toast.makeText(Add_Key_Voter.this, "Saved in Database", Toast.LENGTH_SHORT).show();
                ClearAll();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String Get_affect_count(Spinner spinner) {
        String string_count = "";
        switch (spinner.getSelectedItemPosition()) {
            case 1:
                string_count = "100";
                break;
            case 2:
                string_count = "50";

                break;
            case 3:
                string_count = "10";

                break;

        }
        return string_count;

    }

    private void SaveKeyVoter(String message, final int position, final String Key_voter, final String ngo_name, final String ngo_owner_name, final String ngo_mobile, final String ngo_work, final String ngo_affective, final String family_member, final String ShaeedDiwas) {
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
                                ClearAll();
                                //Clear_Detail();
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
                Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_AddkeyVoter);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid());
                params.put("voterType", Key_voter);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());

                if (position == 1) {
                    params.put("owner_name", ngo_owner_name);
                    params.put("ngo_name", ngo_name);
                    params.put("area_of_work", ngo_work);
                    params.put("affectivity", ngo_affective);
                    params.put("owner_mobile", ngo_mobile);

                }
                if (position == 2) {
                    params.put("owner_name", ngo_owner_name);
                    params.put("group_name", ngo_name);
                    params.put("area_of_work", ngo_work);
                    params.put("affectivity", ngo_affective);
                    params.put("owner_mobile", ngo_mobile);

                }
                if (position == 3) {
                    params.put("owner_name", ngo_owner_name);
                    params.put("total_family_member", ngo_affective);
                    params.put("retired_from", ngo_work);
                    params.put("owner_mobile", ngo_mobile);

                }
                if (position == 4) {
                    params.put("shaheed_name", ngo_owner_name);
                    params.put("total_family_member", family_member);
                    params.put("retired_from", ShaeedDiwas);
                    params.put("shaheed_mobile", ngo_mobile);
                    params.put("work_for", ngo_work);
                    params.put("shaheed_mobile", ngo_mobile);

                }
                if (position == 5) {


                    params.put("owner_name", ngo_name);
                    params.put("occupation", ngo_work);
                    params.put("affectivity", ngo_affective);
                    params.put("mobile", ngo_mobile);

                }


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }


    private void spinnerClickEvent() {
        spinner_select_key_voter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLayoutVisiblity(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setLayoutVisiblity(int position) {

        switch (position) {
            case 1:
                lnt_ngo.setVisibility(View.VISIBLE);
                lnt_religious.setVisibility(View.INVISIBLE);
                lnt_army.setVisibility(View.INVISIBLE);
                lnt_shaeed.setVisibility(View.INVISIBLE);
                lnt_influential.setVisibility(View.INVISIBLE);

                break;
            case 2:
                lnt_ngo.setVisibility(View.INVISIBLE);
                lnt_religious.setVisibility(View.VISIBLE);
                lnt_army.setVisibility(View.INVISIBLE);
                lnt_shaeed.setVisibility(View.INVISIBLE);
                lnt_influential.setVisibility(View.INVISIBLE);

                break;
            case 3:
                lnt_ngo.setVisibility(View.INVISIBLE);
                lnt_religious.setVisibility(View.INVISIBLE);
                lnt_army.setVisibility(View.VISIBLE);
                lnt_shaeed.setVisibility(View.INVISIBLE);
                lnt_influential.setVisibility(View.INVISIBLE);

                break;
            case 4:
                lnt_ngo.setVisibility(View.INVISIBLE);
                lnt_religious.setVisibility(View.INVISIBLE);
                lnt_army.setVisibility(View.INVISIBLE);
                lnt_shaeed.setVisibility(View.VISIBLE);
                lnt_influential.setVisibility(View.INVISIBLE);

                break;
            case 5:
                lnt_ngo.setVisibility(View.INVISIBLE);
                lnt_religious.setVisibility(View.INVISIBLE);
                lnt_army.setVisibility(View.INVISIBLE);
                lnt_shaeed.setVisibility(View.INVISIBLE);
                lnt_influential.setVisibility(View.VISIBLE);

                break;
            default:

        }

    }

    private void Create_List() {
        LIST_Key_Voter = new ArrayList<>();
        LIST_Key_Voter.add("N.G.O");
        LIST_Key_Voter.add("Religious Group");
        LIST_Key_Voter.add("Ex- Army");
        LIST_Key_Voter.add("Shaheed Pariwar");
        LIST_Key_Voter.add("Other");
        Spinner_Key_adapter = new ArrayAdapter<String>(Add_Key_Voter.this,
                R.layout.spinner_layout_white_text, LIST_Key_Voter);
        Spinner_Key_adapter.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_select_key_voter.setAdapter(Spinner_Key_adapter);

    }

    private void createids() {
        prg = new ProgressDialog(this);
        datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        spinner_select_key_voter = (Spinner) findViewById(R.id.spinner_select_key_voter);
        lnt_ngo = (LinearLayout) findViewById(R.id.lnt_ngo);
        lnt_religious = (LinearLayout) findViewById(R.id.lnt_religious);
        lnt_army = (LinearLayout) findViewById(R.id.lnt_army);
        lnt_shaeed = (LinearLayout) findViewById(R.id.lnt_shaeed);
        lnt_influential = (LinearLayout) findViewById(R.id.lnt_influential);

        button_submit = (Button) findViewById(R.id.button_submit);
        // NGO IDS
        edt_ngo_name = (EditText) findViewById(R.id.edt_ngo_name);
        edt_ngo_owner_name = (EditText) findViewById(R.id.edt_ngo_owner_name);
        edt_ngo_mobile = (EditText) findViewById(R.id.edt_ngo_mobile);
        edt_ngo_workArea = (EditText) findViewById(R.id.edt_ngo_workArea);
        Spinner_ngo_Affective_Area = (Spinner) findViewById(R.id.Spinner_ngo_Affective_Area);

        // Religion IDS
        edt_religion_name = (EditText) findViewById(R.id.edt_religion_name);
        edt_religion_Owner = (EditText) findViewById(R.id.edt_religion_Owner);
        edt_religion_mobile = (EditText) findViewById(R.id.edt_religion_mobile);
        edt_religion_area_of_work = (EditText) findViewById(R.id.edt_religion_area_of_work);
        Spinner_religious_Affective_Area = (Spinner) findViewById(R.id.Spinner_religious_Affective_Area);
        // EX- Army Officer
        edt_army_head_of_family = (EditText) findViewById(R.id.edt_army_head_of_family);
        edt_army_mobile = (EditText) findViewById(R.id.edt_army_mobile);
        Spinner_retired_from = (Spinner) findViewById(R.id.Spinner_retired_from);
        Spinner_army_Affective_Area = (Spinner) findViewById(R.id.Spinner_army_Affective_Area);

        // Shaeed- Pariwar
        edt_shaeed_name = (EditText) findViewById(R.id.edt_shaeed_name);
        edt_shaeed_mobile = (EditText) findViewById(R.id.edt_shaeed_mobile);
        edt_shaeed_work_in = (EditText) findViewById(R.id.edt_shaeed_work_in);
        edt_shaeed_family_member = (EditText) findViewById(R.id.edt_shaeed_family_member);
        Spinner_shaeed_Affective_Area = (Spinner) findViewById(R.id.Spinner_shaeed_Affective_Area);
        txt_shaeed_diwas = (TextView) findViewById(R.id.txt_shaeed_diwas);
// influence member
        edt_influential_person_name = (EditText) findViewById(R.id.edt_influential_person_name);
        edt_influential_mobile = (EditText) findViewById(R.id.edt_influential_mobile);
        edt_influential_occupation = (EditText) findViewById(R.id.edt_influential_occupation);
        Spinner_influence_Affective_Area = (Spinner) findViewById(R.id.Spinner_influence_Affective_Area);

        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(Add_Key_Voter.this,
                R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);
        setLanguage();


    }

    private void setLanguage() {
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());
        mKeyVoter = (TextView)findViewById(R.id.text_key_voter);
        mGroupName = (TextInputLayout)findViewById(R.id.til_group_name);
        mNameofOwner = (TextInputLayout)findViewById(R.id.til_name_of_owner);
        mMobileNumber = (TextInputLayout)findViewById(R.id.til_mobile_number);
        mAreaOfWork = (TextInputLayout)findViewById(R.id.til_area_of_work);
        mNameOfArmyPerson = (TextInputLayout)findViewById(R.id.til_name_of_army_person);
        mNameOfShaheed = (TextInputLayout)findViewById(R.id.til_name_of_shaheed);
        mMobileOfFamily = (TextInputLayout)findViewById(R.id.til_mobile_number_of_family);
        mWorkInDetail = (TextInputLayout)findViewById(R.id.til_work_in_detail);
        mPersonName = (TextInputLayout)findViewById(R.id.til_person_name);
        mOccupation = (TextInputLayout)findViewById(R.id.til_occupation);
        mFamilyMember = (TextInputLayout)findViewById(R.id.til_family_members);


        mKeyVoter.setText(SelectedLanguage[TextUtils.key_voter]);
        mGroupName.setHint(SelectedLanguage[TextUtils.group_name]);
        mNameofOwner.setHint(SelectedLanguage[TextUtils.name_of_owner]);
        mMobileNumber.setHint(SelectedLanguage[TextUtils.mobile]);
        mAreaOfWork.setHint(SelectedLanguage[TextUtils.area_of_work]);
        mNameOfArmyPerson.setHint(SelectedLanguage[TextUtils.name_of_army_person]);
        mNameOfShaheed.setHint(SelectedLanguage[TextUtils.name_of_shaheed]);
        mMobileOfFamily.setHint(SelectedLanguage[TextUtils.mobile_number_of_family]);
        mWorkInDetail.setHint(SelectedLanguage[TextUtils.work_in_detail]);
        mPersonName.setHint(SelectedLanguage[TextUtils.person_name]);
        mOccupation.setHint(SelectedLanguage[TextUtils.occupation]);
        mFamilyMember.setHint(SelectedLanguage[TextUtils.family_member]);
        button_submit.setText(SelectedLanguage[TextUtils.submit]);



    }

    private void ClearAll() {

        edt_ngo_name.setText("");
        edt_ngo_owner_name.setText("");
        edt_ngo_mobile.setText("");
        edt_ngo_workArea.setText("");
//        edt_ngo_Affective_Area.setText("");
        Spinner_ngo_Affective_Area.setSelection(0);
        //
        edt_religion_name.setText("");
        edt_religion_Owner.setText("");
        edt_religion_mobile.setText("");
        edt_religion_area_of_work.setText("");
        Spinner_religious_Affective_Area.setSelection(0);
        //
        edt_army_head_of_family.setText("");
        edt_army_mobile.setText("");
        Spinner_retired_from.setSelection(0);
        Spinner_army_Affective_Area.setSelection(0);
        //
        edt_shaeed_name.setText("");
        edt_shaeed_mobile.setText("");
        edt_shaeed_work_in.setText("");
        edt_shaeed_family_member.setText("");
        Spinner_shaeed_Affective_Area.setSelection(0);
        txt_shaeed_diwas.setText("Shaeed Diwas");
        //
        edt_influential_person_name.setText("");
        edt_influential_mobile.setText("");
        edt_influential_occupation.setText("");
        Spinner_influence_Affective_Area.setSelection(0);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        txt_shaeed_diwas.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
    }
}
package bailiwick.bjpukh.com.vistarak.db;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Itag;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;

/**
 * Created by Prince on 26-09-2017.
 */

public class Upload_operation {
    private static final String TAG = "backend_upload";
    private SharedPreferences pref;


    // Kameti Member
    public static void SaveKametiMember(final Context con, final ArrayList<KametiModel> listKameti_member) {
        if (listKameti_member.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    String entry_no = listKameti_member.get(0).getEntry_no();
                                    boolean isSuccess = DBOperation.deleteKameti_member(con, entry_no);
                                    Log.e("Deleted ", "" + isSuccess);
                                    //             // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                } else {
                                    //           // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception ex) {
                                SavedData.saveOperation_status(false);
                                ex.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Log.e("Error :", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
////////////////

                    if ((listKameti_member.get(0).getValuetype().equalsIgnoreCase(Itag.new_entry))) {
                        params.put("action", Utils_url.Action_ADDKameti_Member);

                    } else {
                        params.put("action", Utils_url.Action_UPDATEKameti_Member);

                    }
                    params.put("vistarak_id", SavedData.getUSER_NAME());
                    params.put("booth_id", listKameti_member.get(0).getVistarak_id());
                    params.put("adyaksh_name", listKameti_member.get(0).getStr_adykash_name());
                    params.put("latitude", SavedData.getLattitudeLocation());
                    params.put("longitude", SavedData.getLongitudeLocation());
                    params.put("adyaksh_contact", listKameti_member.get(0).getStr_adykash_mobile());

                    params.put("palak_name", listKameti_member.get(0).getStr_palak_name());
                    params.put("palak_contact", listKameti_member.get(0).getStr_palak_mobile());

                    params.put("bla_name", listKameti_member.get(0).getStr_bla_name());
                    params.put("bla_contact", listKameti_member.get(0).getStr_bla_mobile());


                    Log.e("DatABse Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        } else {
            SavedData.saveOperation_status(false);
        }

//        queue.add(stringRequest);


    }

    // Smart Phone User
    public static void SaveSmartPhone(final Context con, final ArrayList<SmartPhoneModel> listSmartPhoneUser) {

        if (listSmartPhoneUser.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    String entry_no = listSmartPhoneUser.get(0).getEntry_no();
                                    boolean isSuccess = DBOperation.deleteSmart_Phone(con, entry_no);
                                    Log.e("Deleted ", "" + isSuccess);
                                    //             // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                } else {
                                    //           // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception ex) {
                                SavedData.saveOperation_status(false);
                                ex.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Log.e("Error :", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
////////////////

                    params.put("action", Utils_url.Action_AddSmartPhone);
                    params.put("vistarak_id", listSmartPhoneUser.get(0).getVistarak_id());
                    params.put("booth_id", listSmartPhoneUser.get(0).getBoothID());
                    params.put("member_name", listSmartPhoneUser.get(0).getPerson_name());
                    params.put("latitude", listSmartPhoneUser.get(0).getLattitudeLocation());
                    params.put("longitude", listSmartPhoneUser.get(0).getLongitudeLocation());
                    params.put("member_contact", listSmartPhoneUser.get(0).getContact());

                    Log.e("DatABse Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        } else {
            SavedData.saveOperation_status(false);
        }

//        queue.add(stringRequest);
    }


    // Panna Pramukh
    public static void SavePannaPramukh(final Context con, final ArrayList<PannaPramukhModel> listPannaPramukh) {

        if (listPannaPramukh.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    String entry_no = listPannaPramukh.get(0).getStr_entry_no();
                                    boolean isSuccess = DBOperation.deletePannaPramukh(con, entry_no);
                                    Log.e("Deleted ", "" + isSuccess);
                                    //             // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                } else {
                                    //           // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception ex) {
                                SavedData.saveOperation_status(false);
                                ex.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Log.e("Error :", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", Utils_url.Action_AddPannaPramukh);
                    params.put("vistarak_id", listPannaPramukh.get(0).getStr_vistarakID());
                    params.put("booth_id", listPannaPramukh.get(0).getStr_booth_id());
                    params.put("latitude", listPannaPramukh.get(0).getStr_lat());
                    params.put("longitude", listPannaPramukh.get(0).getStr_lan());
                    params.put("pramukhName", listPannaPramukh.get(0).getStr_pramukh_name());
                    params.put("address", listPannaPramukh.get(0).getAddress());
                    params.put("pramukhMobile", listPannaPramukh.get(0).getStr_mobile());
////////////////

                    Log.e("Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        } else {
            SavedData.saveOperation_status(false);
        }

//        queue.add(stringRequest);
    }

    public static void SaveHome_visit(final Context con, final ArrayList<Home_visitModal> listHomeVisit) {

        if (listHomeVisit.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    String entry_no = listHomeVisit.get(0).getStr_entery_no();
                                    boolean isSuccess = DBOperation.deleteHome_visit(con, entry_no);
                                    Log.e("Deleted ", "" + isSuccess);
                                    //             // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                } else {
                                    //           // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception ex) {
                                SavedData.saveOperation_status(false);
                                ex.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Log.e("Error :", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", Utils_url.Action_AddHomeVisit);
                    params.put("vistarak_id", listHomeVisit.get(0).getStr_vistarak_id());
                    params.put("booth_id", listHomeVisit.get(0).getStr_booth_id());
                    params.put("latitude", listHomeVisit.get(0).getStr_lat());
                    params.put("longitude", listHomeVisit.get(0).getStr_Lan());
                    params.put("head_name", listHomeVisit.get(0).getStr_head_name());
                    params.put("family_member", listHomeVisit.get(0).getStr_family_member());
                    params.put("address", listHomeVisit.get(0).getStr_address());
                    params.put("Mobile", listHomeVisit.get(0).getMobile());

                    String entryType = listHomeVisit.get(0).getStr_visit_type();
                    if (entryType.equalsIgnoreCase("new")) {
                        params.put("total_voter", listHomeVisit.get(0).getStr_total_voter());
                        params.put("head_voter_id", "NA");
                        params.put("visitType", "new");


                    } else {
                        params.put("visitType", "old");
                        params.put("membership_id", listHomeVisit.get(0).getStr_membership_id());

                    }
                    Log.e("Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        } else {
            SavedData.saveOperation_status(false);
        }

//        queue.add(stringRequest);
    }

    public static void SaveManPramukh(final Context con, final ArrayList<Man_Ki_BaatModal> listManKiBaat) {
        if (listManKiBaat.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    //// Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listManKiBaat.get(0).getStr_entry_no();

                                    Boolean isSuccess = DBOperation.deleteMan_ki_baat(con, entry_no);
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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
                    params.put("action", Utils_url.Action_AddMankiBaat);

                    params.put("vistarak_id", listManKiBaat.get(0).getStr_vistarak_id());
                    params.put("booth_id", listManKiBaat.get(0).getStr_booth_id());
                    params.put("pramukhName", listManKiBaat.get(0).getStr_pramukh_name());
                    params.put("latitude", listManKiBaat.get(0).getStr_lat());
                    params.put("longitude", listManKiBaat.get(0).getStr_Lan());
                    params.put("pramukhMobile", listManKiBaat.get(0).getStr_pramukh_mobile());

                    Log.e("Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveSpecialArea(final Context con, final ArrayList<Special_AreaModal> listSpecialArea) {
        if (listSpecialArea.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    //// Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listSpecialArea.get(0).getStr_entry_no();

                                    Boolean isSuccess = DBOperation.deleteSpecial_Area(con, entry_no);
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    //     // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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
                    params.put("action", Utils_url.Action_AddSpecial_Area);

                    params.put("vistarak_id", listSpecialArea.get(0).getStr_vistarak_id());
                    params.put("booth_id", listSpecialArea.get(0).getStr_booth_id());
                    params.put("priorCategory", listSpecialArea.get(0).getStr_prior_catagory());
                    params.put("latitude", listSpecialArea.get(0).getStr_lat());
                    params.put("longitude", listSpecialArea.get(0).getStr_lan());
                    params.put("no_of_famliy", listSpecialArea.get(0).getStr_Family_count());
                    params.put("type", listSpecialArea.get(0).getStr_area_type());

                    Log.e("Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveWhatsApp(final Context con, final ArrayList<Whats_AppModal> listWhatsApp) {
        if (listWhatsApp.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listWhatsApp.get(0).getStr_entry_no();

                                    Boolean isSuccess = DBOperation.deleteWhatsApp(con, entry_no);
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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
                    params.put("action", Utils_url.Action_AddWhatsAppGroup);

                    params.put("vistarak_id", listWhatsApp.get(0).getStr_vistarakID());
                    params.put("booth_id", listWhatsApp.get(0).getStr_booth_id());
                    params.put("pramukhName", listWhatsApp.get(0).getStr_pramukh_name());
                    params.put("latitude", listWhatsApp.get(0).getStr_lat());
                    params.put("longitude", listWhatsApp.get(0).getStr_lan());
                    params.put("pramukhMobile", listWhatsApp.get(0).getStr_mobile());
                    params.put("groupName", listWhatsApp.get(0).getStr_group_name());
                    Log.e("Param Response ", "" + params);


                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveSwachta(final Context con, final ArrayList<SwachtaModel> listSwachta) {
        if (listSwachta.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listSwachta.get(0).getStr_entry_no();

                                    Boolean isSuccess = DBOperation.deleteSwachta(con, entry_no);
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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
                    params.put("action", Utils_url.Action_AddSwachtaInBooth);

                    params.put("vistarak_id", listSwachta.get(0).getStr_vistarakID());
                    params.put("booth_id", listSwachta.get(0).getStr_booth_id());
                    params.put("pramukhName", listSwachta.get(0).getStr_pramukh_name());
                    params.put("latitude", listSwachta.get(0).getStr_lat());
                    params.put("longitude", listSwachta.get(0).getStr_lan());
                    params.put("pramukhMobile", listSwachta.get(0).getStr_mobile());

                    Log.e("Param Response ", "" + params);


                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveNgo(final Context con, final ArrayList<Ngo_Model> listNGO, final int position) {
        if (listNGO.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listNGO.get(0).getStr_entry_no();
                                    Boolean isSuccess = false;
                                    if (position == 1) {
                                        isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.key_voter_NGO_table);

                                    }
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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


                    params.put("action", Utils_url.Action_AddkeyVoter);
                    params.put("vistarak_id", listNGO.get(0).getStr_Vistarak_id());
                    params.put("booth_id", listNGO.get(0).getStr_boothID());
                    params.put("voterType", listNGO.get(0).getStr_voter_type());
                    params.put("latitude", listNGO.get(0).getStr_lat());
                    params.put("longitude", listNGO.get(0).getStr_lan());
                    params.put("owner_name", listNGO.get(0).getStr_owner_name());
                    params.put("ngo_name", listNGO.get(0).getStr_ngo_name());
                    params.put("area_of_work", listNGO.get(0).getStr_area_work());
                    params.put("affectivity", listNGO.get(0).getStr_affectivity());
                    params.put("owner_mobile", listNGO.get(0).getStr_mobile());


                    Log.e("Param Response ", "" + params);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveReligious(final Context con, final ArrayList<Religious_Model> listReligious, final int position) {
        if (listReligious.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listReligious.get(0).getStr_entry_no();
                                    Boolean isSuccess = false;
                                    if (position == 1) {
                                        isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.key_voter_Religious_table);

                                    }
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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


                    params.put("action", Utils_url.Action_AddkeyVoter);
                    params.put("vistarak_id", listReligious.get(0).getStr_vistarak_id());
                    params.put("booth_id", listReligious.get(0).getStr_boothID());
                    params.put("voterType", listReligious.get(0).getStr_voter_type());
                    params.put("latitude", listReligious.get(0).getStr_lat());
                    params.put("longitude", listReligious.get(0).getStr_lan());
                    params.put("owner_name", listReligious.get(0).getStr_owner_name());
                    params.put("group_name", listReligious.get(0).getStr_relg_name());
                    params.put("area_of_work", listReligious.get(0).getStr_area_work());
                    params.put("affectivity", listReligious.get(0).getStr_affectivity());
                    params.put("owner_mobile", listReligious.get(0).getStr_mobile());


                    Log.e("Param Response ", "" + params);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveArmy(final Context con, final ArrayList<Army_Model> listArmy, final int position) {
        if (listArmy.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listArmy.get(0).getStr_entry_no();
                                    Boolean isSuccess = false;
                                    if (position == 1) {
                                        isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.key_voter_EX_Army_table);

                                    }
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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


                    params.put("action", Utils_url.Action_AddkeyVoter);
                    params.put("vistarak_id", listArmy.get(0).getStr_vistarkID());
                    params.put("booth_id", listArmy.get(0).getStr_boothID());
                    params.put("voterType", listArmy.get(0).getStr_voter_type());
                    params.put("latitude", listArmy.get(0).getStr_lat());
                    params.put("longitude", listArmy.get(0).getStr_lan());
                    params.put("owner_name", listArmy.get(0).getStr_army_person());
                    params.put("total_family_member", listArmy.get(0).getStr_Army_family_mem());
                    params.put("retired_from", listArmy.get(0).getStr_retried_from());
                    params.put("owner_mobile", listArmy.get(0).getStr_army_mobile());


                    Log.e("Param Response ", "" + params);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveShaheed(final Context con, final ArrayList<Shaheed_Model> listshaheed, final int position) {
        if (listshaheed.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listshaheed.get(0).getStr_entry();
                                    Boolean isSuccess = false;
                                    if (position == 1) {
                                        isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.key_voter_Shaheed__table);

                                    }
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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


                    params.put("action", Utils_url.Action_AddkeyVoter);
                    params.put("vistarak_id", listshaheed.get(0).getStr_vistarakID());
                    params.put("booth_id", listshaheed.get(0).getStr_boothID());
                    params.put("voterType", listshaheed.get(0).getStr_voter_type());
                    params.put("latitude", listshaheed.get(0).getStr_lat());
                    params.put("longitude", listshaheed.get(0).getStr_lan());

                    params.put("shaheed_name", listshaheed.get(0).getStr_shaheed_name());
                    params.put("total_family_member", listshaheed.get(0).getStr_family_mem());
                    params.put("retired_from", listshaheed.get(0).getStr_retried_from());
                    params.put("shaheed_mobile", listshaheed.get(0).getStr_mobile());
                    params.put("work_for", listshaheed.get(0).getStr_work_for());


                    Log.e("Param Response ", "" + params);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveInfluence(final Context con, final ArrayList<Influence_Model> listInfluence, final int position) {
        if (listInfluence.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listInfluence.get(0).getStr_entry_no();
                                    Boolean isSuccess = false;
                                    if (position == 1) {
                                        isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.key_voter_other_table);

                                    }
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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


                    params.put("action", Utils_url.Action_AddkeyVoter);
                    params.put("vistarak_id", listInfluence.get(0).getStr_vistarakID());
                    params.put("booth_id", listInfluence.get(0).getStr_boothID());
                    params.put("voterType", listInfluence.get(0).getStr_voter_Type());
                    params.put("latitude", listInfluence.get(0).getStr_lat());
                    params.put("longitude", listInfluence.get(0).getStr_lan());
                    params.put("owner_name", listInfluence.get(0).getStr_person_name());
                    params.put("occupation", listInfluence.get(0).getStr_occupation());
                    params.put("affectivity", listInfluence.get(0).getStr_affectivity());
                    params.put("mobile", listInfluence.get(0).getStr_mobile());


                    Log.e("Param Response ", "" + params);

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    // jsson Data
    public static void SaveJssonManPramukh(final Context con, ArrayList<Man_Ki_BaatModal> listManKiBaat) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();


        String JsonString = "";

        try {
            for (int i = 0; i < listManKiBaat.size(); i++) {
                JSONObject jsOBJ = new JSONObject();
                jsOBJ.put("vistarak_id", listManKiBaat.get(i).getStr_vistarak_id());
                jsOBJ.put("booth_id", listManKiBaat.get(i).getStr_booth_id());
                jsOBJ.put("pramukhName", listManKiBaat.get(i).getStr_pramukh_name());
                jsOBJ.put("latitude", listManKiBaat.get(i).getStr_lat());
                jsOBJ.put("longitude", listManKiBaat.get(i).getStr_Lan());
                jsOBJ.put("pramukhMobile", listManKiBaat.get(i).getStr_pramukh_mobile());
                jsonArray.put(jsOBJ);

            }
            Log.e(TAG, "Array " + jsonArray.toString());
            JsonString = jsonArray.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                                // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                Boolean isSuccess = DBOperation.deleteAll_man_ki_baat(con);
                                Log.e("Success", "" + isSuccess);

                                //Clear_Detail();
                            } else {
                                // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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
                params.put("action", Utils_url.Action_AddMankiBaat);
                params.put("MankiBaat", finalJsonString);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }


    public static void SaveBike(final Context con, final ArrayList<Bike_Model> listBike) {
        if (listBike.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listBike.get(0).getStr_entry_no();

                                    Boolean isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.Add_Bike_table);
                                    Log.e("Success", "" + isSuccess);

                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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
                    params.put("action", Utils_url.Action_AddVechicle);
                    params.put("vistarak_id", listBike.get(0).getStr_vistarakID());
                    params.put("booth_id", listBike.get(0).getStr_boothID());
                    params.put("memberName", listBike.get(0).getStr_PramukhName());
                    params.put("latitude", listBike.get(0).getStr_lat());
                    params.put("longitude", listBike.get(0).getStr_lan());
                    params.put("vehicle_no", listBike.get(0).getStr_vechicle_no());
                    params.put("memberContact", listBike.get(0).getStr_PramukhMobile());

                    Log.e("Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    public static void SaveMeeting(final Context con, final ArrayList<BoothMeeting_modal> listMeeting_booth) {
        if (listMeeting_booth.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    SavedData.saveOperation_status(false);
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                    String entry_no = listMeeting_booth.get(0).getStr_entry_no();
                                    byte image_path[] = listMeeting_booth.get(0).getStr_path();
                                    if (image_path == null) {
                                        Boolean isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.booth_meeting_table);
                                        Log.e("Success", "" + isSuccess);

                                    } else {

                                        String meetingId = jsdata.getString("meetingId");
                                        String base64 = Base64.encodeToString(image_path,
                                                Base64.NO_WRAP);
                                        UploadImage(meetingId, base64, entry_no, con);
                                    }
                                  /*  Boolean isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.Add_Bike_table);
                                    Log.e("Success", "" + isSuccess);
*/
                                    //Clear_Detail();
                                } else {
                                    // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

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

                    params.put("action", Utils_url.Action_AddMeeting);
                    params.put("vistarak_id", listMeeting_booth.get(0).getStr_VistarakID());
                    params.put("booth_id", listMeeting_booth.get(0).getStr_BoothID());
                    params.put("male_member", listMeeting_booth.get(0).getStr_mal_mem());
                    params.put("latitude", listMeeting_booth.get(0).getStr_lat());
                    params.put("longitude", listMeeting_booth.get(0).getStr_lan());
                    params.put("female_member", listMeeting_booth.get(0).getStr_female_mem());
                    params.put("total_member", listMeeting_booth.get(0).getStr_total_MEM());

                    Log.e("Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
        } else {
            SavedData.saveOperation_status(false);
        }
    }

    // Upload Image
    private static void UploadImage(final String meetingId, final String base64, final String entry_no, final Context con) {


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
                                Boolean isSuccess = DBOperation.deleteNGO(con, entry_no, Database_Utils.booth_meeting_table);
                                Log.e("Upload pic db", "Upload pic db" + isSuccess);

                            } else {
                                // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.e("Error :", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_UpdateMeeting);
                params.put("meetingId", meetingId);
                params.put("image", "data:image/jpeg;base64," + base64);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    public static void SaveAnusuchit(final Context con,final  ArrayList<AnusuchitModel> listAnusuchit_member) {

        if (listAnusuchit_member.size() > 0) {
            SavedData.saveOperation_status(true);

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
                                    String entry_no = listAnusuchit_member.get(0).getEntry_no();
                                    boolean isSuccess = DBOperation.deleteAnusuchit(con, entry_no);
                                    Log.e("Deleted ", "" + isSuccess);
                                    //             // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                                } else {
                                    //           // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception ex) {
                                SavedData.saveOperation_status(false);
                                ex.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Log.e("Error :", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
////////////////

                    params.put("action", Utils_url.Action_AddAnusuchitActivity);
                    params.put("vistarak_id", listAnusuchit_member.get(0).getVistarak_id());
                    params.put("booth_id", listAnusuchit_member.get(0).getBoothID());
                    params.put("pramukhName", listAnusuchit_member.get(0).getPerson_name());
                    params.put("latitude", listAnusuchit_member.get(0).getLattitudeLocation());
                    params.put("longitude", listAnusuchit_member.get(0).getLongitudeLocation());
                    params.put("pramukhMobile", listAnusuchit_member.get(0).getContact());
                    params.put("category", listAnusuchit_member.get(0).getCatagory());



                    Log.e("DatABse Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        } else {
            SavedData.saveOperation_status(false);
        }

    }

    public static void SaveEvent_pramukh(final Context con,final  ArrayList<Event_Pramukh_model> mlist) {

        if (mlist.size() > 0) {
            SavedData.saveOperation_status(true);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils_url.Base_Url, new Response.Listener<String>() {
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
                            String entry_no = mlist.get(0).getStr_entry_no();
                            boolean isSuccess = DBOperation.deleteEventPramukh(con, entry_no);
                            Log.e("Deleted ", "" + isSuccess);
                            //             // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();
                        } else {
                            //           // Toast.makeText(con, msg, // Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception ex) {
                        SavedData.saveOperation_status(false);
                        ex.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Log.e("Error :", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
////////////////

                    params.put("action", Utils_url.Action_AddEventPramukh);
                    params.put("vistarak_id", mlist.get(0).getStr_vistarakID());
                    params.put("booth_id", mlist.get(0).getStr_boothID());
                    params.put("EpramukhName", mlist.get(0).getStr_PramukhName());
                    params.put("latitude", mlist.get(0).getStr_lat());
                    params.put("longitude", mlist.get(0).getStr_lon());
                    params.put("EpramukhMobile", mlist.get(0).getStr_PramukhMobile());


                    Log.e("DatABse Param Response ", "" + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        } else {
            SavedData.saveOperation_status(false);
        }

    }
}

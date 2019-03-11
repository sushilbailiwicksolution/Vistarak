package bailiwick.bjpukh.com.vistarak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Getter_Setter.District_model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Level_model;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;
import bailiwick.bjpukh.com.vistarak.db.DBOperation;

/**
 * Created by Prince on 01-03-2019.
 */

public class Shakti_Kendra extends RootActivity {

    Spinner spinner_shakti_kendra, spinner_district, spinner_level, spinner_blood;
    ImageView img_profile;
    Button button_submit;
    EditText edt_mobile, edt_name, edt_booth_name;

    Bitmap bitmap;
    String myBase64Image;
    boolean isImage_loaded = false;
    Uri mCropImageUri;
    byte[] imgBytes = null;

    // Shakti_kendra
    ArrayList<String> ar_kendra_id;
    ArrayList<String> ar_kendra_name;
    ArrayAdapter<String> spinner_shakti_adaptor;


    ArrayList<String> lst_blood_group;
    ArrayAdapter<String> lst_blood_adaptor;

    // level
//    ArrayList<String> ar_level_id;
    ArrayList<String> ar_level_name;
    ArrayAdapter<String> spinner_level_adaptor;
    // district
//    ArrayList<String> ar_district_id;
    ArrayList<String> ar_district_name;
    ArrayAdapter<String> spinner_district_adaptor;

    private ProgressDialog prg;
    ArrayList<District_model> DistrictModals = new ArrayList<>();
    ArrayList<Level_model> LevelModals = new ArrayList<>();

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shakti_kendra);
        createIDS();
        img_profile.setImageResource(R.drawable.camera);

        setDatainSpinner();
        getShaktiKendra();
        clickEvent();
        spinner_click();
    }

    private void spinner_click() {
        spinner_shakti_kendra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("kendra id", "kendra  id  : " + ar_kendra_id.get(position - 1));
                    get_kendra_detail(ar_kendra_id.get(position - 1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void get_kendra_detail(final String kendra_id) {

        prg.setMessage("Please Wait...");

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

                        setJsondetail(response);
                    } else {

                        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        spinner_level.setSelection(0);
                        spinner_district.setSelection(0);
                        spinner_blood.setSelection(0);
                        edt_mobile.setText("");
                        edt_booth_name.setText("");
                        edt_name.setText("");
                        edt_name.requestFocus();
                        img_profile.setImageResource(R.drawable.camera);

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
                params.put("action", Utils_url.Action_get_Shakti_Kendra_adhyaksha);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("shakti_kendra_id", kendra_id);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void setJsondetail(String response) {
        try {
            JSONObject js = new JSONObject(response);
            String name = js.getJSONArray("data").getJSONObject(0).getString("user_name");
            String blood_group = js.getJSONArray("data").getJSONObject(0).getString("blood_groop");
            String district_id = js.getJSONArray("data").getJSONObject(0).getString("district_id");
            String district_name = js.getJSONArray("data").getJSONObject(0).getString("district_name");
            String level_id = js.getJSONArray("data").getJSONObject(0).getString("level_id");
            String level_name = js.getJSONArray("data").getJSONObject(0).getString("level_name");
            String mobile = js.getJSONArray("data").getJSONObject(0).getString("mobile");
            String profile_pic = js.getJSONArray("data").getJSONObject(0).getString("profile_pic");
            String booth_name = js.getJSONArray("data").getJSONObject(0).getString("booth_ids");

            edt_booth_name.setText(booth_name);
            edt_name.setText(name);
            edt_mobile.setText(mobile);
            spinner_level.setSelection(getIndex(spinner_level, level_name));
            spinner_district.setSelection(getIndex(spinner_district, district_name));
            spinner_blood.setSelection(getIndex(spinner_blood, blood_group.trim()));

            if (!profile_pic.equalsIgnoreCase("")) {

                Picasso.with(this).load(profile_pic).placeholder(R.drawable.camera).error(R.drawable.camera).into(img_profile);

            } else {
                img_profile.setImageResource(R.drawable.camera);
            }

            Log.e("user name : ", name + "   " + blood_group);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private void clickEvent() {

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(Shakti_Kendra.this);
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_validation();

            }
        });
    }

    private void check_validation() {
        String str_name, str_mobile, str_booth_name;
        str_name = edt_name.getText().toString().trim();
        str_mobile = edt_mobile.getText().toString().trim();
        str_booth_name = edt_booth_name.getText().toString().trim();

        if (spinner_shakti_kendra.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select Shakti Kendra", Toast.LENGTH_LONG).show();
            return;
        } else if (str_name.equalsIgnoreCase("")) {
            edt_name.setError(Html.fromHtml("<font color='orange'>Requried</font>"));
            edt_name.requestFocus();
            return;
        } else if (str_name.equalsIgnoreCase("")) {
            edt_name.setError(Html.fromHtml("<font color='orange'>Requried</font>"));
            edt_name.requestFocus();
            return;
        } else if (str_mobile.equalsIgnoreCase("")) {
            edt_mobile.setError(Html.fromHtml("<font color='orange'>Requried</font>"));
            return;
        } else if (str_mobile.length() != 10) {
            edt_mobile.setError(Html.fromHtml("<font color='orange'>Invalid Mobile No.</font>"));
            return;
        } else if (spinner_level.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select Level", Toast.LENGTH_LONG).show();
            return;
        } else if (spinner_district.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select District", Toast.LENGTH_LONG).show();
            return;
        } else if (spinner_blood.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select Blood group", Toast.LENGTH_LONG).show();
            return;
        } else {
            Log.e("level value ", "level id : " + LevelModals.get(spinner_level.getSelectedItemPosition() - 1).getLevel_id().toString());
            SaveShaktiKendra(ar_kendra_id.get(spinner_shakti_kendra.getSelectedItemPosition() - 1).toString(), str_name, str_mobile, LevelModals.get(spinner_level.getSelectedItemPosition() - 1).getLevel_id().toString(), DistrictModals.get(spinner_district.getSelectedItemPosition() - 1).getDistrict_id().toString(), spinner_blood.getSelectedItem().toString(), str_booth_name);

        }
    }

    private void SaveShaktiKendra(final String kendra_id, final String str_name, final String str_mobile, final String level_id, final String district_id, final String blood_group, final String str_booth_name) {


        prg.setMessage("Please Wait...");

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
                        Clear_Detail();
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
                params.put("action", Utils_url.Action_Add_Shakti_Kendra_adhyaksha);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("shakti_kendra_id", kendra_id);
                params.put("user_name", str_name);
                params.put("mobile", str_mobile);
                params.put("district_id", district_id);
                params.put("level_id", level_id);
                params.put("blood_groop", blood_group);

                params.put("booth_ids", str_booth_name);

                params.put("profile_pic", "data:image/jpeg;base64," + myBase64Image);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void Clear_Detail() {


        spinner_shakti_kendra.setSelection(0);
        spinner_level.setSelection(0);
        spinner_district.setSelection(0);
        spinner_blood.setSelection(0);
        edt_mobile.setText("");
        edt_booth_name.setText("");
        edt_name.setText("");
        edt_name.requestFocus();
        img_profile.setImageResource(R.drawable.camera);
//        Picasso.with(this).load("").placeholder(R.drawable.camera).error(R.drawable.camera).into(img_profile);


        imgBytes = null;
    }

    // district
    private void getDistrict_fromDB() {
        DistrictModals = DBOperation.getAllDistrict(this);
        if (DistrictModals.size() > 0 && DistrictModals != null) {
            Log.e("in Database Calling", "in Database Calling");

            for (int i = 0; i < DistrictModals.size(); i++) {
                ar_district_name.add(DistrictModals.get(i).getDistrict_name());

            }
            spinner_district_adaptor.notifyDataSetChanged();
            getfromLevelDB();


        } else {
            Log.e("Have to get from server", "Have to get from server");
            getDistrict();
        }
    }

    private void getfromLevelDB() {
        LevelModals = DBOperation.getAlllevel(this);
        if (LevelModals.size() > 0 && LevelModals != null) {
            Log.e("in Database Calling", "in Database Calling");

            for (int i = 0; i < LevelModals.size(); i++) {
                ar_level_name.add(LevelModals.get(i).getLevel_name());

            }
            spinner_level_adaptor.notifyDataSetChanged();


        } else {
            Log.e("Have to get from server", "Have to get from server");
            getLevel();
        }
    }

    private void getDistrict() {

        prg.setMessage("Please Wait");
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
                    SavedData.saveOperation_status(false);

                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsAr = jsdata.getJSONArray("district_list");
                        for (int i = 0; i < jsAr.length(); i++) {
                            Log.e("Level IDs ", "" + jsAr.getJSONObject(i).getString("district_id"));
                            ar_district_name.add(jsAr.getJSONObject(i).getString("district_name"));

                            DistrictModals.add(new District_model(jsAr.getJSONObject(i).getString("district_id"), jsAr.getJSONObject(i).getString("district_name")));
                            ar_district_name.add(jsAr.getJSONObject(i).getString("district_name"));
                            boolean isSuccess = DBOperation.insertDistrict(Shakti_Kendra.this, jsAr.getJSONObject(i).getString("district_id"), jsAr.getJSONObject(i).getString("district_name"));
                            Log.e("Success", "" + isSuccess);

                        }
                        spinner_district_adaptor.notifyDataSetChanged();
                        getLevel();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    prg.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_GetDistrict);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                //  params.put("state_id", "1");


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);

    }

    private void getShaktiKendra() {

        prg.setMessage("Please Wait");
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
                    SavedData.saveOperation_status(false);

                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsAr = jsdata.getJSONArray("data");
                        for (int i = 0; i < jsAr.length(); i++) {
                            Log.e("Level IDs ", "" + jsAr.getJSONObject(i).getString("shakti_kendra_name"));
                            ar_kendra_id.add(jsAr.getJSONObject(i).getString("shakti_kendra_id"));
                            ar_kendra_name.add(jsAr.getJSONObject(i).getString("shakti_kendra_name"));

                        }
                        spinner_shakti_adaptor.notifyDataSetChanged();
                        getDistrict_fromDB();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    prg.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_GetShaktiKendra);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                //  params.put("state_id", "1");


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);

    }


    private void getLevel() {

        prg.setMessage("Please Wait");
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
                    SavedData.saveOperation_status(false);

                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsAr = jsdata.getJSONArray("level_list");
                        for (int i = 0; i < jsAr.length(); i++) {
                            Log.e("Level IDs ", "" + jsAr.getJSONObject(i).getString("level_id"));
                            //ar_level_id.add(jsAr.getJSONObject(i).getString("level_id"));


                            Log.e("Level IDs ", "" + jsAr.getJSONObject(i).getString("level_name"));
                            ar_level_name.add(jsAr.getJSONObject(i).getString("level_name"));

                            LevelModals.add(new Level_model(jsAr.getJSONObject(i).getString("level_id"), jsAr.getJSONObject(i).getString("level_name")));
                            ar_level_name.add(jsAr.getJSONObject(i).getString("level_name"));
                            boolean isSuccess = DBOperation.insertlevel(Shakti_Kendra.this, jsAr.getJSONObject(i).getString("level_id"), jsAr.getJSONObject(i).getString("level_name"));
                            Log.e("Success", "" + isSuccess);

                        }
                        spinner_level_adaptor.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    prg.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prg.dismiss();
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
//        queue.add(stringRequest);

    }

    private void setDatainSpinner() {

        lst_blood_group = new ArrayList<>();
        lst_blood_group.add("Select Blood Group");
        lst_blood_group.add("A +");
        lst_blood_group.add("A -");
        lst_blood_group.add("B +");
        lst_blood_group.add("B -");
        lst_blood_group.add("AB +");
        lst_blood_group.add("AB -");
        lst_blood_group.add("O +");
        lst_blood_group.add("O -");
        lst_blood_group.add("ABO -");
        lst_blood_group.add("RH -");
        lst_blood_group.add("RH-");

        lst_blood_adaptor = new ArrayAdapter<String>(Shakti_Kendra.this, R.layout.spinner_layout_white_text, lst_blood_group);
        lst_blood_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_blood.setAdapter(lst_blood_adaptor);
    }

    private void createIDS() {
        prg = new ProgressDialog(this);

        spinner_shakti_kendra = (Spinner) findViewById(R.id.spinner_shakti_kendra);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);
        spinner_level = (Spinner) findViewById(R.id.spinner_level);
        spinner_blood = (Spinner) findViewById(R.id.spinner_blood);

        img_profile = (ImageView) findViewById(R.id.img_profile);

        button_submit = (Button) findViewById(R.id.button_submit);

        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_booth_name = (EditText) findViewById(R.id.edt_booth_name);

//        ar_level_id = new ArrayList<>();
        ar_level_name = new ArrayList<>();
        ar_level_name.add("Select Level");

        spinner_level_adaptor = new ArrayAdapter<String>(Shakti_Kendra.this, R.layout.spinner_layout_white_text, ar_level_name);
        spinner_level_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_level.setAdapter(spinner_level_adaptor);

        //ar_district_id = new ArrayList<>();
        ar_district_name = new ArrayList<>();
        ar_district_name.add("Select District");

        spinner_district_adaptor = new ArrayAdapter<String>(Shakti_Kendra.this, R.layout.spinner_layout_white_text, ar_district_name);
        spinner_district_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_district.setAdapter(spinner_district_adaptor);


        ar_kendra_id = new ArrayList<>();
        ar_kendra_name = new ArrayList<>();
        ar_kendra_name.add("Select Shakti Kendra");

        spinner_shakti_adaptor = new ArrayAdapter<String>(Shakti_Kendra.this, R.layout.spinner_layout_white_text, ar_kendra_name);
        spinner_shakti_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_shakti_kendra.setAdapter(spinner_shakti_adaptor);

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
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos);
                    imgBytes = baos.toByteArray();

                    Bitmap imageBitmap = SiliCompressor.with(Shakti_Kendra.this).getCompressBitmap(resultUri.toString(), true);

                    Log.e("new Bitmap Size", "" + imageBitmap.getByteCount());
                    Log.e("old Bitmap Size", "" + bitmap.getByteCount());

                    //  myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 75);
// Silicon compress
                    myBase64Image = encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 100);

                    // Log.e("bitmapString",myBase64Image.toString()+"");
                    if (bitmap != null) {
                        img_profile.setImageDrawable(null);
                        img_profile.setImageResource(0);
                    }
                    img_profile.setImageBitmap(bitmap);

                    isImage_loaded = true;

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
}

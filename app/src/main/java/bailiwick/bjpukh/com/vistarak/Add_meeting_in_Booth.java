package bailiwick.bjpukh.com.vistarak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iceteck.silicompressorr.SiliCompressor;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class Add_meeting_in_Booth extends RootActivity {
    EditText edt_no_male, edt_no_female, edt_total;
    Button button_submit;
    ImageView img_profile;
    TextInputLayout mMale, mFemale, mTotal;
    TextView mBoothMeeting, mPicUpload;

    private Spinner spinner_booth;
    ArrayList<BoothSpinnerModal> boothSpinnerModals;
    ArrayList<String> spinner_booth_array;
    ArrayAdapter<String> spinner_booth_adaptor;

    Bitmap bitmap;
    String myBase64Image;
    boolean isImage_loaded = false;
    ProgressDialog prg;
    Uri mCropImageUri;
    byte[] imgBytes = null;

    private TextView txt_member_count;

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_in_booth);
        createIDs();
        ClickEvent();
        getBoothFromDB();
        Spinne_Click();
    }

    private void Spinne_Click() {
        spinner_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Log.e("booth id", "booth id  : " + boothSpinnerModals.get(position - 1).getBid());
                    get_count(boothSpinnerModals.get(position - 1).getBid());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void get_count(final String bid) {


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
                                if (jsdata.has("entry_count")) {

                                    txt_member_count.setText("Register Member " + jsdata.getString("entry_count"));
                                }
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", Utils_url.Action_meeting_count);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", bid);


                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);


    }

    private void ClickEvent() {
        edt_no_female.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    getTotal();
                }
            }
        });
        edt_no_male.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    getTotal();
                }
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(Add_meeting_in_Booth.this);
            }
        });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String male_count, female_count, total;
                male_count = edt_no_male.getText().toString().trim();
                female_count = edt_no_female.getText().toString().trim();
                total = edt_total.getText().toString().trim();
                if (spinner_booth.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), SelectedLanguage[TextUtils.select_booth], Toast.LENGTH_LONG).show();
                    return;
                } else if (male_count.equalsIgnoreCase("")) {
                    edt_no_male.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else if (female_count.equalsIgnoreCase("")) {
                    edt_no_female.setError(Html
                            .fromHtml("<font color='orange'>Requried</font>"));
                    return;
                } else {

                    getTotal();
                    total = edt_total.getText().toString().trim();

                    if (CheckNetworkState.isNetworkAvailable(Add_meeting_in_Booth.this)) {
                        SaveMeetingAppgroup(male_count, female_count, total, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());

                    } else {
                        SaveDatabase_Meeting(male_count, female_count, total, boothSpinnerModals.get(spinner_booth.getSelectedItemPosition() - 1).getBid(), SavedData.getUSER_NAME());
                    }
                }

            }
        });
    }

    private void SaveDatabase_Meeting(String male_count, String female_count, String total, String booth_id, String user_name) {


        try {
            Log.e("img byte", "" + imgBytes);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
            String currentTimeStamp = dateFormat.format(new Date());

            boolean isSuccess = DBOperation.insertBoothMeeting(Add_meeting_in_Booth.this,
                    SavedData.getUSER_NAME(), booth_id, male_count,
                    SavedData.getLattitudeLocation(), SavedData.getLongitudeLocation(), female_count, total, imgBytes, currentTimeStamp);


            if (isSuccess) {
                Toast.makeText(getApplicationContext(), "Saved in Database", Toast.LENGTH_SHORT).show();
                Clear_Detail();
            } else {
                Log.e("Data no Saved", "Data not Saved");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getTotal() {
        if (!edt_no_male.getText().toString().trim().equalsIgnoreCase("") && !edt_no_female.getText().toString().trim().equalsIgnoreCase("")) {
            int a1 = Integer.parseInt(edt_no_male.getText().toString());
            int b1 = Integer.parseInt(edt_no_female.getText().toString());
            int c1 = a1 + b1;
            Log.e("check", "a= " + a1 + "  b1=  " + b1);
            edt_total.setText("" + c1);
        } else {
            edt_total.setText("");
        }
    }

    private void SaveMeetingAppgroup(final String male_count, final String female_count, final String total, final String booth_id, String user_name) {

        prg.setMessage("Add meeting Detail....");
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
                                String meetingId = jsdata.getString("meetingId");
                                UploadImage(meetingId);

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
                params.put("action", Utils_url.Action_AddMeeting);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", booth_id);
                params.put("male_member", male_count);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());

                params.put("female_member", female_count);
                params.put("total_member", total);
                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    // Upload Image
    private void UploadImage(final String meetingId) {

        prg.setMessage("Uploading  Image...");
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
                params.put("action", Utils_url.Action_UpdateMeeting);
                params.put("meetingId", meetingId);
                params.put("image", "data:image/jpeg;base64," + myBase64Image);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void Clear_Detail() {
        edt_no_male.setText("");

        edt_no_female.setText("");
        edt_total.setText("");
        img_profile.setImageResource(0);
        img_profile.setImageDrawable(null);
        img_profile.setBackgroundResource(R.drawable.camera);
        imgBytes = null;
    }

    private void createIDs() {
        prg = new ProgressDialog(this);
        spinner_booth = (Spinner) findViewById(R.id.spinner_booth);
        edt_no_male = (EditText) findViewById(R.id.edt_no_male);
        edt_no_female = (EditText) findViewById(R.id.edt_no_female);
        edt_total = (EditText) findViewById(R.id.edt_total);
        button_submit = (Button) findViewById(R.id.button_submit);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        txt_member_count = (TextView) findViewById(R.id.txt_member_count);

        spinner_booth_array = new ArrayList<>();
        boothSpinnerModals = new ArrayList<>();

        spinner_booth_adaptor = new ArrayAdapter<String>(Add_meeting_in_Booth.this,
                R.layout.spinner_layout_white_text, spinner_booth_array);
        spinner_booth_adaptor.setDropDownViewResource(R.layout.spinner_layout_white);
        spinner_booth.setAdapter(spinner_booth_adaptor);

        setLannguage();

    }

    private void setLannguage() {
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());

        mBoothMeeting = (TextView) findViewById(R.id.txt_booth_meeting);
        mPicUpload = (TextView) findViewById(R.id.tv_upload_pic);
        mMale = (TextInputLayout) findViewById(R.id.til_male);
        mFemale = (TextInputLayout) findViewById(R.id.til_female);
        mTotal = (TextInputLayout) findViewById(R.id.til_total);

        mBoothMeeting.setText(SelectedLanguage[TextUtils.booth_meeting]);
        mPicUpload.setText(SelectedLanguage[TextUtils.pic_upload]);
        mMale.setHint(SelectedLanguage[TextUtils.male]);
        mFemale.setHint(SelectedLanguage[TextUtils.female]);
        mTotal.setHint(SelectedLanguage[TextUtils.total]);
        button_submit.setText(SelectedLanguage[TextUtils.submit]);

    }

    /*************************
     * on Activity Result Overridden
     ******************/
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
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

                    Bitmap imageBitmap = SiliCompressor.with(Add_meeting_in_Booth.this).getCompressBitmap(resultUri.toString(), true);

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


package bailiwick.bjpukh.com.vistarak;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.Support.CheckNetworkState;
import bailiwick.bjpukh.com.vistarak.Support.RootActivity;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Utils_url;
import bailiwick.bjpukh.com.vistarak.app.AppController;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 19-03-2018.
 */

public class Expensive_Activity extends RootActivity implements DatePickerDialog.OnDateSetListener {
    private Button btn_button_submit;
    private EditText edt_total, edt_desc, edt_tittle;
    private ImageView img_bill_copy;
    private TextView tv_upload_bill;

    final Calendar c = Calendar.getInstance();
    TextView date_of_birth;
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog;
    private EditText edt_dateDD, edt_dateMM, edt_dateYYY;

    Bitmap bitmap;
    String myBase64Image;
    boolean isImage_loaded = false;
    Uri mCropImageUri;
    byte[] imgBytes = null;

    ProgressDialog prg;

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static boolean isDateValid(String date) {
        final String DATE_FORMAT = "dd-MM-yyyy";
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expensive_activity);
        createIDS();
        Datechecker();
        DateClickEvent();
        clickEvent();


    }

    private void clickEvent() {
        btn_button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_tittle, str_description, str_amount, str_date;
                str_tittle = edt_tittle.getText().toString().trim();
                str_description = edt_desc.getText().toString().trim();
                str_amount = edt_total.getText().toString().trim();
                str_date = edt_tittle.getText().toString().trim();
                if (str_tittle.equalsIgnoreCase("")) {
                    edt_tittle.setError("requried");
                    return;
                } else if (str_description.equalsIgnoreCase("")) {
                    edt_desc.setError("requried");
                    return;
                } else if (str_amount.equalsIgnoreCase("") || str_amount.equalsIgnoreCase("0")) {
                    edt_total.setError("requried");
                    return;
                } else if (!isdateValidate()) {
                    return;
                } else {
                    if (CheckNetworkState.isNetworkAvailable(Expensive_Activity.this)) {
                        SaveExpensive(str_tittle, str_description, str_amount, SavedData.getUSER_NAME());

                    } else {
// Database is in progress
                    }
                }
            }
        });

        img_bill_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(Expensive_Activity.this);
            }
        });

    }

    private void SaveExpensive(final String str_tittle, final String str_description, final String str_amount, String user_name) {


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
                                String bill_id = jsdata.getString("Bill_id");
                                if (isImage_loaded) {
                                    UploadImage(bill_id);
                                    isImage_loaded = false;

                                } else {
                                    isImage_loaded = false;
                                    Clear_Detail();
                                }

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
                params.put("action", Utils_url.Action_AddExpenses);
                params.put("vistarak_id", SavedData.getUSER_NAME());
                params.put("booth_id", "0");
                params.put("tittle", str_tittle);
                params.put("latitude", SavedData.getLattitudeLocation());
                params.put("longitude", SavedData.getLongitudeLocation());

                params.put("description", str_description);
                params.put("amount", str_amount);
                String bill_date = edt_dateYYY.getText().toString() + "-" + edt_dateMM.getText().toString() + "-" + edt_dateDD.getText().toString();

                params.put("bill_date", bill_date);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);

    }

    // Upload Image
    private void UploadImage(final String billID) {

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
                params.put("action", Utils_url.Action_UpdateExpenses);
                params.put("billID", billID);
                params.put("image", "data:image/jpeg;base64," + myBase64Image);

                Log.e("Param Response ", "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        queue.add(stringRequest);
    }

    private void Clear_Detail() {
        edt_tittle.setText("");
        edt_desc.setText("");
        edt_total.setText("");
        edt_dateDD.setText("");
        edt_dateMM.setText("");
        edt_dateYYY.setText("");
        img_bill_copy.setImageResource(0);
        img_bill_copy.setImageDrawable(null);
        img_bill_copy.setBackgroundResource(R.drawable.camera);
        imgBytes = null;
    }

    private void DateClickEvent() {
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void Datechecker() {

        edt_dateDD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edt_dateDD.setText("");
                    //  edt_dateDD.requestFocus();
                }
            }
        });

        edt_dateDD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);
                if (edt_dateDD.getText().toString().length() >= 2) {
                    int dd = Integer.parseInt(edt_dateDD.getText().toString().trim());
                    if (dd > 31 || dd <= 0) {
                        edt_dateDD.setError("invalid date");
                        edt_dateDD.setText("");
                    }
                    if (edt_dateMM.getText().toString().equalsIgnoreCase("")) {
                        edt_dateMM.requestFocus();

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_dateMM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edt_dateMM.setText("");
                }
            }
        });

        edt_dateMM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);
                if (edt_dateMM.getText().toString().length() >= 2) {
                    int dd = Integer.parseInt(edt_dateMM.getText().toString().trim());
                    if (dd >= 13 || dd <= 0) {
                        edt_dateMM.setError("invalid Month");
                        edt_dateMM.setText("");
                    }
                    if (edt_dateYYY.getText().toString().equalsIgnoreCase("")) {
                        edt_dateYYY.requestFocus();

                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_dateYYY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edt_dateYYY.setText("");
                } else {
                    if (edt_dateYYY.getText().toString().length() != 4 && !edt_dateYYY.getText().toString().equalsIgnoreCase("")) {

                        edt_dateYYY.setError("Invalid year");
                    }
                }
            }
        });

        edt_dateYYY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void createIDS() {
        prg = new ProgressDialog(this);
        btn_button_submit = (Button) findViewById(R.id.button_submit);
        edt_total = (EditText) findViewById(R.id.edt_total);
        edt_desc = (EditText) findViewById(R.id.edt_desc);
        edt_tittle = (EditText) findViewById(R.id.edt_tittle);

        date_of_birth = (TextView) findViewById(R.id.date_of_birth);
        edt_dateDD = (EditText) findViewById(R.id.edt_dateDD);
        edt_dateMM = (EditText) findViewById(R.id.edt_dateMM);
        edt_dateYYY = (EditText) findViewById(R.id.edt_dateYYYY);
        img_bill_copy = (ImageView) findViewById(R.id.img_bill_copy);
        tv_upload_bill = (TextView) findViewById(R.id.tv_upload_bill);

        datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, this, year, month, day);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date result = cal.getTime();

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(result.getTime());


        setLanguage();

    }

    private void setLanguage() {
        SelectedLanguage = getResources().getStringArray(SavedData.getLangArray());

        edt_tittle.setHint(SelectedLanguage[TextUtils.Tittle]);
        edt_total.setHint(SelectedLanguage[TextUtils.Amount]);
        edt_desc.setHint(SelectedLanguage[TextUtils.Description]);
        btn_button_submit.setText(SelectedLanguage[TextUtils.submit]);

        tv_upload_bill.setText(SelectedLanguage[TextUtils.upload_bill]);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String month1 = "", day1 = "";
        if (String.valueOf(month + 1).length() == 1)
            month1 = "0" + (month + 1);
        else
            month1 = (month + 1) + "";
        if (String.valueOf(day).length() == 1)
            day1 = "0" + (day);
        else
            day1 = (day) + "";

        edt_dateDD.setText(day1);

        edt_dateMM.setText(month1);
        edt_dateYYY.setText("" + year);
    }

    private boolean isdateValidate() {
        Log.e("i m heree", "1111");
        boolean isValid = false;
        String Str_dd, Str_mm, Str_yyy;
        Str_dd = edt_dateDD.getText().toString().trim();
        Str_mm = edt_dateMM.getText().toString().trim();
        Str_yyy = edt_dateYYY.getText().toString().trim();


        if (edt_dateDD.getText().toString().trim().equalsIgnoreCase("")) {
            edt_dateDD.setError("Requried");
            return isValid;
        } else if (edt_dateMM.getText().toString().trim().equalsIgnoreCase("")) {
            edt_dateMM.setError("Requried");
            return isValid;
        } else if (edt_dateYYY.getText().toString().trim().equalsIgnoreCase("")) {
            edt_dateYYY.setError("Requried");
            return isValid;
        } else if (!isDateValid(Str_dd + "-" + Str_mm + "-" + Str_yyy)) {
            Toast.makeText(getApplicationContext(), "not valid Date", Toast.LENGTH_SHORT).show();
            return isValid;
        } else {

            isValid = true;
        }

        return isValid;
    }

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

                    Bitmap imageBitmap = SiliCompressor.with(Expensive_Activity.this).getCompressBitmap(resultUri.toString(), true);

                    Log.e("new Bitmap Size", "" + imageBitmap.getByteCount());
                    Log.e("old Bitmap Size", "" + bitmap.getByteCount());

                    //  myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 75);
// Silicon compress
                    myBase64Image = encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 100);

                    // Log.e("bitmapString",myBase64Image.toString()+"");
                    if (bitmap != null) {
                        img_bill_copy.setImageDrawable(null);
                        img_bill_copy.setImageResource(0);
                    }
                    img_bill_copy.setImageBitmap(bitmap);

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

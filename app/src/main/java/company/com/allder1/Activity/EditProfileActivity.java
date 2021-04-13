package company.com.allder1.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.MultiPartRequester;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.ParseContent;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {
    EditText edtfirstname, edtlastname, edtemail, edtlocation;
    Button btnsave;
    LinearLayout layoutlocation;
    TextView txtfullname;
    private AQuery aQuery;
    Spinner spinnergeder;
    private ImageView profile_image;
    String gader;
    private File cameraFile;
    private String filePath = "";
    private Uri uri = null;
    private PreferenceHelper PreferenceHelper;
    ParseContent pcontent;
    String mFiestmane, mLastname, mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Mapped();
        PreferenceHelper = new PreferenceHelper(this);
        pcontent = new ParseContent(this);
        setspinner();
        setValues();
        if (PreferenceHelper.getSpecies().equals("consumer")) {
            layoutlocation.setVisibility(View.GONE);
        } else {
            layoutlocation.setVisibility(View.GONE);
            layoutlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditProfileActivity.this, LocationActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(EditProfileActivity.this, "fadein-to-fadeout");
                }
            });
        }
    }

    private void setValues() {
        if (PreferenceHelper != null) {
            edtfirstname.setText(PreferenceHelper.getUser_name());
            edtlastname.setText(PreferenceHelper.getlastname());
            edtemail.setText(PreferenceHelper.getEmail());
            txtfullname.setText(PreferenceHelper.getUser_name() + " " + PreferenceHelper.getlastname());
            Log.d("ccccc", "setValues: " + PreferenceHelper.getGeder());

            if (PreferenceHelper.getPicture().isEmpty()) {
//                spinnergeder.setSelection(1);
            } else {
                String base64 = PreferenceHelper.getPicture();
                String base64Image = base64.split(",")[1];
                byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
                Glide.with(EditProfileActivity.this).load(imageByteArray).override(400, 400).centerCrop().into(profile_image);
            }


//            new AQuery(this).id(R.id.iv_avata).image(PreferenceHelper.getPicture(), true, true,
//                    200, 0, new BitmapAjaxCallback() {
//                        @Override
//                        public void callback(String url, ImageView iv, Bitmap bm,
//                                             AjaxStatus status) {
//                            if (url != null && !url.equals("")) {
//                                filePath = aQuery.getCachedFile(url).getPath();
//                            }
//                        }
//
//                    });
        }
    }

    private void setspinner() {
        List<String> categories = new ArrayList<String>();
        Log.d("Manh", "setspinner: "+PreferenceHelper.getGeder());
        if (PreferenceHelper.getGeder().equals("male")) {
            categories.add(getResources().getString(R.string.male));
            categories.add(getResources().getString(R.string.female));
        } else if (PreferenceHelper.getGeder().equals("female")) {
            categories.add(getResources().getString(R.string.female));
            categories.add(getResources().getString(R.string.male));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnergeder.setAdapter(dataAdapter);
        spinnergeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    gader = spinnergeder.getSelectedItem().toString();
                } else if (position == 1) {
                    gader = spinnergeder.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Mapped() {
        edtfirstname = findViewById(R.id.edtfirstname);
        profile_image = findViewById(R.id.iv_avata);
        txtfullname = findViewById(R.id.txtfullname);
        edtlastname = findViewById(R.id.edtlastname);
        edtemail = findViewById(R.id.edtemail);
        layoutlocation = findViewById(R.id.layoutlocation);
        edtlocation = findViewById(R.id.edtlocation);
        btnsave = findViewById(R.id.btnsave);
        spinnergeder = findViewById(R.id.spinnergeder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsave:
                if (PreferenceHelper.getSpecies().equals("consumer")) {
                    updateprofileconsumer();
                } else {
                    updateprofileprovider();
                }
                break;
            case R.id.iv_avata:
                showPictureDialog();
                break;
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.txt_slct_option));
        String[] items = {getResources().getString(R.string.txt_gellery), getResources().getString(R.string.txt_cameray)};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        choosePhotoFromGallary();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;

                }
            }
        });
        dialog.show();
    }

    private void takePhotoFromCamera() {
        Calendar cal = Calendar.getInstance();
        cameraFile = new File(Environment.getExternalStorageDirectory(),
                (cal.getTimeInMillis() + ".jpg"));
        if (!cameraFile.exists()) {
            try {
                cameraFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            cameraFile.delete();
            try {
                cameraFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        uri = Uri.fromFile(cameraFile);
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(i, Const.TAKE_PHOTO);
    }

    private void choosePhotoFromGallary() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Const.CHOOSE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
            Commonutils.showtoast("Gallery not found!", this);
        }

    }

    private void updateprofileconsumer() {
        Commonutils.progressdialog_show(this, getResources().getString(R.string.updating_pro_load));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.UPDATE_PROFILE);
        map.put(Const.Params.FIRSTNAME, edtfirstname.getText().toString());
        map.put(Const.Params.LAST_NAME, edtlastname.getText().toString());
        map.put(Const.Params.EMAIL, edtemail.getText().toString());
        map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(this).getSessionToken());
        map.put(Const.Params.PICTURE, filePath);
        map.put(Const.Params.PHONE, new PreferenceHelper(this).getPhone());
        map.put(Const.Params.TIMEZONE, TimeZone.getDefault().getID());
        map.put(Const.Params.GENDER, gader);
        Log.d("Manh", "updateprofileconsumer: " + map);
        if (filePath.equals("") || null == filePath) {
            new VollyRequester(this, Const.POST, map, Const.ServiceCode.UPDATE_PROFILE,
                    this);
        } else {
            new MultiPartRequester(this, map, Const.ServiceCode.UPDATE_PROFILE,
                    this);
        }

    }

    private void updateprofileprovider() {
        Commonutils.progressdialog_show(this, getResources().getString(R.string.updating_pro_load));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.UPDATE_PROFILEPROVIDER);
        map.put(Const.Params.FIRSTNAME, edtfirstname.getText().toString());
        map.put(Const.Params.LAST_NAME, edtlastname.getText().toString());
        map.put(Const.Params.EMAIL, edtemail.getText().toString());
        map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(this).getSessionToken());
        map.put(Const.Params.PICTURE, filePath);
        map.put(Const.Params.PHONE, new PreferenceHelper(this).getPhone());
        map.put(Const.Params.TIMEZONE, TimeZone.getDefault().getID());
        map.put(Const.Params.GENDER, gader);
        Log.d("Manh", map.toString());
        if (filePath.equals("") || null == filePath) {
            new VollyRequester(this, Const.POST, map, Const.ServiceCode.UPDATE_PROFILEPROVIDER,
                    this);
        } else {

            new MultiPartRequester(this, map, Const.ServiceCode.UPDATE_PROFILEPROVIDER,
                    this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Const.CHOOSE_PHOTO:
                if (data != null) {
                    uri = data.getData();
                    if (uri != null) {
                        beginCrop(uri);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.txt_img_error),
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case Const.TAKE_PHOTO:
                if (uri != null) {
                    beginCrop(uri);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.txt_img_error),
                            Toast.LENGTH_LONG).show();
                }

                break;
            case Crop.REQUEST_CROP:
                if (data != null)
                    handleCrop(resultCode, data);
                break;
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), (Calendar.getInstance()
                .getTimeInMillis() + ".jpg")));
        Crop.of(source, outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            filePath = getRealPathFromURI(Crop.getOutput(result));

            //  .setImageURI(Crop.getOutput(result));

            Glide.with(EditProfileActivity.this).load(filePath).override(100, 100).centerCrop().into(profile_image);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.UPDATE_PROFILE:
                Log.d("Manh", "update profile" + response);
                Commonutils.progressdialog_hide();
                try {
                    if (response != null) {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {
                            this.recreate();
                            Toast.makeText(this, "Edit Profile successful", Toast.LENGTH_SHORT).show();
                            try {
                                if (!filePath.equals("")) {
                                    File file = new File(filePath);
                                    file.getAbsoluteFile().delete();
                                }
                                if (cameraFile != null) {
                                    cameraFile.getAbsoluteFile().delete();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (pcontent.isSuccessWithStoreId(response)) {
                                pcontent.parseUserAndStoreToDb(response);
                                Commonutils.showtoast(getString(R.string.update_success_text), this);
                                Intent intent = new Intent(EditProfileActivity.this, EditProfileActivity.class);
                                this.finish();
                                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                CustomIntent.customType(this, "fadein-to-fadeout");
                            } else {

                            }
                        } else {
                            String error = job1.getString("error_messages");
                            Commonutils.showtoast(error, this);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.UPDATE_PROFILEPROVIDER:
                Log.d("Manh", "update profile" + response);
                Commonutils.progressdialog_hide();
                try {
                    if (response != null) {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {
                            try {
                                if (!filePath.equals("")) {
                                    File file = new File(filePath);
                                    file.getAbsoluteFile().delete();
                                }
                                if (cameraFile != null) {
                                    cameraFile.getAbsoluteFile().delete();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (pcontent.isSuccessWithStoreId(response)) {
                                pcontent.parseUserAndStoreToDb(response);
                                Commonutils.showtoast(getString(R.string.update_success_text), this);
                                Intent intent = new Intent(EditProfileActivity.this, EditProfileActivity.class);
                                this.finish();
                                startActivity(intent);
                                CustomIntent.customType(this, "fadein-to-fadeout");
                            } else {

                            }

                        } else {
                            String error = job1.getString("error_messages");
                            Commonutils.showtoast(error, this);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null,
                null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        CustomIntent.customType(this, "fadein-to-fadeout");
        super.onBackPressed();
    }
}
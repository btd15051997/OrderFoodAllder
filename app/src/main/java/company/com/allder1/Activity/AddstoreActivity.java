package company.com.allder1.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.MultiPartRequester;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.DataProvider;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class AddstoreActivity extends Activity implements View.OnClickListener, AsyncTaskCompleteListener {
    ImageView cover_picture;
    TextView txtlocation, txtclosedtime, txtopentime;
    EditText edtnamestore;
    Calendar calendar = Calendar.getInstance();
    int hour, minute;
    Button btnsave;
    Double Lat, Long;
    String address_name;
    String address_name1;
    private Uri uri = null;
    private File cameraFile;
    private String filePath = "";
    public static List<DataProvider> dataProviders = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstore);
        Mapped();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        getstoreprovder();
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatastore();
            }
        });
    }

    private void Mapped() {
        cover_picture = findViewById(R.id.cover_picture);
        btnsave = findViewById(R.id.btnsave);
        txtlocation = findViewById(R.id.txtlocation);
        txtclosedtime = findViewById(R.id.txtclosedtime);
        txtopentime = findViewById(R.id.txtopentime);
        edtnamestore = findViewById(R.id.edtnamestore);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtclosedtime:
                TimePickerDialog timePickerclose = new TimePickerDialog(AddstoreActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txtclosedtime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                timePickerclose.show();
                break;

            case R.id.txtopentime:
                TimePickerDialog timePickeropen = new TimePickerDialog(AddstoreActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txtopentime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                timePickeropen.show();
                break;
            case R.id.txtlocation:
                Intent intent1 = new Intent(AddstoreActivity.this, LocationActivity.class);
                if (Lat != null && Long != null) {
                    intent1.putExtra("Lat", Lat);
                    intent1.putExtra("Long", Long);
                    intent1.putExtra("address_name", address_name);
                }
                startActivity(intent1);
                break;
            case R.id.cover_picture:
                showPictureDialog();
                break;
//            case R.id.btnsave:
//                updatastore();
//                break;
        }
    }

    private void updatastore() {
        // Commonutils.progressdialog_show(this, getResources().getString(R.string.updating_pro_load));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.UPDATESTORE);
        map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(this).getSessionToken());
        map.put("address", txtlocation.getText().toString());
        map.put("latitude", String.valueOf(Lat));
        map.put("name_store", edtnamestore.getText().toString());
        map.put("longitude", String.valueOf(Long));
        map.put("closed_time", txtclosedtime.getText().toString());
        map.put("opening_time", txtopentime.getText().toString());
        map.put("cover_picture", filePath);
        Log.d("mahi", map.toString());
        if (filePath.equals("") || null == filePath) {
            new VollyRequester(this, Const.POST, map, Const.ServiceCode.UPDATESTORE,
                    this);
        } else {
            new MultiPartRequester(this, map, Const.ServiceCode.UPDATESTORE,
                    this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Const.CHOOSE_PHOTO:
                if (data != null) {
                    uri = data.getData();
                    if (uri != null) {
                        final File file = new File(uri.getPath());
                        Glide.with(this).load(data.getData())
                                .error(R.drawable.ic_avata)
                                .crossFade()
                                .placeholder(R.drawable.no_image).into(cover_picture);
                        //filePath = uri.getPath();
                        filePath = getRealPathFromURI(uri);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.txt_img_error),
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case Const.TAKE_PHOTO:
                if (uri != null) {
                    filePath = getRealPathFromURI(uri);
                    filePath = uri.toString();
                    Glide.with(this).load(filePath)
                            .error(R.drawable.ic_avata)
                            .crossFade()
                            .placeholder(R.drawable.no_image).into(cover_picture);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.txt_img_error),
                            Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getApplication().getContentResolver().query(contentURI, null,
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

    private void getstoreprovder() {
        HashMap<String, String> map = new HashMap<>();
        Commonutils.progressdialog_show(this, "Loading...");
        map.put(Const.Params.URL, Const.ServiceType.GETSTORE + "provider_id=" + new PreferenceHelper(this).getUserId() + "&sort=asc");
        Log.d("maih", "getstore" + map.toString());
        new VollyRequester(AddstoreActivity.this, Const.GET, map, Const.ServiceCode.GETSTOER,
                this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.UPDATESTORE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("cccccc", "onTaskCompleted: " + response);
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(AddstoreActivity.this, "Update successful store information", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "system error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                this.recreate();
                break;
            case Const.ServiceCode.GETSTOER:
                JSONObject jsonObject = null;
                Commonutils.progressdialog_hide();
                try {
                    jsonObject = new JSONObject(response);
                    Log.d("ccc", "onTaskCompleted2: " + response);
                    JSONArray response1 = jsonObject.getJSONArray("data_provider");
                    TypeToken<List<DataProvider>> token = new TypeToken<List<DataProvider>>() {
                    };
                    dataProviders = new Gson().fromJson(response1.toString(), token.getType());
                    setdatastore(dataProviders);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    private void setdatastore(List<DataProvider> dataProviders) {
        txtopentime.setText(dataProviders.get(0).getOpeningTime());
        txtclosedtime.setText(dataProviders.get(0).getClosedTime());
        txtlocation.setText(dataProviders.get(0).getAddress());
        if (dataProviders.get(0).getCoverPicture().isEmpty()) {

        } else {
            String base64 = dataProviders.get(0).getCoverPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(this).load(imageByteArray)
                    .error(R.drawable.ic_avata)
                    .crossFade()
                    .placeholder(R.drawable.no_image).into(cover_picture);
        }

        Lat = dataProviders.get(0).getLatitude();
        Long = dataProviders.get(0).getLongitude();
        address_name = dataProviders.get(0).getAddress();
        edtnamestore.setText(dataProviders.get(0).getNameStore());
    }

    @Override
    protected void onRestart() {
        Log.d("cccc", "onRestart: " + LocationActivity.address_name);
        if (LocationActivity.address_name != null) {
            txtlocation.setText(LocationActivity.address_name);
            address_name = LocationActivity.address_name;
            Lat = LocationActivity.Lat;
            Long = LocationActivity.Long;
        }

        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        CustomIntent.customType(AddstoreActivity.this, "fadein-to-fadeout");
        super.onBackPressed();
    }
}

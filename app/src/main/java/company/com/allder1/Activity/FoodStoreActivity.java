package company.com.allder1.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import company.com.allder1.Adapter.OrderfoodAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.DataFood;
import company.com.allder1.model.DataProvider;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.LocationHelper;
import maes.tech.intentanim.CustomIntent;

public class FoodStoreActivity extends AppCompatActivity implements AsyncTaskCompleteListener, OrderfoodAdapter.ItemClickListener1, LocationHelper.OnLocationReceived {
    String idprovider;
    TextView txtnamestore, txtdistance, txtopentime, txtnumberphone, txtclosedtime;
    RecyclerView RecyclerViewfood;
    ImageView coverimage;
    public static LatLng locationstore;
    private boolean mPermissionCheck = false;
    LatLng locationUser;
    public static List<DataProvider> dataProviders = null;
    FloatingActionButton fab_order;
    public static DataFood dataFood1;
    public LocationManager locationManager;
    public static String currency;
    ArrayList<DataFood> datafood = null;
    public static int screen;
    OrderfoodAdapter adapter;
    String opentime;
    Date dateopentime;
    Date dateclosetime;
    Date datetime;
    String time;
    public static boolean close_the_door = false;
    String openclose;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    LinearLayout layoutphone, layoutlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_store);
        Mapped();
        Intent intent = getIntent();
        idprovider = String.valueOf(intent.getIntExtra("id_provider", 0));
        getstoreprovder();
//        if (new Database(FoodStoreActivity.this).getStore().isEmpty()) {
//        } else {
//            if (dataProviders == null || datafood == null) {
//                getDatabasestore(new Database(FoodStoreActivity.this).getStore());
//            }
//        }


        if (isServiceOk()) {
            init();
            //initToolbar();
        }
        if (OrderfoodAdapter.listorder.size() > 0) {
            fab_order.show();
        } else if (OrderfoodAdapter.listorder.size() == 0) {
            fab_order.hide();
        }
    }

    private void getDatabasestore(String response) {
        try {
            //data provider
            JSONObject jsonObject = new JSONObject(response);
            currency = jsonObject.getString("currency");
            JSONArray response1 = jsonObject.getJSONArray("data_provider");
            TypeToken<List<DataProvider>> token = new TypeToken<List<DataProvider>>() {
            };
            dataProviders = new Gson().fromJson(response1.toString(), token.getType());
            setdatastore(dataProviders);
            getDeviceLocation(dataProviders);
//===========
            //lisfood
            String response2 = jsonObject.getString("data_foods");
            TypeToken<List<DataFood>> token1 = new TypeToken<List<DataFood>>() {
            };
            datafood = new Gson().fromJson(response2, token1.getType());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            RecyclerViewfood.setLayoutManager(linearLayoutManager);
            adapter = new OrderfoodAdapter(FoodStoreActivity.this, datafood, this, currency);
            RecyclerViewfood.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Mapped() {
        txtnamestore = findViewById(R.id.txtnamestore);
        txtdistance = findViewById(R.id.txtdistance);
        txtopentime = findViewById(R.id.txtopentime);
        txtclosedtime = findViewById(R.id.txtclosedtime);
        RecyclerViewfood = findViewById(R.id.RecyclerViewfood);
        coverimage = findViewById(R.id.coverimage);
        txtnumberphone = findViewById(R.id.txtnumberphone);
        layoutphone = findViewById(R.id.layoutphone);
        layoutlocation = findViewById(R.id.layoutlocation);
        fab_order = findViewById(R.id.fab_order);
    }

    private void getstoreprovder() {
        HashMap<String, String> map = new HashMap<>();
        Commonutils.progressdialog_show(this, "Loading...");
        map.put(Const.Params.URL, Const.ServiceType.GETSTORE + "provider_id=" + idprovider + "&sort=asc");
        new VollyRequester(FoodStoreActivity.this, Const.GET, map, Const.ServiceCode.GETSTOER,
                this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GETSTOER:
                Commonutils.progressdialog_hide();
                //new Database(this).putStore(response);
                try {
                    // new Database(FoodStoreActivity.this).putStore(response);
                    JSONObject jsonObject = new JSONObject(response);
                    currency = jsonObject.getString("currency");
                    JSONArray response1 = jsonObject.getJSONArray("data_provider");
                    TypeToken<List<DataProvider>> token = new TypeToken<List<DataProvider>>() {
                    };
                    dataProviders = new Gson().fromJson(response1.toString(), token.getType());
                    setdatastore(dataProviders);
                    getDeviceLocation(dataProviders);
//===========
                    //lisfood
                    String response2 = jsonObject.getString("data_foods");
                    TypeToken<List<DataFood>> token1 = new TypeToken<List<DataFood>>() {
                    };
                    datafood = new Gson().fromJson(response2, token1.getType());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    RecyclerViewfood.setLayoutManager(linearLayoutManager);
                    adapter = new OrderfoodAdapter(FoodStoreActivity.this, datafood, this, currency);
                    RecyclerViewfood.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 101:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        JSONObject elementsObject = jsonArray.getJSONObject(0);
                        JSONArray elementsArray = elementsObject.getJSONArray("elements");
                        JSONObject distanceObject = elementsArray.getJSONObject(0);
                        JSONObject dObject = distanceObject.getJSONObject("distance");
                        String distance = dObject.getString("text");
                        txtdistance.setText(distance);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    private void setdatastore(List<DataProvider> dataProviders) {
        final String numberphone;
        DataProvider dataProvider1 = dataProviders.get(0);
        txtnamestore.setText("Store Name: " + dataProvider1.getNameStore());
        txtopentime.setText(dataProvider1.getOpeningTime());
        txtclosedtime.setText(dataProvider1.getClosedTime());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        time = simpleDateFormat.format(calendar.getTime());
        opentime = dataProvider1.getOpeningTime();
        openclose = dataProvider1.getClosedTime();
        Log.d("giohientai", "setdatastore: " + time + "   " + opentime + "   " + openclose);
        try {
            datetime = simpleDateFormat.parse(time);
            dateopentime = simpleDateFormat.parse(opentime);
            dateclosetime = simpleDateFormat.parse(openclose);
            if (dateopentime != null && dateclosetime != null) {
                if (datetime.after(dateopentime) && datetime.before(dateclosetime)) {
                    close_the_door = false;
                } else {
                    close_the_door = true;
                    new AlertDialog.Builder(this)
                            .setMessage("Currently the store is closed")
                            .setCancelable(false)
                            .setPositiveButton("See the store", null)
                            .setNegativeButton("Come back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtnumberphone.setText(dataProvider1.getMobile());
        if (dataProvider1.getCoverPicture().isEmpty()) {

        } else {
            String base64 = dataProvider1.getCoverPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(this).load(imageByteArray)
                    .asBitmap()
                    .into(coverimage);
        }
        numberphone = txtnumberphone.getText().toString().trim();
        layoutphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:" + numberphone));
//                startActivity(intent);
            }
        });
        locationstore = new LatLng(dataProvider1.getLatitude(), dataProvider1.getLongitude());
        layoutlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodStoreActivity.this, LocatinonstoreActivity.class);
                startActivity(intent);
                CustomIntent.customType(FoodStoreActivity.this, "fadein-to-fadeout");
            }
        });

    }


    private boolean isServiceOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(FoodStoreActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(FoodStoreActivity.this, available, 911);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void init() {
        getLocationPermission();

    }

    private void getLocationPermission() {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mPermissionCheck = true;

            } else {
                ActivityCompat.requestPermissions(this, permissions, 123);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 123);
        }
    }

    private void getDeviceLocation(final List<DataProvider> dataProviders) {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mPermissionCheck) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            android.location.Location currentLocation = (android.location.Location) task.getResult();
                            DataProvider dataProvider1 = dataProviders.get(0);
                            HashMap<String, String> map = new HashMap<>();
                            map.put(Const.Params.URL, Const.GOOGLE_MATRIX_URL + Const.Params.ORIGINS + "="
                                    + String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude()) + "&" + Const.Params.DESTINATION + "="
                                    + String.valueOf(dataProvider1.getLatitude()) + "," + String.valueOf(dataProvider1.getLongitude()) + "&" + Const.Params.MODE + "="
                                    + "driving" + "&" + Const.Params.LANGUAGE + "="
                                    + "en-EN" + "&" + "key=" + Const.GOOGLE_API_KEY + "&" + Const.Params.SENSOR + "="
                                    + String.valueOf(false));
                            Log.e("mahi", "distance api6" + map);
                            new VollyRequester(FoodStoreActivity.this, Const.GET, map, 101, FoodStoreActivity.this);
                        } else {
                            Toast.makeText(FoodStoreActivity.this, "Unable to get current location. Please open Google Map at least one time", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {

        }
    }


    @Override
    public void onClick1(ArrayList<DataFood> dataFood, int position, boolean isLongClick) {
        if (isLongClick == true) {
            Sheetdetailfood sheetdetailfood = new Sheetdetailfood();
            sheetdetailfood.show(getSupportFragmentManager(), "exampleBottomSheet");
            screen = 0;
            dataFood1 = dataFood.get(position);
        }
        if (isLongClick == false) {
            Log.d("bbbb", "onTaskCompleted:4 " + dataFood.size());
            if (dataFood.size() > 0) {
                fab_order.show();
                fab_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FoodStoreActivity.this, CartActivity.class);
                        startActivity(intent);
                        CustomIntent.customType(FoodStoreActivity.this, "fadein-to-fadeout");
                    }
                });
                for (int i = 0; i < dataFood.size(); i++) {
                    Log.d("listorder1", "onClick1: " + dataFood.get(i).getName() + position);
                }
            }
        }
        if (dataFood.size() == 0) {
            Log.d("bbbb", "onTaskCompleted:4 ");
            fab_order.hide();
        }
    }

    @Override
    protected void onRestart() {
        adapter.notifyDataSetChanged();
        if (OrderfoodAdapter.listorder.size() > 0) {
            fab_order.show();
        } else if (OrderfoodAdapter.listorder.size() == 0) {
            fab_order.hide();
        }
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        CustomIntent.customType(FoodStoreActivity.this, "fadein-to-fadeout");
        super.onBackPressed();
    }

    @Override
    public void onLocationReceived(LatLng latlong) {

    }

    @Override
    public void onLocationReceived(Location location) {

    }

    @Override
    public void onConntected(Bundle bundle) {

    }

    @Override
    public void onConntected(Location location) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        OrderfoodAdapter.listorder.clear();
        OrderfoodAdapter.map.clear();
        Log.d("cccccc", "onDestroy: ");
        openclose = null;
        openclose = null;
        dateopentime = null;
        dateclosetime = null;
        super.onDestroy();
    }
}

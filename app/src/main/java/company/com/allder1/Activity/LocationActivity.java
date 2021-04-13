package company.com.allder1.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import company.com.allder1.Adapter.PlacesAutoCompleteAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.AndyUtils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.LocationHelper;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, LocationHelper.OnLocationReceived, AsyncTaskCompleteListener {
    GoogleMap mMap;
    MapFragment mapFragment;
    LatLng locationstore;
    public static String address_name;
    String sourceAddress, destAddress, stopAddress;
    public static double Long, Lat;
    private double Long1, Lat1;
    private LatLng des_latLng;
    AutoCompleteTextView et_destination_address;
    private PlacesAutoCompleteAdapter placesadapter;
    private PlacesAutoCompleteAdapter dest_placesadapter, stop_placesadapter;
    private boolean mPermissionCheck = false;
    private android.support.v7.widget.Toolbar toolbar;
    private LocationHelper locHelper;
    private Boolean mLocationPermissionsGranted = false;
    private boolean s_click = false, d_click = false, stop_click = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        locHelper = new LocationHelper(LocationActivity.this);
        locHelper.setLocationReceivedLister(this);
        if (isServiceOk()) {
            init();
            //initToolbar();
        }
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initToolbar();
        Intent intent1 = getIntent();
        Lat1 = intent1.getDoubleExtra("Lat", 0.0);
        Long1 = intent1.getDoubleExtra("Long", 0.0);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else getDeviceLocation();
    }

    private void init() {
        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mPermissionCheck) {
            if (Long != 0 && Lat != 0 && Lat1 == 0.00000000 && Long1 == 0.00000000) {
                mMap.clear();
                moveCamera(new LatLng(Lat, Long), 15f);
                mMap.addMarker(new MarkerOptions().position(new LatLng(Lat, Long))
                        .title(getCompleteAddressString(Lat, Long)));
            } else if (Lat1 != 0.00000000 && Long1 != 0.00000000) {
                mMap.clear();
                moveCamera(new LatLng(Lat1, Long1), 15f);
                mMap.addMarker(new MarkerOptions().position(new LatLng(Lat1, Long1))
                        .title(getCompleteAddressString(Lat1, Long1)));
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                Long = point.longitude;
                Lat = point.latitude;
                //  moveCamera(point,15f );
                mMap.addMarker(new MarkerOptions().position(point)
                        .title(getCompleteAddressString(point.latitude, point.longitude)));
                et_destination_address.setText(getCompleteAddressString(point.latitude, point.longitude));
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_img_bt: {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(LocationActivity.this);
                    startActivityForResult(intent, 111);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
            ;
            break;
            case R.id.gps_img_bt: {
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else getDeviceLocation();
            }
            break;

        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mPermissionCheck) {
                Log.d("Manh", Long + "onComplete4: " + Lat);
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Log.d("Manh", Long + "onComplete1: " + Lat);
                        if (task.isSuccessful() && task.getResult() != null) {
                            android.location.Location currentLocation = (android.location.Location) task.getResult();
                            mMap.clear();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                    .title(getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude())));
                            Long = currentLocation.getLongitude();
                            Lat = currentLocation.getLatitude();
                            et_destination_address.setText(getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude()));
                            Log.d("Manh", Long + "onComplete2: " + Lat);
                            mMap.setMyLocationEnabled(true);
                            destAddress = getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude());
                        } else {
                            Log.d("Manh", Long + "onComplete3: " + Lat);
                            Toast.makeText(LocationActivity.this, "Unable to get current location. Please open Google Map at least one time", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {

        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    mMap.clear();
                    moveCamera(latLng, 15f);
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(String.valueOf(place.getAddress())));
                    Long = latLng.longitude;
                    Lat = latLng.latitude;
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private boolean isServiceOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LocationActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LocationActivity.this, available, 911);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
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
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 123);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 123);
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionCheck = false;
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mPermissionCheck = false;
                            return;
                        }
                    }
                    mPermissionCheck = true;
                    initMap();
                }
            }
        }
    }


    @Override
    public void onLocationReceived(LatLng latlong) {

    }

    @Override
    public void onLocationReceived(Location location) {
        Log.d("Manh", "location: " + location.getLongitude());
    }

    @Override
    public void onConntected(Bundle bundle) {

    }

    @Override
    public void onConntected(Location location) {
        Log.d("Manh", "location: " + location.getLongitude());
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.toobarlocation, null);
        actionBar.setCustomView(customView);
        final ImageButton cancel = customView.findViewById(R.id.imgBack);
        et_destination_address = customView.findViewById(R.id.et_destination_address);
        Button btnsave = customView.findViewById(R.id.btnsave);
        ImageButton imgBack = customView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dest_placesadapter = new PlacesAutoCompleteAdapter(LocationActivity.this,
                R.layout.autocomplete_list_text);
        if (dest_placesadapter != null) {
            et_destination_address.setAdapter(dest_placesadapter);
        }

        et_destination_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_destination_address.setSelection(0);
                destAddress = et_destination_address.getText().toString();
                d_click = true;
                s_click = false;
                stop_click = false;
                AndyUtils.hideKeyBoard(LocationActivity.this);
                final String selectedDestPlace = dest_placesadapter.getItem(i);
                try {
                    getLatlanfromAddress(URLEncoder.encode(selectedDestPlace, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }


        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationActivity.this.finish();
                address_name = et_destination_address.getText().toString().trim();
                LocationActivity.this.finish();
            }
        });
    }

    private void getLatlanfromAddress(String selectedSourcePlace) {
        if (!AndyUtils.isNetworkAvailable(LocationActivity.this)) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.LOCATION_API_BASE + selectedSourcePlace + "&key=" + Const.GOOGLE_API_KEY);
        new VollyRequester(LocationActivity.this, Const.GET, map, Const.ServiceCode.LOCATION_API_BASE_SOURCE, (AsyncTaskCompleteListener) this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.LOCATION_API_BASE_SOURCE:
                if (null != response) {
                    if (d_click == true) {
                        try {
                            Log.d("Manh", "location: " + response);
                            JSONObject job = new JSONObject(response);
                            JSONArray jarray = job.optJSONArray("results");
                            JSONObject locObj = jarray.getJSONObject(0);
                            JSONObject geometryOBJ = locObj.optJSONObject("geometry");
                            JSONObject locationOBJ = geometryOBJ.optJSONObject("location");
                            Lat = locationOBJ.getDouble("lat");
                            Long = locationOBJ.getDouble("lng");
                            LatLng latLng = new LatLng(Lat, Long);
                            mMap.clear();
                            moveCamera(latLng, 15f);
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .title(String.valueOf(address_name)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
        }
    }
}

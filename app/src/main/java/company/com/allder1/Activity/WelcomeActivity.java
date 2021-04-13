package company.com.allder1.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import company.com.allder1.R;
import company.com.allder1.database.Database;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.LocationHelper;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class WelcomeActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, AsyncTaskCompleteListener, LocationHelper.OnLocationReceived {
    Handler handler;
    GoogleApiClient googleApiClient;
    Database dbfoodhome;
    double Lat, Long;
    private LocationHelper locHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mapped();
        dbfoodhome = new Database(this);
        getDrivertoken();
        locHelper = new LocationHelper(WelcomeActivity.this);
        locHelper.setLocationReceivedLister(this);
        getcategory();
        getrecommended();
        handlerwelcome();
    }

    private void getcategory() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETCATEGORY + "sort=" + "asc");
        Log.d("maih", "getcategory:" + map.toString());
        new VollyRequester(WelcomeActivity.this, Const.GET, map, Const.ServiceCode.GETCATEGORY,
                this);
    }

    private void handlerwelcome() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new PreferenceHelper(WelcomeActivity.this).getUserId() != null) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(WelcomeActivity.this, "fadein-to-fadeout");
                    finish();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, ChooseloginActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(WelcomeActivity.this, "fadein-to-fadeout");
                    finish();
                }
            }
        }, 3000);
    }

    private void getDrivertoken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("token", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        new PreferenceHelper(WelcomeActivity.this).putDeviceToken(token);
                        Log.d("token", token);
                        // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mapped() {

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void getrecommended() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETSTORENEARBY + "type=recommended");
        Log.d("maih", "getcategory:" + map.toString());
        new VollyRequester(WelcomeActivity.this, Const.GET, map, Const.ServiceCode.GETSRECOMMENDED,
                this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 111:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleApiClient.connect();
                } else {

                }
                break;
        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GETCATEGORY:
                try {
                    Log.d("3333", "onTaskCompleted: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        dbfoodhome.putListcategory(response);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.GETSTORENEARBY:
                Commonutils.progressdialog_hide();
                Log.d("4444", "onTaskCompleted: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                        dbfoodhome.putnearbystore(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.GETSRECOMMENDED:
                try {
                    Log.d("5555", "onTaskCompleted: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                        dbfoodhome.putlistrecommended(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
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
        Lat = location.getLatitude();
        Long = location.getLongitude();
        getstorenearby(Lat, Long);
    }
    private void getstorenearby(double Lat, double Long) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETSTORENEARBY + "type=nearby&latitude=" + Lat + "&longitude=" + Long);
        Log.d("maih", "getcategory: " + map.toString());
        new VollyRequester(WelcomeActivity.this, Const.GET, map, Const.ServiceCode.GETSTORENEARBY,
                this);
    }
}

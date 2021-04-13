package company.com.allder1.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.AndyUtils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class SettingsActivity extends AppCompatActivity implements AsyncTaskCompleteListener {
    Button btnlogout,btneditprofile,btnchangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Mapped();
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SettingsActivity.this,EditProfileActivity.class);
                    startActivity(intent);
                CustomIntent.customType(SettingsActivity.this, "fadein-to-fadeout");
            }
        });
        btnchangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SettingsActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
                CustomIntent.customType(SettingsActivity.this, "fadein-to-fadeout");
            }
        });

    }

    private void dialog() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to log out")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (new PreferenceHelper(SettingsActivity.this).getSpecies().equals("consumer")) {
                            logout();
                        } else {
                            logoutprovider();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void logoutprovider() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.LOGOUTPOVIDER);
        map.put(Const.Params.ID, new PreferenceHelper(SettingsActivity.this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(SettingsActivity.this).getSessionToken());
        Log.d("Manh", "logout provider map " + map.toString());
        new VollyRequester(SettingsActivity.this, Const.POST, map, Const.ServiceCode.LOGOUTPOVIDER, this);
    }

    private void logout() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.LOGOUT);
        map.put(Const.Params.ID, new PreferenceHelper(SettingsActivity.this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(SettingsActivity.this).getSessionToken());
        Log.d("Manh", "logout consumer map " + map.toString());
        new VollyRequester(SettingsActivity.this, Const.POST, map, Const.ServiceCode.LOGOUT, this);
    }
    private void Mapped() {
        btnlogout = findViewById(R.id.btnlogout);
        btneditprofile = findViewById(R.id.btneditprofile);
        btnchangePassword = findViewById(R.id.btnchangePassword);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        Log.d("Manh", "response logout "+response);
        switch (serviceCode) {
            case Const.ServiceCode.LOGOUT:
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {
                        new PreferenceHelper(SettingsActivity.this).Logout();
                        Intent i = new Intent(SettingsActivity.this, ChooseloginActivity.class);
                        startActivity(i);
                        finishAffinity();
                        finish();
                    } else {
                        String error_code = job.optString("error_code");
                        if (error_code.equals("104")) {
                            AndyUtils.showShortToast("You have logged in other device!", SettingsActivity.this);
                            new PreferenceHelper(SettingsActivity.this).Logout();
                            startActivity(new Intent(SettingsActivity.this, ChooseloginActivity.class));
                            SettingsActivity.this.finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.LOGOUTPOVIDER:
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {
                        new PreferenceHelper(SettingsActivity.this).Logout();
                        new PreferenceHelper(SettingsActivity.this).Logout();
                        Intent i = new Intent(SettingsActivity.this, ChooseloginActivity.class);
                        startActivity(i);
                        finishAffinity();
                        finish();
                    } else {
                        String error_code = job.optString("error_code");
                        if (error_code.equals("104")) {
                            AndyUtils.showShortToast("You have logged in other device!", SettingsActivity.this);
                            new PreferenceHelper(SettingsActivity.this).Logout();
                            startActivity(new Intent(SettingsActivity.this, ChooseloginActivity.class));
                            SettingsActivity.this.finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        CustomIntent.customType(this, "fadein-to-fadeout");
        super.onBackPressed();
    }
}

package company.com.allder1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TimeZone;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.ParseContent;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class RegisterActivity extends AppCompatActivity implements AsyncTaskCompleteListener, View.OnClickListener {
    EditText edtfirstname, edtlastname, edtemail, edtpassword, edtreferral;
    Button btnsignup;
    String species;
    ImageView iv_showpass;
    String mFiestmane, mLastname, mEmail, mPassword, mReferral;
    private ParseContent pcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Mapedd();
        iv_showpass.setOnClickListener(this);
        pcontent = new ParseContent(this);
    }

    private void Mapedd() {
        edtfirstname = findViewById(R.id.edtfirstname);
        edtlastname = findViewById(R.id.edtlastname);
        edtemail = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassworld);
        edtreferral = findViewById(R.id.edtcode);
        btnsignup = findViewById(R.id.btnsign_up);
        iv_showpass = findViewById(R.id.iv_showpass);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.REGISTERCONSUMER:
                if (response != null)
                    try {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {
                            if (pcontent.isSuccessWithStoreId(response)) {
                                pcontent.parseUserAndStoreToDb(response);
                                new PreferenceHelper(RegisterActivity.this).putPassword(mPassword);
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                Toast.makeText(this, "Sign success consumer", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            CustomIntent.customType(this, "fadein-to-fadeout");
                            RegisterActivity.this.finish();
                        } else if (job1.getString("success").equals("false")) {
                            String error = job1.getString("error_messages");
                            Commonutils.showtoast(error, RegisterActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
            case Const.ServiceCode.REGISTERPROVIDER:
                if (response != null)
                    try {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {
                            if (pcontent.isSuccessWithStoreId(response)) {
                                pcontent.parseUserAndStoreToDb(response);
                                new PreferenceHelper(RegisterActivity.this).putPassword(mPassword);
                                Toast.makeText(this, "Sign success provider", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                            }
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            CustomIntent.customType(this, "fadein-to-fadeout");
                            RegisterActivity.this.finish();
                        } else if (job1.getString("success").equals("false")) {
                            String error = job1.getString("error_messages");
                            Commonutils.showtoast(error, RegisterActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsign_up:
                if (new PreferenceHelper(this).getSpecies().contains("consumer")) {
                    if (validate()) {
                        registeration(Const.MANUAL);
                    }
                } else if (new PreferenceHelper(this).getSpecies().contains("provider")) {
                    if (validate()) {
                        registerationpovider(Const.MANUAL);
                    }
                }
                break;
            case  R.id.iv_showpass:
                if (edtpassword.getInputType() == 129) {
                    edtpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (edtpassword.getInputType() == 1) {
                    edtpassword.setInputType(129);
                }
                break;
        }

    }

    private void registerationpovider(String type) {
        if (type.equals(Const.MANUAL)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Const.Params.URL, Const.ServiceType.REGISTERPROVIDER);
            map.put(Const.Params.FIRSTNAME, mFiestmane);
            map.put(Const.Params.LAST_NAME, mLastname);
            map.put(Const.Params.EMAIL, mEmail);
            map.put(Const.Params.PASSWORD, mPassword);
            map.put(Const.Params.CURRENCEY, "1");
            map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(RegisterActivity.this).getDeviceToken());
            map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
            map.put(Const.Params.LOGIN_BY, Const.MANUAL);
            map.put(Const.Params.PHONE, SenOtpActivity.NumberPhone2);
            map.put(Const.Params.TIMEZONE, TimeZone.getDefault().getID());
            map.put(Const.Params.REFERRAL_CODE, mReferral);
            Log.d("Manh", "registeration: " + map);
            new VollyRequester(RegisterActivity.this, Const.POST, map, Const.ServiceCode.REGISTERPROVIDER,
                    this);
        }

    }

    private boolean validate() {
        getAllRegisterDetails();
        if (mFiestmane.length() == 0) {
            edtfirstname.setError(getResources().getString(R.string.txt_fname_error));
            edtfirstname.requestFocus();
            return false;
        } else if (mLastname.length() == 0) {
            edtlastname.setError(getResources().getString(R.string.txt_fname_error));
            edtlastname.requestFocus();
            return false;
        } else if (mEmail.length() == 0) {
            edtemail.setError(getResources().getString(R.string.txt_fname_error));
            edtemail.requestFocus();
            return false;
        } else if (mPassword.length() == 0) {
            edtpassword.setError(getResources().getString(R.string.txt_fname_error));
            edtpassword.requestFocus();
            return false;
        }
        return true;
    }

    private void getAllRegisterDetails() {
        mFiestmane = edtfirstname.getText().toString().trim();
        mLastname = edtlastname.getText().toString().trim();
        mEmail = edtemail.getText().toString().trim();
        mPassword = edtpassword.getText().toString().trim();
        mReferral = edtreferral.getText().toString().trim();
    }

    private void registeration(String type) {
        if (type.equals(Const.MANUAL)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Const.Params.URL, Const.ServiceType.REGISTERCONSUMWE);
            map.put(Const.Params.FIRSTNAME, mFiestmane);
            map.put(Const.Params.LAST_NAME, mLastname);
            map.put(Const.Params.EMAIL, mEmail);
            map.put(Const.Params.PASSWORD, mPassword);
            map.put(Const.Params.CURRENCEY, "1");
            map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(RegisterActivity.this).getDeviceToken());
            map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
            map.put(Const.Params.LOGIN_BY, Const.MANUAL);
            map.put(Const.Params.PHONE, SenOtpActivity.NumberPhone2);
            map.put(Const.Params.TIMEZONE, TimeZone.getDefault().getID());
            map.put(Const.Params.REFERRAL_CODE, mReferral);
            Log.d("Manh", "registeration: " + map);
            new VollyRequester(RegisterActivity.this, Const.POST, map, Const.ServiceCode.REGISTERCONSUMER,
                    this);
        }

    }
}

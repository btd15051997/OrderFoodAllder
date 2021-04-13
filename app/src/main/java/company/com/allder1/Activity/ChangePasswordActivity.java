package company.com.allder1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class ChangePasswordActivity extends AppCompatActivity implements AsyncTaskCompleteListener {
    TextView txtnotifi, txttitle;
    EditText edtpassword;
    Button btnnext;
    String oldpassword, newpassword, confirm_password;
    private PreferenceHelper PreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Mapped();
        PreferenceHelper = new PreferenceHelper(this);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpassword = edtpassword.getText().toString().trim();
                if (oldpassword.length() != 0) {
                    if (PreferenceHelper.getPassword().equals(oldpassword)) {
                        edtpassword.setText("");
                            txtnotifi.setText("Confirmed password");
                        txttitle.setText("New password");
                        btnnext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newpassword = edtpassword.getText().toString().trim();
                                if (newpassword.length() != 0) {
                                    edtpassword.setText("");
                                    txttitle.setText("Confirmed password");
                                    txtnotifi.setText("Please enter a confirm password");
                                    btnnext.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            confirm_password = edtpassword.getText().toString().trim();
                                            btnnext.setText(R.string.validate);
                                            if (confirm_password.length() != 0) {
                                                if (newpassword.equals(confirm_password)) {
                                                    if (new PreferenceHelper(ChangePasswordActivity.this).getSpecies().equals("consumer")) {
                                                        Changepassword();
                                                    } else {
                                                        Changepasswordprovider();
                                                    }
                                                } else {
                                                    Toast.makeText(ChangePasswordActivity.this, "Passwords aren't matched together", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                edtpassword.setError("Please input your password");
                                            }
                                        }
                                    });

                                } else {
                                    edtpassword.setError("Please input your password");
                                }

                            }
                        });
                    }
                } else {
                    edtpassword.setError("Please input your password");
                }
            }
        });
    }

    private void Changepasswordprovider() {
        HashMap<String, String> map = new HashMap<String, String>();
        Commonutils.progressdialog_show(this, "Loading...");
        map.put(Const.Params.URL, Const.ServiceType.CHANGEPASSWORDPROVIDER);
        map.put(Const.Params.ID, PreferenceHelper.getUserId());
        map.put(Const.Params.TOKEN, PreferenceHelper.getSessionToken());
        map.put(Const.Params.OLD_PASSWORD, oldpassword);
        map.put(Const.Params.PASSWORD, newpassword);
        map.put(Const.Params.confirm_password, confirm_password);
        Log.d("Manhchangpasswordprovider"," Changepasswordprovider:"+ map.toString());
        new VollyRequester(this, Const.POST, map, Const.ServiceCode.CHANGE_PASSWORD_CONSUMER,
                this);
    }

    private void Changepassword() {
        Commonutils.progressdialog_show(this, "Loading...");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.CHANGEPASSWORDCONSUMER);
        map.put(Const.Params.ID, PreferenceHelper.getUserId());
        map.put(Const.Params.TOKEN, PreferenceHelper.getSessionToken());
        map.put(Const.Params.OLD_PASSWORD, oldpassword);
        map.put(Const.Params.PASSWORD, newpassword);
        map.put(Const.Params.confirm_password, confirm_password);
        Log.d("Manh","Changepassword consumer"+ map.toString());
        new VollyRequester(this, Const.POST, map, Const.ServiceCode.CHANGE_PASSWORD_CONSUMER,
                this);
    }

    private void Mapped() {
        txtnotifi = findViewById(R.id.txtnotification);
        txttitle = findViewById(R.id.txttitle);
        edtpassword = findViewById(R.id.edtpassword);
        btnnext = findViewById(R.id.btnnext);
    }
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.CHANGE_PASSWORD_CONSUMER:
                Commonutils.progressdialog_hide();
                try {
                    JSONObject job1 = new JSONObject(response);
                    if (job1.getString("success").equals("true")) {
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        CustomIntent.customType(this, "fadein-to-fadeout");
                    } else {
                        String error = job1.getString("error_messages");
                        Commonutils.showtoast(error, ChangePasswordActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        Log.d("Manh", "CHANGE PASSWORD: " + response);
    }

    @Override
    public void onBackPressed() {
        CustomIntent.customType(this, "fadein-to-fadeout");
        super.onBackPressed();
    }
}

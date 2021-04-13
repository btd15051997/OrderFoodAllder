package company.com.allder1.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;

public class ForgotPasswordActivity extends AppCompatActivity implements AsyncTaskCompleteListener {
    EditText edtemail;
    TextInputLayout input_layout_email;
    Button btnRequestPassword;
    String email;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Mapped();
        btnRequestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    if (new PreferenceHelper(ForgotPasswordActivity.this).getSpecies().contains("consumer")) {
                        forgotpasswordconsumer();
                    } else {
                        forgotpasswordprovider();
                    }
                }

            }
        });
    }

    private void forgotpasswordprovider() {
        Commonutils.progressdialog_show(this, "Requesting Password...");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.FORGOT_PASSWORDPROVIDER);
        map.put(Const.Params.EMAIL, email);
        Log.d("Manh", "post : for got password provider" + map.toString());
        new VollyRequester(this, Const.POST, map, Const.ServiceCode.FORGOT_PASSWORDPROVIDER,
                this);
    }
    private void forgotpasswordconsumer() {
        Commonutils.progressdialog_show(this, "Requesting Password...");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.FORGOT_PASSWORD);
        map.put(Const.Params.EMAIL,email);
        Log.d("Manh", "post : for got password consumer" + map.toString());
        new VollyRequester(this, Const.POST, map, Const.ServiceCode.FORGOT_PASSWORD,
                this);
    }

    private boolean validate() {
        email = edtemail.getText().toString().trim();
        if (email.length() == 0) {
            input_layout_email.setError("Please enter email");
            return false;
        }
        return true;
    }

    private void Mapped() {
        edtemail = findViewById(R.id.edtemail);
        btnRequestPassword = findViewById(R.id.btnRequestPassword);
        input_layout_email = findViewById(R.id.input_layout_email);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.FORGOT_PASSWORD:
                Log.d("ManhForGotPASSWORD", "response: forgot password" + response);
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {
                        Commonutils.progressdialog_hide();
                        Commonutils.showtoast("Password Sent successfully to Registered Mail Id!", this);
                    } else {
                        Commonutils.progressdialog_hide();
                        String error = job.getString("error_messages");
                        Commonutils.showtoast(error, this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.FORGOT_PASSWORDPROVIDER:
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {
                        Commonutils.progressdialog_hide();
                        Commonutils.showtoast("Password Sent successfully to Registered Mail Id!", this);
                    } else {
                        Commonutils.progressdialog_hide();
                        String error = job.getString("error_messages");
                        Commonutils.showtoast(error, this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}

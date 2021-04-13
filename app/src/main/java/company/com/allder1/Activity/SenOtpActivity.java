package company.com.allder1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.AndyUtils;
import company.com.allder1.utils.Const;
import maes.tech.intentanim.CustomIntent;

public class SenOtpActivity extends AppCompatActivity implements AsyncTaskCompleteListener {
    EditText edtnumberphone, edtphone;
    Button btnsend;
    String NumberPhone;
    public static String NumberPhone2;
    TextView txttitle;
    ImageView imgedit;
    LinearLayout linear, send_back_otp;
    TextInputLayout mInput_layout_otp;
    CountryCodePicker cpp_PhoneCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sen_otp);
        Mapped();
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtnumberphone.getText().toString().length() != 0) {
                    getNumberPhone();
                    senopt(NumberPhone);
                    edtphone.setText(NumberPhone);
                } else {
                    mInput_layout_otp.setError(getResources().getString(R.string.txt_phone_error));
                }
            }
        });
    }

    private void getNumberPhone() {
        cpp_PhoneCode.registerCarrierNumberEditText(edtnumberphone);
        NumberPhone = cpp_PhoneCode.getFullNumberWithPlus();
        Toast.makeText(this, "Number Phone: " + NumberPhone, Toast.LENGTH_SHORT).show();
    }

    private void Mapped() {
        edtnumberphone = findViewById(R.id.consumer_send_edt_numberphone);
        edtphone = findViewById(R.id.edtphone);
        cpp_PhoneCode = findViewById(R.id.ccp_PhoneCode);
        btnsend = findViewById(R.id.btnsend);
        txttitle = findViewById(R.id.txttitle);
        linear = findViewById(R.id.linear);
        imgedit = findViewById(R.id.imgedit);
        send_back_otp = findViewById(R.id.send_back_otp);
        mInput_layout_otp = findViewById(R.id.input_layout_otp);
    }

    private void senopt(String NumberPhone) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.GET_OTP + "&" + Const.Params.PHONE + "=" + NumberPhone);
        new VollyRequester(SenOtpActivity.this, Const.GET, map, Const.ServiceCode.GET_OTP,
                this);
        Log.d("bbbb", "senopt: "+map);
        AndyUtils.showSimpleProgressDialog(SenOtpActivity.this, "Requesting Otp...", false);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        Log.d("mahi", "OTP Response" + serviceCode);
        switch (serviceCode) {
            case Const.ServiceCode.GET_OTP:
                Log.d("mahi", "OTP Response" + response);
                AndyUtils.removeProgressDialog();
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {
                        final String code = job.optString("code");
                        cpp_PhoneCode.setVisibility(View.GONE);
                        linear.setVisibility(View.VISIBLE);
                        btnsend.setText(R.string.validate);
                        send_back_otp.setVisibility(View.VISIBLE);
                        imgedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                edtphone.setEnabled(true);
                            }
                        });
                        send_back_otp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                senopt(edtphone.getText().toString().trim());
                                edtphone.setText(edtphone.getText().toString().trim());
                            }
                        });
                        txttitle.setText(R.string.comfirm_otp);
                        edtnumberphone.setText(code);
                        mInput_layout_otp.setErrorEnabled(false);
                        if (edtnumberphone.getText().toString().length() != 0) {
                            btnsend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String otpCode = edtnumberphone.getText().toString().trim();
                                    if (otpCode.equals(code)) {
                                            Intent intent = new Intent(SenOtpActivity.this, RegisterActivity.class);
                                            startActivity(intent);
                                        CustomIntent.customType(SenOtpActivity.this, "fadein-to-fadeout");
                                            intent.putExtra("phone", edtphone.getText().toString().trim());
                                            NumberPhone2 = edtphone.getText().toString().trim();
                                            SenOtpActivity.this.finish();

                                    } else {
                                        mInput_layout_otp.setError(getResources().getString(R.string.txt_otp_wrong));
                                    }
                                }
                            });
                        } else {
                            mInput_layout_otp.setError(getResources().getString(R.string.txt_otp_error));
                        }
                    } else {
                        String error = job.optString("message");
                        AndyUtils.showShortToast(error, SenOtpActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

package company.com.allder1.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.Commonutils;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.ParseContent;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {
    EditText edtemail, edtpassworld;
    Button btnlogin;
    ImageView iv_showpass;
    LinearLayout btnfacebook, btngoogle, txtregistered;
    TextView txtforgot_pasworld;
    String mphone, mpassworld;
    private ParseContent pcontent;
    private FacebookCallback<LoginResult> loginResult;
    private LoginActivity loginActivity;
    private CallbackManager callbackFaceBookManager;
    private GoogleSignInClient mGoogleSignInClient;
    int GG_SIGN_IN = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        getPermission();
        printKeyHash(this);
        initFBLogin();
        pcontent = new ParseContent(this);
        btnlogin.setOnClickListener(this);
        iv_showpass.setOnClickListener(this);
    }

    private void initFBLogin() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackFaceBookManager = CallbackManager.Factory.create();
        loginActivity = this;
        validateLoginFaceBook();
        LoginManager.getInstance().registerCallback(callbackFaceBookManager, loginResult);
        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFaceBook();
            }
        });

    }

    public void loginFaceBook() {
        LoginManager.getInstance().logInWithReadPermissions(loginActivity, Arrays.asList("public_profile", "user_friends", "email"));
    }

    public String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info

            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void mapping() {
        initGoogleLogin();
        edtemail = findViewById(R.id.edtemail);
        edtpassworld = findViewById(R.id.edtpassworld);
        btnlogin = findViewById(R.id.btnlogin);
        btnfacebook = findViewById(R.id.btnfacebook);
        btngoogle = findViewById(R.id.btngoogle);
        txtforgot_pasworld = findViewById(R.id.txtforgotpassworld);
        txtregistered = findViewById(R.id.txtregistered);
        iv_showpass = findViewById(R.id.iv_showpass);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnlogin:
                if (new PreferenceHelper(this).getSpecies().contains("consumer")) {
                    if (validate()) {
                        UserLogin(Const.MANUAL);
                    }
                } else if (new PreferenceHelper(this).getSpecies().contains("provider")) {
                    if (validate()) {
                        ProviderLogin(Const.MANUAL);
                    }
                }
                break;
            case R.id.btnfacebook:
                Toast.makeText(this, "đăng ký , đăng nhập facebook", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btngoogle:
                loginGoogle();
                break;
            case R.id.txtregistered:
                Intent intent = new Intent(LoginActivity.this, SenOtpActivity.class);
                startActivity(intent);
                CustomIntent.customType(LoginActivity.this, "fadein-to-fadeout");
                break;
            case R.id.txtforgotpassworld:
                Intent intent1 = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_showpass:

                if (edtpassworld.getInputType() == 129) {
                    edtpassworld.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (edtpassworld.getInputType() == 1) {
                    edtpassworld.setInputType(129);
                }
                break;
        }
    }

    private void ProviderLogin(String manual) {
        if (manual.equalsIgnoreCase(Const.MANUAL)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Const.Params.URL, Const.ServiceType.LOGINPROVIDER);
            map.put(Const.Params.EMAIL, mphone);
            map.put(Const.Params.PASSWORD, mpassworld);
            map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(LoginActivity.this).getDeviceToken());
            map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
            map.put(Const.Params.LOGIN_BY, Const.MANUAL);
            Log.d("mahi", map.toString());
            new VollyRequester(this, Const.POST, map, Const.ServiceCode.LOGINPROVIDER,
                    this);
        } else {
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put(Const.Params.URL, Const.ServiceType.LOGIN);
//            map.put(Const.Params.SOCIAL_ID, sSocial_unique_id);
//            map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(this).getDeviceToken());
//            map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
//            map.put(Const.Params.LOGIN_BY, logintype);
//            Log.d("mahi", "social" + map.toString());
//            new VollyRequester(this, Const.POST, map, Const.ServiceCode.LOGIN,
//                    this);
        }
    }

    private void UserLogin(String logintype) {
        if (logintype.equalsIgnoreCase(Const.MANUAL)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Const.Params.URL, Const.ServiceType.LOGINCONSUMER);
            map.put(Const.Params.EMAIL, mphone);
            map.put(Const.Params.PASSWORD, mpassworld);
            map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(LoginActivity.this).getDeviceToken());
            map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
            map.put(Const.Params.LOGIN_BY, Const.MANUAL);
            Log.d("mahi", map.toString());
            new VollyRequester(this, Const.POST, map, Const.ServiceCode.LOGINCONSUMER,
                    this);
        } else {
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put(Const.Params.URL, Const.ServiceType.LOGIN);
//            map.put(Const.Params.SOCIAL_ID, sSocial_unique_id);
//            map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(this).getDeviceToken());
//            map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
//            map.put(Const.Params.LOGIN_BY, logintype);
//            Log.d("mahi", "social" + map.toString());
//            new VollyRequester(this, Const.POST, map, Const.ServiceCode.LOGIN,
//                    this);
        }
    }


    private void getLoginDetails() {
        mphone = edtemail.getText().toString().trim();
        mpassworld = edtpassworld.getText().toString().trim();
    }

    private boolean validate() {
        getLoginDetails();
        if (mphone.length() == 0) {
            edtemail.setError(getResources().getString(R.string.txt_email_error));
            edtemail.requestFocus();
            return false;
        } else if (mpassworld.length() == 0) {
            edtpassworld.setError(getResources().getString(R.string.txt_pass_error));
            edtpassworld.requestFocus();
            return false;
        } else {
            edtemail.setError(null);
            edtpassworld.setError(null);
            return true;
        }
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        Commonutils.progressdialog_hide();
        switch (serviceCode) {
            case Const.ServiceCode.LOGINCONSUMER:
                Log.d("loginresponse", "" + response);
                if (response != null) {
                    try {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {
                            Toast.makeText(this, "Login consumer", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            if (pcontent.isSuccessWithStoreId(response)) {
                                new PreferenceHelper(LoginActivity.this).putPassword(mpassworld);
                                pcontent.parseUserAndStoreToDb(response);
                                this.finish();
                            } else {
                                Toast.makeText(this, "login error! ", Toast.LENGTH_SHORT).show();
                            }

                        } else if (job1.getString("success").equals("false")) {
                            String error = job1.getString("error");
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Const.ServiceCode.LOGINPROVIDER:
                if (response != null) {
                    try {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {
                            Toast.makeText(this, "Login provider", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            if (pcontent.isSuccessWithStoreId(response)) {
                                pcontent.parseUserAndStoreToDb(response);
                                new PreferenceHelper(LoginActivity.this).putPassword(mpassworld);
                                startActivity(new Intent(this, MainActivity.class));
                                this.finish();
                            } else {

                            }

                        } else if (job1.getString("success").equals("false")) {
                            String error = job1.getString("error");
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    private void getPermission() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            String[] permissions_dummy = new String[6];
            int i = 0;

            String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
            int res = checkCallingOrSelfPermission(permission);
            if (res != PackageManager.PERMISSION_GRANTED) {
                permissions_dummy[i] = permission;
                i = i + 1;


            }
            permission = "android.permission.READ_EXTERNAL_STORAGE";
            res = checkCallingOrSelfPermission(permission);
            if (res != PackageManager.PERMISSION_GRANTED) {
                permissions_dummy[i] = permission;
                i = i + 1;
            }
            permission = "android.permission.WRITE_EXTERNAL_STORAGE";
            res = checkCallingOrSelfPermission(permission);
            if (res != PackageManager.PERMISSION_GRANTED) {
                permissions_dummy[i] = permission;
                i = i + 1;
            }
            permission = "android.permission.READ_CONTACTS";
            res = checkCallingOrSelfPermission(permission);
            if (res != PackageManager.PERMISSION_GRANTED) {
                permissions_dummy[i] = permission;
                i = i + 1;
            }
            permission = "android.permission.CAMERA";
            res = checkCallingOrSelfPermission(permission);
            if (res != PackageManager.PERMISSION_GRANTED) {
                permissions_dummy[i] = permission;
                i = i + 1;
            }
            permission = "android.permission.ACCESS_FINE_LOCATION";
            res = checkCallingOrSelfPermission(permission);
            if (res != PackageManager.PERMISSION_GRANTED) {
                permissions_dummy[i] = permission;
                i = i + 1;
            }


            String[] permissions = new String[i];

            for (int j = 0; j < i; j++) {
                permissions[j] = permissions_dummy[j];
            }
            int yourRequestId = 1;
            if (i != 0) {
                // Do something for lollipop and above versions
                requestPermissions(permissions, yourRequestId);
            }

        }
    }

    private void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
    }

    private void loginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GG_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GG_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackFaceBookManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "" + account.getEmail(), Toast.LENGTH_SHORT).show();
            // Signed in successfully, show authenticated UI.
            //login thành công sử lí ở đây
            //updateUI(account);
        } catch (ApiException e) {
            Log.w("failed", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void validateLoginFaceBook() {
        loginResult = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Login thành công xử lý tại đây
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                // Application code
                                String name = object.optString(getString(R.string.name));
                                String id = object.optString(getString(R.string.id));
                                String email = object.optString(getString(R.string.email));
                                String link = object.optString(getString(R.string.link));
                                URL imageURL = extractFacebookIcon(id);
                                Log.d("name: ", name);
                                Log.d("id: ", id);
                                Log.d("email: ", email);
                                Log.d("link: ", link);
                                Log.d("imageURL: ", imageURL.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString(getString(R.string.fields), getString(R.string.fields_name));
                request.setParameters(parameters);
                request.executeAsync();
                String MANH;
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
    }

    public URL extractFacebookIcon(String id) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageURL = new URL("http://graph.facebook.com/" + id
                    + "/picture?type=large");
            return imageURL;
        } catch (Throwable e) {
            return null;
        }
    }
}

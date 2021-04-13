package company.com.allder1.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import company.com.allder1.R;
import company.com.allder1.utils.ParseContent;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class ChooseloginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnconsumer_login, btnproviderlogin;
    public static String species;
    Activity activity;
    private ParseContent pcontent;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooselogin);
        Mapped();
        pcontent = new ParseContent(this);
        btnconsumer_login.setOnClickListener(this);
        btnproviderlogin.setOnClickListener(this);
    }

    private void Mapped() {
        btnconsumer_login = findViewById(R.id.consumerlogin);
        btnproviderlogin = findViewById(R.id.providerloin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.consumerlogin:
                Intent intent = new Intent(ChooseloginActivity.this, LoginActivity.class);
                new PreferenceHelper(ChooseloginActivity.this).putSpecies("consumer");
               // pcontent.checkSpecies(species);
                startActivity(intent);
                CustomIntent.customType(ChooseloginActivity.this, "fadein-to-fadeout");
                break;
            case R.id.providerloin:
              //  pcontent.checkSpecies("provider");
                new PreferenceHelper(ChooseloginActivity.this).putSpecies("provider");
                intent = new Intent(ChooseloginActivity.this, LoginActivity.class);
                startActivity(intent);
                CustomIntent.customType(this, "fadein-to-fadeout");
                break;
        }
    }

}



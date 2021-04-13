package company.com.allder1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import company.com.allder1.R;
import company.com.allder1.model.DetailOrder;

public class zoomqr extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomqr);
        Intent intent = getIntent();
        String qrcode = intent.getStringExtra("qrcode");
        Log.d("vvvvvv", "onCreate: " + qrcode);
        imageView =findViewById(R.id.imageqr);
        if (qrcode.isEmpty()) {

        } else {
            String base64 = qrcode;
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(this).load(imageByteArray).into(imageView);
        }
    }
}

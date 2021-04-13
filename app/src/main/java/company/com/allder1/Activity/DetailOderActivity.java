package company.com.allder1.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import company.com.allder1.Adapter.DetailOrderAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.Vollyheaders;
import company.com.allder1.model.DetailOrder;
import company.com.allder1.model.DetailOrderFood;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;

public class DetailOderActivity extends AppCompatActivity implements AsyncTaskCompleteListener {
    int totalmoney, idfood;
    RecyclerView recyclerView;
    DetailOrderAdapter adapter;
    TextView tv_Chopsticks,tv_spoon,tv_bowl,tv_folk;
    ImageView imgqr_code;
    TextView txtname, txtmonery, txtnotes, orderid;
    String currency, note;
    private Toolbar toolbarorder;
    String name, Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_oder);
        ToolbarOderHistory();
        final Intent intent = getIntent();
        totalmoney = intent.getIntExtra("totalmoney", 0);
        idfood = intent.getIntExtra("idfood", 0);
        Status = intent.getStringExtra("is_approved");
        name = intent.getStringExtra("name");
        note = intent.getStringExtra("note");
        Mapped();
        if (new PreferenceHelper(this).getSpecies().contains("provider")) {
            GetDetailOderApproveProvider();
        } else {
            GetDetailOderApproveConsumer();
        }
        txtname.setText(name);
        if (note.isEmpty()) {
            txtnotes.setText("No notes");
        } else {
            txtnotes.setText(note + "");
        }

    }

    private void GetDetailOderApproveConsumer() {
        String url = Const.ServiceType.NOT_APPROVE_ORDER_USER + "id=" + new PreferenceHelper(this).getUserId() + "&&order_id=" + String.valueOf(idfood);
        Log.d("url1", "GetOderApprove: " + url);
        new Vollyheaders(url, new PreferenceHelper(this).getSessionToken(), Const.ServiceCode.DETAIL_ORDER, this);
    }

    private void Mapped() {
        txtname = findViewById(R.id.txtname);
        imgqr_code = findViewById(R.id.imgqr_code);
        txtmonery = findViewById(R.id.txtmonery);
        recyclerView = findViewById(R.id.recyclerview);
        txtnotes = findViewById(R.id.txtnotes);
        orderid = findViewById(R.id.txtorderid);
       // tv_Chopsticks,tv_spoon,tv_bowl,tv_folk;
        tv_Chopsticks = findViewById(R.id.tv_Chopsticks);
        tv_spoon = findViewById(R.id.tv_spoon);
        tv_bowl = findViewById(R.id.tv_bowl);
        tv_folk = findViewById(R.id.tv_folk);

    }

    private void GetDetailOderApproveProvider() {
        String url = Const.ServiceType.NOT_APPROVE_ORDER + "id=" + new PreferenceHelper(this).getUserId() + "&order_id=" + idfood;
        Log.d("url1", "GetOderApprove: " + url);
        new Vollyheaders(url, new PreferenceHelper(this).getSessionToken(), Const.ServiceCode.DETAIL_ORDER, this);
    }

    private void ToolbarOderHistory() {
        toolbarorder = findViewById(R.id.toolbardetailorder);
        setSupportActionBar(toolbarorder);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customViewh = inflater.inflate(R.layout.toobarorderhistory, null);
        actionBar.setCustomView(customViewh);
        toolbarorder = (Toolbar) customViewh.getParent();
        toolbarorder.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbarorder.setContentInsetsAbsolute(0, 0);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.DETAIL_ORDER:
                try {
                    Log.d("123456", "onTaskCompleted: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    currency = jsonObject.getString("currency");
                    txtmonery.setText("Total " + totalmoney + currency);
                    Log.d("ccccc", "onTaskCompleted: " + currency);
                    if (jsonObject.getString("success").equals("true")) {
                        //chi tiết  order
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        // String response2 = jsonObject.getString("data");
                        final ArrayList<DetailOrder> DetailOrder;

                        TypeToken<List<DetailOrder>> token = new TypeToken<List<DetailOrder>>() {
                        };
                        DetailOrder = new Gson().fromJson(response1.toString(), token.getType());
                        // TextView tv_Chopsticks,tv_spoon,tv_bowl,tv_folk;
                        tv_Chopsticks.setText(DetailOrder.get(0).getChopsticks()+"");
                        tv_spoon.setText(DetailOrder.get(0).getSpoon()+"");
                        tv_bowl.setText(DetailOrder.get(0).getBowl()+"");
                        tv_folk.setText(DetailOrder.get(0).getFork()+"");
                        orderid.setText(DetailOrder.get(0).getOrder_number());
                        if (DetailOrder.get(0).getQr_code().isEmpty()) {

                        } else {
                            String base64 = DetailOrder.get(0).getQr_code();
                            String base64Image = base64.split(",")[1];
                            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
                            Glide.with(this).load(imageByteArray).into(imgqr_code);
                        }
                        imgqr_code.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(DetailOderActivity.this, zoomqr.class);
                                intent1.putExtra("qrcode", DetailOrder.get(0).getQr_code());
                                startActivity(intent1);
                            }
                        });
                        ArrayList<DetailOrderFood> DetailOrderfood;
                        DetailOrderfood = (ArrayList<DetailOrderFood>) DetailOrder.get(0).getOrderDetail();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new DetailOrderAdapter(DetailOderActivity.this, DetailOrderfood, currency);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}

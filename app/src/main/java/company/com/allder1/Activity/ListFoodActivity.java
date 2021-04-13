package company.com.allder1.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import company.com.allder1.Adapter.ListFoodAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.Listfood;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.ParseContent;
import maes.tech.intentanim.CustomIntent;

public class ListFoodActivity extends AppCompatActivity implements AsyncTaskCompleteListener {
    RecyclerView recyclerViewfood;
    String id_category;
    public static String currency;
    ListFoodAdapter adapter;
    private ParseContent pcontent;
    ArrayList<Listfood> listfoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_foofy);
        Intent intent = getIntent();
        id_category = intent.getStringExtra("id_categori");
        Mapped();
        pcontent = new ParseContent(this);
        getfood();

    }

    private void Mapped() {
        recyclerViewfood = findViewById(R.id.listfood);
    }

    private void getfood() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETFOODS + "cuisine_category_id=" + id_category + "&page=1&limit=10&sort=asc");
        Log.d("Manh", "list food category " + map.toString());
        new VollyRequester(ListFoodActivity.this, Const.GET, map, Const.ServiceCode.GETFOODS,
                this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GETFOODS:
                Log.d("Manh", "list food category" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        currency = jsonObject.getString("currency");
                        listfoods = pcontent.parseListfoods(response1);
                        recyclerViewfood.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                        recyclerViewfood.setLayoutManager(linearLayoutManager);
                        adapter = new ListFoodAdapter(ListFoodActivity.this, listfoods, currency);
                        recyclerViewfood.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}

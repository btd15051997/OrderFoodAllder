package company.com.allder1.Activity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
import company.com.allder1.utils.LocationHelper;
import company.com.allder1.utils.ParseContent;

public class SearchActivity extends AppCompatActivity implements AsyncTaskCompleteListener, LocationHelper.OnLocationReceived {
    ImageButton imgexitNavigation;
    Toolbar toolbarsearch;
    ImageButton imgEnterNavigation;
    CheckBox checkKm, CheckUSD, checkStar;
    RecyclerView recyclerViewfood;
    String currency;
    ListFoodAdapter adapter;
    private ParseContent pcontent;
    ArrayList<Listfood> listfoods = new ArrayList<>();
    SeekBar seekBarKM, seekBarDola;
    TextView txtKM, txtDola;
    int progressKM = 0;
    int progressDola = 0;
    Spinner spinner;
    int SpinerStar = 0;
    LocationHelper locHelper;
    Double  Kinhlatlong;
    Double  Vilatlong;


    String Query_filter = "";
    public static ImageButton imgfiltersearch;
    public static DrawerLayout drawerLayoutSearchActivity;
    private SearchView searchview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        anhxa();
         toolbar();
        locHelper = new LocationHelper(this);
        locHelper.setLocationReceivedLister(this);
        Eventnavigation();
        ActionSeekbarKM();
        ActionSeekbarDola();
        SpinerStar();
        FilterView();
        ActionCheckBox();
        //event click search
        searchview1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url =  Const.ServiceType.GETSTORENEARBY1+"name="+ query +"&rating=&price=&distance=";
                GetSearch(query,url);
                Query_filter = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // event click to navigationview
        pcontent = new ParseContent(this);
    }
    @Override
    public void onLocationReceived(LatLng latlong) {
    Kinhlatlong = latlong.longitude;
    Vilatlong = latlong.latitude;
        Log.d("onLocationReceived", "Vi " + Kinhlatlong);// vĩ
        Log.d("onLocationReceived", "Kinh " + Vilatlong);//kinh
    }

    @Override
    public void onLocationReceived(Location location) {
        Log.d("location", "onLocationReceived: " + location.getLongitude());
    }

    @Override
    public void onConntected(Bundle bundle) {

    }

    @Override
    public void onConntected(Location location) {

    }

    private void toolbar() {
        toolbarsearch = findViewById(R.id.toolbarsearch);
        setSupportActionBar(toolbarsearch);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void ActionCheckBox() {
        checkKm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                } else {

                }
            }
        });
        checkStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                } else {

                }
            }
        });
        CheckUSD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                } else {

                }
            }
        });

    }

    public static void FilterView() {
        imgfiltersearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayoutSearchActivity.openDrawer(GravityCompat.START);
            }
        });

    }

    private void SpinerStar() {
        Integer[] soluong = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(getApplicationContext()
                , android.R.layout.simple_spinner_dropdown_item
                , soluong);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // gọi hàm này khi có một sự kiện chọn item nào đó.
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinerStar = (int) spinner.getSelectedItem();
            }
            //gọi hàm này khi click vào Spinner mà không chọn item nào cả
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void ActionSeekbarDola() {
        txtDola.setText("Selected: " + seekBarDola.getProgress() + "/" + seekBarDola.getMax() + "$");
        seekBarDola.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progressDola = progressValue;
                txtDola.setText("Selected: " + progressValue + "/" + seekBarDola.getMax() + "$");
                Log.d("Seekbar", String.valueOf(progressValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtDola.setText("Selected: " + progressDola + "/" + seekBarDola.getMax() + "$");
            }
        });

    }

    private void ActionSeekbarKM() {
        txtKM.setText("Selected: " + seekBarKM.getProgress() + "/" + seekBarKM.getMax() + ":Km");
        seekBarKM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progressKM = progressValue;
                txtKM.setText("Selected: " + progressValue + "/" + seekBarKM.getMax() + ":Km");
                Log.d("Seekbar", String.valueOf(progressValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtKM.setText("Selected: " + progressKM + "/" + seekBarKM.getMax() + ":Km");
            }
        });

    }


    private void Eventnavigation() {
        imgexitNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayoutSearchActivity != null) {
                    drawerLayoutSearchActivity.closeDrawers();
                }
            }
        });
              imgEnterNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String ZeroDola = "000";
//                String ZeroKM = "000000";

//              Toast.makeText(SearchActivity.this, TotalCheckKm + "/" + TotalCheckDola + "/" + TotalCheck, Toast.LENGTH_SHORT).show();
                if (Query_filter != null || Query_filter == null) {
                    if (checkKm.isChecked()) {
                        int TotalCheckKm = 0;
                        TotalCheckKm += progressKM;
                        String Km_Search = "0-" + TotalCheckKm ;
                        String URLKM= Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter+"&rating="+"&price="+"&distance="+Km_Search+"@"+ Vilatlong+"-"+Kinhlatlong;
//                        Toast.makeText(SearchActivity.this, URLKM + "", Toast.LENGTH_SHORT).show();
                        GetSearch(Query_filter,URLKM);
                        Log.d("LLLL1",URLKM);
                    }
                    if (checkStar.isChecked()) {
                        int TotalCheckKm = 0, TotalCheckDola = 0, TotalCheck = 0;
                        TotalCheck += SpinerStar;
                        String URLStar = Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter + "&rating=" + TotalCheck+ "&price="+"&distance=";
//                        Toast.makeText(SearchActivity.this, URLStar + "", Toast.LENGTH_SHORT).show();
                        GetSearch(Query_filter,URLStar);
                        Log.d("LLLL2",URLStar);
                    }
                    if (CheckUSD.isChecked()) {
                        int TotalCheckDola = 0;
                        TotalCheckDola += progressDola;
                        String Dola_Search = "0-" + TotalCheckDola;
                        String URLDOla=  Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter + "&rating="+"&price="+Dola_Search+"&distance=";
//                        Toast.makeText(SearchActivity.this, URLDOla + "", Toast.LENGTH_SHORT).show();
                        GetSearch(Query_filter,URLDOla);
                        Log.d("LLLL3",URLDOla);
                    }

                    if(checkStar.isChecked()&&checkKm.isChecked()){
                        int TotalCheckKm = 0, TotalCheck = 0;
                        TotalCheck += SpinerStar;
                        TotalCheckKm += progressKM;
                        String Km_Search = "0-" + TotalCheckKm;
                        String URLStarKm = Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter + "&rating=" + TotalCheck+ "&price="+"&distance="+Km_Search+"@"+ Vilatlong+"-"+Kinhlatlong;
//                        Toast.makeText(SearchActivity.this, URLStarKm + "", Toast.LENGTH_SHORT).show();
                        GetSearch(Query_filter,URLStarKm);
                        Log.d("LLL4",URLStarKm);

                    }
                    if(checkStar.isChecked()&&CheckUSD.isChecked()){
                        int  TotalCheckDola = 0, TotalCheck = 0;
                        TotalCheck += SpinerStar;
                        TotalCheckDola += progressDola;
                        String Dola_Search = "0-" + TotalCheckDola  ;
                        String URLStarUSD = Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter + "&rating=" + TotalCheck+ "&price="+Dola_Search+"&distance=";
//                        Toast.makeText(SearchActivity.this, URLStarUSD + "", Toast.LENGTH_SHORT).show();
                        GetSearch(Query_filter,URLStarUSD);
                        Log.d("LLL5",URLStarUSD);

                    }
                    if(CheckUSD.isChecked()&&checkKm.isChecked()){
                        int TotalCheckKm = 0, TotalCheckDola = 0, TotalCheck = 0;
                        TotalCheckDola += progressDola;
                        TotalCheckKm += progressKM;
                        String Km_Search = "0-" + TotalCheckKm ;
                        String Dola_Search = "0-" + TotalCheckDola  ;
                        String URLKMUSD = Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter + "&rating="+"&price="+Dola_Search+"&distance="+Km_Search+"@"+ Vilatlong+"-"+Kinhlatlong;
//                        Toast.makeText(SearchActivity.this, URLKMUSD + "", Toast.LENGTH_SHORT).show();
                        GetSearch(Query_filter,URLKMUSD);
                        Log.d("LLL6",URLKMUSD);


                    }
                    if(checkKm.isChecked()&&checkStar.isChecked()&&CheckUSD.isChecked()){
                        int TotalCheckKm = 0, TotalCheckDola = 0, TotalCheck = 0;
                     String Star_Search = "" + TotalCheck;
                     String Dola_Search = "0-" + TotalCheckDola ;
                     String Km_Search = "0-" + TotalCheckKm ;
                     Log.d("BBBB11",Query_filter);
                     String Queryfilter = Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter + "&rating=" + Star_Search+ "&price=" + Dola_Search + "&distance=" + Km_Search+"@"+ Vilatlong+"-"+Kinhlatlong;
//                     Toast.makeText(SearchActivity.this, Queryfilter + "", Toast.LENGTH_SHORT).show();
                     GetSearch(Query_filter,Queryfilter);
                     Log.d("DDD1",Queryfilter);}
                }
//                if(Query_filter == null){
//                    if(checkKm.isChecked()&&checkStar.isChecked()&&CheckUSD.isChecked()){
//                        String Star_Search = "" + TotalCheck;
//                        String Dola_Search = "0-" + TotalCheckDola ;
//                        String Km_Search = "0-" + TotalCheckKm ;
//                        Log.d("BBBB11",Query_filter);
//                        String Queryfilter = Const.ServiceType.GETSTORENEARBY1+"name="+Query_filter + "&rating=" + Star_Search+ "&price=" + Dola_Search+ZeroDola + "&distance=" + Km_Search+ZeroKM+"@10.8538493-106.6261721";
//                        Toast.makeText(SearchActivity.this, Queryfilter + "", Toast.LENGTH_SHORT).show();
//                        GetSearch(Query_filter,Queryfilter);
//                        Log.d("DDD2",Queryfilter);}
//
////                    String Star_Search = "" + TotalCheck;
////                    String Dola_Search = "0-" + TotalCheckDola ;
////                    String Km_Search = "0-" + TotalCheckKm ;
//////                    Log.d("BBBB",Query_filterNULL);
////                    String Queryfilter =Const.ServiceType.GETSTORENEARBY1+ "name="+"&rating=" + Star_Search+ "&price=" + Dola_Search+ZeroDola + "&distance=" + Km_Search+ZeroKM+"@10.8538493-106.6261721";
////                    Toast.makeText(SearchActivity.this, Queryfilter + "", Toast.LENGTH_SHORT).show();
////                    GetSearch(Query_filter,Queryfilter);
////                    Log.d("FFFF",Queryfilter);
//                }
                // thoát navigation
                if (drawerLayoutSearchActivity != null) {
                    drawerLayoutSearchActivity.closeDrawers();
                    checkKm.setChecked(false);
                    CheckUSD.setChecked(false);
                    checkStar.setChecked(false);
                }
            }
        });

    }
    private void anhxa() {
        recyclerViewfood = findViewById(R.id.recyclerviewsearch);
        //toolbar();
        drawerLayoutSearchActivity = findViewById(R.id.drawerLayoutSearchActivity);
        // searchview
        searchview1 = findViewById(R.id.searchview1);
        imgfiltersearch = findViewById(R.id.imgfiltersearch);
        imgexitNavigation = findViewById(R.id.extnavigation);
        imgEnterNavigation = findViewById(R.id.enternavigation);
        //SeekbarKm
        seekBarKM = findViewById(R.id.seekBarKm);
        txtKM = findViewById(R.id.txtKm);
        //SeekbarDola
        seekBarDola = findViewById(R.id.seekBarDola);
        txtDola = findViewById(R.id.txtDola);
        //Spiner
        spinner = findViewById(R.id.sniper);
        //checkbox
        checkKm = findViewById(R.id.checkBoxkm);
        CheckUSD = findViewById(R.id.checkBoxDola);
        checkStar = findViewById(R.id.checkBoxSpiner);

    }

    private void GetSearch(String txt_search,String url) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, url);
        Log.d("AAAA", "get: search food" + map.toString());
        new VollyRequester(SearchActivity.this, Const.GET, map, 5555,
                this);
    }
//    private void GetSearchFilter(String txt_searchFilter) {
//        HashMap<String, String> map2 = new HashMap<>();
//        map2.put(Const.Params.URL,Const.ServiceType.GETSTORENEARBY1+txt_searchFilter+"@10.8538493-106.6261721");
//        Log.d("AAAA", "get: search food" + map2.toString());
//        new VollyRequester(SearchActivity.this, Const.GET,map2, 9999,
//                this);
//    }
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        if(response == null){
//            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }

        Log.d("Manh", "response: search food111 " + response);
        switch (serviceCode) {
//            case 9999:
//                Log.d("Manh", "response: search food " + response);
//                Log.d("Manhfoood", "response: search food " + serviceCode);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("success").equals("true")) {
//                        JSONArray response1 = jsonObject.getJSONArray("data");
//                        currency = jsonObject.getString("currency");
//                        listfoods = pcontent.parseListfoods(response1);
//                        recyclerViewfood.setHasFixedSize(true);
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                        recyclerViewfood.setLayoutManager(linearLayoutManager);
//                        adapter = new ListFoodAdapter(SearchActivity.this, listfoods, currency);
//                        recyclerViewfood.setAdapter(adapter);
//                    } else {
//                        Toast.makeText(this, "No dishes", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            break;
            case 5555:
                Log.d("Manh5555", "response: search food " + response);
                Log.d("Manhfoood5555", "response: search food " + serviceCode);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        currency = jsonObject.getString("currency");
                        listfoods = pcontent.parseListfoods(response1);
                        recyclerViewfood.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                        recyclerViewfood.setLayoutManager(linearLayoutManager);
                        adapter = new ListFoodAdapter(SearchActivity.this, listfoods, currency);
                        recyclerViewfood.setAdapter(adapter);
                    } else {
//                        Toast.makeText(this, "No dishes", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


}

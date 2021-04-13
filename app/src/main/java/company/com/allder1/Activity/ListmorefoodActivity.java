package company.com.allder1.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.futuremind.recyclerviewfastscroll.RecyclerViewScrollListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import company.com.allder1.Adapter.NearbyAdapter;
import company.com.allder1.Adapter.RecommendationAdapter;
import company.com.allder1.Adapter.RecommendationfoodAdapter;
import company.com.allder1.R;
import company.com.allder1.fragment.FoodFragment;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.Nearbystore;
import company.com.allder1.model.RecommendationFood;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.LocationHelper;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

public class ListmorefoodActivity extends AppCompatActivity implements LocationHelper.OnLocationReceived, AsyncTaskCompleteListener {
    ArrayList<Nearbystore> Nearbystore = null;
    NearbyAdapter NearbyAdapter;
    String species;
    TextView txttitle;
    Handler handler;
    ProgressBar ProgressBar;
    ArrayList<RecommendationFood> RecommendationFood = null;
    RecommendationAdapter RecommendationfoodAdapter;
    RecyclerView recyclerviewmore;
    boolean isLoading = false;
    View view;
    private LocationHelper locHelper;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmore);
        locHelper = new LocationHelper(this);
        locHelper.setLocationReceivedLister(this);
        Mapped();
        Intent intent = getIntent();
        species = intent.getStringExtra("species");
        if (species.equals("moreRecommendation")) {
            getrecommended();
        }
//        if (species.equals("morenearby")) {
//            if (FoodFragment.stringNearbystore != null) {
//                txttitle.setText("List nearby");
//                try {
//                    JSONObject jsonObject = new JSONObject(FoodFragment.stringNearbystore);
//                    if (jsonObject.getBoolean("success")) {
//                        JSONArray response1 = jsonObject.getJSONArray("data");
//                        TypeToken<List<Nearbystore>> token = new TypeToken<List<Nearbystore>>() {
//                        };
//                        Nearbystore = new Gson().fromJson(response1.toString(), token.getType());
//                        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                        recyclerviewnearystore.setLayoutManager(linearLayoutManager);
//                        NearbyAdapter = new NearbyAdapter(this, Nearbystore, 4);
//                        recyclerviewnearystore.setAdapter(NearbyAdapter);
//                        recyclerviewnearystore.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                super.onScrollStateChanged(recyclerView, newState);
//                            }
//
//                            @Override
//                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                super.onScrolled(recyclerView, dx, dy);
//                                // linearLayoutManager.getChildCount()
//                                if ((linearLayoutManager.getChildCount() + linearLayoutManager.findFirstVisibleItemPosition()) >= linearLayoutManager.getItemCount() && isLoading == false) {
//                                    Log.d("cccccccccc11", "onScrolled: ");
//                                    isLoading = true;
//                                    ProgressBar.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else if (species.equals("moreRecommendation")) {
//            txttitle.setText("List recommendation");
//            if (FoodFragment.stringRecommendationFood != null) {
//                try {
//                    JSONObject jsonObject = new JSONObject(FoodFragment.stringRecommendationFood);
//                    Log.d("11111", "onTaskCompleted: " + FoodFragment.stringRecommendationFood);
//                    if (jsonObject.getBoolean("success")) {
//                        // dbfoodhome.putlistrecommended(response);
//                        JSONArray response1 = jsonObject.getJSONArray("data");
//                        TypeToken<List<RecommendationFood>> token = new TypeToken<List<RecommendationFood>>() {
//                        };
//                        RecommendationFood = new Gson().fromJson(response1.toString(), token.getType());
//                        recyclerviewnearystore.setHasFixedSize(true);
//                        GridLayoutManager gridVertical = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
//                        recyclerviewnearystore.setLayoutManager(gridVertical);
//                        RecommendationfoodAdapter = new RecommendationAdapter(this, RecommendationFood);
//                        recyclerviewnearystore.setAdapter(RecommendationfoodAdapter);
//                        //  Log.d(TAG, ":GETSTORENEARBY "+Nearbystore.get(0).getNameStore());
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    private void Mapped() {
        recyclerviewmore = findViewById(R.id.recyclerviewmore);
        txttitle = findViewById(R.id.txttitle);
        ProgressBar = findViewById(R.id.ProgressBar);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_load_more, null);
    }

    private void getrecommended() {
        txttitle.setText("List recommendation");
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETSTORENEARBY + "type=recommended");
        Log.d("Manh", "recommended: " + map.toString());
        new VollyRequester(this, Const.GET, map, Const.ServiceCode.GETSRECOMMENDED,
                this);
    }

    @Override
    public void onLocationReceived(LatLng latlong) {

    }

    @Override
    public void onLocationReceived(Location location) {

    }

    @Override
    public void onConntected(Bundle bundle) {

    }

    @Override
    public void onConntected(Location location) {
        Log.d("Manh", "location more:" + location);
        if (species.equals("morenearby"))
            getstorenearby(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GETSTORENEARBY:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        TypeToken<List<Nearbystore>> token = new TypeToken<List<Nearbystore>>() {
                        };
                        Nearbystore = new Gson().fromJson(response1.toString(), token.getType());
                        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                        recyclerviewmore.setLayoutManager(linearLayoutManager);
                        NearbyAdapter = new NearbyAdapter(this, Nearbystore, Nearbystore.size());
                        recyclerviewmore.setAdapter(NearbyAdapter);
                        recyclerviewmore.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                // linearLayoutManager.getChildCount()
                                if ((linearLayoutManager.getChildCount() + linearLayoutManager.findFirstVisibleItemPosition()) >= linearLayoutManager.getItemCount() && isLoading == false) {
                                    Log.d("cccccccccc11", "onScrolled: ");
                                    isLoading = true;
                                    ProgressBar.setVisibility(View.VISIBLE);
                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isLoading = false;
                                            ProgressBar.setVisibility(View.GONE);
                                        }
                                    }, 5000);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.GETSRECOMMENDED:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("11111", "onTaskCompleted: " + response);
                    if (jsonObject.getBoolean("success")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");

                        TypeToken<List<RecommendationFood>> token = new TypeToken<List<RecommendationFood>>() {
                        };
                        RecommendationFood = new Gson().fromJson(response1.toString(), token.getType());
                        final GridLayoutManager gridVertical = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
                        recyclerviewmore.setLayoutManager(gridVertical);
                        RecommendationfoodAdapter = new RecommendationAdapter(this, RecommendationFood);
                        recyclerviewmore.setAdapter(RecommendationfoodAdapter);
                        recyclerviewmore.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                // linearLayoutManager.getChildCount()
                                if ((gridVertical.getChildCount() + gridVertical.findFirstVisibleItemPosition()) >= gridVertical.getItemCount() && isLoading == false) {
                                    // Log.d("cccccccccc11", "onScrolled: ");
                                    isLoading = true;
                                    ProgressBar.setVisibility(View.VISIBLE);
                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isLoading = false;
                                            ProgressBar.setVisibility(View.GONE);
                                        }
                                    }, 5000);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//       switch (msg.what){
//           case  0:
//               recyclerviewnearystore.add
//       }
        }
    }

    private void getstorenearby(double Lat, double Long) {
        txttitle.setText("List nearby");
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETSTORENEARBY + "type=nearby&latitude=" + Lat + "&longitude=" + Long);
        Log.d("maih", "getcategory: " + map.toString());
        new VollyRequester(this, Const.GET, map, Const.ServiceCode.GETSTORENEARBY,
                this);
    }
}

package company.com.allder1.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import company.com.allder1.Activity.ListmorefoodActivity;
import company.com.allder1.Adapter.AdapterCategory;
import company.com.allder1.Adapter.NearbyAdapter;
import company.com.allder1.Adapter.RecommendationfoodAdapter;
import company.com.allder1.R;
import company.com.allder1.database.Database;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.Category;
import company.com.allder1.model.Nearbystore;
import company.com.allder1.model.RecommendationFood;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.LocationHelper;
import company.com.allder1.utils.PreferenceHelper;


public class FoodFragment extends Fragment implements LocationHelper.OnLocationReceived, AsyncTaskCompleteListener {
    RecyclerView recycategory, recyclerviewnearystore, recyclerviewRecommendationstore;
    AdapterCategory adapterCategory;
    NearbyAdapter NearbyAdapter;
    private LocationHelper locHelper;
    private PreferenceHelper preferenceHelper;
    TextView morenearby, moreRecommendation;
    RecommendationfoodAdapter RecommendationfoodAdapter;
    String TAG = "FoodFragment";
    double Lat, Long;
    public static String stringNearbystore;
    public static String stringRecommendationFood;
    Handler handler;
    //  CircleIndicator2 indicator;

    Database dbfoodhome;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mPermissionCheck = true;
    ArrayList<Nearbystore> Nearbystore = null;
    ArrayList<Category> listcategories = new ArrayList<>();
    ArrayList<RecommendationFood> RecommendationFood = null;

    private void getcategory() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETCATEGORY + "sort=" + "asc");
        Log.d("Manh", "getcategory:" + map.toString());
        new VollyRequester(getActivity(), Const.GET, map, Const.ServiceCode.GETCATEGORY,
                this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Mapped(view);
        dbfoodhome = new Database(getContext());
        // dbfoodhome();
        getcategory();
        getrecommended();
        morenearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ListmorefoodActivity.class);
                intent1.putExtra("species", "morenearby");
                startActivity(intent1);
            }
        });
        moreRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ListmorefoodActivity.class);
                intent1.putExtra("species", "moreRecommendation");
                startActivity(intent1);
            }
        });

        return view;
    }

    private void dbfoodhome() {
        String dbcategory = null;
        dbcategory = new Database(getContext()).getListcategory();
        String recommended = null;
        recommended = new Database(getContext()).getlistrecommended();
        String listnearbystore = null;
        listnearbystore = new Database(getContext()).getnearbystore();
        try {
            JSONObject jsonObject = new JSONObject(dbcategory);
            if (jsonObject.getString("success").equals("true")) {
                JSONArray categoryArray = jsonObject.getJSONArray("data");
                if (categoryArray.length() > 0) {
                    for (int i = 0; i < categoryArray.length(); i++) {
                        JSONObject obj = categoryArray.getJSONObject(i);
                        Category category = new Category();
                        category.setId(obj.getString("id"));
                        category.setName(obj.getString("name"));
                        category.setPicture(obj.getString("picture"));
                        category.setCreatedAt(obj.getString("created_at"));
                        category.setUpdatedAt(obj.getString("updated_at"));
                        listcategories.add(category);
                        Category();
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(listnearbystore);
            if (jsonObject.getBoolean("success")) {
                JSONArray response1 = jsonObject.getJSONArray("data");
                TypeToken<List<Nearbystore>> token = new TypeToken<List<Nearbystore>>() {
                };
                Nearbystore = new Gson().fromJson(response1.toString(), token.getType());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerviewnearystore.setLayoutManager(linearLayoutManager);
                NearbyAdapter = new NearbyAdapter(getActivity(), Nearbystore, 1);
                recyclerviewnearystore.setAdapter(NearbyAdapter);
                Log.d(TAG, ":GETSTORENEARBY " + Nearbystore.get(0).getNameStore());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(recommended);
            Log.d("Manh", "onTaskCompleted: " + recommended);
            if (jsonObject.getBoolean("success")) {
                JSONArray response1 = jsonObject.getJSONArray("data");
                TypeToken<List<RecommendationFood>> token = new TypeToken<List<RecommendationFood>>() {
                };
                RecommendationFood = new Gson().fromJson(response1.toString(), token.getType());
                recyclerviewRecommendationstore.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerviewRecommendationstore.setLayoutManager(linearLayoutManager);
                RecommendationfoodAdapter = new RecommendationfoodAdapter(getActivity(), RecommendationFood, 6);
                recyclerviewRecommendationstore.setAdapter(RecommendationfoodAdapter);
                Log.d(TAG, ":GETSTORENEARBY " + Nearbystore.get(0).getNameStore());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locHelper = new LocationHelper(getActivity());
        locHelper.setLocationReceivedLister(this);
    }

    private void getrecommended() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETSTORENEARBY + "type=recommended");
        Log.d("Manh", "recommended: " + map.toString());
        new VollyRequester(getActivity(), Const.GET, map, Const.ServiceCode.GETSRECOMMENDED,
                this);
    }


    private void Category() {
        recycategory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycategory.setLayoutManager(linearLayoutManager);
        adapterCategory = new AdapterCategory(listcategories, getActivity());
        recycategory.setAdapter(adapterCategory);
//        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(recycategory);
//        indicator.attachToRecyclerView(recycategory,pagerSnapHelper);
//// optional
//        adapterCategory.registerAdapterDataObserver(indicator.getAdapterDataObserver());

    }

    private void Mapped(View view) {
        //   indicator = (CircleIndicator2) view.findViewById(R.id.indicator);
        recycategory = view.findViewById(R.id.recycategory);
        morenearby = view.findViewById(R.id.morenearby);
        moreRecommendation = view.findViewById(R.id.moreRecommendation);
        recyclerviewnearystore = view.findViewById(R.id.recyclerviewnearystore);
        recyclerviewRecommendationstore = view.findViewById(R.id.recyclerviewRecommendationstore);
    }


    @Override
    public void onTaskCompleted(final String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GETCATEGORY:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        recycategory.setVisibility(View.VISIBLE);
                        dbfoodhome.putListcategory(response);
                        JSONArray categoryArray = jsonObject.getJSONArray("data");
                        if (categoryArray.length() > 0) {
                            for (int i = 0; i < categoryArray.length(); i++) {
                                JSONObject obj = categoryArray.getJSONObject(i);
                                Category category = new Category();
                                category.setId(obj.getString("id"));
                                category.setName(obj.getString("name"));
                                category.setPicture(obj.getString("picture"));
                                category.setCreatedAt(obj.getString("created_at"));
                                category.setUpdatedAt(obj.getString("updated_at"));
                                listcategories.add(category);
                                Category();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onTaskCompleted: " + response);
                break;
            case Const.ServiceCode.GETSTORENEARBY:
                stringNearbystore = response;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        dbfoodhome.putnearbystore(response);
                        TypeToken<List<Nearbystore>> token = new TypeToken<List<Nearbystore>>() {
                        };
                        Nearbystore = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerviewnearystore.setLayoutManager(linearLayoutManager);
                        NearbyAdapter = new NearbyAdapter(getActivity(), Nearbystore, 3);
                        recyclerviewnearystore.setAdapter(NearbyAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.GETSRECOMMENDED:
                stringRecommendationFood = response;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("11111", "onTaskCompleted: " + response);
                    if (jsonObject.getBoolean("success")) {
                        dbfoodhome.putlistrecommended(response);
                        JSONArray response1 = jsonObject.getJSONArray("data");

                        TypeToken<List<RecommendationFood>> token = new TypeToken<List<RecommendationFood>>() {
                        };
                        RecommendationFood = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerviewRecommendationstore.setLayoutManager(linearLayoutManager);
                        RecommendationfoodAdapter = new RecommendationfoodAdapter(getActivity(), RecommendationFood, 6);
                        recyclerviewRecommendationstore.setAdapter(RecommendationfoodAdapter);
                        //  Log.d(TAG, ":GETSTORENEARBY "+Nearbystore.get(0).getNameStore());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public void onLocationReceived(LatLng latlong) {
        Log.d("location123", "onLocationReceived: " + latlong);

    }

    @Override
    public void onLocationReceived(Location location) {
        Log.d("location123", "onLocationReceived: " + location.getLongitude());
        Lat = location.getLatitude();
        Long = location.getLongitude();
        preferenceHelper = new PreferenceHelper(getContext());
        getstorenearby(Lat, Long);
    }

    private void getstorenearby(double Lat, double Long) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETSTORENEARBY + "type=nearby&latitude=" + Lat + "&longitude=" + Long);
        Log.d("maih", "getcategory: " + map.toString());
        new VollyRequester(getActivity(), Const.GET, map, Const.ServiceCode.GETSTORENEARBY,
                this);
    }

    @Override
    public void onConntected(Bundle bundle) {

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onConntected(Location location) {
        Lat = location.getLatitude();
        Long = location.getLongitude();
        getstorenearby(Lat, Long);
        Log.d(TAG, "onConntected: ");

    }

}

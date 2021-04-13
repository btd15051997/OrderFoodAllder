package company.com.allder1.fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import company.com.allder1.Activity.FoodStoreActivity;
import company.com.allder1.Adapter.AdapterOrder;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.Vollyheaders;
import company.com.allder1.model.OrderNOApprove;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;

import static android.content.DialogInterface.BUTTON_POSITIVE;


public class ComingOrderProviderFragment extends Fragment implements AsyncTaskCompleteListener, AdapterOrder.ItemClickListener {
    RecyclerView recyclerView;
    AdapterOrder adapter;
    Handler handler;
    TextView txtdate, txtview;
    ImageView add_order_p;
    private Toolbar toolbar;
    public static String currency;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    SwipeRefreshLayout mWaveSwipeRefreshLayout;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming_order, container, false);
        Mapped(view);

        GetOderApprove();
        add_order_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodStoreActivity.class);
                intent.putExtra("id_provider", Integer.valueOf(new PreferenceHelper(getActivity()).getUserId()));
                startActivity(intent);
                CustomIntent.customType(getContext(), "fadein-to-fadeout");
            }
        });
        txtdate.setText("Show All");
        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                txtdate.setText(simpleDateFormat.format(calendar.getTime()));
                                Searchbydate(simpleDateFormat.format(calendar.getTime()));

                            }
                        }, year, month, day);
                dialog.show();
               // Context themeContext = getContext();
//                dialog.setButton(BUTTON_POSITIVE,
//                        themeContext.getText(R.string.datePicker_setButton), this);
            }
        });

        return view;
    }


    private void GetOderApprove() {
        String url = Const.ServiceType.NOT_APPROVE_ORDER + "id=" + new PreferenceHelper(getContext()).getUserId() + "&&status=pending";
        Log.d("aaaaaaa", url+"GetOderApprove: "+new PreferenceHelper(getContext()).getSessionToken());
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), Const.ServiceCode.NOT_APPROVE_ORDER, this);
    }

    private void loadorder() {
        String url = Const.ServiceType.NOT_APPROVE_ORDER + "id=" + new PreferenceHelper(getContext()).getUserId() + "&&status=pending";
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), 211, this);
    }

    private void Mapped(View v) {
        recyclerView = v.findViewById(R.id.order_no_tapprove);
//        refresh_layout = v.findViewById(R.id.refresh_layout);
        txtdate = v.findViewById(R.id.txtdate);
        add_order_p = v.findViewById(R.id.add_order_p);
        txtview = v.findViewById(R.id.txtview);
        mWaveSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetOderApprove();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("refresh_layout", "run: ");
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.NOT_APPROVE_ORDER:
                Log.d("cccccc", "onTaskCompleted: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        txtview.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        LayoutAnimationController controller = null;
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        currency = jsonObject.getString("currency");
                        ArrayList<OrderNOApprove> OrderNOApprove;
                        TypeToken<List<OrderNOApprove>> token = new TypeToken<List<OrderNOApprove>>() {
                        };
                        OrderNOApprove = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new AdapterOrder(getContext(), OrderNOApprove, this, 0);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "There is no order list", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 111:
                try {
                    Log.d("ccccc", "onTaskCompleted: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        txtview.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        LayoutAnimationController controller = null;
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        currency = jsonObject.getString("currency");
                        ArrayList<OrderNOApprove> OrderNOApprove;
                        TypeToken<List<OrderNOApprove>> token = new TypeToken<List<OrderNOApprove>>() {
                        };
                        OrderNOApprove = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new AdapterOrder(getContext(), OrderNOApprove, this, 0);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "There is no order list", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 211:
                Log.d("manh112", "onTaskCompleted: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        txtview.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        LayoutAnimationController controller = null;
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        currency = jsonObject.getString("currency");
                        ArrayList<OrderNOApprove> OrderNOApprove;
                        TypeToken<List<OrderNOApprove>> token = new TypeToken<List<OrderNOApprove>>() {
                        };
                        OrderNOApprove = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new AdapterOrder(getContext(), OrderNOApprove, this, 0);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.d("manh112222", "onTaskCompleted: " + response);
                        txtview.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onClick() {
        GetOderApprove();
    }


    private void Searchbydate(String date) {
        String url = Const.ServiceType.NOT_APPROVE_ORDER + "id=" + new PreferenceHelper(getContext()).getUserId() + "&&date=" + date + "&&status=pending";
        Log.d("url3", "Searchbydate: " + url);
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), 111, this);
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter("Comingorder");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
                filter);
        super.onStart();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String notification = intent.getStringExtra("notify");
            if (notification != null) {
                if (notification.contains("approved_declined_order")) {
                    loadorder();
                } else {
                    GetOderApprove();
                    Log.d("manh123", "onReceive: ");
                }
            }
        }
    };

}

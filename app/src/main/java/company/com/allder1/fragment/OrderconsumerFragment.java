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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
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

import company.com.allder1.Adapter.AdapterOrder;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.Vollyheaders;
import company.com.allder1.model.OrderNOApprove;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;

public class OrderconsumerFragment extends Fragment implements AsyncTaskCompleteListener, AdapterOrder.ItemClickListener {
    RecyclerView recyclerView;
    AdapterOrder adapter;
    public static String currency;
    Handler handler;
    TextView txtdate;
    SwipeRefreshLayout mWaveSwipeRefreshLayout;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        Mapped(view);
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
            }
        });
        GetOderApprove();
        return view;
    }

    private void Mapped(View v) {
        recyclerView = v.findViewById(R.id.order_no_tapprove);
        txtdate = v.findViewById(R.id.txtdate);
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
                        Log.d("Manh", "refresh layout");
                        GetOderApprove();
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void GetOderApprove() {
        String url = Const.ServiceType.NOT_APPROVE_ORDER_USER + "id=" + new PreferenceHelper(getActivity()).getUserId();
        Log.d("Manh", "Get: Oder consumer" + url);
        new Vollyheaders(url, new PreferenceHelper(getActivity()).getSessionToken(), Const.ServiceCode.NOT_APPROVE_ORDER, this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        Log.d("Manh", "Get: Oder consumer" + response);
        switch (serviceCode) {
            case Const.ServiceCode.NOT_APPROVE_ORDER:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        LayoutAnimationController controller = null;
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        currency = jsonObject.getString("currency");
                        ArrayList<OrderNOApprove> OrderNOApprove;
                        TypeToken<List<OrderNOApprove>> token = new TypeToken<List<OrderNOApprove>>() {
                        };
                        OrderNOApprove = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new AdapterOrder(getContext(), OrderNOApprove, this, 2);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "There is no order list" + txtdate.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 111:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        Log.d("Manh", "response: order consumer" + response);
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        currency = jsonObject.getString("currency");
                        ArrayList<OrderNOApprove> OrderNOApprove;
                        TypeToken<List<OrderNOApprove>> token = new TypeToken<List<OrderNOApprove>>() {
                        };
                        OrderNOApprove = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new AdapterOrder(getContext(), OrderNOApprove, this, 2);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "There is no order list on the day " + txtdate.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick() {

    }

    private void Searchbydate(String date) {
        String url = Const.ServiceType.NOT_APPROVE_ORDER_USER + "id=" + new PreferenceHelper(getContext()).getUserId() + "&&date=" + date;
        Log.d("Manh", "Search order by date:consumer " + url);
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), 111, this);
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter("Orderconsumer");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
                filter);
        super.onStart();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String notification = intent.getStringExtra("notify");
            if (notification != null) {
                GetOderApprove();
            }
        }
    };
}

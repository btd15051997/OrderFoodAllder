package company.com.allder1.fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class OrderProviderFragment extends Fragment implements AsyncTaskCompleteListener, AdapterOrder.ItemClickListener {
    RecyclerView recyclerVieworderapprove;
    AdapterOrder adapter;
    Handler handler;
    TextView txtdate;
    SwipeRefreshLayout refresh_layout;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        Mapped(view);
        GetOderApprove();
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
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void Mapped(View v) {
        recyclerVieworderapprove = v.findViewById(R.id.orderapprove);
        refresh_layout = v.findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        txtdate = v.findViewById(R.id.txtdate);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetOderApprove();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Manh", "refresh layout");
                        refresh_layout.setRefreshing(false);
                    }
                }, 3000);
                // new Task
                new Task().execute();
            }
        });
        new Task().doInBackground();
    }

    private void GetOderApprove() {
        String url = Const.ServiceType.NOT_APPROVE_ORDER + "id=" + new PreferenceHelper(getContext()).getUserId() + "&&status=approved";
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), Const.ServiceCode.NOT_APPROVE_ORDER, this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.NOT_APPROVE_ORDER:
                try {
                    Log.d("Manh", "list order approve: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        refresh_layout.setRefreshing(false);
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        ArrayList<OrderNOApprove> OrderNOApprove;
                        TypeToken<List<OrderNOApprove>> token = new TypeToken<List<OrderNOApprove>>() {
                        };
                        OrderNOApprove = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerVieworderapprove.setLayoutManager(linearLayoutManager);
                        adapter = new AdapterOrder(getContext(), OrderNOApprove, this, 1);
                        recyclerVieworderapprove.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 111:
                Log.d("Manh", "load order approve" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        refresh_layout.setRefreshing(false);
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        ArrayList<OrderNOApprove> OrderNOApprove;
                        TypeToken<List<OrderNOApprove>> token = new TypeToken<List<OrderNOApprove>>() {
                        };
                        OrderNOApprove = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerVieworderapprove.setLayoutManager(linearLayoutManager);
                        adapter = new AdapterOrder(getContext(), OrderNOApprove, this, 1);
                        recyclerVieworderapprove.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
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
        Fragment frg = null;
        frg = ((AppCompatActivity) getActivity()).getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG");
        final FragmentTransaction ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void Searchbydate(String date) {
        String url = Const.ServiceType.NOT_APPROVE_ORDER + "id=" + new PreferenceHelper(getContext()).getUserId() + "&date=" + date + "&status=approved";
        Log.d("Manh", "Search order by date: " + url);
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
                Log.d("Manh", "notification order approve");
                GetOderApprove();
            }
        }
    };

    private class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            refresh_layout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }
}

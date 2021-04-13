package company.com.allder1.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import company.com.allder1.Adapter.NotificationAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.Vollyheaders;
import company.com.allder1.model.Notification;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;


public class NotificationFragment extends Fragment implements AsyncTaskCompleteListener {
    RecyclerView Recyclernotification;
    String Species;
    NotificationAdapter adapter;
    TextView txtdate, txtname;
    ImageView image_avata;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        Mapped(view);

        txtname.setText(new PreferenceHelper(getContext()).getUser_name() + new PreferenceHelper(getContext()).getlastname());
        if (new PreferenceHelper(getContext()).getPicture().isEmpty()) {

        } else {
            String base64 = new PreferenceHelper(getContext()).getPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(getActivity()).load(imageByteArray).into(image_avata);
        }
        Species = new PreferenceHelper(getActivity()).getSpecies();
        if (Species.contains("provider")) {
            getnotificationprovider();
        } else {
            getnotification();
        }
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
                                if (Species.contains("provider")) {
                                    SearchbydateProvider(simpleDateFormat.format(calendar.getTime()));
                                } else {
                                    Searchbydate(simpleDateFormat.format(calendar.getTime()));
                                }

                            }
                        }, year, month, day);
                dialog.show();
            }
        });
        return view;
    }

    private void SearchbydateProvider(String date) {
        String url = Const.ServiceType.GETNOTIFIVATIONPROVIDER + "id=" + new PreferenceHelper(getContext()).getUserId() + "&&date=" + date + "&&status=pending";
        Log.d("url3", "Searchbydate: " + url);
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), 111, this);
    }

    private void getnotificationprovider() {
        String url = Const.ServiceType.GETNOTIFIVATIONPROVIDER + "id=" + new PreferenceHelper(getContext()).getUserId();
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), 123, this);
    }

    private void getnotification() {
        String url = Const.ServiceType.GETNOTIFIVATION + "id=" + new PreferenceHelper(getContext()).getUserId();
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), 123, this);
    }

    private void Mapped(View v) {
        Recyclernotification = v.findViewById(R.id.Recyclernotification);
        txtdate = v.findViewById(R.id.txtdate);
        image_avata = v.findViewById(R.id.image_avata);
        txtname = v.findViewById(R.id.txtname);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case 123:
                Log.d("cccc", "onTaskCompleted: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        ArrayList<Notification> Notification;
                        TypeToken<List<Notification>> token = new TypeToken<List<Notification>>() {
                        };
                        Notification = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        Recyclernotification.setLayoutManager(linearLayoutManager);
                        adapter = new NotificationAdapter(getContext(), Notification);
                        Recyclernotification.setAdapter(adapter);
                    } else {

                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 111:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        ArrayList<Notification> Notification;
                        TypeToken<List<Notification>> token = new TypeToken<List<Notification>>() {
                        };
                        Notification = new Gson().fromJson(response1.toString(), token.getType());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        Recyclernotification.setLayoutManager(linearLayoutManager);
                        adapter = new NotificationAdapter(getContext(), Notification);
                        Recyclernotification.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "No Notification", Toast.LENGTH_SHORT).show();
                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
        }
        Log.d("notification", "onTaskCompleted: " + response);
    }

    private void Searchbydate(String date) {
        String url = Const.ServiceType.GETNOTIFIVATION + "id=" + new PreferenceHelper(getContext()).getUserId() + "&&date=" + date + "&&status=pending";
        Log.d("url3", "Searchbydate: " + url);
        new Vollyheaders(url, new PreferenceHelper(getContext()).getSessionToken(), 111, this);
    }
}

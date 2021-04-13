package company.com.allder1.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import company.com.allder1.Adapter.CommentAdapter;
import company.com.allder1.Adapter.ViewpagerPictureAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.Comment;
import company.com.allder1.model.DataFood;
import company.com.allder1.model.Picturesdatum;
import company.com.allder1.utils.Const;

public class Sheetdetailfood extends BottomSheetDialogFragment implements AsyncTaskCompleteListener {
    ViewPager viewpagerimager;
    RecyclerView RecyclerViewcomment;
    TextView txtnamefood, txtnocommnent, numberrating, txtprice;
    String TAG = "Sheetdetailfood";
    int idfood;
    ImageView imagefood;
    DataFood detailfood;
    CommentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.sheetdetailfood, container, false);
        Mapped(v);
        detailfood = FoodStoreActivity.dataFood1;
        idfood = detailfood.getId();
        txtnamefood.setText(detailfood.getName());
        txtprice.setText(detailfood.getPrice() + "");
        if (detailfood.getPicture().contains(",")) {
            if (detailfood.getPicture().isEmpty()) {

            } else {
                String base64 = detailfood.getPicture();
                String base64Image = base64.split(",")[1];
                byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
                Glide.with(this).load(imageByteArray)
                        .error(R.drawable.ic_avata)
                        .centerCrop()
                        .crossFade()
                        .placeholder(R.drawable.ic_avata).into(imagefood);
            }
        } else {
            Glide.with(this).load(detailfood.getPicture())
                    .error(R.drawable.ic_avata)
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.drawable.ic_avata).into(imagefood);
        }
        getdatacomment();

        getdatapicturesfood();
        return v;
    }

    private void getdatapicturesfood() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETDETAILFOOD + "type_get=pictures" + "&food_id=" + idfood);
        Log.d("Manh", "getdatapicturesfood: " + map.toString());
        new VollyRequester(getActivity(), Const.GET, map, Const.ServiceCode.GETDATAPICTURES,
                this);
    }

    private void getdatacomment() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GETDETAILFOOD + "type_get=comments" + "&food_id=" + idfood);
        Log.d("Manh", "getdatacomment: " + map.toString());
        new VollyRequester(getActivity(), Const.GET, map, Const.ServiceCode.GETDATACOMMENTS,
                this);
    }

    private void Mapped(View v) {
        viewpagerimager = v.findViewById(R.id.viewpagerimager);
        RecyclerViewcomment = v.findViewById(R.id.RecyclerViewcomment);
        txtnamefood = v.findViewById(R.id.txtnamefood);
        txtnocommnent = v.findViewById(R.id.txtnocommnent);
        numberrating = v.findViewById(R.id.numberrating);
        txtprice = v.findViewById(R.id.txtprice);
        imagefood = v.findViewById(R.id.imagefood);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GETDATACOMMENTS:
                try {
                    Log.d(TAG, "onTaskCompleted1: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray response1 = jsonObject.getJSONArray("data");
                        ArrayList<Comment> datacomment = null;
                        TypeToken<List<Comment>> token1 = new TypeToken<List<Comment>>() {
                        };
                        datacomment = new Gson().fromJson(response1.toString(), token1.getType());
                        if (response1.get(0).equals("")) {
                            RecyclerViewcomment.setVisibility(View.GONE);
                            txtnocommnent.setVisibility(View.VISIBLE);
                        } else {
                            RecyclerViewcomment.setVisibility(View.VISIBLE);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        RecyclerViewcomment.setLayoutManager(linearLayoutManager);
                        adapter = new CommentAdapter(getContext(), datacomment);
                        RecyclerViewcomment.setAdapter(adapter);
                        //Holder.ratingBar_over.setRating(Float.valueOf(comment.getRating()));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.GETDATAPICTURES:
                try {
                    Log.d(TAG, "onTaskCompleted2: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray sasa = jsonObject.getJSONArray("data");
                    if (jsonObject.getString("success").equals("true")) {
                        ArrayList<Picturesdatum> datacomment = null;
                        TypeToken<List<Picturesdatum>> token1 = new TypeToken<List<Picturesdatum>>() {
                        };
                        datacomment = new Gson().fromJson(sasa.toString(), token1.getType());
                        ViewpagerPictureAdapter adapter = new ViewpagerPictureAdapter(getActivity(), datacomment);
                        viewpagerimager.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


        }
    }
}

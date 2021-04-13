package company.com.allder1.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import company.com.allder1.Adapter.Cartfood;
import company.com.allder1.Adapter.OrderfoodAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.MyService;
import company.com.allder1.utils.PreferenceHelper;
import io.socket.client.On;
public class Sheet_payman extends BottomSheetDialogFragment implements AsyncTaskCompleteListener  {
    LinearLayout card, qr, cardbit;
    String number_card;
    String order;
 static Dialog dialogA;
    ImageButton btnclosePaymetnt;
    static WebView webView;
    String total_money,chopsticks,fork,spoon,bowl,note;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_sheet_payman, container, false);
        card = v.findViewById(R.id.card);
        qr = v.findViewById(R.id.qr);
        cardbit = v.findViewById(R.id.carddebit);
        cardbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getContext(), "thanh cong", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "order successful", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            order = bundle.getString("order");
            total_money = bundle.getString("Totaldishes");
            chopsticks = bundle.getString("chopsticks");
            fork = bundle.getString("fork");
            spoon = bundle.getString("spoon");
            bowl = bundle.getString("bowl");
            note = bundle.getString("note");
            Log.d("Bundle",total_money);
        }
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Total",total_money);
                Dialogcard();
            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogQr();
            }
        });

        return v;


    }
//    private void ProgressDialog(){
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//
//
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                progressDialog.dismiss();
//
//
//            }
//        }).start();
//        progressDialog.show();
//
//
//    }

    private void DialogQr() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_qr_payman);
        dialog.show();
    }

public  static  void CloesPayment(){
       dialogA.cancel();
       OrderfoodAdapter.listorder.clear();
}
    private void Dialogcard() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogA = new Dialog(getContext(), R.style.DialogSlideAnim_leftright_Fullscreen);
        dialogA.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogA.setCancelable(false);
        dialogA.setContentView(R.layout.layoutcard);
        webView = dialogA.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject ;
        ArrayList<Cartfood> listoder = new ArrayList<>();
        for (int s = 0; s < OrderfoodAdapter.listorder.size(); s++) {
            listoder.add(new Cartfood(OrderfoodAdapter.listorder.get(s).getId()
                    , Integer.valueOf(OrderfoodAdapter.map.get(String.valueOf(OrderfoodAdapter.listorder.get(s).getId())))));
            jsonObject = new JSONObject();
            try {
                jsonObject.put("id", listoder.get(s).getId_food());
                jsonObject.put("quantity", listoder.get(s).getAmount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        String User_id=new PreferenceHelper(getActivity()).getUserId();
        String prider = String.valueOf(FoodStoreActivity.dataProviders.get(0).getId());
//        String token = new PreferenceHelper(getActivity()).getSessionToken();
        String food = jsonArray.toString();

        if(new PreferenceHelper(getContext()).getSpecies().equals("provider")) {
            String payment2 = "user_id="+ User_id+"&food="+food+"&type_person_order=providers&total_money="+total_money+"&provider_id="+prider+"&note="+note+"&spoon="+spoon+"&fork="+fork+"&chopsticks="+chopsticks+"&bowl="+bowl;
            String urlpayment = "http://allder.qooservices.cf/managetmentFoodApi/payment?" + payment2;
            webView.loadUrl(urlpayment);
            Log.d("UrlPayment", "" + urlpayment);
        }else {
            String payment2 = "user_id="+ User_id+"&food="+food+"&type_person_order=users&total_money="+String.valueOf(total_money)+"&provider_id="+prider.toString()+"&note="+note+"&spoon="+spoon.toString()+"&fork="+fork.toString()+"&chopsticks="+chopsticks.toString()+"&bowl="+bowl;
            String urlpayment = "http://allder.qooservices.cf/managetmentFoodApi/payment?" + payment2;
            webView.loadUrl(urlpayment);
            Log.d("UrlPayment", "" + urlpayment);
        }
//        HashMap<String, String> map = new HashMap<>();
//        map.put(Const.Params.URL, "http://allder.qooservices.cf/managetmentFoodApi/payment?");
//        map.put("food", "[{\"id\":23,\"quantity\":1},{\"id\":22,\"quantity\":1}]");
//        map.put("user_id", "14");
//        map.put("type_person_order", "users");
//        map.put("token","2y10I55uPLtrlwgWuhXk9WclOOq5ag8D0dq8hVzREyPbP0h8YAycmVRu");
//        map.put("total_money","1179");
//        map.put("provider_id", "15");
//        map.put("note", "notexzzzz");
////          EditText ed_Chopsticks,ed_spoon,ed_bowl,ed_folk;
//        map.put("chopsticks","5");
//        map.put("fork", "4");
//        map.put("spoon", "3");
//        map.put("bowl", "6");
//        Log.d("Manh", map.toString());
//      String.valueOf(new VollyRequester(getActivity(), Const.POST, map, Const.ServiceCode.ORDER,
//                this));
        btnclosePaymetnt = dialogA.findViewById(R.id.closePayment);
        btnclosePaymetnt.setVisibility(View.VISIBLE);
        btnclosePaymetnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogA.dismiss();
                getActivity().finish();
            }
        });
        dialogA.show();
        // chạy xong 3s mới dừng
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
        progressDialog.show();






    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            //211
            case Const.ServiceCode.ORDER:
                // webView.loadUrl(response);
//                Log.d("AAAAA",response+"");
//                webView.loadData(response, "text/html", "UTF-8");

                //                Log.d("Manh", "onTaskCompleted: " + response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.d("Manh", "onTaskCompleted: " + response);
//                    if (jsonObject.getBoolean("success")) {
//                        dialog.dismiss();
//                        OrderfoodAdapter.listorder.clear();
//                        Log.d("Manh", "onTaskCompleted: " + response);
//                        OrderfoodAdapter.map.clear();
//                      //  Toast.makeText(getContext(), "thanh cong", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "order successful", Toast.LENGTH_SHORT).show();
//                        getActivity().finish();
//                    } else {
//                        Toast.makeText(getContext(), "system error", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;

        }
    }
    private void Order(String numbersJson,String Totaldishes,String ed_Chopsticks,String ed_folk ,String ed_spoon,String ed_bowl,String note) {
        String type_person_order;
        if (new PreferenceHelper(getActivity()).getUserId().contains(String.valueOf(FoodStoreActivity.dataProviders.get(0).getId()))) {
            Log.d("cccccccccc", "Order: ");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Const.Params.URL, Const.ServiceType.POSTORDER);
            map.put("food", numbersJson);
            map.put("user_id", new PreferenceHelper(getActivity()).getUserId());
            map.put("type_person_order", "providers");
            map.put("total_money", String.valueOf(Totaldishes));
            map.put("token", new PreferenceHelper(getActivity()).getSessionToken());
            map.put("provider_id", String.valueOf(FoodStoreActivity.dataProviders.get(0).getId()));
            //EditText ed_Chopsticks,ed_spoon,ed_bowl,ed_folk;
            map.put("chopsticks", ed_Chopsticks.toString());
            map.put("fork", ed_folk.toString());
            map.put("spoon", ed_spoon.toString());
            map.put("bowl", ed_bowl.toString());
            Log.d("Manhproviders", map.toString());
            new VollyRequester(getActivity(), Const.POST, map, Const.ServiceCode.ORDER,
                    this);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(Const.Params.URL, Const.ServiceType.POSTORDER);
            map.put("food", numbersJson);
            map.put("user_id", new PreferenceHelper(getActivity()).getUserId());
            map.put("type_person_order", "users");
            map.put("token", new PreferenceHelper(getActivity()).getSessionToken());
            map.put("total_money", String.valueOf(Totaldishes));
            map.put("provider_id", String.valueOf(FoodStoreActivity.dataProviders.get(0).getId()));
            map.put("note", note);
//          EditText ed_Chopsticks,ed_spoon,ed_bowl,ed_folk;
            map.put("chopsticks", ed_Chopsticks);
            map.put("fork", ed_folk);
            map.put("spoon", ed_spoon);
            map.put("bowl", ed_bowl);
            Log.d("Manh", map.toString());
            new VollyRequester(getActivity(), Const.POST, map, Const.ServiceCode.ORDER,
                    this);
        }
    }
}

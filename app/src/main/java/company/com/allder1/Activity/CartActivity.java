package company.com.allder1.Activity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nets.enets.exceptions.InvalidPaymentRequestException;
import com.nets.enets.listener.PaymentCallback;
import com.nets.enets.network.PaymentRequestManager;
import com.nets.enets.utils.result.DebitCreditPaymentResponse;
import com.nets.enets.utils.result.NETSError;
import com.nets.enets.utils.result.NonDebitCreditPaymentResponse;
import com.nets.enets.utils.result.PaymentResponse;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import company.com.allder1.Adapter.Cartfood;
import company.com.allder1.Adapter.OrderfoodAdapter;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.DataFood;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.HMAC_Gen;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;



public class CartActivity extends AppCompatActivity implements AsyncTaskCompleteListener, OrderfoodAdapter.ItemClickListener1, View.OnClickListener {
    RecyclerView recyclerviewcart;
    OrderfoodAdapter adapter;
    TextView txttotal_price, txttotal_dishes;
    public static DataFood dataFood1;
    EditText ed_Chopsticks, ed_spoon, ed_bowl, ed_folk;
    public static int screen;
    Button btnorder;
    int Totaldishes;
    EditText edtnote;
    int total_dishes;
    private RequestQueue requestQueue;
    int gia;
    View background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_cart);
        Mapped();
        antimations(savedInstanceState);
        SetAdapterCart();
        if (OrderfoodAdapter.listorder.size() <= 0) {
            CartActivity.this.finish();
        }
        for (int s = 0; s < OrderfoodAdapter.listorder.size(); s++) {
            Totaldishes += OrderfoodAdapter.listorder.get(s).getPrice() * Integer.valueOf(OrderfoodAdapter.map.get(String.valueOf(OrderfoodAdapter.listorder.get(s).getId())));
            txttotal_price.setText(Totaldishes + FoodStoreActivity.currency);
            Log.d("Manh", "onBindViewHolder: " + Totaldishes);
            total_dishes += Integer.valueOf(OrderfoodAdapter.map.get(String.valueOf(OrderfoodAdapter.listorder.get(s).getId())));
            txttotal_dishes.setText(total_dishes + "");
            Log.d("Manh", "onBindViewHolder: " + total_dishes);
        }
    }

    private void antimations(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            background.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularRevealActivity() {
        int cx = background.getRight() - getDips(44);
        int cy = background.getBottom() - getDips(44);

        float finalRadius = Math.max(background.getWidth(), background.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                background,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(1000);
        background.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    private void SetAdapterCart() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewcart.setLayoutManager(linearLayoutManager);
        adapter = new OrderfoodAdapter(CartActivity.this, OrderfoodAdapter.listorder, this, FoodStoreActivity.currency);
        recyclerviewcart.setAdapter(adapter);
    }

    private void Mapped() {
        recyclerviewcart = findViewById(R.id.recyclerviewcart);
        txttotal_price = findViewById(R.id.txttotal_price);
        btnorder = findViewById(R.id.btnorder);
        edtnote = findViewById(R.id.edtnote);
        background = findViewById(R.id.background);
        txttotal_dishes = findViewById(R.id.txttotal_dishes);
        ed_Chopsticks = findViewById(R.id.ed_Chopsticks);
        ed_spoon = findViewById(R.id.ed_spoon);
        ed_bowl = findViewById(R.id.ed_bowl);
        ed_folk = findViewById(R.id.ed_folk);
    }

    @Override
    public void onClick1(ArrayList<DataFood> dataFood, int position, boolean isLongClick) {
        if (isLongClick == false && position == 0) {
            this.recreate();
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
            if (dataFood.size() <= 0) {
                CartActivity.this.finish();
            }
        }
    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.ORDER:
                Log.d("Manh", "onTaskCompleted: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Manh", "onTaskCompleted: " + response);
                    if (jsonObject.getBoolean("success")) {
                        OrderfoodAdapter.listorder.clear();
                        Log.d("Manh", "onTaskCompleted: " + response);
                        OrderfoodAdapter.map.clear();
                        Toast.makeText(this, "order successful", Toast.LENGTH_SHORT).show();
                        CartActivity.this.finish();
                    } else {
                        Toast.makeText(this, "system error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnorder:
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject ;
                ArrayList<Cartfood> listoder = new ArrayList<>();
                for (int s = 0; s < OrderfoodAdapter.listorder.size(); s++){
                    listoder.add(new Cartfood(OrderfoodAdapter.listorder.get(s).getId()
                            ,Integer.valueOf(OrderfoodAdapter.map.get(String.valueOf(OrderfoodAdapter.listorder.get(s).getId())))));
                    Gson gson = new Gson();
                    //  String numbersJson = gson.toJson(listoder);
                    //Order(numbersJson);
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", listoder.get(s).getId_food());
                        jsonObject.put("quantity", listoder.get(s).getAmount());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                if(new PreferenceHelper(this).getSpecies().equals("provider")&& !new PreferenceHelper(this).getUserId().contains(String.valueOf(FoodStoreActivity.dataProviders.get(0).getId()))){
                    Sheet_payman sheet_payma = new Sheet_payman();
                    sheet_payma.show(getSupportFragmentManager(), "exampleBottomSheet");
                    Bundle bundle = new Bundle();
                    bundle.putString("order", jsonArray.toString());
                    bundle.putString("Totaldishes", String.valueOf(Totaldishes));
                    bundle.putString("chopsticks", ed_Chopsticks.getText().toString());
                    bundle.putString("fork", ed_folk.getText().toString());
                    bundle.putString("spoon", ed_spoon.getText().toString());
                    bundle.putString("bowl", ed_bowl.getText().toString());
                    bundle.putString("note", edtnote.getText().toString());
                    sheet_payma.setArguments(bundle);
                }else if(new PreferenceHelper(this).getSpecies().equals("Consumer")) {
                    Sheet_payman sheet_payma = new Sheet_payman();
                    sheet_payma.show(getSupportFragmentManager(), "exampleBottomSheet");
                    Bundle bundle = new Bundle();
                    bundle.putString("order", jsonArray.toString());
                    bundle.putString("Totaldishes", String.valueOf(Totaldishes));
                    bundle.putString("chopsticks", ed_Chopsticks.getText().toString());
                    bundle.putString("fork", ed_folk.getText().toString());
                    bundle.putString("spoon", ed_spoon.getText().toString());
                    bundle.putString("bowl", ed_bowl.getText().toString());
                    bundle.putString("note", edtnote.getText().toString());
                    sheet_payma.setArguments(bundle);
                }else {
                      Order(jsonArray.toString());
                }
                break;
        }
    }
    private void Order(String numbersJson) {

        String type_person_order;
        if (new PreferenceHelper(this).getUserId().contains(String.valueOf(FoodStoreActivity.dataProviders.get(0).getId()))) {
            HashMap<String, String> map = new HashMap<String, String>();
            //Const.ServiceType.POSTORDER
            map.put(Const.Params.URL, "http://allder.qooservices.cf/managetmentFoodApi/order");
            map.put("food", numbersJson);
            map.put("user_id", new PreferenceHelper(this).getUserId());
            map.put("type_person_order", "providers");
            map.put("total_money", String.valueOf(Totaldishes));
//            map.put("token", new PreferenceHelper(this).getSessionToken());
            map.put("provider_id", String.valueOf(FoodStoreActivity.dataProviders.get(0).getId()));
            //EditText ed_Chopsticks,ed_spoon,ed_bowl,ed_folk;
            map.put("chopsticks", ed_Chopsticks.getText().toString());
            map.put("fork", ed_folk.getText().toString());
            map.put("spoon", ed_spoon.getText().toString());
            map.put("bowl", ed_bowl.getText().toString());
            Log.d("Manh", map.toString());
            new VollyRequester(this, Const.POST, map, Const.ServiceCode.ORDER,
                    this);
        } else {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Const.Params.URL, Const.ServiceType.POSTORDER);
            map.put("food", numbersJson);
            map.put("user_id", new PreferenceHelper(this).getUserId());
            map.put("type_person_order", "users");
            map.put("token", new PreferenceHelper(this).getSessionToken());
            map.put("total_money", String.valueOf(Totaldishes));
            map.put("provider_id", String.valueOf(FoodStoreActivity.dataProviders.get(0).getId()));
            map.put("note", edtnote.getText().toString());
//            EditText ed_Chopsticks,ed_spoon,ed_bowl,ed_folk;
            map.put("chopsticks", ed_Chopsticks.getText().toString());
            map.put("fork", ed_folk.getText().toString());
            map.put("spoon", ed_spoon.getText().toString());
            map.put("bowl", ed_bowl.getText().toString());
            Log.d("Manh", map.toString());
            new VollyRequester(this, Const.POST, map, Const.ServiceCode.ORDER,
                    this);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //tạo hiệu ứng
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            int cx = background.getWidth() - getDips(44);
//            int cy = background.getBottom() - getDips(44);
//
//            float finalRadius = Math.max(background.getWidth(), background.getHeight());
//            Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, finalRadius, 0);
//
//            circularReveal.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    background.setVisibility(View.INVISIBLE);
//                    finish();
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            });
//            circularReveal.setDuration(1000);
//            circularReveal.start();
//        } else {
//            super.onBackPressed();
//        }
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
    private void startPayment(String txnReq, final String sKey, String key) {

        // Generate HMAC
        String hmac = HMAC_Gen.generateSignature(txnReq, sKey);
        Log.d("startPayment", "txn: " + txnReq);
        Log.d("startPayment", "DEFAULT_SECRET_KEY: " + sKey);
        Log.d("startPayment", "hmac: " + hmac);
        PaymentRequestManager manager = PaymentRequestManager.getSharedInstance();
        try {
            manager.sendPaymentRequest(key, hmac, txnReq, new PaymentCallback() {
                @Override
                public void onResult(PaymentResponse paymentResponse) {
                    // To implement callback functions
                    if (paymentResponse instanceof DebitCreditPaymentResponse) {
                        final DebitCreditPaymentResponse debitCreditPaymentResponse = (DebitCreditPaymentResponse) paymentResponse;
                        String txnRes = debitCreditPaymentResponse.txnResp;
                        String hmac = debitCreditPaymentResponse.hmac;
                        String keyId = debitCreditPaymentResponse.keyId;
                        Log.d("DebitCreditPaymentResponse", "txnRes: " + txnRes);
                        Log.d("DebitCreditPaymentResponse", "hmac: " + hmac);
                        Log.d("DebitCreditPaymentResponse", "keyId: " + keyId);
                        // Next 4 lines show a simplified verification.
                        // Basically checking if the hmac returned tallies with a hmac generated by our secret key
                        String hmacVerification = HMAC_Gen.generateSignature(txnRes, sKey);
                        Log.d("DebitCreditPaymentResponse", "hmacVerification: " + hmacVerification);
                        if (hmacVerification.equals(hmac)){
                            Log.d("DebitCreditPaymentResponse", "Verification Successful");
                        }
                        try {
                            JSONObject txnJSON = new JSONObject(txnRes);
                            JSONObject msg = txnJSON.getJSONObject("msg");
                            String stageRespCode = msg.getString("stageRespCode");
                            Toast.makeText(CartActivity.this, "Payment Success\nstageRespCode: " + stageRespCode, Toast.LENGTH_LONG).show();
                            Log.d("DebitCreditPaymentResponse", "stageRespCode: " + stageRespCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // NETSPay payment will result in this callback.
                    } else if (paymentResponse instanceof NonDebitCreditPaymentResponse) {
                        final NonDebitCreditPaymentResponse nonDebitCreditPaymentResponse = (NonDebitCreditPaymentResponse) paymentResponse;
                        String txn_Status = nonDebitCreditPaymentResponse.status;
                        Log.d("nonDebitCreditPaymentResponse", "txn_Status: " + txn_Status);
                        Toast.makeText(CartActivity.this, "nonDebitCreditPaymentResponse Payment Success", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(NETSError netsError) {
                    String txn_ResponseCode = netsError.responeCode;
                    String txn_ActionCode = netsError.actionCode;
                    Log.d("netsError", "txn_ResponseCode: " + txn_ResponseCode);
                    Log.d("netsError", "txn_ActionCode: " + txn_ActionCode);

                }
            }, this);
        } catch (InvalidPaymentRequestException e) {
            e.printStackTrace();
            Log.e("InvalidPaymentException", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
        }

    }
}






package company.com.allder1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import company.com.allder1.Activity.DetailOderActivity;
import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.OrderNOApprove;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;


public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> implements AsyncTaskCompleteListener {
    ArrayList<OrderNOApprove> listorder;
    Context mcontext;
    int screen;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final Calendar calendar = Calendar.getInstance();
    java.util.Date datestar, dateend;
    private ItemClickListener itemClickListener;

    public AdapterOrder(Context mcontext, ArrayList<OrderNOApprove> listorder, ItemClickListener itemClickListener, int screen) {
        this.listorder = listorder;
        this.mcontext = mcontext;
        this.itemClickListener = itemClickListener;
        this.screen = screen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_order, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder Holder, final int i) {
        Holder.txttotalorder.setText(listorder.get(i).getLastName() + " " + listorder.get(i).getFirstName());
        String endtime = simpleDateFormat.format(calendar.getTime());
        Log.d("time", "onBindViewHolder: ");
        try {
            datestar = simpleDateFormat.parse(listorder.get(i).getUpdatedAt());
            dateend = simpleDateFormat.parse(endtime);
            Long a = dateend.getTime() - datestar.getTime();
            long diffSeconds = a / 1000 % 60;
            long diffMinutes = a / (60 * 1000) % 60;
            long diffHours = a / (60 * 60 * 1000) % 24;
            long diffDays = a / (24 * 60 * 60 * 1000);
            Log.d("time1", "diffSeconds: " + diffSeconds);
            Log.d("time1", "diffMinutes: " + diffMinutes);
            Log.d("time1", "diffHours: " + diffHours);
            Log.d("time1", "diffDays: " + diffDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Holder.txtdata.setText(listorder.get(i).getUpdatedAt());
        Holder.txttotalorder.setText(listorder.get(i).getTotalFoods() + "");
        if (listorder.get(i).getTotalFoods() != null) {
            if (listorder.get(i).getTotalFoods() == 1) {
                Holder.texttotalorder.setText(listorder.get(i).getTotalFoods() + " Dish");
            } else {
                Holder.texttotalorder.setText(listorder.get(i).getTotalFoods() + " Dishes");
            }
        }
        if (new PreferenceHelper(mcontext).getSpecies().contains("provider")) {
            if (listorder.get(i).getTypePersonOrder().contains("users")) {
                Holder.imgic.setImageResource(R.drawable.ic_order);
                Holder.txtname.setTextColor(Color.rgb(0, 255, 239));
                Holder.txtdata.setTextColor(Color.rgb(0, 255, 239));
                Holder.texttotalorder.setTextColor(Color.rgb(0, 255, 239));
                Holder.txttotalorder.setBackground(mcontext.getResources().getDrawable(R.drawable.custom_circle_1));
                Holder.txtname.setText(listorder.get(i).getLastName() + " " + listorder.get(i).getFirstName());
            } else {
                Holder.imgic.setImageResource(R.drawable.ic_order_m);
                Holder.txtname.setText(listorder.get(i).getNameStore());
            }
        } else {
            Holder.txtname.setText(listorder.get(i).getNameStore());
        }
        Holder.relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, DetailOderActivity.class);
                intent.putExtra("totalmoney", listorder.get(i).getTotalMoney());
                intent.putExtra("idfood", listorder.get(i).getId());
                intent.putExtra("note", listorder.get(i).getNote());
                intent.putExtra("is_approved", listorder.get(i).getStatus());
                if (listorder.get(i).getLastName().isEmpty()) {
                    intent.putExtra("name", listorder.get(i).getNameStore());
                } else {
                    intent.putExtra("name", listorder.get(i).getLastName() + " " + listorder.get(i).getFirstName());
                }
                mcontext.startActivity(intent);
                CustomIntent.customType(mcontext, "fadein-to-fadeout");
            }
        });
        Holder.img_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "approve";
                Handleorder(action, i);
            }
        });
        Holder.img_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "decline";
                Handleorder(action, i);
            }
        });
        if (screen == 0) {
            Holder.img_yes.setVisibility(View.VISIBLE);
            Holder.img_no.setVisibility(View.VISIBLE);
            Holder.txtstatus.setVisibility(View.GONE);
        } else if (screen == 1) {
            Holder.img_yes.setVisibility(View.GONE);
            Holder.img_no.setVisibility(View.GONE);
            Holder.txtstatus.setVisibility(View.GONE);
        } else if (screen == 2) {
            Holder.img_yes.setVisibility(View.GONE);
            Holder.img_no.setVisibility(View.GONE);
            Holder.txtstatus.setText(listorder.get(i).getStatus());

        }
    }

    private void Handleorder(String action, int position) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.HANDLEORDER);
        map.put("id", new PreferenceHelper(mcontext).getUserId());
        map.put("token", new PreferenceHelper(mcontext).getSessionToken());
        map.put("order_id", String.valueOf(listorder.get(position).getId()));
        map.put("action", action);
        Log.d("mahi", map.toString());
        new VollyRequester(mcontext, Const.POST, map, 1111,
                this);
    }

    @Override
    public int getItemCount() {
        return listorder.size();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                Toast.makeText(mcontext, "success", Toast.LENGTH_SHORT).show();
                itemClickListener.onClick();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttotalorder, txtname, txtdata, txtstatus, texttotalorder;
        ImageView img_yes, img_no, imgic;
        ConstraintLayout relativelayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttotalorder = itemView.findViewById(R.id.txttotalorder);
            txtname = itemView.findViewById(R.id.txtname);
            txtdata = itemView.findViewById(R.id.txtdata);
            img_yes = itemView.findViewById(R.id.img_yes);
            img_no = itemView.findViewById(R.id.img_no);
            txtstatus = itemView.findViewById(R.id.txtstatus);
            relativelayout = itemView.findViewById(R.id.relativelayout);
            texttotalorder = itemView.findViewById(R.id.texttotalorder);
            imgic = itemView.findViewById(R.id.imgic);
            Log.d("manh", "onClick: " + screen);
        }
    }

    public interface ItemClickListener {
        void onClick();
    }
}

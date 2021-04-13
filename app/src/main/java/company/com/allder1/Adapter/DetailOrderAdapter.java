package company.com.allder1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import company.com.allder1.R;
import company.com.allder1.model.DetailOrderFood;


public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.ViewHolder> {
    ArrayList<DetailOrderFood> detailoreder;
    Context mcontext;
    String currency;

    public DetailOrderAdapter(Context mcontext, ArrayList<DetailOrderFood> detailoreder, String currency) {
        this.detailoreder = detailoreder;
        this.mcontext = mcontext;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_detail_order, viewGroup, false);

        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, final int i) {
        int money = 0;
        Holder.txtnamefood.setText(detailoreder.get(i).getNameFood());
        Holder.txtamount.setText("x"+detailoreder.get(i).getQuantity());
        money += detailoreder.get(i).getQuantity() * detailoreder.get(i).getPrice();
        Holder.txtmoney.setText(detailoreder.get(i).getPrice() + currency);
        if(detailoreder.get(i).getPicture().isEmpty()){

        }else {
            String base64 = detailoreder.get(i).getPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(mcontext).load(imageByteArray).into(Holder.imagefood);
        }
    }

    @Override
    public int getItemCount() {
        return detailoreder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnamefood, txtamount, txtmoney;
        ImageView imagefood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagefood = itemView.findViewById(R.id.imagefood);
            txtnamefood = itemView.findViewById(R.id.txtnamefood);
            txtamount = itemView.findViewById(R.id.txtamount);
            txtmoney = itemView.findViewById(R.id.txtmoney);
        }
    }

}

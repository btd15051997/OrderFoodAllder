package company.com.allder1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import company.com.allder1.Activity.FoodStoreActivity;
import company.com.allder1.R;
import company.com.allder1.model.Listfood;
import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;


public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.ViewHolder> {
    ArrayList<Listfood> listfoods;
    Context mcontext;
    String currency;


    public ListFoodAdapter(Context mcontext, ArrayList<Listfood> listfoods,String currency) {
        this.listfoods = listfoods;
        this.mcontext = mcontext;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.itemfood, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, final int i) {
        final Listfood listfood = listfoods.get(i);
        Holder.txtnamefood.setText(listfood.getName());
        Holder.txtaddress.setText(listfood.getAddress());
        if (currency != null) {
            Holder.txtprice.setText(listfood.getPrice() + " " + currency);
        }else if(currency==null){
            Holder.txtprice.setText(listfood.getPrice());
        }
        if(listfood.getPicture().isEmpty()){

        }else {
            String base64 = listfood.getPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(mcontext).load(imageByteArray).into(Holder.image_food);
        }
        Holder.txtcomment.setText(listfood.getComments() + "");
        if (listfood.getLikes() != null) {
            Holder.txtlike.setText(listfood.getLikes() + "");
        } else {
            Holder.txtlike.setText("0");
        }
        Holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, FoodStoreActivity.class);
                intent.putExtra("id_provider", listfood.getProviderId());
                mcontext.startActivity(intent);
                CustomIntent.customType(mcontext, "fadein-to-fadeout");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listfoods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image_food;
        TextView txtnamefood, txtaddress, txtnamestore, txtnumberorder, txtcomment, txtlike, txtprice;
        Button btnoder, btnminusorder, imgaddorder;
        LinearLayout layout1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_food = itemView.findViewById(R.id.image_food);
            layout1 = itemView.findViewById(R.id.layout1);
            txtnamefood = itemView.findViewById(R.id.txtnamefood);
            txtaddress = itemView.findViewById(R.id.txtaddress);
            txtnamestore = itemView.findViewById(R.id.txtnamestore);
            txtnumberorder = itemView.findViewById(R.id.txtnumberorder);
            txtcomment = itemView.findViewById(R.id.txtcomment);
            txtlike = itemView.findViewById(R.id.txtlike);
            txtprice = itemView.findViewById(R.id.txtprice);
//            btnoder =  itemView.findViewById(R.id.btnoder);
//            btnminusorder =  itemView.findViewById(R.id.btnminusorder);
//            imgaddorder =  itemView.findViewById(R.id.imgaddorder);
            Log.d("hoo", " ");
            ///

        }
    }

}

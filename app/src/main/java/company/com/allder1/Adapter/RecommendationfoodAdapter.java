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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import company.com.allder1.Activity.FoodStoreActivity;
import company.com.allder1.R;
import company.com.allder1.model.RecommendationFood;
import maes.tech.intentanim.CustomIntent;


public class RecommendationfoodAdapter extends RecyclerView.Adapter<RecommendationfoodAdapter.ViewHolder> {
    ArrayList<RecommendationFood> listRecommendationFood;
    Context mcontext;
    int more;

    public RecommendationfoodAdapter(Context mcontext, ArrayList<RecommendationFood> listRecommendationFood, int more) {
        this.listRecommendationFood = listRecommendationFood;
        this.mcontext = mcontext;
        this.more = more;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_food_recommendation, viewGroup, false);
        return new ViewHolder(itemview);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.txtnamefood.setText(listRecommendationFood.get(i).getName());
        viewHolder.txtlike.setText(listRecommendationFood.get(i).getLikes() + "");
        viewHolder.txtcomment.setText(listRecommendationFood.get(i).getComments() + "");
        viewHolder.txtprice.setText(listRecommendationFood.get(i).getPrice());
        if (listRecommendationFood.get(i).getPicture().isEmpty()) {

        } else {
            String base64 = listRecommendationFood.get(i).getPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(mcontext).load(imageByteArray).into(viewHolder.imgfood);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, FoodStoreActivity.class);
                Log.d("cccc", "onClick: " + listRecommendationFood.get(i).getProviderId());
                intent.putExtra("id_provider", Integer.valueOf(listRecommendationFood.get(i).getProviderId()));
                mcontext.startActivity(intent);
                CustomIntent.customType(mcontext, "fadein-to-fadeout");
            }
        });
    }

    @Override
    public int getItemCount() {
        return more;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgfood;
        TextView txtnamefood, txtcomment, txtlike, txtprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgfood = itemView.findViewById(R.id.imgfood);
            txtnamefood = itemView.findViewById(R.id.txtnamefood);
            txtcomment = itemView.findViewById(R.id.txtcomment);
            txtlike = itemView.findViewById(R.id.txtlike);
            txtprice = itemView.findViewById(R.id.txtprice);
        }
    }

}

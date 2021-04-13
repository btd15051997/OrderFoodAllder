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
import company.com.allder1.model.Nearbystore;
import maes.tech.intentanim.CustomIntent;


public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {
    ArrayList<Nearbystore> Nearbystore;
    Context mcontext;
    int more;

    public NearbyAdapter(Context mcontext, ArrayList<Nearbystore> Nearbystore, int more) {
        this.Nearbystore = Nearbystore;
        this.mcontext = mcontext;
        this.more = more;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_nearbystore, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if (Nearbystore != null) {
            viewHolder.txtnamestore.setText(Nearbystore.get(i).getNameStore());
            viewHolder.txtdistance.setText(Nearbystore.get(i).getDistance());
            if (Nearbystore.get(i).getCover_picture().isEmpty()) {

            } else {
                String base64 = Nearbystore.get(i).getCover_picture();
                String base64Image = base64.split(",")[1];
                byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
                Glide.with(mcontext).load(imageByteArray).into(viewHolder.imgcover_picture);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, FoodStoreActivity.class);
                    Log.d("Manh", "onClick: " + Nearbystore.get(i).getId());
                    intent.putExtra("id_provider", Nearbystore.get(i).getId());
                    mcontext.startActivity(intent);
                    CustomIntent.customType(mcontext, "fadein-to-fadeout");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return more;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnamestore, txtdistance;
        ImageView imgcover_picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtnamestore = itemView.findViewById(R.id.txtnamestore);
            txtdistance = itemView.findViewById(R.id.txtdistance);
            imgcover_picture = itemView.findViewById(R.id.imgcover_picture);
        }
    }

}

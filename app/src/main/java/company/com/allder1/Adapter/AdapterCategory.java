package company.com.allder1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import company.com.allder1.Activity.ListFoodActivity;
import company.com.allder1.R;
import company.com.allder1.model.Category;
import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {
    ArrayList<Category> listcategory;
    Context mcontext;

    public AdapterCategory(ArrayList<Category> listcategory, Context mcontext) {
        this.listcategory = listcategory;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.itemcategory, viewGroup, false);
        return new AdapterCategory.ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.txtname.setText(listcategory.get(i).getName());
        if (listcategory.get(i).getPicture().isEmpty()) {

        } else {
            String base64 = listcategory.get(i).getPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(mcontext).load(imageByteArray).into(viewHolder.img_icon);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ListFoodActivity.class);
                intent.putExtra("id_categori", listcategory.get(i).getId());
                mcontext.startActivity(intent);
                CustomIntent.customType(mcontext, "fadein-to-fadeout");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listcategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        CircleImageView img_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            img_icon = itemView.findViewById(R.id.iv_user_icon);
        }
    }
}

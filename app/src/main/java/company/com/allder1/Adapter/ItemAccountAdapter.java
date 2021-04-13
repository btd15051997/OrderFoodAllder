package company.com.allder1.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import company.com.allder1.R;
import company.com.allder1.model.ItemAccount;
import maes.tech.intentanim.CustomIntent;


public class ItemAccountAdapter extends RecyclerView.Adapter<ItemAccountAdapter.ViewHolder> {
    List<ItemAccount> Listacc;
    Context mcontext;

    public ItemAccountAdapter(Context mcontext, List<ItemAccount> listaccount) {
        this.Listacc = listaccount;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_account, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
//        Drawable drawable = mcontext.getResources().getDrawable(Listacc.get(i).getImage());
//        drawable.setBounds(0, 0, 60, 60);
        //setCompoundDrawablesz
        holder.imgicon.setImageResource(Listacc.get(i).getImage());
       // holder.btnitem.setCompoundDrawables(drawable, null, null, null);
        holder.btnitem.setText(Listacc.get(i).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcontext.startActivity(Listacc.get(i).getIntent());
                CustomIntent.customType(mcontext, "fadein-to-fadeout");
            }
        });
    }

    @Override
    public int getItemCount() {
        return Listacc.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnitem;
        ImageView imgicon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnitem = itemView.findViewById(R.id.btn_item);
            imgicon = itemView.findViewById(R.id.imgicon);

        }
    }

}

package company.com.allder1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import company.com.allder1.R;
import company.com.allder1.model.ItemAccount;


public class ItemAccountAdapter1 extends RecyclerView.Adapter<ItemAccountAdapter1.ViewHolder> {
    ArrayList<ItemAccount> listaccount;
    Context mcontext;

    public ItemAccountAdapter1( Context mcontext,ArrayList<ItemAccount> listaccount) {
        this.listaccount = listaccount;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_account, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

    }

    @Override
    public int getItemCount() {
        return listaccount.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}

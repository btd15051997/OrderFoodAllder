package company.com.allder1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import company.com.allder1.R;
import company.com.allder1.model.Notification;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    ArrayList<Notification> Notification;
    Context mcontext;


    public NotificationAdapter(Context mcontext, ArrayList<Notification> Notification) {
        this.Notification = Notification;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_notification, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, final int i) {
        if (Notification.get(i).getTitle().contains("Order")) {
            Holder.imgicon.setImageResource(R.drawable.ic_newmenu);
        }
        Holder.txtnotification.setText(Notification.get(i).getContent());
        Holder.txttitle.setText(Notification.get(i).getTitle());
        Holder.txtdate.setText(Notification.get(i).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return Notification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgicon;
        TextView txtnotification, txttitle, txtdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgicon = itemView.findViewById(R.id.imgicon);
            txtnotification = itemView.findViewById(R.id.txtnotification);
            txttitle = itemView.findViewById(R.id.txttitle);
            txtdate = itemView.findViewById(R.id.txtdate);

        }
    }

}

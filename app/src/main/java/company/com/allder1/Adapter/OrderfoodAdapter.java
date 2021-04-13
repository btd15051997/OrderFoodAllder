package company.com.allder1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import company.com.allder1.Activity.FoodStoreActivity;
import company.com.allder1.R;
import company.com.allder1.model.DataFood;


public class OrderfoodAdapter extends RecyclerView.Adapter<OrderfoodAdapter.ViewHolder> {
    ArrayList<DataFood> datafood;
    Context mcontext;
    int index;
    int amount;
    String currency;
    int Totaldishes;
    public static HashMap<String, String> map = new HashMap<>();
    public static ArrayList<DataFood> listorder = new ArrayList<>();
    private ItemClickListener1 itemClickListener1;

    public OrderfoodAdapter(Context mcontext, ArrayList<DataFood> datafood, ItemClickListener1 itemClickListener1, String currency) {
        this.datafood = datafood;
        this.mcontext = mcontext;
        this.itemClickListener1 = itemClickListener1;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.itemfoodorder, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder Holder, final int i) {
        final int y = 1;

        final DataFood DataFood = datafood.get(i);
        if (OrderfoodAdapter.map.values().isEmpty() == true) {
            Holder.lineorder.setVisibility(View.GONE);
            Holder.imgnote.setVisibility(View.GONE);
        } else {
            Log.d("Manh", OrderfoodAdapter.map.toString());
            Holder.lineorder.setVisibility(View.VISIBLE);
            Holder.imgnote.setVisibility(View.VISIBLE);
            Holder.btnorder.setVisibility(View.GONE);
            Holder.txtnumberorder.setText(OrderfoodAdapter.map.get(String.valueOf(DataFood.getId())));
            if (Holder.txtnumberorder.getText().toString().equals("")) {
                amount = 0;
            } else {
                amount = Integer.valueOf(Holder.txtnumberorder.getText().toString());
            }

        }
        Holder.txtnamefood.setText(DataFood.getName());
        if (currency != null) {
            Holder.txtprice.setText(DataFood.getPrice() + " " + currency);
        } else if (currency == null) {
            Holder.txtprice.setText(DataFood.getPrice());
        }
        index = i;
        if (DataFood.getPicture().isEmpty()) {

        } else {
            String base64 = DataFood.getPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(mcontext).load(imageByteArray).into(Holder.imgfood);
        }
        if (FoodStoreActivity.close_the_door == true) {
            Holder.btnorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext, "Currently the store is closed and cannot order", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Holder.btnorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (datafood.get(0).getId() == null) {
                    } else {
                        OrderfoodAdapter.listorder.remove(datafood.get(i));
                        itemClickListener1.onClick1(listorder, 0, false);
                    }
                    OrderfoodAdapter.listorder.add(DataFood);
                    itemClickListener1.onClick1(listorder, 1, false);
                    Holder.lineorder.setVisibility(View.VISIBLE);
                    Holder.imgnote.setVisibility(View.VISIBLE);
                    Holder.btnorder.setVisibility(View.GONE);
                    Holder.txtnumberorder.setText(y + "");
                    OrderfoodAdapter.map.put(String.valueOf(DataFood.getId()), String.valueOf(y));
                    itemClickListener1.onClick1(listorder, 0, false);
                }
            });
        }
        Holder.txtcomment.setText(DataFood.getComments() + "");
        Holder.txtlike.setText(DataFood.getLikes() + "");

        Holder.imgaddorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(Holder.txtnumberorder.getText().toString());
                if (amount > 0) {
                    amount++;
                    itemClickListener1.onClick1(listorder, amount, false);
                }
                Holder.txtnumberorder.setText(amount + "");
                OrderfoodAdapter.map.put(String.valueOf(DataFood.getId()), Holder.txtnumberorder.getText().toString());
                itemClickListener1.onClick1(listorder, 0, false);
                if (Integer.parseInt(Holder.txtnumberorder.getText().toString()) == 0) {
                    Holder.lineorder.setVisibility(View.GONE);
                    Holder.imgnote.setVisibility(View.GONE);
                    Holder.btnorder.setVisibility(View.VISIBLE);
                }
            }
        });

        Holder.btnminusorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(Holder.txtnumberorder.getText().toString());
                if (amount != 0) {
                    amount--;
                    if (amount == 0) {
                        OrderfoodAdapter.listorder.remove(datafood.get(i));
                        itemClickListener1.onClick1(listorder, amount, false);
                        Log.d("Manh", "listorder: " + listorder.toString());
                    }

                }
                Holder.txtnumberorder.setText(amount + "");
                OrderfoodAdapter.map.put(String.valueOf(DataFood.getId()), Holder.txtnumberorder.getText().toString());
                itemClickListener1.onClick1(listorder, 0, false);
                itemClickListener1.onClick1(listorder, amount, false);
                if (Integer.parseInt(Holder.txtnumberorder.getText().toString()) == 0) {
                    Holder.lineorder.setVisibility(View.GONE);
                    Holder.imgnote.setVisibility(View.GONE);
                    Holder.btnorder.setVisibility(View.VISIBLE);
                }
            }
        });
        Holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener1.onClick1(listorder, amount, false);
                itemClickListener1.onClick1(datafood, i, true);
            }
        });
        if (amount == 0) {
            Holder.btnorder.setVisibility(View.VISIBLE);
            Holder.lineorder.setVisibility(View.GONE);
            Holder.imgnote.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datafood.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Button txtnamefood;
        TextView txtprice, txtcomment, txtlike, txtnumberorder, txtnamefood;
        Button btnorder;
        ImageView imgnote;
        ImageView imgfood;
        LinearLayout lineorder;
        ImageButton btnminusorder, imgaddorder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtnamefood = itemView.findViewById(R.id.txtnamefood);
            txtcomment = itemView.findViewById(R.id.txtcomment);
            txtlike = itemView.findViewById(R.id.txtlike);
            txtnumberorder = itemView.findViewById(R.id.txtnumberorder);
            btnorder = itemView.findViewById(R.id.btnorder);
            imgnote = itemView.findViewById(R.id.imgnote);
            lineorder = itemView.findViewById(R.id.lineorder);
            btnminusorder = itemView.findViewById(R.id.btnminusorder);
            imgaddorder = itemView.findViewById(R.id.imgaddorder);
            txtprice = itemView.findViewById(R.id.txtprice);
            imgfood = itemView.findViewById(R.id.image_food);
        }
    }

    public interface ItemClickListener1 {
        void onClick1(ArrayList<DataFood> dataFood, int position, boolean isLongClick);
    }
}

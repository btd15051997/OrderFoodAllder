package company.com.allder1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import company.com.allder1.Activity.AboutNowActivity;
import company.com.allder1.Activity.AddstoreActivity;
import company.com.allder1.Activity.FeedbackActivity;
import company.com.allder1.Activity.HistoryAccActivity;
import company.com.allder1.Activity.InviteFriendsActivity;
import company.com.allder1.Activity.MainActivity;
import company.com.allder1.Activity.MyFavoriteActivity;
import company.com.allder1.Activity.MyPromoCodeActivity;
import company.com.allder1.Activity.PaymentActivity;
import company.com.allder1.Activity.SettingsActivity;
import company.com.allder1.Activity.UserPolicyActivity;
import company.com.allder1.Activity.WalletActivity;
import company.com.allder1.Adapter.ItemAccountAdapter;
import company.com.allder1.R;
import company.com.allder1.model.ItemAccount;
import company.com.allder1.utils.PreferenceHelper;
import maes.tech.intentanim.CustomIntent;


public class AccountFragment extends Fragment {

    ArrayList<ItemAccount> listitem1;
    ArrayList<ItemAccount> listitem2;
    ArrayList<ItemAccount> listitem3;
    ItemAccountAdapter adapter1;
    ItemAccountAdapter adapter2;
    ItemAccountAdapter adapter3;
    RecyclerView listviewa, listviewb, listviewc;
    public static Button btntoconsumer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Mapped(view);
        addlist();
        if (new PreferenceHelper(getContext()).getSpecies().equals("consumer1")) {
            btntoconsumer.setVisibility(View.VISIBLE);
            btntoconsumer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PreferenceHelper(getActivity()).putSpecies("provider");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(getContext(), "fadein-to-fadeout");
                }
            });
        } else if (new PreferenceHelper(getContext()).getSpecies().equals("provider")) {
            btntoconsumer.setVisibility(View.VISIBLE);
            btntoconsumer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PreferenceHelper(getActivity()).putSpecies("consumer1");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(getContext(), "fadein-to-fadeout");
                }
            });
        }
        if (new PreferenceHelper(getContext()).getSpecies().equals("consumer")) {
            btntoconsumer.setVisibility(View.GONE);
        }
        return view;
    }

    private void addlist() {

        listitem1 = new ArrayList<>();
        listitem2 = new ArrayList<>();
        listitem3 = new ArrayList<>();

        addlistitem1();
        addlistitem2();
        addlistitem3();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listviewa.setLayoutManager(linearLayoutManager);
        adapter1 = new ItemAccountAdapter(getContext(), listitem1);
        listviewa.setAdapter(adapter1);


        LinearLayoutManager linearLayoutManage = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listviewb.setLayoutManager(linearLayoutManage);
        adapter2 = new ItemAccountAdapter(getContext(), listitem2);
        listviewb.setAdapter(adapter2);


        LinearLayoutManager linearLayoutMana = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listviewc.setLayoutManager(linearLayoutMana);
        adapter3 = new ItemAccountAdapter(getContext(), listitem3);
        listviewc.setAdapter(adapter3);
    }

    private void Mapped(View v) {
        listviewa = v.findViewById(R.id.listview1);
        btntoconsumer = v.findViewById(R.id.btntoconsumer);
        listviewb = v.findViewById(R.id.listview2);
        listviewc = v.findViewById(R.id.listview3);
    }

    private void addlistitem1() {
        Intent History = new Intent(getContext(), HistoryAccActivity.class);
        Intent Addstore = new Intent(getContext(), AddstoreActivity.class);
        Intent Favorite = new Intent(getContext(), MyFavoriteActivity.class);
        Intent promo = new Intent(getContext(), MyPromoCodeActivity.class);
        Intent Payment = new Intent(getContext(), PaymentActivity.class);
        Intent Wallet = new Intent(getContext(), WalletActivity.class);
        //Intent Order = new Intent(getContext(), OrderActivity.class);
        if (new PreferenceHelper(getActivity()).getSpecies().contains("provider")) {
            listitem1.add(new ItemAccount(Addstore,R.drawable.ic_namestore,"Store"));
            //  listitem1.add(new ItemAccount(Favorite, R.drawable.ic_myfavorite, "My Favorite"));
//            listitem1.add(new ItemAccount(promo, R.drawable.ic_my_promo_code, "My promo Code"));
            listitem1.add(new ItemAccount(Payment, R.drawable.ic_payment, "Payment"));
            listitem1.add(new ItemAccount(Wallet, R.drawable.ic_wallet, "Wallet"));
            //listitem1.add(new ItemAccount(Order, R.drawable.order, "Order"));
        } else {
            listitem1.add(new ItemAccount(History, R.drawable.ic_historyacc, "History"));
            listitem1.add(new ItemAccount(Favorite, R.drawable.ic_myfavorite, "My Favorites"));
            listitem1.add(new ItemAccount(promo, R.drawable.ic_my_promo_code, "My promotion code"));
            listitem1.add(new ItemAccount(Payment, R.drawable.ic_payment, "Payment"));
            listitem1.add(new ItemAccount(Wallet, R.drawable.ic_wallet, "Wallet"));
        }

        //  listitem1.add(new ItemAccount(Order, R.drawable.order, "Order"));
    }

    private void addlistitem2() {
        Intent InviteFriends = new Intent(getContext(), InviteFriendsActivity.class);
        Intent Feedback = new Intent(getContext(), FeedbackActivity.class);
        if (new PreferenceHelper(getActivity()).getSpecies().contains("provider")) {
            //  listitem2.add(new ItemAccount(Feedback, R.drawable.ic_feedback, "Feedback"));
        } else {
            listitem2.add(new ItemAccount(InviteFriends, R.drawable.ic_invite_friends, "Invited Friends"));
            listitem2.add(new ItemAccount(Feedback, R.drawable.ic_feedback, "Feedback"));
        }

    }

    private void addlistitem3() {
        Intent User_policy = new Intent(getContext(), UserPolicyActivity.class);
        Intent Settings = new Intent(getContext(), SettingsActivity.class);
        Intent About_Now = new Intent(getContext(), AboutNowActivity.class);
        listitem3.add(new ItemAccount(User_policy, R.drawable.ic_polocy, "User's Policy"));
        listitem3.add(new ItemAccount(Settings, R.drawable.ic_settings, "Settings"));
        listitem3.add(new ItemAccount(About_Now, R.drawable.ic_history, "About Now"));
    }

}

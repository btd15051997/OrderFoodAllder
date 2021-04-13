package company.com.allder1.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import company.com.allder1.Adapter.NotifiViewpagerAdapter;
import company.com.allder1.R;
import company.com.allder1.utils.PreferenceHelper;

public class HistoryOrderFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    String Species;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private Toolbar toolbar;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        Mapped(view);
        Species = new PreferenceHelper(getActivity()).getSpecies();
        ToolbarOderHistory();
        Taplayout();
        return view;
    }

    private void Taplayout() {

        tabLayout.setupWithViewPager(viewPager);
        if (Species.contains("consumer")) {
            NotifiViewpagerAdapter adapter = new NotifiViewpagerAdapter(getActivity().getSupportFragmentManager());
            // adapter.addfragment(new NewMenuFragment());
            adapter.addfragment(new OrderconsumerFragment(), "Order");
            adapter.addfragment(new HistoryConsumerFragment(), "History");
            // tabLayout.getTabAt(0).setText("New Food");
            viewPager.setAdapter(adapter);
         //   viewPager.setPageTransformer(true, new ScaleInOutTransformer(),viewPager.LAYER_TYPE_NONE);
            tabLayout.setupWithViewPager(viewPager);
            //tạo hiệu ứng
        } else if (Species.contains("provider")) {
            NotifiViewpagerAdapter adapter = new NotifiViewpagerAdapter(getActivity().getSupportFragmentManager());
            adapter.addfragment(new ComingOrderProviderFragment(), "Pending\n" + "Order");
            adapter.addfragment(new OrderProviderFragment(), "Order");
            adapter.addfragment(new HistoryFragment(), "History");
            //viewPager.setPageTransformer(true, new RotateDownTransformer());
            viewPager.setAdapter(adapter);
            //tạo hiệu ứng
            //viewPager.setZOrderOnTop()
          //  viewPager.setPageTransformer(true, new ScaleInOutTransformer(),viewPager.LAYER_TYPE_HARDWARE);
            tabLayout.setupWithViewPager(viewPager);

        }
    }

    private void Mapped(View v) {
        tabLayout = v.findViewById(R.id.tabs);
        viewPager = v.findViewById(R.id.container);
        toolbar = v.findViewById(R.id.toolbarhistory);
    }


    private void ToolbarOderHistory() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //  getActivity().setSupportActionBar(toolbarorder);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customViewh = inflater.inflate(R.layout.toobarorderhistory, null);
        actionBar.setCustomView(customViewh);
        toolbar = (Toolbar) customViewh.getParent();

        final TextView txtSearchbydate = customViewh.findViewById(R.id.txtSearchbydate);
        toolbar.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);
        txtSearchbydate.setText("show all");
        txtSearchbydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                txtSearchbydate.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, year, month, day);
                dialog.show();
            }
        });

    }
}

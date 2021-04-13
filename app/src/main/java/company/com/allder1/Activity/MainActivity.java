package company.com.allder1.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import company.com.allder1.R;
import company.com.allder1.fragment.AccountFragment;
import company.com.allder1.fragment.FoodFragment;
import company.com.allder1.fragment.HistoryOrderFragment;
import company.com.allder1.fragment.NotificationFragment;
import company.com.allder1.utils.PreferenceHelper;
import company.com.allder1.utils.bottonNavigationViewHelper;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragmnet = null;
    String Species;
    private FragmentManager fragmentManager;
    RelativeLayout layouttoobar;
    private Toolbar toolbarHome, toolbarorder;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        Intent intent = getIntent();
        String notification = intent.getStringExtra("aaa");
        if (notification != null) {
            layouttoobar.setVisibility(View.GONE);
            mReplaySearchHistoryfragment();
        } else {
            mReplayFoodfragment();
        }
        ToolbaHome();
        menunavigation();
        Species = new PreferenceHelper(this).getSpecies();

    }

    private void ToolbaHome() {
        toolbarHome = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbarHome);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customViewh = inflater.inflate(R.layout.toolbar_home, null);
        actionBar.setCustomView(customViewh);
        ImageButton scanqr = customViewh.findViewById(R.id.scanqr);
        ImageButton btnimgfilterHome = customViewh.findViewById(R.id.imgfilterHome);
        btnimgfilterHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);



            }
        });
        LinearLayout relative_toolbar_home = customViewh.findViewById(R.id.relative_toolbar_home);
        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanqrActivity.class));
            }
        });
        relative_toolbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this, "fadein-to-fadeout");
            }
        });
        toolbarHome = (Toolbar) customViewh.getParent();
        toolbarHome.setPadding(0, 0, 0, 0);
        toolbarHome.setContentInsetsAbsolute(0, 0);
    }

    private void Mapping() {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        layouttoobar = findViewById(R.id.layouttoolbar);
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() != R.id.main_food) {
            // selectedFragmnet = new FoodFragment();
            //  getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, selectedFragmnet).commit();
            mId = R.id.main_food;
            updateNavigationBarState(mId);
        } else if (bottomNavigationView.getSelectedItemId() == R.id.main_food) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    private void updateNavigationBarState(int id) {
        bottomNavigationView.setSelectedItemId(id);
    }

    private void menunavigation() {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        bottonNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
//        layouttoobar.setVisibility(View.VISIBLE);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_food:
                        mReplayFoodfragment();
                        layouttoobar.setVisibility(View.VISIBLE);
                        layouttoobar.setBackground(null);
                        break;
                    case R.id.notification:
                        mReplayNotificationfragment();
                        layouttoobar.setVisibility(View.GONE);
                        //ToolbarOderHistory();
                        break;
                    case R.id.search_history:
                        mReplaySearchHistoryfragment();
                        layouttoobar.setVisibility(View.GONE);
                        break;
                    case R.id.account:
                        layouttoobar.setVisibility(View.GONE);
                        mReplayaccountfragment();
                        break;
                }

                return true;
            }
        });
    }

    private void mReplayaccountfragment() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        AccountFragment accountfragment = new AccountFragment();
        if (fragmentManager.findFragmentByTag("account") != null) {
            //if the fragment exists, show it.
            transaction.show(fragmentManager.findFragmentByTag("account")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            transaction.add(R.id.main_container, accountfragment, "account")
                    .addToBackStack("account").commit();
        }
        if (fragmentManager.findFragmentByTag("fragmentfood") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fragmentfood")).commit();
        }
        if (fragmentManager.findFragmentByTag("fragmentnotification") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fragmentnotification")).commit();
        }
        if (fragmentManager.findFragmentByTag("SearchHistory") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("SearchHistory")).commit();
        }
    }

    private void mReplaySearchHistoryfragment() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        HistoryOrderFragment HistorySsearchfragment = new HistoryOrderFragment();

        if (fragmentManager.findFragmentByTag("SearchHistory") != null) {
            //if the fragment exists, show it.
            transaction.show(fragmentManager.findFragmentByTag("SearchHistory")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            transaction.add(R.id.main_container, HistorySsearchfragment, "SearchHistory")
                    .addToBackStack("SearchHistory").commit();
        }
        if (fragmentManager.findFragmentByTag("fragmentfood") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fragmentfood")).commit();
        }
        if (fragmentManager.findFragmentByTag("fragmentnotification") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fragmentnotification")).commit();
        }
        if (fragmentManager.findFragmentByTag("account") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("account")).commit();
        }
    }

    private void mReplayNotificationfragment() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        NotificationFragment Notificationfragment = new NotificationFragment();

        if (fragmentManager.findFragmentByTag("fragmentnotification") != null) {
            //if the fragment exists, show it.
            transaction.show(fragmentManager.findFragmentByTag("fragmentnotification")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            transaction.add(R.id.main_container, Notificationfragment, "fragmentnotification")
                    .addToBackStack("fragmentnotification").commit();
        }
        if (fragmentManager.findFragmentByTag("fragmentfood") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fragmentfood")).commit();
        }
        if (fragmentManager.findFragmentByTag("SearchHistory") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("SearchHistory")).commit();
        }
        if (fragmentManager.findFragmentByTag("account") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("account")).commit();
        }
    }

    private void mReplayFoodfragment() {
        layouttoobar.setVisibility(View.VISIBLE);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        FoodFragment Foodfragment = new FoodFragment();

        if (fragmentManager.findFragmentByTag("fragmentfood") != null) {
            //if the fragment exists, show it.
            transaction.show(fragmentManager.findFragmentByTag("fragmentfood")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            transaction.add(R.id.main_container, Foodfragment, "fragmentfood")
                    .addToBackStack("fragmentfood").commit();
        }
        if (fragmentManager.findFragmentByTag("fragmentnotification") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fragmentnotification")).commit();
        }
        if (fragmentManager.findFragmentByTag("SearchHistory") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("SearchHistory")).commit();
        }
        if (fragmentManager.findFragmentByTag("account") != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("account")).commit();
        }
    }
}

package company.com.allder1.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotifiViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private final  List<String> namefragmentList = new ArrayList<>();

    public NotifiViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addfragment(Fragment fragment ,String name) {
        fragmentList.add(fragment);
        namefragmentList.add(name);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return namefragmentList.get(position);
    }
}

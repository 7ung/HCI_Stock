package com.se215h12.hci_stock;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.se215h12.hci_stock.widgets.fragments.CommodityFragment;
import com.se215h12.hci_stock.widgets.fragments.OverViewIndexFragment;
import com.se215h12.hci_stock.util.Utils;
import com.se215h12.hci_stock.widgets.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static com.se215h12.hci_stock.widgets.fragments.OverViewIndexFragment.PAGE_NAME;

public class HomeActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Utils.loadAllStock(this, "data.json");
        } catch (Exception e) {
            Log.d("", e.getMessage());
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        super.initView();
        getToolBar().setTitle(getString(R.string.home_title));
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpViewPager(ViewPager vp)       // -> TOdo: should be go to abstract
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag("HOSE", OverViewIndexFragment.create(PAGE_NAME.HOSE));
        adapter.addFrag("HNX",  OverViewIndexFragment.create(PAGE_NAME.HNX));
        adapter.addFrag("Thế Giới", OverViewIndexFragment.create(PAGE_NAME.WORLD));
        adapter.addFrag("Hàng Hoá", new CommodityFragment());
        vp.setAdapter(adapter);
    }
    @Override
    protected void initEventListener() {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private List<Pair<String, Fragment>> _fragments = new ArrayList<>() ;
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return _fragments.get(position).second;
        }

        @Override
        public int getCount() {
            return _fragments.size();
        }

        public ViewPagerAdapter addFrag(String title, Fragment fragment) {
            _fragments.add(new Pair<String, Fragment>(title, fragment));
            return this;
        }

        public String getPageTitle(int position)
        {
            return _fragments.get(position).first;
        }
    }
}

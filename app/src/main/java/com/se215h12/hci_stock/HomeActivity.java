package com.se215h12.hci_stock;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.se215h12.hci_stock.data.Stock;
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
    private DrawerLayout drawLayout;
    private ListView menu;
    private Snackbar mSnackBar;


    private static boolean isOpened = false;

    public static ArrayList<Stock> _stock;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            _stock = Utils.loadAllStock(this, "data.json");
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
        drawLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu = (ListView) findViewById(R.id.left_menu);

        setUpLeftMenu(menu);
        setUpViewPager(viewPager);
        setUpDrawerLayout(drawLayout);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpened) {
//                    drawerToggle.onDrawerClosed(v);
                 drawLayout.closeDrawer(Gravity.LEFT);
                }else {
//                    drawerToggle.onDrawerOpened(v);
                    drawLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        showSnackBarSchedule();
    }


    @Override
    protected void onStart(){
        super.onStart();
    }



    private void setUpDrawerLayout(DrawerLayout drawLayout) {
        drawerToggle = new ActionBarDrawerToggle(this, drawLayout, getToolBar(),
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getActionBar().setTitle(mTitle);
                isOpened = false;
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActionBar().setTitle(mDrawerTitle);
                isOpened = false;
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawLayout.addDrawerListener(drawerToggle);
    }

    private void setUpLeftMenu(ListView listView) {
        String[] items = this.getResources().getStringArray(R.array.slide_menu_items);
        listView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.partial_drawer_list_item, R.id.tv_menu_item,items) {
            @Override
            public @NonNull View getView ( int position, @Nullable View convertView, @NonNull ViewGroup parent){
                View v = super.getView(position, convertView, parent);
                final int pos = position;
                ((TextView)v.findViewById(R.id.tv_menu_item)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewGroup vg = (ViewGroup) v.getParent();
                        if (vg.getChildCount() > 1)
                            clearSubMenu(vg);
                        else if (pos == 1) {
                            initSubMenu(vg, R.array.sub_menu_News);
                        } else if (pos == 2){
                            initSubMenu(vg, R.array.sub_menu_Filter);
                        }
                    }
                });
                return v;
            }

            private void initSubMenu(ViewGroup v, int sub_menu_news) {
                String[] subMenus = getResources().getStringArray(sub_menu_news);
                for (String sub : subMenus){
                    View subItem =
                        LayoutInflater.from(getContext()).inflate(R.layout.partial_drawer_list_item, null, false);
                    float density = getResources().getDisplayMetrics().density;
                    subItem.findViewById(R.id.tv_menu_item).setPadding(
                            (int) (48 * density), (int) (16 * density),0, (int) (14 *density));
                    ((TextView)subItem.findViewById(R.id.tv_menu_item)).setText(sub);
                    v.addView(subItem);
                }
            }

            private void clearSubMenu(ViewGroup v) {
                v.removeViews(1, v.getChildCount() - 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initMenu(menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    protected void setNavigationButton() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getToolBar().setNavigationIcon(R.drawable.ic_menu_black_24dp);
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

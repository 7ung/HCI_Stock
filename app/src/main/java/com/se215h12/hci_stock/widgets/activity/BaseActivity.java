package com.se215h12.hci_stock.widgets.activity;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.se215h12.hci_stock.HomeActivity;
import com.se215h12.hci_stock.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by TungHo on 10/26/2016.
 */
public abstract class BaseActivity  extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        initView();

        initEventListener();
    }

    protected void initView(){
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        setNavigationButton();
    }

    protected void initMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.SearchAutoComplete mSearchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);

        String[] test = new String[HomeActivity._stock.size()];
        for (int i = 0; i < HomeActivity._stock.size(); ++i){
            test[i] = HomeActivity._stock.get(i).getStockName()  + " - " + HomeActivity._stock.get(i).getCompanyName();
        }
        mSearchAutoComplete.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.search_suggest_list_item,
                R.id.tv_search, test){
            @Override
            public View getView(int position, View convertView, ViewGroup parrent){
                View v = super.getView(position,convertView, parrent);
//                ((TextView )v.findViewById(R.id.tv_search)).setText(test[position]);
                return v;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSearchAutoComplete.setElevation(8.0f);
        }
        mSearchAutoComplete.setThreshold(1);

        mSearchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // todo: go to stock detail
            }
        });
        searchView.setOnQueryTextListener(this);

    }

    protected abstract @LayoutRes int getLayoutResource();
    protected abstract void initEventListener();
    protected abstract void setNavigationButton();
    public Toolbar getToolBar() {
        return mToolBar;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    protected void showSnackbar(){
        String[] news = getResources().getStringArray(R.array.snack_bar_list);
        int p = (int) (Math.random() * (news.length));
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.main_layout), news[p],5000);
        snackBar.setAction("GO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }

    protected void showSnackBarSchedule(){
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        showSnackbar();
                    }
                }, 0, 5, TimeUnit.MINUTES);
    }
}

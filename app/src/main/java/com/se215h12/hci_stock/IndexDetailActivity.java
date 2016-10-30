package com.se215h12.hci_stock;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.se215h12.hci_stock.data.Stock;
import com.se215h12.hci_stock.util.Utils;
import com.se215h12.hci_stock.widgets.activity.BaseActivity;

public class IndexDetailActivity extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private AutoCompleteTextView mSearchAutoComplete;

    public static void create(Context context){
        Intent intent = new Intent(context, IndexDetailActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_index_detail;
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void initView(){
        super.initView();


    }

    private static String[] test;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);

        test = new String[HomeActivity._stock.size()];
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

        mSearchAutoComplete.setOnItemClickListener(this);
        searchView.setOnQueryTextListener(this);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(this.SEARCH_SERVICE);
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        mSearchAutoComplete.showDropDown();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
    }
}

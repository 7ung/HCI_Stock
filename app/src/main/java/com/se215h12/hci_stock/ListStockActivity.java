package com.se215h12.hci_stock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.se215h12.hci_stock.data.Stock;
import com.se215h12.hci_stock.util.Utils;
import com.se215h12.hci_stock.widgets.DialogStockMenu;
import com.se215h12.hci_stock.widgets.activity.BaseActivity;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by TungHo on 11/06/2016.
 */
public class ListStockActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;

    private String mName;

    private static final String KEY_BUNDLE_GROUP_NAME = "key_name";
    public static void create(Context context, String name){
        Intent intent = new Intent(context, ListStockActivity.class);
//        ((Activity)context).overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
        intent.putExtra(KEY_BUNDLE_GROUP_NAME, name);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();

        Bundle bundle = getIntent().getExtras();
        mName = bundle.getString(KEY_BUNDLE_GROUP_NAME, "");
        ((TextView) findViewById(R.id.tv_title)).setText(mName);

        mListView = (ListView) findViewById(R.id.listview);
        findViewById(R.id.tv_stock_name_header).setOnClickListener(this);
        findViewById(R.id.tv_price_ref_header).setOnClickListener(this);
        findViewById(R.id.tv_price_top_header).setOnClickListener(this);
        findViewById(R.id.tv_price_bot_header).setOnClickListener(this);
        findViewById(R.id.tv_price_header).setOnClickListener(this);
        findViewById(R.id.tv_change_header).setOnClickListener(this);
        sortByName();
        setAdapterToLV(mListView);


    }

    private void updateView(View v, Stock st) {

        ((TextView)v.findViewById(R.id.tv_stock_name)).setText(st.getStockName());
        ((TextView)v.findViewById(R.id.tv_price_ref)).setText(Utils.format(st.getRefPrice()));
        ((TextView)v.findViewById(R.id.tv_price_top)).setText(Utils.format(st.getMaxPrice()));
        ((TextView)v.findViewById(R.id.tv_price_bot)).setText(Utils.format(st.getMinPrice()));
        ((TextView)v.findViewById(R.id.tv_price)).setText(Utils.format(st.getPrice()));
        ((TextView)v.findViewById(R.id.tv_change)).setText(Utils.format(st.getChanged()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.trendColor_M(this, (TextView)v.findViewById(R.id.tv_stock_name), st.getChanged());
            Utils.trendColor_M(this, (TextView)v.findViewById(R.id.tv_price), st.getChanged());
            Utils.trendColor_M(this, (TextView)v.findViewById(R.id.tv_change), st.getChanged());
        }
        else {
            Utils.trendColor(this, (TextView)v.findViewById(R.id.tv_stock_name), st.getChanged());
            Utils.trendColor(this, (TextView)v.findViewById(R.id.tv_price), st.getChanged());
            Utils.trendColor(this, (TextView)v.findViewById(R.id.tv_change), st.getChanged());
        }

        if (st.getChanged() > 0){
            ((TextView)v.findViewById(R.id.tv_change)).setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_up_10dp,0
            );
        } else if (st.getChanged() == 0){
            ((TextView)v.findViewById(R.id.tv_change)).setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_unchange_10dp,0
            );
        } else {
            ((TextView)v.findViewById(R.id.tv_change)).setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_down_10dp,0
            );
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_list_stock;
    }

    @Override
    protected void initEventListener() {
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    private static int currentSort = 1;
    private void sortByName(){
        Collections.sort(HomeActivity._stock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int temp = o1.getStockName().compareTo(o2.getStockName());
                temp *= temp;
                return checkSort(currentSort, 1);
            }
        });
    }

    private int checkSort(int currentSort, int currentChecker) {
        if (currentSort == currentChecker){
            return -1;
        } else {
            return 1;
        }
    }

    private void sortByPriceRef(){
        Collections.sort(HomeActivity._stock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int temp =  (o1.getRefPrice() - o2.getRefPrice()) > 0 ? 1 : -1;
                if (currentSort < 0)
                    return -temp;
                return temp;
            }
        });
    }

    private void sortByPriceTop(){
        Collections.sort(HomeActivity._stock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int temp =   (o1.getMaxPrice() - o2.getMinPrice()) > 0 ? 1 : -1;
                if (currentSort < 0)
                    return -temp;
                return temp;
            }
        });
    }

    private void sortByPriceBottom(){
        Collections.sort(HomeActivity._stock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int temp = (o1.getMinPrice() - o2.getMinPrice()) > 0 ? 1 : -1;
                if (currentSort < 0)
                    return -temp;
                return temp;
            }
        });
    }

    private void sortByPrice(){
        Collections.sort(HomeActivity._stock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int temp = (o1.getPrice() - o2.getPrice()) > 0 ? 1 : -1;
                if (currentSort < 0)
                    return -temp;
                return temp;
            }
        });
    }

    private void sortByChange(){
        Collections.sort(HomeActivity._stock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int temp =  (o1.getChanged() - o2.getChanged()) > 0 ? 1 : -1;
                if (currentSort < 0)
                    return -temp;
                return temp;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int des;
        switch (v.getId()){
            case R.id.tv_stock_name_header:
                des = checkSort(currentSort, 1);
                currentSort = 1* des;
                sortByName();
                setAdapterToLV(mListView);
                break;
            case R.id.tv_price_ref_header:
                des = checkSort(currentSort, 2);
                currentSort = 2 * des;
                sortByPriceRef();
                setAdapterToLV(mListView);
                break;
            case R.id.tv_price_top_header:
                des = checkSort(currentSort, 3);
                currentSort = 3 * des;
                sortByPriceTop();
                setAdapterToLV(mListView);
                break;
            case R.id.tv_price_bot_header:
                des = checkSort(currentSort, 4);
                currentSort = 4 * des;
                sortByPriceBottom();
                setAdapterToLV(mListView);
                break;
            case R.id.tv_price_header:
                des = checkSort(currentSort, 5);
                currentSort = 5 * des;
                sortByPrice();
                setAdapterToLV(mListView);
                break;
            case R.id.tv_change_header:
                des = checkSort(currentSort, 6);
                currentSort = 6 * des;
                sortByChange();
                setAdapterToLV(mListView);
                break;
        }

    }

    private void setAdapterToLV(ListView lv) {
        lv.setAdapter(new ArrayAdapter<Stock>(
                this,
                R.layout.partial_list_stock_item,
                R.id.tv_price,
                HomeActivity._stock
        ){
            @Override
            public View getView(final int position, @Nullable View convertView,
                                @NonNull final ViewGroup parent) {
                final View v =  super.getView(position, convertView, parent);
                final Stock st = getItem(position);
                updateView(v, st);
                v.setLongClickable(true);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StockDetailActivity.create(ListStockActivity.this, st);

                    }
                });
                return v;
            }
        });
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DialogStockMenu dialog = new DialogStockMenu(ListStockActivity.this);
        int[] pos = new int[2];
        mListView.getLocationInWindow(pos);
        Rect rect = new Rect();
        view.getLocationOnScreen(pos);
        dialog.setPosition(0, pos[1] - view.getHeight());
        dialog.show();
        return true;
    }

    @Override
    protected void setNavigationButton(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        StockDetailActivity.create(this, (Stock) parent.getItemAtPosition(position));
    }
}

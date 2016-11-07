package com.se215h12.hci_stock;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.se215h12.hci_stock.data.Index;
import com.se215h12.hci_stock.util.Utils;
import com.se215h12.hci_stock.widgets.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class IndexDetailActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        AdapterView.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private TextView mTvPrice;
    private TextView mTvChanged;

    private TextView mTvValue;
    private TextView mTvVolume;
    private TextView mTvForeignBuyValue;
    private TextView mTvForeignBuyVolume;
    private TextView mTvForeignSellValue;
    private TextView mTvForeignSellVolume;

    private TextView mTvIncrease;
    private TextView mTvNotChanged;
    private TextView mTVDecrease;

    private Index index;

    private ImageView mIvDown;
    private RadioGroup groupRange;

    private static int DayNumber;
    private LineChart lineChart;

    private static final String KEY_BUNDLE_INDEX = "key_index";
    public static void create(Context context, Index index){
        Intent intent = new Intent(context, IndexDetailActivity.class);
        intent.putExtra(KEY_BUNDLE_INDEX, index);
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
        mIvDown.setOnClickListener(this);
        ((RadioButton) findViewById(R.id.rbtn_1d)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_3d)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_1w)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_1m)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_3m)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_6m)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_1y)).setOnCheckedChangeListener(this);
    }

    @Override
    protected void initView(){
        super.initView();

        Bundle bundle = getIntent().getExtras();
        index = bundle.getParcelable(KEY_BUNDLE_INDEX);

        ((TextView) findViewById(R.id.title)).setText(index.getName());
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvChanged = (TextView) findViewById(R.id.tv_changed);
        mTvVolume = (TextView) findViewById(R.id.tv_volume);
        mTvValue = (TextView) findViewById(R.id.tv_value);
        mTvForeignBuyValue = (TextView) findViewById(R.id.tv_foreign_buy_value);
        mTvForeignBuyVolume = (TextView) findViewById(R.id.tv_foreign_buy_volume);
        mTvForeignSellValue = (TextView) findViewById(R.id.tv_foreign_sell_value);
        mTvForeignSellVolume = (TextView) findViewById(R.id.tv_foreign_sell_volume);
        mTvIncrease = (TextView) findViewById(R.id.tv_increase);
        mTvNotChanged = (TextView) findViewById(R.id.tv_notChanged);
        mTVDecrease = (TextView) findViewById(R.id.tv_decrease);
        mIvDown = (ImageView) findViewById(R.id.iv_scroll_down);

        mTvPrice.setText(Utils.format(index.getPrice()));
        mTvVolume.setText(Utils.format(index.getVolume()));
        mTvValue.setText(Utils.format(index.getValue()));
        mTvForeignBuyValue.setText("KLGD: " + Utils.format(index.getForeignTrade().getInValue()));
        mTvForeignBuyVolume.setText("GTGD: " + Utils.format(index.getForeignTrade().getInVolumne()));
        mTvForeignSellValue.setText("KLGD: " + Utils.format(index.getForeignTrade().getOutValue()));
        mTvForeignSellVolume.setText("GTGD: " + Utils.format(index.getForeignTrade().getOutVolume()));
        mTvChanged.setText(Utils.format(index.getChangedValue()) + "\n" + "(" + Utils.format(index.getChangedRatio())+ "%)");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.trendColor_M(this, mTvChanged, index.getChangedValue());
            Utils.trendColor(this, mTvPrice, index.getChangedValue());
        }
        else {
            Utils.trendColor(this, mTvChanged, index.getChangedValue());
            Utils.trendColor(this, mTvPrice, index.getChangedValue());
        }

//        if (index.getChangedValue() > 0){
//            mTvChanged.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up_10dp, 0, 0, 0);
//        }  else if (index.getChangedValue() == 0){
//            mTvChanged.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unchange_10dp, 0, 0, 0);
//        } else {
//            mTvChanged.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down_10dp, 0, 0, 0);
//        }

        int notChange = (int) (200 * Math.random());
        int increase = (int) (200 * Math.random() + 200);
        int decrease = (int) (200 * Math.random() + 200);

        setWeight(mTvNotChanged, notChange, notChange + increase + decrease);
        setWeight(mTvIncrease, increase, notChange + increase + decrease);
        setWeight(mTVDecrease, decrease, notChange + increase + decrease);

        findViewById(R.id.layout_graph).setVisibility(View.GONE);

        groupRange = (RadioGroup) findViewById(R.id.rg_range);
        lineChart = (LineChart) findViewById(R.id.chart);

        DayNumber = getNumberDays(groupRange.getCheckedRadioButtonId());
        refreshChartByDays(DayNumber);
    }

    private static float last = 0.0f;
    private void refreshChartByDays(int days){
        last = this.index.getPrice();
        List<Entry> entries = new ArrayList<>();
        for (int i = 360 - days; i < 360; ++i){
            entries.add(new Entry(i, last = (float) (last + 200 * Math.random() -100)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(getResources().getColor(R.color.indexMore));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorAccent)); // styling, ...

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh

    }

    private int getNumberDays(@IdRes int checkedRadioButtonId) {
        switch (checkedRadioButtonId){
            case R.id.rbtn_1d:
                return CommodityDetailActivity.rangeDayNumber[0];
            case R.id.rbtn_3d:
                return CommodityDetailActivity.rangeDayNumber[1];
            case R.id.rbtn_1w:
                return CommodityDetailActivity.rangeDayNumber[2];
            case R.id.rbtn_1m:
                return CommodityDetailActivity.rangeDayNumber[3];
            case R.id.rbtn_3m:
                return CommodityDetailActivity.rangeDayNumber[4];
            case R.id.rbtn_6m:
                return CommodityDetailActivity.rangeDayNumber[5];
            case R.id.rbtn_1y:
                return CommodityDetailActivity.rangeDayNumber[6];
        }
        return 0;
    }

    private void setWeight(TextView mTvNotChanged, int weight, int total) {
        TableRow.LayoutParams lp = (TableRow.LayoutParams) mTvNotChanged.getLayoutParams();
        lp.weight = weight;
        mTvNotChanged.setLayoutParams(lp);
        mTvNotChanged.setText(weight + " " + "(" + Utils.format(((float)weight * 100)/ total)+ "%)");
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

    private static boolean isCollapse = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_scroll_down:
                final int[] counting = {0};
                Animation anim;
                Animation anim2;
                if (isCollapse == false) {
                    anim = AnimationUtils.loadAnimation(this, R.anim.slide_exit);
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.slide_exit);
                }
                else {
                    anim = AnimationUtils.loadAnimation(this, R.anim.slide_enter);
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.slide_enter);
                }
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        if (isCollapse == false) {
                            mIvDown.setScaleY(-1.0f);
                            findViewById(R.id.layout_graph).setVisibility(View.VISIBLE);
                        } else {
                            mIvDown.setScaleY(1.0f);
                            findViewById(R.id.layout_main_content).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (isCollapse == false) {
                            findViewById(R.id.layout_main_content).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.layout_graph).setVisibility(View.GONE);
                        }
                        isCollapse = !isCollapse;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                findViewById(R.id.layout_main_content).startAnimation(anim);
                findViewById(R.id.layout_graph).startAnimation(anim);
                mIvDown.startAnimation(anim);
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            DayNumber = getNumberDays(buttonView.getId());
            refreshChartByDays(DayNumber);

        }
    }
}

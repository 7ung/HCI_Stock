package com.se215h12.hci_stock;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.se215h12.hci_stock.data.Stock;
import com.se215h12.hci_stock.util.Utils;
import com.se215h12.hci_stock.widgets.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class StockDetailActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Stock mStock;
    private TextView mTitle;
    private static final String KEY_BUNDLE_STOCK = "key_stock";
    private TextView mSubTitle;
    private TextView mIndustry;
    private TextView mPrice;
    private TextView mChanged;
    private ViewGroup mLayoutMainPrice;

    private TextView mRefPrice;
    private TextView mOpenPrice;
    private TextView mTopPrice;
    private TextView mBottomPrice;
    private TextView mMinPrice;
    private TextView mMaxPrice;

    private ListView mLvNews;
    private RadioGroup groupRange;
    private LineChart lineChart;
    private CombinedChart combineChart;
    private ImageView mIvDown;

    public static void create(Context context, Stock stock){
        Intent intent = new Intent(context, StockDetailActivity.class);
        intent.putExtra(KEY_BUNDLE_STOCK, stock);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        mStock = bundle.getParcelable(KEY_BUNDLE_STOCK);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_stock_detail;
    }

    private static int DayNumber;

    @Override
    protected void initView() {
        super.initView();
        mTitle = (TextView) findViewById(R.id.title);
        mSubTitle = (TextView) findViewById(R.id.tv_sub_title);
        mIndustry = (TextView) findViewById(R.id.tv_industry);
        mPrice = (TextView) findViewById(R.id.tv_price);
        mChanged = (TextView) findViewById(R.id.tv_price_changed);
        mLayoutMainPrice = (ViewGroup) findViewById(R.id.layout_main_price);
        mRefPrice = (TextView) findViewById(R.id.tv_ref_price);
        mOpenPrice = (TextView) findViewById(R.id.tv_open_price);
        mTopPrice = (TextView) findViewById(R.id.tv_top_price);
        mBottomPrice = (TextView) findViewById(R.id.tv_bottom_price);
        mMinPrice = (TextView) findViewById(R.id.tv_min_price);
        mMaxPrice = (TextView) findViewById(R.id.tv_max_price);
        mLvNews = (ListView) findViewById(R.id.lv_news);
        mIvDown = (ImageView) findViewById(R.id.iv_scroll_down);

        mTitle.setText(mStock.getStockName());
        mSubTitle.setText(mStock.getCompanyName());
        mIndustry.setText(mStock.getIndustry());
        mPrice.setText(Utils.format(mStock.getPrice()));
        mChanged.setText(Utils.format(mStock.getChanged()) + " ("+ Utils.format(mStock.getChangedRatio()) + "%)" ) ;
        if (mStock.getChanged() > 0) {
            mLayoutMainPrice.setBackgroundResource(R.color.indexMore);
        } else if (mStock.getChanged() < 0){
            mLayoutMainPrice.setBackgroundResource(R.color.indexLess);
        } else {
            mLayoutMainPrice.setBackgroundResource(R.color.indexEqual);
        }

        mRefPrice.setText(Utils.format(mStock.getRefPrice()));
        mOpenPrice.setText(Utils.format(mStock.getOpenedPrice()));
        mMaxPrice.setText(Utils.format(mStock.getMaxPrice()));
        mMinPrice.setText(Utils.format(mStock.getMinPrice()));

        mTopPrice.setText(Utils.format((float) (mStock.getRefPrice() * 1.1)));
        mBottomPrice.setText(Utils.format((float) (mStock.getRefPrice() * 0.9)));

//        mMaxPrice.setText(Utils.format((float) ((mStock.getMaxPrice() - mStock.getPrice()) * Math.random() + mStock.getPrice())));
//        mMinPrice.setText(Utils.format((float) ((- mStock.getMinPrice() + mStock.getPrice()) * Math.random() + mStock.getMinPrice())));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.trendColor_M(this, mOpenPrice, mStock.getChanged());
            Utils.trendColor_M(this, mMinPrice, mStock.getMinPrice());
            Utils.trendColor_M(this, mMaxPrice, mStock.getMaxPrice());
        }
        else {
            Utils.trendColor(this, mOpenPrice,mStock.getChanged());
            Utils.trendColor(this, mMinPrice, mStock.getMinPrice());
            Utils.trendColor(this, mMaxPrice, mStock.getMaxPrice());
        }

        String[] news = getResources().getStringArray(R.array.news);
        
        mLvNews.setAdapter(new ArrayAdapter<String>(this, R.layout.partial_layout_news_list_item, news));
        findViewById(R.id.layout_graph).setVisibility(View.GONE);

        groupRange = (RadioGroup) findViewById(R.id.rg_range);
        lineChart = (LineChart) findViewById(R.id.chart);
        lineChart.setVisibility(View.GONE);
        combineChart = (CombinedChart) findViewById(R.id.combine_chart);
        combineChart.setVisibility(View.VISIBLE);

        DayNumber = getNumberDays(groupRange.getCheckedRadioButtonId());
        refreshChartByDays(DayNumber);
    }

    private void refreshChartByDays(int days){
        float last = mStock.getPrice();
        List<Entry> entries = new ArrayList<>();
        for (int i = 360 - days; i < 360; ++i){
            entries.add(new Entry(i, last = (float) (last + 20 * Math.random() -10)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price"); // add entries to dataset
        dataSet.setColor(getResources().getColor(R.color.indexMore));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorAccent)); // styling, ...

        LineData lineData = new LineData(dataSet);
//        lineChart.setData(lineData);
//        lineChart.invalidate(); // refresh

        // column
        last = mStock.getVolume() / 2000;
        List<BarEntry> columnEntries = new ArrayList<>();
        for (int i = 360 - days; i < 360; ++i){
            columnEntries.add(new BarEntry(i, Math.max(last = (float) (last + 200 * Math.random() -100),10)  ));
        }

        BarDataSet bardataset = new BarDataSet(columnEntries, "Volume");
        bardataset.setColor(getResources().getColor(R.color.colorAccentDark));
        bardataset.setValueTextColor(getResources().getColor(R.color.colorAccentDark)); // styling, ...


        CombinedData combinedata = new CombinedData();
        combinedata.setData(lineData);

        BarData bardata = new BarData(bardataset);
        combinedata.setData(bardata);

        combineChart.setData(combinedata);
//        combineChart.set(bardata.getYMax());
        YAxis y = combineChart.getAxisRight();
        y.setAxisMaximum(bardata.getYMax());
        combineChart.invalidate();

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

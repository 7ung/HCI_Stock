package com.se215h12.hci_stock;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.se215h12.hci_stock.data.Commodity;
import com.se215h12.hci_stock.util.Utils;
import com.se215h12.hci_stock.widgets.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class CommodityDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private LineChart lineChart;

    private Commodity[] commodities;

    private String commodityName;

    private TextView title;
    private RadioGroup groupRange;
    private TextView price;
    private TextView chnaged;
    private TextView max;
    private TextView min;

    private static final int[] rangeDayNumber = new int[]{
            24, 72, 7, 30, 90, 180, 360
    };

    private static int DayNumber;

    // tên mặt hàng
    // giá hôm nay
    // đơn vị tính.
    // thay đổi hôm nay
    //

    // biểu đồ
    // chọn khoản thời gian:
    // 1 ngày: 24 mốc
    // 3 ngày: 72 mốc
    // 7 ngày: 7 mốc
    // 30 ngày: 30 mốc
    // 3 month:  90 mốc
    // 6 tháng: 180 mốc
    // 1 year: 360 mốc.


    private static final String KEY_COMMODITY_NAME = "key_name";

    public static void create(Context context, String name){
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(KEY_COMMODITY_NAME, name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = this.getIntent().getExtras();
        commodityName = bundle.getString(KEY_COMMODITY_NAME, "");

        commodities = new Commodity[360];

        for (int i = 0; i < 360; ++i){
            commodities[i] = Commodity.create(commodityName);
            if (i != 0){
                commodities[i].setPrice(commodities[i - 1].getPrice() + commodities[i - 1].getChanged());
            }
        }

        super.onCreate(savedInstanceState);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void initView(){
        super.initView();

        title = (TextView) findViewById(R.id.tv_title);
        groupRange = (RadioGroup) findViewById(R.id.rg_range);
        lineChart = (LineChart) findViewById(R.id.chart);
        price = (TextView)findViewById(R.id.tv_price);
        chnaged = (TextView) findViewById(R.id.tv_changed);
        max = (TextView) findViewById(R.id.tv_max);
        min = (TextView) findViewById(R.id.tv_min);

        DayNumber = getNumberDays(groupRange.getCheckedRadioButtonId());
        refreshChartByDays(DayNumber);
        refreshViewData();

        groupRange.getCheckedRadioButtonId();
        title.setText(commodityName);
    }

    private void refreshChartByDays(int days){

        List<Entry> entries = new ArrayList<>();
        for (int i = 360 - days; i < 360; ++i){
            entries.add(new Entry(i, commodities[i].getPrice()));
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
                return rangeDayNumber[0];
            case R.id.rbtn_3d:
                return rangeDayNumber[1];
            case R.id.rbtn_1w:
                return rangeDayNumber[2];
            case R.id.rbtn_1m:
                return rangeDayNumber[3];
            case R.id.rbtn_3m:
                return rangeDayNumber[4];
            case R.id.rbtn_6m:
                return rangeDayNumber[5];
            case R.id.rbtn_1y:
                return rangeDayNumber[6];
        }
        return 0;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_commodity_detail;
    }

    @Override
    protected void initEventListener() {

        ((RadioButton) findViewById(R.id.rbtn_1d)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_3d)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_1w)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_1m)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_3m)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_6m)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rbtn_1y)).setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            DayNumber = getNumberDays(buttonView.getId());
            refreshChartByDays(DayNumber);
            refreshViewData();

        }
    }

    private void refreshViewData() {
        price.setText(Utils.format(commodities[359].getPrice()));
        chnaged.setText(Utils.format(commodities[359].getPrice() - commodities[358].getPrice()));
        max.setText(Utils.format(lineChart.getData().getYMax()));
        min.setText(Utils.format(lineChart.getData().getYMin()));

    }
}

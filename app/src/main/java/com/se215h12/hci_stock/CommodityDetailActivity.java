package com.se215h12.hci_stock;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.View;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class CommodityDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private LineChart lineChart;

    private Commodity[] commodities;

    private String commodityName;

    private TextView title;
    private RadioGroup groupRange;
    private RadioGroup groupUnit;
    private TextView price;
    private TextView chnaged;
    private TextView max;
    private TextView min;

    private static final int[] rangeDayNumber = new int[]{
            24, 72, 7, 30, 90, 180, 360
    };

    private static int DayNumber;

    private static ArrayList<Pair<String, Float>> _unitRatio = new ArrayList<>();

    private static final String KEY_COMMODITY_NAME = "key_name";

    public static void create(Context context, String name){

        _unitRatio = createUnitRatio(name);

        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(KEY_COMMODITY_NAME, name);
        context.startActivity(intent);
    }

    private static ArrayList<Pair<String, Float>> createUnitRatio(String name) {
        ArrayList<Pair<String, Float>> _units =  new ArrayList<>();
        if (TextUtils.equals(name, "gold")
                || TextUtils.equals(name, "silver")){
            _units.add(new Pair<>("USD /oz", 1.0f));
            _units.add(new Pair<>("USD /kg", 32.15f));
            _units.add(new Pair<>("Tr đ/chỉ", (float) (0.12  * 22222 / 1000000)));
        } else if (TextUtils.equals(name, "oil")) {
            _units.add(new Pair<>("USD /barrel", 1.0f));
            _units.add(new Pair<>("USD /lít", (float) (1 / 119.24)));
            _units.add(new Pair<>("Đồng /lít", (float) ( (1 / 119.24) * 22222)));
        } else if (TextUtils.equals(name, "gas")) {
            _units.add(new Pair<>("USD /GigaJun", 1.0f));
            _units.add(new Pair<>("USD /MWh", (float) (1000 / 277.77)));
            _units.add(new Pair<>("Đồng /MWh", (float)( (1000 / 277.77) * 22222)));
        } else if (TextUtils.equals(name, "coffee")
                || TextUtils.equals(name, "sugar")
                || TextUtils.equals(name, "corn")) {
            _units.add(new Pair<>("USD /Pound", 1.0f));
            _units.add(new Pair<>("USD /Kg", (float) (1.0f / 0.4536)));
            _units.add(new Pair<>("Đồng /Kg", (float) (1.0f / 0.4536 * 222222)));
        }  else if (TextUtils.equals(name, "usd")
                || TextUtils.equals(name, "eur")
                || TextUtils.equals(name, "pound")
                || TextUtils.equals(name, "sgd")
                || TextUtils.equals(name, "hkd")){
            _units.add(new Pair<>("Đồng /"+name.toUpperCase(), 1.0f));
        } else {
            _units.add(new Pair<>("% /tháng", 1.0f));
        }
        return _units;
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
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title = (TextView) findViewById(R.id.tv_title);
        groupRange = (RadioGroup) findViewById(R.id.rg_range);
        groupUnit = (RadioGroup) findViewById(R.id.rg_unit);
        lineChart = (LineChart) findViewById(R.id.chart);
        price = (TextView)findViewById(R.id.tv_price);
        chnaged = (TextView) findViewById(R.id.tv_changed);
        max = (TextView) findViewById(R.id.tv_max);
        min = (TextView) findViewById(R.id.tv_min);

        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            for (int i = 0; i < _unitRatio.size(); ++i){
                ((RadioButton)groupUnit.getChildAt(i)).setText(_unitRatio.get(i).first);
                ((RadioButton)groupUnit.getChildAt(i)).setOnCheckedChangeListener(
                        new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                refreshChartByDays(DayNumber);
                                refreshViewData();
                            }
                        });
                ((RadioButton)groupUnit.getChildAt(i)).setVisibility(View.VISIBLE);
                if (_unitRatio.size() > 1){
                    groupUnit.getChildAt(i).setEnabled(true);
                }
            }

        DayNumber = getNumberDays(groupRange.getCheckedRadioButtonId());
        refreshChartByDays(DayNumber);
        refreshViewData();

        groupRange.getCheckedRadioButtonId();
        title.setText(commodityName);
    }

    private void refreshChartByDays(int days){

        float ratio = getUnitRatio(groupUnit.getCheckedRadioButtonId());
        List<Entry> entries = new ArrayList<>();
        for (int i = 360 - days; i < 360; ++i){
            entries.add(new Entry(i, commodities[i].getPrice() * ratio));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(getResources().getColor(R.color.indexMore));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorAccent)); // styling, ...

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh

    }

    private float getUnitRatio(int checkedRadioButtonId) {
        switch (checkedRadioButtonId){
            case R.id.rbtb_unit_1:
                return _unitRatio.get(0).second;
            case R.id.rbtb_unit_2:
                return _unitRatio.get(1).second;
            case R.id.rbtb_unit_3:
                return _unitRatio.get(2).second;
        }
        return 1.0f;
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

package com.se215h12.hci_stock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.se215h12.hci_stock.widgets.activity.BaseActivity;

public class StockDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_stock_detail;
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void setNavigationButton() {

    }
}

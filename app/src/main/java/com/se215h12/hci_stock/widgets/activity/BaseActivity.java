package com.se215h12.hci_stock.widgets.activity;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.se215h12.hci_stock.R;

/**
 * Created by TungHo on 10/26/2016.
 */
public abstract class BaseActivity  extends AppCompatActivity {

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        initView();

        initEventListener();
    }

    protected void initView()
    {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
    }

    protected abstract @LayoutRes int getLayoutResource();
    protected abstract void initEventListener();

    public Toolbar getToolBar() {
        return mToolBar;
    }

}

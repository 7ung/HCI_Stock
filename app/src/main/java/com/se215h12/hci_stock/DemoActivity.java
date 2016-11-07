package com.se215h12.hci_stock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.se215h12.hci_stock.widgets.activity.BaseActivity;

/**
 * Created by TungHo on 11/06/2016.
 */
public class DemoActivity extends BaseActivity {

    public static void create(Context context){
        Intent intent = new Intent(context, DemoActivity.class);
//        ((Activity)context).overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_index_detail_graph;
    }

    @Override
    protected void initEventListener() {

    }
}

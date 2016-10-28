package com.se215h12.hci_stock.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.se215h12.hci_stock.R;
import com.se215h12.hci_stock.data.Commodity;
import com.se215h12.hci_stock.util.Utils;

/**
 * Created by TungHo on 10/28/2016.
 */
public class CommodityItem extends RelativeLayout{

    private Commodity commodity;
    private TextView price;
    private TextView change;

    private ViewGroup parent;
    public CommodityItem(Context context, ViewGroup vg) {
        super(context);
        initLayout(context);
        parent = vg;

    }

    public CommodityItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);

    }

    public CommodityItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommodityItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayout(context);
    }

    private void initLayout(Context context){
        View v = LayoutInflater.from(context).inflate(R.layout.commodity_grid_item, parent, false);
//        ViewGroup.LayoutParams lp = v.getLayoutParams();
//        lp.height = lp.width;
//        v.setLayoutParams(lp);
        price = (TextView) v.findViewById(R.id.tv_price);
        change = (TextView) v.findViewById(R.id.tv_changed);




    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }

    public void setCommodity(Commodity commodity){
        this.commodity = commodity;
        initName(commodity);
        initChange(commodity);
    }


    private void initName(Commodity commodity) {
        price.setText(String.valueOf(commodity.getPrice()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Utils.trendColor_M(getContext(), price, commodity.getChanged());
        else
            Utils.trendColor(getContext(), price, commodity.getChanged());
    }

    private void initChange( Commodity commodity) {
        change.setText(String.valueOf(commodity.getChanged()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Utils.trendColor_M(getContext(), change, commodity.getChanged());
        else
            Utils.trendColor(getContext(), change, commodity.getChanged());
    }
}

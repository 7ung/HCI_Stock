package com.se215h12.hci_stock.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.se215h12.hci_stock.R;
import com.se215h12.hci_stock.data.Commodity;
import com.se215h12.hci_stock.util.Utils;

/**
 * Created by TungHo on 10/28/2016.
 */
public class GroupCommodityItem  extends LinearLayout implements View.OnClickListener {

    private static final String TAG = GroupCommodityItem.class.getSimpleName();
    private TextView header;
    private ExpandableHeightGridView gridView;
    private ImageView expandmore;
    private ImageView expandless;

    private Commodity[] commodities;

    public GroupCommodityItem(Context context) {
        super(context);
        try {
            initLayout(context, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        initEventListener();
    }

    public GroupCommodityItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            initLayout(context, attrs);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }        initEventListener();

    }

    public GroupCommodityItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            initLayout(context, attrs);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        initEventListener();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GroupCommodityItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        try {
            initLayout(context, attrs);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        initEventListener();
    }

    private void initLayout(Context context, AttributeSet atrs) throws Exception {
        View v = LayoutInflater.from(context).inflate(R.layout.widget_group_commity, this, true);gridView = (ExpandableHeightGridView) v.findViewById(R.id.gridView);
        gridView.setExpanded(true);

        expandmore = (ImageView) v.findViewById(R.id.ib_expand_more);
        expandless = (ImageView) v.findViewById(R.id.ib_expand_less);
        header = (TextView) findViewById(R.id.tv_commdity_group);

        TypedArray typedArray =
                this.getContext().obtainStyledAttributes(atrs, R.styleable.GroupCommodityItem, 0, 0);

        int iconId = typedArray.getResourceId(R.styleable.GroupCommodityItem_icon, 0);
        int listArray = typedArray.getResourceId(R.styleable.GroupCommodityItem_items, 0);
        String headerTitle = typedArray.getString(R.styleable.GroupCommodityItem_name);
        int nameRes = typedArray.getResourceId(R.styleable.GroupCommodityItem_nameRes, 0);

        if (headerTitle != null && nameRes == 0)
            throw new Exception("Not allow declare attr:name and attr:nameRes at the same time");
        if (iconId != 0){
            header.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
        }
        if (headerTitle != null){
            header.setText(headerTitle);
        } else if (nameRes != 0){
            header.setText(getContext().getResources().getString(nameRes));
        }
        if (listArray != 0){
            String[] ws = getContext().getResources().getStringArray(listArray);
            commodities = new Commodity[ws.length];
            for (int i = 0; i < ws.length; ++i){
                commodities[i] = Commodity.create(ws[i]);
            }
        }

        if (commodities != null && commodities.length != 0)
            gridView.setAdapter(new GridViewAdapter(
                    this.getContext(),
                    R.layout.widget_commodity_grid_item,        // ?
                    R.id.tv_price,                              // ?
                    commodities));                          // ?
    }

    private void initEventListener() {

        expandmore.setOnClickListener(this);
        expandless.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        TranslateAnimation anim = null;

        switch (v.getId()){
            case R.id.ib_expand_less:
                //                gridView.startAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.collapse_anim));
                anim = new TranslateAnimation(0.0f, 0.0f, 0.0f,- gridView.getMeasuredHeight());
                anim.setDuration(400);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        gridView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                expandmore.setVisibility(View.VISIBLE);
                expandless.setVisibility(View.GONE);
                gridView.startAnimation(anim);
                break;
            case R.id.ib_expand_more:
                anim = new TranslateAnimation(0.0f, 0.0f, - gridView.getMeasuredHeight(), 0.0f);
                anim.setDuration(400);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                expandmore.setVisibility(View.GONE);
                expandless.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.VISIBLE);
                gridView.startAnimation(anim);

                break;
        }

    }


    private class GridViewAdapter extends ArrayAdapter<Commodity> {

        public GridViewAdapter(Context context, @LayoutRes int resource, int textViewResourceId, Commodity[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent)
        {
            View v = super.getView(position, convertView, parent);
            Commodity commodity = getItem(position);

            initName(v, commodity);
            initChange(v, commodity);
            int w = GroupCommodityItem.this.gridView.getColumnWidth();
            v.setLayoutParams(new ViewGroup.LayoutParams(w, w));
            v.findViewById(R.id.iv_commodity_avt).setBackgroundResource(commodity.getImage());
            return v;
        }

        private void initName(View v, Commodity commodity) {
            TextView tv = (TextView) v.findViewById(R.id.tv_price);
            tv.setText(Utils.format(commodity.getPrice()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Utils.trendColor_M(getContext(), tv, commodity.getChanged());
            else
                Utils.trendColor(getContext(), tv, commodity.getChanged());
        }

        private void initChange(View v, Commodity commodity) {
            TextView tv = (TextView) v.findViewById(R.id.tv_changed);
            tv.setText(Utils.format(commodity.getChanged()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Utils.trendColor_M(getContext(), tv, commodity.getChanged());
            else {
                Utils.trendColor(getContext(), tv, commodity.getChanged());
            }

            if (commodity.getChanged() > 0){
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_10dp, 0);
            }  else if (commodity.getChanged() == 0){
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_unchange_10dp, 0);
            } else if (commodity.getChanged() < 0){
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_10dp, 0);
            }
        }

    }
}

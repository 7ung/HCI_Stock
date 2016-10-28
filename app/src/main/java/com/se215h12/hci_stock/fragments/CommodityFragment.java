package com.se215h12.hci_stock.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.se215h12.hci_stock.R;
import com.se215h12.hci_stock.StockHCIApplication;
import com.se215h12.hci_stock.data.Commodity;
import com.se215h12.hci_stock.data.Index;
import com.se215h12.hci_stock.util.Utils;
import com.se215h12.hci_stock.widgets.CommodityItem;

/**
 * Created by TungHo on 10/27/2016.
 */
public class CommodityFragment extends Fragment {

    private GridView gridView;

    public CommodityFragment()
    {

    }
    public static CommodityFragment create() {
        CommodityFragment fragment = new CommodityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_commodity, container, false);
        gridView = (GridView) v.findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(
                this.getContext(),
                R.layout.commodity_grid_item,
                R.id.tv_price,
                Commodity.woods));
        return v;
    }


    private class GridViewAdapter extends ArrayAdapter<Commodity>{

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
            int w = CommodityFragment.this.gridView.getColumnWidth();
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
            else
                Utils.trendColor(getContext(), tv, commodity.getChanged());
        }

    }
}

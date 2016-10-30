package com.se215h12.hci_stock.widgets.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.se215h12.hci_stock.IndexDetailActivity;
import com.se215h12.hci_stock.R;
import com.se215h12.hci_stock.data.Index;
import com.se215h12.hci_stock.util.Utils;

import junit.framework.Assert;

public class OverViewIndexFragment extends Fragment {

    public enum PAGE_NAME{
        HOSE,
        HNX,
        WORLD
    }

    private final static String KEY_PAGENAME = "pagename";

    private ListView listView;
    private ViewGroup header;

    private PAGE_NAME pageName;

    public OverViewIndexFragment() {
        // Required empty public constructor
    }

    public static OverViewIndexFragment create(PAGE_NAME pageName) {
        OverViewIndexFragment fragment = new OverViewIndexFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PAGENAME, pageName.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String pagename = getArguments().getString(KEY_PAGENAME);
            if (pagename  == PAGE_NAME.HOSE.toString()){
                pageName = PAGE_NAME.HOSE;
            }else if (pagename  == PAGE_NAME.HNX.toString()){
                pageName = PAGE_NAME.HNX;
            }else if (pagename  == PAGE_NAME.WORLD.toString()){
                pageName = PAGE_NAME.WORLD;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_over_view_index, container, false);
        header = (ViewGroup) v.findViewById(R.id.listheader);
        listView = (ListView) v.findViewById(R.id.listview);

        Index[] indexes = null;
        switch (pageName)
        {
            case HOSE:
                indexes = Index.hoses;
                break;
            case HNX:
                indexes = Index.hnx;
                break;
            case WORLD:
                indexes = Index.world;
            break;
        }
        Assert.assertNotNull(indexes);
        listView.setAdapter(new IndexViewAdapter(
                this.getContext(),
                R.layout.partial_overview_index_listitem,
                R.id.tv_stock_name,
                indexes));

//        if (StockHCIApplication.getInstance().getPreferences().isShowVolume())
//            header.findViewById(R.id.tv_value_header).setVisibility(View.GONE);
//        else
//            header.findViewById(R.id.tv_volume_header).setVisibility(View.GONE);

        return v;
    }

    private void initSubtitle(TextView tv) {
        switch (pageName)
        {
            case HOSE:
                tv.setText(getContext().getString(R.string.hose_subTitle));
                break;
            case HNX:
                tv.setText(getContext().getString(R.string.hnx_subTitle));
                break;
            case WORLD:
                tv.setText(getContext().getString(R.string.world_subTitle));
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ViewGroup getHeader()
    {
        return header;
    }

    public ListView getListView()
    {
        return listView;
    }

    private class IndexViewAdapter extends ArrayAdapter<Index>
    {

        public IndexViewAdapter(Context context, int resource, int textViewResourceId, Index[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent)
        {
            View v = super.getView(position, convertView, parent);
            Index index = this.getItem(position);

            initIndexName(v, index);
            initIndexPrice(v, index);
            initIndexChanged(v, index);

            TextView value = (TextView) v.findViewById(R.id.tv_value);
            value.setText( Utils.format(index.getValue()));

            TextView volume = (TextView) v.findViewById(R.id.tv_volume);
            volume.setText( Utils.format(index.getVolume()));


            //todo: test
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IndexDetailActivity.create(getContext());
                }
            });

//            if (StockHCIApplication.getInstance().getPreferences().isShowVolume())
//                value.setVisibility(View.GONE);
//            else
//                volume.setVisibility(View.GONE);

            return v;
        }


        private void initIndexPrice(View v, Index index) {
            TextView tv = (TextView) v.findViewById(R.id.tv_price);
            tv.setText(Utils.format(index.getPrice()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Utils.trendColor_M(getContext(), tv, index.getChangedValue());
            else
                Utils.trendColor(getContext(), tv, index.getChangedValue());
        }
        private void initIndexChanged(View v, Index index) {
            TextView tv = (TextView) v.findViewById(R.id.tv_price_changed);
            tv.setText(Utils.format( index.getChangedValue()) + "\n(" +
                    Utils.format( index.getChangedRatio()) +  " %)");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Utils.trendColor_M(getContext(), tv, index.getChangedValue());
            else
                Utils.trendColor(getContext(), tv, index.getChangedValue());

            if (index.getChangedValue() > 0) {
                v.setBackgroundResource(R.drawable.ripple_green);
            } else if (index.getChangedValue() == 0) {
                v.setBackgroundResource(R.drawable.ripple_yellow);
            } else if (index.getChangedValue() < 0) {
                v.setBackgroundResource(R.drawable.ripple_red);
            }

        }
        private void initIndexName(View v, Index index) {
            TextView tv = (TextView) v.findViewById(R.id.tv_stock_name);
            tv.setText(index.getName());
        }



    }

}

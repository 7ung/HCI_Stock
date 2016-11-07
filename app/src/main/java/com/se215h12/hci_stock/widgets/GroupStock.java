package com.se215h12.hci_stock.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.se215h12.hci_stock.R;
import com.se215h12.hci_stock.util.Utils;

/**
 * Created by TungHo on 11/05/2016.
 */
public class GroupStock extends RelativeLayout {

    private TextView mAllButton;
    private RecyclerView mListItems;

    public GroupStock(Context context) {
        super(context);
        initLayout(context, null);
        initEventListener();
    }

    public GroupStock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
        initEventListener();
    }

    public GroupStock(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs);
        initEventListener();
    }

    private void initEventListener() {
        mAllButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: go to all stock page;
                Toast.makeText(getContext(),"go to all stock page",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initLayout(Context context, AttributeSet attrs) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(context)
                .inflate(R.layout.widget_group_stock,this, true);
        mAllButton = (TextView) v.findViewById(R.id.tv_all);
        mListItems = (RecyclerView) v.findViewById(R.id.rv_group_stocks);

        initRecycleView(mListItems);
    }

    private void initRecycleView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        adapter.setDatas(com.se215h12.hci_stock.data.GroupStock.create());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        private com.se215h12.hci_stock.data.GroupStock[] datas;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.widget_card_group_stock, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((TextView)holder.mView.findViewById(R.id.tv_group_name)).setText(getDatas()[position].getName());
            TextView tvChanged = (TextView)holder.mView.findViewById(R.id.tv_changed);
            tvChanged.setText(Utils.format(getDatas()[position].getChanged()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Utils.trendColor_M(getContext(), tvChanged, getDatas()[position].getChanged());
            else {
                Utils.trendColor(getContext(), tvChanged, getDatas()[position].getChanged());
            }

            if ( getDatas()[position].getChanged() > 0){
                tvChanged.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up_10dp, 0, 0, 0);
            }  else if (getDatas()[position].getChanged() == 0){
                tvChanged.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unchange_10dp, 0, 0, 0);
            } else {
                tvChanged.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down_10dp, 0, 0, 0);
            }

            if (datas[position].getImage() != 0){
                ((ImageView)holder.mView.findViewById(R.id.iv_picture))
                        .setImageResource(datas[position].getImage());
                ((ImageView)holder.mView.findViewById(R.id.iv_picture)).setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        @Override
        public int getItemCount() {
            return datas.length;
        }

        public com.se215h12.hci_stock.data.GroupStock[] getDatas() {
            return datas;
        }

        public void setDatas(com.se215h12.hci_stock.data.GroupStock[] datas) {
            this.datas = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public CardView mView;
            public ViewHolder(CardView v) {
                super(v);
                mView = v;
            }
        }
    }
}

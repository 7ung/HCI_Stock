package com.se215h12.hci_stock.widgets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;

import com.se215h12.hci_stock.R;
import com.se215h12.hci_stock.data.Commodity;
import com.se215h12.hci_stock.widgets.GroupCommodityItem;

/**
 * Created by TungHo on 10/27/2016.
 */
public class CommodityFragment extends Fragment implements View.OnClickListener {

    private GridView gridView;
    private ImageView expandmore;
    private ImageView expandless;
    private View root;
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
        root = inflater.inflate(R.layout.fragment_commodity, container, false);
        GroupCommodityItem group = (GroupCommodityItem) root.findViewById(R.id.group_commodity_marked);
        group.initMarkerList();
        group.validateMarkedList();
        return root;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_expand_less:
                break;
            case R.id.ib_expand_more:
//                gridView.startAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.collapse_anim));
                TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f,- gridView.getMeasuredHeight());
                anim.setDuration(400);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        expandmore.setVisibility(View.GONE);
                        expandless.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                gridView.startAnimation(anim);
                break;
        }
    }

}

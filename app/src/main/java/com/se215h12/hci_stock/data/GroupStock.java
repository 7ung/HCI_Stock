package com.se215h12.hci_stock.data;

import android.support.annotation.DrawableRes;

import com.se215h12.hci_stock.R;

import java.util.List;

/**
 * Created by TungHo on 11/05/2016.
 */
public class GroupStock {
    private @DrawableRes int mImage;
    private String mName;
    private float mChanged;

    private List<Stock> _stocks;

    public static GroupStock create(String name, @DrawableRes int image){
        GroupStock stock = new GroupStock();
        stock.mImage = image;
        stock.mName = name;
        stock.mChanged = (float) (10 * Math.random() - 5);
        return stock;
    }

    public static GroupStock[] create(){
        return new GroupStock[]{
            GroupStock.create("Top KLGD", R.drawable.bg_top_volume),
            GroupStock.create("Top GTGD", R.drawable.bg_top_traded_price),
            GroupStock.create("Top Giá", R.drawable.bg_top_price),
            GroupStock.create("Top Tăng mạnh", R.drawable.bg_top_increase)

        };
    }

    public int getImage() {
        return mImage;
    }

    public String getName() {
        return mName;
    }

    public float getChanged() {
        return mChanged;
    }

    public List<Stock> get_stocks() {
        return _stocks;
    }
}

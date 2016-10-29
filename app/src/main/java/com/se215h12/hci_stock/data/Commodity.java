package com.se215h12.hci_stock.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.se215h12.hci_stock.R;

import java.util.HashMap;

/**
 * Created by TungHo on 10/27/2016.
 */
public class Commodity implements Parcelable {

    private String name;
    private float price;
    private float changed;

    private @DrawableRes int image;
    private boolean marked;

    public static HashMap<String, Commodity> _hash = new HashMap<>();

    public static Commodity create(String name){
        Commodity commodity = new Commodity();
        commodity.name = name;

        if (TextUtils.equals(name, "gold")){
            commodity = initGold(commodity);
        } else if (TextUtils.equals(name, "silver")) {
            commodity = initSilver(commodity);
        } else if (TextUtils.equals(name, "oil")) {
            commodity = initOil(commodity);
        } else if (TextUtils.equals(name, "gas")) {
            commodity = initGas(commodity);
        } else if (TextUtils.equals(name, "coffee")) {
            commodity = initCoffee(commodity);
        } else if (TextUtils.equals(name, "sugar")) {
            commodity = initSugar(commodity);
        } else if (TextUtils.equals(name, "corn")) {
            commodity = initCorn(commodity);
        }else if (TextUtils.equals(name, "usd")){
            commodity = initUse(commodity);
        }else if (TextUtils.equals(name, "eur")){
            commodity = initEur(commodity);
        }else if (TextUtils.equals(name, "pound")){
            commodity = initPound(commodity);
        }else if (TextUtils.equals(name, "sgd")){
            commodity = initSgd(commodity);
        }else if (TextUtils.equals(name, "hkd")){
            commodity = initHkd(commodity);
        }else if (TextUtils.equals(name, "vcb")){
            commodity = initInterestedBank(commodity);
        }else if (TextUtils.equals(name, "acb")){
            commodity = initInterestedBank(commodity);
        }else if (TextUtils.equals(name, "vib")){
            commodity = initInterestedBank(commodity);
        }else if (TextUtils.equals(name, "tcb")){
            commodity = initInterestedBank(commodity);
        }else if (TextUtils.equals(name, "abb")){
            commodity = initInterestedBank(commodity);
        }

        commodity.changed = ((int)(Math.random() * 18) == 15 ) ? 0 : commodity.changed / 8;
        commodity.image = mappingBackgroundRes.get(commodity.getName());
        commodity.setMarked((((int) (Math.random()*2 )) == 1) ? true: false);

        _hash.put(commodity.name, commodity);
        return commodity;
    }

    private static Commodity initInterestedBank(Commodity commodity) {
        commodity.setPrice((float) (6 * Math.random() + 7));        // % percent
        commodity.changed = (float) (6 * Math.random() - 3);
        return commodity;
    }

    private static Commodity initHkd(Commodity commodity) {
        commodity.setPrice((float) (300 * Math.random() + 2700));        // Đồng
        commodity.changed = (float) (300 * Math.random() - 150);
        return commodity;
    }

    private static Commodity initSgd(Commodity commodity) {
        commodity.setPrice((float) (2000 * Math.random() + 15000));        // Đồng
        commodity.changed = (float) (2000 * Math.random() - 1000);
        return commodity;
    }

    private static Commodity initPound(Commodity commodity) {
        commodity.setPrice((float) (9000 * Math.random() + 26000));        // Đồng
        commodity.changed = (float) (9000 * Math.random() - 4500);
        return commodity;
    }

    private static Commodity initEur(Commodity commodity) {
        commodity.setPrice((float) (3000 * Math.random() + 23000));        // Đồng
        commodity.changed = (float) (3000 * Math.random() - 1500);
        return commodity;
    }

    private static Commodity initUse(Commodity commodity) {
        commodity.setPrice((float) (2000 * Math.random() + 21000));        // Đồng
        commodity.changed = (float) (2000 * Math.random() - 1000);
        return commodity;
    }

    private static Commodity initCorn(Commodity commodity) {
        commodity.setPrice((float) (140 * Math.random() + 300));        // USD / pound (1 pound = 0.4536 kg)
        commodity.changed = (float) (140 * Math.random() - 70);
        return commodity;
    }

    private static Commodity initSugar(Commodity commodity) {
        commodity.setPrice((float) (12 * Math.random() + 12));        // USD / pound (1 pound = 0.4536 kg)
        commodity.changed = (float) (12 * Math.random() - 6);
        return commodity;
    }

    private static Commodity initCoffee(Commodity commodity) {
        commodity.setPrice((float) (70 * Math.random() + 100));        // USD / pound (1 pound = 0.4536 kg)
        commodity.changed = (float) (70 * Math.random() - 35);
        return commodity;
    }

    private static Commodity initGas(Commodity commodity) {
        commodity.setPrice((float) (2 * Math.random() + 1.5));        // USD / gigajoule
        commodity.changed = (float) (2 * Math.random() - 1);
        return commodity;
    }

    private static Commodity initSilver(Commodity commodity) {
        commodity.setPrice((float) (8 * Math.random() + 13));            // USD / oz
        commodity.changed = (float) (8 * Math.random() - 4);
        return commodity;
    }

    private static Commodity initGold(Commodity commodity){
        commodity.setPrice((float) (300 * Math.random() + 1050));             // USD / oz
        commodity.changed = (float) (300 * Math.random() - 150);
        return commodity;
    }

    private static Commodity initOil(Commodity commodity){
        commodity.setPrice((float) (25 * Math.random() + 25));             // USD / barrel (1 barrel = 119.24 lít)
        commodity.changed = (float) (25 * Math.random() - 12.5);
        return commodity;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public float getChanged() {
        return changed;
    }


    public static HashMap<String, Integer> mappingBackgroundRes = new HashMap<>();

    static {
        mappingBackgroundRes.put("gold", R.drawable.bg_gold);
        mappingBackgroundRes.put("silver", R.drawable.bg_silver);
        mappingBackgroundRes.put("oil", R.drawable.bg_oil);
        mappingBackgroundRes.put("gas", R.drawable.bg_gas);
        mappingBackgroundRes.put("coffee", R.drawable.bg_coffee);
        mappingBackgroundRes.put("sugar", R.drawable.bg_sugar);
        mappingBackgroundRes.put("corn", R.drawable.bg_corn);
        mappingBackgroundRes.put("usd", R.drawable.bg_usd);
        mappingBackgroundRes.put("eur", R.drawable.bg_euro);
        mappingBackgroundRes.put("pound", R.drawable.bg_pound);
        mappingBackgroundRes.put("sgd", R.drawable.bg_sgd);
        mappingBackgroundRes.put("hkd", R.drawable.bg_hkd);
        mappingBackgroundRes.put("vcb", R.drawable.bg_vcb);
        mappingBackgroundRes.put("acb", R.drawable.bg_acb);
        mappingBackgroundRes.put("vib", R.drawable.bg_vib);
        mappingBackgroundRes.put("tcb", R.drawable.bg_tcb);
        mappingBackgroundRes.put("abb", R.drawable.bg_abb);

    }


    public int getImage() {
        return image;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(getPrice());
        dest.writeFloat(changed);
        dest.writeInt(image);
        dest.writeBooleanArray(new boolean[]{marked});
    }

    public void setPrice(float price) {
        this.price = price;
    }


    // tham chiếu giá vàng, giá bạc
    // đơn vị usd/ oz
    // http://silverprice.org/spot-silver.html
    // http://goldprice.org/spot-gold.html

    // Tham chiếu đơn vị tính vàng
    // https://ericforex.wordpress.com/kien-thuc-giao-dich/kien-thuc-tong-hop/1-ounce-bang-bao-nhieu-luongcay-vang/
}

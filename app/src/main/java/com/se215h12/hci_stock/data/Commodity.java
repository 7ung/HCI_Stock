package com.se215h12.hci_stock.data;

import android.support.annotation.DrawableRes;

import com.se215h12.hci_stock.R;

import java.util.HashMap;

/**
 * Created by TungHo on 10/27/2016.
 */
public class Commodity {

    private String name;
    private float price;
    private float changed;

    private @DrawableRes int image;

    public static Commodity create(String name){
        Commodity commodity = new Commodity();
        commodity.name = name;
        if (name == "gold"){
            commodity = initGold(commodity);
        } else if (name == "silver") {
            commodity = initSilver(commodity);
        } else if (name == "oil") {
            commodity = initOil(commodity);

        } else if (name == "gas") {
            commodity = initGas(commodity);

        } else if (name == "coffee") {
            commodity = initCoffee(commodity);

        } else if (name == "sugar") {
            commodity = initSugar(commodity);

        } else if (name == "corn") {
            commodity = initCorn(commodity);
        }
        commodity.image = mappingBackgroundRes.get(commodity.getName());

        return commodity;
    }

    private static Commodity initCorn(Commodity commodity) {
        commodity.price = (float) (140 * Math.random() + 300);        // USD / pound (1 pound = 0.4536 kg)
        commodity.changed = (float) (140 * Math.random() - 70);
        return commodity;
    }

    private static Commodity initSugar(Commodity commodity) {
        commodity.price = (float) (12 * Math.random() + 12);        // USD / pound (1 pound = 0.4536 kg)
        commodity.changed = (float) (12 * Math.random() - 6);
        return commodity;
    }

    private static Commodity initCoffee(Commodity commodity) {
        commodity.price = (float) (70 * Math.random() + 100);        // USD / pound (1 pound = 0.4536 kg)
        commodity.changed = (float) (70 * Math.random() - 35);
        return commodity;
    }

    private static Commodity initGas(Commodity commodity) {
        commodity.price = (float) (2 * Math.random() + 1.5);        // USD / gigajoule
        commodity.changed = (float) (2 * Math.random() - 1);
        return commodity;
    }

    private static Commodity initSilver(Commodity commodity) {
        commodity.price = (float) (8 * Math.random() + 13);            // USD / oz
        commodity.changed = (float) (8 * Math.random() - 4);
        return commodity;
    }

    private static Commodity initGold(Commodity commodity){
        commodity.price = (float) (300 * Math.random() + 1050);             // USD / oz
        commodity.changed = (float) (300 * Math.random() - 150);
        return commodity;
    }

    private static Commodity initOil(Commodity commodity){
        commodity.price = (float) (25 * Math.random() + 25);             // USD / barrel (1 barrel = 119.24 lít)
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
    }

    public static Commodity[] woods = new Commodity[]{
            Commodity.create("gold"),
            Commodity.create("silver"),
            Commodity.create("oil"),
            Commodity.create("gas"),
            Commodity.create("coffee"),
            Commodity.create("sugar"),
            Commodity.create("corn")
    };

    public int getImage() {
        return image;
    }

    // tham chiếu giá vàng, giá bạc
    // đơn vị usd/ oz
    // http://silverprice.org/spot-silver.html
    // http://goldprice.org/spot-gold.html

    // Tham chiếu đơn vị tính vàng
    // https://ericforex.wordpress.com/kien-thuc-giao-dich/kien-thuc-tong-hop/1-ounce-bang-bao-nhieu-luongcay-vang/
}

package com.se215h12.hci_stock.data;

/**
 * Created by TungHo on 10/27/2016.
 */
public class Commodity {

    private String name;
    private float price;            // triệu đồng một chỉ
    private float changed;

    public static Commodity create(String name){
        Commodity commodity = new Commodity();
        commodity.name = name;
        if (name == "gold"){
            commodity = initGold(commodity);
        }
        return commodity;
    }

    private static Commodity initGold(Commodity commodity){
        commodity.price = (float) (0.2 * Math.random() + 2.8);
        commodity.changed = (float) (0.2 * Math.random() - 0.1);
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

    public static Commodity[] dummyGolds = new Commodity[]{
        Commodity.create("gold"),
        Commodity.create("gold"),
        Commodity.create("gold"),
        Commodity.create("gold"),
        Commodity.create("gold"),
        Commodity.create("gold"),
        Commodity.create("gold"),
        Commodity.create("gold"),

    };
}

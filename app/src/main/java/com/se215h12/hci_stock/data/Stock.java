package com.se215h12.hci_stock.data;

import com.se215h12.hci_stock.util.Utils;

import java.util.Random;

/**
 * Created by TungHo on 10/27/2016.
 */
public class Stock {

    private Index parent;

    private String stockName;
    private String companyName;
    private String industry;

    private float price;            // giá hiện tại
    private float refPrice;         // giá tham chiếu
    private float openedPrice;
    private float maxPrice;
    private float minPrice;

    private float changed;
    private float changedRatio;

    private int reBuy;            // dư mua
    private int reSell;           // dư bán

    private int volume;           // Khối lượng giao dịch
    private float value;            // Giá trị giao dịch

    private int foreignBuy;       // Nước ngoài mua
    private int foreignSell;      // Nước ngoài bán
    private float room;             // Room còn lại

    private int stockTraded;        // Số chứng khoán đang lưu hành

    private float beta;
    private float EPS;
    private float pe;
    private int roa;              // percent value
    private int roe;              // percent value


/*    public Stock(String parent, String code, String name, String industry) throws Exception {
        this.parent = Index.sIndexes.get(parent);
        if (this.parent == null)
            throw new Exception("not found parent");

    }*/

    private Stock(){}

    public static Stock create(String parent, String code, String name, String industry) throws Exception {
        Stock st = new Stock();
        st.parent = Index.sIndexes.get(parent);
        if (st.parent == null)
            throw new Exception("not found parent");
        if (Index.sIndexes.containsKey(parent) == false)
            throw new Exception("not found parent");

        st.parent.addStock(st);
        float deltaRatio;
        if (parent.startsWith("VN"))
            deltaRatio = 0.075f;
        else
            deltaRatio = 0.01f;

        st.stockName  = code;
        st.companyName = name;
        st.industry = industry;

        st.refPrice = (float) (200 * Math.random());

        st.openedPrice = (float) (st.refPrice * (1 + deltaRatio * Utils.randNegativeOne()));
        st.maxPrice = (float) (st.refPrice * (1 + deltaRatio * Utils.randNegativeOne()));
        st.minPrice = (float) (st.refPrice * (1 + deltaRatio * Utils.randNegativeOne()));

        st.changed = (float) (st.refPrice *  deltaRatio * Utils.randNegativeOne());
        st.changedRatio = st.changed / st.refPrice * 100;
        st.price = st.changed + st.refPrice;

        st.reBuy = (int) (60000 * Math.random());
        st.reSell = (int) (60000 * Math.random());

        st.volume = (int) (2000000 *Math.random());
        st.value = (float) ( Utils.randNegativeOne() * st.refPrice * deltaRatio * st.value);

        st.foreignBuy = (int) (120000 * Math.random());
        st.foreignSell = (int) (120000 * Math.random());
        st.room = (float) (100 * Math.random());

        st.stockTraded = (int) ((2000000) * Math.random());

        st.beta = (float) (4 * Math.random() - 1);
        st.EPS = (float) (80 * Math.random() - 60);
        st.pe = (float) (16000 * Math.random() - 1000);
        st.roa = (int) (25 *Math.random() - 5);
        st.roe = (int) (25 * Math.random() - 5);

        return st;
    }


}

package com.se215h12.hci_stock.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import com.se215h12.hci_stock.R;
import com.se215h12.hci_stock.data.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by TungHo on 10/26/2016.
 */
public class Utils {

    private static final String DEFAULT_STRING_FORMAT = "%.2f";
    private Utils(){}

    public static String format(float number)
    {
        return String.format(DEFAULT_STRING_FORMAT, number);
    }

    public static ArrayList<Stock> loadAllStock(Context context, String fileName) throws Exception {
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, "UTF-8");
        JSONArray objs =  new JSONArray(json);

        ArrayList<Stock> stocks = new ArrayList<>();
        for (int i = 0; i < objs.length(); ++i)
        {
            String code = ((JSONObject) objs.get(i)).getString("code");
            String name = ((JSONObject) objs.get(i)).getString("name");
            String industry = ((JSONObject) objs.get(i)).getString("industry");
            String index = ((JSONObject) objs.get(i)).getString("index");

            stocks.add(Stock.create(index, code, name, industry));
        }
        return stocks;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void trendColor_M(Context context, TextView tv, float price)
    {
        if (price > 0)
            tv.setTextColor(context.getResources().getColor(R.color.indexMore, null));
        else if (price == 0)
            tv.setTextColor(context.getResources().getColor(R.color.indexEqual, null));
        else if (price < 0)
            tv.setTextColor(context.getResources().getColor(R.color.indexLess, null));
    }
    public static void trendColor(Context context, TextView tv, float price)
    {
        if (price > 0)
            tv.setTextColor(context.getResources().getColor(R.color.indexMore));
        else if (price == 0)
            tv.setTextColor(context.getResources().getColor(R.color.indexEqual));
        else if (price < 0)
            tv.setTextColor(context.getResources().getColor(R.color.indexLess));
    }

    public static double randNegativeOne()
    {
        return Math.random() * 2 - 1;
    }
}

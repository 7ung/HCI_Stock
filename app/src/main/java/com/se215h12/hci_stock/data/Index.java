package com.se215h12.hci_stock.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TungHo on 10/26/2016.
 */
public class Index implements Parcelable{
    private String name;
    private float price;
    private float changedValue;
    private float changedRatio; // percent value
    private float value;        // giá trị giao dịch (nghìn tỉ)
    private float volume;       // khối lượng giao dịch

    private ForeignTrade foreignTrade;

    ArrayList<Stock> stocks = new ArrayList<>();

    public Index(String name) {
        this.name = name;
        price = (float) (500 * Math.random() + 300);
        changedValue = (float) (200 * Math.random() - 100);
        changedValue = ((int)(Math.random() * 18) == 8) ? 0 : changedValue;
        changedRatio = (price - changedValue) / price;
        value = (float) (2000 * Math.random() + 500);
        volume = (float) (80000000 * Math.random() + 70000000) / 1000;

        if (changedValue < 0){
            changedRatio *= -1;
        }
        foreignTrade = ForeignTrade.create();
    }

    protected Index(Parcel in) {
        name = in.readString();
        price = in.readFloat();
        changedValue = in.readFloat();
        changedRatio = in.readFloat();
        value = in.readFloat();
        volume = in.readFloat();
        foreignTrade = in.readParcelable(ForeignTrade.class.getClassLoader());
    }

    public static final Creator<Index> CREATOR = new Creator<Index>() {
        @Override
        public Index createFromParcel(Parcel in) {
            return new Index(in);
        }

        @Override
        public Index[] newArray(int size) {
            return new Index[size];
        }
    };

    public Index addStock(Stock stock)
    {
        this.stocks.add(stock);
        return this;
    }

    public ForeignTrade getForeignTrade() {
        return foreignTrade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeFloat(changedValue);
        dest.writeFloat(changedRatio);
        dest.writeFloat(value);
        dest.writeFloat(volume);
        dest.writeParcelable(foreignTrade,0);
    }

    public static class ForeignTrade implements Parcelable{
        private float inValue;
        private float outValue;
        private float inVolumne;
        private float outVolume;

        protected ForeignTrade(Parcel in) {
            inValue = in.readFloat();
            outValue = in.readFloat();
            inVolumne = in.readFloat();
            outVolume = in.readFloat();
        }

        private ForeignTrade(){}

        public static final Creator<ForeignTrade> CREATOR = new Creator<ForeignTrade>() {
            @Override
            public ForeignTrade createFromParcel(Parcel in) {
                return new ForeignTrade(in);
            }

            @Override
            public ForeignTrade[] newArray(int size) {
                return new ForeignTrade[size];
            }
        };

        public float getInValue() {
            return inValue;
        }

        public void setInValue(float inValue) {
            this.inValue = inValue;
        }

        public float getOutValue() {
            return outValue;
        }

        public void setOutValue(float outValue) {
            this.outValue = outValue;
        }

        public float getInVolumne() {
            return inVolumne;
        }

        public void setInVolumne(float inVolumne) {
            this.inVolumne = inVolumne;
        }

        public float getOutVolume() {
            return outVolume;
        }

        public void setOutVolume(float outVolume) {
            this.outVolume = outVolume;
        }

        public static ForeignTrade create()
        {
            ForeignTrade ft = new ForeignTrade();
            ft.inValue = (float) (2000 * Math.random());
            ft.outValue =  (float) (2000 * Math.random());
            ft.inVolumne = (float) (500 * Math.random());
            ft.outVolume = (float) (500 * Math.random());
            return  ft;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(inValue);
            dest.writeFloat(outValue);
            dest.writeFloat(inVolumne);
            dest.writeFloat(outVolume);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getChangedValue() {
        return changedValue;
    }

    public void setChangedValue(float changedValue) {
        this.changedValue = changedValue;
    }

    public float getChangedRatio() {
        return changedRatio;
    }

    public void setChangedRatio(float changedRatio) {
        this.changedRatio = changedRatio;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public static Index[] hoses = new Index[]
        {
            new Index("VNIndex"),
            new Index("VN100"),
            new Index("VN30"),
            new Index("VNALL"),
            new Index("VNMID"),
            new Index("VNSML")
        };

    public static Index[] hnx = new Index[]
        {
            new Index("HNXIndex"),
            new Index("HNX30TRI"),
            new Index("HNXCon"),
            new Index("HNXFin"),
            new Index("HNXMan"),
            new Index("UpCom")
        };

    public static Index[] world = new Index[]
        {
            new Index("NASDAQ"),
            new Index("NYSE"),
            new Index("Dow Jone"),
            new Index("S&P 500"),
            new Index("FTSE 100"),
            new Index("DAX"),
            new Index("CAC40"),
            new Index("Hang Seng"),
            new Index("Nikkei"),
            new Index("Shanghai")
        };

    public static HashMap<String, Index> sIndexes = new HashMap<>();

    static {
        sIndexes.put("VNIndex", hoses[0]);
        sIndexes.put("VN100", hoses[1]);
        sIndexes.put("VN30", hoses[2]);
        sIndexes.put("VNALL", hoses[3]);
        sIndexes.put("VNMID", hoses[4]);
        sIndexes.put("VNSML", hoses[5]);

        sIndexes.put("HNX", hnx[0]);
        sIndexes.put("HNX30TRI", hnx[1]);
        sIndexes.put("HNXCon", hnx[2]);
        sIndexes.put("HNXFin", hnx[3]);
        sIndexes.put("HNXMan", hnx[4]);
        sIndexes.put("UpCom", hnx[5]);

        sIndexes.put("NASDAQ", world[0]);
        sIndexes.put("NYSE", world[1]);
        sIndexes.put("Dow Jone", world[2]);
        sIndexes.put("S&P 500", world[3]);
        sIndexes.put("FTSE 100", world[4]);
        sIndexes.put("DAX", world[5]);
        sIndexes.put("CAC40", world[6]);
        sIndexes.put("Hang Seng", world[7]);
        sIndexes.put("Nikkei", world[8]);
        sIndexes.put("Shanghai", world[9]);

    }
}

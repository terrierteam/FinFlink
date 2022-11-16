package eu.infinitech.finflink.structures;

import java.io.Serializable;

public interface TradingData extends Comparable<TradingData>, Serializable {

    public long getUnixDate();

    public String getAssetSymbol();

}

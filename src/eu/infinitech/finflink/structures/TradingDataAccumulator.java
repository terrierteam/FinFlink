package eu.infinitech.finflink.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of trades, that accumulates across workers processing a
 * single asset and window
 * @author Richard
 *
 */
public class TradingDataAccumulator implements Serializable{

	private static final long serialVersionUID = 8287152296129785823L;
	List<TradingData> tradingData;

	public TradingDataAccumulator() {
		tradingData = new ArrayList<TradingData>();
	}
	
	public TradingDataAccumulator(List<TradingData> tradingData) {
		super();
		this.tradingData = tradingData;
	}

	public List<TradingData> getTradingData() {
		return tradingData;
	}

	public void setTradingData(List<TradingData> tradingData) {
		this.tradingData = tradingData;
	}
	
	
	
}

package eu.infinitech.finflink.keys;

import eu.infinitech.finflink.structures.TradingData;
import org.apache.flink.api.java.functions.KeySelector;

import eu.infinitech.finflink.structures.Trade;

/**
 * Extracts the Asset Symbol from the trade to use as a grouping key
 * @author richardm
 *
 */
public class KeyByAssetID implements KeySelector<TradingData, String>{

	private static final long serialVersionUID = -1428294311796298639L;

	@Override
	public String getKey(TradingData value) throws Exception {
		return value.getAssetSymbol();
	}

}

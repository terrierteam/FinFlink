package eu.infinitech.finflink.transformations.data;

import org.apache.flink.api.common.functions.MapFunction;

import eu.infinitech.finflink.structures.Trade;

/**
 * Converts a trade data string to a Trade object
 * @author richardm
 *
 */
public class ToTrade implements InputMapper{

	private static final long serialVersionUID = 4819959066683219070L;

	@Override
	public Trade map(String tradeData) throws Exception {

		String[] parts = tradeData.split(",");
		
		Trade trade = new Trade(parts[0], Long.parseLong(parts[1])*1000, Double.parseDouble(parts[2]), Long.parseLong(parts[3]));
		
		return trade;
	}

}

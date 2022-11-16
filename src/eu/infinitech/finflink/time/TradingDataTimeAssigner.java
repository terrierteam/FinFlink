package eu.infinitech.finflink.time;

import eu.infinitech.finflink.structures.TradingData;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;

/**
 * Extracts the trade timestamp from a Trade
 * @author richardm
 *
 */
public class TradingDataTimeAssigner implements TimestampAssigner<TradingData>, TimestampAssignerSupplier<TradingData> {

	private static final long serialVersionUID = 9031211879131780647L;

	@Override
	public long extractTimestamp(TradingData tradingData, long recordTimestamp) {
		return tradingData.getUnixDate();
	}

	@Override
	public TimestampAssigner<TradingData> createTimestampAssigner(Context context) {
		return new TradingDataTimeAssigner();
	}

}

package eu.infinitech.finflink.util;

import java.util.ArrayList;
import java.util.List;

import eu.infinitech.finflink.structures.InputStreamType;
import eu.infinitech.finflink.structures.Trade;
import eu.infinitech.finflink.structures.TradePeriod;
import eu.infinitech.finflink.structures.TradingData;

/**
 * Utility Class for working with TradePeriods
 * @author Richard
 *
 */
public class TradePeriodsGenerator {

	/**
	 * Converts a list of trades into a list of TradePeriods, where each trade period represents
	 * trades from a particular time period. The length of each time period is defined by timePeriod.
	 * TradePeriods are returned in time order.
	 * @param timePeriod
	 * @param tradingData
	 * @return
	 */
	public static List<TradePeriod> generateTradePeriods(InputStreamType inputStreamType, long timePeriod, List<TradingData> tradingData) {
		
		//System.err.println(trades.size()+" "+timePeriod+" "+trades.get(0).getUnixDate()+" "+trades.get(trades.size()-1).getUnixDate());
		
		if (tradingData.size()==0) return new ArrayList<TradePeriod>();
		
		long currentStartTime = tradingData.get(0).getUnixDate();
		long currentEndTime = currentStartTime + timePeriod;
		
		List<TradePeriod> tradePeriods = new ArrayList<TradePeriod>();
		List<TradingData> tradesInCurrentPeriod = new ArrayList<TradingData>();
		
		for (TradingData tradingDatum : tradingData) {
			//System.err.println(trade.getUnixDate()+" "+trade.getVolume());
			if (tradingDatum.getUnixDate()>=currentEndTime) {
				currentStartTime = tradingDatum.getUnixDate();
				currentEndTime = currentStartTime + timePeriod;
				tradePeriods.add(new TradePeriod(inputStreamType, tradesInCurrentPeriod));
				tradesInCurrentPeriod = new ArrayList<TradingData>();
			}
			tradesInCurrentPeriod.add(tradingDatum);
		}
		tradePeriods.add(new TradePeriod(inputStreamType, tradesInCurrentPeriod));
		
		
		return tradePeriods;
	}
	
}

package eu.infinitech.finflink.transformations.technicalIndicators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.infinitech.finflink.structures.Trade;

/**
 * Data Accumulator for True Range. The true range indicator is taken as the greatest of the following: 
 * current high less the current low; the absolute value of the current high less the previous 
 * close; and the absolute value of the current low less the previous close.
 * 
 * Windowing Assumptions:
 *  - True Range is a comparative metric, i.e. it compares two time periods of the same length. 
 *    We assume that the current window contains both periods 
 * 
 * 
 * @author richardm
 *
 */
public class TrueRange implements IndicatorState, Serializable{

	private static final long serialVersionUID = 6338907420931948467L;
	List<Trade> trades;
	
	public TrueRange() {
		this.trades = new ArrayList<Trade>();
	}
	
	@Override
	public Double getResult() {
		
		if (trades.size()<2) return 0.0; 
		
		// Sort trades by time
		Collections.sort(trades);
		
		// Get start and end dates
		long firstTradeTime = trades.get(0).getUnixDate();
		long lastTradeTime = trades.get(trades.size()-1).getUnixDate();
		
		// Figure out the middle time
		long timeSplitLength = (lastTradeTime-firstTradeTime)/2;
		long splitTime = firstTradeTime+timeSplitLength;
		
		// Calculate high and low prices for the asset in the current period
		double currentHigh = Double.MIN_VALUE;
		double currentLow = Double.MAX_VALUE;
		
		double prevClose = 0;
		for (Trade t : trades) {
			if (t.getUnixDate()<splitTime) {
				prevClose = t.getPrice();
				continue;
			}
			if (t.getPrice()>currentHigh) currentHigh = t.getPrice();
			if (t.getPrice()<currentLow) currentLow = t.getPrice();
		}
		
		return Math.max(Math.abs(currentLow-prevClose), Math.max(currentHigh-currentLow, Math.abs(currentHigh-prevClose)));
	}

	@Override
	public void add(Trade value) {
		trades.add(value);
	}

	@Override
	public void merge(IndicatorState b) {
		trades.addAll(((TrueRange)b).getTrades());
	}

	public List<Trade> getTrades() {
		return trades;
	}

	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}

	@Override
	public IndicatorState get() {
		return new TrueRange();
	}
	
	

}

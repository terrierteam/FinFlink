package eu.infinitech.finflink.structures;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a list of trades grouped by a time period.
 * @author Richard
 *
 */
public class TradePeriod implements Serializable{

	private static final long serialVersionUID = 6547195579242790966L;
	
	long startTime;
	long stopTime;
	InputStreamType inputStreamType;
	List<TradingData> tradingData;
	double openPrice;
	double highPrice;
	double lowPrice;
	double closePrice;

	long volume;

	public TradePeriod() {};
	
	public TradePeriod(InputStreamType inputStreamType, List<TradingData> tradingData) {
		this.inputStreamType = inputStreamType;
		initializeTradePeriod(tradingData);
	}
	
	public TradePeriod(InputStreamType inputStreamType, List<TradingData> tradingData, int numSlices, int periodIndex) {
		this.inputStreamType = inputStreamType;

		int numTrades = tradingData.size();
		int slideSize = numTrades/numSlices;
		
		int startTradeIndex = slideSize*periodIndex;
		int endTradeIndex = slideSize*(periodIndex+1);
		if (endTradeIndex>numTrades) endTradeIndex = numTrades;
		
		List<TradingData> periodTrades = tradingData.subList(startTradeIndex, endTradeIndex);
		
		initializeTradePeriod(periodTrades);
	}

	protected void usingPricePoints(List<PricePoint> pricePoints){
		startTime = pricePoints.get(0).getUnixDate();
		stopTime = pricePoints.get(pricePoints.size()-1).getUnixDate();

		highPrice = Double.MIN_VALUE;
		lowPrice = Double.MAX_VALUE;
		openPrice = pricePoints.get(0).getOpenPrice();
		closePrice = pricePoints.get(pricePoints.size()-1).getClosePrice();

		for (PricePoint pricePoint : pricePoints) {
			if (pricePoint.getHighPrice()>highPrice) highPrice = pricePoint.getHighPrice();
			if (pricePoint.getLowPrice()<lowPrice) lowPrice = pricePoint.getLowPrice();
		}

		volume = pricePoints.stream().map(PricePoint::getVolume).reduce(0L, Long::sum);

	}

	protected void usingTrades(List<Trade> trades){
		// Get start and end dates
		startTime = trades.get(0).getUnixDate();
		stopTime = trades.get(trades.size()-1).getUnixDate();

		// Calculate high and low prices for the asset in the current period
		highPrice = Double.MIN_VALUE;
		lowPrice = Double.MAX_VALUE;
		openPrice = trades.get(0).getPrice();
		closePrice = trades.get(trades.size()-1).getPrice();

		for (Trade t : trades) {
			if (t.getPrice()>highPrice) highPrice = t.getPrice();
			if (t.getPrice()<lowPrice) lowPrice = t.getPrice();
		}

		volume = trades.stream().map(Trade::getVolume).reduce(0L, Long::sum);
	}
	
	protected void initializeTradePeriod(List<TradingData> tradingData) {
		this.tradingData = tradingData;

		if (this.inputStreamType.getType() == InputStreamType.Type.Trade) {
			List<Trade> trades = tradingData
					.stream()
					.map(e -> (Trade) e)
					.collect(Collectors.toList());
			usingTrades(trades);
		}

		if(this.inputStreamType.getType() == InputStreamType.Type.PricePoint){
			List<PricePoint> pricePoints = tradingData
					.stream()
					.map(e -> (PricePoint) e)
					.collect(Collectors.toList());

			usingPricePoints(pricePoints);
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}

	public List<TradingData> getTradingData() {
		return tradingData;
	}

	public void setTradingData(List<TradingData> tradingData) {
		this.tradingData = tradingData;
	}

	public double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}

	public double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public double getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}
	
}

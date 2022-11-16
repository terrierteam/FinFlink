package eu.infinitech.finflink.structures;

import java.io.Serializable;

/**
 * Represents the price for an asset at a particular point in time
 * @author richardm
 *
 */
public class PricePoint implements TradingData {

	private static final long serialVersionUID = -2706243625495783892L;
	
	long unixDate;
	double openPrice;
	double highPrice;
	double lowPrice;
	double closePrice;
	long volume;
	short openInt;
	boolean ignore;
	
	String assetSymbol; 
	
	public PricePoint() {
		ignore = false;
	}

	public PricePoint(long unixDate, double openPrice, double highPrice, double lowPrice, double closePrice,
			long volume, short openInt) {
		super();
		this.unixDate = unixDate;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
		this.openInt = openInt;
		ignore = false;
	}
	
	

	public PricePoint(long unixDate, double openPrice, double highPrice, double lowPrice, double closePrice,
			long volume, short openInt, String assetSymbol) {
		super();
		this.unixDate = unixDate;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
		this.openInt = openInt;
		this.assetSymbol = assetSymbol;
		ignore = false;
	}

	public long getUnixDate() {
		return unixDate;
	}

	public void setUnixDate(long unixDate) {
		this.unixDate = unixDate;
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

	public short getOpenInt() {
		return openInt;
	}

	public void setOpenInt(short openInt) {
		this.openInt = openInt;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public String getAssetSymbol() {
		return assetSymbol;
	}

	public void setAssetSymbol(String assetSymbol) {
		this.assetSymbol = assetSymbol;
	}

	@Override
	public int compareTo(TradingData tradingData) {
		if (ignore) return -1;
		return Long.compare(unixDate, tradingData.getUnixDate());
	}
	
	
	
}

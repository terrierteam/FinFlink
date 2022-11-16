package eu.infinitech.finflink.structures;

public class Trade implements TradingData{

	String assetSymbol;
	long unixDate;
	double price;
	long volume;
	
	public Trade() {}
	
	public Trade(String assetSymbol, long unixDate, double price, long volume) {
		super();
		this.assetSymbol = assetSymbol;
		this.unixDate = unixDate;
		this.price = price;
		this.volume = volume;
	}
	
	public String getAssetSymbol() {
		return assetSymbol;
	}
	public void setAssetSymbol(String assetSymbol) {
		this.assetSymbol = assetSymbol;
	}
	public long getUnixDate() {
		return unixDate;
	}
	public void setUnixDate(long unixDate) {
		this.unixDate = unixDate;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public int compareTo(TradingData o) {
		return Long.compare(unixDate, o.getUnixDate());
	}
	
	
	
}

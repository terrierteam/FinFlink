package eu.infinitech.finflink.transformations.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

import org.apache.flink.api.common.functions.MapFunction;

import eu.infinitech.finflink.structures.PricePoint;

/**
 * Converts a String of the form Date,Open,High,Low,Close,Volume,OpenInt
 * to a PricePoint object, that represents the price of an asset at a particular
 * point in time.
 * @author richardm
 *
 */
public class ToPricePoint implements InputMapper{

	private static final long serialVersionUID = -1518776394785929900L;
	String assetSymbol;
	
	/**
	 * Default constructor, will not set the assetSymbol in the PricePoint
	 */
	public ToPricePoint() {}
	
	/**
	 * Constructor, will sets the assetSymbol in the PricePoint withthe given value
	 */
	public ToPricePoint(String assetSymbol) {
		this.assetSymbol = assetSymbol;
	}
	
	@Override
	public PricePoint map(String priceData) throws Exception {
		
		String[] parts = priceData.split(",");
		
		PricePoint pp = new PricePoint();
		
		// Date,Open,High,Low,Close,Volume,OpenInt
		
		if (parts[0].startsWith("Date")) pp.setIgnore(true); // ignore the header
		else {
			// Date
			LocalDate date = LocalDate.parse(parts[0]);
			LocalTime time = LocalTime.of(18,0); //LocalTime.parse(parts[1]);
			pp.setUnixDate(date.toEpochSecond(time, ZoneOffset.UTC));
			
			// Open,High,Low,Close
			pp.setOpenPrice(Double.parseDouble(parts[1]));
			pp.setHighPrice(Double.parseDouble(parts[2]));
			pp.setLowPrice(Double.parseDouble(parts[3]));
			pp.setClosePrice(Double.parseDouble(parts[4]));
			
			// Volume,OpenInt
			pp.setVolume(Long.parseLong(parts[5]));
			pp.setOpenInt(Short.parseShort(parts[6]));
		}

		// assetSymbol
		if (assetSymbol!=null) pp.setAssetSymbol(assetSymbol);
		
		return pp;
	}

}

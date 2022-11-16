package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.List;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import org.apache.flink.streaming.api.windowing.time.Time;

import eu.infinitech.finflink.structures.TradePeriod;

/**
 * Negative price directional momentum, indicates that the degree to which the low price is trending downward
 * is faster than the high price is trending upward
 * @author Richard
 *
 */
public class LowPriceDirectionalMomentum extends TechnicalIndicatorGenerator{

	private static final long serialVersionUID = 8451563241381149449L;

	public LowPriceDirectionalMomentum() {}
	
	public LowPriceDirectionalMomentum(Time timePeriod) {
		setTimePeriod(timePeriod);

		setIndicatorRequirements(new IndicatorRequirements()
				.needsHigh()
				.needsLow());
	}
	
	@Override
	public double calculate(List<TradePeriod> periodsToConsider) {
		
		if (periodsToConsider.size()<3) return 0.0;
		
		double component1 = 0.0;
		double component2 = 0.0;
		
		
		TradePeriod prevPeriodM1 = null;
		TradePeriod prevPeriodM2 = null;
		for (TradePeriod period : periodsToConsider) {
			
			if (prevPeriodM1==null && prevPeriodM2==null) {
				prevPeriodM1 = period;
				continue;
			}
			
			if (prevPeriodM2==null) {
				prevPeriodM2 = prevPeriodM1;
				prevPeriodM1 = period;
				continue;
			}
			
			double minusDM = 0;
			if (period.getHighPrice()-prevPeriodM1.getHighPrice() < prevPeriodM1.getLowPrice()-period.getLowPrice()) minusDM = prevPeriodM1.getLowPrice()-period.getLowPrice();
			
			double minusDMM1 = 0;
			if (prevPeriodM1.getHighPrice()-prevPeriodM2.getHighPrice() < prevPeriodM2.getLowPrice()-prevPeriodM1.getLowPrice()) minusDMM1 = prevPeriodM2.getLowPrice()-prevPeriodM1.getLowPrice();
			
			component1 = component1+minusDMM1;
			component2 = component2+minusDMM1+minusDM;
			
			prevPeriodM2 = prevPeriodM1;
			prevPeriodM1 = period;
			
		}
		
		return component1-(1.0/(periodsToConsider.size()-2)*component2);
	}

}

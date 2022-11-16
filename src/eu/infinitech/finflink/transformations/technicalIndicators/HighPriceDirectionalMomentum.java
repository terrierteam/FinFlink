package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.List;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import org.apache.flink.streaming.api.windowing.time.Time;

import eu.infinitech.finflink.structures.TradePeriod;

/**
 * High price directional momentum, indicates that the degree to which the high price is trending upward
 * is faster than the low price is trending downward
 * @author Richard
 *
 */
public class HighPriceDirectionalMomentum extends TechnicalIndicatorGenerator{


	private static final long serialVersionUID = -5564854844166797426L;

	public HighPriceDirectionalMomentum() {}
	
	public HighPriceDirectionalMomentum(Time timePeriod) {
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
			
			double plusDM = 0;
			if (period.getHighPrice()-prevPeriodM1.getHighPrice() > prevPeriodM1.getLowPrice()-period.getLowPrice()) plusDM = period.getHighPrice()-prevPeriodM1.getHighPrice();
			
			double plusDMM1 = 0;
			if (prevPeriodM1.getHighPrice()-prevPeriodM2.getHighPrice() > prevPeriodM2.getLowPrice()-prevPeriodM1.getLowPrice()) plusDMM1 = prevPeriodM1.getHighPrice()-prevPeriodM2.getHighPrice();
			
			component1 = component1+plusDMM1;
			component2 = component2+plusDMM1+plusDM;
			
			prevPeriodM2 = prevPeriodM1;
			prevPeriodM1 = period;
			
		}
		
		return component1-(1.0/(periodsToConsider.size()-2)*component2);
	}

}

package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.List;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import org.apache.flink.streaming.api.windowing.time.Time;

import eu.infinitech.finflink.structures.TradePeriod;

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
public class AverageTrueRange extends TechnicalIndicatorGenerator{

	private static final long serialVersionUID = 6338907420931948467L;
	
	public AverageTrueRange() {}
	
	public AverageTrueRange(Time timePeriod) {
		setTimePeriod(timePeriod);

		setIndicatorRequirements(new IndicatorRequirements()
				.needsClose()
				.needsHigh()
				.needsLow());
	}
	
	@Override
	public double calculate(List<TradePeriod> periodsToConsider) {
		
		//System.err.println(periodsToConsider.size());
		
		if (periodsToConsider.size()<2) return 0.0;
		
		double averageTrueRange = 0.0;
		
		TradePeriod prevPeriod = null;
		for (TradePeriod period : periodsToConsider) {
			
			if (prevPeriod==null) {
				prevPeriod = period;
				continue;
			}
			
			double trueRange = Math.max(Math.abs(period.getLowPrice()-prevPeriod.getClosePrice()), Math.max(period.getHighPrice()-period.getLowPrice(), Math.abs(period.getHighPrice()-prevPeriod.getClosePrice())));
			averageTrueRange = averageTrueRange + trueRange;
			
			prevPeriod = period;
			
		}
		
		return averageTrueRange/(periodsToConsider.size()-1);
	}


}

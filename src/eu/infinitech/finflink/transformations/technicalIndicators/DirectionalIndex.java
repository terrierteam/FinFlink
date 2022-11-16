package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.List;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import org.apache.flink.streaming.api.windowing.time.Time;

import eu.infinitech.finflink.structures.TradePeriod;

/**
 * Positive High Price Movement minus Negative Low Price Movement
 * Indicates upper perceived value is increasing over the time period
 * @author Richard
 *
 */
public class DirectionalIndex extends TechnicalIndicatorGenerator{

	private static final long serialVersionUID = -7059169774556810819L;

	public DirectionalIndex() {}
	
	public DirectionalIndex(Time timePeriod) {
		setTimePeriod(timePeriod);

		setIndicatorRequirements(new IndicatorRequirements()
				.needsClose()
				.needsHigh()
				.needsLow());
	}
	
	@Override
	public double calculate(List<TradePeriod> periodsToConsider) {
		
		double hpDM = new HighPriceDirectionalIndex().calculate(periodsToConsider);
		double lpDM = new LowPriceDirectionalIndex().calculate(periodsToConsider);
		
		return 100*(Math.abs(hpDM-lpDM)/Math.abs(hpDM+lpDM));
	}

}

package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.List;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import org.apache.flink.streaming.api.windowing.time.Time;

import eu.infinitech.finflink.structures.TradePeriod;

/**
 * Low Price Directional Momentum divided by Average True Range.
 * This is a normalized version of Positive Price Directional Momentum that accounts for
 * how much movement on average there is within a time period.
 * @author Richard
 *
 */
public class LowPriceDirectionalIndex extends TechnicalIndicatorGenerator{

	private static final long serialVersionUID = -7059169774556810819L;

	public LowPriceDirectionalIndex() {}
	
	public LowPriceDirectionalIndex(Time timePeriod) {
		setTimePeriod(timePeriod);

		setIndicatorRequirements(new IndicatorRequirements()
				.needsClose()
				.needsHigh()
				.needsLow());
	}
	
	@Override
	public double calculate(List<TradePeriod> periodsToConsider) {
		
		double lsmDM = new LowPriceDirectionalMomentum().calculate(periodsToConsider);
		double atr = new AverageTrueRange().calculate(periodsToConsider);
		
		return 100*(lsmDM/atr);
	}
}

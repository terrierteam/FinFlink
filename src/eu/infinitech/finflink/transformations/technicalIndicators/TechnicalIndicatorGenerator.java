package eu.infinitech.finflink.transformations.technicalIndicators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import org.apache.flink.streaming.api.windowing.time.Time;

import eu.infinitech.finflink.structures.TradePeriod;

/**
 * An interface indicating that the implementing class can generate a technical
 * indicator for a set of time periods.
 * @author Richard
 *
 */
public abstract class TechnicalIndicatorGenerator implements Serializable{

	private static final long serialVersionUID = -8657180338593758211L;
	long timePeriod;
	IndicatorRequirements indicatorRequirements;
	
	/**
	 * Calculate the technical indicator value given a set of trade periods to
	 * consider.
	 * @param periodsToConsider
	 * @return
	 */
	public abstract double calculate(List<TradePeriod> periodsToConsider);
	
	/**
	 * For indicators that perform a comparisons between multiple time periods,
	 * this sets how long each time period should be. This interacts with the
	 * tumbling window size and span allow variants of each technical indicator
	 * to be calculated.
	 * @param numSplits
	 */
	public void setTimePeriodLength(Time timePeriod) {
		this.timePeriod = timePeriod.toMilliseconds();
	}
	
	/**
	 * Gets the length of each time period for calculation of this indicator
	 * @return
	 */
	public long getTimePeriod() {
		return timePeriod;
	}
	
	/**
	 * Returns a name for the indicator produced. Note that this is not unique
	 * because it can be calculated for different time periods
	 * @return
	 */
	public String getName() {
		return getClass().getSimpleName();
	}
	
	
	/**
	 * Gets configuration properties for this indicator generator
	 * @return
	 */
	public Map<String,String> getProperties() {
		return new HashMap<String,String>();
	}

	public void setTimePeriod(Time timePeriod) {
		this.timePeriod = timePeriod.toMilliseconds();
	}

	public void setTimePeriod(long timePeriod) {
		this.timePeriod = timePeriod;
	}

	public IndicatorRequirements getIndicatorRequirements() {
		return indicatorRequirements;
	}

	public void setIndicatorRequirements(IndicatorRequirements indicatorRequirements) {
		this.indicatorRequirements = indicatorRequirements;
	}
	
}

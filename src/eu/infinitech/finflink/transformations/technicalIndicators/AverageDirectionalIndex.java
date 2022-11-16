package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.List;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import org.apache.flink.streaming.api.windowing.time.Time;

import eu.infinitech.finflink.structures.TradePeriod;

/**
 * The average directional index (ADX) is a technical analysis indicator used
 * by some traders to determine the strength of a trend. The ADX makes use of 
 * a positive (+DI) and negative (-DI) directional indicator in addition to 
 * the trendline. The ADX identifies a strong trend when it is over 25 and 
 * a weak trend when it is below 20. Crossovers of the -DI and +DI lines 
 * can be used to generate trade signals. Usually, it is computed over a period of 14 days (n=14)
 * @author Richard
 *
 */
public class AverageDirectionalIndex extends TechnicalIndicatorGenerator{

	private static final long serialVersionUID = -1992817759187577157L;

	public AverageDirectionalIndex() {}
	
	public AverageDirectionalIndex(Time timePeriod) {
		setTimePeriod(timePeriod);

		setIndicatorRequirements(new IndicatorRequirements()
				.needsClose()
				.needsHigh()
				.needsLow());
	}
	
	@Override
	public double calculate(List<TradePeriod> periodsToConsider) {
		
		double dx = new DirectionalIndex().calculate(periodsToConsider);
		double atr = new AverageTrueRange().calculate(periodsToConsider);
		
		return ((periodsToConsider.size()-1)*(atr+dx))/periodsToConsider.size();
	}

}

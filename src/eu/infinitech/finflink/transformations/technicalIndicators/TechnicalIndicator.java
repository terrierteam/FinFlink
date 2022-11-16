package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.function.Supplier;

import org.apache.flink.api.common.functions.AggregateFunction;

import eu.infinitech.finflink.structures.Trade;

/**
 * Defines a Financial Technical Indicator. Technical indicators should be
 * calculated incrementally, i.e. when a new data point arrives, it should
 * be used to update the Technical Indicator state and value for the current
 * window.
 * 
 * The logic for any technical indicator is not found in this class but rather
 * the associated IndicatorState object (T), which acts as the accumulator for the
 * indicator as it is calculated over a time window. 
 * 
 * @author richardm
 *
 */
public class TechnicalIndicator<T extends IndicatorState> implements AggregateFunction<Trade, IndicatorState, Double> {

	private static final long serialVersionUID = 5501942456485932765L;

	Supplier<T> supplier;
	
	public TechnicalIndicator() {}
	
	public TechnicalIndicator(Supplier<T> supplier) {
		this.supplier = supplier;
	}
	
	
	@Override
	public IndicatorState add(Trade value, IndicatorState accumulator) {
		accumulator.add(value);
		return accumulator;
	}

	@Override
	public Double getResult(IndicatorState accumulator) {
		return accumulator.getResult();
	}

	@Override
	public IndicatorState merge(IndicatorState a, IndicatorState b) {
		a.merge(b);
		return a;
	}

	@Override
	public IndicatorState createAccumulator() {
		return supplier.get();
	}

	
}

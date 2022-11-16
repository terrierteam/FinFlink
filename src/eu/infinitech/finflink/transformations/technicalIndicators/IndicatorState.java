package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.function.Supplier;

import eu.infinitech.finflink.structures.Trade;

public interface IndicatorState extends Supplier<IndicatorState> {

	/**
	 * Gets the result of this technical indicator based on any trades seen so far in
	 * the current window 
	 * @return
	 */
	public Double getResult();
	
	/**
	 * Adds a new trade to the Indicator State
	 * @param value
	 * @param accumulator
	 * @return
	 */
	public void add(Trade value);
	
	/**
	 * Merges two indicator states together
	 * @param a
	 * @param b
	 * @return
	 */
	public void merge(IndicatorState accumulator);
	

	
}

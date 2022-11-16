package eu.infinitech.finflink.transformations.technicalIndicators;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.infinitech.finflink.structures.*;
import eu.infinitech.finflink.structures.TechnicalIndicator;
import org.apache.flink.api.common.functions.AggregateFunction;
import eu.infinitech.finflink.util.TradePeriodsGenerator;

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
public class TechnicalIndicatorPipeline implements AggregateFunction<TradingData, TradingDataAccumulator, TechnicalIndicators> {

	private static final long serialVersionUID = 5501942456485932765L;

	List<TechnicalIndicatorGenerator> pipeline;
	String pipelineID;

	InputStreamType inputStreamType;
	
	public TechnicalIndicatorPipeline() {
	}
	
	public TechnicalIndicatorPipeline(String pipelineID, InputStreamType inputStreamType, List<TechnicalIndicatorGenerator> pipeline) {
		this.pipeline = pipeline;
		this.inputStreamType = inputStreamType;
		this.pipelineID = pipelineID;
	}
	
	
	@Override
	public TradingDataAccumulator add(TradingData value, TradingDataAccumulator accumulator) {
		accumulator.getTradingData().add(value);
		return accumulator;
	}

	@Override
	public TechnicalIndicators getResult(TradingDataAccumulator accumulator) {
		
		// make sure the trades are in time order
		Collections.sort(accumulator.getTradingData());
		
		// Stage 1: Get the list of unique timer periods we have in this pipeline
		Set<Long> uniqueTimePeriods = new HashSet<Long>();
		for (TechnicalIndicatorGenerator generator : pipeline) uniqueTimePeriods.add(generator.getTimePeriod());
		
		// Stage 2: Produce the list of TradePeriod for each unique time period used by the indicators
		Map<Long, List<TradePeriod>> timePeriodMap = new HashMap<Long, List<TradePeriod>>();
		for (Long timePeriodSplit : uniqueTimePeriods) {
			timePeriodMap.put(timePeriodSplit, TradePeriodsGenerator.generateTradePeriods(inputStreamType, timePeriodSplit, accumulator.getTradingData()));
		}
		
		// Stage 3: Calculate the technical indicators
		TechnicalIndicators indicators = new TechnicalIndicators();
		for (TechnicalIndicatorGenerator generator : pipeline) {
			long timePeriod = generator.getTimePeriod();
			List<TradePeriod> tradePeriods = timePeriodMap.get(timePeriod);
			double technicalIndicatorValue = generator.calculate(tradePeriods);
			
			// Generate identifier for the indicator
			String indicatorID = pipelineID+"-"+generator.getName()+"-"+((1.0*timePeriod)/60);
			
			TechnicalIndicator indicator = new TechnicalIndicator(indicatorID, technicalIndicatorValue, generator.getProperties());
			indicators.getIndicators().add(indicator);
		}
		
		return indicators;
	}

	@Override
	public TradingDataAccumulator merge(TradingDataAccumulator a, TradingDataAccumulator b) {
		
		TradingDataAccumulator newAcc = createAccumulator();
		newAcc.getTradingData().addAll(a.getTradingData());
		newAcc.getTradingData().addAll(b.getTradingData());
		
		return newAcc;
	}

	@Override
	public TradingDataAccumulator createAccumulator() {
		return new TradingDataAccumulator();
	}

	public List<TechnicalIndicatorGenerator> getPipeline() {
		return pipeline;
	}

	public void setPipeline(List<TechnicalIndicatorGenerator> pipeline) {
		this.pipeline = pipeline;
	}

	public String getPipelineID() {
		return pipelineID;
	}

	public void setPipelineID(String pipelineID) {
		this.pipelineID = pipelineID;
	}

	

	
}

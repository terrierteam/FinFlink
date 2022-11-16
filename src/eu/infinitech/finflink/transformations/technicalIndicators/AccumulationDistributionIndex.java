package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.*;

public class AccumulationDistributionIndex extends TechnicalIndicatorGenerator{
    int shortSpan;
    int longSpan;

    public AccumulationDistributionIndex(Time timePeriod, int shortSpan, int longSpan) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsVolume()
                .needsClose()
                .needsHigh()
                .needsLow());

        this.shortSpan = shortSpan;
        this.longSpan = longSpan;

    }
    public AccumulationDistributionIndex(Time timePeriod) {
         new AccumulationDistributionIndex(timePeriod, 3, 10);
    }

    private double accDist(TradePeriod tp){
        return (2 * tp.getClosePrice() - (tp.getLowPrice() + tp.getHighPrice())
                / (tp.getHighPrice() -tp.getLowPrice()) * tp.getVolume());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < 2) return 0d;

        List<Double> accDist = periodsToConsider
                .stream()
                .map(this::accDist)
                .collect(Collectors.toList());

        List<Double> cumSumAccDist = cumSum(accDist);

        List<Double> ewm3AccDist = ewm(cumSumAccDist, shortSpan);
        List<Double> ewm10AccDist = ewm(cumSumAccDist, longSpan);

        return mean(ewm3AccDist) - mean(ewm10AccDist);
    }
}

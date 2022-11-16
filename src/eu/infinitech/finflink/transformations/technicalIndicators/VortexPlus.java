package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.shaded.guava30.com.google.common.collect.Streams;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.std;

public class VortexPlus extends TechnicalIndicatorGenerator{
    public VortexPlus(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsHigh()
                .needsLow());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < 2) return 0d;

        Stream<Double> currentHigh = periodsToConsider.subList(1, periodsToConsider.size())
                .stream()
                .map(TradePeriod::getHighPrice);

        Stream<Double> previousLow = periodsToConsider.subList(0, periodsToConsider.size() - 1)
                .stream()
                .map(TradePeriod::getLowPrice);

        double accHighMinusLow = Streams.zip(
                currentHigh,
                previousLow,
                (a, b) -> (a - b))
                .reduce(0d, Double::sum);

        double accTrueRange = Utils.trueRange(periodsToConsider)
                .stream()
                .reduce(0d, Double::sum);

        return accHighMinusLow / accTrueRange;

    }
}

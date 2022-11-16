package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.shaded.guava30.com.google.common.collect.Streams;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.mean;
import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.std;

public class Volatility extends TechnicalIndicatorGenerator{
    public Volatility(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < 2) return 0d;

        Stream<Double> currentClose = periodsToConsider.subList(1, periodsToConsider.size())
                .stream()
                .map(TradePeriod::getClosePrice);

        Stream<Double> previousClose = periodsToConsider.subList(0, periodsToConsider.size() - 1)
                .stream()
                .map(TradePeriod::getClosePrice);

        List<Double> returns = Streams.zip(
                currentClose,
                previousClose,
                (a, b) -> (a - b) / b)
                .map(e -> e > 0 ? Math.log(e) : (e < 0 ? -Math.log(-e) : 0))
                .collect(Collectors.toList());

        return std(returns);

    }
}

package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.*;

public class MovingAverageConvergenceDivergence extends TechnicalIndicatorGenerator{
    int shortSpan;
    int longSpan;

    public MovingAverageConvergenceDivergence(Time timePeriod, int shortSpan, int longSpan) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose());

        this.shortSpan = shortSpan;
        this.longSpan = longSpan;

    }
    public MovingAverageConvergenceDivergence(Time timePeriod) {
         new MovingAverageConvergenceDivergence(timePeriod, 12, 26);
    }


    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < 2) return 0d;

        List<Double> closePrices = periodsToConsider.stream()
                .map(TradePeriod::getClosePrice).collect(Collectors.toList());

        List<Double> shortEwmClose = ewm(closePrices, shortSpan);
        List<Double> longEwmClose = ewm(closePrices, longSpan);

        return mean(shortEwmClose) - mean(longEwmClose);
    }
}

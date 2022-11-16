package eu.infinitech.finflink.transformations.technicalIndicators;

import com.google.common.collect.Streams;
import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.diff;

public class Returns extends TechnicalIndicatorGenerator{
    int numPeriods;

    public Returns(Time timePeriod, int numPeriods) {
        setTimePeriod(timePeriod);
        this.numPeriods = numPeriods;

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < numPeriods + 1) return 0d;

        Double previousPrice = periodsToConsider.get(periodsToConsider.size() - (numPeriods + 1)).getClosePrice();
        Double currentPrice = periodsToConsider.get(periodsToConsider.size() - 1).getClosePrice();

        Double momentum = currentPrice - previousPrice;
        return momentum / previousPrice;
    }
}

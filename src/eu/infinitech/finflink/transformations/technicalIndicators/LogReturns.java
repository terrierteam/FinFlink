package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;

public class LogReturns extends TechnicalIndicatorGenerator{
    int numPeriods;

    public LogReturns(Time timePeriod, int numPeriods) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose());

        this.numPeriods = numPeriods;
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < numPeriods + 1) return 0d;

        double previousPrice = periodsToConsider.get(periodsToConsider.size() - (numPeriods + 1)).getClosePrice();
        double currentPrice = periodsToConsider.get(periodsToConsider.size() - 1).getClosePrice();

        double momentum = currentPrice - previousPrice;

        if (momentum == 0 || previousPrice == 0) return 0d;

        return Math.log(momentum / previousPrice);
    }
}

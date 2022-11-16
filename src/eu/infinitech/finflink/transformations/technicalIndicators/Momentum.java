package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;

public class Momentum extends TechnicalIndicatorGenerator{
    int numPeriods;

    public Momentum(int numPeriods) {
        this.numPeriods = numPeriods;
    }

    public Momentum(Time timePeriod, int numPeriods) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose());

        this.numPeriods = numPeriods;
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        // (df['close'] - df['close'].shift(1)) * df['volume']
        if (periodsToConsider.size() < numPeriods + 1) return 0d;

        TradePeriod last = periodsToConsider.get(periodsToConsider.size() - 1);
        TradePeriod numPeriodsPrevious = periodsToConsider.get(periodsToConsider.size() - (numPeriods + 1));

        return last.getClosePrice() - numPeriodsPrevious.getClosePrice();
    }
}

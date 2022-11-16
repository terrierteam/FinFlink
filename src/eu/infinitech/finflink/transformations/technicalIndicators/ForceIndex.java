package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;

public class ForceIndex extends TechnicalIndicatorGenerator{
    public ForceIndex(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose()
                .needsVolume());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        // (df['close'] - df['close'].shift(1)) * df['volume']
        if (periodsToConsider.size() < 2) return 0d;

        TradePeriod last = periodsToConsider.get(periodsToConsider.size() - 1);
        TradePeriod nextToLast = periodsToConsider.get(periodsToConsider.size() - 2);

        return (last.getClosePrice() - nextToLast.getClosePrice()) * last.getVolume();
    }
}

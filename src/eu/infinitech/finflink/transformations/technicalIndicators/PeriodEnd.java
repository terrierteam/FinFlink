package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;

public class PeriodEnd extends TechnicalIndicatorGenerator{
    public PeriodEnd(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        return periodsToConsider.get(periodsToConsider.size() - 1).getStopTime();
    }
}

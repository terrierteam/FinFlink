package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.mean;

public class DetrendedCloseOscillator extends TechnicalIndicatorGenerator{
    public DetrendedCloseOscillator(Time timePeriod){
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < 3) return 0d;

        int midPoint = (int) Math.ceil(periodsToConsider.size() / 2d);

        double midPrice = periodsToConsider.get(midPoint).getClosePrice();

        double smaPrice = mean(periodsToConsider.stream()
                .map(TradePeriod::getClosePrice)
                .collect(Collectors.toList()));

        return midPrice - smaPrice;
    }
}

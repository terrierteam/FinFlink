package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.*;

public class RelativeStrengthIndex extends TechnicalIndicatorGenerator{
    public RelativeStrengthIndex(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        // (df['close'] - df['close'].shift(1)) * df['volume']
        if (periodsToConsider.size() < 2) return 0d;

        List<Double> u = diff(periodsToConsider.stream()
                .map(TradePeriod::getClosePrice)
                .collect(Collectors.toList()));

        Stream<Double> d = u.stream().map(a -> -a);

        List<Double> up = u.stream().map(e -> e > 0 ? e : 0).collect(Collectors.toList());
        List<Double> down = d.map(e-> e < 0 ? e : 0).collect(Collectors.toList());

        double smoothMeanUp = mean(ewm(up, 14));
        double smoothMeanDown = mean(ewm(down, 14));

        return 100d - 100d / ((1d + smoothMeanUp) / smoothMeanDown);
    }
}

package eu.infinitech.finflink.transformations.technicalIndicators;

import com.google.common.collect.Streams;
import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.diff;

public class OnBalanceVolumeIndicator extends TechnicalIndicatorGenerator{
    public OnBalanceVolumeIndicator(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose()
                .needsVolume());
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        // (df['close'] - df['close'].shift(1)) * df['volume']
        if (periodsToConsider.size() < 2) return 0d;

        List<Double> m1s = diff(periodsToConsider.stream()
                .map(TradePeriod::getClosePrice)
                .collect(Collectors.toList()));

        Stream<Long> volumes = periodsToConsider.stream()
                .map(TradePeriod::getVolume);

        return Streams.zip(m1s.stream(),
                volumes,
                (a, b) -> a > 0 ? b : (a == 0 ? 0 : -b)).reduce(0L, Long::sum);

    }
}

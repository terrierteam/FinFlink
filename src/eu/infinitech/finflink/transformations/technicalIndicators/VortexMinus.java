package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.shaded.guava30.com.google.common.collect.Streams;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Stream;

public class VortexMinus extends TechnicalIndicatorGenerator{
    public VortexMinus(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsHigh()
                .needsLow());
    }

    /*
        vm_up = np.abs(df['high'] - df['low'].shift(1))
    vm_down = np.abs(df['low'] - df['high'].shift(1))

    tr_14 = df['tr'].rolling(window=14).sum()
    vm_up_14 = vm_up.rolling(window=14).sum()
    vm_down_14 = vm_down.rolling(window=14).sum()

    df['vi_14_plus'] = vm_up_14 / tr_14
    df['vi_14_neg'] = vm_down_14 / tr_14
     */
    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() < 2) return 0d;

        Stream<Double> currentLow = periodsToConsider.subList(1, periodsToConsider.size())
                .stream()
                .map(TradePeriod::getLowPrice);

        Stream<Double> previousHigh = periodsToConsider.subList(0, periodsToConsider.size() - 1)
                .stream()
                .map(TradePeriod::getHighPrice);

        double accHighMinusLow = Streams.zip(
                currentLow,
                previousHigh,
                (a, b) -> (a - b))
                .reduce(0d, Double::sum);

        double accTrueRange = Utils.trueRange(periodsToConsider)
                .stream()
                .reduce(0d, Double::sum);

        return accHighMinusLow / accTrueRange;

    }
}

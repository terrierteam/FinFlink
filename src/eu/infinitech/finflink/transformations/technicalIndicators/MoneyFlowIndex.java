package eu.infinitech.finflink.transformations.technicalIndicators;

import com.google.common.collect.Streams;
import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.stream.Collectors;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.diff;
import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.truePrice;


public class MoneyFlowIndex extends TechnicalIndicatorGenerator{
    public MoneyFlowIndex(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsVolume()
                .needsClose()
                .needsHigh()
                .needsLow());
    }

    private double truePriceVolume(TradePeriod tradePeriod){
        return truePrice(tradePeriod) * tradePeriod.getVolume();
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        // (df['close'] - df['close'].shift(1)) * df['volume']
        if (periodsToConsider.size() < 2) return 0d;

        List<Double> truePriceVolume = periodsToConsider.stream()
                .map(this::truePriceVolume)
                .collect(Collectors.toList());

        List<Double> truePriceDiff = diff(periodsToConsider.stream()
                .map(TradePeriod::getClosePrice)
                .collect(Collectors.toList()));

        Double posMF = Streams.zip(truePriceDiff.stream(),
                truePriceVolume.stream(),
                (a, b) -> (a > 0) ? b : 0).reduce(0d, Double::sum);

        Double negMF = Streams.zip(truePriceDiff.stream(),
                truePriceVolume.stream(),
                (a, b) -> (a < 0) ? b : 0).reduce(0d, Double::sum);

        return 100 * posMF / (posMF + negMF);
    }
}

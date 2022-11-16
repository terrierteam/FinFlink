package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.IndicatorRequirements;
import eu.infinitech.finflink.structures.TradePeriod;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.infinitech.finflink.transformations.technicalIndicators.Utils.*;

public class CommodityChannelIndex extends TechnicalIndicatorGenerator {
    public CommodityChannelIndex(Time timePeriod) {
        setTimePeriod(timePeriod);

        setIndicatorRequirements(new IndicatorRequirements()
                .needsClose()
                .needsHigh()
                .needsLow());
    }

    private double mad(List<Double> in) {
        Double inMean = mean(in);

        List<Double> diffMean = in.stream()
                .map(p -> p - inMean)
                .collect(Collectors.toList());

        return mean(diffMean);
    }



    private double cci(Double truePrice, Double truePriceSMA, Double truePriceMAD) {
        return 1 / 0.015 * ((truePrice - truePriceSMA) / truePriceMAD);
    }

    @Override
    public double calculate(List<TradePeriod> periodsToConsider) {
        if (periodsToConsider.size() <= 2) return 0d;

        List<Double> truePrices = periodsToConsider
                .stream()
                .map(Utils::truePrice)
                .collect(Collectors.toList());

        Double  truePricesSMA = mean(truePrices);

        Double truePricesMAD = mad(truePrices);

        return cci(truePrices.get(truePrices.size() - 1),
                truePricesSMA,
                truePricesMAD);
    }
}

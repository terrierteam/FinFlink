package eu.infinitech.finflink.transformations.technicalIndicators;

import eu.infinitech.finflink.structures.TradePeriod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
    public static double sum(List<Double> in){
        return in.stream().reduce(0d, Double::sum);
    }

    public static double mean(List<Double> in){
        return sum(in) / in.size();
    }

    public static double std(List<Double> in) {
        double m = mean(in);

        double squareResiduals = in.stream()
                .map(e -> e - m)
                .map(e -> e * e)
                .reduce(0d, Double::sum);

        return Math.sqrt(squareResiduals / in.size());
    }

    private static class CumSum {
        private Double acc;

        public double calculate(double value) {
            if (acc == null) {
                acc = value;
            } else {
                acc += value;
            }
            return acc;
        }
    }

    public static List<Double> cumSum(List<Double> in){
        CumSum cumSum = new CumSum();

        return in.stream()
                .map(cumSum::calculate)
                .collect(Collectors.toList());
    }

    private static class ExponentialMovingAverage {
        private final double alpha;
        private Double acc;
        public ExponentialMovingAverage(double span) {
            this.alpha = 2 / (1 + span);
        }

        public double calculate(double value) {
            if (acc == null) {
                acc = value;
            } else {
                acc = acc + alpha * (value - acc);
            }
            return acc;
        }
    }

    public static List<Double> ewm(List<Double> in, int span ){
        ExponentialMovingAverage exponentialMovingAverage = new ExponentialMovingAverage(span);

        return in.stream()
                .map(exponentialMovingAverage::calculate)
                .collect(Collectors.toList());
    }

    private static class TrueRange {
        private TradePeriod prev;

        public double calculate(TradePeriod period) {
            if (prev == null) {
                prev = period;
            }

            double lowNowPrevClose = Math.abs(period.getLowPrice()-prev.getClosePrice());
            double highLow = period.getHighPrice()-period.getLowPrice();
            double highNowPrevClose = Math.abs(period.getHighPrice()-prev.getClosePrice());

            prev = period;

            return Math.max(lowNowPrevClose, Math.max(highLow, highNowPrevClose));
        }

    }

    public static List<Double> trueRange(List<TradePeriod> in) {
        TrueRange trueRange = new TrueRange();

        return in.stream()
                .map(trueRange::calculate)
                .collect(Collectors.toList());
    }

    public static List<Double> diff(List<Double> in){
        Diff diff = new Diff();

        return in.stream()
                .map(diff::calculate)
                .collect(Collectors.toList());
    }

    private static class Diff {
        private Double prev;

        public double calculate(Double current) {
            if (prev == null) {
                prev = current;
            }

            double diff = current - prev;
            prev = current;

            return diff;
        }

    }

    public static double truePrice(TradePeriod tradePeriod){
        return (tradePeriod.getHighPrice() + tradePeriod.getLowPrice() + tradePeriod.getClosePrice()) / 3;
    }

    public static <T> Stream<List<T>> sliding(List<T> list, long size) {
        return IntStream.range(0, (int) (list.size()-size+1))
                .mapToObj(start -> list.subList(start,  start+ (int)size));
    }

    public static <T> Stream<List<T>> expanding(List<T> list) {
        return IntStream.range(0, (list.size() - 1))
                .mapToObj(end -> list.subList(0, end));
    }

}

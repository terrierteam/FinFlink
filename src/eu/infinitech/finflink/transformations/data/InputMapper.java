package eu.infinitech.finflink.transformations.data;

import eu.infinitech.finflink.structures.TradingData;
import org.apache.flink.api.common.functions.MapFunction;

public interface InputMapper extends MapFunction<String, TradingData> {
}

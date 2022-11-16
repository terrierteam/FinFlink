package eu.infinitech.finflink.sinks;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import eu.infinitech.finflink.structures.TechnicalIndicators;

public class PrintSink implements SinkFunction<TechnicalIndicators>{

	private static final long serialVersionUID = 8125267289665777316L;

	public void invoke(TechnicalIndicators value, Context context) throws Exception {
		System.err.println(value.toString());
	}
}

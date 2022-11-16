package eu.infinitech.finflink.apps;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.infinitech.finflink.sinks.QuestDBSink;
import eu.infinitech.finflink.structures.*;
import eu.infinitech.finflink.time.TradingDataTimeAssigner;
import eu.infinitech.finflink.transformations.data.InputParser;
import eu.infinitech.finflink.transformations.technicalIndicators.*;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import eu.infinitech.finflink.keys.KeyByAssetID;


/**
 * This class illustrates the application of a FinFlink feature extraction pipeline
 * over a static dataset. 
 * @author richardm
 *
 */
public class FinFlinkExample {

	enum Example {
		LocalTrades, RemoteTrades, TimeSeries
	}

	public static void main(String[] args) {
		// Set up the Flink Execution Environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<String> inputStream;
		InputStreamType inputStreamType;
		StreamCharacteristics streamCharacteristics;

		Time windowSize;
		Time calculationFrequency;
		Time tradingPeriodResolution;

		Example exampleInstance = Example.LocalTrades;

		switch (exampleInstance) {
			case LocalTrades:
				// load data
				inputStream = env.readTextFile("data/trade-aa.gz");
				// define stream type
				inputStreamType = InputStreamType.trade();
				// and characteristics (generate from stream type enum?)
				streamCharacteristics = new StreamCharacteristics()
						.hasOpen()
						.hasClose()
						.hasLow()
						.hasHigh()
						.hasVolume();
				// define windowing constants and tradingPeriod resolution
				windowSize = Time.minutes(30);
				calculationFrequency = Time.minutes(10);
				tradingPeriodResolution = Time.minutes(5);
				break;

			case RemoteTrades:
				KafkaSource<String> source = KafkaSource.<String>builder()
						.setBootstrapServers("kafka-service:9092")
						.setTopics("trades")
						.setGroupId("flink")
						.setStartingOffsets(OffsetsInitializer.earliest())
						.setValueOnlyDeserializer(new SimpleStringSchema())
						.build();

				inputStream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
				inputStreamType = InputStreamType.trade();
				streamCharacteristics = new StreamCharacteristics()
						.hasOpen()
						.hasClose()
						.hasLow()
						.hasHigh()
						.hasVolume();

				windowSize = Time.minutes(30);
				calculationFrequency = Time.minutes(10);
				tradingPeriodResolution = Time.minutes(5);
				break;
			case TimeSeries:
			default:
				inputStream = env.readTextFile("data/period-1d-aa.us.txt");
				inputStreamType = InputStreamType.pricePoint("aa");
				streamCharacteristics = new StreamCharacteristics()
						.hasOpen()
						.hasClose()
						.hasLow()
						.hasHigh()
						.hasVolume();

				windowSize = Time.days(30);
				calculationFrequency = Time.days(10);
				tradingPeriodResolution = Time.days(1);
				break;
		}

		// Convert the price stream to Trade or PricePoint based on stream type
		DataStream<TradingData> tradingDataStream = inputStream.map(InputParser.selectMapper(inputStreamType));

		
		// Use the unix date provided in each trade as the timestamp
		// Allow for 5 seconds of 'lateness' in trades (trades arriving out of order)
		// for TimeSeriesRow do similar
		WatermarkStrategy<TradingData> timestampHandler = WatermarkStrategy
				.<TradingData>forBoundedOutOfOrderness(Duration.ofSeconds(5))
				.withTimestampAssigner(new TradingDataTimeAssigner());
		
		WindowedStream<TradingData, String, TimeWindow> tradeAssetWindows = tradingDataStream
				.assignTimestampsAndWatermarks(timestampHandler) // Define Timestamps for the stream, since we are not using system time
				.keyBy(new KeyByAssetID()) // Group by Asset
				.window(SlidingEventTimeWindows.of(windowSize, calculationFrequency)); // Split into Sliding Windows



		// Defined Technical Indicators to Extract
		List<TechnicalIndicatorGenerator> indicatorGenerators = new ArrayList<TechnicalIndicatorGenerator>();

		indicatorGenerators.add(new PeriodEnd(tradingPeriodResolution));
		indicatorGenerators.add(new AverageTrueRange(tradingPeriodResolution));
		indicatorGenerators.add(new LowPriceDirectionalMomentum(tradingPeriodResolution));
		indicatorGenerators.add(new HighPriceDirectionalMomentum(tradingPeriodResolution));
		indicatorGenerators.add(new LowPriceDirectionalIndex(tradingPeriodResolution));
		indicatorGenerators.add(new HighPriceDirectionalIndex(tradingPeriodResolution));
		indicatorGenerators.add(new DirectionalIndex(tradingPeriodResolution));
		indicatorGenerators.add(new AverageDirectionalIndex(tradingPeriodResolution));

		// newly implemented
		indicatorGenerators.add(new AccumulationDistributionIndex(tradingPeriodResolution, 3, 10));
		indicatorGenerators.add(new CommodityChannelIndex(tradingPeriodResolution));
		indicatorGenerators.add(new DetrendedCloseOscillator(tradingPeriodResolution));
		indicatorGenerators.add(new ForceIndex(tradingPeriodResolution));
		indicatorGenerators.add(new LogReturns(tradingPeriodResolution, 3));
		indicatorGenerators.add(new Momentum(tradingPeriodResolution, 3));
		indicatorGenerators.add(new MoneyFlowIndex(tradingPeriodResolution));
		indicatorGenerators.add(new MovingAverageConvergenceDivergence(tradingPeriodResolution, 10, 26));
		indicatorGenerators.add(new OnBalanceVolumeIndicator(tradingPeriodResolution));
		indicatorGenerators.add(new RelativeStrengthIndex(tradingPeriodResolution));
		indicatorGenerators.add(new Returns(tradingPeriodResolution, 3));
		indicatorGenerators.add(new Volatility(tradingPeriodResolution));
		indicatorGenerators.add(new VortexMinus(tradingPeriodResolution));
		indicatorGenerators.add(new VortexPlus(tradingPeriodResolution));

		List<TechnicalIndicatorGenerator> validGenerators = indicatorGenerators
				.stream()
				.filter(streamCharacteristics::meetsRequirements)
				.collect(Collectors.toList());

		TechnicalIndicatorPipeline pipeline = new TechnicalIndicatorPipeline("2-1", inputStreamType, validGenerators);

		SingleOutputStreamOperator<TechnicalIndicators> technicalIndicators = tradeAssetWindows.aggregate(pipeline);
		
		//technicalIndicators.addSink(new PrintSink());

		List<String> columNames = validGenerators
				.stream()
				.map(TechnicalIndicatorGenerator::getName)
				.collect(Collectors.toList());

		technicalIndicators.addSink(new QuestDBSink("localhost:9009", columNames));

		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

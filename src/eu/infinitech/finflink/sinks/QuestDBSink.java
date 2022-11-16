package eu.infinitech.finflink.sinks;

import eu.infinitech.finflink.structures.TechnicalIndicators;
import io.questdb.cutlass.line.LineSenderException;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import io.questdb.client.Sender;

import java.util.List;

public class QuestDBSink implements SinkFunction<TechnicalIndicators>{
	private final List<String> columNames;
	private final String dbUrl;

	public QuestDBSink(String dbUrl, List<String> columNames) {
		this.columNames = columNames;

		String envUrl = System.getenv("QUESTDB_SERVICE");

		if (envUrl == null){
			this.dbUrl = dbUrl;
		} else {
		 	this.dbUrl = envUrl;
		}
	}

	public void invoke(TechnicalIndicators value, Context context) throws Exception {
		try (Sender sender = Sender.builder().address(this.dbUrl).build()) {
			double time = value.getIndicators().get(0).getValue();

			for (int i = 1; i < columNames.size(); i++) {
				String name = columNames.get(i);
				double indicatorValue = value.getIndicators().get(i).getValue();

				sender.table(name)
						.doubleColumn("value", indicatorValue)
						.longColumn("period end", (long) time)
						.atNow();
			}
		} catch(LineSenderException e) {
			System.err.println(e.toString());
			System.exit(1);
		}

	}
}

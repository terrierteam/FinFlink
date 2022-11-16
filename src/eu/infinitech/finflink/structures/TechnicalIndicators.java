package eu.infinitech.finflink.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A map containing generated technical indicators 
 * @author Richard
 *
 */
public class TechnicalIndicators implements Serializable{

	private static final long serialVersionUID = -5548823033926482013L;
	
	List<TechnicalIndicator> indicators;

	public TechnicalIndicators() {
		indicators = new ArrayList<TechnicalIndicator>();	
	}
	
	public TechnicalIndicators(List<TechnicalIndicator> indicators) {
		super();
		this.indicators = indicators;
	}

	public List<TechnicalIndicator> getIndicators() {
		return indicators;
	}

	public void setIndicators(List<TechnicalIndicator> indicators) {
		this.indicators = indicators;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		boolean first = true;
		for (TechnicalIndicator ti : indicators) {
			if (!first) {
				builder.append(", ");
			}
			first = false;
			builder.append(ti.getName());
			builder.append(":");
			builder.append(ti.value);
			
		}
		builder.append("]");
		return builder.toString();
	}
	
	
}

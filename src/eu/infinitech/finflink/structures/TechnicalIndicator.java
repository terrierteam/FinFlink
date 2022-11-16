package eu.infinitech.finflink.structures;

import java.io.Serializable;
import java.util.Map;

public class TechnicalIndicator implements Serializable{

	private static final long serialVersionUID = -6711313523820667064L;
	
	public String name;
	public double value;
	Map<String,String> properties;
	
	public TechnicalIndicator() {}
	
	public TechnicalIndicator(String name, double value, Map<String, String> properties) {
		super();
		this.name = name;
		this.value = value;
		this.properties = properties;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	
	
}

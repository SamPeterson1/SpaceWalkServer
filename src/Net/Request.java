package Net;

import java.util.HashMap;

public class Request {
	
	private HashMap<String, String> parameters;
	private String message;
	
	public Request(String message) {
		this.message = message;
		this.parameters = new HashMap<String, String>();
	}
	
	public void addParameter(String name, String value) {
		this.parameters.put(name, value);
	}
	
	public static Request parseString(String str) {
		String[] tokens = str.split(" ");
		Request retVal = new Request(tokens[0]);
		for(int i = 1; i < tokens.length; i ++) {
			retVal.addParameter(tokens[i].split(":")[0], tokens[i].split(":")[1]);
		}
		
		return retVal;
	}
	
	public String toString() {
		StringBuilder retVal = new StringBuilder(this.message);
		for(String key: parameters.keySet()) {
			retVal.append(" ").append(key).append(":").append(parameters.get(key));
		}
		
		return retVal.toString();
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getInt(String parameter) {
		return Integer.parseInt(this.parameters.get(parameter));
	}
	
	public String getString(String parameter) {
		return this.parameters.get(parameter);
	}
	
	public boolean getBoolean(String parameter) {
		return Boolean.valueOf(this.parameters.get(parameter));
	}
}

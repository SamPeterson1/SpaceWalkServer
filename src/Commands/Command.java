package Commands;

import Net.Request;

public interface Command {
	
	String getName();
	
	Request doJob();
	
}

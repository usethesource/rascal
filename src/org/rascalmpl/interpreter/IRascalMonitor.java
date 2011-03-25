package org.rascalmpl.interpreter;

public interface IRascalMonitor {
	/**
	 * Register a job with a name and a total amount of steps todo.
	 */
	public void startJob(String name, int totalWork);
	
	/**
	 * Log the <bold>start</bold> of an event. 
	 */
	public void event(String name);
	
	/**
	 * Log the start of an event with the amount of work that will be done when it's finished.
	 * An event is finished when the next event is logged, or when endJob() is called.
	 */
	public void event(String name, int inc);
	
	/**
	 * Log the start of an event with the amount of work that will be done when it's finished.
	 * An event is finished when the next event is logged, or when endJob() is called.
	 */
	public void event(int inc);
	
	/**
	 * This should always be called once for every startJob, unless an exception is thrown.
	 */
	public void endJob(boolean succeeded);
}

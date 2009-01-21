package org.meta_environment.rascal.interpreter.exceptions;

/*package*/ class FailureException extends RuntimeException {
	private static final long serialVersionUID = 2774285953244945424L;
	private String fLabel;
	private FailureException() { };
	
	private static class InstanceHolder {
		public static FailureException sInstance = new FailureException();
	}
	
	public static synchronized FailureException getInstance(String label) {
		return InstanceHolder.sInstance.setLabel(label);
	}
	
	public static synchronized FailureException getInstance() {
		return InstanceHolder.sInstance.setLabel(null);
	}
	
	private synchronized FailureException setLabel(String label) {
		fLabel = label;
		return this;
	}
	
	public String getLabel() {
		return fLabel;
	}
	
	public boolean hasLabel() {
		return fLabel != null;
	}
}

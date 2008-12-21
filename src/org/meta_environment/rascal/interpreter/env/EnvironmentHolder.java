package org.meta_environment.rascal.interpreter.env;

public class EnvironmentHolder {
  private Environment env;
  
  public void setEnvironment(Environment env) {
	  this.env = env;
  }
  
  public Environment getEnvironment() {
	  return env;
  }
}

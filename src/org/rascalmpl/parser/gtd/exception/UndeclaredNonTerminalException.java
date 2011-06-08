package org.rascalmpl.parser.gtd.exception;

public class UndeclaredNonTerminalException extends RuntimeException{
	private static final long serialVersionUID = 1584464650068099643L;
	
	private final String name;
	private final Class<?> clazz;
	
	public UndeclaredNonTerminalException(String name, Class<?> clazz){
		super();
		
		this.name = name;
		this.clazz = clazz;
	}
	
	public String getName(){
		return name;
	}
	
	public String getMessage(){
		return "Undeclared non-terminal: " + name + ", in class: "+clazz;
	}
}

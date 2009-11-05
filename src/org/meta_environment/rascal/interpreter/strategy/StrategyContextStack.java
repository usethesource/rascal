package org.meta_environment.rascal.interpreter.strategy;

import java.util.ArrayList;
import java.util.List;

public class StrategyContextStack{
	private final List<IStrategyContext> stack;
	
	public StrategyContextStack(){
		super();
		
		stack = new ArrayList<IStrategyContext>();
	}
	
	public void pushContext(IStrategyContext context){
		stack.add(context);
	}
	
	public void popContext(){
		int size = stack.size();
		if(size == 0) throw new RuntimeException("No strategy context available.");
		
		stack.remove(size - 1);
	}
	
	public IStrategyContext getCurrentContext(){
		int size = stack.size();
		if(size == 0) return null;
		
		return stack.get(size - 1);
	}
}

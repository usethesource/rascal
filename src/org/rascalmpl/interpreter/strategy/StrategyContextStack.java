/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.strategy;

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

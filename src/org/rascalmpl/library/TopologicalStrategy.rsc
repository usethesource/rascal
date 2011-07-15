@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module TopologicalStrategy

//TODO: This module will be removed when there are abstract functions in Rascal modules
// It will be merged with Strategy.rsc with abstract functions for the traversal operators 

@doc{Apply the strategy in argument to all the children of the subject using a topological order for binary relations.}
@javaClass{org.rascalmpl.library.Strategy}
public java &T(&T) makeTopologicalAll(&T(&T) strategy);

@doc{Apply the strategy in argument to one of the children of the subject using a topological order for binary relations.}
@javaClass{org.rascalmpl.library.Strategy}
public java &T(&T) makeTopologicalOne(&T(&T) strategy);

@javaClass{org.rascalmpl.library.Strategy}
@reflect{use the current strategy context}
public java value getCurrentStratCtx();

public &T1(&T1) topological_top_down(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
		&T3 res = strategy(subject);
		return makeTopologicalAll(topological_top_down(strategy))(res);
	};
}

public &T1(&T1) topological_bottom_up(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
		&T3 res = makeTopologicalAll(topological_bottom_up(strategy))(subject);
		return strategy(res);
	};
}

public &T1(&T1) topological_once_top_down(&T2(&T2) strategy) {
 	return &T3(&T3 subject) {
		&T3 res = strategy(subject);
		if (res == subject) {
			return makeTopologicalOne(topological_once_top_down(strategy))(res);
		} else {
			return res;
		}
	};
}

public &T1(&T1) topological_once_bottom_up(&T2(&T2) strategy) {
  return &T3(&T3 subject) {
       // TODO: the ctx is not initialized at the first call (for the moment, it returns a special value)
       value oldctx = getCurrentStratCtx();
	   &T3 res = makeTopologicalOne(topological_once_bottom_up(strategy))(subject);
	   value newctx = getCurrentStratCtx();
	   if (oldctx == newctx) {
			return strategy(res);
		} else {
			return res;
		}
	};
}

public &T1(&T1) topological_innermost(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
	   &T3 temp =  makeTopologicalAll(topological_innermost(strategy))(subject);
	   do {
	    	subject = temp;
	    	temp = strategy(subject);
	   	} while (subject != temp);
		return temp;
	};
}

// NOTE: This may, or may not, work for some bottom up stuff now,
// however it's currently only used for the topological_outermost strategy (which is top down).
public &T1(&T1) topological_repeat_strat(&T2(&T2) strategy) { 
  return &T3(&T3 subject) {
       &T3 temp = strategy(subject);
	   while (subject != temp) {
	    	subject = temp;
	    	temp = strategy(subject);
	   	}
		return temp;
	};
}

public &T1(&T1) topological_outermost(&T2(&T2) strategy) { 
	return topological_repeat_strat(topological_once_top_down(strategy));
}

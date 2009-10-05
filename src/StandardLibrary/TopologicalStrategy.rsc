module TopologicalStrategy

//TODO: This module will be removed when there are abstract functions in Rascal modules
// It will be merged with Strategy.rsc with abstract functions for the traversal operators 

@doc{Apply the strategy in argument to all the children of the subject using a topological order for binary relations.}
@javaClass{org.meta_environment.rascal.interpreter.strategy.topological.TopologicalAll}
public &T(&T) java makeTopologicalAll(&T(&T) strategy);

@doc{Apply the strategy in argument to one of the children of the subject using a topological order for binary relations.}
@javaClass{org.meta_environment.rascal.interpreter.strategy.topological.TopologicalOne}
public &T(&T) java makeTopologicalOne(&T(&T) strategy);

@javaClass{org.meta_environment.rascal.interpreter.strategy.topological.TopologicalStrategy}
public &T(&T) java makeTopologicalStrategy(&T(&T) strategy);


@javaClass{org.meta_environment.rascal.interpreter.strategy.ContextualStrategy}
@reflect{use the current strategy context}
public value java getCurrentStratCtx();

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
       value oldctx = getCurrentStratCtx();
		&T3 res = makeTopologicalOne(topological_once_bottom_up(strategy))(subject);
		if (oldctx == getCurrentStratCtx()) {
			return strategy(res);
		} else {
			return res;
		}
	};
}

public &T1(&T1) topological_repeat_strat(&T2(&T2) strategy) { 
  return &T3(&T3 subject) {
       value ctx = getCurrentStratCtx();
       &T3 temp = strategy(subject);
	   while (ctx != getCurrentStratCtx()) {
	    	subject = temp;
	    	ctx = getCurrentStratCtx();
	    	temp = strategy(subject);
	   	}
		return temp;
	};
}

public &T1(&T1) topological_innermost(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
	   value ctx = getCurrentStratCtx();
	   &T3 temp =  makeTopologicalAll(topological_innermost(strategy))(subject);
	   do {
	    	subject = temp;
	    	ctx = getCurrentStratCtx();
	    	temp = strategy(subject);
	   	} while (ctx != getCurrentStratCtx());
		return temp;
	};
}

public &T1(&T1) topological_outermost(&T2(&T2) strategy) { 
	return topological_repeat_strat(topological_once_top_down(strategy));
}
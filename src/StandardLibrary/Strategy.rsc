module Strategy

@doc{Apply the strategy given in argument to all the children of the subject.}
@javaClass{org.meta_environment.rascal.interpreter.strategy.All}
public &T(&T) java makeAll(&T(&T) strategy);

public &T1(&T1) bottom_up(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
		&T3 res = makeAll(bottom_up(strategy))(subject);
		return strategy(res);
	};
}

public &T1(&T1) top_down(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
		&T3 res = strategy(subject);
		return makeAll(top_down(strategy))(res);
	};
}

public &T1(&T1) innermost(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
	   &T3 temp =  makeAll(innermost(strategy))(subject);
	   do {
	    	subject = temp;
	   		temp = strategy(subject);
	   	} while (subject != temp);
		return temp;
	};
}

public &T1(&T1) outermost(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
	   &T3 temp = subject;
	   do {
	    	subject = temp;
	   		temp = strategy(subject);
	   	} while (subject != temp);
		return makeAll(outermost(strategy))(temp);
	};
}



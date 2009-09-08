module Strategy

@doc{Apply the strategy given in argument to all the children of the subject.}
@javaClass{org.meta_environment.rascal.interpreter.strategy.All}
public &T(&T) java makeAll(&T(&T) strategy);

public &T(&T) bottom_up(&T(&T) strategy) { 
	return &T(&T subject) {
		&T res = makeAll(bottom_up(strategy))(subject);
		return strategy(res);
	};
}

public &T(&T) top_down(&T(&T) strategy) { 
	return &T(&T subject) {
		&T res = strategy(subject);
		return makeAll(top_down(strategy))(res);
	};
}

public &T(&T) innermost(&T(&T) strategy) { 
	return &T(&T subject) {
	   &T temp =  makeAll(innermost(strategy))(subject);
	   while(temp != subject) {
	    subject = temp;
	    temp = innermost(strategy)(subject);
	   }
	   return temp;
	};
}
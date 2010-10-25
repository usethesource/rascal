module Strategy

@doc{Apply the function if the argument is of the same type and returns identity otherwise.}
@javaClass{org.rascalmpl.library.Strategy}
public &T(&T) java functionToStrategy(value function);

@doc{Apply the strategy given in argument to all the children of the subject.}
@javaClass{org.rascalmpl.library.Strategy}
public &T(&T) java makeAll(&T(&T) strategy);

@doc{Apply the strategy given in argument to one of the children of the subject.}
@javaClass{org.rascalmpl.library.Strategy}
public &T(&T) java makeOne(&T(&T) strategy);


public &T1(&T1) top_down(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
		&T3 res = strategy(subject);
		return makeAll(top_down(strategy))(res);
	};
}

public &T1(&T1) bottom_up(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
		&T3 res = makeAll(bottom_up(strategy))(subject);
		return strategy(res);
	};
}

public &T1(&T1) once_top_down(&T2(&T2) strategy) {
 	return &T3(&T3 subject) {
		&T3 res = strategy(subject);
		if (res == subject) {
			return makeOne(once_top_down(strategy))(res);
		} else {
			return res;
		}
	};
}

public &T1(&T1) once_bottom_up(&T2(&T2) strategy) {
  return &T3(&T3 subject) {
		&T3 res = makeOne(once_bottom_up(strategy))(subject);
		if (res == subject) {
			return strategy(res);
		} else {
			return res;
		}
	};
}

public &T1(&T1) repeat_strat(&T2(&T2) strategy) { 
  return &T3(&T3 subject) {
	   &T3 temp = strategy(subject);
	   while (temp != subject) {
	    	subject = temp;
	   		temp = strategy(subject);
	   	}
		return temp;
	};
}

public &T1(&T1) innermost(&T2(&T2) strategy) { 
	return &T3(&T3 subject) {
	   &T3 temp =  makeAll(innermost(strategy))(subject);
	   do {
	    	subject = temp;
	   		temp = strategy(subject);
	   	} while (temp != subject);
		return temp;
	};
}

public &T1(&T1) outermost(&T2(&T2) strategy) { 
	return repeat_strat(once_top_down(strategy));
}

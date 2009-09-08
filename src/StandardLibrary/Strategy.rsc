module Strategy

@doc{Apply the strategy given in argument to all the children of the subject.}
@javaClass{org.meta_environment.rascal.interpreter.strategy.All}
public &T java applyAll(&T(&T) strategy, &T subject);
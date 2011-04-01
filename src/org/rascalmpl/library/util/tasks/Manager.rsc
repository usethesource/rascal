module util::tasks::Manager

alias Task = void (Transaction tr, type[&T] key, &N name);
@reflect{Needs access to context in order to reify types when calling producer.}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public void java registerProducer(Task producer, set[value] keys);

public void registerProducer(&T (Transaction tr, type[&T] key, &N name) producer, set[value] keys) {
	registerProducer(void(Transaction t,type[&T] k, value n){v = producer(t,k,n); setFact(t,k,n,v);});
}

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public void java unregisterProducer(void (Transaction tr, type[&T] key,  value name) producer);

alias Transaction = value;
alias Fact = value;

@reflect{Access to evaluator's error stream}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public Transaction java startTransaction();

@reflect{Access to evaluator's error stream}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public Transaction java startTransaction(Transaction parent);

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public void java endTransaction(Transaction tr);

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public void java abandonTransaction(Transaction tr);


@reflect{For producing exceptions with stack traces}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public &T java getFact(Transaction tr, type[&T] key, value name);

@reflect{For producing exceptions with stack traces}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public &T java queryFact(Transaction tr, type[&T] key, value name);

public bool hasFact(Transaction tr, type[&T] key, value name) {
	return queryFact(tr, key, name)?;
}

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public void java removeFact(Transaction tr, type[&T] key, value name);

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public Fact java setFact(Transaction tr, type[&T] key, value name, &T val);

@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)}
module util::tasks::Manager

alias Task[&T,&N] = bool (Transaction tr, type[&T] key, &N name);
@reflect{
Needs access to context in order to reify types when calling producer.
}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java void registerProducer(Task[&T,&N] producer, set[value] keys);
//public void registerProducer(&T (Transaction tr, type[&T] key, &N name) producer, set[value] keys) {
//	registerProducer(bool(Transaction t,type[&T] k, value n){v = producer(t,k,n); setFact(t,k,n,v);});
//}

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java void unregisterProducer(Task[&T,&N] producer);

@synopsis{Lock the producer registry ‚Äì use this before adding/removing a
	set of related producers. Remember to enclose the critical section in
	try { ...¬†} finally { unlockProducerRegistry(); }}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
private java void lockProducerRegistry();

@javaClass{org.rascalmpl.library.util.tasks.Manager}
private java void unlockProducerRegistry();

public void registryTransaction(void() f) {
	lockProducerRegistry();
	try {
		f();
	}
	catch "ATotallyFakeException": ;
	finally {
		unlockProducerRegistry();
	}
}
alias Transaction = value;
alias Fact = value;

@reflect{
Access to evaluator's error stream
}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java Transaction startTransaction();

@reflect{
Access to evaluator's error stream
}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java Transaction startTransaction(Transaction parent);

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java void endTransaction(Transaction tr);

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java void abandonTransaction(Transaction tr);


@reflect{
For producing exceptions with stack traces
}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java &T getFact(Transaction tr, type[&T] key, value name);

@reflect{
For producing exceptions with stack traces
}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java &T queryFact(Transaction tr, type[&T] key, value name);

public bool hasFact(Transaction tr, type[&T] key, value name) {
    try {
	   queryFact(tr, key, name);
	   return true;
	} catch _: {
	   return false;
	}
    
}

@reflect{
For producing exceptions with stack traces
}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java void removeFact(Transaction tr, type[&T] key, value name);

@reflect{
For producing exceptions with stack traces
}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java void setFact(Transaction tr, type[&T] key, value name, &T val);

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public java tuple[rel[str,str,str,int],rel[str,str,str]] getDependencyGraph(Transaction tr);

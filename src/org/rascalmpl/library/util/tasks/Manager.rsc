@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anya Helene Bagge - A.H.S.Bagge@cwi.nl (Univ. Bergen)}
module util::tasks::Manager

alias Task = bool (Transaction tr, type[&T] key, &N name);
@reflect{Needs access to context in order to reify types when calling producer.}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public void java registerProducer(Task producer, set[value] keys);

//public void registerProducer(&T (Transaction tr, type[&T] key, &N name) producer, set[value] keys) {
//	registerProducer(bool(Transaction t,type[&T] k, value n){v = producer(t,k,n); setFact(t,k,n,v);});
//}

@javaClass{org.rascalmpl.library.util.tasks.Manager}
public void java unregisterProducer(Task producer);

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

@reflect{For producing exceptions with stack traces}
@javaClass{org.rascalmpl.library.util.tasks.Manager}
public Fact java setFact(Transaction tr, type[&T] key, value name, &T val);

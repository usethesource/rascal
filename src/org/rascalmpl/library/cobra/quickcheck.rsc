@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::quickcheck

public bool quickcheck( value func ){
	return _quickcheck( func, true, false );
}

public bool quickcheck( value func, int maxDepth, int maxTries ){
	return _quickcheck( func, true, false, maxDepth, maxTries);
}

public bool silentQuickcheck( value func ){
	return _quickcheck( func, false, false );
}

public bool silentQuickcheck( value func, int maxDepth, int maxTries ){
	return _quickcheck( func, false, false, maxDepth, maxTries);
}

public bool verboseQuickcheck( value func ){
	return _quickcheck( func, true, true );
}  
 
public bool verboseQuickcheck( value func, int maxDepth, int maxTries ){
	return _quickcheck( func, true, true, maxDepth, maxTries);
} 

@javaClass{org.rascalmpl.library.cobra.Cobra}
@reflect
private java bool _quickcheck( value func, bool verbose, bool maxVerbose, int maxDepth, int tries);

@javaClass{org.rascalmpl.library.cobra.Cobra}
@reflect
private java bool _quickcheck( value func, bool verbose, bool maxVerbose );

@javaClass{org.rascalmpl.library.cobra.Cobra}
@reflect
public java &T arbitrary(type[&T] reified, int depthLimit );

@javaClass{org.rascalmpl.library.cobra.Cobra}
public java bool setGenerator( &T (int) generator );

@javaClass{org.rascalmpl.library.cobra.Cobra}
public java bool resetGenerator( type[&T] reified );

@javaClass{org.rascalmpl.library.cobra.Cobra}
@reflect
public java &T (int) getGenerator( type[&T] reified );

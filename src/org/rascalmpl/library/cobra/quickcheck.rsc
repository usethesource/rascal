module cobra::quickcheck

public bool quickcheck( value func ){
	return _quickcheck( func, 5, true, false, 100);
}

public bool quickcheck( value func, int maxDepth ){
	return _quickcheck( func, maxDepth, true, false, 100);
}

public bool quickcheck( value func, int maxDepth, int maxTries ){
	return _quickcheck( func, maxDepth, true, false, maxTries);
}

public bool silentQuickcheck( value func, int maxDepth ){
	return _quickcheck( func, maxDepth, false, false, 100);
}

public bool silentQuickcheck( value func, int maxDepth, int maxTries ){
	return _quickcheck( func, maxDepth, false, false, maxTries);
}

public bool verboseQuickcheck( value func ){
	return _quickcheck( func, 5, true, true, 100);
}  

public bool verboseQuickcheck( value func, int maxDepth ){
	return _quickcheck( func, maxDepth, true, true, 100);
}  
 
public bool verboseQuickcheck( value func, int maxDepth, int maxTries ){
	return _quickcheck( func, maxDepth, true, true, maxTries);
} 

@javaClass{org.rascalmpl.library.cobra.Cobra}
@reflect
private java bool _quickcheck( value func, int maxDepth, bool verbose, bool maxVerbose, int tries);

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

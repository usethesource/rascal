module Real

@doc{Returns an arbitrary real value in the interval [0.0,1.0).}
@javaClass{org.rascalmpl.library.Real}
public real java arbReal();

@doc{Largest of two reals}
public real max(real n, real m)
{
	return n > m ? n : m;
}

@doc{Smallest of two reals}
public real min(real n, real m)
{
	return n < m ? n : m;
}

@doc{Convert a real to integer.}
@javaClass{org.rascalmpl.library.Real}
public int java toInt(real d);

@doc{Convert a real to a string.}
@javaClass{org.rascalmpl.library.Real}
public str java toString(real d);

@doc{Round to the nearest integer}
@javaClass{org.rascalmpl.library.Real}
public real java round(real d);

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

@doc{Returns the constant PI}
@javaClass{org.rascalmpl.library.Real}
public real java PI();

@doc{Returns the constant E}
@javaClass{org.rascalmpl.library.Real}
public real java E();

@doc{Computes the power of x by y}
@javaClass{org.rascalmpl.library.Real}
public real java pow(real x, real y);

@doc{Computes exp(x)}
@javaClass{org.rascalmpl.library.Real}
public real java exp(real x);

@doc{Computes sin(x)}
@javaClass{org.rascalmpl.library.Real}
public real java sin(real x);

@doc{Computes cos(x)}
@javaClass{org.rascalmpl.library.Real}
public real java cos(real x);

@doc{Computes tan(x)}
@javaClass{org.rascalmpl.library.Real}
public real java tan(real x);

@doc{Computes sqrt(x)}
@javaClass{org.rascalmpl.library.Real}
public real java sqrt(real x);
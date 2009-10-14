module Real

/*
 * Functions on reals:
 * - arbReal
 * - max
 * - min
 * - toInt
 * - toString
 */

@doc{Returns an arbitrary real value in the interval [0.0,1.0).}
@javaClass{org.meta_environment.rascal.library.Real}
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
@javaClass{org.meta_environment.rascal.library.Real}
public int java toInt(real d);

@doc{Convert a real to a string.}
@javaClass{org.meta_environment.rascal.library.Real}
public str java toString(real d);

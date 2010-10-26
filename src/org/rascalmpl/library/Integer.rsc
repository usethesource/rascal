module Integer

@doc{Absolute value of integer.}
public int abs(int N)
{
	return N >= 0 ? N : -N;
}

@doc{Return an arbitrary integer value.}
@javaClass{org.rascalmpl.library.Integer}
public int java arbInt();

@doc{Return an arbitrary integer value in the interval [0, limit).}
@javaClass{org.rascalmpl.library.Integer}
public int java arbInt(int limit);

@doc{Largest of two integers.}
public int max(int n, int m)
{
	return n > m ? n : m;
}

@doc{Smallest of two integers.}
public int min(int n, int m)
{
	return n < m ? n : m;
}

@doc{Convert an integer value to a real value.}
@javaClass{org.rascalmpl.library.Integer}
public real java toReal(int n);

@doc{Convert an integer value to a string.}
@javaClass{org.rascalmpl.library.Integer}
public str java toString(int n);
module Integer

public int abs(int N)
@doc{abs -- absolute value of integer}
{
	return N >= 0 ? N : -N;
}


public int java arbInt()
@doc{arbInt -- return an arbitrary integer value}
@javaClass{org.meta_environment.rascal.std.Integer};

public int java arbInt(int limit)
@doc{arbInt -- return an arbitrary integer value in the interval [0, limit).}
@javaClass{org.meta_environment.rascal.std.Integer};

public int max(int n, int m)
@doc{max -- largest of two integers}
{
	return n > m ? n : m;
}

public int min(int n, int m)
@doc{max -- smallest of two integers}
{
	return n < m ? n : m;
}

public real java toReal(int n)
@doc{toReal -- convert an integer value to a real value.}
@javaClass{org.meta_environment.rascal.std.Integer};

public str java toString(int n)
@doc{toString -- convert an integer value to a string.}
@javaClass{org.meta_environment.rascal.std.Integer};

module Number

@doc{Absolute value of integer.}
public &T <: num abs(&T <: num N)
{
	return N >= 0 ? N : -N;
}

@doc{Return an arbitrary integer value.}
@javaClass{org.rascalmpl.library.Number}
public int java arbInt();

@doc{Return an arbitrary integer value in the interval [0, limit).}
@javaClass{org.rascalmpl.library.Integer}
public int java arbInt(int limit);

@doc{Returns an arbitrary real value in the interval [0.0,1.0).}
@javaClass{org.rascalmpl.library.Real}
public real java arbReal();

@doc{Largest of two numbers.}
public &T <: num max(&T <: num N, &T <: num M)
{
	return N > M ? N : M;
}

@doc{Smallest of two numbers.}
public &T <: num min(&T <: num N, &T <: num M)
{
	return N < M ? N : M;
}

@doc{Convert a number to an integer.}
@javaClass{org.rascalmpl.library.Number}
public int java toInt(num N);

@doc{Convert a number value to a real value.}
@javaClass{org.rascalmpl.library.Number}
public real java toReal(num N);

@doc{Convert a number value to a string.}
@javaClass{org.rascalmpl.library.Number}
public str java toString(num N);
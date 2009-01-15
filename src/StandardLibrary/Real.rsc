module Real

public real java arbReal()
@doc{arbReal -- returns an arbitrary real value in the interval [0.0,1.0).}
{
  return values.dubble(random.nextDouble());
}

public real max(real n, real m)
@doc{max -- largest of two reals}
{
	return n > m ? n : m;
}

public real min(real n, real m)
@doc{min -- smallest of two reals}
{
	return n < m ? n : m;
}

public int java toInteger(real d)
@doc{toInteger -- convert a real to integer.}
{
  return d.toInteger();
}

public str java toString(real d)
@doc{toString -- convert a real to a string.}
{
  return values.string(d.toString());
}

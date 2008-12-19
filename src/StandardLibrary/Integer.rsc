module Integer

public int java arb(int limit)
@doc{arb -- return an arbitrary integer value in the interval [0, limit).}
{
   return values.integer(random.nextInt(limit.getValue()));
}

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

public double java toDouble(int n)
@doc{toDouble -- convert an integer value to a double value.}
{
  return n.toDouble();
}

public str java toString(int n)
@doc{toString -- convert an integer value to a string.}
{
  return values.string(n.toString());
}
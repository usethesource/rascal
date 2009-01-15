module Integer


public int java arbInt()
@doc{arbInt -- return an arbitrary integer value}
{
   return values.integer(random.nextInt());
}

public int java arbInt(int limit)
@doc{arbInt -- return an arbitrary integer value in the interval [0, limit).}
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

public real java toReal(int n)
@doc{toReal -- convert an integer value to a real value.}
{
  return n.toDouble();
}

public str java toString(int n)
@doc{toString -- convert an integer value to a string.}
{
  return values.string(n.toString());
}
module Double

public double java arbDouble()
@doc{arbDouble -- returns an arbitrary double value in the interval [0.0,1.0).}
{
  return values.dubble(random.nextDouble());
}

public double max(double n, double m)
@doc{max -- largest of two doubles}
{
	return n > m ? n : m;
}

public double min(double n, double m)
@doc{min -- smallest of two doubles}
{
	return n < m ? n : m;
}

public int java toInteger(double d)
@doc{toInteger -- convert a double to integer.}
{
  return d.toInteger();
}

public str java toString(double d)
@doc{toString -- convert a double to a string.}
{
  return values.string(d.toString());
}

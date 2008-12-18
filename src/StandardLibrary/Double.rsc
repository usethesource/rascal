module Double

public double java arbDouble()
@java-imports{import java.util.Random;}
@doc{arbDouble -- returns an arbitrary double value in the interval [0.0,1.0).}
{
  return values.dubble(new Random().nextDouble());
}

public double max(double n, double m)
{
	return n > m ? n : m;
}

public double min(double n, double m)
{
	return n < m ? n : m;
}

public int java toInteger(double d)
@doc{toInteger a double value integer.}
{
  return d.toInteger();
}

public str java toString(double d)
@doc{toString converts a double value to a string.}
{
  return values.string(d.toString());
}

module Double

public double java arb()
@java-imports{import java.lang.Math;}
@doc{arb returns an arbitrary double value in the interval [0.0,1.0).}
{
  return values.dubble(Math.random());
}

public double java toInteger(double d)
@doc{toInteger a double value integer.}
{
  return d.floor();
}

public str java toString(double d)
@doc{toString converts a double value to a string.}
{
  return values.string(d.toString());
}

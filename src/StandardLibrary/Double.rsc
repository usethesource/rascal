module Double

public double java arb()
@doc{arb returns an arbitrary double value in the interval [0.0,1.0).}
JavaImports{java.util.Random;}
{
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());
  return generator.nextDouble();
}

public double java toInteger(double d)
@doc{toInteger a double value integer.}
{
  return Math.round(d);
}

public str java toString(double d)
@doc{toString converts a double value to a string.}
{
  return d.toString();
}
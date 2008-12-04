module Integer

public int java arb(int limit)
@doc{arb returns an arbitrary ineteger value in the interval [0, limit).}
@java-imports{import java.lang.Math;}
{
   IDouble rnd = values.dubble(Math.random() * limit.getValue());
   return rnd.floor();
}

public double java toDouble(int n)
@doc{toDouble converts an integer value to a double value.}
{
  return n.toDouble();
}

public str java toString(int n)
@doc{toString converts an integer value to a string.}
{
  return values.string(n.toString());
}
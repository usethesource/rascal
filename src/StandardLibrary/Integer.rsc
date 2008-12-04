module Integers

public int java arb(int limit)
@doc{arb returns an arbitrary ineteger value in the interval [0, limit).}
JavaImports{java.util.Random;}
{
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());
  return generator.nextInteger(limit);
}

public double java toDouble(int n)
@doc{toDouble converts an integer value to a double value.}
{
  return n * 1.0;
}

public str java toString(int n)
@doc{toString converts an integer value to a string.}
{
  return n.toString();
}
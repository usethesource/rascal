module Integers

public int java arb(int limit)
JavaImports{java.util.Random;}
{
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());
  return generator.nextInteger(limit);
}

public double java toDouble(int n)
{
  return n * 1.0;
}

public str java toString(int n)
{
  return n.toString();
}
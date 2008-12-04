module Booleans

public bool java arb()
@doc{arb returns an arbitrary boolean value.}
JavaImports{java.util.Random;}
{
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());
  return generator.next Boolean();
}

public int java toInt(bool b)
@doc{toInt converts a boolean value to integer.}
{
  return b ? 1 : 0;
}

public double java toDouble(bool b)
@doc{toDouble converts a boolean value to a double value.}
{
  return b ? 1.0 : 0.0;
}

public str java toString(bool b)
@doc{toString converts a boolean value to a string.}
{
  return b ? "true" : "false;
}

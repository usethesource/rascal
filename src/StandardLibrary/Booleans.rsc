module Booleans

public bool java arb()
JavaImports{java.util.Random;}
{
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());
  return generator.next Boolean();
}

public int java toInt(bool b)
{
  return b ? 1 : 0;
}

public double java toDouble(bool b)
{
  return b ? 1.0 : 0.0;
}

public str java toString(bool b)
{
  return b ? "true" : "false;
}

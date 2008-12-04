module Booleans

public bool java arb()
@java-imports{import java.util.Random;}
@doc{arb returns an arbitrary boolean value.}
{
  Random generator = new Random(System.currentTimeMillis());
  return values.bool(generator.next());
}

public int java toInt(bool b)
@doc{toInt converts a boolean value to integer.}
{
  return values.integer(b ? 1 : 0);
}

public double java toDouble(bool b)
@doc{toDouble converts a boolean value to a double value.}
{
  return values.dubble(b ? 1.0 : 0.0);
}

public str java toString(bool b)
@doc{toString converts a boolean value to a string.}
{
  return values.string(b ? "true" : "false");
}

public bool fromString(str s) {
  return "true" == s;
}


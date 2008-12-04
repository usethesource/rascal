module Booleans

public bool java arb()
@java-imports{import java.lang.Math;}
@doc{arb returns an arbitrary boolean value.}
{
  return values.bool(Math.random() > 0.5);
}

public int java toInt(bool b)
@doc{toInt converts a boolean value to integer.}
{
  return values.integer(b.getValue() ? 1 : 0);
}

public double java toDouble(bool b)
@doc{toDouble converts a boolean value to a double value.}
{
  return values.dubble(b.getValue() ? 1.0 : 0.0);
}

public str java toString(bool b)
@doc{toString converts a boolean value to a string.}
{
  return values.string(b.getValue() ? "true" : "false");
}

public bool java fromString(str s) {
  return values.bool("true".equals(s.getValue()));
}


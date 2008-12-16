module Boolean

public bool java arbBool()
@doc{arbBool -- get an arbitrary boolean value.}
{
  return values.bool(random.nextInt(2) == 1);
}

public int java toInt(bool b)
@doc{toInt -- convert a boolean value to integer.}
{
  return values.integer(b.getValue() ? 1 : 0);
}

public double java toDouble(bool b)
@doc{toDouble -- convert a boolean value to a double value.}
{
  return values.dubble(b.getValue() ? 1.0 : 0.0);
}

public str java toString(bool b)
@doc{toString -- convert a boolean value to a string.}
{
  return values.string(b.getValue() ? "true" : "false");
}


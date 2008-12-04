module Double

public double java arb()
JavaImports{java.util.Random;}
{
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());
  return generator.nextDouble();
}

public double java toIntegere(double d)
{
  return Math.round(d);
}

public str java toString(double d)
{
  return d.toString();
}
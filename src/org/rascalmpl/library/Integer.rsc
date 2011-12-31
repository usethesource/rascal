@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
//@deprecated{Use "import Math;" instead}
module Integer

@doc{Absolute value of integer.}
public int abs(int N)
{
	return N >= 0 ? N : -N;
}

@doc{Return an arbitrary integer value.}
@javaClass{org.rascalmpl.library.Integer}
public java int arbInt();

@doc{Return an arbitrary integer value in the interval [0, limit).}
@javaClass{org.rascalmpl.library.Integer}
public java int arbInt(int limit);

@doc{Largest of two integers.}
public int max(int n, int m)
{
	return n > m ? n : m;
}

@doc{Smallest of two integers.}
public int min(int n, int m)
{
	return n < m ? n : m;
}

@doc{Convert an integer value to a real value.}
@javaClass{org.rascalmpl.library.Integer}
public java real toReal(int n);

@doc{Convert an integer value to a rational value.}
@javaClass{org.rascalmpl.library.Integer}
public java real toRational(int n);

@doc{Convert an integer value to a string.}
@javaClass{org.rascalmpl.library.Integer}
public java str toString(int n);

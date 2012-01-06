/*****************************/
/* DEPRECATED                */
/* Use util::Math            */
/* DO NOT EDIT               */
/*****************************/

@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anya Helene Bagge - anya@ii.uib.no}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Rational

/*** Moved to util::Math
@doc{An arbitrary rational.}
public rat arbRat() {
	n = arbInt();
	d = arbInt();
	if(d == 0)
		d = 1;
	return toRat(n, d);
}
@doc{Absolute value of rational.}
public rat abs(rat N)
{
	return N >= 0 ? N : -N;
}

@doc{Largest of two rationals.}
public rat max(rat n, rat m)
{
	return n > m ? n : m;
}

@doc{Smallest of two rationals.}
public rat min(rat n, rat m)
{
	return n < m ? n : m;
}
*/

@doc{Convert a rational value to an integer value. (Will round or truncate.)}
@javaClass{org.rascalmpl.library.util.Math}
public java int toInt(rat n);

@doc{Return the rational's numerator}
@javaClass{org.rascalmpl.library.Prelude}
public java int numerator(rat n);

@doc{Return the rational's denominator}
@javaClass{org.rascalmpl.library.Prelude}
public java int denominator(rat n);

@doc{Return the remainder of dividing the numerator by the denominator}
@javaClass{org.rascalmpl.library.Prelude}
public java int remainder(rat n);

@doc{Convert a given numerator and denominator to a rational value.}
@javaClass{org.rascalmpl.library.util.Math}
public java real toRat(int numerator, int denominator);

@doc{Convert a rational value to a real value.}
@javaClass{org.rascalmpl.library.Rational}
public java real toReal(rat n);

@doc{Convert a rational value to a string.}
@javaClass{org.rascalmpl.library.util.Math}
public java str toString(rat n);

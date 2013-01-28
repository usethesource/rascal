/*****************************/
/* DEPRECATED                */
/* Use util::Math            */
/* DO NOT EDIT               */
/*****************************/

@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}

@deprecated{Use "import util::Math;" instead.}
module Number

import Real;

@doc{Absolute value of integer.}
public &T <: num abs(&T <: num N)
{
	return N >= 0 ? N : -N;
}

@doc{Return an arbitrary integer value.}
@javaClass{org.rascalmpl.library.util.Math}
public java int arbInt();

@doc{Return an arbitrary integer value in the interval [0, limit).}
@javaClass{org.rascalmpl.library.util.Math}
public java int arbInt(int limit);

@doc{Returns an arbitrary real value in the interval [0.0,1.0).}
@javaClass{org.rascalmpl.library.util.Math}
public java real arbReal();

@doc{Round to the nearest integer}
public num round(num d) {
    return toInt(round(toReal(d)));
    }

@doc{Largest of two numbers.}
public &T <: num max(&T <: num N, &T <: num M)
{
	return N > M ? N : M;
}

@doc{Smallest of two numbers.}
public &T <: num min(&T <: num N, &T <: num M)
{
	return N < M ? N : M;
}

@doc{Convert a number to an integer.}
@javaClass{org.rascalmpl.library.util.Math}
public java int toInt(num N);

@doc{Convert a number value to a real value.}
@javaClass{org.rascalmpl.library.util.Math}
public java real toReal(num N);

//@doc{Convert a number value to a rational value (not supported on reals).}
//@javaClass{org.rascalmpl.library.util.Math}
//public java rat toRational(num N);

@doc{Convert a number value to a string.}
@javaClass{org.rascalmpl.library.util.Math}
public java str toString(num N);

@doc{pi -- returns the constant PI}
@javaClass{org.rascalmpl.library.util.Math}
public java real PI();

@doc{e -- returns the constant E}
@javaClass{org.rascalmpl.library.util.Math}
public java real E();

@doc{computes the power of x by y}
public real pow(num x, int y) {
    return util::Math::pow(toReal(x), y);
}

@doc{computes exp(x)}
public real exp(num x) {
    return util::Math::exp(toReal(x));
}

@doc{computes sin(x)}
public real sin(num x) {
    return util::Math::sin(toReal(x));
}

@doc{computes cos(x)}
public real cos(num x) {
    return util::Math::cos(toReal(x));
}

@doc{computes tan(x)}
public real tan(num x) {
    return util::Math::tan(toReal(x));
}

@doc{computes sqrt(x)}
public real sqrt(num x) {
    return util::Math::sqrt(toReal(x));
}

@doc{computes n-th root of x}
public real nroot(num x, int n) {
    return util::Math::nroot(toReal(x), n);
}

@doc{computes log_base_(x)}
public real log(num x, num base) {
    return util::Math::log(toReal(x), toReal(base));
}

@doc{computes natural log of x}
public real ln(num x) {
	return util::Math::ln(toReal(x));
}

@doc{computes 10 based log(x)}
public real log10(num x) {
    return util::Math::log10(toReal(x));
}

@doc{Computes the 2 based log(x)}
public real log2(num x) {
	return util::Math::log2(toReal(x));
}


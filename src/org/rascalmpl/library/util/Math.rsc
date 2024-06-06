@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}

@synopsis{Mathematical functions.}
@description{
The `Math` library provides the following functions:

(((TOC)))
}
module util::Math

import List;
import Exception;


@synopsis{Absolute value of a number.}
@description{
Absolute value of the number `n`. The result type is equal to the type of the argument `n`.
}
@examples{
```rascal-shell
import util::Math;
abs(13)
abs(-13)
abs(3.14)
abs(-3.14)
```
}
public &T <: num abs(&T <: num N)
{
	return N >= 0 ? N : -N;
}


@synopsis{Generate a random integer value.}
@description{
Return an arbitrary integer value. When the argument `limit` is given, the generated value is in the interval [0, `limit`),
i.e., the limit is exclusive.
}
@examples{
```rascal-shell
import util::Math;
arbInt();
arbInt();
arbInt();
arbInt(10);
arbInt(10);
arbInt(10);
```
}
@benefits{
`arbInt` is a convenient generator for pseudo-random integers.
}
@javaClass{org.rascalmpl.library.util.Math}
public java int arbInt();

@javaClass{org.rascalmpl.library.util.Math}
public java int arbInt(int limit);


@synopsis{Generate a random real value in the interval [0.0,1.0).}
@description{
Generates an arbitrary real value in the interval [0.0, 1.0].
}
@examples{
```rascal-shell
import util::Math;
arbReal();
arbReal();
arbReal();
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real arbReal();


@synopsis{Define the seed for the generation of arbitrary values.}
@description{
Define the seed for the generation of arbitrary values such as ((arbBool)), ((arbInt)), ((arbReal)),
((arbRat)), ((List-getOneFrom)),((Set-getOneFrom)), ((List-takeOneFrom)) and ((Set-takeOneFrom)). ((arbSeed)) resets the random number generator that
is used to choose arbitrary values. This can be used to generate a reproducible series of choices.
}
@javaClass{org.rascalmpl.library.util.Math}
public java void arbSeed(int seed);


@synopsis{Generate an arbitrary rational value.}
@examples{
```rascal-shell
import util::Math;
arbRat();
arbRat();
arbRat();
arbRat(10,100);
arbRat(10,100);
arbRat(10,100);
```
}

public rat arbRat() {
	n = arbInt();
	d = arbInt();
	if(d == 0)
		d = 1;
	return toRat(n, d);
}

public rat arbRat(int limit1, int limit2) {
	n = arbInt(limit1);
	d = arbInt(limit2);
	if(d == 0)
		d = 1;
	return toRat(n, d);
}


@synopsis{Compute the smallest integer that is larger than a given number.}
@description{
Computes the _ceiling_ of a given number.
Also see ((util::Math::floor)).
}
@examples{
```rascal-shell
import util::Math;
ceil(3.2);
ceil(-3.2);
```
}
public int ceil(num x) { 
	int i = toInt(x);
	if (i == x || x < 0) {
		return i;
	}
	else {
		return i + 1;	
	}
}


@synopsis{Calculate the cosine of a numeric value.}
@description{
The cosine of the number `x`.
}
@examples{
```rascal-shell
import util::Math;
cos(1.0)
cos(60 * PI() / 180)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real cos(num x);


@synopsis{Return the denominator of a rational value.}
@javaClass{org.rascalmpl.library.util.Math}
public java int denominator(rat n);


@synopsis{The constant E.}
@examples{
```rascal-shell
import util::Math;
E();
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real E();



@synopsis{Compute exp(x).}
@description{
Calculate `e`<sup>`x`</sup>.
}
@javaClass{org.rascalmpl.library.util.Math}
public java real exp(num x);


@synopsis{Compute the largest integer that is smaller than a given number.}
@description{
Computes the _floor_ of a given number.
Also see ((util::Math::ceil)).
}
@examples{
```rascal-shell
import util::Math;
floor(3.2);
floor(-3.2);
```
}
public int floor(num x) {
	i = toInt(x);
	if (i == x || x >= 0) {
		return i;
	}
	else {
		return i - 1;	
	} 
}


@synopsis{Calculate the natural log of a numeric value.}
@description{
Calculate natural log of `x`.
}
@examples{
```rascal-shell
import util::Math;
ln(20.0)
ln(42.0)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real ln(num x);


@synopsis{Calculate the log<sub>base</sub> of a numeric value.}
@description{
Calculate log<sub>base</sub> of `x`.
}
@examples{
```rascal-shell
import util::Math;
log(9.99999999, 10)
log(10, 10)
log(256.0, 2)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real log(num x, num base);


@synopsis{Compute the 10 based log(x).}
public real log10(num x) = log(x, 10.0);


@synopsis{Compute the 2 based log(x).}
public real log2(num x) = log(x, 2.0);


@synopsis{Determine the largest of two numeric values.}
@description{
The largest of two numbers. The type of the result is the same as the type of the largest argument.
}
@examples{
```rascal-shell
import util::Math;
max(12, 13);
max(12, 13.5);
max(12, 11.5);
```
}
public &T <: num max(&T <: num N, &T <: num M)
{
	return N > M ? N : M;
}


@synopsis{Determine the smallest of two numeric values.}
@description{
The smallest of two numbers. The type of the result is the same as the type of the smallest argument.
}
@examples{
```rascal-shell
import util::Math;
min(12, 13);
min(12, -13);
min(3.14, 4);
```
}
public &T <: num min(&T <: num N, &T <: num M)
{
	return N < M ? N : M;
}


@synopsis{Return the numerator of a rational value.}
@javaClass{org.rascalmpl.library.util.Math}
public java int numerator(rat n);


@synopsis{Calculate the n<sup>th</sup> root of a numeric value.}
@description{
Calculate <sup>n</sup>&radic;`x` where `n` can only be a integer.
}
@examples{
```rascal-shell
import util::Math;
nroot(42 * 42, 2);
nroot(42 * 42 * 42, 3);
nroot(123456789012345678901234567890123456789.0, 100)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real nroot(num x, int n);


@synopsis{The constant pi.}
@examples{
```rascal-shell
import util::Math;
PI();
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real PI();


@synopsis{Calculate an arbitrary power of a numeric value.}
@description{
The calculate `x`<sup>`y`</sup> where `y` can only be a integer.
}
@examples{
```rascal-shell
import util::Math;
pow(sqrt(42), 2)
pow(12345678901234567890.0, 1000)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real pow(num x, int y);

@synopsis{Calculate an arbitrary power of a numeric value.}
@description{
The calculate `x`<sup>`y`</sup> where `y` can be any real value.
}
@examples{
```rascal-shell
import util::Math;
pow(sqrt(42), 2.3)
pow(12345678901234567890.0, 100.2)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real pow(num x, real y);


@synopsis{Return the precision of a real number.}
@javaClass{org.rascalmpl.library.util.Math}
public java int precision(num x);




@synopsis{Return a real number with given precision}
@javaClass{org.rascalmpl.library.util.Math}
public java real precision(num x, int p);


@synopsis{Define the precision for numeric calculations; returns the previous precision.}
@javaClass{org.rascalmpl.library.util.Math}
public java int setPrecision(int p);


@synopsis{Return the scale of a real number.}
@javaClass{org.rascalmpl.library.util.Math}
public java int scale(num x);


@synopsis{Return the unscaled integer of a real.}
@javaClass{org.rascalmpl.library.util.Math}
public java int unscaled(real x);


@synopsis{Return the remainder of dividing the numerator by the denominator.}
@javaClass{org.rascalmpl.library.util.Math}
public java int remainder(rat n);


@synopsis{Round a number to the nearest multiple of a given number (default 1).}
@examples{
```rascal-shell
import util::Math;
round(3.4);
round(3.5);
round(3.6);
round(-3.4);
round(-3.5);
round(-3.6);
round(13, 5);
round(1.5,0.2);
round(3r2,1r4);
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java int round(num d);
public (&T <: num) round(&T <: num r, &T <: num nearest) = round(r / (nearest * 1.0)) * nearest;



@synopsis{p
Push real value into a float using coercion and return the value represented by that float as a real}
@description{
The function fitFloat converts the unlimited precision real into a JVM float value.
}
@benefits{
* This function comes in handy in combination with random real test values which have to 
go through coercion in a Java library, like so:
 `bool test myTest(real r, real j) = fitFloat(r) + fitFloat(j) == fitFloat(r) + fitFloat(j);`
}
@pitfalls{
* If the real is smaller than the minimum float value or larger than the maximum float
value, this function will throw an ArithmeticException.
}
@javaClass{org.rascalmpl.library.util.Math}
public java real fitFloat(real r) throws ArithmeticException;


@synopsis{Push real value into a JVM double using coercion and return the value represented by that float as a real}
@description{
The function fitDouble converts the unlimited precision real into a JVM double value.
}
@benefits{
* This function comes in handy in combination with random real test values which have to 
go through coercion in a Java library, like so:
 `bool test myTest(real r, real j) = fitDouble(r) + fitDouble(j) == fitDouble(r) + fitDouble(j);`
}
@pitfalls{
* If the real is smaller than the minimum double value or larger than the maximum double
value, this function will throw an ArithmeticException.
}
@javaClass{org.rascalmpl.library.util.Math}
public java real fitDouble(real r) throws ArithmeticException;


@synopsis{Compute the ratio between two numbers as a percentage.}
@examples{
```rascal-shell
import util::Math;
percent(1r4, 1);
percent(13,250);
percent(80.0,160.0);
```
}
public int percent(num part, num whole) = round((part / (whole * 1.0)) * 100);


@synopsis{Calculate the sine of a numeric value.}
@description{
The sine of the number `x`.
}
@examples{
```rascal-shell
import util::Math;
sin(0)
sin(PI() / 2)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real sin(num x);


@synopsis{Calculate the square root of a numeric value.}
@description{
Calculate &radic;`x`.
}
@examples{
```rascal-shell
import util::Math;
sqrt(42 * 42);
sqrt(12345678901234567890.5 * 12345678901234567890.5);
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real sqrt(num x);


@synopsis{Calculate the tangent of a numeric value.}
@description{
The tangent of the number `x`.
}
@examples{
```rascal-shell
import util::Math;
tan(45 * PI() / 180)
```
}

@javaClass{org.rascalmpl.library.util.Math}
public java real tan(num x);


@synopsis{Convert a numeric value to an integer.}
@description{
Convert a number to an integer. If `n` is an integer, this is the identity. If `n` is a real value (implemented as BigDecimal) to an integer (implemented as BigInteger). This conversion is analogous to a narrowing primitive conversion from double to long as defined in the Java Language Specification: any fractional part of this BigDecimal will be discarded. Note that this conversion can loose information about the precision of the BigDecimal value.
}
@examples{
```rascal-shell
import util::Math;
toInt(13)
toInt(13.5)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java int toInt(num N);


@synopsis{Convert two numbers to a rational value (not supported on reals).}
@javaClass{org.rascalmpl.library.util.Math}
public java rat toRat(int numerator, int denominator);


@synopsis{Convert a numeric value to a real.}
@examples{
```rascal-shell
import util::Math;
toReal(12)
toReal(3.14)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java real toReal(num N);


@synopsis{Convert a numeric value to a string.}
@examples{
```rascal-shell
import util::Math;
toString(12)
toString(3.14)
```
}
@javaClass{org.rascalmpl.library.util.Math}
public java str toString(num N);


@synopsis{generate prime numbers up to a maximum}
@memo
public list[int] primes(int upTo)  // TODO: replaced "p <- ..." by "int p <- ..." to help new typechecker
  = [p | int p <- [2..upTo], p < 4 || all(i <- [2..toInt(sqrt(p))+1], p != i ? p % i != 0 : true)];
// Some test code: https://gist.github.com/grammarware/839f63b1a4999992ade7

// TODO: replaced "ps :=" by "list[int] ps :=" to help new typechecker
public int arbPrime(int upTo) = ps[arbInt(size(ps))] when list[int] ps := primes(upTo);

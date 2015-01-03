@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::arbitrary

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java int arbInt(int min, int limit);

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java int arbInt();

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java str arbString(int maxlength);

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java str arbStringWhitespace(int length);

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java str arbStringAlphabetic(int length);

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java str arbStringAlphanumeric(int length);

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java str arbStringAscii(int length);

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java str arbStringNumeric(int length);	

public real arbReal(){
	return arbReal(-9999.0,9999.0);
}

@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java datetime arbDateTime();


@javaClass{org.rascalmpl.library.cobra.Arbitrary}
public java real arbReal(real min, real max);

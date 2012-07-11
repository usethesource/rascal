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

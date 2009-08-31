module ValueIO

@doc{Read  a value from a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO}
public value java readValueFromBinaryFile(str namePBFFile);
	
@doc{Read a value from a text file}
@javaClass{org.meta_environment.rascal.std.ValueIO}
public value java readValueFromTextFile(str namePBFFile);
	
@doc{Write a value to a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO}
public void java writeValueToBinaryFile(str namePBFFile, value val);
	
@doc{Write a value to a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO}
public void java writeValueToTextFile(str namePBFFile, value val);
	

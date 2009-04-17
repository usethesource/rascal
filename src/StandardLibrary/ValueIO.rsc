module ValueIO

public value java readValueFromBinaryFile(str namePBFFile)
@doc{readValueFromBinaryFile -- read  a value from a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
public value java readValueFromTextFile(str namePBFFile)
@doc{readValueFromTextFile -- read a value from a text file}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
public void java writeValueToBinaryFile(str namePBFFile, value val)
@doc{writeValueToBinaryFile -- write a value to a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
public void java writeValueToTextFile(str namePBFFile, value val)
@doc{writeValueToTextFile -- write a value to a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
module ValueIO

public value readValueFromBinaryFile(str namePBFFile)
@doc{readValueFromBinaryFile -- read  a value from a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
public value readValueFromTextFile(str namePBFFile)
@doc{readValueFromTextFile -- read a value from a text file}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
public void writeValueToBinaryFile(str namePBFFile, value val)
@doc{writeValueToBinaryFile -- write a value to a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
public void writeValueToTextFile(str namePBFFile, value val)
@doc{writeValueToTextFile -- write a value to a binary file in PBF format}
@javaClass{org.meta_environment.rascal.std.ValueIO};
	
module AUT

@doc{Read relations from an AUT file. An AUT file contains tuples of ternary relation as lines with the following format
   (<int>,<str>,<int>)
   
   where each field is separated by a comma 
   readAUT takes an AUT file nameAUTFile and generates rel[int, str,int]].

}
@javaClass{org.rascalmpl.library.AUT}
public rel[int, str, int] java readAUT(str nameAUTFile);

@doc{write an AUT file}
@javaClass{org.rascalmpl.library.AUT}
public void java writeAUT(str nameAUTFile, rel[int, str, int] r);


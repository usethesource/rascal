@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rsf::IO

@synopsis{Read an RSF file.

Read relations from an RSF file. An RSF file contains tuples of binary relations
in the following format:
    RelationName Arg1 Arg2
where each field is separated by a tabulation character (\t). One file may contain tuples for more than one relation. readRSF takes an RSF file nameRSFFile and generates a map[str,rel[str,str]] that maps each relation name to the actual relation.}
@javaClass{org.rascalmpl.library.lang.rsf.RSFIO}
public java map[str, rel[str,str]] readRSF(loc nameRSFFile);

@javaClass{org.rascalmpl.library.lang.rsf.RSFIO}
public java map[str, type[value]] getRSFTypes(loc location);

@javaClass{org.rascalmpl.library.lang.rsf.RSFIO}
public java &T readRSFRelation(type[&T] result, str name, loc location);

@resource{
rsf
}
@synopsis{The RSF schema should be given as:
    rsf+rascal-file-uri
  where rascal-file-uri is a standard Rascal URI, for instance:
    rsf+file:///tmp/myRSFFile.rsf
  or
    rsf+project://MyProject/src/data/myRSFFile.rsf}
public str generate(str moduleName, loc uri) {
    
    // Retrieve the relation names and their types
    map[str, type[value]] rsfRels = getRSFTypes(uri);
    
    return  "module <moduleName>
            'import lang::rsf::IO;
            '<for(rname <- rsfRels){>
                 'public <rsfRels[rname]> <rname>() {
                 '  return readRSFRelation(#<rsfRels[rname]>, \"<rname>\", <uri>);
                 '}
            '<}>
            '";
}

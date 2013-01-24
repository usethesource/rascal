@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Jeroen van den Bos - Jeroen.van.den.Bos@cwi.nl (CWI)}
module experiments::Subversion

/*
 * Library functions on Subversion repositories:
 * - setCredentials
 * - getRevisions
 * - readRepositoryFile
 * - readRepositoryFileLines
 * - getRepositoryFileList
 * - getRepositoryDirectoryList
 *
 * TODO:
 * - getInfo (retrieve additional metadata)
 *
 */

@doc{Set the credentials to use when accessing Subversion repositories}
@javaClass{org.rascalmpl.library.experiments.Subversion}
public java void setCredentials(str name, str password);

@doc{Return all versions of a file that exists in a specified repository}
@javaClass{org.rascalmpl.library.experiments.Subversion}
public java list[int] getRevisions(loc file)
throws Subversion;

@doc{Return the contents of a provided location as a string}
@javaClass{org.rascalmpl.library.experiments.Subversion}
public java str readRepositoryFile(loc file, int revision)
throws Subversion;

@doc{Return the contents of a provided location as a list of strings (one for each line)}
@javaClass{org.rascalmpl.library.experiments.Subversion}
public java list[str] readRepositoryFileLines(loc file, int revision)
throws Subversion;

@doc{Return all files in the provided directory location as a list of locations}
@javaClass{org.rascalmpl.library.experiments.Subversion}
public java list[loc] getRepositoryFileList(loc directory, int revision)
throws Subversion;

@doc{Return all directories in the provided directory location as a list of locations}
@javaClass{org.rascalmpl.library.experiments.Subversion}
public java list[loc] getRepositoryDirectoryList(loc directory, int revision)
throws Subversion;

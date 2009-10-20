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
@javaClass{org.meta_environment.rascal.library.experiments.Subversion}
public void java setCredentials(str name, str password);

@doc{Return all versions of a file that exists in a specified repository}
@javaClass{org.meta_environment.rascal.library.experiments.Subversion}
public list[int] java getRevisions(loc file)
throws Subversion;

@doc{Return the contents of a provided location as a string}
@javaClass{org.meta_environment.rascal.library.experiments.Subversion}
public str java readRepositoryFile(loc file, int revision)
throws Subversion;

@doc{Return the contents of a provided location as a list of strings (one for each line)}
@javaClass{org.meta_environment.rascal.library.experiments.Subversion}
public list[str] java readRepositoryFileLines(loc file, int revision)
throws Subversion;

@doc{Return all files in the provided directory location as a list of locations}
@javaClass{org.meta_environment.rascal.library.experiments.Subversion}
public list[loc] java getRepositoryFileList(loc directory, int revision)
throws Subversion;

@doc{Return all directories in the provided directory location as a list of locations}
@javaClass{org.meta_environment.rascal.library.experiments.Subversion}
public list[loc] java getRepositoryDirectoryList(loc directory, int revision)
throws Subversion;

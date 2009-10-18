module experiments::Subversion

/*
 * Library functions on Subversion repositories:
 * - setCredentials
 * - getRevisions
 * - readRepositoryFile
 * - readRepositoryFileLines
 *
 * TODO:
 * - getInfo (retrieve additional metadata)
 * - getDirectoryListing (or some other mechanism to traverse a repository)
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

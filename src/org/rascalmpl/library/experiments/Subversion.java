package org.rascalmpl.library.experiments;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Implements the Rascal library's standard support for accessing Subversion repositories. 
 */
public class Subversion {

	private static final TypeFactory _typeFactory = TypeFactory.getInstance();
    private static final IValueFactory _valueFactory = ValueFactoryFactory.getValueFactory();
    
    private static ISVNAuthenticationManager _authenticationManager = SVNWCUtil.createDefaultAuthenticationManager();

    static {
		// Setup SVNKit to we support the native svn/svn+ssh, http/https DAV and file protocols.
		SVNRepositoryFactoryImpl.setup();
		DAVRepositoryFactory.setup();
		FSRepositoryFactory.setup();
    }

    /**
     * Set the credentials to use along with Subversion's servers configuration settings. These credentials
     * will be used on all subsequent calls that access a Subversion repository through this interface.
     * @param name The user name to use.
     * @param password The password to use.
     */
    public static void setCredentials(IString name, IString password) {
		_authenticationManager = SVNWCUtil.createDefaultAuthenticationManager(name.getValue(), password.getValue());
    }

    /**
     * Retrieves all revision numbers of the provided location. May throw an exception of type <b>Subversion</b>
     * in case of a Subversion-related error.
     * @param l A valid Subversion location including repository and path.
     * @return An <b>IList</b> of <b>IInteger</b>s representing the revisions of the provided location.
     */
	public static IList getRevisions(ISourceLocation l) {
		IListWriter writer = _valueFactory.listWriter(_typeFactory.integerType());
		try {
			SVNRepository repository = createRepository(l);
			// Create collection to pass in (and not use returned) to prevent an unchecked cast warning.
			ArrayList<SVNLogEntry> entries = new ArrayList<SVNLogEntry>();
			// SVNRepository.log instead of SVNRepository.getFileRevisions because it supports directories.
			repository.log(new String[] {""}, entries, 0, repository.getLatestRevision(), false, false);
			for (SVNLogEntry entry : entries) {
				// TODO: Use long factory method if it gets added.
				// For now, convert to string instead of casting to int to prevent data loss.
				writer.append(_valueFactory.integer(Long.toString(entry.getRevision())));
			}
		} catch (SVNException e) {
			throw RuntimeExceptionFactory.subversionException(e.getMessage(), null, null);
		}
		return writer.done();
	}
	
	/**
	 * Retrieves the contents of the provided location at the provided revision number, if it exists, as a
	 * single String. May throw an exception of type <b>Subversion</b> in case of a Subversion-related error or
	 * if the provided combination of location and revision number does not point to a file.
	 * @param file A valid Subversion location including repository and path pointing to a file.
	 * @param revision A valid revision number.
	 * @return The contents of the provided location at the provided revision number as a single <b>IString</b>
	 */
	public static IString readRepositoryFile(ISourceLocation file, IInteger revision) {
		return _valueFactory.string(readRepositoryFileContents(file, revision));
	}
	
	/**
	 * Retrieves the contents of the provided location at the provided revision number, if it exists, as a list
	 * of strings, one for each line. May throw an exception of type <b>Subversion</b> in case of a Subversion-
	 * related error, or if the provided combination of location and revision number does not point to a file.
	 * @param file A valid Subversion location including repository and path pointing to a file.
	 * @param revision A valid revision number.
	 * @return The contents of the provided location at the provided revision number as an <b>IList</b> of
	 * <b>IString</b>s, one for each line.
	 */
	public static IList readRepositoryFileLines(ISourceLocation file, IInteger revision) {
		IListWriter writer = _valueFactory.listWriter(_typeFactory.stringType());
		java.lang.String[] lines = readRepositoryFileContents(file, revision).split("\n");
		for (java.lang.String line : lines) {
			writer.append(_valueFactory.string(line));
		}
		return writer.done();
	}

	/**
	 * Retrieves a list of file locations present in the provided directory at the provided revision number.
	 * May throw an exception of type <b>Subversion</b> in case of a Subversion-related error, or if the
	 * provided combination of location and revision number does not point to a directory.
	 * @param directory A location that points to a directory in the provided revision.
	 * @param revision A valid revision of the repository referenced in the provided directory location.
	 * @return A list of file locations.
	 */
	public static IList getRepositoryFileList(ISourceLocation directory, IInteger revision) {
		return getRepositoryEntryList(directory, revision, SVNNodeKind.FILE);
	}

	/**
	 * Retrieves a list of directory locations present in the provided directory at the provided revision
	 * number. May throw an exception of type <b>Subversion</b> in case of a Subversion-related error, or if
	 * the provided combination of location and revision number does not point to a directory.
	 * @param directory A location that points to a directory in the provided revision.
	 * @param revision A valid revision of the repository referenced in the provided directory location.
	 * @return A list of directory locations.
	 */
	public static IList getRepositoryDirectoryList(ISourceLocation directory, IInteger revision) {
		return getRepositoryEntryList(directory, revision, SVNNodeKind.DIR);
	}

	private static IList getRepositoryEntryList(ISourceLocation directory, IInteger revision, SVNNodeKind type) {
		IListWriter writer = _valueFactory.listWriter(_typeFactory.sourceLocationType());
		try {
			SVNRepository repository = createRepository(directory);
			checkPathType(repository, "", revision.longValue(), SVNNodeKind.DIR);
			// Create collection to pass in (and not use returned) to prevent an unchecked cast warning.
			ArrayList<SVNDirEntry> entries = new ArrayList<SVNDirEntry>();
			repository.getDir("", revision.longValue(), false, entries);
			for (SVNDirEntry entry : entries) {
				if (entry.getKind() == type) {
					writer.append(_valueFactory.sourceLocation(URI.create(entry.getURL().toString())));
				}
			}
		} catch (SVNException e) {
			throw RuntimeExceptionFactory.subversionException(e.getMessage(), null, null);
		}
		return writer.done();
	}

    private static SVNRepository createRepository(ISourceLocation l) throws SVNException {
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(l.getURI().toString()));
		repository.setAuthenticationManager(_authenticationManager);
		return repository;
    }

	private static void checkPathType(SVNRepository repository, String path, long revision, SVNNodeKind type) throws SVNException {
		if (repository.checkPath("", revision) != type) {
			// TODO: Decide whether this should be a Subversion or other type of exception.
			// A typical FileNotFoundException doesn't include the revision component.
			throw RuntimeExceptionFactory.subversionException("Combination of location and revision does not point to a " + type.toString() + ".", null, null);
		}
	}

	private static String readRepositoryFileContents(ISourceLocation file, IInteger revision) {
		SVNProperties properties = new SVNProperties();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			SVNRepository repository = createRepository(file);
			checkPathType(repository, "", revision.longValue(), SVNNodeKind.FILE);
			repository.getFile("", revision.longValue(), properties, outStream);
		} catch (SVNException e) {
			throw RuntimeExceptionFactory.subversionException(e.getMessage(), null, null);
		}
		return outStream.toString();
	}
}

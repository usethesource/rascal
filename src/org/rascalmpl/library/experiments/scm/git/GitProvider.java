package org.rascalmpl.library.experiments.scm.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.experiments.scm.Scm;
import org.rascalmpl.library.experiments.scm.ScmProvider;
import org.rascalmpl.library.experiments.scm.ScmProviderException;
import org.rascalmpl.library.experiments.scm.ScmTypes;
import org.rascalmpl.library.experiments.scm.ScmTypes.Annotation;
import org.rascalmpl.library.experiments.scm.ScmTypes.CheckoutUnit;
import org.rascalmpl.library.experiments.scm.ScmTypes.LogOption;
import org.rascalmpl.library.experiments.scm.ScmTypes.Repository;
import org.rascalmpl.library.experiments.scm.ScmTypes.Resource;
import org.rascalmpl.library.experiments.scm.ScmTypes.Revision;
import org.rascalmpl.library.experiments.scm.ScmTypes.RevisionId;
import org.rascalmpl.library.experiments.scm.ScmTypes.Sha;
import org.rascalmpl.library.experiments.scm.ScmTypes.Tag;
import org.rascalmpl.library.experiments.scm.ScmTypes.WcResource;

import edu.nyu.cs.javagit.api.DotGit;
import edu.nyu.cs.javagit.api.GitFileSystemObject;
import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.WorkingTree;
import edu.nyu.cs.javagit.api.commands.GitCheckout;
import edu.nyu.cs.javagit.api.commands.GitCheckoutOptions;
import edu.nyu.cs.javagit.api.commands.GitLog;
import edu.nyu.cs.javagit.api.commands.GitLogOptions;
import edu.nyu.cs.javagit.client.ClientManager;
import edu.nyu.cs.javagit.client.IClient;
import edu.nyu.cs.javagit.client.cli.CliGitLog;
import edu.nyu.cs.javagit.client.cli.CliGitLog.GitLogParser;
import edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

public class GitProvider implements ScmProvider<GitLogEntryHandler> {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");
	private static final String GIT_PATH = "/usr/bin/";
	
	
	public GitProvider() {
		try {
			JavaGitConfiguration.setGitPath(GIT_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaGitException e) {
			e.printStackTrace();
		}
	}
	
	public GitProvider(IValueFactory factory) {
    	this();
	}
	
	public static void startExtractingRevisions(IConstructor repository, IValue extractFacts, IListWriter logEntriesWriter) throws ScmProviderException {
		new GitProvider().extractLogs(repository, (RascalFunction) extractFacts, logEntriesWriter);
	}
	
	private void extractLogsFromFile(ISourceLocation logsExport, GitLogEntryHandler handler) throws ScmProviderException {
		//GitLogEntryHandler handler = new GitLogEntryHandler(repository, (RascalFunction) factExtractor, null);
		
		IClient client = ClientManager.getInstance().getPreferredClient();
	    CliGitLog gitLog = (CliGitLog) client.getGitLogInstance();
	    GitLogParser parser = gitLog.new GitLogParser(handler);

	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(logsExport.getURI().getPath()));
		} catch (FileNotFoundException e1) {
			throw new ScmProviderException(e1.getMessage(), e1);
		}
	    while (true) {
	      try {
	        String str = br.readLine();
	        if (null == str) {
	          break;
	        }
	        parser.parseLine(str);
	      } catch (IOException e) {
	        ScmProviderException toThrow = new ScmProviderException(ExceptionMessageMap.getMessage("020101"));
	        toThrow.initCause(e);
	        throw toThrow;
	      }
	    }
	}
	
	public GitLogEntryHandler extractLogs(IConstructor repository, RascalFunction factExtractor, IListWriter logEntriesWriter) throws ScmProviderException {
		GitLogEntryHandler handler = createLogEntryHandler(repository, factExtractor, logEntriesWriter);
		extractLogs(repository, handler);
		return handler;
	}
	
	public GitLogEntryHandler createLogEntryHandler(IConstructor repository,
			RascalFunction factExtractor, IListWriter logEntriesWriter) {
		return new GitLogEntryHandler(repository, factExtractor, logEntriesWriter);
	}
	
	/**
	 * Constructor to be used when the git command does not exist in the PATH of the system.
	 * @param gitPath Absolute path to git binaries installed on the system. 
	 * The path must be absolute since it needs to persist for the life of the client code, 
	 * even if the working directory changes. 
	 * Throws a NullPointerException if path is null, or an IllegalArgumentException 
	 * if it's the empty string.
	 * @throws IOException Thrown if the provided path does not exist.
     * @throws JavaGitException Thrown if we cannot find git at the path provided.
	 */
	public GitProvider(String gitPath) throws IOException, JavaGitException {
		JavaGitConfiguration.setGitPath(gitPath);
	}
	
	public void checkoutResources(IConstructor checkoutUnit, IConstructor repository) throws ScmProviderException {
		
		String val;
		if (CheckoutUnit.hasRevision(checkoutUnit)) {
			IConstructor revisionId = Revision.getId(CheckoutUnit.getRevision(checkoutUnit));
			IString sha = Sha.getSha(RevisionId.getSha(revisionId));
			val = sha.getValue();
		} else if (CheckoutUnit.hasSymname(checkoutUnit)) {
			IString tag = Tag.getName(CheckoutUnit.getSymname(checkoutUnit));
			val = tag.getValue();
		} else {
			throw new ScmProviderException("Don't support any other checkoutUnit then Revision or Tag");
		}
		
		//IConstructor revisionId = Revision.getId(CheckoutUnit.getRevision(checkoutUnit));
		
		DotGit dotGit = getDotGitInstance(repository);
		WorkingTree workingTree = dotGit.getWorkingTree();
		
		GitCheckoutOptions options = new GitCheckoutOptions();
		//options.setOptF(true);
		GitCheckout gitCheckout = new GitCheckout();
		try {
			gitCheckout.checkout(workingTree.getPath(), options, Ref.createBranchRef(val));
		} catch (JavaGitException e) {
			throw new ScmProviderException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ScmProviderException(e.getMessage(), e);
		}
	}
	
	private void seekEntries(File[] files, ISetWriter writer) throws JavaGitException {		
		for (File file : files) {
			IConstructor child;
			if (file.isDirectory()) {
				seekEntries(file.listFiles(), writer);
			} else {
				IConstructor resource = WcResource.RESOURCE.make(Resource.FILE.make(ScmTypes.VF.sourceLocation(Scm.encodePath(file.getPath()))));
				writer.insert(resource);
			}
		}
	}
	
	public ISet getResources(IConstructor repository) throws ScmProviderException {
		DotGit dotGit = getDotGitInstance(repository);
		WorkingTree workingTree = dotGit.getWorkingTree();

		try {
			List<GitFileSystemObject> tree = workingTree.getTree();
			File[] files = new File[tree.size()];
			int i = 0;
			for (GitFileSystemObject gitFile : tree) {
				files[i++] = gitFile.getFile();
			}
			ISourceLocation id = ScmTypes.VF.sourceLocation(Scm.encodePath(workingTree.getPath().getAbsolutePath()));
			ISetWriter entriesWriter = ScmTypes.VF.setWriter(WcResource.getAbstractType());
			
			seekEntries(files, entriesWriter);
			return entriesWriter.done();
		} catch (JavaGitException e) {
			throw new ScmProviderException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ScmProviderException(e.getMessage(), e);
		}
	}
	
	private DotGit getDotGitInstance(IConstructor repository) {
		IConstructor connection = ScmTypes.Repository.getConnection(repository);
		File repositoryDirectory = new File(ScmTypes.Connection.getUrl(connection).getValue());
		return DotGit.getInstance(repositoryDirectory);
	}
	
	public void extractLogs(IConstructor repository, GitLogEntryHandler handler) throws ScmProviderException {
		IConstructor connection = Repository.getConnection(repository);
		if (Annotation.LOG_FILE.has(connection)) {
			extractLogsFromFile(Annotation.LOG_FILE.get(connection, ISourceLocation.class), handler);
			return;
		}
		try {
			DotGit dotGit = getDotGitInstance(repository);
			
			GitLogOptions options = new GitLogOptions();
			options.setOptLimitFullHistory(true);
			IConstructor startOption = null;
			IConstructor endOption = null;
			
			ISet logOptions = Repository.getOptions(repository);
			for (IValue iValue : logOptions) {
				IConstructor logOption = (IConstructor) iValue;
				LogOption optionType = LogOption.from(logOption);
				switch (optionType) {
					case START:
						startOption = LogOption.getUnit(logOption);
						break;
					case END:
						endOption = LogOption.getUnit(logOption);
						break;
					case SYMMETRIC_DIFF:
						startOption = LogOption.getFrom(logOption);
						endOption = LogOption.getTo(logOption);
						break;
					case FILE_DETAILS:
						options.setOptFileDetails(true);
						options.setOptFindCopiesHarder(true);
						break;
					case NO_MERGES:
						options.setOptLimitNoMerges(true);
						break;
					case MERGE_DETAILS:
						options.setOptMergeDetails(true);
						break;
					case ONLY_MERGES:
						options.setOptLimitOnlyMerges(true);
						break;
					case REVERSE: 
						options.setOptOrderingReverse(true);
						break;
					case ALL_BRANCHES:
						options.setOptLimitAll(true);
						break;
					default:
						System.err.println("don't know what to do with " + 
							logOption + " of type " + optionType);
						break;
				}
			}
			
			String optLimitRange = null;
			if (startOption != null) {
				if (CheckoutUnit.hasDate(startOption)) {
					IDateTime start = CheckoutUnit.getDate(startOption);
					options.setOptLimitCommitAfter(true, GitProvider.DATE_FORMAT.format(new Date(start.getInstant())));
				} else if (CheckoutUnit.hasSymname(startOption)) {
					IConstructor symname = CheckoutUnit.getSymname(startOption);
					optLimitRange = Tag.getName(symname).getValue() + "..";
				} else if (CheckoutUnit.hasRevision(startOption)) {
					IConstructor revision = CheckoutUnit.getRevision(startOption);
					optLimitRange = Sha.getSha(RevisionId.getSha(Revision.getId(revision))).getValue() + "..";
				}
			}
			
			if (endOption != null) {
				if (CheckoutUnit.hasDate(endOption)) {
					IDateTime end = CheckoutUnit.getDate(endOption);
					options.setOptLimitCommitBefore(true, GitProvider.DATE_FORMAT.format(new Date(end.getInstant())));
				} else if (CheckoutUnit.hasSymname(endOption)) {
					IConstructor symname = CheckoutUnit.getSymname(endOption);
					optLimitRange = (optLimitRange == null ? ".." : optLimitRange) + Tag.getName(symname).getValue();
				} else if (CheckoutUnit.hasRevision(endOption)) {
					IConstructor revision = CheckoutUnit.getRevision(endOption);
					optLimitRange = (optLimitRange == null ? ".." : optLimitRange) + Sha.getSha(RevisionId.getSha(Revision.getId(revision))).getValue();
				}
			}
			
			if (optLimitRange != null) {
				options.setOptLimitRange(true, optLimitRange);
			}
			new GitLog().log(dotGit.getPath(), options, handler);
			handler.callBackToDoMerges();
		} catch (JavaGitException e) {
			throw new ScmProviderException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ScmProviderException(e.getMessage(), e);
		} finally {
			
		}
	}
	
	/**
	 * @return the string representation of the tag or revision, or an empty String if none.
	 */
	private String getCheckoutString(IConstructor tagOrRevision) {
		if (CheckoutUnit.hasRevision(tagOrRevision)) {
			IConstructor rev = CheckoutUnit.getRevision(tagOrRevision);
			return Sha.getSha(RevisionId.getSha(Revision.getId(rev))).getValue();
		} else if (CheckoutUnit.hasSymname(tagOrRevision)) {
			return Tag.getName(CheckoutUnit.getSymname(tagOrRevision)).getValue();
		} 
		return "";
	}
	
	/*public static IConstructor createResource(String workspace, Commit c, CommitFile file, IDateTime date, ISet tags) {
				//Tue Apr 27 11:09:38 2010 +0200
		long parsedDate = 0;
		try {
			parsedDate = GitProvider.DATE_FORMAT.parse(c.getDateString()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ChangeCode changeCode = file.getChangeStatus().getStatusCode();
		String revisionId;
		if (changeCode == ChangeCode.DELETED) {
			revisionId = file.getOldSha();
		} else {
			revisionId = file.getSha();
		}
		
		boolean hasOrigin = (file.getOriginName() != null && file.getOldSha() != null);
		
		ResourceChange changeType = ResourceChange.from(changeCode, hasOrigin);
		IConstructor change;
		if (hasOrigin) {
			IConstructor origin = ScmTypes.Resource.FILE_REV.make(Scm.createResourceId(workspace, file.getOriginName()), 
				ScmTypes.Revision.BLOB.make(file.getOldSha()));
			change = changeType.make(origin);
			
			if (file.getChangeStatus() instanceof ChangeCodeValue) {
				//It has a value, besides the change-code
				ChangeCodeValue cd = (ChangeCodeValue) file.getChangeStatus();
				try {
					int copyPercent = Integer.parseInt(cd.getStatusValue());
					change = ScmTypes.Annotation.ORIGIN_PERCENT.set(change, ScmTypes.VF.integer(copyPercent));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		} else {
			change = changeType.make();
		}
		
		IConstructor revision;
		
		if (tags != null) {
			revision = Revision.BLOB_DATE_TAGS.make(revisionId, date, tags);
		} else {
			revision = Revision.BLOB_DATE.make(revisionId, date);
		}
		
		return Resource.FILE_REV_CHANGE.make(Scm.createResourceId(workspace, file.getName()), revision, change);
		
	}*/

//	public static IConstructor createResource(ScmEntryChangeKind changeKind, String revSha, ISourceLocation path, String author, long date, String log, ISet tags, IValue origin) {
//		
//		
//		return Resource.FILE.make(path, Revision.SHA.make(revSha == null ? "null" : revSha, ScmTypes.VF.datetime(date), tags), changeType.make(author, log, origin));
//	}
}

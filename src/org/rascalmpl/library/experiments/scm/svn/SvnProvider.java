package org.rascalmpl.library.experiments.scm.svn;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.experiments.scm.ScmProvider;
import org.rascalmpl.library.experiments.scm.ScmProviderException;
import org.rascalmpl.library.experiments.scm.ScmTypes;
import org.rascalmpl.library.experiments.scm.ScmTypes.CheckoutUnit;
import org.rascalmpl.library.experiments.scm.ScmTypes.LogOption;
import org.rascalmpl.library.experiments.scm.ScmTypes.Repository;
import org.rascalmpl.library.experiments.scm.ScmTypes.Revision;
import org.rascalmpl.library.experiments.scm.ScmTypes.RevisionId;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.auth.SVNAuthentication;
import org.tmatesoft.svn.core.auth.SVNSSHAuthentication;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;

public class SvnProvider implements ScmProvider<SvnLogEntryHandler> {
	
	

	public SvnProvider() {
		SVNRepositoryFactoryImpl.setup();
	    DAVRepositoryFactory.setup();
		FSRepositoryFactory.setup();
	}
	
	public SvnProvider(IValueFactory factory) {
    	this();
	}
	public static void startExtractingRevisions(IConstructor repository, IValue extractFacts, IListWriter logEntriesWriter) throws ScmProviderException {
		new SvnProvider().extractLogs(repository, (RascalFunction) extractFacts, logEntriesWriter);
	}
	
	public SvnLogEntryHandler extractLogs(IConstructor repository, RascalFunction factExtractor, IListWriter logEntriesWriter) throws ScmProviderException {
		SvnLogEntryHandler handler = createLogEntryHandler(repository, factExtractor, logEntriesWriter);
		extractLogs(repository, handler);
		return handler;
	}
	
	public SvnLogEntryHandler createLogEntryHandler(IConstructor repository,
			RascalFunction factExtractor, IListWriter logEntriesWriter) {
		return new SvnLogEntryHandler(repository, factExtractor, logEntriesWriter);
	}
	
	public ISet getResources(IConstructor repositoryConfig) throws ScmProviderException {
		IConstructor connection = ScmTypes.Repository.getConnection(repositoryConfig);
		ISVNAuthenticationManager authManager = getAuthManager(connection);
		
		File workspace = new File(getWorkspaceModulePath(repositoryConfig));
		
		SvnWcInfoHandler handler = new SvnWcInfoHandler();
		SVNWCClient wcClient = new SVNWCClient(authManager, null);
		try {
			wcClient.doInfo(workspace, SVNRevision.WORKING, SVNRevision.WORKING, SVNDepth.INFINITY, null, handler);
		} catch (SVNException e) {
			throw new ScmProviderException(e.getMessage(), e);
		}
		
		return handler.done();
	}

	/**
	 * {@inheritDoc}
	 */
	public void checkoutResources(IConstructor checkoutUnit, IConstructor repositoryConfig) throws ScmProviderException {
		try {
			SVNRevision revision;
			if (CheckoutUnit.hasDate(checkoutUnit)) {
				revision = SVNRevision.create(new Date(CheckoutUnit.getDate(checkoutUnit).getInstant()));
			} else {
				IConstructor revisionId = Revision.getId(CheckoutUnit.getRevision(checkoutUnit));
				revision = SVNRevision.create(RevisionId.getId(revisionId).longValue());
			}
			checkoutRevision(revision, repositoryConfig);
		} catch (NumberFormatException e) {
			throw new ScmProviderException(e.getMessage(), e);
		} catch (SVNException e) {
			throw new ScmProviderException(e.getMessage(), e);
		}
	}
	
	private String getWorkspaceModulePath(IConstructor repositoryConfig) {
		String workspacePath = ScmTypes.Repository.getWorkspace(repositoryConfig).getURI().getPath();
		String module = ScmTypes.Repository.getModule(repositoryConfig).getValue().trim();
		
		if (module.length() > 0) {
			workspacePath += File.separator + module;
		}
		
		return workspacePath;
	}
	
	private void checkoutRevision(SVNRevision revision, IConstructor repositoryConfig) throws SVNException {
		IConstructor connection = ScmTypes.Repository.getConnection(repositoryConfig);
		String module = ScmTypes.Repository.getModule(repositoryConfig).getValue().trim();
		ISVNAuthenticationManager authManager = getAuthManager(connection);
		
		SVNUpdateClient updateClient = new SVNUpdateClient(authManager, null);
		
		SVNURL url = createSvnUrl(connection, module);
		File workspace = new File(getWorkspaceModulePath(repositoryConfig));
		
		long doCheckout = updateClient.doCheckout(url, workspace, SVNRevision.HEAD, revision, SVNDepth.INFINITY, true);
		System.out.println("-- " + revision + " is of revision " + doCheckout + " --");
	}
	
	private ISVNAuthenticationManager getAuthManager(IConstructor connection) {
		BasicAuthenticationManager auth = null;
		if (ScmTypes.Connection.hasUsername(connection)) {
			String username = ScmTypes.Connection.getUsername(connection).getValue();
			String password = ScmTypes.Connection.hasPassword(connection) ? 
				ScmTypes.Connection.getPassword(connection).getValue() : null;
			if (ScmTypes.Connection.hasPrivateKey(connection)) {
				URI privateKey = ScmTypes.Connection.getPrivateKey(connection).getURI();
				auth = new BasicAuthenticationManager(new SVNAuthentication[] {
					new SVNSSHAuthentication(username, new File(privateKey), password, 22, false)});
			} else {
				auth = new BasicAuthenticationManager(username, password);
			}
		}
		return auth;
	}
	
	private SVNURL createSvnUrl(IConstructor connection, String module) throws SVNException {
		String repositoryUrl = ScmTypes.Connection.getUrl(connection).getValue();
		if (repositoryUrl.startsWith("/")) {
			repositoryUrl = "file://" + repositoryUrl;
		}
		if (repositoryUrl.endsWith("/")) {
			repositoryUrl += module;
		} else if (module.length() > 0) {
			repositoryUrl += "/" + module;
		}
		return SVNURL.parseURIDecoded(repositoryUrl);
	}
	
	private SVNRepository createConnection(IConstructor repositoryConfig) throws ScmProviderException {
		try {
			IConstructor connection = ScmTypes.Repository.getConnection(repositoryConfig);
			String module = ScmTypes.Repository.getModule(repositoryConfig).getValue().trim();
			SVNURL url = createSvnUrl(connection, module);
			SVNRepository repository = SVNRepositoryFactory.create(url, null );
			ISVNAuthenticationManager authManager = getAuthManager(connection);
			if (authManager != null) {
				repository.setAuthenticationManager(authManager);
			}
			return repository;
		} catch (SVNException e) {
			throw new ScmProviderException(e.getMessage(), e);
		}
	}
	
	public void extractLogs(IConstructor repositoryConfig, SvnLogEntryHandler handler) throws ScmProviderException {
		
		try {
			SVNRepository repository = createConnection(repositoryConfig);

			long startRevision = 1; // = repository.getDatedRevision(new Date(start.getInstant()));
			long endRevision = SVNRevision.HEAD.getNumber(); // = repository.getDatedRevision(new Date(end.getInstant()));
			boolean includeMergedRevisions = false;
			
			ISet logOptions = Repository.getOptions(repositoryConfig);
			for (IValue iValue : logOptions) {
				IConstructor logOption = (IConstructor) iValue;
				LogOption optionType = LogOption.from(logOption);
				
				IConstructor startCheckoutUnit = null;
				IConstructor endCheckoutUnit = null;
				switch (optionType) {
					case START:
						startCheckoutUnit = LogOption.getUnit(logOption);
						break;
					case END:
						endCheckoutUnit = LogOption.getUnit(logOption);
						break;
					case MERGE_DETAILS:
						includeMergedRevisions = true;
						break;
					default:
						//TODO implement the other options
						System.err.println("[SvnProvider] Ignoring the option with type:" + optionType);
				}
				if (startCheckoutUnit != null) {
					if (CheckoutUnit.hasDate(startCheckoutUnit)) {
						startRevision = repository.getDatedRevision(new Date(CheckoutUnit.getDate(startCheckoutUnit).getInstant()));
					} else {
						 throw new IllegalArgumentException("only supports start date ranges");
					}
				}
				if (endCheckoutUnit != null) {
					if (CheckoutUnit.hasDate(endCheckoutUnit)) {
						endRevision = repository.getDatedRevision(new Date(CheckoutUnit.getDate(endCheckoutUnit).getInstant()));
					} else {
						 throw new IllegalArgumentException("only supports end date ranges");
					}
				}
			}

			repository.log(new String[] {""}, startRevision, endRevision, true, true, -1, includeMergedRevisions, null, handler);
			
//			repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded("http://pffan.shahbazian.nl/svn/poker"), null );
//			repository.setAuthenticationManager(new BasicAuthenticationManager("dekkers", "finest"));
//			
//			Calendar instance = GregorianCalendar.getInstance();
//		    instance.set(2000, 1, 1);
//			long datedRevision = repository.getDatedRevision(instance.getTime());
//			repository.log(new String[] { "" }, datedRevision, repository.getLatestRevision(), true, true, Long.MAX_VALUE, false, null, handler);

		} catch (SVNException e) {
			throw new ScmProviderException(e.getMessage(), e);
		}
		
	}
	
//	public static IConstructor createResource(String workspace, SVNLogEntryPath entry, IConstructor revision) {
//		ScmEntryChangeKind changeKind = SvnChangeKind.from(entry.getType()); //ChangeCodeValue.from(Character.toString(entry.getType()));
//		Resource resType = (entry.getKind() == SVNNodeKind.FILE) ? Resource.FILE_REV_CHANGE : Resource.FOLDER_REV_CHANGE;
//		
//		boolean hasOriging = (entry.getCopyPath() != null);
//		
//		ResourceChange changeType = ResourceChange.from(changeKind.getStatusCode(), hasOriging);
//		
//		IConstructor change;
//		if (hasOriging) {
//			IConstructor origin = ScmTypes.Resource.FILE_REV.make(Scm.createResourceId(workspace, entry.getCopyPath().substring(1)), 
//				ScmTypes.Revision.ID.make(entry.getCopyRevision()));
//			change = changeType.make(origin);
//		} else {
//			change = changeType.make();
//		}
//		
//		
//		return resType.make(Scm.createResourceId(workspace, entry.getPath().substring(1)), revision, change);
//	}
	
	
//	public static IConstructor createResource(ScmEntryChangeKind changeKind, long revId, ISourceLocation path, SVNNodeKind pathKind, String author, long date, String log, ISet tags) {
//		Resource resType;
//		if (pathKind == SVNNodeKind.FILE) {
//			resType = Resource.FILE_REV;
//		} else {
//			resType = Resource.FOLDER_REV;
//		}
//		ResourceChange changeType = ResourceChange.from(changeKind.getStatusCode());
//		
//		return resType.make(path, Revision.ID.make(revId, ScmTypes.VF.datetime(date), tags));
//	}


}

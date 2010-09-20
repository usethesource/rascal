package org.rascalmpl.library.experiments.scm.cvs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.checkout.CheckoutCommand;
import org.netbeans.lib.cvsclient.command.log.LogBuilder;
import org.netbeans.lib.cvsclient.command.log.RlogCommand;
import org.netbeans.lib.cvsclient.commandLine.BasicListener;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.connection.LocalConnection;
import org.netbeans.lib.cvsclient.connection.PServerConnection;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.experiments.scm.Scm;
import org.rascalmpl.library.experiments.scm.ScmProvider;
import org.rascalmpl.library.experiments.scm.ScmProviderException;
import org.rascalmpl.library.experiments.scm.ScmTypes;
import org.rascalmpl.library.experiments.scm.ScmTypes.Annotation;
import org.rascalmpl.library.experiments.scm.ScmTypes.CheckoutUnit;
import org.rascalmpl.library.experiments.scm.ScmTypes.Info;
import org.rascalmpl.library.experiments.scm.ScmTypes.LogOption;
import org.rascalmpl.library.experiments.scm.ScmTypes.Repository;
import org.rascalmpl.library.experiments.scm.ScmTypes.Resource;
import org.rascalmpl.library.experiments.scm.ScmTypes.Revision;
import org.rascalmpl.library.experiments.scm.ScmTypes.RevisionId;
import org.rascalmpl.library.experiments.scm.ScmTypes.WcResource;

public class CvsProvider implements ScmProvider<CvsLogEntryHandler> {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	
	public CvsProvider() {
		System.setProperty("javacvs.multiple_commands_warning", "false");
	}
	
	public CvsProvider(IValueFactory factory) {
    	this();
	}
	
	public static void startExtractingRevisions(IConstructor repository, IValue extractFacts, IListWriter logEntriesWriter) throws ScmProviderException {
		new CvsProvider().extractLogs(repository, (RascalFunction) extractFacts, logEntriesWriter);
	}
	
	public CvsLogEntryHandler extractLogs(IConstructor repository, RascalFunction factExtractor, IListWriter logEntriesWriter) throws ScmProviderException {
		CvsLogEntryHandler handler = createLogEntryHandler(repository, factExtractor, logEntriesWriter);
		extractLogs(repository, handler);
		return handler;
	}
	
	public CvsLogEntryHandler createLogEntryHandler(IConstructor repository,
			RascalFunction factExtractor, IListWriter logEntriesWriter) {
		return new CvsLogEntryHandler(repository, factExtractor, logEntriesWriter);
	}
	
	/**
	 * Fetches the cvs entries of the given folder and optionnaly it's subdirectories and stores the {@link Resource} information
	 * with additional {@link Revision} and {@link Info} fields.
	 * @param folder to seek the entries of
	 * @param recursive if true, the subdirs of the folder will be fetched too
	 * @param writer that will contain the resources info as an {@link WcResource}
	 */
	private static void seekEntries(String folder, Client client, boolean recursive, ISetWriter writer) throws IOException {
		File dir = new File(folder);
		if (dir.isFile()) {
			throw new IllegalArgumentException("seekEntries only works on folders!");
		}
		System.out.println("Dir:" + dir);
		Iterator<Entry> iterator = client.getEntries(dir);

		while(iterator.hasNext()) {
			Entry entry = iterator.next();
			String childPath = folder + "/" + Scm.encodePath(entry.getName());
			ISourceLocation id = ScmTypes.VF.sourceLocation(childPath);
			IConstructor child;
			if (!entry.isDirectory()) {
				IConstructor revision = Revision.REVISION.make(RevisionId.NUMBER.make(entry.getRevision()));
				IConstructor info = Info.NONE.make(ScmTypes.VF.datetime(entry.getLastModified().getTime()));
				writer.insert(WcResource.RESOURCE_REVISION_INFO.make(Resource.FILE.make(id), revision, info));
				
				//revision = Annotation.AUTHOR.set(revision, Info.NONE.make(ScmTypes.VF.datetime(entry.getLastModified().getTime())));
				//writer.put(Resource.FILE.make(id), revision);
			} else if (recursive) {
				seekEntries(childPath, client, recursive, writer);
			}
		}
	}
	
	private static Client createClient(IConstructor conn, ISourceLocation workspace) {
		ScmTypes.Connection connType = ScmTypes.Connection.from(conn);
		Connection connection;
		switch (connType) {
			case FS:
				connection = new LocalConnection();
				((LocalConnection)connection).setRepository(ScmTypes.Connection.getUrl(conn).getValue());
				break;
			case PSERVER:
				connection = new PServerConnection();
				((PServerConnection)connection).setRepository(ScmTypes.Connection.getUrl(conn).getValue());
				((PServerConnection)connection).setHostName(ScmTypes.Connection.getHost(conn).getValue());
				((PServerConnection)connection).setUserName(ScmTypes.Connection.getUsername(conn).getValue());
				break;
			default:
				throw new IllegalArgumentException("Don't know how to create a connection of type '" + 
					connType + "' for '" + conn + "'");
		}
		
		Client client = new Client(connection, new StandardAdminHandler());
		client.setLocalPath(workspace.getURI().getPath());
		return client;
	}
	
	public void checkoutResources(IConstructor checkoutUnit, IConstructor repository) throws ScmProviderException {
		if (!ScmTypes.CheckoutUnit.hasDate(checkoutUnit)) {
			throw new ScmProviderException("CvsProvider can't checkout on a checkoutUnit without a date");
		}
		checkoutRevision(ScmTypes.CheckoutUnit.getDate(checkoutUnit), repository);
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void checkoutRevision(IDateTime dateTime, IConstructor repository) throws ScmProviderException {
		IConstructor conn = ScmTypes.Repository.getConnection(repository);
		ISourceLocation workspace = ScmTypes.Repository.getWorkspace(repository);
		String module = ScmTypes.Repository.getModule(repository).getValue();
		
		GlobalOptions gtx = new GlobalOptions();
		gtx.setCVSRoot(ScmTypes.Connection.getUrl(conn).getValue());
		
		Client client = createClient(conn, workspace);
		client.getEventManager().addCVSListener(new BasicListener());
		
		CheckoutCommand command = new CheckoutCommand(true, module);
		command.setBuilder(null);
		command.setCheckoutByDate(dateFormat.format(new Date(dateTime.getInstant())));
		
		try {
			boolean succeeded = client.executeCommand(command, gtx);
		} catch (CommandAbortedException e) {
			throw new ScmProviderException(e.getMessage(), e);
		} catch (CommandException e) {
			throw new ScmProviderException(e.getMessage(), e);
		} catch (AuthenticationException e) {
			throw new ScmProviderException(e.getMessage(), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ISet getResources(IConstructor repository) throws ScmProviderException {
		IConstructor conn = ScmTypes.Repository.getConnection(repository);
		ISourceLocation workspace = ScmTypes.Repository.getWorkspace(repository);
		String module = ScmTypes.Repository.getModule(repository).getValue();
	
		Client client = createClient(conn, workspace);
		ISetWriter writer = ScmTypes.VF.setWriter(WcResource.getAbstractType());
		try {
			seekEntries(workspace.getURI().getPath() + "/" + module, client, true, writer);
			return writer.done();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private void extractLogsFromFile(ISourceLocation logsExport, Client client) throws ScmProviderException {
		LogBuilder parser = new LogBuilder(client.getEventManager(), new RlogCommand());

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
	        parser.parseLine(str, false);
	      } catch (IOException e) {
	        throw new ScmProviderException(e.getMessage(), e);
	      }
	    }
	    parser.outputDone();
	}
	
	public void extractLogs(IConstructor repository, CvsLogEntryHandler handler) throws ScmProviderException {
		IConstructor conn = ScmTypes.Repository.getConnection(repository);
		
		GlobalOptions gtx = new GlobalOptions();
		gtx.setCVSRoot(ScmTypes.Connection.getUrl(conn).getValue());
		
		Client client = createClient(conn, ScmTypes.Repository.getWorkspace(repository));
		client.getEventManager().addCVSListener(handler);
		
		if (Annotation.LOG_FILE.has(conn)) {
			extractLogsFromFile(Annotation.LOG_FILE.get(conn, ISourceLocation.class), client);
			return;
		}
		
		RlogCommand command = new RlogCommand();
		command.setBuilder(new LogBuilder(client.getEventManager(), command));
		command.setModule(ScmTypes.Repository.getModule(repository).getValue());
		command.setSuppressHeader(true);
		
		//IConstructor startCheckoutUnit = Repository.getStart(repository);
		//IConstructor endCheckoutUnit = Repository.getEnd(repository);
		
		Date start = new Date(0);
		Date end = new Date();
			
		
		ISet logOptions = Repository.getOptions(repository);
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
			}
			if (startCheckoutUnit != null) {
				if (CheckoutUnit.hasDate(startCheckoutUnit)) {
					start = new Date(CheckoutUnit.getDate(startCheckoutUnit).getInstant());
				} else {
					 throw new IllegalArgumentException("only supports start date ranges");
				}
			}
			if (endCheckoutUnit != null) {
				if (CheckoutUnit.hasDate(endCheckoutUnit)) {
					end = new Date(CheckoutUnit.getDate(endCheckoutUnit).getInstant());
				} else {
					 throw new IllegalArgumentException("only supports end date ranges");
				}
			}
		}
		
		command.setDateFilter(dateFormat.format(end) + " > " + dateFormat.format(start));
		
		try {
			client.executeCommand(command, gtx);
		} catch (CommandAbortedException e1) {
			e1.printStackTrace();
		} catch (CommandException e1) {
			e1.printStackTrace();
		} catch (AuthenticationException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
}

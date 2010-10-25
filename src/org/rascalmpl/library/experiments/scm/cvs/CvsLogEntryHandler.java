package org.rascalmpl.library.experiments.scm.cvs;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;
import org.netbeans.lib.cvsclient.command.log.LogInformation;
import org.netbeans.lib.cvsclient.command.log.LogInformation.Revision;
import org.netbeans.lib.cvsclient.command.log.LogInformation.SymName;
import org.netbeans.lib.cvsclient.event.BinaryMessageEvent;
import org.netbeans.lib.cvsclient.event.CVSListener;
import org.netbeans.lib.cvsclient.event.FileAddedEvent;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import org.netbeans.lib.cvsclient.event.FileRemovedEvent;
import org.netbeans.lib.cvsclient.event.FileToRemoveEvent;
import org.netbeans.lib.cvsclient.event.FileUpdatedEvent;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.event.ModuleExpansionEvent;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.experiments.scm.AbstractScmLogEntryHandler;
import org.rascalmpl.library.experiments.scm.Scm;
import org.rascalmpl.library.experiments.scm.ScmEntryChangeKind;
import org.rascalmpl.library.experiments.scm.ScmTypes;
import org.rascalmpl.library.experiments.scm.ScmTypes.Info;
import org.rascalmpl.library.experiments.scm.ScmTypes.Resource;
import org.rascalmpl.library.experiments.scm.ScmTypes.Tag;

public class CvsLogEntryHandler extends AbstractScmLogEntryHandler<LogInformation> implements CVSListener {
	
	private final String host;
	private final String url;
	private final String workspace;
	
	public CvsLogEntryHandler(IConstructor repository, RascalFunction factExtractor, IListWriter logEntriesWriter) {
		super(repository, factExtractor, logEntriesWriter);
		
		IConstructor connection = ScmTypes.Repository.getConnection(repository);
		host = ScmTypes.Connection.hasHost(connection) ? ScmTypes.Connection.getHost(connection).getValue() : null;
		url = ScmTypes.Connection.getUrl(connection).getValue();
		workspace = ScmTypes.Repository.getWorkspace(repository).getURI().getPath();
	}

	public void handleLogEntry(LogInformation logInfo) {
		ISetWriter resources = ScmTypes.VF.setWriter(ScmTypes.Resource.getAbstractType());
		
		String fileName = logInfo.getRepositoryFilename();
		//We will use the substring of the filename, starting after the repository url part (if matched)
		//and ended at length - 2, since we don't need the ',v' part of the filename.
		fileName = fileName.substring(fileName.startsWith(url) ? url.length() : 0, fileName.length() - 2);
		ISourceLocation sourceLocation = ScmTypes.VF.sourceLocation(workspace + Scm.encodePath(fileName));
	
		IConstructor resource = Resource.FILE.make(sourceLocation);
		IRelationWriter revisions = ScmTypes.VF.relationWriter(ScmTypes.TF.tupleType(ScmTypes.RevisionChange.getAbstractType(), ScmTypes.Info.getAbstractType()));
		IRelationWriter revTags = ScmTypes.VF.relationWriter(ScmTypes.TF.tupleType(ScmTypes.Revision.getAbstractType(), ScmTypes.Tag.getAbstractType()));
		
		@SuppressWarnings("unchecked")
		List<Revision> revisionList = (List<Revision>) logInfo.getRevisionList();
		for (Revision rev : revisionList) {

			IConstructor revision = ScmTypes.Revision.REVISION.make(ScmTypes.RevisionId.NUMBER.make(rev.getNumber()));
			IConstructor revisionChange = getRevisionKind(rev).make(revision);
			
			IConstructor committer = Info.makeInfo(
				ScmTypes.VF.datetime(rev.getDate().getTime()), rev.getAuthor(), rev.getMessage());
			
			//Fill the revisions relation
			revisions.insert(ScmTypes.VF.tuple(revisionChange, committer));
			
			//Fill the revTags relations
			List<LogInformation.SymName> symNames = rev.getLogInfoHeader().getSymNamesForRevision(rev.getNumber());
			if (symNames.size() > 0) {
				for (SymName tag : symNames) {
					revTags.insert(ScmTypes.VF.tuple(revision, Tag.LABEL.make(tag.getName())));
				}
			}
		}
		
		IConstructor changeSet = ScmTypes.ChangeSet.RESOURCE.make(resource, revisions.done(), revTags.done());
		IConstructor result = callBack(changeSet);
	}
	
	private ScmTypes.RevisionChange getRevisionKind(Revision revision) {
		ScmEntryChangeKind changeKind;
		String state = revision.getState();
		if (state.equals("dead")) {
			changeKind = ScmEntryChangeKind.ChangeCode.DELETED;
		} else if (state.equals("Exp")) {
			if (revision.getLines() == null || revision.getLines().trim().length() == 0) {
				changeKind = ScmEntryChangeKind.ChangeCode.ADDED;
			} else {
				changeKind = ScmEntryChangeKind.ChangeCode.MODIFIED;
			}
		} else {
			changeKind = ScmEntryChangeKind.ChangeCode.UNKNOWN;
		}
		return ScmTypes.RevisionChange.from(changeKind, false);
	}
	
	public void fileInfoGenerated(FileInfoEvent e) {
		FileInfoContainer infoContainer = e.getInfoContainer();
		if (infoContainer instanceof LogInformation) {
			handleLogEntry((LogInformation) infoContainer);
		} else {
			System.err.println("WTF:" + infoContainer.getClass());
		}
	}

	public void commandTerminated(TerminationEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void fileAdded(FileAddedEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void fileRemoved(FileRemovedEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void fileToRemove(FileToRemoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void fileUpdated(FileUpdatedEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void messageSent(MessageEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void messageSent(BinaryMessageEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void moduleExpanded(ModuleExpansionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

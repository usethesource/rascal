package org.rascalmpl.library.experiments.scm.svn;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.experiments.scm.AbstractScmLogEntryHandler;
import org.rascalmpl.library.experiments.scm.Scm;
import org.rascalmpl.library.experiments.scm.ScmEntryChangeKind;
import org.rascalmpl.library.experiments.scm.ScmTypes;
import org.rascalmpl.library.experiments.scm.ScmEntryChangeKind.SvnChangeKind;
import org.rascalmpl.library.experiments.scm.ScmTypes.Annotation;
import org.rascalmpl.library.experiments.scm.ScmTypes.LogOption;
import org.rascalmpl.library.experiments.scm.ScmTypes.MergeDetail;
import org.rascalmpl.library.experiments.scm.ScmTypes.Repository;
import org.rascalmpl.library.experiments.scm.ScmTypes.Resource;
import org.rascalmpl.library.experiments.scm.ScmTypes.Revision;
import org.rascalmpl.library.experiments.scm.ScmTypes.RevisionId;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;


public class SvnLogEntryHandler extends AbstractScmLogEntryHandler<SVNLogEntry> implements ISVNLogEntryHandler {
	
	private static final boolean DEBUG = true;

	private final String workspace;
	private boolean fileDetails;
	
	private IConstructor lastRevisionId;
	
	private IListWriter mergeParentsWriter;
	private int mergeParentsLeft;
	private SVNLogEntry mergeLogEntry;
	
	
	public SvnLogEntryHandler(IConstructor repository, RascalFunction factExtractor, IListWriter logEntriesWriter) {
		super(repository, factExtractor, logEntriesWriter);
		workspace = ScmTypes.Repository.getWorkspace(repository).getURI().getPath();
		
		ISet logOptions = Repository.getOptions(repository);
		
		for (IValue iValue : logOptions) {
			IConstructor logOption = (IConstructor) iValue;
			LogOption optionType = LogOption.from(logOption);
			
			switch (optionType) {
				case FILE_DETAILS:
					fileDetails = true;
					break;
				default:
					//TODO implement the other options
					System.err.println("[SvnLogEntryHandler] Ignoring the option with type:" + optionType);
					break;
			}
		}
		
	}
	
	private void handleLogEntry(IConstructor revision, SVNLogEntry logEntry) {
		lastRevisionId = Revision.getId(revision);
		IDateTime datetime = ScmTypes.VF.datetime(logEntry.getDate().getTime());
        IRelationWriter resources = ScmTypes.VF.relationWriter(ScmTypes.TF.tupleType(ScmTypes.Resource.getAbstractType(), ScmTypes.RevisionChange.getAbstractType()));
        
    	@SuppressWarnings("unchecked")
        Map<String, SVNLogEntryPath> changedPaths = logEntry.getChangedPaths();
        if (changedPaths.size() > 0 && fileDetails) {
        	if (DEBUG) { 
	            System.out.println();
	            System.out.println("changed paths:");
        	}
        	
            //TODO set tags and merge parents!
            for (SVNLogEntryPath entryPath : changedPaths.values()) {
            	ScmEntryChangeKind changeKind = SvnChangeKind.from(entryPath.getType());
            	boolean hasOrigin = (entryPath.getCopyPath() != null);
            	
            	ScmTypes.RevisionChange changeType = ScmTypes.RevisionChange.from(changeKind, hasOrigin);
            	
            	IConstructor revisionChange;
				if (hasOrigin) {
					IConstructor parent = Revision.REVISION.make(RevisionId.ID.make(entryPath.getCopyRevision()));
					IConstructor resRevision = Revision.REVISION_PARENT.make(lastRevisionId, parent);
					revisionChange = changeType.make(resRevision, getResourceType(entryPath).make(Scm.createResourceId(workspace, entryPath.getCopyPath().substring(1))));
					if (DEBUG) {
						System.err.println("!Revision with parent:" + revisionChange);
					}
				} else {
					revisionChange = changeType.make(Revision.REVISION.make(lastRevisionId));
				}
				
				IConstructor resource = getResourceType(entryPath).make(Scm.createResourceId(workspace, entryPath.getPath().substring(1)));
				resources.insert(ScmTypes.VF.tuple(resource, revisionChange));

            	if (DEBUG) {
	                System.out.println(entryPath.getType() + " (" + entryPath.getKind() + ") " +  
	                    entryPath.getPath() + " " + 
	                    (entryPath.getCopyPath() != null ? 
	                        "( from " + entryPath.getCopyPath() + 
	                        " revision " + entryPath.getCopyRevision() + ")"
	                         : ""));
            	}
            }
        }
        String author = logEntry.getAuthor();
        String msg = logEntry.getMessage();
        IConstructor committer = ScmTypes.Info.makeInfo(datetime, author, msg);
        
        IConstructor changeSet = ScmTypes.ChangeSet.CHANGE_SET.make(revision, resources.done(), committer);
		IConstructor result = callBack(changeSet);
	}
	
	private Resource getResourceType(SVNLogEntryPath entryPath) {
		SVNNodeKind kind = entryPath.getKind();
		if (kind == SVNNodeKind.DIR) {
			return Resource.FOLDER;
		} else if (kind == SVNNodeKind.FILE) {
			return Resource.FILE;
		}
		//System.err.println(kind + " nodekind, assume it's a file:" + entryPath.getPath());
		return Resource.FILE;	
	}
	
	public void handleLogEntry(SVNLogEntry logEntry) {
		if (DEBUG) {
			System.out.println("---------------------------------------------" );
	        System.out.println("revision: " + logEntry.getRevision( ) );
	        System.out.println("author: " + logEntry.getAuthor( ) );
	        System.out.println("date: " + logEntry.getDate() );
	        System.out.println("log message: " + logEntry.getMessage() );
	        System.out.println("regular props:" + logEntry.hasChildren());			
		}
		IConstructor revision;
		if (lastRevisionId != null) {
			revision = Revision.REVISION_PARENT.make(RevisionId.ID.make(logEntry.getRevision()), Revision.REVISION.make(lastRevisionId));
		} else {
			revision = Revision.REVISION.make(RevisionId.ID.make(logEntry.getRevision()));
		}
    	
    	if (mergeParentsLeft == 1 && logEntry.getRevision() != SVNRepository.INVALID_REVISION) {
    		//We found a direct merge parent
    		mergeParentsWriter.insert(MergeDetail.PARENT.make(revision));
    	}
    	
    	if (logEntry.hasChildren()) {
    		//If this is a merge changeset
			mergeParentsLeft++;
			if (mergeParentsWriter == null) {
				if (mergeParentsLeft != 1) {
					throw new IllegalStateException("MergeParentsLeft should have been 1, but is " + 1 + " for the logEntry " + logEntry);
				}
				mergeParentsWriter = ScmTypes.VF.listWriter(MergeDetail.getAbstractType());
    			mergeLogEntry = logEntry;
    		}
    	} else if (logEntry.getRevision() == SVNRepository.INVALID_REVISION) {
    		//Merge parents are finished
    		mergeParentsLeft--;
    		if (mergeParentsLeft == 0) {
    			revision = Revision.REVISION.make(RevisionId.ID.make(mergeLogEntry.getRevision()));
    			revision = Annotation.MERGE_DETAIL.set(revision, mergeParentsWriter.done());
    			handleLogEntry(revision, mergeLogEntry);
    			mergeLogEntry = logEntry;
    			mergeParentsWriter = null;
    		}
    	} else if (mergeParentsLeft == 0){
    		handleLogEntry(revision, logEntry);
    	}
    	
	}
}

package org.rascalmpl.library.experiments.scm.svn;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.library.experiments.scm.Scm;
import org.rascalmpl.library.experiments.scm.ScmTypes;
import org.rascalmpl.library.experiments.scm.ScmTypes.Info;
import org.rascalmpl.library.experiments.scm.ScmTypes.Resource;
import org.rascalmpl.library.experiments.scm.ScmTypes.Revision;
import org.rascalmpl.library.experiments.scm.ScmTypes.RevisionId;
import org.rascalmpl.library.experiments.scm.ScmTypes.WcResource;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.wc.ISVNInfoHandler;
import org.tmatesoft.svn.core.wc.SVNInfo;

public class SvnWcInfoHandler implements ISVNInfoHandler {
	
	private final ISetWriter resourceFilesWriter;
	
	private ISet results;
	
	public SvnWcInfoHandler() {
		resourceFilesWriter = ScmTypes.VF.setWriter(WcResource.getAbstractType());
	}
	
	public void handleInfo(SVNInfo info) throws SVNException {
		long revNumber = info.getCommittedRevision().getNumber();
		boolean isDir = (info.getKind() == SVNNodeKind.DIR);
		
		Resource resType = isDir ? Resource.FOLDER : Resource.FILE;
		
		ISourceLocation id = ScmTypes.VF.sourceLocation(Scm.encodePath(Scm.encodePath(info.getFile().getAbsolutePath())));
		IDateTime datetime = ScmTypes.VF.datetime(info.getCommittedDate().getTime());
		IConstructor revision = Revision.REVISION.make(RevisionId.ID.make(revNumber));
		
		resourceFilesWriter.insert(WcResource.RESOURCE_REVISION_INFO.make(resType.make(id), revision, Info.AUTHOR.make(datetime, info.getAuthor())));
	}
	
	/**
	 * Finalizes the info handler and gets the resources in the working copy.
	 * @return a set of resources as an {@link WcResource} with optionally {@link Revision} and {@link Info} fields.
	 */
	public ISet done() {
		if (results != null) {
			throw new IllegalStateException("Can't call done twice:" + results);
		}
		results = resourceFilesWriter.done();
		return results;
	} 
	
	
	public ISet getResults() {
		if (results == null) {
			throw new IllegalStateException("Please call done() first to finalize the fileresources writer");
		}
		return results;
	}
}

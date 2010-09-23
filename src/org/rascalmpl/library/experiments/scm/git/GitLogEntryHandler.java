package org.rascalmpl.library.experiments.scm.git;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.experiments.scm.AbstractScmLogEntryHandler;
import org.rascalmpl.library.experiments.scm.Scm;
import org.rascalmpl.library.experiments.scm.ScmEntryChangeKind;
import org.rascalmpl.library.experiments.scm.ScmEntryChangeKind.ChangeCodeValue;
import org.rascalmpl.library.experiments.scm.ScmTypes;
import org.rascalmpl.library.experiments.scm.ScmTypes.Annotation;
import org.rascalmpl.library.experiments.scm.ScmTypes.Info;
import org.rascalmpl.library.experiments.scm.ScmTypes.MergeDetail;
import org.rascalmpl.library.experiments.scm.ScmTypes.Resource;
import org.rascalmpl.library.experiments.scm.ScmTypes.Revision;
import org.rascalmpl.library.experiments.scm.ScmTypes.RevisionId;
import org.rascalmpl.library.experiments.scm.ScmTypes.Sha;
import org.rascalmpl.library.experiments.scm.ScmTypes.Tag;

import edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;
import edu.nyu.cs.javagit.api.commands.GitLogResponse.CommitFile;

public class GitLogEntryHandler extends AbstractScmLogEntryHandler<Commit> {
	private static final String BRANCH_START = "refs/heads/";
	private static final int BRANCH_OFFSET = BRANCH_START.length();
	private static final String TAG_START = "refs/tags/";
	private static final int TAG_OFFSET = TAG_START.length();
	
	private static final Type RESOURCE_CHANGE = 
		ScmTypes.TF.tupleType(ScmTypes.Resource.getAbstractType(), ScmTypes.RevisionChange.getAbstractType());
	//private static final Type REV_RESOURCE_CHANGE =
	//	ScmTypes.TF.tupleType(ScmTypes.Revision.getAbstractType(), ScmTypes.Resource.getAbstractType(), ScmTypes.RevisionChange.getAbstractType());
	
	private static final boolean DEBUG = false;
	
	private final String repositoryUrl;
	
	//this set holds the previous merge commits with the same commit sha! 
	private final Set<Commit> todoMerges;
	//private final Map<String, Set<Commit>> merges;
	
	public GitLogEntryHandler(IConstructor repository, RascalFunction factExtractor, IListWriter logEntriesWriter) {
		super(repository, factExtractor, logEntriesWriter);
		IConstructor connection = ScmTypes.Repository.getConnection(repository);
		repositoryUrl = ScmTypes.Connection.getUrl(connection).getValue();
		
		todoMerges = new HashSet<Commit>();
	}
	
	/**
	 * Processes the committer or author info from the commit object.
	 * Note that the commit message will only be set of author parameter is false.
	 * @param commit to process
	 * @param author if true, the author info will be processed, otherwise the committer info will be processed
	 * @return the Info as an IConstructor object
	 */
	private IConstructor processInfo(Commit commit, boolean author) {
		String name = null;
		String date = null;
		
		if (author) {
			name = commit.getAuthor();
			date = commit.getDateString();
		} else {
			name = commit.getCommitter();
			date = commit.getCommitDate();
		}
		
		if (date == null) {
			return null; //we don't have any committer info!
			//throw new IllegalArgumentException("Can't process any " +
			//	(author ? "author" : "committer") + " info without any date on commit " + commit.getSha());
		}

		try {
			long dateInstance = GitProvider.DATE_FORMAT.parse(date).getTime();
			return ScmTypes.Info.makeInfo(ScmTypes.VF.datetime(dateInstance), name, (author == true ? null : commit.getMessage()));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
	
	private ISet processTags(Commit commit) {
		String[] tagNames = commit.getTags();
		if (tagNames == null || tagNames.length == 0) {
			return null;
		}
		
		ISetWriter tagsWriter = ScmTypes.VF.setWriter(ScmTypes.Tag.getAbstractType());
		for (String tagName : tagNames) {
			String tag = tagName.trim();
			Tag type;
			
			if (tag.startsWith(BRANCH_START)) {
				tag = tag.substring(BRANCH_OFFSET);
				type = Tag.BRANCH;
			} else {
				if (tag.startsWith(TAG_START)) {
					tag = tag.substring(TAG_OFFSET);
				}
				type = Tag.LABEL;
			}
			
			tagsWriter.insert(type.make(tag));
		}
		return tagsWriter.done();
	}
	
	private IConstructor processRevision(Commit commit) {
		IConstructor sha = Sha.COMMIT.make(commit.getSha());
		IConstructor commitRevision;
		if (commit.getParentSha() != null) {
			IConstructor oldSha = Sha.COMMIT.make(commit.getParentSha());
			commitRevision = Revision.REVISION_PARENT.make(RevisionId.HASH.make(sha), Revision.REVISION.make(RevisionId.HASH.make(oldSha)));
		} else {
			commitRevision = Revision.REVISION.make(RevisionId.HASH.make(sha));
		}
		
		ISet tags = processTags(commit);
		if (tags != null) {
			commitRevision = Annotation.TAGS.set(commitRevision, tags);
		}
		return commitRevision;
	}
	
	/**
	 * TODO remove the code duplication with {@link #handleLogEntry(Commit)}
	 * @return
	 */
	public boolean callBackToDoMerges() {
		if (todoMerges.size() == 0) {
			return false;
		}
		List<String> todoMergeParents = null;
		Commit random = null;
		IListWriter mergeDetails = ScmTypes.VF.listWriter(MergeDetail.getAbstractType());
		for (Commit mergeCommit : todoMerges) {
			if (random == null) {
				random = mergeCommit;
				todoMergeParents = random.getMergeDetails();
			}
			String mergeOrigin = mergeCommit.getMergeOrigin();
			boolean removed = todoMergeParents.remove(mergeOrigin);
			if (!removed) {
				System.err.println("The merge origin '" + mergeOrigin + "' was not in the mergeDetails list: '" + mergeDetails + "'");
			}
			IConstructor mergeOriginRevision = Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(mergeOrigin)));
			IRelationWriter resources = ScmTypes.VF.relationWriter(RESOURCE_CHANGE);
			for (CommitFile file : mergeCommit.getFiles()) {
				resources.insert(processCommitFile(file));
			}
			mergeDetails.append(MergeDetail.RESOURCES.make(mergeOriginRevision, resources.done()));
		}
		
		for (String todoParent : todoMergeParents) {
			mergeDetails.append(MergeDetail.PARENT.make(Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(todoParent)))));
		}
		
		IConstructor commitRevision = processRevision(random);
		commitRevision = Annotation.MERGE_DETAIL.set(commitRevision, mergeDetails.done());
		
		IConstructor author = processInfo(random, true);
		IConstructor committer = processInfo(random, false);
		if (committer == null) { 
			//if we don't have any committer info, lets copy it from the author and set the commit message, since the author doesn't have it
			committer = Info.makeInfo(Info.getDate(author), Info.getAuthorName(author).getValue(), random.getMessage());
		}
		
		IConstructor changeSet = ScmTypes.ChangeSet.CHANGE_SET.make(commitRevision, ScmTypes.VF.relation(RESOURCE_CHANGE), committer);
		if (author != null) {
			changeSet = Annotation.AUTHOR.set(changeSet, author);
		}
		
		IConstructor result = callBack(changeSet);
		
		todoMerges.clear();
		return true;
	}
	
	public void handleLogEntry(Commit c) {
		if (c.getMergeOrigin() != null) {
			if (todoMerges.size() > 0 && !todoMerges.iterator().next().getSha().equals(c.getSha())) {
				//this is another merge, lets process the previous merge commits first!
				callBackToDoMerges();
			}
			todoMerges.add(c);
			return; //we may have more merge commits with the same sha to process
		}
		
		IConstructor author = processInfo(c, true);
		IConstructor committer = processInfo(c, false);
		if (committer == null && author != null) { 
			//if we don't have any committer info, lets copy it from the author and set the commit message, since the author doesn't have it
			committer = Info.makeInfo(Info.getDate(author), Info.getAuthorName(author).getValue(), c.getMessage());
		}
		
		IRelationWriter resources = ScmTypes.VF.relationWriter(RESOURCE_CHANGE);
		if (c.getFiles() != null) {
			for (CommitFile file : c.getFiles()) {
				resources.insert(processCommitFile(file));
			}
		}
		
		IConstructor commitRevision = processRevision(c);
		
		if (c.getMergeDetails() != null) {
			//we don't have the detailed merge origin info, but we do know the merge parents!
			IListWriter mergeDetails = ScmTypes.VF.listWriter(MergeDetail.getAbstractType());
			for (String parent : c.getMergeDetails()) {
				mergeDetails.append(MergeDetail.PARENT.make(Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(parent)))));
			}
			commitRevision = Annotation.MERGE_DETAIL.set(commitRevision, mergeDetails.done());
		}
		
		IConstructor changeSet = ScmTypes.ChangeSet.CHANGE_SET.make(commitRevision, resources.done(), committer);
		if (author != null) {
			changeSet = Annotation.AUTHOR.set(changeSet, author);
		}
		
		IConstructor result = callBack(changeSet);
	}

	public void handleLogEntryOld(Commit c) {
		String commitSha = c.getSha();
		
//		Set<Commit> mergesFrom = null;
//		if (c.getMergeDetails() != null && c.getMergeOrigin() != null) {
//			List<String> mergeDetails = c.getMergeDetails();
//			
//			if (merges.containsKey(commitSha)) {
//				mergesFrom = merges.get(commitSha);
//			} else {
//				mergesFrom = new HashSet<Commit>(mergeDetails.size());
//				merges.put(commitSha, mergesFrom);
//			}
//			mergesFrom.add(c);
//			
//			if (mergesFrom.size() < mergeDetails.size()) {
//				//we are not ready yet to handle the logentry
//				return;
//			} else {
//				//we have all the merge from commits, so lets hande it!
//				merges.remove(commitSha);
//			}
//		}
		
//		if (DEBUG) {
//			System.out.println("\ncommit " + c.getSha() + "(" + c.getTags() + ")");
//			if (c.getMergeDetails() != null) {
//				System.out.println("\ncommit " + c.getSha() + "(" + c.getTags() + ")");
//				StringBuilder builder = new StringBuilder();
//				for (String part : c.getMergeDetails()) {
//					builder.append(part);
//					builder.append(" ");
//				}
//				System.out.println("Merge: " + builder.toString());
//			}
//			System.out.println("Author: " + c.getAuthor());
//			System.out.println("Date: " + c.getDateString());
//			System.out.println("\n" + c.getMessage() + "\n");
//		}
		
		
		
		
//		Type resRelType;
//		if (mergesFrom != null) {
//			resRelType = ScmTypes.TF.tupleType(ScmTypes.Revision.getAbstractType(), ScmTypes.Resource.getAbstractType(), ScmTypes.RevisionChange.getAbstractType());
//		}
		
		
		IConstructor sha = Sha.COMMIT.make(c.getSha());
		IConstructor commitRevision;
		if (c.getParentSha() != null) {
			IConstructor oldSha = Sha.COMMIT.make(c.getParentSha());
			commitRevision = Revision.REVISION_PARENT.make(RevisionId.HASH.make(sha), Revision.REVISION.make(RevisionId.HASH.make(oldSha)));
		} else {
			commitRevision = Revision.REVISION.make(RevisionId.HASH.make(sha));
		}
		
		
//			if (DEBUG) {
//				for (CommitFile file : c.getFiles()) {
//					System.out.println(":" + (file.getOldPermissions() != null ? file.getOldPermissions() : "000000") + 
//						" " + file.getPermissions() + " " + 
//						(file.getOldSha() != null ? file.getOldSha() + "... " : "0000000...") +
//						file.getSha() + "... " + file.getChangeStatus() + " " + 
//						(file.getOriginName() != null ? file.getOriginName() + " " : "") + 
//						file.getName());
//				}
//				for (CommitFile file : c.getFiles()) {
//					System.out.println(+ file.getLinesAdded() + "       " + file.getLinesDeleted() + 
//						"       " + (file.getOriginName() != null ? file.getOriginName() + " => " : "") + file.getName());
//				}
//			}
			
		
		//FIXME somehow merge changesets have no parent :|, but they do have the mergeparents
//		if (c.getMergeDetails() != null) {
//			IListWriter mergeParents = ScmTypes.VF.listWriter(Revision.getAbstractType());
//			
//			//System.out.println("\ncommit " + c.getSha() + "(" + c.getTags() + ")");
//			StringBuilder builder = new StringBuilder();
//			for (String parent : c.getMergeDetails()) {
//				mergeParents.append(Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(parent))));
//				builder.append(parent);
//				builder.append(" ");
//			}
//			//System.out.println("Merge: " + builder.toString());
//			revision = ScmTypes.Annotation.MERGE_PARENTS.set(revision, mergeParents.done());
//		}
//		
//		if (c.getMergeOrigin() != null) {
//			revision = Annotation.MERGE_ORIGIN.set(revision, ScmTypes.Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(c.getMergeOrigin()))));
//		}
		ISet tags = processTags(c);
		if (tags != null) {
			commitRevision = Annotation.TAGS.set(commitRevision, tags);
		}
		
		IConstructor author = processInfo(c, true);
		IConstructor committer = processInfo(c, false);
		if (committer == null && author != null) { 
			//if we don't have any committer info, lets copy it from the author and set the commit message, since the author doesn't have it
			committer = Info.makeInfo(Info.getDate(author), Info.getAuthorName(author).getValue(), c.getMessage());
		}
		
		if (c.getMergeOrigin() != null) {
			if (todoMerges.size() > 0 && !todoMerges.iterator().next().getSha().equals(commitSha)) {
				//this is another merge, lets process the previous merge commits first!
				callBackToDoMerges();
			}
			todoMerges.add(c);
		} else {
			IRelationWriter resources = ScmTypes.VF.relationWriter(RESOURCE_CHANGE);
			if (c.getFiles() != null) {
				for (CommitFile file : c.getFiles()) {
					resources.insert(processCommitFile(file));
				}
			}
			if (c.getMergeDetails() != null) {
				//we don't have the detailed merge origin info, but we do know the merge parents!
				IListWriter mergeDetails = ScmTypes.VF.listWriter(MergeDetail.getAbstractType());
				for (String parent : c.getMergeDetails()) {
					mergeDetails.append(MergeDetail.PARENT.make(Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(parent)))));
				}
				commitRevision = Annotation.MERGE_DETAIL.set(commitRevision, mergeDetails.done());
			}
			IConstructor changeSet = ScmTypes.ChangeSet.CHANGE_SET.make(commitRevision, resources.done(), committer);
			if (author != null) {
				changeSet = Annotation.AUTHOR.set(changeSet, author);
			}
			IConstructor result = callBack(changeSet);
		}
		
		
//		IConstructor changeSet;
//		if (mergesFrom != null) {
//			IListWriter mergeDetails = ScmTypes.VF.listWriter(MergeDetail.getAbstractType());
//			for (Commit other : mergesFrom) { //for each merge partner
//				IRelationWriter resources = ScmTypes.VF.relationWriter(RESOURCE_CHANGE); //the merged resources;
//				IConstructor mergeOriginRevision = Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(other.getMergeOrigin())));
//				for (CommitFile file : other.getFiles()) {
//					resources.insert(processCommitFile(file));
//				}
//				mergeDetails.append(MergeDetail.RESOURCES.make(mergeOriginRevision, resources.done()));
//			}
//			commitRevision = Annotation.MERGE_DETAIL.set(commitRevision, mergeDetails.done());
//			changeSet = ScmTypes.ChangeSet.CHANGE_SET.make(commitRevision, ScmTypes.VF.relation(RESOURCE_CHANGE), committer);
//			mergesFrom = null;
//		} else {
//			IRelationWriter resources = ScmTypes.VF.relationWriter(RESOURCE_CHANGE);
//			if (c.getFiles() != null) {
//				for (CommitFile file : c.getFiles()) {
//					resources.insert(processCommitFile(file));
//				}
//			}
//			if (c.getMergeDetails() != null) {
//				IListWriter mergeDetails = ScmTypes.VF.listWriter(MergeDetail.getAbstractType());
//				//IListWriter mergeParents = ScmTypes.VF.listWriter(Revision.getAbstractType());
//				for (String parent : c.getMergeDetails()) {
//					mergeDetails.append(MergeDetail.PARENT.make(Revision.REVISION.make(RevisionId.HASH.make(Sha.COMMIT.make(parent)))));
//				}
//				commitRevision = Annotation.MERGE_DETAIL.set(commitRevision, mergeDetails.done());
//			}
//			changeSet = ScmTypes.ChangeSet.CHANGE_SET.make(commitRevision, resources.done(), committer);
//		}
//		
//		changeSet = ScmTypes.Annotation.AUTHOR.set(changeSet, author);
//		
//		IConstructor result = callBack(changeSet);
	}
	
	private ITuple processCommitFile(CommitFile file) {
		String shaHash = file.getSha();
		if (shaHash == null) {
			shaHash = "0000000";
		}
		IConstructor blobSha = Sha.BLOB.make(shaHash);
		
		IConstructor blobRevision;
		if (file.getOldSha() != null) {
			IConstructor oldSha = Sha.BLOB.make(file.getOldSha());
			blobRevision = Revision.REVISION_PARENT.make(RevisionId.HASH.make(blobSha), Revision.REVISION.make(RevisionId.HASH.make(oldSha)));
		} else {
			blobRevision = Revision.REVISION.make(RevisionId.HASH.make(blobSha));
		}
		boolean hasOrigin = (file.getOriginName() != null);
		
		ScmEntryChangeKind changeStatus = file.getChangeStatus();
		ScmTypes.RevisionChange changeType = ScmTypes.RevisionChange.from(changeStatus, hasOrigin);
		
		IConstructor revisionChange;
		if (hasOrigin) {
			revisionChange = changeType.make(blobRevision, Resource.FILE.make(Scm.createResourceId(repositoryUrl, file.getOriginName())));	
			if (changeStatus instanceof ChangeCodeValue) {
				try {
					int percent = Integer.parseInt(((ChangeCodeValue) changeStatus).getStatusValue());
					revisionChange = Annotation.ORIGIN_PERCENT.set(revisionChange, ScmTypes.VF.integer(percent));
				} catch (NumberFormatException e) {
					System.err.println("Cannot parse the origin percent value from '" + changeStatus + "' : " + e.getMessage());
				}
			}
		} else {
			revisionChange = changeType.make(blobRevision);
		}
		int linesAdded = file.getLinesAdded();
		if (linesAdded >= 0) {
			revisionChange = Annotation.LINES_ADDED.set(revisionChange, ScmTypes.VF.integer(linesAdded));
		}
		int linesDeleted = file.getLinesDeleted();
		if (linesAdded >= 0) {
			revisionChange = Annotation.LINES_REMOVED.set(revisionChange, ScmTypes.VF.integer(linesDeleted));
		}
		
		IConstructor fileResource = Resource.FILE.make(Scm.createResourceId(repositoryUrl, file.getName()));
		
		return ScmTypes.VF.tuple(fileResource, revisionChange);	
	}
	
//	public void finish() {
//		for (String todo : merges.keySet()) {
//			System.out.println("Forgotten merge '" + todo + "'");
//			
//		}
//	}
	
}

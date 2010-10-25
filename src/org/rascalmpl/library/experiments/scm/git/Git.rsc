module experiments::scm::git::Git
import experiments::scm::Scm;

data Repository = git(Connection conn, str mod, set[LogOption] options);
data LogOption = mergeDetails() | fileDetails() |
				 symdiff(CheckoutUnit from, CheckoutUnit to) | 
				 onlyMerges() | noMerges() | reverse() | allBranches();				 

data ChangeSet 	= changeset(Revision revision, rel[Resource resource, RevisionChange change] resources, Info committer);
data RevisionChange = renamed(Revision revision, Resource origin) |
					  copied(Revision revision, Resource origin);
data RevisionId	= hash(Sha sha);
data Sha	= blob(str sha) | commit(str sha);

data MergeDetail = mergeResources(Revision parent, rel[Resource resource, RevisionChange change] resources);
data WcResource = wcResource(Resource resource);
data CheckoutUnit = cunit(Revision revision) | cunit(Tag symname);

anno loc Connection@logFile;
anno Info ChangeSet@author;
anno int RevisionChange@originPercent;
anno int RevisionChange@linesAdded;
anno int RevisionChange@linesRemoved;
anno list[MergeDetail] Revision@mergeDetails;
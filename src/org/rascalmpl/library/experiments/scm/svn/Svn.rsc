module experiments::scm::svn::Svn
import experiments::scm::Scm;

data Repository = svn(Connection conn, str mod, loc workspace, set[LogOption] options);
data Connection = ssh(str url, str username, str password) |
				  ssh(str url, str username, str password, loc privateKey);
data LogOption = mergeDetails() | fileDetails();

data ChangeSet 	= changeset(Revision revision, rel[Resource resource, RevisionChange change] resources, Info committer);
data RevisionChange = added(Revision revision, Resource origin) |
					  replaced(Revision revision) | replaced(Revision revision, Resource origin);
data RevisionId	= id(int id);

data MergeDetail = mergeParent(Revision parent);
data WcResource = wcResourceRevisionInfo(Resource resource, Revision revision, Info info);
data CheckoutUnit = cunit(datetime date) | cunit(Revision revision);

anno list[MergeDetail] Revision@mergeDetails;
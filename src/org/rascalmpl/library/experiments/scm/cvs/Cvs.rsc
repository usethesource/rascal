module experiments::scm::cvs::Cvs
import experiments::scm::Scm;

data Repository = cvs(Connection conn, str mod, loc workspace, set[LogOption] options);
data Connection = pserver(str url, str repname, str host, str username, str password);

data ChangeSet = resource(Resource resource, rel[RevisionChange change, Info committer] revisions, rel[Revision revision, Tag symname] revTags);
data RevisionId	= number(str number);

data WcResource = wcResourceRevisionInfo(Resource resource, Revision revision, Info info);
data CheckoutUnit = cunit(datetime date);

anno loc Connection@logFile;
anno int RevisionChange@linesAdded;
anno int RevisionChange@linesRemoved;
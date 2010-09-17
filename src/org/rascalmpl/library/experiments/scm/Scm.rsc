module experiments::scm::Scm

data Project = project(Repository configuration, list[ChangeSet] changesets);

data Repository;
data Connection = fs(str url);
data LogOption = startUnit(CheckoutUnit unit) | endUnit(CheckoutUnit unit);

data ChangeSet;
data RevisionChange = added(Revision revision) | modified(Revision revision) | removed(Revision revision);
data Revision = revision(RevisionId id) | revision(RevisionId id, Revision parent);
data RevisionId;
data Info 	= none(datetime date) | author(datetime date, str name) | 
			  message(datetime date, str message) | message(datetime date, str name, str message);
data Resource = file(loc id) | folder(loc id) | folder(loc id, set[Resource] resources);
data WcResource;

data CheckoutUnit;
data Tag 	= label(str name) | branch(str name);

anno set[Tag] Revision@tags;

@javaClass{org.rascalmpl.library.experiments.scm.Scm}
public list[ChangeSet] java getChangesets(Repository repository);

@javaClass{org.rascalmpl.library.experiments.scm.Scm}
public void java getChangesets(Repository repository, ChangeSet (ChangeSet) callBack);

@javaClass{org.rascalmpl.library.experiments.scm.Scm}
public void java checkoutResources(CheckoutUnit unit, Repository repository);

@javaClass{org.rascalmpl.library.experiments.scm.Scm}
public set[WcResource] java getResources(Repository repository);

@javaClass{org.rascalmpl.library.experiments.scm.Scm}
public map[Resource, int] java linesCount(set[Resource] files);

@javaClass{org.rascalmpl.library.experiments.scm.Scm}
public set[Resource] java buildResourceTree(set[Resource] files);
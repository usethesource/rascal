package org.rascalmpl.library.experiments.scm;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.library.experiments.scm.ScmEntryChangeKind.ChangeCode;
import org.rascalmpl.values.ValueFactoryFactory;

public interface ScmTypes {
	public static final TypeFactory TF = TypeFactory.getInstance();
	public static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	public static final TypeStore store = new TypeStore();
	
	public enum Annotation {
		LOG_FILE("logFile",
			TF.sourceLocationType(), Connection.getAbstractType()),
		AUTHOR("author",
			Info.getAbstractType(), ChangeSet.getAbstractType()),
		ORIGIN_PERCENT("originPercent", 
			TF.integerType(), RevisionChange.getAbstractType()),
		LINES_ADDED("linesAdded", 
			TF.integerType(), RevisionChange.getAbstractType()),
		LINES_REMOVED("linesRemoved", 
			TF.integerType(), RevisionChange.getAbstractType()),
		MERGE_DETAIL("mergeDetails",
			TF.listType(MergeDetail.getAbstractType()), Revision.getAbstractType()),
		TAGS("tags",
			TF.setType(Tag.getAbstractType()), Revision.getAbstractType());
		private final String label;
		
		private Annotation(String label, Type valueType, Type onType) {
			this.label = label;
			store.declareAnnotation(onType, label, valueType);
		}
		
		public boolean has(IConstructor constructor) {
			return constructor.hasAnnotation(label);
		}
		
		public <E extends IValue> E get(IConstructor constructor, Class<E> type) {
			return type.cast(constructor.getAnnotation(label));
		}
		
		public IConstructor set(IConstructor constructor, IValue annotation) {
			return constructor.setAnnotation(label, annotation);
		}
	}
		
	public enum AbstractDataType {
		PROJECT("Project"),
		CONNECTION("Connection"),
		REPOSITORY("Repository"),
		LOG_OPTION("LogOption"),
		TAG("Tag"),
		SHA("Sha"),
		INFO("Info"),
		REVISION_ID("RevisionId"),
		REVISION("Revision"),
		REVISION_CHANGE("RevisionChange"),
		RESOURCE("Resource"),
		CHANGE_SET("ChangeSet"),
		CHECKOUT_UNIT("CheckoutUnit"),
		WC_RESOURCE("WcResource"),
		MERGE_DETAIL("MergeDetail");
		
		private final Type type;

		private AbstractDataType(String name) {
			type = TF.abstractDataType(store, name);
		}
		
		public Type getType() {
			return type;
		}
	}
	
	public enum Project {
		PROJECT("project", 
			Repository.getAbstractType(), "configuration",
			TF.listType(ChangeSet.getAbstractType()), "changesets");

		private final Type type;
		
		private Project(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.PROJECT.getType();
		}
		
		public static IConstructor getRepository(IConstructor project) {
			return (IConstructor) project.get("configuration");
		}
		
		public static IList getChangeSets(IConstructor project) {
			return (IList) project.get("changesets");
		}
		
		public IConstructor make(IConstructor repository,  IList changeSets) {
			return (IConstructor)  type.make(VF, repository, changeSets);
		}
	}
	
	public enum Connection {
		FS("fs", TF.stringType(), "url"),
		PSERVER("pserver",
			TF.stringType(), "url",
			TF.stringType(), "host",
			TF.stringType(), "username",
			TF.stringType(), "password"),
		SSH("ssh", 
			TF.stringType(), "url",
			TF.stringType(), "username",
			TF.stringType(), "password",
			TF.sourceLocationType(), "privateKey"),
		SSH_KEY("ssh", 
			TF.stringType(), "url",
			TF.stringType(), "username",
			TF.stringType(), "password",
			TF.sourceLocationType(), "privateKey");
		
		private final Type type;

		private Connection(String name, Object... childrenAndLabels) {
			this.type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public static IString getUrl(IConstructor repository) {
			return (IString) repository.get("url");
		}

		public static boolean hasHost(IConstructor repository) {
			return repository.has("host");
		}
		
		public static IString getHost(IConstructor repository) {
			return (IString) repository.get("host");
		}
		
		public static boolean hasUsername(IConstructor repository) {
			return repository.has("username");
		}

		public static IString getUsername(IConstructor repository) {
			return (IString) repository.get("username");
		}

		public static IString getPassword(IConstructor repository) {
			return (IString) repository.get("password");
		}
		
		public static boolean hasPassword(IConstructor repository) {
			return repository.has("password");
		}

		public static ISourceLocation getPrivateKey(IConstructor repository) {
			return (ISourceLocation) repository.get("privateKey");
		}
		
		public static boolean hasPrivateKey(IConstructor repository) {
			return repository.has("privateKey");
		}
		
		public Type getType() {
			return type;
		}

		public static Type getAbstractType() {
			return AbstractDataType.CONNECTION.getType();
		}
		
		public static Connection from(IConstructor connection) {
			Type conType = connection.getConstructorType();
			for (Connection conn : values()) {
				if (conn.getType() == conType) {
					return conn;
				}
			}
			throw new IllegalArgumentException("Unknown connection (" + connection + ") constructor type:" + conType);
		}
	}
	
	public enum Repository {
		CVS("cvs", 
			Connection.getAbstractType(), "conn",
			TF.stringType(), "mod",
			TF.sourceLocationType(), "workspace",
			TF.setType(LogOption.getAbstractType()), "options"),
		SVN("svn", 
			Connection.getAbstractType(), "conn",
			TF.stringType(), "mod",
			TF.sourceLocationType(), "workspace",
			TF.setType(LogOption.getAbstractType()), "options"),
		GIT("git", 
			Connection.getAbstractType(), "conn",
			TF.stringType(), "mod",
			TF.setType(LogOption.getAbstractType()), "options");
		
		private final Type type;
		
		private Repository(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.REPOSITORY.getType();
		}
		
		public static Repository from(IConstructor value) {
			Type type = value.getConstructorType();
			for (Repository repo : values()) {
				if (repo.getType() == type) {
					return repo;
				}
			}
			throw new IllegalArgumentException("Unknown Repository (" + value + ") constructor type:" + type);
		}
		
		public static IConstructor getConnection(IConstructor repository) {
			return (IConstructor) repository.get("conn");
		}
		
		public static IString getModule(IConstructor repository) {
			return (IString) repository.get("mod");
		}
		
		public static ISourceLocation getWorkspace(IConstructor repository) {
			return (ISourceLocation) repository.get("workspace");
		}
		
		public static ISet getOptions(IConstructor repository) {
			return (ISet) repository.get("options");
		}

	}
	
	public enum LogOption {
		START("startUnit", 
			CheckoutUnit.getAbstractType(), "unit"),
		END("endUnit", 
			CheckoutUnit.getAbstractType(), "unit"),
		SYMMETRIC_DIFF("symdiff", 
			CheckoutUnit.getAbstractType(), "from",
			CheckoutUnit.getAbstractType(), "to"),
		MERGE_DETAILS("mergeDetails"),
		FILE_DETAILS("fileDetails"),
		ONLY_MERGES("onlyMerges"),
		NO_MERGES("noMerges"),
		REVERSE("reverse"),
		ALL_BRANCHES("allBranches");

		private final Type type;
		
		private LogOption(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.LOG_OPTION.getType();
		}
		
		public static IConstructor getUnit(IConstructor project) {
			return (IConstructor) project.get("unit");
		}
		
		public static IConstructor getFrom(IConstructor project) {
			return (IConstructor) project.get("from");
		}
		
		public static IConstructor getTo(IConstructor project) {
			return (IConstructor) project.get("to");
		}
		
		public static LogOption from(IConstructor value) {
			Type type = value.getConstructorType();
			for (LogOption option : values()) {
				if (option.getType() == type) {
					return option;
				}
			}
			throw new IllegalArgumentException("No LogOption type for '" + value + "'");
		}
	}
	
	public enum Tag {
		LABEL("label",
			TF.stringType(), "name"),
		BRANCH("branch",
			TF.stringType(), "name");

		private final Type type;
		
		private Tag(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public static IString getName(IConstructor revision) {
			return (IString) revision.get("name");
		}

		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.TAG.getType();
		}
		
		public IConstructor make(String name) {
			return (IConstructor)  type.make(VF, VF.string(name));
		}
	}

	public enum Sha {
		BLOB("blob",
			TF.stringType(), "sha"),
		COMMIT("commit",
			TF.stringType(), "sha");

		private final Type type;
		
		private Sha(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public static IString getSha(IConstructor revision) {
			return (IString) revision.get("sha");
		}

		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.SHA.getType();
		}
		
		public IConstructor make(String sha) {
			return (IConstructor)  type.make(VF, VF.string(sha));
		}
	}
	
	public enum RevisionId {
		NUMBER("number",
			TF.stringType(), "number"),
		ID("id",
			TF.integerType(), "id"),
		HASH("hash",
			Sha.getAbstractType(), "sha");

		private final Type type;
		
		private RevisionId(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public static IInteger getId(IConstructor revision) {
			return (IInteger) revision.get("id");
		}

		public static IString getNumber(IConstructor revision) {
			return (IString) revision.get("number");
		}
		
		public static IConstructor getSha(IConstructor revision) {
			return (IConstructor) revision.get("sha");
		}

		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.REVISION_ID.getType();
		}
		
		public IConstructor make(long id) {
			return (IConstructor)  type.make(VF, VF.integer(id));
		}
		
		public IConstructor make(String number) {
			return (IConstructor) type.make(VF, VF.string(number));
		}
		
		public IConstructor make(IConstructor sha) {
			return (IConstructor) type.make(VF, sha);
		}
	}
	
	public enum Info {
		NONE("none",
			TF.dateTimeType(), "date"),
		AUTHOR("author", 
			TF.dateTimeType(), "date",
			TF.stringType(), "name"),
		MESSAGE("message",
			TF.dateTimeType(), "date",
			TF.stringType(), "message"),
		AUTHOR_MESSAGE("message", 
			TF.dateTimeType(), "date",
			TF.stringType(), "name",
			TF.stringType(), "message");
		private final Type type;
		
		private Info(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}

		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.INFO.getType();
		}
		
		public static IDateTime getDate(IConstructor logMessage) {
			return (IDateTime) logMessage.get("date");
		}
		
		public static IString getAuthorName(IConstructor logMessage) {
			return (IString) logMessage.get("name");
		}
		
		public static IString getMessage(IConstructor logMessage) {
			return (IString) logMessage.get("message");
		}
		
		public IConstructor make(IDateTime date) {
			return (IConstructor) type.make(VF, date);
		}
		
		/**
		 * @param date represents the date of the logMessage
		 * @param arg can be author or message
		 */
		public IConstructor make(IDateTime date, String arg) {
			return (IConstructor) type.make(VF, date, VF.string(arg));
		}
		
		public IConstructor make(IDateTime date, String authorName, String message) {
			return (IConstructor) type.make(VF, date, VF.string(authorName), VF.string(message));
		}
		
		public static IConstructor makeInfo(IDateTime datetime, String authorName, String message) {
			if (authorName == null && message == null) {
            	return ScmTypes.Info.NONE.make(datetime);
            } else if (authorName != null && message == null) {
            	return ScmTypes.Info.AUTHOR.make(datetime, authorName);
            } else if (authorName == null && message != null) {
            	return ScmTypes.Info.MESSAGE.make(datetime, message);
            }
            	
			return ScmTypes.Info.AUTHOR_MESSAGE.make(datetime, authorName, message);
		}
	}
	
	public enum Revision {
		REVISION("revision", 
			RevisionId.getAbstractType(), "id"),
		REVISION_PARENT("revision", 
			RevisionId.getAbstractType(), "id",
			getAbstractType(), "parent");
		
		private final Type type;
		
		private Revision(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		/**
		 * @return the id of the revision as an {@link RevisionId}.
		 */
		public static IConstructor getId(IConstructor revision) {
			return (IConstructor) revision.get("id");
		}
		
		public static IConstructor getParent(IConstructor revision) {
			return (IConstructor) revision.get("parent");
		}
		
		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.REVISION.getType();
		}
		
		/**
		 * @param id of type {@link RevisionId}
		 */
		public IConstructor make(IConstructor id) {
			return (IConstructor) type.make(VF, id);
		}
		
		/**
		 * @param id of type {@link RevisionId}
		 * @param parent of type {@link Revision}
		 */
		public IConstructor make(IConstructor id, IConstructor parent) {
			return (IConstructor) type.make(VF, id, parent);
		}		
	}
	
	public enum RevisionChange {
		ADDED("added", ChangeCode.ADDED,
			Revision.getAbstractType(), "revision"),
		ADDED_ORIGIN("added", ChangeCode.ADDED,
			Revision.getAbstractType(), "revision",
			Resource.getAbstractType(), "origin"),
		RENAMED("renamed", ChangeCode.RENAMED,
			Revision.getAbstractType(), "revision"),
		RENAMED_ORIGIN("renamed", ChangeCode.RENAMED,
			Revision.getAbstractType(), "revision",
			Resource.getAbstractType(), "origin"),
		COPIED("copied", ChangeCode.COPIED,
			Revision.getAbstractType(), "revision"),
		COPIED_ORIGIN("copied", ChangeCode.COPIED,
			Revision.getAbstractType(), "revision",
			Resource.getAbstractType(), "origin"),
		REPLACED("replaced", ChangeCode.REPLACED,
			Revision.getAbstractType(), "revision"),
		REPLACED_ORIGIN("replaced", ChangeCode.REPLACED,
			Revision.getAbstractType(), "revision",
			Resource.getAbstractType(), "origin"),
		REMOVED("removed", ChangeCode.DELETED,
			Revision.getAbstractType(), "revision"),
		MODIFIED("modified", ChangeCode.MODIFIED,
			Revision.getAbstractType(), "revision");
		
		private final boolean hasOrigin;
		private final ChangeCode changeStatusCode;
		private final Type type;
		
		private RevisionChange(String name, ChangeCode changeStatusCode, Object... childrenAndLabels) {
			this.changeStatusCode = changeStatusCode;
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
			hasOrigin = type.hasField("origin");
		}
		
		public static IConstructor getRevision(IConstructor change) {
			return (IConstructor) change.get("revision");
		}
		
		public static boolean hasOrigin(IConstructor change) {
			return change.has("origin");
		}

		public static IConstructor getOrigin(IConstructor change) {
			return (IConstructor) change.get("origin");
		}
		
		public static boolean hasLinesAdded(IConstructor change) {
			return Annotation.LINES_ADDED.has(change);
		}
		
		public static IInteger getLinesAdded(IConstructor change) {
			return Annotation.LINES_ADDED.get(change, IInteger.class);
		}
		
		public static boolean hasLinesRemoved(IConstructor change) {
			return Annotation.LINES_REMOVED.has(change);
		}

		public static IInteger getLinesRemoved(IConstructor change) {
			return Annotation.LINES_REMOVED.get(change, IInteger.class);
		}
		
		public static boolean hasOriginPercent(IConstructor change) {
			return Annotation.ORIGIN_PERCENT.has(change);
		}

		public static IInteger getPercent(IConstructor change) {
			return Annotation.ORIGIN_PERCENT.get(change, IInteger.class);
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.REVISION_CHANGE.getType();
		}
		
		public Type getType() {
			return type;
		}
		
		public IConstructor make() {
			return (IConstructor) type.make(VF);
		}
		
		public IConstructor make(IConstructor revision) {
			return (IConstructor) type.make(VF, revision);
		}
		
		public IConstructor make(IConstructor revision, IConstructor origin) {
			return (IConstructor) type.make(VF, revision, origin);
		}
		
//		public IConstructor make(IConstructor revisionId, IInteger linesAdded, IInteger linesRemoved) {
//			IConstructor rev = make(revisionId);
//			rev = Annotation.LINES_ADDED.set(rev, linesAdded);
//			rev = Annotation.LINES_REMOVED.set(rev, linesRemoved);
//			return rev;
//		}
		
		public static RevisionChange from(ScmEntryChangeKind changeKind, boolean hasOrigin) {
			for (RevisionChange changeType : values()) {
				if (changeType.changeStatusCode == changeKind.getStatusCode()) {
					if (changeType.hasOrigin == hasOrigin) {
						return changeType;
					}
				}
				
			}
			throw new IllegalArgumentException("No ResourceChange type for '" + changeKind + 
					"(" + changeKind.getStatusCode() + ")"+ "' with" + (hasOrigin ? "" :"out") + " origin");
		}
	}
	
	public enum Resource {
		FILE("file", 
			TF.sourceLocationType(), "id"),
		FOLDER("folder", 
			TF.sourceLocationType(), "id"),
		FOLDER_CONTENT("folder", 
			TF.sourceLocationType(), "id", 
			TF.setType(AbstractDataType.RESOURCE.getType()), "resources");
		
		private final Type type;
		
		private Resource(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}
		
		public static ISourceLocation getId(IConstructor resource) {
			return (ISourceLocation) resource.get("id");
		}
		
		public static boolean hasResources(IConstructor resource) {
			return resource.has("resources");
		}
		
		public static ISet getResources(IConstructor resource) {
			return (ISet) resource.get("resources");
		}
		
		public static Resource from(IConstructor value) {
			Type type = value.getConstructorType();
			for (Resource res : values()) {
				if (res.getType() == type) {
					return res;
				}
			}
			throw new IllegalArgumentException("No Resource type for '" + value + "'");
		}
		
		public Type getType() {
			return type;
		}
		
		public static Type getAbstractType() {
			return AbstractDataType.RESOURCE.getType();
		}

		public IConstructor make(ISourceLocation id) {
			return (IConstructor) type.make(VF, id);
		}
		
		public IConstructor make(ISourceLocation id, ISet content) {
			return (IConstructor) type.make(VF, id, content);
		}
	}
	
	public enum ChangeSet {
		RESOURCE("resource", 
			Resource.getAbstractType(), "resource",
			TF.relType(
				RevisionChange.getAbstractType(), "change",
				Info.getAbstractType(), "committer"), "revisions",
			TF.relType(
				Revision.getAbstractType(), "revision",
				Tag.getAbstractType(), "symname"), "revTags"),
		CHANGE_SET("changeset", 
			Revision.getAbstractType(), "revision",
			TF.relType(
				Resource.getAbstractType(), "resource",
				RevisionChange.getAbstractType(), "change"), "resources",
			Info.getAbstractType(), "committer");
		
		private final Type type;
		
		private ChangeSet(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}

		public static Type getAbstractType() {
			return AbstractDataType.CHANGE_SET.getType();
		}
		
		public Type getType() {
			return type;
		}
		
		/**
		 * Only available on the type {@link ChangeSet#RESOURCE}
		 * @return the resource field as an {@link Resource} value
		 */
		public IConstructor getResource(IConstructor changeSet) {
			return (IConstructor) changeSet.get("resource");
		}
		
		/**
		 * Only available on the type {@link ChangeSet#RESOURCE}
		 * @return the revisions of the resource as an relation between {@link RevisionChange} and {@link Info}.
		 */
		public IRelation getRevisions(IConstructor changeSet) {
			return (IRelation) changeSet.get("revisions");
		}

		/**
		 * Only available on the type {@link ChangeSet#RESOURCE}
		 * @return the relation between {@link Revision}s and {@link Tag}s.
		 */
		public IRelation getRevTags(IConstructor changeSet) {
			return (IRelation) changeSet.get("revTags");
		}

		
		/**
		 * Available on the type {@link ChangeSet#CHANGE_SET}
		 * @return the revision of the changeset as an {@link Revision}.
		 */
		public IConstructor getRevision(IConstructor changeSet) {
			return (IConstructor) changeSet.get("revision");
		}
		
		/**
		 * Available on the type {@link ChangeSet#CHANGE_SET}
		 * @return the resources (in relation with the {@link RevisionChange}) in the changeset.
		 */
		public IRelation getResources(IConstructor changeSet) {
			return (IRelation) changeSet.get("resources");
		}

		/**
		 * Available on the type {@link ChangeSet#CHANGE_SET}
		 * @return the committer information as an {@link Info} value.
		 */
		public IConstructor getCommitter(IConstructor changeSet) {
			return (IConstructor) changeSet.get("committer");
		}
		
		public boolean hasAuthor(IConstructor changeSet) {
			return Annotation.AUTHOR.has(changeSet);
		}
		
		/**
		 * Available on the type {@link ChangeSet#CHANGE_SET} as an optional annotation.
		 * @return the original author information as an {@link Info} value.
		 */
		public IConstructor getAuthor(IConstructor changeSet) {
			return Annotation.AUTHOR.get(changeSet, IConstructor.class);
		}
		
		/**
		 * @return an ChangeSet of type {@link ChangeSet#RESOURCE}
		 */
		public IConstructor make(IConstructor resource, IRelation revisions, IRelation revTags) {
			return (IConstructor) getType().make(VF, resource, revisions, revTags);
		}
		
		/**
		 * @return an ChangeSet of type {@link ChangeSet#CHANGE_SET}
		 */
		public IConstructor make(IConstructor revision, IRelation resources, IConstructor committer) {
			return (IConstructor) getType().make(VF, revision, resources, committer);
		}
		
		/**
		 * @return an ChangeSet of type {@link ChangeSet#CHANGE_SET_BLOB}
		 */
		public IConstructor make(IConstructor revision, IRelation resources, IConstructor committer, IConstructor author) {
			IConstructor changeSet = make(revision, resources, committer);
			return  Annotation.AUTHOR.set(changeSet, author);
		}
	}
	
	public enum CheckoutUnit {
		REVISION("cunit", 
			Revision.getAbstractType(), "revision"),
		DATE("cunit", 
			TF.dateTimeType(), "date"),
		SYM_NAME("cunit", 
			Tag.getAbstractType(), "symname");
		
		private final Type type;
		
		private  CheckoutUnit(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}

		public static Type getAbstractType() {
			return AbstractDataType.CHECKOUT_UNIT.getType();
		}
		
		public Type getType() {
			return type;
		}
		
		public static boolean hasDate(IConstructor checkoutUnit) {
			return checkoutUnit.has("date");
		}
		
		public static IDateTime getDate(IConstructor checkoutUnit) {
			return (IDateTime) checkoutUnit.get("date");
		}
		
		public static boolean hasRevision(IConstructor checkoutUnit) {
			return checkoutUnit.has("revision");
		}
		
		public static IConstructor getRevision(IConstructor checkoutUnit) {
			return (IConstructor) checkoutUnit.get("revision");
		}

		public static boolean hasSymname(IConstructor checkoutUnit) {
			return checkoutUnit.has("symname");
		}
		public static IConstructor getSymname(IConstructor checkoutUnit) {
			return (IConstructor) checkoutUnit.get("symname");
		}
		
		public IConstructor make(IConstructor arg) {
			return (IConstructor) type.make(VF, arg);
		}
				
		public IConstructor make(IDateTime date) {
			return (IConstructor) type.make(VF, date);
		}
	}

	public enum WcResource {
		RESOURCE("wcResource", 
			Resource.getAbstractType(), "resource"),
		RESOURCE_REVISION("wcResourceRevision", 
			Resource.getAbstractType(), "resource",
			Revision.getAbstractType(), "revision"),
		RESOURCE_REVISION_INFO("wcResourceRevision", 
			Resource.getAbstractType(), "resource",
			Revision.getAbstractType(), "revision",
			Info.getAbstractType(), "info");
		
		private final Type type;
		
		private  WcResource(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}

		public static Type getAbstractType() {
			return AbstractDataType.WC_RESOURCE.getType();
		}
		
		public Type getType() {
			return type;
		}
		
		public static boolean hasRevision(IConstructor wcResource) {
			return wcResource.has("revision");
		}
		
		/**
		 * @return the revision field as a Revision
		 */
		public static IConstructor getRevisions(IConstructor wcResource) {
			return (IConstructor) wcResource.get("revision");
		}
		
		public static boolean hasInfo(IConstructor wcResource) {
			return wcResource.has("info");
		}
		
		public static IConstructor getInfo(IConstructor wcResource) {
			return (IConstructor) wcResource.get("info");
		}
		
		public IConstructor make(IConstructor resource) {
			return (IConstructor) type.make(VF, resource);
		}
				
		public IConstructor make(IConstructor resource, IConstructor revision) {
			return (IConstructor) type.make(VF, resource, revision);
		}
		
		public IConstructor make(IConstructor resource, IConstructor revision, IConstructor info) {
			return (IConstructor) type.make(VF, resource, revision, info);
		}
	}
	
	public enum MergeDetail {
		PARENT("mergeParent", 
			Revision.getAbstractType(), "parent"),
		RESOURCES("mergeResources", 
			Revision.getAbstractType(), "parent", 
			TF.relType(Resource.getAbstractType(), RevisionChange.getAbstractType()), "resources");
		
		private final Type type;
		
		private  MergeDetail(String name, Object... childrenAndLabels) {
			type = TF.constructor(store, getAbstractType(), name, childrenAndLabels);
		}

		public static Type getAbstractType() {
			return AbstractDataType.MERGE_DETAIL.getType();
		}
		
		public Type getType() {
			return type;
		}
		
		public static IList getParent(IConstructor mergeDetails) {
			return (IList) mergeDetails.get("parent");
		}
		
		public static IRelation getResources(IConstructor mergeDetails) {
			return (IRelation) mergeDetails.get("resources");
		}
		
		public IConstructor make(IConstructor parent) {
			return (IConstructor) type.make(VF, parent);
		}
		
		public IConstructor make(IConstructor parent, IRelation resources) {
			return (IConstructor) type.make(VF, parent, resources);
		}
		
	}
}

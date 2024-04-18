package org.rascalmpl.library.lang.java.m3.internal;

import java.util.Stack;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public abstract class M3Converter extends JavaToRascalConverter {
	private static final String DATATYPE_M3_NODE = "M3";
	private final io.usethesource.vallang.type.Type DATATYPE_M3_NODE_TYPE;
	private final io.usethesource.vallang.type.Type DATATYPE_M3_LANGUAGE_TYPE;
	
	protected final Stack<ISourceLocation> scopeManager = new Stack<ISourceLocation>();
	
	protected ISetWriter uses;
	protected ISetWriter declarations;
	protected ISetWriter containment;
	protected ISetWriter extendsRelations;
	protected ISetWriter implementsRelations;
	protected ISetWriter fieldAccess;
	protected ISetWriter methodInvocation;
	protected ISetWriter typeDependency;
	protected ISetWriter documentation;
	protected ISetWriter modifiers;
	protected ISetWriter names;
	protected ISetWriter methodOverrides;
	protected ISetWriter types;
	protected ISetWriter annotations;
	protected final io.usethesource.vallang.type.Type CONSTRUCTOR_M3;
	protected final Type JAVA_LANGUAGE_M3;
	protected ISetWriter languages;
	
	M3Converter(final LimitedTypeStore typeStore, java.util.Map<String, ISourceLocation> cache) {
		super(typeStore, cache, true);
		this.DATATYPE_M3_NODE_TYPE = this.typeStore.lookupAbstractDataType(DATATYPE_M3_NODE);
		this.DATATYPE_M3_LANGUAGE_TYPE = this.typeStore.lookupAbstractDataType("Language");
		TypeFactory tf = TypeFactory.getInstance();
		this.CONSTRUCTOR_M3= this.typeStore.lookupConstructor(DATATYPE_M3_NODE_TYPE, "m3", tf.tupleType(tf.sourceLocationType()));
		this.JAVA_LANGUAGE_M3 = this.typeStore.lookupConstructor(DATATYPE_M3_LANGUAGE_TYPE, "java", tf.tupleEmpty());
		uses = values.setWriter();
		declarations = values.setWriter();
		containment = values.setWriter();
		extendsRelations = values.setWriter();
		implementsRelations = values.setWriter();
		fieldAccess = values.setWriter();
		methodInvocation = values.setWriter();
		modifiers = values.setWriter();
		typeDependency = values.setWriter();
		documentation = values.setWriter();
		names = values.setWriter();
		methodOverrides = values.setWriter();
		annotations = values.setWriter();
		types = values.setWriter();
		languages = values.setWriter();
	}
	
	public IValue getModel(boolean insertErrors) {
		ownValue = values.constructor(CONSTRUCTOR_M3, loc);
		setKeywordParameter("declarations", declarations.done());
		setKeywordParameter("uses", uses.done());
		setKeywordParameter("containment", containment.done());
		setKeywordParameter("extends", extendsRelations.done());
		setKeywordParameter("implements", implementsRelations.done());
		setKeywordParameter("methodInvocation", methodInvocation.done());
		setKeywordParameter("modifiers", modifiers.done());
		setKeywordParameter("typeDependency", typeDependency.done());
		setKeywordParameter("documentation", documentation.done());
		setKeywordParameter("fieldAccess", fieldAccess.done());
		setKeywordParameter("names", names.done());
		setKeywordParameter("methodOverrides", methodOverrides.done());
		setKeywordParameter("types", types.done());
		setKeywordParameter("annotations", annotations.done());
		insertCompilationUnitMessages(insertErrors, messages.done());
		return ownValue;
	}
	
	public ISourceLocation getParent() {
		return scopeManager.peek();
	}
	
	public void insert(ISetWriter relW, IValue lhs, IValue rhs) {
		if ((isValid((ISourceLocation) lhs) && isValid((ISourceLocation) rhs))) {
			relW.insert(values.tuple(lhs, rhs));
		}
	}

	public void insert(ISetWriter relW, IValue lhs, IList rhs) {
		for (IValue oneRHS: rhs) {
			if (lhs.getType().isString() || (isValid((ISourceLocation) lhs) && isValid((ISourceLocation) oneRHS))) {
				insert(relW, lhs, oneRHS);
			}
		}
	}
	
	public void insert(ISetWriter relW, IString lhs, IValue rhs) {
		if (isValid((ISourceLocation) rhs)) {
			relW.insert(values.tuple(lhs, rhs));
		}
	}
	
	public void insert(ISetWriter relW, IValue lhs, IConstructor rhs) {
		if (isValid((ISourceLocation) lhs) && rhs != null) {
			relW.insert(values.tuple(lhs, rhs));
		}
	}
	
	protected boolean isValid(ISourceLocation binding) {
		return binding != null && !(binding.getScheme().equals("unknown") || binding.getScheme().equals("unresolved"));
	}
}

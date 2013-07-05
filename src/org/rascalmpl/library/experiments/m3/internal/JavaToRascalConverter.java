package org.rascalmpl.library.experiments.m3.internal;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.rascalmpl.values.ValueFactoryFactory;

@SuppressWarnings({"rawtypes", "deprecation"})
public abstract class JavaToRascalConverter extends ASTVisitor {
	protected static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	protected static final TypeFactory TF = TypeFactory.getInstance();

	protected final TypeStore typeStore;
	
	protected IValue ownValue;
	private static final String DATATYPE_RASCAL_AST_TYPE_NODE 			= "Type";
	private static final String DATATYPE_RASCAL_AST_MODIFIER_NODE 		= "Modifiers";
	private static final String DATATYPE_RASCAL_AST_DECLARATION_NODE 	= "Declaration";
	private static final String DATATYPE_RASCAL_AST_EXPRESSION_NODE 	= "Expression";
	private static final String DATATYPE_RASCAL_AST_STATEMENT_NODE 		= "Statement";
	
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE;
	protected static org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_TYPE_NODE_TYPE;
	protected static org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE;
	protected CompilationUnit compilUnit;
	protected ISourceLocation loc;
	protected String project;
	
	private BindingsResolver bindingsResolver;
	protected boolean collectBindings;
	
	JavaToRascalConverter(final TypeStore typeStore, boolean collectBindings) {
		//super(true);
		this.typeStore = typeStore;
		this.bindingsResolver = new BindingsResolver(collectBindings);
		DATATYPE_RASCAL_AST_TYPE_NODE_TYPE 		= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_TYPE_NODE);
		DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE = this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_MODIFIER_NODE);
		this.DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE 	= typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_DECLARATION_NODE);
		this.DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE 	= typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_EXPRESSION_NODE);
		this.DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE 	= typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_STATEMENT_NODE);
	}
	
	public void set(CompilationUnit compilUnit) {
		this.compilUnit = compilUnit;
	}
	
	public void set(ISourceLocation loc) {
		this.loc = loc;
		this.project = loc.getURI().getAuthority();
		bindingsResolver.setProject(this.project);
	}
	
	public void set(String project) {
		this.project = project;
	}
	
	protected ISourceLocation resolveBinding(CompilationUnit node) {
		URI compilationUnit = new BindingsResolver(true) {
			public URI resolveBinding(CompilationUnit node) {
				setProject(project);
				return convertBinding("java+compilationUnit", loc.getURI().getPath(), null, null);
			}
		}.resolveBinding(node);
		
		return values.sourceLocation(compilationUnit);
	}
	
	protected ISourceLocation resolveBinding(ASTNode node) {
		if (node instanceof CompilationUnit)
			return resolveBinding((CompilationUnit) node);
		return values.sourceLocation(bindingsResolver.resolveBinding(node));
	}
	
	protected ISourceLocation getSourceLocation(ASTNode node) {
		int start = compilUnit.getExtendedStartPosition(node);
		int end = start + compilUnit.getExtendedLength(node) - 1;
		
		return values.sourceLocation(loc.getURI(), 
				 start, node.getLength(), 
				 compilUnit.getLineNumber(start), compilUnit.getLineNumber(end), 
				 compilUnit.getColumnNumber(start), compilUnit.getColumnNumber(end));
	}
	
	protected IValue[] removeNulls(IValue... withNulls) {
		List<IValue> withOutNulls = new ArrayList<IValue>();
		for (IValue child : withNulls) {
			if (!(child == null))
				withOutNulls.add(child);
		}
		return withOutNulls.toArray(new IValue[withOutNulls.size()]);
	}
	
	protected IValueList parseModifiers(int modifiers) {
		IValueList extendedModifierList = new IValueList(values);
		
		
		for (String constructor: java.lang.reflect.Modifier.toString(modifiers).split(" ")) {
			Set<org.eclipse.imp.pdb.facts.type.Type> exConstr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE, constructor);
			for (org.eclipse.imp.pdb.facts.type.Type con: exConstr) {
				extendedModifierList.add(values.constructor(con));
			}
		}
		
		return extendedModifierList;
	}
	
	protected IValueList parseExtendedModifiers(List ext) {
		IValueList extendedModifierList = new IValueList(values);
	
		for (Iterator it = ext.iterator(); it.hasNext();) {
			ASTNode p = (ASTNode) it.next();
			IValue val = visitChild(p);
			extendedModifierList.add(val);
		}
		return extendedModifierList;
	}
	
	protected IValueList parseExtendedModifiers(BodyDeclaration node) {
		if (node.getAST().apiLevel() == AST.JLS2) {
			return parseModifiers(node.getModifiers());
		} else {
			return parseExtendedModifiers(node.modifiers());
		}
	}
	
	protected IValue visitChild(ASTNode node) {
		node.accept(this);
		return this.getValue();
	}

	public IValue getValue() {
		return this.ownValue;
	}
	
	protected IValue constructModifierNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	protected void setAnnotation(String annoName, IValue annoValue) {
		if(this.ownValue == null) return ;
		if (this.ownValue.getType().declaresAnnotation(this.typeStore, annoName))
			this.ownValue = ((IConstructor) this.ownValue).setAnnotation(annoName, annoValue);
	}
	
	protected IValue constructDeclarationNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE, constructor, args);
		assert constr != null;
		return values.constructor(constr, removeNulls(children));
	}
	
	protected IValue constructExpressionNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	protected IValue constructStatementNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	protected IValue constructTypeNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_TYPE_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	protected void addProblems(IValueList problems) {
		setAnnotation("projectErrors", problems.asList());
	}
}

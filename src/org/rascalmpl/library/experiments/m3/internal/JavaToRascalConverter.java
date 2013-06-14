package org.rascalmpl.library.experiments.m3.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class JavaToRascalConverter extends ASTVisitor {
	protected static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	protected static final TypeFactory TF = TypeFactory.getInstance();

	protected final TypeStore typeStore;
	
	protected IValue ownValue;
	
	protected CompilationUnit compilUnit;
	protected ISourceLocation loc;
	protected String project;
	
	private BindingsResolver bindingsResolver;
	protected boolean collectBindings;

	protected static final String DATATYPE_RASCAL_AST_TYPE_NODE 			= "Type";
	protected static final String DATATYPE_RASCAL_AST_MODIFIER_NODE			= "Modifier";

	protected static org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_TYPE_NODE_TYPE;
	protected static org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE;
	
	JavaToRascalConverter(final TypeStore typeStore, boolean collectBindings) {
		this.typeStore = typeStore;
		this.bindingsResolver = new BindingsResolver(collectBindings);
		DATATYPE_RASCAL_AST_TYPE_NODE_TYPE 		= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_TYPE_NODE);
		DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE		= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_MODIFIER_NODE);
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
	
	protected ISourceLocation resolveBinding(ASTNode node) {
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
		IValueList modifierList = new IValueList(values);
		
		for (String constructor: java.lang.reflect.Modifier.toString(modifiers).split(" ")) {
			Set<org.eclipse.imp.pdb.facts.type.Type> constrs = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE, constructor);
			for (org.eclipse.imp.pdb.facts.type.Type constr: constrs) {
				modifierList.add(values.constructor(constr));
			}
		}
		
		return modifierList;
	}
	
	protected java.util.Map.Entry<IValueList, IValueList> parseExtendedModifiers(List ext) {
		IValueList modifierList = new IValueList(values);
		IValueList annotationsList = new IValueList(values);
	
		for (Iterator it = ext.iterator(); it.hasNext();) {
			ASTNode p = (ASTNode) it.next();
			if(p instanceof IExtendedModifier) {
				IValue val = visitChild(p);
				if(((IExtendedModifier) p).isModifier()) {
					modifierList.add(val);
				} else if(((IExtendedModifier) p).isAnnotation()) {
					annotationsList.add(val);
				}
			}
		}
		return new java.util.AbstractMap.SimpleEntry<IValueList, IValueList>(modifierList, annotationsList);
	}
	
	protected java.util.Map.Entry<IValueList, IValueList> parseExtendedModifiers(BodyDeclaration node) {
		if (node.getAST().apiLevel() == AST.JLS2) {
			return new java.util.AbstractMap.SimpleEntry<IValueList, IValueList>(parseModifiers(node.getModifiers()), new IValueList(values));
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
}

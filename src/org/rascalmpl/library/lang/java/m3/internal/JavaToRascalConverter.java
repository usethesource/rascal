package org.rascalmpl.library.lang.java.m3.internal;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class JavaToRascalConverter extends ASTVisitor {
	protected static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	protected static final TypeFactory TF = TypeFactory.getInstance();

	protected final TypeStore typeStore;
	
	protected IValue ownValue;
	private static final String DATATYPE_RASCAL_AST_TYPE_NODE 			= "Type";
	private static final String DATATYPE_RASCAL_AST_MODIFIER_NODE 		= "Modifier";
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
	
	protected final BindingsResolver bindingsResolver;
	protected final boolean collectBindings;
	
	JavaToRascalConverter(final TypeStore typeStore, boolean collectBindings) {
		super(true);
		this.typeStore = typeStore;
		this.bindingsResolver = new BindingsResolver(typeStore, collectBindings);
		this.collectBindings = collectBindings;
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
	}
	
	protected ISourceLocation resolveBinding(String packageComponent) {
		ISourceLocation packageBinding = new BindingsResolver(typeStore, this.collectBindings) {
			public ISourceLocation resolveBinding(String packageC) {
				try {
					if (collectBindings) {
						if (EclipseJavaCompiler.cache.containsKey(packageC)) {
							return EclipseJavaCompiler.cache.get(packageC);
						}
						return values.sourceLocation("java+package", null, packageC);
					}
					return values.sourceLocation("unknown", null, null);
				} catch (URISyntaxException e) {
					throw new RuntimeException("Should not happen", e);
				}
			}
		}.resolveBinding(packageComponent);
		EclipseJavaCompiler.cache.put(packageComponent, packageBinding);
		return packageBinding;
	}
	
	protected ISourceLocation resolveBinding(CompilationUnit node) {
		ISourceLocation compilationUnit = new BindingsResolver(typeStore, true) {
			public ISourceLocation resolveBinding(CompilationUnit node) {
				return convertBinding("java+compilationUnit", null, loc.getPath());
			}
		}.resolveBinding(node);
		
		return compilationUnit;
	}
	
	protected ISourceLocation resolveBinding(IBinding binding) {
		ISourceLocation resolvedBinding = bindingsResolver.resolveBinding(binding);
		if (binding != null)
			EclipseJavaCompiler.cache.put(binding.getKey(), resolvedBinding);
		return resolvedBinding;
	}
	
	protected ISourceLocation resolveDeclaringClass(IBinding binding) {
		ISourceLocation resolvedBinding;
		if (binding instanceof ITypeBinding) {
		  resolvedBinding = bindingsResolver.resolveBinding(((ITypeBinding) binding).getDeclaringClass());
		} else if (binding instanceof IMethodBinding) {
		  resolvedBinding = bindingsResolver.resolveBinding(((IMethodBinding) binding).getDeclaringClass());
		} else if (binding instanceof IVariableBinding) {
		  resolvedBinding = bindingsResolver.resolveBinding(((IVariableBinding) binding).getDeclaringClass());
		} else {
			binding = null;
			resolvedBinding = bindingsResolver.resolveBinding(binding);
		}
		return resolvedBinding;
	}
	
	protected ISourceLocation resolveBinding(ASTNode node) {
		if (node instanceof CompilationUnit) {
			return resolveBinding((CompilationUnit) node);
		}
		return bindingsResolver.resolveBinding(node);
	}
	
	protected ISourceLocation getSourceLocation(ASTNode node) {
		try {
			int nodeLength = compilUnit.getExtendedLength(node);
			
			if (nodeLength > 0) {
				int start = compilUnit.getExtendedStartPosition(node);
				int end = start + nodeLength -1;
				
				if (end < start && ((node.getFlags() & 9) > 0)) {
					System.err.println("Recovered/Malformed node, guessing the length");
					nodeLength = node.toString().length();
					end = start + nodeLength - 1;
				}
		
				return values.sourceLocation(loc, 
						 start, nodeLength, 
						 compilUnit.getLineNumber(start), compilUnit.getLineNumber(end), 
						 // TODO: only adding 1 at the end seems to work, need to test.
						 compilUnit.getColumnNumber(start), compilUnit.getColumnNumber(end)+1);
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Most probably missing dependency");
		}
		return values.sourceLocation(loc, 0, 0, 0, 0, 0, 0);
	}
	
	protected IValue[] removeNulls(IValue... withNulls) {
		List<IValue> withOutNulls = new ArrayList<IValue>();
		for (IValue child : withNulls) {
			if (!(child == null)) {
        withOutNulls.add(child);
      }
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
	
	@SuppressWarnings({"rawtypes"})
	protected IValueList parseExtendedModifiers(List ext) {
		IValueList extendedModifierList = new IValueList(values);
	
		for (Iterator it = ext.iterator(); it.hasNext();) {
			ASTNode p = (ASTNode) it.next();
			IValue val = visitChild(p);
			if (p instanceof Annotation) {
        val = constructModifierNode("annotation", val);
      }
			extendedModifierList.add(val);
		}
		return extendedModifierList;
	}
	
	@SuppressWarnings("deprecation")
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
	
	protected IConstructor constructModifierNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	protected void setAnnotation(String annoName, IValue annoValue) {
		if(this.ownValue == null) {
      return ;
    }
		if (annoValue != null && ownValue.getType().declaresAnnotation(this.typeStore, annoName)) {
      ownValue = ((IConstructor) ownValue).asAnnotatable().setAnnotation(annoName, annoValue);
    }
	}
	
	protected void setAnnotation(String annoName, IValueList annoList) {
		IList annos = (IList) annoList.asList();
		if(this.ownValue == null) {
      return ;
    }
		if (annoList != null && this.ownValue.getType().declaresAnnotation(this.typeStore, annoName) && !annos.isEmpty()) {
      this.ownValue = ((IConstructor) this.ownValue).asAnnotatable().setAnnotation(annoName, annos);
    }
	}
	
	protected IValue constructDeclarationNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE, constructor, args);
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
	
	protected void insertCompilationUnitMessages() {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(TF.stringType(), TF.sourceLocationType());
		
		IValueList result = new IValueList(values);
		
		int i;

		IProblem[] problems = compilUnit.getProblems();
		for (i = 0; i < problems.length; i++) {
			int offset = problems[i].getSourceStart();
			int length = problems[i].getSourceEnd() - offset + 1;
			int sl = problems[i].getSourceLineNumber();
			ISourceLocation pos = values.sourceLocation(loc, offset, length, sl, sl, 0, 0);
			org.eclipse.imp.pdb.facts.type.Type constr;
			if (problems[i].isError()) {
				constr = typeStore.lookupConstructor(this.typeStore.lookupAbstractDataType("Message"), "error", args);
			} else {
				constr = typeStore.lookupConstructor(this.typeStore.lookupAbstractDataType("Message"), "warning", args);
			}
			result.add(values.constructor(constr, values.string(problems[i].getMessage()), pos));
		}
		setAnnotation("messages", result.asList());
	}
}

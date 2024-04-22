/*******************************************************************************
 * Copyright (c) 2009-2024 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Ashim Shahi
 *   * Jurgen Vinju
 *   * Jouke Stoel
 *   * Lina María Ochoa Venegas
 *   * Davy Landman 
*******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import java.net.URISyntaxException;
import java.util.Map;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

/**
 * This is a utility abstract class with reusable infrastructor for the bridge between
 * the JDT DOM and Rascal models (AST's or M3 databases).
 */
public abstract class JavaToRascalConverter extends ASTVisitor {
    protected static final IValueFactory values = ValueFactoryFactory.getValueFactory();
    protected static final TypeFactory TF = TypeFactory.getInstance();

    protected final LimitedTypeStore typeStore;

    protected IValue ownValue;
    protected IList  ownAnnotations;
    protected IList  ownModifiers;

    private static final String DATATYPE_RASCAL_AST_TYPE_NODE 			= "Type";
    private static final String DATATYPE_RASCAL_AST_MODIFIER_NODE 		= "Modifier";
    private static final String DATATYPE_RASCAL_AST_DECLARATION_NODE 	= "Declaration";
    private static final String DATATYPE_RASCAL_AST_EXPRESSION_NODE 	= "Expression";
    private static final String DATATYPE_RASCAL_AST_STATEMENT_NODE 		= "Statement";
    private static final String DATATYPE_RASCAL_MESSAGE                 = "Message";
    private static final String DATATYPE_RASCAL_MESSAGE_ERROR           = "error";

    protected final io.usethesource.vallang.type.Type DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE;
    protected final io.usethesource.vallang.type.Type DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE;
    protected final io.usethesource.vallang.type.Type DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE;
    protected final io.usethesource.vallang.type.Type DATATYPE_RASCAL_AST_TYPE_NODE_TYPE;
    protected final io.usethesource.vallang.type.Type DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE;
    protected final io.usethesource.vallang.type.Type DATATYPE_RASCAL_MESSAGE_DATA_TYPE;
    protected final io.usethesource.vallang.type.Type DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE;

    protected CompilationUnit compilUnit;
    protected ISourceLocation loc;

    protected final BindingsResolver bindingsResolver;
    protected final boolean collectBindings;

    protected IListWriter messages;
    protected final Map<String, ISourceLocation> locationCache;
    
    JavaToRascalConverter(final LimitedTypeStore typeStore, Map<String, ISourceLocation> cache, boolean collectBindings) {
        super(true);
        this.typeStore = typeStore;
        this.bindingsResolver = new BindingsResolver(typeStore, cache, collectBindings);
        this.collectBindings = collectBindings;
        DATATYPE_RASCAL_AST_TYPE_NODE_TYPE 		= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_TYPE_NODE);
        DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE = this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_MODIFIER_NODE);
        this.DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE 	= typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_DECLARATION_NODE);
        this.DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE 	= typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_EXPRESSION_NODE);
        this.DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE 	= typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_STATEMENT_NODE);
        DATATYPE_RASCAL_MESSAGE_DATA_TYPE          = typeStore.lookupAbstractDataType(DATATYPE_RASCAL_MESSAGE);
        DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE    = typeStore.lookupConstructor(DATATYPE_RASCAL_MESSAGE_DATA_TYPE, DATATYPE_RASCAL_MESSAGE_ERROR).iterator().next();
        this.locationCache = cache;

        messages = values.listWriter();
    }

    /**
     * The JDT's AST format stores list of modifiers separately from lists of annotations, even though
     * syntactically they may occur in arbitrary order before most declaration kinds in Java. To recreate
     * a properly ordered AST, this helper functions merges the lists again in order of appearance
     */
    protected IList mergeModifiersAndAnnotationsInOrderOfAppearance(IList modifiers, IList annotations) {
        if (modifiers == null && annotations == null) {
            return values.list();
        }
        else if (modifiers == null) {
            return annotations;
        }
        else if (annotations == null) {
            return modifiers;
        }

        var everything = modifiers.concat(annotations);

        return everything.stream()
            .map(v -> (IConstructor) v)
            .sorted((IConstructor a, IConstructor b) -> Integer.compare(location(a).getOffset(), location(b).getOffset()))
            .collect(values.listWriter());
    }

    private ISourceLocation location(IValue ast) {
        return (ISourceLocation) ((IConstructor) ast).asWithKeywordParameters().getParameter("src");
    }

    protected ISourceLocation resolveBinding(String packageComponent) {
        ISourceLocation packageBinding = new BindingsResolver(typeStore, locationCache, this.collectBindings) {
            public ISourceLocation resolveBinding(String packageC) {
                try {
                    if (collectBindings) {
                        if (locationCache.containsKey(packageC)) {
                            return locationCache.get(packageC);
                        }
                        return values.sourceLocation("java+package", null, packageC);
                    }
                    return values.sourceLocation("unknown", null, null);
                } catch (URISyntaxException e) {
                    throw new RuntimeException("Should not happen", e);
                }
            }
        }.resolveBinding(packageComponent);
        locationCache.put(packageComponent, packageBinding);
        return packageBinding;
    }

    protected final ISourceLocation resolveBinding(CompilationUnit node) {
        ISourceLocation compilationUnit = new BindingsResolver(typeStore, locationCache, true) {
            public ISourceLocation resolveBinding(CompilationUnit node) {
                return makeBinding("java+compilationUnit", null, loc.getPath());
            }
        }.resolveBinding(node);

        return compilationUnit;
    }

    protected ISourceLocation resolveBinding(IBinding binding) {
        ISourceLocation resolvedBinding = bindingsResolver.resolveBinding(binding);
        if (binding != null)
            locationCache.put(binding.getKey(), resolvedBinding);
        return resolvedBinding;
    }

    protected ISourceLocation resolveDeclaringClass(IBinding binding) {
        ISourceLocation resolvedBinding;
        if (binding instanceof ITypeBinding) {
            resolvedBinding = bindingsResolver.resolveBinding(((ITypeBinding) binding).getDeclaringClass());
        } 
        else if (binding instanceof IMethodBinding) {
            resolvedBinding = bindingsResolver.resolveBinding(((IMethodBinding) binding).getDeclaringClass());
        } 
        else if (binding instanceof IVariableBinding) {
            resolvedBinding = bindingsResolver.resolveBinding(((IVariableBinding) binding).getDeclaringClass());
        } 
        else {
            binding = null;
            resolvedBinding = bindingsResolver.resolveBinding(binding);
        }
        return resolvedBinding;
    }

    protected ISourceLocation resolveBinding(ASTNode node) {
        if (node instanceof CompilationUnit) {
            return resolveBinding((CompilationUnit) node);
        }
        return bindingsResolver.resolveBinding(node, true);
    }

    protected ISourceLocation getSourceLocation(ASTNode node) {
        try {
            int nodeLength = compilUnit.getExtendedLength(node);

            if (nodeLength > 0) {
                int start = compilUnit.getExtendedStartPosition(node);
                int end = start + nodeLength -1;

                if (end < start && ((node.getFlags() & 9) > 0)) {
                    insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE,
                        values.string("Recovered/Malformed node, guessing the length"),
                        values.sourceLocation(loc, 0, 0)));

                    nodeLength = node.toString().length();
                    end = start + nodeLength - 1;
                }

                return values.sourceLocation(loc, 
                    start, nodeLength, 
                    compilUnit.getLineNumber(start), compilUnit.getLineNumber(end), 
                    // TODO: only adding 1 at the end seems to work, need to test.
                    compilUnit.getColumnNumber(start), compilUnit.getColumnNumber(end) + 1);
            }
        } catch (IllegalArgumentException e) {
            insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE,
                values.string("Most probably missing dependency"),
                values.sourceLocation(loc, 0, 0)));
        }
        return values.sourceLocation(loc, 0, 0, 0, 0, 0, 0);
    }

    /* unused */
    /*
    protected IList parseExtendedModifiers(BodyDeclaration node) {
        if (node.getAST().apiLevel() == AST.JLS2) {
            return parseModifiers(node.getModifiers());
        } else {
            return parseExtendedModifiers(node.modifiers());
        }
    }
    */

    protected IValue visitChild(ASTNode node) {
        node.accept(this);
        return this.getValue();
    }

    public IValue getValue() {
        return this.ownValue;
    }

    protected IConstructor constructModifierNode(String constructor, IValue... children) {
        io.usethesource.vallang.type.Type args = TF.tupleType(children);
        io.usethesource.vallang.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE, constructor, args);
        return values.constructor(constr, children);
    }

    protected void setKeywordParameter(String label, IValue value) {
        if(this.ownValue == null) {
            return ;
        }
        if (value != null) {
            ownValue = ((IConstructor) ownValue).asWithKeywordParameters().setParameter(label, value);
        }
    }

    protected void setKeywordParameters(String label, IList values) {
        if (this.ownValue == null) {
            return ;
        }
        if (values != null && !values.isEmpty()) {
            this.ownValue = ((IConstructor) this.ownValue).asWithKeywordParameters().setParameter(label, values);
        }
    }

    protected IValue constructDeclarationNode(String constructor, IValue... children) {
        io.usethesource.vallang.type.Type args = TF.tupleType(children);
        io.usethesource.vallang.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE, constructor, args);
        return values.constructor(constr, children);
    }

    protected IValue constructExpressionNode(String constructor, IValue... children) {
        io.usethesource.vallang.type.Type args = TF.tupleType(children);
        io.usethesource.vallang.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE, constructor, args);
        return values.constructor(constr, children);
    }

    protected IValue constructStatementNode(String constructor, IValue... children) {
        io.usethesource.vallang.type.Type args = TF.tupleType(children);
        io.usethesource.vallang.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE, constructor, args);
        return values.constructor(constr, children);
    }

    protected IValue constructTypeNode(String constructor, IValue... children) {
        io.usethesource.vallang.type.Type args = TF.tupleType(children);
        io.usethesource.vallang.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_TYPE_NODE_TYPE, constructor, args);
        return values.constructor(constr, children);
    }

    protected Type getAdtType() {
        if (ownValue == null) {
            return TF.voidType();
        }
        return ownValue.getType().getAbstractDataType();
    }

    protected void insertCompilationUnitMessages(boolean insertErrors, IList otherMessages) {
        io.usethesource.vallang.type.Type args = TF.tupleType(TF.stringType(), TF.sourceLocationType());

        IListWriter result = values.listWriter();

        if (otherMessages != null) {
            for (IValue message : otherMessages) {
                result.append(message);
            }
        }

        if (insertErrors) {
            int i;

            IProblem[] problems = compilUnit.getProblems();
            for (i = 0; i < problems.length; i++) {
                int offset = problems[i].getSourceStart();
                int length = problems[i].getSourceEnd() - offset + 1;
                int sl = problems[i].getSourceLineNumber();
                ISourceLocation pos = values.sourceLocation(loc, offset, length, sl, sl, 0, 0);
                io.usethesource.vallang.type.Type constr;
                if (problems[i].isError()) {
                    constr = typeStore.lookupConstructor(this.typeStore.lookupAbstractDataType("Message"), "error", args);
                } else {
                    constr = typeStore.lookupConstructor(this.typeStore.lookupAbstractDataType("Message"), "warning", args);
                }
                result.append(values.constructor(constr, values.string(problems[i].getMessage()), pos));
            }
        }
        setKeywordParameter("messages", result.done());
    }

    public void insert(IListWriter listW, IValue message) {
        if (message.getType().isConstructor() && message.getType().getAbstractDataType().getName().equals("Message")) {
            listW.insert(message);
        }
    }

    public void convert(CompilationUnit root, ASTNode node, ISourceLocation loc) {
        this.compilUnit = root;
        this.loc = loc;
        node.accept(this);
    }
}

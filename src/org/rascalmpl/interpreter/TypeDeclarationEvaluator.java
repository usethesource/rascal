/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Anya Helene Bagge - UiB
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.rascalmpl.ast.Declaration;
import org.rascalmpl.ast.Declaration.Alias;
import org.rascalmpl.ast.Declaration.Annotation;
import org.rascalmpl.ast.Declaration.Data;
import org.rascalmpl.ast.Declaration.DataAbstract;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.ast.Toplevel.GivenVisibility;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.ast.TypeVar;
import org.rascalmpl.ast.UserType;
import org.rascalmpl.ast.Variant;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ConstructorFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.IllegalQualifiedDeclaration;
import org.rascalmpl.interpreter.staticErrors.RedeclaredField;
import org.rascalmpl.interpreter.staticErrors.RedeclaredType;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.utils.Names;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeDeclarationException;
import io.usethesource.vallang.exceptions.FactTypeRedeclaredException;
import io.usethesource.vallang.exceptions.RedeclaredFieldNameException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TypeDeclarationEvaluator {
	private Evaluator eval;

	public TypeDeclarationEvaluator(Evaluator eval) {
		this.eval = eval;
	}

	private Environment env;

	public void evaluateDeclarations(List<Toplevel> decls, Environment env, boolean ignoreErrors) {
		this.env = env;
		Set<UserType> abstractDataTypes = new HashSet<>();
		Set<Data> constructorDecls = new HashSet<>();
		Set<Alias> aliasDecls = new HashSet<>();
		Set<Annotation> annotationDecls = new HashSet<>();

		// this code is very much order dependent
		collectDeclarations(decls, abstractDataTypes, constructorDecls, aliasDecls, annotationDecls);
		
		declareAbstractDataTypes(abstractDataTypes);
		
		if (!ignoreErrors) {
		    declareAnnotations(annotationDecls);
		}
		
		declareAliases(aliasDecls, ignoreErrors);
		
		if (!ignoreErrors) {
		    declareConstructors(constructorDecls, ignoreErrors);
		}
	}
	
	private void declareAnnotations(Set<Annotation> annotationDecls) {
        for (Annotation anno : annotationDecls) {
            declareAnnotation(anno, env);
        }
    }

    private void declareAnnotation(Annotation anno, Environment env2) {
        anno.interpret(eval);
    }

    private void declareConstructors(Set<Data> constructorDecls, boolean ignoreErrors) {
		for (Data data : constructorDecls) {
		    try {
		        declareConstructor(data, env);
		    }
		    catch (UndeclaredType e) {
		        if (ignoreErrors) {
		            return;
		        }
		        else {
		            throw e;
		        }
		    }
		}
	}

	public static Type computeKeywordParametersType(List<KeywordFormal> kws, IEvaluator<Result<IValue>> eval) {
		Type[] kwTypes = new Type[kws.size()];
		String[] kwLabels = new String[kws.size()];
		
		int i = 0;
		for (KeywordFormal kw : kws) {
			kwLabels[i] = Names.name(kw.getName());
			kwTypes[i++] = kw.getType().typeOf(eval.getCurrentEnvt(), eval, true);
		}
		
		return TypeFactory.getInstance().tupleType(kwTypes, kwLabels);
	}
	
	private static List<String> TreeDeclaringModules = Arrays.asList("lang::rascalcore::check::AType", "ParseTree");
	
	public void declareConstructor(Data x, Environment env) {
		TypeFactory tf = TypeFactory.getInstance();

		// needs to be done just in case the declaration came
		// from a shell instead of from a module
		Type adt = declareAbstractDataType(x.getUser(), env);

		if (adt.getName().equals("Tree")) {
		    if (!TreeDeclaringModules.contains(env.getRoot().getName())) {
                throw new UnsupportedOperation("The Tree data-type from the ParseTree library module is \"final\"; it can not be extended. Please choose another name.", x.getUser());
            }
        }
        
		List<KeywordFormal> common = x.getCommonKeywordParameters().isPresent() 
				? x.getCommonKeywordParameters().getKeywordFormalList()
		        : Collections.<KeywordFormal>emptyList();
		
		if (common.size() > 0) {
			env.declareGenericKeywordParameters(adt, computeKeywordParametersType(common, eval), common);
		}
	
		for (Variant var : x.getVariants()) {
			String altName = Names.name(var.getName());

			if (var.isNAryConstructor()) {
				List<KeywordFormal> local = var.getKeywordArguments().hasKeywordFormalList() ? var.getKeywordArguments().getKeywordFormalList() : Collections.<KeywordFormal>emptyList();
				List<TypeArg> args = var.getArguments();
				int nAllArgs = args.size();
				
				Type[] fields = new Type[nAllArgs];
				String[] labels = new String[nAllArgs];

				for (int i = 0; i < args.size(); i++) {
					TypeArg arg = args.get(i);
					fields[i] = arg.getType().typeOf(env, eval, true);

					if (fields[i] == null) {
						throw new UndeclaredType(arg.hasName() ? Names.name(arg.getName()) : "?", arg);
					}

					if (arg.hasName()) {
						labels[i] = Names.name(arg.getName());
					} else {
						labels[i] = "arg" + java.lang.Integer.toString(i);
					}
				}
				
				try {
					ConstructorFunction cons = env.constructorFromTuple(var, eval, adt, altName, tf.tupleType(fields, labels), local);
					
					if (local.size() > 0) {
						Type kwType = computeKeywordParametersType(local, eval);
						for (String label : kwType.getFieldNames()) {
							env.getStore().declareKeywordParameter(cons.getConstructorType(), label, kwType.getFieldType(label));
						}
					}
				} catch (io.usethesource.vallang.exceptions.RedeclaredConstructorException e) {
					throw new RedeclaredType(altName, var);
				} catch (RedeclaredFieldNameException e) {
					throw new RedeclaredField(e.getMessage(), var);
				}
			} 
		}
	}

	public void declareAbstractADT(DataAbstract x, Environment env) {
		Type adt = declareAbstractDataType(x.getUser(), env);
		
		List<KeywordFormal> common = x.getCommonKeywordParameters().isPresent() 
				? x.getCommonKeywordParameters().getKeywordFormalList()
		        : Collections.<KeywordFormal>emptyList();
		
		if (common.size() > 0) {
			env.declareGenericKeywordParameters(adt, computeKeywordParametersType(common, eval), common);
		}
	}
	
	private void declareAliases(Set<Alias> aliasDecls, boolean ignoreErrors) {
		List<Alias> todo = new LinkedList<Alias>();
		todo.addAll(aliasDecls);
		
		int countdown = todo.size();
		while (!todo.isEmpty()) {
			Alias trial = todo.remove(0);
			--countdown;
			try {
				declareAlias(trial, env);
				countdown = todo.size();
			}
			catch (UndeclaredType e) {
				if (countdown == 0) {	
					// Cycle
				    if (!ignoreErrors) {
				        throw e;
				    }
				    else {
				        return;
				    }
				    
				}
				// Put at end of queue
				todo.add(trial);
			}
		}
	}
	
	public void declareAlias(Alias x, Environment env) {
		try {
			Type base = x.getBase().typeOf(env, eval, false);

			assert base != null;
			
			QualifiedName name = x.getUser().getName();
			if (Names.isQualified(name)) {
				throw new IllegalQualifiedDeclaration(name);
			}

			env.aliasType(Names.typeName(name), base,
					computeTypeParameters(x.getUser(), env));
		} 
		catch (FactTypeRedeclaredException e) {
			throw new RedeclaredType(e.getName(), x);
		}
		catch (FactTypeDeclarationException e) {
			throw new ImplementationError("Unknown FactTypeDeclarationException: " + e.getMessage());
		}
	}

	private void declareAbstractDataTypes(Set<UserType> abstractDataTypes) {
		List<UserType> todo = new LinkedList<UserType>();
		todo.addAll(abstractDataTypes);
		
		int countdown = todo.size();
		while (!todo.isEmpty()) {
			UserType trial = todo.remove(0);
			--countdown;
			try {
				declareAbstractDataType(trial, env);
				countdown = todo.size();
			}
			catch (UndeclaredType e) {
				if (countdown == 0) {	
					// Cycle
				    throw e;
				}
				// Put at end of queue
				todo.add(trial);
			}
		}
	}

	public Type declareAbstractDataType(UserType decl, Environment env) {
		QualifiedName name = decl.getName();
		if (Names.isQualified(name)) {
			throw new IllegalQualifiedDeclaration(name);
		}
		
		return env.abstractDataType(Names.typeName(name), computeTypeParameters(decl, env));
	}

	private Type[] computeTypeParameters(UserType decl, Environment env) {
		TypeFactory tf = TypeFactory.getInstance();

		Type[] params;
		if (decl.isParametric()) {
			java.util.List<org.rascalmpl.ast.Type> formals = decl
					.getParameters();
			params = new Type[formals.size()];
			int i = 0;
			for (org.rascalmpl.ast.Type formal : formals) {
				if (!formal.isVariable()) {
					throw new SyntaxError(
							"Declaration of parameterized type with type instance "
									+ formal + " is not allowed", formal.getLocation());
				}
				TypeVar var = formal.getTypeVar();	
				Type bound = var.hasBound() ? var.getBound().typeOf(env, eval, false) : tf
						.valueType();
				params[i++] = tf
						.parameterType(Names.name(var.getName()), bound);
			}
		} else {
			params = new Type[0];
		}
		return params;
	}

	private void collectDeclarations(List<Toplevel> decls,
			Set<UserType> abstractDataTypes, Set<Data> constructorDecls,
			Set<Alias> aliasDecls, Set<Annotation> annotations) {
		DeclarationCollector collector = new DeclarationCollector(
				abstractDataTypes, constructorDecls, aliasDecls, annotations);

		for (Toplevel t : decls) {
			t.accept(collector);
		}
	}

	private static class DeclarationCollector extends NullASTVisitor<Declaration> {
		private final Set<UserType> abstractDataTypes;
		private final Set<Data> constructorDecls;
		private final Set<Alias> aliasDecls;
        private final Set<Annotation> annotations;

		public DeclarationCollector(Set<UserType> abstractDataTypes, Set<Data> constructorDecls, Set<Alias> aliasDecls, Set<Annotation> annotations) {
			this.abstractDataTypes = abstractDataTypes;
			this.constructorDecls = constructorDecls;
			this.aliasDecls = aliasDecls;
			this.annotations = annotations;
		}

		@Override
		public Declaration visitToplevelGivenVisibility(GivenVisibility x) {
			return x.getDeclaration().accept(this);
		}

		@Override
		public Declaration visitDeclarationAlias(Alias x) {
			aliasDecls.add(x);
			return x;
		}

		@Override
		public Declaration visitDeclarationData(Data x) {
			abstractDataTypes.add(x.getUser());
			constructorDecls.add(x);
			return x;
		}

		@Override
		public Declaration visitDeclarationDataAbstract(DataAbstract x) {
			abstractDataTypes.add(x.getUser());
			return x;
		}
		
		@Override
		public Declaration visitDeclarationAnnotation(Annotation x) {
		    annotations.add(x);
		    return x;
		}
	}
}

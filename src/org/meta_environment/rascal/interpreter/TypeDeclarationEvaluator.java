package org.meta_environment.rascal.interpreter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.exceptions.FactTypeDeclarationException;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeRedeclaredException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Declaration;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Toplevel;
import org.meta_environment.rascal.ast.TypeArg;
import org.meta_environment.rascal.ast.TypeVar;
import org.meta_environment.rascal.ast.UserType;
import org.meta_environment.rascal.ast.Variant;
import org.meta_environment.rascal.ast.Declaration.Alias;
import org.meta_environment.rascal.ast.Declaration.Data;
import org.meta_environment.rascal.ast.Toplevel.DefaultVisibility;
import org.meta_environment.rascal.ast.Toplevel.GivenVisibility;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.ConstructorFunction;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredTypeError;
import org.meta_environment.rascal.interpreter.utils.Names;


public class TypeDeclarationEvaluator {
	private Evaluator eval;

	public TypeDeclarationEvaluator(Evaluator eval) {
		this.eval = eval;
	}

	private Environment env;

	public void evaluateDeclarations(List<Toplevel> decls, Environment env) {
		this.env = env;
		Set<UserType> abstractDataTypes = new HashSet<UserType>();
		Set<Data> constructorDecls = new HashSet<Data>();
		Set<Alias> aliasDecls = new HashSet<Alias>();

		// this code is very much order dependent
		collectDeclarations(decls, abstractDataTypes, constructorDecls,
				aliasDecls);
		declareAbstractDataTypes(abstractDataTypes);
		declareAliases(aliasDecls);
		declareConstructors(constructorDecls);
	}

	private void declareConstructors(Set<Data> constructorDecls) {
		for (Data data : constructorDecls) {
			declareConstructor(data, env);
		}
	}

	public void declareConstructor(Data x, Environment env) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		TypeFactory tf = TypeFactory.getInstance();

		// needs to be done just in case the declaration came
		// from a shell instead of from a module
		Type adt = declareAbstractDataType(x.getUser(), env);

		for (Variant var : x.getVariants()) {
			String altName = Names.name(var.getName());

			if (var.isNAryConstructor()) {
				java.util.List<TypeArg> args = var.getArguments();
				Type[] fields = new Type[args.size()];
				String[] labels = new String[args.size()];

				for (int i = 0; i < args.size(); i++) {
					TypeArg arg = args.get(i);
					fields[i] = te.eval(arg.getType(), env);

					if (fields[i] == null) {
						throw new UndeclaredTypeError(arg.getType()
								.toString(), arg);
					}

					if (arg.hasName()) {
						labels[i] = arg.getName().toString();
					} else {
						labels[i] = "arg" + java.lang.Integer.toString(i);
					}
				}

				Type children = tf.tupleType(fields, labels);
				try {
					ConstructorFunction cons = env.constructorFromTuple(var, eval, adt, altName, children);
					cons.setPublic(true); // TODO: implement declared visibility
				} catch (org.eclipse.imp.pdb.facts.exceptions.RedeclaredConstructorException e) {
					throw new RedeclaredTypeError(altName, var);
				}
			} 
		}
	}

	private void declareAliases(Set<Alias> aliasDecls) {
		List<Alias> todo = new LinkedList<Alias>();
		todo.addAll(aliasDecls);
		
		int len = todo.size();
		int i = 0;
		while (!todo.isEmpty()) {
			Alias trial = todo.remove(0);
			try {
				declareAlias(trial, env);
				i--;
			}
			catch (UndeclaredTypeError e) {
				if (i >= len) {
					// Cycle
					throw e;
				}
				// Put at end of queue
				todo.add(trial);
			}
			i++;
		}
	}
	
	public void declareAlias(Alias x, Environment env) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		try {
			Type base = te.eval(x.getBase(), env);

			if (base == null) {
				throw new UndeclaredTypeError(x.getBase().toString(), x
						.getBase());
			}

			env.aliasType(Names.name(x.getUser().getName()), base,
					computeTypeParameters(x.getUser(), env));
		} 
		catch (FactTypeRedeclaredException e) {
			throw new RedeclaredTypeError(e.getName(), x);
		}
		catch (FactTypeDeclarationException e) {
			throw new ImplementationError("Unknown FactTypeDeclarationException: " + e.getMessage());
		}
	}

	private void declareAbstractDataTypes(Set<UserType> abstractDataTypes) {
		for (UserType decl : abstractDataTypes) {
			declareAbstractDataType(decl, env);
		}
	}

	public Type declareAbstractDataType(UserType decl, Environment env) {
		String name = Names.name(decl.getName());
		return env.abstractDataType(name, computeTypeParameters(decl, env));
	}

	private Type[] computeTypeParameters(UserType decl, Environment env) {
		TypeFactory tf = TypeFactory.getInstance();
		TypeEvaluator te = TypeEvaluator.getInstance();

		Type[] params;
		if (decl.isParametric()) {
			java.util.List<org.meta_environment.rascal.ast.Type> formals = decl
					.getParameters();
			params = new Type[formals.size()];
			int i = 0;
			for (org.meta_environment.rascal.ast.Type formal : formals) {
				if (!formal.isVariable()) {
					throw new SyntaxError(
							"Declaration of parameterized type with type instance "
									+ formal + " is not allowed", formal.getLocation());
				}
				TypeVar var = formal.getTypeVar();
				Type bound = var.hasBound() ? te.eval(var.getBound(), env) : tf
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
			Set<Alias> aliasDecls) {
		DeclarationCollector collector = new DeclarationCollector(
				abstractDataTypes, constructorDecls, aliasDecls);

		for (Toplevel t : decls) {
			t.accept(collector);
		}
	}

	private class DeclarationCollector extends NullASTVisitor<Declaration> {
		private Set<UserType> abstractDataTypes;
		private Set<Data> constructorDecls;
		private Set<Alias> aliasDecls;

		public DeclarationCollector(Set<UserType> abstractDataTypes,
				Set<Data> constructorDecls, Set<Alias> aliasDecls) {
			this.abstractDataTypes = abstractDataTypes;
			this.constructorDecls = constructorDecls;
			this.aliasDecls = aliasDecls;
		}

		@Override
		public Declaration visitToplevelDefaultVisibility(DefaultVisibility x) {
			return x.getDeclaration().accept(this);
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
	}

}

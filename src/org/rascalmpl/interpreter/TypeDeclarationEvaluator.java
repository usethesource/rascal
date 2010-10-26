package org.rascalmpl.interpreter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeDeclarationException;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeRedeclaredException;
import org.eclipse.imp.pdb.facts.exceptions.RedeclaredFieldNameException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Declaration;
import org.rascalmpl.ast.Import;
import org.rascalmpl.ast.Nonterminal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.ast.TypeVar;
import org.rascalmpl.ast.UserType;
import org.rascalmpl.ast.Variant;
import org.rascalmpl.ast.Declaration.Alias;
import org.rascalmpl.ast.Declaration.Data;
import org.rascalmpl.ast.Declaration.DataAbstract;
import org.rascalmpl.ast.Toplevel.GivenVisibility;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ConstructorFunction;
import org.rascalmpl.interpreter.staticErrors.IllegalQualifiedDeclaration;
import org.rascalmpl.interpreter.staticErrors.RedeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.RedeclaredTypeError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;


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
	
	public void evaluateSyntaxDefinitions(List<Import> imports,
			Environment env) {
		for (Import i : imports) {
			if (i.isSyntax()) {
				// TODO: declare all the embedded regular symbols as well
				declareSyntaxType(i.getSyntax().getDefined(), env);
			}
		}
	}

	public void declareSyntaxType(Sym type, Environment env) {
		IValueFactory vf = eval.getValueFactory();
		
		if (type.isNonterminal()) {
			String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
		
			env.concreteSyntaxType(nt, (IConstructor) Factory.Symbol_Sort.make(vf, vf.string(nt)));
		}
		// do nothing
	}

	private void declareConstructors(Set<Data> constructorDecls) {
		for (Data data : constructorDecls) {
			declareConstructor(data, env);
		}
	}

	public void declareConstructor(Data x, Environment env) {
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
					fields[i] = new TypeEvaluator(env, null).eval(arg.getType());

					if (fields[i] == null) {
						throw new UndeclaredTypeError(arg.getType()
								.toString(), arg);
					}

					if (arg.hasName()) {
						labels[i] = Names.name(arg.getName());
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
				} catch (RedeclaredFieldNameException e) {
					throw new RedeclaredFieldError(e.getMessage(), var);
				}
			} 
		}
	}

	public void declareAbstractADT(DataAbstract x, Environment env) {
		TypeFactory.getInstance();
		declareAbstractDataType(x.getUser(), env);
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
		try {
			Type base = new TypeEvaluator(env, eval.getHeap()).eval(x.getBase());

			if (base == null) {
				throw new UndeclaredTypeError(x.getBase().toString(), x
						.getBase());
			}
			
			QualifiedName name = x.getUser().getName();
			if (Names.isQualified(name)) {
				throw new IllegalQualifiedDeclaration(name);
			}

			env.aliasType(Names.typeName(name), base,
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
				Type bound = var.hasBound() ? new TypeEvaluator(env, null).eval(var.getBound()) : tf
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
	}

	

}

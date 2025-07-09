/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.utils.InfoCmp.Capability;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.OptionalTerminator;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.env.Pair;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

public abstract class ShellCommand extends org.rascalmpl.ast.ShellCommand {

	static public class Edit extends org.rascalmpl.ast.ShellCommand.Edit {
		public Edit(ISourceLocation __param1, IConstructor tree, QualifiedName __param2, OptionalTerminator term) {
			super(__param1, tree, __param2, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			IRascalMonitor monitor = __eval.getMonitor();

			if (monitor instanceof IDEServices) {
				IDEServices services = (IDEServices) monitor;
				String name = Names.fullName(getName());
				
				ISourceLocation uri = __eval.getRascalResolver().resolveModule(name);
	       	 	if (uri == null) {
					__eval.getErrorPrinter().println("module " + name + " can not be found in the search path.");
	        	}
				else {
					services.edit(uri);
				}
			}
			else {
				__eval.getErrorPrinter().println("The current Rascal execution environment does not know how to start an editor.");
			}

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
	}
	
	static public class Clear extends org.rascalmpl.ast.ShellCommand.Clear {

		public Clear(ISourceLocation __param1, IConstructor tree, OptionalTerminator term) {
			super(__param1, tree, term);
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			IRascalMonitor monitor = __eval.getMonitor();

			// clear the screen, if we have that capability
			if (monitor instanceof IDEServices) {
				var services = (IDEServices) monitor;
				var term = services.activeTerminal();
				if (term != null) {
					term.puts(Capability.clear_screen);
				}
				else {
					__eval.getErrorPrinter().println(":clear does not work for this context.");
				}
			}
			else {
				__eval.getErrorPrinter().println(":clear is not implemented in this context.");
			}
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
		
	}

	static public class Help extends org.rascalmpl.ast.ShellCommand.Help {

		public Help(ISourceLocation __param1, IConstructor tree, OptionalTerminator term) {
			super(__param1, tree, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.printHelpMessage(__eval.getOutPrinter());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}
	}

	static public class History extends org.rascalmpl.ast.ShellCommand.History {

		public History(ISourceLocation __param1, IConstructor tree, OptionalTerminator term) {
			super(__param1, tree, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			eval.getErrorPrinter().println(":history command is not implemented here yet.");
			return nothing();
		}
	}

	static public class ListDeclarations extends org.rascalmpl.ast.ShellCommand.ListDeclarations {

		public ListDeclarations(ISourceLocation __param1, IConstructor tree, OptionalTerminator term) {
			super(__param1, tree, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			ModuleEnvironment env = __eval.getCurrentModuleEnvironment();

			var pr = __eval.getOutPrinter();

			if (!env.getVariables().isEmpty()) {
				pr.println("Variables:");
				var vars = env.getVariables();
				for (Entry<String, Result<IValue>> var : vars.entrySet()) {
					pr.println(var.getValue().getStaticType() + " " + var.getKey());
				}
			}

			if (!env.getFunctions().isEmpty()) {
				var functions = env.getFunctions();
				pr.println("Functions:");
				for (Pair<String, List<AbstractFunction>> func : functions) {
					pr.println(func.getFirst() + ":");
					for (AbstractFunction alt : func.getSecond()) {
						pr.println("   - " + alt.getHeader().replaceAll("\n", " "));
					}
				}
			}

			if (!env.getAbstractDatatypes().isEmpty()) {
				var data = env.getAbstractDatatypes();
				pr.println("Data:");
				for (io.usethesource.vallang.type.Type t : data) {
					pr.println(t + ":");
					env.getStore().getConstructors().stream().filter(c -> c.getAbstractDataType() == t).forEach(cons -> {
						pr.println("   - " + cons);
					});
				}
			}

			if (!env.getProductions().isEmpty()) {
				var syntax = env.getProductions();
				
				pr.println("Syntax definitions:");
				for (IValue def : syntax) {
					pr.println(TreeAdapter.yield((IConstructor) def));
				}
			}
			
			
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	static public class Quit extends org.rascalmpl.ast.ShellCommand.Quit {

		public Quit(ISourceLocation __param1, IConstructor tree, OptionalTerminator term) {
			super(__param1, tree, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			throw new QuitException();
		}

	}

	static public class SetOptionTrue extends org.rascalmpl.ast.ShellCommand.SetOptionTrue {

			public SetOptionTrue(ISourceLocation src, IConstructor node, OptionalTerminator terminator) {
				super(src, node, terminator);
			}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			String name = "rascal." + ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) this.getName()).fullName();
			setOption(__eval, name, "true");
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();			
		}
	}

	static public class UnsetOptionTrue extends org.rascalmpl.ast.ShellCommand.UnsetOption {

		public UnsetOptionTrue(ISourceLocation src, IConstructor node, OptionalTerminator terminator) {
			super(src, node, terminator);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			String name = "rascal." + ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) this.getName()).fullName();
			setOption(__eval, name, "false");
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();			
		}
	}

	private static void setOption(IEvaluator<Result<IValue>> __eval, String name, String value) {
		switch (name) {
			case Configuration.GENERATOR_PROFILING_PROPERTY:
				  __eval.getConfiguration().setGeneratorProfiling(Boolean.parseBoolean(value));
				  __eval.getParserGenerator().setGeneratorProfiling(Boolean.parseBoolean(value));
				  break;
			case Configuration.PROFILING_PROPERTY: 
			  __eval.getConfiguration().setProfiling(Boolean.parseBoolean(value));
			  break;
			case Configuration.ERRORS_PROPERTY:
			  __eval.getConfiguration().setErrors(Boolean.parseBoolean(value));
			  break;
			case Configuration.TRACING_PROPERTY:
			  __eval.getConfiguration().setTracing(Boolean.parseBoolean(value));
			  break;
			case Configuration.DEBUGGING_PROPERTY:
			__eval.getConfiguration().setDebugging(Boolean.parseBoolean(value));
		}

		__eval.updateProperties();
	}

	static public class SetOption extends
			org.rascalmpl.ast.ShellCommand.SetOption {

		public SetOption(ISourceLocation __param1, IConstructor tree, QualifiedName __param2,
				Expression __param3, OptionalTerminator term) {
			super(__param1, tree, __param2, __param3, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			String name = "rascal." + ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) this.getName()).fullName();
			String value = this.getExpression().interpret(__eval).getValue().toString();

			setOption(__eval, name, value);
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	static public class Test extends org.rascalmpl.ast.ShellCommand.Test {

		public Test(ISourceLocation __param1, IConstructor tree, QualifiedName mut, OptionalTerminator term) {
			super(__param1, tree, mut, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			Optional<String> name = getOptName() != null ? Optional.of(Names.fullName(getOptName())) : Optional.empty();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(TF.boolType(), VF.bool(__eval.runTests(__eval.getMonitor(), name)), __eval);
		}
	}

	static public class Undeclare extends org.rascalmpl.ast.ShellCommand.Undeclare {

		public Undeclare(ISourceLocation src, IConstructor node, @Nullable QualifiedName optName, OptionalTerminator terminator) {
			super(src, node, optName, terminator);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			if (getOptName() == null) {
				// then we clear everything!
				eval.getCurrentModuleEnvironment().reset();
			}
			else {
				var n = getOptName();
				if (Names.isQualified(n)) {
					throw new IllegalArgumentException("name " + Names.fullName(n) + " should not be qualified for :undeclare");
				}

				var simpleName = Names.name(Names.lastName(n));

				var env = eval.getCurrentModuleEnvironment();
				
				env.unsetSimpleVariable(simpleName);
				env.unsetAllFunctions(simpleName);
				// TODO: remove ADT from store when vallang TypeStore gets that capability.
				env.unsetConcreteSyntaxType(simpleName);
			}

			return nothing();
		}

	}
	static public class Unimport extends
			org.rascalmpl.ast.ShellCommand.Unimport {

		public Unimport(ISourceLocation __param1, IConstructor tree, QualifiedName __param2, OptionalTerminator term) {
			super(__param1, tree, __param2, term);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			((ModuleEnvironment) __eval.getCurrentEnvt().getRoot()).unImport(Names.fullName(this.getName()));
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	static public class Unextend extends org.rascalmpl.ast.ShellCommand.Unextend {

		public Unextend(ISourceLocation src, IConstructor node, QualifiedName name, OptionalTerminator terminator) {
			super(src, node, name, terminator);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			((ModuleEnvironment) __eval.getCurrentEnvt().getRoot()).unExtend(Names.fullName(this.getName()));
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	public ShellCommand(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}

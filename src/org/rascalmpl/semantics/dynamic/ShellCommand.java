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

import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

public abstract class ShellCommand extends org.rascalmpl.ast.ShellCommand {

	static public class Edit extends org.rascalmpl.ast.ShellCommand.Edit {
		public Edit(ISourceLocation __param1, IConstructor tree, QualifiedName __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			IRascalMonitor monitor = __eval.getMonitor();

			if (monitor instanceof IDEServices) {
				IDEServices services = (IDEServices) monitor;
				String name = Names.fullName(getName());
				
				ISourceLocation uri = __eval.getRascalResolver().resolveModule(name);
	       	 	if (uri == null) {
					__eval.getOutPrinter().println("module " + name + " can not be found in the search path.");
	        	}
				
				services.edit(uri);
			}
			else {
				__eval.getOutPrinter().println("The current Rascal execution environment does not know how to start an editor.");
			}

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
	}
	
	static public class Clear extends org.rascalmpl.ast.ShellCommand.Clear {

		public Clear(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return null;
		}
		
	}

	static public class Help extends org.rascalmpl.ast.ShellCommand.Help {

		public Help(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.printHelpMessage(__eval.getOutPrinter());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class History extends org.rascalmpl.ast.ShellCommand.History {

		public History(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

	}

	static public class ListDeclarations extends
			org.rascalmpl.ast.ShellCommand.ListDeclarations {

		public ListDeclarations(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	static public class Quit extends org.rascalmpl.ast.ShellCommand.Quit {

		public Quit(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			throw new QuitException();
		}

	}

	static public class SetOption extends
			org.rascalmpl.ast.ShellCommand.SetOption {

		public SetOption(ISourceLocation __param1, IConstructor tree, QualifiedName __param2,
				Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			String name = "rascal." + ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) this.getName()).fullName();
			String value = this.getExpression().interpret(__eval).getValue()
					.toString();

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
			}

			__eval.updateProperties();

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	static public class Test extends org.rascalmpl.ast.ShellCommand.Test {

		public Test(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(TF.boolType(), VF.bool(__eval.runTests(__eval.getMonitor())), __eval);
		}
	}

	static public class Unimport extends
			org.rascalmpl.ast.ShellCommand.Unimport {

		public Unimport(ISourceLocation __param1, IConstructor tree, QualifiedName __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			((ModuleEnvironment) __eval.getCurrentEnvt().getRoot()).unImport(Names.fullName(this.getName()));
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	public ShellCommand(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}

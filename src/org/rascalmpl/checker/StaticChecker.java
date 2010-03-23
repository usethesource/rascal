package org.rascalmpl.checker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.Command;
import org.rascalmpl.interpreter.CommandEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.ValueFactoryFactory;

public class StaticChecker {
	private final CommandEvaluator eval;
	private final ASTBuilder astBuilder;
	
	private ArrayList<String> checkerPipeline;
	private ArrayList<Boolean> pipelineElementEnabled;
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	public static final String TYPECHECKER = "typecheckTree";
	
	private static final class InstanceKeeper {
		public static final StaticChecker sInstance = new StaticChecker();
	}

	private StaticChecker() {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***static-checker***"));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);

		this.eval = new CommandEvaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		this.astBuilder = new ASTBuilder(new ASTFactory());

		checkerPipeline = new ArrayList<String>();
		pipelineElementEnabled = new ArrayList<Boolean>();
		
		// Add the pass for type checking
		checkerPipeline.add(TYPECHECKER); pipelineElementEnabled.add(Boolean.FALSE);
	}
	
	public static StaticChecker getInstance() {
		return InstanceKeeper.sInstance;
	}

	private IValue eval(String cmd) {
		try {
			Command cmdAst = astBuilder.buildCommand(eval.parseCommand(cmd));
			return eval.eval(cmdAst).getValue();
		} catch (IOException e) {
			throw new ImplementationError("static checker failed to execute command: " + cmd, e);
		} catch (SyntaxError se) {
			throw new ImplementationError("syntax error in static checker modules", se);
		}
		
	}
	
	public IConstructor resolveImports(IConstructor moduleParseTree) {
		ISet imports = (ISet) eval.call("importedModules", moduleParseTree);
		
		System.err.println("imports: " + imports);
		
		IMapWriter mw = VF.mapWriter(TypeFactory.getInstance().stringType(), TypeFactory.getInstance().sourceLocationType());
		
		for (IValue i : imports) {
			URI uri = eval.getModuleLoader().findModule(((IString) i).getValue());
			if (uri != null) {
				mw.put(i, VF.sourceLocation(uri));
			}
		}
		
		System.err.println("locations: " + mw.done());
		
		return (IConstructor) eval.call("linkImportedModules", moduleParseTree, mw.done());
	}
	
	public IConstructor checkModule(IConstructor moduleParseTree) {
		IConstructor res = moduleParseTree;
		res = resolveImports(res);
		for (int n = 0; n < checkerPipeline.size(); ++n) {
			if (pipelineElementEnabled.get(n).booleanValue()) 
				res = (IConstructor) eval.call(checkerPipeline.get(n), res);
		}
		return res;
	}

	public void disablePipelinePass(String passName) {
		for (int n = 0; n < checkerPipeline.size(); ++n) {
			if (checkerPipeline.get(n).equalsIgnoreCase(passName)) { 
				pipelineElementEnabled.set(n, Boolean.FALSE);
				break;
			}
		}
	}

	public void enablePipelinePass(String passName) {
		for (int n = 0; n < checkerPipeline.size(); ++n) {
			if (checkerPipeline.get(n).equalsIgnoreCase(passName)) { 
				pipelineElementEnabled.set(n, Boolean.TRUE);
				break;
			}
		}
	}

	public boolean isPassEnabled(String passName) {
		for (int n = 0; n < checkerPipeline.size(); ++n) {
			if (checkerPipeline.get(n).equalsIgnoreCase(passName)) {
				return pipelineElementEnabled.get(n).booleanValue();
			}
		}
		return false;
	}

	public void reload() {
		eval("import rascal::checker::Check;");
		eval("import rascal::checker::Import;");
	}
}

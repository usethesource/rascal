package org.rascalmpl.tutor;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.ambiguousMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.interruptedExceptionMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.resultMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.util.Eval;
import org.rascalmpl.library.util.Eval.EvalTimer;
import org.rascalmpl.parser.gtd.exception.ParseError;

public class HTMLGenerator {
	private final TypeReifier tr;
	private final TypeFactory tf = TypeFactory.getInstance();
	
	private final TypeStore ts = new TypeStore();
	private final Type ShellException = tf.abstractDataType(ts, "ShellException");
	private final Type ShellError = tf.constructor(ts, ShellException, "error", tf.stringType(), "message");
	private final Type ShellParseError = tf.constructor(ts, ShellException, "parseError", tf.stringType(), "message", tf.sourceLocationType(), "location");
	private final IValueFactory values;
	private final Eval eval;
	private Evaluator evaluator;
	private StringWriter errString;
	private StringWriter outString;
	private PrintWriter err;
	private PrintWriter out;
	private Environment old = null;
	private ModuleEnvironment env = null;

	public HTMLGenerator(IValueFactory vf) {
		this.values = vf;
		this.tr = new TypeReifier(values);
		this.eval = new org.rascalmpl.library.util.Eval(values); 
	}
	
	private Evaluator createEvaluator(IEvaluatorContext ctx) {
		if (this.evaluator == null) {
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = new ModuleEnvironment("___SCREEN___", heap);
			errString = new StringWriter();
			outString = new StringWriter();
			err = new PrintWriter(errString);
			out = new PrintWriter(outString);
			this.evaluator = new Evaluator(values, err, out, root, heap, ctx.getEvaluator().getClassLoaders(), ctx.getEvaluator().getRascalResolver());
		}
		
		return this.evaluator;
	}
	
	private ModuleEnvironment getUniqueModuleEnvironment(Evaluator eval) {
		ModuleEnvironment mod = new ModuleEnvironment("___SCREEN_INSTANCE___", eval.getHeap());
		eval.getHeap().addModule(mod);
		return mod;	
	}
	
	public void startShell(IEvaluatorContext ctx) {
		if (old != null || env != null) {
			throw new ImplementationError("Can not nest shell calls! Call endShell before another startShell please.");
		}
		evaluator = createEvaluator(ctx);
		old = evaluator.getCurrentEnvt();
		env = getUniqueModuleEnvironment(evaluator);
		evaluator.setCurrentEnvt(env);
	}
	
	public void endShell(IEvaluatorContext ctx) {
		evaluator.getHeap().removeModule(env);
		env = null;
		evaluator.setCurrentEnvt(old);
		old = null;
	}
	
	public IString shell(IString command, IInteger duration, IEvaluatorContext ctx) {
		if (evaluator == null || old == null || env == null) {
			throw new ImplementationError("First call startShell, then shell, then end with endShell");
		}
		
		IValue valueType = tr.typeToValue(TypeFactory.getInstance().valueType(), ctx).getValue();
		StringBuilder content = new StringBuilder();
		
		try {
			outString.getBuffer().setLength(0);
			errString.getBuffer().setLength(0);
			Result<IValue> result = eval(valueType, values.list(command), duration, evaluator);
			out.flush();
			err.flush();
			String output = outString.toString();
			if (output.length() > 0) {
				content.append(output);
			}
			/*
			output = errString.toString();
			if (output.length() > 0) {
				for (String line : output.split("\n")) {
					content.append("debug:");
					content.append(line);
				}
			}
			*/
			content.append(resultMessage(result));
		}
		catch (ParseError pe) {
			content.append(parseErrorMessage(command.getValue(), "stdin", pe));
			content.append('\n');
			ISourceLocation sourceLocation = values.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine(), pe.getEndLine(), pe.getBeginColumn(), pe.getEndColumn());
			throw new Throw(ShellParseError.make(values, values.string(content.toString()), sourceLocation), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch (QuitException q){
			//
		}
		catch(InterruptException i) {
			content.append(interruptedExceptionMessage(i));
			content.append('\n');
		}
		catch (Ambiguous e) {
			content.append(ambiguousMessage(e));
			content.append('\n');
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(StaticError e){
			content.append(staticErrorMessage(e));
			content.append('\n');
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(Throw e){
			content.append(throwMessage(e));
			content.append('\n');
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(Throwable e){
			content.append(throwableMessage(e, eval != null ? ctx.getStackTrace() : ""));
			content.append('\n');
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		return values.string(content.toString());
	}
	
	
	private Result<IValue> eval (IValue expected, IList commands, IInteger duration, IEvaluatorContext ctx) {
		IEvaluator<Result<IValue>> evaluator = ctx.getEvaluator();
		EvalTimer timer = new EvalTimer(evaluator, duration.intValue());

		Result<IValue> result = null;
		
		timer.start();

		if(!timer.hasExpired() && commands.length() > 0){
			for(IValue command : commands){
				result = evaluator.eval(null, ((IString) command).getValue(), URI.create("stdin:///"));
			}
			timer.cancel();
			if (timer.hasExpired()) {
				throw RuntimeExceptionFactory.timeout(null, null);
			}

			if (expected != null) {
				Type typ = tr.valueToType((IConstructor) expected);
				if (!result.getType().isSubtypeOf(typ)) {
					throw new UnexpectedTypeError(typ, result.getType(), ctx.getCurrentAST());
				}
			}
			return result;
		}

		throw new IllegalArgumentException();
	}
}

package org.rascalmpl.library.experiments.RascalTutor;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.ambiguousMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.interruptedExceptionMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.resultMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.library.util.Eval;
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
	
	public IString shell(IString command, IInteger duration, IEvaluatorContext ctx) {
		evaluator = createEvaluator(ctx);
		
		IValue valueType = tr.typeToValue(TypeFactory.getInstance().valueType(), ctx).getValue();
		StringBuilder content = new StringBuilder();
		
		try {
			outString.getBuffer().setLength(0);
			errString.getBuffer().setLength(0);
			Result<IValue> result = eval.doEval(valueType, values.list(command), duration, evaluator);
			out.flush();
			err.flush();
			String output = outString.toString();
			if (output.length() > 0) {
				content.append(output);
			}
			output = errString.toString();
			if (output.length() > 0) {
				content.append(output);
			}
			content.append(resultMessage(result));
		}
		catch (ParseError pe) {
			content.append(parseErrorMessage(command.getValue(), "eval", pe));
			ISourceLocation sourceLocation = values.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine(), pe.getEndLine(), pe.getBeginColumn(), pe.getEndColumn());
			throw new Throw(ShellParseError.make(values, values.string(content.toString()), sourceLocation), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch (QuitException q){
			//
		}
		catch(InterruptException i) {
			content.append(interruptedExceptionMessage(i));
		}
		catch (Ambiguous e) {
			content.append(ambiguousMessage(e));
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(StaticError e){
			content.append(staticErrorMessage(e)); 
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(Throw e){
			content.append(throwMessage(e));
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(Throwable e){
			content.append(throwableMessage(e, eval != null ? ctx.getStackTrace() : ""));
			throw new Throw(ShellError.make(values, values.string(content.toString())), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		return values.string(content.toString());
	}
}

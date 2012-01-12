package org.rascalmpl.library.experiments.RascalTutor;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.toAmbiguous;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.toInterruptedException;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.toParseError;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.toResult;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.toStaticError;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.toThrow;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.toThrowable;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Throw;
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

	public HTMLGenerator(IValueFactory vf) {
		this.values = vf;
		this.tr = new TypeReifier(values);
		this.eval = new org.rascalmpl.library.util.Eval(values); 
	}
	
	public IString shell(IString command, IInteger duration, IEvaluatorContext ctx) {
		IValue valueType = tr.typeToValue(TypeFactory.getInstance().valueType(), ctx).getValue();
		String content = "";
		
		try {
			Result<IValue> result = eval.doEval(valueType, values.list(command), duration, ctx);
			content = toResult(result);
		}
		catch (ParseError pe) {
			content = toParseError(command.getValue(), pe);
			ISourceLocation sourceLocation = values.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine(), pe.getEndLine(), pe.getBeginColumn(), pe.getEndColumn());
			throw new Throw(ShellParseError.make(values, values.string(content), sourceLocation), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch (QuitException q){
			content = "";
		}
		catch(InterruptException i) {
			content = toInterruptedException(i);
		}
		catch (Ambiguous e) {
			content = toAmbiguous(e);
			throw new Throw(ShellError.make(values, values.string(content)), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(StaticError e){
			content = toStaticError(e);
			throw new Throw(ShellError.make(values, values.string(content)), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(Throw e){
			content = toThrow(e);
			throw new Throw(ShellError.make(values, values.string(content)), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		catch(Throwable e){
			content = toThrowable(e, eval != null ? ctx.getStackTrace() : "");
			throw new Throw(ShellError.make(values, values.string(content)), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		return values.string(content);
	}
}

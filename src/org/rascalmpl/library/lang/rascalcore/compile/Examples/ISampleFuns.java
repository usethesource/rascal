package org.rascalmpl.library.experiments.Compiler.Examples;

import io.usethesource.vallang.*;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalKeywordParameters;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalModule;

/* Automatically generated code; do not change */

@RascalModule("lang::rascalcore::compile::Examples::SampleFuns")
public interface ISampleFuns {
	IConstructor D_d1(int n, KWd1 kwArgs);
	IConstructor D_d1(String s, KWd1 kwArgs);
	IConstructor D_d2(String s, KWd1 kwArgs);
	IConstructor D_d3(int n, KWd3 kwArgs);
	IConstructor D_d4(String z, KWd1 kwArgs);
	int fun1(int n, KWfun1 kwArgs);
	int fun1(IList l);
	int fun1(IList l, int n);
	double fun1(double r);
	boolean isEmpty(IList lst);
	IValue sum(IList arg0);
	IConstructor D_d1(int n);
	IConstructor D_d2(String s);
	IConstructor RuntimeException_InvalidUseOfDate(String message);
	IConstructor RuntimeException_ImplodeError(String message);
	IConstructor RuntimeException_InvalidURI(String uri);
	IConstructor RuntimeException_InvalidArgument();
	IConstructor RuntimeException_JavaException(String class$, String message, IConstructor cause);
	IConstructor RuntimeException_PathNotFound(ISet locs);
	IConstructor RuntimeException_NoSuchAnnotation(String label);
	IConstructor RuntimeException_JavaException(String class$, String message);
	IConstructor RuntimeException_RegExpSyntaxError(String message);
	IConstructor RuntimeException_Ambiguity(ISourceLocation location, String nonterminal, String sentence);
	IConstructor RuntimeException_IO(String message);
	IConstructor RuntimeException_PermissionDenied(String message);
	IConstructor RuntimeException_IllegalArgument(IValue v, String message);
	IConstructor D_d3(int n);
	IConstructor RuntimeException_InvalidArgument(IValue v, String message);
	IConstructor RuntimeException_AssertionFailed();
	IConstructor RuntimeException_PermissionDenied();
	IConstructor RuntimeException_ParseError(ISourceLocation location);
	IConstructor RuntimeException_IllegalArgument(IValue v);
	IConstructor RuntimeException_NoSuchElement(IValue v);
	IConstructor RuntimeException_Timeout();
	IConstructor RuntimeException_InvalidArgument(IValue v);
	IConstructor D_d1(String s);
	IConstructor RuntimeException_ArithmeticException(String message);
	IConstructor RuntimeException_NotImplemented(String message);
	IConstructor RuntimeException_IndexOutOfBounds(int index);
	IConstructor RuntimeException_EmptyMap();
	IConstructor RuntimeException_MultipleKey(IValue key);
	IConstructor RuntimeException_Java(String class$, String message);
	IConstructor RuntimeException_NoMainFunction();
	IConstructor RuntimeException_InvalidUseOfTime(String message);
	IConstructor RuntimeException_StackOverflow();
	IConstructor RuntimeException_AssertionFailed(String label);
	IConstructor RuntimeException_Java(String class$, String message, IConstructor cause);
	IConstructor RuntimeException_EmptyList();
	IConstructor RuntimeException_EmptySet();
	IConstructor D_d4(String z);
	IConstructor RuntimeException_NoSuchKey(IValue key);
	IConstructor RuntimeException_UnavailableInformation(String message);
	IConstructor RuntimeException_InvalidUseOfLocation(String message);
	IConstructor RuntimeException_PathNotFound(ISourceLocation l);
	IConstructor RuntimeException_NoSuchField(String name);
	IConstructor RuntimeException_IllegalArgument();
	IConstructor RuntimeException_ModuleNotFound(String name);

	@RascalKeywordParameters
	interface KWd1 {
		KWd1 x(int val);
	}
	KWd1 kw_d1();

	@RascalKeywordParameters
	interface KWd3 {
		KWd3 x(int val);
		KWd3 opt(String val);
	}
	KWd3 kw_d3();

	@RascalKeywordParameters
	interface KWfun1 {
		KWfun1 delta(int val);
	}
	KWfun1 kw_fun1();
}

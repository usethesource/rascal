package org.rascalmpl.library.experiments.Compiler.Examples;

import org.rascalmpl.value.*;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalKeywordParameters;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalModule;

/* Automatically generated code; do not change */

@RascalModule("experiments::Compiler::Examples::SampleFuns")
interface ISampleFuns {
	IConstructor d1(int arg0, KWd1 kwArgs);
	IConstructor d1(String arg0, KWd1 kwArgs);
	IConstructor d2(String arg0, KWd1 kwArgs);
	IConstructor d3(int arg0, KWd3 kwArgs);
	IConstructor d4(String arg0, KWd1 kwArgs);
	int fun1(int n, KWfun1 kwArgs);
	int fun1(IList l);
	int fun1(IList l, int n);
	double fun1(double r);
	IValue main();

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

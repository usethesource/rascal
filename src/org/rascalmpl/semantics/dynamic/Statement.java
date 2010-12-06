package org.rascalmpl.semantics.dynamic;

public abstract class Statement extends org.rascalmpl.ast.Statement {


public Statement (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Solve extends org.rascalmpl.ast.Statement.Solve {


public Solve (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.QualifiedName> __param2,org.rascalmpl.ast.Bound __param3,org.rascalmpl.ast.Statement __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		int size = this.getVariables().size();
		org.rascalmpl.ast.QualifiedName vars[] = new org.rascalmpl.ast.QualifiedName[size];
		org.eclipse.imp.pdb.facts.IValue currentValue[] = new org.eclipse.imp.pdb.facts.IValue[size];

		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();

		try {
			java.util.List<org.rascalmpl.ast.QualifiedName> varList = this.getVariables();

			for (int i = 0; i < size; i++) {
				org.rascalmpl.ast.QualifiedName var = varList.get(i);
				vars[i] = var;
				if (__eval.getCurrentEnvt().getVariable(var) == null) {
					throw new org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError(var.toString(), var);
				}
				if (__eval.getCurrentEnvt().getVariable(var).getValue() == null) {
					throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(var.toString(), var);
				}
				currentValue[i] = __eval.getCurrentEnvt().getVariable(var).getValue();
			}

			__eval.pushEnv();
			org.rascalmpl.ast.Statement body = this.getBody();

			int max = -1;

			org.rascalmpl.ast.Bound bound= this.getBound();
			if(bound.isDefault()){
				org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> res = bound.getExpression().__evaluate(__eval);
				if(!res.getType().isIntegerType()){
					throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().integerType(),res.getType(), this);
				}
				max = ((org.eclipse.imp.pdb.facts.IInteger)res.getValue()).intValue();
				if(max <= 0){
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.indexOutOfBounds((org.eclipse.imp.pdb.facts.IInteger) res.getValue(), __eval.getCurrentAST(), __eval.getStackTrace());
				}
			}

			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> bodyResult = null;

			boolean change = true;
			int iterations = 0;

			while (change && (max == -1 || iterations < max)){
				change = false;
				iterations++;
				if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
				bodyResult = body.__evaluate(__eval);
				for(int i = 0; i < size; i++){
					org.rascalmpl.ast.QualifiedName var = vars[i];
					org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> v = __eval.getCurrentEnvt().getVariable(var);
					if(currentValue[i] == null || !v.getValue().isEqual(currentValue[i])){
						change = true;
						currentValue[i] = v.getValue();
					}
				}
			}
			return bodyResult;
		}
		finally {
			__eval.unwind(old);
		}
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Break extends org.rascalmpl.ast.Statement.Break {


public Break (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Target __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(this.toString()); // TODO
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class VariableDeclaration extends org.rascalmpl.ast.Statement.VariableDeclaration {


public VariableDeclaration (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.LocalVariableDeclaration __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getDeclaration().__evaluate(__eval);
	
}

}
static public class Fail extends org.rascalmpl.ast.Statement.Fail {


public Fail (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Target __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		if (!this.getTarget().isEmpty()) {
			throw new org.rascalmpl.interpreter.control_exceptions.Failure(this.getTarget().getName().toString());
		}

		throw new org.rascalmpl.interpreter.control_exceptions.Failure();
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Insert extends org.rascalmpl.ast.Statement.Insert {


public Insert (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.DataTarget __param2,org.rascalmpl.ast.Statement __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.control_exceptions.Insert(this.getStatement().__evaluate(__eval));
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Expression extends org.rascalmpl.ast.Statement.Expression {


public Expression (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();

		try {
			__eval.pushEnv();
			return this.getExpression().__evaluate(__eval);
		}
		finally {
			__eval.unwind(old);
		}
	
}

}
static public class Try extends org.rascalmpl.ast.Statement.Try {


public Try (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Statement __param2,java.util.List<org.rascalmpl.ast.Catch> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalStatementTry(this.getBody(), this.getHandlers(), null);
	
}

}
static public class AssertWithMessage extends org.rascalmpl.ast.Statement.AssertWithMessage {


public AssertWithMessage (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = this.getExpression().__evaluate(__eval);
		if (!r.getType().equals(org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(),r.getType(), this);	
		}
		if(r.getValue().isEqual(__eval.__getVf().bool(false))){
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> msgValue = this.getMessage().__evaluate(__eval);
			org.eclipse.imp.pdb.facts.IString msg = __eval.__getVf().string(org.rascalmpl.interpreter.utils.Utils.unescape(msgValue.getValue().toString(), this, __eval.getCurrentEnvt()));
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.assertionFailed(msg, __eval.getCurrentAST(), __eval.getStackTrace());
		}
		return r;	
	
}

}
static public class Append extends org.rascalmpl.ast.Statement.Append {


public Append (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.DataTarget __param2,org.rascalmpl.ast.Statement __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.Accumulator target = null;
		if (__eval.__getAccumulators().empty()) {
			throw new org.rascalmpl.interpreter.staticErrors.AppendWithoutLoop(this);
		}
		if (!this.getDataTarget().isEmpty()) {
			java.lang.String label = org.rascalmpl.interpreter.utils.Names.name(this.getDataTarget().getLabel());
			for (org.rascalmpl.interpreter.Accumulator accu: __eval.__getAccumulators()) {
				if (accu.hasLabel(label)) {
					target = accu;
					break;
				}
			}
			if (target == null) {
				throw new org.rascalmpl.interpreter.staticErrors.AppendWithoutLoop(this); // TODO: better error message
			}
		}
		else {
			target = __eval.__getAccumulators().peek();
		}
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = this.getStatement().__evaluate(__eval);
		target.append(result);
		return result;
	
}

}
static public class IfThen extends org.rascalmpl.ast.Statement.IfThen {


public IfThen (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,java.util.List<org.rascalmpl.ast.Expression> __param3,org.rascalmpl.ast.Statement __param4,org.rascalmpl.ast.NoElseMayFollow __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.Statement body = this.getThenStatement();
		java.util.List<org.rascalmpl.ast.Expression> generators = this.getConditions();
		int size = generators.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];
		org.rascalmpl.interpreter.env.Environment[] olds = new org.rascalmpl.interpreter.env.Environment[size];
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();

		int i = 0;
		try {
			gens[0] = __eval.makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = __eval.getCurrentEnvt();
			__eval.pushEnv();

			while(i >= 0 && i < size) {	
				if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
				if(gens[i].hasNext() && gens[i].next()){
					if(i == size - 1){
						__eval.setCurrentAST(body);
						return body.__evaluate(__eval);
					}

					i++;
					gens[i] = __eval.makeBooleanResult(generators.get(i));
					gens[i].init();
					olds[i] = __eval.getCurrentEnvt();
					__eval.pushEnv();
				} else {
					__eval.unwind(olds[i]);
					__eval.pushEnv();
					i--;
				}
			}
		} finally {
			__eval.unwind(old);
		}
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class Switch extends org.rascalmpl.ast.Statement.Switch {


public Switch (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,org.rascalmpl.ast.Expression __param3,java.util.List<org.rascalmpl.ast.Case> __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> subject = this.getExpression().__evaluate(__eval);

		for(org.rascalmpl.ast.Case cs : this.getCases()){
			if(cs.isDefault()){
				// TODO: what if the default statement uses a fail statement?
				return cs.getStatement().__evaluate(__eval);
			}
			org.rascalmpl.ast.PatternWithAction rule = cs.getPatternWithAction();
			if(rule.isArbitrary() && __eval.matchAndEval(subject, rule.getPattern(), rule.getStatement())){
				return org.rascalmpl.interpreter.result.ResultFactory.nothing();
				/*
			} else if(rule.isGuarded())	{
				org.meta_environment.rascal.ast.Type tp = rule.getType();
				Type t = evalType(tp);
				if(subject.getType().isSubtypeOf(t) && matchAndEval(subject.getValue(), rule.getPattern(), rule.getStatement())){
					return ResultFactory.nothing();
				}
				 */
			} else if(rule.isReplacing()){
				throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(rule);
			}
		}
		return null;
	
}

}
static public class While extends org.rascalmpl.ast.Statement.While {


public While (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,java.util.List<org.rascalmpl.ast.Expression> __param3,org.rascalmpl.ast.Statement __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.Statement body = this.getBody();
		java.util.List<org.rascalmpl.ast.Expression> generators = this.getConditions();
		
		int size = generators.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];
		org.rascalmpl.interpreter.env.Environment[] olds = new org.rascalmpl.interpreter.env.Environment[size];
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();

		java.lang.String label = null;
		if (!this.getLabel().isEmpty()) {
			label = org.rascalmpl.interpreter.utils.Names.name(this.getLabel().getName());
		}
		__eval.__getAccumulators().push(new org.rascalmpl.interpreter.Accumulator(__eval.__getVf(), label));

		// a while statement is different from a for statement, the body of the while can influence the
		// variables that are used to test the condition of the loop
		// while does not iterate over all possible matches, rather it produces every time the first match
		// that makes the condition true
		
		loop: while (true) {
			int i = 0;
			try {
				gens[0] = __eval.makeBooleanResult(generators.get(0));
				gens[0].init();
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();

				while(i >= 0 && i < size) {		
					if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
					if(gens[i].hasNext() && gens[i].next()){
						if(i == size - 1){
							body.__evaluate(__eval);
							continue loop;
						}

						i++;
						gens[i] = __eval.makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = __eval.getCurrentEnvt();
						__eval.pushEnv();
					} else {
						__eval.unwind(olds[i]);
						__eval.pushEnv();
						i--;
					}
				}
			} finally {
				__eval.unwind(old);
			}
			org.eclipse.imp.pdb.facts.IValue value = __eval.__getAccumulators().pop().done();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(value.getType(), value, __eval);
		}
	
}

}
static public class Return extends org.rascalmpl.ast.Statement.Return {


public Return (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Statement __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.control_exceptions.Return(this.getStatement().__evaluate(__eval), this.getStatement().getLocation());
	
}

}
static public class IfThenElse extends org.rascalmpl.ast.Statement.IfThenElse {


public IfThenElse (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,java.util.List<org.rascalmpl.ast.Expression> __param3,org.rascalmpl.ast.Statement __param4,org.rascalmpl.ast.Statement __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.Statement body = this.getThenStatement();
		java.util.List<org.rascalmpl.ast.Expression> generators = this.getConditions();
		int size = generators.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];
		org.rascalmpl.interpreter.env.Environment[] olds = new org.rascalmpl.interpreter.env.Environment[size];
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();

		int i = 0;
		try {
			gens[0] = __eval.makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = __eval.getCurrentEnvt();
			__eval.pushEnv();
			while(i >= 0 && i < size) {		
				if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
				if(gens[i].hasNext() && gens[i].next()){
					if(i == size - 1){
						__eval.setCurrentAST(body);
						return body.__evaluate(__eval);
					}

					i++;
					gens[i] = __eval.makeBooleanResult(generators.get(i));
					gens[i].init();
					olds[i] = __eval.getCurrentEnvt();
					__eval.pushEnv();
				} else {
					__eval.unwind(olds[i]);
					__eval.pushEnv();
					i--;
				}
			}
		} finally {
			__eval.unwind(old);
		}

		org.rascalmpl.ast.Statement elsePart = this.getElseStatement();
		__eval.setCurrentAST(elsePart);
		return elsePart.__evaluate(__eval);
	
}

}
static public class Assignment extends org.rascalmpl.ast.Statement.Assignment {


public Assignment (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assignable __param2,org.rascalmpl.ast.Assignment __param3,org.rascalmpl.ast.Statement __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getStatement().__evaluate(__eval);
		return this.getAssignable().__evaluate(new org.rascalmpl.interpreter.AssignableEvaluator(__eval.getCurrentEnvt(), this.getOperator(), right, __eval));
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.Statement.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Statement> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) this.getTree());
	
}

}
static public class NonEmptyBlock extends org.rascalmpl.ast.Statement.NonEmptyBlock {


public NonEmptyBlock (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,java.util.List<org.rascalmpl.ast.Statement> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = org.rascalmpl.interpreter.result.ResultFactory.nothing();
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();

		__eval.pushEnv(this);
		try {
			for (org.rascalmpl.ast.Statement stat : this.getStatements()) {
				__eval.setCurrentAST(stat);
				r = stat.__evaluate(__eval);
			}
		}
		finally {
			__eval.unwind(old);
		}
		return r;
	
}

}
static public class GlobalDirective extends org.rascalmpl.ast.Statement.GlobalDirective {


public GlobalDirective (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,java.util.List<org.rascalmpl.ast.QualifiedName> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(this.toString()); // TODO
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class For extends org.rascalmpl.ast.Statement.For {


public For (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,java.util.List<org.rascalmpl.ast.Expression> __param3,org.rascalmpl.ast.Statement __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.Statement body = this.getBody();
		java.util.List<org.rascalmpl.ast.Expression> generators = this.getGenerators();
		int size = generators.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();
		org.rascalmpl.interpreter.env.Environment[] olds = new org.rascalmpl.interpreter.env.Environment[size];

		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().voidType(), __eval.__getVf().list(), __eval);

		java.lang.String label = null;
		if (!this.getLabel().isEmpty()) {
			label = org.rascalmpl.interpreter.utils.Names.name(this.getLabel().getName());
		}
		__eval.__getAccumulators().push(new org.rascalmpl.interpreter.Accumulator(__eval.__getVf(), label));


		// TODO: does __eval prohibit that the body influences the behavior of the generators??

		int i = 0;
		boolean normalCflow = false;
		try {
			gens[0] = __eval.makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = __eval.getCurrentEnvt();
			__eval.pushEnv();

			while(i >= 0 && i < size) {	
				if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
				if(gens[i].hasNext() && gens[i].next()){
					if(i == size - 1){
						// NB: no result handling here.
						body.__evaluate(__eval);
					} else {
						i++;
						gens[i] = __eval.makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = __eval.getCurrentEnvt();
						__eval.pushEnv();
					}
				} else {
					__eval.unwind(olds[i]);
					i--;
					__eval.pushEnv();
				}
			}
			// TODO: __eval is not enough, we must also detect
			// break and return a list result then.
			normalCflow = true;
		} finally {
			org.eclipse.imp.pdb.facts.IValue value = __eval.__getAccumulators().pop().done();
			if (normalCflow) {
				result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(value.getType(), value, __eval);
			}
			__eval.unwind(old);
		}
		return result;
	
}

}
static public class TryFinally extends org.rascalmpl.ast.Statement.TryFinally {


public TryFinally (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Statement __param2,java.util.List<org.rascalmpl.ast.Catch> __param3,org.rascalmpl.ast.Statement __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalStatementTry(this.getBody(), this.getHandlers(), this.getFinallyBody());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Visit extends org.rascalmpl.ast.Statement.Visit {


public Visit (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,org.rascalmpl.ast.Visit __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getVisit().__evaluate(__eval);
	
}

}
static public class Continue extends org.rascalmpl.ast.Statement.Continue {


public Continue (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Target __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(this.toString()); // TODO
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Throw extends org.rascalmpl.ast.Statement.Throw {


public Throw (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Statement __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.control_exceptions.Throw(this.getStatement().__evaluate(__eval).getValue(), __eval.getCurrentAST(), __eval.getStackTrace());
	
}

}
static public class DoWhile extends org.rascalmpl.ast.Statement.DoWhile {


public DoWhile (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,org.rascalmpl.ast.Statement __param3,org.rascalmpl.ast.Expression __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.Statement body = this.getBody();
		org.rascalmpl.ast.Expression generator = this.getCondition();
		org.rascalmpl.interpreter.matching.IBooleanResult gen;
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();
		java.lang.String label = null;
		if (!this.getLabel().isEmpty()) {
			label = org.rascalmpl.interpreter.utils.Names.name(this.getLabel().getName());
		}
		__eval.__getAccumulators().push(new org.rascalmpl.interpreter.Accumulator(__eval.__getVf(), label));

		
		while (true) {
			try {
				body.__evaluate(__eval);

				gen = __eval.makeBooleanResult(generator);
				gen.init();
				if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
				if(!(gen.hasNext() && gen.next())) {
					org.eclipse.imp.pdb.facts.IValue value = __eval.__getAccumulators().pop().done();
					return org.rascalmpl.interpreter.result.ResultFactory.makeResult(value.getType(), value, __eval);
				}
			} finally {
				__eval.unwind(old);
			}
		}
	
}

}
static public class Assert extends org.rascalmpl.ast.Statement.Assert {


public Assert (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = this.getExpression().__evaluate(__eval);
		if (!r.getType().equals(org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), r.getType(), this);	
		}

		if(r.getValue().isEqual(__eval.__getVf().bool(false))) {
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.assertionFailed(this, __eval.getStackTrace());
		}
		return r;	
	
}

}
static public class EmptyStatement extends org.rascalmpl.ast.Statement.EmptyStatement {


public EmptyStatement (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class FunctionDeclaration extends org.rascalmpl.ast.Statement.FunctionDeclaration {


public FunctionDeclaration (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.FunctionDeclaration __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getFunctionDeclaration().__evaluate(__eval);
	
}

}
}
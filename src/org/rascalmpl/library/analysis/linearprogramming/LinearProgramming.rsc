@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
// High level linear programming interface
module analysis::linearprogramming::LinearProgramming

import analysis::linearprogramming::LLLinearProgramming;
import List;
import util::Maybe;
import Set;
import Number;
import Map;
import util::Math;
import Real;

alias Coefficients = map[str var,num coef];

data LinearExpression = linearExp(Coefficients coefficients, num const);
alias ObjectiveFun = LinearExpression;

public ObjectiveFun linearExp(Coefficients coefficients) =
	linearExp(coefficients,0);

data ConstraintType = leq() | eq() | geq();

data Constraint = constraint(	Coefficients coefficients,
			   					ConstraintType ctype, num const);


public LinearExpression neg(LinearExpression exp) = 
	linearExp((n : -v  | <n,v> <- toList(exp.coefficients)),-exp.const);
	
public LinearExpression add(LinearExpression lhs, LinearExpression rhs) =
	linearExp(	(n : (lhs.coefficients[n] ? 0) + (rhs.coefficients[n] ? 0) |
				n <- domain(lhs.coefficients) + domain(rhs.coefficients)),
			  lhs.const + rhs.const);

public LinearExpression sub(LinearExpression lhs, LinearExpression rhs) =
	add(lhs,neg(rhs));

public Constraint constraint(LinearExpression lhs, ConstraintType ctype) =
	constraint(lhs.coefficients,ctype, -lhs.const);

public Constraint constraint(LinearExpression lhs, 
							 ConstraintType ctype, LinearExpression rhs) =
	constraint(sub(lhs,rhs),ctype);
							 
alias Constraints = set[Constraint];

alias VariableVals = map[str var, num val];
data Solution = solution(VariableVals varVals, num funVal);

num runObjFul(ObjectiveFun f, VariableVals vals) =
	(f.const | 
	 it + f.coefficients[var]*varVals[var] |
	 var <- domain(f.coefficients));


public Maybe[Solution] 
minimizeNonNegative(Constraints constraints, ObjectiveFun f) =
	optimize(true,true, constraints, f);

public Maybe[Solution] 
minimize(Constraints constraints, ObjectiveFun f) =
	optimize(true,false, constraints, f);


public Maybe[Solution] 
maximizeNonNegative(Constraints constraints, ObjectiveFun f) =
	optimize(false,true, constraints, f);

public Maybe[Solution] 
maximize(set[Constraint] constraints, ObjectiveFun f) =
	optimize(false,false, constraints, f);


public Maybe[Solution] 
optimize(bool minimize, bool nonZero, 
		 Constraints constraints, ObjectiveFun f) {
	indexVar = getIndexVar(constraints,f);
	llConstraints = toLLConstraints(constraints,indexVar);
	llf = toLLObjectiveFun(f, indexVar);
	llSol = llOptimize(minimize, nonZero, llConstraints, llf);
	switch(llSol) {
		case nothing()   : return nothing();
		case just(llsol) : return just(fromLLSolution(llsol,indexVar));
	}
}

num zero = 0;

list[num] toLLCoefficients(Coefficients coefficients, list[str] indexVar) =
	[coefficients[i] ? zero | i <- indexVar];

Coefficients normalize(Coefficients coefs) =
	( () | it + ((c != 0) ? (var : c) : ()) | <var,c> <- toList(coefs));

public LinearExpression normalizeLinExp(LinearExpression l) =
	l[coefficients = normalize(l.coefficients)];

Coefficients 
fromLLVariableVals(LLVariableVals vars, list[str] indexVar) =
	( indexVar[i] : vars[i] | i <- index(indexVar));


LLObjectiveFun toLLObjectiveFun(ObjectiveFun f, list[str] indexVar) =
	llObjFun(toLLCoefficients(f.coefficients,indexVar),f.const);

LLConstraint toLLConstraint(Constraint c, list[str] indexVar) =
	llConstraint(toLLCoefficients(c.coefficients,indexVar),c.ctype, c.const);

LLConstraints toLLConstraints(Constraints cs, list[str] indexVar) =
	{ toLLConstraint(c,indexVar) | c <- cs }; 
	
list[str] getIndexVar(Constraints cons,ObjectiveFun f) =
	toList({*domain(con.coefficients) | con <- cons} 
		   + domain(f.coefficients));


Solution fromLLSolution(LLSolution l, list[str] indexVar) =
	solution( normalize(( indexVar[i] : l.varVals[i] | i <- index(l.varVals))),
			 l.funVal);
	

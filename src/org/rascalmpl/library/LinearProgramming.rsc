@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

module LinearProgramming

import List;

data LinearEntry 
	= constant(real con)
	| coefficients(list[real] c);


data LinearObjectiveFunction = linearObjFun(list[real] coefficients, real constant);

data GoalType = minimize() | maximize();

data ConstraintType = lessOrEqual() | equal() | greaterOrEqual();

data Constraint 
	= constraint(LinearEntry lhs, ConstraintType conType, LinearEntry rhs);
	
data Solution 
	= sol(list[real] sollution, real fVal)
	| noSolFound();
	
 
data LinearProblem = 
	linProblem(
		GoalType goal,
		LinearObjectiveFunction f,
		set[Constraint] constraints,
		bool nonZero
	);

list[real] padToSize(list[real] l, int s) =
	l + [0.0 | _ <- [1..s - size(l)]];

public int nrVars(LinearProblem lp) = max([size(l) |  /coefficients(l) <- lp ]);
	
public LinearProblem normalize(LinearProblem lp,int nrVars) =
	visit(lp) {
		case coefficients(l) => coefficients(padToSize(l)) 
		     when size(l) != nrVars
	};
	
public Solution optimize(LinearProblem lp) {
	n = nrVars(lp);
	return optimizeR(n,normalize(lp,n));
}
	
	
@javaClass{org.rascalmpl.library.LinearProgramming}
public java Solution optimizeR(int nrVars,LinearProblem lp) ; 
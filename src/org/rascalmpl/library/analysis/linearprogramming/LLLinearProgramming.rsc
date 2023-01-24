@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@synopsis{Low level linear programming interface}
module analysis::linearprogramming::LLLinearProgramming

import List;
import util::Maybe;

alias LLCoefficients = list[num];

data LLObjectiveFun = llObjFun(LLCoefficients coefficients, num const);

public LLObjectiveFun llObjFun(LLCoefficients coefficients) =
	llObjFun(coefficients,0);

data ConstraintType = leq() | eq() | geq();

data LLConstraint = 
	llConstraint(LLCoefficients coefficients,
			   	ConstraintType ctype, num const);
				
alias LLConstraints = set[LLConstraint];
alias LLVariableVals = list[num];

data LLSolution = llSolution(LLVariableVals varVals, num funVal);

public num llRunObjFul(LLObjectiveFun f, LLVariableVals vals) =
	(f.const | 
	 it + f.coefficients[var]*vals[var] |
	 var <- index(f.coefficients));

list[num] padToSize(list[num] l, int s) =
	l + [0.0 | _ <- [1,2..s - size(l) + 1]];

public tuple[LLConstraints constraints, LLObjectiveFun f]
normalize(LLConstraints constraints, LLObjectiveFun f){
	int nrVars = max([
			max([size(con.coefficients) | con <- constraints ]),
			size(f.coefficients)
		]);
	constraints = {llConstraint(padToSize(con.coefficients,nrVars),con.ctype, con.const) |
					con <- constraints };
	f = llObjFun(padToSize(f.coefficients,nrVars),f.const);
	return <constraints, f>;
}

@javaClass{org.rascalmpl.library.analysis.linearprogramming.LinearProgramming}
public java Maybe[LLSolution] 
llOptimize(bool minimize, bool nonZero, 
		   LLConstraints constraints, LLObjectiveFun f);

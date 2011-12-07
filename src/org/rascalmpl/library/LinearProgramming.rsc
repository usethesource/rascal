@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

module LinearProgramming

import List;
import Maybe;
import Set;
import Number;
import Map;
import IO;

alias Variable = str;
alias ScaledVar = tuple[num scale, Variable var];

data LinearCombination = linComb(set[ScaledVar] coefficients, num constant);

data Constraint = eq(LinearCombination leq, LinearCombination req)
				| leq(LinearCombination lhs, LinearCombination rhs);				
Constraint geq(LinearCombination lhs, LinearCombination rhs) =
	leq(rhs,lhs);
alias Constraints = set[Constraint];

alias ObjectiveFunction = LinearCombination;

data Solution = solution(map[Variable var, num solution], num funVal);

public Maybe[Solution] 
minimizeNonNegative(set[Constraint] constraints, ObjectiveFunction f) =
	optimize(true,true, constraints, f);

public Maybe[Solution] 
minimize(set[Constraint] constraints, ObjectiveFunction f) =
	optimize(true,false, constraints, f);

public Maybe[Solution] 
minimize(set[Constraint] constraints, ObjectiveFunction f, bool nonZero) =
	optimize(true,nonZero, constraints, f);

public Maybe[Solution] 
maximizeNonNegative(set[Constraint] constraints, ObjectiveFunction f) =
	optimize(false,true, constraints, f);

public Maybe[Solution] 
maximize(set[Constraint] constraints, ObjectiveFunction f) =
	optimize(false,false, constraints, f);

public Maybe[Solution] 
maximize(set[Constraint] constraints, ObjectiveFunction f, bool nonZero) =
	optimize(false,nonZero, constraints, f);

public Maybe[Solution] 
optimize(bool minimize, bool nonZero, 
		 set[Constraint] constraints, ObjectiveFunction f) {
	varIndex = getVarIndex(constraints,f);
	llConstraints = toLowLevelConstraints(constraints,varIndex);
	llf = toLowLevelLinearCombination(f, varIndex);
	llSol = llOptimize(minimize, nonZero, llConstraints, llf);
	switch(llSol) {
		case nothing()   : return nothing();
		case just(llsol) : return just(fromLLSolution(llsol,invertUnique(varIndex)));
	}
}
	
// below: Low level interface (without names for the variables, just indexes)

alias IndexUpdate = tuple[int index,num update];

LLLinComb toLowLevelLinearCombination
	(LinearCombination comb, map[Variable,int] varIndex) {
	list[IndexUpdate] indexUpdates = 
		[ <varIndex[v],s> | <s,v> <- comb.coefficients];
	indexUpdates = sort(indexUpdates);
	int lastInd = (indexUpdates == []) ? -1 : last(indexUpdates)[0];
	cofs = for (i <- [0,1..lastInd]) {
		cof = 0.0;
		while (!isEmpty(indexUpdates) && head(indexUpdates).index == i) {
			cof += toReal(head(indexUpdates).update);
			indexUpdates = tail(indexUpdates);
		}
		append cof;
	}
	return <cofs, comb.constant>;
} 

LLConstraint toLowLevelConstraint(Constraint c, map[Variable,int] varIndex) {
	LinearCombination left, right;
	bool maybeLess;
	switch (c) {
		case leq(l,r) : { left = l ; right = r; maybeLess = true; }
		case eq (l,r) : { left = l ; right = r; maybeLess = false;}
	}
	return llConstraint(toLowLevelLinearCombination(left,varIndex),
						toLowLevelLinearCombination(right,varIndex),
						maybeLess);
}

LLConstraints toLowLevelConstraints(Constraints cs, map[Variable,int] varIndex) =
	{ toLowLevelConstraint(c,varIndex) | c <- cs }; 
	
map[Variable,int] getVarIndex(Constraints con,ObjectiveFunction f) {
	vars = toList({var | /linComb(co,_) <-con, <_,var> <- co} 
				 + {var | <_,var> <- f.coefficients});
	return ( vars[i] : i | i <- index(vars));
}

Solution fromLLSolution(LLSolution l, map[int,Variable] indexVar) =
	solution( ( indexVar[i] : l.values[i] | i <- index(l.values)),
			 l.fval);
	

alias LLLinComb = tuple[list[num] coefficients, num constant];
alias LLObjFun = LLLinComb;

data LLConstraint = llConstraint(LLLinComb l, LLLinComb r, bool maybeLess);
alias LLConstraints = set[LLConstraint];

list[num] padToSize(list[num] l, int s) =
	l + [0.0 | _ <- [1,2..s - size(l)]];


alias LLSolution = tuple[list[num] values, num fval];

public Maybe[LLSolution] 
llOptimize(bool minimize, bool nonZero, LLConstraints constraints, LLLinComb f) {
	int nrVars = max([size(co) | /<co,_> <- [constraints,f] ]);
	constraints = {llConstraint(<padToSize(lcs,nrVars),lcon>,
								<padToSize(rcs,nrVars),rcon>,less) 
				  | llConstraint(<lcs,lcon>,<rcs,rcon>,less) <- constraints};
	f.coefficients = padToSize(f.coefficients,nrVars);
	return llOptimizeJ(minimize,nonZero,constraints,f);
}
	 

@javaClass{org.rascalmpl.library.LinearProgramming}
java Maybe[LLSolution] 
llOptimizeJ(bool minimize, bool nonZero, LLConstraints constraints, LLLinComb f) ; 


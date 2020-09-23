@bootstrapParser
module lang::rascalcore::check::CheckType

//extend analysis::typepal::TypePal;
//extend lang::rascalcore::check::AType;

extend lang::rascalcore::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;

void checkConditions(list[Expression] condList, Solver s){
    for(Expression cond <- condList){
        tcond = s.getType(cond);
        if(!s.isFullyInstantiated(tcond)){
            s.requireUnify(abool(), tcond, error(cond, "Cannot unify %t with `bool`", cond));
            tcond = s.instantiate(tcond); 
        } 
        s.requireSubType(tcond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
    }
}
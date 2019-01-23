@bootstrapParser
module lang::rascalcore::check::CheckType

import lang::rascal::\syntax::Rascal;

extend analysis::typepal::TypePal;
extend lang::rascalcore::check::AType;

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
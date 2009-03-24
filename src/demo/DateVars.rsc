module demo::Misc::DateVars

set[Var] getDateVars(Program P){
   return {V | Var V : P, 
               /^.*(date|dt|year|yr).*$/i ~= toString(V)};
}
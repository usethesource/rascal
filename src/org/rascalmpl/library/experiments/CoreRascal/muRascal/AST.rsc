module experiments::CoreRascal::muRascal::AST

import Prelude;

public data MuModule =
            muModule(str name, list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initialization);
          
public data MuFunction =
            muFunction(str name, int scope, int nformal, int nlocal, list[MuExp] body)
          ;
public data MuVariable =
            muVariable(str name)
          ;
          
public data MuType =
            muType(list[Symbol] symbols)  
          ;
          
public data MuExp = 
            muEmpty()
          | muConstant(value c)
          | muLabel(str name)
          | muTypeCon(Symbol tp)
          | muVar(str id, int scope, int pos)
          
          | muCall(MuExp fun, list[MuExp] args)
          | muCallPrim(str name, MuExp exp1)
          | muReturn()
          | muReturn(MuExp exp)
          | muCallPrim(str name, MuExp exp1, MuExp exp2)
                    
          | muAssign(str id, int scope, int pos, MuExp exp)
          | muIfelse(MuExp exp1, MuExp exp2, MuExp exp3)
          | muWhile(MuExp cond, MuExp body)
          | muLabeled(str name, MuExp MuExp)
          
          | muCreate(MuExp exp)
          | muNext(MuExp exp)
          | muNextnext(MuExp exp1, MuExp exp2)
          | muYield()
          | muYield(MuExp exp)
          | muHasNext(MuExp exp)
          
          | muBlock(list[MuExp] exps)
		  ;

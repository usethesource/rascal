module experiments::CoreRascal::muRascal::AST

import Prelude;

public data MuModule =
            muModule(str name, list[MuDefinition] definitions, MuExp initialization);
          
public data MuDefinition =
            fun(str name, int nformal, int nlocals, MuExp body);
          
public data MuExp = 
            empty()
          | constant(value c)
          | label(str name)
          | typecon(Symbol tp)
          | var(str id, int scope, int pos)
          
          | call(MuExp fun, list[MuExp] args)
          | callprim(str name, MuExp exp1)
          | ret()
          | ret(MuExp exp)
          | callprim(str name, MuExp exp1, MuExp exp2)
                    
          | assign(str id, int scope, int pos, MuExp exp)
          | ifelse(MuExp exp1, MuExp exp2, MuExp exp3)
          | \while(MuExp cond, MuExp body)
          | labeled(str name, MuExp MuExp)
          
          | create(MuExp exp)
          | next(MuExp exp)
          | next(MuExp exp1, MuExp exp2)
          | yield()
          | yield(MuExp exp)
          | hasNext(MuExp exp)
          
          | block(list[MuExp] exps)
		  ;

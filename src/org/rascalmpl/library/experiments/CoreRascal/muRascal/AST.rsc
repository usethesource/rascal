module experiments::CoreRascal::muRascal::AST

import Prelude;

public data MuModule =
            muModule(str name, list[MuType] types, list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initialization);
          
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
            muCon(value c)
          | muLab(str name)
          | muFun(str name)
          | muConstr(str name) // constructors
          | muVar(str id, int scope, int pos)
          | muVarRef(str id, int scope, int pos) // call-by-reference: the kind of a variable that refers to a value location
          | muTypeCon(Symbol tp)
          | muNote(str txt)
          
          | muCall(MuExp fun, list[MuExp] args)
          | muCall(str fname, list[MuExp] args)
          | muCallConstr(str cname, list[MuExp] args) // constructors
          | muCallPrim(str name, MuExp exp1)
          | muCallPrim(str name, MuExp exp1, MuExp exp2)
          | muReturn()
          | muReturn(MuExp exp)
              
          | muAssign(str id, int scope, int pos, MuExp exp)
          | muAssignRef(str id, int scope, int pos, MuExp exp) // call-by-reference: the left-hand side is a variable that refers to a value location
          | muIfelse(MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)
          | muWhile(MuExp cond, list[MuExp] body)
          | muLabeled(str name, list[MuExp] MuExp)
          
          | muCreate(str fname)
          | muCreate(MuExp exp)
          | muInit(MuExp coro)
          | muInit(MuExp coro, list[MuExp] args)
          | muHasNext(MuExp exp)
          | muNext(MuExp exp)
          | muNext(MuExp exp1, list[MuExp] args)
          | muYield()
          | muYield(MuExp exp)
          
          | muRefVar(str id, int scope, int pos) // call-by-reference: expression that returns a value location
       	  ;

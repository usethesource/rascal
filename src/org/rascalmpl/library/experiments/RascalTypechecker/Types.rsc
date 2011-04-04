@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::RascalTypechecker::Types

// Some initial exercises to explore how a Rascal typechecker would look like ...

data Type =
       AbstractDataType(str Name, list[ConstructorType] constructors)
     | AliasType(str Name, Type type)
     | BoolType()
     | Constructor(str Name, list[NamedType] elmTypes)
     | FunctionType(Type resultType, list[Type] argTypes)
     | IntegerType()
     | ListType(Type elmType)
     | MapType(Type fromType, Type toType)
     | NodeType()
     | ParameterType(str Name)
     | RealType()
     | RelationType(list[NamedType] elmTypes)
     | SetType(Type elmType)
     | SourceLocationType()
     | StringType()
     | TupleType(list[NamedType] elmTypes)
     | ValueType()
     | VoidType()
     // Missing: NonTerminalType, SymbolType
     | ErrorType(str msg, loc where)
     ;
     
data ConstructorType =
       ConstructorType(str Name, list[NamedType] elmTypes);
       
data NamedType =
     | UnnamedType(Type elmType)
     | NamedType(Type elmType, str Name)
     ;
     
Type getType(NamedType nt){
     switch(nt){
     case UnnamedType(Type t): return t;
     case NamedType(Type t, _): return t;
     }
}
     
Type subtypeOf(Type t1, Type t2){

     if(t1 == t2 || t1 == VoidType() || t2 == ValueType())
         return true;
         
     switch(t1){
     
     case AliasType(_, Type t1a): return subtypeOf(t1a t2);
     
     case ListType(Type elmType1): 
          return ListType(Type elmType2) := t2 && subtypeOf(elmType1, elmType2)
          
     case SetType(Type elmType1): 
          return SetType(Type elmType2) := t2 && subtypeOf(elmType1, elmType2)
          
     case RelationType(list[NamedType] elmTypes1): 
          return RelationType(list[NamedType] elmTypes2) := t2 && 
                 subtypeOf(SetType(TupleType(elmTypes1)), SetType(TupleType(elmTypes2)))
          
     case MapType(Type fromType1, Type toType1): 
          return MapType(Type fromType2, Type toType2) := t2 && 
                 subtypeOf(fromType1, fromType2) &&
                 subtypeOf(toType1, toType2)
                 
     case TupleType(list[NamedType] elmTypes1):
          return TupleType(list[NamedType] elmTypes2) && size(emlTypes1) == size(elmTypes2) &&
                 all(subtypeOf(getType(elmTypes1[i]), getType(elmTypes2[i])) | int i <- domain(elmTypes1))
     
     case FunctionType(Type resultType1, list[Type] argTypes1):  // Not sure what we want here
          return FunctionType(Type resultType2, list[Type] argTypes2) := t2 &&
                 size(argTypes1) == size(argTypes2) &&
                 subtypeOf(resultType1, resultType2) &&
                 all(subtypeOf(argTypes1[i], argTypes2[i]) | int i <- domain(argTypes1))

      // TODO: ADT, alias, constructor, parameter, NonTerminal Type
             
     default:
          return false;
     }
}

// Define lub here


//-----------------------------------------------------
// Here we us a list of maps to represent type scope
// Alternative: a single relation of <name, type, scopeId> tuples (but ordering?)

alias Scope   = map[Identifier, Type]
alias TypeEnv = list[Scope];

TypeEnv Env = [()];

Scope currentScope = ();

void enterScope(){
    Env = currentScope + Env;
    currentScope = ();
}

void leaveScope(){
    currentScope = head(Env);
    Env = tail(Env);
}

bool isDeclared(Identifer id){
    return currentScope[id]? || any(scope[id]? | scope <- Env);
}

Type getType(Identifier id){
    if(currentScope[id]?)
       return currentScope[id];
     for(scope <- Env){
       if(scope[id]?)
         return scope[id];
     }
     return ErrorType("Undeclared variable <id>", id@posinfo);
}

Statement declare(Identifier id, Type t, Statement stat){
  if(currentScope[id]?){
     return stat@error("Double declaration of variable <id>", id@posinfo);
  }
  currentScope[Id] = t;
  return stat;
}

// Type annotations

anno Type Expression@type;
anno Type Statement@type;

Type expected(Type t1, Type 2){
  return ErrorType("Expected type <t1> but got <t2>");
}

Type incompatible(Type t1, Type t2){
  return ErrorType("Incompatible types <t1> and <t2>");
}

Type noOperator(str op, Type t1, Type t2){
  return ErrorType("Operator <op> not defined on operands of type <t1> and <t2>");
}

Expression checkExp(Expression Exp){

    return bottom-up visit Exp {
    
    case E:Integer N => E@IntType();
    
    case E:Identifier Id => E@getType(id);
    
    case E:[|<Expression E1> + <Expression E2>|]:
         switch(<E1@type, E2@type>){
         
         case <IntType(), IntType()>                  => E@IntType();   
         case <RealType(), RealType()>                => E@RealType();
         case <StrType(), StrType()>                  => E@StrType();
         case <ListType(Type et1), ListType(Type et2) => E@ListType(lub(et1, et2));  
         case <SetType(Type et1), SetType(Type et2)   => E@SetType(lub(et1, et2));  
         case <TupleType(list[NamedType] elmTypes1), TupleType(list[NamedType] elmTypes2)>
                                                      => E@TupleType(elmTypes1 + elmTypes2);
         case <MapType(Type fromType1, Type toType1), MapType(Type fromType2, Type toType2)>
                                                      => E@mapType(lub(fromType1, fromType2), lub(toType1, toType2));
         default                                      => E@noOperator("+", E1@type, E2@type);
         }
     // all the other operators
     default => E@ErrorType("Cannot check expression <Exp>");    
    }
}

Statement checkStatement(Statement Stat){
    return bottom-up visit(Stat){
    
    case S: [|<Type t> <Identifier id>|] => declare(id, t, S);
         
    case S2: [|<Type t1> <Identifier id> = <Expression E>;|]:{
         if(subtypeOf(E@type, t1))
            insert declare(id, t1, S2);
         else
            insert S2@incompatible(":=", t1, t2, E@posinfo);
    
    case S: [|<Identifier Id> = <Expression E>|]:
         if(isDeclared(id)){
            t = getType(id);
            insert subtypeOf(E@type, t)) ? S : S@incompatible(E@type, t);
         } else {  
         	insert declare(id, E@type, S);
         }
         
    case S: Statement[|<Expression E>|] => S;
         
    case S: [|if(<Expression E>)<Block B1>else<Block B2>;|] =>
           	(E@type == BoolType()) ? S : S@expected(BoolType(), E@type);
            
    case S: [|while(<Expression E>)<Block B>;|] =>
         	(E@type == BoolType()) ? S : S@expected(BoolType(), E@type);
    
    }
}

Block checkBlock(Block B){
    enterScope();
    B1 = topdown-break visit(B){
         case Statement S => checkStatement(S);
    }
    leaveScope();
    return B1;
}


     
     

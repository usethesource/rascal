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

Type declaration(Identifier id){
    if(currentScope[id]?)
       return currentScope[id];
     for(scope <- Env){
       if(scope[id]?)
         return scope[id];
     }
     throw UndeclaredVariable(id, id@pos);
}

void declare(Identifier id, Type t){
  if(currentScope[id]?)
     throw DoubleDeclaration(id, id@posinfo);
  currentScope[Id] = t;
}

// Type annotations

anno Type Expression@type

// Error

data error = error(str, Type, loc)
           | error(str, Type, Type, loc)
           ;
           
anno error Expression@error

Expression checkExp(Expression Exp){

    visit Exp {
    
    case E:Integer N => E@IntType();
    
    case E:Identifier Id => E@declaration(id);
    
    case E:[|<Expression E1> + <Expression E2>|]:
         switch(<E1@type, E2@type>){
         
         case <IntType(), IntType()> => E@IntType();
         
         case <RealType(), RealType()> => E@RealType();
         
         case <StrType(), StrType()> => E@StrType();
         
         case <ListType(Type et1), ListType(Type et2) =>
              E@ListType(lub(et1, et2));
              
         case <SetType(Type et1), SetType(Type et2) =>
              E@SetType(lub(et1, et2));
              
         case <TupleType(list[NamedType] elmTypes1), TupleType(list[NamedType] elmTypes2)> =>
              E@TupleType(elmTypes1 + elmTypes2);
              
         case <MapType(Type fromType1, Type toType1), MapType(Type fromType2, Type toType2)> =>
              E@mapType(lub(fromType1, fromType2), lub(toType1, toType2));
         default => E@error("+", t1, t2, E@posinfo);
         }
    }
}

bool checkStatement(Statement S){
    switch(S){
    
    case [|<Type t> <Identifier id>|]:
         declare(id, t);
         return true;
         
    case [|<Type t1> <Identifier id> = <Expression E>;|]:
         t2 = checkExp(E);
         if(subtypeOf(t2, 1))
            declare(id, t1);
         else
            throw IncompatibleOperands(":=", t1, t2, E@posinfo);
    
    case [|<Identifier Id> = <Expression E>|]:
         t = checkExp(E);
         if(isDeclared(id) && subtypeOf(t, declaration(id))
            return true;
         declare(id, t)
         return true;
         
    case Statement[|<Expression E>|]:
         checkExp(E);
         return true;
         
    case [|if(<Expression E>)<Block B1>else<Block2>;|]:
         return checkExp(E) == BoolType() && checkBlock(B1) && checkBlock(B2);
         
    case [|while(<Expression E>)<Block B>;|]:
         return checkExp(E) == BoolType() && checkBlock(B);  
    
    }
}

bool checkBlock(Block B){
    enterScope();
    try
       for(Statement S <- B)
           checkStatement(S);
    finally:
       leaveScope();
}


     
     
module lang::rascal::checker::TTL::TTLsyntax

extend lang::rascal::\syntax::Rascal;

// TTL : Typechecker Test Language
// A DSL for writing type-related tests aiming at:
// - testing the Rascal type checker
// - testing compatibility between Rascal type checker and evaluator
// A TTL definition may contain the following elements:
// - definition of a module to be imported in tests in the same file
// - definition of a named declaration that can be used in tests in the same file
// - test definitions:
//   (a) A general test: a sequence of Rascal declarations and statements
//   (b) An operator test (infix, prefix or postfix)
// - Each test defines an expectation about its outcome:
//   (a) specific type of a variable
//   (b) specific error message
//   (c) specific exception 

start syntax TTL = ttl: TestItem* items;

start syntax TestItem =
	  defMod:      "define" Name name "{" Module moduleText "}"
	| defDecl:     "define" Name name "{" Declaration declaration "}"
	| GeneralTest: "test" DecimalIntegerLiteral nargs "variables" "{" Use use Statement+ statements "}" "expect" "{" {Expect ","}* expectations "}" 
	| InfixTest:   "infix" Name name {StringLiteral ","}+ operators "{" {BinarySignature ","}+ signatures "}"
	| PrefixTest:  "prefix" Name name {StringLiteral ","}+ operators "{" {UnarySignature ","}+ signatures "}"
	| PostfixTest: "postfix" Name name {StringLiteral ","}+ operators "{" {UnarySignature ","}+ signatures "}"
	| PatternTest: "test" DecimalIntegerLiteral nargs "patterns" "{" Expression expression "}"
	;

syntax BinarySignature = ExtendedType left "x" ExtendedType right "-\>" ExtendedType result Condition condition;
syntax UnarySignature = ExtendedType left "-\>" ExtendedType result Condition condition;

syntax Condition = 
       nonempty: "when" "&" Name name "is" "not" "a" RascalKeywords typeName
     | empty: ()
     ;

syntax ExtendedType =
       intType: "int"          
     | boolType: "bool"
     | realType: "real"
     | ratType: "rat"
     | strType: "str"
     | numType: "num"
     | nodeType: "node"
     | voidType: "void"
     | valueType: "value"
     | locType: "loc"
     | datetimeType: "datetime"
     | listType: "list" "[" ExtendedType elemType "]"
     | lrelType:  "lrel" "["  {ExtendedType ","}+ elemTypes"]"
     | setType:  "set" "[" ExtendedType elemType "]"
     | relType:  "rel" "["  {ExtendedType ","}+ elemTypes "]"
     | mapType:  "map" "[" ExtendedType keyType "," ExtendedType valType "]"
     | tupleType: "tuple" "[" {ExtendedType ","}+ elemTypes "]"
     | lubType:  "LUB" "(" ExtendedType left "," ExtendedType right ")"
     | typeVar: "&" Name name
     | typeVarBounded: "&" Name name "\<:" ExtendedType bound
     | testVar: Name name
     ;

start syntax Use = use: "use" Name+ names "::" |  none: ()  ;

start syntax Expect =
         inferred: ExtendedType expectedType Name name
       | message: RegExpLiteral regexp
       | exception: Name name
       ;  
       
data Symbol = LUB(Symbol l, Symbol r);
              
public loc TTLRoot = |rascal:///lang/rascal/checker/TTL/|;         // where TTL resides
public str modulePrefix = "lang::rascal::checker::TTL::generated"; // where modules defined in TTL files reside
public str TTL = "ttl"; 											// TTL language extension

                                  
str toSymbolAsStr(ExtendedType t){
  if( t is intType) return  "\\int()";
  if( t is boolType) return  "\\bool()";
  if( t is realType) return  "\\real()";
  if( t is ratType) return  "\\rat()";
  if( t is strType) return  "\\str()";
  if( t is numType) return  "\\num()";
  if( t is nodeType) return  "\\node()";
  if( t is voidType) return  "\\void()";
  if( t is valueType) return  "\\value()";
  if( t is locType) return  "\\loc()";
  if( t is datetimeType) return  "\\datetime()";
  if(t is listType) return "\\list(<toSymbolAsStr(t.elemType)>)";
  if(t is setType) return "\\set(<toSymbolAsStr(t.elemType)>)";
  if(t is mapType) return "\\map(<toSymbolAsStr(t.keyType)>,<toSymbolAsStr(t.valType)>)";
  if(t is tupleType) return "\\tuple([<intercalate(",", [toSymbolAsStr(e) | e <- t.elemTypes])>])";
  if(t is relType) return "\\rel([<intercalate(",", [toSymbolAsStr(e) | e <- t.elemTypes])>])";
  if(t is lrelType) return "\\lrel([<intercalate(",", [toSymbolAsStr(e) | e <- t.elemTypes])>])";
  if(t is lubType) return "\\LUB(<toSymbolAsStr(t.left)>,<toSymbolAsStr(t.right)>)";
  if(t is typeVar) return "\\parameter(\"<t.name>\", \\value())";
  if(t is typeVarBounded) return "\\parameter(\"<t.name>\", <toSymbolAsStr(t.bound)>)";
  throw "unexpected case in toSymbolAsStr";
}

Symbol toSymbol(ExtendedType t){
  if( t is intType) return  \int();
  if( t is boolType) return  \bool();
  if( t is realType) return  \real();
  if( t is ratType) return  \rat();
  if( t is strType) return  \str();
  if( t is numType) return  \num();
  if( t is nodeType) return  \node();
  if( t is voidType) return  \void();
  if( t is valueType) return  \value();
  if( t is locType) return  \loc();
  if( t is datetimeType) return  \datetime();
  if(t is listType) return \list(toSymbol(t.elemType));
  if(t is setType) return \set(toSymbol(t.elemType));
  if(t is mapType) return \map(toSymbol(t.keyType),toSymbol(t.valType));
  if(t is tupleType) return \tuple([toSymbol(e) | e <- t.elemTypes]);
  if(t is relType) return \rel([toSymbol(e) | e <- t.elemTypes]);
  if(t is lrelType) return \lrel([toSymbol(e) | e <- t.elemTypes]);
  if(t is lubType) return \LUB(toSymbol(t.left),toSymbol(t.right));
  if(t is typeVar) return \parameter("<t.name>", \value());
  if(t is typeVarBounded) return \parameter("<t.name>", toSymbol(t.bound));
  throw "unexpected case in toSymbol";
}


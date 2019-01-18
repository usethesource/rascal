module lang::rascal::tutor::OldQuestions

// Syntax of the Question language

lexical LAYOUT = [\ \t \n];

layout Layout = LAYOUT* !>> [\ \t \n];


//layout LAYOUTLIST
//  = LAYOUT* !>> [\t-\n \r \ ] !>> "//" !>> "/*";
//
//lexical LAYOUT
//  = Comment 
//  | [\t-\n \r \ ];
//    
//lexical Comment
//  = /*@category="Comment"*/  "/*" (![*] | [*] !>> [/])* "*/" 
//  | /*@category="Comment"*/  "//" ![\n]* [\n];
  

keyword Reserved
    = "question"
    | "prep"
    | "expr"
    | "end"
    ;

keyword TypeNames
    = "bool"
    | "int"
    | "real"
    | "num"
    | "str"
    | "loc"
    | "datetime"
    | "list"
    | "set"
    | "map"
    | "tuple"
    | "rel"
    | "lrel"
    | "value"
    | "void"
    | "arb"
    ;

lexical InsName = ([A-Z a-z] [A-Z a-z 0-9 _]* !>> [A-Z a-z 0-9 _]) \ TypeNames;

lexical Name = [A-Z a-z] [A-Z a-z 0-9 _]* !>> [A-Z a-z 0-9 _];

lexical IntCon = "-"?[0-9]+ !>> [0-9];

lexical HoleContents
    = "\\" !<< "{" ( ![{}] | ("\\" [{}]) | HoleContents)* parts "\\" !<< "}";
    
lexical HoleCmd 
    = "?" HoleContents contents
    ;
    
lexical NonInsertText
    = ![!{} \ \n \t]
    ;

syntax EvalCmd
    = "%{" EvalElem+ elements "}"
    ;

syntax EvalElem
    = insert_cmd: InsertCmd insertCmd
    | non_insert_cmd: NonInsertText
    ;
    
syntax InsertCmd = "!{" Insert insertDetails "}";

syntax Insert =
         InsName name
       | InsName name ":" Type type
       | Type type
       ;
       
syntax Range = "[" IntCon min "," IntCon max "]" range;

syntax Type 
        = "bool" 
        | "int"
        | "int" Range range
        | "real"
        | "real" Range range
        | "num"
        | "num" Range range
        | "str"
        | "loc"
        | "datetime"
        | "list" "[" Type elemType "]"
        | "list" "[" Type elemType "]" Range range
        | "set" "[" Type elemType "]"
        | "set" "[" Type elemType "]" Range range
        | "map" "[" Type keyType "," Type valType "]"
        | "map" "[" Type keyType "," Type valType "]" Range range
        | "tuple" "[" {Type ","}+ elemTypes "]"
        | "tuple" "[" {Type ","}+ elemTypes "]" Range range
        | "rel" "[" {Type ","}+ elemTypes "]"
        | "lrel" "[" {Type ","}+ elemTypes "]"
        | "value"
        | "void"
        | "arb" "[" IntCon depth ","  {Type ","}+ elemTypes  "]"
        ;

lexical Op = [? % !];

lexical AlphaNum = [a-z A-Z 0-9 _];

lexical NonOp = !([? % !] || [a-z A-Z 0-9 _] || [\ \t \n]);

lexical AnyText
    = NonOp+ !>> !([? % !] || [a-z A-Z 0-9 _] || [\ \t \n])
   // = NonOp+ !>> NonOp    // Should be the same but does not work.
    | Op !>> "{"
    | AlphaNum+ !>> [a-z A-Z 0-9 _]
    ;
 
lexical AnyButReserved = AnyText \ Reserved; 

syntax QuestionPart = (HoleCmd | InsertCmd | EvalCmd | AnyButReserved)+;

syntax Question = Intro? intro "question" QuestionPart text Body body "end";

syntax Body 
    = Prep prep
    | Prep prep Expr expr
    | Expr expr
    ;

syntax Text = "text" ":" QuestionPart text;

syntax Prep = "prep" ":" QuestionPart text;

syntax Expr = "expr" Name name ":" QuestionPart text;

syntax Intro = AnyText+ !>> "question";

start syntax Questions = Question+ questions;
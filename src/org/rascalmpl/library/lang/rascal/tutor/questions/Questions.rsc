module lang::rascal::tutor::questions::Questions

import ParseTree;

// Syntax of the Question language

lexical LAYOUT = [\ \t \n];

layout Layout = LAYOUT* !>> [\ \t \n];
  
start syntax Questions = IntroAndQuestion+ introAndQuestions;

syntax IntroAndQuestion
    = Intro? intro "question" Tokens description Question question "end";

syntax Question
    = CodeQuestion
    | ChoiceQuestion
    | ClickQuestion
    | MoveQuestion
    | FactQuestion
    ;

syntax CodeQuestion
    = Prep? prep Expr? expr;

syntax Prep 
    = "prep" TokenOrCmdList text;

syntax Expr 
    = "expr" Name name TokenOrCmdList text;

syntax ChoiceQuestion
    = Choice+ choices;

syntax Choice
    =  "choice" YesOrNo correct "|||" Tokens description "|||" Tokens feedback
    ;

lexical YesOrNo = "y" | "n";
    
syntax ClickQuestion
    = "clickable" TokenOrCmdList text;

syntax MoveQuestion
    = "movable" Tokens text Decoy? decoy;
    
syntax Decoy
    = "decoy" Tokens text;

syntax FactQuestion
    = Fact+ facts;
    
syntax Fact
    = "fact" Tokens leftText "|||" Tokens rightText;
    
keyword Reserved
    = "question"
    | "prep"
    | "expr"
    | "end"
    | "choice"
    | "clickable"
    | "movable"
    | "decoy"
    | "fact"
    | "$answer"
    | "$gen" 
    | "$use" 
    | "$eval"
    | "$click"
    ;

lexical Name = [$]? [A-Za-z] [A-Z a-z 0-9 _]* !>> [A-Z a-z 0-9 _];

lexical IntCon = "-"?[0-9]+ !>> [0-9];

syntax Intro = AnyText+ !>> "question";
    
lexical AnyText = Name \ Reserved | IntCon | ![$ a-z A-Z 0-9 \ \t \n] ;

lexical AnyButSpecial = Name \ Reserved | IntCon | ![$ ( ) \\ a-z A-Z 0-9 \t \n] ;

lexical Escaped
    = "\\" ( "(" | ")" | "$" | Reserved)
    ; 

syntax Cmd 
    = AnswerCmd
    | GenCmd
    | UseCmd
    | EvalCmd
    | ClickCmd
    ;
syntax AnswerCmd
    = "$answer" "(" TokenOrCmdList elements ")"
    ;
syntax GenCmd
    = "$gen"    "(" Type type ")"
    | "$gen"    "(" Type type "," Name name ")"
    ;
syntax UseCmd 
    = "$use"    "(" Name name ")"
    ;
syntax EvalCmd
    = "$eval"   "(" TokenOrCmdList elements ")"
    ;
syntax ClickCmd
    = "$click"  "(" Tokens elements ")"
    ;

syntax Token
    = Escaped
    > AnyButSpecial
    ;

syntax Tokens
    = "(" Tokens ")" Tokens?
    | Token Tokens?
    ;
    
syntax TokenOrCmd
    = aCmd: Cmd aCmd
    > aToken: Token aToken
    ;

syntax TokenOrCmdList
    = parens: "(" TokenOrCmdList tocList ")" TokenOrCmdList? optTocList
    | noparens: TokenOrCmd toc TokenOrCmdList? optTocList
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
        
syntax Range = "[" IntCon min "," IntCon max "]" range;

public Questions parse(str src) = parse(#start[Questions], src).top;

value pmain(){
/*

"123, 456
             'question Replace text box by a function name and make the test true.
             'prep import List;
             'expr listFunction $answer(headTail)($gen(list[int][1,10]))
             'end
             
             'question Replace text box by the result of the intersection and make the test true.
             'expr setIntersection $eval($gen(set[int]) + $gen(set[int],B)) & $eval($gen(set[int]) + $use(B))
             'end
             ' $abc (
            'question Which means of transportation is faster
              choice Apache Helicopter
                     correct no      
                     feedback The speed of an Apache is 293 km/hour
              choice High-speed train
                     correct yes
                     feedback  The speed of high-speed train is 570 km/hour
              choice Ferrari F430
                     correct no
                     feedback The speed of a Ferrari is 315 km/hour
              choice Hovercraft
                     correct no
                     feedback The speed of a Hovercraft is 137 km/hour
              end
              question Click on all identifiers in this code fragment:
             'clickable  
                    $click(x) = 1;
                    $click(y) = $click(x) + 2;
             'end"
             */
    
return parse("123, 456
             'question Replace text box by a function name and make the test true.
             'prep import List;
             'expr listFunction $answer(headTail)($gen(list[int][1,10]))
             'end
             
             'question Replace text box by the result of the intersection and make the test true.
             'expr setIntersection $eval($gen(set[int]) + $gen(set[int],B)) & $eval($gen(set[int]) + $use(B))
             'end
             ' $abc (
            'question Which means of transportation is faster
              choice Apache Helicopter
                     correct no      
                     feedback The speed of an Apache is 293 km/hour
              choice High-speed train
                     correct yes
                     feedback  The speed of high-speed train is 570 km/hour
              choice Ferrari F430
                     correct no
                     feedback The speed of a Ferrari is 315 km/hour
              choice Hovercraft
                     correct no
                     feedback The speed of a Hovercraft is 137 km/hour
              end
              question Click on all identifiers in this code fragment:
             'clickable  
                    $click(x) = 1;
                    $click(y) = $click(x) + 2;
             'end"
/*
    "question[code] Replace text box by a function name and make the test true.
    '   prep import List;
    '   expr listFunction: $answer[headTail]($gen[list[int][1,10]])
    'end
    'question[code] Replace text box by the result of the intersection and make the test true.
    '   expr setIntersection: $eval[ $gen[set[int]] + $gen[set[int],B] } & $eval[$gen[set[int]] + $use[B]] ]
    'end"
    */);
}

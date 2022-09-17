module lang::rascal::tests::concrete::Patterns4

import ParseTree;

syntax OptTestGrammar = A? a B b;

syntax A = "a";
syntax B = "b";

layout L = " "*;
syntax QValue
  = "[" QConst "]"
  ;

syntax QConst = QExp;

syntax QExp
    = "x"
    | QExp "-" QExp
    ;

syntax Question = "question" QValue? v;

test bool optionalNotPresentIsFalse() = !((A)`a` <- ([OptTestGrammar] "b").a);
test bool optionalPresentIsTrue() = (A)`a` <- ([OptTestGrammar] "ab").a;

test bool optAbsent(){
    if((Question) `question <QValue? v>` := [Question] "question"){
        for((QValue) `[<QConst const>]` <- v){
           return false;
        }
    }
    return true; 
}

test bool optPresent(){
    if((Question) `question <QValue? v>` := [Question] "question [x-x]"){
        for((QValue) `[<QConst const>]` <- v){
           return const := [QConst] "x-x";
        }
    }
    return false; 
}

bool absent((Question) `question <QValue? v>`){
    bool b = true;
    for((QValue) `[<QConst const>]` <- v){
       b = false;
    }
    return b;
}

test bool optAbsentInParameter() = absent([Question] "question");

bool present((Question) `question <QValue? v>`){
    bool b = false;
    for((QValue) `[<QConst const>]` <- v){
       b = const := [QConst] "x-x";
    }
    return b;
}

test bool optPresentInParameter() = present([Question] "question [x-x]");
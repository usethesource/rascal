module lang::rascal::tests::concrete::Layout

// DO NOT ADD CODE THAT TRIGGERS A PARSER TO THIS MODULE
// WE NEED A MODULE WHICH CAN RUN THE GRAMMAR NORMALIZER LIKE THIS WITHOUT TRIGGERING THE GENERATOR
import ParseTree;

layout Manual = @manual "***";
layout Auto = [\ \t\n\r]*;

syntax A = "a" "b" "c";
syntax B = "a" Manual "b" "c";
syntax C = {"a" ","}*;
syntax D = {"a" (Manual "," Manual)}*;
syntax E = A a "x" B b;

test bool layoutA() = /prod(sort("A"),[lit("a"),layouts("Auto"),lit("b"),layouts("Auto"),lit("c")],_) := #A.definitions[sort("A")];
test bool layoutB() = /prod(sort("B"),[lit("a"),layouts("Manual"),lit("b"),layouts("Auto"),lit("c")],_) := #B.definitions[sort("B")];
test bool layoutC() = /prod(sort("C"),[\iter-star-seps(lit("a"),[layouts("Auto"),lit(","),layouts("Auto")])],_) := #C.definitions[sort("C")];
test bool layoutD() = /prod(sort("D"),[\iter-star-seps(lit("a"),[seq([layouts("Manual"),lit(","),layouts("Manual")])])],_) := #D.definitions[sort("D")];
test bool layoutE() = /prod(sort("E"),[label("a",sort("A")),layouts("Auto"),lit("x"),layouts("Auto"),label("b",sort("B"))],_) := #E.definitions[sort("E")];

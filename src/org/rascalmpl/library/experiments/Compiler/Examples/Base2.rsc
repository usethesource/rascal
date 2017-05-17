module experiments::Compiler::Examples::Base2

import IO;
import ParseTree;

default void EXTENDED_FUNCTION(Tree t) { println("default use: <t> :-("); }

void bug2() =  
EXTENDED_FUNCTION(
appl(
  prod(
    sort("A"),
    [lit("a")],
    {}),
  [appl(
      prod(
        lit("a"),
        [\char-class([range(97,97)])],
        {}),
      [char(97)])])[
  @\loc=|test-modules:///ConsoleInput.rsc|(0,1,<1,0>,<1,1>)
]);
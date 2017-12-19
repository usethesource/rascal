module lang::rascalcore::check::Test3


value priority(list[int] levels) {
  ordering = { father | [pre*,int father] := levels };
  }
  
test bool matchSet18() = {*Y} := {1,2} && Y == {1,2};
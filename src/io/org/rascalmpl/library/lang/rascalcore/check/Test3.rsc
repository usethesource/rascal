module lang::rascalcore::check::Test3


value priority(set[int] levels) {
  //ordering = { father | {*pre,int father} := levels };
   {*pre,int father} := levels; //{1,2};
  }
  
test bool matchSet18() = {*Y} := {1,2} && Y == {1,2};

bool testSetMultiVariable2() = {*S1, *S2} := {} && (S1 == {}) && (S2 == {});

test bool testSetMultiVariable8()  {R = for({*S1, *S2} := {100, 200}) append <S1, S2>; return {*R} == {<{200,100}, {}>, <{200}, {100}>, <{100}, {200}>, <{}, {200,100}>};}

value removeIdPair(){
         if ( { *c } := {1} ) 
              return c;
}
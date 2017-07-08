@ignoreCompiler{Test fails, cause unknown}
module experiments::Compiler::Tests::StringTemplates

extend experiments::Compiler::Tests::TestUtils;
import List;


test bool tst() = run("\"ab\"") == "ab";
test bool tst() = run("\"a\<12345\>b\"") == "a<12345>b";
test bool tst() = run("\"a\<5 + 7\>b\"") == "a<5 + 7>b";
test bool tst() = run("{x = 100; \"a\<x + 7\>b\";}") == {x = 100; "a<x + 7>b";};

test bool tst() = run("{x = 5; \"a\<if(true){\>\<x + 10\>\<}\>b\";}") == {x = 5; "a<if(true){><x + 10><}>b";};
test bool tst() = run("{x = 5; \"a\<if(false){\>\<x + 10\>\<}\>b\";}") == {x = 5; "a<if(false){><x + 10><}>b";};

test bool tst() = run("{x = 5; \"a\<if(true){\>\<x + 10\>\<} else {\> \<100\> \<}\>b\";}") == {x = 5; "a<if(true){><x + 10><} else {> <100> <}>b";};
test bool tst() = run("{x = 5; \"a\<if(false){\>\<x + 10\>\<} else {\> \<100\> \<}\>b\";}") == {x = 5; "a<if(false){><x + 10><} else {> <100> <}>b";};

test bool tst() = run("{x = 5; \"a\<while(x \> 0){\>\<x + 10\>\<x -= 1;}\>b\";}") == {x = 5; "a<while(x > 0){><x + 10><x -= 1;}>b";};
test bool tst()  = run("{x = 5; \"a\<do{\> \<x + 10\>\<x -= 1;}while(x \> 0)\>b\";}") == {x = 5; "a<do{> <x + 10><x -= 1;}while(x > 0)>b";};


test bool tst() = run("{\"a\<for(x \<- [0 .. 5]){\>\<x\>\<}\>b\";}") == "a<for(x <- [0 .. 5]){><x><}>b";


// Indentation

test bool tst() = run("{x = 5; \"a\<if(true){\>
     \'\<x + 10\>\<}\>b\";}") == {x = 5; "a<if(true){>
     '<x + 10><}>b";};
     
     
test bool tst() = run("{\"a\<for(x \<- [0 .. 5]){\>
                         \' zz\<x\>\<}\>b\";}") == "a<for(x <- [0 .. 5]){>
                          ' zz<x><}>b";
                                      
bool tstIntercalate(str sep, list[value] L) = 
      intercalate(sep, L)
      == 
      (isEmpty(L) ? "" : "<L[0]><for(int i <- [1..size(L)]){><sep><L[i]><}>");
/*  
The string template misses one \n character:                              
value: "2140780238r200812343\n \t\t\n\n \t\t|tmp:///B83|\n \t\t"
value: "2140780238r200812343\n \t\t\n \t\t|tmp:///B83|\n \t\t"
*/

test bool  tst() = tstIntercalate("\n \t\t", [2140780238r200812343,"\n",|tmp:///B83|,""]);          
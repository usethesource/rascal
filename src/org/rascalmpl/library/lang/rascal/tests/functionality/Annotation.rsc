@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module lang::rascal::tests::functionality::Annotation

import Exception;

data F = f() | f(int n) | g(int n) | deep(F f);
anno int F@pos;
data AN = an(int n);

anno int F@notThere;
  	
// boolannotations

test bool boolannotations1() = true ||  /*documentation of old behavior: */ f() [@pos=1] == f();
test bool boolannotations2() = f() [@pos=1]@pos == 1;
test bool boolannotations3() = f() [@pos=1][@pos=2]@pos == 2;

// since annotations are simulated by kw params this is no longer true:  		
test bool boolannotations4() = true || /*documentation of old behavior: */ f(5) [@pos=1] == f(5);
test bool boolannotations5() = true || /*documentation of old behavior: */ f(5) [@pos=1]@pos == 1;
test bool boolannotations6() = true || /*documentation of old behavior: */ f(5) [@pos=1][@pos=2]@pos == 2;
  		
// since annotations are simulated by kw params this is no longer true  		
test bool boolannotations7() = true || /*documentation of old behavior: */ deep(f(5) [@pos=1]) == deep(f(5));
test bool boolannotations8() = true || /*documentation of old behavior: */ f(5) [@pos=1] == f(5) [@pos=2];	
  	
// annotationsInSets
// since annotations are simulated by kw params this is no longer true:  
//test bool annotationsInSets1() = true || /*documentation of old behavior: */ {f() [@pos=1]} == {f()};
//test bool annotationsInSets2() = true || /*documentation of old behavior: */ {f() [@pos=1], g(2) [@pos=2]} == {f(), g(2)};
//test bool annotationsInSets3() = true || /*documentation of old behavior: */ {f() [@pos=1], g(2)} == {f(), g(2)[@pos=2]};		
//test bool annotationsInSets4() = true || /*documentation of old behavior: */ {deep(f(5) [@pos=1])} == {deep(f(5))};
//test bool annotationsInSets5() = true || /*documentation of old behavior: */ {f() [@pos=1]} + {g(2) [@pos=2]} == {f(), g(2)};
//test bool annotationsInSets6() = true || /*documentation of old behavior: */ {X = {f() [@pos=1]} + {f() [@pos=2]}; {F elem} := X && (elem@pos == 2 || elem@pos == 1);};

test bool accessAnnoAsKeywordField(){
    F example = f();
    example@pos = 1;
    return example.pos == 1;
}

test bool accessAnnoUpdateAsKeywordField(){
   F example = f();
   example@pos = 1;
   return example[@pos=2].pos == 2;
}

test bool checkAnnoExistsAsKeywordField(){
   F example = f();
   example@pos = 1;
   return example.pos?;
}

@ignoreInterpreter{
TODO: JV this still fails
}
test bool KeywordFieldUpdateVisibleAsAnno(){
    F example = f();
   // keyword updates are visible to anno projection
   return example[pos=3]@\pos == 3;
}

test bool KeywordAssignVisibleViaAnno1(){
    F example = f();
    example@pos = 1;
    example.pos = 4;
    return example@pos == 4;
}

test bool KeywordAssignVisibleViaAnno2(){
    F example = f();
    example@pos = 1;
    example.pos += 4;
    return example@pos == 5;
}

test bool unavailableAnno1(){
    F example = f();
    try {
     example@notThere;
     return false;
   }
   catch NoSuchAnnotation("notThere"):
      return true;
}

test bool unavailableAnno2(){
     F example = f();

    // default behavior for simulated annotations is to throw a comparable exception
   try {
     node x = example; // must hide type to avoid static error
     x.notThere;
     return false;
   }
   catch NoSuchField("notThere"):
     return true;
}


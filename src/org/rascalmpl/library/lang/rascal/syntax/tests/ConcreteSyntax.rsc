module lang::rascal::\syntax::tests::ConcreteSyntax

import IO;
import ParseTree;

syntax A 
  = a:"a" 
  | b:"b"
  | left two: A lhs A rhs 
  ;

syntax S 
  = "s"
  | "epsilon" () "."
  | "opt" S? "."
  | "seq2" (S S) "."
  | "seq3" (S S S) "."
  | "alt2" (S | T) "."
  | "star" S* list "."
  | "plus" S+ plist "."
  | "starsep" {S T}* slist "."
  | "plussep" {S T}+ pslist "."
  ;
  
syntax T 
  = "t";
     
layout WS = [\t\n\ ]* !>> [\t\n\ ];
   
public A a = (A) `a`;  
public A b = (A) `b`;  
public A aa = (A) `a a`; 
public A ab = (A) `a b`;      

public A twoA = (A) `<A a> <A a>`;
public A threeA = (A) `<A aa> <A a>`; 

public test bool typeMatch() = A _ := a;
public test bool twoMatch() = (A) `<A a1> <A a2>` := twoA && a1 == a && a2 == a;
public test bool twoMatch2() = (A) `<A a1> <A a2>` := ab && a1 == a && a2 == b;
public test bool threeMatch() = (A) `<A a1> <A a2> <A a3>` := threeA && a1 == a && a2 == a && a3 == a;
 
// testing regular expressions

public S s = (S) `s`;
public T t = (T) `t`;
public S eps = (S) `epsilon.`;
public S opt1 = (S) `opt.`;
public S opt2 = (S) `opt <S s>.`;
public S seq2 = (S) `seq2 <S s> <S s>.`;
public S seq3 = (S) `seq3 <S s> <S s> <S s>.`;
public S alt2_1 = (S) `alt2 <S s>.`;
public S alt2_2 = (S) `alt2 <T t>.`;
public S star_none = (S) `star.`;
public S star_one = (S) `star <S s>.`;
public S star_two = (S) `star <S s> <S s>.`;
public S plus_one = (S) `plus <S s>.`;
public S plus_two = (S) `plus <S s> <S s> <S s>.`;

public S star_sep_none = (S) `starsep.`;
public S star_sep__one = (S) `starsep <S s>.`;
public S star_sep__two = (S) `starsep <S s> <T t> <S s>.`;
public S plus_sep__one = (S) `plussep <S s>.`;
public S plus_sep__two = (S) `plussep <S s> <T t> <S s> <T t> <S s>.`;

public test bool matcheps() = (S) `epsilon .` := eps; 

public test bool matchopt1() = (S) `opt .` := opt1;
public test bool matchopt2() = (S) `opt s.` := opt2;
//public test bool matchopt2() = (S) `opt <S? _>.` := opt1;
//public test bool matchopt2() = (S) `opt <S? _>.` := opt2;

public test bool matchseq1() = (S) `seq2 s s.` := seq2;
public test bool matchseq2() = (S) `seq2 s <S _>.` := seq2;
//public test bool matchseq3() = (S) `seq2 <(S S) _>.` := seq2;

public test bool matchstar() = (S) `star .` := star_none;
public test bool matchstar2() = (S) `star s.` := star_one;
public test bool matchstar3() = (S) `star <S s1>.` := star_one && s1 == s; 
public test bool matchstar4() = (S) `star <S* ss>.` := star_two;
public test bool matchstar5() = (S) `star <S s1> <S* ss>.` := star_two;

public test bool splicestar1() = (S*) x := star_two.\list && (S) `star <S x> <S* x>.` == (S) `star s s s s.`;

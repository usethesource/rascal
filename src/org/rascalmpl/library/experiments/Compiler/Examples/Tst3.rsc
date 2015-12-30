module experiments::Compiler::Examples::Tst3

import IO;
import String;

bool areOverlapping(str s1, str s2) 
    //= (size(s1) > 0 && size(s2) > 0
    //&& (s1[-1] == s2[0] || s1[0] == s2[-1] || s1[0] == s2[0]))
    //|| 
    //(size(s2) > 1 && areOverlapping(s1, s2[..-1]))
    //;
    = s1 == s2 || findAll(s1 + s2, s2) != [s1] || findAll(s2 + s1, s1) != [s2];
    

//test bool tstReplaceAll(str S1, str S2, str S3) {
//  if(areOverlapping(S1, S2)) return true;
//  S = S1 + S2 + S1 + S2 + S1;
// // iprintln("S = <S>");
//  res = replaceAll(S, S2, S3);
//  expected = S1 + S3 + S1 + S3 + S1;
//  //iprintln("replaceAll(<S>, <S2>, <S3>)");
//  //iprintln("expected:  <expected>");
//  //iprintln("got:       <res>");
//  
//  return replaceAll(S, S2, S3) == S1 + S3 + S1 + S3 + S1;
//}

//value main() = tstReplaceAll("1\t\t\n\n\t\t", "\n\t\t\n\t", "rAq");

test bool tstReplaceLast(str S1, str S2, str S3) {
  if(areOverlapping(S1, S2)) return true;
  S = S1 + S2 + S1 + S2 + S1;
  res=  replaceLast(S, S2, S3);

  expected =  S1 + S2 + S1 + S3 + S1;
  iprintln("S =      <S>");
  iprintln("returns  <res>");
  iprintln("expected <expected>");
  return res  == S1 + S2 + S1 + S3 + S1;
}

value main() = tstReplaceLast("\t\nU\t", "\t\n\t\n", "up");
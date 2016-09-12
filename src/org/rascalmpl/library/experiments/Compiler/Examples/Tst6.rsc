module experiments::Compiler::Examples::Tst6

import String; 
import IO;

bool areOverlapping(str s1, str s2) = 
    s1 == s2 || findAll(s1 + s2, s2) != [size(s1)] || findAll(s2 + s1, s1) != [size(s2)]
    || ((size(s1) > 0 && size(s2) > 0) && s1[-1] == s2[0] || s1[0] == s2[-1]) ;

test bool tstReplaceLast(str S1, str S2, str S3) {
  if(areOverlapping(S1, S2) || areOverlapping(S1+S2, S2+S1)) return true;
  println("other");
  S = S1 + S2 + S1 + S2 + S1;
  return replaceLast(S, S2, S3) == S1 + S2 + S1 + S3 + S1;
}

value main() = tstReplaceLast("\t \n\tnNJ", " \t ", "");
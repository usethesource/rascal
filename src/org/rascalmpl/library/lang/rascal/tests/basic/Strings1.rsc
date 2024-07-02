module lang::rascal::tests::basic::Strings1

import String;
import List;
import util::Math;
import IO;

test bool subscription(str S){
  R = "";
  for(int i <- [0..size(S)]){
      R += S[i];
  }
  return R == S;
}

test bool sliceEmpty() = ""[0..5] == "";
test bool sliceOverEnd() = "/"[1..] == "";
test bool sliceOverEnd2() = "/"[2..] == "";

test bool subscriptionWrapped(str S){
  for(int i <- [0 .. size(S)]){
      if(S[i] != S[i - size(S)]){
      	 return false;
      }
  }
  return true;
}

test bool sliceFirst1(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  S = L[f .. e];
  return S == makeSlice(L, f, f + 1, e);
}

test bool sliceFirst2(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  S = L[f..];
  return S == makeSlice(L, f, f + 1, size(L));
}

// In an ideal world, this should work, but we have to adapt ranges first ...

//public list[int] makeSlice(list[int] L, int b, int s, int e){
//  return
//    for(int i <- [b, s .. e])
//      append L[i];
//}

public str makeSlice(str L, int f, int s, int e){
  res = "";
  int i = f;
  int delta = s - f;
  if(delta == 0 || f == e)
     return res;
  if(f <= e){
     while(i >= 0 && i < size(L) && i < e ){
        res += L[i];
        i += delta;
     }
  } else {
    while(i >= 0 && i < size(L) && i > e){
        res += L[i];
        i += delta;
     }
  }
  return res;
}

test bool sliceFirstSecond(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  return L[f, f + incr..] == makeSlice(L, f, f + incr, size(L));
}


test bool sliceEnd(str L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..e] == makeSlice(L, 0, 1, e);
}

test bool sliceSecondEnd(str L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  incr = 2;
  first = incr > e ? size(L)-1 : 0;
  return L[,incr..e] == makeSlice(L, first, incr, e);
}

public tuple[int,int] arbFirstEnd(str L){
  if(isEmpty(L)) throw "No beging/end indices possible";
  if(size(L) == 1) return <0,0>;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  return <f, e>;
}
test bool sliceFirstSecondEnd(str L) {
  if(isEmpty(L)) return true;
  <f, e> = arbFirstEnd(L);
  incr = 2;
  return L[f, f + incr .. e] == makeSlice(L, f, f + incr, e);
}

test bool sliceFirstNegative(str L) {
  if(isEmpty(L)) return true;
  f = 1;
  return L[-f..] == makeSlice(L, size(L) - f,  size(L) - f + 1, size(L));
}

test bool sliceEndNegative(str L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..-e] == makeSlice(L, 0, 1, e == 0 ? e : size(L) - e);
}

test bool sliceFirstNegativeSecondNegative(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  if(f == 0)
     return L[0, -incr..] == makeSlice(L, 0, size(L) - incr, size(L));
  else
     return L[-f, -(f + incr)..] == makeSlice(L, size(L) - f, size(L) - (f + incr), -1);
}

test bool sliceSecondNegative(str L) {
  if(isEmpty(L)) return true;
  incr = 2;
  S = L[, -incr ..];
  return S == makeSlice(L, 0, size(L) - incr, size(L));
}

test bool assignSlice1() { L = "abcdefghij"; L[..] = "XY"; return L == "XYXYXYXYXY";}
test bool assignSlice2() { L = "abcdefghij"; L[2..] = "XY"; return   L == "abXYXYXYXY";}
test bool assignSlice3() { L = "abcdefghij"; L[2..6] = "XY"; return L == "abXYXYghij";}
test bool assignSlice4() { L = "abcdefghij"; L[8..3] = "XY"; return L == "abcdXYXYXj";}

test bool assignStep1() { L = "abcdefghij"; L[,2..] = "X"; return L == "XbXdXfXhXj";}
test bool assignStep2() { L = "abcdefghij"; L[,2..] = "XY"; return L == "XbYdXfYhXj";}
test bool assignStep3() { L = "abcdefghij"; L[,2..] = "X"; return L == "XbXdXfXhXj";}
test bool assignStep4() { L = "abcdefghij"; L[,2..] = "XY"; return L == "XbYdXfYhXj";}
test bool assignStep5() { L = "abcdefghij"; L[,2..] = "XYZ"; return L == "XbYdZfXhYj";}
test bool assignStep6() { L = "abcdefghij"; L[,2..] = "XYZPQRS"; return L == "XbYdZfPhQjRS";}

test bool assignStep7() { L = "abcdefghij"; L[2,4..] = "X"; return L == "abXdXfXhXj";}
test bool assignStep8() { L = "abcdefghij"; L[2,4..6] = "X"; return L == "abXdXfghij";}

test bool assignStep9() { L = "abcdefghij"; L[,6..1] = "X"; return L == "abcXefXhiX";}
test bool assignStep10() { L = "abcdefghij"; L[8,6..] = "X"; return L == "XbXdXfXhXj";}
test bool assignStep11() { L = "abcdefghij"; L[8,6..3] = "X"; return L == "abcdXfXhXj";}


test bool assignStep12() { L = "abcdefghij"; L[-1,-2..] = "XYZPQ"; return L == "QPZYXQPZYX";}
test bool assignStep13() { L = "abcdefghij"; L[-1,-3..] = "XYZPQ"; return L == "aQcPeZgYiX";}

// Library functions

test bool tstCenter1(str S) { c = center(S, size(S) + 5); return contains(c, S) && startsWith(c, " ") && endsWith(c, " "); }
test bool tstCenter2(str S) { c = center(S, size(S) + 5, "x"); return contains(c, S) && startsWith(c, "x") && endsWith(c, "x"); }

test bool  tstCharAt(str S) {  
  for(i <- [0 .. size(S)])
      if(charAt(S, i) != chars(S[i])[0]) return false;
  return true;
}

test bool tstChars(str S) = S == stringChars(chars(S));

test bool tstContains(str S1, str S2, str S3) = contains(S1+S2+S3, S1) && contains(S1+S2+S3, S2) && contains(S1+S2+S3, S3);

test bool tstEndsWith(str S1, str S2) = endsWith(S1+S2, S2);

test bool tstEscape(str S, str K1, str R1, str K2, str R2){
  if(isEmpty(K1) || isEmpty(K2) || K1[0] == K2[0] || contains(S, K1[0]) || contains(S, K2[0])) return true;
  T = K1[0] + S + K2[0] + S + K2[0] + S + K1[0];
  return escape(T, (K1[0] : R1, K2[0] : R2)) == R1 + S + R2 + S + R2 + S + R1;
}

test bool tstFindAll(str S1, str S2){
  S = S2 + S1 + S2 + S1 + S2;
  for(i <- findAll(S, S2))
      if(!startsWith((i < size(S) ? S[i..] : ""), S2)) return false;
  return true;
}

test bool tstFindFirst(str S1, str S2){
  S = S1 + S2 + S1 + S2;
  i = findFirst(S, S2);
  return i >= 0 && startsWith((i < size(S) ? S[i..] : ""), S2);
}

test bool tstFindLast(str S1, str S2){
  S = S1 + S2 + S1 + S2 + S1;
  i = findLast(S, S2);
  return i >= 0 && startsWith((i < size(S) ? S[i..] : ""), S2);
}

test bool tstIsEmpty(str S) = isEmpty(S) ? size(S) == 0 : size(S) > 0;

test bool tstStringChar(str S) {
  for(i <- [0 .. size(S)])
     if(stringChar(chars(S)[i]) != S[i]) return false;
  return true;
}

test bool tstIsValidCharacter(str S) = isEmpty(S) || all(i <- [0 .. size(S)], isValidCharacter(chars(S[i])[0]));

test bool tstLeft1(str S) { l = left(S, size(S) + 5); return startsWith(l, S) && endsWith(l, " "); }
test bool tstLeft2(str S) { l = left(S, size(S) + 5, "x"); return startsWith(l, S) && endsWith(l, "x"); }
test bool tstLeft1_s(str S) { l = left(S, size(S) + 1); return startsWith(l, S) && endsWith(l, " "); }
test bool tstLeft2_s(str S) { l = left(S, size(S) + 1, "x"); return startsWith(l, S) && endsWith(l, "x"); }

bool areOverlapping(str s1, str s2) = 
    s1 == s2 || findAll(s1 + s2, s2) != [size(s1)] || findAll(s2 + s1, s1) != [size(s2)]
    || ((size(s1) > 0 && size(s2) > 0) && s1[-1] == s2[0] || s1[0] == s2[-1]) ;

test bool tstReplaceAll(str S1, str S2, str S3) {
  if(areOverlapping(S1, S2) || areOverlapping(S1+S2, S2+S1)) return true;
  S = S1 + S2 + S1 + S2 + S1;
  return replaceAll(S, S2, S3) == S1 + S3 + S1 + S3 + S1;
}

test bool tstReplaceFirst(str S1, str S2, str S3) {
  if(areOverlapping(S1, S2) || areOverlapping(S1+S2, S2+S1)) return true;
  S = S1 + S2 + S1 + S2 + S1;
  return replaceFirst(S, S2, S3) == S1 + S3 + S1 + S2 + S1;
}

test bool tstReplaceLast(str S1, str S2, str S3) {
  if(areOverlapping(S1, S2) || areOverlapping(S1+S2, S2+S1)) return true;
  S = S1 + S2 + S1 + S2 + S1;
  return replaceLast(S, S2, S3) == S1 + S2 + S1 + S3 + S1;
}

test bool tstReverse(str S) = S == reverse(reverse(S));

// rexpMatch

test bool tstRight1(str S) { r = right(S, size(S) + 5); return endsWith(r, S) && startsWith(r, " "); }
test bool tstRight2(str S) { r = right(S, size(S) + 5, "x"); return endsWith(r, S) && startsWith(r, "x"); }

test bool tstSize(str S) = size(S) == size(chars(S));

test bool tstSplit(str S1, str S2) = areOverlapping(S1,S2) || isEmpty(S1) || isEmpty(S2) || contains(S2, S1) || S1[-1] == S2[0] || S1[0] == S2[-1] || split(S1, S2 + S1 + S2 + S1) == [S2, S2];

// squeeze

test bool tstSqueeze1(str S) = /<c:[a-zA-Z]><c>/ !:= squeeze(S, "a-zA-Z");
test bool tstSqueeze2(str S) = squeeze(S, "") == S;
test bool tstSqueeze3(str S) {
  if (/<c:[a-z]><c>/ := S) {
    return /<c><c>/ := squeeze(S, "0-9");
  }
  return true;
}


test bool tstStartsWith(str S1, str S2) = startsWith(S1+S2, S1);

test bool tstSubstring1(str S){
  for(i <- [0 .. size(S)])
      if(substring(S, i) != (i < size(S) ? S[i..] : "")) return false;
  return true;
}

test bool tstSubstring2(str S){
  for(i <- [0 .. size(S)])
      for(j <- [i .. size(S)])
          if(substring(S, i, j) != (i < size(S) ? S[i..j] : "")) return false;
  return true;
}

test bool toInt(int N) = N == toInt("<N>");

test bool tstToLowerCase(str S) = /[A-Z]/ !:= toLowerCase(S);

test bool toReal(real R) = R == toReal("<R>");

test bool tstToUpperCase(str S) = /[a-z]/ !:= toUpperCase(S);

test bool tstTrim(str S) = trim(S) == trim(" \t\n" + S + "\r\b\t ");

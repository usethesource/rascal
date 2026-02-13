module lang::rascal::tests::basic::Strings2

import String;
import List;
import util::Math;
import util::Reflective;

test bool tstWrap(str S1 , str S2) {
  S1 = trim(S1);
  S2 = trim(S2);
  if(contains(S1, "\n") || contains(S2, "\n") || contains(S1, "  ") || contains(S2, "  ")) return true;
  if (S1 == "" && S2 == "") return true;
  S = S1 + " " + S2 + " " + S1 + " " + S2;
  n = max(size(S1), size(S2)) + 2;
  return trim(S) == trim(replaceAll(wrap(S, n), getLineSeparator(), " "));
}

private set[str] UNICODE_WS = {
  "\u0009", "\u000A", "\u000B", "\u000C", "\u000D",
  "\u0020",
  "\u0085",
  "\u00A0",
  "\u1680",
  "\u180E",
  "\u2000", "\u2001", "\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009", "\u200A",
  "\u2028", "\u2029",
  "\u202F",
  "\u205F",
  "\u3000"
};

test bool tstRemoveWhitespace1(str S1)
  = removeWhitespace(S1) == "<for(int i <- [0..size(S1)]){><S1[i] in UNICODE_WS ? "" : S1[i]><}>";
test bool tstRemoveWhitespace2(str S1)
  = size(removeWhitespace(S1)) <= size(S1);

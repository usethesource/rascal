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

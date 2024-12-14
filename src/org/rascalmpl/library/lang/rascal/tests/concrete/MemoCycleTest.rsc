module lang::rascal::tests::concrete::MemoCycleTest

import ParseTree;
import vis::Text;

syntax S = T | U;

syntax T = X T? | "$";

syntax U = X T? | "$";

syntax X = "b"? | "c";

// Test for regression of a bug in the node flattener
test bool memoCycleBug() {
   Tree tree = parse(#S, "bc$", |unknown:///|, allowAmbiguity=true);
   if (amb({appl1, appl2 }) := tree) {

      // Find the suspect alternative
      Tree suspectAppl = appl1;
      if (appl2.prod.symbols == [sort("T")]) {
         suspectAppl = appl2;
      }

      if (appl(_, [amb({alt1,alt2})]) := suspectAppl) {
         /* Yield of one of the alternatives should be empty because all we have left is a cycle:
         T = X  T?
         ├─ X = "b"?
         │  └─ "b"?
         └─ T?
            └─ cycle(T, 2)
         */
         return "<alt1>" == "" || "<alt2>" == "";
      }
   }

   return false;
}

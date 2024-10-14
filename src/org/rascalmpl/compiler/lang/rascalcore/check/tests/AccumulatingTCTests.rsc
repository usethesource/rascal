@bootstrapParser
module lang::rascalcore::check::tests::AccumulatingTCTests
 
import lang::rascalcore::check::tests::StaticTestingUtils;

test bool testappendWithoutFor() = illegalUse("append 3;");

test bool AppendHasLexicalScopingFunction() 
    = illegalUseInModule("
        module AppendHasLexicalScopingFunction
            bool f() { append 3; }
            void main() {for (x \<- [1,2,3]) f(); }");
 
test bool AppendHasLexicalScopingClosure() =
    illegalUseInModule("
        module AppendHasLexicalScopingClosure
            void main() {
                f = () { append 3; }; 
                for (x \<- [1,2,3]) { f(); }
            }
    ");

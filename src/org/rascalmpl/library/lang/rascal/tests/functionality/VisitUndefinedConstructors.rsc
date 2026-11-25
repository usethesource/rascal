module lang::rascal::tests::functionality::VisitUndefinedConstructors

// this has constructors for TextEdit that we do not have in scope
import lang::rascal::tests::functionality::VisitUndefinedConstructorsHelper1;

// this defined a "changed" constructor but of a differen arity then we get in the test
import analysis::diff::edits::FileSystemChanges;

test bool visitUndeclaredTextEditConstructorWhileAnOverloadIsInScope() {
    v = giveTestContent();

    w = visit (v) {
        case loc _ => |test:///| // we need to rewrite something to trigger rewriting the paren
    }

    // matching on the node name to avoid having to import the missing constructor
    return {"changed"(loc l, [])} := w && l == |test:///|;
}
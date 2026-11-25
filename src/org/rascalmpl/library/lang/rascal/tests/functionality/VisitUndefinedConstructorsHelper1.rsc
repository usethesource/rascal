module lang::rascal::tests::functionality::VisitUndefinedConstructorsHelper1

import analysis::diff::edits::FileSystemChanges;
import analysis::diff::edits::TextEdits;

@synopsis{Hide a well-defined constructor in a deeper value}
@description{
* `changed` is defined inside `analysis::diff::edits::TextEdits` which is an import, so callers do not have to have the definition in scope
* `value` is returned to emphasize the ADT is hidden from the caller
}
value giveTestContent() = {
    changed(|demo:///|, [])
};
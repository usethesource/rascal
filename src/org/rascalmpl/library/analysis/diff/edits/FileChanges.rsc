module analysis::diff::edits::FileChanges

@synopsis{Representing file (and directory) change events.}
@description{
A ((FileChange)) describes what happened to a file or directory:
* ((removed))
* ((created))
* ((renamed))
* or ((changed))

For detailed in-file changes, see ((TextEdits)).
}
@benefits{
* Implementations of file watchers can use ((FileChange)) to report what happened.
* Implementations of file and directory diff tools can represent the differences
using ((FileChange))
}
@pitfalls{
* To represent the internal differences of files, have a look at ((TextEdits)) instead.
* Note that the concept of "file", as used here in the fields names of ((FileChange)), 
includes "directories". This is because directories are indeed a kind of files 
from the OS perspective, and because it allows us to unify the two concepts here.
}
data FileChange
    = removed(loc file)
    | created(loc file)
    | renamed(loc from, loc to)
    | changed(loc file)
    ;
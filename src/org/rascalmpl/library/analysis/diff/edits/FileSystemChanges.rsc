module analysis::diff::edits::FileSystemChanges

@synopsis{Representing file (and directory) change events.}
@description{
A ((FileSystemChange)) describes what happened, or what will happen, to a file or directory:
* ((removed))
* ((Library:analysis::diff::edits::FileSystemChanges-created))
* ((renamed))
* or ((changed))

FileSystemChanges are an intermediate contract between the sources and the targets
of changes to files and directories on a file system. Typical sources are
((IO::watch)) or source-to-source refactoring tools like "rename". Typical targets
are preview UI and patch execution tools in the UI or on the commandline,
or the listeners of ((IO::watch)) that can trigger incremental re-compilation or re-loading
features.

The location scheme which is passed to the respective constructors of ((FileSystemChange))
must have the ((IO-watching)) and ((IO-writing)) ((IOCapability)). Otherwise 
((RuntimeException-IO)) errors can be expected.

For detailed changes _within_ files, see ((TextEdits)).
}
@benefits{
* Implementations of file watchers can use ((FileSystemChanges-FileSystemChange)) to report what happened.
* Implementations of file and directory diff tools can represent the differences
using ((FileSystemChange))
}
@pitfalls{
* To represent the internal differences of files, have a look at ((TextEdits)) instead.
* Note that the concept of "file", as used here in the fields names of ((FileSystemChange)), 
includes "directories". This is because directories are indeed a kind of files 
from the OS perspective, and because it allows us to unify the two concepts here.
}
data FileSystemChange
    = removed(loc file)
    | created(loc file)
    | renamed(loc from, loc to)
    | changed(loc file)
    ;
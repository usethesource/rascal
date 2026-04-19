@synopsis{A semantics for text and file patching based on ((TextEdits-FileSystemChange)) and ((TextEdits-TextEdit)) operators.} 
@description{
This module provides the same functionality as ((util::IDEServices)) for executing ((FileSystemChange))s.
Instead of deferring to the IDE to execute the patches and include them in the undo stack and preview modes,
this code simply directly patches strings and files.


}
@benefits{
* This module is very useful for batch execution of large scale renovation rewrites, 
* or for _testing_ refactoring code without all the UI dependencies.
}
module analysis::diff::edits::ExecuteTextEdits

extend analysis::diff::edits::TextEdits;

import DateTime;
import IO;
import List; 
import String;

@synopsis{Execute file changes, including in-file edits if present.}
@deprecated{Replaced by ((executeFileSystemChanges)) due to a rename of the concept.}
void executeDocumentEdits(list[FileSystemChange] edits) {
    executeFileSystemChanges(edits);
}

@synopsis{Execute file changes, including in-file edits if present.}
void executeFileSystemChanges(list[FileSystemChange] edits) {
    for (e <- edits) {
        executeFileSystemChange(e); 
    }
}

void executeFileSystemChange(removed(loc f)) {
    remove(f.top);
}

void executeFileSystemChange(created(loc f)) {
    writeFile(f, "");
}

void executeFileSystemChange(renamed(loc from, loc to)) {
    move(from.top, to.top, overwrite=true);
}

void executeFileSystemChange(changed(loc file)) {
    setLastModified(file, now());
}

@synopsis{Edit a file according to the given ((TextEdits-TextEdit)) instructions}
void executeFileSystemChange(changed(loc file, list[TextEdit] edits)) {
    str content = readFile(file);

    content = executeTextEdits(content, edits);

    writeFile(file.top, content);
}

@synopsis{Edit a string according to the given ((TextEdits-TextEdit)) instructions}
@description{
If you have a patch in the shape of a list of ((TextEdits-TextEdit))s, then this function
applies it for you.

Good sources of correct ((TextEdits-TextEdit)):
* ((analysis::diff::edits::HiFiTreeDiff))
* ((analysis::diff::edits::HiFiLayoutDiff))
}
@benefits{
* fast way to reconstruct an entire file based on a list of changes to that file.
* the `str` interface helps with unit testing of refactoring tools without file IO.
* use ((executeFileSystemChange)) for a file based interface to the same functionality.
}
@pitfalls{
* the list of edits _must be_ sorted on the offset, or this algorithm will not have the desired effect.
* the individual edits' range must not overlap, or this algorithm will not have the desired effect.
* the text edits must have been derived earlier from the current state of the string. There is a clear
semantic dependency between `content` and `edits` that the caller of the function must respect to get the desired effect.
Not doing so would be like throwing a patch of file A on a completely different file B.
}
str executeTextEdits(str content, list[TextEdit] edits) {
    int cursor = 0;

    // linear-time streamed reconstruction of the entire text
    return "<for (replace(loc range, str repl) <- edits) {><content[cursor..range.offset]><repl>< 
             cursor = range.offset + range.length;}><content[cursor..]>";
}

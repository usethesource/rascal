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

@synopsis{Edit a file according to the given ((TextEdit)) instructions}
void executeFileSystemChange(changed(loc file, list[TextEdit] edits)) {
    str content = readFile(file);

    content = executeTextEdits(content, edits);

    writeFile(file.top, content);
}

str executeTextEdits(str content, list[TextEdit] edits) {
    // assert isSorted(edits, less=bool (TextEdit e1, TextEdit e2) { 
    //     return e1.range.offset < e2.range.offset; 
    // });

    int cursor = 0;

    // linear-time streamed reconstruction of the entire text
    return "<for (replace(loc range, str repl) <- edits) {><content[cursor..range.offset]><repl>< 
             cursor = range.offset + range.length;}><content[cursor..]>";
}

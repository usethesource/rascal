module analysis::diff::edits::ExecuteTextEdits

extend analysis::diff::edits::TextEdits;

import DateTime;
import IO;
import List;
import String;

@synopsis{Execute file changes, including in-file edits if present.}
@deprecated{Replaced by ((executeFileChanges)) due to a rename of the concept.}
void executeDocumentEdits(list[FileChange] edits) {
    executeFileChanges(edits);
}

@synopsis{Execute file changes, including in-file edits if present.}
void executeFileChanges(list[FileChange] edits) {
    for (e <- edits) {
        executeFileChange(e);
    }
}

void executeFileChange(removed(loc f)) {
    remove(f.top);
}

void executeFileChange(created(loc f)) {
    writeFile(f, "");
}

void executeFileChange(renamed(loc from, loc to)) {
    move(from.top, to.top, overwrite=true);
}

void executeFileChange(changed(loc file)) {
    setLastModified(file, now());
}

@synopsis{Edit a file according to the given ((TextEdit)) instructions}
void executeFileChange(changed(loc file, list[TextEdit] edits)) {
    assert isSorted(edits, less=bool (TextEdit e1, TextEdit e2) { 
        return e1.range.offset < e2.range.offset; 
    });

    str content = readFile(file);

    for (replace(loc range, str repl) <- reverse(edits)) {
        assert range.top == file.top;
        content = "<content[..range.offset]><repl><content[range.offset+range.length..]>";
    }

    writeFile(file.top, content);
}

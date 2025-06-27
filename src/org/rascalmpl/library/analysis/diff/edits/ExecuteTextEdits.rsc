module analysis::diff::edits::ExecuteTextEdits

extend analysis::diff::edits::TextEdits;
import IO;
import String;
import List;

void executeDocumentEdits(list[DocumentEdit] edits) {
    for (e <- edits) {
        executeDocumentEdit(e);
    }
}

void executeDocumentEdit(removed(loc f)) {
    remove(f.top);
}

void executeDocumentEdit(created(loc f)) {
    writeFile(f, "");
}

void executeDocumentEdit(renamed(loc from, loc to)) {
    move(from.top, to.top, overwrite=true);
}

void executeDocumentEdit(changed(loc file, list[TextEdit] edits)) {
    str content = readFile(file);

    content = executeTextEdits(content, edits);

    writeFile(file.top, content);
}

str executeTextEdits(str content, list[TextEdit] edits) {
    assert isSorted(edits, less=bool (TextEdit e1, TextEdit e2) { 
        return e1.range.offset < e2.range.offset; 
    });

    for (replace(loc range, str repl) <- reverse(edits)) {
        content = "<content[..range.offset]><repl><content[range.offset+range.length..]>";
    }

    return content;
}

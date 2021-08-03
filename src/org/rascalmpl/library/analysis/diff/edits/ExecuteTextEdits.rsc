module analysis::diff::edits::ExecuteTextEdits

extend analysis::diff::edits::TextEdits;
import IO;
import String;

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
    content = readFile(file);
    shift = 0;

    for (replace(loc range, str repl) <- edits) {
        content[(shift + range.offset) .. (shift + range.offset + range.length)] = repl;
        shift += size(repl) - range.length;
    }

    writeFile(file, content);
}

module analysis::diff::edits::TextEdits

data DocumentEdit
    = removed(loc file)
    | created(loc file)
    | renamed(loc from, loc to)
    | changed(loc file, list[TextEdit] edits)
    ;

data TextEdit
    = replace(loc range, str replacement)
    ;

TextEdit delete(loc range) = replace(range, "");


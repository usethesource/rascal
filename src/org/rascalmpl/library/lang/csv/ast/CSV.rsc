module lang::csv::ast::CSV

import String;

data Table = table(list[Record] records);

data Record = record(list[Field] fields);

data Field
  = unquoted(str text)
  | quoted(str text);


anno loc Field@location;
anno loc Record@location;
anno loc Table@location;

public Table unquote(Table tbl) {
  str unescape(str s) = replaceAll(replaceAll(replaceAll(s, "\\n", "\n"), "\\t", "\t"), "\"\"", "\"");
  
  return visit (tbl) {
    case quoted(txt) => unquoted(unescape(substring(txt, 1, size(txt) - 1)))
  }
}

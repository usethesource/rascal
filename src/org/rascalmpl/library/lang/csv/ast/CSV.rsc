module lang::csv::ast::CSV

import String;

data Table(loc src=|unknown:///|) = table(list[Record] records);

data Record(loc src=|unknown:///|) = record(list[Field] fields);

data Field(loc src=|unknown:///|)
  = unquoted(str text)
  | quoted(str text);

public Table unquote(Table tbl) {
  str unescape(str s) = replaceAll(replaceAll(replaceAll(s, "\\n", "\n"), "\\t", "\t"), "\"\"", "\"");
  
  return visit (tbl) {
    case quoted(txt) => unquoted(unescape(substring(txt, 1, size(txt) - 1)))
  }
}

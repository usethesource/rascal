module lang::csv::ast::CSV

import String;

data Table = table(list[Record] records);

data Record = record(list[Field] fields);

data Field
  = unquoted(str text)
  | quoted(str text);


 
data Field(loc origin = |unknown:///|);
 
data Record(loc origin = |unknown:///|);
 
data Table(loc origin = |unknown:///|);

public Table unquote(Table tbl) {
  str unescape(str s) = replaceAll(replaceAll(replaceAll(s, "\\n", "\n"), "\\t", "\t"), "\"\"", "\"");
  
  return visit (tbl) {
    case quoted(txt) => unquoted(unescape(substring(txt, 1, size(txt) - 1)))
  }
}
